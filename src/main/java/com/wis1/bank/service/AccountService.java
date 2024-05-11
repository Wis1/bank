package com.wis1.bank.service;

import com.wis1.bank.dto.ClientDtoToAccount;
import com.wis1.bank.dto.TransactionDto;
import com.wis1.bank.entity.Transaction;
import com.wis1.bank.dto.form.TransferForm;
import com.wis1.bank.dto.form.WithdrawDepositForm;
import com.wis1.bank.entity.Account;
import com.wis1.bank.entity.Client;
import com.wis1.bank.repository.AccountRepository;
import com.wis1.bank.repository.ClientRepository;
import com.wis1.bank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    public void transferMoney(TransferForm transferForm) {

        String senderAccountNumber = transferForm.getSenderAccountNumber();
        String receiverAccountNumber = transferForm.getReceiverAccountNumber();
        BigDecimal amount = transferForm.getAmount();

        Client sender = findClientByAccountNumber(senderAccountNumber);
        Client receiver = findClientByAccountNumber(receiverAccountNumber);

        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Sender or receiver client not found");
        }

        Account senderAccount = getAccountByNumber(sender, senderAccountNumber);
        BigDecimal senderBalance = senderAccount.getBalance();
        if (senderBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance in sender account");
        }
        senderAccount.setBalance(senderBalance.subtract(amount));

        Transaction transactionSender = new Transaction(new Date(), "Transfer", amount, sender, receiver);
        transactionRepository.save(transactionSender);
        senderAccount.addTransaction(transactionSender);
        accountRepository.save(senderAccount);


        Transaction transactionReceiver = new Transaction(new Date(), "Transfer", amount, sender, receiver);
        transactionRepository.save(transactionReceiver);
        Account receiverAccount = getAccountByNumber(receiver, receiverAccountNumber);
        BigDecimal receiverBalance = receiverAccount.getBalance();
        receiverAccount.setBalance(receiverBalance.add(amount));
        receiverAccount.addTransaction(transactionReceiver);
        accountRepository.save(receiverAccount);
    }

    private Client findClientByAccountNumber(String accountNumber) {

        return clientRepository.findAll().stream()
                .filter(client -> client.getAccounts().stream()
                        .anyMatch(account -> account.getAccountNumber().equals(accountNumber)))
                .findFirst()
                .orElse(null);
    }

    public void depositMoney(WithdrawDepositForm depositForm) {
        UUID clientId = depositForm.getClientId();
        String accountNumber = depositForm.getAccountNumber();
        BigDecimal sum = depositForm.getSum();

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client with id: " + clientId + " not exist."));

        if (client.getAccounts().isEmpty()) {
            throw new IllegalArgumentException("Client with id: " + clientId + " does not have any account.");
        }

        Account account = accountRepository.findByClientAndAccountNumber(client, accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Client hasn't account with number: " + accountNumber));
        BigDecimal newBalance = account.getBalance().add(sum);
        account.setBalance(newBalance);

        Transaction transaction = new Transaction(new Date(), "Deposit", sum, null, client);
        transactionRepository.save(transaction);
        account.addTransaction(transaction);
        accountRepository.save(account);
    }

    private Account getAccountByNumber(Client client, String accountNumber) {
        return client.getAccounts().stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Account not found for client"));
    }

    public List<TransactionDto> getAccountHistoryByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(Account::getHistory)
                .map(TransactionMapper::mapToDtoList)
                .orElseThrow(() -> new RuntimeException("There is any account with this number"));
    }


    @Transactional
    public void deleteAccount(Long clientId, String accountNumber) {
        accountRepository.findByAccountNumber(accountNumber)
                .filter(account -> account.getBalance().compareTo(BigDecimal.ZERO) > 0)
                .ifPresent(account -> {
                    throw new RuntimeException("musisz przelać pieniądze");
                });
        accountRepository.deleteByAccountNumber(accountNumber);
    }

    public void withdraw(WithdrawDepositForm form) {

        UUID clientId = form.getClientId();
        String accountNumber = form.getAccountNumber();
        BigDecimal amount = form.getSum();

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client with id: " + clientId + " not exist."));

        Account account = client.getAccounts().stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Client hasn't account with number: " + accountNumber));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Client with id: " + clientId + " does not have enough money.");
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        Transaction transaction = new Transaction(new Date(), "Withdraw", amount, client, null);
        transactionRepository.save(transaction);
        account.addTransaction(transaction);
        accountRepository.save(account);
    }

    private static class TransactionMapper {
        private static TransactionDto mapToDto(Transaction transaction) {
            return new TransactionDto(transaction.getId(), transaction.getTimestamp(), transaction.getType(), transaction.getAmount(), mapToClientDtoToAccount(transaction.getSender()), mapToClientDtoToAccount(transaction.getReceiver()));
        }

        private static List<TransactionDto> mapToDtoList(List<Transaction> transactions) {
            return transactions.stream()
                    .map(TransactionMapper::mapToDto)
                    .toList();
        }

        public static ClientDtoToAccount mapToClientDtoToAccount(Client client) {
            if (client != null)
                return new ClientDtoToAccount(client.getId(), client.getName(), client.getLastname(), client.getPesel());
            else return null;
        }
    }
}
