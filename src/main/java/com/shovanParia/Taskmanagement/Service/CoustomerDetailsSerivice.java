package com.shovanParia.Taskmanagement.Service;

import com.shovanParia.Taskmanagement.Entity.User;
import com.shovanParia.Taskmanagement.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CoustomerDetailsSerivice implements UserDetailsService {
    @Autowired
    private     UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("USERNAME RECEIVED = [" + username + "]");

        User user = userRepository.findByUserName(username);

        if (user == null) {
            System.out.println("USER NOT FOUND");
            throw new UsernameNotFoundException(
                    "User not found: " + username
            );
        }

        System.out.println("USER FOUND = [" + user.getUserName() + "]");
        System.out.println("PASSWORD FROM DB = [" + user.getPassword() + "]");




     //   System.out.println("PASSWORD MATCH = " + result);


        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .build();
    }
}
