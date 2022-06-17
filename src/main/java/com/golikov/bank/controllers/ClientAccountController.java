package com.golikov.bank.controllers;


import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientTransaction;
import com.golikov.bank.entity.DepositAccount;
import com.golikov.bank.repository.DepositAccountRepository;
import com.golikov.bank.service.BankService;
import com.golikov.bank.service.ClientService;
import com.golikov.bank.utils.ProxyDepositAccount;
import com.golikov.bank.validator.ClienBalanceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


// Контроллер для личного кабинета клиента
@Controller
public class ClientAccountController {
    @Autowired
    BankService bankService;

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Autowired
    ClienBalanceValidator clienBalanceValidator;

    @Autowired
    ClientService clientService;

    MessageSource messageSource;

    MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);

    @RequestMapping("/account")
    public String account(@AuthenticationPrincipal Client client, Model model) {
        model.addAttribute("client",client);
        List<DepositAccount> depoAccList = clientService.findClientAccounts(client.getId());
        model.addAttribute("depoAccs", depoAccList);
        model.addAttribute("clientTransaction", new ClientTransaction());
        model.addAttribute("newDepoAcc", new DepositAccount());
        model.addAttribute("proxyDepositAccount", new ProxyDepositAccount());
        return "account";
    }

    @PostMapping("/up-balance")
    public String upBalance(@AuthenticationPrincipal Client client,
                            @ModelAttribute("clientTransaction") ClientTransaction clientTransaction){
        bankService.cardToBaLance(clientTransaction, client);
        return "redirect:/account";
    }

    @PostMapping("/create-deposit-account")
    public String createDepoAcc(@AuthenticationPrincipal Client client, @ModelAttribute("newDepoAcc") DepositAccount depositAccount) {
        bankService.createDepositAccount(client, depositAccount);
        return "redirect:/account";
    }

    @PostMapping("/up-account-balance")
    public String upDepositAccBalance(@AuthenticationPrincipal Client client,
                                      @ModelAttribute("proxyDepositAccount") ProxyDepositAccount proxyDepositAccount,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        //валидация переводимой сумыы денег
        clienBalanceValidator.setClient(client);
        clienBalanceValidator.setRedirectAttributes(redirectAttributes);
        clienBalanceValidator.validate(proxyDepositAccount.getAmount(), result);
        // если есть ошибки валидации то редирект с флэш сообщениями из ClienBalanceValidator
        if (result.hasErrors()){
           return  "redirect:/account";
        }
        // защита от подмены id счёта для зачисления
        else if (proxyDepositAccount.getDepositAccount()==null || proxyDepositAccount.getDepositAccount().getClient().getId()!=client.getId()){
            redirectAttributes.addFlashAttribute("unaccesableClientId", "Попытка подмены id счёта отклонена.");
        } else bankService.upDepositAccounBalance(client, proxyDepositAccount);
        return "redirect:/account";
    }


}
