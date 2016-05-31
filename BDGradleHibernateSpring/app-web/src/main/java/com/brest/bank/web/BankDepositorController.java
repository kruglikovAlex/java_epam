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
import java.util.*;

@Controller
@RequestMapping("/depositor")
public class BankDepositorController {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";
    public static final String ERROR_PARAM_VALUE = "The parameter must be '0' or '1'";
    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    BankDepositService depositService;

    @Autowired
    private BankDepositorService depositorService;

    private BankDeposit deposit;
    private BankDepositor depositor;
    private List<Map> deposits = new ArrayList<Map>();
    private List<BankDepositor> depositors = new ArrayList<BankDepositor>();
    private Long idDeposit;
    private String year;

    /**
     *
     * @param binder
     * @throws Exception
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) throws Exception
    {
        String datePattern="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(datePattern);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class,new CustomDateEditor(dateFormat,true,datePattern.length()));
        binder.registerCustomEditor(String.class,new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class,null,new CustomNumberEditor(Integer.class,null,true));
    }

    /**
     * Get form for input Bank Depositor
     *
     * @param model
     * @param redirectAttributes
     * @param depositId
     * @return ModelAndView "depositorFrame"
     */
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

    /**
     * Input Bank Depositor from fields of input form to the BD
     *
     * @param model
     * @param depositId String - ID of Bank Deposit
     * @param depositor BankDepositor - entity from field of input form
     * @param result
     * @param status
     * @return ModelAndVie - redirect Url="/deposit/main"
     */
    @RequestMapping(value={"/submitDataDepositor"}, method = RequestMethod.POST)
    public ModelAndView postInputFormDepositor(ModelMap model,
                                               @ModelAttribute("idDeposit") String depositId,
                                               @ModelAttribute("depositor") BankDepositor depositor,
                                               BindingResult result,
                                               SessionStatus status)
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
    * Get form for update Bank Depositor
     *
    * @param redirectAttributes
    * @param depositorId Long - Request parameter to get BankDepositor entity for updating
    * @return ModelAndView "updateFromDepositor"
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
     * Update Bank Depositor from fields of update form to the BD
     *
     * @param map
     * @param depositor BankDepositor - entity from field of update form
     * @param status
     * @return String - redirect Url="/deposit/main"
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
     * Deleting Bank Depositor by ID
     *
     * @param redirectAttributes
     * @param depositorId Long - Request parameter to delete BankDepositor entity from BD
     * @return ModelAndView - redirect Url="/deposit/main"
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

    /**
     * Get Bank Deposit by Depositor ID with depositors
     *
     * @param redirectAttributes
     * @param depositorId Long - depositorId of the Bank Depositor to return
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterById"}, method = RequestMethod.GET)
    public ModelAndView filterById(RedirectAttributes redirectAttributes,
                                   @RequestParam("depositorId") Long depositorId,
                                   SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterById(depositorId={})",depositorId);
        status.setComplete();
        try{
            Assert.notNull(depositorId,ERROR_METHOD_PARAM);

            LOGGER.debug("getBankDepositByDepositorIdWithDepositors(depositorId={})",depositorId);
            deposits = new ArrayList<Map>();
            deposits.add(depositService.getBankDepositByDepositorIdWithDepositors(depositorId));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                LOGGER.debug("getBankDepositorById(depositorId={})",depositorId);
                depositors = new ArrayList<BankDepositor>();
                depositors.add(depositorService.getBankDepositorById(depositorId));
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorById(depositorId={}), Exception:{}", depositorId,e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterById({}), Exception:{}", depositorId,e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());

            deposits = DataConfig.getEmptyAllDepositsAllDepositors();
            LOGGER.debug("deposits - {}",deposits.get(0));

            depositors = DataConfig.getEmptyDepositors();
            LOGGER.debug("depositors - {}",depositors.get(0));

            idDeposit = 1L;
            LOGGER.debug("idDeposit={}",idDeposit);

            year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
            LOGGER.debug("year={}",year);
        }

        ModelAndView view = new ModelAndView("mainFrame");
        view.addObject("deposits",deposits);
        view.addObject("depositors",depositors);
        view.addObject("year",year);
        view.addObject("idDeposit",idDeposit);

        return view;
    }

    /**
     * Get Bank Deposit by Depositor ID with depositors
     *
     * @param redirectAttributes
     * @param depositorName String - depositorName of the Bank Depositor to return
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByName"}, method = RequestMethod.GET)
    public ModelAndView filterByName(RedirectAttributes redirectAttributes,
                                     @RequestParam("depositorName") String depositorName,
                                     SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByName(depositorName={})",depositorName);
        status.setComplete();
        try{
            Assert.notNull(depositorName,ERROR_METHOD_PARAM);

            LOGGER.debug("getBankDepositByDepositorNameWithDepositors(depositorName={})",depositorName);
            deposits = new ArrayList<Map>();
            deposits.add(depositService.getBankDepositByDepositorNameWithDepositors(depositorName));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                LOGGER.debug("getBankDepositorByName(depositorName={})",depositorName);
                depositors = new ArrayList<BankDepositor>();
                depositors.add(depositorService.getBankDepositorByName(depositorName));
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByName(depositorName={}), Exception:{}", depositorName,e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByName({}), Exception:{}", depositorName,e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());

            deposits = DataConfig.getEmptyAllDepositsAllDepositors();
            LOGGER.debug("deposits - {}",deposits.get(0));

            depositors = DataConfig.getEmptyDepositors();
            LOGGER.debug("depositors - {}",depositors.get(0));

            idDeposit = 1L;
            LOGGER.debug("idDeposit={}",idDeposit);

            year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
            LOGGER.debug("year={}",year);
        }

        ModelAndView view = new ModelAndView("mainFrame");
        view.addObject("deposits",deposits);
        view.addObject("depositors",depositors);
        view.addObject("year",year);
        view.addObject("idDeposit",idDeposit);

        return view;
    }

    /**
     * Get Bank Deposits by from-to Depositor Amount with depositors
     *
     * @param redirectAttributes
     * @param from Integer - Amount of the Bank Depositor
     * @param to Integer - Amount of the Bank Depositor
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByAmount"}, method = RequestMethod.GET)
    public ModelAndView filterByAmount(RedirectAttributes redirectAttributes,
                                       @RequestParam("fromAmountDepositor") Integer from,
                                       @RequestParam("toAmountDepositor") Integer to,
                                       SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByAmount(from={}, to={})",from,to);
        status.setComplete();
        try{
            Assert.notNull(from,ERROR_METHOD_PARAM);
            Assert.notNull(to,ERROR_METHOD_PARAM);
            Assert.isTrue(from<=to,ERROR_FROM_TO_PARAM);

            LOGGER.debug("getBankDepositsByDepositorAmountWithDepositors(from={},to={})",from,to);
            deposits = depositService.getBankDepositsByDepositorAmountWithDepositors(from, to);
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                for(Map depositReport:deposits){
                    LOGGER.debug("getBankDepositorByIdDeposit(depositId={})",depositReport.get("depositId"));
                    for(BankDepositor depr:depositorService
                            .getBankDepositorByIdDeposit((Long)depositReport.get("depositId"))){
                        if((depr.getDepositorAmountDeposit()>=from)&(depr.getDepositorAmountDeposit()<=to)){
                            depositors.add(depr);
                        }
                    }
                }
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(), Exception:{}", e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByAmount(from={}, to={}), Exception:{}", from,to,e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());

            deposits = DataConfig.getEmptyAllDepositsAllDepositors();
            LOGGER.debug("deposits - {}",deposits.get(0));

            depositors = DataConfig.getEmptyDepositors();
            LOGGER.debug("depositors - {}",depositors.get(0));

            idDeposit = 1L;
            LOGGER.debug("idDeposit={}",idDeposit);

            year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
            LOGGER.debug("year={}",year);
        }

        ModelAndView view = new ModelAndView("mainFrame");
        view.addObject("deposits",deposits);
        view.addObject("depositors",depositors);
        view.addObject("year",year);
        view.addObject("idDeposit",idDeposit);

        return view;
    }

    /**
     * Get Bank Deposits by mark return  with depositors
     *
     * @param redirectAttributes
     * @param markReturn Integer - Mark Return of the Bank Depositor
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByMarkReturn"}, method = RequestMethod.GET)
    public ModelAndView filterByMarkReturn(RedirectAttributes redirectAttributes,
                                           @RequestParam("depositorMarkReturnDeposit") Integer markReturn,
                                           SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByMarkReturn(markReturn={})",markReturn);
        status.setComplete();
        try{
            Assert.notNull(markReturn,ERROR_METHOD_PARAM);
            Assert.isTrue(markReturn==0||markReturn==1,ERROR_PARAM_VALUE);

            LOGGER.debug("getBankDepositsByDepositorMarkReturnWithDepositors(markReturn={})",markReturn);
            deposits = depositService.getBankDepositsByDepositorMarkReturnWithDepositors(markReturn);
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                for(Map depositReport:deposits){
                    LOGGER.debug("getBankDepositorByIdDeposit(depositId={})",depositReport.get("depositId"));
                    for(BankDepositor depr:depositorService
                            .getBankDepositorByIdDeposit((Long)depositReport.get("depositId"))){
                        if(depr.getDepositorMarkReturnDeposit()==markReturn){
                            depositors.add(depr);
                        }
                    }
                }
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(), Exception:{}", e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByMarkReturn(markReturn={}), Exception:{}", markReturn,e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());

            deposits = DataConfig.getEmptyAllDepositsAllDepositors();
            LOGGER.debug("deposits - {}",deposits.get(0));

            depositors = DataConfig.getEmptyDepositors();
            LOGGER.debug("depositors - {}",depositors.get(0));

            idDeposit = 1L;
            LOGGER.debug("idDeposit={}",idDeposit);

            year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
            LOGGER.debug("year={}",year);
        }

        ModelAndView view = new ModelAndView("mainFrame");
        view.addObject("deposits",deposits);
        view.addObject("depositors",depositors);
        view.addObject("year",year);
        view.addObject("idDeposit",idDeposit);

        return view;
    }
}
