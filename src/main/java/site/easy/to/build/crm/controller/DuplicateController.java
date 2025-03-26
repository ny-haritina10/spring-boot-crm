package site.easy.to.build.crm.controller;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.duplicate.DuplicateService;

@Controller
public class DuplicateController {

    private final CustomerService customerService;
    private final DuplicateService duplicateService;

    public DuplicateController(CustomerService customerService, DuplicateService duplicateService) {
        this.customerService = customerService;
        this.duplicateService = duplicateService;
    }

    @GetMapping("/duplicate/{customerId}")
    public ResponseEntity<Resource> duplicate(@PathVariable("customerId") int customerId) {
        Customer customer = customerService.findByCustomerId(customerId);
        String filePath = "D:\\Studies\\ITU\\S6\\_Evaluation\\20-01-2025_CRM\\springboot-crm\\json\\export_" + customer.getCustomerId() + ".json";
        
        this.duplicateService.duplicateByCustomerToJson(customer.getCustomerId(), filePath);
        
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            System.err.println("File is empty or doesn't exist: " + filePath);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    
        Resource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=customer_duplicate_" + 
                   customer.getCustomerId() + ".json");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
    
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }
}