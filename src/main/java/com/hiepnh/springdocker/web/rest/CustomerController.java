package com.hiepnh.springdocker.web.rest;

import com.hiepnh.springdocker.service.CustomerService;
import com.hiepnh.springdocker.viewmodel.customer.CustomerPageGetVm;
import com.hiepnh.springdocker.viewmodel.customer.CustomerGetVm;
import com.hiepnh.springdocker.viewmodel.customer.CustomerPostVm;
import com.hiepnh.springdocker.viewmodel.customer.CustomerPutVm;
import com.hiepnh.springdocker.viewmodel.error.ErrorVm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Operation(
            summary = "Get customer by id",
            description = "Get customer by id",
            tags = {"Customer APIs"},
            responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found customer",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomerGetVm.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorVm.class)
                    )
            )
    })
    @GetMapping("{id}")
    public ResponseEntity<CustomerGetVm> getCustomerById(@PathVariable Integer id) {
        CustomerGetVm customerVm = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerVm);
    }

    @Operation(
            summary = "Get customers by name",
            description = "Get customers by name",
            tags = {"Customer APIs"},
            responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found customers",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomerPageGetVm.class)
                    )
            )
    })
    @GetMapping(params = {"name", "page", "size"})
    public ResponseEntity<CustomerPageGetVm> getCustomersByName(String name, int page, int size) {
        CustomerPageGetVm customerPageGetVm = customerService.getCustomersByName(name, page, size);
        return ResponseEntity.ok(customerPageGetVm);
    }

    @Operation(
            summary = "Create customer",
            description = "Create customer",
            tags = {"Customer APIs"},
            responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created customer successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomerGetVm.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Phone number already exists",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorVm.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<CustomerGetVm> createCustomer(@RequestBody CustomerPostVm customerVm) {
        CustomerGetVm createdCustomerVm = customerService.createCustomer(customerVm);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomerVm);
    }

    @Operation(
            summary = "Update customer",
            description = "Update customer",
            tags = {"Customer APIs"},
            responses = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Updated customer successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorVm.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorVm.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorVm.class)
                    )
            )
    })
    @PutMapping("{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Integer id, @RequestBody CustomerPutVm customerVm) {
        customerService.updateCustomer(id, customerVm);
        return ResponseEntity.noContent().build();
    }
}
