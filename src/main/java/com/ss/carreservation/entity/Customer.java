package com.ss.carreservation.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("customer")
public class Customer extends User {

    @ElementCollection
    @CollectionTable(
            name = "customer_reservations",
            joinColumns = @JoinColumn(name = "customer_id")
    )
    @Column(name = "reservation_details")
    private List<String> reservations;

    @ElementCollection
    @CollectionTable(
            name = "customer_feedback",
            joinColumns = @JoinColumn(name = "customer_id")
    )
    @Column(name = "feedback_text")
    private List<String> feedbackList;
}
