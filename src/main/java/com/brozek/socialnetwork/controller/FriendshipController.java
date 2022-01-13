package com.brozek.socialnetwork.controller;

import com.brozek.socialnetwork.service.IFriendshipsService;
import com.brozek.socialnetwork.validation.exception.StringResponse;
import com.brozek.socialnetwork.vos.EmailVO;
import com.brozek.socialnetwork.vos.NameLikeVO;
import com.brozek.socialnetwork.vos.PotentialFriendsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendshipController {

    private final IFriendshipsService friendshipsService;

    @GetMapping("/findUsersLike")
    public ResponseEntity<?> findUserWithNameLike(@RequestBody @Valid final NameLikeVO nameLikeVO){
        List<PotentialFriendsVO> potentialFriendsVOS = friendshipsService.searchForUsersLike(nameLikeVO);
        return ResponseEntity.ok(potentialFriendsVOS);
    }

    @PostMapping("/sendFriendshipRequest")
    public ResponseEntity<?> sendFriendshipRequest(@RequestBody @Valid final EmailVO emailVO){
        try {
            friendshipsService.sendFriendshipRequest(emailVO);
        } catch (StringResponse e) {
            return ResponseEntity.badRequest().body(e.getResponse());
        }

        return ResponseEntity.ok(null);
    }

}
