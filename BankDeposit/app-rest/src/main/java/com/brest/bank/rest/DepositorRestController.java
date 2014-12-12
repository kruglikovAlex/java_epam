package com.brest.bank.rest;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired; //Dependency lookup
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/depositors")
public class DepositorRestController {
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
    @Autowired
    private BankDepositorService depositorService;

	//--- getBankDepositorById()
    @RequestMapping(value = "/{depositorId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BankDepositor> getDepositorById(@PathVariable Long depositorId) {
        try {
            BankDepositor depositor = depositorService.getBankDepositorById(depositorId);
            return new ResponseEntity(depositor, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Depositor not found for id=" + depositorId + " error:"
                    + ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

	//--- getBankDepositorByIdDeposit()
    @RequestMapping(value = "/deposit/{depositorIdDeposit}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<BankDepositor>> getDepositorByIdDeposit(@PathVariable Long depositorIdDeposit) {
        try {
            List depositors = depositorService.getBankDepositorByIdDeposit(depositorIdDeposit);
            return new ResponseEntity(depositors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Depositor not found for Deposit Id=" + depositorIdDeposit + " error:"
                    + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

	//--- getDepositorByName()
    @RequestMapping(value = "/name/{depositorName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BankDepositor> getDepositorByName(@PathVariable String depositorName) {
        try{
        	BankDepositor depositor = depositorService.getBankDepositorByName(depositorName);
            return new ResponseEntity(depositor, HttpStatus.OK);
        } catch (Exception e) {
        	return new ResponseEntity("Depositor not found for Deposit Name="+depositorName+ " error:"
        			+e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

	//--- getDepositorBetweenDateDeposit()
    @RequestMapping(value = "/date/{DateDeposit1}/{DateDeposit2}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<BankDepositor>> getBankDepositorBetweenDateDeposit(@PathVariable String DateDeposit1, @PathVariable String DateDeposit2) {
        try{
        	List depositors = depositorService.getBankDepositorBetweenDateDeposit(dateFormat.parse(DateDeposit1), dateFormat.parse(DateDeposit2));
            return new ResponseEntity(depositors, HttpStatus.OK);
        } catch (Exception e){
        	return new ResponseEntity("Depositors not found for Dates Deposit="+DateDeposit1+" and "+DateDeposit2+" error:"
        			+e.getMessage()+", Cause="+e.getCause(), HttpStatus.NOT_FOUND);
        }
    }

	//--- getDepositorBetweenDateReturnDeposit()
    @RequestMapping(value = "/date/return/{DateDeposit1}/{DateDeposit2}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<BankDepositor>> getDepositorBetweenDateReturnDeposit(@PathVariable String DateDeposit1, @PathVariable String DateDeposit2) {
        try{
        	List depositors = depositorService.getBankDepositorBetweenDateReturnDeposit(dateFormat.parse(DateDeposit1), dateFormat.parse(DateDeposit2));
            return new ResponseEntity(depositors, HttpStatus.OK);
        } catch (Exception e){
        	return new ResponseEntity("Depositors not found for Dates Return Deposit="+DateDeposit1+" and "+DateDeposit2+" error:"
        			+e.getMessage()+", Cause="+e.getCause(), HttpStatus.NOT_FOUND);
        }
    }
	
	//--- getDepositors()
    @RequestMapping(method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<BankDepositor>> getDepositors() {
    	try {
    		List depositors = depositorService.getBankDepositors();
            return new ResponseEntity(depositors, HttpStatus.OK);
    	} catch (Exception e){
    		return new ResponseEntity("Depositor not found - BD is Empty, error:"
    				+e.getMessage(), HttpStatus.NOT_FOUND);
    	}
    }

	//--- addDepositor()
    @RequestMapping(method= RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Long> addDepositor(@RequestBody BankDepositor depositor) {
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
        	return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

	//--- updateDepositor()
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity updateDepositor(@RequestBody BankDepositor depositor) {
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
        	return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

	//--- removeDepositor()
    @RequestMapping(value = "/{depositorId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity removeDepositor(@PathVariable Long depositorId) {
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
        	return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

	//--- removeDepositorByIdDeposit()
    @RequestMapping(value = "/deposit/{depositorIdDeposit}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity removeDepositorByIdDeposit(@PathVariable Long depositorIdDeposit) {
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
        	return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}