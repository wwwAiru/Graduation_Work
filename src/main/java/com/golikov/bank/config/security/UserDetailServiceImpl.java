package com.golikov.bank.config.security;

import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.client.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// реализация UserDetailsService для SpringSecurity
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetailsImpl userDetails = new UserDetailsImpl(new Client());
        userDetails.setClient(clientRepository.findByEmail(email));
        return userDetails;
    }
}
