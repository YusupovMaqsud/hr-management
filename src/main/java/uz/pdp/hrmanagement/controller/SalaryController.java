package uz.pdp.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagement.payload.ApiResponse;
import uz.pdp.hrmanagement.payload.SalaryDto;
import uz.pdp.hrmanagement.service.SalaryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    @Autowired
    SalaryService salaryService ;

    @GetMapping("/byUser/{Id}")
    public HttpEntity<?> getSalariesByMonth(@PathVariable UUID id) {
        ApiResponse apiResponse = salaryService.getSalarybyEmployee(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }

    @PostMapping("/paySalary")
    public HttpEntity<?> paySalary(@RequestBody SalaryDto salaryDto){
        ApiResponse apiResponse = salaryService.paySalary(salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }
}
