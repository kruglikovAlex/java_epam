package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class DepositController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BankDepositService depositService;
    private BankDepositorService depositorService;

//    @RequestMapping("/")
//    public String init() {
//        return "redirect:/depositsList";
//    }

    @RequestMapping("/inputFormDeposit")
    public ModelAndView launchInputForm() {
        return new ModelAndView("inputFormDeposit", "deposit", new BankDeposit());
    }

    @RequestMapping("/submitDataDeposit")
    public String getInputForm(@RequestParam("depositName")String depositName, 
    							@RequestParam("depositMinTerm")Integer depositMinTerm,
    							@RequestParam("depositMinAmount")Integer depositMinAmount,
    							@RequestParam("depositCurrency")String depositCurrency,
    							@RequestParam("depositInterestRate")Integer depositInterestRate,
    							@RequestParam("depositAddConditions")String depositAddConditions) {
        BankDeposit deposit = new BankDeposit();
        
        deposit.setDepositName(depositName);
        deposit.setDepositMinTerm(depositMinTerm);
        deposit.setDepositMinAmount(depositMinAmount);
        deposit.setDepositCurrency(depositCurrency);
        deposit.setDepositInterestRate(depositInterestRate);
        deposit.setDepositAddConditions(depositAddConditions);
        
        depositService.addBankDeposit(deposit);
        return "redirect:/depositsList";
        
    }

    @RequestMapping("/depositsList")
    public ModelAndView getListDepositsView() {
        List<BankDeposit> deposits = depositService.getBankDeposits();
        LOGGER.debug("deposits.size = " + deposits.size());
        ModelAndView view = new ModelAndView("depositsList","deposits", deposits);
        return view;
    }
}