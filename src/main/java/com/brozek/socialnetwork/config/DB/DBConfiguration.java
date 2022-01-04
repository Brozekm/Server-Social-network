package com.brozek.socialnetwork.config.DB;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DBConfiguration implements InitializingBean {

    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    private final String admin_email = "admin@gmail.com";
    private final String admin_first_name = "admin";
    private final String admin_surname = "admin";
    private final String admin_password = "Admin12345";


    @Override
    public void afterPropertiesSet() throws Exception {
        if (doesAdminExists()) {
            log.info("Admin account was not found");
            if (!createAdmin())
                throw new Exception("Could not create default admin account");
        }
    }

    private boolean createAdmin() {
        int insert = jdbcTemplate.update("INSERT INTO auth_user (email, password, first_name, surname, role)" +
                        " VALUES (?, ?, ?, ?, 'admin')",
                admin_email,
                passwordEncoder.encode(admin_password),
                admin_first_name,
                admin_surname);
        if (insert == 1) {
            log.info("Admin account created email: {}, password: {}", admin_email, admin_password);
            return true;
        } else {
            log.error("Could not create admin account");
            return false;
        }
    }

    private boolean doesAdminExists() {
        return jdbcTemplate.queryForList("SELECT (1) FROM auth_user where email = ?", admin_email).isEmpty();
    }
}
