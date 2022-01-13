package com.brozek.socialnetwork.vos;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class EmailVO {

    @NotNull
    @Email
    String email;

    public EmailVO() {
    }

    public EmailVO(String email) {
        this.email = email;
    }
}
