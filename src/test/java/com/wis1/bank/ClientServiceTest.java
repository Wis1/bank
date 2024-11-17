package com.wis1.bank;

import com.wis1.bank.controller.dto.AddressDto;
import com.wis1.bank.controller.dto.ClientDto;
import com.wis1.bank.controller.dto.form.AddressForm;
import com.wis1.bank.controller.dto.form.ClientForm;
import com.wis1.bank.repository.AddressRepository;
import com.wis1.bank.repository.ClientRepository;
import com.wis1.bank.repository.entity.Address;
import com.wis1.bank.repository.entity.Client;
import com.wis1.bank.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateClient() {
        // Given
        AddressForm addressForm = new AddressForm("City", "Street", (short) 1);
        ClientForm clientForm = new ClientForm("John", "Doe", "jode", "12345678901", (short) 30, "123456789", "john@gmail.com", "oiuoiu", addressForm);
        Address address = new Address("City", "Street", (short) 1);
        Client client = new Client("John", "Doe", "jode", "12345678901", (short) 30, "123456789", "john@gmail.com", "oiuoiu", address);
        client.setId(UUID.randomUUID());

        ClientDto clientDto = new ClientDto(UUID.randomUUID(), "John", "Doe","jode", "12345678901", (short) 30, "123456789","john@gmail.com", "oiuoiu", new AddressDto("City", "Street", (short) 1), null);

        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        try (MockedStatic<ClientService.ClientMapper> mockedStatic = Mockito.mockStatic(ClientService.ClientMapper.class)) {
            mockedStatic.when(() -> ClientService.ClientMapper.mapToClientDto(any(Client.class)))
                    .thenReturn(clientDto);
        }

        // When
        ClientDto result = clientService.createClient(clientForm);

        // Then
        assertNotNull(result);
        assertEquals("John", result.name());
        assertEquals("Doe", result.lastname());
        verify(addressRepository, times(1)).save(any(Address.class));
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testGetAllClient() {

        //Given

        Client client1= new Client("Pablo", "Picasso", "PaPi", "87964783", (short) 56, "777888999","pablo@picasso.com", "qwerty", new Address("Hamburg", "BerlinerStr", (short)54));
        Client client2= new Client("Marco", "Polo", "MaPo", "99994783", (short) 56, "777888999","marco@polo.pl", "qwerty", new Address("Hamburg", "BerlinerStr", (short)54));
        Client client3= new Client("Leonardo", "daVinci", "LeVi", "67864783", (short) 56, "777888999", "leonardo@davinci.pl", "qwerty", new Address("Hamburg", "BerlinerStr", (short)54));
        Client client4= new Client("Van", "Gogh", "VaGa", "00064783", (short) 56, "777888999","van@gogh.pl", "qwerty", new Address("Hamburg", "BerlinerStr", (short)54));
        Client client5= new Client("Rubens", "Baricello", "RuBa", "87664783", (short) 56, "777888999", "rubens@bariceloo.pl", "qwerty", new Address("Hamburg", "BerlinerStr", (short)54));
        List<Client> clients= List.of(client1,client2, client3,client4,client5);
        Page<Client> clientPage= new PageImpl<>(clients);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(clientPage);

        Page<ClientDto> result = clientService.getAllClient();

        // Then
        assertNotNull(result);
        assertEquals(5, result.getTotalElements());
        assertEquals("PaPi", result.getContent().get(0).login());
        assertEquals("MaPo", result.getContent().get(1).login());
    }
}