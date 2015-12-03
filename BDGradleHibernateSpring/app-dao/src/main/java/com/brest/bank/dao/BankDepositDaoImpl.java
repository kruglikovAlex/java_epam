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

    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";

    private BankDeposit deposit;
    private List<BankDeposit> deposits;
    private Session session;
    private SessionFactory sessionFactory;

    /**
     * Set Hibernate session factory
     * @param sessionFactory SessionFactory
     */
    @Override
    public void setSession(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
        this.session = this.sessionFactory.getCurrentSession();
        this.session.beginTransaction();
        LOGGER.debug("BankDepositDaoImpl session: {}", session.toString());
    }

    /**
     * Get all deposits with Criteria
     * @return List<BankDeposit>
     */
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

    /**
     * Get by depositId with Criteria
     * @param id Long
     * @return BankDeposit
     */
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

    /**
     * get by depositName createCriteria
     * @param name String
     * @return BankDeposit
     */
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

    /**
     * Get Bank Deposits by CURRENCY
     * @param currency String
     * @return List<BankDeposit>
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsByCurrencyCriteria(String currency){
        LOGGER.debug("getBankDepositByCurrencyCriteria({})", currency);
        assertNotNull(currency);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            for(Object d: HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class)
                    .add(Restrictions.eq("depositCurrency",currency))
                    .list()){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

            LOGGER.debug("deposits: {}",deposits);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByCurrencyCriteria({}) - {}", currency, e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsByCurrencyCriteria() "+e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposits by INTEREST RATE
     * @param rate Integer
     * @return
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsByInterestRateCriteria(Integer rate){
        LOGGER.debug("getBankDepositsByInterestRateCriteria({})", rate);
        assertNotNull(rate);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            for(Object d: HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class)
                    .add(Restrictions.eq("depositInterestRate",rate))
                    .list()){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByInterestRateCriteria({}) - {}", rate, e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsByInterestRateCriteria() "+e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposits between MINTERM values
     * @param fromTerm Integer
     * @param toTerm Integer
     * @return List<BankDeposit>
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsFromToMinTermCriteria(Integer fromTerm, Integer toTerm){
        LOGGER.debug("getBankDepositsFromToMinTermCriteria({}, {})",fromTerm,toTerm);
        assertNotNull(ERROR_METHOD_PARAM,fromTerm);
        assertNotNull(ERROR_METHOD_PARAM,toTerm);
        assertTrue(fromTerm<=toTerm);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            for(Object d: HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class)
                    .add(Restrictions.between("depositMinTerm",fromTerm,toTerm))
                    .list()){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToMinTermCriteria({}, {}) - {}", fromTerm, toTerm, e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToMinTermCriteria() "+e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposits from-to Interest Rate values
     * @param startRate
     * @param endRate
     * @return List<BankDeposit>
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsFromToInterestRateCriteria(Integer startRate, Integer endRate){
        LOGGER.debug("getBankDepositsFromToInterestRateCriteria({}, {})",startRate,endRate);
        assertNotNull(ERROR_METHOD_PARAM,startRate);
        assertNotNull(ERROR_METHOD_PARAM,endRate);
        assertTrue(startRate<=endRate);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            for(Object d: HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class)
                    .add(Restrictions.between("depositInterestRate",startRate,endRate))
                    .list()){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToInterestRateCriteria({}, {}) - {}", startRate, endRate, e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToInterestRateCriteria() "+e.toString());
        }
        return deposits;
    }

    /**
     * Add Bank Deposit
     * @param deposit BankDeposit
     */
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

    /**
     * Update Bank Deposit
     * @param deposit BankDeposit
     */
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

    /**
     * Remove Bank Deposit
     * @param id Long
     */
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
