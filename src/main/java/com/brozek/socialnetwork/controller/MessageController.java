package com.brozek.socialnetwork.controller;

import com.brozek.socialnetwork.vos.chat.MessageVO;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void processMessages(@Payload @NotNull MessageVO message, Principal user){
        Assert.notNull(message);
        simpMessagingTemplate.convertAndSendToUser(message.getTargetUser(),
                "/queue/chat", new MessageVO(user.getName(),
                        message.getMessage()));
    }

}
