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
@Table(name = "lead_expense")
public class LeadExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_expense_id")
    private Integer leadExpenseId;

    @ManyToOne
    @JoinColumn(name = "lead_id", nullable = false)
    @NotNull(message = "Lead is required")
    private Lead lead;

    @Column(name = "amount")
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @Column(name = "lead_expense_date")
    @NotNull(message = "Expense date is required")
    private LocalDate leadExpenseDate;

    // Getters and Setters
    public Integer getLeadExpenseId() {
        return leadExpenseId;
    }

    public void setLeadExpenseId(Integer leadExpenseId) {
        this.leadExpenseId = leadExpenseId;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getLeadExpenseDate() {
        return leadExpenseDate;
    }

    public void setLeadExpenseDate(LocalDate leadExpenseDate) {
        this.leadExpenseDate = leadExpenseDate;
    }
}