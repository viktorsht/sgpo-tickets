package com.sgpo.ms.tickets.mapper;

import com.sgpo.ms.tickets.dto.UserDataResponse;

import java.util.stream.Collectors;

public class UserMapper {

    static boolean isAdmin(UserDataResponse user){
        return user.isAdmin();
    }
}
