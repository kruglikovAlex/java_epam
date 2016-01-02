package com.brest.bank.rest;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/deposit")
@ContextConfiguration(locations = {"classpath:/spring-rest.xml"})
public class DepositRestController {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private BankDepositService depositService;

    /**
     * Get all Bank deposits
     *
     * @return ResponseEntity<List<BankDeposit>> - a list containing all of the Bank Deposits in the database
     */
    @ResponseBody
    @RequestMapping(value="/all",method= RequestMethod.GET)
    public ResponseEntity<List<BankDeposit>> getDeposits() {
        LOGGER.debug("getDeposits()");
        try {
            List<BankDeposit> deposits = depositService.getBankDeposits();
            return new ResponseEntity(deposits, HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("getDeposits(), Exception:{}", e.toString());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposit by id deposit
     *
     * @param depositId Long - id of the Bank Deposit to return
     * @return ResponseEntity - BankDeposit with the specified id from the database
     */
    @RequestMapping(value = "/id/{depositId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BankDeposit> getDepositById(@PathVariable Long depositId) {
        LOGGER.debug("getDepositById(id={})",depositId);
        try {
            BankDeposit deposit = depositService.getBankDepositById(depositId);
            return new ResponseEntity(deposit, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("getDepositById({}), Exception:{}", depositId, e.toString());
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposit by name
     *
     * @param depositName String - name of the Bank Deposit to return
     * @return ResponseEntity(BankDeposit) - BankDeposit with the specified depositName from the database
     */
    @RequestMapping(value = "/name/{depositName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BankDeposit> getDepositByName(@PathVariable String depositName) {
        LOGGER.debug("getDepositByName(name={})",depositName);
        try {
    		BankDeposit deposit = depositService.getBankDepositByName(depositName);
            return new ResponseEntity(deposit, HttpStatus.OK);
    	} catch(Exception e){
            LOGGER.error("getDepositByName({}), Exception:{}", depositName, e.toString());
    		return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
    	}
    }

    /**
     * Get Bank Deposits by currency
     *
     * @param currency String - currency of the Bank Deposit to return
     * @return ResponseEntity<List<BankDeposit>> - a list containing all of the Bank Deposits with the specified
     * currency in the database
     */
    @ResponseBody
    @RequestMapping(value = "/currency/{currency}",method = RequestMethod.GET)
    public ResponseEntity<List<BankDeposit>> getBankDepositsByCurrency(@PathVariable String currency){
        LOGGER.debug("getBankDepositsByCurrency(currency={})",currency);
        try{
            List<BankDeposit> deposits = depositService.getBankDepositsByCurrency(currency);
            return new ResponseEntity<List<BankDeposit>>(deposits,HttpStatus.OK);
        }catch (Exception e){
            LOGGER.error("getBankDepositsByCurrency(currency={}), Exception:{}",currency,e.toString());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposits by interest rate
     *
     * @param rate - Integer - interest rate of the Bank Deposits to return
     * @return ResponseEntity(List<BankDeposit>) - a list containing all of the Bank Deposits with the specified
     * interest rate in the database
     */
    @ResponseBody
    @RequestMapping(value = "/rate/{rate}",method = RequestMethod.GET)
    public ResponseEntity<List<BankDeposit>> getBankDepositsByInterestRate(@PathVariable Integer rate){
        LOGGER.debug("getBankDepositsByInterestRate(rate={})",rate);
        try{
            List<BankDeposit> deposits = depositService.getBankDepositsByInterestRate(rate);
            return new ResponseEntity<List<BankDeposit>>(deposits, HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositsByInterestRate(rate={}), Exception:{}",rate,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposits from-to MIN TERM values
     *
     * @param fromTerm Integer - start value of the min term (count month)
     * @param toTerm Integer - end value of the min term (count month)
     * @return ResponseEntity(List<BankDeposit>) - a list containing all of the Bank Deposits in the database
     * with the specified task`s min term of deposit
     */
    @ResponseBody
    @RequestMapping(value = "/term/{fromTerm},{toTerm}", method = RequestMethod.GET)
    public ResponseEntity<List<BankDeposit>> getBankDepositsFromToMinTerm(@PathVariable Integer fromTerm,
                                                                          @PathVariable Integer toTerm){
        LOGGER.debug("getBankDepositsFromToMinTerm(fromTerm={}, toTerm={})",fromTerm,toTerm);
        try{
            List<BankDeposit> deposits = depositService.getBankDepositsFromToMinTerm(fromTerm, toTerm);
            return new ResponseEntity<List<BankDeposit>>(deposits,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositsFromToMinTerm(fromTerm={}, toTerm={}), Exception:{}",fromTerm,
                    toTerm,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposits from-to INTEREST RATE values
     *
     * @param startRate Integer - start value of the interest rate (0% < startRate <= 100%)
     * @param endRate Integer - end value of the interest rate (0% < endRate <= 100%)
     * @return ResponseEntity(List<BankDeposit>) - a list containing all of the Bank Deposits in the database
     * with the specified task`s in of deterest rate of deposit
     */
    @ResponseBody
    @RequestMapping(value = "/rate/{startRate},{endRate}", method = RequestMethod.GET)
    public ResponseEntity<List<BankDeposit>> getBankDepositsFromToInterestRate(@PathVariable Integer startRate,
                                                                               @PathVariable Integer endRate){
        LOGGER.debug("getBankDepositsFromToInterestRate(startRate={}, endRate={})",startRate,endRate);
        try{
            List<BankDeposit> deposits = depositService.getBankDepositsFromToInterestRate(startRate, endRate);
            return new ResponseEntity<List<BankDeposit>>(deposits, HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositsFromToInterestRate(startRate={},endRate={}), Exception:{}",
                    startRate,endRate,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposits from-to Date Deposit values
     *
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @return ResponseEntity(List<BankDeposit>) - a list containing all of the Bank Deposits in the database
     * with the specified task`s date of deposit
     */
    @ResponseBody
    @RequestMapping(value = "/date/{startDate},{endDate}", method = RequestMethod.GET)
    public ResponseEntity<List<BankDeposit>> getBankDepositsFromToDateDeposit(@PathVariable String startDate,
                                                                              @PathVariable String endDate){
        LOGGER.debug("getBankDepositsFromToDateDeposit(startDate={},endDate={})",startDate,endDate);
        try{
            List<BankDeposit> deposits = depositService.getBankDepositsFromToDateDeposit(dateFormat.parse(startDate),
                    dateFormat.parse(endDate));
            return new ResponseEntity<List<BankDeposit>>(deposits,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositsFromToDateDeposit(startDate={},endDate={}), Exception:{}",startDate,
                    endDate,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposits from-to Date Return Deposit values
     *
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @return ResponseEntity(List<BankDeposit>) - a list containing all of the Bank Deposits in the database
     * with the specified task`s date return of deposit
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/returnDate/{startDate},{endDate}", method = RequestMethod.GET)
    public ResponseEntity<List<BankDeposit>> getBankDepositsFromToDateReturnDeposit(@PathVariable String startDate,
                                                                                    @PathVariable String endDate)
                                                                                    throws ParseException{
        LOGGER.debug("getBankDepositsFromToDateReturnDeposit(startDate={},endDate={})",startDate,endDate);
        try{
            List<BankDeposit> deposits = depositService
                    .getBankDepositsFromToDateReturnDeposit(dateFormat.parse(startDate),
                            dateFormat.parse(endDate));
            return new ResponseEntity<List<BankDeposit>>(deposits,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositsFromToDateReturnDeposit(startDate={},endDate={}), Exception:{}",
                    startDate,endDate,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposits by NAME with depositors
     *
     * @param name String - name of the Bank Deposit to return
     * @return ResponseEntity(Map) - a bank deposit with a report on all relevant
     * bank depositors
     */
    @ResponseBody
    @RequestMapping(value = "/report/name/{name}",method = RequestMethod.GET)
    public ResponseEntity<Map> getBankDepositByNameWithDepositors(@PathVariable String name){
        LOGGER.debug("getBankDepositByNameWithDepositors(name={})",name);
        try{
            Map deposit = depositService.getBankDepositByNameWithDepositors(name);
            return new ResponseEntity<Map>(deposit,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositByNameWithDepositors(name={}),Exception:{}",name,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Deposit values
     *
     * @param name String - name of the Bank Deposit to return
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @return ResponseEntity(Map) - a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date of deposit
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "report/nameDate/{name},{startDate},{endDate}",method = RequestMethod.GET)
    public ResponseEntity<Map> getBankDepositByNameFromToDateDepositWithDepositors(@PathVariable String name,
                                                                                   @PathVariable String startDate,
                                                                                   @PathVariable String endDate)
                                                                                    throws ParseException{
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositors(name={},startDate={},endDate={})",
                name,startDate,endDate);
        try{
            Map deposit = depositService.getBankDepositByNameFromToDateDepositWithDepositors(name,
                    dateFormat.parse(startDate),dateFormat.parse(endDate));
            return new ResponseEntity<Map>(deposit,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositByNameFromToDateDepositWithDepositors(name={},startDate={},endDate={})," +
                            "Exception:{}",name,startDate,endDate,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Return Deposit values
     *
     * @param name String - name of the Bank Deposit to return
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @return ResponseEntity(Map) - a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date return of deposit
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/report/nameDateReturn/{name},{startDate},{endDate}", method = RequestMethod.GET)
    public ResponseEntity<Map> getBankDepositByNameFromToDateReturnDepositWithDepositors(@PathVariable String name,
                                                                                         @PathVariable String startDate,
                                                                                         @PathVariable String endDate)
                                                                                            throws ParseException{
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositors(name={},startDate={},endDate={})",
                name,startDate,endDate);
        try{
            Map deposit = depositService.getBankDepositByNameFromToDateReturnDepositWithDepositors(name,
                    dateFormat.parse(startDate),dateFormat.parse(endDate));
            return new ResponseEntity<Map>(deposit,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositByNameFromToDateReturnDepositWithDepositors(name={},startDate={}," +
                            "endDate={}),Exception:{}",name,startDate,endDate,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposits by ID with depositors
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @return ResponseEntity(Map) - a bank deposit with a report on all relevant
     * bank depositors
     */
    @ResponseBody
    @RequestMapping(value = "/report/id/{id}",method = RequestMethod.GET)
    public ResponseEntity<Map> getBankDepositByIdWithDepositors(@PathVariable Long id){
        LOGGER.debug("getBankDepositByIdWithDepositors(id={})",id);
        try{
            Map deposit = depositService.getBankDepositByIdWithDepositors(id);
            return new ResponseEntity<Map>(deposit,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositByIdWithDepositors(id={}), Exception:{}",id,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposits by ID with depositors from-to Date Deposit values
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @param startDate String - start value of the date deposit (startDate < endDate)
     * @param endDate String - end value of the date deposit (endDate > startDate)
     * @return ResponseEntity(Map) - a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date of deposit
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/report/idDate/{id},{startDate},{endDate}", method = RequestMethod.GET)
    public ResponseEntity<Map> getBankDepositByIdFromToDateDepositWithDepositors(@PathVariable Long id,
                                                                                 @PathVariable String startDate,
                                                                                 @PathVariable String endDate)
                                                                                    throws ParseException{
        LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositors(id={},startDate={},endDate={})",
                id, startDate, endDate);
        try{
            Map deposit = depositService.getBankDepositByIdFromToDateDepositWithDepositors(id,
                    dateFormat.parse(startDate),dateFormat.parse(endDate));
            return new ResponseEntity<Map>(deposit, HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositByIdFromToDateDepositWithDepositors(id={},startDate={},endDate={}), " +
                            "Exception:{}",id,startDate,endDate,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposits by ID with depositors from-to Date Return Deposit values
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @param startDate String - start value of the date return deposit (startDate < endDate)
     * @param endDate String - end value of the date return deposit (endDate > startDate)
     * @return ResponseEntity(Map) - a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date of deposit
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/report/idDateReturn/{id},{startDate},{endDate}", method = RequestMethod.GET)
    public ResponseEntity<Map> getBankDepositByIdFromToDateReturnDepositWithDepositors(@PathVariable Long id,
                                                                                       @PathVariable String startDate,
                                                                                       @PathVariable String endDate)
                                                                                        throws ParseException{
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositors(id={},startDate={},endDate={})",
                id, startDate, endDate);
        try{
            Map deposit = depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(id,
                    dateFormat.parse(startDate), dateFormat.parse(endDate));
            return new ResponseEntity<Map>(deposit, HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositByIdFromToDateReturnDepositWithDepositors(id={},startDate={},endDate={}), " +
                    "Exception:{}",id,startDate,endDate,e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get Bank Deposit with depositors
     *
     * @return ResponseEntity(List<Map>) - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    @ResponseBody
    @RequestMapping(value = "/report/all",method = RequestMethod.GET)
    public ResponseEntity<List<Map>> getBankDepositsWithDepositors(){
        LOGGER.debug("getBankDepositsWithDepositors()");
        try{
            List<Map> deposits = depositService.getBankDepositsWithDepositors();
            return new ResponseEntity<List<Map>>(deposits,HttpStatus.FOUND);
        }catch (Exception e){
            LOGGER.error("getBankDepositsWithDepositors(), Exception;{}",e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Adding Bank Deposit
     *
     * @param deposit BankDeposit - Bank Deposit to be inserted to the database
     * @return ResponseEntity
     */
    @ResponseBody
    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity addDeposit(@RequestBody BankDeposit deposit) {
        LOGGER.debug("addDeposit({})", deposit);
        try {
            if (deposit == null){
                throw new Exception("Can not be added to the database NULL deposit");
            }
            if (deposit.getDepositName()==null) {
                throw new Exception("Can not be added to the database Empty deposit");
            }
            depositService.addBankDeposit(deposit);
            return new ResponseEntity("Bank Deposit - ", HttpStatus.CREATED);
        }catch (Exception e){
            LOGGER.error("addDeposit({}), Exception:{}", deposit, e.toString());
        	return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    //--- Update Deposit
    @RequestMapping(method = RequestMethod.PUT)
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
            return new ResponseEntity("Bank Deposit updated - ", HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("updateDeposit({}), Exception:{}", deposit, e.toString());
        	return new ResponseEntity(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

	//--- Remove Deposit
    @RequestMapping(value = "/id/{depositId}", method = RequestMethod.DELETE)
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
            depositService.deleteBankDeposit(depositId);
            return new ResponseEntity("Bank Deposit removed - ", HttpStatus.OK);
        }catch (Exception e){
            LOGGER.error("removeDeposit({}), Exception:{}", depositId, e.toString());
        	return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}