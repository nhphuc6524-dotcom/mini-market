package com.minimarket.controller;

import com.minimarket.model.User;
import com.minimarket.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public AuthController(UserRepository userRepository,
                          BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){

        user.setPassword(
                encoder.encode(user.getPassword())
        );

        user.setRole("CASHIER");

        return userRepository.save(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User login){

        Optional<User> user =
                userRepository.findByUsername(login.getUsername());

        if(user.isPresent() &&
           encoder.matches(login.getPassword(),
                           user.get().getPassword())){

            return user.get();
        }

        throw new RuntimeException("Sai tài khoản hoặc mật khẩu");
    }

}