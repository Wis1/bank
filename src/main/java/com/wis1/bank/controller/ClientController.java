package com.wis1.bank.controller;

import com.wis1.bank.controller.dto.ClientDto;
import com.wis1.bank.controller.dto.RateDto;
import com.wis1.bank.controller.dto.form.ClientForm;
import com.wis1.bank.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
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

    @GetMapping("/signup")
    public String showSignUpForm() {return "signUpForm"; }

    @PostMapping(value = "/new")
    public ResponseEntity<String> createClient(@Valid @RequestBody ClientForm clientForm) {
        clientService.createClient(clientForm);
        return ResponseEntity.ok("Client has been added.");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody ClientForm clientForm) {

        return ResponseEntity.ok().body(clientService.registerNewClient(clientForm));
    }

    @GetMapping()
    public String getAllClients(Model model) {
        Page<ClientDto> clients = clientService.getAllClient();
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
        model.addAttribute("loanSchedule", clientService.calculateLoan(loanAmount, loanTerm));
        return "loanCalculatorResult";
    }

    @GetMapping("/rate")
    public ResponseEntity<RateDto> showActualRate() throws URISyntaxException {
        return ResponseEntity.ok().body(clientService.getActualRate());
    }
}
