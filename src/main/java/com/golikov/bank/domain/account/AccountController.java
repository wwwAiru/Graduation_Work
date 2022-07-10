package com.golikov.bank.domain.account;


import com.golikov.bank.config.security.UserDetailsImpl;
import com.golikov.bank.domain.account.dto.UpAccountBalanceFormDto;
import com.golikov.bank.domain.account.dto.WithdrawAccountBalanceFormDto;
import com.golikov.bank.domain.account.validator.UpAccountBalanceValidator;
import com.golikov.bank.domain.account.validator.WithdrawAccountBalanceValidator;
import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.client.ClientService;
import com.golikov.bank.domain.investment.ClientInvestProd;
import com.golikov.bank.domain.transaction.ClientTransaction;
import com.golikov.bank.domain.transaction.TransactionService;
import com.golikov.bank.domain.transaction.validator.ClientTransactionValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


// Контроллер для личного кабинета клиента
@Controller
@AllArgsConstructor
public class AccountController {

    private AccountService accountService;

    private ClientService clientService;

    private TransactionService transactionService;

    private ClientTransactionValidator clientTransactionValidator;

    private UpAccountBalanceValidator upAccountBalanceValidator;

    private WithdrawAccountBalanceValidator withdrawAccountBalanceValidator;

    @InitBinder("transaction")
    private void transactionValidator(WebDataBinder binder) {
        binder.addValidators(clientTransactionValidator);
    }

    @InitBinder("accountForm")
    private void upAccountBalanceValidator(WebDataBinder binder) {
        binder.addValidators(upAccountBalanceValidator);
    }

    @InitBinder("withdrawAccountForm")
    private void withdrawAccountBalanceValidator(WebDataBinder binder) {
        binder.addValidators(withdrawAccountBalanceValidator);
    }


    @GetMapping("/account")
    public String account(@AuthenticationPrincipal UserDetailsImpl userDetails,
                          Model model) {
        Client client = userDetails.getClient();
        model.addAttribute("client", client);
        List<Account> clientAccounts = clientService.findClientAccounts(client.getId());
        model.addAttribute("clientAccounts", clientAccounts);
        if (!model.containsAttribute("transaction")) {
            model.addAttribute("transaction", new ClientTransaction());
        }
        if (!model.containsAttribute("accountForm")) {
            model.addAttribute("accountForm", new UpAccountBalanceFormDto(clientAccounts));
        }
        if (!model.containsAttribute("withdrawAccountForm")) {
            model.addAttribute("withdrawAccountForm", new WithdrawAccountBalanceFormDto());
        }
        model.addAttribute("newAccount", new Account());
        return "account/index";
    }

    // пополнения баланса с карты
    @PostMapping("/up-balance")
    public String upBalance(@AuthenticationPrincipal UserDetailsImpl userDetails,
                            @ModelAttribute @Valid ClientTransaction clientTransaction,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){
        Client client = userDetails.getClient();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transaction", bindingResult);
            redirectAttributes.addFlashAttribute("transaction", clientTransaction);
            redirectAttributes.addFlashAttribute("upBalanceError", "upBalanceError");
            return "redirect:/account";
        }
        accountService.cardToBaLance(clientTransaction, client);
        return "redirect:/account";
    }

    @PostMapping("/withdraw-balance")
    public String withdrawBalance(@AuthenticationPrincipal UserDetailsImpl userDetails,
                            @ModelAttribute("transaction") @Valid ClientTransaction clientTransaction,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        Client client = userDetails.getClient();
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transaction", bindingResult);
            redirectAttributes.addFlashAttribute("transaction", clientTransaction);
            redirectAttributes.addFlashAttribute("withdrawBalanceError", "withdrawBalanceError");
            return "redirect:/account";
        }
        accountService.balanceToCard(clientTransaction, client);
        redirectAttributes.addFlashAttribute("success", "Денежные средства успешно выведены");
        return "redirect:/account";
    }

    // открытие ивест счёта
    @PostMapping("/account/create-account")
    public String createAccount(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @ModelAttribute("newAccount") Account account) {
        Client client = userDetails.getClient();
        accountService.createAccount(client, account);
        return "redirect:/account";
    }

    // перевод денег на счёт(аккаунт)
    @PostMapping("/account/up-balance")
    public String upAccountBalance(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @ModelAttribute("accountForm") @Valid UpAccountBalanceFormDto accountForm,
                                      BindingResult bindingResult,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        Client client = userDetails.getClient();
        accountForm.setAccounts(clientService.findClientAccounts(client.getId()));
        // если есть ошибки валидации то редирект с флэш сообщениями из upAccountBalanceValidator
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.accountForm", bindingResult);
            redirectAttributes.addFlashAttribute("accountForm", accountForm);
            redirectAttributes.addFlashAttribute("inputAmountError", "inputAmountError");
            return  "redirect:/account";
        }
        accountService.upAccountBalance(client, accountForm.getChosenAccount(), accountForm.getAmount());
        session.removeAttribute("clientAccountIds");
        return "redirect:/account";
    }

    // вывод денег со счёта(акаунта) на общий счёт с пересчётом в рубли
    @PostMapping("/account/withdraw/{id}")
    public String withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @PathVariable(name = "id", required = false) Account account,
                           @ModelAttribute("withdrawAccountForm") @Valid WithdrawAccountBalanceFormDto withdrawAccountForm,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        Client client = userDetails.getClient();
        // если есть ошибки валидации то редирект с флэш сообщениями из withdrawAccountBalanceValidator
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.withdrawAccountForm", bindingResult);
            redirectAttributes.addFlashAttribute("withdrawAccountForm", withdrawAccountForm);
            return "redirect:/account";
        }
        accountService.withdrawMoney(client, account, withdrawAccountForm.getAmount());
        redirectAttributes.addFlashAttribute("success", "Денежные средства успешно выведены");
        return "redirect:/account";
    }

    @PostMapping("/account/investment/close/{id}")
    public String closeInvestment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @PathVariable(name = "id", required = false) ClientInvestProd investment,
                                  RedirectAttributes redirectAttributes) {
        Client client = userDetails.getClient();
        // защита от подмены id вклада
        List<Long> clientAccounts = client.getAccounts().stream().map(Account::getId).collect(Collectors.toList());
        if (investment == null || !clientAccounts.contains(investment.getAccount().getId())){
            redirectAttributes.addFlashAttribute("validationError", "Некоррекный идентификатор");
        } else {
            accountService.closeInvestment(investment);
            redirectAttributes.addFlashAttribute("success", investment.getInvestProduct().getName() + " закрыт");
        }
        return "redirect:/account";
    }

    @GetMapping("account/transactions")
    public String clientTransactions(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     Model model){
        Client client = userDetails.getClient();
        model.addAttribute("transactions", transactionService.getClientTransactions(client));
        return "account/transactions";
    }

}
