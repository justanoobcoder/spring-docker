package com.hiepnh.springdocker.viewmodel.customer;

import java.util.List;

public record CustomerPageGetVm(
        List<CustomerGetVm> customers,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
}
