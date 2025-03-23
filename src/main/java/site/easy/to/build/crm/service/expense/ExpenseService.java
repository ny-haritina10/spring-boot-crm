package site.easy.to.build.crm.service.expense;

import site.easy.to.build.crm.entity.Expense;

public interface ExpenseService {
    Expense findById(int id);
    Expense save(Expense expense);
    void delete(Expense expense);
    void deleteById(int id);
}