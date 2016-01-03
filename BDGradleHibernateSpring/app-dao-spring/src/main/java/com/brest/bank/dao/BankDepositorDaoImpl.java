package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@ContextConfiguration(locations = {"classpath:/spring-dao.xml"})
public class BankDepositorDaoImpl implements BankDepositorDao {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";

    @Autowired
    private SessionFactory sessionFactory;

    private BankDepositor depositor;
    private List<BankDepositor> depositors = new ArrayList<BankDepositor>();

    /**
     * Set hibernate session factory
     *
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Get all Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    @Override
    @Transactional
    public List<BankDepositor> getBankDepositorsCriteria() {
        LOGGER.debug("getBankDepositorsCriteria()");

        depositors = new ArrayList<BankDepositor>();
        //--- open session
        sessionFactory.getCurrentSession().beginTransaction();
        //--- query
        for(Object d: sessionFactory.getCurrentSession()
                .createCriteria(BankDepositor.class).list()){
            depositors.add((BankDepositor)d);
        }
        //--- close session
        sessionFactory.getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositors:{}", depositors);
        return depositors;
    }

    /**
     * Get all Bank Depositors from-to Date Deposit
     *
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (startDate < endDate)
     * @return List<BankDepositors> a list of all bank depositors with the specified task`s date deposit
     */
    public List<BankDepositor> getBankDepositorsFromToDateDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositorsFromToDateDeposit()");

        depositors = new ArrayList<BankDepositor>();
        //--- open session
        sessionFactory.getCurrentSession().beginTransaction();
        //--- query
        for(Object d: sessionFactory.getCurrentSession()
                .createCriteria(BankDepositor.class, "depositor")
                .add(Restrictions.between("depositor.depositorDateDeposit", start, end))
                .list()){
            depositors.add((BankDepositor)d);
        }
        //--- close session
        sessionFactory.getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositors:{}", depositors);
        return depositors;
    }

    /**
     * Get all Bank Depositors from-to Date return Deposit
     *
     * @param start Date - start value of the date return deposit (startDate < endDate)
     * @param end Date - end value of the date return deposit (startDate < endDate)
     * @return List<BankDepositors> a list of all bank depositors with the specified task`s date return deposit
     */
    public List<BankDepositor> getBankDepositorsFromToDateReturnDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositorsFromToDateReturnDeposit()");

        depositors = new ArrayList<BankDepositor>();
        //--- open session
        sessionFactory.getCurrentSession().beginTransaction();
        //--- query
        for(Object d: sessionFactory.getCurrentSession()
                .createCriteria(BankDepositor.class, "depositor")
                .add(Restrictions.between("depositor.depositorDateReturnDeposit", start, end))
                .list()){
            depositors.add((BankDepositor)d);
        }
        //--- close session
        sessionFactory.getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositors:{}", depositors);
        return depositors;
    }

    /**
     * Get Bank Depositor by ID
     *
     * @param id  Long - id of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    @Override
    @Transactional
    public BankDepositor getBankDepositorByIdCriteria(Long id){
        LOGGER.debug("getBankDepositorByIdCriteria({})", id);
        Assert.notNull(id,ERROR_METHOD_PARAM);

        sessionFactory.getCurrentSession().beginTransaction();
        //--- query
        depositor = (BankDepositor)sessionFactory.getCurrentSession()
                .createCriteria(BankDepositor.class)
                .add(Restrictions.eq("depositorId", id)).uniqueResult();

        sessionFactory.getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor:{}", depositor);
        return depositor;
    }

    /**
     * Get Bank Depositors by ID Bank Deposit
     *
     * @param id  Long - id of the Bank Deposit
     * @return List<BankDepositor> with the specified id bank deposit from the database
     */
    public List<BankDepositor> getBankDepositorByIdDepositCriteria(Long id){
        LOGGER.debug("getBankDepositorByIdDepositCriteria({})", id);
        Assert.notNull(id,ERROR_METHOD_PARAM);

        sessionFactory.getCurrentSession().beginTransaction();
        //--- query
        BankDeposit deposit = (BankDeposit)sessionFactory.getCurrentSession()
                .createCriteria(BankDeposit.class)
                .add(Restrictions.eq("depositId", id)).uniqueResult();
        depositors = new ArrayList<BankDepositor>(deposit.getDepositors());

        sessionFactory.getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositors:{}", depositors);
        return depositors;
    }

    /**
     * Get Bank Depositor by Name
     *
     * @param name  String - name of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    @Override
    @Transactional
    public BankDepositor getBankDepositorByNameCriteria(String name){
        LOGGER.debug("getBankDepositorByNameCriteria({})",name);
        Assert.notNull(name,ERROR_METHOD_PARAM);

        sessionFactory.getCurrentSession().beginTransaction();
        //--- query
        depositor = (BankDepositor)sessionFactory.getCurrentSession()
                .createCriteria(BankDepositor.class)
                .add(Restrictions.eq("depositorName", name)).uniqueResult();

        sessionFactory.getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor:{}", depositor);
        return depositor;
    }

    /**
     * Adding Bank Depositor
     *
     * @param depositId Long - id of the Bank Deposit
     * @param depositor BankDepositor - Bank Depositor to be inserted to the database
     */
    @Override
    @Transactional
    public void addBankDepositor(Long depositId, BankDepositor depositor){
        LOGGER.debug("addBankDepositor({},{})",depositId, depositor);
        Assert.notNull(depositId,ERROR_METHOD_PARAM);
        Assert.notNull(depositor,ERROR_METHOD_PARAM);
        Assert.isNull(depositor.getDepositorId(), ERROR_NULL_PARAM);

        try {
            sessionFactory.getCurrentSession().beginTransaction();

            sessionFactory.getCurrentSession().save(depositor);

            BankDeposit theDeposit = (BankDeposit)sessionFactory
                    .getCurrentSession().createQuery("select p from BankDeposit p left join fetch p.depositors where p.depositId = :pid")
                    .setParameter("pid", depositId)
                    .uniqueResult();

            theDeposit.getDepositors().add(depositor);

            sessionFactory.getCurrentSession().getTransaction().commit();

        } catch (Exception e){
            LOGGER.error("error - addBankDepositor({}) - {}", depositor, e.toString());
            sessionFactory.getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - addBankDepositor()"+e.toString());
        }
    }

    /**
     * Updating Bank Depositor
     *
     * @param depositor BankDepositor - Bank Depositor to be stored in the database
     */
    @Override
    @Transactional
    public void updateBankDepositor(BankDepositor depositor){
        LOGGER.debug("updateBankDepositor({})",depositor);
        Assert.notNull(depositor,ERROR_METHOD_PARAM);
        Assert.notNull(depositor.getDepositorId(), ERROR_NULL_PARAM);

        try {
            //--- open session
            sessionFactory.getCurrentSession().beginTransaction();
            //--- update
            sessionFactory.getCurrentSession().update(depositor);
            //--- close session
            sessionFactory.getCurrentSession().getTransaction().commit();

        } catch (Exception e){
            LOGGER.error("error - updateBankDepositor({}) - {}", depositor, e.toString());
            sessionFactory.getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - updateBankDepositor()"+e.toString());
        }
    }

    /**
     * Deleting Bank Depositor by ID
     *
     * @param id Long - id of the Bank Depositor to be removed
     */
    @Override
    @Transactional
    public void removeBankDepositor(Long id){
        LOGGER.debug("removeBankDepositor({})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);

        try {
            sessionFactory.getCurrentSession().beginTransaction();

            BankDepositor depositor = (BankDepositor)sessionFactory.getCurrentSession()
                    .createCriteria(BankDepositor.class)
                    .add(Restrictions.eq("depositorId", id))
                    .uniqueResult();

            sessionFactory.getCurrentSession().delete(depositor);

            sessionFactory.getCurrentSession().getTransaction().commit();

        } catch (Exception e){
            LOGGER.error("error - removeBankDepositor({}) - {}", depositor, e.toString());
            sessionFactory.getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - removeBankDepositor()"+e.toString());
        }
    }
}
