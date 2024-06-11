package com.wis1.bank.controller;

import com.wis1.bank.controller.dto.form.TransferForm;
import com.wis1.bank.controller.dto.form.WithdrawDepositForm;
import com.wis1.bank.service.AccountService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

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
    public ResponseEntity<?> withdraw(@RequestParam String accountNumber, @PathVariable UUID clientId, @PathVariable BigDecimal sum) {
        WithdrawDepositForm withdrawForm= new WithdrawDepositForm(accountNumber, clientId, sum);
        accountService.withdraw(withdrawForm);
        return ResponseEntity.ok("Withdrawal successful");
    }

    @PutMapping("/client/{clientId}/sum/{sum}")
    public ResponseEntity<Void> depositMoney(@RequestParam String accountNumber,
                                             @PathVariable UUID clientId,
                                             @PathVariable @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
                                             @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "Amount must have a maximum of two decimal places")
                                             BigDecimal sum) {
        WithdrawDepositForm depositForm= new WithdrawDepositForm(accountNumber,clientId,sum);
        accountService.depositMoney(depositForm);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/client/{clientId}/account/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long clientId, @PathVariable String accountNumber) {
        accountService.deleteAccount(clientId, accountNumber);
        return ResponseEntity.ok().build();
    }
}
