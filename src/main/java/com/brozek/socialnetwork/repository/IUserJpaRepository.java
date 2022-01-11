package com.brozek.socialnetwork.repository;

import com.brozek.socialnetwork.dos.AuthUserDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserJpaRepository extends JpaRepository<AuthUserDO, Integer> {

    boolean existsByEmail(String email);

    AuthUserDO findByEmail(String email);

}
