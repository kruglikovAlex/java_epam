package com.brest.bank.domain;

//package com.brest.bank.domain;

public class BankDeposit{
	private Long 	depositId;				//�������������
	private String 	depositName;			//������������ ������
	private Integer	depositMinTerm;			//����������� ���� ������
	private Integer	depositMinAmount;		//����������� ����� ������
	private String 	depositCurrency;		//������ ������
	private Integer	depositInterestRate;	//���������� ������
	private String 	depositAddConditions;	//�������������� �������

	//--- ����������� � �����������
	public BankDeposit(Long      	depositId,
					   String 	    depositName,
					   Integer 		depositMinTerm,
					   Integer 		depositMinAmount,
					   String 	    depositCurrency,
					   Integer 		depositInterestRate,
					   String 	    depositAddConditions){
		this.depositId = depositId;
		this.depositName = depositName;
		this.depositMinTerm = depositMinTerm;
		this.depositMinAmount = depositMinAmount;
		this.depositCurrency = depositCurrency;
		this.depositInterestRate = depositInterestRate;
		this.depositAddConditions = depositAddConditions; 
	}

    //--- ����������� ��� ����������
    public BankDeposit(){
    }
	//--- get/set Id (������������� ������)
	public Long getDepositId(){
		return depositId;
	}

	public void setDepositId(Long depositId){
		this.depositId = depositId;	
	}
	//--- get/set Name (������������ ������)
	public String getDepositName(){
		return depositName;	
	}

	public void setDepositName(String depositName){
		this.depositName = depositName;
	}
	//--- get/set depositMinTerm (����������� ���� ������)
	public int getDepositMinTerm(){
		return depositMinTerm;	
	}

	public void setDepositMinTerm(int depositMinTerm){
		this.depositMinTerm = depositMinTerm;	
	}
	//--- get/set depositMinAmount (����������� ����� ������)
	public int getDepositMinAmount(){
		return depositMinAmount;	
	}

	public void setDepositMinAmount(int depositMinAmount){
		this.depositMinAmount = depositMinAmount;	
	}
	//--- get/set depositCurrency (������ ������)
	public String getDepositCurrency(){
		return depositCurrency;	
	}

	public void setDepositCurrency(String depositCurrency){
		this.depositCurrency = depositCurrency;	
	}
	//--- get/set depositInterestRate (% ������)
	public int getDepositInterestRate(){
		return depositInterestRate;	
	}

	public void setDepositInterestRate(int depositInterestRate){
		this.depositInterestRate = depositInterestRate;	
	}
	//--- get/set depositAddConditions (�������������� �������)
	public String getDepositAddConditions(){
		return depositAddConditions;	
	}

	public void setDepositAddConditions(String depositAddConditions){
		this.depositAddConditions = depositAddConditions;	
	}
	
	@Override
    public String toString() {
        return "BankDeposit: { depositId=" + depositId + 
        		", depositName=" + depositName + 
        		", depositMinTerm=" + depositMinTerm + 
        		", depositMinAmount=" + depositMinAmount + 
        		", depositCurrency=" + depositCurrency + 
        		", depositInterestRate=" + depositInterestRate + 
        		", depositAddConditions=" + depositAddConditions + '}';
    }
}
