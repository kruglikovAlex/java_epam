package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.util.HibernateUtil;
import org.hibernate.Criteria;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.criterion.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import org.hibernate.transform.AliasToBeanResultTransformer;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.Assert;

@Component
public class BankDepositDaoImpl implements BankDepositDao {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";

    private BankDeposit deposit;
    private List<BankDeposit> deposits;

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
        Assert.notNull(id,ERROR_METHOD_PARAM);
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
        Assert.notNull(name,ERROR_METHOD_PARAM);
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
        Assert.notNull(currency,ERROR_METHOD_PARAM);
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
     * @return List<BankDeposit>
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsByInterestRateCriteria(Integer rate){
        LOGGER.debug("getBankDepositsByInterestRateCriteria({})", rate);
        Assert.notNull(rate,ERROR_METHOD_PARAM);
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
        Assert.notNull(fromTerm,ERROR_METHOD_PARAM);
        Assert.notNull(toTerm,ERROR_METHOD_PARAM);
        Assert.isTrue(fromTerm <= toTerm,ERROR_FROM_TO_PARAM);
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
     * @param startRate Integer
     * @param endRate Integer
     * @return List<BankDeposit>
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsFromToInterestRateCriteria(Integer startRate, Integer endRate){
        LOGGER.debug("getBankDepositsFromToInterestRateCriteria({}, {})",startRate,endRate);
        Assert.notNull(startRate,ERROR_METHOD_PARAM);
        Assert.notNull(endRate,ERROR_METHOD_PARAM);
        Assert.isTrue(startRate<=endRate,ERROR_FROM_TO_PARAM);
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
     * Get Bank Deposits from-to Date Deposit values
     * @param startDate Date
     * @param endDate Date
     * @return List<BankDeposit>
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsFromToDateDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDeposit({}, {})",dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            for(Object d: HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class)
                    .createAlias("depositors","depositor")
                    .add(Restrictions.between("depositor.depositorDateDeposit", startDate, endDate))
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("depositId"),"depositId")
                            .add(formProjection(properties)))
                    )
                    .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class))
                    .list()){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateDeposit({}, {}) - {}", dateFormat.format(startDate),
                    dateFormat.format(endDate), e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToDateDeposit() "+e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposits from-to Date Return Deposit values
     * @param startDate Date
     * @param endDate Date
     * @return List<BankDeposit>
     */
    @Override
    @Transactional
    public List<BankDeposit> getBankDepositsFromToDateReturnDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDeposit({}, {})",dateFormat.format(startDate),
                dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            for(Object d: HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class)
                    .createAlias("depositors","depositor")
                    .add(Restrictions.between("depositor.depositorDateReturnDeposit", startDate, endDate))
                    .setProjection(Projections.distinct(Projections.projectionList()
                                    .add(Projections.property("depositId"),"depositId")
                                    .add(formProjection(properties)))
                    )
                    .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class))
                    .list()){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateReturnDeposit({}, {}) - {}", dateFormat.format(startDate),
                    dateFormat.format(endDate), e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToDateReturnDeposit() "+e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposits by Name with depositors
     * @param name String
     * @return List<Map>
     */
    @Override
    @Transactional
    public Map getBankDepositByNameWithDepositors(String name){
        LOGGER.debug("getBankDepositByNameWithDepositors({})", name);
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Map list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            list = (Map)HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class,"deposit")
                    .add(Restrictions.eq("deposit.depositName",name))
                    .createAlias("depositors","depositor")
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"),"depositId")
                            .add(formProjection(properties))
                            .add(Projections.count("depositor.depositorId").as("depositorCount"))
                            .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                            .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                            .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                            .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).uniqueResult();
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
            LOGGER.debug("list - {}",list);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameWithDepositors({}) - {}", name, e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByNameWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Deposit values
     * @param name String
     * @param startDate Date
     * @param endDate Date
     * @return List<Map>
     */
    @Override
    @Transactional
    public Map getBankDepositByNameFromToDateDepositWithDepositors(String name,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositors({}, {}, {})",name,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        Map list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            list = (Map)HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class,"deposit")
                    .add(Restrictions.eq("deposit.depositName",name))
                    .createAlias("depositors","depositor")
                    .add(Restrictions.between("depositor.depositorDateDeposit",startDate,endDate))
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
                    .uniqueResult();
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
            LOGGER.debug("list - {}",list);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameFromToDateDepositWithDepositors({},{},{}) - {}",
                    name, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByNameFromToDateDepositWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposits by NAME with depositors from-to Date Return Deposit values
     * @param name String
     * @param startDate Date
     * @param endDate Date
     * @return Map
     */
    @Override
    @Transactional
    public Map getBankDepositByNameFromToDateReturnDepositWithDepositors(String name,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositors({}, {}, {})",name,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        Map list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            list = (Map)HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class,"deposit")
                    .add(Restrictions.eq("deposit.depositName",name))
                    .createAlias("depositors","depositor")
                    .add(Restrictions.between("depositor.depositorDateReturnDeposit",startDate,endDate))
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
                    .uniqueResult();
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
            LOGGER.debug("list - {}",list);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameFromToDateReturnDepositWithDepositors({},{},{}) - {}",
                    name, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByNameFromToDateReturnDepositWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposits by ID with depositors
     * @param id Long
     * @return Map
     */
    @Override
    @Transactional
    public Map getBankDepositByIdWithDepositors(Long id){
        LOGGER.debug("getBankDepositByIdWithDepositors({})", id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Map list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = (Map)HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class, "deposit")
                    .add(Restrictions.eq("deposit.depositId", id))
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
                    .uniqueResult();
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdWithDepositors({}) - {}",id,e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByIdWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposits by ID with depositors from-to Date Deposit values
     * @param id Long
     * @param startDate Date
     * @param endDate Date
     * @return Map
     */
    @Override
    @Transactional
    public Map getBankDepositByIdFromToDateDepositWithDepositors(Long id,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositors({}, {}, {})",id,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        Map list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class).getPropertyNames();
            list = (Map)HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class, "deposit")
                    .add(Restrictions.eq("deposit.depositId",id))
                    .createAlias("depositors","depositor")
                    .add(Restrictions.between("depositor.depositorDateDeposit",startDate,endDate))
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"),"depositId")
                            .add(formProjection(properties))
                            .add(Projections.count("depositor.depositorId").as("depositorCount"))
                            .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                            .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                            .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                            .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .uniqueResult();
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdFromToDateDepositWithDepositors({},{},{}) - {}",
                    id, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByIdFromToDateDepositWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposits by ID with depositors from-to Date Return Deposit values
     * @param id Long
     * @param startDate Date
     * @param endDate Date
     * @return Map
     */
    @Override
    @Transactional
    public Map getBankDepositByIdFromToDateReturnDepositWithDepositors(Long id,Date startDate, Date endDate){
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositors({}, {}, {})",id,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        Map list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class).getPropertyNames();
            list = (Map)HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class,"deposit")
                    .add(Restrictions.eq("deposit.depositId",id))
                    .createAlias("depositors","depositor")
                    .add(Restrictions.between("depositor.depositorDateReturnDeposit",startDate,endDate))
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"),"depositId")
                            .add(formProjection(properties))
                            .add(Projections.count("depositor.depositorId").as("depositorCount"))
                            .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                            .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                            .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                            .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .uniqueResult();
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdFromToDateReturnDepositWithDepositors({},{},{}) - {}",
                    id, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByIdFromToDateReturnDepositWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposit with depositors
     *
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsWithDepositors(){
        LOGGER.debug("getBankDepositsWithDepositors()");
        List list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class, "deposit")
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
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsWithDepositors() - {}",e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsWithDepositors() "+e.toString());
        }
        return mapRow(list);
    }

    /**
     * Get Bank Deposit from-to Date Deposit with Bank Depositors
     * @param startDate Date
     * @param endDate Date
     * @return List<Map>
     */
    @Override
    @Transactional
    public List<Map> getBankDepositsFromToDateDepositWithDepositors(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDepositWithDepositors({}, {})",
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        List list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class).getPropertyNames();
            list = HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class, "deposit")
                    .createAlias("depositors", "depositor")
                    .add(Restrictions.between("depositor.depositorDateDeposit",startDate,endDate))
                    .setProjection(Projections.distinct(Projections.projectionList()
                                    .add(Projections.property("deposit.depositId"),"depositId")
                                    .add(formProjection(properties))
                                    .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                    .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                    .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                    .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                    .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .list();
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateDepositWithDepositors({},{}) - {}",
                    dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToDateDepositWithDepositors() "+e.toString());
        }
        return mapRow(list);
    }

    /**
     * Get Bank Deposit from-to Date Return Deposit with depositors
     * @param startDate Date
     * @param endDate Date
     * @return List<Map>
     */
    @Override
    @Transactional
    public List<Map> getBankDepositsFromToDateReturnDepositWithDepositors(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDepositWithDepositors({}, {})",
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        List list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class).getPropertyNames();
            list = HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class, "deposit")
                    .createAlias("depositors", "depositor")
                    .add(Restrictions.between("depositor.depositorDateReturnDeposit",startDate,endDate))
                    .setProjection(Projections.distinct(Projections.projectionList()
                                    .add(Projections.property("deposit.depositId"),"depositId")
                                    .add(formProjection(properties))
                                    .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                    .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                    .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                    .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                    .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .list();
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateReturnDepositWithDepositors({},{}) - {}",
                    dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToDateReturnDepositWithDepositors() "+e.toString());
        }
        return mapRow(list);
    }

    @Override
    @Transactional
    public List<Map> getBankDepositsByCurrencyWithDepositors(String currency){
        LOGGER.debug("getBankDepositByCurrencyWithDepositors({})", currency);
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        List list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class, "deposit")
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
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyWithDepositors({}) - {}",currency,e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByCurrencyWithDepositors() "+e.toString());
        }
        return mapRow(list);
    }

    /**
     * Get Bank Deposit from-to Date Deposit by Currency with depositors
     *
     * @param currency String
     * @param startDate Date
     * @param endDate Date
     * @return List<Map>
     */
    @Override
    @Transactional
    public List<Map> getBankDepositsByCurrencyFromToDateDepositWithDepositors(String currency,
                                                                             Date startDate,
                                                                             Date endDate){
        LOGGER.debug("getBankDepositByCurrencyFromToDateDepositWithDepositors({}, {}, {})", currency,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        List list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class, "deposit")
                    .add(Restrictions.eq("deposit.depositCurrency", currency))
                    .createAlias("depositors", "depositor")
                    .add(Restrictions.between("depositor.depositorDateDeposit",startDate,endDate))
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
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyFromToDateDepositWithDepositors({}) - {}",currency,e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByCurrencyFromToDateDepositWithDepositors() "+e.toString());
        }
        return mapRow(list);
    }

    /**
     * Get Bank Deposit from-to Date return Deposit by Currency with depositors
     *
     * @param currency String
     * @param startDate Date
     * @param endDate Date
     * @return List<Map>
     */
    @Override
    @Transactional
    public List<Map> getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(String currency,
                                                                                   Date startDate,
                                                                                   Date endDate){
        LOGGER.debug("getBankDepositByCurrencyFromToDateReturnDepositWithDepositors({}, {}, {})", currency,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        List list;
        try{
            //--- open session
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = HibernateUtil.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class, "deposit")
                    .add(Restrictions.eq("deposit.depositCurrency", currency))
                    .createAlias("depositors", "depositor")
                    .add(Restrictions.between("depositor.depositorDateReturnDeposit",startDate,endDate))
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
            //--- close session
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyFromToDateReturnDepositWithDepositors({}) - {}",currency,e.toString());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByCurrencyFromToDateReturnDepositWithDepositors() "+e.toString());
        }
        return mapRow(list);
    }

    /**
     * Add Bank Deposit
     * @param deposit BankDeposit
     */
    @Override
    @Transactional
    public void addBankDeposit(BankDeposit deposit) {
        LOGGER.debug("addBankDeposit({})",deposit);
        Assert.notNull(deposit,ERROR_METHOD_PARAM);
        Assert.isNull(deposit.getDepositId(),ERROR_NULL_PARAM);
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
     * @param deposit BankDeposit - Bank Deposit to be stored in the database
     */
    @Override
    @Transactional
    public void updateBankDeposit(BankDeposit deposit){
        LOGGER.debug("updateBankDeposit({})",deposit);
        Assert.notNull(deposit,ERROR_METHOD_PARAM);
        Assert.notNull(deposit.getDepositId(),ERROR_NULL_PARAM);
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
     * @param id Long - id of the Bank Deposit to be removed
     */
    @Override
    @Transactional
    public void deleteBankDeposit(Long id){
        LOGGER.debug("deleteBankDeposit({})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
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

    /**
     * List to List<Map>
     * @param list List
     * @return List<Map>
     */
    public List<Map> mapRow(List list){
        List<Map> depositAgrDepositor = new ArrayList<Map>(list.size());
        for(Object aList: list){
            Map map = (Map)aList;
            LOGGER.debug("map - {}", map);
            depositAgrDepositor.add(map);
        }
        return depositAgrDepositor;
    }

    /**
     * List properties for query output
     * @param properties String[]
     * @return ProjectionList
     */
    public Projection formProjection(String[] properties) {
        ProjectionList list = Projections.projectionList();
        for (int i=0; i<properties.length-1; i++){
            list.add(Projections.property(properties[i]),properties[i]);
        }
        return list;
    }
}
