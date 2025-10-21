package com.sportspro.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "connection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_id")
    private Long connectionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "connected_user_id")
    private User connectedUser;

    @Column(name = "status", nullable = false)
    private String status;  // Pending, Accepted, Rejected

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
