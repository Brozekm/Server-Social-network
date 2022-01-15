package com.brozek.socialnetwork.repository;
import com.brozek.socialnetwork.dos.AuthUserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface IUserJpaRepository extends JpaRepository<AuthUserDO, Integer> {

    boolean existsByEmail(String email);

    AuthUserDO findByEmail(String email);

    AuthUserDO getAuthUserDOByEmail(String email);

    List<AuthUserDO> findByEmailIn(List<String> emails);
}
