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

import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private BankDepositorService bankDepositorService;

    @Autowired
    public void setBankDepositDao(BankDepositDao bankDepositDao) {
        this.bankDepositDao = bankDepositDao;
    }

    @Autowired
    public void setBankDepositorService(BankDepositorService bankDepositorService) {
        this.bankDepositorService = bankDepositorService;
    }

    @Override
    @Transactional
    public Long addBankDeposit(BankDeposit deposit) {
        LOGGER.debug("addBankDeposit({}) ", deposit);
        Assert.notNull(deposit);
        Assert.isNull(deposit.getDepositId());
        //Assert.notNull(deposit.getDepositName(), ERROR_METHOD_PARAM + ": DepositName");
        //Assert.notNull(deposit.getDepositMinTerm(), ERROR_METHOD_PARAM + ": DepositMinTerm");
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
        if (depositors.isEmpty()) {
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
        //Assert.notNull(deposit.getDepositName(), ERROR_METHOD_PARAM + ": DepositName");
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

    @Override
    @Transactional
    public List<Map> getBankDepositsAllDepositors() {
        LOGGER.debug("getBankDepositsAllDepositors()");
        List<Map> listDeposits = bankDepositDao.getBankDepositsAllDepositors();
        Assert.notEmpty(listDeposits, ERROR_DB_EMPTY);
        return listDeposits;
    }

    @Override
    @Transactional
    public List<Map> getBankDepositsAllDepositorsBetweenDateDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsAllDepositorsBetweenDateDeposit(Dates:{},{})", startDate, endDate);
        List<Map> depositsList = null;
        try {
            depositsList = bankDepositDao.getBankDepositsAllDepositorsBetweenDateDeposit(startDate, endDate);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositsAllDepositorsBetweenDateDeposit({}{}), Exception:{}",startDate, endDate, e.toString());
        }
        Assert.notEmpty(depositsList, ERROR_DB_EMPTY);
        return depositsList;
    }

    @Override
    @Transactional
    public List<Map> getBankDepositsAllDepositorsBetweenDateReturnDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsAllDepositorsBetweenDateReturnDeposit(Date:{}{}) ", startDate, endDate);
        List<Map> depositsList = null;
        try {
            depositsList = bankDepositDao.getBankDepositsAllDepositorsBetweenDateReturnDeposit(startDate, endDate);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositsAllDepositorsBetweenDateReturnDeposit({}{}), Exception:{}",startDate, endDate, e.toString());
        }
        Assert.notEmpty(depositsList, ERROR_DB_EMPTY);
        return depositsList;
    }

    @Override
    @Transactional
    public List<Map> getBankDepositByIdAllDepositors(Long depositId) {
        LOGGER.debug("getBankDepositByIdAllDepositors({})", depositId);
        List<Map> depositsList = null;
        try {
            depositsList = bankDepositDao.getBankDepositByIdAllDepositors(depositId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositByIdAllDepositors({}), Exception:{}",depositId, e.toString());
        }
        Assert.notEmpty(depositsList, ERROR_DB_EMPTY);
        return depositsList;
    }

    @Override
    @Transactional
    public List<Map> getBankDepositByIdWithAllDepositorsBetweenDateDeposit(Long depositId, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositByIdWithAllDepositorsBetweenDateDeposit(depositId={}, Dates={},{})", depositId, startDate, endDate);
        List<Map> depositsList = null;
        try {
            depositsList = bankDepositDao.getBankDepositByIdWithAllDepositorsBetweenDateDeposit(depositId, startDate, endDate);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositByIdWithAllDepositorsBetweenDateDeposit(depositId={}, Dates={},{}), Exception:{}",depositId, startDate, endDate, e.toString());
        }
        Assert.notEmpty(depositsList, ERROR_DB_EMPTY);
        return depositsList;
    }

    @Override
    @Transactional
    public List<Map> getBankDepositByIdWithAllDepositorsBetweenDateReturnDeposit(Long depositId, Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByIdWithAllDepositorsBetweenDateReturnDeposit(depositId={}, Dates={},{})", depositId, startDate, endDate);
        List<Map> depositsList = null;
        try {
            depositsList = bankDepositDao.getBankDepositByIdWithAllDepositorsBetweenDateReturnDeposit(depositId, startDate, endDate);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositByIdWithAllDepositorsBetweenDateReturnDeposit(depositId={}, Dates={},{}), Exception:{}",depositId, startDate, endDate, e.toString());
        }
        Assert.notEmpty(depositsList, ERROR_DB_EMPTY);
        return depositsList;
    }

    @Override
    @Transactional
    public List<Map> getBankDepositByNameAllDepositors(String depositName){
        LOGGER.debug("getBankDepositByNameAllDepositors(depositName={}) ", depositName);
        List<Map> depositList = null;
        try {
            depositList = bankDepositDao.getBankDepositByNameAllDepositors(depositName);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositByNameAllDepositors({}), Exception:{}", depositName, e.toString());
        }
        return depositList;
    }

    @Override
    @Transactional
    public List<Map> getBankDepositByNameWithAllDepositorsBetweenDateDeposit(String depositName, Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByNameWithAllDepositorsBetweenDateDeposit(depositName={}, Dates={},{})", depositName, startDate, endDate);
        List<Map> depositsList = null;
        try {
            depositsList = bankDepositDao.getBankDepositByNameWithAllDepositorsBetweenDateDeposit(depositName, startDate, endDate);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositByNameWithAllDepositorsBetweenDateDeposit(depositName={}, Dates={},{}), Exception:{}",depositName, startDate, endDate, e.toString());
        }
        Assert.notEmpty(depositsList, ERROR_DB_EMPTY);
        return depositsList;
    }

    @Override
    @Transactional
    public List<Map> getBankDepositByNameWithAllDepositorsBetweenDateReturnDeposit(String depositName, Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByNameWithAllDepositorsBetweenDateReturnDeposit(depositName={}, Dates={},{})", depositName, startDate, endDate);
        List<Map> depositsList = null;
        try {
            depositsList = bankDepositDao.getBankDepositByNameWithAllDepositorsBetweenDateReturnDeposit(depositName, startDate, endDate);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getBankDepositByNameWithAllDepositorsBetweenDateReturnDeposit(depositName={}, Dates={},{}), Exception:{}",depositName, startDate, endDate, e.toString());
        }
        Assert.notEmpty(depositsList, ERROR_DB_EMPTY);
        return depositsList;
    }
}


