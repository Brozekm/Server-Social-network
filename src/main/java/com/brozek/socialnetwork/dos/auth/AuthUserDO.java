package com.brozek.socialnetwork.dos.auth;


import lombok.Getter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "auth_user")
@Getter
public class AuthUserDO {

    private static final String SEQUENCE_NAME = "auth_user_id_seq";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String USER_NAME = "user_name";
    private static final String ROLE = "role";
    private static final String USER_ID = "user_id";
    private static final String ROLE_ID = "role_id";
    public static final String AUTH_USER_ROLE_TABLE = "auth_user_role";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    private Integer id;

    @Column(name = EMAIL, nullable = false, unique = true)
    private String email;

    @Column(name = PASSWORD, nullable = false)
    private String password;

    @Column(name = USER_NAME, nullable = false, length = 20)
    private String userName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = AUTH_USER_ROLE_TABLE,
            joinColumns = @JoinColumn(name = USER_ID),
            inverseJoinColumns = @JoinColumn(name = ROLE_ID))
    @ToString.Exclude
    private Set<AuthUserRoleDO> roles;

    public AuthUserDO() {
    }

    public AuthUserDO(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.roles = new LinkedHashSet<>();
    }

    public AuthUserDO(String email, String password, String userName, Set<AuthUserRoleDO> roles) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.roles = roles;
    }

    public Set<AuthUserRoleDO> getRoles() {
        return Set.copyOf(roles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AuthUserDO userDO = (AuthUserDO) o;
        return id != null && Objects.equals(id, userDO.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addRole(final AuthUserRoleDO role) {
        roles.add(role);
    }

    public boolean removeRole(final EnumAuthUserRole role) {
        return roles.removeIf(userRole -> userRole.getRoleName() == role);
    }
}
