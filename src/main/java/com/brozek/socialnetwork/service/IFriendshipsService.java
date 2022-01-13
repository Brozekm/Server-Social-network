package com.brozek.socialnetwork.service;

import com.brozek.socialnetwork.validation.exception.StringResponse;
import com.brozek.socialnetwork.vos.EmailVO;
import com.brozek.socialnetwork.vos.NameLikeVO;
import com.brozek.socialnetwork.vos.PotentialFriendsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IFriendshipsService {

    List<PotentialFriendsVO> searchForUsersLike(NameLikeVO nameLikeVO);

    @Transactional
    void sendFriendshipRequest(EmailVO targetEmail) throws StringResponse;

    boolean deleteFriendship(String targetEmail);

}
