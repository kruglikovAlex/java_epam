package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.brest.bank.util.HibernateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.service.UnknownServiceException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public class DataFixture {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Session session;
    private Object result;

    public void init() throws ParseException, ClassNotFoundException{

        LOGGER.debug("start initDB - run method init(): ");
        BankDeposit deposit;
        BankDepositor depositor;
        Set depositors;
        String date, returnDate;
        try{
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            result = HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class)
                    .setProjection(Projections.rowCount()).uniqueResult();

            if (Integer.parseInt(result.toString()) == 0) {

                for (int i = 0; i < 4; i++) {
                    deposit = new BankDeposit();
                        deposit.setDepositName("depositName" + i);
                        deposit.setDepositMinTerm(12+i);
                        deposit.setDepositMinAmount(100+100*i);
                        deposit.setDepositCurrency("usd");
                        deposit.setDepositInterestRate(4+i);
                        deposit.setDepositAddConditions("condition" + i);

                    HibernateUtil.getSessionFactory().getCurrentSession().save(deposit);
                }

                for (int i = 1; i < 5; i++) {
                    date = "2014-12-0"+i;
                    returnDate = "2014-12-0"+(i+1);
                    depositors = new HashSet();

                    deposit = (BankDeposit)HibernateUtil.getSessionFactory().getCurrentSession()
                                        .load(BankDeposit.class, (long) i);
                    LOGGER.debug("deposit: {}", deposit);

                    //---- first
                    depositor = new BankDepositor();
                        depositor.setDepositorName("depositorName" + i);
                        depositor.setDepositorDateDeposit(dateFormat.parse(date));
                        depositor.setDepositorAmountDeposit(1000+i);
                        depositor.setDepositorAmountPlusDeposit(10 * (i + 1));
                        depositor.setDepositorAmountMinusDeposit(10 * (i + 1));
                        depositor.setDepositorDateReturnDeposit(dateFormat.parse(returnDate));
                        depositor.setDepositorMarkReturnDeposit(0);

                    depositors.add(depositor);

                    deposit.setDepositors(depositors);
                    LOGGER.debug("depositors1: {}", deposit.getDepositors());
                    deposit.getDepositors().addAll(depositors);
                    LOGGER.debug("depositors2: {}", deposit.getDepositors());

                    HibernateUtil.getSessionFactory().getCurrentSession().update(deposit);

                    for (Object d : deposit.getDepositors()) {
                        HibernateUtil.getSessionFactory().getCurrentSession().save((BankDepositor) d);
                    }

                    //---- second
                    date = "2014-12-0"+(5+i);
                    returnDate = "2014-12-0"+(6+i);

                    depositor = new BankDepositor();
                        depositor.setDepositorName("depositorName" + (5+i));
                        depositor.setDepositorDateDeposit(dateFormat.parse(date));
                        depositor.setDepositorAmountDeposit(1000-i);
                        depositor.setDepositorAmountPlusDeposit(10 * (i + 5));
                        depositor.setDepositorAmountMinusDeposit(10 * (i + 5));
                        depositor.setDepositorDateReturnDeposit(dateFormat.parse(returnDate));
                        depositor.setDepositorMarkReturnDeposit(0);

                    depositors.add(depositor);

                    deposit.setDepositors(depositors);
                    LOGGER.debug("depositors1: {}", deposit.getDepositors());
                    deposit.getDepositors().addAll(depositors);
                    LOGGER.debug("depositors2: {}", deposit.getDepositors());

                    HibernateUtil.getSessionFactory().getCurrentSession().update(deposit);
                    for (Object d : deposit.getDepositors()) {
                        HibernateUtil.getSessionFactory().getCurrentSession().save((BankDepositor) d);
                    }
                }
            }
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        } catch (UnknownServiceException e) {
            LOGGER.debug("UnknownServiceException: ", e);
        }
    }
}
