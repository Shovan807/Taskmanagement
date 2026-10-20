package com.shovanParia.Taskmanagement.Controller;

import com.shovanParia.Taskmanagement.Entity.User;
import com.shovanParia.Taskmanagement.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;


    // =========================
    // HEALTH CHECK
    // =========================
    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }


    // =========================
    // SIGN UP
    // =========================
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody User user) {

        try {

            if (user == null) {
                return new ResponseEntity<>(
                        "Invalid user data",
                        HttpStatus.BAD_REQUEST
                );
            }

            // Password should be encoded inside saveNewUser()
            userService.saveNewUser(user);

            return new ResponseEntity<>(
                    user,
                    HttpStatus.CREATED
            );

        } catch (Exception e) {

            e.printStackTrace();

            return new ResponseEntity<>(
                    "Something went wrong",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}