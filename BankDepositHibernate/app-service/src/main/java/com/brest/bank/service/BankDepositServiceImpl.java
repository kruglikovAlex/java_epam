package com.brest.bank.service;

import com.brest.bank.dao.BankDepositDaoImpl;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.dao.BankDepositDao;

import com.brest.bank.domain.BankDepositor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.MatcherAssert;
import org.hibernate.SessionFactory;
import org.junit.Assert;

import org.hibernate.HibernateException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BankDepositServiceImpl implements BankDepositService{

    private static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final String ERROR_ADD = "Deposit is present in DB";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_DEPOSIT = "In the database there is no Deposit with such parameters";

    private BankDepositDao depositDao;
    public static SessionFactory sessionFactory;

    public BankDepositServiceImpl() {
        depositDao = new BankDepositDaoImpl();
    }

    public void setBankDepositDao(BankDepositDao depositDao) {
        this.depositDao = depositDao;
    }

    public void setSession(SessionFactory sessionFactory){
    	this.sessionFactory  = sessionFactory;
        depositDao.setSession(this.sessionFactory);
    }

    @Override
    public List<BankDeposit> getBankDeposits(){
        LOGGER.debug("getBankDeposits()");
        List<BankDeposit> deposits = null;
        try{
            deposits = depositDao.getBankDepositsCriteria();
        }catch (HibernateException e){
            LOGGER.error("getBankDeposits(), Exception:{}",e.toString());
        }
        return deposits;
    }

    @Override
    public BankDeposit getBankDepositById(Long id){
        LOGGER.debug("getBankDepositById(depositId={}) ", id);
        BankDeposit deposit = null;
        try{
            deposit = depositDao.getBankDepositByIdCriteria(id);
        }catch (HibernateException e) {
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
        }catch (HibernateException e){
            LOGGER.error("getBankDepositByName({}), Exception:{}",name, e.toString());
        }
        return deposit;
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
    public void updateBankDeposit(BankDeposit deposit){
        LOGGER.debug("updateBankDeposit({})",deposit);
        Assert.assertNotNull(deposit);
        Assert.assertNotNull(deposit.getDepositId());
        Assert.assertNotNull(deposit.getDepositName());
        BankDeposit exitsDeposit;
        try{
            exitsDeposit = depositDao.getBankDepositByIdCriteria(deposit.getDepositId());
        }catch (HibernateException e){
            LOGGER.warn("Error method dao.getBankDepositorByIdCriteria() in service.updateBankDepositor(), Exception:{}", e.toString());
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
    public void deleteBankDeposit(Long id){
        LOGGER.debug("deleteBankDeposit({})",id);
        Assert.assertNotNull(id);
        BankDeposit exitsDeposit;
        try{
            exitsDeposit = depositDao.getBankDepositByIdCriteria(id);
        }catch(HibernateException e){
            LOGGER.warn("Error method dao.getBankDepositorByIdCriteria() in service.deleteBankDepositor(), Exception:{}", e.toString());
            throw new   IllegalArgumentException(ERROR_DEPOSIT);
        }
        if(exitsDeposit!=null){
            depositDao.deleteBankDeposit(id);
        }else {
            LOGGER.warn(ERROR_DEPOSIT + "- method: deleteBankDeposit()");
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
    }
    
}
