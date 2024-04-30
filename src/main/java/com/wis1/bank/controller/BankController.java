package com.wis1.bank.controller;

import com.wis1.bank.dto.*;
import com.wis1.bank.entity.Employee;
import com.wis1.bank.service.AccountService;
import com.wis1.bank.service.ClientService;
import com.wis1.bank.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/bank")
@RequiredArgsConstructor
@Validated
public class BankController {

    private final EmployeeService employeeService;
    private final AccountService accountService;
    private final ClientService clientService;

    @GetMapping("/client-form")
    public String showClientForm() {
        return "client-form";
    }

    @PostMapping(value = "/client/new")
    public ResponseEntity<String> createClient(@Valid @RequestBody ClientForm clientForm) {
        clientService.createClient(clientForm);
        return ResponseEntity.ok("Client has been added.");
    }

    @GetMapping("/client")
    public String getAllClients(Model model) {
        List<ClientDto> clients = clientService.getAllClient();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @GetMapping("/client/{clientId}")
    public String getClientDetails(@PathVariable Long clientId, Model model) {
        ClientDto client = clientService.getClientById(clientId);
        model.addAttribute("client", client);
        return "clientDetails";
    }

    @GetMapping("/transfer-form")
    public String showTransferForm(@RequestParam(name = "senderAccountNumber", required = false) String senderAccountNumber, Model model) {
        model.addAttribute("senderAccountNumber", senderAccountNumber);
        return "transfer-form";
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestParam String senderAccountNumber,
                                           @RequestParam String receiverAccountNumber,
                                           @RequestParam BigDecimal amount) {
        TransferForm transferForm= new TransferForm(senderAccountNumber, receiverAccountNumber, amount);
        accountService.transferMoney(transferForm);
        return ResponseEntity.ok("Transfer successful");
    }

    @PostMapping("/client/{clientId}/sum/{sum}")
    public ResponseEntity<?> withdraw(@RequestParam String accountNumber, @PathVariable Long clientId, @PathVariable BigDecimal sum) {
        WithdrawForm withdrawForm= new WithdrawForm(accountNumber, clientId, sum);
        accountService.withdraw(withdrawForm);
        return ResponseEntity.ok("Withdrawal successful");
    }

    @PutMapping("/client/{clientId}/sum/{sum}")
    public ResponseEntity<Void> depositMoney(@RequestParam String accountNumber,
                                             @PathVariable Long clientId,
                                             @PathVariable @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
                                             @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "Amount must have a maximum of two decimal places")
                                             BigDecimal sum) {
        WithdrawForm depositForm= new WithdrawForm(accountNumber,clientId,sum);
        clientService.depositMoney(depositForm);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@RequestBody EmployeeForm employeeForm) {
        employeeService.registerEmployee(employeeForm);
        return ResponseEntity.ok("Employee registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

        Employee employee = employeeService.login(username, password);
        if (employee != null) {
            return ResponseEntity.ok("redirect:/bank/menu");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateEmployeeData(@RequestParam String username,
                                                @RequestParam String password,
                                                @RequestBody Employee updatedEmployee) {
        employeeService.updateEmployeeData(username, password, updatedEmployee);
        return ResponseEntity.ok("Employee data updated successfully");
    }

    @PostMapping("/account/{accountNumber}/client/{clientId}")
    public ResponseEntity<?> addAccountToClient(@PathVariable Long clientId,
                                                @PathVariable String accountNumber,
                                                @RequestParam String username,
                                                @RequestParam String password) {
        Employee employee = employeeService.login(username, password);
        if (employee != null) {
            employeeService.addAccountToClient(clientId, accountNumber);
            System.out.println(accountNumber + " dodano do klienta o id: " + clientId);
            return ResponseEntity.ok("Add number account to client");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PutMapping("/client/{clientId}/update")
    public ResponseEntity<Void> updateClientData(@PathVariable Long clientId,
                                                 @Valid @RequestBody ClientForm clientForm) {
        clientService.updateClient(clientId, clientForm);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/loanCalculator")
    public String showLoanCalculator() {
        return "loanCalculator";
    }

    @PostMapping("/calculateLoan")
    public String calculateLoan(@RequestParam double loanAmount, @RequestParam int loanTerm, Model model) {
        model.addAttribute("loanSchedule", clientService.calculateLoanSchedule(loanAmount, loanTerm));
        return "loanCalculatorResult";
    }

    @DeleteMapping("/client/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/client/{clientId}/account/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long clientId, @PathVariable String accountNumber) {
        accountService.deleteAccount(clientId, accountNumber);
        return ResponseEntity.ok().build();
    }
}

