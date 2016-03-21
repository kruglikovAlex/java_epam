package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/deposit")
public class BankDepositController {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    BankDepositService depositService;

    @Autowired
    BankDepositorService depositorService;

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/inputDeposit", method = RequestMethod.GET)
    public ModelAndView getInputFormDeposit(ModelMap model)
    {
        LOGGER.debug("getInputFormDeposit({})",model);
        model.put("deposit",DataConfig.getEmptyDeposit());
        return new ModelAndView("depositFrame", model);
    }

    /**
     *
     * @param model
     * @param deposit
     * @param status
     * @return
     */
    @RequestMapping(value={"/submitDataDeposit"}, method = RequestMethod.POST)
    public String postInputFormDeposit( ModelMap model,
                                        @ModelAttribute("deposit") BankDeposit deposit,
                                        SessionStatus status)
    {
        status.setComplete();
        Assert.isNull(deposit.getDepositId(),ERROR_NULL_PARAM);
        try{
            LOGGER.debug("depositService.addBankDeposit({})",deposit);
            depositService.addBankDeposit(deposit);
            return "redirect:/deposit/main";
        }catch(Exception e) {
            LOGGER.debug("postInputFormDeposit(), Exception:{}", e.toString());
            return "redirect:/submitDataDeposit?depositId=" + deposit.getDepositId();
        }
    }

    /**
     *
     * @param redirectAttributes
     * @param depositId
     * @return
     */
    @RequestMapping(value={"/updateDeposit"}, method = RequestMethod.GET)
    public ModelAndView getUpdateFormDeposit(RedirectAttributes redirectAttributes,
                                             @RequestParam("depositId")Long depositId
    ){
        LOGGER.debug("getUpdateFormDeposit({})", depositId);
        try {
            BankDeposit deposit = depositService.getBankDepositById(depositId);
            LOGGER.debug("getBankDepositById({})={}", depositId, deposit);

            return new ModelAndView("updateFormDeposit", "deposit", deposit);

        }catch(Exception e) {
            LOGGER.debug("getUpdateFormDeposit({}), Exception:{}",depositId, e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/deposit/main");
        }
    }

    /**
     *
     * @param map
     * @param deposit
     * @param status
     * @return
     */
    @RequestMapping(value = {"/updateDeposit"}, method = RequestMethod.POST)
    public String postUpdateFormDeposit(ModelMap map,
                                        @ModelAttribute("deposit") BankDeposit deposit,
                                        BindingResult result,
                                        SessionStatus status)
    {
        LOGGER.debug("postUpdateFormDeposit({},{},{})",deposit, result, status);
        status.setComplete();
        Assert.notNull(deposit.getDepositId(),ERROR_METHOD_PARAM);
        try{
            LOGGER.debug("depositService.updateBankDeposit({})",deposit);
            depositService.updateBankDeposit(deposit);
            return "redirect:/deposit/main";
        }catch(Exception e) {
            LOGGER.debug("postUpdateFormDeposit(), Exception:{}", e.toString());
            return "redirect:/updateFormDeposit?depositId=" + deposit.getDepositId();
        }
    }

    @RequestMapping(value = {"/deleteDeposit"}, method = RequestMethod.DELETE)
    public ModelAndView deleteDeposit(RedirectAttributes redirectAttributes,
                                      @RequestParam("depositId") Long depositId)
    {
        LOGGER.debug("deleteDeposit({})",depositId);
        try{
            LOGGER.debug("deleteBankDeposit({})",depositId);
            depositService.deleteBankDeposit(depositId);
            return new ModelAndView("redirect:/deposit/main");
        }catch(Exception e) {
            LOGGER.debug("deleteDeposit{}), Exception:{}", depositId,e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/deposit/main");
        }
    }

    /**
     *
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/main")
    public ModelAndView getListDepositsView() throws ParseException
    {
        LOGGER.debug("getListDepositsView()");
        Long idDeposit;
        List<Map> deposits;
        List<BankDepositor> depositors;
        try{
            deposits = depositService.getBankDepositsWithDepositors();
            LOGGER.debug("deposits - {}",deposits.get(0));
        }catch (Exception e){
            deposits = DataConfig.getExistAllDepositsAllDepositors();
            LOGGER.debug("deposits - {}",deposits.get(0));
        }
        try{
            depositors = depositorService.getBankDepositors();
            LOGGER.debug("depositors - {}",depositors.get(0));
        }catch (Exception e){
            depositors = DataConfig.getEmptyDepositors();
            LOGGER.debug("depositors - {}",depositors.get(0));
        }
        if(deposits.get(0).get("depositId") == null){
            idDeposit = 1L;
            LOGGER.debug("depositId - {}",idDeposit);
        }else{
            idDeposit = (Long)deposits.get(0).get("depositId");
            LOGGER.debug("depositId - {}",idDeposit);
        }

        String year = dateFormat.format(Calendar.getInstance().getTime());
        LOGGER.debug("year - {}",year);

        ModelAndView view = new ModelAndView("mainFrame");
        view.addObject("deposits",deposits);
        view.addObject("depositors",depositors);
        view.addObject("year",year);
        view.addObject("idDeposit",idDeposit);

        return view;
    }
}
