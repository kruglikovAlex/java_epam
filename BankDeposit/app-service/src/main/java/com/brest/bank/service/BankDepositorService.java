package com.brest.bank.service;

import com.brest.bank.domain.BankDepositor;

import java.util.Date;
import java.util.List;

public interface BankDepositorService {
	//--- output
	public List<BankDepositor> getBankDepositors();
	public BankDepositor getBankDepositorsAllSummAmount();

	public BankDepositor getBankDepositorById(Long depositorId);
	public BankDepositor getBankDepositorByName(String depositorName);

	public List<BankDepositor> getBankDepositorBetweenDateDeposit(Date startDate, Date endDate);
	public BankDepositor getBankDepositorSummAmountDepositBetweenDateDeposit(Date startDate, Date endDate);
	public List<BankDepositor> getBankDepositorBetweenDateReturnDeposit(Date startDate, Date endDate);
	public BankDepositor getBankDepositorSummAmountDepositBetweenDateReturnDeposit(Date startDate, Date endDate);

	public List<BankDepositor> getBankDepositorByIdDeposit(Long depositorIdDeposit);
	public BankDepositor getBankDepositorsSummAmountByIdDeposit(Long depositorIdDeposit);
	public List<BankDepositor> getBankDepositorByIdDepositBetweenDateDeposit(Long depositorIdDeposit, Date startDate, Date endDate);
	public BankDepositor getBankDepositorsSummAmountByIdDepositBetweenDateDeposit(Long depositorIdDeposit, Date startDate, Date endDate);
	public List<BankDepositor> getBankDepositorByIdDepositBetweenDateReturnDeposit(Long depositorIdDeposit, Date startDate, Date endDate);

	//--- change data
	public Long addBankDepositor(BankDepositor bankDepositor);
	public void removeBankDepositor(Long depositorId);
	public boolean removeBankDepositorByIdDeposit(Long depositorIdDeposit);
	public void updateBankDepositor(BankDepositor bankDepositor);
}