package com.brozek.socialnetwork.service;

import com.brozek.socialnetwork.dos.auth.EnumAuthUserRole;
import com.brozek.socialnetwork.vos.admin.EnumRoleAction;
import com.brozek.socialnetwork.vos.admin.UserWithRolesVO;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface IAdminService {

    @Transactional
    void changeRole(String userLogin, EnumRoleAction action, EnumAuthUserRole role) throws AccessDeniedException;

    List<UserWithRolesVO> getAdminsFriends() throws AccessDeniedException;

}
