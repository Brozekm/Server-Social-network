package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.dos.IUserDO;
import com.brozek.socialnetwork.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException{
        final var userDO = userRepository.findUserWithRoleByEmail(email);
        if (userDO == null)
            throw new UsernameNotFoundException("User's email was not found: " + email);

        return User.builder()
                .username(userDO.getEmail())
                .password(userDO.getPassword())
                .authorities(userDO.getRole().name())
                .build();
    }
}
