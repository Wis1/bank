package com.wis1.bank.controller;

import com.wis1.bank.dto.*;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class GenericValidator implements Validator {

    @Override
    public boolean supports(Class<?> arg0) {
        return (ClientForm.class.equals(arg0) ||
                EmployeeForm.class.equals(arg0) ||
                TransferForm.class.equals(arg0) ||
                WithdrawForm.class.equals(arg0));
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof ClientForm) {
            ValidationUtils.invokeValidator( new ClientFormValidator(), target,errors);
        }
        if (target instanceof WithdrawForm) {
            ValidationUtils.invokeValidator( new WithdrawFormValidator(), target,errors);
        }
        if (target instanceof TransferForm) {
            ValidationUtils.invokeValidator( new TransferFormValidator(), target,errors);
        }
    }
}
