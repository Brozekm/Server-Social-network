package com.brozek.socialnetwork.repository;

import com.brozek.socialnetwork.dos.IUserDO;
import com.brozek.socialnetwork.validation.exception.TakenEmailException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface IUserRepository {

    Set<IUserDO> getAllUsers();

    IUserDO findUserWithRoleByEmail(String email);

    @Transactional
    void registerUser(IUserDO userDO) throws TakenEmailException;

    boolean isEmailTaken(String email);
}
