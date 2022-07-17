package uz.pdp.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.hrmanagement.payload.ApiResponse;
import uz.pdp.hrmanagement.service.TurniketService;

@RestController
@RequestMapping("/api/turniket")
public class TurniketController {
    @Autowired
    TurniketService turniketService;

    @PostMapping
    public HttpEntity<?> enterToWork() {
        ApiResponse apiResponse = turniketService.enterToWork();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }

    @PutMapping
    public HttpEntity<?> exitFromWork(){
        ApiResponse apiResponse = turniketService.exitFromWork();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }
}
