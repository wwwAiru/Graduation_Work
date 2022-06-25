package com.golikov.bank.service;

import com.golikov.bank.entity.Account;
import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientDTO;
import com.golikov.bank.entity.Role;
import com.golikov.bank.repository.AccountRepository;
import com.golikov.bank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Client findByEmail(String email){
        return clientRepository.findByEmail(email);
    }

    public void createClient(ClientDTO clientDTO){
        Client client = new Client();
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setMiddleName(clientDTO.getMiddleName());
        client.setEmail(clientDTO.getEmail());
        client.setActive(true);
        client.setRoles(Collections.singleton(Role.USER));
        client.setPassword(passwordEncoder.encode(clientDTO.getPassword()));
        clientRepository.save(client);
    }

    public List<Account> findClientAccounts(Long id){
        return accountRepository.findByClientIdOrderById(id);
    }
}
