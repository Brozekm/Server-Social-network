package com.brozek.socialnetwork.dos.auth;


import lombok.Getter;

import javax.persistence.*;

@Entity(name = "auth_user")
@Getter
public class AuthUserDO {

    private static final String SEQUENCE_NAME = "auth_user_id_seq";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String USER_NAME = "user_name";
    private static final String ROLE = "role";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    private Integer id;

    @Column(name = EMAIL, nullable = false, unique = true)
    private String email;

    @Column(name = PASSWORD, nullable = false)
    private String password;

    @Column(name = USER_NAME, nullable = false, length = 20)
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(name = ROLE, nullable = false)
    private EnumAuthRole role;

    public AuthUserDO() {
    }

    public AuthUserDO(String email, String password, String userName, EnumAuthRole role) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.role = role;
    }
}