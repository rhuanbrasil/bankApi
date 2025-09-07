package com.api.bankApi.infra.repository;

import com.api.bankApi.infra.entitys.Account;
import com.api.bankApi.infra.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findById(long id);
}
