package com.example.MyLogistics.Service;

import com.example.MyLogistics.DTO.Request.*;
import com.example.MyLogistics.DTO.Response.*;
import com.example.MyLogistics.Model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService service;

    @Test
    public void testUserCanRegister(){
        RegisterRequest request = request();
        RegisterResponse response = service.register(request);
        assertEquals("register Successful",response.getMessage());
    }

    @Test
    public void testUserCanRegister1(){
        RegisterRequest request = request1();
        RegisterResponse response = service.register(request);
        assertEquals("register Successful",response.getMessage());
    }

    private RegisterRequest request(){
        RegisterRequest request = new RegisterRequest();
        request.setEmail("magic@gmail.com");
        request.setName("magic");
        request.setPhoneNumber("09134969216");
        request.setPassword("yuxcvsk");
        request.setRole(Role.ROLE_USER);
        return request;
    }
    private RegisterRequest request1(){
        RegisterRequest request = new RegisterRequest();
        request.setEmail("usman@gmail.com");
        request.setName("usman");
        request.setPhoneNumber("09134969244");
        request.setPassword("yuxcvsk");
        request.setRole(Role.ROLE_USER);
        return request;
    }
    @Test
    public void  testUserCanLogin(){
        LoginRequest request = loginRequest();
        LoginResponse response = service.login(request);
        assertEquals("login Successful",response.getMessage());
    }

    @Test
    public void  testUserCanLogin1(){
        LoginRequest request = loginRequest1();
        LoginResponse response = service.login(request);
        assertEquals("login Successful",response.getMessage());
    }

    private LoginRequest loginRequest(){
        LoginRequest request = new LoginRequest();
        request.setEmail("magic@gmail.com");
        request.setPassword("yuxcvsk");
        return request;
    }

    private LoginRequest loginRequest1(){
        LoginRequest request = new LoginRequest();
        request.setEmail("usman@gmail.com");
        request.setPassword("yuxcvsk");
        return request;
    }

    @Test
    public void testUserCanLogOut(){
        LogOutRequest requestEmail = new LogOutRequest();
        requestEmail.setEmail("magic@gmail.com");
        requestEmail.setToken("tgch");

        ResponseEmail response = service.logOut(requestEmail);
        assertEquals("Logged out.",response.getMessage());
    }


    @Test
    public void testSwitchActivity(){
        RequestEmail requestEmail = new RequestEmail();
        requestEmail.setEmail("magic@gmail.com");
        ResponseEmail response = service.switchActivity(requestEmail);
        assertEquals("switched",response.getMessage());

    }



    @Test
    public void testSwitchActivity1(){
        RequestEmail requestEmail = new RequestEmail();
        requestEmail.setEmail("usman@gmail.com");
        ResponseEmail response = service.switchActivity(requestEmail);
        assertEquals("switched",response.getMessage());
    }




    @Test
    public void testOrderRide(){
        OrderRiderRequest request = orderRiderRequest();
        OrderRiderResponse response = service.order(request);
        assertEquals("your ride as be ordered",response.getMessage());
    }

    private OrderRiderRequest orderRiderRequest(){
        OrderRiderRequest request = new OrderRiderRequest();
        request.setEmail("usman@gmail.com");
        request.setAddress("ayobo");
        request.setDestination("yaba");
        return  request;
    }

    @Test
    public void testMessageBox(){
        MessageRequest request = messageRequest();
        MessageResponse response = service.message(request);
        assertEquals("Done",response.getMessage());
        MessageRequest request1 = messageRequest1();
        MessageResponse response1 = service.message(request1);
        assertEquals("Done",response1.getMessage());

    }

    private MessageRequest messageRequest(){
        MessageRequest request = new MessageRequest();
        request.setPhoneNumber("09134969244");
        request.setMessage("wassup");
        return request;
    }

    private MessageRequest messageRequest1(){
        MessageRequest request = new MessageRequest();
        request.setPhoneNumber("09134969216");
        request.setMessage("u need a ride");
        return request;
    }

    @Test
    public void testArrived(){
        AtAddressRequest request = new AtAddressRequest();
        request.setEmail("magic@gmail.com");
        AtAddressResponse response = service.AtAddress(request);
        assertEquals("Already at the Address",response.getMessage());
    }

    @Test
    public void testStartRide(){
        RequestEmail requestEmail = new RequestEmail();
        requestEmail.setEmail("usman@gmail.com");
        ResponseEmail response = service.StartRide(requestEmail);
        assertEquals("Ride has begin",response.getMessage());
    }

    @Test
    public void testCompleteRide(){
        RequestEmail requestEmail = new RequestEmail();
        requestEmail.setEmail("usman@gmail.com");
        CompleteResponse response = service.completeRide(requestEmail);
        assertEquals("Ride Completed",response.getMessage());
    }

    @Test
    public void testCancelRide(){
        RequestEmail requestEmail = new RequestEmail();
        requestEmail.setEmail("usman@gmail.com");
        CancelResponse response = service.cancel(requestEmail);
        assertEquals("Ride Cancel",response.getMessage());
    }
}