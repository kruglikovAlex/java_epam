package com.brest.bank.domain;

import java.util.HashSet;
import java.util.Set;

public class BankDeposit{

	private Long 	depositId;				//идентификатор
	private String 	depositName;			//наименование вклада
	private Integer	depositMinTerm;			//минимальный срок вклада (мес.)
	private Integer	depositMinAmount;		//минимальная сумма вклада
	private String 	depositCurrency;		//валюта
	private Integer	depositInterestRate;	//процентная ставка
	private String 	depositAddConditions;	//дополнительные условия

	private Set depositors = new HashSet(); // для связи таблиц

	//--- конструктор по умолчанию
	public BankDeposit(){
	}

	//--- конструктор с параметрами
	public BankDeposit(Long      	depositId,
					   String 	    depositName,
					   Integer 		depositMinTerm,
					   Integer 		depositMinAmount,
					   String 	    depositCurrency,
					   Integer 		depositInterestRate,
					   String 	    depositAddConditions,
                       Set depositors){
		this.depositId = depositId;
		this.depositName = depositName;
		this.depositMinTerm = depositMinTerm;
		this.depositMinAmount = depositMinAmount;
		this.depositCurrency = depositCurrency;
		this.depositInterestRate = depositInterestRate;
		this.depositAddConditions = depositAddConditions;
        this.depositors = depositors;
	}

	//--- get/set Id (идентификатор фклада)
	public Long getDepositId(){
		return depositId;
	}

	public void setDepositId(Long depositId){
		this.depositId = depositId;	
	}
	//--- get/set Name (наименование вклада)
	public String getDepositName(){
		return depositName;	
	}

	public void setDepositName(String depositName){
		this.depositName = depositName;
	}
	//--- get/set depositMinTerm (минимальный срок вклада)
	public int getDepositMinTerm(){
		return depositMinTerm;	
	}

	public void setDepositMinTerm(int depositMinTerm){
		this.depositMinTerm = depositMinTerm;	
	}
	//--- get/set depositMinAmount (минимальная суииа вклада)
	public int getDepositMinAmount(){
		return depositMinAmount;	
	}

	public void setDepositMinAmount(int depositMinAmount){
		this.depositMinAmount = depositMinAmount;	
	}
	//--- get/set depositCurrency (валюта вклада)
	public String getDepositCurrency(){
		return depositCurrency;	
	}

	public void setDepositCurrency(String depositCurrency){
		this.depositCurrency = depositCurrency;	
	}
	//--- get/set depositInterestRate (% ставка)
	public int getDepositInterestRate(){
		return depositInterestRate;	
	}

	public void setDepositInterestRate(int depositInterestRate){
		this.depositInterestRate = depositInterestRate;	
	}
	//--- get/set depositAddConditions (дополнительные услович)
	public String getDepositAddConditions(){
		return depositAddConditions;	
	}

	public void setDepositAddConditions(String depositAddConditions){
		this.depositAddConditions = depositAddConditions;	
	}

	//--- get/set one-to-many link
	public Set getDepositors(){
		return depositors;
	}

	public void setDepositors(Set depositors){
		this.depositors = depositors;
	}

	public void setToDepositors(BankDepositor depositor){
		this.getDepositors().add(depositor);
		depositor.setDeposit(this);
	}

	@Override
    public String toString() {
        return "BankDeposit: { depositId=" + depositId + 
        		", depositName=" + depositName + 
        		", depositMinTerm=" + depositMinTerm + 
        		", depositMinAmount=" + depositMinAmount + 
        		", depositCurrency=" + depositCurrency + 
        		", depositInterestRate=" + depositInterestRate + 
        		", depositAddConditions=" + depositAddConditions +'}';
    }
}
