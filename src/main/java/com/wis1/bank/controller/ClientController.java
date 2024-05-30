package com.wis1.bank.controller;

import com.wis1.bank.dto.ClientDto;
import com.wis1.bank.dto.form.ClientForm;
import com.wis1.bank.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
@Validated
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/form")
    public String showClientForm() {
        return "client-form";
    }

    @PostMapping(value = "/new")
    public ResponseEntity<String> createClient(@Valid @RequestBody ClientForm clientForm) {
        clientService.createClient(clientForm);
        return ResponseEntity.ok("Client has been added.");
    }

    @GetMapping()
    public String getAllClients(Model model) {
        List<ClientDto> clients = clientService.getAllClient();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @GetMapping("/{clientId}")
    public String getClientDetails(@PathVariable UUID clientId, Model model) {
        ClientDto client = clientService.getClientById(clientId);
        model.addAttribute("client", client);
        return "clientDetails";
    }



    @PutMapping("/{clientId}/update")
    public ResponseEntity<Void> updateClientData(@PathVariable UUID clientId,
                                                 @Valid @RequestBody ClientForm clientForm) {
        clientService.updateClient(clientId, clientForm);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/client/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/loanCalculator")
    public String showLoanCalculator() {
        return "loanCalculator";
    }

    @PostMapping("/calculateLoan")
    public String calculateLoan(@RequestParam double loanAmount, @RequestParam int loanTerm, Model model) {
        model.addAttribute("loanSchedule", clientService.calculateLoanSchedule(loanAmount, loanTerm));
        return "loanCalculatorResult";
    }
}
