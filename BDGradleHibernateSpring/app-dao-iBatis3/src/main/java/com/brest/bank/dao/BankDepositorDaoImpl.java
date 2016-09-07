package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.ibatis.common.resources.Resources;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
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
    private BankDepositor param;
    private BankDeposit paramDeposit;
    private Map<String,Object> paramMap;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private SqlSession sqlSession;

    /**
     * Get all Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    @Override
    public List<BankDepositor> getBankDepositorsCriteria(){
        LOGGER.debug("getBankDepositorsCriteria()");
        depositors = new ArrayList<BankDepositor>();

        try{
            sqlSession = sqlSessionFactory.openSession();

            depositors = (List<BankDepositor>)sqlSession.selectList("BankDepositor.getAll",null);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorsCriteria() - {}", e.toString());
            throw new IllegalArgumentException("error - getBankDepositorsCriteria()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("depositors-> {}",depositors);
        return depositors;
    }

    /**
     * Get Bank Depositor by ID
     *
     * @param depositorId  Long - id of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    @Override
    public BankDepositor getBankDepositorByIdCriteria(Long depositorId){
        LOGGER.debug("getBankDepositorsByIdCriteria(id={})",depositorId);
        Assert.notNull(depositorId,ERROR_METHOD_PARAM);
        depositor = new BankDepositor();

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new BankDepositor();
            param.setDepositorId(depositorId);

            depositor = (BankDepositor)sqlSession.selectOne("BankDepositor.getById",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorByIdCriteria(id = {}) - {}", depositorId,e.toString());
            throw new IllegalArgumentException("error - getBankDepositorByIdCriteria()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("depositor-> {}",depositor);
        return depositor;
    }

    /**
     * Get Bank Depositor by Name
     *
     * @param depositorName  String - name of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    @Override
    public BankDepositor getBankDepositorByNameCriteria(String depositorName){
        LOGGER.debug("getBankDepositorByNameCriteria(name: {})",depositorName);
        Assert.notNull(depositorName,ERROR_METHOD_PARAM);
        depositor = new BankDepositor();

        try{
            sqlSession = sqlSessionFactory.openSession();

            param = new BankDepositor();
            param.setDepositorName(depositorName);

            depositor = (BankDepositor)sqlSession.selectOne("BankDepositor.getByName",param);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorByNameCriteria(id = {}) - {}", depositorName,e.toString());
            throw new IllegalArgumentException("error - getBankDepositorByNameCriteria()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("depositor-> {}",depositor);
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
    public List<BankDepositor> getBankDepositorsFromToDateDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositorsFromToDateDeposit(start={}, end={}",dateFormat.format(start),dateFormat.format(end));
        Assert.notNull(start,ERROR_METHOD_PARAM);
        Assert.notNull(end,ERROR_METHOD_PARAM);
        Assert.isTrue(start.before(end),ERROR_FROM_TO_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            paramMap = new HashMap<String, Object>();
                paramMap.put("startDate",start);
                paramMap.put("endDate",end);

            depositors = (List<BankDepositor>)sqlSession.selectList("BankDepositor.getFromToDateDeposit",paramMap);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorsFromToDateDeposit({}, {}) - {}", dateFormat.format(start),
                    dateFormat.format(end),e.toString());
            throw new IllegalArgumentException("error - getBankDepositorsFromToDateDeposit()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("depositors-> {}",depositors);
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
    public List<BankDepositor> getBankDepositorsFromToDateReturnDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositorsFromToDateReturnDeposit(start={}, end={}",dateFormat.format(start),dateFormat.format(end));
        Assert.notNull(start,ERROR_METHOD_PARAM);
        Assert.notNull(end,ERROR_METHOD_PARAM);
        Assert.isTrue(start.before(end),ERROR_FROM_TO_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            paramMap = new HashMap<String, Object>();
                paramMap.put("startDate",start);
                paramMap.put("endDate",end);

            depositors = (List<BankDepositor>)sqlSession.selectList("BankDepositor.getFromToDateReturnDeposit",paramMap);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorsFromToDateReturnDeposit({}, {}) - {}", dateFormat.format(start),
                    dateFormat.format(end),e.toString());
            throw new IllegalArgumentException("error - getBankDepositorsFromToDateReturnDeposit()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("depositors-> {}",depositors);
        return depositors;
    }

    /**
     * Get Bank Depositors by ID Bank Deposit
     *
     * @param depositId  Long - id of the Bank Deposit
     * @return List<BankDepositor> with the specified id bank deposit from the database
     */
    @Override
    public List<BankDepositor> getBankDepositorByIdDepositCriteria(Long depositId){
        LOGGER.debug("getBankDepositorByIdDepositCriteria(id={})",depositId);
        Assert.notNull(depositId,ERROR_METHOD_PARAM);

        try{
            sqlSession = sqlSessionFactory.openSession();

            paramDeposit = new BankDeposit();
            paramDeposit.setDepositId(depositId);

            depositors = (List<BankDepositor>)sqlSession.selectList("BankDepositor.getByIdDeposit",paramDeposit);

            sqlSession.commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorByIdDepositCriteria(id={}) - {}", depositId, e.toString());
            throw new IllegalArgumentException("error - getBankDepositorByIdDepositCriteria()"+e.toString());
        }finally {
            sqlSession.close();
        }
        LOGGER.debug("depositors-> {}",depositors);
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
            sqlSession = sqlSessionFactory.openSession();

            paramMap = new HashMap<String, Object>();
                paramMap.put("depositorId",depositor.getDepositorId());
                paramMap.put("depositorName",depositor.getDepositorName());
                paramMap.put("depositorDateDeposit",depositor.getDepositorDateDeposit());
                paramMap.put("depositorAmountDeposit",depositor.getDepositorAmountDeposit());
                paramMap.put("depositorAmountPlusDeposit",depositor.getDepositorAmountPlusDeposit());
                paramMap.put("depositorAmountMinusDeposit",depositor.getDepositorAmountMinusDeposit());
                paramMap.put("depositorDateReturnDeposit",depositor.getDepositorDateReturnDeposit());
                paramMap.put("depositorMarkReturnDeposit",depositor.getDepositorMarkReturnDeposit());
                paramMap.put("depositId",depositId);

            sqlSession.insert("BankDepositor.insert",paramMap);

            sqlSession.commit();
        }catch (Exception e){
            sqlSession.rollback();
        }finally {
            sqlSession.close();
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
            sqlSession = sqlSessionFactory.openSession();

            sqlSession.update("BankDepositor.update",depositor);

            sqlSession.commit();
        }catch (Exception e){
            sqlSession.rollback();
        }finally {
            sqlSession.close();
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
            sqlSession = sqlSessionFactory.openSession();

            param = new BankDepositor();
            param.setDepositorId(id);

            sqlSession.delete("BankDepositor.delete",param);

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

            count = (Integer) sqlSession.selectOne("BankDepositor.rowCount");

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
