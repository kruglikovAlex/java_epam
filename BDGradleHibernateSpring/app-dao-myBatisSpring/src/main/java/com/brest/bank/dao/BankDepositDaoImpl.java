package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class BankDepositDaoImpl implements BankDepositDao{
    private static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    private static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    private static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";

    private static final Logger LOGGER = LogManager.getLogger(BankDepositDaoImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private BankDeposit deposit = new BankDeposit();
    private List<BankDeposit> deposits = new ArrayList<BankDeposit>();
    private BankDeposit paramDeposit;
    private Map list;
    private Map<String,Object> param;
    private List<Map> mapList;

    @Autowired
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;

    @PostConstruct
    public void init(){
        try{
            sqlSessionFactory = sqlSessionFactoryBean.getObject();
        }catch (Exception e){
            LOGGER.error("error - init() for BankDepositDaoImpl.class - {}", e.toString());
            throw new IllegalArgumentException("error - init() for BankDepositDaoImpl.class"+e.toString());
        }
    }

    /**
     * Get all Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    @Override
    public List<BankDeposit> getBankDepositsCriteria(){
        LOGGER.debug("getBankDepositsCriteria()");
        try{
            sqlSession = sqlSessionFactory.openSession();

            deposits = sqlSession.selectList("BankDeposit.getAll",null);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsCriteria() - {}", e.toString());
            throw new IllegalArgumentException("error - getBankDepositsCriteria()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("deposits-> {}", deposits);
        return deposits;
    }

    /**
     * Get Bank Deposit by ID
     *
     * @param id Long - id of the Bank Deposit to return
     * @return BankDeposit with the specified id from the database
     */
    @Override
    public BankDeposit getBankDepositByIdCriteria(Long id){
        LOGGER.debug("getBankDepositByIdCriteria({})", id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        try{
            sqlSession = sqlSessionFactory.openSession();

            paramDeposit = new BankDeposit();
            paramDeposit.setDepositId(id);

            deposit = sqlSession.selectOne("BankDeposit.getById",paramDeposit);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdCriteria({}) - {}", id, e.toString());
            throw new IllegalArgumentException("error - getBankDepositByIdCriteria()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("deposit-> {}", deposit);
        return deposit;
    }

    /**
     * Get Bank Deposit by NAME
     *
     * @param depositName String - name of the Bank Deposit to return
     * @return BankDeposit with the specified depositName from the database
     */
    @Override
    public BankDeposit getBankDepositByNameCriteria(String depositName){
        LOGGER.debug("getBankDepositByNameCriteria({})",depositName);
        Assert.notNull(depositName,ERROR_METHOD_PARAM);
        try{
            sqlSession = sqlSessionFactory.openSession();

            paramDeposit = new BankDeposit();
            paramDeposit.setDepositName(depositName);

            deposit = sqlSession.selectOne("BankDeposit.getByName",paramDeposit);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameCriteria({}) - {}",depositName,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByNameCriteria()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("deposit-> {}",deposit);
        return deposit;
    }

    /**
     * Get Bank Deposit by Currency
     *
     * @param currency String - currency of the Bank Deposits to return
     * @return List<BankDeposit> - a list containing all of the Bank Deposits with the specified
     * currency in the database
     */
    @Override
    public List<BankDeposit> getBankDepositsByCurrencyCriteria(String currency){
        LOGGER.debug("getBankDepositByCurrencyCriteria({})",currency);
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        try{
            sqlSession = sqlSessionFactory.openSession();

            paramDeposit = new BankDeposit();
            paramDeposit.setDepositCurrency(currency);

            deposits = sqlSession.selectList("BankDeposit.getByCurrency",paramDeposit);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByCurrencyCriteria({}) - {}",currency,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsByCurrencyCriteria()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("deposits-> {}",deposits);
        return deposits;
    }

    /**
     * Get Bank Deposits by INTEREST RATE
     *
     * @param rate Integer - interest rate of the Bank Deposits to return
     * @return List<BankDeposit>  - a list containing all of the Bank Deposits with the specified
     * interest rate in the database
     */
    @Override
    public List<BankDeposit> getBankDepositsByInterestRateCriteria(Integer rate){
        LOGGER.debug("getBankDepositByInterestRateCriteria({})",rate);
        Assert.notNull(rate,ERROR_METHOD_PARAM);
        Assert.isInstanceOf(Integer.class,rate);
        try{
            sqlSession = sqlSessionFactory.openSession();

            paramDeposit = new BankDeposit();
            paramDeposit.setDepositInterestRate(rate);

            deposits = sqlSession.selectList("BankDeposit.getByInterestRate",paramDeposit);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByInterestRatecrite({}) - {}",rate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByIntrerestRateCriteria()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("deposits-> {}",deposits);
        return deposits;
    }

    /**
     * Get Bank Deposits from-to MIN TERM values
     *
     * @param fromTerm Integer - start value of the min term (count month)
     * @param toTerm Integer - end value of the min term (count month)
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     * with the specified task`s min term of deposit
     */
    @Override
    public List<BankDeposit> getBankDepositsFromToMinTermCriteria(Integer fromTerm,
                                                                  Integer toTerm){
        LOGGER.debug("getBankDepositFromToMinTermCriteria(from:{}, to:{})",fromTerm,toTerm);
        Assert.notNull(fromTerm,ERROR_METHOD_PARAM);
        Assert.notNull(toTerm,ERROR_METHOD_PARAM);
        Assert.isTrue(fromTerm<=toTerm,ERROR_FROM_TO_PARAM);
        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String, Object>();
            param.put("fromTerm",fromTerm);
            param.put("toTerm",toTerm);

            deposits = sqlSession.selectList("BankDeposit.getFromToMinTerm",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositFromToMinTermCriteria(from:{}, to:{}) - {}",fromTerm,toTerm,e.toString());
            throw new IllegalArgumentException("error - getBankDepositFromToMinTermCriteria()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("deposits-> {}",deposits);
        return deposits;
    }

    /**
     * Get Bank Deposits from-to INTEREST RATE values
     *
     * @param startRate Integer - start value of the interest rate (0% < startRate <= 100%)
     * @param endRate Integer - end value of the interest rate (0% < endRate <= 100%)
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     * with the specified task`s in of deterest rate of deposit
     */
    @Override
    public List<BankDeposit> getBankDepositsFromToInterestRateCriteria(Integer startRate,
                                                                       Integer endRate){
        LOGGER.debug("getBankDepositsFromToInterestRateCriteria(from:{}, to:{})",startRate,endRate);
        Assert.notNull(startRate, ERROR_METHOD_PARAM);
        Assert.notNull(endRate, ERROR_METHOD_PARAM);
        Assert.isTrue(startRate<=endRate,ERROR_FROM_TO_PARAM);
        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String, Object>();
            param.put("startRate",startRate);
            param.put("endRate",endRate);

            deposits = sqlSession.selectList("BankDeposit.getFromToInterestRate",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToInterestRateCriteria(from:{}, to:{}) - {}",startRate,endRate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsFromToInterestRateCriteria() - {}"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("deposits-> {}",deposits);
        return deposits;
    }

    /**
     * Get Bank Deposits from-to Date Deposit values
     *
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (endDate > startDate)
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     * with the specified task`s date of deposit
     */
    @Override
    public List<BankDeposit> getBankDepositsFromToDateDeposit(Date startDate,
                                                              Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDeposit(from:{}, to:{})",dateFormat.format(startDate),
                dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String, Object>();
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            deposits = sqlSession.selectList("BankDeposit.getFromToDate",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateDeposit(from:{}, to:{}) - {}",dateFormat.format(startDate),
                    dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositsFromToDateDeposit() - {}"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("deposits-> {}",deposits);
        return deposits;
    }

    /**
     * Get Bank Deposits from-to Date Return Deposit values
     *
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (endDate > startDate)
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     * with the specified task`s date return of deposit
     */
    @Override
    public List<BankDeposit> getBankDepositsFromToDateReturnDeposit(Date startDate,
                                                                    Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDeposit(from:{}, to:{})",dateFormat.format(startDate),
                dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String, Object>();
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            deposits = sqlSession.selectList("BankDeposit.getFromToDateReturn",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateReturnDeposit(from:{}, to:{}) - {}",dateFormat.format(startDate),
                    dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositsFromToDateReturnDeposit() - {}"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("deposits-> {}",deposits);
        return deposits;
    }

    /**
     * Get Bank Deposit by NAME with depositors
     *
     * @param name String - name of the Bank Deposit to return
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    @Override
    public Map getBankDepositByNameWithDepositors(String name){
        LOGGER.debug("getBankDepositByNameWithDepositors({})",name);
        Assert.notNull(name,ERROR_METHOD_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            paramDeposit = new BankDeposit();
            paramDeposit.setDepositName(name);

            list = sqlSession.selectOne("BankDeposit.getByNameWithDepositors",paramDeposit);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameWithDepositors({}) - {}", name, e.toString());
            throw new IllegalArgumentException("error - getBankDepositByNameWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("list-> {}",list);
        return list;
    }

    /**
     * Get Bank Deposit by NAME with depositors from-to Date Deposit values
     *
     * @param name String - name of the Bank Deposit to return
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (endDate > startDate)
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date of deposit
     */
    @Override
    public Map getBankDepositByNameFromToDateDepositWithDepositors(String name,
                                                                   Date startDate,
                                                                   Date endDate){
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositors(name: {}, start: {}, end: {})",
                name,dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String, Object>();
            param.put("depositName",name);
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            list = sqlSession.selectOne("BankDeposit.getByNameFromToDateDepositWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameFromToDateDepositWithDepositors(name: {}, start: {}, end: {}) - {}",
                    name, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositByNameFromToDateDepositWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("list-> {}",list);
        return list;
    }

    /**
     * Get Bank Deposit by NAME with depositor from-to Date Return Deposit values
     *
     * @param name String - name of the Bank Deposit to return
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (endDate > startDate)
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date return of deposit
     */
    @Override
    public Map getBankDepositByNameFromToDateReturnDepositWithDepositors(String name,
                                                                         Date startDate,
                                                                         Date endDate){
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositors(name: {}, start: {}, end: {})",
                name,dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String,Object>();
            param.put("depositName",name);
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            list = sqlSession.selectOne("BankDeposit.getByNameFromToDateReturnDepositWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameFromToDateReturnDepositWithDepositors(name: {}, start: {}, end: {}) - {}",
                    name, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositByNameFromToDateReturnDepositWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("list-> {}",list);
        return list;
    }

    /**
     * Get Bank Deposit by ID with depositors
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    @Override
    public Map getBankDepositByIdWithDepositors(Long id){
        LOGGER.debug("getBankDepositByIdWithDepositors({})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String, Object>();
            param.put("depositId",id);

            list = sqlSession.selectOne("BankDeposit.getByIdWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdWithDepositors(id: {}) - {}",id,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByIdWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("list-> {}",list);
        return list;
    }

    /**
     * Get Bank Deposits by ID with depositors from-to Date Deposit values
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (endDate > startDate)
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date of deposit
     */
    @Override
    public Map getBankDepositByIdFromToDateDepositWithDepositors(Long id,
                                                                 Date startDate,
                                                                 Date endDate){
        LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositors(id:{},from:{},to:{})",id,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String, Object>();
            param.put("depositId",id);
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            list = sqlSession.selectOne("BankDeposit.getByIdFromToDateDepositWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdFromToDateDepositWithDepositors(id:{},from:{},to:{}) - {}",
                    id,dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositByIdFromToDateDepositWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("list-> {}",list);
        return list;
    }

    /**
     * Get Bank Deposit by ID with depositors from-to Date Return Deposit values
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (startDate < endDate)
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors with the specified task`s date return deposit
     */
    @Override
    public Map getBankDepositByIdFromToDateReturnDepositWithDepositors(Long id,
                                                                       Date startDate,
                                                                       Date endDate){
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositors(id:{},from:{},to:{})",id,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String,Object>();
            param.put("depositId",id);
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            list = sqlSession.selectOne("BankDeposit.getByIdFromToDateReturnDepositWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdFromToDateReturnDepositWithDepositors(id:{},from:{},to:{}) - {}",
                    id,dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositByIdFromToDateReturnDepositWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("list-> {}",list);
        return list;
    }

    /**
     * Get Bank Deposits by Min term with depositors
     *
     * @param term Integer - Min term of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    @Override
    public List<Map> getBankDepositsByTermWithDepositors(Integer term){
        LOGGER.debug("getBankDepositsByTermWithDepositors({})",term);
        Assert.notNull(term,ERROR_METHOD_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            paramDeposit = new BankDeposit();
            paramDeposit.setDepositMinTerm(term);

            mapList = sqlSession.selectList("BankDeposit.getByTermWithDepositors",paramDeposit);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByTermWithDepositors(term:{}) - {}",term,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsByTermWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("maplist-> {}",mapList);
        return mapList;
    }

    /**
     * Get Bank Deposits by Min Amount with depositors
     *
     * @param amount Integer - Min amount of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    @Override
    public List<Map> getBankDepositsByAmountWithDepositors(Integer amount){
        LOGGER.debug("getBankDepositByAmountWithDepositors(amount: {})",amount);
        Assert.notNull(amount,ERROR_METHOD_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            paramDeposit = new BankDeposit();
            paramDeposit.setDepositMinAmount(amount);

            mapList = sqlSession.selectList("BankDeposit.getByAmountWithDepositors",paramDeposit);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByAmountWithDepositors(amount:{}) - {}",amount,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsByAmountWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("mapList-> {}",mapList);
        return mapList;
    }

    /**
     * Get Bank Deposits by Interest Rate with depositors
     *
     * @param rate Integer - Interest Rate of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    @Override
    public List<Map> getBankDepositsByRateWithDepositors(Integer rate){
        LOGGER.debug("getBankDepositsByRateWithDepositors(rate: {})",rate);
        Assert.notNull(rate,ERROR_METHOD_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            paramDeposit = new BankDeposit();
            paramDeposit.setDepositInterestRate(rate);

            mapList = sqlSession.selectList("BankDeposit.getByRateWithDepositors",paramDeposit);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByRateWithDepositors(rate:{}) - {}",rate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsByRateWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("mapList-> {}",mapList);
        return mapList;
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
        LOGGER.debug("getBankDepositByDepositorIdWithDepositors(id:{})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            BankDepositor param = new BankDepositor();
            param.setDepositorId(id);

            list = sqlSession.selectOne("BankDeposit.getByDepositorIdWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByDepositorIdWithDepositors(id:{}) - {}",id,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByDepositorIdWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("list-> {}",list);
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
        LOGGER.debug("getBankDepositByDepositorNameWithDepositors(name:{})",name);
        Assert.notNull(name,ERROR_METHOD_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            BankDepositor param = new BankDepositor();
            param.setDepositorName(name);

            list = sqlSession.selectOne("BankDeposit.getByDepositorNameWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByDepositorNameWithDepositors(name:{}) - {}",name,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByDepositorNameWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("list-> {}",list);
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
        LOGGER.debug("getBankDepositsByDepositorAmountWithDepositors(from:{}, to:{})",from,to);
        Assert.notNull(from,ERROR_METHOD_PARAM);
        Assert.notNull(to,ERROR_METHOD_PARAM);
        Assert.isTrue(from<=to,ERROR_FROM_TO_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String,Object>();
            param.put("from",from);
            param.put("to",to);

            mapList = sqlSession.selectList("BankDeposit.getByDepositorAmountWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByDepositorAmountWithDepositors(from:{}, to:{}) - {}",from,to,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsByDepositorAmountWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("mapList-> {}",mapList);
        return mapList;
    }

    /**
     * Get Bank Deposit with depositors
     *
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    @Override
    public List<Map> getBankDepositsWithDepositors(){
        LOGGER.debug("getBankDepositsWithDepositors()");

        try{
            sqlSession = sqlSessionFactory.openSession();

            mapList = sqlSession.selectList("BankDeposit.getAllWithDepositors");

            sqlSession.commit();
        }catch(Exception e){
            LOGGER.error("error - getBankDepositsWithDepositors() - {}",e.toString());
            throw new IllegalArgumentException("error - getBankDepositsWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("mapList-> {}",mapList);
        return mapList;
    }

    /**
     * Get Bank Deposit from-to Date Deposit with depositors
     *
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (startDate < endDate)
     * @return List<Map> a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date deposit
     */
    @Override
    public List<Map> getBankDepositsFromToDateDepositWithDepositors(Date startDate,
                                                                    Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDpositWithDepositors(start:{}. end:{}",startDate,endDate);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String,Object>();
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            mapList = sqlSession.selectList("BankDeposit.getByFromToDateDepositWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateDepositWithDepositors({}, {}) - {}",startDate,endDate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsFromToDateDepositWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("mapList-> {}",mapList);
        return mapList;
    }

    /**
     * Get Bank Deposit from-to Date Return Deposit with depositors
     *
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (startDate < endDate)
     * @return List<Map> a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date return deposit
     */
    @Override
    public List<Map> getBankDepositsFromToDateReturnDepositWithDepositors(Date startDate,
                                                                          Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDpositWithDepositors(start:{}. end:{}",startDate,endDate);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String,Object>();
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            mapList = sqlSession.selectList("BankDeposit.getByFromToDateReturnDepositWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateReturnDepositWithDepositors({}, {}) - {}",
                    startDate,endDate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsFromToDateReturnDepositWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("mapList-> {}",mapList);
        return mapList;
    }

    /**
     * Get Bank Deposit by Currency with depositors
     *
     * @param currency String - Currency of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    @Override
    public List<Map> getBankDepositsByCurrencyWithDepositors(String currency){
        LOGGER.debug("getBankDepositByCurrencyWithDepositors(currency:{})",currency);
        Assert.notNull(currency,ERROR_METHOD_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            paramDeposit = new BankDeposit();
            paramDeposit.setDepositCurrency(currency);

            mapList = sqlSession.selectList("BankDeposit.getByCurrencyWithDepositors",paramDeposit);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyWithDepositors(currency:{}) - {}",currency,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByCurrencyWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("mapList-> {}",mapList);
        return mapList;
    }

    /**
     * Get Bank Deposit from-to Date Deposit by Currency with depositors
     *
     * @param currency String - Currency of the Bank Deposit to return
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (startDate < endDate)
     * @return List<Map> a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date deposit
     */
    @Override
    public List<Map> getBankDepositsByCurrencyFromToDateDepositWithDepositors(String currency,
                                                                              Date startDate,
                                                                              Date endDate){
        LOGGER.debug("getBankDepositByCurrencyFromToDateDepositWithDepositors(currency:{}," +
                "startDate:{}, endDate:{})",currency, dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String,Object>();
            param.put("currency",currency);
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            mapList = sqlSession.selectList("BankDeposit.getByCurrencyFromToDateDepositWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyFromToDateDepositWithDepositors(currency:{},startDate:{}, endDate:{}) - {}",
                    currency,startDate,endDate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByCurrencyFromToDateDepositWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("mapList-> {}",mapList);
        return mapList;
    }

    /**
     * Get Bank Deposit from-to Date Return Deposit by Currency with depositors
     *
     * @param currency String - Currency of the Bank Deposit to return
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (startDate < endDate)
     * @return List<Map> a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date return deposit
     */
    @Override
    public List<Map> getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(String currency,
                                                                                    Date startDate,
                                                                                    Date endDate){
        LOGGER.debug("getBankDepositByCurrencyFromToDateReturnDepositWithDepositors(currency:{}," +
                "startDate:{}, endDate:{})",currency, dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String,Object>();
            param.put("currency",currency);
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            mapList = sqlSession.selectList("BankDeposit.getByCurrencyFromToDateReturnDepositWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyFromToDateReturnDepositWithDepositors(currency:{}," +
                    "startDate:{}, endDate:{}) - {}",currency,startDate,endDate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByCurrencyFromToDateReturnDepositWithDepositors() "
                    +e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("mapList-> {}",mapList);
        return mapList;
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
        LOGGER.debug("getBankDepositByDepositorMarkReturnWithDepositors(mark:{})",markReturn);
        Assert.notNull(markReturn,ERROR_METHOD_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            BankDepositor param = new BankDepositor();
            param.setDepositorMarkReturnDeposit(markReturn);

            mapList = sqlSession.selectList("BankDeposit.getByDepositorMarkReturnWithDepositors",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByDepositorMarkReturnWithDepositors(mark:{}) - {}",markReturn,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByDepositorMarkReturnWithDepositors() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("mapList-> {}",mapList);
        return mapList;
    }

    /**
     * Get Bank Deposits by Variant Args with depositors
     *
     * @param args Object - Variant number of arguments
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    @Override
    public List<Map> getBankDepositsByVarArgs(Object... args){
        LOGGER.debug("getBankDepositsByVarArgs(args: {})",args);
        Assert.notNull(args,ERROR_METHOD_PARAM);

        for(int i=0; i<args.length; i=i+2){
            for(int j=i+2; j<args.length; j=j+2){
                if(args[i].toString().equals(args[j].toString())){
                    args[i] = args[i].toString()+"Le";
                    args[j] = args[j].toString()+"Ge";
                    LOGGER.debug("le-{}",args[i]);
                    LOGGER.debug("ge-{}",args[j]);
                }
            }
        }

        param = new HashMap<String,Object>();
        for(int i=0; i<args.length;i=i+2){
            if(args[i].toString().contains("deposit.")){
                param.put(args[i].toString().replace("deposit.",""),args[i+1]);
            } else {
                param.put(args[i].toString().replace("depositor.",""),args[i+1]);
            }
        }

        LOGGER.debug("param: {}",param);
        try{
            sqlSession = sqlSessionFactory.openSession();

            mapList = sqlSession.selectList("BankDeposit.getByVarArgs",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByVarArgs(args: {}) - {}",args,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsByVarArgs() "+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("mapList-> {}",mapList);
        return mapList;
    }

    /**
     * Adding Bank Deposit
     *
     * @param deposit BankDeposit - Bank Deposit to be inserted to the database
     */
    @Override
    @Transactional
    public void addBankDeposit(BankDeposit deposit){
        LOGGER.debug("addBankDeposit({})",deposit);
        Assert.notNull(deposit,ERROR_METHOD_PARAM);
        Assert.isNull(deposit.getDepositId(),ERROR_NULL_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            sqlSession.insert("BankDeposit.insert",deposit);

            sqlSession.commit();
        }catch (Exception e){
            sqlSession.rollback();
        }finally {
            sqlSession.close();
        }
    }

    /**
     * Updating Bank Deposit
     *
     * @param deposit BankDeposit - Bank Deposit to be stored in the database
     */
    @Override
    @Transactional
    public void updateBankDeposit(BankDeposit deposit){
        LOGGER.debug("updateBankDeposit({})",deposit.toString());
        Assert.notNull(deposit,ERROR_METHOD_PARAM);
        Assert.notNull(deposit.getDepositId(),ERROR_NULL_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            sqlSession.update("BankDeposit.update",deposit);

            sqlSession.commit();
        }catch (Exception e){
            sqlSession.rollback();
        }finally {
            sqlSession.close();
        }
    }

    /**
     * Deleting Bank Deposit by ID
     *
     * @param depositId Long - id of the Bank Deposit to be removed
     */
    @Override
    @Transactional
    public void deleteBankDeposit(Long depositId){
        LOGGER.debug("deleteBankDeposit({})",depositId);
        Assert.notNull(depositId,ERROR_METHOD_PARAM);

        try {
            sqlSession = sqlSessionFactory.openSession();

            param = new HashMap<String,Object>();
            param.put("depositId", depositId);

            sqlSession.delete("BankDeposit.delete", param);

            sqlSession.commit();
        }catch (Exception e){
            sqlSession.rollback();
        }finally {
            sqlSession.close();
        }
    }

    @Override
    public Integer rowCount(){
        LOGGER.debug("rowCount()");
        Integer count;
        try{
            sqlSession = sqlSessionFactory.openSession();

            count = sqlSession.selectOne("BankDeposit.rowCount");

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - rowCount() - {}",e.toString());
            throw new IllegalArgumentException("error - rowCount():"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("count-> {}",count);
        return count;
    }
}


