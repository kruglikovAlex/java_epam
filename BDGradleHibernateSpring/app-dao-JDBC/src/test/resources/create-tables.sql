CREATE TABLE BANKDEPOSIT (
depositId BIGINT IDENTITY,
depositName VARCHAR(255) NULL,
depositMinTerm INT,
depositMinAmount INT,
depositCurrency VARCHAR(3) NULL,
depositInterestRate INT,
depositAddConditions VARCHAR(255) NULL
);

CREATE TABLE BANKDEPOSITOR (
depositorId BIGINT IDENTITY,
depositorName VARCHAR(255) NULL,
depositorDateDeposit DATE NULL,
depositorAmountDeposit INT,
depositorAmountPlusDeposit INT,
depositorAmountMinusDeposit INT,
depositorDateReturnDeposit DATE NULL,
depositorMarkReturnDeposit INT,
depositId INT
);