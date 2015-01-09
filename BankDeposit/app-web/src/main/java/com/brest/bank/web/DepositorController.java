package com.brest.bank.web;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.validator.BankDepositorValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.validation.BindingResult;
import org.springframework.ui.ModelMap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Map;

@RequestMapping("/depositors")
@Controller
public class DepositorController {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BankDepositorValidator depositorValidator;

    @Autowired
    private BankDepositorService depositorService;

    @Autowired
    private BankDepositService depositService;

    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        String datePattern="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(datePattern);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class,new CustomDateEditor(dateFormat,true,datePattern.length()));
        binder.registerCustomEditor(String.class,new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class,null,new CustomNumberEditor(Integer.class,null,true));
    }


    @RequestMapping("/inputDepositor")
    public ModelAndView InputForm(ModelMap model) {
        model.put("depositor", new BankDepositor());
        return new ModelAndView("inputFormDepositor", "depositor", new BankDepositor());
    }

    @RequestMapping(value={"/inputFormDepositor"}, method = RequestMethod.GET)
    public ModelAndView launchAddForm(ModelMap model,
                                      RedirectAttributes redirectAttributes,
                                      @RequestParam("depositorIdDeposit")Long depositorIdDeposit
                                     ){

        LOGGER.debug("launchAddForm({})",depositorIdDeposit);
        try {
            BankDepositor depositor = new BankDepositor(null, null, 0L, null, 0, 0, 0, null, 0);
            depositor.setDepositorIdDeposit(depositorIdDeposit);
            return new ModelAndView("inputFormDepositor", "depositor", depositor);
        }catch(Exception e) {
            LOGGER.debug("launchAddForm({}), Exception:{}", depositorIdDeposit,e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/deposits/");
        }
    }

    @RequestMapping(value={"/submitDataDepositor"}, method=RequestMethod.POST)
    public String getInputForm( ModelMap model,@ModelAttribute("depositor")
                                BankDepositor depositor,
                                BindingResult result,
                                SessionStatus status
                                ) throws ParseException {
        LOGGER.debug("getInputForm({})", depositor);
        LOGGER.debug("depositorValidator.validate({},{})",depositor, result);

        depositorValidator.validate(depositor, result);

        if (result.hasErrors()) {
            LOGGER.debug("depositorValidator.validate({},{})",depositor, result);
            return  "inputFormDepositor";
        } else {
            status.setComplete();
        }

        LOGGER.debug("addBankDepositor({})",depositor);
        depositorService.addBankDepositor(depositor);
        return "redirect:/deposits/";
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

        List<Map> depositsList = depositService.getBankDepositsAllDepositorsBetweenDateDeposit(startDate, endDate);
        LOGGER.debug("depositsList.size = " + depositsList.size());

        BankDepositor depositorSum = depositorService.getBankDepositorSummAmountDepositBetweenDateDeposit(startDate, endDate);
        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        depositorsSum.add(depositorSum);
        LOGGER.debug("depositorSum.size = " + depositorsSum.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",depositsList);
        view.addObject("depositorSum",depositorsSum);

        return  view;
    }

    @RequestMapping("/filterBetweenDateReturnDeposit")
    public ModelAndView getFilterBetweenDateReturn( @RequestParam("StartDateReturnDeposit")String StartDateDeposit,
                                                    @RequestParam("EndDateReturnDeposit")String EndDateDeposit
                                                )throws ParseException{
        LOGGER.debug("getFilterBetweenDateReturnDeposit({},{})",StartDateDeposit, EndDateDeposit);

        Date startDate = dateFormat.parse(StartDateDeposit);
        Date endDate = dateFormat.parse(EndDateDeposit);

        List<BankDepositor> depositors = depositorService.getBankDepositorBetweenDateReturnDeposit(startDate, endDate);
        LOGGER.debug("depositors.size = " + depositors.size());

        List<Map> depositsList = depositService.getBankDepositsAllDepositorsBetweenDateReturnDeposit(startDate, endDate);
        LOGGER.debug("depositsList.size = " + depositsList.size());

        BankDepositor depositorSum = depositorService.getBankDepositorSummAmountDepositBetweenDateReturnDeposit(startDate, endDate);
        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        depositorsSum.add(depositorSum);
        LOGGER.debug("depositorsSum.size = " + depositorsSum.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",depositsList);
        view.addObject("depositorSum",depositorsSum);

        return  view;
    }

    @RequestMapping("/filterByIdDepositor")
    public ModelAndView getFilterDepositorById(@RequestParam("depositorById")Long depositorById
                                            ){
        LOGGER.debug("getFilterDepositorById({})",depositorById);

        BankDepositor depositor = depositorService.getBankDepositorById(depositorById);
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
        depositors.add(depositor);
        LOGGER.debug("depositors.size = " + depositors.size());

        List<Map> depositsList = depositService.getBankDepositByIdAllDepositors(depositor.getDepositorIdDeposit());
        LOGGER.debug("depositsList.size = " + depositsList.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",depositsList);
        view.addObject("depositorSum",depositors);

        return  view;
    }

    @RequestMapping("/filterByIdDepositDepositor")
    public ModelAndView getFilterDepositorByIdDeposit(@RequestParam("depositorByIdDeposit")Long depositorByIdDeposit
                                                    ){
        LOGGER.debug("getFilterDepositorByIdDeposit({})",depositorByIdDeposit);

        List<BankDepositor> depositors = depositorService.getBankDepositorByIdDeposit(depositorByIdDeposit);
        LOGGER.debug("depositors.size = " + depositors.size());

        List<Map> depositsList = depositService.getBankDepositByIdAllDepositors(depositorByIdDeposit);
        LOGGER.debug("depositsList.size = " + depositsList.size());

        List<BankDepositor> depositorsSum = new ArrayList<BankDepositor>();
        BankDepositor depositor = depositorService.getBankDepositorsSummAmountByIdDeposit(depositorByIdDeposit);
        depositorsSum.add(depositor);
        LOGGER.debug("depositorsSum.size = " + depositorsSum.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",depositsList);
        view.addObject("depositorSum",depositorsSum);

        return  view;
    }

    @RequestMapping("/filterByNameDepositor")
    public ModelAndView getFilterDepositorByName(@RequestParam("depositorByName")String depositorByName
                                                ){
        LOGGER.debug("getFilterDepositorByName({})",depositorByName);

        BankDepositor depositor = depositorService.getBankDepositorByName(depositorByName);
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
        depositors.add(depositor);
        LOGGER.debug("depositors.size = " + depositors.size());

        List<Map> depositsList = depositService.getBankDepositByIdAllDepositors(depositor.getDepositorIdDeposit());
        LOGGER.debug("depositsList.size = " + depositsList.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",depositsList);
        view.addObject("depositorSum",depositors);

        return  view;
    }

    @RequestMapping(value={"/updateFormDepositor"}, method = RequestMethod.GET)
    public ModelAndView launchUpdateForm(RedirectAttributes redirectAttributes,
                                         @RequestParam("depositorId")Long depositorId
                                        ) {
        LOGGER.debug("launchUpdateForm({})",depositorId);
        try {
            return new ModelAndView("updateFormDepositor", "depositor", depositorService.getBankDepositorById(depositorId));
        }catch(Exception e) {
            LOGGER.debug("launchUpdateForm({}), Exception:{}",depositorId,e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/deposits/");
        }
    }

    @RequestMapping(value={"/updateFormDepositor"}, method = RequestMethod.POST)
    public String getUpdateForm(ModelMap model,@ModelAttribute("depositor")
                                      BankDepositor depositor,
                                      BindingResult result,
                                      SessionStatus status
                                    ) throws ParseException {
        LOGGER.debug("depositorValidator.validate({},{})",depositor, result);
        depositorValidator.validate(depositor, result);

        if (result.hasErrors()) {
            LOGGER.debug("depositorValidator.validate({},{})",depositor, result);

            return "updateFormDepositor";
        } else {
            status.setComplete();
        }

        try {
            LOGGER.debug("updateBankDepositor({})",depositor);
            depositorService.updateBankDepositor(depositor);
            return "redirect:/deposits/";
        }catch(Exception e) {
            LOGGER.debug("getUpdateForm({}), Exception:{}",depositor,e.toString());
            return "redirect:/updateFormDepositor?depositorId="+depositor.getDepositorId();
        }
    }

    @RequestMapping(value={"/delete"}, method = RequestMethod.GET)
    public ModelAndView launchDeleteForm(RedirectAttributes redirectAttributes,
                                         @RequestParam("depositorId")Long depositorId
                                        ) {
        LOGGER.debug("launchDeleteForm({})",depositorId);
        try {
            LOGGER.debug("removeBankDepositor({})",depositorId);
            depositorService.removeBankDepositor(depositorId);
            return new ModelAndView("redirect:/deposits/");
        }catch(Exception e) {
            LOGGER.debug("launchDeleteForm({}), Exception: {}",depositorId, e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/deposits/");
        }
    }
}