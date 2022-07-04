package com.golikov.bank.domain.account;


import com.golikov.bank.config.security.UserDetailsImpl;
import com.golikov.bank.domain.account.transaction.ClientTransaction;
import com.golikov.bank.domain.account.transaction.TransactionService;
import com.golikov.bank.domain.account.validator.ClientBalanceValidator;
import com.golikov.bank.domain.account.validator.TransferBalanceValidator;
import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.client.ClientService;
import com.golikov.bank.domain.investment.ClientInvestProd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


// Контроллер для личного кабинета клиента
@Controller()
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    TransactionService transactionService;


    @GetMapping("/account")
    public String account(@AuthenticationPrincipal UserDetailsImpl userDetails,
                          ClientTransaction clientTransaction,
                          Model model,
                          HttpSession session) {
        Client client = userDetails.getClient();
        model.addAttribute("client", client);
        List<Account> clientAccounts = clientService.findClientAccounts(client.getId());
        // добавил в сессию id всех аккаунтов пользователя для последующих проверок
        Set<Long> clientAccountIds = clientAccounts.stream().map(Account::getId).collect(Collectors.toSet());
        session.setAttribute("clientAccountIds", clientAccountIds);
        model.addAttribute("clientAccounts", clientAccounts);
        if (!model.containsAttribute("transaction")) {
            model.addAttribute("transaction", new ClientTransaction());
        }
        model.addAttribute("newAccount", new Account());
        model.addAttribute("account", new Account());
        return "account/index";
    }

    // пополнения баланса с карты
    @PostMapping("/up-balance")
    public String upBalance(@AuthenticationPrincipal UserDetailsImpl userDetails,
                            @ModelAttribute("transaction") @Valid ClientTransaction clientTransaction,
                            BindingResult result,
                            @ModelAttribute Account account,
                            RedirectAttributes redirectAttributes){
        Client client = userDetails.getClient();
        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transaction", result);
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
                            BindingResult result,
                            @ModelAttribute Account account,
                            RedirectAttributes redirectAttributes){
        Client client = userDetails.getClient();
        ClientBalanceValidator clientBalanceValidator = new ClientBalanceValidator(client, redirectAttributes, "output");
        clientBalanceValidator.validate(clientTransaction.getAmount());
        if (result.hasErrors() | clientBalanceValidator.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transaction", result);
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

    // перевод средств на аккаунт
    @PostMapping("/account/up-balance")
    public String upAccountBalance(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @ModelAttribute("account") Account account,
                                      @RequestParam(required = false) BigDecimal amount,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        Client client = userDetails.getClient();
        //валидация переводимой суммы денег
        ClientBalanceValidator clientBalanceValidator = new ClientBalanceValidator(client, redirectAttributes, "input");
        clientBalanceValidator.validate(amount);
        // если есть ошибки валидации то редирект с флэш сообщениями из ClientBalanceValidator
        if (clientBalanceValidator.hasErrors()){
           return  "redirect:/account";
        }
        // защита от подмены id счёта для зачисления, проверяется содержит ли Set id из формы
        if (account == null || !((Set<Long>)session.getAttribute("clientAccountIds")).contains(account.getId())){
            redirectAttributes.addFlashAttribute("accountValidationError", "Попытка подмены id счёта отклонена.");
        } else accountService.upAccountBalance(client, account.getId(), amount);
        session.removeAttribute("clientAccountIds");
        return "redirect:/account";
    }

    // вывод денег с акаунта на общий счёт с пересчётом в рубли
    @PostMapping("/account/withdraw/{id}")
    public String withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @PathVariable(name = "id", required = false) Account account,
                           @RequestParam(required = false) String amount,
                           RedirectAttributes redirectAttributes) {
        Client client = userDetails.getClient();
        // защита от попадания строки в поле для цифр
        BigDecimal amountDecimal = null;
        if (amount.matches("\\d+|\\d+\\.\\d+")){
            amountDecimal = new BigDecimal(amount);
        } else {
            redirectAttributes.addFlashAttribute("validationError", "Допускаются только числовые положительные значения");
            return "redirect:/account";
        }
        // защита от подмены id счёта вывода денег
        if (account == null || account.getClient().getId() != client.getId()){
            redirectAttributes.addFlashAttribute("validationError", "Попытка подмены id счёта отклонена.");
            return "redirect:/account";
        }
        // валидация выводимой суммы денег
        TransferBalanceValidator balanceValidator = new TransferBalanceValidator(account, redirectAttributes);
        balanceValidator.validate(amountDecimal);
        // если есть ошибки валидации то редирект с флэш сообщениями из TransferBalanceValidator
        if (balanceValidator.hasErrors()){
            return "redirect:/account";
        }
        accountService.withdrawMoney(client, account, amountDecimal);
        redirectAttributes.addFlashAttribute("success", "Денежные средства успешно выведены");
        return "redirect:/account";
    }

    @PostMapping("/account/investment/close/{id}")
    public String closeInvestment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @PathVariable(name = "id", required = false) ClientInvestProd investment,
                                  RedirectAttributes redirectAttributes) {
        Client client = userDetails.getClient();
        // защита от подмены id вклада
        List<Long> clientAccounts = client.getAccounts().stream().map(Account::getId).toList();
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
