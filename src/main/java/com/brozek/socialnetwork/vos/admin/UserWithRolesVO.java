package com.brozek.socialnetwork.vos.admin;

import com.brozek.socialnetwork.dos.auth.EnumAuthUserRole;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserWithRolesVO {

    private String email;

    private String username;

    private Set<EnumAuthUserRole> roles;

    public UserWithRolesVO() {
    }

    public UserWithRolesVO(String email, String username, Set<EnumAuthUserRole> roles) {
        this.email = email;
        this.username = username;
        this.roles = roles;
    }
}
