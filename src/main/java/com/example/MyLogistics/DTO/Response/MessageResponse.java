package com.example.MyLogistics.DTO.Response;

import lombok.Data;

import java.util.List;

@Data
public class MessageResponse {
    private String message;
    private List<String> chart;
}
