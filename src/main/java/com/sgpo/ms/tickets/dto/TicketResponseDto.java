package com.sgpo.ms.tickets.dto;

import com.sgpo.ms.tickets.dto.TravelRoutes;

import java.time.LocalDateTime;

public record TicketResponseDto(
        Long id,
        UserDataResponse user,
        TravelRoutes travelRoute,
        String cardNumber,
        Integer numberOfTickets,
        LocalDateTime createdAt
) {}
