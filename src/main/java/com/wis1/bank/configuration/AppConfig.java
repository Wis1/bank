package com.wis1.bank.configuration;

import com.wis1.bank.controller.GenericValidator;
import com.wis1.bank.repository.*;
import com.wis1.bank.service.AccountService;
import com.wis1.bank.service.ClientService;
import com.wis1.bank.service.EmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.wis1.bank.repository")
public class AppConfig {


    @Bean
    public AccountService accountService(AccountRepository accountRepository, ClientRepository clientRepository, TransactionLogRepository transactionLogRepository) {
        return new AccountService(accountRepository, clientRepository, transactionLogRepository);
    }

    @Bean
    public ClientService clientService(ClientRepository clientRepository, AddressRepository addressRepository) {
        return new ClientService(clientRepository, addressRepository);
    }

    @Bean
    public EmployeeService employeeService(ClientRepository clientRepository, EmployeeRepository employeeRepository, AccountRepository accountRepository) {
        return new EmployeeService(clientRepository, employeeRepository, accountRepository);
    }

    @Bean
    public GenericValidator genericValidator() {
        return new GenericValidator();
    }
}
