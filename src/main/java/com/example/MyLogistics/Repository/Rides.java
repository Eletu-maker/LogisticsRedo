package com.example.MyLogistics.Repository;

import com.example.MyLogistics.Model.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Rides extends MongoRepository<Ride, String> {
    Ride findRideById(String id);
}
