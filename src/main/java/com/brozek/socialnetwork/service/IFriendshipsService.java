package com.brozek.socialnetwork.service;

import com.brozek.socialnetwork.validation.exception.StringResponse;
import com.brozek.socialnetwork.vos.EmailVO;
import com.brozek.socialnetwork.vos.UserVO;
import com.brozek.socialnetwork.vos.friendship.SearchFriendVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IFriendshipsService {

    @Transactional
    List<SearchFriendVO> searchForUsersLike(String nameLikeVO);

    @Transactional
    List<UserVO> getFriendRequests();

    @Transactional
    List<UserVO> getFriends();

    @Transactional
    List<UserVO> getBlockedUsers();

    @Transactional
    void sendFriendshipRequest(EmailVO targetEmail) throws StringResponse;

    @Transactional
    void deleteFriendship(String targetEmail);

    @Transactional
    void acceptFriendship(EmailVO targetEmail);

    @Transactional
    void blockFriend(EmailVO targetEmail);

    @Transactional
    void unblockFriend(EmailVO targetEmail);

}
