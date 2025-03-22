package site.easy.to.build.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.easy.to.build.crm.entity.LeadExpense;

@Repository
public interface LeadExpenseRepository extends JpaRepository<LeadExpense, Integer> {

    List<LeadExpense> findByLead_LeadId(int leadId); 
}