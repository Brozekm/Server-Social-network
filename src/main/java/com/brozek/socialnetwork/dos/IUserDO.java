package com.brozek.socialnetwork.dos;

import java.util.UUID;

public interface IUserDO {

    UUID getId();

    String getEmail();

    String getPassword();

    String getFirstName();

    String getSurname();

    EnumUserRole getRole();

    enum EnumUserRole{
        admin,
        user
    }

}
