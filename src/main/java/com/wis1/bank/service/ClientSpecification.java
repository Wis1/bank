package com.wis1.bank.service;

import com.wis1.bank.dto.ClientSearch;
import com.wis1.bank.entity.Client;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class ClientSpecification implements Specification<Client> {

    public ClientSearch clientSearch;

    public ClientSpecification(final ClientSearch clientSearch) {
        this.clientSearch= clientSearch;
    }

    @Override
    public Predicate toPredicate(final Root<Client> root, final CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(clientSearch.getName())) {
            predicates.add(criteriaBuilder.like(root.get(Client.Fields.name), "%" + clientSearch.getName().toLowerCase() + "%"));
        }
        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
