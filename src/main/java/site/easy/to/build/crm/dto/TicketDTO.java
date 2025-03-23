package site.easy.to.build.crm.dto;

import java.time.LocalDateTime;

import site.easy.to.build.crm.entity.Expense;

public class TicketDTO {
    
    private int ticketId;
    private String subject;
    private String status;
    private String priority;
    private Integer managerId;
    private Integer employeeId;
    private Integer customerId;
    private Expense expense;
    private LocalDateTime createdAt;

    public TicketDTO(int ticketId, String subject, String status, String priority, 
                    Integer managerId, Integer employeeId, Integer customerId, 
                    Expense expense, LocalDateTime createdAt) {
        this.ticketId = ticketId;
        this.subject = subject;
        this.status = status;
        this.priority = priority;
        this.managerId = managerId;
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.expense = expense;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public Expense getExpense() { return expense; }
    public void setExpense(Expense expense) { this.expense = expense; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}