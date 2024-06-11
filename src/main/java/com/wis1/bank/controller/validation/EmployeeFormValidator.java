package com.wis1.bank.controller.validation;

import com.wis1.bank.controller.dto.form.EmployeeForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmployeeFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EmployeeForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        EmployeeForm e= (EmployeeForm) target;
        if(e.getUsername().isEmpty()) {
            errors.rejectValue("username", "invalid.length", "Username cannot be empty");
        }
        if(e.getPassword().isEmpty()) {
            errors.rejectValue("password", "invalid.length", "Password cannot be empty");
        }
    }
}
