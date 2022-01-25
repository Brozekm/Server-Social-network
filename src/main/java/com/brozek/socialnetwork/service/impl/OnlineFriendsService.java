package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.dos.friendship.ISearchResultDO;
import com.brozek.socialnetwork.repository.IFriendshipRepository;
import com.brozek.socialnetwork.repository.IUserJpaRepository;
import com.brozek.socialnetwork.vos.chat.EnumOnlineStatus;
import com.brozek.socialnetwork.vos.chat.OnlineFriendVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnlineFriendsService {

    private Map<String, String> onlineUsers = new ConcurrentHashMap<>();

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final IUserJpaRepository userJpaRepository;

    private final IFriendshipRepository friendshipRepository;

    private static final String DESTINATION_ONLINE_FRIENDS = "/queue/online-friends";


    @EventListener
    public void onNewConnection(SessionSubscribeEvent event) {
        if (isOnlineFriendsSubscription(event.getMessage().getHeaders())) {
            if (event.getUser() != null) {
                log.debug("{} subscribed for online friends", event.getUser().getName());
                addNewOnlineAndNotify(event.getUser().getName());
            }
        }
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event){
        if (event.getUser() != null){
            log.debug("{} disconnected", event.getUser().getName());
            notifyUsersAboutDisconnected(event.getUser().getName());
        }
    }

    private void notifyUsersAboutDisconnected(String login) {
        OnlineFriendVO disconnectingUserVO = new OnlineFriendVO(login, onlineUsers.get(login), EnumOnlineStatus.OFFLINE);
        onlineUsers.remove(login);
        List<ISearchResultDO> usersOnlineFriends = getOnlineFriends(login);
        for (var onlineFriend: usersOnlineFriends){
            simpMessagingTemplate.convertAndSendToUser(onlineFriend.getEmail(), DESTINATION_ONLINE_FRIENDS, List.of(disconnectingUserVO));
        }
    }

    private void addNewOnlineAndNotify(String login) {
        String userName = userJpaRepository.findUserNameByEmail(login).orElseThrow(() -> new IllegalArgumentException("Could not fetch username from login"));

        List<ISearchResultDO> usersOnlineFriends = getOnlineFriends(login);

        OnlineFriendVO loggedUser = new OnlineFriendVO(login, userName, EnumOnlineStatus.ONLINE);
        List<OnlineFriendVO> onlineFriendVOS = new ArrayList<>();
        for (var onlineFriend : usersOnlineFriends) {
            OnlineFriendVO ofVO = new OnlineFriendVO(onlineFriend.getEmail(), onlineFriend.getUserName(), EnumOnlineStatus.ONLINE);
            onlineFriendVOS.add(ofVO);
            simpMessagingTemplate.convertAndSendToUser(onlineFriend.getEmail(), DESTINATION_ONLINE_FRIENDS, List.of(loggedUser));
        }

        simpMessagingTemplate.convertAndSendToUser(login, DESTINATION_ONLINE_FRIENDS, onlineFriendVOS);

        onlineUsers.putIfAbsent(login, userName);
    }


    public void notifyAfterFriendshipIfBothOnline(OnlineFriendVO first, OnlineFriendVO second){
        if (this.onlineUsers.containsKey(first.getEmail()) && this.onlineUsers.containsKey(second.getEmail())){
            this.simpMessagingTemplate.convertAndSendToUser(first.getEmail(), DESTINATION_ONLINE_FRIENDS, List.of(second));
            this.simpMessagingTemplate.convertAndSendToUser(second.getEmail(), DESTINATION_ONLINE_FRIENDS, List.of(first));
        }
    }

    private List<ISearchResultDO> getOnlineFriends(String login) {
        var onlineLogins = onlineUsers.keySet();
        return friendshipRepository.getUsersOnlineFriends(login, onlineLogins);
    }

    private boolean isOnlineFriendsSubscription(MessageHeaders headers) {
        String header = (String) headers.get("simpDestination");
        return header != null && header.equals("/user/queue/online-friends");
    }


}
