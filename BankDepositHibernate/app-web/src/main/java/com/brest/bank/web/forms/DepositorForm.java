package com.brest.bank.web.forms;

/**
 * Created by alexander on 20.3.15.
 */
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class DepositorForm {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Long        depositId;

    private Long 		depositorId;
    private String 		depositorName;
    private String      depositorDateDeposit;
    private Integer		depositorAmountDeposit;
    private Integer		depositorAmountPlusDeposit;
    private Integer		depositorAmountMinusDeposit;
    private String		depositorDateReturnDeposit;
    private Integer		depositorMarkReturnDeposit;

    public void initFormDepositor(BankDepositor depositor){
        this.depositorId = depositor.getDepositorId();
        this.depositorName = depositor.getDepositorName();
        this.depositorDateDeposit = dateFormat.format(depositor.getDepositorDateDeposit());
        this.depositorAmountDeposit = depositor.getDepositorAmountDeposit();
        this.depositorAmountPlusDeposit = depositor.getDepositorAmountPlusDeposit();
        this.depositorAmountMinusDeposit = depositor.getDepositorAmountMinusDeposit();
        this.depositorDateReturnDeposit = dateFormat.format(depositor.getDepositorDateReturnDeposit());
        this.depositorMarkReturnDeposit = depositor.getDepositorMarkReturnDeposit();
    }

    /**
     *   getters & setters
     */
    public void setDepositorId(Long depositorId) {
        this.depositorId = depositorId;
    }

    public Long getDepositorId() {
        return depositorId;
    }

    public void setDepositorName(String depositorName) {
        this.depositorName = depositorName;
    }

    public String getDepositorName() {
        return depositorName;
    }

    public void setDepositorDateDeposit(String depositorDateDeposit) {
        this.depositorDateDeposit = depositorDateDeposit;
    }

    public String getDepositorDateDeposit(){
        return depositorDateDeposit;
    }

    public void setDepositorAmountDeposit(Integer depositorAmountDeposit){
        this.depositorAmountDeposit = depositorAmountDeposit;
    }

    public Integer getDepositorAmountDeposit() {
        return depositorAmountDeposit;
    }

    public void setDepositorAmountPlusDeposit(Integer depositorAmountPlusDeposit) {
        this.depositorAmountPlusDeposit = depositorAmountPlusDeposit;
    }

    public Integer getDepositorAmountPlusDeposit() {
        return depositorAmountPlusDeposit;
    }

    public void setDepositorAmountMinusDeposit(Integer depositorAmountMinusDeposit) {
        this.depositorAmountMinusDeposit = depositorAmountMinusDeposit;
    }

    public Integer getDepositorAmountMinusDeposit() {
        return depositorAmountMinusDeposit;
    }

    public void setDepositorDateReturnDeposit(String depositorDateReturnDeposit) {
        this.depositorDateReturnDeposit = depositorDateReturnDeposit;
    }

    public String getDepositorDateReturnDeposit() {
        return depositorDateReturnDeposit;
    }

    public void setDepositorMarkReturnDeposit(Integer depositorMarkReturnDeposit) {
        this.depositorMarkReturnDeposit = depositorMarkReturnDeposit;
    }

    public Integer getDepositorMarkReturnDeposit() {
        return depositorMarkReturnDeposit;
    }

    public void setDepositId(Long depositId) {
        this.depositId = depositId;
    }

    public Long getDepositId() {
        return depositId;
    }
}
