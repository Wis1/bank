package com.wis1.bank.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class TransactionLog {

    @Id
    @Column(unique = true)
    private UUID id=UUID.randomUUID();

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    private String type;

    private BigDecimal amount;

    private boolean success;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Client sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Client receiver;

    public TransactionLog(Date timestamp, String type, BigDecimal amount, Client sender, Client receiver, boolean success) {
        this.timestamp = timestamp;
        this.type = type;
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
        this.success = success;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", success=" + success +
                '}';
    }
}
