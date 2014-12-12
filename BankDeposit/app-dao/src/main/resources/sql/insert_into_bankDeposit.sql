insert into BANKDEPOSIT (depositId,depositName,depositMinTerm,
							depositMinAmount,depositCurrency,
							depositInterestRate,depositAddConditions) 
values (:depositId,:depositName,:depositMinTerm,:depositMinAmount,
		:depositCurrency,:depositInterestRate,:depositAddConditions)