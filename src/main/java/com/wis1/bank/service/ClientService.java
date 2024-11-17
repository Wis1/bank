package com.wis1.bank.service;

import com.wis1.bank.controller.dto.*;
import com.wis1.bank.controller.dto.form.AddressForm;
import com.wis1.bank.controller.dto.form.ClientForm;
import com.wis1.bank.exception.AuthenticationException;
import com.wis1.bank.repository.entity.Account;
import com.wis1.bank.repository.entity.Address;
import com.wis1.bank.repository.entity.Client;
import com.wis1.bank.repository.AddressRepository;
import com.wis1.bank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final String uri;
    private final MailService mailService;


    public ClientDto createClient(ClientForm clientForm) {
        addressRepository.save(AddressMapper.mapToAddress(clientForm.getAddress()));
        return ClientMapper.mapToClientDto(clientRepository.save(ClientMapper.mapToClient(clientForm)));
    }

    public Page<ClientDto> getAllClient() {
        return filterByCriteria("",0,10,"lastname");
    }

    public List<LoanSchedule> calculateLoan(double loanAmount, int loanTerm) {
        double interestRate = 0.05;
        return calculateMonthlyPayment(loanAmount, interestRate, loanTerm);
    }

    private List<LoanSchedule> calculateMonthlyPayment(double loanAmount, double interestRate, int loanTerm) {

        double monthlyRate = interestRate / 12;
        int n = loanTerm * 12;
        double principalPart = loanAmount / n;
        List<LoanSchedule> schedule= new ArrayList<>();

        for (int i = 0; i < n; i++) {
            double remainingPrincipal = loanAmount - i * principalPart;
            double interestPart = remainingPrincipal * monthlyRate;
            double monthlyPayment = principalPart + interestPart;
            schedule.add(new LoanSchedule(i + 1, BigDecimal.valueOf(monthlyPayment).setScale(2, RoundingMode.HALF_DOWN)));
        }
        return schedule;
    }

    public ClientDto getClientById(UUID clientId) {

        return clientRepository.findById(clientId)
                .map(ClientMapper::mapToClientDto)
                .orElse(null);
    }

    public void updateClient(UUID clientId, ClientForm clientForm) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);

        optionalClient.ifPresent(client -> {
            client.setName(clientForm.getName());
            client.setLastname(clientForm.getLastname());
            client.setLogin(clientForm.getLogin());
            client.setPesel(clientForm.getPesel());
            client.setAge(clientForm.getAge());
            client.setPhoneNumber(clientForm.getPhoneNumber());
            client.setEmail(clientForm.getEmail());
            client.setAddress(new Address(clientForm.getAddress().getCity(), clientForm.getAddress().getStreet(), clientForm.getAddress().getBuildingNumber()));
            clientRepository.save(client);
        });
    }

    public void deleteClient(UUID clientId) {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client with id " + clientId + " not found"))
                .getAccounts()
                .stream()
                .filter(account -> account.getBalance().compareTo(BigDecimal.ZERO) > 0)
                .findFirst()
                .ifPresent(account -> {
                    throw new RuntimeException("You must first transfer this client's money.");
                });

        clientRepository.deleteById(clientId);
    }

    public RateDto getActualRate() throws URISyntaxException {
        RestTemplate template = new RestTemplate();
        RateDto[] rateDtos = template.getForObject(new URI(uri), RateDto[].class);

        if (rateDtos ==  null) {
            return new RateDto("A", "000/A/NBP/0000", "0000-00-00", List.of());
        }

        return new RateDto(
                rateDtos[0].table(),
                rateDtos[0].number(),
                rateDtos[0].effectiveDate(),
                rateDtos[0]
                .rates()
                .stream()
                .filter(c -> c.code().equals("USD") || c.code().equals("EUR") || c.code().equals("GBP"))
                .toList());
    }

    public ClientDto registerNewClient(ClientForm clientForm) {
        Client newClient = ClientMapper.mapToClient(clientForm);

        Client savedClient = clientRepository.save(newClient);

        mailService.sendVerificationEmail(savedClient.getEmail());

        return ClientMapper.mapToClientDto(savedClient);
    }


    public static class ClientMapper {
        public static Client mapToClient(ClientForm clientForm) {
            return new Client(clientForm.getName(), clientForm.getLastname(), clientForm.getLogin(), clientForm.getPesel(), clientForm.getAge(), clientForm.getPhoneNumber(), clientForm.getEmail(), clientForm.getPassword(), AddressMapper.mapToAddress(clientForm.getAddress()));
        }

        public static ClientDto mapToClientDto(Client client) {
            return new ClientDto(client.getId(), client.getName(), client.getLastname(), client.getLogin(), client.getPesel(), client.getAge(), client.getPhoneNumber(), client.getEmail(), client.getPassword(), AddressMapper.mapToAdressDto(client.getAddress()), AccountMapper.mapToListAccountDto(client.getAccounts()));
        }

        public static List<ClientDto> mapToListClientDto(List<Client> clients) {
            return clients.stream()
                    .map(ClientMapper::mapToClientDto)
                    .toList();
        }
    }

    private static class AccountMapper {
        public static AccountDto mapToAccountDto(Account account) {
            return new AccountDto(account.getAccountNumber(), account.getBalance());
        }
        public static List<AccountDto> mapToListAccountDto(List<Account> accounts) {
            if (accounts != null) {
                return accounts.stream()
                        .map(AccountMapper::mapToAccountDto)
                        .toList();
            }
            return null;
        }
    }
    public static class AddressMapper {
        public static AddressDto mapToAdressDto(Address address) {
            if (address != null) {
                return new AddressDto(address.getCity(), address.getStreet(), address.getBuildingNumber());
            }
            return null;
        }
        public static Address mapToAddress(AddressForm addressForm) {
            return new Address(addressForm.getCity(), addressForm.getStreet(), addressForm.getBuildingNumber());
        }
    }

    public record LoanSchedule(int month, BigDecimal monthlyPayment) {
    }

    public ClientDto findByLoginAndAuthenticate(String login, String password) {
//
        ClientSearch clientSearch = new ClientSearch();
        clientSearch.setLogin(login);

        return filterByCriteria(login, 0, 1, "login")
                .stream()
                .findFirst()
                .filter(clientDto -> clientDto.password().equals(password))
                .orElseThrow(() -> new AuthenticationException("Login or password is wrong."));
    }

    public Page<ClientDto> filterByCriteria(String searchPhrase, final Integer pageNo, final Integer pageSize, final String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Specification<Client> specification = new ClientSpecification(searchPhrase);
        Page<Client> page = clientRepository.findAll(specification, paging);
        return page.map(ClientMapper::mapToClientDto);
    }
}
