package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

@Component
public class BankDepositDaoImpl implements BankDepositDao{

    private static final Logger LOGGER=LogManager.getLogger();

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_all_bankDeposits_with_all_bankDeposittors_path}')).inputStream)}")
    public String selectAllBankDepositsWithAllBankDeposittorsSql;

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
    public Long addBankDeposit(BankDeposit bankDeposit){
        LOGGER.debug("addBankDeposit({})", bankDeposit);
        Assert.notNull(bankDeposit);
        Assert.isNull(bankDeposit.getDepositId());
        //Assert.notNull(bankDeposit.getDepositName(), "Deposit name should be specified.");
        //Assert.notNull(bankDeposit.getDepositMinTerm(), "Deposit MinTerm should be specified.");

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
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<BankDeposit> getBankDeposits() {
        LOGGER.debug("get deposits()");
        return jdbcTemplate.query(selectAllBankDepositsSql, new BankDepositMapper());
    }

    @Override
    public void removeBankDeposit(Long depositId) {
        LOGGER.debug("removeBankDepositor(depositorId={})",depositId);
        jdbcTemplate.update(deleteDepositSql, depositId);
    }

    @Override
    public BankDeposit getBankDepositByName(String depositName){
        LOGGER.debug("getBankDepositByName(depositName={}) ", depositName);
        return jdbcTemplate.queryForObject(selectBankDepositByNameSql,new String[]{depositName.toLowerCase()}, new BankDepositMapper());
    }

    @Override
    public BankDeposit getBankDepositById(Long depositId) {
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

    @Override
    public List<Map> getBankDepositsAllDepositors() {
        LOGGER.debug("get deposits with all depositors()");
        return jdbcTemplate.query(selectAllBankDepositsWithAllBankDeposittorsSql, new BankDepositDepositorMapper());
    }

    @Override
    public List<Map> getBankDepositsAllDepositorsBetweenDateDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsAllDepositorsBetweenDateDeposit(Dates:{},{})", dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.query(selectAllBankDepositsWithAllBankDeposittorsBetweenDateDepositSql, new BankDepositDepositorMapper(),dateFormat.format(startDate),dateFormat.format(endDate));
    }

    @Override
    public List<Map> getBankDepositsAllDepositorsBetweenDateReturnDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsAllDepositorsBetweenDateReturnDeposit(Dates:{},{})", dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.query(selectAllBankDepositsWithAllBankDeposittorsBetweenDateReturnDepositSql, new BankDepositDepositorMapper(),dateFormat.format(startDate),dateFormat.format(endDate));
    }

    @Override
    public List<Map> getBankDepositByIdAllDepositors(Long depositId) {
        LOGGER.debug("getBankDepositByIdAllDepositors(depositId={})", depositId);
        return jdbcTemplate.query(selectBankDepositsByIdWithAllBankDeposittorsSql, new BankDepositDepositorMapper(),depositId);
    }

    @Override
    public List<Map> getBankDepositByIdWithAllDepositorsBetweenDateDeposit(Long depositId, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsByIdWithAllDepositorsBetweenDateDeposit(depositId={}, Dates:{},{})", depositId, dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.query(selectBankDepositsByIdWithAllBankDeposittorsBetweenDateDepositSql, new BankDepositDepositorMapper(),dateFormat.format(startDate),dateFormat.format(endDate),depositId);
    }

    @Override
    public List<Map> getBankDepositByIdWithAllDepositorsBetweenDateReturnDeposit(Long depositId, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsByIdWithAllDepositorsBetweenDateReturnDeposit(depositId={}, Dates:{},{})", depositId, dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.query(selectBankDepositsByIdWithAllBankDeposittorsBetweenDateReturnDepositSql, new BankDepositDepositorMapper(),dateFormat.format(startDate),dateFormat.format(endDate),depositId);
    }

    @Override
    public List<Map> getBankDepositByNameAllDepositors(String depositName) {
        LOGGER.debug("getBankDepositByNameAllDepositors(depositName={})", depositName);
        return jdbcTemplate.query(selectBankDepositsByNameWithAllBankDeposittorsSql, new BankDepositDepositorMapper(),depositName);
    }

    @Override
    public List<Map> getBankDepositByNameWithAllDepositorsBetweenDateDeposit(String depositName, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsByNameWithAllDepositorsBetweenDateDeposit(depositId={}, Dates:{},{})", depositName, dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.query(selectBankDepositsByNameWithAllBankDeposittorsBetweenDateDepositSql, new BankDepositDepositorMapper(),dateFormat.format(startDate),dateFormat.format(endDate),depositName);
    }

    @Override
    public List<Map> getBankDepositByNameWithAllDepositorsBetweenDateReturnDeposit(String depositName, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositsByNameWithAllDepositorsBetweenDateReturnDeposit(depositId={}, Dates:{},{})", depositName, dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.query(selectBankDepositsByNameWithAllBankDeposittorsBetweenDateReturnDepositSql, new BankDepositDepositorMapper(),dateFormat.format(startDate),dateFormat.format(endDate),depositName);
    }

    public class BankDepositDepositorMapper implements RowMapper<Map> {
        public Map mapRow(ResultSet rs, int i) throws SQLException {
            Map<String, Object> list = new HashMap<String, Object>(12);

            list.put(DEPOSIT_ID, rs.getLong(DEPOSIT_ID));
            list.put(DEPOSIT_NAME, rs.getString(DEPOSIT_NAME));
            list.put(DEPOSIT_MIN_TERM, rs.getInt(DEPOSIT_MIN_TERM));
            list.put(DEPOSIT_MIN_AMOUNT, rs.getInt(DEPOSIT_MIN_AMOUNT));
            list.put(DEPOSIT_CURRENCY, rs.getString(DEPOSIT_CURRENCY));
            list.put(DEPOSIT_INT_RATE, rs.getInt(DEPOSIT_INT_RATE));
            list.put(DEPOSIT_CONDITIONS, rs.getString(DEPOSIT_CONDITIONS));
            list.put(DEPOSITOR_ID_DEPOSIT,rs.getLong(DEPOSITOR_ID_DEPOSIT));
            list.put(DEPOSITOR_AMOUNT_DEPOSIT,rs.getInt(DEPOSITOR_AMOUNT_DEPOSIT));
            list.put(DEPOSITOR_AMOUNT_PLUS,rs.getInt(DEPOSITOR_AMOUNT_PLUS));
            list.put(DEPOSITOR_AMOUNT_MINUS,rs.getInt(DEPOSITOR_AMOUNT_MINUS));
            list.put(DEPOSITOR_NUMBER,rs.getInt(DEPOSITOR_NUMBER));
            return list;
        }
    }
}