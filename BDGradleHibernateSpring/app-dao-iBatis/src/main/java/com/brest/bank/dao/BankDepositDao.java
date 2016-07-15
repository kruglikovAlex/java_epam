package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BankDepositDao {

   /**
    * Get all Bank Deposits
    *
    * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
    */
    List<BankDeposit> getBankDepositsCriteria();

   /**
    * Get Bank Deposit by ID
    *
    * @param id Long - id of the Bank Deposit to return
    * @return BankDeposit with the specified id from the database
    */
    BankDeposit getBankDepositByIdCriteria(Long id);

   /**
    * Get Bank Deposit by NAME
    *
    * @param depositName String - name of the Bank Deposit to return
    * @return BankDeposit with the specified depositName from the database
    */
    BankDeposit getBankDepositByNameCriteria(String depositName);

   /**
    * Get Bank Deposit by Currency
    *
    * @param currency String - currency of the Bank Deposits to return
    * @return List<BankDeposit> - a list containing all of the Bank Deposits with the specified
    * currency in the database
    */
    List<BankDeposit> getBankDepositsByCurrencyCriteria(String currency);

   /**
    * Get Bank Deposits by INTEREST RATE
    *
    * @param rate Integer - interest rate of the Bank Deposits to return
    * @return List<BankDeposit>  - a list containing all of the Bank Deposits with the specified
    * interest rate in the database
    */
    List<BankDeposit> getBankDepositsByInterestRateCriteria(Integer rate);

   /**
    * Get Bank Deposits from-to MIN TERM values
    *
    * @param fromTerm Integer - start value of the min term (count month)
    * @param toTerm Integer - end value of the min term (count month)
    * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
    * with the specified task`s min term of deposit
    */
    List<BankDeposit> getBankDepositsFromToMinTermCriteria(Integer fromTerm,
                                                           Integer toTerm);

   /**
    * Get Bank Deposits from-to INTEREST RATE values
    *
    * @param startRate Integer - start value of the interest rate (0% < startRate <= 100%)
    * @param endRate Integer - end value of the interest rate (0% < endRate <= 100%)
    * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
    * with the specified task`s in of deterest rate of deposit
    */
    List<BankDeposit> getBankDepositsFromToInterestRateCriteria(Integer startRate,
                                                                Integer endRate);

   /**
    * Get Bank Deposits from-to Date Deposit values
    *
    * @param startDate Date - start value of the date deposit (startDate < endDate)
    * @param endDate Date - end value of the date deposit (endDate > startDate)
    * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
    * with the specified task`s date of deposit
    */
    List<BankDeposit> getBankDepositsFromToDateDeposit(Date startDate,
                                                       Date endDate);

   /**
    * Get Bank Deposits from-to Date Return Deposit values
    *
    * @param startDate Date - start value of the date return deposit (startDate < endDate)
    * @param endDate Date - end value of the date return deposit (endDate > startDate)
    * @return List<BankDeposit> - a list containing all of the Bank Deposits in the database
    * with the specified task`s date return of deposit
    */
    List<BankDeposit> getBankDepositsFromToDateReturnDeposit(Date startDate,
                                                             Date endDate);

   /**
    * Get Bank Deposit by NAME with depositors
    *
    * @param name String - name of the Bank Deposit to return
    * @return Map - a bank deposit with a report on all relevant
    * bank depositors
    */
    Map getBankDepositByNameWithDepositors(String name);

   /**
    * Get Bank Deposit by NAME with depositors from-to Date Deposit values
    *
    * @param name String - name of the Bank Deposit to return
    * @param startDate Date - start value of the date deposit (startDate < endDate)
    * @param endDate Date - end value of the date deposit (endDate > startDate)
    * @return Map - a bank deposit with a report on all relevant
    * bank depositors with the specified task`s date of deposit
    */
    Map getBankDepositByNameFromToDateDepositWithDepositors(String name,
                                                            Date startDate,
                                                            Date endDate);

   /**
    * Get Bank Deposit by NAME with depositor from-to Date Return Deposit values
    *
    * @param name String - name of the Bank Deposit to return
    * @param startDate Date - start value of the date return deposit (startDate < endDate)
    * @param endDate Date - end value of the date return deposit (endDate > startDate)
    * @return Map - a bank deposit with a report on all relevant
    * bank depositors with the specified task`s date return of deposit
    */
    Map getBankDepositByNameFromToDateReturnDepositWithDepositors(String name,
                                                                  Date startDate,
                                                                  Date endDate);

   /**
    * Get Bank Deposit by ID with depositors
    *
    * @param id Long - depositId of the Bank Deposit to return
    * @return Map - a bank deposit with a report on all relevant
    * bank depositors
    */
    Map getBankDepositByIdWithDepositors(Long id);

   /**
    * Get Bank Deposits by ID with depositors from-to Date Deposit values
    *
    * @param id Long - depositId of the Bank Deposit to return
    * @param startDate Date - start value of the date deposit (startDate < endDate)
    * @param endDate Date - end value of the date deposit (endDate > startDate)
    * @return Map - a bank deposit with a report on all relevant
    * bank depositors with the specified task`s date of deposit
    */
    Map getBankDepositByIdFromToDateDepositWithDepositors(Long id,
                                                          Date startDate,
                                                          Date endDate);

   /**
    * Get Bank Deposit by ID with depositors from-to Date Return Deposit values
    *
    * @param id Long - depositId of the Bank Deposit to return
    * @param startDate Date - start value of the date return deposit (startDate < endDate)
    * @param endDate Date - end value of the date return deposit (startDate < endDate)
    * @return Map - a bank deposit with a report on all relevant
    * bank depositors with the specified task`s date return deposit
    */
    Map getBankDepositByIdFromToDateReturnDepositWithDepositors(Long id,
                                                                Date startDate,
                                                                Date endDate);

    /**
     * Get Bank Deposits by Min term with depositors
     *
     * @param term Integer - Min term of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    List<Map> getBankDepositsByTermWithDepositors(Integer term);

    /**
     * Get Bank Deposits by Min Amount with depositors
     *
     * @param amount Integer - Min amount of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    List<Map> getBankDepositsByAmountWithDepositors(Integer amount);

    /**
     * Get Bank Deposits by Interest Rate with depositors
     *
     * @param rate Integer - Interest Rate of the Bank Deposit to return
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    List<Map> getBankDepositsByRateWithDepositors(Integer rate);

    /**
     * Get Bank Deposit by Depositor ID with depositors
     *
     * @param id Long - depositorId of the Bank Depositor
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    //Map getBankDepositByDepositorIdWithDepositors(Long id);

    /**
     * Get Bank Deposit by Depositor Name with depositors
     *
     * @param name String - depositorName of the Bank Depositor
     * @return Map - a bank deposit with a report on all relevant
     * bank depositors
     */
    //Map getBankDepositByDepositorNameWithDepositors(String name);

    /**
     * Get Bank Deposits by from-to Depositor Amount with depositors
     *
     * @param from Integer - Amount of the Bank Depositor
     * @param to Integer - Amount of the Bank Depositor
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    //List<Map> getBankDepositsByDepositorAmountWithDepositors(Integer from, Integer to);

    /**
     * Get Bank Deposit with depositors
     *
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    //List<Map> getBankDepositsWithDepositors();

   /**
    * Get Bank Deposit from-to Date Deposit with depositors
    *
    * @param startDate Date - start value of the date deposit (startDate < endDate)
    * @param endDate Date - end value of the date deposit (startDate < endDate)
    * @return List<Map> a list of all bank deposits with a report on all relevant
    * bank depositors with the specified task`s date deposit
    */
    //List<Map> getBankDepositsFromToDateDepositWithDepositors(Date startDate,
    //                                                         Date endDate);

   /**
    * Get Bank Deposit from-to Date Return Deposit with depositors
    *
    * @param startDate Date - start value of the date return deposit (startDate < endDate)
    * @param endDate Date - end value of the date return deposit (startDate < endDate)
    * @return List<Map> a list of all bank deposits with a report on all relevant
    * bank depositors with the specified task`s date return deposit
    */
    //List<Map> getBankDepositsFromToDateReturnDepositWithDepositors(Date startDate,
    //                                                               Date endDate);

   /**
    * Get Bank Deposit by Currency with depositors
    *
    * @param currency String - Currency of the Bank Deposit to return
    * @return List<Map> - a list of all bank deposits with a report on all relevant
    * bank depositors
    */
    //List<Map> getBankDepositsByCurrencyWithDepositors(String currency);

   /**
    * Get Bank Deposit from-to Date Deposit by Currency with depositors
    *
    * @param currency String - Currency of the Bank Deposit to return
    * @param startDate Date - start value of the date deposit (startDate < endDate)
    * @param endDate Date - end value of the date deposit (startDate < endDate)
    * @return List<Map> a list of all bank deposits with a report on all relevant
    * bank depositors with the specified task`s date deposit
    */
    //List<Map> getBankDepositsByCurrencyFromToDateDepositWithDepositors(String currency,
    //                                                                   Date startDate,
    //                                                                   Date endDate);

   /**
    * Get Bank Deposit from-to Date Return Deposit by Currency with depositors
    *
    * @param currency String - Currency of the Bank Deposit to return
    * @param startDate Date - start value of the date deposit (startDate < endDate)
    * @param endDate Date - end value of the date deposit (startDate < endDate)
    * @return List<Map> a list of all bank deposits with a report on all relevant
    * bank depositors with the specified task`s date return deposit
    */
    //List<Map> getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(String currency,
    //                                                                         Date startDate,
    //                                                                         Date endDate);

    /**
     * Get Bank Deposits by Depositor mark return with depositors
     *
     * @param markReturn Integer - Mark Return of the Bank Depositor
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    //List<Map> getBankDepositsByDepositorMarkReturnWithDepositors(Integer markReturn);

    /**
     * Get Bank Deposits by Variant Args with depositors
     *
     * @param args Object - Variant number of arguments
     * @return List<Map> - a list of all bank deposits with a report on all relevant
     * bank depositors
     */
    //List<Map> getBankDepositsByVarArgs(Object... args);

   /**
    * Adding Bank Deposit
    *
    * @param deposit BankDeposit - Bank Deposit to be inserted to the database
    */
    void addBankDeposit(BankDeposit deposit);

   /**
    * Updating Bank Deposit
    *
    * @param deposit BankDeposit - Bank Deposit to be stored in the database
    */
    void updateBankDeposit(BankDeposit deposit);

   /**
    * Deleting Bank Deposit by ID
    *
    * @param id Long - id of the Bank Deposit to be removed
    */
    void deleteBankDeposit(Long id);

    /**
     *
     * @return
     */
    Integer rowCount();
}
