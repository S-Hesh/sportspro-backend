package com.sportspro.repository;

import com.sportspro.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    // Existing
    List<Connection> findByUserUserIdAndStatus(Long userId, String status);
    List<Connection> findByConnectedUserUserIdAndStatus(Long userId, String status);

    // NEW: directed lookups (pair helpers)
    boolean existsByUserUserIdAndConnectedUserUserId(Long userId, Long connectedUserId);
    Connection findByUserUserIdAndConnectedUserUserId(Long userId, Long connectedUserId);
    Optional<Connection> findByUserUserIdAndConnectedUserUserIdAndStatus(Long userId, Long connectedUserId, String status);
}
