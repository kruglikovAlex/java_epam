package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.util.HibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.criterion.*;
import org.hibernate.mapping.*;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.AliasToBeanResultTransformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BankDepositDaoImpl implements BankDepositDao {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
    //---- get by depositCurrency createCriteria
    @Override
    public List<BankDeposit> getBankDepositsByCurrencyCriteria(String currency){
        LOGGER.debug("getBankDepositByCurrencyCriteria({})",currency);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object aDeposits: session.createCriteria(BankDeposit.class)
                .add(Restrictions.eq("depositCurrency", currency))
                .list()){
            deposits.add((BankDeposit)aDeposits);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposits:{}", deposits);
        return deposits;
    }

    //---- get by depositCurrency createCriteria
    @Override
    public List<BankDeposit> getBankDepositsByInterestRateCriteria(Integer rate){
        LOGGER.debug("getBankDepositByInterestRateCriteria({})",rate);
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        for(Object aDeposits: session.createCriteria(BankDeposit.class)
                .add(Restrictions.eq("depositInterestRate", rate))
                .list()){
            deposits.add((BankDeposit)aDeposits);
        }
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        LOGGER.debug("deposits:{}", deposits);
        return deposits;
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
    //---- get deposit by Currency with aggregation and grouping Depositors
    @Override
    public List<Map> getBankDepositByCurrencyWithDepositors(String currency){
        LOGGER.debug("getBankDepositByCurrencyWithDepositors({})",currency);
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                .add(Restrictions.eq("deposit.depositCurrency", currency))
                .createAlias("depositors", "depositor")
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("deposit.depositId"), "depositId")
                                .add(formProjection(properties))
                                .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                .add(Projections.groupProperty("deposit.depositId"))
                ))
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
    }

    @Override
    public List<Map> getBankDepositByCurrencyBetweenDateDepositWithDepositors(String currency,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByCurrencyBetweenDateDepositWithDepositors({},{},{})",currency, startDate, endDate);
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                .add(Restrictions.eq("deposit.depositCurrency", currency))
                .createAlias("depositors", "depositor")
                .add(Restrictions.between("depositor.depositorDateDeposit", startDate, endDate))
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("deposit.depositId"), "depositId")
                                .add(formProjection(properties))
                                .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                .add(Projections.groupProperty("deposit.depositId"))
                ))
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
    }

    @Override
    public List<Map> getBankDepositByCurrencyBetweenDateReturnDepositWithDepositors(String currency,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByCurrencyBetweenDateReturnDepositWithDepositors({},{},{})",currency, startDate, endDate);
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                .add(Restrictions.eq("deposit.depositCurrency", currency))
                .createAlias("depositors", "depositor")
                .add(Restrictions.between("depositor.depositorDateReturnDeposit", startDate, endDate))
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("deposit.depositId"), "depositId")
                                .add(formProjection(properties))
                                .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                .add(Projections.groupProperty("deposit.depositId"))
                ))
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
    }

    //---- get deposit by InterestRate with aggregation and grouping Depositors
    @Override
    public List<Map> getBankDepositByInterestRateWithDepositors(Integer rate){
        LOGGER.debug("getBankDepositByInterestRateWithDepositors({})",rate);
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                .add(Restrictions.eq("deposit.depositInterestRate", rate))
                .createAlias("depositors", "depositor")
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("deposit.depositId"), "depositId")
                                .add(formProjection(properties))
                                .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                .add(Projections.groupProperty("deposit.depositId"))
                ))
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
    }

    //---- get deposit between InterestRate with aggregation and grouping Depositors
    @Override
    public List<Map> getBankDepositBetweenInterestRateWithDepositors(Integer startRate, Integer endRate){
        LOGGER.debug("getBankDepositBetweenInterestRateWithDepositors({},{})",startRate, endRate);
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                .add(Restrictions.between("deposit.depositInterestRate", startRate, endRate))
                .createAlias("depositors", "depositor")
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("deposit.depositId"), "depositId")
                                .add(formProjection(properties))
                                .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                .add(Projections.groupProperty("deposit.depositId"))
                ))
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
    }

    //---- get deposit between InterestRate and Date Deposit with aggregation and grouping Depositors
    @Override
    public List<Map> getBankDepositBetweenInterestRateBetweenDateDepositWithDepositors(Integer startRate, Integer endRate,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositBetweenInterestRateBetweenDateDepositWithDepositors({},{},{},{})",startRate, endRate, startDate,endDate);
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                .add(Restrictions.between("deposit.depositInterestRate", startRate, endRate))
                .createAlias("depositors", "depositor")
                .add(Restrictions.between("depositor.depositorDateDeposit", startDate, endDate))
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("deposit.depositId"), "depositId")
                                .add(formProjection(properties))
                                .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                .add(Projections.groupProperty("deposit.depositId"))
                ))
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
    }

    @Override
    public List<Map> getBankDepositBetweenInterestRateBetweenDateReturnDepositWithDepositors(Integer startRate, Integer endRate,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositBetweenInterestRateBetweenDateReturnDepositWithDepositors({},{},{},{})",startRate, endRate, startDate,endDate);
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                .add(Restrictions.between("deposit.depositInterestRate", startRate, endRate))
                .createAlias("depositors", "depositor")
                .add(Restrictions.between("depositor.depositorDateReturnDeposit", startDate, endDate))
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("deposit.depositId"), "depositId")
                                .add(formProjection(properties))
                                .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                .add(Projections.groupProperty("deposit.depositId"))
                ))
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
    }

    //---- get deposit with max value of depositMinTerm
    @Override
    public List<BankDeposit> getBankDepositsBetweenMinTermCriteria(Integer minValue, Integer maxValue){
        LOGGER.debug("getBankDepositBetweenMinTermCriteria({},{})",minValue,maxValue);
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        for(Object d: session.createCriteria(BankDeposit.class)
                .add(Restrictions.between("depositMinTerm", minValue, maxValue)).list()){
            deposits.add((BankDeposit)d);
        }
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depposits.size = {}", deposits.size());

        return deposits;
    }

    //---- get deposit with max value of depositMinTerm
    @Override
    public List<BankDeposit> getBankDepositsBetweenInterestRateCriteria(Integer startRate, Integer endRate){
        LOGGER.debug("getBankDepositBetweenInterestRateCriteria({},{})",startRate, endRate);
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        for(Object d: session.createCriteria(BankDeposit.class)
                .add(Restrictions.between("depositInterestRate", startRate, endRate)).list()){
            deposits.add((BankDeposit)d);
        }
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("depposits.size = {}", deposits.size());

        return deposits;
    }
    //---- get deposit where Date Deposit between days
    @Override
    public List<BankDeposit> getBankDepositsBetweenDateDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsBetweenDateDeposit({},{})",dateFormat.format(startDate),dateFormat.format(endDate));
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        for(Object d:session.createCriteria(BankDeposit.class)
                                .createAlias("depositors", "depositor")
                                .add(Restrictions.between("depositor.depositorDateDeposit", startDate, endDate))
                                .setProjection(Projections.distinct(Projections.projectionList()
                                    .add(Projections.property("depositId"),"depositId")
                                    .add(formProjection(properties)))
                                )
                                .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class))
                                .list()) {
            deposits.add((BankDeposit)d);
        }
        //---- end session
        LOGGER.debug("deposits.size = {}", deposits.size());
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        return deposits;
    }
    //---- get deposit where Date Return Deposit between days
    @Override
    public List<BankDeposit> getBankDepositsBetweenDateReturnDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsBetweenDateReturnDeposit({},{})",dateFormat.format(startDate),dateFormat.format(endDate));
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        for(Object d:session.createCriteria(BankDeposit.class)
                .createAlias("depositors", "depositor")
                .add(Restrictions.between("depositor.depositorDateReturnDeposit", startDate, endDate))
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("depositId"), "depositId")
                                .add(formProjection(properties)))
                )
                .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class))
                .list()) {
            deposits.add((BankDeposit)d);
        }
        //---- end session
        LOGGER.debug("deposits.size = {}", deposits.size());
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        return deposits;
    }
    //---- get deposit where Date Deposit between days with aggregation and grouping Depositors
    @Override
    public List<Map> getBankDepositsBetweenDateDepositWithDepositors(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsBetweenDateDepositWithDepositors({},{})",dateFormat.format(startDate),dateFormat.format(endDate));
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                            .createAlias("depositors", "depositor")
                            .add(Restrictions.between("depositor.depositorDateDeposit", startDate, endDate))
                            .setProjection(Projections.distinct(Projections.projectionList()
                                 .add(Projections.property("deposit.depositId"), "depositId")
                                 .add(formProjection(properties))
                                 .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                 .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                 .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                 .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                 .add(Projections.groupProperty("deposit.depositId"))
                            ))
                            .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                            .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
    }
    //---- get deposit by Name with aggregation and grouping Depositors
    @Override
    public List<Map> getBankDepositByNameWithDepositors(String name){
        LOGGER.debug("getBankDepositByNameWithDepositors({})",name);
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                .add(Restrictions.eq("deposit.depositName", name))
                .createAlias("depositors", "depositor")
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("deposit.depositId"), "depositId")
                                .add(formProjection(properties))
                                .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                .add(Projections.groupProperty("deposit.depositId"))
                ))
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
    }

    @Override
    public List<Map> getBankDepositByNameBetweenDateDepositWithDepositors(String name,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByNameBetweenDateDepositWithDepositors({},{})",dateFormat.format(startDate),dateFormat.format(endDate));
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                .add(Restrictions.eq("deposit.depositName",name))
                .createAlias("depositors", "depositor")
                .add(Restrictions.between("depositor.depositorDateDeposit", startDate, endDate))
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("deposit.depositId"), "depositId")
                                .add(formProjection(properties))
                                .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                .add(Projections.groupProperty("deposit.depositId"))
                ))
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
    }
    //---- get deposit where Date Return Deposit between days with aggregation and grouping Depositors
    @Override
    public List<Map> getBankDepositsBetweenDateReturnDepositWithDepositors(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsBetweenDateReturnDepositWithDepositors({},{})",dateFormat.format(startDate),dateFormat.format(endDate));
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                .createAlias("depositors", "depositor")
                .add(Restrictions.between("depositor.depositorDateReturnDeposit", startDate, endDate))
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("deposit.depositId"), "depositId")
                                .add(formProjection(properties))
                                .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                .add(Projections.groupProperty("deposit.depositId"))
                ))
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
    }
    //---- get deposit by Name where Date Deposit between days with aggregation and grouping Depositors
    @Override
    public List<Map> getBankDepositByNameBetweenDateReturnDepositWithDepositors(String name,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByNameBetweenDateReturnDepositWithDepositors({},{})",dateFormat.format(startDate),dateFormat.format(endDate));
        //---- connection
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //---- query
        String[] properties = session.getSessionFactory().getClassMetadata(BankDeposit.class).getPropertyNames();
        List list = session.createCriteria(BankDeposit.class, "deposit")
                .add(Restrictions.eq("deposit.depositName",name))
                .createAlias("depositors", "depositor")
                .add(Restrictions.between("depositor.depositorDateReturnDeposit", startDate, endDate))
                .setProjection(Projections.distinct(Projections.projectionList()
                                .add(Projections.property("deposit.depositId"), "depositId")
                                .add(formProjection(properties))
                                .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                .add(Projections.groupProperty("deposit.depositId"))
                ))
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        //---- end session
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        LOGGER.debug("list = {}", list);
        return mapRow(list);
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
    //---- List to List<Map>
    public List<Map> mapRow(List list) {
        List<Map> depositAgrDepositor = new ArrayList<Map>(list.size());
        for (Object aList : list) {
            Map map = (Map) aList;
            LOGGER.debug("map = {}", map);
            depositAgrDepositor.add(map);
        }
        return depositAgrDepositor;
    }
    //---- list properties for guery output
    public Projection formProjection(String[] properties) {
        ProjectionList list = Projections.projectionList();
        for (int i=0; i<properties.length-1; i++){
            list.add(Projections.property(properties[i]),properties[i]);
        }
        return list;
    }
}
