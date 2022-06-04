package com.golikov.bank.repository;

import com.golikov.bank.entity.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
