package com.wis1.bank.controller;

import com.wis1.bank.dto.*;
import com.wis1.bank.dto.form.ClientForm;
import com.wis1.bank.dto.form.EmployeeForm;
import com.wis1.bank.dto.form.TransferForm;
import com.wis1.bank.dto.form.WithdrawDepositForm;
import com.wis1.bank.entity.Employee;
import com.wis1.bank.service.AccountService;
import com.wis1.bank.service.ClientService;
import com.wis1.bank.service.EmployeeService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bank/json")
@RequiredArgsConstructor
public class JsonController {

    private final EmployeeService employeeService;
    private final AccountService accountService;
    private final ClientService clientService;
    private final GenericValidator genericValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(genericValidator);
    }

    @PostMapping(value = "/client/new")
    public ResponseEntity<String> createClient(@Validated @RequestBody ClientForm clientForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + bindingResult.getAllErrors());
        } else {
            ClientDto clientDto=clientService.createClient(clientForm);
            return ResponseEntity.ok("Client "+ clientDto.name()+" "+clientDto.lastname() +" has been added.");
        }
    }
    @GetMapping("/account/{accountNumber}/history")
    public List<TransactionDto> getAccountHistory(@PathVariable String accountNumber) {
        return accountService.getAccountHistoryByAccountNumber(accountNumber);
    }


    @GetMapping("/client")
    public List<ClientDto> getAllClients() {
        return clientService.getAllClient();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getClientDetails(@PathVariable UUID clientId) {
        ClientDto clientDto = clientService.getClientById(clientId);

        if (clientDto != null) {
            return ResponseEntity.ok(clientDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No client with id= '" + clientId + "' found");
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@Validated @RequestBody TransferForm transferForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + bindingResult.getAllErrors());
        } else {
            try {
                accountService.transferMoney(transferForm);
                return ResponseEntity.ok("Transfer successful");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@Validated @RequestBody WithdrawDepositForm withdrawForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + bindingResult.getAllErrors());
        } else {
            try {
                accountService.withdraw(withdrawForm);
                return ResponseEntity.ok("Withdrawal successful");
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @PutMapping("/deposit")
    public ResponseEntity<?> depositMoney(@Validated @RequestBody WithdrawDepositForm depositForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error:" + bindingResult.getAllErrors());
        } else {
            try {
                accountService.depositMoney(depositForm);
                return ResponseEntity.ok().build();
            } catch (IllegalArgumentException | ConstraintViolationException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@Validated @RequestBody EmployeeForm employeeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error:" + bindingResult.getAllErrors());
        } else {
            try {
                employeeService.registerEmployee(employeeForm);
                return ResponseEntity.ok("Employee registered successfully");
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

        Employee employee = employeeService.login(username, password);
        if (employee != null) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateEmployeeData(@RequestParam String username,
                                                @RequestParam String password,
                                                @RequestBody Employee updatedEmployee) {
        employeeService.updateEmployeeData(username, password, updatedEmployee);
        return ResponseEntity.ok("Employee data updated successfully");
    }

    @PostMapping("/account/{accountNumber}/client/{clientId}")
    public ResponseEntity<?> addAccountToClient(@PathVariable UUID clientId,
                                                @PathVariable String accountNumber,
                                                @RequestParam String username,
                                                @RequestParam String password) {
        Employee employee = employeeService.login(username, password);
        if (employee != null) {
            employeeService.addAccountToClient(clientId, accountNumber);
            System.out.println(accountNumber + " added to client with id: " + clientId);
            return ResponseEntity.ok("Add number account to client");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PutMapping("/client/{clientId}/update")
    public ResponseEntity<Void> updateClientData(@PathVariable UUID clientId,
                                                 @Valid @RequestBody ClientForm clientForm) {
        clientService.updateClient(clientId, clientForm);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculateLoan")
    public List<ClientService.LoanSchedule> calculateLoan(@RequestParam double loanAmount, @RequestParam int loanTerm) {
        return clientService.calculateLoanSchedule(loanAmount, loanTerm);
    }

    @DeleteMapping("/client/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/client/{clientId}/account/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long clientId, @PathVariable String accountNumber) {
        accountService.deleteAccount(clientId, accountNumber);
        return ResponseEntity.ok().build();
    }
}

