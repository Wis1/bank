package com.wis1.bank.controller.json;

import com.wis1.bank.controller.GenericValidator;
import com.wis1.bank.controller.dto.ClientDto;
import com.wis1.bank.controller.dto.form.EmployeeForm;
import com.wis1.bank.repository.entity.Employee;
import com.wis1.bank.service.ClientService;
import com.wis1.bank.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/json/employee")
@RequiredArgsConstructor
public class EmployeeJsonController {

    private final ClientService clientService;

    private final EmployeeService employeeService;
    private final GenericValidator genericValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(genericValidator);
    }

    @GetMapping
    public Page<ClientDto> filterClientByCriteria(String searchPhrase,
                                                  @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false, defaultValue = "lastname") String sortBy) {
        return clientService.filterByCriteria(searchPhrase, pageNo, pageSize, sortBy);
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
}
