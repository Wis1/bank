package com.wis1.bank.controller.json;

import com.wis1.bank.controller.GenericValidator;
import com.wis1.bank.dto.TransactionLogDto;
import com.wis1.bank.dto.form.TransferForm;
import com.wis1.bank.dto.form.WithdrawDepositForm;
import com.wis1.bank.service.AccountService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/json/account")
@RequiredArgsConstructor
public class AccountJsonController {

    private final AccountService accountService;
    private final GenericValidator genericValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(genericValidator);
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

    @DeleteMapping("/client/{clientId}/account/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long clientId, @PathVariable String accountNumber) {
        accountService.deleteAccount(clientId, accountNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/account/{accountNumber}/history")
    public List<TransactionLogDto> getAccountHistory(@PathVariable String accountNumber) {
        return accountService.getAccountHistoryByAccountNumber(accountNumber);
    }
}
