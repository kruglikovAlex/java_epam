package com.brest.bank.rest;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired; //Dependency lookup
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/deposits")
public class DepositRestController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BankDepositService depositService;

	//--- get Deposit by Id
    @RequestMapping(value = "/deposits/{depositId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BankDeposit> getDepositById(@PathVariable Long depositId) {
        LOGGER.debug("getDepositById({})",depositId);
        try {
            BankDeposit deposit = depositService.getBankDepositById(depositId);
            return new ResponseEntity(deposit, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("getDepositById({}), Exception:{}", depositId, e.toString());
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

	//--- get Deposit by name
    @RequestMapping(value = "/deposits/name/{depositName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BankDeposit> getDepositByName(@PathVariable String depositName) {
        LOGGER.debug("getDepositByName({})",depositName);
        try {
    		BankDeposit deposit = depositService.getBankDepositByName(depositName);
            return new ResponseEntity(deposit, HttpStatus.OK);
    	} catch(Exception e){
            LOGGER.error("getDepositByName({}), Exception:{}", depositName, e.toString());
    		return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
    	}
    }

	//--- get all Deposits
    @ResponseBody
    @RequestMapping(value = "/deposits",method= RequestMethod.GET)
    public ResponseEntity<List<BankDeposit>> getDeposits() {
        LOGGER.debug("getDeposits()");
        try {
        	List deposits = depositService.getBankDeposits();
            return new ResponseEntity(deposits, HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("getDeposits(), Exception:{}", e.toString());
        	return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

	//--- add Deposit
    @ResponseBody
    @RequestMapping(value = "/deposits",method= RequestMethod.POST)
    public ResponseEntity<Long> addDeposit(@RequestBody BankDeposit deposit) {
        LOGGER.debug("addDeposit({})", deposit);
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
            LOGGER.error("addDeposit({}), Exception:{}", deposit, e.toString());
        	return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    //--- Update Deposit
    @RequestMapping(value = "/deposits",method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity updateDeposit(@RequestBody BankDeposit deposit) {
        LOGGER.debug("updateDeposit({})", deposit);
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
            LOGGER.error("updateDeposit({}), Exception:{}", deposit, e.toString());
        	return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

	//--- Remove Deposit
    @RequestMapping(value = "/deposits/{depositId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity removeDeposit(@PathVariable Long depositId) {
        LOGGER.debug("removeDeposit({})", depositId);
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
            LOGGER.error("removeDeposit({}), Exception:{}", depositId, e.toString());
        	return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}