package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.brest.bank.util.HibernateUtil;
import org.hibernate.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.criterion.*;

import java.util.*;

import static org.junit.Assert.*;

public class BankDepositorDaoImpl implements BankDepositorDao {

    private static final Logger LOGGER = LogManager.getLogger();

    private BankDepositor depositor;
    private BankDeposit deposit;
    public List<BankDepositor> depositors = new ArrayList<BankDepositor>();
    private Session session;
    public SessionFactory sessionFactory;

    @Override
    public void setSession(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
        this.session = this.sessionFactory.getCurrentSession();
        this.session.beginTransaction();
        LOGGER.debug("BankDepositDaoImpl session: {}", session.toString());
    }

    //---- get all deposits with Criteria
    @Override
    public List<BankDepositor> getBankDepositorsCriteria() {
        LOGGER.debug("getBankDepositorsCriteria()");

        depositors = new ArrayList<BankDepositor>();
        //--- open session
        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        //--- query
        for(Object d: HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(BankDepositor.class).list()){
            depositors.add((BankDepositor)d);
        }
        //--- close session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositors:{}", depositors);
        return depositors;
    }

    //---- get by depositorId with Criteria
    @Override
    public BankDepositor getBankDepositorByIdCriteria(Long id){
        LOGGER.debug("getBankDepositorByIdCriteria({})", id);

        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        //--- query
        depositor = (BankDepositor)HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(BankDepositor.class)
                .add(Restrictions.eq("depositorId", id)).uniqueResult();

        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor:{}", depositor);
        return depositor;
    }

    //---- get by depositorName createCriteria
    @Override
    public BankDepositor getBankDepositorByNameCriteria(String name){
        LOGGER.debug("getBankDepositorByNameCriteria({})",name);


        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        //--- query
        depositor = (BankDepositor)HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(BankDepositor.class)
                .add(Restrictions.eq("depositorName", name)).uniqueResult();

        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor:{}", depositor);
        return depositor;
    }

    //---- add BankDepositor to BankDeposit
    @Override
    public void addBankDepositor(Long depositId, BankDepositor depositor){
        LOGGER.debug("addBankDepositor({},{})",depositId, depositor);
        assertNotNull(depositor);

        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            HibernateUtil.getSessionFactory()
                    .getCurrentSession().save(depositor);

            BankDeposit theDeposit = (BankDeposit)HibernateUtil.getSessionFactory()
                    .getCurrentSession().createQuery("select p from BankDeposit p left join fetch p.depositors where p.depositId = :pid")
                    .setParameter("pid", depositId)
                    .uniqueResult();

            theDeposit.getDepositors().add(depositor);

            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        } catch (Exception e){
            LOGGER.error("error - addBankDepositor({}) - {}", depositor, e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - addBankDepositor()"+e.toString());
        }
    }

    //---- update
    @Override
    public void updateBankDepositor(BankDepositor depositor){
        LOGGER.debug("updateBankDepositor({})",depositor);
        assertNotNull(depositor);

        try {
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- update
            HibernateUtil.getSessionFactory()
                    .getCurrentSession().update(depositor);
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        } catch (Exception e){
            LOGGER.error("error - updateBankDepositor({}) - {}", depositor, e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - updateBankDepositor()"+e.toString());
        }
    }

    //---- delete
    @Override
    public void removeBankDepositor(Long id){
        LOGGER.debug("removeBankDepositor({})",id);
        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            BankDepositor depositor = (BankDepositor)HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDepositor.class)
                    .add(Restrictions.eq("depositorId", id))
                    .uniqueResult();

            HibernateUtil.getSessionFactory().getCurrentSession().delete(depositor);

            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        } catch (Exception e){
            LOGGER.error("error - removeBankDepositor({}) - {}", depositor, e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - removeBankDepositor()"+e.toString());
        }
    }
}
