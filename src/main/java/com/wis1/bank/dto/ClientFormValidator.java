package com.wis1.bank.dto;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ClientFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ClientForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ClientForm c= (ClientForm) target;
        if(c.getPesel().length()!=11) {
            errors.rejectValue("pesel", "invalid.length", "PESEL must be 11 digits long");
        }
    }
}
