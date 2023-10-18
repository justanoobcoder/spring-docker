package com.hiepnh.springdocker.service;

import com.hiepnh.springdocker.exception.BadRequestException;
import com.hiepnh.springdocker.exception.DuplicatedException;
import com.hiepnh.springdocker.exception.NotFoundException;
import com.hiepnh.springdocker.model.Customer;
import com.hiepnh.springdocker.repository.CustomerRepository;
import com.hiepnh.springdocker.viewmodel.customer.CustomerGetVm;
import com.hiepnh.springdocker.viewmodel.customer.CustomerPostVm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {
    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
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
        when(customer.getPhone()).thenReturn("123456789");

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
}
