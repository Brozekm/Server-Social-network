package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.dos.AuthUserDO;
import com.brozek.socialnetwork.dos.ISearchResultDO;
import com.brozek.socialnetwork.dos.IUserEmailID;
import com.brozek.socialnetwork.repository.IFriendshipRepository;
import com.brozek.socialnetwork.repository.IUserJpaRepository;
import com.brozek.socialnetwork.service.IAuthenticationService;
import com.brozek.socialnetwork.service.IFriendshipsService;
import com.brozek.socialnetwork.validation.exception.StringResponse;
import com.brozek.socialnetwork.vos.EmailVO;
import com.brozek.socialnetwork.vos.NameLikeVO;
import com.brozek.socialnetwork.vos.PotentialFriendsVO;
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
    public List<PotentialFriendsVO> searchForUsersLike(NameLikeVO nameLikeVO) {
        final String loggedUserEmail = authenticationService.getUserEmail();
        log.debug("{} is looking for friends named: {}", loggedUserEmail, nameLikeVO.getName());

        var searchString = "%"+nameLikeVO.getName().toLowerCase()+"%";
        List<ISearchResultDO> searchResults = friendshipRepository.searchForUsernameLike(searchString, loggedUserEmail);
        List<PotentialFriendsVO> potentialFriends = new ArrayList<>();
        for (var oneResult: searchResults){
            String s = oneResult.getFriendStatus();
            log.info("Friend status: {}", oneResult.getFriendStatus());
            PotentialFriendsVO poFriend = new PotentialFriendsVO(oneResult.getEmail(), oneResult.getUserName());
            potentialFriends.add(poFriend);
        }
        return potentialFriends;
    }

    @Override
    public void sendFriendshipRequest(EmailVO targetEmail) throws StringResponse {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null){
            return;
        }
        log.debug("Creating new friendship between {} and {}", loggedUser, targetEmail.getEmail());
        if (loggedUser.equals(targetEmail.getEmail())){
            throw new StringResponse("Are you really that lonely?");
        }
        if (friendshipRepository.doUsersHaveRelationship(loggedUser, targetEmail.getEmail())){
            //TODO change to 400 -> currently throws 500 on client
            throw new IllegalStateException("Users already have relationship");
        }
        var userList = new ArrayList<String>(2);
        userList.add(loggedUser);
        userList.add(targetEmail.getEmail());
        List<IUserEmailID> usersEmailId = userJpaRepository.getUsersIds(userList);
        if (usersEmailId.size() != 2){
            //TODO throw error -> users not found
            return;
        }

        var idLogged = 0;
        var idTarget = 0;

        if (usersEmailId.get(0).getEmail().equals(loggedUser)){
            idLogged = usersEmailId.get(0).getId();
            idTarget = usersEmailId.get(1).getId();
        }else{
            idLogged = usersEmailId.get(1).getId();
            idTarget = usersEmailId.get(0).getId();
        }



    }

    @Override
    public boolean deleteFriendship(String targetEmail) {
        return false;
    }
}
