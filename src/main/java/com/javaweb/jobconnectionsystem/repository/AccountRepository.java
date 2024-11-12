package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,Long> {
    public boolean existsByUsername(String username);
    public Optional<AccountEntity> findByUsername(String username);

}
