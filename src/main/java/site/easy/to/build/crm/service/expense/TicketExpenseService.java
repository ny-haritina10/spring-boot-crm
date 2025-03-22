package site.easy.to.build.crm.service.expense;

import java.util.List;

import site.easy.to.build.crm.entity.TicketExpense;

public interface TicketExpenseService {
    TicketExpense findByTicketExpenseId(int id);
    List<TicketExpense> findAll();
    List<TicketExpense> findByTicketId(int ticketId);
    TicketExpense save(TicketExpense ticketExpense);
    void delete(TicketExpense ticketExpense);
}