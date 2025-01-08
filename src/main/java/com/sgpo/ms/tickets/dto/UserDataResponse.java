package com.sgpo.ms.tickets.dto;

import java.util.UUID;

public record UserDataResponse(
        UUID id,
        String name,
        String email,
        String phone,
        boolean isAdmin
) {}
