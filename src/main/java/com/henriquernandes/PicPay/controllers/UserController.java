package com.henriquernandes.PicPay.controllers;

import com.henriquernandes.PicPay.dtos.UserDTO;
import com.henriquernandes.PicPay.entities.User;
import com.henriquernandes.PicPay.repositories.UserRepository;
import com.henriquernandes.PicPay.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    private UserRepository userRepository;


    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() throws Exception {
        User user = userService.authenticatedUser();
        return ResponseEntity.ok(user);
    }
    
}
