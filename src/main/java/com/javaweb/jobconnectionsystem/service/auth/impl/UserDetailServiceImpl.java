package com.javaweb.jobconnectionsystem.service.auth.impl;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;
import com.javaweb.jobconnectionsystem.repository.AccountRepository;
import com.javaweb.jobconnectionsystem.repository.AdminRepository;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity account = accountRepository.findByUsername(username).get();
        String passwordFromDb = account.getPassword();
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        String Role;
        if(adminRepository.existsById(account.getId())){
            Role = "admin";
        }
        else if(companyRepository.existsById(account.getId())){
            Role = "company";
        }
        else if(applicantRepository.existsById(account.getId())){
            Role = "applicant";
        }else{
            throw new UsernameNotFoundException("Invalid account type");
        }
        return new org.springframework.security.core.userdetails.User(
                account.getUsername(),
                passwordFromDb,
                Collections.singletonList(new SimpleGrantedAuthority(Role))
        );
    }
    public String loadRoleByUsername(String username){
        AccountEntity account = accountRepository.findByUsername(username).get();
        String passwordFromDb = account.getPassword();
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        String Role;
        if(adminRepository.existsById(account.getId())){
            return "admin";
        }
        else if(companyRepository.existsById(account.getId())){
            return "company";
        }
        else if(applicantRepository.existsById(account.getId())){
            return "applicant";
        }else{
            throw new UsernameNotFoundException("Invalid account type");
        }
    }
}
