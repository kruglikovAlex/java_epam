INSERT INTO BANKDEPOSIT (depositId,depositName,depositMinTerm,depositMinAmount,
							depositCurrency,depositInterestRate,depositAddConditions) 
VALUES (1,'depositName1',12,100,'usd',4,'condition1');
INSERT INTO BANKDEPOSIT (depositId,depositName,depositMinTerm,depositMinAmount,
						depositCurrency,depositInterestRate,depositAddConditions) 
VALUES (2,'depositName2',12,100,'eur',5,'condition2');
INSERT INTO BANKDEPOSIT (depositId,depositName,depositMinTerm,depositMinAmount,
						depositCurrency,depositInterestRate,depositAddConditions)
VALUES (3,'depositName3',12,100,'eur',5,'condition3');

INSERT INTO BANKDEPOSITOR (depositorId,depositorName,depositorIdDeposit,
						depositorDateDeposit,depositorAmountDeposit,
						depositorAmountPlusDeposit,depositorAmountMinusDeposit,
						depositorDateReturnDeposit,depositorMarkReturnDeposit)
VALUES (1,'depositorName1',1,'2014-12-01',10000,1000,100,'2014-12-02',0);
INSERT INTO BANKDEPOSITOR (depositorId,depositorName,depositorIdDeposit,
						depositorDateDeposit,depositorAmountDeposit,
						depositorAmountPlusDeposit,depositorAmountMinusDeposit,
						depositorDateReturnDeposit,depositorMarkReturnDeposit)
VALUES (2,'depositorName2',1,'2014-12-02',1000,100,1000,'2014-12-03',0);
INSERT INTO BANKDEPOSITOR (depositorId,depositorName,depositorIdDeposit,
						depositorDateDeposit,depositorAmountDeposit,
						depositorAmountPlusDeposit,depositorAmountMinusDeposit,
						depositorDateReturnDeposit,depositorMarkReturnDeposit)
VALUES (3,'depositorName2',2,'2014-12-03',10000,100,1000,'2014-12-04',0);
INSERT INTO BANKDEPOSITOR (depositorId,depositorName,depositorIdDeposit,
						depositorDateDeposit,depositorAmountDeposit,
						depositorAmountPlusDeposit,depositorAmountMinusDeposit,
						depositorDateReturnDeposit,depositorMarkReturnDeposit)
VALUES (4,'depositorName2',2,'2014-12-03',10000,100,1000,'2014-12-04',0);
INSERT INTO BANKDEPOSITOR (depositorId,depositorName,depositorIdDeposit,
						depositorDateDeposit,depositorAmountDeposit,
						depositorAmountPlusDeposit,depositorAmountMinusDeposit,
						depositorDateReturnDeposit,depositorMarkReturnDeposit)
VALUES (5,'depositorName2',3,'2014-12-03',10000,100,1000,'2014-12-05',0);