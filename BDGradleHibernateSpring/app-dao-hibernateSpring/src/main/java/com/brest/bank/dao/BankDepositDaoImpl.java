package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.hibernate.transform.AliasToBeanResultTransformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class BankDepositDaoImpl implements BankDepositDao {

    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";
    public static final String ERROR_PARAM_VALUE = "The parameter must be '0' or '1'";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    HibernateTemplate hibernateTemplate;

    private BankDeposit deposit;
    private List<BankDeposit> deposits;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
    /**
     * Get all deposits with Criteria
     * @return List<BankDeposit>
     */
    @Override
    @Transactional(readOnly = true)
    public List<BankDeposit> getBankDepositsCriteria(){
        LOGGER.debug("getBankDepositsCriteria()");
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query;
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            for(Object d: hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                    ))
                    .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class)))){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsCriteria() - {}", e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsCriteria()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        LOGGER.debug("deposits:{}", deposits);
        return deposits;
    }

    /**
     * Get by depositId with Criteria
     * @param id Long
     * @return BankDeposit
     */
    @Override
    @Transactional(readOnly = true)
    public BankDeposit getBankDepositByIdCriteria(Long id){
        LOGGER.debug("getBankDepositByIdCriteria({})", id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .add(Restrictions.idEq(id))
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                    ))
                    .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class)));
            if (listRes.size() > 0)
                deposit = (BankDeposit) listRes.get(0);
            else
                deposit = null;
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdCriteria({}) - {}", id, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByIdCriteria()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        LOGGER.debug("deposit:{}", deposit);
        return deposit;
    }

    /**
     * get by depositName createCriteria
     * @param name String
     * @return BankDeposit
     */
    @Override
    @Transactional(readOnly = true)
    public BankDeposit getBankDepositByNameCriteria(String name){
        LOGGER.debug("getBankDepositByNameCriteria({})",name);
        Assert.notNull(name,ERROR_METHOD_PARAM);
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .add(Restrictions.eq("depositName",name))
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                    ))
                    .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class)));
            if (listRes.size() > 0)
                deposit = (BankDeposit)listRes.get(0);
            else
                deposit = null;

            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameCriteria({}) - {}", name, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByNameCriteria()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        LOGGER.debug("deposit:{}", deposit);
        return deposit;
    }

    /**
     * Get Bank Deposits by CURRENCY
     * @param currency String
     * @return List<BankDeposit>
     */
    @Override
    @Transactional(readOnly = true)
    public List<BankDeposit> getBankDepositsByCurrencyCriteria(String currency){
        LOGGER.debug("getBankDepositByCurrencyCriteria({})", currency);
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            for(Object d: hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .add(Restrictions.eq("depositCurrency",currency))
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                    ))
                    .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class)))){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();

            LOGGER.debug("deposits: {}",deposits);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByCurrencyCriteria({}) - {}", currency, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsByCurrencyCriteria() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return deposits;
    }

    /**
     * Get Bank Deposits by INTEREST RATE
     * @param rate Integer
     * @return List<BankDeposit>
     */
    @Override
    @Transactional(readOnly = true)
    public List<BankDeposit> getBankDepositsByInterestRateCriteria(Integer rate){
        LOGGER.debug("getBankDepositsByInterestRateCriteria({})", rate);
        Assert.notNull(rate,ERROR_METHOD_PARAM);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            for(Object d: hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .add(Restrictions.eq("depositInterestRate",rate))
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                    ))
                    .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class)))){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByInterestRateCriteria({}) - {}", rate, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsByInterestRateCriteria() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    @Transactional(readOnly = true)
    public List<BankDeposit> getBankDepositsFromToMinTermCriteria(Integer fromTerm, Integer toTerm){
        LOGGER.debug("getBankDepositsFromToMinTermCriteria({}, {})",fromTerm,toTerm);
        Assert.notNull(fromTerm,ERROR_METHOD_PARAM);
        Assert.notNull(toTerm,ERROR_METHOD_PARAM);
        Assert.isTrue(fromTerm <= toTerm,ERROR_FROM_TO_PARAM);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            for(Object d: hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .add(Restrictions.between("depositMinTerm",fromTerm,toTerm))
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                    ))
                    .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class)))){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToMinTermCriteria({}, {}) - {}", fromTerm, toTerm, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToMinTermCriteria() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    @Transactional(readOnly = true)
    public List<BankDeposit> getBankDepositsFromToInterestRateCriteria(Integer startRate, Integer endRate){
        LOGGER.debug("getBankDepositsFromToInterestRateCriteria({}, {})",startRate,endRate);
        Assert.notNull(startRate,ERROR_METHOD_PARAM);
        Assert.notNull(endRate,ERROR_METHOD_PARAM);
        Assert.isTrue(startRate<=endRate,ERROR_FROM_TO_PARAM);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            for(Object d: hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .add(Restrictions.between("depositInterestRate",startRate,endRate))
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                    ))
                    .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class)))){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToInterestRateCriteria({}, {}) - {}", startRate, endRate, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToInterestRateCriteria() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    @Transactional(readOnly = true)
    public List<BankDeposit> getBankDepositsFromToDateDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDeposit({}, {})",dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            for(Object d: hibernateTemplate.findByCriteria(DetachedCriteria.forClass(BankDeposit.class)
                    .createAlias("depositors","depositor")
                    .add(Restrictions.between("depositor.depositorDateDeposit", startDate, endDate))
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("depositId"),"depositId")
                            .add(formProjection(properties)))
                    )
                    .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class)))) {
                deposits.add((BankDeposit)d);
            }
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateDeposit({}, {}) - {}", dateFormat.format(startDate),
                    dateFormat.format(endDate), e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToDateDeposit() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    @Transactional(readOnly = true)
    public List<BankDeposit> getBankDepositsFromToDateReturnDeposit(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDeposit({}, {})",dateFormat.format(startDate),
                dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        deposits = new ArrayList<BankDeposit>();
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            for(Object d: hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class)
                    .createAlias("depositors","depositor")
                    .add(Restrictions.between("depositor.depositorDateReturnDeposit", startDate, endDate))
                    .setProjection(Projections.distinct(Projections.projectionList()
                                    .add(Projections.property("depositId"),"depositId")
                                    .add(formProjection(properties)))
                    )
                    .setResultTransformer(new AliasToBeanResultTransformer(BankDeposit.class)))){
                deposits.add((BankDeposit)d);
            }
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateReturnDeposit({}, {}) - {}", dateFormat.format(startDate),
                    dateFormat.format(endDate), e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToDateReturnDeposit() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return deposits;
    }

    /**
     * Get Bank Deposits by Name with depositors
     * @param name String
     * @return List<Map>
     */
    @Override
    @Transactional(readOnly = true)
    public Map getBankDepositByNameWithDepositors(String name){
        LOGGER.debug("getBankDepositByNameWithDepositors({})", name);
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Map list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .add(Restrictions.eq("deposit.depositName",name))
                            .createAlias("depositors","depositor", JoinType.LEFT_OUTER_JOIN)
                            .setProjection(Projections.distinct(Projections.projectionList()
                                    .add(Projections.property("deposit.depositId"),"depositId")
                                    .add(formProjection(properties))
                                    .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                    .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                    .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                    .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                    .add(Projections.groupProperty("deposit.depositId"))
                            ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            if (listRes.size() > 0)
                list = (Map)listRes.get(0);
            else
                list = null;
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
            LOGGER.debug("list - {}",list);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameWithDepositors({}) - {}", name, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByNameWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    @Transactional(readOnly = true)
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
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .add(Restrictions.eq("deposit.depositName",name))
                            .createAlias("depositors","depositor",JoinType.LEFT_OUTER_JOIN)
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
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            if (listRes.size() > 0)
                list = (Map)listRes.get(0);
            else
                list = null;
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
            LOGGER.debug("list - {}",list);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameFromToDateDepositWithDepositors({},{},{}) - {}",
                    name, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByNameFromToDateDepositWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    @Transactional(readOnly = true)
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
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .add(Restrictions.eq("deposit.depositName",name))
                            .createAlias("depositors","depositor",JoinType.LEFT_OUTER_JOIN)
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
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            if (listRes.size() > 0)
                list = (Map)listRes.get(0);
            else
                list = null;
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
            LOGGER.debug("list - {}",list);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameFromToDateReturnDepositWithDepositors({},{},{}) - {}",
                    name, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByNameFromToDateReturnDepositWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return list;
    }

    /**
     * Get Bank Deposits by ID with depositors
     * @param id Long
     * @return Map
     */
    @Override
    @Transactional(readOnly = true)
    public Map getBankDepositByIdWithDepositors(Long id){
        LOGGER.debug("getBankDepositByIdWithDepositors({})", id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Map list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .add(Restrictions.eq("deposit.depositId", id))
                            .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                            .setProjection(Projections.distinct(Projections.projectionList()
                                    .add(Projections.property("deposit.depositId"), "depositId")
                                    .add(formProjection(properties))
                                    .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                    .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                    .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                    .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                    .add(Projections.groupProperty("deposit.depositId"))
                            ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            if (listRes.size() > 0)
                list = (Map)listRes.get(0);
            else
                list = null;
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
            LOGGER.debug("list - {}",list);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdWithDepositors({}) - {}",id,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByIdWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    //=============================================
    @Override
    @Transactional(readOnly = true)
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
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class).getPropertyNames();
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .add(Restrictions.eq("deposit.depositId",id))
                            .createAlias("depositors","depositor",JoinType.LEFT_OUTER_JOIN)
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
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            if (listRes.size() > 0)
                list = (Map)listRes.get(0);
            else
                list = null;
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
            LOGGER.debug("list - {}",list);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdFromToDateDepositWithDepositors({},{},{}) - {}",
                    id, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByIdFromToDateDepositWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    @Transactional(readOnly = true)
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
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class).getPropertyNames();
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .add(Restrictions.eq("deposit.depositId",id))
                            .createAlias("depositors","depositor",JoinType.LEFT_OUTER_JOIN)
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
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            if (listRes.size() > 0)
                list = (Map)listRes.get(0);
            else
                list = null;
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
            LOGGER.debug("list - {}",list);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdFromToDateReturnDepositWithDepositors({},{},{}) - {}",
                    id, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByIdFromToDateReturnDepositWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return list;
    }

    /**
     * Get Bank Deposits by Min term with depositors
     *
     * @param term Integer - Min term of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByTermWithDepositors(Integer term){
        LOGGER.debug("getBankDepositByTermWithDepositors(term = {})", term);
        Assert.notNull(term,ERROR_METHOD_PARAM);
        List list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .add(Restrictions.eq("deposit.depositMinTerm", term))
                    .createAlias("depositors", "depositor", JoinType.LEFT_OUTER_JOIN)
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                            .add(Projections.count("depositor.depositorId").as("depositorCount"))
                            .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                            .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                            .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                            .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByTermWithDepositors(term={}) - {}",term,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByTermWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return mapRow(list);
    }

    /**
     * Get Bank Deposits by Min Amount with depositors
     *
     * @param amount Integer - Min amount of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByAmountWithDepositors(Integer amount){
        LOGGER.debug("getBankDepositsByAmountWithDepositors(amount = {})", amount);
        Assert.notNull(amount,ERROR_METHOD_PARAM);
        List list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .add(Restrictions.eq("deposit.depositMinAmount", amount))
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                            .add(Projections.count("depositor.depositorId").as("depositorCount"))
                            .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                            .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                            .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                            .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDeposistByAmountWithDepositors(amount={}) - {}",amount,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsByAmountWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return mapRow(list);
    }

    /**
     * Get Bank Deposits by Interest Rate with depositors
     *
     * @param rate Integer - Interest Rate of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByRateWithDepositors(Integer rate){
        LOGGER.debug("getBankDepositByRateWithDepositors(rate = {})", rate);
        Assert.notNull(rate,ERROR_METHOD_PARAM);
        List list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .add(Restrictions.eq("deposit.depositInterestRate", rate))
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                            .add(Projections.count("depositor.depositorId").as("depositorCount"))
                            .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                            .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                            .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                            .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByRateWithDepositors(rate={}) - {}",rate,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByRatetWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return mapRow(list);
    }

    /**
     * Get Bank Deposit by Depositor ID with depositors
     *
     * @param id Long - depositorId of the Bank Depositor
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    @Override
    public Map getBankDepositByDepositorIdWithDepositors(Long id){
        LOGGER.debug("getBankDepositByDepositorIdWithDepositors({})", id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Map list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .add(Restrictions.eq("depositor.depositorId", id))
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                            .add(Projections.count("depositor.depositorId").as("depositorCount"))
                            .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                            .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                            .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                            .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            if (listRes.size() > 0)
                list = (Map)listRes.get(0);
            else
                list = null;
            LOGGER.debug("list - {}",list);
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByDepositorIdWithDepositors({}) - {}",id,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByDepositorIdWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return list;
    }

    /**
     * Get Bank Deposit by Depositor Name with depositors
     *
     * @param name String - depositorName of the Bank Depositor
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    @Override
    public Map getBankDepositByDepositorNameWithDepositors(String name){
        LOGGER.debug("getBankDepositByDepositorNameWithDepositors({})", name);
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Map list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .add(Restrictions.eq("depositor.depositorName", name))
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                            .add(Projections.count("depositor.depositorId").as("depositorCount"))
                            .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                            .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                            .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                            .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            if (listRes.size() > 0)
                list = (Map)listRes.get(0);
            else
                list = null;
            LOGGER.debug("list - {}",list);
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByDepositorNameWithDepositors({}) - {}",name,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByDepositorNameWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return list;
    }

    /**
     * Get Bank Deposits by from-to Depositor Amount with depositors
     *
     * @param from Integer - Amount of the Bank Depositor
     * @param to Integer - Amount of the Bank Depositor
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    @Override
    public List<Map> getBankDepositsByDepositorAmountWithDepositors(Integer from, Integer to){
        LOGGER.debug("getBankDepositsByDepositorAmountWithDepositors(from = {}, to = {})", from, to);
        Assert.notNull(from,ERROR_METHOD_PARAM);
        Assert.notNull(to,ERROR_METHOD_PARAM);
        Assert.isTrue(from<=to,ERROR_FROM_TO_PARAM);
        List list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .add(Restrictions.between("depositor.depositorAmountDeposit", from,to))
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                            .add(Projections.count("depositor.depositorId").as("depositorCount"))
                            .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                            .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                            .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                            .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByDepositorAmountWithDepositors(from={}, to={}) - {}",from,to,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsByDepositorAmountWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return mapRow(list);
    }

    /**
     * Get Bank Deposits by Depositor mark return with depositors
     *
     * @param markReturn Integer - Mark Return of the Bank Depositor
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    @Override
    public List<Map> getBankDepositsByDepositorMarkReturnWithDepositors(Integer markReturn){
        LOGGER.debug("getBankDepositsByDepositorMarkReturnWithDepositors(markReturn = {})", markReturn);
        Assert.notNull(markReturn,ERROR_METHOD_PARAM);
        Assert.isTrue(markReturn==0||markReturn==1,ERROR_PARAM_VALUE);
        List list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = hibernateTemplate.findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                    .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                    .add(Restrictions.eq("depositor.depositorMarkReturnDeposit", markReturn))
                    .setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("deposit.depositId"), "depositId")
                            .add(formProjection(properties))
                            .add(Projections.count("depositor.depositorId").as("depositorCount"))
                            .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                            .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                            .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                            .add(Projections.groupProperty("deposit.depositId"))
                    ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByDepositorMarkReturnWithDepositors(markReturn={}) - {}",markReturn,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsByDepositorMarkReturnWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return mapRow(list);
    }

    /**
     * Get Bank Deposits by Variant Args with depositors
     *
     * @param args Object - Variant number of arguments
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByVarArgs(Object...args){
        LOGGER.debug("getBankDepositsByVarArgs(args: {})",args);
        Assert.notNull(args,ERROR_METHOD_PARAM);

        //== convert String.class to Date.class or Integer.class
        for(int i=0; i<args.length;i=i+2){
            try{
                args[i+1] = Integer.parseInt(args[i+1].toString());
            } catch (Exception intE){
                LOGGER.error("error - parseInt - {}, {}",args[i+1] ,intE.toString());
                try{
                    args[i+1] = dateFormat.parse(args[i+1].toString());
                } catch (ParseException dateE){
                    LOGGER.error("error - parseDate - {}, {}",args[i+1] ,dateE.toString());
                }
            }
        }

        List<Object[]> listLe = new ArrayList<Object[]>();
        List<Object[]> listGe = new ArrayList<Object[]>();

        for(int i=0; i<args.length; i=i+2){
            for(int j=i+2; j<args.length; j=j+2){
                if(args[i].toString().equals(args[j].toString())){
                    Object[] le = {args[i],args[i+1]};
                    Object[] ge = {args[j],args[j+1]};
                    listLe.add(le);
                    listGe.add(ge);
                    LOGGER.debug("le-{}",le);
                    LOGGER.debug("ge-{}",ge);
                }
            }
        }

        LOGGER.debug("listLe.size({})",listLe.size());
        LOGGER.debug("listGe.size({})",listGe.size());

        List list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- create query criteria
            Map restrict = new HashMap();
            for(int i=0; i<args.length; i=i+2){
                if(listLe.size()==0){
                    restrict.put(args[i].toString(),args[i+1]);
                }else{
                    int count = 0;
                    for(int j=0; j<listLe.size(); j++){
                        if(!args[i].toString().equals(listLe.get(j)[0].toString())){
                            count++;
                        }
                    }
                    if(count==listLe.size()){
                        restrict.put(args[i].toString(),args[i+1]);
                    }
                }
            }

            Criteria varArgs =  hibernateTemplate.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class, "deposit");

            varArgs.createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN);
            varArgs.add(Restrictions.allEq(restrict));

            if(listLe.size()!=0&&listGe.size()!=0){
                for(int i=0; i<listLe.size(); i++){
                    LOGGER.debug("listLe.get({})[0]-{}",i,listLe.get(i)[0].toString());
                    LOGGER.debug("listLe.get({})[1]-{}",i,listLe.get(i)[1].toString());
                    LOGGER.debug("listGe.get({})[0]-{}",i,listGe.get(i)[0].toString());
                    LOGGER.debug("listGe.get({})[1]-{}",i,listGe.get(i)[1].toString());
                    varArgs.add(Restrictions.between(listLe.get(i)[0].toString(),listLe.get(i)[1],listGe.get(i)[1]));
                }
            }

            varArgs.setProjection(Projections.distinct(Projections.projectionList()
                    .add(Projections.property("deposit.depositId"), "depositId")
                    .add(formProjection(properties))
                    .add(Projections.count("depositor.depositorId").as("depositorCount"))
                    .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                    .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                    .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                    .add(Projections.groupProperty("deposit.depositId"))
            ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            //--- query
            list = varArgs.list();
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByVarArgs({}) - {}",args ,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByVarArgs() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return mapRow(list);
    }

    /**
     * Get Bank Deposit with depositors
     *
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map> getBankDepositsWithDepositors(){
        LOGGER.debug("getBankDepositsWithDepositors()");
        List list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                            .setProjection(Projections.distinct(Projections.projectionList()
                                    .add(Projections.property("deposit.depositId"), "depositId")
                                    .add(formProjection(properties))
                                    .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                    .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                    .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                    .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                    .add(Projections.groupProperty("deposit.depositId"))
                            ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsWithDepositors() - {}",e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    @Transactional(readOnly = true)
    public List<Map> getBankDepositsFromToDateDepositWithDepositors(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDepositWithDepositors({}, {})",
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        List list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class).getPropertyNames();
            list = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
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
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateDepositWithDepositors({},{}) - {}",
                    dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToDateDepositWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    @Transactional(readOnly = true)
    public List<Map> getBankDepositsFromToDateReturnDepositWithDepositors(Date startDate, Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDepositWithDepositors({}, {})",
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        List list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class).getPropertyNames();
            list = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
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
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateReturnDepositWithDepositors({},{}) - {}",
                    dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositsFromToDateReturnDepositWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        return mapRow(list);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map> getBankDepositsByCurrencyWithDepositors(String currency){
        LOGGER.debug("getBankDepositByCurrencyWithDepositors({})", currency);
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        List list;
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .add(Restrictions.eq("deposit.depositCurrency", currency))
                            .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
                            .setProjection(Projections.distinct(Projections.projectionList()
                                    .add(Projections.property("deposit.depositId"), "depositId")
                                    .add(formProjection(properties))
                                    .add(Projections.count("depositor.depositorId").as("depositorCount"))
                                    .add(Projections.sum("depositor.depositorAmountDeposit").as("depositorAmountSum"))
                                    .add(Projections.sum("depositor.depositorAmountPlusDeposit").as("depositorAmountPlusSum"))
                                    .add(Projections.sum("depositor.depositorAmountMinusDeposit").as("depositorAmountMinusSum"))
                                    .add(Projections.groupProperty("deposit.depositId"))
                            ))
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyWithDepositors({}) - {}",currency,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByCurrencyWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    @Transactional(readOnly = true)
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
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .add(Restrictions.eq("deposit.depositCurrency", currency))
                            .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
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
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyFromToDateDepositWithDepositors({}) - {}",currency,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByCurrencyFromToDateDepositWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
    @Transactional(readOnly = true)
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
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            String[] properties = hibernateTemplate.getSessionFactory()
                    .getClassMetadata(BankDeposit.class)
                    .getPropertyNames();
            //--- query
            list = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class,"deposit")
                            .add(Restrictions.eq("deposit.depositCurrency", currency))
                            .createAlias("depositors", "depositor",JoinType.LEFT_OUTER_JOIN)
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
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyFromToDateReturnDepositWithDepositors({}) - {}",currency,e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositByCurrencyFromToDateReturnDepositWithDepositors() "+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- сохранение объекта
            hibernateTemplate.save(deposit);
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception e){
            LOGGER.error("error - addBankDeposit({}) - {}", deposit, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - addBankDeposit()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }

    }

/*
    @Transactional(readOnly=false)
    public void createEmployee(final String Name){
        System.out.println("Create new employee " + Name);
        Employee emp = hibernateTemplate.execute(new HibernateCallback() {

            public Employee doInHibernate(Session session) throws HibernateException {
                Employee emp = new Employee();
                emp.setName(Name);
                session.saveOrUpdate(emp);
                return emp;
            }
        });
        System.out.println("Employee created " + emp);
    }
     */

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
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- update
            hibernateTemplate.update(deposit);
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();

        } catch (Exception e){
            LOGGER.error("error - updateBankDeposit({}) - {}", deposit, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - updateBankDeposit()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- delete
            /*
            BankDeposit deposit = (BankDeposit) hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class)
                    .add(Restrictions.eq("depositId", id))).get(0);
*/
            BankDeposit deposit = hibernateTemplate.get(BankDeposit.class,id);

            hibernateTemplate.delete(deposit);
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();

        }catch(Exception e){
            LOGGER.error("error - deleteBankDeposit({}) - {}", deposit, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - deleteBankDeposit()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
