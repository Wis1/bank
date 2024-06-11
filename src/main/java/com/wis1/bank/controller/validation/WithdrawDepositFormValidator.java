package com.wis1.bank.controller.validation;

import com.wis1.bank.controller.dto.form.WithdrawDepositForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
public class WithdrawDepositFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return WithdrawDepositForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        WithdrawDepositForm form= (WithdrawDepositForm) target;
        if (form.getAccountNumber()== null || form.getAccountNumber().isEmpty()) {
            errors.rejectValue("accountNumber", "accountNumber.required", "Account number is required");
        }
        if (form.getClientId() == null) {
            errors.rejectValue("clientId", "clientId.required", "ClientId is required");
        }
        if (form.getSum() == null || form.getSum().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("sum", "sum.invalid", "Sum must be a positive number");
        }
        if (!(form.getSum().scale()==0||form.getSum().scale()==1||form.getSum().scale()==2)) {
            errors.rejectValue("sum", "sum.invalid", "Sum must have a maximum of two decimal places");
        }
    }
}
