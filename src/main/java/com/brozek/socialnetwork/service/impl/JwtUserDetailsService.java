package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.vos.impl.JwtResponseCredentials;
import com.brozek.socialnetwork.vos.impl.JwtResponseVO;
import com.brozek.socialnetwork.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var userDO = userRepository.findUserWithRoleByEmail(username);
        if (userDO == null)
            throw new UsernameNotFoundException("User's email was not found: " + username);

        return User.builder()
                .username(userDO.getEmail())
                .password(userDO.getPassword())
                .authorities("ROLE_"+userDO.getRole().name())
                .build();
    }

    public JwtResponseCredentials loadUserByEmail(String email){
        final var userDO = userRepository.findUserWithRoleByEmail(email);
        if (userDO == null){
            return null;
        }
        var userDetails = User.builder()
                .username(userDO.getEmail())
                .password(userDO.getPassword())
                .authorities("ROLE_"+userDO.getRole().name())
                .build();

        var jwtResponseVO = new JwtResponseVO(userDO.getEmail(), userDO.getFirstName(), userDO.getSurname());

        return new JwtResponseCredentials(userDetails, jwtResponseVO);
    }

    public boolean doesUserExists(String email) throws UsernameNotFoundException{
        //TODO validate email
        return userRepository.isEmailTaken(email);
    }
}
