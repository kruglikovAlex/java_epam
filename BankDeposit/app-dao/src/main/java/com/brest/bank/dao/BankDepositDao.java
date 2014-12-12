package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import java.util.List;

public interface BankDepositDao {
	//--- output
	public List<BankDeposit> getBankDeposits();
	public BankDeposit getBankDepositById(Long depositId);
	public BankDeposit getBankDepositByName(String depositName);
	//--- change data
	public Long addBankDeposit(BankDeposit bankDeposit);
	public void removeBankDeposit(Long depositId);
	public void updateBankDeposit(BankDeposit bankDeposit);
}
