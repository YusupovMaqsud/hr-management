package uz.pdp.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.hrmanagement.payload.ApiResponse;
import uz.pdp.hrmanagement.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping
    public HttpEntity<?> findAll(){
        ApiResponse apiResponse = employeeService.findAllEmployees();
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }
}
