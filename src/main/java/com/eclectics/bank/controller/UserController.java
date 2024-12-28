package com.eclectics.bank.controller;

import com.eclectics.bank.domain.ConfirmationToken;
import com.eclectics.bank.dto.LoginDto;
import com.eclectics.bank.dto.LoginRequest;
import com.eclectics.bank.dto.LoginResponse;
import com.eclectics.bank.dto.UserDto;
import com.eclectics.bank.repository.ConfirmationTokenRepository;
import com.eclectics.bank.security.JWTTokenUtil;
import com.eclectics.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bank/users")
public class UserController {


    private final UserService userService;

    private final ConfirmationTokenRepository confirmationTokenRepo;

    private final AuthenticationManager authenticationManager;

    private final JWTTokenUtil jwtTokenUtil;

    public UserController(UserService userService, ConfirmationTokenRepository confirmationTokenRepo, AuthenticationManager authenticationManager, JWTTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.confirmationTokenRepo = confirmationTokenRepo;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserDto userDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.register(userDto);
            response.put("message", "User registered successfully. Please check your email for the confirmation token.");
            response.put("data", userDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", "Failed to register user: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<Map<String, Object>> confirmUser(@RequestParam String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.confirmUser(token);
            response.put("message", "User confirmed successfully.");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", "Failed to confirm user: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/set-password")
    public ResponseEntity<Map<String, Object>> setPassword(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.setPassword(loginRequest.getPassword(), loginRequest.getToken());
            response.put("message", "Password set successfully.");
            response.put("data", Map.of("token", loginRequest.getToken(), "password", loginRequest.getPassword()));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", "Failed to set password: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto login) {
        ConfirmationToken confirmationToken = confirmationTokenRepo.findByUsername(login.getUsername()).orElseThrow(() -> new RuntimeException("Invalid username"));

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenUtil.generateToken(confirmationToken);

        LoginResponse loginResponse = new LoginResponse(confirmationToken, token);



        return ResponseEntity.ok().body(loginResponse);
    }

    @GetMapping("/find")
    public ResponseEntity<Map<String, Object>> findByUsername(@RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        try {
            ConfirmationToken token = userService.findByUsername(username);
            response.put("message", "User found successfully.");
            response.put("data", token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", "User not found: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
