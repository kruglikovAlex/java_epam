package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/depositor")
public class BankDepositorController {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    private BankDepositorService depositorService;

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

    @RequestMapping(value={"/inputDepositor"}, method = RequestMethod.GET)
    public ModelAndView getInputFormDepositor(ModelMap model,
                                              RedirectAttributes redirectAttributes,
                                              @RequestParam("idDeposit")Long depositId)
    {
        LOGGER.debug("getInputFormDepositor(idDeposit={})",depositId);
        try {
            model.put("idDeposit",depositId);
            model.put("depositor",DataConfig.getEmptyDepositor());
            return new ModelAndView("depositorFrame", model);
        }catch(Exception e) {
            LOGGER.debug("getInputFormDepositor(depositId={}), Exception:{}", depositId,e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/deposit/main");
        }
    }

    @RequestMapping(value={"/submitDataDepositor"}, method = RequestMethod.POST)
    public ModelAndView postInputFormDepositor(ModelMap model,
                                               @ModelAttribute("idDeposit") String depositId,
                                               @ModelAttribute("depositor") BankDepositor depositor,
                                               BindingResult result,
                                               SessionStatus status//,
                                               //@RequestParam("idDeposit") String depositId
    )
    {
        LOGGER.debug("postInputFormDepositor({},{},{},{})", depositId,depositor,result,status);
        status.setComplete();
        Assert.isNull(depositor.getDepositorId(),ERROR_NULL_PARAM);
        try {
            LOGGER.debug("depositorService.addBankDepositor({},{})", depositId, depositor);

            depositorService.addBankDepositor(Long.parseLong(depositId), depositor);
            return new ModelAndView("redirect:/deposit/main");
        }catch(Exception e) {
            LOGGER.debug("postInputFormDepositor(), Exception:{}", e.toString());
            return new ModelAndView("redirect:/inputDepositor?idDeposit=" + depositId);
        }
    }

    /**
    *
    * @param redirectAttributes
    * @param depositorId
    * @return
    */
    @RequestMapping(value={"/updateDepositor"}, method = RequestMethod.GET)
    public ModelAndView getUpdateFormDepositor(RedirectAttributes redirectAttributes,
                                               @RequestParam("depositorId")Long depositorId)
    {
        LOGGER.debug("getUpdateFormDepositor({})", depositorId);
        try {
            BankDepositor depositor = depositorService.getBankDepositorById(depositorId);
            LOGGER.debug("getBankDepositorById({})={}", depositorId, depositor);

            return new ModelAndView("updateFormDepositor", "depositor", depositor);

        }catch(Exception e) {
            LOGGER.debug("getUpdateFormDepositor({}), Exception:{}",depositorId, e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/deposit/main");
        }
    }

    /**
     *
     * @param map
     * @param depositor
     * @param status
     * @return
     */
    @RequestMapping(value = {"/updateDepositor"}, method = RequestMethod.POST)
    public String postUpdateFormDepositor(ModelMap map,
                                          @ModelAttribute("depositor") BankDepositor depositor,
                                          BindingResult result,
                                          SessionStatus status)
    {
        LOGGER.debug("postUpdateFormDepositor({},{},{})",depositor, result, status);
        status.setComplete();
        Assert.notNull(depositor.getDepositorId(),ERROR_METHOD_PARAM);
        try{
            LOGGER.debug("depositorService.updateBankDepositor({})",depositor);
            depositorService.updateBankDepositor(depositor);
            return "redirect:/deposit/main";
        }catch(Exception e) {
            LOGGER.debug("postUpdateFormDepositor(), Exception:{}", e.toString());
            return "redirect:/updateFormDepositor?depositorId=" + depositor.getDepositorId();
        }
    }

    /**
     *
     * @param redirectAttributes
     * @param depositorId
     * @return
     */
    @RequestMapping(value = {"/deleteDepositor"}, method = RequestMethod.GET)
    public ModelAndView deleteDepositor(RedirectAttributes redirectAttributes,
                                        @RequestParam("depositorId") Long depositorId)
    {
        LOGGER.debug("deleteDepositor({})",depositorId);
        try{
            LOGGER.debug("removeBankDepositor({})",depositorId);
            depositorService.removeBankDepositor(depositorId);
            return new ModelAndView("redirect:/deposit/main");
        }catch(Exception e) {
            LOGGER.debug("deleteDepositor{}), Exception:{}", depositorId,e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/deposit/main");
        }
    }
}
