package com.brest.bank.service;

import com.brest.bank.dao.BankDepositDao;

import com.brest.bank.domain.BankDeposit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class BankDepositServiceImpl implements BankDepositService{

    public static final String ERROR_DB_EMPTY = "There is no RECORDS in the DataBase";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BankDepositDao bankDepositDao;

    @Autowired
    public void setBankDepositDao(BankDepositDao bankDepositDao){
        this.bankDepositDao = bankDepositDao;
    }

    @Override
    public void setSession(SessionFactory sessionFactory){

    }
    /**
     * Get all Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsCriteria(){
        LOGGER.debug("getBankDeposits()");
        List<BankDeposit> deposits = bankDepositDao.getBankDepositsCriteria();
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
    public BankDeposit getBankDepositByIdCriteria(Long id){
        LOGGER.debug("getBankDepositByIdCriteria");
        Assert.notNull(id,ERROR_METHOD_PARAM);
        BankDeposit deposit = null;
        try{
            deposit = bankDepositDao.getBankDepositByIdCriteria(id);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositByIdCriteria({}), Exception:{}",id,e.toString());
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
    public BankDeposit getBankDepositByNameCriteria(String depositName){
        LOGGER.debug("getBankDepositByName(name={})",depositName);
        Assert.notNull(depositName,ERROR_METHOD_PARAM);
        BankDeposit deposit = null;
        try{
            deposit = bankDepositDao.getBankDepositByNameCriteria(depositName);
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
    public List<BankDeposit> getBankDepositsByCurrencyCriteria(String currency){
        LOGGER.debug("getBankDepositsByCurrency(currency={})",currency);
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        List<BankDeposit> deposits = null;
        try{
            deposits = bankDepositDao.getBankDepositsByCurrencyCriteria(currency);
        }catch (EmptyResultDataAccessException e){
            LOGGER.error("getBankDepositByCurrency({}), Exception:{}",currency,e.toString());
        }
        return deposits;
    }

}
