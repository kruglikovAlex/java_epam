package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

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
