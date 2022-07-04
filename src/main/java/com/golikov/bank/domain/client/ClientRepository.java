package com.golikov.bank.domain.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);

    List<Client> findAllByOrderById();
}
