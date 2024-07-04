package com.wis1.bank.service;

import com.wis1.bank.controller.dto.*;
import com.wis1.bank.controller.dto.form.AddressForm;
import com.wis1.bank.controller.dto.form.ClientForm;
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
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;


    public ClientDto createClient(ClientForm clientForm) {
        addressRepository.save(AddressMapper.mapToAddress(clientForm.getAddress()));
        return ClientMapper.mapToClientDto(clientRepository.save(ClientMapper.mapToClient(clientForm)));
    }

    public List<ClientDto> getAllClient() {
        return ClientMapper.mapToListClientDto(clientRepository.findAll());
    }

    public BigDecimal calculateLoan(double loanAmount, int loanTerm) {
        double interestRate = 0.05;
        return BigDecimal.valueOf(calculateMonthlyPayment(loanAmount, interestRate, loanTerm));
    }

    private double calculateMonthlyPayment(double loanAmount, double interestRate, int loanTerm) {
        return loanAmount / (loanTerm * 12) * (1 + interestRate);
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
            client.setPesel(clientForm.getPesel());
            clientRepository.save(client);
        });
    }

    public List<LoanSchedule> calculateLoanSchedule(double loanAmount, int loanTerm) {

        BigDecimal monthlyPayment = calculateLoan(loanAmount, loanTerm).setScale(2, RoundingMode.HALF_DOWN);

        return IntStream.rangeClosed(1, loanTerm * 12)
                .mapToObj(month -> new LoanSchedule(month, monthlyPayment))
                .collect(toList());
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
        RateDto[] rateDtos = template.getForObject(new URI("http://api.nbp.pl/api/exchangerates/tables/a/"), RateDto[].class);

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


    private static class ClientMapper {
        public static Client mapToClient(ClientForm clientForm) {
            return new Client(clientForm.getName(), clientForm.getLastname(), clientForm.getPesel(), clientForm.getAge(), clientForm.getPhoneNumber(), AddressMapper.mapToAddress(clientForm.getAddress()));
        }

        public static ClientDto mapToClientDto(Client client) {
            return new ClientDto(client.getId(), client.getName(), client.getLastname(), client.getPesel(), client.getAge(), client.getPhoneNumber(), AddressMapper.mapToAdressDto(client.getAddress()), AccountMapper.mapToListAccountDto(client.getAccounts()));
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
    private static class AddressMapper {
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

    public Page<ClientDto> filterByCriteria(ClientSearch clientSearch, final Integer pageNo, final Integer pageSize, final String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Specification<Client> specification = new ClientSpecification(clientSearch);
        Page<Client> page = clientRepository.findAll(specification, paging);
        return page.map(ClientMapper::mapToClientDto);
    }
}
