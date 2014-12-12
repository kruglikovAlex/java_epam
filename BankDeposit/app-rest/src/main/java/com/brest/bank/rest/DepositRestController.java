package com.brest.bank.rest;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositService;

import org.springframework.beans.factory.annotation.Autowired; //Dependency lookup
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/deposits")
public class DepositRestController {

    @Autowired
    private BankDepositService depositService;

	//--- getBankDepositById()
    @RequestMapping(value = "/{depositId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BankDeposit> getDepositById(@PathVariable Long depositId) {
        try {
            BankDeposit deposit = depositService.getBankDepositById(depositId);
            return new ResponseEntity(deposit, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

	//--- getDepositByName()
    @RequestMapping(value = "/name/{depositName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BankDeposit> getDepositByName(@PathVariable String depositName) {
    	try {
    		BankDeposit deposit = depositService.getBankDepositByName(depositName);
            return new ResponseEntity(deposit, HttpStatus.OK);
    	} catch(Exception e){
    		return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
    	}
    }

	//--- getDeposits()
    @ResponseBody
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<List<BankDeposit>> getDeposits() {
        try {
        	List deposits = depositService.getBankDeposits();
            return new ResponseEntity(deposits, HttpStatus.OK);
        } catch (Exception e){
        	return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

	//--- addDeposit()
    @ResponseBody
    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity<Long> addDeposit(@RequestBody BankDeposit deposit) {
        try {
            if (deposit == null){
                throw new Exception("Can not be added to the database NULL deposit");
            }
            if (deposit.getDepositName()==null) {
                throw new Exception("Can not be added to the database Empty deposit");
            }
            Long depositId = depositService.addBankDeposit(deposit);
            return new ResponseEntity(depositId, HttpStatus.CREATED);
        }catch (Exception e){
        	return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    //--- UpdateDeposit()
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity updateDeposit(@RequestBody BankDeposit deposit) {
        try {
            if (deposit == null){
                throw new Exception("You can not upgrade NULL deposit");
            }
            if (deposit.getDepositName()==null) {
                throw new Exception("You can not upgrade EMPTY deposit");
            }
        	depositService.updateBankDeposit(deposit);
            return new ResponseEntity("", HttpStatus.OK);
        } catch (Exception e){
        	return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

	//--- RemoveDeposit()
    @RequestMapping(value = "/{depositId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity removeDeposit(@PathVariable Long depositId) {
        try {
            if (Long.valueOf(depositId) < 0) {
                throw new Exception("Id Deposit - incorrect");
            }
            if (depositId == null) {
                throw new Exception("Deposit can not be removed with null id");
            }
            depositService.removeBankDeposit(depositId);
            return new ResponseEntity("", HttpStatus.OK);
        }catch (Exception e){
        	return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}