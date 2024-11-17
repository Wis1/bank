package com.wis1.bank.controller;

import com.wis1.bank.controller.dto.form.EmployeeForm;
import com.wis1.bank.repository.entity.Employee;
import com.wis1.bank.service.ClientService;
import com.wis1.bank.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ClientService clientService;

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchClients(
            @RequestParam(value = "q", required = false) String searchWord,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(clientService.filterByCriteria(searchWord, page, size, "lastname"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@RequestBody EmployeeForm employeeForm) {
        employeeService.registerEmployee(employeeForm);
        return ResponseEntity.ok("Employee registered successfully");
    }

    @PostMapping("/account/{accountNumber}/client/{clientId}")
    public ResponseEntity<?> addAccountToClient(@PathVariable UUID clientId,
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

    @PostMapping("/update")
    public ResponseEntity<?> updateEmployeeData(@RequestParam String username,
                                                @RequestParam String password,
                                                @RequestBody Employee updatedEmployee) {
        employeeService.updateEmployeeData(username, password, updatedEmployee);
        return ResponseEntity.ok("Employee data updated successfully");
    }
}
