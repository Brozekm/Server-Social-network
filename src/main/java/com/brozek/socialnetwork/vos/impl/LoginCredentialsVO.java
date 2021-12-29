package com.brozek.socialnetwork.vos.impl;

import com.brozek.socialnetwork.vos.ILoginCredentialsVO;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record LoginCredentialsVO(
        @NotNull @Length(min = 4, max = 255) @Email String email,
        @NotNull String password
) implements ILoginCredentialsVO {
    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
