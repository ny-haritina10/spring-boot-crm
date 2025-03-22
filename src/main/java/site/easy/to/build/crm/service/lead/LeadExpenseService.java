package site.easy.to.build.crm.service.lead;

import java.util.List;

import site.easy.to.build.crm.entity.LeadExpense;

public interface LeadExpenseService {

    LeadExpense findByLeadExpenseId(int id);

    List<LeadExpense> findAll();

    List<LeadExpense> findByLeadId(int leadId);

    LeadExpense save(LeadExpense leadExpense);

    void delete(LeadExpense leadExpense);
}