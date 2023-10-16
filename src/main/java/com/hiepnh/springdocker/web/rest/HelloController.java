package com.hiepnh.springdocker.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Hello", description = "Hello API")
public class HelloController {

    @Operation(summary = "Say hello", description = "Say hello")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Say hello successfully"),
    })
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
