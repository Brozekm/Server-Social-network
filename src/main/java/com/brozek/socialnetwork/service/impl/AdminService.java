package com.brozek.socialnetwork.service.impl;


import com.brozek.socialnetwork.dos.auth.AuthUserDO;
import com.brozek.socialnetwork.dos.auth.AuthUserRoleDO;
import com.brozek.socialnetwork.dos.auth.EnumAuthUserRole;
import com.brozek.socialnetwork.dos.friendship.ISearchResultDO;
import com.brozek.socialnetwork.repository.IFriendshipRepository;
import com.brozek.socialnetwork.repository.IUserJpaRepository;
import com.brozek.socialnetwork.service.IAdminService;
import com.brozek.socialnetwork.service.IAuthenticationService;
import com.brozek.socialnetwork.service.IUserService;
import com.brozek.socialnetwork.vos.admin.EnumRoleAction;
import com.brozek.socialnetwork.vos.admin.UserWithRolesVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {

    private final IFriendshipRepository friendshipRepository;

    private final IAuthenticationService authenticationService;

    private final IUserJpaRepository userJpaRepository;

    private final IUserService userService;


    @Override
    public void changeRole(String userLogin, EnumRoleAction action, EnumAuthUserRole role) throws AccessDeniedException {
        if (authenticationService.getUserEmail() == null || !authenticationService.isAdmin()) {
            throw new AccessDeniedException("Unauthorized user");
        }
        if (authenticationService.getUserEmail().equals(userLogin)){
            throw new IllegalArgumentException("User can not change his roles");
        }

        AuthUserDO user = userJpaRepository.findByEmail(userLogin);
        if (user == null){
            throw new IllegalArgumentException("Targeted user was not found");
        }

        switch (action){
            case ADD -> user.addRole(userService.getRoleFromEnum(role));
            case REMOVE -> user.removeRole(role);
        }
    }

    @Override
    public List<UserWithRolesVO> getAdminsFriends() throws AccessDeniedException {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null || !authenticationService.isAdmin()) {
            throw new AccessDeniedException("Unauthorized user");
        }
        List<ISearchResultDO> friends = friendshipRepository.getUsersFriends(loggedUser);

        List<String> emails = friends.stream()
                .map(ISearchResultDO::getEmail).toList();

        List<AuthUserDO> authUserWithRoles = userJpaRepository.findByEmailAndFetchRoles(emails);

        List<UserWithRolesVO> friendsWithRoles = new ArrayList<>();
        for (var user : authUserWithRoles){
            Set<EnumAuthUserRole> roles = user.getRoles().stream()
                    .map(AuthUserRoleDO::getRoleName)
                    .collect(Collectors.toSet());
            UserWithRolesVO userWithRolesVO = new UserWithRolesVO(user.getEmail(), user.getUserName(), roles);
            friendsWithRoles.add(userWithRolesVO);
        }

        return friendsWithRoles;
    }
}
