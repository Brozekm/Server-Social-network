package com.brozek.socialnetwork.service;

import com.brozek.socialnetwork.validation.exception.TakenEmailException;
import com.brozek.socialnetwork.vos.IUserVO;

import java.util.Set;

public interface IUserService {

    Set<IUserVO> getAllUsers();


    IUserVO findUserByEmail(String email);

    boolean createUser(String email, String password, String firstName, String surname) throws TakenEmailException;
}
