package com.brozek.socialnetwork.service;

import com.brozek.socialnetwork.dos.auth.AuthUserRoleDO;
import com.brozek.socialnetwork.dos.auth.EnumAuthUserRole;
import com.brozek.socialnetwork.validation.exception.StringResponse;
import com.brozek.socialnetwork.validation.exception.TakenEmailException;
import com.brozek.socialnetwork.vos.EmailVO;
import com.brozek.socialnetwork.vos.RegisterCredentialsVO;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

public interface IUserService {

    @Transactional
    void createUser(@Valid RegisterCredentialsVO RegisterCredentialsVO) throws TakenEmailException, StringResponse;

    boolean isEmailTaken(String email);

    AuthUserRoleDO getRoleFromEnum(EnumAuthUserRole role);
}
