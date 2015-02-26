package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.util.HibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.criterion.*;

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
        depositors = new ArrayList<BankDepositor>();
        for(Object d: session.createCriteria(BankDepositor.class).list()){
            depositors.add((BankDepositor)d);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositors:{}", depositors);
        return depositors;
    }

    //---- get by depositorId with get
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

    //---- get by depositorId with load
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

    //---- get by depositorId with Criteria
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

    //---- get by depositorName SQL
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

    //---- get by depositorName createCriteria
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

    //---- get by depositId createCriteria
    @Override
    public List<BankDepositor> getBankDepositorByIdDepositCriteria(Long id){
        LOGGER.debug("getBankDepositorByIdDepositCriteria({})",id);
        List<BankDepositor> dep = new ArrayList<BankDepositor>();
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object aDepositor: session.createCriteria(BankDepositor.class, "depositor")
                .createCriteria("deposit", "deposit")
                .add(Restrictions.eq("deposit.depositId", id))
                .addOrder(Order.asc("depositor.depositorId"))
                .list()){
            dep.add((BankDepositor)aDepositor);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositors:{}", dep);
        return dep;
    }

    //---- get Depositors by id Deposit between Date Deposit
    @Override
    public List<BankDepositor> getBankDepositorByIdDepositBetweenDateDeposit(Long id, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorBetweenDateDeposit({},{},{})",id,startDate,endDate);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object aDepositor: session.createCriteria(BankDepositor.class, "depositor")
                    .add(Restrictions.between("depositorDateDeposit",startDate,endDate))
                    .createAlias("deposit", "deposit")
                    .add(Restrictions.eq("deposit.depositId", id))
                    .addOrder(Order.asc("depositor.depositorId"))
                .list()){
            depositors.add((BankDepositor)aDepositor);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor:{}", depositor);
        return depositors;
    }

    //---- get Depositors by id Deposit between Date Return Deposit
    @Override
    public List<BankDepositor> getBankDepositorByIdDepositBetweenDateReturnDeposit(Long id, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorBetweenDateReturnDeposit({},{},{})",id,startDate,endDate);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object aDepositor: session.createCriteria(BankDepositor.class, "depositor")
                .add(Restrictions.between("depositorDateReturnDeposit",startDate,endDate))
                .createAlias("deposit", "deposit")
                .add(Restrictions.eq("deposit.depositId", id))
                .addOrder(Order.asc("depositor.depositorId"))
                .list()){
            depositors.add((BankDepositor)aDepositor);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor:{}", depositor);
        return depositors;
    }

    //---- get Depositors between Date Deposit
    @Override
    public List<BankDepositor> getBankDepositorBetweenDateDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorBetweenDateDeposit({},{})",startDate,endDate);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object aDepositor: session.createCriteria(BankDepositor.class, "depositor")
                .add(Restrictions.between("depositorDateDeposit", startDate, endDate))
                .addOrder(Order.asc("depositor.depositorId"))
                .list()){
            depositors.add((BankDepositor)aDepositor);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor:{}", depositor);
        return depositors;
    }

    //---- get Depositors between Date Return Deposit
    @Override
    public List<BankDepositor> getBankDepositorBetweenDateReturnDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorBetweenDateReturnDeposit({},{})",startDate,endDate);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object aDepositor: session.createCriteria(BankDepositor.class, "depositor")
                .add(Restrictions.between("depositorDateReturnDeposit", startDate, endDate))
                .addOrder(Order.asc("depositor.depositorId"))
                .list()){
            depositors.add((BankDepositor)aDepositor);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("depositor:{}", depositor);
        return depositors;
    }

    @Override
    public BankDepositor getBankDepositorSumAll(){
        LOGGER.debug("getBankDepositorSumAll()");
        //--- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        List list = session.createCriteria(BankDepositor.class)
                    .setProjection(Projections.projectionList()
                                    .add(Projections.sum("depositorAmountDeposit").as("depositorAmountDeposit"))
                                    .add(Projections.sum("depositorAmountPlusDeposit").as("depositorAmountPlusDeposit"))
                                    .add(Projections.sum("depositorAmountMinusDeposit").as("depositorAmountMinusDeposit"))
                    ).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        LOGGER.debug("list: {}", list);
        Map depositorSum = (Map)list.get(0);
        LOGGER.debug("depositorSum = {}", depositorSum);

        depositor = new BankDepositor();
        depositor.setDepositorAmountDeposit(Integer.parseInt(depositorSum.get("depositorAmountDeposit").toString()));
        depositor.setDepositorAmountPlusDeposit(Integer.parseInt(depositorSum.get("depositorAmountPlusDeposit").toString()));
        depositor.setDepositorAmountMinusDeposit(Integer.parseInt(depositorSum.get("depositorAmountMinusDeposit").toString()));

        //--- close connection
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depositor: {}", depositor);
        return depositor;
    }

    @Override
    public BankDepositor getBankDepositorByIdDepositSum(Long id){
        LOGGER.debug("getBankDepositorByIdDepositSum({})", id);
        //--- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        List list = session.createCriteria(BankDepositor.class)
                .createAlias("deposit", "deposit")
                .add(Restrictions.eq("deposit.depositId", id))
                .setProjection(Projections.projectionList()
                                .add(Projections.sum("depositorAmountDeposit").as("depositorAmountDeposit"))
                                .add(Projections.sum("depositorAmountPlusDeposit").as("depositorAmountPlusDeposit"))
                                .add(Projections.sum("depositorAmountMinusDeposit").as("depositorAmountMinusDeposit"))
                ).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        LOGGER.debug("list: {}", list);
        Map depositorSum = (Map)list.get(0);
        LOGGER.debug("depositorSum = {}", depositorSum);

        depositor = new BankDepositor();
        depositor.setDepositorAmountDeposit(Integer.parseInt(depositorSum.get("depositorAmountDeposit").toString()));
        depositor.setDepositorAmountPlusDeposit(Integer.parseInt(depositorSum.get("depositorAmountPlusDeposit").toString()));
        depositor.setDepositorAmountMinusDeposit(Integer.parseInt(depositorSum.get("depositorAmountMinusDeposit").toString()));

        //--- close connection
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depositor: {}", depositor);
        return depositor;
    }

    @Override
    public BankDepositor getBankDepositorBetweenDateDepositSum(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorBetweenDateDepositSum({},{})", startDate, endDate);
        //--- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        List list = session.createCriteria(BankDepositor.class)
                .add(Restrictions.between("depositorDateDeposit", startDate, endDate))
                .setProjection(Projections.projectionList()
                                .add(Projections.sum("depositorAmountDeposit").as("depositorAmountDeposit"))
                                .add(Projections.sum("depositorAmountPlusDeposit").as("depositorAmountPlusDeposit"))
                                .add(Projections.sum("depositorAmountMinusDeposit").as("depositorAmountMinusDeposit"))
                ).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        LOGGER.debug("list: {}", list);
        Map depositorSum = (Map)list.get(0);
        LOGGER.debug("depositorSum = {}", depositorSum);

        depositor = new BankDepositor();
        depositor.setDepositorAmountDeposit(Integer.parseInt(depositorSum.get("depositorAmountDeposit").toString()));
        depositor.setDepositorAmountPlusDeposit(Integer.parseInt(depositorSum.get("depositorAmountPlusDeposit").toString()));
        depositor.setDepositorAmountMinusDeposit(Integer.parseInt(depositorSum.get("depositorAmountMinusDeposit").toString()));

        //--- close connection
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depositor: {}", depositor);
        return depositor;
    }

    @Override
    public BankDepositor getBankDepositorBetweenDateReturnDepositSum(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositorBetweenDateReturnDepositSum({},{})", startDate, endDate);
        //--- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        List list = session.createCriteria(BankDepositor.class)
                .add(Restrictions.between("depositorDateReturnDeposit", startDate, endDate))
                .setProjection(Projections.projectionList()
                                .add(Projections.sum("depositorAmountDeposit").as("depositorAmountDeposit"))
                                .add(Projections.sum("depositorAmountPlusDeposit").as("depositorAmountPlusDeposit"))
                                .add(Projections.sum("depositorAmountMinusDeposit").as("depositorAmountMinusDeposit"))
                ).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        LOGGER.debug("list: {}", list);
        Map depositorSum = (Map)list.get(0);
        LOGGER.debug("depositorSum = {}", depositorSum);

        depositor = new BankDepositor();
        depositor.setDepositorAmountDeposit(Integer.parseInt(depositorSum.get("depositorAmountDeposit").toString()));
        depositor.setDepositorAmountPlusDeposit(Integer.parseInt(depositorSum.get("depositorAmountPlusDeposit").toString()));
        depositor.setDepositorAmountMinusDeposit(Integer.parseInt(depositorSum.get("depositorAmountMinusDeposit").toString()));

        //--- close connection
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depositor: {}", depositor);
        return depositor;
    }

    @Override
    public List<BankDepositor> getBankDepositorByMarkReturn(Integer mark){
        LOGGER.debug("getBankDepositorByMarkReturn({}): ", mark);
        //--- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object dep:session.createCriteria(BankDepositor.class)
                .add(Restrictions.eq("depositorMarkReturnDeposit", mark)
                ).list()){
            depositors.add((BankDepositor)dep);
        }
        //--- close session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        return depositors;
    }

    @Override
    public List<BankDepositor> getBankDepositorByIdDepositByMarkReturn(Long id, Integer mark){
        LOGGER.debug("getBankDepositorByIdDepositByMarkReturn({},{})", id, mark);
        //--- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object dep:session.createCriteria(BankDepositor.class)
                .add(Restrictions.eq("depositorMarkReturnDeposit", mark))
                .createAlias("deposit", "deposit")
                .add(Restrictions.eq("deposit.depositId",id))
                .list()){
            depositors.add((BankDepositor)dep);
        }
        //--- close session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depositors: {}", depositors);
        return depositors;
    }

    @Override
    public List<BankDepositor> getBankDepositorBetweenAmountDeposit(Integer start, Integer end){
        LOGGER.debug("getBankDepositorBetweenAmountDeposit({},{})", start, end);
        //--- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object dep:session.createCriteria(BankDepositor.class)
                .add(Restrictions.between("depositorAmountDeposit", start, end))
                .list()){
            depositors.add((BankDepositor)dep);
        }
        //--- close session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depositors: {}", depositors);
        return depositors;
    }

    @Override
    public BankDepositor getBankDepositorMaxAmount(){
        LOGGER.debug("getBankDepositorMaxAmount() - run");
        //--- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        DetachedCriteria maxAmount = DetachedCriteria.forClass(BankDepositor.class)
                                    .setProjection(Projections.projectionList()
                                            .add(Property.forName("depositorAmountDeposit").max()));
        depositor = (BankDepositor)session.createCriteria(BankDepositor.class)
                .add(Subqueries.propertiesEq(new String[]{"depositorAmountDeposit"}, maxAmount))
                .uniqueResult();

        //--- close session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depositors: {}",depositor);
        return depositor;
    }

    @Override
    public BankDepositor getBankDepositorByIdDepositMaxAmount(Long id){
        LOGGER.debug("getBankDepositorByIdDepositMaxAmount({})", id);
        //--- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        DetachedCriteria minAmount = DetachedCriteria.forClass(BankDepositor.class, "depositor")
                .createCriteria("deposit", "deposit")
                .add(Restrictions.eq("deposit.depositId", id))
                .setProjection(Projections.max("depositor.depositorAmountDeposit"));
        depositor = (BankDepositor)session.createCriteria(BankDepositor.class)
                .add(Subqueries.propertiesEq(new String[]{"depositorAmountDeposit"}, minAmount))
                .uniqueResult();
        //--- close session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depositor: {}", depositor);
        return depositor;
    }

    @Override
    public BankDepositor getBankDepositorMinAmount(){
        LOGGER.debug("getBankDepositorMaxAmount() - run");
        //--- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        DetachedCriteria maxAmount = DetachedCriteria.forClass(BankDepositor.class)
                .setProjection(Projections.projectionList()
                        .add(Property.forName("depositorAmountDeposit").min()));
        depositor = (BankDepositor)session.createCriteria(BankDepositor.class)
                .add(Subqueries.propertiesEq(new String[]{"depositorAmountDeposit"}, maxAmount))
                .uniqueResult();
        //--- close session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depositor: {}",depositor);
        return depositor;
    }

    @Override
    public BankDepositor getBankDepositorByIdDepositMinAmount(Long id){
        LOGGER.debug("getBankDepositorByIdDepositMinAmount({})", id);
        //--- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        DetachedCriteria minAmount = DetachedCriteria.forClass(BankDepositor.class, "depositor")
                .createCriteria("deposit", "deposit")
                    .add(Restrictions.eq("deposit.depositId", id))
                .setProjection(Projections.min("depositor.depositorAmountDeposit"));
        depositor = (BankDepositor)session.createCriteria(BankDepositor.class)
                .add(Subqueries.propertiesEq(new String[]{"depositorAmountDeposit"}, minAmount))
                .uniqueResult();
        //--- close session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depositor: {}", depositor);
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
