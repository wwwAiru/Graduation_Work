package com.golikov.bank.domain.client;

import com.golikov.bank.domain.account.Account;
import com.golikov.bank.domain.account.AccountRepository;
import com.golikov.bank.domain.client.dto.NewClient;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {

    private ClientRepository clientRepository;

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

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
