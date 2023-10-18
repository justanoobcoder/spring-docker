package com.hiepnh.springdocker.viewmodel.customer;

import com.hiepnh.springdocker.model.Customer;

import java.time.LocalDate;

public record CustomerGetVm(Integer id, String name, String phone, LocalDate birthday) {
    public static CustomerGetVm fromEntity(Customer customer) {
        return new CustomerGetVm(customer.getId(), customer.getName(), customer.getPhone(), customer.getBirthday());
    }
}
