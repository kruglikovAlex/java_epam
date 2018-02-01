package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import com.sun.istack.internal.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.util.Assert;

@Component
public class BankDepositDaoImpl implements BankDepositDao{

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final String ERROR_METHOD_PARAM = "The parameter can not be NULL";
    public static final String DEPOSIT_ID = "depositId";
    public static final String DEPOSIT_NAME = "depositName";
    public static final String DEPOSIT_MIN_TERM = "depositMinTerm";
    public static final String DEPOSIT_MIN_AMOUNT = "depositMinAmount";
    public static final String DEPOSIT_CURRENCY = "depositCurrency";
    public static final String DEPOSIT_INT_RATE = "depositInterestRate";
    public static final String DEPOSIT_CONDITIONS = "depositAddConditions";
    public static final String DEPOSITOR_ID_DEPOSIT = "depId";
    public static final String DEPOSITOR_AMOUNT_DEPOSIT = "sumAmount";
    public static final String DEPOSITOR_AMOUNT_PLUS = "sumPlusAmount";
    public static final String DEPOSITOR_AMOUNT_MINUS = "sumMinusAmount";
    public static final String DEPOSITOR_NUMBER = "numDepositors";
    private static final String NAMEFILE = "../BDGradleHibernateSpring/app-dao-JDBC/src/main/resources/sql/select_bankDeposits_by_var_args.sql";

    private static final Logger LOGGER=LogManager.getLogger();
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${insert_into_bankDeposit_path}')).inputStream)}")
    public String addNewBankDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_all_bankDeposits_path}')).inputStream)}")
    public String selectAllBankDepositsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${delete_bankDeposit_path}')).inputStream)}")
    public String deleteDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${update_bankDeposit_path}')).inputStream)}")
    public String updateDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposit_by_id_path}')).inputStream)}")
    public String selectBankDepositByIdSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposit_by_name_path}')).inputStream)}")
    public String selectBankDepositByNameSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposit_by_currency_path}')).inputStream)}")
    public String selectBankDepositByCurrencySql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposit_by_interest_rate_path}')).inputStream)}")
    public String selectBankDepositByInterestRateSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_from_to_min_term_path}')).inputStream)}")
    public String selectBankDepositsFromToMinTermSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_from_to_interest_rate_path}')).inputStream)}")
    public String selectBankDepositsFromToInterestRateSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_from_to_date_deposit_path}')).inputStream)}")
    public String selectBankDepositsFromToDateDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_from_to_date_return_deposit_path}')).inputStream)}")
    public String selectBankDepositsFromToDateReturnDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_term_with_depositors_path}')).inputStream)}")
    public String selectBankDepositsByTermWithDepositorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_amount_with_depositors_path}')).inputStream)}")
    public String selectBankDepositsByAmountWithDepositorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_rate_with_depositors_path}')).inputStream)}")
    public String selectBankDepositsByRateWithDepositorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposit_by_depositorId_with_depositors_path}')).inputStream)}")
    public String selectBankDepositByDepositorIdWithDepositorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposit_by_depositorName_with_depositors_path}')).inputStream)}")
    public String selectBankDepositByDepositorNameWithDepositorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_depositorAmount_with_depositors_path}')).inputStream)}")
    public String selectBankDepositsByDepositorAmountWithDepositorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_currency_with_depositors_path}')).inputStream)}")
    public String selectBankDepositsByCurrencyWithDepositorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_currency_from_to_dateDeposit_with_depositors_path}')).inputStream)}")
    public String selectBankDepositsByCurrencyFromToDateDepositWithDepositorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_currency_from_to_dateReturnDeposit_with_depositors_path}')).inputStream)}")
    public String selectBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_depositorMarkReturn_with_depositors_path}')).inputStream)}")
    public String selectBankDepositsByDepositorMarkReturnWithDepositorsSql;

    public String selectBankDepositsByVarArgsSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_all_bankDeposits_with_all_bankDepositors_path}')).inputStream)}")
    public String selectAllBankDepositsWithAllBankDepositorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_all_bankDeposits_with_all_bankDeposittors_between_date_deposit_path}')).inputStream)}")
    public String selectAllBankDepositsWithAllBankDeposittorsBetweenDateDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_all_bankDeposits_with_all_bankDeposittors_between_date_return_deposit_path}')).inputStream)}")
    public String selectAllBankDepositsWithAllBankDeposittorsBetweenDateReturnDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_id_with_all_bankDeposittors_path}')).inputStream)}")
    public String selectBankDepositsByIdWithAllBankDeposittorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_id_with_all_bankDeposittors_between_date_deposit_path}')).inputStream)}")
    public String selectBankDepositsByIdWithAllBankDeposittorsBetweenDateDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_id_with_all_bankDeposittors_between_date_return_deposit_path}')).inputStream)}")
    public String selectBankDepositsByIdWithAllBankDeposittorsBetweenDateReturnDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_Name_with_all_bankDeposittors_path}')).inputStream)}")
    public String selectBankDepositsByNameWithAllBankDeposittorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_Name_with_all_bankDeposittors_between_date_deposit_path}')).inputStream)}")
    public String selectBankDepositsByNameWithAllBankDeposittorsBetweenDateDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDeposits_by_Name_with_all_bankDeposittors_between_date_return_deposit_path}')).inputStream)}")
    public String selectBankDepositsByNameWithAllBankDeposittorsBetweenDateReturnDepositSql;
       @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @PostConstruct
    public void init(){
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void addBankDeposit(BankDeposit bankDeposit){
        LOGGER.debug("addBankDeposit({})", bankDeposit);
        Assert.notNull(bankDeposit);
        Assert.isNull(bankDeposit.getDepositId());
        Assert.notNull(bankDeposit.getDepositName(), "Deposit name should be specified.");
        Assert.notNull(bankDeposit.getDepositMinTerm(), "Deposit MinTerm should be specified.");

        Map<String, Object> parameters = new HashMap(7);

        parameters.put(DEPOSIT_ID, bankDeposit.getDepositId());
        parameters.put(DEPOSIT_NAME, bankDeposit.getDepositName());
        parameters.put(DEPOSIT_MIN_TERM, bankDeposit.getDepositMinTerm());
        parameters.put(DEPOSIT_MIN_AMOUNT, bankDeposit.getDepositMinAmount());
        parameters.put(DEPOSIT_CURRENCY, bankDeposit.getDepositCurrency());
        parameters.put(DEPOSIT_INT_RATE, bankDeposit.getDepositInterestRate());
        parameters.put(DEPOSIT_CONDITIONS, bankDeposit.getDepositAddConditions());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(addNewBankDepositSql, new MapSqlParameterSource(parameters), keyHolder);
        //return keyHolder.getKey().longValue();
    }

    @Override
    public List<BankDeposit> getBankDepositsCriteria() {
        LOGGER.debug("get deposits()");
        return jdbcTemplate.query(selectAllBankDepositsSql, new BankDepositMapper());
    }

    @Override
    public void deleteBankDeposit(Long depositId) {
        LOGGER.debug("removeBankDepositor(depositorId={})",depositId);
        jdbcTemplate.update(deleteDepositSql, depositId);
    }

    @Override
    public BankDeposit getBankDepositByNameCriteria(String depositName){
        LOGGER.debug("getBankDepositByName(depositName={}) ", depositName);
        return jdbcTemplate.queryForObject(selectBankDepositByNameSql,new String[]{depositName.toLowerCase()}, new BankDepositMapper());
    }

    @Override
    public BankDeposit getBankDepositByIdCriteria(Long depositId) {
        LOGGER.debug("getBankDepositById(depositId={})", depositId);
        return jdbcTemplate.queryForObject(selectBankDepositByIdSql, new BankDepositMapper(),depositId);
    }
    @Override
    public void updateBankDeposit(BankDeposit bankDeposit){
        KeyHolder keyholder = new GeneratedKeyHolder();
        LOGGER.debug("updateBankDeposit({})", bankDeposit);

        Map<String, Object> parameters = new HashMap(7);
        parameters.put(DEPOSIT_ID, bankDeposit.getDepositId());
        parameters.put(DEPOSIT_NAME, bankDeposit.getDepositName());
        parameters.put(DEPOSIT_MIN_TERM, bankDeposit.getDepositMinTerm());
        parameters.put(DEPOSIT_MIN_AMOUNT, bankDeposit.getDepositMinAmount());
        parameters.put(DEPOSIT_CURRENCY, bankDeposit.getDepositCurrency());
        parameters.put(DEPOSIT_INT_RATE, bankDeposit.getDepositInterestRate());
        parameters.put(DEPOSIT_CONDITIONS, bankDeposit.getDepositAddConditions());

        namedJdbcTemplate.update(updateDepositSql,new MapSqlParameterSource(parameters),keyholder);
        LOGGER.debug("updateBankDeposit(): depositId{}",keyholder.getKey());
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
        LOGGER.debug("getBankDepositsByCurrencyCriteria(depositCurrency={})", currency);
        return jdbcTemplate.query(selectBankDepositByCurrencySql, new BankDepositMapper(),currency);
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
        LOGGER.debug("getBankDepositsByInterestRateCriteria(depositInterestRate={})", rate);
        return jdbcTemplate.query(selectBankDepositByInterestRateSql, new BankDepositMapper(),rate);
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
        LOGGER.debug("getBankDepositsFromToMinTermCriteria(from={}, to={})", fromTerm, toTerm);
        Assert.notNull(fromTerm,ERROR_METHOD_PARAM);
        Assert.notNull(toTerm,ERROR_METHOD_PARAM);
        return jdbcTemplate.query(selectBankDepositsFromToMinTermSql, new BankDepositMapper(),fromTerm, toTerm);
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
        Assert.notNull(startRate, ERROR_METHOD_PARAM);
        Assert.notNull(endRate, ERROR_METHOD_PARAM);
        LOGGER.debug("getBankDepositsFromToInterestRateCriteria(start={}, end={})", startRate,endRate);
        return jdbcTemplate.query(selectBankDepositsFromToInterestRateSql, new BankDepositMapper(),startRate,endRate);
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
        LOGGER.debug("getBankDepositsFromToDateDeposit(start={}, end={})", startDate, endDate);
        return jdbcTemplate.query(selectBankDepositsFromToDateDepositSql, new BankDepositMapper(),startDate, endDate);
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
        LOGGER.debug("getBankDepositsFromToDateReturnDeposit(start={}, end={})", startDate, endDate);
        return jdbcTemplate.query(selectBankDepositsFromToDateReturnDepositSql, new BankDepositMapper(),startDate, endDate);
    }

    @Override
    public List<Map> getBankDepositsWithDepositors() {
        LOGGER.debug("get deposits with all depositors()");
        return jdbcTemplate.query(selectAllBankDepositsWithAllBankDepositorsSql, new BankDepositDepositorMapper());
    }

    @Override
    public List<Map> getBankDepositsFromToDateDepositWithDepositors(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsAllDepositorsBetweenDateDeposit(Dates:{},{})", dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.query(selectAllBankDepositsWithAllBankDeposittorsBetweenDateDepositSql, new BankDepositDepositorMapper(),dateFormat.format(startDate),dateFormat.format(endDate));
    }

    @Override
    public List<Map> getBankDepositsFromToDateReturnDepositWithDepositors(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsAllDepositorsBetweenDateReturnDeposit(Dates:{},{})", dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.query(selectAllBankDepositsWithAllBankDeposittorsBetweenDateReturnDepositSql, new BankDepositDepositorMapper(),dateFormat.format(startDate),dateFormat.format(endDate));
    }

    @Override
    public Map getBankDepositByIdWithDepositors(Long depositId) {
        LOGGER.debug("getBankDepositByIdAllDepositors(depositId={})", depositId);
        return jdbcTemplate.queryForMap(selectBankDepositsByIdWithAllBankDeposittorsSql, depositId);
    }

    @Override
    public Map getBankDepositByIdFromToDateDepositWithDepositors(Long depositId, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsByIdWithAllDepositorsBetweenDateDeposit(depositId={}, Dates:{},{})", depositId, dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.queryForMap(selectBankDepositsByIdWithAllBankDeposittorsBetweenDateDepositSql, dateFormat.format(startDate),dateFormat.format(endDate),depositId);
    }

    @Override
    public Map getBankDepositByIdFromToDateReturnDepositWithDepositors(Long depositId, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsByIdWithAllDepositorsBetweenDateReturnDeposit(depositId={}, Dates:{},{})", depositId, dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.queryForMap(selectBankDepositsByIdWithAllBankDeposittorsBetweenDateReturnDepositSql, dateFormat.format(startDate),dateFormat.format(endDate),depositId);
    }

    @Override
    public Map getBankDepositByNameWithDepositors(String depositName) {
        LOGGER.debug("getBankDepositByNameAllDepositors(depositName={})", depositName);
        return jdbcTemplate.queryForMap(selectBankDepositsByNameWithAllBankDeposittorsSql, depositName);
    }

    @Override
    public Map getBankDepositByNameFromToDateDepositWithDepositors(String depositName, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsByNameWithAllDepositorsBetweenDateDeposit(depositId={}, Dates:{},{})", depositName, dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.queryForMap(selectBankDepositsByNameWithAllBankDeposittorsBetweenDateDepositSql, dateFormat.format(startDate),dateFormat.format(endDate),depositName);
    }

    @Override
    public Map getBankDepositByNameFromToDateReturnDepositWithDepositors(String depositName, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsByNameWithAllDepositorsBetweenDateReturnDeposit(depositId={}, Dates:{},{})", depositName, dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.queryForMap(selectBankDepositsByNameWithAllBankDeposittorsBetweenDateReturnDepositSql, dateFormat.format(startDate),dateFormat.format(endDate),depositName);
    }

    /**
     * Get Bank Deposits by Min term with depositors
     *
     * @param term Integer - Min term of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByTermWithDepositors(Integer term){
        LOGGER.debug("getBankDepositsByTermWithDepositors(term ={})", term);
        return jdbcTemplate.query(selectBankDepositsByTermWithDepositorsSql, new BankDepositDepositorMapper(),term);
    }

    /**
     * Get Bank Deposits by Min Amount with depositors
     *
     * @param amount Integer - Min amount of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByAmountWithDepositors(Integer amount){
        LOGGER.debug("getBankDepositsByAmountWithDepositors(amount ={})", amount);
        return jdbcTemplate.query(selectBankDepositsByAmountWithDepositorsSql, new BankDepositDepositorMapper(),amount);
    }

    /**
     * Get Bank Deposits by Interest Rate with depositors
     *
     * @param rate Integer - Interest Rate of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByRateWithDepositors(Integer rate){
        LOGGER.debug("getBankDepositsByRateWithDepositors(rate ={})", rate);
        return jdbcTemplate.query(selectBankDepositsByRateWithDepositorsSql, new BankDepositDepositorMapper(),rate);
    }

    /**
     * Get Bank Deposit by Depositor ID with depositors
     *
     * @param id Long - depositorId of the Bank Depositor
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    public Map getBankDepositByDepositorIdWithDepositors(Long id){
        LOGGER.debug("getBankDepositByDepositorIdWithDepositors(id ={})", id);
        return jdbcTemplate.queryForMap(selectBankDepositByDepositorIdWithDepositorsSql, id);
    }

    /**
     * Get Bank Deposit by Depositor Name with depositors
     *
     * @param name String - depositorName of the Bank Depositor
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    public Map getBankDepositByDepositorNameWithDepositors(String name){
        LOGGER.debug("getBankDepositByDepositorNameWithDepositors(name ={})", name);
        return jdbcTemplate.queryForMap(selectBankDepositByDepositorNameWithDepositorsSql, name);
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
        LOGGER.debug("getBankDepositsByDepositorAmountWithDepositors(from ={}, to ={})", from, to);
        return jdbcTemplate.query(selectBankDepositsByDepositorAmountWithDepositorsSql, new BankDepositDepositorMapper(),from, to);
    }

    /**
     * Get Bank Deposit by Currency with depositors
     *
     * @param currency String - Currency of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByCurrencyWithDepositors(String currency){
        LOGGER.debug("getBankDepositsByCurrencyWithDepositors(currency ={})", currency);
        return jdbcTemplate.query(selectBankDepositsByCurrencyWithDepositorsSql, new BankDepositDepositorMapper(),currency);
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
    public List<Map> getBankDepositsByCurrencyFromToDateDepositWithDepositors(Date startDate,
                                                                              Date endDate,
                                                                              String currency){
        LOGGER.debug("getBankDepositsByCurrencyFromToDateDepositWithDepositors(currency ={}, startDate ={}, endDate = {})", currency, startDate, endDate);
        return jdbcTemplate.query(selectBankDepositsByCurrencyFromToDateDepositWithDepositorsSql, new BankDepositDepositorMapper(), dateFormat.format(startDate),dateFormat.format(endDate),currency);
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
    public List<Map> getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(Date startDate,
                                                                                    Date endDate,
                                                                                    String currency){
        LOGGER.debug("getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(currency ={}, startDate ={}, endDate = {})", currency, startDate, endDate);
        return jdbcTemplate.query(selectBankDepositsByCurrencyFromToDateReturnDepositWithDepositorsSql, new BankDepositDepositorMapper(), startDate, endDate,currency);
    }

    /**
     * Get Bank Deposits by Depositor mark return with depositors
     *
     * @param markReturn Integer - Mark Return of the Bank Depositor
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByDepositorMarkReturnWithDepositors(Integer markReturn){
        LOGGER.debug("getBankDepositsByDepositorMarkReturnWithDepositors(return ={})", markReturn);
        return jdbcTemplate.query(selectBankDepositsByDepositorMarkReturnWithDepositorsSql, new BankDepositDepositorMapper(),markReturn);
    }

    /**
     * Get Bank Deposits by Variant Args with depositors
     *
     * @param args Object - Variant number of arguments
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    public List<Map> getBankDepositsByVarArgs(Object... args){
        LOGGER.debug("getBankDepositsByVarArgs(args ={})", args);
        selectBankDepositsByVarArgsSql = buildSqlString(args);
        LOGGER.debug("selectBankDepositsByVarArgsSql\n{}",selectBankDepositsByVarArgsSql);
        return jdbcTemplate.query(selectBankDepositsByVarArgsSql, new BankDepositDepositorMapper());
    }

    public class BankDepositMapper implements RowMapper<BankDeposit> {
        public BankDeposit mapRow(ResultSet rs, int i) throws SQLException {
            BankDeposit bankDeposit = new BankDeposit();

            bankDeposit.setDepositId(rs.getLong(DEPOSIT_ID));
            bankDeposit.setDepositName(rs.getString(DEPOSIT_NAME));
            bankDeposit.setDepositMinTerm(rs.getInt(DEPOSIT_MIN_TERM));
            bankDeposit.setDepositMinAmount(rs.getInt(DEPOSIT_MIN_AMOUNT));
            bankDeposit.setDepositCurrency(rs.getString(DEPOSIT_CURRENCY));
            bankDeposit.setDepositInterestRate(rs.getInt(DEPOSIT_INT_RATE));
            bankDeposit.setDepositAddConditions(rs.getString(DEPOSIT_CONDITIONS));

            return bankDeposit;
        }
    }

    public class BankDepositDepositorMapper implements RowMapper<Map> {
        public Map mapRow(ResultSet rs, int i) throws SQLException {
            Map<String, Object> list = new HashMap<String, Object>(11);

            list.put(DEPOSIT_ID, rs.getLong(DEPOSIT_ID));
            list.put(DEPOSIT_NAME, rs.getString(DEPOSIT_NAME));
            list.put(DEPOSIT_MIN_TERM, rs.getInt(DEPOSIT_MIN_TERM));
            list.put(DEPOSIT_MIN_AMOUNT, rs.getInt(DEPOSIT_MIN_AMOUNT));
            list.put(DEPOSIT_CURRENCY, rs.getString(DEPOSIT_CURRENCY));
            list.put(DEPOSIT_INT_RATE, rs.getInt(DEPOSIT_INT_RATE));
            list.put(DEPOSIT_CONDITIONS, rs.getString(DEPOSIT_CONDITIONS));
            list.put(DEPOSITOR_AMOUNT_DEPOSIT,rs.getInt(DEPOSITOR_AMOUNT_DEPOSIT));
            list.put(DEPOSITOR_AMOUNT_PLUS,rs.getInt(DEPOSITOR_AMOUNT_PLUS));
            list.put(DEPOSITOR_AMOUNT_MINUS,rs.getInt(DEPOSITOR_AMOUNT_MINUS));
            list.put(DEPOSITOR_NUMBER,rs.getInt(DEPOSITOR_NUMBER));
            return list;
        }
    }

    private String buildSqlString(Object... args){
        LOGGER.debug("writeSqlFile(args: {})",args);
        Assert.notNull(args,ERROR_METHOD_PARAM);

        String resultVarArgs = "";
        //== convert String.class to Date.class or Integer.class
        for(int i=0; i<args.length;i=i+2){
            try{
                args[i+1] = Integer.parseInt(args[i+1].toString());
            } catch (Exception intE){
                LOGGER.error("error - parseInt - {}, {}",args[i+1] ,intE.toString());
                if(args[i+1].getClass()==Date.class){
                    args[i+1] = dateFormat.format(args[i+1]);
                }
                LOGGER.debug("args[i+1] as Date={}",args[i+1]);
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
        LOGGER.debug("restrict.size({})",restrict.size());
        LOGGER.debug("restrict({})",restrict);
        LOGGER.debug("restrict.get(0)({})",restrict.values().toString());

        // get set of elements
        Set<Map.Entry<String, Integer>> set = restrict.entrySet();
        // query string
        String queryStringDeposit = "";
        String queryStringDepositor = "";
        int addAndDeposit=0, addAndDepositor=0;
        for (Map.Entry<String, Integer> resTemp : set) {
            if(resTemp.getKey().contains("depositor")){
                addAndDepositor++;
            }else {
                addAndDeposit++;
            }
        }
        addAndDeposit--;
        addAndDepositor--;
        LOGGER.debug("addAndDepositor({})",addAndDepositor);
        for (Map.Entry<String, Integer> resTemp : set) {
            if(resTemp.getKey().contains("depositor")){
                queryStringDepositor += resTemp.getKey() +"='"+resTemp.getValue()+"'\n";
            }else {
                queryStringDeposit += resTemp.getKey() +"='"+resTemp.getValue()+"'\n";
            }

            if(addAndDepositor>0){
                addAndDepositor--;
                queryStringDepositor += " and ";
            }
            if(addAndDeposit>0){
                addAndDeposit--;
                queryStringDeposit += " and ";
            }
        }

        String betweenStringDeposit = "";
        String betweenStringDepositor = "";
        for (int i=0; i<listLe.size(); i++){
            LOGGER.debug("listLe.get({})[0].toString()-{}",i,listLe.get(i)[0].toString());
            if(listLe.get(i)[0].toString().contains("depositor")){
                if(!betweenStringDepositor.isEmpty()){
                    betweenStringDepositor += " and ";
                }
                if(listLe.get(i)[0].toString().equals("depositor.depositorDateDeposit")||
                        listLe.get(i)[0].toString().equals("depositor.depositorDateReturnDeposit")){
                    betweenStringDepositor += listLe.get(i)[0].toString()+
                            " between '"+listLe.get(i)[1]+"' and '"+listGe.get(i)[1]+"'";
                }else{
                    betweenStringDepositor += listLe.get(i)[0].toString()+
                            " between "+listLe.get(i)[1].toString()+" and "+listGe.get(i)[1].toString();
                }
            }else{
                if(!betweenStringDeposit.isEmpty()){
                    betweenStringDeposit += " and ";
                }
                betweenStringDeposit += listLe.get(i)[0].toString()+
                        " between "+listLe.get(i)[1].toString()+" and "+listGe.get(i)[1].toString();
            }
        }

        String queryStringDepositorRep = queryStringDepositor.replaceAll("depositor.d","dep.d");
        LOGGER.debug("queryStringDepositorRep={}",queryStringDepositorRep);
        String betweenStringDepositorRep = betweenStringDepositor.replaceAll("depositor.d","dep.d");
        LOGGER.debug("betweenStringDepositorRep={}",betweenStringDepositorRep);

        resultVarArgs +="select deposit.*, depositor.*\n" +
                "from BANKDEPOSIT as deposit\n" +
                "\tleft join\n" +
                "\t(select dep.depositId as depId,\n" +
                "\t\t\tsum(dep.depositorAmountDeposit) as sumAmount,\n" +
                "\t\t\tsum(dep.depositorAmountPlusDeposit) as sumPlusAmount,\n" +
                "\t\t\tsum(dep.depositorAmountMinusDeposit) as sumMinusAmount,\n" +
                "\t\t\tcount(dep.depositorId) as numDepositors\n" +
                "\t from BANKDEPOSITOR as dep\n";

        if(!queryStringDepositorRep.isEmpty()||!betweenStringDepositorRep.isEmpty()){
            resultVarArgs+=" where ";
        }
        if(!queryStringDepositorRep.isEmpty()){
            resultVarArgs+=queryStringDepositorRep;
        }
        if(!betweenStringDepositorRep.isEmpty()){
            if(!queryStringDepositorRep.isEmpty()){
                resultVarArgs+=" and ";
            }
            resultVarArgs+=betweenStringDepositorRep;
        }

        resultVarArgs+="\t group by depId) as depositor\n" +
                "\t on deposit.depositId = depositor.depId\n" +
                "where deposit.depositId = depositor.depId and\n";

        if(!queryStringDeposit.isEmpty()){
            resultVarArgs+=queryStringDeposit;
        }
        if(!betweenStringDeposit.isEmpty()){
            if(!queryStringDeposit.isEmpty()){
                resultVarArgs+=" and ";
            }
            resultVarArgs+=betweenStringDeposit;
        }

        LOGGER.debug("resultVarArgs\n{}",resultVarArgs);
        return resultVarArgs;
    }

}