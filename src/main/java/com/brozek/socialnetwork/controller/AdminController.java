package com.brozek.socialnetwork.controller;

import com.brozek.socialnetwork.dos.auth.EnumAuthUserRole;
import com.brozek.socialnetwork.service.IAdminService;
import com.brozek.socialnetwork.vos.admin.ChangeRoleVO;
import com.brozek.socialnetwork.vos.admin.EnumRoleAction;
import com.brozek.socialnetwork.vos.admin.UserWithRolesVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final IAdminService adminService;

    @PutMapping("/friend/role/add")
    public ResponseEntity<?> addUserRole(@RequestBody ChangeRoleVO changeRoleVO){
        try {
            this.adminService.changeRole(changeRoleVO.getUser(), EnumRoleAction.ADD, changeRoleVO.getRole());
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalStateException | IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/friend/role/remove")
    public ResponseEntity<?> removeUserRole(@RequestBody ChangeRoleVO changeRoleVO){
        try {
            this.adminService.changeRole(changeRoleVO.getUser(), EnumRoleAction.REMOVE, changeRoleVO.getRole());
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalStateException | IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/friend")
    public ResponseEntity<?> getFriendsWithRoles(){
        try{
            List<UserWithRolesVO> friends = adminService.getAdminsFriends();
            return ResponseEntity.ok(friends);
        }catch (IllegalStateException | AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }




}
