package com.brest.bank.service;

import com.brest.bank.domain.BankDeposit;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BankDepositService {
	//--- output
	public List<BankDeposit> getBankDeposits();
	/**
	 * @return all deposits with summ and count depositors
	 */
	public List<Map> getBankDepositsAllDepositors();
	public List<Map> getBankDepositsAllDepositorsBetweenDateDeposit(Date startDate, Date endDate);
	public List<Map> getBankDepositsAllDepositorsBetweenDateReturnDeposit(Date startDate, Date endDate);

	public BankDeposit getBankDepositById(Long depositId);
	public List<Map> getBankDepositByIdAllDepositors(Long depositId);
	public List<Map> getBankDepositByIdWithAllDepositorsBetweenDateDeposit(Long depositId, Date startDate, Date endDate);
	public List<Map> getBankDepositByIdWithAllDepositorsBetweenDateReturnDeposit(Long depositId, Date startDate, Date endDate);

	public BankDeposit getBankDepositByName(String depositName);
	public List<Map> getBankDepositByNameAllDepositors(String depositName);
	public List<Map> getBankDepositByNameWithAllDepositorsBetweenDateDeposit(String depositName, Date startDate, Date endDate);
	public List<Map> getBankDepositByNameWithAllDepositorsBetweenDateReturnDeposit(String depositName, Date startDate, Date endDate);

	//--- change data
	public Long addBankDeposit(BankDeposit bankDeposit);
	public void removeBankDeposit(Long depositId);
	public void updateBankDeposit(BankDeposit bankDeposit);

}