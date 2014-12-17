package com.brest.bank.web;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;
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
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;


@Controller
public class DepositorController {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BankDepositorService depositorService;

    @RequestMapping("/inputFormDepositor")
    public ModelAndView launchInputForm() {
        return new ModelAndView("inputFormDepositor", "depositor", new BankDepositor());
    }

    @RequestMapping("/submitDataDepositor")
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
        LOGGER.debug("depositors.size = " + depositors.size());
        ModelAndView view = new ModelAndView("depositorsList", "depositors", depositors);
        return view;
    }
}