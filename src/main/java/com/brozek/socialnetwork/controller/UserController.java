package com.brozek.socialnetwork.controller;

import com.brozek.socialnetwork.config.auth.JwtTokenUtil;
import com.brozek.socialnetwork.service.IUserService;
import com.brozek.socialnetwork.service.impl.JwtUserDetailsService;
import com.brozek.socialnetwork.vos.IUserVO;
import com.brozek.socialnetwork.vos.impl.JwtRequestVO;
import com.brozek.socialnetwork.vos.impl.LoginCredentialsVO;
import com.brozek.socialnetwork.vos.impl.RegisterCredentialsVO;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;


    @GetMapping("/getAllUsers")
    public ResponseEntity<Object> getAllUsersFromDB(){
        var users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid RegisterCredentialsVO registerVO){
        if (!userService.createUser(registerVO.getEmail(),
                registerVO.getPassword(),
                registerVO.getFirstName(),
                registerVO.getSurname())){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(null);
    }


    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody JwtRequestVO jwtRequestVO){
        var userDetails = jwtUserDetailsService.loadUserByEmail(jwtRequestVO.getEmail());
        if (userDetails == null){
            return ResponseEntity.badRequest().build();
        }

        try {
            authenticate(userDetails.getUsername(), userDetails.getPassword());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
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
