package com.brozek.socialnetwork.repository;

import com.brozek.socialnetwork.dos.auth.AuthUserRoleDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthRoleRepository extends JpaRepository<AuthUserRoleDO, Integer> {
}
