package com.sgpo.ms.tickets.dto;

public record TravelRoutes(
        Long id,
        String routeName,
        String departureLocation,
        String arrivalLocation,
        String departureTime,
        Double price,
        Integer seats
) {}