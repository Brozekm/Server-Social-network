package com.brozek.socialnetwork.repository;
import com.brozek.socialnetwork.dos.AuthUserDO;
import com.brozek.socialnetwork.dos.IUserEmailID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface IUserJpaRepository extends JpaRepository<AuthUserDO, Integer> {

    boolean existsByEmail(String email);

    AuthUserDO findByEmail(String email);

    @Query(value = "select id as id, email as email from auth_user where email in ?1")
    List<IUserEmailID> getUsersIds(List<String> emails);
}
