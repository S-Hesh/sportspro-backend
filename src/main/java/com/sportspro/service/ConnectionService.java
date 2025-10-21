package com.sportspro.service;

import com.sportspro.dto.ConnectionDTO;
import com.sportspro.dto.ConnectionRequestDTO;
import com.sportspro.mapper.ConnectionMapper;
import com.sportspro.model.Connection;
import com.sportspro.model.Skill;
import com.sportspro.model.User;
import com.sportspro.repository.ConnectionRepository;
import com.sportspro.repository.UserRepository;
import com.sportspro.util.ConnectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConnectionService {

    @Autowired private ConnectionRepository connectionRepository;
    @Autowired private ConnectionMapper connectionMapper;
    @Autowired private UserRepository userRepository;

    // ---------- New core actions ----------

    @Transactional
    public ConnectionDTO sendRequest(Long requesterId, Long receiverId) {
        if (Objects.equals(requesterId, receiverId)) {
            throw new IllegalArgumentException("Cannot connect to yourself.");
        }
        // Already connected?
        Connection accepted = getAcceptedAnyDirection(requesterId, receiverId);
        if (accepted != null) return connectionMapper.connectionToConnectionDTO(accepted);

        // Pending same direction -> idempotent
        Optional<Connection> sameDirPending =
                connectionRepository.findByUserUserIdAndConnectedUserUserIdAndStatus(requesterId, receiverId, ConnectionStatus.PENDING);
        if (sameDirPending.isPresent()) return connectionMapper.connectionToConnectionDTO(sameDirPending.get());

        // Pending opposite direction -> return that (client may accept)
        Optional<Connection> oppositePending =
                connectionRepository.findByUserUserIdAndConnectedUserUserIdAndStatus(receiverId, requesterId, ConnectionStatus.PENDING);
        if (oppositePending.isPresent()) return connectionMapper.connectionToConnectionDTO(oppositePending.get());

        // Create new pending
        User requester = userRepository.findById(requesterId).orElseThrow(() -> new NoSuchElementException("Requester not found"));
        User receiver  = userRepository.findById(receiverId).orElseThrow(() -> new NoSuchElementException("Receiver not found"));

        Connection c = new Connection();
        c.setUser(requester);
        c.setConnectedUser(receiver);
        c.setStatus(ConnectionStatus.PENDING);
        c.setCreatedAt(LocalDateTime.now());
        Connection saved = connectionRepository.save(c);
        return connectionMapper.connectionToConnectionDTO(saved);
    }

    @Transactional
    public ConnectionDTO acceptFromUser(Long currentUserId, Long otherUserId) {
        Connection incoming = connectionRepository.findByUserUserIdAndConnectedUserUserId(otherUserId, currentUserId);
        if (incoming == null || !ConnectionStatus.PENDING.equals(incoming.getStatus())) {
            throw new NoSuchElementException("No pending request from this user.");
        }
        incoming.setStatus(ConnectionStatus.ACCEPTED);
        return connectionMapper.connectionToConnectionDTO(connectionRepository.save(incoming));
    }

    @Transactional
    public ConnectionDTO rejectFromUser(Long currentUserId, Long otherUserId) {
        Connection incoming = connectionRepository.findByUserUserIdAndConnectedUserUserId(otherUserId, currentUserId);
        if (incoming == null || !ConnectionStatus.PENDING.equals(incoming.getStatus())) {
            throw new NoSuchElementException("No pending request from this user.");
        }
        incoming.setStatus(ConnectionStatus.REJECTED);
        return connectionMapper.connectionToConnectionDTO(connectionRepository.save(incoming));
    }

    @Transactional
    public void cancelOrRemove(Long currentUserId, Long otherUserId) {
        // Cancel outgoing pending
        Optional<Connection> myPending = connectionRepository
                .findByUserUserIdAndConnectedUserUserIdAndStatus(currentUserId, otherUserId, ConnectionStatus.PENDING);
        if (myPending.isPresent()) {
            connectionRepository.delete(myPending.get());
            return;
        }
        // Remove accepted either direction
        Connection acc = getAcceptedAnyDirection(currentUserId, otherUserId);
        if (acc != null) connectionRepository.delete(acc);
    }

    // ---------- Pending requests listing ----------

    @Transactional(readOnly = true)
    public List<ConnectionRequestDTO> listPending(Long currentUserId, String direction) {
        boolean incoming = !"outgoing".equalsIgnoreCase(direction);
        List<Connection> rows = incoming
                ? connectionRepository.findByConnectedUserUserIdAndStatus(currentUserId, ConnectionStatus.PENDING)
                : connectionRepository.findByUserUserIdAndStatus(currentUserId, ConnectionStatus.PENDING);

        Set<Long> myAccepted = getAcceptedPeerIds(currentUserId);

        List<ConnectionRequestDTO> out = new ArrayList<ConnectionRequestDTO>();
        for (Connection c : rows) {
            User other = incoming ? c.getUser() : c.getConnectedUser();

            List<String> skills = other.getSkills() == null ? Collections.<String>emptyList()
                    : other.getSkills().stream().map(Skill::getSkillName).collect(Collectors.toList());

            int mutuals = countMutuals(myAccepted, getAcceptedPeerIds(other.getUserId()));
            String role = other.getSport();
            String requestDate = humanize(c.getCreatedAt());

            out.add(new ConnectionRequestDTO(
                    c.getConnectionId(),
                    other.getUserId(),
                    other.getName(),
                    role,
                    other.getLocation(),
                    other.getAvatar(),
                    skills,
                    mutuals,
                    requestDate
            ));
        }
        return out;
    }

    // ---------- Flags for networking cards ----------

    @Transactional(readOnly = true)
    public RelationshipFlags flags(Long currentUserId, Long otherUserId) {
        boolean outgoing = connectionRepository
                .findByUserUserIdAndConnectedUserUserIdAndStatus(currentUserId, otherUserId, ConnectionStatus.PENDING)
                .isPresent();
        boolean incoming = connectionRepository
                .findByUserUserIdAndConnectedUserUserIdAndStatus(otherUserId, currentUserId, ConnectionStatus.PENDING)
                .isPresent();
        boolean connected = isConnected(currentUserId, otherUserId);
        return new RelationshipFlags(connected, incoming, outgoing);
    }

    // ---------- Existing methods preserved for compatibility ----------

    /** Wrapper for old signature; prefers new logic. */
    @Deprecated
    public ConnectionDTO sendConnectionRequest(ConnectionDTO connectionDTO) {
        if (connectionDTO == null) throw new IllegalArgumentException("ConnectionDTO is null");
        return sendRequest(connectionDTO.getUserId(), connectionDTO.getConnectedUserId());
    }

    /** Old helper kept as-is (pending requests you sent). */
    public List<ConnectionDTO> getPendingConnectionsByUserId(Long userId) {
        List<Connection> connections = connectionRepository.findByUserUserIdAndStatus(userId, ConnectionStatus.PENDING);
        return connections.stream().map(connectionMapper::connectionToConnectionDTO).collect(Collectors.toList());
    }

    // ---------- Private helpers ----------

    private boolean isConnected(Long a, Long b) {
        Connection c1 = connectionRepository.findByUserUserIdAndConnectedUserUserId(a, b);
        if (c1 != null && ConnectionStatus.ACCEPTED.equals(c1.getStatus())) return true;
        Connection c2 = connectionRepository.findByUserUserIdAndConnectedUserUserId(b, a);
        return c2 != null && ConnectionStatus.ACCEPTED.equals(c2.getStatus());
    }

    private Connection getAcceptedAnyDirection(Long a, Long b) {
        Connection c1 = connectionRepository.findByUserUserIdAndConnectedUserUserId(a, b);
        if (c1 != null && ConnectionStatus.ACCEPTED.equals(c1.getStatus())) return c1;
        Connection c2 = connectionRepository.findByUserUserIdAndConnectedUserUserId(b, a);
        if (c2 != null && ConnectionStatus.ACCEPTED.equals(c2.getStatus())) return c2;
        return null;
    }

    private Set<Long> getAcceptedPeerIds(Long userId) {
        List<Connection> mine = connectionRepository.findByUserUserIdAndStatus(userId, ConnectionStatus.ACCEPTED);
        List<Connection> toMe = connectionRepository.findByConnectedUserUserIdAndStatus(userId, ConnectionStatus.ACCEPTED);
        Set<Long> ids = new HashSet<Long>();
        for (Connection c : mine) ids.add(c.getConnectedUser().getUserId());
        for (Connection c : toMe) ids.add(c.getUser().getUserId());
        return ids;
    }

    private static int countMutuals(Set<Long> a, Set<Long> b) {
        int count = 0;
        for (Long id : a) if (b.contains(id)) count++;
        return count;
    }

    private static String humanize(LocalDateTime t) {
        if (t == null) return "";
        long mins = Duration.between(t, LocalDateTime.now()).toMinutes();
        if (mins < 60) return mins + " min ago";
        long hrs = mins / 60;
        if (hrs < 24) return hrs + " hr ago";
        long days = hrs / 24;
        return days + " day" + (days == 1 ? "" : "s") + " ago";
    }

    // POJO for flags
    public static class RelationshipFlags {
        public boolean isConnected;
        public boolean hasIncomingRequest;
        public boolean hasOutgoingRequest;
        public RelationshipFlags(boolean c, boolean in, boolean out) {
            this.isConnected = c; this.hasIncomingRequest = in; this.hasOutgoingRequest = out;
        }
    }
}
