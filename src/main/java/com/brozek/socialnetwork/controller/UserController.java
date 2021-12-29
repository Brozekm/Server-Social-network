package com.brozek.socialnetwork.controller;

import com.brozek.socialnetwork.service.IUserService;
import com.brozek.socialnetwork.vos.impl.LoginCredentialsVO;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/getAllUsers")
    public ResponseEntity<Object> getAllUsersFromDB(){
        var users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }


    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginCredentialsVO loginVO){
        var userVO = userService.findUserByEmail(loginVO.getEmail());
        if (userVO == null){
            return ResponseEntity.badRequest().build();
        }
        if (!Objects.equals(loginVO.password(), userVO.getPassword())){
            //TODO return password does not match
            return ResponseEntity.badRequest().build();
        }

        //TODO Create JWT and return to client

        return ResponseEntity.ok(userVO);
    }

}
