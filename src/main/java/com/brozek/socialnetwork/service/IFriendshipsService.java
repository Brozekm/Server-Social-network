package com.brozek.socialnetwork.service;

import com.brozek.socialnetwork.validation.exception.StringResponse;
import com.brozek.socialnetwork.vos.EmailVO;
import com.brozek.socialnetwork.vos.NameLikeVO;
import com.brozek.socialnetwork.vos.PotentialFriendsVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IFriendshipsService {

    List<PotentialFriendsVO> searchForUsersLike(NameLikeVO nameLikeVO);

    @Transactional
    void sendFriendshipRequest(EmailVO targetEmail) throws StringResponse;

    @Transactional
    void deleteFriendship(EmailVO targetEmail);

    @Transactional
    void acceptFriendship(EmailVO targetEmail);

    @Transactional
    void blockFriend(EmailVO targetEmail);

}