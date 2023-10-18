package com.hiepnh.springdocker.service;

import com.hiepnh.springdocker.exception.BadRequestException;
import com.hiepnh.springdocker.exception.DuplicatedException;
import com.hiepnh.springdocker.exception.NotFoundException;
import com.hiepnh.springdocker.model.Customer;
import com.hiepnh.springdocker.repository.CustomerRepository;
import com.hiepnh.springdocker.util.Constants;
import com.hiepnh.springdocker.viewmodel.customer.CustomerPageGetVm;
import com.hiepnh.springdocker.viewmodel.customer.CustomerGetVm;
import com.hiepnh.springdocker.viewmodel.customer.CustomerPostVm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerPageGetVm getCustomersByName(String name, int page, int size) {
        Page<Customer> customerPage = customerRepository.findAllByNameContains(name, PageRequest.of(page, size));
        List<CustomerGetVm> customerVmList = customerPage.getContent().stream().map(CustomerGetVm::fromEntity).toList();
        return new CustomerPageGetVm(
                customerVmList,
                customerPage.getNumber(),
                customerPage.getSize(),
                customerPage.getTotalElements(),
                customerPage.getTotalPages(),
                customerPage.isLast()
        );
    }

    public CustomerGetVm getCustomerById(Integer id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            throw new NotFoundException(Constants.CUSTOMER_NOT_FOUND, id);
        }
        return CustomerGetVm.fromEntity(customer);
    }

    public CustomerGetVm createCustomer(CustomerPostVm customerVm) {
        if (customerRepository.existsByPhone(customerVm.phone())) {
            throw new DuplicatedException(Constants.PHONE_NUMBER_ALREADY_EXISTS, customerVm.phone());
        }
        if (customerVm.birthday().isBefore(LocalDate.now().minusYears(18))) {
            throw new BadRequestException(Constants.CUSTOMER_MUST_BE_AT_LEAST_18_YEARS_OLD);
        }
        Customer customer = Customer.builder()
                .name(customerVm.name())
                .phone(customerVm.phone())
                .birthday(customerVm.birthday())
                .build();
        customer = customerRepository.saveAndFlush(customer);
        return CustomerGetVm.fromEntity(customer);
    }
}
