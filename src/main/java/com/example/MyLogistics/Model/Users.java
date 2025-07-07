package com.example.MyLogistics.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("User")
public class Users {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private Role role;
    private boolean login;
    private boolean available;
    private Activity activity;
    private Ride ride;
    private String previousRiderId;






}
