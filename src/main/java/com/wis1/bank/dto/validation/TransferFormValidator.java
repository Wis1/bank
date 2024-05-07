package com.wis1.bank.dto.validation;

import com.wis1.bank.dto.form.TransferForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
public class TransferFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TransferForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransferForm t= (TransferForm) target;
        if (t.getSenderAccountNumber()== null || t.getSenderAccountNumber().isEmpty()) {
            errors.rejectValue("senderAccountNumber", "sender.required", "Sender account number is required");
        }
        if (t.getReceiverAccountNumber() == null || t.getReceiverAccountNumber().isEmpty()) {
            errors.rejectValue("receiverAccountNumber", "receiver.required", "Receiver account number is required");
        }
        if (t.getAmount() == null || t.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("amount", "amount.invalid", "Amount must be a positive number");
        }
        if (!(t.getAmount().scale()==0||t.getAmount().scale()==1||t.getAmount().scale()==2)) {
            errors.rejectValue("amount", "amount.invalid", "Amount must have a maximum of two decimal places");
        }
    }
}
