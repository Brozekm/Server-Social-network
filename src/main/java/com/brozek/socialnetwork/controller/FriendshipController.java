package com.brozek.socialnetwork.controller;

import com.brozek.socialnetwork.service.IFriendshipsService;
import com.brozek.socialnetwork.validation.exception.StringResponse;
import com.brozek.socialnetwork.vos.EmailVO;
import com.brozek.socialnetwork.vos.NameLikeVO;
import com.brozek.socialnetwork.vos.PotentialFriendsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendshipController {

    private final IFriendshipsService friendshipsService;

    @GetMapping("/findUsersLike")
    public ResponseEntity<?> findUserWithNameLike(@RequestParam String name){
        List<PotentialFriendsVO> potentialFriendsVOS = friendshipsService.searchForUsersLike(name);
        return ResponseEntity.ok(potentialFriendsVOS);
    }

    @GetMapping("/getFriendshipRequests")
    public ResponseEntity<?> getNewFriendRequests(){
        List<PotentialFriendsVO> friendRequests;
        try{
            friendRequests = this.friendshipsService.getFriendRequests();
        } catch (IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(friendRequests);
    }

    @PostMapping("/sendFriendshipRequest")
    public ResponseEntity<?> sendFriendshipRequest(@RequestBody @Valid final EmailVO emailVO){
        try {
            friendshipsService.sendFriendshipRequest(emailVO);
        } catch (StringResponse e) {
            return ResponseEntity.badRequest().body(e.getResponse());
        } catch (IllegalStateException | IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(null);
    }

    @PostMapping("/acceptFriendship")
    public ResponseEntity<?> acceptFriendshipRequest(@RequestBody @Valid final EmailVO emailVO){
        try{
            friendshipsService.acceptFriendship(emailVO);
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/deleteRelationship")
    public ResponseEntity<?> deleteRelationship(@RequestParam String email){
        try{
            friendshipsService.deleteFriendship(email);
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(null);
    }

    @PostMapping("/blockFriendship")
    public ResponseEntity<?> blockFriendship(@RequestBody @Valid final EmailVO emailVO){
        try{
            friendshipsService.blockFriend(emailVO);
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(null);
    }

}
