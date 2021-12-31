package com.brozek.socialnetwork.repository.impl;

import com.brozek.socialnetwork.dos.IUserDO;
import com.brozek.socialnetwork.dos.impl.UserDO;
import com.brozek.socialnetwork.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserRepository implements IUserRepository {

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert userInsert;

    @PostConstruct
    public void init() {
        userInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("auth_user")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Set<IUserDO> getAllUsers() {
        final List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM auth_user");

        Set<IUserDO> users = new HashSet<>();
        for (Map<String, Object> rowMap : result) {
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

    private static final String SELECT_USER_BY_EMAIL_WITH_ROLE = "SELECT au.id, au.email, au.password, au.first_name, au.surname, ar.rolename FROM auth_user au" +
            " INNER JOIN auth_user_role aur on au.id = aur.user_id" +
            " INNER JOIN auth_role ar on ar.id = aur.role_id" +
            " WHERE au.email = ?";

    @Override
    public IUserDO findUserWithRoleByEmail(String email) {
        final List<Map<String, Object>> result = jdbcTemplate.queryForList(SELECT_USER_BY_EMAIL_WITH_ROLE, email);

        if (result.isEmpty()) {
            return null;
        }

        final Map<String, Object> userRaw = result.get(0);
        return new UserDO(
                (UUID) userRaw.get("id"),
                (String) userRaw.get("email"),
                (String) userRaw.get("password"),
                (String) userRaw.get("first_name"),
                (String) userRaw.get("surname"),
                IUserDO.EnumUserRole.valueOf((String) userRaw.get("rolename"))
        );
    }

    @Override
    public boolean registerUser(@NotNull IUserDO userDO) {
        if (isEmailTaken(userDO.getEmail())){
            return false;
        }

        var parameters = new HashMap<String, Object>(4);
        parameters.put("email", userDO.getEmail());
        parameters.put("password", userDO.getPassword());
        parameters.put("first_name", userDO.getFirstName());
        parameters.put("surname", userDO.getSurname());

        KeyHolder userKeyHolder = userInsert.executeAndReturnKeyHolder(parameters);
        if (userKeyHolder.getKeyList().isEmpty()){
            return false;
        }
        var userId = (String) userKeyHolder.getKeyList().get(0).get("id");

        //TODO add role to user
        return true;
    }

    @Override
    public boolean isEmailTaken(String email) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT (1) FROM auth_user where email = ?", email);
        return !result.isEmpty();
    }
}
