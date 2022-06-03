package com.golikov.bank.repository;

import com.golikov.bank.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
