package com.example.MyLogistics.Service;

import com.example.MyLogistics.DTO.Request.*;
import com.example.MyLogistics.DTO.Response.*;
import com.example.MyLogistics.Exceptions.LoginException;
import com.example.MyLogistics.Exceptions.OrderRiderException;
import com.example.MyLogistics.Exceptions.RegistrationException;
import com.example.MyLogistics.Model.Activity;
import com.example.MyLogistics.Model.Ride;
import com.example.MyLogistics.Model.Role;
import com.example.MyLogistics.Model.Users;
import com.example.MyLogistics.Repository.Rides;
import com.example.MyLogistics.Repository.UserRepo;
import com.example.MyLogistics.Validation.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.channels.AcceptPendingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo repo;

    @Autowired
    private Rides rides;

    @Autowired
    private JWTServices jwtServices;

    @Autowired
    AuthenticationManager manager;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public RegisterResponse register(RegisterRequest request){
        RegisterResponse registerResponse = new RegisterResponse();
        if(repo.existsByEmail(request.getEmail()) || repo.existsByPhoneNumber(request.getPhoneNumber())) throw new RegistrationException("Account already exist");
        else save(request);
        registerResponse.setMessage("register Successful");
        return registerResponse;
    }

    private void save(RegisterRequest request){
        Users users = new Users();
        Validation.validateName(request);
        users.setName(request.getName());
        Validation.validateEmail(request);
        users.setEmail(request.getEmail());
        Validation.validatePhoneNumber(request);
        users.setPhoneNumber(request.getPhoneNumber());
        Validation.validatePassword(request);
        users.setPassword(encoder.encode(request.getPassword()));
        users.setRole(request.getRole());
        users.setActivity(Activity.CUSTOMER);
        users.setPreviousRiderId("default");
        repo.save(users);
    }

    public LoginResponse login(LoginRequest request){
        LoginResponse response = new LoginResponse();
        Users users = repo.findByEmail(request.getEmail());
        if (users == null) throw new LoginException("Account does not exist");
        logger.info("Login attempt for user: {}", request.getEmail());
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            String accessToken = jwtServices.generateToken(request.getEmail());
            String refreshToken = jwtServices.generateRefreshToken(request.getEmail());
            users.setLogin(true);
            users.setAvailable(true);
            logger.info("Login successful for user: {}", request.getEmail());
            response.setMessage("login Successful");
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            repo.save(users);
            return response;
        }
        logger.warn("Login failed for user: {}", request.getEmail());
        throw  new LoginException("Wrong password");
    }

    public ResponseEmail logOut(RequestEmail request){
        ResponseEmail response = new ResponseEmail();
        Users users = repo.findByEmail(request.getEmail());
        if (users == null) throw new LoginException("User not found");
        if (!users.isLogin()) throw new LoginException("User is already logged out");
        if(users.getRide() != null)throw new LoginException("Can't log out until package is delivered");
        users.setLogin(false);
        users.setAvailable(false);
        repo.save(users);
        response.setMessage("Logged out.");
        return response;
    }

    public ResponseEmail switchActivity (RequestEmail request){
        ResponseEmail response = new ResponseEmail();
        Users users = repo.findByEmail(request.getEmail());
        if (users == null) throw new LoginException("User not found");
        if (users.getRide() != null)throw new OrderRiderException("can't switch until ride is over");
        if(users.getActivity() == Activity.CUSTOMER){
            users.setActivity(Activity.RIDER);
        }else {
            users.setActivity(Activity.CUSTOMER);
        }
        repo.save(users);
        response.setMessage("switched");
        return response;
    }

    public OrderRiderResponse order(OrderRiderRequest request){
        OrderRiderResponse riderResponse = new OrderRiderResponse();
        Users users = repo.findByEmail(request.getEmail());
        if (users == null) throw new LoginException("User not found");
        if (users.getActivity().equals(Activity.RIDER))throw new OrderRiderException("switch to Customer");
        if (users.getRide() != null)throw new OrderRiderException("can't order another ride ");
        Ride trip = new Ride();
        trip.setCustomerName(users.getName());
        trip.setCustomerPhoneNumber(users.getPhoneNumber());
        trip.setCustomerAddress(request.getAddress());
        trip.setDestinationAddress(request.getDestination());
        trip.setRideStarted(false);
        Users rider = getRider(request);
        if(rider == null)throw new OrderRiderException("No Rider Available try again later");
        trip.setRiderName(rider.getName());
        trip.setRiderPhoneNumber(rider.getPhoneNumber());
        users.setPreviousRiderId(rider.getId());
        rider.setAvailable(false);
        rider.setRide(trip);
        users.setRide(trip);
        rides.save(trip);
        repo.save(users);
        repo.save(rider);
        riderResponse.setMessage("your ride as be ordered");
        return riderResponse;
    }

    private Users getRider(OrderRiderRequest request){
        Users users = repo.findByEmail(request.getEmail());
        List<Users> allRider = repo.findAllByActivity(Activity.RIDER);
        if(allRider == null)throw new OrderRiderException("No Rider Available try again later");
        for (Users rider: allRider){
            if(!users.getPreviousRiderId().equals(rider.getId()) && rider.isAvailable()){
                return rider;
            }
        }
        return null;
    }

    public MessageResponse message(MessageRequest request){
        MessageResponse response = new MessageResponse();
        Users users = repo.findByPhoneNumber(request.getPhoneNumber());
        Ride ride = rides.findRideById(users.getRide().getId());
        if( ride.getMessageBox() == null){
            List<String> box = new ArrayList<>();
            ride.setMessageBox(box);
        }
        String chat;
        if(users.getActivity().equals(Activity.CUSTOMER)){
             chat = "Customer: "+request.getMessage();
        }else {
             chat = "Rider: "+request.getMessage();
        }

        ride.getMessageBox().add(chat);
        users.setRide(ride);
        Users rider = repo.findByPhoneNumber(users.getRide().getRiderPhoneNumber());
        rider.setRide(ride);
        rides.save(ride);
        repo.save(users);
        repo.save(rider);
        response.setMessage("Done");
        response.setChart(users.getRide().getMessageBox());
        return response;

    }


    public AtAddressResponse AtAddress (AtAddressRequest request){
        AtAddressResponse response = new AtAddressResponse();
        Users users = repo.findByEmail(request.getEmail());
        if(users.getActivity().equals(Activity.CUSTOMER))throw  new OrderRiderException("This function are for riders only");
        if(users.getRide() == null)throw  new OrderRiderException("No ride Order");
        Ride ride = rides.findRideById(users.getRide().getId());
        ride.setAtAddress(true);
        Users customer = repo.findByPhoneNumber(ride.getCustomerPhoneNumber());
        users.setRide(ride);
        customer.setRide(ride);
        rides.save(ride);
        repo.save(users);
        repo.save(customer);
        response.setMessage("Already at the Address");
        return response;

    }

    public ResponseEmail StartRide(RequestEmail request){
        ResponseEmail response = new ResponseEmail();
        Users users = repo.findByEmail(request.getEmail());
        if(users.getActivity().equals(Activity.CUSTOMER))throw  new OrderRiderException("This function are for riders only");
        if(users.getRide() == null)throw  new OrderRiderException("No ride Order");
        Ride ride = rides.findRideById(users.getRide().getId());
        ride.setRideStarted(true);
        Users customer = repo.findByPhoneNumber(ride.getCustomerPhoneNumber());
        users.setRide(ride);
        customer.setRide(ride);
        rides.save(ride);
        repo.save(users);
        repo.save(customer);
        response.setMessage("Ride has begin");
        return response;
    }


    public CompleteResponse completeRide(RequestEmail request){
        CompleteResponse response = new CompleteResponse();
        Users users = repo.findByEmail(request.getEmail());
        if(users.getActivity().equals(Activity.CUSTOMER))throw  new OrderRiderException("This function are for riders only");
        if(users.getRide() == null)throw  new OrderRiderException("No ride Order");
        Ride ride = rides.findRideById(users.getRide().getId());
        Users customer = repo.findByPhoneNumber(ride.getCustomerPhoneNumber());
        users.setRide(null);
        users.setAvailable(true);
        customer.setRide(null);
        repo.save(users);
        repo.save(customer);
        response.setMessage("Ride Completed");
        return response;
    }


    public CancelResponse cancel(RequestEmail request){
        CancelResponse response = new CancelResponse();
        Users users = repo.findByEmail(request.getEmail());
        if(users.getActivity().equals(Activity.RIDER))throw  new OrderRiderException("This function are for customer only");
        if(users.getRide().isRideStarted())throw  new OrderRiderException("Can't cancel ride until order is complete");
        Users rider = repo.findByPhoneNumber(users.getRide().getRiderPhoneNumber());
        rider.setRide(null);
        rider.setAvailable(true);
        users.setRide(null);
        repo.save(users);
        repo.save(rider);
        response.setMessage("Ride Cancel");
        return response;
    }



}
