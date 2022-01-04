package com.brozek.socialnetwork.dos.impl;

import com.brozek.socialnetwork.dos.IUserDO;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserDO implements IUserDO {
    private int id;
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

    public UserDO(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public UserDO(String email, String password, String firstName, String surname) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
    }

    public UserDO(int id, String email, String password, String firstName, String surname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
    }

    public UserDO(int id, String email, String password, String firstName, String surname, EnumUserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.role = role;
    }
}
