package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BankDepositorDaoImpl implements BankDepositorDao {

    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String ERROR_NULL_PARAM = "The parameter must be NULL";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private HibernateTemplate hibernateTemplate;
    private BankDepositor depositor;
    private List<BankDepositor> depositors = new ArrayList<BankDepositor>();

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    /**
     * Get all Bank Depositors
     *
     * @return List<BankDepositor> - a list containing all of the Bank Depositors in the database
     */
    @Override
    @Transactional(readOnly = true)
    public List<BankDepositor> getBankDepositorsCriteria() {
        LOGGER.debug("getBankDepositorsCriteria()");

        depositors = new ArrayList<BankDepositor>();
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            for(Object d: hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDepositor.class))){
                depositors.add((BankDepositor)d);
            }
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorsCriteria() - {}", e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositorsCriteria()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        LOGGER.debug("depositors:{}", depositors);
        return depositors;
    }

    /**
     * Get all Bank Depositors from-to Date Deposit
     *
     * @param start Date - start value of the date deposit (startDate < endDate)
     * @param end Date - end value of the date deposit (startDate < endDate)
     * @return List<BankDepositors> a list of all bank depositors with the specified task`s date deposit
     */
    @Override
    @Transactional(readOnly = true)
    public List<BankDepositor> getBankDepositorsFromToDateDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositorsFromToDateDeposit()");

        depositors = new ArrayList<BankDepositor>();
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            for(Object d: hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDepositor.class,"depositor")
                            .add(Restrictions.between("depositor.depositorDateDeposit", start, end)))){
                depositors.add((BankDepositor)d);
            }
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorsFromToDateDeposit({}, {}) - {}",
                    dateFormat.format(start), dateFormat.format(end), e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositorsFromToDateDeposit()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        LOGGER.debug("depositors:{}", depositors);
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
    @Transactional(readOnly = true)
    public List<BankDepositor> getBankDepositorsFromToDateReturnDeposit(Date start, Date end){
        LOGGER.debug("getBankDepositorsFromToDateReturnDeposit()");

        depositors = new ArrayList<BankDepositor>();
        try{
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            for(Object d: hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDepositor.class,"depositor")
                            .add(Restrictions.between("depositor.depositorDateReturnDeposit", start, end)))){
                depositors.add((BankDepositor)d);
            }
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorsFromToDateReturnDeposit({}, {}) - {}",
                    dateFormat.format(start), dateFormat.format(end), e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositorsFromToDateReturnDeposit()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        LOGGER.debug("depositors:{}", depositors);
        return depositors;
    }

    /**
     * Get Bank Depositor by ID
     *
     * @param id  Long - id of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    @Override
    @Transactional(readOnly = true)
    public BankDepositor getBankDepositorByIdCriteria(Long id){
        LOGGER.debug("getBankDepositorByIdCriteria({})", id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        try{
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDepositor.class)
                            .add(Restrictions.eq("depositorId", id)));
            if (listRes.size() > 0)
                depositor = (BankDepositor) listRes.get(0);
            else
                depositor = null;
            LOGGER.debug("list - {}",depositor);

            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorByIdCriteria({}) - {}", id, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositorByIdCriteria()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        LOGGER.debug("depositor:{}", depositor);
        return depositor;
    }

    /**
     * Get Bank Depositors by ID Bank Deposit
     *
     * @param id  Long - id of the Bank Deposit
     * @return List<BankDepositor> with the specified id bank deposit from the database
     */
    @Override
    @Transactional(readOnly = true)
    public List<BankDepositor> getBankDepositorByIdDepositCriteria(Long id){
        LOGGER.debug("getBankDepositorByIdDepositCriteria({})", id);
        Assert.notNull(id,ERROR_METHOD_PARAM);
        try{
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDeposit.class)
                            .add(Restrictions.eq("depositId", id)));

            BankDeposit deposit;
            if (listRes.size() > 0) {
                deposit = (BankDeposit) listRes.get(0);
                depositors = new ArrayList<BankDepositor>(deposit.getDepositors());
            }
            else
                depositors = null;
            LOGGER.debug("depositors - {}",depositors);

            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorByIdDepositCriteria({}) - {}", id, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositorByIdDepositCriteria()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
        LOGGER.debug("depositors:{}", depositors);
        return depositors;
    }

    /**
     * Get Bank Depositor by Name
     *
     * @param name  String - name of the Bank Depositor to return
     * @return BankDepositor with the specified id from the database
     */
    @Override
    @Transactional(readOnly = true)
    public BankDepositor getBankDepositorByNameCriteria(String name){
        LOGGER.debug("getBankDepositorByNameCriteria({})",name);
        Assert.notNull(name,ERROR_METHOD_PARAM);
        try{
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- query
            List listRes = hibernateTemplate
                    .findByCriteria(DetachedCriteria.forClass(BankDepositor.class)
                            .add(Restrictions.eq("depositorName", name)));
            if (listRes.size() > 0)
                depositor = (BankDepositor) listRes.get(0);
            else
                depositor = null;
            LOGGER.debug("depositor - {}",depositor);
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        }catch (Exception e){
            LOGGER.error("error - getBankDepositorByNameCriteria({}) - {}", name, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - getBankDepositorByNameCriteria()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }

        LOGGER.debug("depositor:{}", depositor);
        return depositor;
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
        LOGGER.debug("addBankDepositor({},{})",depositId, depositor);
        Assert.notNull(depositId,ERROR_METHOD_PARAM);
        Assert.notNull(depositor,ERROR_METHOD_PARAM);
        Assert.isNull(depositor.getDepositorId(), ERROR_NULL_PARAM);

        try {
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();

            hibernateTemplate.save(depositor);

            List listRes = hibernateTemplate
                    .find("select p from BankDeposit p left join fetch " +
                            "p.depositors where p.depositId = ?",depositId);

            BankDeposit theDeposit;
            if (listRes.size() > 0){
                theDeposit = (BankDeposit) listRes.get(0);
                theDeposit.getDepositors().add(depositor);
            }

            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception e){
            LOGGER.error("error - addBankDepositor({}) - {}", depositor, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - addBankDepositor()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
        Assert.notNull(depositor.getDepositorId(), ERROR_NULL_PARAM);

        try {
            //--- open session
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();
            //--- update
            hibernateTemplate.update(depositor);
            //--- close session
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();

        } catch (Exception e){
            LOGGER.error("error - updateBankDepositor({}) - {}", depositor, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - updateBankDepositor()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
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
        LOGGER.debug("removeBankDepositor({})",id);
        Assert.notNull(id,ERROR_METHOD_PARAM);

        try {
            hibernateTemplate.getSessionFactory().getCurrentSession().beginTransaction();

            BankDepositor depositor = hibernateTemplate.get(BankDepositor.class,id);
            hibernateTemplate.delete(depositor);

            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();

        } catch (Exception e){
            LOGGER.error("error - removeBankDepositor({}) - {}", depositor, e.toString());
            hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new IllegalArgumentException("error - removeBankDepositor()"+e.toString());
        }finally {
            hibernateTemplate.getSessionFactory().getCurrentSession().close();
        }
    }
}
