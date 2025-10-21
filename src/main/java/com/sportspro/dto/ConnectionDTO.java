package com.sportspro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDTO {

    private Long connectionId;
    private Long userId;  // Sender's user ID
    private Long connectedUserId;  // Receiver's user ID
    private String status;  // Pending, Accepted, Rejected
}
