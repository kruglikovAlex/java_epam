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
import java.util.List;

import java.util.HashMap;
import java.util.Map;

@Component
public class BankDepositDaoImpl implements BankDepositDao{

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

	
	public static final String DEPOSIT_ID = "depositId";
	public static final String DEPOSIT_NAME = "depositName";
	public static final String DEPOSIT_MIN_TERM = "depositMinTerm";
	public static final String DEPOSIT_MIN_AMOUNT = "depositMinAmount";
	public static final String DEPOSIT_CURRENCY = "depositCurrency";
	public static final String DEPOSIT_INT_RATE = "depositInterestRate";
	public static final String DEPOSIT_CONDITIONS = "depositAddConditions";

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
}
