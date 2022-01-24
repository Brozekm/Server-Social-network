package com.brozek.socialnetwork.controller;

import com.brozek.socialnetwork.config.auth.JwtTokenUtil;
import com.brozek.socialnetwork.service.IFriendshipsService;
import com.brozek.socialnetwork.service.impl.FriendshipsService;
import com.brozek.socialnetwork.validation.exception.StringResponse;
import com.brozek.socialnetwork.vos.*;
import com.brozek.socialnetwork.service.IUserService;
import com.brozek.socialnetwork.service.impl.JwtUserDetailsService;
import com.brozek.socialnetwork.validation.exception.TakenEmailException;
import lombok.RequiredArgsConstructor;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterCredentialsVO registerVO) {
        try {
            userService.createUser(registerVO);
        } catch (TakenEmailException e) {
            return ResponseEntity.badRequest().body(new StringResponse("Email is taken"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(null);
    }

    @GetMapping("/register/email")
    public ResponseEntity<?> isEmailTaken(@RequestParam @Valid String email) {
        try {
            return ResponseEntity.ok(userService.isEmailTaken(email));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Email is not valid");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponseVO> loginUser(@RequestBody JwtRequestVO jwtRequestVO) {
        var jwtResponseCredentials = jwtUserDetailsService.loadUserByEmail(jwtRequestVO.getEmail());
        if (jwtResponseCredentials == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            authenticate(jwtRequestVO.getEmail(), jwtRequestVO.getPassword());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        final String token = jwtTokenUtil.generateToken(jwtResponseCredentials.getUserDetails());
        jwtResponseCredentials.getJwtResponseVO().setToken(token);
        return ResponseEntity.ok(jwtResponseCredentials.getJwtResponseVO());
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


}
