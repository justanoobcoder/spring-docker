package com.hiepnh.springdocker.service;

import com.hiepnh.springdocker.exception.BadRequestException;
import com.hiepnh.springdocker.exception.DuplicatedException;
import com.hiepnh.springdocker.exception.NotFoundException;
import com.hiepnh.springdocker.model.Customer;
import com.hiepnh.springdocker.repository.CustomerRepository;
import com.hiepnh.springdocker.viewmodel.customer.CustomerGetVm;
import com.hiepnh.springdocker.viewmodel.customer.CustomerPageGetVm;
import com.hiepnh.springdocker.viewmodel.customer.CustomerPostVm;
import com.hiepnh.springdocker.viewmodel.customer.CustomerPutVm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {
    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    public void getCustomerByName_shouldSuccess() {
        // Given
        String name = "customer";
        int page = 0;
        int size = 10;

        // When
        when(customerRepository.findAllByNameContains(name, PageRequest.of(page, size)))
                .thenReturn(Page.empty());

        // Stub
        CustomerPageGetVm customerPageGetVm = customerService.getCustomersByName(name, page, size);

        // Then
        assertThat(customerPageGetVm.customers()).hasSize(0);
    }

    @Test
    public void getCustomerById_whenCustomerIdInvalid_shouldThrowNotFoundException() {
        // Given
        Integer id = 1;

        // When
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // Stub
        NotFoundException exception = assertThrows(NotFoundException.class, () -> customerService.getCustomerById(id));

        // Then
        assertThat(exception.getMessage()).isEqualTo(String.format("Customer %s is not found", id));
    }

    @Test
    public void getCustomerById_whenCustomerIdValid_shouldSuccess() {
        // Given
        Integer id = 1;
        Customer customer = mock(Customer.class);

        // When
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customer.getPhone()).thenReturn(anyString());

        // Stub
        CustomerGetVm customerVm = customerService.getCustomerById(id);

        // Then
        assertThat(customerVm.phone()).isEqualTo(customer.getPhone());
    }

    @Test
    public void createCustomer_whenPhoneAlreadyExists_shouldThrowDuplicatedException() {
        // Given
        String phone = "123456789";
        CustomerPostVm customerVm = mock(CustomerPostVm.class);

        // When
        when(customerVm.phone()).thenReturn(phone);
        when(customerRepository.existsByPhone(phone)).thenReturn(true);

        // Stub
        DuplicatedException exception = assertThrows(DuplicatedException.class,
                () -> customerService.createCustomer(customerVm));

        // Then
        assertThat(exception.getMessage()).isEqualTo(String.format("Phone number %s already exists", phone));
    }

    @Test
    public void createCustomer_whenBirthdayInvalid_shouldThrowBadRequestException() {
        // Given
        String phone = "123456789";
        LocalDate birthday = LocalDate.now().minusYears(17);
        CustomerPostVm customerVm = mock(CustomerPostVm.class);

        // When
        when(customerVm.phone()).thenReturn(phone);
        when(customerVm.birthday()).thenReturn(birthday);
        when(customerRepository.existsByPhone(phone)).thenReturn(false);

        // Stub
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> customerService.createCustomer(customerVm));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Customer must be at least 18 years old");
    }

    @Test
    public void createCustomer_whenPhoneAndBirthdayValid_shouldSuccess() {
        // Given
        String name = "customer";
        String phone = "123456789";
        LocalDate birthday = LocalDate.now().minusYears(18);
        CustomerPostVm customerVm = mock(CustomerPostVm.class);
        Customer customer = mock(Customer.class);

        // When
        when(customerVm.name()).thenReturn(name);
        when(customerVm.phone()).thenReturn(phone);
        when(customerVm.birthday()).thenReturn(birthday);
        when(customer.getId()).thenReturn(1);
        when(customer.getName()).thenReturn(name);
        when(customer.getPhone()).thenReturn(phone);
        when(customer.getBirthday()).thenReturn(birthday);
        when(customerRepository.existsByPhone(phone)).thenReturn(false);
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

        // Stub
        CustomerGetVm customerGetVm = customerService.createCustomer(customerVm);

        // Then
        assertThat(customerGetVm.name()).isEqualTo(name);
        assertThat(customerGetVm.phone()).isEqualTo(phone);
        assertThat(customerGetVm.birthday()).isEqualTo(birthday);
    }

    @Test
    public void updateCustomer_whenCustomerIdInvalid_shouldThrowNotFoundException() {
        // Given
        Integer id = 1;
        CustomerPutVm customerVm = mock(CustomerPutVm.class);

        // When
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // Stub
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> customerService.updateCustomer(id, customerVm));

        // Then
        assertThat(exception.getMessage()).isEqualTo(String.format("Customer %s is not found", id));
    }

    @Test
    public void updateCustomer_whenPhoneAlreadyExists_shouldThrowDuplicatedException() {
        // Given
        Integer id = 1;
        String phone = "123456789";
        String newPhone = "987654321";
        CustomerPutVm customerVm = mock(CustomerPutVm.class);
        Customer customer = mock(Customer.class);

        // When
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));
        when(customerVm.phone()).thenReturn(newPhone);
        when(customer.getPhone()).thenReturn(phone);
        when(customerRepository.existsByPhone(newPhone)).thenReturn(true);

        // Stub
        DuplicatedException exception = assertThrows(DuplicatedException.class,
                () -> customerService.updateCustomer(id, customerVm));

        // Then
        assertThat(exception.getMessage()).isEqualTo(String.format("Phone number %s already exists", newPhone));
    }

    @Test
    public void updateCustomer_whenBirthdayInvalid_shouldThrowBadRequestException() {
        // Given
        Integer id = 1;
        String phone = "123456789";
        LocalDate birthday = LocalDate.now().minusYears(17);
        CustomerPutVm customerVm = mock(CustomerPutVm.class);
        Customer customer = mock(Customer.class);

        // When
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));
        when(customerVm.phone()).thenReturn(phone);
        when(customerVm.birthday()).thenReturn(birthday);
        when(customer.getPhone()).thenReturn(phone);
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);

        // Stub
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> customerService.updateCustomer(id, customerVm));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Customer must be at least 18 years old");
    }

    @Test
    public void updateCustomer_whenPhoneAndBirthdayValid_shouldSuccess() {
        // Given
        Integer id = 1;
        String name = "customer";
        String phone = "123456789";
        LocalDate birthday = LocalDate.now().minusYears(18);
        CustomerPutVm customerVm = mock(CustomerPutVm.class);
        Customer customer = mock(Customer.class);

        // When
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerVm.name()).thenReturn(name);
        when(customerVm.phone()).thenReturn(phone);
        when(customerVm.birthday()).thenReturn(birthday);
        when(customer.getId()).thenReturn(id);
        when(customer.getName()).thenReturn(name);
        when(customer.getPhone()).thenReturn(phone);
        when(customer.getBirthday()).thenReturn(birthday);
        when(customerRepository.existsByPhone(phone)).thenReturn(false);
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

        // Stub
        customerService.updateCustomer(id, customerVm);

        // Then
        verify(customer).setName(name);
        verify(customer).setPhone(phone);
        verify(customer).setBirthday(birthday);
        verify(customerRepository).saveAndFlush(customer);
    }

    @Test
    public void deleteCustomer_whenCustomerIdInvalid_shouldThrowNotFoundException() {
        // Given
        Integer id = 1;

        // When
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // Stub
        NotFoundException exception = assertThrows(NotFoundException.class, () -> customerService.deleteCustomer(id));

        // Then
        assertThat(exception.getMessage()).isEqualTo(String.format("Customer %s is not found", id));
    }

    @Test
    public void deleteCustomer_whenCustomerIdValid_shouldSuccess() {
        // Given
        Integer id = 1;
        Customer customer = mock(Customer.class);

        // When
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        // Stub
        customerService.deleteCustomer(id);

        // Then
        verify(customerRepository).delete(customer);
    }
}