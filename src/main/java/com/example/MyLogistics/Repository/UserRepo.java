package com.example.MyLogistics.Repository;

import com.example.MyLogistics.Model.Activity;
import com.example.MyLogistics.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepo extends MongoRepository<Users,String> {
    Users findByEmail(String email);

    Users findAllByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
    Users findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    List<Users> findAllByActivity(Activity activity);
}
