package com.wis1.bank.service;

import com.wis1.bank.dto.AccountDto;
import com.wis1.bank.dto.ClientDto;
import com.wis1.bank.dto.form.ClientForm;
import com.wis1.bank.entity.Account;
import com.wis1.bank.entity.Client;
import com.wis1.bank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;


    public ResponseEntity<?> createClient(ClientForm clientForm) {
        BindingResult result = new BeanPropertyBindingResult(clientForm, "clientForm");

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            ClientDto clientDto = ClientMapper.mapToClientDto(clientRepository.save(ClientMapper.mapToClient(clientForm)));
            return ResponseEntity.ok(clientDto);
        }
    }

    public List<ClientDto> getAllClient() {
        return ClientMapper.mapToListClientDto(clientRepository.findAll());
    }

    public BigDecimal calculateLoan(double loanAmount, int loanTerm) {
        double interestRate = 0.05;
        return BigDecimal.valueOf(calculateMonthlyPayment(loanAmount, interestRate, loanTerm));
    }

    private double calculateMonthlyPayment(double loanAmount, double interestRate, int loanTerm) {
        return loanAmount / (loanTerm * 12) * (1 + interestRate);
    }

    public ClientDto getClientById(Long clientId) {

        return clientRepository.findById(clientId)
                .map(ClientMapper::mapToClientDto)
                .orElse(null);
    }

    public void updateClient(Long clientId, ClientForm clientForm) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);

        optionalClient.ifPresent(client -> {
            client.setName(clientForm.getName());
            client.setLastname(clientForm.getLastname());
            client.setPesel(clientForm.getPesel());
            clientRepository.save(client);
        });
    }

    public List<LoanSchedule> calculateLoanSchedule(double loanAmount, int loanTerm) {

        BigDecimal monthlyPayment = calculateLoan(loanAmount, loanTerm).setScale(2, RoundingMode.HALF_DOWN);

        return IntStream.rangeClosed(1, loanTerm * 12)
                .mapToObj(month -> new LoanSchedule(month, monthlyPayment))
                .collect(Collectors.toList());
    }

    public void deleteClient(Long clientId) {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client with id " + clientId + " not found"))
                .getAccounts()
                .stream()
                .filter(account -> account.getBalance().compareTo(BigDecimal.ZERO) > 0)
                .findFirst()
                .ifPresent(account -> {
                    throw new RuntimeException("You must first transfer this client's money.");
                });

        clientRepository.deleteById(clientId);
    }

    private static class ClientMapper {
        public static Client mapToClient(ClientForm clientForm) {
            return new Client(clientForm.getName(), clientForm.getLastname(), clientForm.getPesel());
        }

        public static ClientDto mapToClientDto(Client client) {
            return new ClientDto(client.getId(), client.getName(), client.getLastname(), client.getPesel(), AccountMapper.mapToListAccountDto(client.getAccounts()));
        }

        public static List<ClientDto> mapToListClientDto(List<Client> clients) {
            return clients.stream()
                    .map(ClientMapper::mapToClientDto)
                    .toList();
        }
    }

    private static class AccountMapper {
        public static AccountDto mapToAccountDto(Account account) {
            return new AccountDto(account.getAccountNumber(), account.getBalance());
        }

        public static List<AccountDto> mapToListAccountDto(List<Account> accounts) {
            return accounts.stream()
                    .map(AccountMapper::mapToAccountDto)
                    .toList();
        }
    }

    public record LoanSchedule(int month, BigDecimal monthlyPayment) {
    }

}
