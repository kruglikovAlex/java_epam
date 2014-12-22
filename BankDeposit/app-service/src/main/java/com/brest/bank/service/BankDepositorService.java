package com.brest.bank.service;

import com.brest.bank.domain.BankDepositor;

import java.util.Date;
import java.util.List;

public interface BankDepositorService {
	//--- output
	public List<BankDepositor> getBankDepositors();
	public BankDepositor getBankDepositorById(Long depositorId);
	public BankDepositor getBankDepositorByName(String depositorName);
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
	public boolean removeBankDepositorByIdDeposit(Long depositorIdDeposit);
	public void updateBankDepositor(BankDepositor bankDepositor);
}