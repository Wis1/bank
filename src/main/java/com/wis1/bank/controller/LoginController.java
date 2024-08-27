package com.wis1.bank.controller;

import com.wis1.bank.controller.dto.ClientDto;
import com.wis1.bank.repository.entity.Employee;
import com.wis1.bank.repository.entity.Role;
import com.wis1.bank.service.ClientService;
import com.wis1.bank.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final EmployeeService employeeService;
    private final ClientService clientService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

        Employee employee = employeeService.login(username, password);
        if (employee != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("role", Role.EMPLOYEE);
            response.put("employeeData", employee);
            return ResponseEntity.ok(response);
        } else {

            ClientDto clientDto = clientService.findByLoginAndAuthenticate(username, password);
            if (clientDto != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("role", Role.CLIENT);
                response.put("clientData", clientDto);
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Nieprawidłowe dane logowania"));
    }
}
