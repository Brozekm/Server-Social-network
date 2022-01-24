package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.dos.auth.AuthUserDO;
import com.brozek.socialnetwork.dos.auth.AuthUserRoleDO;
import com.brozek.socialnetwork.repository.IUserJpaRepository;
import com.brozek.socialnetwork.vos.JwtResponseCredentials;
import com.brozek.socialnetwork.vos.JwtResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final IUserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthUserDO authUserDO = userJpaRepository.findByEmail(email);
        if (authUserDO == null){
            throw new UsernameNotFoundException("User's email was not found: " + email);
        }

        return User.builder()
                .username(authUserDO.getEmail())
                .password(authUserDO.getPassword())
                .authorities(authUserDO.getRoles().stream()
                        .map(AuthUserRoleDO::getRoleName)
                        .map(Enum::name)
                        .map(name -> "ROLE_" + name)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet()))
                .build();
    }

    public JwtResponseCredentials loadUserByEmail(String email){
        log.info("Loading user with email: {}",email);
        AuthUserDO authUserDO = userJpaRepository.findByEmail(email);
        if (authUserDO == null){
            log.info("User not found");
            return null;
        }
        var userDetails = User.builder()
                .username(authUserDO.getEmail())
                .password(authUserDO.getPassword())
                .authorities(authUserDO.getRoles().stream()
                        .map(AuthUserRoleDO::getRoleName)
                        .map(Enum::name)
                        .map(name -> "ROLE_" + name)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet()))
                .build();

        var jwtResponseVO = new JwtResponseVO(authUserDO.getEmail(), authUserDO.getUserName(),
                authUserDO.getRoles().stream()
                        .map(AuthUserRoleDO::getRoleName)
                        .collect(Collectors.toSet()));

        return new JwtResponseCredentials(userDetails, jwtResponseVO);
    }
}
