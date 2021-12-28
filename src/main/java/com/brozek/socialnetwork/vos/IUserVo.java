package com.brozek.socialnetwork.vos;


public interface IUserVo {

    String getEmail();

    String getPassword();

    String getFirstName();

    String getSurname();

    EnumUserRole getRole();

    enum EnumUserRole {
        admin,
        user
    }

}
