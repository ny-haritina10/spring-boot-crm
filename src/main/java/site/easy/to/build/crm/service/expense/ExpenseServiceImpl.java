package site.easy.to.build.crm.service.expense;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.ExpenseRepository;
import site.easy.to.build.crm.repository.LeadRepository;
import site.easy.to.build.crm.repository.TicketRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TicketRepository ticketRepository;
    private final LeadRepository leadRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository, TicketRepository ticketRepository, LeadRepository leadRepository) {
        this.expenseRepository = expenseRepository;
        this.ticketRepository = ticketRepository;
        this.leadRepository = leadRepository;
    }

    @Override
    public Expense findById(int id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Override
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public void delete(Expense expense) {
        expenseRepository.delete(expense);
    }

    @Override
    public void deleteById(int id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }    

    @Override
    public BigDecimal getTotalExpenses() {
        List<Expense> expenses = this.expenseRepository.findAll();
        BigDecimal totalExpenses = BigDecimal.ZERO;

        for (Expense expense : expenses) {
            totalExpenses = totalExpenses.add(new BigDecimal(expense.getAmount()));
        }

        return totalExpenses;
    }

    @Override
    public BigDecimal getTotalExpenseCustomer(Customer Customer) {
        BigDecimal result = BigDecimal.ZERO;

        List<Ticket> tickets = this.ticketRepository.findAll();
        List<Lead> leads = this.leadRepository.findAll();

        BigDecimal ticketExpense = BigDecimal.ZERO;
        BigDecimal leadExpense = BigDecimal.ZERO;


        for (Ticket ticket : tickets) {
            ticketExpense = ticketExpense.add(new BigDecimal(ticket.getExpense().getAmount()));
        }

        for (Lead lead : leads) {
            leadExpense = leadExpense.add(new BigDecimal(lead.getExpense().getAmount()));
        }
        
        result = result.add(ticketExpense);
        result = result.add(leadExpense);

        return result;
    }
}