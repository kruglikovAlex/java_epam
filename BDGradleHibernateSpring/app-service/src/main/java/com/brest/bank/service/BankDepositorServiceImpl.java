package com.brest.bank.service;

import com.brest.bank.domain.BankDepositor;
import com.brest.bank.dao.BankDepositorDao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.Assert;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

@Component
public class BankDepositorServiceImpl implements BankDepositorService{

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final String ERROR_DB_EMPTY = "There is no RECORDS in the DataBase";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_DEPOSITOR = "In the database there is no Depositor with such parameters";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";

    @Autowired
    private BankDepositorDao bankDepositorDao;

    @Autowired
    public void setBankDepositorDao(BankDepositorDao bankDepositorDao){
        this.bankDepositorDao = bankDepositorDao;
    }
    /**
     * Get all Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    @Override
    @Transactional
    public List<BankDepositor> getBankDepositors(){
        LOGGER.debug("getBankDepositors()");
        List<BankDepositor> depositors = bankDepositorDao.getBankDepositorsCriteria();
        Assert.notEmpty(depositors, ERROR_DB_EMPTY);
        return depositors;
    }

    /**
     * Get all Bank Depositors from-to Date Deposit
     *
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (startDate < endDate)
     * @return List<BankDepositors> a list of all bank depositors with the specified task`s date deposit
     */
    @Override
    @Transactional
    public List<BankDepositor> getBankDepositorsFromToDateDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositorsFromToDateDeposit(start date={}, end date={})",start,end);
        Assert.notNull(start,ERROR_METHOD_PARAM);
        Assert.notNull(end,ERROR_METHOD_PARAM);
        Assert.isTrue(start.before(end)||start.equals(end),ERROR_FROM_TO_PARAM);
        List<BankDepositor> depositors = null;
        try{
            depositors = bankDepositorDao.getBankDepositorsFromToDateDeposit(start, end);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositorsFromToDateDeposit(start date={}, end date={}), error-{}",start,end,e.toString());
        }
        return depositors;
    }

    /**
     * Get Bank Depositor by ID
     *
     * @param depositorId  Long - id of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    @Override
    @Transactional
    public BankDepositor getBankDepositorById(Long depositorId){
        LOGGER.debug("getBankDepositorById(depositorId={})",depositorId);
        Assert.notNull(depositorId,ERROR_METHOD_PARAM);
        BankDepositor depositor = null;
        try{
            depositor = bankDepositorDao.getBankDepositorByIdCriteria(depositorId);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositorById({}), Exception:{}",depositorId,e.toString());
        }
        return depositor;
    }

    /**
     * Get Bank Depositor by Name
     *
     * @param depositorName  String - name of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    @Override
    @Transactional
    public BankDepositor getBankDepositorByName(String depositorName){
        LOGGER.debug("getBankDepositorByName(name={})",depositorName);
        Assert.notNull(depositorName,ERROR_METHOD_PARAM);
        BankDepositor depositor = null;
        try{
            depositor = bankDepositorDao.getBankDepositorByNameCriteria(depositorName);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositorById({}), Exception:{}",depositorName,e.toString());
        }
        return depositor;
    }

    /**
     * Adding Bank Depositor
     *
     * @param depositId Long - id of the Bank Deposit
     * @param depositor BankDepositor - Bank Depositor to be inserted to the database
     */
    @Override
    @Transactional
    public void addBankDepositor(Long depositId, BankDepositor depositor){
        LOGGER.debug("addBankDepositor(depositId={},depositor={})",depositId,depositor);
        Assert.notNull(depositId,ERROR_METHOD_PARAM);
        Assert.notNull(depositor,ERROR_METHOD_PARAM);
        Assert.isNull(depositor.getDepositorId(),ERROR_NULL_PARAM);
        Assert.notNull(depositor.getDepositorName(),ERROR_METHOD_PARAM);
        BankDepositor existingDepositor = bankDepositorDao.getBankDepositorByNameCriteria(depositor.getDepositorName());
        if(existingDepositor != null){
            throw new IllegalArgumentException("Bank Depositor is present in DB");
        }
        bankDepositorDao.addBankDepositor(depositId,depositor);
    }

    /**
     * Updating Bank Depositor
     *
     * @param depositor BankDepositor - Bank Depositor to be stored in the database
     */
    @Override
    @Transactional
    public void updateBankDepositor(BankDepositor depositor){
        LOGGER.debug("updateBankDepositor(depositor={})",depositor);
        Assert.notNull(depositor,ERROR_METHOD_PARAM);
        Assert.notNull(depositor.getDepositorId(),ERROR_METHOD_PARAM);
        Assert.notNull(depositor.getDepositorName(),ERROR_METHOD_PARAM);
        BankDepositor existingDepositor;
        try {
            existingDepositor = bankDepositorDao.getBankDepositorByIdCriteria(depositor.getDepositorId());
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("Error method dao.getBankDepositorByIdCriteria() in service.updateBankDepositor(), Exception:{}",
                    e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        if (existingDepositor != null) {
            bankDepositorDao.updateBankDepositor(depositor);
        } else {
            LOGGER.warn(ERROR_DEPOSITOR + "- method: updateBankDepositor()");
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
    }

    /**
     * Deleting Bank Depositor by ID
     *
     * @param id Long - id of the Bank Depositor to be removed
     */
    @Override
    @Transactional
    public void removeBankDepositor(Long id){
        LOGGER.debug("removeBankDepositor(depositorId={})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Assert.notNull(bankDepositorDao.getBankDepositorByIdCriteria(id),ERROR_DEPOSITOR+": depositorId");
        bankDepositorDao.removeBankDepositor(id);
    }
}
