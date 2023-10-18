package com.hiepnh.springdocker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiepnh.springdocker.exception.BadRequestException;
import com.hiepnh.springdocker.exception.DuplicatedException;
import com.hiepnh.springdocker.exception.NotFoundException;
import com.hiepnh.springdocker.service.CustomerService;
import com.hiepnh.springdocker.viewmodel.customer.CustomerGetVm;
import com.hiepnh.springdocker.viewmodel.customer.CustomerPostVm;
import com.hiepnh.springdocker.viewmodel.customer.CustomerPutVm;
import com.hiepnh.springdocker.web.rest.CustomerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void getCustomerById_whenCustomerIdInvalid_shouldReturnNotFound() throws Exception {
        // Given
        Integer id = 1;

        // When
        when(customerService.getCustomerById(id)).thenThrow(NotFoundException.class);

        // Then
        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCustomerById_whenCustomerIdValid_shouldReturnOk() throws Exception {
        // Given
        Integer id = 1;
        CustomerGetVm customerGetVm = mock(CustomerGetVm.class);

        // When
        when(customerService.getCustomerById(id)).thenReturn(customerGetVm);

        // Then
        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void createCustomer_whenPhoneAlreadyExits_shouldReturnConflict() throws Exception {
        // Given
        CustomerPostVm customerPostVm = mock(CustomerPostVm.class);
        ObjectMapper mapper = new ObjectMapper();

        // When
        when(customerService.createCustomer(any(CustomerPostVm.class))).thenThrow(DuplicatedException.class);

        // Then
        mockMvc.perform(
                post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(customerPostVm))
                ).andExpect(status().isConflict());
    }

    @Test
    public void createCustomer_whenBirthdayInvalid_shouldReturnBadRequest() throws Exception {
        // Given
        CustomerPostVm customerPostVm = mock(CustomerPostVm.class);
        ObjectMapper mapper = new ObjectMapper();

        // When
        when(customerService.createCustomer(any(CustomerPostVm.class))).thenThrow(BadRequestException.class);

        // Then
        mockMvc.perform(
                post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerPostVm))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void createCustomer_whenCustomerValid_shouldReturnCreated() throws Exception {
        // Given
        CustomerPostVm customerPostVm = mock(CustomerPostVm.class);
        CustomerGetVm customerGetVm = mock(CustomerGetVm.class);
        ObjectMapper mapper = new ObjectMapper();

        // When
        when(customerService.createCustomer(any(CustomerPostVm.class))).thenReturn(customerGetVm);

        // Then
        mockMvc.perform(
                post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerPostVm))
        ).andExpect(status().isCreated());
    }

    @Test
    public void updateCustomer_whenCustomerIdInvalid_shouldReturnNotFound() throws Exception {
        // Given
        Integer id = 1;
        CustomerPutVm customerPutVm = mock(CustomerPutVm.class);
        ObjectMapper mapper = new ObjectMapper();

        // When
        doThrow(NotFoundException.class).when(customerService).updateCustomer(anyInt(), any(CustomerPutVm.class));

        // Then
        mockMvc.perform(
                put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerPutVm))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void updateCustomer_whenPhoneAlreadyExits_shouldReturnConflict() throws Exception {
        // Given
        Integer id = 1;
        CustomerPutVm customerPutVm = mock(CustomerPutVm.class);
        ObjectMapper mapper = new ObjectMapper();

        // When
        doThrow(DuplicatedException.class).when(customerService).updateCustomer(anyInt(), any(CustomerPutVm.class));

        // Then
        mockMvc.perform(
                put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerPutVm))
        ).andExpect(status().isConflict());
    }

    @Test
    public void updateCustomer_whenBirthdayInvalid_shouldReturnBadRequest() throws Exception {
        // Given
        Integer id = 1;
        CustomerPutVm customerPutVm = mock(CustomerPutVm.class);
        ObjectMapper mapper = new ObjectMapper();

        // When
        doThrow(BadRequestException.class).when(customerService).updateCustomer(anyInt(), any(CustomerPutVm.class));

        // Then
        mockMvc.perform(
                put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerPutVm))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void updateCustomer_whenCustomerValid_shouldReturnNoContent() throws Exception {
        // Given
        Integer id = 1;
        CustomerPutVm customerPutVm = mock(CustomerPutVm.class);
        ObjectMapper mapper = new ObjectMapper();

        // When
        doNothing().when(customerService).updateCustomer(anyInt(), any(CustomerPutVm.class));

        // Then
        mockMvc.perform(
                put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerPutVm))
        ).andExpect(status().isNoContent());
    }
}
