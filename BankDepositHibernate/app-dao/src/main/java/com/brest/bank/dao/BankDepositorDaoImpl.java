package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.util.HibernateUtil;

import org.hibernate.Query;
import org.hibernate.Session;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.criterion.Restrictions;

import java.util.*;

import static org.junit.Assert.*;

public class BankDepositorDaoImpl implements BankDepositorDao {

    private static final Logger LOGGER = LogManager.getLogger();

    private BankDeposit deposit;
    private BankDepositor depositor;
    private List<BankDeposit> deposits = new ArrayList<BankDeposit>();
    private List<BankDepositor> depositors = new ArrayList<BankDepositor>();
    private Session session;

    @Override
    public void closeConnection() {
        session.getTransaction().commit();
        HibernateUtil.getSessionFactory().close();
    }

    //---- get all deposits with SQL
    @Override
    public List<BankDepositor> getBankDepositorsSQL() {
        LOGGER.debug("getBankDepositorsSQL()");
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for (Object d : session.createQuery("from BankDepositor").list()) {
            depositors.add((BankDepositor)d);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositors:{}", depositors);
        return depositors;
    }
    //---- get all deposits with Criteria
    @Override
    public List<BankDepositor> getBankDepositorsCriteria() {
        LOGGER.debug("getBankDepositorsCriteria()");
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object d: session.createCriteria(BankDepositor.class).list()){
            depositors.add((BankDepositor)d);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositors:{}", depositors);
        return depositors;
    }
    //---- get by depositId with get
    @Override
    public BankDepositor getBankDepositorByIdGet(Long id){
        LOGGER.debug("getBankDepositorByIdGet({})", id);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- GET
        depositor = (BankDepositor)session.get(BankDepositor.class, id);
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor: {}", depositor);
        return depositor;
    }
    //---- get by depositId with load
    @Override
    public BankDepositor getBankDepositorByIdLoad(Long id){
        LOGGER.debug("getBankDepositorByIdLoad({})", id);
        //--- соединение
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        //--- LOAD
        depositor = (BankDepositor)session.load(BankDepositor.class, id);
        //--- завершение сессии
        session.getTransaction().commit();

        LOGGER.debug("depositor: {}", depositor);
        return depositor;
    }
    //---- get by depositId with Criteria
    @Override
    public BankDepositor getBankDepositorByIdCriteria(Long id){
        LOGGER.debug("getBankDepositorByIdCriteria({})", id);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        depositor = (BankDepositor)session.createCriteria(BankDepositor.class)
                .add(Restrictions.eq("depositorId", id)).uniqueResult();
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor:{}", depositor);
        return depositor;
    }
    //---- get by depositName SQL
    @Override
    public BankDepositor getBankDepositorByNameSQL(String name){
        LOGGER.debug("getBankDepositorByNameSQL({})",name);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        String q = "from BankDepositor d WHERE d.depositorName=:findName";
        Query query = session.createQuery(q).setString("findName",name);
        depositor = (BankDepositor)query.uniqueResult();
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor:{}", depositor);
        return depositor;
    }
    //---- get by depositName createCriteria
    @Override
    public BankDepositor getBankDepositorByNameCriteria(String name){
        LOGGER.debug("getBankDepositorByNameCriteria({})",name);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        depositor = (BankDepositor)session.createCriteria(BankDepositor.class)
                    .add(Restrictions.eq("depositorName", name)).uniqueResult();
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor:{}", depositor);
        return depositor;
    }
    //---- add BankDepositor to BankDeposit
    @Override
    public void addBankDepositor(Long depositId, BankDepositor depositor){
        LOGGER.debug("addBankDepositor({},{})",depositId, depositor);
        assertNotNull(depositor);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            //--- add
            deposit = (BankDeposit)session.load(BankDeposit.class, depositId);
            LOGGER.debug("deposit-id{}: {}", depositId, deposit);
            deposit.getDepositors().add(depositor);

            session.update(deposit);
            session.save(depositor);
            //--- завершение сессии
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception e){
            LOGGER.error("error - addBankDeposit({}) - {}", deposit, e.toString());
            if (session != null) {
                session.flush();
                session.close();
            }
        }
    }
    //---- update
    @Override
    public void updateBankDepositor(BankDepositor depositor){
        LOGGER.debug("updateBankDepositor({})",depositor);
        assertNotNull(depositor);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            //--- update
            session.update(depositor);
            //--- завершение сессии
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception e){
            LOGGER.error("error - updateBankDepositor({}) - {}", depositor, e.toString());
            if (session != null) {
                session.flush();
                session.close();
            }
        }
    }
    //---- delete
    @Override
    public void removeBankDepositor(Long id){
        LOGGER.debug("removeBankDepositor({})",id);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            //--- query
            depositor = (BankDepositor)session.load(BankDepositor.class,id);
            deposit = depositor.getDeposit();
            deposit.getDepositors().remove(depositor);

            session.saveOrUpdate(deposit);
            session.delete(depositor);
            //--- завершение сессии
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception e){
            LOGGER.error("error - removeBankDepositor({}) - {}", depositor, e.toString());
            if (session != null) {
                session.flush();
                session.close();
            }
        }
    }
}
