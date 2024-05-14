package com.wis1.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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
