package com.api.bankApi.infra.repository;

import com.api.bankApi.infra.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    UserDetails findByLogin(String login);
}
