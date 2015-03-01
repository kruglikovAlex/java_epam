package com.brest.bank.service;

import com.brest.bank.dao.BankDepositDaoImpl;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.dao.BankDepositDao;

import com.brest.bank.domain.BankDepositor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.MatcherAssert;
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
        }catch (HibernateException e){
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
    public List<BankDeposit> getBankDepositsBetweenDateDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsBetweenDateDeposit({},{})",startDate,endDate);
        List<BankDeposit> deposits = null;
        try{
            deposits = depositDao.getBankDepositsBetweenDateDeposit(startDate,endDate);
        }catch (HibernateException e){
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
        }catch (HibernateException e){
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
        BankDeposit exitsDeposit = null;
        try{
            exitsDeposit = depositDao.getBankDepositByIdCriteria(id);
        }catch (HibernateException e){
            LOGGER.warn("removeBankDeposit({}), Exception:{}",id, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
        if(exitsDeposit!=null){
            depositDao.removeBankDeposit(id);
        }else {
            LOGGER.warn(ERROR_DEPOSIT + "- method: removeBankDeposit("+id+")");
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
    }

    @Override
    public List<Map> getBankDepositByCurrencyAll(String currency){
        LOGGER.debug("getBankDepositByCurrencyAll({})", currency);
        Assert.assertNotNull(ERROR_METHOD_PARAM,currency);
        List<Map> deposits = new ArrayList<Map>();
        try{
            depositDao.getBankDepositByCurrencyWithDepositors(currency);
        }catch(HibernateException e){
            LOGGER.warn("getBankDepositByCurrencyAll({}), Exception:{}",currency, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
        return deposits;
    }

    @Override
    public List<Map> getBankDepositByCurrencyBetweenDateDepositAll(String currency,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByCurrencyBetweenDateDepositAll({},{},{})", currency,startDate,endDate);
        Assert.assertNotNull(ERROR_METHOD_PARAM, currency);
        MatcherAssert.assertThat(endDate, DateMatchers.after(startDate));
        MatcherAssert.assertThat(startDate, DateMatchers.before(endDate));
        List<Map> deposits = new ArrayList<Map>();
        try{
            depositDao.getBankDepositByCurrencyBetweenDateDepositWithDepositors(currency, startDate, endDate);
        }catch(HibernateException e){
            LOGGER.warn("getBankDepositByCurrencyBetweenDateDepositAll({}), Exception:{}",currency, startDate,endDate,e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
        return deposits;
    }

    @Override
    public List<Map> getBankDepositByCurrencyBetweenDateReturnDepositAll(String currency,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByCurrencyBetweenDateReturnDepositAll({},{},{})", currency,startDate,endDate);
        Assert.assertNotNull(ERROR_METHOD_PARAM, currency);
        MatcherAssert.assertThat(endDate, DateMatchers.after(startDate));
        MatcherAssert.assertThat(startDate, DateMatchers.before(endDate));
        List<Map> deposits = new ArrayList<Map>();
        try{
            depositDao.getBankDepositByCurrencyBetweenDateReturnDepositWithDepositors(currency, startDate, endDate);
        }catch(HibernateException e){
            LOGGER.warn("getBankDepositByCurrencyBetweenDateReturnDepositAll({}), Exception:{}",currency, startDate,endDate,e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
        return deposits;
    }

    @Override
    public List<Map> getBankDepositByNameAll(String name){
        //TODO
        List<Map> deposits = new ArrayList<Map>();
        return deposits;
    }

    @Override
    public List<Map> getBankDepositByNameBetweenDateDepositAll(String name,Date startDate, Date endDate){
        //TODO
        List<Map> deposits = new ArrayList<Map>();
        return deposits;
    }

    @Override
    public List<Map> getBankDepositByNameBetweenDateReturnDepositAll(String name,Date startDate, Date endDate){
        //TODO
        List<Map> deposits = new ArrayList<Map>();
        return deposits;
    }

    @Override
    public List<Map> getBankDepositByInterestRateAll(Integer rate){
        //TODO
        List<Map> deposits = new ArrayList<Map>();
        return deposits;
    }

    @Override
    public List<Map> getBankDepositBetweenInterestRateAll(Integer startRate, Integer endRate){
        //TODO
        List<Map> deposits = new ArrayList<Map>();
        return deposits;
    }

    @Override
    public List<Map> getBankDepositBetweenInterestRateBetweenDateDepositAll(Integer startRate, Integer endRate,Date startDate, Date endDate){
        //TODO
        List<Map> deposits = new ArrayList<Map>();
        return deposits;
    }

    @Override
    public List<Map> getBankDepositBetweenInterestRateBetweenDateReturnDepositAll(Integer startRate, Integer endRate,Date startDate, Date endDate){
        //TODO
        List<Map> deposits = new ArrayList<Map>();
        return deposits;
    }


    public List<Map> getBankDepositsBetweenDateDepositAll(Date startDate, Date endDate){
        //TODO
        List<Map> deposits = new ArrayList<Map>();
        return deposits;
    }

    public List<Map> getBankDepositsBetweenDateReturnDepositAll(Date startDate, Date endDate){
        //TODO
        List<Map> deposits = new ArrayList<Map>();
        return deposits;
    }
}
