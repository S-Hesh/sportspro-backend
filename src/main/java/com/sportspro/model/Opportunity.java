package com.sportspro.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "opportunitie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opportunity_id")
    private Long opportunityId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "location")
    private String location;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "sport", nullable = false)
    private String sport;

    @Column(name = "salary")
    private String salary;

    @Column(name = "description")
    private String description;

    @Column(name = "requirements")
    private String requirements;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "posted_by")
    private User user;

    @Column(name = "posted_at")
    private LocalDateTime postedAt = LocalDateTime.now();
}
