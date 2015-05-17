package com.brest.bank.web.forms;

/**
 * Created by alexander on 19.3.15.
 */
import java.util.Collection;

public class MainFrameForm {
    private int year;
    private Long depositId;
    private Collection deposits;
    private Collection depositors;

    /**
     *   getters & setters
     */
    public void setYear(int year){
        this.year = year;
    }

    public int getYear(){
        return year;
    }

    public void setDepositId(Long depositId){
        this.depositId = depositId;
    }

    public Long getDepositId(){
        return depositId;
    }

    public void setDeposits(Collection deposits){
        this.deposits = deposits;
    }

    public Collection getDeposits(){
        return deposits;
    }

    public void setDepositors(Collection depositors) {
        this.depositors = depositors;
    }

    public Collection getDepositors() {
        return depositors;
    }
}
