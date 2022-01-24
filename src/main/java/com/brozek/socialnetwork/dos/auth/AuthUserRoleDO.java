package com.brozek.socialnetwork.dos.auth;


import lombok.Getter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity(name = "auth_role")
public class AuthUserRoleDO {

    public static final String SEQUENCE_NAME = "auth_role_id_seq";
    public static final String ROLE_NAME = "role_name";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = ROLE_NAME, nullable = false, unique = true, length = 15)
    private EnumAuthUserRole roleName;

    public AuthUserRoleDO(EnumAuthUserRole roleName) {
        this.roleName = roleName;
    }

    public AuthUserRoleDO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AuthUserRoleDO that = (AuthUserRoleDO) o;
        return Objects.equals(id, that.id) &&
                roleName == that.roleName;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
