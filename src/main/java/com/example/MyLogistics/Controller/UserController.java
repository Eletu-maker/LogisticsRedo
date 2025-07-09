package com.example.MyLogistics.Controller;

import com.example.MyLogistics.DTO.Request.*;
import com.example.MyLogistics.DTO.Response.*;
import com.example.MyLogistics.Exceptions.LoginException;
import com.example.MyLogistics.Exceptions.OrderRiderException;
import com.example.MyLogistics.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        try{
            RegisterResponse response = service.register(request);
            return  new ResponseEntity<>(new ApiResponse(true,response),HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest request){
        try{
            LoginResponse response = service.login(request);
            return new ResponseEntity<>(new ApiResponse(true,response),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/switch-activity")
    public ResponseEntity<?> switchActivity (@RequestBody RequestEmail request){
        try{
            ResponseEmail response = service.switchActivity(request);
            return new ResponseEntity<>(new ApiResponse(true,response),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/OrderRide")
    public ResponseEntity<?> orderRide (@RequestBody OrderRiderRequest request){
        try {
            OrderRiderResponse response = service.order(request);
            return new ResponseEntity<>(new ApiResponse(true,response),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/CancelRide")
    public ResponseEntity<?> CancelRide(@RequestBody RequestEmail request){
        try {
            CancelResponse response = service.cancel(request);
            return new ResponseEntity<>(new ApiResponse(true,response),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/Message")
    public ResponseEntity<?> Message(@RequestBody MessageRequest request){
        try {
            MessageResponse response =service.message(request);
            return new ResponseEntity<>(new ApiResponse(true,response),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/AtAddress")
    public ResponseEntity<?> AtAddress(@RequestBody AtAddressRequest request){
        try {
            AtAddressResponse response = service.AtAddress(request);
            return new ResponseEntity<>(new ApiResponse(true,response),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/StartRide")
    public ResponseEntity<?> StartRide (@RequestBody RequestEmail request){
        try {
            ResponseEmail response = service.StartRide(request);
            return new ResponseEntity<>(new ApiResponse(true,response),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/Complete")
    public ResponseEntity<?> completeRide (@RequestBody RequestEmail request){
        try {
            CompleteResponse response = service.completeRide(request);
            return new ResponseEntity<>(new ApiResponse(true,response),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logOut")
    public ResponseEntity<?> logOut (@RequestBody LogOutRequest request){
        try {
            ResponseEmail response = service.logOut(request);
            return new ResponseEntity<>(new ApiResponse(true,response),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}