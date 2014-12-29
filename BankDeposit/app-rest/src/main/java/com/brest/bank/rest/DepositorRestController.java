package com.brest.bank.rest;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired; //Dependency loookup
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/depositors")
public class DepositorRestController {
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger LOGGER = LogManager.getLogger();
	
    @Autowired
    private BankDepositorService depositorService;

	//--- getBankDepositorById()
    @RequestMapping(value = "/depositors/{depositorId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BankDepositor> getDepositorById(@PathVariable Long depositorId) {
        LOGGER.debug("getDepositorById({})",depositorId);
        try {
            BankDepositor depositor = depositorService.getBankDepositorById(depositorId);
            return new ResponseEntity(depositor, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("getDepositorById({}), Exception:{}", depositorId, e.toString());
            return new ResponseEntity("Depositor not found for id=" + depositorId + " error:"
                    + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

	//--- getBankDepositorByIdDeposit()
    @RequestMapping(value = "/depositors/deposit/{depositorIdDeposit}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<BankDepositor>> getDepositorByIdDeposit(@PathVariable Long depositorIdDeposit) {
        LOGGER.debug("getDepositorByIdDeposit({})",depositorIdDeposit);
        try {
            List depositors = depositorService.getBankDepositorByIdDeposit(depositorIdDeposit);
            return new ResponseEntity(depositors, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("getDepositorByIdDeposit({}), Exception:{}", depositorIdDeposit, e.toString());
            return new ResponseEntity("Depositor not found for Deposit Id=" + depositorIdDeposit + " error:"
                    + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

	//--- getDepositorByName()
    @RequestMapping(value = "/depositors/name/{depositorName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BankDepositor> getDepositorByName(@PathVariable String depositorName) {
        LOGGER.debug("getDepositorByName({})",depositorName);
        try{
        	BankDepositor depositor = depositorService.getBankDepositorByName(depositorName);
            return new ResponseEntity(depositor, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("getDepositorByName({}), Exception:{}", depositorName, e.toString());
        	return new ResponseEntity("Depositor not found for Deposit Name="+depositorName+ " error:"
        			+e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

	//--- getDepositorBetweenDateDeposit()
    @RequestMapping(value = "/depositors/date/{DateDeposit1}/{DateDeposit2}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<BankDepositor>> getBankDepositorBetweenDateDeposit(@PathVariable String DateDeposit1, @PathVariable String DateDeposit2) {
        LOGGER.debug("getBankDepositorBetweenDateDeposit({},{})",DateDeposit1,DateDeposit2);
        try{
        	List depositors = depositorService.getBankDepositorBetweenDateDeposit(dateFormat.parse(DateDeposit1), dateFormat.parse(DateDeposit2));
            return new ResponseEntity(depositors, HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("getBankDepositorBetweenDateDeposit({},{}), Exception:{}", DateDeposit1,DateDeposit2, e.toString());
        	return new ResponseEntity("Depositors not found for Dates Deposit="+DateDeposit1+" and "+DateDeposit2+" error:"
        			+e.getMessage()+", Cause="+e.getCause(), HttpStatus.NOT_FOUND);
        }
    }

	//--- getDepositorBetweenDateReturnDeposit()
    @RequestMapping(value = "/depositors/date/return/{DateDeposit1}/{DateDeposit2}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<BankDepositor>> getDepositorBetweenDateReturnDeposit(@PathVariable String DateDeposit1, @PathVariable String DateDeposit2) {
        LOGGER.debug("getDepositorBetweenDateReturnDeposit({},{})",DateDeposit1,DateDeposit2);
        try{
        	List depositors = depositorService.getBankDepositorBetweenDateReturnDeposit(dateFormat.parse(DateDeposit1), dateFormat.parse(DateDeposit2));
            return new ResponseEntity(depositors, HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("getDepositorBetweenDateReturnDeposit({},{}), Exception:{}", DateDeposit1,DateDeposit2, e.toString());
        	return new ResponseEntity("Depositors not found for Dates Return Deposit="+DateDeposit1+" and "+DateDeposit2+" error:"
        			+e.getMessage()+", Cause="+e.getCause(), HttpStatus.NOT_FOUND);
        }
    }
	
	//--- getDepositors()
    @RequestMapping(value = "/depositors",method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<BankDepositor>> getDepositors() {
        LOGGER.debug("getDepositors()");
        try {
    		List depositors = depositorService.getBankDepositors();
            return new ResponseEntity(depositors, HttpStatus.OK);
    	} catch (Exception e){
            LOGGER.error("getDepositors(), Exception:{}", e.toString());
    		return new ResponseEntity("Depositor not found - BD is Empty, error:"
    				+e.getMessage(), HttpStatus.NOT_FOUND);
    	}
    }

	//--- addDepositor()
    @RequestMapping(value = "/depositors",method= RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Long> addDepositor(@RequestBody BankDepositor depositor) {
        LOGGER.debug("addDepositor({})", depositor);
        try {
            if (depositor == null){
                throw new Exception("Can not be added to the database NULL depositor");
            }
            if (depositor.getDepositorName()==null) {
                throw new Exception("Can not be added to the database Empty depositor");
            }
        	Long depositorId = depositorService.addBankDepositor(depositor);
            return new ResponseEntity(depositorId, HttpStatus.CREATED);
        } catch (Exception e){
            LOGGER.error("addDepositor({}), Exception:{}", depositor, e.toString());
        	return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

	//--- updateDepositor()
    @RequestMapping(value = "/depositors",method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity updateDepositor(@RequestBody BankDepositor depositor) {
        LOGGER.debug("updateDepositor({})", depositor);
        try{
            if (depositor == null){
                throw new Exception("You can not upgrade NULL depositor");
            }
            if (depositor.getDepositorName()==null) {
                throw new Exception("You can not upgrade EMPTY depositor");
            }
        	depositorService.updateBankDepositor(depositor);
            return new ResponseEntity("", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("updateDepositor({}), Exception:{}", depositor, e.toString());
        	return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

	//--- removeDepositor()
    @RequestMapping(value = "/depositors/{depositorId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity removeDepositor(@PathVariable Long depositorId) {
        LOGGER.debug("removeDepositor({})", depositorId);
        try {
            if (Long.valueOf(depositorId) < 0) {
                throw new Exception("Id Depositor - incorrect");
            }
            if (depositorId == null) {
                throw new Exception("Depositor can not be removed with null id");
            }
        	depositorService.removeBankDepositor(depositorId);
            return new ResponseEntity("", HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("removeDepositor({}), Exception:{}", depositorId, e.toString());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

	//--- removeDepositorByIdDeposit()
    @RequestMapping(value = "/depositors/deposit/{depositorIdDeposit}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity removeDepositorByIdDeposit(@PathVariable Long depositorIdDeposit) {
        LOGGER.debug("removeDepositorByIdDeposit({})", depositorIdDeposit);
        try {
            if (Long.valueOf(depositorIdDeposit) < 0) {
                throw new Exception("Id Deposit of depositor - incorrect");
            }
            if (depositorIdDeposit == null) {
                throw new Exception("Depositor can not be removed with null ID deposit");
            }
        	depositorService.removeBankDepositorByIdDeposit(depositorIdDeposit);
            return new ResponseEntity("", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("removeDepositorByIdDeposit({}), Exception:{}", depositorIdDeposit, e.toString());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
