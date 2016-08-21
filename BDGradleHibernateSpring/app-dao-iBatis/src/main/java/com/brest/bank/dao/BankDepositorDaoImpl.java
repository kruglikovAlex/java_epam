package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class BankDepositorDaoImpl implements BankDepositorDao {

    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    public static final String ERROR_FROM_TO_PARAM = "The first parameter should be less than the second";

    private static final Logger LOGGER = LogManager.getLogger(BankDepositDaoImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private BankDepositor depositor;
    private List<BankDepositor> depositors;

    /**
     * Get all Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    @Override
    @Transactional
    public List<BankDepositor> getBankDepositorsCriteria(){
        LOGGER.debug("getBankDepositorsCriteria()");
        depositors = new ArrayList<BankDepositor>();

        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            depositors = smc.queryForList("BankDepositor.getAll",null);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorsCriteria() - {}", e.toString());
            throw new IllegalArgumentException("error - getBankDepositorsCriteria()"+e.toString());
        }
        return depositors;
    }

    /**
     * Get Bank Depositor by ID
     *
     * @param depositorId  Long - id of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    @Override
    @Transactional
    public BankDepositor getBankDepositorByIdCriteria(Long depositorId){
        LOGGER.debug("getBankDepositorsByIdCriteria(id={})",depositorId);
        Assert.notNull(depositorId,ERROR_METHOD_PARAM);
        depositor = new BankDepositor();

        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDepositor param = new BankDepositor();
            param.setDepositorId(depositorId);

            depositor = (BankDepositor)smc.queryForObject("BankDepositor.getById",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorByIdCriteria(id = {}) - {}", depositorId,e.toString());
            throw new IllegalArgumentException("error - getBankDepositorByIdCriteria()"+e.toString());
        }
        LOGGER.debug("depositor: {}",depositor);
        return depositor;
    }

    /**
     * Get Bank Depositor by Name
     *
     * @param depositorName  String - name of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    @Override
    @Transactional
    public BankDepositor getBankDepositorByNameCriteria(String depositorName){
        LOGGER.debug("getBankDepositorByNameCriteria(name: {})",depositorName);
        Assert.notNull(depositorName,ERROR_METHOD_PARAM);
        depositor = new BankDepositor();

        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDepositor param = new BankDepositor();
            param.setDepositorName(depositorName);

            depositor = (BankDepositor)smc.queryForObject("BankDepositor.getByName",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorByNameCriteria(id = {}) - {}", depositorName,e.toString());
            throw new IllegalArgumentException("error - getBankDepositorByNameCriteria()"+e.toString());
        }
        LOGGER.debug("depositor: {}",depositor);
        return depositor;
    }

    /**
     * Get all Bank Depositors from-to Date Deposit
     *
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (startDate < endDate)
     * @return List<BankDepositors> a list of all bank depositors with the specified task`s date deposit
     */
    @Override
    @Transactional
    public List<BankDepositor> getBankDepositorsFromToDateDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositorsFromToDateDeposit(start={}, end={}",dateFormat.format(start),dateFormat.format(end));
        Assert.notNull(start,ERROR_METHOD_PARAM);
        Assert.notNull(end,ERROR_METHOD_PARAM);
        Assert.isTrue(start.before(end),ERROR_FROM_TO_PARAM);

        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("startDate",start);
            param.put("endDate",end);

            depositors = smc.queryForList("BankDepositor.getFromToDateDeposit",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorsFromToDateDeposit({}, {}) - {}", dateFormat.format(start),
                    dateFormat.format(end),e.toString());
            throw new IllegalArgumentException("error - getBankDepositorsFromToDateDeposit()"+e.toString());
        }
        LOGGER.debug("depositors: {}",depositors);
        return depositors;
    }

    /**
     * Get all Bank Depositors from-to Date return Deposit
     *
     * @param start Date - start value of the date return deposit (startDate < endDate)
     * @param end Date - end value of the date return deposit (startDate < endDate)
     * @return List<BankDepositors> a list of all bank depositors with the specified task`s date return deposit
     */
    @Override
    @Transactional
    public List<BankDepositor> getBankDepositorsFromToDateReturnDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositorsFromToDateReturnDeposit(start={}, end={}",dateFormat.format(start),dateFormat.format(end));
        Assert.notNull(start,ERROR_METHOD_PARAM);
        Assert.notNull(end,ERROR_METHOD_PARAM);
        Assert.isTrue(start.before(end),ERROR_FROM_TO_PARAM);

        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            Map param = new HashMap();
            param.put("startDate",start);
            param.put("endDate",end);

            depositors = smc.queryForList("BankDepositor.getFromToDateReturnDeposit",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorsFromToDateReturnDeposit({}, {}) - {}", dateFormat.format(start),
                    dateFormat.format(end),e.toString());
            throw new IllegalArgumentException("error - getBankDepositorsFromToDateReturnDeposit()"+e.toString());
        }
        LOGGER.debug("depositors: {}",depositors);
        return depositors;
    }

    /**
     * Get Bank Depositors by ID Bank Deposit
     *
     * @param depositId  Long - id of the Bank Deposit
     * @return List<BankDepositor> with the specified id bank deposit from the database
     */
    @Override
    @Transactional
    public List<BankDepositor> getBankDepositorByIdDepositCriteria(Long depositId){
        LOGGER.debug("getBankDepositorByIdDepositCriteria(id={})",depositId);
        Assert.notNull(depositId,ERROR_METHOD_PARAM);

        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDeposit param = new BankDeposit();
            param.setDepositId(depositId);

            depositors = smc.queryForList("BankDepositor.getByIdDeposit",param);
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorByIdDepositCriteria(id={}) - {}", depositId, e.toString());
            throw new IllegalArgumentException("error - getBankDepositorByIdDepositCriteria()"+e.toString());
        }
        LOGGER.debug("depositors: {}",depositors);
        return depositors;
    }

    /**
     * Adding Bank Depositor
     *
     * @param depositId Long - id of the Bank Deposit
     * @param depositor BankDepositor - Bank Depositor to be inserted to the database
     */
    @Override
    @Transactional
    public void addBankDepositor(Long depositId, BankDepositor depositor){
        LOGGER.debug("addBankDepositor(id={}, depositor: {}",depositId,depositor);
        Assert.notNull(depositId,ERROR_METHOD_PARAM);
        Assert.notNull(depositor,ERROR_METHOD_PARAM);
        Assert.isNull(depositor.getDepositorId(),ERROR_NULL_PARAM);

        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDeposit paramDeposit = new BankDeposit();
            paramDeposit.setDepositId(depositId);

            BankDeposit theDeposit = (BankDeposit)smc.queryForObject("BankDepositor.getDepositById",paramDeposit);
            theDeposit.setToDepositors(depositor);

            smc.update("BankDeposit.update",theDeposit);

            depositor.setDeposit(theDeposit);

            smc.insert("BankDepositor.insert",depositor);
        }catch (Exception e){
            LOGGER.error("error - addBankDepositor({}, {}) - {}", depositId, depositor, e.toString());
            throw new IllegalArgumentException("error - addBankDepositor()"+e.toString());
        }
    }

    /**
     * Updating Bank Depositor
     *
     * @param depositor BankDepositor - Bank Depositor to be stored in the database
     */
    @Override
    @Transactional
    public void updateBankDepositor(BankDepositor depositor){
        LOGGER.debug("updateBankDepositor({})",depositor);
        Assert.notNull(depositor,ERROR_METHOD_PARAM);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            smc.update("BankDepositor.update",depositor);
        }catch (Exception e){
            LOGGER.error("error - updateBankDepositor({}) - {}", depositor, e.toString());
            throw new IllegalArgumentException("error - updateBankDepositor()"+e.toString());
        }
    }

    /**
     * Deleting Bank Depositor by ID
     *
     * @param id Long - id of the Bank Depositor to be removed
     */
    @Override
    @Transactional
    public void removeBankDepositor(Long id){
        LOGGER.debug("removeBankDepositor(id={})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        try{
            Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

            BankDepositor param = new BankDepositor();
            param.setDepositorId(id);

            smc.delete("BankDepositor.delete",param);
        }catch (Exception e){
            LOGGER.error("error - removeBankDepositor({}) - {}", depositor, e.toString());
            throw new IllegalArgumentException("error - removeBankDepositor()"+e.toString());
        }
    }

    @Override
    @Transactional
    public Integer rowCount(){
        LOGGER.debug("rowCount()");
        Integer count;
        try{
            Reader rd = com.ibatis.common.resources.Resources.getResourceAsReader("SqlMapConfig.xml");

            SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);
            count = (Integer) smc.queryForObject("BankDepositor.rowCount");
        }catch (Exception e){
            LOGGER.error("error - rowCount() - {}",e.toString());
            throw new IllegalArgumentException("error - rowCount():"+e.toString());
        }
        return count;
    }
}
