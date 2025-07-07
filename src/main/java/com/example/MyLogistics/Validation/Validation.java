package com.example.MyLogistics.Validation;

import com.example.MyLogistics.DTO.Request.RegisterRequest;
import com.example.MyLogistics.Exceptions.EmailException;
import com.example.MyLogistics.Exceptions.NameException;
import com.example.MyLogistics.Exceptions.PasswordException;
import com.example.MyLogistics.Exceptions.PhoneNumberException;

import java.util.regex.Pattern;

public class Validation {
    public static void validateName(RegisterRequest registerRequest){
        if (registerRequest.getName().isEmpty()) throw  new NameException("please input your name");
    }

    public static void validatePhoneNumber(RegisterRequest registerRequest) {
        String phoneNumber = registerRequest.getPhoneNumber();
        phoneNumber = phoneNumber.replaceAll("\\s+", "").trim();
        String regex = "^(?:0|\\+234|234)(701|702|703|704|705|706|707|708|709|" +
                "802|803|804|805|806|807|808|809|" +
                "810|811|812|813|814|815|816|817|818|819|" +
                "901|902|903|904|905|906|907|908|909|" +
                "911|913|915|916|917|918|919)[0-9]{7}$";
        boolean check = phoneNumber.matches(regex);
        if(!check) throw new PhoneNumberException("please enter a Nigeria phone number");
    }

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    public static void validateEmail(RegisterRequest registerRequest) {
        boolean check = Pattern.matches(EMAIL_REGEX, registerRequest.getEmail());
        if(!check) throw new EmailException("please enter a correct email and allow adding spaces");
    }

    public static void validatePassword(RegisterRequest registerRequest){
        if(registerRequest.getPassword().length() <7)throw  new PasswordException("password must be 7 figures and above");
        if(registerRequest.getPassword().contains(" ")) throw  new PasswordException("password must not contain space");
    }


}
