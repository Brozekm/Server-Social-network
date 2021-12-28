package com.brozek.socialnetwork.dos.impl;

import com.brozek.socialnetwork.dos.IUserDO;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDO implements IUserDO {
    private UUID id;
    private String email;
    private String password;
    private String firstName;
    private String surname;
    private EnumUserRole role;

    public UserDO(){
        this(null,null);
    }

    public UserDO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserDO(UUID id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public UserDO(UUID id, String email, String password, String firstName, String surname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
    }

    public UserDO(UUID id, String email, String password, String firstName, String surname, EnumUserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.role = role;
    }
}
