package com.ss.carreservation.dto;

public record UserDTO(
        String userId,
        String name,
        String email,
        String phone,
        String address
) {
}
