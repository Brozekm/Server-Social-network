package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.service.IAuthenticationService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements IAuthenticationService {
    @Override
    public String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
