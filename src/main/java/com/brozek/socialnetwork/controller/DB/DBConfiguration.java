package com.brozek.socialnetwork.controller.DB;

import com.brozek.socialnetwork.dos.EnumAuthRole;
import com.brozek.socialnetwork.dos.AuthUserDO;
import com.brozek.socialnetwork.repository.IUserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DBConfiguration implements InitializingBean {

    private final PasswordEncoder passwordEncoder;

    private final IUserJpaRepository userJpaRepository;

    private final String admin_email = "admin@gmail.com";

    private final String admin_username = "admin";

    private final String admin_password = "Admin12345";


    @Override
    public void afterPropertiesSet() {
        if (!doesAdminExists()) {
            log.info("Admin account was not found");
            createAdmin();
        }
    }

    private void createAdmin() {
        AuthUserDO authUserDO = new AuthUserDO(admin_email, passwordEncoder.encode(admin_password), admin_username, EnumAuthRole.ADMIN);
        userJpaRepository.save(authUserDO);
        log.info("Default admin account created, email: {}, password: {}", admin_email, admin_password);
    }

    private boolean doesAdminExists() {
        return userJpaRepository.existsByEmail(admin_email);
    }
}
