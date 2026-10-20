package com.shovanParia.Taskmanagement.Controller;

import com.shovanParia.Taskmanagement.Entity.User;
import com.shovanParia.Taskmanagement.Service.UserService;
import com.shovanParia.Taskmanagement.Utill.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private   UserService userService;
    @Autowired
    private   PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtill;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        Authentication authentication =
                authenticationProvider.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUserName(),
                                user.getPassword()
                        )
                );

        if (authentication.isAuthenticated()) {

            String jwt =
                    jwtUtill.generateToken(user.getUserName());

            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid username or password",
                HttpStatus.UNAUTHORIZED);
    }

    // GET CURRENT LOGGED-IN USER
    @GetMapping
    public ResponseEntity<?> getMyDetails() {
        //check authenticritaion and get name
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        User user = userService.findByUserName(userName);



        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);

    }


    // DELETE CURRENT LOGGED-IN USER
    @DeleteMapping
    public ResponseEntity<?> deleteMyAccount() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        User user = userService.findByUserName(userName);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        userService.deleteByName(userName);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // UPDATE CURRENT LOGGED-IN USER
    @PutMapping
    public ResponseEntity<?> updateMyAccount(
            @RequestBody User user) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        User oldUser = userService.findByUserName(userName);

        if (oldUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }


        // Update username
        if (user.getUserName() != null &&
                !user.getUserName().isBlank()) {

            oldUser.setUserName(user.getUserName());
        }


        // Update email
        if (user.getEmailId() != null &&
                !user.getEmailId().isBlank()) {

            oldUser.setEmailId(user.getEmailId());
        }


        // Update password
        if (user.getPassword() != null &&
                !user.getPassword().isBlank()) {

            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }


        userService.save(oldUser);

        return new ResponseEntity<>(oldUser, HttpStatus.OK);
    }
}