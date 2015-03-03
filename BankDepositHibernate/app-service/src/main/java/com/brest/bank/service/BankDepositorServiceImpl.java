package com.brest.bank.service;

import com.brest.bank.dao.BankDepositorDaoImpl;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.dao.BankDepositorDao;

import com.brest.bank.domain.BankDeposit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.MatcherAssert;
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

    public BankDepositorServiceImpl() {
        depositorDao = new BankDepositorDaoImpl();
    }

    public void setBankDepositorDao(BankDepositorDao depositorDao) {
        this.depositorDao = depositorDao;
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
    public List<BankDepositor> getBankDepositorsBetweenDateDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorsBetweenDateDeposit({},{})", startDate, endDate);
        List<BankDepositor> depositors = null;
        try{
            depositors = depositorDao.getBankDepositorBetweenDateDeposit(startDate, endDate);
        }catch (HibernateException e){
            LOGGER.error("getBankDepositorsBetweenDateDeposit({},{}), Exception:{}",startDate, endDate,e.toString());
        }
        return depositors;
    }

    @Override
    public List<BankDepositor> getBankDepositorsBetweenDateReturnDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorsBetweenDateReturnDeposit({},{})", startDate, endDate);
        List<BankDepositor> depositors = null;
        try{
            depositors = depositorDao.getBankDepositorBetweenDateReturnDeposit(startDate, endDate);
        }catch (HibernateException e){
            LOGGER.error("getBankDepositorsBetweenDateReturnDeposit({},{}), Exception:{}",startDate, endDate,e.toString());
        }
        return depositors;
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
        LOGGER.debug("removeBankDepositor({})", id);
        Assert.assertNotNull(ERROR_METHOD_PARAM,id);
        BankDepositor exitsDepositor;
        try{
            exitsDepositor = depositorDao.getBankDepositorByIdCriteria(id);
        }catch (HibernateException e){
            LOGGER.warn("removeBankDepositor({}), Exception:{}",id, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        if(exitsDepositor!=null){
            depositorDao.removeBankDepositor(id);
        } else{
            LOGGER.warn(ERROR_DEPOSITOR + "- method: removeBankDepositor("+id+")");
            throw new IllegalArgumentException(ERROR_DEPOSITOR);   
        }
    }

    @Override
    public BankDepositor getBankDepositorByIdDepositMinAmount(Long id){
        LOGGER.debug("getBankDepositorByIdDepositMinAmount({})", id);
        Assert.assertNotNull(ERROR_METHOD_PARAM,id);
        BankDepositor depositor;
        try{
            depositor = depositorDao.getBankDepositorByIdDepositMinAmount(id);
        }catch (HibernateException e){
            LOGGER.warn("getBankDepositorByIdDepositMinAmount({}), Exception:{}",id, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        return depositor;
    }

    @Override
    public BankDepositor getBankDepositorByIdDepositMaxAmount(Long id){
        LOGGER.debug("getBankDepositorByIdDepositMaxAmount({})", id);
        Assert.assertNotNull(ERROR_METHOD_PARAM,id);
        BankDepositor depositor;
        try{
            depositor = depositorDao.getBankDepositorByIdDepositMaxAmount(id);
        }catch (HibernateException e){
            LOGGER.warn("getBankDepositorByIdDepositMaxAmount({}), Exception:{}",id, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        return depositor;
    }

    @Override
    public List<BankDepositor> getBankDepositorsByIdDeposit(Long id){
        LOGGER.debug("getBankDepositorByIdDeposit({})", id);
        Assert.assertNotNull(ERROR_METHOD_PARAM, id);
        List<BankDepositor> depositors;
        try{
            depositors = depositorDao.getBankDepositorByIdDepositCriteria(id);
        }catch (HibernateException e){
            LOGGER.warn("getBankDepositorByIdDeposit({}), Exception:{}",id, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        return depositors;
    }

    @Override
    public List<BankDepositor> getBankDepositorsByIdDepositBetweenDateDeposit(Long id, Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorByIdDepositBetweenDateDeposit({},{},{})", id,startDate,endDate);
        Assert.assertNotNull(ERROR_METHOD_PARAM, id);
        Assert.assertNotNull(ERROR_METHOD_PARAM,startDate);
        Assert.assertNotNull(ERROR_METHOD_PARAM,endDate);
        MatcherAssert.assertThat(startDate,DateMatchers.before(endDate));
        List<BankDepositor> depositors;
        try{
            depositors = depositorDao.getBankDepositorByIdDepositBetweenDateDeposit(id,startDate,endDate);
        }catch (HibernateException e){
            LOGGER.warn("getBankDepositorByIdDepositBetweenDateDeposit({},{},{}), Exception:{}",id,startDate,endDate, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        return depositors;
    }

    @Override
    public List<BankDepositor> getBankDepositorsByIdDepositBetweenDateReturnDeposit(Long id, Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorByIdDepositBetweenDateReturnDeposit({},{},{})", id,startDate,endDate);
        Assert.assertNotNull(ERROR_METHOD_PARAM + " id=" + id, id);
        Assert.assertNotNull(ERROR_METHOD_PARAM, startDate);
        Assert.assertNotNull(ERROR_METHOD_PARAM,endDate);
        MatcherAssert.assertThat(startDate,DateMatchers.before(endDate));
        List<BankDepositor> depositors;
        try{
            depositors = depositorDao.getBankDepositorByIdDepositBetweenDateReturnDeposit(id,startDate,endDate);
        }catch (HibernateException e){
            LOGGER.warn("getBankDepositorByIdDepositBetweenDateReturnDeposit({},{},{}), Exception:{}",id,startDate,endDate, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        return depositors;
    }

    @Override
    public List<BankDepositor> getBankDepositorsByIdDepositByMarkReturn(Long id, Integer mark){
        LOGGER.debug("getBankDepositorsByIdDepositByMarkReturn({},{})", id,mark);
        Assert.assertNotNull(ERROR_METHOD_PARAM, id);
        Assert.assertNotNull(ERROR_METHOD_PARAM,mark);
        Assert.assertTrue(ERROR_METHOD_PARAM+" expect mark=0 or 1, but actual mark="+mark,mark==0||mark==1);
        List<BankDepositor> depositors;
        try{
            depositors = depositorDao.getBankDepositorByIdDepositByMarkReturn(id,mark);
        }catch (HibernateException e){
            LOGGER.warn("getBankDepositorByIdDepositByMarkReturn({},{}), Exception:{}",id,mark, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        return depositors;
    }

    @Override
    public List<BankDepositor> getBankDepositorsByMarkReturn(Integer mark){
        LOGGER.debug("getBankDepositorsByMarkReturn({})", mark);
        Assert.assertNotNull(ERROR_METHOD_PARAM,mark);
        Assert.assertTrue(ERROR_METHOD_PARAM+" expect mark=0 or 1, but actual mark="+mark, mark==0||mark==1);
        List<BankDepositor> depositors;
        try{
            depositors = depositorDao.getBankDepositorByMarkReturn(mark);
        }catch (HibernateException e){
            LOGGER.warn("getBankDepositorByMarkReturn({}), Exception:{}",mark, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        return depositors;
    }

    @Override
    public List<BankDepositor> getBankDepositorsBetweenAmountDeposit(Integer start, Integer end) {
        LOGGER.debug("getBankDepositorBetweenAmountDeposit({})", start,end);
        List<BankDepositor> depositors = null;
        //TODO
        return depositors;
    }

    @Override
    public BankDepositor getBankDepositorSumAll(){
        LOGGER.debug("getBankDepositorByIdDepositMinAmount()");

        BankDepositor depositor = null;
        //TODO
        return depositor;
    }

    @Override
    public BankDepositor getBankDepositorByIdDepositSum(Long id){
        LOGGER.debug("getBankDepositorByIdDepositSum({})", id);
        Assert.assertNotNull(ERROR_METHOD_PARAM,id);
        BankDepositor depositor = null;
        //TODO
        return depositor;
    }

    @Override
    public BankDepositor getBankDepositorBetweenDateDepositSum(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorBetweenDateDepositSum({},{})", startDate,endDate);
        Assert.assertNotNull(ERROR_METHOD_PARAM, startDate);
        Assert.assertNotNull(ERROR_METHOD_PARAM,endDate);
        MatcherAssert.assertThat(startDate, DateMatchers.before(endDate));
        BankDepositor depositor = null;
        //TODO
        return depositor;
    }

    @Override
    public BankDepositor getBankDepositorBetweenDateReturnDepositSum(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorBetweenDateReturnDepositSum({},{})", startDate,endDate);
        Assert.assertNotNull(ERROR_METHOD_PARAM, startDate);
        Assert.assertNotNull(ERROR_METHOD_PARAM,endDate);
        MatcherAssert.assertThat(startDate, DateMatchers.before(endDate));
        BankDepositor depositor = null;
        //TODO
        return depositor;
    }
}
