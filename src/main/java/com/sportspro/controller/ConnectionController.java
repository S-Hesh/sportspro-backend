package com.sportspro.controller;

import com.sportspro.dto.ConnectionDTO;
import com.sportspro.dto.ConnectionRequestDTO;
import com.sportspro.model.User;
import com.sportspro.service.ConnectionService;
import com.sportspro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    @Autowired private ConnectionService connectionService;
    @Autowired private UserService userService;

    // Send request (idempotent)
    @PostMapping("/{targetUserId}")
    public ResponseEntity<ConnectionDTO> sendRequest(@PathVariable Long targetUserId,
                                                     @AuthenticationPrincipal UserDetails ud) {
        User me = userService.findEntityByEmail(ud.getUsername());
        return ResponseEntity.ok(connectionService.sendRequest(me.getUserId(), targetUserId));
    }

    // Accept incoming from user
    @PostMapping("/user/{otherUserId}/accept")
    public ResponseEntity<ConnectionDTO> acceptFromUser(@PathVariable Long otherUserId,
                                                        @AuthenticationPrincipal UserDetails ud) {
        User me = userService.findEntityByEmail(ud.getUsername());
        return ResponseEntity.ok(connectionService.acceptFromUser(me.getUserId(), otherUserId));
    }

    // Reject incoming from user
    @PostMapping("/user/{otherUserId}/reject")
    public ResponseEntity<ConnectionDTO> rejectFromUser(@PathVariable Long otherUserId,
                                                        @AuthenticationPrincipal UserDetails ud) {
        User me = userService.findEntityByEmail(ud.getUsername());
        return ResponseEntity.ok(connectionService.rejectFromUser(me.getUserId(), otherUserId));
    }

    // Cancel outgoing pending OR remove accepted connection
    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<Void> cancelOrRemove(@PathVariable Long targetUserId,
                                               @AuthenticationPrincipal UserDetails ud) {
        User me = userService.findEntityByEmail(ud.getUsername());
        connectionService.cancelOrRemove(me.getUserId(), targetUserId);
        return ResponseEntity.noContent().build();
    }

    // List pending (incoming by default; or outgoing)
    @GetMapping("/pending")
    public ResponseEntity<List<ConnectionRequestDTO>> listPending(@RequestParam(required = false) String direction,
                                                                  @AuthenticationPrincipal UserDetails ud) {
        User me = userService.findEntityByEmail(ud.getUsername());
        String dir = (direction == null ? "incoming" : direction);
        return ResponseEntity.ok(connectionService.listPending(me.getUserId(), dir));
    }
}
