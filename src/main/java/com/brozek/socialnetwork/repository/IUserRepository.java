package com.brozek.socialnetwork.repository;

import com.brozek.socialnetwork.dos.IUserDO;

import java.util.Set;

public interface IUserRepository {

    Set<IUserDO> getAllUsers();

}
