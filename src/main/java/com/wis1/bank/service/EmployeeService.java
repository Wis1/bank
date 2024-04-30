package com.wis1.bank.service;

import com.wis1.bank.dto.EmployeeForm;
import com.wis1.bank.entity.Account;
import com.wis1.bank.entity.Employee;
import com.wis1.bank.repository.AccountRepository;
import com.wis1.bank.repository.ClientRepository;
import com.wis1.bank.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmployeeService {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;

    private boolean authenticate(String username, String password) {

        return employeeRepository.findAll().stream()
                .anyMatch(employee ->
                        employee.getUsername().equals(username) && employee.getPassword().equals(password)
                );
    }

    public void registerEmployee(EmployeeForm employeeForm) {
        employeeRepository.save(EmployeeMapper.mapToEmployee(employeeForm));
    }

    public Employee login(String username, String password) {
        if (authenticate(username, password)) {
            return employeeRepository.findAll().stream()
                    .filter(employee -> employee.getUsername().equals(username) && employee.getPassword().equals(password))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        }
        return null;
    }

    public void updateEmployeeData(String username, String password, Employee updatedEmployee) {
        if (authenticate(username, password)) {

            employeeRepository.findAll().stream()
                    .filter(e -> e.getUsername().equals(username) && e.getPassword().equals(password))
                    .findAny()
                    .ifPresentOrElse((employee) -> {
                                employee.setUsername(updatedEmployee.getUsername());
                                employee.setPassword(updatedEmployee.getPassword());
                            }
                            , () -> {
                                throw new RuntimeException("Invalid username or password");
                            }
                    );
        } else {
            throw new RuntimeException("Unauthorized access. Please log in.");
        }
    }

    public void addAccountToClient(Long clientId, String accountNumber) {

        clientRepository.findById(clientId)
                .ifPresentOrElse(client -> accountRepository.save(new Account(accountNumber, client)), () -> {
                    throw new RuntimeException("Client with id: " + clientId + " not exist.");
                });
    }

    private static class EmployeeMapper {
        public static Employee mapToEmployee(EmployeeForm employeeForm) {
            return new Employee(employeeForm.getUsername(), employeeForm.getPassword());
        }
    }
}
