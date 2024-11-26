package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;
import com.javaweb.jobconnectionsystem.model.dto.LoginDTO;
import com.javaweb.jobconnectionsystem.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class AuthServiceImpl {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailserviceImpl userDetailservice;
    @Autowired
    private JwtUtils JwtUtils;

    public String login(LoginDTO loginDTO) {
        AccountEntity account = accountRepository.findByUsername(loginDTO.getUsername()).get();

        if (account == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = userDetailservice.loadUserByUsername(loginDTO.getUsername());
            return JwtUtils.generateToken(userDetails) ;
        } else {
            throw new BadCredentialsException("Username or password is incorrect");
        }

    }
}