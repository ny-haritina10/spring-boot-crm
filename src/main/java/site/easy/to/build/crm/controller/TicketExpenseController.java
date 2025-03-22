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

import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.TicketExpense;
import site.easy.to.build.crm.service.expense.TicketExpenseService;
import site.easy.to.build.crm.service.ticket.TicketService;

@Controller
@RequestMapping("/employee/ticket-expense")
public class TicketExpenseController {

    private final TicketExpenseService ticketExpenseService;
    private final TicketService ticketService;

    @Autowired
    public TicketExpenseController(TicketExpenseService ticketExpenseService, TicketService ticketService) {
        this.ticketExpenseService = ticketExpenseService;
        this.ticketService = ticketService;
    }

    @GetMapping("/show/{id}")
    public String showDetails(@PathVariable("id") int id, Model model) {
        TicketExpense ticketExpense = ticketExpenseService.findByTicketExpenseId(id);
        if (ticketExpense == null) {
            return "error/not-found";
        }
        model.addAttribute("ticketExpense", ticketExpense);
        return "ticket-expense/show-details";
    }

    @GetMapping("/all")
    public String showAllExpenses(Model model) {
        List<TicketExpense> expenses = ticketExpenseService.findAll();
        model.addAttribute("expenses", expenses);
        return "ticket-expense/show-expenses";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<Ticket> tickets = ticketService.findAll();
        model.addAttribute("ticketExpense", new TicketExpense());
        model.addAttribute("tickets", tickets);
        return "ticket-expense/create-expense";
    }

    @PostMapping("/create")
    public String createExpense(@ModelAttribute("ticketExpense") TicketExpense ticketExpense, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Ticket> tickets = ticketService.findAll();
            model.addAttribute("tickets", tickets);
            return "ticket-expense/create-expense";
        }
        ticketExpenseService.save(ticketExpense);
        return "redirect:/employee/ticket-expense/all";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        TicketExpense ticketExpense = ticketExpenseService.findByTicketExpenseId(id);
        if (ticketExpense == null) {
            return "error/not-found";
        }
        List<Ticket> tickets = ticketService.findAll();
        model.addAttribute("ticketExpense", ticketExpense);
        model.addAttribute("tickets", tickets);
        return "ticket-expense/create-expense";
    }

    @PostMapping("/update")
    public String updateExpense(@ModelAttribute("ticketExpense") TicketExpense ticketExpense, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Ticket> tickets = ticketService.findAll();
            model.addAttribute("tickets", tickets);
            return "ticket-expense/create-expense";
        }
        ticketExpenseService.save(ticketExpense);
        return "redirect:/employee/ticket-expense/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteExpense(@PathVariable("id") int id) {
        TicketExpense ticketExpense = ticketExpenseService.findByTicketExpenseId(id);
        if (ticketExpense != null) {
            ticketExpenseService.delete(ticketExpense);
        }
        return "redirect:/employee/ticket-expense/all";
    }
}