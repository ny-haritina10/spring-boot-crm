package site.easy.to.build.crm.util.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.repository.UserRepository;

public class RandomTicketGenerator {

    private static final Random random = new Random();

    // Possible values based on @Pattern annotations
    private static final String[] STATUSES = {
        "open", "assigned", "on-hold", "in-progress", "resolved", "closed", 
        "reopened", "pending-customer-response", "escalated", "archived"
    };
    
    private static final String[] PRIORITIES = {
        "low", "medium", "high", "closed", "urgent", "critical"
    };
    
    private static final String[] SUBJECTS = {
        "Login Issue", "Payment Failure", "Feature Request", "Bug Report",
        "Account Suspension", "Performance Problem", "Security Concern"
    };
    
    private static final String[] DESCRIPTION_TEMPLATES = {
        "User reported an issue with %s.",
        "Customer experienced %s during their session.",
        "Request for %s submitted by customer.",
        "System encountered %s error on last login attempt.",
        "Urgent: %s affecting multiple users."
    };

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    // Constructor to inject repositories
    public RandomTicketGenerator(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    // Generate a single random ticket using existing entities from DB
    public Ticket generateRandomTicket() throws IllegalStateException {
        List<User> users = userRepository.findAll();
        List<Customer> customers = customerRepository.findAll();

        if (users.isEmpty() || customers.isEmpty()) {
            throw new IllegalStateException("Cannot generate ticket: No users or customers found in the database.");
        }

        User manager = getRandomUser(users);
        User employee = getRandomUser(users);
        Customer customer = getRandomCustomer(customers);

        return createTicket(manager, employee, customer);
    }

    // Helper to create a ticket with given entities
    private Ticket createTicket(User manager, User employee, Customer customer) {
        Ticket ticket = new Ticket();
        
        ticket.setSubject(generateRandomSubject());
        ticket.setDescription(generateRandomDescription());
        ticket.setStatus(generateRandomStatus());
        ticket.setPriority(generateRandomPriority());
        ticket.setManager(manager);
        ticket.setEmployee(employee);
        ticket.setCustomer(customer);
        ticket.setCreatedAt(generateRandomCreatedAt());
        
        return ticket;
    }

    // Generate random subject
    private String generateRandomSubject() {
        return SUBJECTS[random.nextInt(SUBJECTS.length)];
    }

    // Generate random description
    private String generateRandomDescription() {
        String template = DESCRIPTION_TEMPLATES[random.nextInt(DESCRIPTION_TEMPLATES.length)];
        String detail = generateRandomDetail();
        return String.format(template, detail);
    }

    // Helper to generate random detail for description
    private String generateRandomDetail() {
        String[] details = {
            "authentication", "payment processing", "new dashboard feature",
            "unexpected crash", "slow response times", "unauthorized access"
        };
        return details[random.nextInt(details.length)];
    }

    // Generate random status
    private String generateRandomStatus() {
        return STATUSES[random.nextInt(STATUSES.length)];
    }

    // Generate random priority
    private String generateRandomPriority() {
        return PRIORITIES[random.nextInt(PRIORITIES.length)];
    }

    // Generate random created_at timestamp (within the last 30 days)
    private LocalDateTime generateRandomCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        int daysBack = random.nextInt(30); // Random day within last 30 days
        int hours = random.nextInt(24);
        int minutes = random.nextInt(60);
        return now.minusDays(daysBack).minusHours(hours).minusMinutes(minutes);
    }

    // Get a random user from the list
    private User getRandomUser(List<User> users) {
        return users.get(random.nextInt(users.size()));
    }

    // Get a random customer from the list
    private Customer getRandomCustomer(List<Customer> customers) {
        return customers.get(random.nextInt(customers.size()));
    }
}