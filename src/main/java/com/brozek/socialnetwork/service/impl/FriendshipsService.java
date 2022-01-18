package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.dos.auth.AuthUserDO;
import com.brozek.socialnetwork.dos.friendship.EnumFriendshipStatus;
import com.brozek.socialnetwork.dos.friendship.FriendshipDO;
import com.brozek.socialnetwork.dos.friendship.ISearchResultDO;
import com.brozek.socialnetwork.repository.IFriendshipRepository;
import com.brozek.socialnetwork.repository.IUserJpaRepository;
import com.brozek.socialnetwork.service.IAuthenticationService;
import com.brozek.socialnetwork.service.IFriendshipsService;
import com.brozek.socialnetwork.validation.exception.StringResponse;
import com.brozek.socialnetwork.vos.EmailVO;
import com.brozek.socialnetwork.vos.UserVO;
import com.brozek.socialnetwork.vos.friendship.SearchFriendVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class FriendshipsService implements IFriendshipsService {

    private final IFriendshipRepository friendshipRepository;

    private final IUserJpaRepository userJpaRepository;

    private final IAuthenticationService authenticationService;

    @Override
    public List<SearchFriendVO> searchForUsersLike(String nameLikeVO) {
        final String loggedUserEmail = authenticationService.getUserEmail();
        log.debug("{} is looking for friends named: {}", loggedUserEmail, nameLikeVO);

        var searchString = "%" + nameLikeVO.toLowerCase() + "%";
        List<ISearchResultDO> searchResults = friendshipRepository.searchForUsernameLike(searchString, loggedUserEmail);

        List<SearchFriendVO> potentialFriends = new ArrayList<>();
        for (var oneResult : searchResults) {
            var s = oneResult.getStatus();
            SearchFriendVO poFriend = new SearchFriendVO(oneResult.getEmail(), oneResult.getUserName(), oneResult.getStatus());
            potentialFriends.add(poFriend);
        }
        return potentialFriends;
    }

    @Override
    public List<UserVO> getFriendRequests() {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null) {
            log.warn("User is not logged after passing authentication");
            throw new IllegalStateException("User is not logged");
        }
        List<ISearchResultDO> searchResults = friendshipRepository.getUsersFriendshipRequest(loggedUser);

        return mapISearchDOToUserVO(searchResults);
    }

    @Override
    public List<UserVO> getFriends() {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null) {
            log.warn("User is not logged after passing authentication");
            throw new IllegalStateException("User is not logged");
        }
        List<ISearchResultDO> searchResults = friendshipRepository.getUsersFriends(loggedUser);

        return mapISearchDOToUserVO(searchResults);
    }

    @Override
    public List<UserVO> getBlockedUsers() {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null) {
            log.warn("User is not logged after passing authentication");
            throw new IllegalStateException("User is not logged");
        }
        List<ISearchResultDO> searchResults = friendshipRepository.getUsersBlockedUsers(loggedUser);

        return mapISearchDOToUserVO(searchResults);
    }

    private List<UserVO> mapISearchDOToUserVO(List<ISearchResultDO> searchResults) {
        List<UserVO> users = new ArrayList<>();
        for (var oneResult : searchResults) {
            UserVO poFriend = new UserVO(oneResult.getEmail(), oneResult.getUserName());
            users.add(poFriend);
        }
        return users;
    }

    @Override
    public void sendFriendshipRequest(EmailVO targetEmail) throws StringResponse {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null) {
            log.warn("User is not logged after passing authentication");
            throw new IllegalStateException("User is not logged");
        }

        log.debug("Creating new friendship between {} and {}", loggedUser, targetEmail.getEmail());
        if (loggedUser.equals(targetEmail.getEmail())) {
            throw new StringResponse("Are you really that lonely?");
        }

        if (friendshipRepository.doUsersHaveRelationship(loggedUser, targetEmail.getEmail())) {
            throw new IllegalStateException("Users already have relationship");
        }

        List<AuthUserDO> users = userJpaRepository.findByEmailIn(List.of(loggedUser, targetEmail.getEmail()));

        if (users.size() != 2) {
            throw new IllegalArgumentException("Targeted user do not exists");
        }

        FriendshipDO friendshipDO;
        if (users.get(0).getEmail().equals(loggedUser)) {
            friendshipDO = new FriendshipDO(users.get(0), users.get(1));
        } else {
            friendshipDO = new FriendshipDO(users.get(1), users.get(0));
        }

        friendshipRepository.save(friendshipDO);
        log.debug("Friendship request created");
    }

    @Override
    public void acceptFriendship(EmailVO targetEmail) {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null) {
            log.warn("User is not logged after passing authentication");
            throw new IllegalStateException("User is not logged");
        }
        log.debug("User {} is accepting friendship from {}", loggedUser, targetEmail.getEmail());

        FriendshipDO friendship = friendshipRepository.getRelationshipByEmails(loggedUser, targetEmail.getEmail());
        if (friendship == null) {
            throw new IllegalStateException("Users do not have any relationship");
        }
        if (friendship.getStatus() != EnumFriendshipStatus.NEW) {
            throw new IllegalStateException("Relationship is not in NEW state, could not be accepted");
        }

        friendship.setStatus(EnumFriendshipStatus.FRIEND);
        log.debug("{} and {} are now friends", loggedUser, targetEmail.getEmail());
    }

    @Override
    public void deleteFriendship(String targetEmail) {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null) {
            log.warn("User is not logged after passing authentication");
            throw new IllegalStateException("User is not logged");
        }
        log.debug("User {} is deleting friendship with {}", loggedUser, targetEmail);

        FriendshipDO friendship = friendshipRepository.getRelationshipByEmails(loggedUser, targetEmail);
        if (friendship == null) {
            throw new IllegalStateException("Users do not have any relationship");
        }

        friendshipRepository.delete(friendship);
        log.debug("{} deleted relationship with {}", loggedUser, targetEmail);
    }

    @Override
    public void blockFriend(EmailVO targetEmail) {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null) {
            log.warn("User is not logged after passing authentication");
            throw new IllegalStateException("User is not logged");
        }
        log.debug("User {} is trying to block {}", loggedUser, targetEmail.getEmail());

        FriendshipDO friendship = friendshipRepository.getRelationshipByEmails(loggedUser, targetEmail.getEmail());
        if (friendship == null) {
            throw new IllegalStateException("Users do not have any relationship");
        }
        if (friendship.getStatus() == EnumFriendshipStatus.BLOCKED) {
            return;
        }

        friendship.setStatus(EnumFriendshipStatus.BLOCKED);
        log.debug("{} blocked {}", loggedUser, targetEmail.getEmail());
    }

    @Override
    public void unblockFriend(EmailVO targetEmail) {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null) {
            log.warn("User is not logged after passing authentication");
            throw new IllegalStateException("User is not logged");
        }
        log.debug("User {} is trying to unblock {}", loggedUser, targetEmail.getEmail());

        FriendshipDO friendship = friendshipRepository.getRelationshipByEmails(loggedUser, targetEmail.getEmail());
        if (friendship == null) {
            throw new IllegalStateException("Users do not have any relationship");
        }
        if (friendship.getStatus() != EnumFriendshipStatus.BLOCKED) {
            throw new IllegalStateException("User is trying to unblock someone who is not blocked");
        }

        friendship.setStatus(EnumFriendshipStatus.NEW);
        log.debug("{} unblocked {}", loggedUser, targetEmail.getEmail());
    }
}
