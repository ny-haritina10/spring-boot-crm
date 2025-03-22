package site.easy.to.build.crm.controller;

import java.math.BigDecimal;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.LeadExpense;
import site.easy.to.build.crm.service.alert.AlerteRateService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;

@Controller
@RequestMapping("/employee/lead-expense")
public class LeadExpenseController {    

    private final LeadExpenseService leadExpenseService;
    private final LeadService leadService;
    private final AlerteRateService alerteRateService;
    private final CustomerService customerService;


    @Autowired
    public LeadExpenseController(LeadExpenseService leadExpenseService, LeadService leadService, AlerteRateService alerteRateService, CustomerService customerService) {
        this.leadExpenseService = leadExpenseService;
        this.leadService = leadService;
        this.alerteRateService = alerteRateService;
        this.customerService = customerService;
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
    public String createExpense(@ModelAttribute("leadExpense") LeadExpense leadExpense, 
                            BindingResult bindingResult, Model model, 
                            RedirectAttributes redirectAttributes) 
    {
        if (bindingResult.hasErrors()) {
            List<Lead> leads = leadService.findAll();
            model.addAttribute("leads", leads);
            return "lead-expense/create-expense";
        }

        // Get customer budget and lead amount before saving
        Customer customer = leadExpense.getLead().getCustomer();
        BigDecimal budget = this.customerService.getTotalBudget(customer);
        
        // Calculate the total amount including the new expense
        BigDecimal currentLeadAmount = this.leadService.getLeadTotalAmount(leadExpense.getLead());
        BigDecimal newTotalAmount = currentLeadAmount.add(leadExpense.getAmount());

        if (alerteRateService.isBudgetExceeded(newTotalAmount, budget)) {
            redirectAttributes.addFlashAttribute("pendingExpense", leadExpense);
            redirectAttributes.addFlashAttribute("exceedsBudget", true);
            redirectAttributes.addFlashAttribute("currentAmount", currentLeadAmount);
            redirectAttributes.addFlashAttribute("budget", budget);
            return "redirect:/employee/lead-expense/confirm";
        } else {
            leadExpenseService.save(leadExpense);
            
            if (alerteRateService.isAlerteRateReached(newTotalAmount, budget)) {
                redirectAttributes.addFlashAttribute("alertMessage", "Alerte: Vous avez atteint " + 
                    alerteRateService.getLatestAlerteRatePercentage() + "% du budget.");
            }
            return "redirect:/employee/lead-expense/all";
        }
    }

    // Add new endpoint for confirmation page
    @GetMapping("/confirm")
    public String showConfirmationPage() {
        return "lead-expense/confirm-expense";
    }

    // Add new endpoint to handle confirmation
    @PostMapping("/confirm")
    public String confirmExpense(@ModelAttribute("leadExpense") LeadExpense leadExpense,
                                RedirectAttributes redirectAttributes) {
        // Save the expense after user confirmation
        leadExpenseService.save(leadExpense);
        redirectAttributes.addFlashAttribute("confirmationMessage", 
            "Expense saved despite exceeding the budget.");
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