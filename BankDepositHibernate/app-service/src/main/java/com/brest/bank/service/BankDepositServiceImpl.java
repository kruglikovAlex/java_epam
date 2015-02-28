package com.brest.bank.service;

import com.brest.bank.dao.BankDepositDaoImpl;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.dao.BankDepositDao;

import com.brest.bank.domain.BankDepositor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import org.hibernate.exception.DataException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BankDepositServiceImpl implements BankDepositService{

    private static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final String ERROR_ADD = "Deposit is present in DB";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_DEPOSIT = "In the database there is no Deposit with such parameters";

    private BankDepositDao depositDao;

    public BankDepositServiceImpl() {
        depositDao = new BankDepositDaoImpl();
    }

    public void setBankDepositDao(BankDepositDao depositDao) {
        this.depositDao = depositDao;
    }

    @Override
    public List<BankDeposit> getBankDeposits(){
        LOGGER.debug("getBankDeposits()");
        List<BankDeposit> deposits = null;
        try{
            deposits = depositDao.getBankDepositsCriteria();
        }catch (DataException e){
            LOGGER.error("getBankDeposits(), Exception:{}",e.toString());
        }
        return deposits;
    }

    @Override
    public void addBankDeposit(BankDeposit deposit){
        LOGGER.debug("addBankDeposit({}) ", deposit);
        Assert.assertNotNull(deposit);
        Assert.assertTrue(deposit.getDepositId() == null);
        Assert.assertNotNull(deposit.getDepositName(), ERROR_METHOD_PARAM + ": DepositName");
        Assert.assertNotNull(ERROR_METHOD_PARAM + ": DepositMinTerm",deposit.getDepositMinTerm());
        BankDeposit existDeposit = getBankDepositByName(deposit.getDepositName());
        if(existDeposit != null){
            throw new IllegalArgumentException(ERROR_ADD);
        }
        depositDao.addBankDeposit(deposit);
    }

    @Override
    public BankDeposit getBankDepositById(Long id){
        LOGGER.debug("getBankDepositById(depositId={}) ", id);
        BankDeposit deposit = null;
        try{
            deposit = depositDao.getBankDepositByIdCriteria(id);
        }catch (DataException e) {
            LOGGER.error("getBankDepositById({}), Exception:{}",id, e.toString());
        }
        return deposit;
    }

    @Override
    public BankDeposit getBankDepositByName(String name) {
        LOGGER.debug("getBankDepositByName({})", name);
        BankDeposit deposit = null;
        try{
            deposit = depositDao.getBankDepositByNameCriteria(name);
        }catch (DataException e){
            LOGGER.error("getBankDepositByName({}), Exception:{}",name, e.toString());
        }
        return deposit;
    }

    @Override
    public List<BankDeposit> getBankDepositsBetweenDateDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsBetweenDateDeposit({},{})",startDate,endDate);
        List<BankDeposit> deposits = null;
        try{
            deposits = depositDao.getBankDepositsBetweenDateDeposit(startDate,endDate);
        }catch (DataException e){
            LOGGER.error("getBankDepositsBetweenDateDeposit({},{}), Exception:{}",startDate, endDate, e.toString());
        }
        return deposits;
    }

    @Override
    public void updateBankDeposit(BankDeposit deposit){
        LOGGER.debug("updateBankDeposit({})",deposit);
        Assert.assertNotNull(deposit);
        Assert.assertNotNull(deposit.getDepositId());
        Assert.assertNotNull(deposit.getDepositName());
        BankDeposit exitsDeposit = null;
        try{
            exitsDeposit = depositDao.getBankDepositByIdCriteria(deposit.getDepositId());
        }catch (DataException e){
            LOGGER.warn("Error method dao.getBankDepositByIdCriteria() in service.updateBankDeposit(), Exception:{}", e.toString());
            throw new   IllegalArgumentException(ERROR_DEPOSIT);
        }
        if(exitsDeposit!=null){
            depositDao.updateBankDeposit(deposit);
        }else {
            LOGGER.warn(ERROR_DEPOSIT + "- method: updateBankDeposit()");
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
    }

    @Override
    public void removeBankDeposit(Long id){
        LOGGER.debug("removeBankDeposit({})",id);
        Assert.assertNotNull(ERROR_METHOD_PARAM,id);
        BankDeposit deposit = null;
        try{
            deposit = depositDao.getBankDepositByIdCriteria(id);
        }catch (DataException e){
            LOGGER.warn("removeBankDeposit({}), Exception:{}",id, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
        depositDao.removeBankDeposit(id);
    }
}
