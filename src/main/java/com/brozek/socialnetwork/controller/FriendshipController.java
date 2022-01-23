package com.brozek.socialnetwork.controller;

import com.brozek.socialnetwork.service.IFriendshipsService;
import com.brozek.socialnetwork.validation.exception.StringResponse;
import com.brozek.socialnetwork.vos.EmailVO;
import com.brozek.socialnetwork.vos.UserVO;
import com.brozek.socialnetwork.vos.friendship.SearchFriendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendshipController implements IApiRestController{

    private final IFriendshipsService friendshipsService;


    //    @GetMapping("/findUsersLike")
    @GetMapping("/users")
    public ResponseEntity<?> findUserWithNameLike(@RequestParam String nameLike) {
        List<SearchFriendVO> userVOS = friendshipsService.searchForUsersLike(nameLike);
        return ResponseEntity.ok(userVOS);
    }


    //    @GetMapping("/getFriendshipRequests")
    @GetMapping("/relationships/request")
    public ResponseEntity<?> getNewFriendRequests() {
        List<UserVO> friendRequests;
        try {
            friendRequests = this.friendshipsService.getFriendRequests();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(friendRequests);
    }

    //    @GetMapping("/getBlockedUsers")
    @GetMapping("/relationships/blocked")
    public ResponseEntity<?> getBlockedUsers() {
        List<UserVO> blockedUsers;
        try {
            blockedUsers = this.friendshipsService.getBlockedUsers();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(blockedUsers);
    }


    //    @GetMapping("/getFriends")
    @GetMapping("/relationships/friend")
    public ResponseEntity<?> getFriendList() {
        List<UserVO> friends;
        try {
            friends = this.friendshipsService.getFriends();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(friends);
    }


    //    @PostMapping("/sendFriendshipRequest")
    @PostMapping("/relationships/request")
    public ResponseEntity<?> sendFriendshipRequest(@RequestBody @Valid final EmailVO emailVO) {
        try {
            friendshipsService.sendFriendshipRequest(emailVO);
        } catch (StringResponse e) {
            return ResponseEntity.badRequest().body(e.getResponse());
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(null);
    }

    //    @PutMapping("/acceptFriendship")
    @PutMapping("/relationships/friend")
    public ResponseEntity<?> acceptFriendshipRequest(@RequestBody @Valid final EmailVO emailVO) {
        try {
            friendshipsService.acceptFriendship(emailVO);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(null);
    }

    //    @DeleteMapping("/deleteRelationship")
    @DeleteMapping("/relationships")
    public ResponseEntity<?> deleteRelationship(@RequestParam String email) {
        try {
            friendshipsService.deleteFriendship(email);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(null);
    }

    //    @PutMapping("/blockUser")
    @PutMapping("/user/block")
    public ResponseEntity<?> blockFriendship(@RequestBody @Valid final EmailVO emailVO) {
        try {
            friendshipsService.blockFriend(emailVO);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(null);
    }

    //    @PutMapping("/unblockUser")
    @PutMapping("/user/unblock")
    public ResponseEntity<?> unblockFriendship(@RequestBody @Valid final EmailVO emailVO) {
        try {
            friendshipsService.unblockFriend(emailVO);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(null);
    }

}
