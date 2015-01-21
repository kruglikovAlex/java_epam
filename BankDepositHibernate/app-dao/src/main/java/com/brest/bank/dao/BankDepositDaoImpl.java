package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;


public class BankDepositDaoImpl implements BankDepositDao {

    private static final Logger LOGGER = LogManager.getLogger();

    private Configuration CONFIGURATION = new Configuration().configure();
    private ServiceRegistry SERVICEREGISTRY = new StandardServiceRegistryBuilder().applySettings(CONFIGURATION.getProperties()).build();

    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    private BankDeposit deposit;

    @Override
    public void startConnection() {
        sessionFactory = CONFIGURATION.buildSessionFactory(SERVICEREGISTRY);
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        LOGGER.debug("init({})",session);
    }

    @Override
    public void closeConnection() {
        session.close();
        sessionFactory.close();
    }

    @Override
    public Session getCurrentSession() {
        LOGGER.debug("getCurrentSession({})",sessionFactory.getCurrentSession());
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory){
        LOGGER.debug("setSessionFactory({})",sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    //---- get all deposits with SQL
    @Override
    public List<BankDeposit> getBankDepositsSQL() {
        LOGGER.debug("getBankDepositsSQL()");

        String q = "from BankDeposit";
        Query query = getCurrentSession().createQuery(q);
        List<BankDeposit> deposits = query.list();

        LOGGER.debug("deposits:", deposits);
        return deposits;
    }
    //---- get all deposits with Criteria
    @Override
    public List<BankDeposit> getBankDepositsCriteria() {
        LOGGER.debug("getBankDepositsCriteria()");

        List<BankDeposit> deposits = getCurrentSession().createCriteria(BankDeposit.class).list();

        LOGGER.debug("deposits:", deposits);
        return deposits;
    }
    //---- get by depositId with get
    @Override
    public BankDeposit getBankDepositByIdGet(Long id){
        LOGGER.debug("getBankDepositByIdGet({})", id);
        return (BankDeposit)getCurrentSession().get(BankDeposit.class, id);
    }
    //---- get by depositId with load
    @Override
    public BankDeposit getBankDepositByIdLoad(Long id){
        LOGGER.debug("getBankDepositByIdLoad({})", id);
        return (BankDeposit)getCurrentSession().load(BankDeposit.class, id);
    }
    //---- get by depositId with Criteria
    @Override
    public BankDeposit getBankDepositByIdCriteria(Long id){
        LOGGER.debug("getBankDepositByIdCriteria({})", id);
        return (BankDeposit)getCurrentSession().createCriteria(BankDeposit.class).add(Restrictions.eq("depositId", id)).uniqueResult();
    }
    //---- get by depositName: Object -> to BankDeposit
    @Override
    public BankDeposit getBankDepositByNameObject(String name){
        LOGGER.debug("getBankDepositByNameObject({})",name);
        String q = "from BankDeposit d WHERE d.depositName=:findName";

        Query query = session.createQuery(q).setString("findName",name);

        deposit = (BankDeposit)query.uniqueResult();

        LOGGER.debug("deposit:", deposit);
        return deposit;
    }

    //---- get by depositName: List -> list.get(0) -> BankDeposit
    @Override
    public BankDeposit getBankDepositByNameList(String name){
        LOGGER.debug("getBankDepositByNameList({})",name);
        return (BankDeposit)getCurrentSession().createCriteria(BankDeposit.class).add(Restrictions.eq("depositName", name)).uniqueResult();
    }
    //---- create
    @Override
    public void addBankDeposit(BankDeposit deposit) {
        LOGGER.debug("addBankDeposit({})",deposit);
        try {
            session.save(deposit);
            transaction.commit();
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
    public void updateBankDeposit(BankDeposit deposit){
        LOGGER.debug("updateBankDeposit({})",deposit);

        getCurrentSession().saveOrUpdate(deposit);
        //transaction.commit();
    }

    //---- delete
    @Override
    public void removeBankDeposit(Long id){
        LOGGER.debug("delete({})",id);
        BankDeposit deposit = (BankDeposit)sessionFactory.getCurrentSession().load(BankDeposit.class,id);
        if (null != deposit) {
            getCurrentSession().delete(deposit);
            //transaction.commit();
        }
    }
}
