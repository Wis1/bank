package com.wis1.bank.configuration;

import com.wis1.bank.controller.GenericValidator;
import com.wis1.bank.repository.AccountRepository;
import com.wis1.bank.repository.ClientRepository;
import com.wis1.bank.repository.EmployeeRepository;
import com.wis1.bank.repository.TransactionRepository;
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
    public AccountService accountService(AccountRepository accountRepository, ClientRepository clientRepository, TransactionRepository transactionRepository) {
        return new AccountService(accountRepository, clientRepository, transactionRepository);
    }

    @Bean
    public ClientService clientService(ClientRepository clientRepository) {
        return new ClientService(clientRepository);
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
