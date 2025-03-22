package site.easy.to.build.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.LeadExpense;
import site.easy.to.build.crm.service.lead.LeadExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;

@Controller
@RequestMapping("/employee/lead-expense")
public class LeadExpenseController {

    private final LeadExpenseService leadExpenseService;
    private final LeadService leadService;

    @Autowired
    public LeadExpenseController(LeadExpenseService leadExpenseService, LeadService leadService) {
        this.leadExpenseService = leadExpenseService;
        this.leadService = leadService;
    }

    @GetMapping("/show/{id}")
    public String showDetails(@PathVariable("id") int id, Model model) {
        LeadExpense leadExpense = leadExpenseService.findByLeadExpenseId(id);
        if (leadExpense == null) {
            return "error/not-found";
        }
        model.addAttribute("leadExpense", leadExpense);
        return "lead-expense/show-details";
    }

    @GetMapping("/all")
    public String showAllExpenses(Model model) {
        List<LeadExpense> expenses = leadExpenseService.findAll();
        model.addAttribute("expenses", expenses);
        return "lead-expense/show-expenses";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<Lead> leads = leadService.findAll(); // Fetch all leads
        model.addAttribute("leadExpense", new LeadExpense());
        model.addAttribute("leads", leads); // Add leads to the model
        return "lead-expense/create-expense";
    }

    @PostMapping("/create")
    public String createExpense(@ModelAttribute("leadExpense") LeadExpense leadExpense, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Lead> leads = leadService.findAll(); // Fetch all leads again in case of errors
            model.addAttribute("leads", leads);
            return "lead-expense/create-expense";
        }
        leadExpenseService.save(leadExpense);
        return "redirect:/employee/lead-expense/all";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        LeadExpense leadExpense = leadExpenseService.findByLeadExpenseId(id);
        if (leadExpense == null) {
            return "error/not-found";
        }
        List<Lead> leads = leadService.findAll(); // Fetch all leads
        model.addAttribute("leadExpense", leadExpense);
        model.addAttribute("leads", leads); // Add leads to the model
        return "lead-expense/create-expense";
    }

    @PostMapping("/update")
    public String updateExpense(@ModelAttribute("leadExpense") LeadExpense leadExpense, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Lead> leads = leadService.findAll(); // Fetch all leads again in case of errors
            model.addAttribute("leads", leads);
            return "lead-expense/create-expense";
        }
        leadExpenseService.save(leadExpense);
        return "redirect:/employee/lead-expense/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteExpense(@PathVariable("id") int id) {
        LeadExpense leadExpense = leadExpenseService.findByLeadExpenseId(id);
        if (leadExpense != null) {
            leadExpenseService.delete(leadExpense);
        }
        return "redirect:/employee/lead-expense/all";
    }
}