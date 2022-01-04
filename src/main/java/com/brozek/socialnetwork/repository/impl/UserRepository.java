package com.brozek.socialnetwork.repository.impl;

import com.brozek.socialnetwork.dos.IUserDO;
import com.brozek.socialnetwork.dos.impl.UserDO;
import com.brozek.socialnetwork.repository.IUserRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserRepository implements IUserRepository {

    private final JdbcTemplate jdbcTemplate;

//    @PostConstruct
//    public void init() {
//
//    }

    @Override
    public Set<IUserDO> getAllUsers() {
        final List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM auth_user");

        Set<IUserDO> users = new HashSet<>();
        for (Map<String, Object> rowMap : result) {
            IUserDO user = new UserDO(
                    (int) rowMap.get("id"),
                    (String) rowMap.get("email"),
                    (String) rowMap.get("password"),
                    (String) rowMap.get("first_name"),
                    (String) rowMap.get("surname"),
                    IUserDO.EnumUserRole.valueOf((String) rowMap.get("role"))
            );
            users.add(user);
        }

        return users;
    }

    private static final String SELECT_USER_BY_EMAIL_WITH_ROLE = "SELECT * FROM auth_user au" +
            " WHERE au.email = ?";

    @Override
    public IUserDO findUserWithRoleByEmail(String email) {
        final List<Map<String, Object>> result = jdbcTemplate.queryForList(SELECT_USER_BY_EMAIL_WITH_ROLE, email);

        if (result.isEmpty()) {
            return null;
        }

        final Map<String, Object> userRaw = result.get(0);
        return new UserDO(
                (int) userRaw.get("id"),
                (String) userRaw.get("email"),
                (String) userRaw.get("password"),
                (String) userRaw.get("first_name"),
                (String) userRaw.get("surname"),
                IUserDO.EnumUserRole.valueOf((String) userRaw.get("role"))
        );
    }

    @Override
    public int registerUser(@NotNull IUserDO userDO){
        Assert.notNull(userDO,"User object is null");

        return jdbcTemplate.update("INSERT INTO auth_user(id,email, password, first_name, surname, role)" +
                " VALUES (DEFAULT,?, ?, ?, ?, DEFAULT)",
                userDO.getEmail(),
                userDO.getPassword(),
                userDO.getFirstName(),
                userDO.getSurname());
    }


    @Override
    public boolean isEmailTaken(String email) {
        return !jdbcTemplate.queryForList("SELECT (1) FROM auth_user where email = ?", email).isEmpty();
    }
}
