package com.wis1.bank.service;

import com.wis1.bank.repository.entity.Address;
import com.wis1.bank.repository.entity.Client;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClientSpecification implements Specification<Client> {

    private final String searchPhrase;

    public ClientSpecification(String searchPhrase) {
        this.searchPhrase = searchPhrase != null ? searchPhrase.toLowerCase() : "";
    }

    @Override
    public Predicate toPredicate(final Root<Client> root, final CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (searchPhrase.isEmpty()) {
            return criteriaBuilder.conjunction();
        } else {

            Expression<String> uuidAsString = criteriaBuilder.function("CAST", String.class, root.get("id"), criteriaBuilder.literal("CHAR"));

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Client.Fields.name)), "%" + searchPhrase + "%"
            ));
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Client.Fields.lastname)), "%" + searchPhrase + "%"
            ));
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Client.Fields.login)), "%" + searchPhrase + "%"
            ));

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Client.Fields.pesel)), "%" + searchPhrase + "%"
            ));

//            predicates.add(criteriaBuilder.like(
//                    criteriaBuilder.lower(uuidAsString), "%" + searchPhrase + "%"
//            ));

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Client.Fields.address).get(Address.Fields.city)), "%" + searchPhrase + "%"
            ));
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Client.Fields.address).get(Address.Fields.street)), "%" + searchPhrase + "%"
            ));
        }
        return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    }
}
