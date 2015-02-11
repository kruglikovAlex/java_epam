package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.util.HibernateUtil;

import org.hibernate.Query;
import org.hibernate.Session;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.criterion.Restrictions;

import java.util.*;

import static org.junit.Assert.*;

public class BankDepositDaoImpl implements BankDepositDao {

    private static final Logger LOGGER = LogManager.getLogger();

    private BankDeposit deposit;
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
    public List<BankDeposit> getBankDepositsSQL() {
        LOGGER.debug("getBankDepositsSQL()");
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for (Object d : session.createQuery("from BankDeposit").list()) {
            deposits.add((BankDeposit)d);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposits:{}", deposits);
        return deposits;
    }
    //---- get all deposits with Criteria
    @Override
    public List<BankDeposit> getBankDepositsCriteria() {
        LOGGER.debug("getBankDepositsCriteria()");
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object d: session.createCriteria(BankDeposit.class).list()){
            deposits.add((BankDeposit)d);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposits:{}", deposits);
        return deposits;
    }
    //---- get by depositId with get
    @Override
    public BankDeposit getBankDepositByIdGet(Long id){
        LOGGER.debug("getBankDepositByIdGet({})", id);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- GET
        deposit = (BankDeposit)session.get(BankDeposit.class, id);
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposit: {}", deposit);
        return deposit;
    }
    //---- get by depositId with load
    @Override
    public BankDeposit getBankDepositByIdLoad(Long id){
        LOGGER.debug("getBankDepositByIdLoad({})", id);
        //--- соединение
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        //--- LOAD
        deposit = (BankDeposit)session.load(BankDeposit.class, id);
        //--- завершение сессии
        session.getTransaction().commit();

        LOGGER.debug("deposit: {}", deposit);
        return deposit;
    }
    //---- get by depositId with Criteria
    @Override
    public BankDeposit getBankDepositByIdCriteria(Long id){
        LOGGER.debug("getBankDepositByIdCriteria({})", id);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        deposit = (BankDeposit)session.createCriteria(BankDeposit.class)
                    .add(Restrictions.eq("depositId", id)).uniqueResult();
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposit:{}", deposit);
        return deposit;
    }
    //---- get by depositName SQL
    @Override
    public BankDeposit getBankDepositByNameSQL(String name){
        LOGGER.debug("getBankDepositByNameSQL({})",name);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        String q = "from BankDeposit d WHERE d.depositName=:findName";
        Query query = session.createQuery(q).setString("findName",name);
        deposit = (BankDeposit)query.uniqueResult();
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposit:{}", deposit);
        return deposit;
    }

    //---- get by depositName createCriteria
    @Override
    public BankDeposit getBankDepositByNameCriteria(String name){
        LOGGER.debug("getBankDepositByNameCriteria({})",name);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        deposit = (BankDeposit)session.createCriteria(BankDeposit.class)
                                .add(Restrictions.eq("depositName", name)).uniqueResult();
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposit:{}", deposit);
        return deposit;
    }

    //---- get by depositName where depositName mapped as a natural id - createCriteria
    @Override
    public BankDeposit getBankDepositByNameByNaturalIdCriteria(String name){
        LOGGER.debug("getBankDepositByNameCriteria({})",name);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        deposit = (BankDeposit)session.createCriteria(BankDeposit.class)
                .add(Restrictions.naturalId()
                        .set("depositName", name))
                .setCacheable(true)
                .uniqueResult();
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposit:{}", deposit);
        return deposit;
    }
    //---- get deposit with max value of depositMinTerm
    @Override
    public List<BankDeposit> getBankDepositBetweenMinTermCriteria(Integer minValue, Integer maxValue){
        LOGGER.debug("getBankDepositByMaxMinTermCriteria() - start");
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        deposits = session.createCriteria(BankDeposit.class)
                .add(Restrictions.between("depositMinTerm",minValue, maxValue)).list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depposits.size = {}", deposits.size());

        return deposits;
    }
    //---- create
    @Override
    public void addBankDeposit(BankDeposit deposit) {
        LOGGER.debug("addBankDeposit({})",deposit);
        assertNotNull(deposit);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            //--- add
            session.save(deposit);
            //--- завершение сессии
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception e){
            LOGGER.error("error - addBankDeposit({}) - {}", deposit, e.toString());
            if (session != null) {
                session.close();
            }
        }
    }
    //---- update
    @Override
    public void updateBankDeposit(BankDeposit deposit){
        LOGGER.debug("updateBankDeposit({})",deposit);
        assertNotNull(deposit);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            //--- update
            session.update(deposit);
            //--- завершение сессии
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception e){
            LOGGER.error("error - updateBankDeposit({}) - {}", deposit, e.toString());
            if (session != null) {
                session.close();
            }
        }
    }

    //---- delete
    @Override
    public void removeBankDeposit(Long id){
        LOGGER.debug("removeBankDeposit({})",id);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            //--- query
            BankDeposit deposit = (BankDeposit)session.load(BankDeposit.class,id);
            LOGGER.debug("deposit: {}", deposit);
            LOGGER.debug("depositors.size: {}", deposit.getDepositors().size());

            if (!deposit.getDepositors().isEmpty()) {
                for(Object d: deposit.getDepositors()){
                    //--- delete depend events
                    session.delete((BankDepositor)d);
                }//--- delete main event
                session.delete(deposit);
            } else {
                //--- delete main event
                session.delete(deposit);
            }
            //--- завершение сессии
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception e){
            LOGGER.error("error - removeBankDeposit({}) - {}", deposit, e.toString());
            if (session != null) {
                session.close();
            }
        }
    }
}
