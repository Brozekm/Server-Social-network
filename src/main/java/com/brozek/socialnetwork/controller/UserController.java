package com.brozek.socialnetwork.controller;

import com.brozek.socialnetwork.service.IUserService;
import com.brozek.socialnetwork.vos.IUserVo;
import com.brozek.socialnetwork.vos.impl.UserVO;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<Set<IUserVo>> getAllUsersFromDB(){
        var users = userService.getAllUsers();

//        if (users == null || users.isEmpty()){
//            return ;
//        }

        return ResponseEntity.ok(users);
    }

}
