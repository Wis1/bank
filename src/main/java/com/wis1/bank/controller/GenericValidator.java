package com.wis1.bank.controller;

import com.wis1.bank.controller.dto.form.ClientForm;
import com.wis1.bank.controller.dto.form.EmployeeForm;
import com.wis1.bank.controller.dto.form.TransferForm;
import com.wis1.bank.controller.dto.form.WithdrawDepositForm;
import com.wis1.bank.controller.validation.ClientFormValidator;
import com.wis1.bank.controller.validation.EmployeeFormValidator;
import com.wis1.bank.controller.validation.TransferFormValidator;
import com.wis1.bank.controller.validation.WithdrawDepositFormValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class GenericValidator implements Validator {

    @Override
    public boolean supports(Class<?> arg0) {
        return (ClientForm.class.equals(arg0) ||
                EmployeeForm.class.equals(arg0) ||
                TransferForm.class.equals(arg0) ||
                WithdrawDepositForm.class.equals(arg0));
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof ClientForm) {
            ValidationUtils.invokeValidator(new ClientFormValidator(), target, errors);
        }
        if (target instanceof WithdrawDepositForm) {
            ValidationUtils.invokeValidator(new WithdrawDepositFormValidator(), target, errors);
        }
        if (target instanceof TransferForm) {
            ValidationUtils.invokeValidator(new TransferFormValidator(), target, errors);
        }
        if (target instanceof EmployeeForm) {
            ValidationUtils.invokeValidator(new EmployeeFormValidator(), target, errors);
        }
    }
}
