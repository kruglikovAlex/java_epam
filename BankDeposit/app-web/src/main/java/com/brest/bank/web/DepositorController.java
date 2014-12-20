package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;
import com.brest.bank.service.BankDepositService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;


@Controller
public class DepositorController {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BankDepositorService depositorService;

    @Autowired
    private BankDepositService depositService;

    @RequestMapping("/inputDepositor")
    public ModelAndView InputForm() {
        return new ModelAndView("inputFormDepositor", "depositor", new BankDepositor());
    }

    @RequestMapping(value={"/inputFormDepositor"}, method = RequestMethod.GET)
    public ModelAndView launchAddForm(RedirectAttributes redirectAttributes,
                                         @RequestParam("depositorIdDeposit")Long depositorIdDeposit
                                     ) {
        try {
            BankDepositor depositor = new BankDepositor();
            depositor.setDepositorIdDeposit(depositorIdDeposit);
            return new ModelAndView("inputFormDepositor", "depositor", depositor);
        }catch(Exception e) {
            LOGGER.debug(e);
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/depositorsList");
        }
    }

    //====
    @RequestMapping("/filterBetweenDateDeposit")
    public ModelAndView getFilterBetweenDate(@RequestParam("StartDateDeposit")String StartDateDeposit,
                                             @RequestParam("EndDateDeposit")String EndDateDeposit
                                            )throws ParseException{
        Date startDate = dateFormat.parse(StartDateDeposit);
        Date endDate = dateFormat.parse(EndDateDeposit);
        List<BankDepositor> depositors = depositorService.getBankDepositorBetweenDateDeposit(startDate, endDate);
        LOGGER.debug("depositors.size = " + depositors.size());
        List<BankDeposit> deposits = depositService.getBankDeposits();
        LOGGER.debug("deposits.size = " + deposits.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",deposits);

        return  view;
    }

    @RequestMapping("/filterBetweenDateReturnDeposit")
    public ModelAndView getFilterBetweenDateReturn( @RequestParam("StartDateReturnDeposit")String StartDateDeposit,
                                                    @RequestParam("EndDateReturnDeposit")String EndDateDeposit
    )throws ParseException{
        Date startDate = dateFormat.parse(StartDateDeposit);
        Date endDate = dateFormat.parse(EndDateDeposit);
        List<BankDepositor> depositors = depositorService.getBankDepositorBetweenDateReturnDeposit(startDate, endDate);
        LOGGER.debug("depositors.size = " + depositors.size());
        List<BankDeposit> deposits = depositService.getBankDeposits();
        LOGGER.debug("deposits.size = " + deposits.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",deposits);

        return  view;
    }

    @RequestMapping("/filterByIdDepositor")
    public ModelAndView getFilterDepositorById(@RequestParam("depositorById")Long depositorById
    ){
        BankDepositor depositor = depositorService.getBankDepositorById(depositorById);
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
        depositors.add(depositor);
        LOGGER.debug("depositors.size = " + depositors.size());
        List<BankDeposit> deposits = depositService.getBankDeposits();
        LOGGER.debug("deposits.size = " + deposits.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",deposits);

        return  view;
    }

    @RequestMapping("/filterByIdDepositDepositor")
    public ModelAndView getFilterDepositorByIdDeposit(@RequestParam("depositorByIdDeposit")Long depositorByIdDeposit
    ){
        List<BankDepositor> depositors = depositorService.getBankDepositorByIdDeposit(depositorByIdDeposit);
        LOGGER.debug("depositors.size = " + depositors.size());
        List<BankDeposit> deposits = depositService.getBankDeposits();
        LOGGER.debug("deposits.size = " + deposits.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",deposits);

        return  view;
    }

    @RequestMapping("/filterByNameDepositor")
    public ModelAndView getFilterDepositorByName(@RequestParam("depositorByName")String depositorByName
    ){
        BankDepositor depositor = depositorService.getBankDepositorByName(depositorByName);
        List<BankDepositor> depositors = new ArrayList<BankDepositor>();
        depositors.add(depositor);
        LOGGER.debug("depositors.size = " + depositors.size());
        List<BankDeposit> deposits = depositService.getBankDeposits();
        LOGGER.debug("deposits.size = " + deposits.size());

        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",deposits);

        return  view;
    }
    //====

    @RequestMapping(value={"/inputFormDepositor"}, method=RequestMethod.POST)
    public String getInputForm( @RequestParam("depositorName")String depositorName,
    							@RequestParam("depositorIdDeposit")Long depositorIdDeposit,
    							@RequestParam("depositorDateDeposit")String depositorDateDeposit,
    							@RequestParam("depositorAmountDeposit")Integer depositorAmountDeposit,
    							@RequestParam("depositorAmountPlusDeposit")Integer depositorAmountPlusDeposit,
    							@RequestParam("depositorAmountMinusDeposit")Integer depositorAmountMinusDeposit,
    							@RequestParam("depositorDateReturnDeposit")String depositorDateReturnDeposit,
    							@RequestParam("depositorMarkReturnDeposit")Integer depositorMarkReturnDeposit
                                ) throws ParseException {
        BankDepositor depositor = new BankDepositor();
        
        depositor.setDepositorName(depositorName);
        depositor.setDepositorIdDeposit(depositorIdDeposit);
        depositor.setDepositorDateDeposit(dateFormat.parse(depositorDateDeposit));
        depositor.setDepositorAmountDeposit(depositorAmountDeposit);
        depositor.setDepositorAmountPlusDeposit(depositorAmountPlusDeposit);
        depositor.setDepositorAmountMinusDeposit(depositorAmountMinusDeposit);
        depositor.setDepositorDateReturnDeposit(dateFormat.parse(depositorDateReturnDeposit));
        depositor.setDepositorMarkReturnDeposit(depositorMarkReturnDeposit);
        
        depositorService.addBankDepositor(depositor);
        return "redirect:/depositorsList";
        
    }

    @RequestMapping(value={"/updateFormDepositor"}, method = RequestMethod.GET)
    public ModelAndView launchUpdateForm(RedirectAttributes redirectAttributes,
                                         @RequestParam("depositorId")Long depositorId
                                        ) {

        try {
            return new ModelAndView("updateFormDepositor", "depositor", depositorService.getBankDepositorById(depositorId));

        }catch(Exception e) {
            LOGGER.debug(e);
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/depositorsList");
        }
    }

    @RequestMapping(value={"/updateFormDepositor"}, method = RequestMethod.POST)
    public ModelAndView getUpdateForm(RedirectAttributes redirectAttributes,
                                      @RequestParam("depositorId") Long depositorId,
                                      @RequestParam("depositorName")String depositorName,
                                      @RequestParam("depositorIdDeposit")Long depositorIdDeposit,
                                      @RequestParam("depositorDateDeposit")String depositorDateDeposit,
                                      @RequestParam("depositorAmountDeposit")Integer depositorAmountDeposit,
                                      @RequestParam("depositorAmountPlusDeposit")Integer depositorAmountPlusDeposit,
                                      @RequestParam("depositorAmountMinusDeposit")Integer depositorAmountMinusDeposit,
                                      @RequestParam("depositorDateReturnDeposit")String depositorDateReturnDeposit,
                                      @RequestParam("depositorMarkReturnDeposit")Integer depositorMarkReturnDeposit
    ) throws ParseException {
        BankDepositor depositor = new BankDepositor();

        depositor.setDepositorId(depositorId);
        depositor.setDepositorName(depositorName);
        depositor.setDepositorIdDeposit(depositorIdDeposit);
        depositor.setDepositorDateDeposit(dateFormat.parse(depositorDateDeposit));
        depositor.setDepositorAmountDeposit(depositorAmountDeposit);
        depositor.setDepositorAmountPlusDeposit(depositorAmountPlusDeposit);
        depositor.setDepositorAmountMinusDeposit(depositorAmountMinusDeposit);
        depositor.setDepositorDateReturnDeposit(dateFormat.parse(depositorDateReturnDeposit));
        depositor.setDepositorMarkReturnDeposit(depositorMarkReturnDeposit);


        try {
            depositorService.updateBankDepositor(depositor);
            return new ModelAndView("redirect:/depositorsList");
        }catch(Exception e) {
            LOGGER.debug(e);
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/updateFormDepositor", "depositorId", depositorId);
        }
    }

    @RequestMapping(value={"/delete"}, method = RequestMethod.GET)
    public ModelAndView launchDeleteForm(RedirectAttributes redirectAttributes,
                                         @RequestParam("depositorId")Long depositorId
                                        ) {

        try {
            depositorService.removeBankDepositor(depositorId);
            return new ModelAndView("redirect:/depositorsList");
        }catch(Exception e) {
            LOGGER.debug(e);
            redirectAttributes.addFlashAttribute( "message", e.getMessage());
            return new ModelAndView("redirect:/depositorsList");
        }
    }

    @RequestMapping("/depositorsList")
    public ModelAndView getListDepositorsView() {
        List<BankDepositor> depositors = depositorService.getBankDepositors();
        List<BankDeposit> deposits = depositService.getBankDeposits();
        LOGGER.debug("depositors.size = " + depositors.size());
        ModelAndView view = new ModelAndView("depositsList", "depositors", depositors);
        view.addObject("deposits",deposits);
        return view;
    }
}