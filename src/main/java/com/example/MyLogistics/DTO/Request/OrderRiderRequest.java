package com.example.MyLogistics.DTO.Request;

import lombok.Data;

@Data
public class OrderRiderRequest {
    private String email;
    private String address;
    private String destination;
}
