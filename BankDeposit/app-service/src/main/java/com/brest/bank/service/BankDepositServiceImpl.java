package com.brest.bank.service;

import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.dao.BankDepositorDao;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class BankDepositServiceImpl implements BankDepositService {
    public static final String ERROR_DB_EMPTY = "There is no RECORDS in the DataBase";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_ADD = "Deposit is present in DB";
    public static final String ERROR_DELETE = "The Deposit can not be deleted or changed";
    public static final String ERROR_DEPOSIT = "In the database there is no Deposit with such parameters";
    public static final String ERROR_DEPOSITOR = "There is no RECORDS of DEPOSITORS in the DataBase";

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BankDepositDao bankDepositDao;

    @Autowired
    private BankDepositorDao bankDepositorDao;

    @Autowired
    private BankDepositorService bankDepositorService;

    @Autowired
    public void setBankDepositDao(BankDepositDao bankDepositDao) {
        this.bankDepositDao = bankDepositDao;
    }

    @Autowired
    public void setBankDepositorDao(BankDepositorDao bankDepositorDao) {
	    this.bankDepositorDao = bankDepositorDao;
    }

    @Autowired
    public void setBankDepositorService(BankDepositorService bankDepositorService) {
        this.bankDepositorService = bankDepositorService;
    }

    @Override
    @Transactional
    public Long addBankDeposit(BankDeposit deposit) {
        Assert.notNull(deposit);
        Assert.isNull(deposit.getDepositId());
        Assert.notNull(deposit.getDepositName(), ERROR_METHOD_PARAM + ": DepositName");
        Assert.notNull(deposit.getDepositMinTerm(), ERROR_METHOD_PARAM + ": DepositMinTerm");
        BankDeposit existingDeposit = getBankDepositByName(deposit.getDepositName());
        if (existingDeposit != null) {
            throw new IllegalArgumentException(ERROR_ADD);
        }
        return bankDepositDao.addBankDeposit(deposit);
    }

    @Override
    @Transactional
    public BankDeposit getBankDepositByName(String depositName) {
        LOGGER.debug("getBankDepositByName(depositName={}) ", depositName);
        BankDeposit deposit = null;
        try {
            deposit = bankDepositDao.getBankDepositByName(depositName);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositByName({}), Exception:{}", depositName, e.toString());
        }
        return deposit;
    }
    
    @Override
    @Transactional
    public BankDeposit getBankDepositById(Long depositId) {
        LOGGER.debug("getBankDepositById(depositId={}) ", depositId);
        BankDeposit deposit = null;
        try {
            deposit = bankDepositDao.getBankDepositById(depositId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositById({}), Exception:{}",depositId, e.toString());
        }
	
        return deposit;
    }
    
    @Override
    @Transactional
    public void removeBankDeposit(Long depositId) {
        LOGGER.debug("removeBankDeposit(depositId={}) ", depositId);
        Assert.notNull(depositId,ERROR_METHOD_PARAM);
        BankDeposit deposit = null;
        try {
            deposit = bankDepositDao.getBankDepositById(depositId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("removeBankDeposit({}), Exception:{}",depositId, e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
        List<BankDepositor> depositors = bankDepositorService.getBankDepositorByIdDeposit(depositId);
        if ((depositors.isEmpty())|(depositors.size()==0)|(depositors==null)) {
            bankDepositDao.removeBankDeposit(depositId);
        } else {
            Assert.isTrue(bankDepositorService.removeBankDepositorByIdDeposit(depositId));
            bankDepositDao.removeBankDeposit(depositId);
        }
    }

    @Override
    @Transactional
    public void updateBankDeposit(BankDeposit deposit) {
        LOGGER.debug("updateBankDeposit(deposit={}) ", deposit);
        Assert.notNull(deposit);
        Assert.notNull(deposit.getDepositId());
        Assert.notNull(deposit.getDepositName(), ERROR_METHOD_PARAM + ": DepositName");
        BankDeposit existingDeposit = null;
        try {
            existingDeposit = bankDepositDao.getBankDepositById(deposit.getDepositId());
        } catch (EmptyResultDataAccessException e) {
        	LOGGER.warn("Error method dao.getBankDepositById() in service.updateBankDeposit(), Exception:{}", e.toString());
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
        if (existingDeposit != null) {
            bankDepositDao.updateBankDeposit(deposit);
        } else {
            LOGGER.warn(ERROR_DEPOSIT + "- method: updateBankDeposit()");
            throw new IllegalArgumentException(ERROR_DEPOSIT);
        }
    }

    @Override
    @Transactional
    public List<BankDeposit> getBankDeposits() {
        LOGGER.debug("getBankDeposits()");
        List<BankDeposit> deposits = bankDepositDao.getBankDeposits();
        Assert.notEmpty(deposits, ERROR_DB_EMPTY);
        return deposits;
    }
}


