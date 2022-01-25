package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.dos.auth.AuthUserDO;
import com.brozek.socialnetwork.dos.auth.AuthUserRoleDO;
import com.brozek.socialnetwork.dos.auth.EnumAuthUserRole;
import com.brozek.socialnetwork.repository.IAuthRoleRepository;
import com.brozek.socialnetwork.repository.IUserJpaRepository;
import com.brozek.socialnetwork.service.IAuthenticationService;
import com.brozek.socialnetwork.service.IUserService;
import com.brozek.socialnetwork.validation.exception.StringResponse;
import com.brozek.socialnetwork.validation.exception.TakenEmailException;
import com.brozek.socialnetwork.vos.EmailVO;
import com.brozek.socialnetwork.vos.RegisterCredentialsVO;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;

    private final IAuthenticationService authenticationService;

    private final IAuthRoleRepository authRoleRepository;

    private final IUserJpaRepository userJpaRepository;

    private Map<EnumAuthUserRole, Integer> authRoleMap;


    @Override
    @Transactional
    public void createUser(RegisterCredentialsVO registerCredentialsVO) throws TakenEmailException, StringResponse {
        log.info("New registration with email: {}", registerCredentialsVO.getEmail());

        if(!validCredentials(registerCredentialsVO)){
            throw new IllegalArgumentException("Invalid credentials");
        }

        AuthUserDO authUserDO = new AuthUserDO(registerCredentialsVO.getEmail(),
                passwordEncoder.encode(registerCredentialsVO.getPassword()),
                registerCredentialsVO.getUserName(),
                Set.of(getRoleFromEnum(EnumAuthUserRole.USER)));

        if (userJpaRepository.existsByEmail(authUserDO.getEmail())){
            log.info("Email ({}) is taken", registerCredentialsVO.getEmail());
            throw new TakenEmailException("Email is taken");
        }

        userJpaRepository.save(authUserDO);
    }

    @Override
    public boolean isEmailTaken(String email) {
        if (email.length() < 3 || !new EmailValidator().isValid(email, null)){
            throw new IllegalArgumentException("Email is not valid");
        }
        return userJpaRepository.existsByEmail(email);
    }

    private boolean validCredentials(RegisterCredentialsVO registerCredentialsVO) throws StringResponse {
        if(!new EmailValidator().isValid(registerCredentialsVO.getEmail(), null)){
            return false;
        }

        if (registerCredentialsVO.getUserName().length() < 1 || registerCredentialsVO.getUserName().length() > 20){
            return false;
        }

        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(registerCredentialsVO.getPassword());
        if (strength.getScore() < 2){
            throw new StringResponse("Weak password");
        }

        boolean upper = false, lower = false, digit = false;
        for (var character: registerCredentialsVO.getPassword().toCharArray()){
            if (Character.isUpperCase(character)) upper = true;
            if (Character.isLowerCase(character)) lower = true;
            if (Character.isDigit(character)) digit = true;
        }

        return (upper && lower && digit);
    }


    public AuthUserRoleDO getRoleFromEnum(EnumAuthUserRole role){
        if (this.authRoleMap == null){
            getRoles();
        }

        return authRoleRepository.getById(this.authRoleMap.get(role));
    }

    private void getRoles() {
        this.authRoleMap = new HashMap<>();
        List<AuthUserRoleDO> listRole = this.authRoleRepository.findAll();
        for(var role: listRole){
            this.authRoleMap.put(role.getRoleName(), role.getId());
        }
    }

}
