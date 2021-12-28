package com.brozek.socialnetwork.service;

import com.brozek.socialnetwork.vos.IUserVO;

import java.util.Set;

public interface IUserService {

    Set<IUserVO> getAllUsers();

}
