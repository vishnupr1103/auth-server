package com.auth_server.controller;

import com.auth_server.entity.User;
import com.auth_server.jwt.JwtKeyProvider;
import com.auth_server.jwt.JwtTokenProvider;
import com.auth_server.model.LoginRequest;
import com.auth_server.model.LoginResponse;
import com.auth_server.repos.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class JwtKeyController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final JwtKeyProvider keys;



    @GetMapping("/public-key")
    public String getPublicKey() {
        return Base64.getEncoder()
                .encodeToString(keys.getPublicKey().getEncoded());
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtTokenProvider.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
