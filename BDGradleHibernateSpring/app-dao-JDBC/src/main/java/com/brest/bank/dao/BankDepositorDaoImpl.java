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

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final String DEPOSIT_ID = "depositId";
    public static final String DEPOSITOR_ID = "depositorId";
    public static final String DEPOSITOR_NAME = "depositorName";
    public static final String DEPOSITOR_DATE_DEPOSIT = "depositorDateDeposit";
    public static final String DEPOSITOR_AMOUNT_DEPOSIT = "depositorAmountDeposit";
    public static final String DEPOSITOR_AMOUNT_PLUS = "depositorAmountPlusDeposit";
    public static final String DEPOSITOR_AMOUNT_MINUS = "depositorAmountMinusDeposit";
    public static final String DEPOSITOR_DATE_RETURN = "depositorDateReturnDeposit";
    public static final String DEPOSITOR_MARK_RETURN = "depositorMarkReturnDeposit";
    private static final Logger LOGGER=LogManager.getLogger();
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${insert_into_bankDepositor_path}')).inputStream)}")
    public String addNewBankDepositorSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_all_bankDepositors_path}')).inputStream)}")
    public String selectAllBankDepositorsSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${delete_bankDepositor_path}')).inputStream)}")
    public String deleteDepositorSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${update_bankDepositor_path}')).inputStream)}")
    public String updateDepositorSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_by_id_path}')).inputStream)}")
    public String selectBankDepositorByIdSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_by_id_deposit_path}')).inputStream)}")
    public String selectBankDepositorByIdDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_from_to_date_deposit_path}')).inputStream)}")
    public String selectBankDepositorsFromToDateDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_from_to_date_return_deposit_path}')).inputStream)}")
    public String selectBankDepositorsFromToDateReturnDepositSql;
    @Value("#{T(org.apache.commons.io.IOUtils).toString((new org.springframework.core.io.ClassPathResource('${select_bankDepositor_by_name_path}')).inputStream)}")
    public String selectBankDepositorByNameSql;
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
    public void addBankDepositor(Long depositId, BankDepositor bankDepositor){
        LOGGER.debug("addBankDepositor({},{})", depositId, bankDepositor);
        Assert.notNull(depositId);
        Assert.notNull(bankDepositor);

        Assert.isNull(bankDepositor.getDepositorId());
        Assert.notNull(bankDepositor.getDepositorName(), "Deposit name should be specified.");

        Map<String, Object> parameters = new HashMap(10);

        parameters.put(DEPOSIT_ID, depositId);
        parameters.put(DEPOSITOR_ID, bankDepositor.getDepositorId());
        parameters.put(DEPOSITOR_NAME, bankDepositor.getDepositorName());
        parameters.put(DEPOSITOR_DATE_DEPOSIT, dateFormat.format(bankDepositor.getDepositorDateDeposit()));
        parameters.put(DEPOSITOR_AMOUNT_DEPOSIT, bankDepositor.getDepositorAmountDeposit());
        parameters.put(DEPOSITOR_AMOUNT_PLUS, bankDepositor.getDepositorAmountPlusDeposit());
        parameters.put(DEPOSITOR_AMOUNT_MINUS, bankDepositor.getDepositorAmountMinusDeposit());
        parameters.put(DEPOSITOR_DATE_RETURN, dateFormat.format(bankDepositor.getDepositorDateReturnDeposit()));
        parameters.put(DEPOSITOR_MARK_RETURN, bankDepositor.getDepositorMarkReturnDeposit());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(addNewBankDepositorSql, new MapSqlParameterSource(parameters), keyHolder);
        //return keyHolder.getKey().longValue();
    }

    @Override
    public List<BankDepositor> getBankDepositorsCriteria() {
        LOGGER.debug("get depositors()");
        return jdbcTemplate.query(selectAllBankDepositorsSql, new BankDepositorMapper());
    }

    @Override
    public void removeBankDepositor(Long depositorId) {
        LOGGER.debug("removeBankDepositor(depositorId={})",depositorId);
        Assert.notNull(depositorId);
        jdbcTemplate.update(deleteDepositorSql, depositorId);
    }

    @Override
    public BankDepositor getBankDepositorByNameCriteria(String depositorName){
        LOGGER.debug("getBankDepositorByName(depositorName={}) ", depositorName);
        return jdbcTemplate.queryForObject(selectBankDepositorByNameSql,new String[]{depositorName.toLowerCase()}, new BankDepositorMapper());
    }

    @Override
    public BankDepositor getBankDepositorByIdCriteria(Long depositorId) {
        LOGGER.debug("getBankDepositorById(depositorId={})", depositorId);
        return jdbcTemplate.queryForObject(selectBankDepositorByIdSql, new BankDepositorMapper(),depositorId);
    }

    @Override
    public List<BankDepositor> getBankDepositorByIdDepositCriteria(Long depositId) {
        LOGGER.debug("getBankDepositorByIdDeposit(depositId={})", depositId);
        return jdbcTemplate.query(selectBankDepositorByIdDepositSql, new BankDepositorMapper(),depositId);
    }

    @Override
    public List<BankDepositor> getBankDepositorsFromToDateDeposit(Date start, Date end) {
        LOGGER.debug("getBankDepositorsFromToDateDeposit(Dates:{}{})", dateFormat.format(start),dateFormat.format(end));
        return jdbcTemplate.query(selectBankDepositorsFromToDateDepositSql, new BankDepositorMapper(),dateFormat.format(start),dateFormat.format(end));
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
        LOGGER.debug("getBankDepositorsFromToDateReturnDeposit(Dates:{}{})", dateFormat.format(start),dateFormat.format(end));
        return jdbcTemplate.query(selectBankDepositorsFromToDateReturnDepositSql, new BankDepositorMapper(),dateFormat.format(start),dateFormat.format(end));
    }

    @Override
    public void updateBankDepositor(BankDepositor bankDepositor){
        KeyHolder keyholder = new GeneratedKeyHolder();
        LOGGER.debug("updateBankDepositor({})", bankDepositor);
        Assert.notNull(bankDepositor.getDepositorId());
        Assert.notNull(bankDepositor.getDepositorAmountDeposit());
        Map<String, Object> parameters = new HashMap(9);

        parameters.put(DEPOSITOR_ID, bankDepositor.getDepositorId());
        parameters.put(DEPOSITOR_NAME, bankDepositor.getDepositorName());
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