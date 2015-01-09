package com.brest.bank.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BankDepositor {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Long 		depositorId; 				//�������������
	private String 		depositorName;				//��� ���������
	private Long		depositorIdDeposit;			//������������� ������
	private Date 		depositorDateDeposit;		//���� ������
	private Integer		depositorAmountDeposit;		//����� ������
	private Integer		depositorAmountPlusDeposit;	//����� ���������� ������
	private Integer		depositorAmountMinusDeposit;//����� �������� ������
	private Date		depositorDateReturnDeposit;//���� �������� ������
	private Integer		depositorMarkReturnDeposit;	//������� � �������� ������
	
	//--- конструктор без параметров
	public BankDepositor(){
	}
	//--- конструктор с параметрами
	public BankDepositor(Long 		depositorId, 
						 String		depositorName, 
						 Long		depositorIdDeposit, 
						 Date	 	depositorDateDeposit, 
						 Integer	depositorAmountDeposit,
						 Integer	depositorAmountPlusDeposit,
						 Integer	depositorAmountMinusDeposit,
						 Date		depositorDateReturnDeposit,
						 Integer	depositorMarkReturnDeposit) {
		this.depositorId = depositorId;
		this.depositorName = depositorName;
		this.depositorIdDeposit = depositorIdDeposit;
		this.depositorDateDeposit = depositorDateDeposit;
		this.depositorAmountDeposit = depositorAmountDeposit;
		this.depositorAmountPlusDeposit = depositorAmountPlusDeposit;
		this.depositorAmountMinusDeposit = depositorAmountMinusDeposit;
		this.depositorDateReturnDeposit = depositorDateReturnDeposit;
		this.depositorMarkReturnDeposit = depositorMarkReturnDeposit; 
	}
	//--- get/set depositorId (������������� ���������)
	public Long getDepositorId(){
		return this.depositorId;
	}
	public void setDepositorId(Long depositorId){
		this.depositorId = depositorId;
	}
	//--- get/set depositorName (��� ���������)
	public String getDepositorName(){
		return this.depositorName;
	}
	public void setDepositorName(String depositorName){
		this.depositorName = depositorName;
	}
	//--- get/set depositorIdDeposit (������������� ������)
	public Long getDepositorIdDeposit(){
		return this.depositorIdDeposit;
	}
	public void setDepositorIdDeposit(Long depositorIdDeposit){
		this.depositorIdDeposit = depositorIdDeposit;
	}
	//--- get/set depositorDateDeposit (���� ������)
	public Date getDepositorDateDeposit(){
		return this.depositorDateDeposit;
	}
	public void setDepositorDateDeposit(Date depositorDateDeposit){
		this.depositorDateDeposit = depositorDateDeposit;
	}
	//--- get/set depositorAmountDeposit (����� ������)
	public int getDepositorAmountDeposit(){
		return this.depositorAmountDeposit;
	}
	public void setDepositorAmountDeposit(int depositorAmountDeposit){
		this.depositorAmountDeposit = depositorAmountDeposit;
	}
	//--- get/set depositorAmountPlusDeposit (����� ���������� ������)
	public int getDepositorAmountPlusDeposit(){
		return this.depositorAmountPlusDeposit;
	}
	public void setDepositorAmountPlusDeposit(int depositorAmountPlusDeposit){
		this.depositorAmountPlusDeposit = depositorAmountPlusDeposit;
	}
	//--- get/set depositorAmountMinusDeposit (����� �������� ������)
	public int getDepositorAmountMinusDeposit(){
		return this.depositorAmountMinusDeposit;
	}
	public void setDepositorAmountMinusDeposit(int depositorAmountMinusDeposit){
		this.depositorAmountMinusDeposit = depositorAmountMinusDeposit;
	}
	//--- get/set depositorDateReturnDeposit (���� �������� ������)
	public Date getDepositorDateReturnDeposit(){
		return this.depositorDateReturnDeposit;
	}
	public void setDepositorDateReturnDeposit(Date depositorDateReturnDeposit){
		this.depositorDateReturnDeposit = depositorDateReturnDeposit;
	}
	//--- get/set depositorMarkReturnDeposit (������� � �������� ������)
	//--- ���� (depositorAmountDeposit-depositorAmountMinusDeposit)=0, ��
	//--- depositorMarkReturnDeposit=1, ����� 0
	public int getDepositorMarkReturnDeposit(){
		return this.depositorMarkReturnDeposit;
	}
	public void setDepositorMarkReturnDeposit(int depositorMarkReturnDeposit){
		this.depositorMarkReturnDeposit = depositorMarkReturnDeposit;
	}
	public void setDepositorMarkReturnDeposit(){
		if ((depositorAmountDeposit-depositorAmountMinusDeposit)==0){
			this.depositorMarkReturnDeposit = 1;
		} else this.depositorMarkReturnDeposit = 0;
	}

	@Override
    public String toString() {
        return "BankDepositor: { depositorId ="+depositorId+
		", depositorName ="+ depositorName+
		", depositorIdDeposit ="+ depositorIdDeposit+
		", depositorDateDeposit ="+ dateFormat.format(depositorDateDeposit)+
		", depositorAmountDeposit ="+ depositorAmountDeposit+
		", depositorAmountPlusDeposit ="+ depositorAmountPlusDeposit+
		", depositorAmountMinusDeposit ="+ depositorAmountMinusDeposit+
		", depositorDateReturnDeposit ="+ dateFormat.format(depositorDateReturnDeposit)+
		", depositorMarkReturnDeposit ="+ depositorMarkReturnDeposit  + '}';
    }
}
