package com.wis1.bank.controller.json;

import com.wis1.bank.controller.GenericValidator;
import com.wis1.bank.dto.ClientDto;
import com.wis1.bank.dto.form.ClientForm;
import com.wis1.bank.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/json/client")
@RequiredArgsConstructor
public class ClientJsonController {

    private final ClientService clientService;
    private final GenericValidator genericValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(genericValidator);
    }

    @PostMapping(value = "/new")
    public ResponseEntity<String> createClient(@Validated @RequestBody ClientForm clientForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + bindingResult.getAllErrors());
        } else {
            ClientDto clientDto=clientService.createClient(clientForm);
            return ResponseEntity.ok("Client "+ clientDto.name()+" "+clientDto.lastname() +" has been added.");
        }
    }

    @GetMapping("/all")
    public List<ClientDto> getAllClients() {
        return clientService.getAllClient();
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<?> getClientDetails(@PathVariable UUID clientId) {
        ClientDto clientDto = clientService.getClientById(clientId);

        if (clientDto != null) {
            return ResponseEntity.ok(clientDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No client with id= '" + clientId + "' found");
        }
    }

    @PutMapping("/{clientId}/update")
    public ResponseEntity<Void> updateClientData(@PathVariable UUID clientId,
                                                 @Valid @RequestBody ClientForm clientForm) {
        clientService.updateClient(clientId, clientForm);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculateLoan")
    public List<ClientService.LoanSchedule> calculateLoan(@RequestParam double loanAmount, @RequestParam int loanTerm) {
        return clientService.calculateLoanSchedule(loanAmount, loanTerm);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok().build();
    }
}
