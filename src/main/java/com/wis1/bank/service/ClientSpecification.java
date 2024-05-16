package com.wis1.bank.service;

import com.wis1.bank.dto.ClientSearch;
import com.wis1.bank.entity.Address;
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
        if (!ObjectUtils.isEmpty(clientSearch.getLastname())) {
            predicates.add(criteriaBuilder.like(root.get(Client.Fields.lastname), "%" + clientSearch.getLastname().toLowerCase() + "%"));
        }
        if (!ObjectUtils.isEmpty(clientSearch.getPesel())) {
            predicates.add(criteriaBuilder.like(root.get(Client.Fields.pesel), "%" + clientSearch.getPesel().toLowerCase() + "%"));
        }
        if (!ObjectUtils.isEmpty(clientSearch.getAge())) {
            predicates.add(criteriaBuilder.equal(root.get(Client.Fields.age), clientSearch.getAge()));
        }
        if (!ObjectUtils.isEmpty(clientSearch.getAddress())) {
            if (!ObjectUtils.isEmpty(clientSearch.getAddress().getCity())) {
                String cityPattern = "%" + clientSearch.getAddress().getCity().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(root.get(Client.Fields.address).get(Address.Fields.city), cityPattern));
            }
            if (!ObjectUtils.isEmpty(clientSearch.getAddress().getStreet())) {
                String cityPattern =  "%" + clientSearch.getAddress().getStreet().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(root.get(Client.Fields.address).get(Address.Fields.street), cityPattern));
            }
        }
        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
