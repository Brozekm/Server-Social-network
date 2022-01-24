package com.brozek.socialnetwork.service;

import com.brozek.socialnetwork.validation.exception.TakenEmailException;
import com.brozek.socialnetwork.vos.EmailVO;
import com.brozek.socialnetwork.vos.RegisterCredentialsVO;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

public interface IUserService {

    @Transactional
    void createUser(@Valid RegisterCredentialsVO RegisterCredentialsVO) throws TakenEmailException;

    boolean isEmailTaken(String email);
}
