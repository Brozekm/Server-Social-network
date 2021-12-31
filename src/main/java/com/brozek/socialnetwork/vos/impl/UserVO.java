package com.brozek.socialnetwork.vos.impl;

import com.brozek.socialnetwork.vos.IUserVO;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record UserVO(
        @NotNull @Length(min = 4, max = 255) @Email String email,
        @NotNull String password,
        @NotNull String firstName,
        @NotNull String surname,
        EnumUserRole role
) implements IUserVO {

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public EnumUserRole getRole() {
        return role;
    }
}
