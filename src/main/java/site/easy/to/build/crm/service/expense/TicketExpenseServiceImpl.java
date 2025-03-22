package site.easy.to.build.crm.service.expense;

import java.util.List;

import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.TicketExpense;
import site.easy.to.build.crm.repository.TicketExpenseRepository;

@Service
public class TicketExpenseServiceImpl implements TicketExpenseService {

    private final TicketExpenseRepository ticketExpenseRepository;

    public TicketExpenseServiceImpl(TicketExpenseRepository ticketExpenseRepository) {
        this.ticketExpenseRepository = ticketExpenseRepository;
    }

    @Override
    public TicketExpense findByTicketExpenseId(int id) {
        return ticketExpenseRepository.findById(id).orElse(null);
    }

    @Override
    public List<TicketExpense> findAll() {
        return ticketExpenseRepository.findAll();
    }

    @Override
    public List<TicketExpense> findByTicketId(int ticketId) {
        return ticketExpenseRepository.findByTicket_TicketId(ticketId);
    }

    @Override
    public TicketExpense save(TicketExpense ticketExpense) {
        return ticketExpenseRepository.save(ticketExpense);
    }

    @Override
    public void delete(TicketExpense ticketExpense) {
        ticketExpenseRepository.delete(ticketExpense);
    }
}