package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;
import java.util.List;
import java.util.Date;

public interface BankDepositorDao {
	//--- output

	public BankDepositor getBankDepositorById(Long depositorId);
	public BankDepositor getBankDepositorByName(String depositorName);
	public List<BankDepositor> getBankDepositors();
	public List<BankDepositor> getBankDepositorByIdDeposit(Long depositorIdDeposit);
	public List<BankDepositor> getBankDepositorBetweenDateDeposit(Date startDate, Date endDate);
	public List<BankDepositor> getBankDepositorBetweenDateReturnDeposit(Date startDate, Date endDate);
	public BankDepositor getBankDepositorsAllSummAmount();
	public BankDepositor getBankDepositorsSummAmountByIdDeposit(Long depositorIdDeposit);
	public BankDepositor getBankDepositorSummAmountDepositBetweenDateDeposit(Date startDate, Date endDate);
	public BankDepositor getBankDepositorSummAmountDepositBetweenDateReturnDeposit(Date startDate, Date endDate);
	//--- change data
	public Long addBankDepositor(BankDepositor bankDepositor);
	public void removeBankDepositor(Long depositorId);
	public void removeBankDepositorByIdDeposit(Long depositorIdDeposit);
	public void updateBankDepositor(BankDepositor bankDepositor);
}
