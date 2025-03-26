package site.easy.to.build.crm.service.duplicate;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerBudget;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.service.budget.CustomerBudgetService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

@Service
public class DuplicateService {

    private final CustomerRepository customerRepository;
    private final TicketService ticketService;
    private final LeadService leadService;
    private final CustomerBudgetService customerBudgetService;
    private ObjectMapper objectMapper = new ObjectMapper(); // json serialization

    public DuplicateService(CustomerRepository customerRepository, TicketService ticketService, 
                             LeadService leadService, CustomerBudgetService customerBudgetService) {
        this.customerRepository = customerRepository;
        this.ticketService = ticketService;
        this.leadService = leadService;
        this.customerBudgetService = customerBudgetService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); 
    }
    
    public void duplicateByCustomer(Integer idCustomer, String filename) {
        Customer cust = this.customerRepository.findById(idCustomer).get();
        
        Customer newCustomer = new Customer();
        newCustomer.setName("copy_" + cust.getName());
        newCustomer.setEmail("copy_" + cust.getEmail());
        newCustomer.setPosition(cust.getPosition());
        newCustomer.setPhone(cust.getPhone());
        newCustomer.setAddress(cust.getAddress());
        newCustomer.setCity(cust.getCity());
        newCustomer.setState(cust.getState());
        newCustomer.setCountry(cust.getCountry());
        newCustomer.setDescription(cust.getDescription());
        newCustomer.setUser(cust.getUser());

        List<Ticket> tickets = this.ticketService.findByCustomer(cust);
        List<Lead> leads = this.leadService.findByCustomer(cust);
        List<CustomerBudget> budgets = this.customerBudgetService.findByCustomerId(cust.getCustomerId());

        List<Ticket> duplicateTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            Ticket duplTicket = new Ticket();
            duplTicket.setCustomer(newCustomer);
            duplTicket.setSubject(ticket.getSubject());
            duplTicket.setStatus(ticket.getStatus());
            duplTicket.setPriority(ticket.getPriority());
            duplTicket.setManager(ticket.getManager());
            duplTicket.setExpense(ticket.getExpense());
            duplTicket.setDescription(ticket.getDescription());
            duplicateTickets.add(duplTicket);
        }

        List<Lead> duplicateLeads = new ArrayList<>();
        for (Lead lead : leads) {
            Lead duplLead = new Lead();
            duplLead.setName(lead.getName());
            duplLead.setCustomer(newCustomer);
            duplLead.setStatus(lead.getStatus());
            duplLead.setManager(lead.getManager());
            duplLead.setExpense(lead.getExpense());
            duplLead.setPhone(lead.getPhone());
            duplicateLeads.add(duplLead);
        }

        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("customer\n");
            writer.append("name,email,position,phone,address,city,state,country,description\n");
            writer.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n", 
                newCustomer.getName(), 
                newCustomer.getEmail(), 
                newCustomer.getPosition(), 
                newCustomer.getPhone(), 
                newCustomer.getAddress(), 
                newCustomer.getCity(), 
                newCustomer.getState(), 
                newCustomer.getCountry(), 
                newCustomer.getDescription()));
            writer.append("\n");

            writer.append("ticket\n");
            writer.append("subject,status,priority,manager,expense,description\n");
            for (Ticket ticket : duplicateTickets) {
                writer.append(String.format("%s,%s,%s,%s,%s,%s\n", 
                    ticket.getSubject(), 
                    ticket.getStatus(), 
                    ticket.getPriority(), 
                    ticket.getManager().getId(), 
                    ticket.getExpense(),
                    ticket.getDescription()));
            }
            writer.append("\n");

            writer.append("leads\n");
            writer.append("name,status,manager,expense,phone\n");
            for (Lead lead : duplicateLeads) {
                writer.append(String.format("%s,%s,%s,%s,%s\n", 
                    lead.getName(), 
                    lead.getStatus(), 
                    lead.getManager().getId(), 
                    ""+lead.getExpense().getAmount(),
                    lead.getPhone()));
            }
            writer.append("\n");

            writer.append("budgets\n");
            writer.append("budget_id,amount\n");
            for (CustomerBudget budget : budgets) {
                writer.append(String.format("%s,%s\n", 
                    budget.getBudgetId(), 
                    budget.getAmount()));
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void duplicateByCustomerToJson(Integer idCustomer, String filename) {
        Customer cust = this.customerRepository.findById(idCustomer).get();
        Customer newCustomer = new Customer();

        newCustomer.setName("copy_" + cust.getName());
        newCustomer.setEmail("copy_" + cust.getEmail());
        newCustomer.setPosition(cust.getPosition());
        newCustomer.setPhone(cust.getPhone());
        newCustomer.setAddress(cust.getAddress());
        newCustomer.setCity(cust.getCity());
        newCustomer.setState(cust.getState());
        newCustomer.setCountry(cust.getCountry());
        newCustomer.setDescription(cust.getDescription());
        newCustomer.setUser(cust.getUser());

        List<Ticket> tickets = this.ticketService.findByCustomer(cust);
        List<Lead> leads = this.leadService.findByCustomer(cust);
        List<CustomerBudget> budgets = this.customerBudgetService.findByCustomerId(cust.getCustomerId());

        List<Ticket> duplicateTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            Ticket duplTicket = new Ticket();
            duplTicket.setCustomer(newCustomer);
            duplTicket.setSubject(ticket.getSubject());
            duplTicket.setStatus(ticket.getStatus());
            duplTicket.setPriority(ticket.getPriority());
            duplTicket.setManager(ticket.getManager());
            duplTicket.setExpense(ticket.getExpense());
            duplTicket.setDescription(ticket.getDescription());
            duplicateTickets.add(duplTicket);
        }

        List<Lead> duplicateLeads = new ArrayList<>();
        for (Lead lead : leads) {
            Lead duplLead = new Lead();
            duplLead.setName(lead.getName());
            duplLead.setCustomer(newCustomer);
            duplLead.setStatus(lead.getStatus());
            duplLead.setManager(lead.getManager());
            duplLead.setExpense(lead.getExpense());
            duplLead.setPhone(lead.getPhone());
            duplicateLeads.add(duplLead);
        }

        new File(filename).getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(filename)) {
            Map<String, Object> data = new HashMap<>();
            
            Map<String, String> customerData = new HashMap<>();
            customerData.put("name", newCustomer.getName());
            customerData.put("email", newCustomer.getEmail());
            customerData.put("position", newCustomer.getPosition());
            customerData.put("phone", newCustomer.getPhone());
            customerData.put("address", newCustomer.getAddress());
            customerData.put("city", newCustomer.getCity());
            customerData.put("state", newCustomer.getState());
            customerData.put("country", newCustomer.getCountry());
            customerData.put("description", newCustomer.getDescription());
            data.put("customer", customerData);

            List<Map<String, Object>> ticketsData = new ArrayList<>();
            for (Ticket ticket : duplicateTickets) {
                Map<String, Object> ticketData = new HashMap<>();
                ticketData.put("subject", ticket.getSubject());
                ticketData.put("status", ticket.getStatus());
                ticketData.put("priority", ticket.getPriority());
                ticketData.put("managerId", ticket.getManager().getId());
                ticketData.put("expense", ticket.getExpense()); 
                ticketData.put("description", ticket.getDescription());
                ticketsData.add(ticketData);
            }
            data.put("tickets", ticketsData);

            List<Map<String, Object>> leadsData = new ArrayList<>();
            for (Lead lead : duplicateLeads) {
                Map<String, Object> leadData = new HashMap<>();
                leadData.put("name", lead.getName());
                leadData.put("status", lead.getStatus());
                leadData.put("managerId", lead.getManager().getId());
                leadData.put("expense", lead.getExpense().getAmount());
                leadData.put("phone", lead.getPhone());
                leadsData.add(leadData);
            }
            data.put("leads", leadsData);

            List<Map<String, Object>> budgetsData = new ArrayList<>();
            for (CustomerBudget budget : budgets) {
                Map<String, Object> budgetData = new HashMap<>();
                budgetData.put("budgetId", budget.getBudgetId());
                budgetData.put("amount", budget.getAmount());
                budgetsData.add(budgetData);
            }
            data.put("budgets", budgetsData);

            String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            writer.write(jsonContent);
            writer.flush();
            System.out.println("Wrote JSON content of length: " + jsonContent.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}