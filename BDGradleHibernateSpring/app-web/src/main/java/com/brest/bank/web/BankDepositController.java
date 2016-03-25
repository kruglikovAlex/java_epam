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
     * @param status
     * @return
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
     *
     * @param map
     * @param deposit
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
     *
     * @param redirectAttributes
     * @param depositId
     * @param status
     * @return
     */
    @RequestMapping(value = {"/deleteDeposit"}, method = RequestMethod.DELETE)
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
            deposits.add(depositService.getBankDepositByIdWithDepositors(depositId));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={})",depositId);
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
            deposits.add(depositService.getBankDepositByIdFromToDateDepositWithDepositors(depositId,dateStart,dateEnd));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                LOGGER.debug("getBankDepositorByIdDepositFromToDateDeposit(depositId={})",depositId);
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
            deposits.add(depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(depositId,dateStart,dateEnd));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                LOGGER.debug("getBankDepositorByIdDepositFromToDateDeposit(depositId={})",depositId);
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
            deposits.add(depositService.getBankDepositByNameWithDepositors(depositName));
            LOGGER.debug("deposits - {}",deposits.get(0));

            idDeposit = (Long)deposits.get(0).get("depositId");
            Assert.notNull(idDeposit,"idDeposit can not be NULL");

            try{
                LOGGER.debug("getBankDepositorByIdDeposit(depositId={})",deposits.get(0).get("depositId"));
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
