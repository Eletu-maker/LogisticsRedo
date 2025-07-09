package com.example.MyLogistics.Repository;

import com.example.MyLogistics.Model.BlackListToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlacklistedTokenRepository extends MongoRepository<BlackListToken,String> {
    boolean existsByToken(String token);
}
