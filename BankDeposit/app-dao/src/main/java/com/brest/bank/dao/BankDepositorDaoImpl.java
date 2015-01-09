package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;

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
import java.util.List;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class BankDepositorDaoImpl implements BankDepositorDao{

    private static final Logger LOGGER=LogManager.getLogger();

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${insert_into_bankDepositor_path}')).inputStream)}")
    public String addNewBankDepositorSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_all_bankDepositors_path}')).inputStream)}")
    public String selectAllBankDepositorsSql;


    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${delete_bankDepositor_path}')).inputStream)}")
    public String deleteDepositorSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${delete_bankDepositor_by_id_deposit_path}')).inputStream)}")
    public String deleteDepositorByIdDepositSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${update_bankDepositor_path}')).inputStream)}")
    public String updateDepositorSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_by_id_path}')).inputStream)}")
    public String selectBankDepositorByIdSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_by_id_deposit_path}')).inputStream)}")
    public String selectBankDepositorByIdDepositSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_by_id_deposit_between_date_deposit_path}')).inputStream)}")
    public String selectBankDepositorByIdDepositBetweenDateDepositSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_summ_by_id_deposit_between_date_deposit_path}')).inputStream)}")
    public String selectBankDepositorSummByIdDepositBetweenDateDepositSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_summ_by_id_deposit_between_date_return_deposit_path}')).inputStream)}")
    public String selectBankDepositorSummByIdDepositBetweenDateReturnDepositSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_by_id_deposit_between_date_return_deposit_path}')).inputStream)}")
    public String selectBankDepositorByIdDepositBetweenDateReturnDepositSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_between_date_deposit_path}')).inputStream)}")
    public String selectBankDepositorBetweenDateDepositSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_between_date_return_deposit_path}')).inputStream)}")
    public String selectBankDepositorBetweenDateReturnDepositSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_by_name_path}')).inputStream)}")
    public String selectBankDepositorByNameSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_summ_amounts_path}')).inputStream)}")
    public String selectBankDepositorSummAmountsSQL;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_summ_amounts_deposit_between_date_deposit_path}')).inputStream)}")
    public String selectBankDepositorSummAmountsDepositBetweenDateDepositSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_summ_amounts_deposit_between_date_return_deposit_path}')).inputStream)}")
    public String selectBankDepositorSummAmountsDepositBetweenDateReturnDepositSql;

    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_summ_by_id_deposit_path}')).inputStream)}")
    public String selectBankDepositorSummByIdDepositSql;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final String DEPOSITOR_ID = "depositorId";
    public static final String DEPOSITOR_NAME = "depositorName";
    public static final String DEPOSITOR_ID_DEPOSIT = "depositorIdDeposit";
    public static final String DEPOSITOR_DATE_DEPOSIT = "depositorDateDeposit";
    public static final String DEPOSITOR_AMOUNT_DEPOSIT = "depositorAmountDeposit";
    public static final String DEPOSITOR_AMOUNT_PLUS = "depositorAmountPlusDeposit";
    public static final String DEPOSITOR_AMOUNT_MINUS = "depositorAmountMinusDeposit";
    public static final String DEPOSITOR_DATE_RETURN = "depositorDateReturnDeposit";
    public static final String DEPOSITOR_MARK_RETURN = "depositorMarkReturnDeposit";

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
    public Long addBankDepositor(BankDepositor bankDepositor){
        LOGGER.debug("addBankDepositor({})", bankDepositor);
        Assert.notNull(bankDepositor);

        Assert.isNull(bankDepositor.getDepositorId());
        //Assert.notNull(bankDepositor.getDepositorName(), "Deposit name should be specified.");

        Map<String, Object> parameters = new HashMap(9);

        parameters.put(DEPOSITOR_ID, bankDepositor.getDepositorId());
        parameters.put(DEPOSITOR_NAME, bankDepositor.getDepositorName());
        parameters.put(DEPOSITOR_ID_DEPOSIT, bankDepositor.getDepositorIdDeposit());
        parameters.put(DEPOSITOR_DATE_DEPOSIT, dateFormat.format(bankDepositor.getDepositorDateDeposit()));
        parameters.put(DEPOSITOR_AMOUNT_DEPOSIT, bankDepositor.getDepositorAmountDeposit());
        parameters.put(DEPOSITOR_AMOUNT_PLUS, bankDepositor.getDepositorAmountPlusDeposit());
        parameters.put(DEPOSITOR_AMOUNT_MINUS, bankDepositor.getDepositorAmountMinusDeposit());
        parameters.put(DEPOSITOR_DATE_RETURN, dateFormat.format(bankDepositor.getDepositorDateReturnDeposit()));
        parameters.put(DEPOSITOR_MARK_RETURN, bankDepositor.getDepositorMarkReturnDeposit());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(addNewBankDepositorSql, new MapSqlParameterSource(parameters), keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<BankDepositor> getBankDepositors() {
        LOGGER.debug("get depositors()");
        return jdbcTemplate.query(selectAllBankDepositorsSql, new BankDepositorMapper());
    }

    @Override
    public BankDepositor getBankDepositorsAllSummAmount(){
        LOGGER.debug("get All sum amount depositors()");
        return jdbcTemplate.queryForObject(selectBankDepositorSummAmountsSQL, new BankDepositorMapper());
    }

    @Override
    public BankDepositor getBankDepositorSummAmountDepositBetweenDateDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorSummAmountDepositBetweenDateDeposit(id:{} Dates:{}{})", dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.queryForObject(selectBankDepositorSummAmountsDepositBetweenDateDepositSql, new BankDepositorMapper(), dateFormat.format(startDate),dateFormat.format(endDate));
    }

    @Override
    public BankDepositor getBankDepositorSummAmountDepositBetweenDateReturnDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorSummAmountDepositBetweenDateReturnDeposit(id:{} Dates:{}{})", dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.queryForObject(selectBankDepositorSummAmountsDepositBetweenDateReturnDepositSql, new BankDepositorMapper(), dateFormat.format(startDate), dateFormat.format(endDate));
    }

    @Override
    public BankDepositor getBankDepositorsSummAmountByIdDeposit(Long depositorIdDeposit) {
        LOGGER.debug("getBankDepositorsSummAmountByIdDeposit(depositorIdDeposit={})", depositorIdDeposit);
        return jdbcTemplate.queryForObject(selectBankDepositorSummByIdDepositSql, new BankDepositorMapper(),depositorIdDeposit);
    }

    @Override
    public BankDepositor getBankDepositorsSummAmountByIdDepositBetweenDateDeposit(Long depositorIdDeposit, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorsSummAmountByIdDepositBetweenDateDeposit(depositorIdDeposit={}, Dates={},{})", depositorIdDeposit, dateFormat.format(startDate), dateFormat.format(endDate));
        return jdbcTemplate.queryForObject(selectBankDepositorSummByIdDepositBetweenDateDepositSql, new BankDepositorMapper(),depositorIdDeposit, dateFormat.format(startDate), dateFormat.format(endDate));
    }

    @Override
    public BankDepositor getBankDepositorsSummAmountByIdDepositBetweenDateReturnDeposit(Long depositorIdDeposit, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorsSummAmountByIdDepositBetweenDateReturnDeposit(depositorIdDeposit={}, Dates={},{})", depositorIdDeposit, dateFormat.format(startDate), dateFormat.format(endDate));
        return jdbcTemplate.queryForObject(selectBankDepositorSummByIdDepositBetweenDateReturnDepositSql, new BankDepositorMapper(),depositorIdDeposit, dateFormat.format(startDate), dateFormat.format(endDate));
    }

    @Override
    public void removeBankDepositor(Long depositorId) {
        LOGGER.debug("removeBankDepositor(depositorId={})",depositorId);
        Assert.notNull(depositorId);
        jdbcTemplate.update(deleteDepositorSql, depositorId);
    }

    @Override
    public void removeBankDepositorByIdDeposit(Long depositorIdDeposit) {
        LOGGER.debug("removeBankDepositorByIdDeposit(depositorIdDeposit={})",depositorIdDeposit);
        Assert.notNull(depositorIdDeposit);
        jdbcTemplate.update(deleteDepositorByIdDepositSql, depositorIdDeposit);
    }

    @Override
    public BankDepositor getBankDepositorByName(String depositorName){
        LOGGER.debug("getBankDepositorByName(depositorName={}) ", depositorName);
        return jdbcTemplate.queryForObject(selectBankDepositorByNameSql,new String[]{depositorName.toLowerCase()}, new BankDepositorMapper());
    }

    @Override
    public BankDepositor getBankDepositorById(Long depositorId) {
        LOGGER.debug("getBankDepositorById(depositorId={})", depositorId);
        return jdbcTemplate.queryForObject(selectBankDepositorByIdSql, new BankDepositorMapper(),depositorId);
    }

    @Override
    public List<BankDepositor> getBankDepositorByIdDeposit(Long depositorIdDeposit) {
        LOGGER.debug("getBankDepositorByIdDeposit(depositorIdDeposit={})", depositorIdDeposit);
        return jdbcTemplate.query(selectBankDepositorByIdDepositSql, new BankDepositorMapper(),depositorIdDeposit);
    }

    @Override
    public List<BankDepositor> getBankDepositorByIdDepositBetweenDateDeposit(Long depositorIdDeposit, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorByIdDepositBetweenDateDeposit(depositorIdDeposit={}, Dates={},{})", depositorIdDeposit, dateFormat.format(startDate), dateFormat.format(endDate));
        return jdbcTemplate.query(selectBankDepositorByIdDepositBetweenDateDepositSql, new BankDepositorMapper(),depositorIdDeposit, dateFormat.format(startDate), dateFormat.format(endDate));
    }

    @Override
    public List<BankDepositor> getBankDepositorByIdDepositBetweenDateReturnDeposit(Long depositorIdDeposit, Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorByIdDepositBetweenDateReturnDeposit(depositorIdDeposit={}, Dates={},{})", depositorIdDeposit, dateFormat.format(startDate), dateFormat.format(endDate));
        return jdbcTemplate.query(selectBankDepositorByIdDepositBetweenDateReturnDepositSql, new BankDepositorMapper(),depositorIdDeposit, dateFormat.format(startDate), dateFormat.format(endDate));
    }

    @Override
    public List<BankDepositor> getBankDepositorBetweenDateDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorBetweenDateDeposit(Dates:{}{})", dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.query(selectBankDepositorBetweenDateDepositSql, new BankDepositorMapper(),dateFormat.format(startDate),dateFormat.format(endDate));
    }

    @Override
    public List<BankDepositor> getBankDepositorBetweenDateReturnDeposit(Date startDate, Date endDate) {
        LOGGER.debug("getBankDepositorBetweenDateReturnDeposit(Dates:{}{})", dateFormat.format(startDate),dateFormat.format(endDate));
        return jdbcTemplate.query(selectBankDepositorBetweenDateReturnDepositSql, new BankDepositorMapper(),dateFormat.format(startDate),dateFormat.format(endDate));
    }

    @Override
    public void updateBankDepositor(BankDepositor bankDepositor){
        KeyHolder keyholder = new GeneratedKeyHolder();
        LOGGER.debug("updateBankDepositor({})", bankDepositor);
        Assert.notNull(bankDepositor.getDepositorId());
        //Assert.notNull(bankDepositor.getDepositorAmountDeposit());
        Map<String, Object> parameters = new HashMap(9);

        parameters.put(DEPOSITOR_ID, bankDepositor.getDepositorId());
        parameters.put(DEPOSITOR_NAME, bankDepositor.getDepositorName());
        parameters.put(DEPOSITOR_ID_DEPOSIT, bankDepositor.getDepositorIdDeposit());
        parameters.put(DEPOSITOR_DATE_DEPOSIT, dateFormat.format(bankDepositor.getDepositorDateDeposit()));
        parameters.put(DEPOSITOR_AMOUNT_DEPOSIT, bankDepositor.getDepositorAmountDeposit());
        parameters.put(DEPOSITOR_AMOUNT_PLUS, bankDepositor.getDepositorAmountPlusDeposit());
        parameters.put(DEPOSITOR_AMOUNT_MINUS, bankDepositor.getDepositorAmountMinusDeposit());
        parameters.put(DEPOSITOR_DATE_RETURN, dateFormat.format(bankDepositor.getDepositorDateReturnDeposit()));
        parameters.put(DEPOSITOR_MARK_RETURN, bankDepositor.getDepositorMarkReturnDeposit());

        namedJdbcTemplate.update(updateDepositorSql,new MapSqlParameterSource(parameters),keyholder);
        LOGGER.debug("updateBankDepositor(): depositorId{}",keyholder.getKey());
    }

    public class BankDepositorMapper implements RowMapper<BankDepositor> {
        public BankDepositor mapRow(ResultSet rs, int i) throws SQLException {
            BankDepositor bankDepositor = new BankDepositor();

            bankDepositor.setDepositorId(rs.getLong(DEPOSITOR_ID));
            bankDepositor.setDepositorName(rs.getString(DEPOSITOR_NAME));
            bankDepositor.setDepositorIdDeposit(rs.getLong(DEPOSITOR_ID_DEPOSIT));
            bankDepositor.setDepositorDateDeposit(rs.getDate(DEPOSITOR_DATE_DEPOSIT));
            bankDepositor.setDepositorAmountDeposit(rs.getInt(DEPOSITOR_AMOUNT_DEPOSIT));
            bankDepositor.setDepositorAmountPlusDeposit(rs.getInt(DEPOSITOR_AMOUNT_PLUS));
            bankDepositor.setDepositorAmountMinusDeposit(rs.getInt(DEPOSITOR_AMOUNT_MINUS));
            bankDepositor.setDepositorDateReturnDeposit(rs.getDate(DEPOSITOR_DATE_RETURN));
            bankDepositor.setDepositorMarkReturnDeposit(rs.getInt(DEPOSITOR_MARK_RETURN));

            return bankDepositor;
        }
    }
}