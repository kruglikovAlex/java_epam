package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;
import java.util.List;
import java.util.Date;

public interface BankDepositorDao {
	//--- output
	public List<BankDepositor> getBankDepositors();
	public BankDepositor getBankDepositorsAllSummAmount();

	public List<BankDepositor> getBankDepositorBetweenDateDeposit(Date startDate, Date endDate);
	public BankDepositor getBankDepositorSummAmountDepositBetweenDateDeposit(Date startDate, Date endDate);

	public List<BankDepositor> getBankDepositorBetweenDateReturnDeposit(Date startDate, Date endDate);
	public BankDepositor getBankDepositorSummAmountDepositBetweenDateReturnDeposit(Date startDate, Date endDate);

	public BankDepositor getBankDepositorById(Long depositorId);
	public BankDepositor getBankDepositorByName(String depositorName);

	public List<BankDepositor> getBankDepositorByIdDeposit(Long depositorIdDeposit);
	public BankDepositor getBankDepositorsSummAmountByIdDeposit(Long depositorIdDeposit);
	public List<BankDepositor> getBankDepositorByIdDepositBetweenDateDeposit(Long depositorIdDeposit, Date startDate, Date endDate);
	public BankDepositor getBankDepositorsSummAmountByIdDepositBetweenDateDeposit(Long depositorIdDeposit, Date startDate, Date endDate);
	public List<BankDepositor> getBankDepositorByIdDepositBetweenDateReturnDeposit(Long depositorIdDeposit, Date startDate, Date endDate);
	public BankDepositor getBankDepositorsSummAmountByIdDepositBetweenDateReturnDeposit(Long depositorIdDeposit, Date startDate, Date endDate);

	//--- change data
	public Long addBankDepositor(BankDepositor bankDepositor);
	public void removeBankDepositor(Long depositorId);
	public void removeBankDepositorByIdDeposit(Long depositorIdDeposit);
	public void updateBankDepositor(BankDepositor bankDepositor);
}
