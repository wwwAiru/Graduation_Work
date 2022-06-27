package com.golikov.bank.domain.client;

import com.golikov.bank.domain.client.account.Account;
import com.golikov.bank.domain.client.dto.NewClient;
import com.golikov.bank.domain.client.account.AccountRepository;
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

    public void createClient(NewClient newClient){
        Client client = new Client();
        client.setFirstName(newClient.getFirstName());
        client.setLastName(newClient.getLastName());
        client.setMiddleName(newClient.getMiddleName());
        client.setEmail(newClient.getEmail());
        client.setActive(true);
        client.setRoles(Collections.singleton(Role.USER));
        client.setPassword(passwordEncoder.encode(newClient.getPassword()));
        clientRepository.save(client);
    }

    public List<Account> findClientAccounts(Long id){
        return accountRepository.findByClientIdOrderById(id);
    }
}
