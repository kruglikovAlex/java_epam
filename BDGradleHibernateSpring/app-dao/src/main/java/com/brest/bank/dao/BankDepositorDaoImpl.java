package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.brest.bank.util.HibernateUtil;
import org.hibernate.Criteria;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.criterion.*;

import java.util.*;

import org.springframework.util.Assert;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

@Component
public class BankDepositorDaoImpl implements BankDepositorDao {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";

    private BankDepositor depositor;
    private List<BankDepositor> depositors = new ArrayList<BankDepositor>();

    //---- get all deposits with Criteria
    @Override
    @Transactional
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
    @Transactional
    public BankDepositor getBankDepositorByIdCriteria(Long id){
        LOGGER.debug("getBankDepositorByIdCriteria({})", id);
        Assert.notNull(id,ERROR_METHOD_PARAM);

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
    @Transactional
    public BankDepositor getBankDepositorByNameCriteria(String name){
        LOGGER.debug("getBankDepositorByNameCriteria({})",name);
        Assert.notNull(name,ERROR_METHOD_PARAM);

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
    @Transactional
    public void addBankDepositor(Long depositId, BankDepositor depositor){
        LOGGER.debug("addBankDepositor({},{})",depositId, depositor);
        Assert.notNull(depositId,ERROR_METHOD_PARAM);
        Assert.notNull(depositor,ERROR_METHOD_PARAM);
        Assert.isNull(depositor.getDepositorId(), ERROR_NULL_PARAM);

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
    @Transactional
    public void updateBankDepositor(BankDepositor depositor){
        LOGGER.debug("updateBankDepositor({})",depositor);
        Assert.notNull(depositor,ERROR_METHOD_PARAM);
        Assert.notNull(depositor.getDepositorId(), ERROR_NULL_PARAM);

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
    @Transactional
    public void removeBankDepositor(Long id){
        LOGGER.debug("removeBankDepositor({})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);

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
