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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Controller
public class DepositController {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private BankDepositService depositService;

    @Autowired
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
    public String getInputForm( @RequestParam("depositName")String depositName,
    							@RequestParam("depositMinTerm")Integer depositMinTerm,
    							@RequestParam("depositMinAmount")Integer depositMinAmount,
    							@RequestParam("depositCurrency")String depositCurrency,
    							@RequestParam("depositInterestRate")Integer depositInterestRate,
    							@RequestParam("depositAddConditions")String depositAddConditions
                                ) {
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

    @RequestMapping("/filterByIdDeposit")
    public ModelAndView getFilterDepositById(@RequestParam("depositById")Long depositById
    ){
        BankDeposit deposit = depositService.getBankDepositById(depositById);
        List<BankDeposit> deposits = new ArrayList<BankDeposit>();
        deposits.add(deposit);
        LOGGER.debug("deposits.size = " + deposits.size());
        List<BankDepositor> depositors = depositorService.getBankDepositors();
        LOGGER.debug("depositors.size = " + depositors.size());

        ModelAndView view = new ModelAndView("depositsList", "deposits", deposits);
        view.addObject("depositors",depositors);

        return  view;
    }

    @RequestMapping("/filterByNameDeposit")
    public ModelAndView getFilterDepositByName(@RequestParam("depositByName")String depositByName
    ){
        BankDeposit deposit = depositService.getBankDepositByName(depositByName);
        List<BankDeposit> deposits = new ArrayList<BankDeposit>();
        deposits.add(deposit);
        LOGGER.debug("deposits.size = " + deposits.size());
        List<BankDepositor> depositors = depositorService.getBankDepositors();
        LOGGER.debug("depositors.size = " + depositors.size());

        ModelAndView view = new ModelAndView("depositsList", "deposits", deposits);
        view.addObject("depositors",depositors);

        return  view;
    }
    @RequestMapping(value={"/updateFormDeposit"}, method = RequestMethod.GET)
    public ModelAndView launchUpdateForm(RedirectAttributes redirectAttributes,
                                         @RequestParam("depositId")Long depositId
    ) {

        try {
            return new ModelAndView("updateFormDeposit", "deposit", depositService.getBankDepositById(depositId));

        }catch(Exception e) {
            LOGGER.debug(e);
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/depositsList");
        }
    }

    @RequestMapping(value={"/updateFormDeposit"}, method = RequestMethod.POST)
    public ModelAndView getUpdateForm(RedirectAttributes redirectAttributes,
                                      @RequestParam("depositId") Long depositId,
                                      @RequestParam("depositName")String depositName,
                                      @RequestParam("depositMinTerm")Integer depositMinTerm,
                                      @RequestParam("depositMinAmount")Integer depositMinAmount,
                                      @RequestParam("depositCurrency")String depositCurrency,
                                      @RequestParam("depositInterestRate")Integer depositInterestRate,
                                      @RequestParam("depositAddConditions")String depositAddConditions
                                     ) {
        BankDeposit deposit = new BankDeposit();

        deposit.setDepositId(depositId);
        deposit.setDepositName(depositName);
        deposit.setDepositMinTerm(depositMinTerm);
        deposit.setDepositMinAmount(depositMinAmount);
        deposit.setDepositCurrency(depositCurrency);
        deposit.setDepositInterestRate(depositInterestRate);
        deposit.setDepositAddConditions(depositAddConditions);

        try {
            depositService.updateBankDeposit(deposit);
            return new ModelAndView("redirect:/depositsList");
        }catch(Exception e) {
            LOGGER.debug(e);
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/updateFormDeposit", "depositId", depositId);
        }
    }

    @RequestMapping(value={"/deleteDeposit"}, method = RequestMethod.GET)
    public ModelAndView launchDeleteForm(RedirectAttributes redirectAttributes,
                                         @RequestParam("depositId")Long depositId
    ) {

        try {
            depositService.removeBankDeposit(depositId);
            return new ModelAndView("redirect:/depositsList");
        }catch(Exception e) {
            LOGGER.debug(e);
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/depositsList");
        }
    }

    @RequestMapping("/depositsList")
    public ModelAndView getListDepositsView() throws ParseException{
        List<BankDeposit> deposits = null;
        List<BankDepositor> depositors = null;
        ModelAndView view = new ModelAndView("depositsList");
        try {
            deposits = depositService.getBankDeposits();
            view.addObject("deposits",deposits);
        } catch(IllegalArgumentException e) {
            return new ModelAndView("inputFormDeposit", "deposit", new BankDeposit());
        } catch(NullPointerException e) {
            return new ModelAndView("inputFormDeposit", "deposit", new BankDeposit());
        }
        try {
            depositors = depositorService.getBankDepositors();
            view.addObject("depositors", depositors);
        } catch(IllegalArgumentException e) {
            return view;
        } catch(NullPointerException e) {
            return view;
        }
        return view;
    }

}