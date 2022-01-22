package com.brozek.socialnetwork.repository;
import com.brozek.socialnetwork.dos.auth.AuthUserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface IUserJpaRepository extends JpaRepository<AuthUserDO, Integer> {

    boolean existsByEmail(String email);

    AuthUserDO findByEmail(String email);


    @Query(value = "select userName from auth_user " +
            "where email = ?1 ")
    Optional<String> findUserNameByEmail(String email);

    List<AuthUserDO> findByEmailIn(List<String> emails);
}
