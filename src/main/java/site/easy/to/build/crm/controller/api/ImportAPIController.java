package site.easy.to.build.crm.controller.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerBudget;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.budget.CustomerBudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.expense.ExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService; 

@RestController
@RequestMapping("/api/dashboard")
public class ImportAPIController {

    private final CustomerService customerService;
    private final TicketService ticketService;
    private final LeadService leadService;
    private final CustomerBudgetService customerBudgetService;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final ExpenseService expenseService; 

    public ImportAPIController(CustomerService customerService, TicketService ticketService,
                               LeadService leadService, CustomerBudgetService customerBudgetService,
                               UserService userService, ExpenseService expenseService) {
        this.customerService = customerService;
        this.ticketService = ticketService;
        this.leadService = leadService;
        this.customerBudgetService = customerBudgetService;
        this.userService = userService;
        this.expenseService = expenseService; 
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/import_duplicate")
    public ResponseEntity<?> importApi(@RequestParam("export") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> jsonData = objectMapper.readValue(file.getBytes(), Map.class);

            Map<String, String> customerData = (Map<String, String>) jsonData.get("customer");
            Customer customer = new Customer();
            customer.setName(customerData.get("name"));
            customer.setEmail(customerData.get("email"));
            customer.setPhone(customerData.get("phone"));
            customer.setAddress(customerData.get("address"));
            customer.setCity(customerData.get("city"));
            customer.setState(customerData.get("state"));
            customer.setCountry(customerData.get("country"));
            customer.setDescription(customerData.get("description"));
            customer.setPosition(customerData.get("position"));
            customer.setUser(null); 

            Customer savedCustomer = customerService.save(customer);

            List<Map<String, Object>> ticketsData = (List<Map<String, Object>>) jsonData.get("tickets");
            for (Map<String, Object> ticketData : ticketsData) {
                Ticket ticket = new Ticket();

                ticket.setSubject((String) ticketData.get("subject"));
                ticket.setDescription((String) ticketData.get("description"));
                ticket.setStatus((String) ticketData.get("status"));
                ticket.setPriority((String) ticketData.get("priority"));
                ticket.setCustomer(savedCustomer);
                ticket.setManager(userService.findById((Integer) ticketData.get("managerId")));

                Map<String, Object> expenseData = (Map<String, Object>) ticketData.get("expense");
                if (expenseData != null) {
                    Expense expense = new Expense();
                    expense.setAmount((Double) expenseData.get("amount"));
                    
                    List<Integer> dateArray = (List<Integer>) expenseData.get("expenseDate");
                    expense.setExpenseDate(LocalDate.of(dateArray.get(0), dateArray.get(1), dateArray.get(2)));

                    Expense savedExpense = expenseService.save(expense); 
                    ticket.setExpense(savedExpense); 
                }

                ticketService.save(ticket);
            }

            List<Map<String, Object>> leadsData = (List<Map<String, Object>>) jsonData.get("leads");
            for (Map<String, Object> leadData : leadsData) {
                Lead lead = new Lead();

                lead.setName((String) leadData.get("name"));
                lead.setPhone((String) leadData.get("phone"));
                lead.setStatus((String) leadData.get("status"));
                lead.setCustomer(savedCustomer);
                lead.setManager(userService.findById((Integer) leadData.get("managerId")));

                Expense expense = new Expense();
                expense.setAmount((Double) leadData.get("expense"));
                expense.setExpenseDate(LocalDate.now()); 

                Expense savedExpense = expenseService.save(expense); 
                lead.setExpense(savedExpense);

                leadService.save(lead);
            }

            List<Map<String, Object>> budgetsData = (List<Map<String, Object>>) jsonData.get("budgets");
            for (Map<String, Object> budgetData : budgetsData) {
                CustomerBudget budget = new CustomerBudget();
                budget.setCustomer(savedCustomer);
                budget.setAmount(new BigDecimal((Double) budgetData.get("amount")));
                budget.setLabel("Imported Budget"); 
                budget.setTransactionDate(LocalDate.now()); 
                
                customerBudgetService.save(budget);
            }

            return new ResponseEntity<>("Successfully imported duplicate data", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error parsing file: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error importing data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}