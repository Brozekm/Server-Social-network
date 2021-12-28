package com.brozek.socialnetwork.controller;

import com.brozek.socialnetwork.service.IUserService;
import com.brozek.socialnetwork.vos.IUserVO;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<Set<IUserVO>> getAllUsersFromDB(){
        Set<IUserVO> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

}
