package com.golikov.bank.service;

import com.golikov.bank.entity.DepositAccount;
import com.golikov.bank.repository.ClientRepository;
import com.golikov.bank.repository.DepositAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService implements UserDetailsService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clientRepository.findByEmail(email);
    }

    public List<DepositAccount> findClientAccounts(Long id){
        return depositAccountRepository.findByClientIdOrderById(id);
    }
}
