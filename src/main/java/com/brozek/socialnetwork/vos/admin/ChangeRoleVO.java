package com.brozek.socialnetwork.vos.admin;

import com.brozek.socialnetwork.dos.auth.EnumAuthUserRole;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ChangeRoleVO {
    @NotNull
    private String user;

    @NotNull
    private EnumAuthUserRole role;

    public ChangeRoleVO() {
    }

    public ChangeRoleVO(String user, EnumAuthUserRole role) {
        this.user = user;
        this.role = role;
    }
}
