package com.brest.bank.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BankDepositor {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Long 		depositorId; 				//идентификатор
	private String 		depositorName;				//Ф.И.О. вкладчика
	private Date 		depositorDateDeposit;		//Дата вклада
	private Integer		depositorAmountDeposit;		//суммма вклада
	private Integer		depositorAmountPlusDeposit;	//сумма процентов
	private Integer		depositorAmountMinusDeposit;//выдача сумм из вклада
	private Date		depositorDateReturnDeposit;	//Дата предполагаемого закрытия вклада
	private Integer		depositorMarkReturnDeposit;	//отметка о закрытии вклада

	private BankDeposit bankDeposit;
	
	//--- конструктор по умолчанию
	public BankDepositor(){
	}
	//--- конструктор с параметрами
	public BankDepositor(Long 		depositorId, 
						 String		depositorName, 
						 Date	 	depositorDateDeposit,
						 Integer	depositorAmountDeposit,
						 Integer	depositorAmountPlusDeposit,
						 Integer	depositorAmountMinusDeposit,
						 Date		depositorDateReturnDeposit,
						 Integer	depositorMarkReturnDeposit) {
		this.depositorId = depositorId;
		this.depositorName = depositorName;

		this.depositorDateDeposit = depositorDateDeposit;
		this.depositorAmountDeposit = depositorAmountDeposit;
		this.depositorAmountPlusDeposit = depositorAmountPlusDeposit;
		this.depositorAmountMinusDeposit = depositorAmountMinusDeposit;
		this.depositorDateReturnDeposit = depositorDateReturnDeposit;
		this.depositorMarkReturnDeposit = depositorMarkReturnDeposit; 
	}
	//--- get/set depositorId (идентификатор вкладчика)
	public Long getDepositorId(){
		return this.depositorId;
	}
	public void setDepositorId(Long depositorId){
		this.depositorId = depositorId;
	}
	//--- get/set depositorName (Ф.И.О. вкладчика)
	public String getDepositorName(){
		return this.depositorName;
	}
	public void setDepositorName(String depositorName){
		this.depositorName = depositorName;
	}

	//--- get/set depositorDateDeposit (Дата вклада)
	public Date getDepositorDateDeposit(){
		return this.depositorDateDeposit;
	}
	public void setDepositorDateDeposit(Date depositorDateDeposit){
		this.depositorDateDeposit = depositorDateDeposit;
	}
	//--- get/set depositorAmountDeposit (сумма вклада)
	public int getDepositorAmountDeposit(){
		return this.depositorAmountDeposit;
	}
	public void setDepositorAmountDeposit(int depositorAmountDeposit){
		this.depositorAmountDeposit = depositorAmountDeposit;
	}
	//--- get/set depositorAmountPlusDeposit (сумма процентов)
	public int getDepositorAmountPlusDeposit(){
		return this.depositorAmountPlusDeposit;
	}
	public void setDepositorAmountPlusDeposit(int depositorAmountPlusDeposit){
		this.depositorAmountPlusDeposit = depositorAmountPlusDeposit;
	}
	//--- get/set depositorAmountMinusDeposit (выдача)
	public int getDepositorAmountMinusDeposit(){
		return this.depositorAmountMinusDeposit;
	}
	public void setDepositorAmountMinusDeposit(int depositorAmountMinusDeposit){
		this.depositorAmountMinusDeposit = depositorAmountMinusDeposit;
	}
	//--- get/set depositorDateReturnDeposit (Дата предполагаемого закрытия вклада)
	public Date getDepositorDateReturnDeposit(){
		return this.depositorDateReturnDeposit;
	}
	public void setDepositorDateReturnDeposit(Date depositorDateReturnDeposit){
		this.depositorDateReturnDeposit = depositorDateReturnDeposit;
	}
	//--- get/set depositorMarkReturnDeposit (отметка о закрытии вклада)
	public int getDepositorMarkReturnDeposit(){
		return this.depositorMarkReturnDeposit;
	}
	public void setDepositorMarkReturnDeposit(int depositorMarkReturnDeposit){
		this.depositorMarkReturnDeposit = depositorMarkReturnDeposit;
	}

	//--- get/set many-to-one link
	public BankDeposit getDeposit(){
		return this.bankDeposit;
	}

	public void setDeposit(BankDeposit bankDeposit){
		this.bankDeposit = bankDeposit;
	}

	public void setToDeposit(BankDeposit deposit){
		this.setDeposit(deposit);
		deposit.getDepositors().add(this);
	}

	@Override
    public String toString() {
		String date, dateReturn;
		if (depositorDateDeposit!=null) date = dateFormat.format(depositorDateDeposit);
		else date = "null";
		if (depositorDateReturnDeposit!=null) dateReturn = dateFormat.format(depositorDateReturnDeposit);
		else dateReturn = "null";
        return "BankDepositor: { depositorId="+depositorId+
		", depositorName="+ depositorName+
		", depositorDateDeposit="+ date +
		", depositorAmountDeposit="+ depositorAmountDeposit+
		", depositorAmountPlusDeposit="+ depositorAmountPlusDeposit+
		", depositorAmountMinusDeposit="+ depositorAmountMinusDeposit+
		", depositorDateReturnDeposit="+ dateReturn +
		", depositorMarkReturnDeposit="+ depositorMarkReturnDeposit  + '}';
    }
}
