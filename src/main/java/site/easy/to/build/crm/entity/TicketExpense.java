package site.easy.to.build.crm.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ticket_expense")
public class TicketExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_expense_id")
    private Integer ticketExpenseId;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    @NotNull(message = "Ticket is required")
    private Ticket ticket;

    @Column(name = "amount")
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @Column(name = "ticket_expense_date")
    @NotNull(message = "Expense date is required")
    private LocalDate ticketExpenseDate;

    // Getters and Setters
    public Integer getTicketExpenseId() {
        return ticketExpenseId;
    }

    public void setTicketExpenseId(Integer ticketExpenseId) {
        this.ticketExpenseId = ticketExpenseId;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getTicketExpenseDate() {
        return ticketExpenseDate;
    }

    public void setTicketExpenseDate(LocalDate ticketExpenseDate) {
        this.ticketExpenseDate = ticketExpenseDate;
    }
}