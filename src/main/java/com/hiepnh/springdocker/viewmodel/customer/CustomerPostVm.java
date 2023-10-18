package com.hiepnh.springdocker.viewmodel.customer;

import java.time.LocalDate;

public record CustomerPostVm(String name, String phone, LocalDate birthday) {
}
