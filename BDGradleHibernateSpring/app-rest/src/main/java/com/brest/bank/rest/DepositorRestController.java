package com.brest.bank.rest;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
//@ContextConfiguration(locations = {"classpath:/spring-rest.xml"})
@RequestMapping("/depositor")
public class    DepositorRestController {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

    @Autowired
    BankDepositorService depositorService;

    /**
     * Get all Bank Depositors
     *
     * @return ResponseEntity List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    @ResponseBody
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResponseEntity<List<BankDepositor>> getBankDepositors(){
        LOGGER.debug("getBankDepositors()");
        try{
            List<BankDepositor> depositors = depositorService.getBankDepositors();
            return new ResponseEntity<List<BankDepositor>>(depositors, HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositors(), Exception:{}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get all Bank Depositors from-to Date Deposit
     *
     * @param start String - start value of the date deposit (startDate < endDate)
     * @param end String - end value of the date deposit (startDate < endDate)
     * @return RespnseEntity List<BankDepositors> a list of all bank depositors with the specified task`s date deposit
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/allDate/{start},{end}",method = RequestMethod.GET)
    public ResponseEntity<List<BankDepositor>> getBankDepositorsFromToDateDeposit(@PathVariable String start,
                                                                                  @PathVariable String end)
                                                                                    throws ParseException{
        LOGGER.debug("getBankDepositorsFromToDateDeposit(start={},end={})",start,end);
        try{
            List<BankDepositor> depositors = depositorService.getBankDepositorsFromToDateDeposit(
                    dateFormat.parse(start),dateFormat.parse(end));
            return new ResponseEntity<List<BankDepositor>>(depositors,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositorsFromToDateDeposit(start={},end={}), Exception:{}",start,end,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Depositor by ID
     *
     * @param depositorId  Long - id of the Bank Depositor to return
     * @return ResponseEntity BankDepositor with the specified id from the database
     */
    @ResponseBody
    @RequestMapping(value = "/id/{depositorId}",method = RequestMethod.GET)
    public ResponseEntity<BankDepositor> getBankDepositorById(@PathVariable Long depositorId){
        LOGGER.debug("getBankDepositorById(depositorId={})",depositorId);
        try{
            BankDepositor depositor = depositorService.getBankDepositorById(depositorId);
            return new ResponseEntity<BankDepositor>(depositor,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositorById(depositorId={}), Exception:{}",depositorId,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Depositor by Name
     *
     * @param depositorName  String - name of the Bank Depositor to return
     * @return ResponseEntity BankDepositor with the specified id from the database
     */
    @ResponseBody
    @RequestMapping(value = "/name/{depositorName}",method = RequestMethod.GET)
    public ResponseEntity<BankDepositor> getBankDepositorByName(@PathVariable String depositorName){
        LOGGER.debug("getBankDepositorByName(name={})",depositorName);
        try{
            BankDepositor depositor = depositorService.getBankDepositorByName(depositorName);
            return new ResponseEntity<BankDepositor>(depositor,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositorByName(name={}), Exception:{}",depositorName,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Adding Bank Depositor
     *
     * @param depositor BankDepositor - Bank Depositor to be inserted to the database
     * @param depositId id of Bank Deposit
     * @return ResponseEntity
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/{depositId}",method= RequestMethod.POST)
    public ResponseEntity addBankDepositor(@RequestBody BankDepositor depositor,
                                           @PathVariable Long depositId) throws ParseException{
        LOGGER.debug("addDepositor({},{})", depositId,depositor);
        try {
            Assert.notNull(depositor,"Can not be added to the database NULL depositor");
            Assert.notNull(depositor.getDepositorName(),"Can not be added to the database Empty depositor");
            Assert.isTrue(Long.valueOf(depositId) > 0,"Id Deposit - incorrect");
            Assert.notNull(depositId,"The parameter can not be NULL");
            depositorService.addBankDepositor(depositId,depositor);
            return new ResponseEntity("a bank depositor created", HttpStatus.CREATED);
        } catch (Exception e){
            LOGGER.error("addDepositor({},{}), Exception:{}", depositId,depositor, e.toString());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updating Bank Depositor
     *
     * @param depositor BankDepositor - Bank Depositor to be stored in the database
     * @return RespoeEntity
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateBankDepositor(@RequestBody BankDepositor depositor){
        LOGGER.debug("updateBankDepositor(depositor={})",depositor);
        try {
            Assert.notNull(depositor,"Can not be updated NULL depositor");
            Assert.notNull(depositor.getDepositorName(),"Can not be updated Empty depositor");
            depositorService.updateBankDepositor(depositor);
            return new ResponseEntity("a bank depositor updated", HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("updateDepositor({}), Exception:{}", depositor, e.toString());
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{depositorId}",method = RequestMethod.DELETE)
    public ResponseEntity removeBankDepositor(@PathVariable Long depositorId){
        LOGGER.debug("removeBankDepositor(id={})", depositorId);
        try {
            Assert.isTrue(Long.valueOf(depositorId) > 0,"Id Depositor - incorrect");
            Assert.notNull(depositorId,"The parameter can not be NULL");
            depositorService.removeBankDepositor(depositorId);
            return new ResponseEntity("a bank depositor removed", HttpStatus.OK);
        }catch (Exception e){
            LOGGER.error("removeDeposit({}), Exception:{}", depositorId, e.toString());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

