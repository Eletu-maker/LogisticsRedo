package com.example.MyLogistics.DTO.Response;

import lombok.Data;

@Data
public class LoginResponse {
    private String message;
    private String accessToken;
    private String refreshToken;
}
