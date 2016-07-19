package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.mapping.statement.DefaultRowHandler;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.beans.Transient;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class BankDepositDaoImpl implements BankDepositDao{

    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";

    private static final Logger LOGGER = LogManager.getLogger(BankDepositDaoImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private BankDeposit deposit = new BankDeposit();
    private List<BankDeposit> deposits = new ArrayList<BankDeposit>();

    /**
     * Get all Bank Deposits
     *
     * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
     */
    public List<BankDeposit> getBankDepositsCriteria(){
        LOGGER.debug("getBankDepositsCriteria()");
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            deposits = (List<BankDeposit>) smc.queryForList("BankDeposit.getAll",null);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsCriteria() - {}", e.toString());
            throw new IllegalArgumentException("error - getBankDepositsCriteria()"+e.toString());
        }
        LOGGER.debug("deposits:{}", deposits);
        return deposits;
    }

    /**
     * Get Bank Deposit by ID
     *
     * @param id Long - id of the Bank Deposit to return
     * @return BankDeposit with the specified id from the database
     */
    @Override
    @Transactional
    public BankDeposit getBankDepositByIdCriteria(Long id){
        LOGGER.debug("getBankDepositByIdCriteria({})", id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDeposit paramDeposit = new BankDeposit();
            paramDeposit.setDepositId(id);

            deposit = (BankDeposit)smc.queryForObject("BankDeposit.getById",paramDeposit);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdCriteria({}) - {}", id, e.toString());
            throw new IllegalArgumentException("error - getBankDepositByIdCriteria()"+e.toString());
        }
        LOGGER.debug("deposit:{}", deposit);
        return deposit;
    }

    /**
     * Get Bank Deposit by NAME
     *
     * @param depositName String - name of the Bank Deposit to return
     * @return BankDeposit with the specified depositName from the database
     */
    public BankDeposit getBankDepositByNameCriteria(String depositName){
        LOGGER.debug("getBankDepositByNameCriteria({})",depositName);
        Assert.notNull(depositName,ERROR_METHOD_PARAM);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDeposit paramDeposit = new BankDeposit();
            paramDeposit.setDepositName(depositName);

            deposit = (BankDeposit)smc.queryForObject("BankDeposit.getByName",paramDeposit);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameCriteria({}) - {}",depositName,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByNameCriteria()"+e.toString());
        }
        LOGGER.debug("deposit:{}",deposit);
        return deposit;
    }

    /**
     * Get Bank Deposit by Currency
     *
     * @param currency String - currency of the Bank Deposits to return
     * @return List<BankDeposit> - a list containing all of the Bank Deposits with the specified
     * currency in the database
     */
    public List<BankDeposit> getBankDepositsByCurrencyCriteria(String currency){
        LOGGER.debug("getBankDepositByCurrencyCriteria({})",currency);
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDeposit paramDeposit = new BankDeposit();
            paramDeposit.setDepositCurrency(currency);

            deposits = (List<BankDeposit>)smc.queryForList("BankDeposit.getByCurrency",paramDeposit);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencycriteria({}) - {}",currency,e.toString());
            throw new IllegalArgumentException("error - getBankDepositbyCurrencyCriteria()"+e.toString());
        }
        LOGGER.debug("deposits:{}",deposits);
        return deposits;
    }

    /**
     * Get Bank Deposits by INTEREST RATE
     *
     * @param rate Integer - interest rate of the Bank Deposits to return
     * @return List<BankDeposit>  - a list containing all of the Bank Deposits with the specified
     * interest rate in the database
     */
    public List<BankDeposit> getBankDepositsByInterestRateCriteria(Integer rate){
        LOGGER.debug("getBankDepositByInterestRateCriteria({})",rate);
        Assert.notNull(rate,ERROR_METHOD_PARAM);
        Assert.isInstanceOf(Integer.class,rate);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDeposit paramDeposit = new BankDeposit();
            paramDeposit.setDepositInterestRate(rate);

            deposits = (List<BankDeposit>)smc.queryForList("BankDeposit.getByInterestRate",paramDeposit);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByInterestRatecrite({}) - {}",rate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByIntrerestRateCriteria()"+e.toString());
        }
        LOGGER.debug("deposits: {}",deposits);
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
    public List<BankDeposit> getBankDepositsFromToMinTermCriteria(Integer fromTerm,
                                                                  Integer toTerm){
        LOGGER.debug("getBankDepositFromToMinTermCriteria(from:{}, to:{})",fromTerm,toTerm);
        Assert.notNull(fromTerm,ERROR_METHOD_PARAM);
        Assert.notNull(toTerm,ERROR_METHOD_PARAM);
        Assert.isTrue(fromTerm<=toTerm,ERROR_FROM_TO_PARAM);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("fromTerm",fromTerm);
            param.put("toTerm",toTerm);

            deposits = (List<BankDeposit>)smc.queryForList("BankDeposit.getFromToMinTerm",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositFromToMinTermCriteria(from:{}, to:{}) - {}",fromTerm,toTerm,e.toString());
            throw new IllegalArgumentException("error - getBankDepositFromToMinTermCriteria()"+e.toString());
        }
        LOGGER.debug("deposits: {}",deposits);
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
    public List<BankDeposit> getBankDepositsFromToInterestRateCriteria(Integer startRate,
                                                                       Integer endRate){
        LOGGER.debug("getBankDepositsFromToInterestRateCriteria(from:{}, to:{})",startRate,endRate);
        Assert.notNull(startRate, ERROR_METHOD_PARAM);
        Assert.notNull(endRate, ERROR_METHOD_PARAM);
        Assert.isTrue(startRate<=endRate,ERROR_FROM_TO_PARAM);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("startRate",startRate);
            param.put("endRate",endRate);

            deposits = (List<BankDeposit>)smc.queryForList("BankDeposit.getFromToInterestRate",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToInterestRateCriteria(from:{}, to:{}) - {}",startRate,endRate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsFromToInterestRateCriteria() - {}"+e.toString());
        }
        LOGGER.debug("deposits: {}",deposits);
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
    public List<BankDeposit> getBankDepositsFromToDateDeposit(Date startDate,
                                                              Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDeposit(from:{}, to:{})",dateFormat.format(startDate),
                dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            deposits = (List<BankDeposit>)smc.queryForList("BankDeposit.getFromToDate",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateDeposit(from:{}, to:{}) - {}",dateFormat.format(startDate),
                    dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositsFromToDateDeposit() - {}"+e.toString());
        }
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
    public List<BankDeposit> getBankDepositsFromToDateReturnDeposit(Date startDate,
                                                                    Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDeposit(from:{}, to:{})",dateFormat.format(startDate),
                dateFormat.format(endDate));
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            deposits = (List<BankDeposit>)smc.queryForList("BankDeposit.getFromToDateReturn",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateReturnDeposit(from:{}, to:{}) - {}",dateFormat.format(startDate),
                    dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositsFromToDateReturnDeposit() - {}"+e.toString());
        }
        return deposits;
    }

    /**
     * Get Bank Deposit by NAME with depositors
     *
     * @param name String - name of the Bank Deposit to return
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    public Map getBankDepositByNameWithDepositors(String name){
        LOGGER.debug("getBankDepositByNameWithDepositors({})",name);
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Map list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDeposit param = new BankDeposit();
            param.setDepositName(name);

            list = (HashMap)smc.queryForObject("BankDeposit.getByNameWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameWithDepositors({}) - {}", name, e.toString());
            throw new IllegalArgumentException("error - getBankDepositByNameWithDepositors() "+e.toString());
        }
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
    public Map getBankDepositByNameFromToDateDepositWithDepositors(String name,
                                                                   Date startDate,
                                                                   Date endDate){
        LOGGER.debug("getBankDepositByNameFromToDateDepositWithDepositors(name: {}, start: {}, end: {})",
                name,dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);

        Map list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
                param.put("depositName",name);
                param.put("startDate",startDate);
                param.put("endDate",endDate);

            list = (Map)smc.queryForObject("BankDeposit.getByNameFromToDateDepositWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameFromToDateDepositWithDepositors(name: {}, start: {}, end: {}) - {}",
                    name, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositByNameFromToDateDepositWithDepositors() "+e.toString());
        }
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
    public Map getBankDepositByNameFromToDateReturnDepositWithDepositors(String name,
                                                                         Date startDate,
                                                                         Date endDate){
        LOGGER.debug("getBankDepositByNameFromToDateReturnDepositWithDepositors(name: {}, start: {}, end: {})",
                name,dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate),ERROR_FROM_TO_PARAM);

        Map list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
                param.put("depositName",name);
                param.put("startDate",startDate);
                param.put("endDate",endDate);

            list = (Map)smc.queryForObject("BankDeposit.getByNameFromToDateReturnDepositWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByNameFromToDateReturnDepositWithDepositors(name: {}, start: {}, end: {}) - {}",
                    name, dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositByNameFromToDateReturnDepositWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposit by ID with depositors
     *
     * @param id Long - depositId of the Bank Deposit to return
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    public Map getBankDepositByIdWithDepositors(Long id){
        LOGGER.debug("getBankDepositByIdWithDepositors({})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);

        Map list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("depositId",id);

            list = (Map)smc.queryForObject("BankDeposit.getByIdWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdWithDepositors(id: {}) - {}",id,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByIdWithDepositors() "+e.toString());
        }
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
    public Map getBankDepositByIdFromToDateDepositWithDepositors(Long id,
                                                                 Date startDate,
                                                                 Date endDate){
        LOGGER.debug("getBankDepositByIdFromToDateDepositWithDepositors(id:{},from:{},to:{})",id,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));

        Map list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
                param.put("depositId",id);
                param.put("startDate",startDate);
                param.put("endDate",endDate);

            list = (Map)smc.queryForObject("BankDeposit.getByIdFromToDateDepositWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdFromToDateDepositWithDepositors(id:{},from:{},to:{}) - {}",
                    id,dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositByIdFromToDateDepositWithDepositors() "+e.toString());
        }
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
    public Map getBankDepositByIdFromToDateReturnDepositWithDepositors(Long id,
                                                                       Date startDate,
                                                                       Date endDate){
        LOGGER.debug("getBankDepositByIdFromToDateReturnDepositWithDepositors(id:{},from:{},to:{})",id,
                dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));

        Map list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("depositId",id);
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            list = (Map)smc.queryForObject("BankDeposit.getByIdFromToDateReturnDepositWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByIdFromToDateReturnDepositWithDepositors(id:{},from:{},to:{}) - {}",
                    id,dateFormat.format(startDate),dateFormat.format(endDate),e.toString());
            throw new IllegalArgumentException("error - getBankDepositByIdFromToDateReturnDepositWithDepositors() "+e.toString());
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
        LOGGER.debug("getBankDepositsByTermWithDepositors({})",term);
        Assert.notNull(term,ERROR_METHOD_PARAM);
        List<Map> list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDeposit param = new BankDeposit();
            param.setDepositMinTerm(term);

            list = smc.queryForList("BankDeposit.getByTermWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByTermWithDepositors(term:{}) - {}",term,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsByTermWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposits by Min Amount with depositors
     *
     * @param amount Integer - Min amount of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByAmountWithDepositors(Integer amount){
        LOGGER.debug("getBankDepositByAmountWithDepositors(amount: {})",amount);
        Assert.notNull(amount,ERROR_METHOD_PARAM);
        List<Map> list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDeposit param = new BankDeposit();
            param.setDepositMinAmount(amount);

            list = smc.queryForList("BankDeposit.getByAmountWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByAmountWithDepositors(amount:{}) - {}",amount,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsByAmountWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposits by Interest Rate with depositors
     *
     * @param rate Integer - Interest Rate of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByRateWithDepositors(Integer rate){
        LOGGER.debug("getBankDepositsByRateWithDepositors(rate: {})",rate);
        Assert.notNull(rate,ERROR_METHOD_PARAM);
        List<Map> list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDeposit param = new BankDeposit();
            param.setDepositInterestRate(rate);

            list = smc.queryForList("BankDeposit.getByRateWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByRateWithDepositors(rate:{}) - {}",rate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsByRateWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposit by Depositor ID with depositors
     *
     * @param id Long - depositorId of the Bank Depositor
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    public Map getBankDepositByDepositorIdWithDepositors(Long id){
        LOGGER.debug("getBankDepositByDepositorIdWithDepositors(id:{})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        Map list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDepositor param = new BankDepositor();
            param.setDepositorId(id);

            list = (Map)smc.queryForObject("BankDeposit.getByDepositorIdWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByDepositorIdWithDepositors(id:{}) - {}",id,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByDepositorIdWithDepositors() "+e.toString());
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
    public Map getBankDepositByDepositorNameWithDepositors(String name){
        LOGGER.debug("getBankDepositByDepositorNameWithDepositors(name:{})",name);
        Assert.notNull(name,ERROR_METHOD_PARAM);
        Map list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDepositor param = new BankDepositor();
            param.setDepositorName(name);

            list = (Map)smc.queryForObject("BankDeposit.getByDepositorNameWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByDepositorNameWithDepositors(name:{}) - {}",name,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByDepositorNameWithDepositors() "+e.toString());
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
    public List<Map> getBankDepositsByDepositorAmountWithDepositors(Integer from, Integer to){
        LOGGER.debug("getBankDepositsByDepositorAmountWithDepositors(from:{}, to:{})",from,to);
        Assert.notNull(from,ERROR_METHOD_PARAM);
        Assert.notNull(to,ERROR_METHOD_PARAM);
        Assert.isTrue(from<=to,ERROR_FROM_TO_PARAM);
        List<Map> list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("from",from);
            param.put("to",to);

            list = smc.queryForList("BankDeposit.getByDepositorAmountWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsByDepositorAmountWithDepositors(from:{}, to:{}) - {}",from,to,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsByDepositorAmountWithDepositors() "+e.toString());
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
        List<Map> list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            list = smc.queryForList("BankDeposit.getAllWithDepositors");
        }catch(Exception e){
            LOGGER.error("error - getBankDepositsWithDepositors() - {}",e.toString());
            throw new IllegalArgumentException("error - getBankDepositsWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposit from-to Date Deposit with depositors
     *
     * @param startDate Date - start value of the date deposit (startDate < endDate)
     * @param endDate Date - end value of the date deposit (startDate < endDate)
     * @return List<Map> a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date deposit
     */
    public List<Map> getBankDepositsFromToDateDepositWithDepositors(Date startDate,
                                                                    Date endDate){
        LOGGER.debug("getBankDepositsFromToDateDpositWithDepositors(start:{}. end:{}",startDate,endDate);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));
        List<Map> list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            list = smc.queryForList("BankDeposit.getByFromToDateDepositWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateDepositWithDepositors({}, {}) - {}",startDate,endDate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsFromToDateDepositWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposit from-to Date Return Deposit with depositors
     *
     * @param startDate Date - start value of the date return deposit (startDate < endDate)
     * @param endDate Date - end value of the date return deposit (startDate < endDate)
     * @return List<Map> a list of all bank deposits with a report on all relevant
     * bank depositors with the specified task`s date return deposit
     */
    public List<Map> getBankDepositsFromToDateReturnDepositWithDepositors(Date startDate,
                                                                          Date endDate){
        LOGGER.debug("getBankDepositsFromToDateReturnDpositWithDepositors(start:{}. end:{}",startDate,endDate);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));
        List<Map> list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            list = smc.queryForList("BankDeposit.getByFromToDateReturnDepositWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositsFromToDateReturnDepositWithDepositors({}, {}) - {}",
                    startDate,endDate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositsFromToDateReturnDepositWithDepositors() "+e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposit by Currency with depositors
     *
     * @param currency String - Currency of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByCurrencyWithDepositors(String currency){
        LOGGER.debug("getBankDepositByCurrencyWithDepositors(currency:{})",currency);
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        List<Map> list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDeposit param = new BankDeposit();
            param.setDepositCurrency(currency);

            list = smc.queryForList("BankDeposit.getByCurrencyWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyWithDepositors(currency:{}) - {}",currency,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByCurrencyWithDepositors() "+e.toString());
        }
        return list;
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
    public List<Map> getBankDepositsByCurrencyFromToDateDepositWithDepositors(String currency,
                                                                              Date startDate,
                                                                              Date endDate){
        LOGGER.debug("getBankDepositByCurrencyFromToDateDepositWithDepositors(currency:{}," +
                "startDate:{}, endDate:{})",currency, dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));
        List<Map> list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("currency",currency);
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            list = smc.queryForList("BankDeposit.getByCurrencyFromToDateDepositWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyFromToDateDepositWithDepositors(currency:{},startDate:{}, endDate:{}) - {}",
                    currency,startDate,endDate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByCurrencyFromToDateDepositWithDepositors() "+e.toString());
        }
        return list;
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
    public List<Map> getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(String currency,
                                                                                    Date startDate,
                                                                                    Date endDate){
        LOGGER.debug("getBankDepositByCurrencyFromToDateReturnDepositWithDepositors(currency:{}," +
                "startDate:{}, endDate:{})",currency, dateFormat.format(startDate),dateFormat.format(endDate));
        Assert.notNull(currency,ERROR_METHOD_PARAM);
        Assert.notNull(startDate,ERROR_METHOD_PARAM);
        Assert.notNull(endDate,ERROR_METHOD_PARAM);
        Assert.isTrue(startDate.before(endDate));
        List<Map> list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("currency",currency);
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            list = smc.queryForList("BankDeposit.getByCurrencyFromToDateReturnDepositWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByCurrencyFromToDateReturnDepositWithDepositors(currency:{}," +
                    "startDate:{}, endDate:{}) - {}",currency,startDate,endDate,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByCurrencyFromToDateReturnDepositWithDepositors() "
                    +e.toString());
        }
        return list;
    }

    /**
     * Get Bank Deposits by Depositor mark return with depositors
     *
     * @param markReturn Integer - Mark Return of the Bank Depositor
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByDepositorMarkReturnWithDepositors(Integer markReturn){
        LOGGER.debug("getBankDepositByDepositorMarkReturnWithDepositors(mark:{})",markReturn);
        Assert.notNull(markReturn,ERROR_METHOD_PARAM);
        List<Map> list;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDepositor param = new BankDepositor();
            param.setDepositorMarkReturnDeposit(markReturn);

            list = smc.queryForList("BankDeposit.getByDepositorMarkReturnWithDepositors",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositByDepositorMarkReturnWithDepositors(mark:{}) - {}",markReturn,e.toString());
            throw new IllegalArgumentException("error - getBankDepositByDepositorMarkReturnWithDepositors() "+e.toString());
        }
        return list;
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
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");

            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);
            smc.insert("BankDeposit.insert",deposit);
        }catch (Exception e){
            LOGGER.error("error - addBankDeposit({}) - {}", deposit, e.toString());
            throw new IllegalArgumentException("error - addBankDeposit()"+e.toString());
        }
    }

    /**
     * Updating Bank Deposit
     *
     * @param deposit BankDeposit - Bank Deposit to be stored in the database
     */
    public void updateBankDeposit(BankDeposit deposit){
        LOGGER.debug("updateBankDeposit({})",deposit.toString());
        Assert.notNull(deposit,ERROR_METHOD_PARAM);
        Assert.notNull(deposit.getDepositId(),ERROR_NULL_PARAM);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            smc.update("BankDeposit.update",deposit);
        }catch (Exception e){
            LOGGER.error("error - updateBankDeposit({}) - {}", deposit, e.toString());
            throw new IllegalArgumentException("error - updateBankDeposit()"+e.toString());
        }
    }

    /**
     * Deleting Bank Deposit by ID
     *
     * @param depositId Long - id of the Bank Deposit to be removed
     */
    public void deleteBankDeposit(Long depositId){
        LOGGER.debug("deleteBankDeposit({})",depositId);
        Assert.notNull(depositId,ERROR_METHOD_PARAM);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("depositId",depositId);

            smc.delete("BankDeposit.delete",param);
        }catch (Exception e){
            LOGGER.error("error - deleteBankDeposit({}) - {}", depositId, e.toString());
            throw new IllegalArgumentException("error - deleteBankDeposit()"+e.toString());
        }
    }

    public Integer rowCount(){
        LOGGER.debug("rowCount()");
        Integer count = 0;
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");

            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);
            count = (Integer) smc.queryForObject("BankDeposit.rowCount");
        }catch (Exception e){
            LOGGER.error("error - rowCount() - {}",e.toString());
            throw new IllegalArgumentException("error - rowCount():"+e.toString());
        }
        return count;
    }
}
