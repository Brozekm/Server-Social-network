package com.brozek.socialnetwork.repository.impl;

import com.brozek.socialnetwork.dos.IUserDO;
import com.brozek.socialnetwork.dos.impl.UserDO;
import com.brozek.socialnetwork.repository.IUserRepository;
import com.brozek.socialnetwork.validation.exception.TakenEmailException;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserRepository implements IUserRepository {

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert userInsert;

    private SimpleJdbcInsert roleInsert;

    private List<Map<String, Object>> roles;

    @PostConstruct
    public void init() {
        userInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("auth_user")
                .usingGeneratedKeyColumns("id");
        roleInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("auth_user_role");

        roles = new ArrayList<>();
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
    @Transactional
    public void registerUser(@NotNull IUserDO userDO) throws TakenEmailException {
        Assert.notNull(userDO,"User object is null");

        if (isEmailTaken(userDO.getEmail())){
            throw new TakenEmailException("Email is taken");
        }

        var parameters = new HashMap<String, Object>(4);
        parameters.put("email", userDO.getEmail());
        parameters.put("password", userDO.getPassword());
        parameters.put("first_name", userDO.getFirstName());
        parameters.put("surname", userDO.getSurname());

        KeyHolder userKeyHolder = userInsert.executeAndReturnKeyHolder(parameters);
        if (userKeyHolder.getKeyList().isEmpty()){
            throw new RuntimeException("User was not created successful");
        }
        var userId = (UUID) userKeyHolder.getKeyList().get(0).get("id");
        var roleId = getRoleID(IUserDO.EnumUserRole.user);
        if (roleId == null){
            throw new RuntimeException("Role id not found");
        }

        parameters = new HashMap<String, Object>(2);
        parameters.put("user_id", userId);
        parameters.put("role_id", roleId);

        int resultInt = roleInsert.execute(parameters);
        if (resultInt == 0){
            throw new RuntimeException("Role was not added correctly");
        }
    }

    private UUID getRoleID(IUserDO.EnumUserRole roleName){
        if (this.roles.isEmpty()){
            this.roles = jdbcTemplate.queryForList("SELECT * from auth_role");
        }
        for (var role: this.roles){
            if (role.get("rolename") == null || role.get("id") == null)
                return null;
            if (role.get("rolename").equals(roleName.name())){
                return (UUID) role.get("id");
            }
        }
        return null;
    }

    @Override
    public boolean isEmailTaken(String email) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT (1) FROM auth_user where email = ?", email);
        return !result.isEmpty();
    }
}
