package com.brest.bank.service;

import com.brest.bank.domain.BankDeposit;
import java.util.List;

public interface BankDepositService {
	//--- output
	public List<BankDeposit> getBankDeposits();
	public BankDeposit getBankDepositById(Long depositId); 			//
	public BankDeposit getBankDepositByName(String depositName); 	//
	//--- change data
	public Long addBankDeposit(BankDeposit bankDeposit); 			//
	public void removeBankDeposit(Long depositId);					//
	public void updateBankDeposit(BankDeposit bankDeposit);			//
}