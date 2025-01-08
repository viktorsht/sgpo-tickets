package com.sgpo.ms.tickets.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "travel_route_id", nullable = false)
    private Long travelRouteId;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "number_of_tickets", nullable = false)
    private Integer numberOfTickets;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Ticket() {
    }

    public Ticket(UUID userId, Long travelRouteId, String cardNumber, Integer numberOfTickets) {
        this.userId = userId;
        this.travelRouteId = travelRouteId;
        this.cardNumber = cardNumber;
        this.numberOfTickets = numberOfTickets;
        this.createdAt = LocalDateTime.now(); // Opcional: define automaticamente a data de criação
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Long getTravelRouteId() {
        return travelRouteId;
    }

    public void setTravelRouteId(Long travelRouteId) {
        this.travelRouteId = travelRouteId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", userId=" + userId +
                ", travelRouteId=" + travelRouteId +
                ", cardNumber='" + cardNumber + '\'' +
                ", numberOfTickets=" + numberOfTickets +
                ", createdAt=" + createdAt +
                '}';
    }
}
