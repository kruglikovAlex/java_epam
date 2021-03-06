package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;
import com.brest.bank.validator.BankDepositValidator;
import com.brest.bank.validator.BankDepositTypeEditor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.validation.BindingResult;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.WebDataBinder;

import org.springframework.beans.propertyeditors.CustomNumberEditor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

@RequestMapping("/deposits")
@Controller
public class DepositController {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private BankDepositValidator depositValidator;

    @Autowired
    private BankDepositService depositService;

    @Autowired
    private BankDepositorService depositorService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(BankDeposit.class, new BankDepositTypeEditor());
        binder.registerCustomEditor(Integer.class,null,new CustomNumberEditor(Integer.class,null,true));
    }

    @RequestMapping("/inputFormDeposit")
    public ModelAndView launchInputForm(ModelMap model) {
        model.put("deposit",new BankDeposit());
        return new ModelAndView("inputFormDeposit", "deposit", new BankDeposit(null,null,0,0,null,0,null));
    }

    @RequestMapping(value={"/submitDataDeposit"}, method=RequestMethod.POST)
    public String getInputForm( ModelMap model,@ModelAttribute("deposit")
                                BankDeposit deposit,
                                BindingResult result,
                                SessionStatus status
                                ) {
        LOGGER.debug("depositValidator.validate({},{})",deposit, result);

        depositValidator.validate(deposit, result);

        if (result.hasErrors()) {
            LOGGER.debug("depositValidator.validate({},{})",deposit, result);

            return  "inputFormDeposit";
        } else {
            status.setComplete();
        }
        LOGGER.debug("depositService.addBankDeposit({})",deposit);
        depositService.addBankDeposit(deposit);
        return "redirect:/deposits/";
        
    }

    @RequestMapping(value={"/filterByIdDeposit"}, method=RequestMethod.GET)
    public ModelAndView getFilterDepositById(@RequestParam("depositById") Long depositById
                                            ){
        LOGGER.debug("getFilterDepositById{}",depositById);

        ModelAndView view = new ModelAndView("depositsList");

        BankDeposit deposit = depositService.getBankDepositById(depositById);
        List<BankDeposit> deposits = new ArrayList<BankDeposit>();
        LOGGER.debug("deposits.size = " + deposits.size());

        List<Map> depositsAllDepositors = depositService.getBankDepositByIdAllDepositors(depositById);
        deposits.add(deposit);
        LOGGER.debug("depositsAllDepositors.size = " + depositsAllDepositors.size());

        List<BankDepositor> depositors = depositorService.getBankDepositorByIdDeposit(depositById);
        LOGGER.debug("depositors.size = " + depositors.size());

        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        BankDepositor depositor = depositorService.getBankDepositorsSummAmountByIdDeposit(depositById);
        depositorsSum.add(depositor);
        LOGGER.debug("depositorsSum.size = " + depositorsSum.size());

        view.addObject("deposits",depositsAllDepositors);
        view.addObject("depositors",depositors);
        view.addObject("depositorSum",depositorsSum);

        return  view;
    }

    @RequestMapping("/filterByIdDepositBetweenDateDeposit")
    public ModelAndView getFilterDepositByIdBetweenDateDeposit(@RequestParam("depositById")Long depositById,
                                                               @RequestParam("StartDateDeposit")String StartDateDeposit,
                                                               @RequestParam("EndDateDeposit")String EndDateDeposit
                                                            ) throws ParseException{
        LOGGER.debug("getFilterDepositByIdBetweenDateDeposit({}, {},{})",depositById, StartDateDeposit, EndDateDeposit);

        List<Map> depositsAllDepositors = depositService.getBankDepositByIdWithAllDepositorsBetweenDateDeposit(depositById, dateFormat.parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        LOGGER.debug("depositsAllDepositors.size = " + depositsAllDepositors.size());

        List<BankDepositor> depositors = depositorService.getBankDepositorByIdDepositBetweenDateDeposit(depositById, dateFormat.parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        LOGGER.debug("depositors.size = " + depositors.size());

        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        BankDepositor depositor = depositorService.getBankDepositorsSummAmountByIdDepositBetweenDateDeposit(depositById, dateFormat.parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        depositorsSum.add(depositor);
        LOGGER.debug("depositorsSum.size = " + depositorsSum.size());

        ModelAndView view = new ModelAndView("depositsList", "deposits", depositsAllDepositors);
        view.addObject("depositors",depositors);
        view.addObject("depositorSum",depositorsSum);

        return  view;
    }

    @RequestMapping("/filterByIdDepositBetweenDateReturnDeposit")
    public ModelAndView getFilterDepositByIdBetweenDateReturnDeposit(@RequestParam("depositById")Long depositById,
                                                                    @RequestParam("StartDateReturnDeposit")String StartDateDeposit,
                                                                    @RequestParam("EndDateReturnDeposit")String EndDateDeposit
                                                                    ) throws ParseException{
        LOGGER.debug("getFilterDepositByIdBetweenDateReturnDeposit({}, {},{})",depositById, StartDateDeposit, EndDateDeposit);

        List<Map> depositsAllDepositors = depositService.getBankDepositByIdWithAllDepositorsBetweenDateReturnDeposit(depositById, dateFormat.parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        LOGGER.debug("depositsAllDepositors.size = " + depositsAllDepositors.size());

        List<BankDepositor> depositors = depositorService.getBankDepositorByIdDepositBetweenDateReturnDeposit(depositById, dateFormat.parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        LOGGER.debug("depositors.size = " + depositors.size());

        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        BankDepositor depositor = depositorService.getBankDepositorsSummAmountByIdDepositBetweenDateReturnDeposit(depositById, dateFormat.parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        depositorsSum.add(depositor);
        LOGGER.debug("depositorsSum.size = " + depositorsSum.size());

        ModelAndView view = new ModelAndView("depositsList", "deposits", depositsAllDepositors);
        view.addObject("depositors",depositors);
        view.addObject("depositorSum",depositorsSum);

        return  view;
    }

    @RequestMapping("/filterByNameDeposit")
    public ModelAndView getFilterDepositByName(@RequestParam("depositByName")String depositByName
                                            ){
        //--- ??? add try
        LOGGER.debug("getFilterDepositByName({})", depositByName);

        BankDeposit deposit = depositService.getBankDepositByName(depositByName);
        LOGGER.debug("getBankDepositByName({})", deposit);
        List<Map> depositList = depositService.getBankDepositByNameAllDepositors(depositByName);
        LOGGER.debug("depositList.size = " + depositList.size());

        List<BankDepositor> depositors = depositorService.getBankDepositorByIdDeposit(deposit.getDepositId());
        LOGGER.debug("depositors.size = " + depositors.size());

        BankDepositor depositorSum = depositorService.getBankDepositorsSummAmountByIdDeposit(deposit.getDepositId());
        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        depositorsSum.add(depositorSum);
        LOGGER.debug("depositorsSum.size = " + depositorsSum.size());

        ModelAndView view = new ModelAndView("depositsList", "deposits", depositList);
        view.addObject("depositors",depositors);
        view.addObject("depositorSum",depositorsSum);
        return  view;
    }

 @RequestMapping("/filterByNameDepositBetweenDateDeposit")
    public ModelAndView getFilterDepositByNameBetweenDateDeposit(@RequestParam("depositByName")String depositByName,
                                                               @RequestParam("StartDateDeposit")String StartDateDeposit,
                                                               @RequestParam("EndDateDeposit")String EndDateDeposit
                                                            ) throws ParseException{
        LOGGER.debug("getFilterDepositByNameBetweenDateDeposit({}, {},{})",depositByName, StartDateDeposit, EndDateDeposit);

        List<Map> depositsAllDepositors = depositService.getBankDepositByNameWithAllDepositorsBetweenDateDeposit(depositByName, dateFormat.parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        LOGGER.debug("depositsAllDepositors.size = " + depositsAllDepositors.size());

        List<BankDepositor> depositors = depositorService.getBankDepositorByIdDepositBetweenDateDeposit((Long)depositsAllDepositors.get(0).get("depositId"), dateFormat.
                parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        LOGGER.debug("depositors.size = " + depositors.size());

        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        BankDepositor depositor = depositorService.getBankDepositorsSummAmountByIdDepositBetweenDateDeposit((Long)depositsAllDepositors.get(0).get("depositId"), dateFormat.
                parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        depositorsSum.add(depositor);
        LOGGER.debug("depositorsSum.size = " + depositorsSum.size());

        ModelAndView view = new ModelAndView("depositsList", "deposits", depositsAllDepositors);
        view.addObject("depositors",depositors);
        view.addObject("depositorSum",depositorsSum);

        return  view;
    }

    @RequestMapping("/filterByNameDepositBetweenDateReturnDeposit")
    public ModelAndView getFilterDepositByNameBetweenDateReturnDeposit(@RequestParam("depositByName")String depositByName,
                                                                        @RequestParam("StartDateReturnDeposit")String StartDateDeposit,
                                                                        @RequestParam("EndDateReturnDeposit")String EndDateDeposit
                                                                        ) throws ParseException{
        LOGGER.debug("getFilterDepositByNameBetweenDateReturnDeposit({}, {},{})",depositByName, StartDateDeposit, EndDateDeposit);

        List<Map> depositsAllDepositors = depositService.getBankDepositByNameWithAllDepositorsBetweenDateReturnDeposit(depositByName, dateFormat.parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        LOGGER.debug("depositsAllDepositors.size = " + depositsAllDepositors.size());

        List<BankDepositor> depositors = depositorService.getBankDepositorByIdDepositBetweenDateReturnDeposit((Long) depositsAllDepositors.get(0).get("depositId"), dateFormat.
                parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        LOGGER.debug("depositors.size = " + depositors.size());

        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        BankDepositor depositor = depositorService.getBankDepositorsSummAmountByIdDepositBetweenDateReturnDeposit((Long)depositsAllDepositors.get(0).get("depositId"),dateFormat.
                parse(StartDateDeposit), dateFormat.parse(EndDateDeposit));
        depositorsSum.add(depositor);
        LOGGER.debug("depositorsSum.size = " + depositorsSum.size());

        ModelAndView view = new ModelAndView("depositsList", "deposits", depositsAllDepositors);
        view.addObject("depositors",depositors);
        view.addObject("depositorSum",depositorsSum);

        return  view;
    }

    @RequestMapping("/filterBetweenDateDeposit")
    public ModelAndView getFilterBetweenDate(@RequestParam("StartDateDeposit")String StartDateDeposit,
                                             @RequestParam("EndDateDeposit")String EndDateDeposit
                                            )throws ParseException{
        LOGGER.debug("getFilterBetweenDate({},{})",StartDateDeposit, EndDateDeposit);

        Date startDate = dateFormat.parse(StartDateDeposit);
        Date endDate = dateFormat.parse(EndDateDeposit);
        List<BankDepositor> depositors = depositorService.getBankDepositorBetweenDateDeposit(startDate, endDate);
        LOGGER.debug("depositors.size = " + depositors.size());

        List<Map> deposits = depositService.getBankDepositsAllDepositorsBetweenDateDeposit(startDate, endDate);
        LOGGER.debug("deposits.size = " + deposits.size());

        BankDepositor depositorSum = depositorService.getBankDepositorSummAmountDepositBetweenDateDeposit(startDate, endDate);
        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        depositorsSum.add(depositorSum);
        LOGGER.debug("depositorsSum.size = " + depositorsSum.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",deposits);
        view.addObject("depositorSum",depositorsSum);

        return  view;
    }

    @RequestMapping("/filterBetweenDateReturnDeposit")
    public ModelAndView getFilterBetweenDateReturn( @RequestParam("StartDateReturnDeposit")String StartDateDeposit,
                                                    @RequestParam("EndDateReturnDeposit")String EndDateDeposit
                                                    )throws ParseException{
        LOGGER.debug("getFilterBetweenDateReturn({},{})",StartDateDeposit, EndDateDeposit);

        Date startDate = dateFormat.parse(StartDateDeposit);
        Date endDate = dateFormat.parse(EndDateDeposit);
        List<BankDepositor> depositors = depositorService.getBankDepositorBetweenDateReturnDeposit(startDate, endDate);
        LOGGER.debug("depositors.size = " + depositors.size());

        List<Map> deposits = depositService.getBankDepositsAllDepositorsBetweenDateReturnDeposit(startDate, endDate);
        LOGGER.debug("deposits.size = " + deposits.size());

        BankDepositor depositorSum = depositorService.getBankDepositorSummAmountDepositBetweenDateReturnDeposit(startDate, endDate);
        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        depositorsSum.add(depositorSum);
        LOGGER.debug("depositorsSum.size = " + depositorsSum.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",deposits);
        view.addObject("depositorSum",depositorsSum);

        return  view;
    }

    @RequestMapping(value={"/updateFormDeposit"}, method = RequestMethod.GET)
    public ModelAndView launchUpdateForm(RedirectAttributes redirectAttributes,
                                         @RequestParam("depositId")Long depositId
                                        ){
        LOGGER.debug("launchUpdateForm({})", depositId);
        try {
            BankDeposit deposit = depositService.getBankDepositById(depositId);
            LOGGER.debug("getBankDepositById({})={}", depositId, deposit);

            return new ModelAndView("updateFormDeposit", "deposit", deposit);

        }catch(Exception e) {
            LOGGER.debug("launchUpdateForm({}), Exception:{}",depositId, e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/deposits/");
        }
    }

    @RequestMapping(value={"/updateFormDeposit"}, method = RequestMethod.POST)
    public String getUpdateForm(ModelMap model,@ModelAttribute("deposit")
                                        BankDeposit deposit,
                                        BindingResult result,
                                        SessionStatus status
                                     ) {

        LOGGER.debug("getUpdateForm({},{},{})",deposit, result, status);
        depositValidator.validate(deposit, result);

        if (result.hasErrors()) {
            LOGGER.debug("depositValidator.validate({},{})",deposit, result);
            return "updateFormDeposit";
        } else {
            status.setComplete();
        }

        try {
            LOGGER.debug("updateBankDeposit({})",deposit);
            depositService.updateBankDeposit(deposit);

            return "redirect:/deposits/";
        }catch(Exception e) {
            LOGGER.debug("getUpdateForm(), Exception:{}",e.toString());
            return "redirect:/updateFormDeposit?depositId="+deposit.getDepositId();
        }
    }

    @RequestMapping(value={"/deleteDeposit"}, method = RequestMethod.GET)
    public ModelAndView launchDeleteForm(RedirectAttributes redirectAttributes,
                                         @RequestParam("depositId")Long depositId
                                        ) {
        LOGGER.debug("launchDeleteForm({})",depositId);
        try {
            LOGGER.debug("removeBankDeposit({})",depositId);
            depositService.removeBankDeposit(depositId);
            return new ModelAndView("redirect:/deposits/");
        }catch(Exception e) {
            LOGGER.debug("launchDeleteForm({}), Exception:{}", depositId,e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/deposits/");
        }
    }

    @RequestMapping("/")
    public ModelAndView getListDepositsView() throws ParseException{
        LOGGER.debug("getListDepositsView()");
        List<BankDeposit> deposits;
        List<Map> depositsAllDepositors;
        List<BankDepositor> depositors = null;
        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        BankDepositor depositorSum;
        ModelAndView view = new ModelAndView("depositsList");
        try {
            deposits = depositService.getBankDeposits();
            LOGGER.debug("deposits.size = " + deposits.size());
            depositsAllDepositors = depositService.getBankDepositsAllDepositors();
            LOGGER.debug("depositsAllDepositors.size = " + depositsAllDepositors.size());
            view.addObject("deposits",depositsAllDepositors);
        } catch(IllegalArgumentException e) {
            LOGGER.debug("getListDepositsView(), Exception:{}", e.toString());
            return new ModelAndView("inputFormDeposit", "deposit", new BankDeposit(null,null,0,0,null,0,null));
        } catch(NullPointerException e) {
            LOGGER.debug("getListDepositsView(), Exception:{}", e.toString());
            return new ModelAndView("inputFormDeposit", "deposit", new BankDeposit(null,null,0,0,null,0,null));
        }
        try {
            depositors = depositorService.getBankDepositors();
            LOGGER.debug("depositors.size = " + depositors.size());
            view.addObject("depositors", depositors);
        } catch(IllegalArgumentException e) {
            LOGGER.debug("getListDepositsView({}), Exception:{}",depositors, e.toString());
            return view;
        } catch(NullPointerException e) {
            LOGGER.debug("getListDepositsView({}), Exception:{}", depositors, e.toString());
            return view;
        }
        try {
            depositorSum = depositorService.getBankDepositorsAllSummAmount();
            depositorsSum.add(depositorSum);
            LOGGER.debug("depositorsSum.size = " + depositorsSum.size());
            view.addObject("depositorSum", depositorsSum);
        } catch(IllegalArgumentException e) {
            LOGGER.debug("getListDepositsView({}), Exception:{}", depositorsSum, e.toString());
            return view;
        } catch(NullPointerException e) {
            LOGGER.debug("getListDepositsView({}), Exception:{}", depositorsSum, e.toString());
            return view;
        }
        return view;
    }

}