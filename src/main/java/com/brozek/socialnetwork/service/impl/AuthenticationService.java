package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.dos.auth.EnumAuthUserRole;
import com.brozek.socialnetwork.service.IAuthenticationService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AuthenticationService implements IAuthenticationService {

    private static final String AUTH_PREFIX = "ROLE_";

    @Override
    public String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public boolean isAdmin() {
        return getRolesFromContext().stream()
                .anyMatch(enumAuthUserRole -> enumAuthUserRole == EnumAuthUserRole.ADMIN);
    }

    public Collection<EnumAuthUserRole> getRolesFromContext(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s =s.substring(AUTH_PREFIX.length()))
                .map(EnumAuthUserRole::valueOf).collect(Collectors.toList());
    }


}
