package com.brozek.socialnetwork.config;

import com.brozek.socialnetwork.config.auth.JwtRequestFilter;
import com.brozek.socialnetwork.config.auth.JwtTokenUtil;
import com.brozek.socialnetwork.service.impl.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.ArrayList;
import java.util.Map;


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final JwtRequestFilter jwtRequestFilter;

    private final JwtUserDetailsService jwtUserDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/online")
                .setAllowedOrigins("http://localhost:4200/");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue");
        config.setApplicationDestinationPrefixes("/ws");
        config.setUserDestinationPrefix("/user");
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null && accessor.getCommand() == StompCommand.CONNECT){
                    setWSAuthentication(accessor, message);
                }
                return message;
            }
        });
    }

    private void setWSAuthentication(StompHeaderAccessor accessor, Message<?> message) {
        String authToken = getTokenFromMessage(message);
        if (authToken != null){
            String jwtToken = jwtRequestFilter.getTokenFromHeader(authToken);

            String email = jwtRequestFilter.getEmailFromToken(jwtToken);

            if (email == null) return;

            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                accessor.setUser(usernamePasswordAuthenticationToken);
            }
        }
    }

    private String getTokenFromMessage(Message<?> message) {
        @SuppressWarnings("unchecked")
        Map<String, Object> nativeHeaders = (Map<String, Object>)message.getHeaders().get("nativeHeaders");
        @SuppressWarnings("unchecked")
        ArrayList<String> values = (ArrayList<String>) nativeHeaders.get("Authorization");
        if (values != null && values.size() > 0){
            return values.get(0);
        }
        return null;
    }
}
