package com.brest.bank.service;

import com.brest.bank.dao.BankDepositorDaoImpl;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.dao.BankDepositorDao;

import com.brest.bank.domain.BankDeposit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import org.hibernate.exception.DataException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BankDepositorServiceImpl implements BankDepositorService{

    private static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final String ERROR_ADD = "Depositor is present in DB";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_DEPOSIT = "In the database there is no Depositor with such parameters";

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
        }catch (DataException e){
            LOGGER.error("getBankDepositors(), Exception:{}",e.toString());
        }
        return depositors;
    }
}
