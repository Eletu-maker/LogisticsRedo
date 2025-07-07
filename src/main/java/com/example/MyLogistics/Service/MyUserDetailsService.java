package com.example.MyLogistics.Service;

import com.example.MyLogistics.Model.UserPrincipal;
import com.example.MyLogistics.Model.Users;
import com.example.MyLogistics.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users= repo.findByEmail(email);
        if(users == null) throw new UsernameNotFoundException("user not found");
        return new UserPrincipal(users);
    }
}
