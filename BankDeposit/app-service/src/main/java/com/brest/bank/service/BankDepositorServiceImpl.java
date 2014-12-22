package com.brest.bank.service;

import com.brest.bank.dao.BankDepositorDao;
import com.brest.bank.domain.BankDepositor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

@Component
public class BankDepositorServiceImpl implements BankDepositorService {
    public static final String ERROR_DB_EMPTY = "There is no RECORDS of DEPOSITORS in the DataBase";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_ADD = "Depositor is present in DB";
    public static final String ERROR_DEPOSITOR = "In the database there is no Depositor with such parameters";

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BankDepositorDao bankDepositorDao;

    @Autowired
    public void setBankDepositorDao(BankDepositorDao bankDepositorDao) {
        this.bankDepositorDao = bankDepositorDao;
    }

    @Override
    @Transactional
    public Long addBankDepositor(BankDepositor depositor) {
        Assert.notNull(depositor);
        Assert.isNull(depositor.getDepositorId());
        Assert.notNull(depositor.getDepositorName(), ERROR_METHOD_PARAM + ": DepositorName");
        BankDepositor existingDepositor = getBankDepositorByName(depositor.getDepositorName());
        if (existingDepositor != null) {
            throw new IllegalArgumentException(ERROR_ADD);
        }
        return bankDepositorDao.addBankDepositor(depositor);
    }

    @Override
    @Transactional
    public BankDepositor getBankDepositorByName(String depositorName) {
        LOGGER.debug("getBankDepositorByName(depositorName={}) ", depositorName);
        BankDepositor depositor = null;
        try {
            depositor = bankDepositorDao.getBankDepositorByName(depositorName);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositorByName({}), Exception:{}", depositorName, e.toString());
        }
        return depositor;
    }
    
    @Override
    @Transactional
    public BankDepositor getBankDepositorById(Long depositorId) {
        LOGGER.debug("getBankDepositorById(depositorId={}) ", depositorId);
        BankDepositor depositor = null;
        try {
            depositor = bankDepositorDao.getBankDepositorById(depositorId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositorById({}), Exception:{}",depositorId, e.toString());
        }
        return depositor;
    }
    
    @Override
    @Transactional
    public List<BankDepositor> getBankDepositorByIdDeposit(Long depositorIdDeposit) {
        LOGGER.debug("getBankDepositorByIdDeposit(depositorIdDeposit={}) ", depositorIdDeposit);
        List<BankDepositor> depositors = null;
        try {
	    depositors = bankDepositorDao.getBankDepositorByIdDeposit(depositorIdDeposit);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositorById({}), Exception:{}", depositorIdDeposit, e.toString());
        }
        return depositors;
    }

    @Override
    @Transactional
    public List<BankDepositor> getBankDepositorBetweenDateDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorBetweenDateDeposit(Date:{}{}) ", startDate, endDate);
        List<BankDepositor> depositors = null;
        try {
	    depositors = bankDepositorDao.getBankDepositorBetweenDateDeposit(startDate, endDate);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositorBetweenDateDeposit({}{}), Exception:{}",startDate, endDate, e.toString());
        }
        Assert.notEmpty(depositors, ERROR_DB_EMPTY);
        return depositors;
    }
    //======================
    @Override
    @Transactional
    public BankDepositor getBankDepositorsAllSummAmount() {
        LOGGER.debug("getBankDepositors()");
        BankDepositor depositor = bankDepositorDao.getBankDepositorsAllSummAmount();
        Assert.notNull(depositor, ERROR_DB_EMPTY);
        return depositor;
    }

    @Override
    @Transactional
    public BankDepositor getBankDepositorsSummAmountByIdDeposit(Long depositorId) {
        LOGGER.debug("getBankDepositorsSummAmountByIdDeposit(depositorId={}) ", depositorId);
        BankDepositor depositor = null;
        try {
            depositor = bankDepositorDao.getBankDepositorsSummAmountByIdDeposit(depositorId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositorsSummAmountByIdDeposit({}), Exception:{}",depositorId, e.toString());
        }
        return depositor;
    }
    @Override
    @Transactional
	public BankDepositor getBankDepositorSummAmountDepositBetweenDateDeposit(Date startDate, Date endDate){
    	LOGGER.debug("getBankDepositorSummAmountDepositBetweenDateDeposit(Date:{}{}) ", startDate, endDate);
        BankDepositor depositor = null;
    	try {
    		depositor = bankDepositorDao.getBankDepositorSummAmountDepositBetweenDateDeposit(startDate, endDate);
    	} catch (EmptyResultDataAccessException e) {
    		LOGGER.error("getBankDepositorSummAmountDepositBetweenDateDeposit({}{}), Exception:{}",startDate, endDate, e.toString());
    	}
    	Assert.notNull(depositor, ERROR_DB_EMPTY);
    	return depositor;
	}
    @Override
    @Transactional
    public BankDepositor getBankDepositorSummAmountDepositBetweenDateReturnDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorSummAmountDepositBetweenDateReturnDeposit(Date:{}{}) ", startDate, endDate);
        BankDepositor depositor = null;
        try {
            depositor = bankDepositorDao.getBankDepositorSummAmountDepositBetweenDateReturnDeposit(startDate, endDate);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositorSummAmountDepositBetweenDateReturnDeposit({}{}), Exception:{}",startDate, endDate, e.toString());
        }
        Assert.notNull(depositor, ERROR_DB_EMPTY);
        return depositor;
    }
    //======================
    @Override
    @Transactional
    public List<BankDepositor> getBankDepositorBetweenDateReturnDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorBetweenDateReturnDeposit(Date:{}{}) ", startDate, endDate);
        List<BankDepositor> depositors = null;
        try {
            depositors = bankDepositorDao.getBankDepositorBetweenDateReturnDeposit(startDate, endDate);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositorBetweenDateReturnDeposit({}{}), Exception:{}",startDate, endDate, e.toString());
        }
        Assert.notEmpty(depositors, ERROR_DB_EMPTY);
        return depositors;
    }

    @Override
    @Transactional
    public void removeBankDepositor(Long depositorId) {
        LOGGER.debug("removeBankDepositor(depositorId={}) ", depositorId);
        Assert.notNull(depositorId,ERROR_METHOD_PARAM);
        try {
            BankDepositor depositor = bankDepositorDao.getBankDepositorById(depositorId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("removeBankDepositor({}), Exception:{}",depositorId, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        bankDepositorDao.removeBankDepositor(depositorId);
    }

    @Override
    @Transactional
    public boolean removeBankDepositorByIdDeposit(Long depositorIdDeposit) {
        LOGGER.debug("removeBankDepositorByIdDeposit(depositorId={}) ", depositorIdDeposit);
        Assert.notNull(depositorIdDeposit,ERROR_METHOD_PARAM);
        List<BankDepositor> depositors = null;
        boolean res = false;
        try {
            depositors = bankDepositorDao.getBankDepositorByIdDeposit(depositorIdDeposit);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("getBankDepositorByIdDeposit() in removeBankDepositorByIdDeposit({}), Exception:{}",depositorIdDeposit, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        if (depositors.size()>0) {
        	int summ = 0;
        	for (BankDepositor elem: depositors) {
        		summ += elem.getDepositorAmountDeposit()+
        				elem.getDepositorAmountPlusDeposit()-
        				elem.getDepositorAmountMinusDeposit();
        	}
        	if (summ == 0) {
        		bankDepositorDao.removeBankDepositorByIdDeposit(depositorIdDeposit);
        		res =  true;
        	} else res = false;
        }
        return res;
    }

    @Override
    @Transactional
    public void updateBankDepositor(BankDepositor depositor) {
        LOGGER.debug("updateBankDepositor(depositor={}) ", depositor);
        Assert.notNull(depositor);
        Assert.notNull(depositor.getDepositorId());
        Assert.notNull(depositor.getDepositorName(), ERROR_METHOD_PARAM + ": DepositorName");

        BankDepositor existingDepositor = null;
        try {
            existingDepositor = bankDepositorDao.getBankDepositorById(depositor.getDepositorId());
        } catch (EmptyResultDataAccessException e) {
        	LOGGER.warn("Error method dao.getBankDepositorById() in service.updateBankDepositor(), Exception:{}", e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
        if (existingDepositor != null) {
            bankDepositorDao.updateBankDepositor(depositor);
        } else {
            LOGGER.warn(ERROR_DEPOSITOR + "- method: updateBankDepositor()");
            throw new IllegalArgumentException(ERROR_DEPOSITOR);
        }
    }

    @Override
    @Transactional
    public List<BankDepositor> getBankDepositors() {
        LOGGER.debug("getBankDepositors()");
        List<BankDepositor> depositors = bankDepositorDao.getBankDepositors();
        Assert.notEmpty(depositors, ERROR_DB_EMPTY);
        return depositors;
    }
}


