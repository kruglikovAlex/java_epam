package com.brest.bank.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public class DateFixture {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void init(BankDepositDao depositDao){

        depositDao.getCurrentSession().getTransaction().begin();
        int i = depositDao.getCurrentSession().createSQLQuery("insert into BankDeposit (depositName,depositMinTerm,depositMinAmount," +
                "depositCurrency,depositInterestRate,depositAddConditions) VALUES ('depositName1',12,100,'usd',4,'condition1')").executeUpdate();
        i += depositDao.getCurrentSession().createSQLQuery("insert into BankDeposit (depositId,depositName,depositMinTerm,depositMinAmount," +
                "depositCurrency,depositInterestRate,depositAddConditions) VALUES (2,'depositName2',12,100,'eur',5,'condition2')").executeUpdate();
        i += depositDao.getCurrentSession().createSQLQuery("insert into BankDeposit (depositId,depositName,depositMinTerm,depositMinAmount," +
                "depositCurrency,depositInterestRate,depositAddConditions) VALUES (3,'depositName3',12,100,'eur',5,'condition3')").executeUpdate();
        i += depositDao.getCurrentSession().createSQLQuery("insert into BankDeposit (depositId,depositName,depositMinTerm,depositMinAmount," +
                "depositCurrency,depositInterestRate,depositAddConditions) VALUES (4,'depositName4',9,100,'eur',5,'condition4');").executeUpdate();

        depositDao.getCurrentSession().getTransaction().commit();
        LOGGER.debug("count test rows = {}", i);
    }

}
