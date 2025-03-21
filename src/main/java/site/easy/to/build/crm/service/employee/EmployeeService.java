package site.easy.to.build.crm.service.employee;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import site.easy.to.build.crm.entity.Employee;
import site.easy.to.build.crm.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void importCSV(MultipartFile file) throws Exception {
        List<Employee> employees = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            // Skip header if exists
            boolean firstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] data = line.split(",");
                Employee employee = new Employee();
                
                employee.setUsername(data[0]);
                employee.setFirstName(data[1]);
                employee.setLastName(data[2]);
                employee.setEmail(data[3]);
                employee.setPassword(data[4]);
                employee.setProvider(data.length > 5 ? data[5] : null);
                
                employees.add(employee);
            }
            
            employeeRepository.saveAll(employees);
        }
    }
}