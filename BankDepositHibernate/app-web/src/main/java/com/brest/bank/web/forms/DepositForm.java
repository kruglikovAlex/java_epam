package com.brest.bank.web.forms;

/**
 * Created by alexander on 19.3.15.
 */

import java.util.Collection;
import com.brest.bank.domain.BankDeposit;

public class DepositForm {

    private Long        depositId;
    private String 	    depositName;
    private Integer 	depositMinTerm;
    private Integer 	depositMinAmount;
    private String 	    depositCurrency;
    private Integer 	depositInterestRate;
    private String 	    depositAddConditions;

    private Collection  depositors;

    public void initFormDeposit(BankDeposit deposit){
        this.depositId = deposit.getDepositId();
        this.depositName = deposit.getDepositName();
        this.depositMinTerm = deposit.getDepositMinTerm();
        this.depositMinAmount = deposit.getDepositMinAmount();
        this.depositCurrency = deposit.getDepositCurrency();
        this.depositInterestRate = deposit.getDepositInterestRate();
        this.depositAddConditions = deposit.getDepositAddConditions();
    }

    /**
     *   getters & setters
     */
    public void setDepositId(Long depositId){
        this.depositId = depositId;
    }

    public Long getDepositId(){
        return depositId;
    }

    public void setDepositName(String depositName){
        this.depositName = depositName;
    }

    public String getDepositName(){
        return depositName;
    }

    public void setDepositMinTerm(Integer depositMinTerm){
        this.depositMinTerm = depositMinTerm;
    }

    public Integer getDepositMinTerm(){
        return depositMinTerm;
    }

    public void setDepositMinAmount(Integer depositMinAmount){
        this.depositMinAmount = depositMinAmount;
    }

    public Integer getDepositMinAmount(){
        return depositMinAmount;
    }

    public void setDepositCurrency(String depositCurrency){
        this.depositCurrency = depositCurrency;
    }

    public String getDepositCurrency(){
        return depositCurrency;
    }

    public void setDepositInterestRate(Integer depositInterestRate){
        this.depositInterestRate = depositInterestRate;
    }

    public Integer getDepositInterestRate(){
        return depositInterestRate;
    }

    public void setDepositAddConditions(String depositAddConditions){
        this.depositAddConditions = depositAddConditions;
    }

    public String getDepositAddConditions(){
        return depositAddConditions;
    }

    public void setDepositors(Collection depositors){
        this.depositors = depositors;
    }

    public Collection getDepositors(){
        return depositors;
    }
}
