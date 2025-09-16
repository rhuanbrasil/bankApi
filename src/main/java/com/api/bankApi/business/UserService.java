package com.api.bankApi.business;

import com.api.bankApi.business.dtos.UserRegisterDto;
import com.api.bankApi.business.exceptions.UserException;
import com.api.bankApi.business.mapstruct.UserMapper;
import com.api.bankApi.business.mapstruct.UserUpdaterMapper;
import com.api.bankApi.infra.entitys.Account;
import com.api.bankApi.infra.entitys.User;
import com.api.bankApi.infra.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserService {

    private final UserUpdaterMapper updaterMapper;
    private final UserMapper mapper;
    private final UserRepository repo;


    public void registerUser(UserRegisterDto dto) {
        User newUser = mapper.toUser(dto);
        Random random = new Random();
        long userId = random.nextLong() & Long.MAX_VALUE;
        newUser.setAccount(new Account(userId, new BigDecimal("0"), newUser));
        repo.save(newUser);
    }

    public void updateUser(UserRegisterDto dto, long id) {
        User user = findUserById(id);
        updaterMapper.updateUser(dto, user);
        repo.save(user);
    }

    public void deleteUser(long id) {
        repo.deleteById(id);
    }

    public User findUserById(long id) {return repo.findById(id).orElseThrow(() -> new UserException("Did not find user with id: " + id));}

}
