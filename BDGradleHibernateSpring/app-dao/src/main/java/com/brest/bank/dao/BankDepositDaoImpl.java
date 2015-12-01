package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.util.HibernateUtil;
import org.hibernate.Session;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;

import java.util.*;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@Component
public class BankDepositDaoImpl implements BankDepositDao {

    private static final Logger LOGGER = LogManager.getLogger();

    private BankDeposit deposit;
    private List<BankDeposit> deposits;
    private Session session;
    private SessionFactory sessionFactory;

    @Override
    public void setSession(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
        this.session = this.sessionFactory.getCurrentSession();
        this.session.beginTransaction();
        LOGGER.debug("BankDepositDaoImpl session: {}", session.toString());
    }

    //---- get all deposits with Criteria
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsCriteria() {
        LOGGER.debug("getBankDepositsCriteria()");
        deposits = new ArrayList<BankDeposit>();
        //--- open session
        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        //--- query
        for(Object d: HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(BankDeposit.class).list()){
            deposits.add((BankDeposit)d);
        }
        //--- close session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposits:{}", deposits);
        return deposits;
    }

    //---- get by depositId with Criteria
    @Override
    @Transactional
    public BankDeposit getBankDepositByIdCriteria(Long id){
        LOGGER.debug("getBankDepositByIdCriteria({})", id);
        //--- open session
        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        //--- query
        deposit = (BankDeposit)HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(BankDeposit.class)
                .add(Restrictions.eq("depositId", id)).uniqueResult();
        //--- close session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposit:{}", deposit);
        return deposit;
    }

    //---- get by depositName createCriteria
    @Override
    @Transactional
    public BankDeposit getBankDepositByNameCriteria(String name){
        LOGGER.debug("getBankDepositByNameCriteria({})",name);

        //--- open session
        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        //--- query
        deposit = (BankDeposit)HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(BankDeposit.class)
                .add(Restrictions.eq("depositName", name)).uniqueResult();
        //--- close session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposit:{}", deposit);
        return deposit;
    }

    //---- create
    @Override
    @Transactional
    public void addBankDeposit(BankDeposit deposit) {
        LOGGER.debug("addBankDeposit({})",deposit);
        assertNotNull(deposit);
        try {
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- сохранение объекта
            HibernateUtil.getSessionFactory()
                    .getCurrentSession().save(deposit);
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception e){
            LOGGER.error("error - addBankDeposit({}) - {}", deposit, e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - addBankDeposit()"+e.toString());
        }
    }

    //---- update
    @Override
    @Transactional
    public void updateBankDeposit(BankDeposit deposit){
        LOGGER.debug("updateBankDeposit({})",deposit);
        assertNotNull(deposit);

        try {
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- update
            HibernateUtil.getSessionFactory()
                    .getCurrentSession().update(deposit);
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        } catch (Exception e){
            LOGGER.error("error - updateBankDeposit({}) - {}", deposit, e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - updateBankDeposit()"+e.toString());
        }
    }

    @Override
    @Transactional
    public void deleteBankDeposit(Long id){
        LOGGER.debug("deleteBankDeposit({})",id);
        assertNotNull(id);
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- delete
            BankDeposit deposit = (BankDeposit) HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class)
                    .add(Restrictions.eq("depositId", id))
                    .uniqueResult();

            HibernateUtil.getSessionFactory().getCurrentSession()
                    .delete(deposit);
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        }catch(Exception e){
            LOGGER.error("error - deleteBankDeposit({}) - {}", deposit, e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - deleteBankDeposit()"+e.toString());
        }

    }

}
