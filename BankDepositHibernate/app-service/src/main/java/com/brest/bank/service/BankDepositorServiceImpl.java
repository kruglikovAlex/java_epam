package com.brest.bank.service;

import com.brest.bank.dao.BankDepositorDaoImpl;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.dao.BankDepositorDao;

import com.brest.bank.domain.BankDeposit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.MatcherAssert;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.hibernate.HibernateException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BankDepositorServiceImpl implements BankDepositorService{

    private static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final String ERROR_ADD = "Depositor is present in DB";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_DEPOSITOR = "In the database there is no Depositor with such parameters";

    private BankDepositorDao depositorDao;
    public static SessionFactory sessionFactory;

    public BankDepositorServiceImpl() {
        depositorDao = new BankDepositorDaoImpl();
    }

    public void setBankDepositorDao(BankDepositorDao depositorDao) {
        this.depositorDao = depositorDao;
    }

    @Override
    public void setSession(SessionFactory sessionFactory){
    	this.sessionFactory  = sessionFactory;
        depositorDao.setSession(this.sessionFactory);
    }

    @Override
    public List<BankDepositor> getBankDepositors(){
        LOGGER.debug("getBankDepositors()");
        List<BankDepositor> depositors = null;
        try{
            depositors = depositorDao.getBankDepositorsCriteria();
        }catch (HibernateException e){
            LOGGER.error("getBankDepositors(), Exception:{}",e.toString());
        }
        return depositors;
    }

    @Override
    public BankDepositor getBankDepositorById(Long id){
        LOGGER.debug("getBankDepositorById({})", id);
        BankDepositor depositor = null;
        try{
            depositor = depositorDao.getBankDepositorByIdCriteria(id);
        }catch (HibernateException e){
            LOGGER.error("getBankDepositorById({}), Exception:{}",id, e.toString());
        }
        return depositor;
    }

    @Override
    public BankDepositor getBankDepositorByName(String depositorName){
        LOGGER.debug("getBankDepositorByName({})", depositorName);
        BankDepositor depositor = null;
        try{
            depositor = depositorDao.getBankDepositorByNameCriteria(depositorName);
        }catch (HibernateException e){
            LOGGER.error("getBankDepositorByName({}), Exception:{}",depositorName,e.toString());
        }
        return depositor;
    }

    @Override
    public void addBankDepositor(Long id, BankDepositor depositor){
        LOGGER.debug("addBankDepositor({},{})", id, depositor);
        Assert.assertNotNull(depositor);
        Assert.assertTrue(depositor.getDepositorId() == null);
        Assert.assertNotNull(depositor.getDepositorName(), ERROR_METHOD_PARAM + ": DepositName");
        BankDepositor existDepositor = getBankDepositorByName(depositor.getDepositorName());
        if(existDepositor != null){
            throw new IllegalArgumentException(ERROR_ADD);
        }
        depositorDao.addBankDepositor(id, depositor);
    }

    @Override
    public void updateBankDepositor(BankDepositor depositor){
        LOGGER.debug("updateBankDepositor({})",depositor);
        Assert.assertNotNull(depositor);
        Assert.assertNotNull(depositor.getDepositorId());
        Assert.assertNotNull(depositor.getDepositorName());
        BankDepositor exitsDepositor;
        try{
            exitsDepositor = depositorDao.getBankDepositorByIdCriteria(depositor.getDepositorId());
        }catch (HibernateException e){
            LOGGER.warn("Error method dao.getBankDepositorByIdCriteria() in service.updateBankDepositor(), Exception:{}", e.toString());
            throw new   IllegalArgumentException(ERROR_DEPOSITOR);
        }
        if(exitsDepositor!=null){
            depositorDao.updateBankDepositor(depositor);
        }else {
            LOGGER.warn(ERROR_DEPOSITOR + "- method: updateBankDepositor()");
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
    }

    @Override
    public void removeBankDepositor(Long id){
        LOGGER.debug("removeBankDeposit({})",id);
        Assert.assertNotNull(id);
        BankDepositor existsDepositor;
        try{
            existsDepositor = depositorDao.getBankDepositorByIdCriteria(id);
        }catch (HibernateException e){
            LOGGER.warn("Error method dao.getBankDepositorByIdCriteria() in service.updateBankDepositor(), Exception:{}", e.toString());
            throw new   IllegalArgumentException(ERROR_DEPOSITOR);
        }
        if(existsDepositor!=null){
            depositorDao.removeBankDepositor(id);
        }else{
            LOGGER.warn(ERROR_DEPOSITOR + "- method: removeBankDepositor()");
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
    }
}
