package com.brozek.socialnetwork.repository.impl;

import com.brozek.socialnetwork.dos.IUserDO;
import com.brozek.socialnetwork.dos.impl.UserDO;
import com.brozek.socialnetwork.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserRepository implements IUserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<IUserDO> getAllUsers() {
        final List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM auth_user");

        Set<IUserDO> users = new HashSet<>();
        for (Map<String, Object> rowMap: result){
            IUserDO user = new UserDO(
                    (UUID) rowMap.get("id"),
                    (String) rowMap.get("email"),
                    (String) rowMap.get("password"),
                    (String) rowMap.get("first_name"),
                    (String) rowMap.get("surname")
            );
            users.add(user);
        }

        return users;
    }

    @Override
    public IUserDO findUserWithRoleByEmail(String email) {
        final List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM auth_user au WHERE au.email = ?", email);

        if (result.isEmpty()){
            return null;
        }

        final Map<String, Object> userRaw = result.get(0);
        return new UserDO(
                (UUID) userRaw.get("id"),
                (String) userRaw.get("email"),
                (String) userRaw.get("password"),
                (String) userRaw.get("first_name"),
                (String) userRaw.get("surname"),
                IUserDO.EnumUserRole.user
        );
    }
}
