package com.example.MyLogistics.Service;

import com.example.MyLogistics.Model.BlackListToken;
import com.example.MyLogistics.Repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    public void blacklist(String token, long expirationMillis) {
        LocalDateTime expiryTime = LocalDateTime.now().plusSeconds(expirationMillis / 1000);
        BlackListToken blacklisted = new BlackListToken(token, expiryTime);
        blacklistedTokenRepository.save(blacklisted);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }
}
