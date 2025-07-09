package com.example.MyLogistics.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BlackListToken {
    @Id
    private String token;
    private LocalDateTime expiryTime;
}
