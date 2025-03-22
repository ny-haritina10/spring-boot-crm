package site.easy.to.build.crm.service.lead;

import java.util.List;

import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.LeadExpense;
import site.easy.to.build.crm.repository.LeadExpenseRepository;

@Service
public class LeadExpenseServiceImpl implements LeadExpenseService {

    private final LeadExpenseRepository leadExpenseRepository;

    public LeadExpenseServiceImpl(LeadExpenseRepository leadExpenseRepository) {
        this.leadExpenseRepository = leadExpenseRepository;
    }

    @Override
    public LeadExpense findByLeadExpenseId(int id) {
        return leadExpenseRepository.findById(id).orElse(null);
    }

    @Override
    public List<LeadExpense> findAll() {
        return leadExpenseRepository.findAll();
    }

    @Override
    public List<LeadExpense> findByLeadId(int leadId) {
        return leadExpenseRepository.findByLead_LeadId(leadId);
    }

    @Override
    public LeadExpense save(LeadExpense leadExpense) {
        return leadExpenseRepository.save(leadExpense);
    }

    @Override
    public void delete(LeadExpense leadExpense) {
        leadExpenseRepository.delete(leadExpense);
    }
}