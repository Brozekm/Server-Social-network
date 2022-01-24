package com.brozek.socialnetwork.controller.DB;

import com.brozek.socialnetwork.dos.auth.AuthUserDO;
import com.brozek.socialnetwork.dos.auth.AuthUserRoleDO;
import com.brozek.socialnetwork.dos.auth.EnumAuthUserRole;
import com.brozek.socialnetwork.repository.IAuthRoleRepository;
import com.brozek.socialnetwork.repository.IUserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DBConfiguration implements InitializingBean {

    private final PasswordEncoder passwordEncoder;

    private final IUserJpaRepository userJpaRepository;

    private final IAuthRoleRepository authRoleRepository;

    private final String admin_email = "admin@gmail.com";

    private final String admin_username = "admin";

    private final String admin_password = "Admin12345";


    @Override
    public void afterPropertiesSet() {
        Set<AuthUserRoleDO> roles = null;
        if (doesAuthUserRolesExists()){
            roles = insertDefaultRoles();
        }


        if (!doesAdminExists()) {
            log.info("Admin account was not found");
            createAdmin(roles);
        }
    }

    private Set<AuthUserRoleDO> insertDefaultRoles() {
        AuthUserRoleDO admin = new AuthUserRoleDO(EnumAuthUserRole.ADMIN);
        AuthUserRoleDO user = new AuthUserRoleDO(EnumAuthUserRole.USER);

        return new HashSet<>(authRoleRepository.saveAll(Set.of(admin, user)));
    }

    private boolean doesAuthUserRolesExists() {
        return authRoleRepository.count() == 0;
    }

    private void createAdmin(Set<AuthUserRoleDO> roles) {
        AuthUserDO authUserDO = new AuthUserDO(admin_email, passwordEncoder.encode(admin_password), admin_username, roles);
        userJpaRepository.save(authUserDO);
        log.info("Default admin account created, email: {}, password: {}", admin_email, admin_password);
    }

    private boolean doesAdminExists() {
        return userJpaRepository.existsByEmail(admin_email);
    }
}
