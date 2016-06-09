package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/deposit")
public class BankDepositController {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_STRING_METHOD_PARAM = "The string parameter can not be empty";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";
    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    BankDepositService depositService;

    @Autowired
    BankDepositorService depositorService;

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
    protected void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Integer.class,null,new CustomNumberEditor(Integer.class,null,true));
    }

    /**
     * Get form for input Bank Deposit
     *
     * @param model
     * @return ModelAndView "depositFrame"
     */
    @RequestMapping(value = "/inputDeposit", method = RequestMethod.GET)
    public ModelAndView getInputFormDeposit(ModelMap model)
    {
        LOGGER.debug("getInputFormDeposit({})",model);
        model.put("deposit",DataConfig.getEmptyDeposit());
        return new ModelAndView("depositFrame", model);
    }

    /**
     * Input Bank Deposit from fields of input form to the BD
     *
     * @param model
     * @param deposit BankDeposit - entity from field of input form
     * @param status
     * @return String - redirect Url="/deposit/main"
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
     * Get form for update Bank Deposit
     *
     * @param redirectAttributes
     * @param depositId Long - Request parameter to get BankDeposit entity for updating
     * @param status
     * @return ModelAndView "updateFromDeposit"
     */
    @RequestMapping(value={"/updateDeposit"}, method = RequestMethod.GET)
    public ModelAndView getUpdateFormDeposit(RedirectAttributes redirectAttributes,
                                             @RequestParam("depositId")Long depositId,
                                             SessionStatus status)
    {
        LOGGER.debug("getUpdateFormDeposit({})", depositId);
        status.setComplete();
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
     * Update Bank Deposit from fields of update form to the BD
     *
     * @param map
     * @param deposit BankDeposit - entity from field of update form
     * @param status
     * @return String - redirect Url="/deposit/main"
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

    /**
     * Deleting Bank Deposit by ID
     *
     * @param redirectAttributes
     * @param depositId Long - Request parameter to delete BankDeposit entity from BD
     * @param status
     * @return ModelAndView - redirect Url="/deposit/main"
     */
    @RequestMapping(value = {"/deleteDeposit"}, method = RequestMethod.GET)
    public ModelAndView deleteDeposit(RedirectAttributes redirectAttributes,
                                      @RequestParam("depositId") Long depositId,
                                      SessionStatus status)
    {
        LOGGER.debug("deleteDeposit({})",depositId);
        status.setComplete();
        try{
            LOGGER.debug("deleteBankDeposit({})",depositId);
            depositService.deleteBankDeposit(depositId);
            return new ModelAndView("redirect:/deposit/main");
        }catch(Exception e) {
            LOGGER.debug("deleteDeposit({}), Exception:{}", depositId,e.toString());
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/deposit/main");
        }
    }

    /**
     * Get Bank Deposits by ID with depositors
     *
     * @param redirectAttributes
     * @param depositId Long - depositId of the Bank Deposit to return
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterById"}, method = RequestMethod.GET)
    public ModelAndView filterById(RedirectAttributes redirectAttributes,
                                   @RequestParam("depositId") Long depositId,
                                   SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterById(depositId={})",depositId);
        status.setComplete();
        try{
            Assert.notNull(depositId,ERROR_METHOD_PARAM);

            LOGGER.debug("getBankDepositByIdWithDepositors(depositId={})",depositId);
            deposits = new ArrayList<Map>();
            deposits.add(depositService.getBankDepositByIdWithDepositors(depositId));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={})",depositId);
                depositors = new ArrayList<BankDepositor>();
                depositors = depositorService.getBankDepositorByIdDeposit(depositId);
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}", depositId,e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterById({}), Exception:{}", depositId,e.toString());
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
     * Get Bank Deposits by ID with depositors from-to Date Deposit values
     *
     * @param redirectAttributes
     * @param depositId Long - depositId of the Bank Deposit to return
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByIdFromToDateDeposit"}, method = RequestMethod.GET)
    public ModelAndView filterByIdFromToDateDeposit(RedirectAttributes redirectAttributes,
                                                    @RequestParam("depositId") Long depositId,
                                                    @RequestParam("startDateDeposit") String startDate,
                                                    @RequestParam("endDateDeposit") String endDate,
                                                    SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByIdFromToDateDeposit(depositId={},start={},end={})",depositId,startDate,endDate);
        status.setComplete();
        try{
            Assert.notNull(depositId,ERROR_METHOD_PARAM);
            Assert.hasLength(startDate,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(endDate,ERROR_METHOD_PARAM);

            Date dateStart = dateFormat.parse(startDate), dateEnd = dateFormat.parse(endDate);
            Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

            LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositors(depositId={},start={},end={})"
                    ,depositId,startDate,endDate);
            deposits = new ArrayList<Map>();
            deposits.add(depositService.getBankDepositByIdFromToDateDepositWithDepositors(depositId,dateStart,dateEnd));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                LOGGER.debug("getBankDepositorByIdDepositFromToDateDeposit(depositId={})",depositId);
                depositors = new ArrayList<BankDepositor>();
                for(BankDepositor dep:depositorService.getBankDepositorByIdDeposit(depositId)){
                    if( ( dep.getDepositorDateDeposit().after(dateStart)
                            &dep.getDepositorDateDeposit().before(dateEnd) )
                            ||dep.getDepositorDateDeposit().equals(dateStart)
                            ||dep.getDepositorDateDeposit().equals(dateEnd) ){
                        depositors.add(dep);
                        LOGGER.debug("depositor - {}",dep);
                    }
                }

                year = startDate.substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}", depositId,e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByIdFromToDateDeposit({},{},{}), Exception:{}", depositId,startDate,endDate,e.toString());
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
     * Get Bank Deposits by ID with depositors from-to Date return Deposit values
     *
     * @param redirectAttributes
     * @param depositId Long - depositId of the Bank Deposit to return
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByIdFromToDateReturnDeposit"}, method = RequestMethod.GET)
    public ModelAndView filterByIdFromToDateReturnDeposit(RedirectAttributes redirectAttributes,
                                                          @RequestParam("depositId") Long depositId,
                                                          @RequestParam("startDateReturnDeposit") String startDate,
                                                          @RequestParam("endDateReturnDeposit") String endDate,
                                                          SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByIdFromToDateReturnDeposit(depositId={},start={},end={})",depositId,startDate,endDate);
        status.setComplete();
        try{
            Assert.notNull(depositId,ERROR_METHOD_PARAM);
            Assert.hasLength(startDate,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(endDate,ERROR_METHOD_PARAM);

            Date dateStart = dateFormat.parse(startDate), dateEnd = dateFormat.parse(endDate);
            Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

            LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositors(depositId={},start={},end={})"
                    ,depositId,startDate,endDate);
            deposits = new ArrayList<Map>();
            deposits.add(depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(depositId,dateStart,dateEnd));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                LOGGER.debug("getBankDepositorByIdDepositFromToDateDeposit(depositId={})",depositId);
                for(BankDepositor dep:depositorService.getBankDepositorByIdDeposit(depositId)){
                    if( ( dep.getDepositorDateReturnDeposit().after(dateStart)
                            &dep.getDepositorDateReturnDeposit().before(dateEnd) )
                            ||dep.getDepositorDateReturnDeposit().equals(dateStart)
                            ||dep.getDepositorDateReturnDeposit().equals(dateEnd) ){
                        depositors.add(dep);
                        LOGGER.debug("depositor - {}",dep);
                    }
                }

                year = startDate.substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}", depositId,e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByIdFromToDateReturnDeposit({},{},{}), Exception:{}", depositId,startDate,endDate,e.toString());
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
     * Get Bank Deposits by Name with depositors
     *
     * @param redirectAttributes
     * @param depositName String - depositName of the Bank Deposit to return
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByName"}, method = RequestMethod.GET)
    public ModelAndView filterByName(RedirectAttributes redirectAttributes,
                                     @RequestParam("depositName") String depositName,
                                     SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByName(depositName={})",depositName);
        status.setComplete();
        try{
            Assert.notNull(depositName,ERROR_METHOD_PARAM);
            Assert.hasLength(depositName,ERROR_STRING_METHOD_PARAM);

            LOGGER.debug("getBankDepositByNameWithDepositors(depositName={})",depositName);
            deposits = new ArrayList<Map>();
            deposits.add(depositService.getBankDepositByNameWithDepositors(depositName));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={})",deposits.get(0).get("depositId"));
                depositors = new ArrayList<BankDepositor>();
                depositors = depositorService.getBankDepositorByIdDeposit((Long)deposits.get(0).get("depositId"));
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}",
                        deposits.get(0).get("depositId"),e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByName({}), Exception:{}", depositName,e.toString());
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
     * Get Bank Deposits by Name with depositors from-to Date Deposit values
     *
     * @param redirectAttributes
     * @param depositName String - depositName of the Bank Deposit to return
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByNameFromToDateDeposit"}, method = RequestMethod.GET)
    public ModelAndView filterByNameFromToDateDeposit(RedirectAttributes redirectAttributes,
                                                      @RequestParam("depositName") String depositName,
                                                      @RequestParam("startDateDeposit") String startDate,
                                                      @RequestParam("endDateDeposit") String endDate,
                                                      SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByNameFromToDateDeposit(depositName={},start={},end={})",depositName,startDate,endDate);
        status.setComplete();
        try{
            Assert.hasLength(depositName,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(startDate,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(endDate,ERROR_STRING_METHOD_PARAM);

            Date dateStart = dateFormat.parse(startDate), dateEnd = dateFormat.parse(endDate);
            Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

            deposits = new ArrayList<Map>();
            LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositors(depositName={},start={},end={})"
                    ,depositName,startDate,endDate);
            deposits.add(depositService.getBankDepositByNameFromToDateDepositWithDepositors(depositName,dateStart,dateEnd));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                LOGGER.debug("getBankDepositorByIdDepositFromToDateDeposit(depositId={})",idDeposit);
                for(BankDepositor dep:depositorService.getBankDepositorByIdDeposit(idDeposit)){
                    if( ( dep.getDepositorDateDeposit().after(dateStart)
                            &dep.getDepositorDateDeposit().before(dateEnd) )
                            ||dep.getDepositorDateDeposit().equals(dateStart)
                            ||dep.getDepositorDateDeposit().equals(dateEnd) ){
                        depositors.add(dep);
                        LOGGER.debug("depositor - {}",dep);
                    }
                }

                year = startDate.substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}", idDeposit,e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByNameFromToDateDeposit({},{},{}), Exception:{}", depositName,startDate,endDate,e.toString());
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
     * Get Bank Deposits by Name with depositors from-to Date return Deposit values
     *
     * @param redirectAttributes
     * @param depositName String - depositName of the Bank Deposit to return
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByNameFromToDateReturnDeposit"}, method = RequestMethod.GET)
    public ModelAndView filterByNameFromToDateReturnDeposit(RedirectAttributes redirectAttributes,
                                                            @RequestParam("depositName") String depositName,
                                                            @RequestParam("startDateReturnDeposit") String startDate,
                                                            @RequestParam("endDateReturnDeposit") String endDate,
                                                            SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByNameFromToDateReturnDeposit(depositName={},start={},end={})",depositName,startDate,endDate);
        status.setComplete();
        try{
            Assert.hasLength(depositName,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(startDate,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(endDate,ERROR_STRING_METHOD_PARAM);

            Date dateStart = dateFormat.parse(startDate), dateEnd = dateFormat.parse(endDate);
            Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

            LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositors(depositName={},start={},end={})"
                    ,depositName,startDate,endDate);
            deposits = new ArrayList<Map>();
            deposits.add(depositService.getBankDepositByNameFromToDateReturnDepositWithDepositors(depositName,dateStart,dateEnd));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                LOGGER.debug("getBankDepositorByIdDepositFromToDateDeposit(depositId={})",idDeposit);
                for(BankDepositor dep:depositorService.getBankDepositorByIdDeposit(idDeposit)){
                    if( ( dep.getDepositorDateReturnDeposit().after(dateStart)
                            &dep.getDepositorDateReturnDeposit().before(dateEnd) )
                            ||dep.getDepositorDateReturnDeposit().equals(dateStart)
                            ||dep.getDepositorDateReturnDeposit().equals(dateEnd) ){
                        depositors.add(dep);
                        LOGGER.debug("depositor - {}",dep);
                    }
                }

                year = startDate.substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}", idDeposit,e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByNameFromToDateReturnDeposit({},{},{}), Exception:{}", depositName,startDate,endDate,e.toString());
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
     * Get Bank Deposits by Term with depositors
     *
     * @param redirectAttributes
     * @param depositMinTerm Integer - Min Term of the Bank Deposit to return
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByTerm"}, method = RequestMethod.GET)
    public ModelAndView filterByTerm(RedirectAttributes redirectAttributes,
                                     @RequestParam("depositMinTerm") Integer depositMinTerm,
                                     SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByTerm(depositMinTerm={})",depositMinTerm);
        status.setComplete();
        try{
            Assert.notNull(depositMinTerm,ERROR_METHOD_PARAM);

            LOGGER.debug("getBankDepositByTermWithDepositors(depositMinTerm={})",depositMinTerm);
            deposits = depositService.getBankDepositsByTermWithDepositors(depositMinTerm);
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                for(Map depositReport:deposits){
                    LOGGER.debug("getBankDepositorByIdDeposit(depositId={})",depositReport.get("depositId"));
                    depositors.addAll(depositorService
                            .getBankDepositorByIdDeposit((Long)depositReport.get("depositId")));
                }
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}",
                        deposits.get(0).get("depositId"),e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByTerm({}), Exception:{}", depositMinTerm,e.toString());
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
     * Get Bank Deposits by Amount with depositors
     *
     * @param redirectAttributes
     * @param depositMinAmount Integer - Min Amount of the Bank Deposit to return
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByAmount"}, method = RequestMethod.GET)
    public ModelAndView filterByAmount(RedirectAttributes redirectAttributes,
                                       @RequestParam("depositMinAmount") Integer depositMinAmount,
                                       SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByAmount(depositMinAmount={})",depositMinAmount);
        status.setComplete();
        try{
            Assert.notNull(depositMinAmount,ERROR_METHOD_PARAM);

            LOGGER.debug("getBankDepositsByAmountWithDepositors(depositMinAmount={})",depositMinAmount);
            deposits = depositService.getBankDepositsByAmountWithDepositors(depositMinAmount);
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                for(Map depositReport:deposits){
                    LOGGER.debug("getBankDepositorByIdDeposit(depositId={})",depositReport.get("depositId"));
                    depositors.addAll(depositorService
                            .getBankDepositorByIdDeposit((Long)depositReport.get("depositId")));
                }
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}",
                        deposits.get(0).get("depositId"),e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByAmount({}), Exception:{}", depositMinAmount,e.toString());
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
     * Get Bank Deposits by Interest Rate with depositors
     *
     * @param redirectAttributes
     * @param depositInterestRate Integer - Interest rate of the Bank Deposit to return
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByRate"}, method = RequestMethod.GET)
    public ModelAndView filterByRate(RedirectAttributes redirectAttributes,
                                     @RequestParam("depositInterestRate") Integer depositInterestRate,
                                     SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByRate(depositInterstRate={})",depositInterestRate);
        status.setComplete();
        try{
            Assert.notNull(depositInterestRate,ERROR_METHOD_PARAM);

            LOGGER.debug("getBankDepositByRateWithDepositors(depositInterestRate={})",depositInterestRate);
            deposits = depositService.getBankDepositsByRateWithDepositors(depositInterestRate);
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                for(Map depositReport:deposits){
                    LOGGER.debug("getBankDepositorByIdDeposit(depositId={})",depositReport.get("depositId"));
                    depositors.addAll(depositorService
                            .getBankDepositorByIdDeposit((Long)depositReport.get("depositId")));
                }
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}",
                        deposits.get(0).get("depositId"),e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByRate({}), Exception:{}", depositInterestRate,e.toString());
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
     * Get Bank Deposits by Currency with depositors
     *
     * @param redirectAttributes
     * @param depositCurrency String - depositCurrency of the Bank Deposit to return
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByCurrency"}, method = RequestMethod.GET)
    public ModelAndView filterByCurrency(RedirectAttributes redirectAttributes,
                                         @RequestParam("depositCurrency") String depositCurrency,
                                         SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByCurrency(depositCurrency={})",depositCurrency);
        status.setComplete();

        try{
            Assert.notNull(depositCurrency,ERROR_METHOD_PARAM);
            Assert.hasLength(depositCurrency,ERROR_STRING_METHOD_PARAM);

            LOGGER.debug("getBankDepositsByCurrencyWithDepositors(depositCurrency={})",depositCurrency);
            deposits = depositService.getBankDepositsByCurrencyWithDepositors(depositCurrency);
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                for(Map depositReport:deposits){
                    LOGGER.debug("getBankDepositorByIdDeposit(depositId={})",depositReport.get("depositId"));
                    depositors.addAll(depositorService
                            .getBankDepositorByIdDeposit((Long)depositReport.get("depositId")));
                }
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}",
                        deposits.get(0).get("depositId"),e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByCurrency({}), Exception:{}", depositCurrency,e.toString());
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
     * Get Bank Deposits by Currency with depositors from-to Date Deposit values
     *
     * @param redirectAttributes
     * @param depositCurrency String - Currency of the Bank Deposit to return
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByCurrencyFromToDateDeposit"}, method = RequestMethod.GET)
    public ModelAndView filterByCurrencyFromToDateDeposit(RedirectAttributes redirectAttributes,
                                                          @RequestParam("depositCurrency") String depositCurrency,
                                                          @RequestParam("startDateDeposit") String startDate,
                                                          @RequestParam("endDateDeposit") String endDate,
                                                          SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByCurrencyFromToDateDeposit(depositCurrency={},start={},end={})",
                depositCurrency,startDate,endDate);
        status.setComplete();
        try{
            Assert.hasLength(depositCurrency,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(startDate,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(endDate,ERROR_STRING_METHOD_PARAM);

            Date dateStart = dateFormat.parse(startDate), dateEnd = dateFormat.parse(endDate);
            Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

            LOGGER.debug("getBankDepositByCurrencyFromToDateDepositWithDepositors(depositCurrency={},start={},end={})"
                    ,depositCurrency,startDate,endDate);
            deposits = depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors(depositCurrency
                    ,dateStart,dateEnd);
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                for(Map dep:deposits){
                    LOGGER.debug("getBankDepositorByIdDepositFromToDateDeposit(depositId={})",dep.get("depositId"));
                    for(BankDepositor depR:depositorService.getBankDepositorByIdDeposit((Long)dep.get("depositId"))){
                        if( ( depR.getDepositorDateDeposit().after(dateStart)
                                &depR.getDepositorDateDeposit().before(dateEnd) )
                                ||depR.getDepositorDateDeposit().equals(dateStart)
                                ||depR.getDepositorDateDeposit().equals(dateEnd) ){
                            depositors.add(depR);
                            LOGGER.debug("depositor - {}",depR);
                        }
                    }
                }

                year = startDate.substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}", idDeposit,e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByCurrencyFromToDateDeposit({},{},{}), Exception:{}"
                    ,depositCurrency,startDate,endDate,e.toString());
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
     * Get Bank Deposits by Currency with depositors from-to Date return Deposit values
     *
     * @param redirectAttributes
     * @param depositCurrency String - Currency of the Bank Deposit to return
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByCurrencyFromToDateReturnDeposit"}, method = RequestMethod.GET)
    public ModelAndView filterByCurrencyFromToDateReturnDeposit(RedirectAttributes redirectAttributes,
                                                                @RequestParam("depositCurrency") String depositCurrency,
                                                                @RequestParam("startDateReturnDeposit") String startDate,
                                                                @RequestParam("endDateReturnDeposit") String endDate,
                                                                SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByCurrencyFromToDateReturnDeposit(depositCurrency={},start={},end={})"
                ,depositCurrency,startDate,endDate);
        status.setComplete();
        try{
            Assert.hasLength(depositCurrency,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(startDate,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(endDate,ERROR_STRING_METHOD_PARAM);

            Date dateStart = dateFormat.parse(startDate), dateEnd = dateFormat.parse(endDate);
            Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

            LOGGER.debug("getBankDepositByCurrencyFromToDateReturnDepositWithDepositors(depositCurrency={}," +
                    "start={},end={})",depositCurrency,startDate,endDate);
            deposits = depositService.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(depositCurrency
                    ,dateStart,dateEnd);
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                for(Map dep:deposits){
                    LOGGER.debug("getBankDepositorByIdDepositFromToDateDeposit(depositId={})",dep.get("depositId"));
                    for(BankDepositor depR:depositorService.getBankDepositorByIdDeposit((Long)dep.get("depositId"))){
                        if( ( depR.getDepositorDateReturnDeposit().after(dateStart)
                                &depR.getDepositorDateReturnDeposit().before(dateEnd) )
                                ||depR.getDepositorDateReturnDeposit().equals(dateStart)
                                ||depR.getDepositorDateReturnDeposit().equals(dateEnd) ){
                            depositors.add(depR);
                            LOGGER.debug("depositor - {}",depR);
                        }
                    }
                }

                year = startDate.substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}", idDeposit,e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByCurrencyFromToDateReturnDeposit({},{},{}), Exception:{}"
                    ,depositCurrency,startDate,endDate,e.toString());
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
     * Get Bank Deposits from-to Date Deposit values
     *
     * @param redirectAttributes
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByDateDeposit"}, method = RequestMethod.GET)
    public ModelAndView filterByDateDeposit(RedirectAttributes redirectAttributes,
                                            @RequestParam("startDateDeposit") String startDate,
                                            @RequestParam("endDateDeposit") String endDate,
                                            SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByDateDeposit(start={},end={})",startDate,endDate);
        status.setComplete();
        try{
            Assert.hasLength(startDate,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(endDate,ERROR_STRING_METHOD_PARAM);

            Date dateStart = dateFormat.parse(startDate), dateEnd = dateFormat.parse(endDate);
            Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

            LOGGER.debug("getBankDepositByDateDepositWithDepositors(start={},end={})",startDate,endDate);
            deposits = depositService.getBankDepositsFromToDateDepositWithDepositors(dateStart,dateEnd);
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                for(Map dep:deposits){
                    LOGGER.debug("getBankDepositorByIdDepositFromToDateDeposit(depositId={})",dep.get("depositId"));
                    for(BankDepositor depR:depositorService.getBankDepositorByIdDeposit((Long)dep.get("depositId"))){
                        if( ( depR.getDepositorDateDeposit().after(dateStart)
                                &depR.getDepositorDateDeposit().before(dateEnd) )
                                ||depR.getDepositorDateDeposit().equals(dateStart)
                                ||depR.getDepositorDateDeposit().equals(dateEnd) ){
                            depositors.add(depR);
                            LOGGER.debug("depositor - {}",depR);
                        }
                    }
                }

                year = startDate.substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}", idDeposit,e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByDateDeposit({},{}), Exception:{}",startDate,endDate,e.toString());
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
     * Get Bank Deposits from-to Date Return Deposit values
     *
     * @param redirectAttributes
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByDateReturnDeposit"}, method = RequestMethod.GET)
    public ModelAndView filterByDateReturnDeposit(RedirectAttributes redirectAttributes,
                                                  @RequestParam("startDateDeposit") String startDate,
                                                  @RequestParam("endDateDeposit") String endDate,
                                                  SessionStatus status) throws ParseException
    {
        LOGGER.debug("filterByDateReturnDeposit(start={},end={})",startDate,endDate);
        status.setComplete();
        try{
            Assert.hasLength(startDate,ERROR_STRING_METHOD_PARAM);
            Assert.hasLength(endDate,ERROR_STRING_METHOD_PARAM);

            Date dateStart = dateFormat.parse(startDate), dateEnd = dateFormat.parse(endDate);
            Assert.isTrue(dateStart.before(dateEnd)||dateStart.equals(dateEnd),ERROR_FROM_TO_PARAM);

            LOGGER.debug("getBankDepositByDateReturnDepositWithDepositors(start={},end={})",startDate,endDate);
            deposits = depositService.getBankDepositsFromToDateReturnDepositWithDepositors(dateStart,dateEnd);
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                for(Map dep:deposits){
                    LOGGER.debug("getBankDepositorByIdDepositFromToDateReturnDeposit(depositId={})",dep.get("depositId"));
                    for(BankDepositor depR:depositorService.getBankDepositorByIdDeposit((Long)dep.get("depositId"))){
                        if( ( depR.getDepositorDateReturnDeposit().after(dateStart)
                                &depR.getDepositorDateReturnDeposit().before(dateEnd) )
                                ||depR.getDepositorDateReturnDeposit().equals(dateStart)
                                ||depR.getDepositorDateReturnDeposit().equals(dateEnd) ){
                            depositors.add(depR);
                            LOGGER.debug("depositor - {}",depR);
                        }
                    }
                }

                year = startDate.substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}", idDeposit,e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByDateReturnDeposit({},{}), Exception:{}",startDate,endDate,e.toString());
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
     * Get Bank Deposits by Amount with depositors
     *
     * @param redirectAttributes
     * @param status
     * @param request HttpServletRequest - Variant number of arguments
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = {"/filterByCriteria"}, method = RequestMethod.GET)
    public ModelAndView filterByCriteria(RedirectAttributes redirectAttributes,
                                         SessionStatus status,
                                         HttpServletRequest request) throws ParseException
    {
        LOGGER.debug("filterByCriteria(request-{})",request);
        Assert.notNull(request,ERROR_METHOD_PARAM+" - request");

        Map<String, String[]> parameters = request.getParameterMap();

        int number = 0;
        for(String key : parameters.keySet()) {
            number++;
            System.out.println(key);
            String[] vals = parameters.get(key);
            number+=vals.length-1;
            for(String val : vals) {
                number++;
                System.out.println(" -> " + val);
            }
        }

        status.setComplete();

        //==== Map<String,String[]> to Object...args
        Object[] args = new Object[number];
        int i=0;
        for(String key : parameters.keySet()) {
            if(i<number){
                args[i]=key;
                i++;
                String[] vals = parameters.get(key);
                int count = vals.length;
                for(String val : vals) {
                    count--;
                    if(count == 0){
                        args[i]=val;
                        i++;
                    } else{
                        args[i]=val;
                        i++;
                        args[i]=key;
                        i++;
                    }
                }
            }
        }

        //====
        try{
            Assert.notNull(parameters,ERROR_METHOD_PARAM+" - parameters");
            LOGGER.debug("getBankDepositsByVarArgs(parameters={} or args={})",parameters,args);
            deposits = depositService.getBankDepositsByVarArgs(args);
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                depositors = new ArrayList<BankDepositor>();
                for(Map depositReport:deposits){
                    LOGGER.debug("getBankDepositorByIdDeposit(depositId={})",depositReport.get("depositId"));
                    depositors.addAll(depositorService
                            .getBankDepositorByIdDeposit((Long)depositReport.get("depositId")));
                }
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={}), Exception:{}",
                        deposits.get(0).get("depositId"),e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("filterByCriteria({}), Exception:{}", parameters,e.toString());
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
     * Get Bank Deposit with depositors
     *
     * @param redirectAttributes
     * @param status
     * @return ModelAndView "mainFrame"
     * @throws ParseException
     */
    @RequestMapping(value = "/main")
    public ModelAndView getListDepositsView(RedirectAttributes redirectAttributes,
                                            SessionStatus status) throws ParseException
    {
        LOGGER.debug("getListDepositsView()");

        status.setComplete();
        try{
            LOGGER.debug("getBankDepositsWithDepositors()");
            deposits = depositService.getBankDepositsWithDepositors();
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                LOGGER.debug("getBankDepositors()");
                depositors = depositorService.getBankDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));

                year = dateFormat.format(depositors.get(0).getDepositorDateDeposit()).substring(0,4);
                LOGGER.debug("year={}",year);
            }catch (Exception e){
                LOGGER.debug("getBankDepositors(), Exception:{}",e.toString());
                redirectAttributes.addFlashAttribute( "message", e.getMessage());

                depositors = DataConfig.getEmptyDepositors();
                LOGGER.debug("depositors - {}",depositors.get(0));
                year = dateFormat.format(Calendar.getInstance().getTime()).substring(0,4);
                LOGGER.debug("year={}",year);
            }
        }catch (Exception e){
            LOGGER.debug("getListDepositsView(), Exception:{}", e.toString());
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
