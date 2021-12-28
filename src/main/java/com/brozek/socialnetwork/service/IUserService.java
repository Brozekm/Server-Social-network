package com.brozek.socialnetwork.service;

import com.brozek.socialnetwork.vos.IUserVo;
import com.brozek.socialnetwork.vos.impl.UserVO;

import java.util.Set;

public interface IUserService {

    Set<IUserVo> getAllUsers();

}
