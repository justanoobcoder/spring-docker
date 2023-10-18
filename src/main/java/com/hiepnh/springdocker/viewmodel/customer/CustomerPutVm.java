package com.hiepnh.springdocker.viewmodel.customer;

import java.time.LocalDate;

public record CustomerPutVm(
        String name,
        String phone,
        LocalDate birthday
) {
}
