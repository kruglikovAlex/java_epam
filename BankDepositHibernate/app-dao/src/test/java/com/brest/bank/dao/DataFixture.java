package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.brest.bank.util.HibernateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public class DataFixture {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public Session session;
    public Object result;

    public static void init() throws ParseException, ClassNotFoundException{
        DataFixture df = new DataFixture();

        LOGGER.debug("start initDB - run method init(): ");
        BankDeposit deposit;
        BankDepositor depositor;
        Set depositors;

        df.session = HibernateUtil.getSessionFactory().getCurrentSession();
        df.session.beginTransaction();

        df.result = df.session.createCriteria(BankDeposit.class)
                .setProjection(Projections.rowCount()).uniqueResult();

        if (Integer.parseInt(df.result.toString()) == 0) {

            for (int i = 0; i < 4; i++) {
                deposit = new BankDeposit();
                deposit.setDepositName("depositName" + i);
                deposit.setDepositMinTerm(12);
                deposit.setDepositMinAmount(100);
                deposit.setDepositCurrency("usd");
                deposit.setDepositInterestRate(4);
                deposit.setDepositAddConditions("condition" + i);

                df.session.save(deposit);
            }

            for (int i = 1; i < 5; i++) {
                depositors = new HashSet();
                deposit = (BankDeposit)df.session.load(BankDeposit.class, (long) i);
                LOGGER.debug("deposit: {}", deposit);

                depositor = new BankDepositor();
                depositor.setDepositorName("depositorName" + i);
                depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-02"));
                depositor.setDepositorAmountDeposit(1000);
                depositor.setDepositorAmountPlusDeposit(10 * (i + 1));
                depositor.setDepositorAmountMinusDeposit(10 * (i + 1));
                depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-03"));
                depositor.setDepositorMarkReturnDeposit(0);

                depositors.add(depositor);

                deposit.setDepositors(depositors);
                LOGGER.debug("depositors1: {}", deposit.getDepositors());
                deposit.getDepositors().addAll(depositors);
                LOGGER.debug("depositors2: {}", deposit.getDepositors());

                df.session.update(deposit);
                for (Object d : deposit.getDepositors()) {
                    df.session.save((BankDepositor) d);
                }
            }
        }
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
    }
}
