package com.wis1.bank.repository;

import com.wis1.bank.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Page<Client> findAll(Specification<Client> spec, Pageable pageable);
}
