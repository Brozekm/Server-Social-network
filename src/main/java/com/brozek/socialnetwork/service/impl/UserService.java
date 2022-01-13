package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.dos.EnumAuthRole;
import com.brozek.socialnetwork.dos.AuthUserDO;
import com.brozek.socialnetwork.repository.IUserJpaRepository;
import com.brozek.socialnetwork.service.IAuthenticationService;
import com.brozek.socialnetwork.service.IUserService;
import com.brozek.socialnetwork.validation.exception.TakenEmailException;
import com.brozek.socialnetwork.vos.RegisterCredentialsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;

    private final IAuthenticationService authenticationService;

    private final IUserJpaRepository userJpaRepository;


    @Override
    @Transactional
    public void createUser(RegisterCredentialsVO registerCredentialsVO) throws TakenEmailException {
        log.info("New registration with email: {}", registerCredentialsVO.getEmail());

        //TODO VALIDATE

        AuthUserDO authUserDO = new AuthUserDO(registerCredentialsVO.getEmail(),
                passwordEncoder.encode(registerCredentialsVO.getPassword()),
                registerCredentialsVO.getUserName(),
                EnumAuthRole.USER);

        if (userJpaRepository.existsByEmail(authUserDO.getEmail())){
            log.info("Email ({}) is taken", registerCredentialsVO.getEmail());
            throw new TakenEmailException("Email is taken");
        }

        userJpaRepository.save(authUserDO);
    }
}
