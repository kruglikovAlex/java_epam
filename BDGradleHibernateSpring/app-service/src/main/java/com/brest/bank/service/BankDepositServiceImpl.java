package com.brest.bank.service;

import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.dao.BankDepositorDao;

import com.brest.bank.domain.BankDeposit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Service
public class BankDepositServiceImpl implements BankDepositService{

    public static final String ERROR_DB_EMPTY = "There is no RECORDS in the DataBase";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";
    public static final String ERROR_DEPOSIT = "In the database there is no Deposit with such parameters";

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public BankDepositDao depositDao;

    @Autowired
    private BankDepositorDao depositorDao;

    @Autowired
    public void setDepositDao(BankDepositDao depositDao){
        this.depositDao = depositDao;
    }

    @Autowired
    public void setDepositorDao(BankDepositorDao depositorDao){
        this.depositorDao = depositorDao;
    }

    /**
     * Get all Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDeposits(){
        LOGGER.debug("getBankDeposits()");
        List<BankDeposit> deposits = depositDao.getBankDepositsCriteria();
        Assert.notEmpty(deposits, ERROR_DB_EMPTY);
        return deposits;
    }

    /**
     * Get Bank Deposit by ID
     *
     * @param id Long - id of the Bank Deposit to return
     * @return BankDeposit with the specified id from the database
     */
    @Override
    @Transactional
    public BankDeposit getBankDepositById(Long id){
        LOGGER.debug("getBankDepositById");
        Assert.notNull(id,ERROR_METHOD_PARAM);
        BankDeposit deposit = null;
        try{
            deposit = depositDao.getBankDepositByIdCriteria(id);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositById({}), Exception:{}",id,e.toString());
        }
        return deposit;
    }

    /**
     * Get Bank Deposit by NAME
     *
     * @param depositName String - name of the Bank Deposit to return
     * @return BankDeposit with the specified depositName from the database
     */
    @Override
    @Transactional
    public BankDeposit getBankDepositByName(String depositName){
        LOGGER.debug("getBankDepositByName(name={})",depositName);
        Assert.notNull(depositName,ERROR_METHOD_PARAM);
        BankDeposit deposit = null;
        try{
            deposit = depositDao.getBankDepositByNameCriteria(depositName);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositByName({}), Exception:{}",depositName,e.toString());
        }
        return deposit;
    }

    /**
     * Get Bank Deposit by Currency
     *
     * @param currency String - currency of the Bank Deposits to return
     * @return List<BankDeposit> - a list containing all of the Bank Deposits with the specified
     * currency in the database
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsByCurrency(String currency){
        LOGGER.debug("getBankDepositsByCurrency(currency={})",currency);
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        List<BankDeposit> deposits = null;
        try{
            deposits = depositDao.getBankDepositsByCurrencyCriteria(currency);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositByCurrency({}), Exception:{}",currency,e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposits by INTEREST RATE
     *
     * @param rate Integer - interest rate of the Bank Deposits to return
     * @return List<BankDeposit>  - a list containing all of the Bank Deposits with the specified
     * interest rate in the database
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsByInterestRate(Integer rate){
        LOGGER.debug("getBankDepositsByInterestRate({})",rate);
        Assert.notNull(rate,ERROR_METHOD_PARAM);
        List<BankDeposit> deposits = null;
        try{
            deposits = depositDao.getBankDepositsByInterestRateCriteria(rate);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositsByInterestRateCriteria({}), Exception:{}",rate,e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposits from-to MIN TERM values
     *
     * @param fromTerm Integer - start value of the min term (count month)
     * @param toTerm Integer - end value of the min term (count month)
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     * with the specified task`s min term of deposit
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsFromToMinTerm(Integer fromTerm,Integer toTerm){
        LOGGER.debug("getBankDepositsFromToMinTerm(from={}, to={})",fromTerm,toTerm);
        Assert.notNull(fromTerm,ERROR_METHOD_PARAM);
        Assert.notNull(toTerm,ERROR_METHOD_PARAM);
        Assert.isTrue(fromTerm<=toTerm,ERROR_FROM_TO_PARAM);
        List<BankDeposit> deposits = null;
        try{
            deposits = depositDao.getBankDepositsFromToMinTermCriteria(fromTerm,toTerm);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositsFromToMinTermCriteria({},{}), Exception:{}",fromTerm,toTerm,e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposits from-to INTEREST RATE values
     *
     * @param startRate Integer - start value of the interest rate (0% < startRate <= 100%)
     * @param endRate Integer - end value of the interest rate (0% < endRate <= 100%)
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     * with the specified task`s in of deterest rate of deposit
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsFromToInterestRate(Integer startRate,Integer endRate) {
        LOGGER.debug("getBankDepositsFromToInterestRate(from={}, to={})", startRate, endRate);
        Assert.notNull(startRate, ERROR_METHOD_PARAM);
        Assert.notNull(endRate, ERROR_METHOD_PARAM);
        Assert.isTrue(startRate <= endRate, ERROR_FROM_TO_PARAM);
        List<BankDeposit> deposits = null;
        try {
            deposits = depositDao.getBankDepositsFromToInterestRateCriteria(startRate, endRate);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositsFromToInterestRateCriteria({},{}), Exception:{}", startRate,endRate, e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposits from-to Date Deposit values
     *
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (endDate > startDate)
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     * with the specified task`s date of deposit
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsFromToDateDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDeposit(from={}, to={})", startDate, endDate);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);
        List<BankDeposit> deposits = null;
        try{
            deposits = depositDao.getBankDepositsFromToDateDeposit(startDate,endDate);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositsFromToDateDeposit({},{}), Exception:{}",
                    dateFormat.format(startDate),dateFormat.format(endDate), e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposits from-to Date Return Deposit values
     *
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (endDate > startDate)
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     * with the specified task`s date return of deposit
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsFromToDateReturnDeposit(Date startDate,Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDeposit(from={}, to={})", startDate, endDate);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);
        List<BankDeposit> deposits = null;
        try{
            deposits = depositDao.getBankDepositsFromToDateReturnDeposit(startDate, endDate);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositsFromToDateReturnDeposit({},{}), Exception:{}",
                    dateFormat.format(startDate),dateFormat.format(endDate), e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposit by NAME with depositors
     *
     * @param name String - name of the Bank Deposit to return
     * @return Map  - a bank deposit with a report on all relevant
     * bank depositors
     */
    @Override
    @Transactional
    public Map getBankDepositByNameWithDepositors(String name){
        LOGGER.debug("getBankDepositByNameWithDepositors(name={})",name);
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Map deposit = null;
        try{
            deposit = depositDao.getBankDepositByNameWithDepositors(name);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositByNameWithDepositors({}), Exception:{}", name, e.toString());
        }
        return deposit;
    }

    /**
     * Get Bank Deposit by NAME with depositors from-to Date Deposit values
     *
     * @param name String - name of the Bank Deposit to return
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (endDate > startDate)
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date of deposit
     */
    @Override
    @Transactional
    public Map getBankDepositByNameFromToDateDepositWithDepositors(String name,Date startDate,Date endDate){
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositors(name={},from={},to={})",name,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);
        Map deposit = null;
        try{
            deposit = depositDao.getBankDepositByNameFromToDateDepositWithDepositors(name,startDate,endDate);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositByNameFromToDateDepositWithDepositors({},{},{}), Exception:{}", name, e.toString());
        }
        return deposit;
    }

    /**
     * Get Bank Deposit by NAME with depositors from-to Date Return Deposit values
     *
     * @param name String - name of the Bank Deposit to return
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (endDate > startDate)
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date return of deposit
     */
    @Override
    @Transactional
    public Map getBankDepositByNameFromToDateReturnDepositWithDepositors(String name,Date startDate,Date endDate){
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositors(name={},from={},to={})",name,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);
        Map deposit = null;
        try{
            deposit = depositDao.getBankDepositByNameFromToDateReturnDepositWithDepositors(name, startDate, endDate);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositByNameFromToDateReturnDepositWithDepositors({},{},{}), Exception:{}", name,
                    dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
        }
        return deposit;
    }

    /**
     * Get Bank Deposit by ID with depositors
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    @Override
    @Transactional
    public Map getBankDepositByIdWithDepositors(Long id){
        LOGGER.debug("getBankDepositByIdWithDepositors(depositId={})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Map deposit = null;
        try{
            deposit = depositDao.getBankDepositByIdWithDepositors(id);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositByIdWithDepositors({}), Exception:{}",id, e.toString());
        }
        return deposit;
    }

    /**
     * Get Bank Deposit by ID with depositor from-to Date Deposit values
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (endDate > startDate)
     * @return Map  a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date of deposit
     */
    @Override
    @Transactional
    public Map getBankDepositByIdFromToDateDepositWithDepositors(Long id,Date startDate,Date endDate){
        LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositors(id={},from={},to={})",id,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);
        Map deposit = null;
        try{
            deposit = depositDao.getBankDepositByIdFromToDateDepositWithDepositors(id,startDate,endDate);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositByIdFromToDateDepositWithDepositors({},{},{}), Exception:{}", id,
                    dateFormat.format(startDate),dateFormat.format(endDate), e.toString());
        }
        return deposit;
    }

    /**
     * Get Bank Deposit by ID with depositor from-to Date Return Deposit values
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (startDate < endDate)
     * @return Map  a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date return deposit
     */
    @Override
    @Transactional
    public Map getBankDepositByIdFromToDateReturnDepositWithDepositors(Long id,Date startDate,Date endDate){
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositors(id={},from={},to={})",id,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);
        Map deposit = null;
        try{
            deposit = depositDao.getBankDepositByIdFromToDateReturnDepositWithDepositors(id, startDate, endDate);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositByIdFromToDateReturnDepositWithDepositors({},{},{}), Exception:{}", id,
                    dateFormat.format(startDate),dateFormat.format(endDate), e.toString());
        }
        return deposit;
    }

    /**
     * Get Bank Deposit with depositors
     *
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsWithDepositors(){
        LOGGER.debug("getBankDepositsWithDepositors");
        List<Map> deposits = null;
        try{
            deposits = depositDao.getBankDepositsWithDepositors();
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositsWithDepositors(), Exception:{}", e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposit from-to Date Deposit with depositors
     *
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (startDate < endDate)
     * @return List<Map> a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date deposit
     */
    @Override
    @Transactional
    public List<Map> getBankDepositsFromToDateDepositWithDepositors(Date startDate,Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDepositWithDepositors(from={},to={})",
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);
        List<Map> deposits = null;
        try{
            deposits = depositDao.getBankDepositsFromToDateDepositWithDepositors(startDate, endDate);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositsFromToDateDepositWithDepositors({},{}), Exception:{}",
                    dateFormat.format(startDate),dateFormat.format(endDate), e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposit from-to Date Return Deposit with depositors
     *
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (startDate < endDate)
     * @return List<Map> a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date return deposit
     */
    @Override
    @Transactional
    public List<Map> getBankDepositsFromToDateReturnDepositWithDepositors(Date startDate,Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDepositWithDepositors(from={},to={})",
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);
        List<Map> deposits = null;
        try{
            deposits = depositDao.getBankDepositsFromToDateReturnDepositWithDepositors(startDate, endDate);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositsFromToDateReturnDepositWithDepositors({},{}), Exception:{}",
                    dateFormat.format(startDate),dateFormat.format(endDate), e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposit by Currency with depositors
     *
     * @param currency String - Currency of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    @Override
    @Transactional
    public List<Map> getBankDepositsByCurrencyWithDepositors(String currency){
        LOGGER.debug("getBankDepositsByCurrencyWithDepositors(currency={})",currency);
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        List<Map> deposits = null;
        try{
            deposits = depositDao.getBankDepositsByCurrencyWithDepositors(currency);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositByCurrencyWithDepositors({}), Exception:{}",currency,e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposit from-to Date Deposit by Currency with depositors
     *
     * @param currency String - Currency of the Bank Deposit to return
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (startDate < endDate)
     * @return List<Map> a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date deposit
     */
    @Override
    @Transactional
    public List<Map> getBankDepositsByCurrencyFromToDateDepositWithDepositors(String currency,
                                                                              Date startDate,
                                                                              Date endDate){
        LOGGER.debug("getBankDepositsByCurrencyFromToDateDepositWithDepositors(currency={},from={},to={})",
                currency,dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);
        List<Map> deposits = null;
        try{
            deposits = depositDao.getBankDepositsByCurrencyFromToDateDepositWithDepositors(currency,
                    startDate, endDate);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositsByCurrencyFromToDateDepositWithDepositors({},{},{}), Exception:{}",
                    currency, dateFormat.format(startDate), dateFormat.format(endDate), e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposits from-to Date Return Deposit by Currency with depositors
     *
     * @param currency String - Currency of the Bank Deposit to return
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (startDate < endDate)
     * @return List<Map> a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date return deposit
     */
    @Override
    @Transactional
    public List<Map> getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(String currency,
                                                                                   Date startDate,
                                                                                   Date endDate){
        LOGGER.debug("getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(currency={},from={},to={})",
                currency,dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate)||startDate.equals(endDate),ERROR_FROM_TO_PARAM);
        List<Map> deposits = null;
        try{
            deposits = depositDao.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(currency,
                    startDate, endDate);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors({},{},{}), Exception:{}",
                    currency, dateFormat.format(startDate), dateFormat.format(endDate), e.toString());
        }
        return deposits;
    }

    /**
     * Adding Bank Deposit
     *
     * @param deposit BankDeposit - Bank Deposit to be inserted to the database
     */
    @Override
    @Transactional
    public void addBankDeposit(BankDeposit deposit){
        LOGGER.debug("addBankDeposit(deposit={})",deposit);
        Assert.notNull(deposit,ERROR_METHOD_PARAM);
        Assert.isNull(deposit.getDepositId(),ERROR_NULL_PARAM);
        Assert.notNull(deposit.getDepositName(),ERROR_METHOD_PARAM);
        BankDeposit existingDeposit = depositDao.getBankDepositByNameCriteria(deposit.getDepositName());
        if(existingDeposit != null){
            throw new IllegalArgumentException("Bank Deposit is present in DB");
        }
        depositDao.addBankDeposit(deposit);
    }

    /**
     * Updating Bank Deposit
     *
     * @param deposit BankDeposit - Bank Deposit to be stored in the database
     */
    @Override
    @Transactional
    public void updateBankDeposit(BankDeposit deposit){
        LOGGER.debug("updateBankDeposit(deposit={})",deposit.toString());
        Assert.notNull(deposit,ERROR_METHOD_PARAM);
        Assert.notNull(deposit.getDepositId(),ERROR_METHOD_PARAM+": depositId");
        Assert.notNull(deposit.getDepositName(),ERROR_METHOD_PARAM+": depositName");
        BankDeposit existingDeposit;
        try {
            existingDeposit = depositDao.getBankDepositByIdCriteria(deposit.getDepositId());
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("Error method dao.getBankDepositByIdCriteria() in service.updateBankDeposit(), Exception:{}",
                    e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
        if (existingDeposit != null) {
            depositDao.updateBankDeposit(deposit);
        } else {
            LOGGER.warn(ERROR_DEPOSIT + "- method: updateBankDeposit()");
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
    }

    /**
     * Deleting Bank Deposit by ID
     *
     * @param id Long - id of the Bank Deposit to be removed
     */
    @Override
    @Transactional
    public void deleteBankDeposit(Long id){
        LOGGER.debug("deleteBankDeposit(depositId={})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Assert.notNull(depositDao.getBankDepositByIdCriteria(id),ERROR_DEPOSIT+": depositId");
        depositDao.deleteBankDeposit(id);
    }

    private Map depositWithoutDepositors(BankDeposit deposit){
        Map<String, Object> list = new HashMap<String, Object>(11);
            list.put("depositId", deposit.getDepositId());
            list.put("depositName", deposit.getDepositName());
            list.put("depositMinTerm", deposit.getDepositMinTerm());
            list.put("depositMinAmount", deposit.getDepositMinAmount());
            list.put("depositCurrency", deposit.getDepositCurrency());
            list.put("depositInterestRate", deposit.getDepositInterestRate());
            list.put("depositAddConditions", deposit.getDepositAddConditions());
            list.put("numDepositors", 0);
            list.put("sumAmount", 0);
            list.put("sumPlusAmount", 0);
            list.put("sumMinusAmount", 0);
        return list;
    }

    private List<Map> depositsWithoutDepositors(List<BankDeposit> deposits){
        List<Map> list = new ArrayList<Map>(deposits.size());
        for(BankDeposit dep:deposits){
            list.add(depositWithoutDepositors(dep));
        }
        return list;
    }
}
