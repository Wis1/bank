package com.wis1.bank.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Getter
@Setter
public class Address {

        @Id
        private UUID id = UUID.randomUUID();

        @Column
        private String city;

        @Column
        private String street;

        @Column
        private short buildingNumber;

        @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
        private Client client;

        public Address(String city, String street, short buildingNumber) {
                this.city = city;
                this.street = street;
                this.buildingNumber = buildingNumber;
        }
}
