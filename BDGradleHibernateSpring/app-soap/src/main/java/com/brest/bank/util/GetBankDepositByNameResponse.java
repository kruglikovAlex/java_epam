//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.11 at 05:41:16 PM MSK 
//


package com.brest.bank.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bankDeposit" type="{http://bank.brest.com/soap}bankDeposit"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bankDeposit"
})
@XmlRootElement(name = "getBankDepositByNameResponse")
public class GetBankDepositByNameResponse {

    @XmlElement(required = true)
    protected BankDeposit bankDeposit;

    /**
     * Gets the value of the bankDeposit property.
     * 
     * @return
     *     possible object is
     *     {@link BankDeposit }
     *     
     */
    public BankDeposit getBankDeposit() {
        return bankDeposit;
    }

    /**
     * Sets the value of the bankDeposit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BankDeposit }
     *     
     */
    public void setBankDeposit(BankDeposit value) {
        this.bankDeposit = value;
    }

}
