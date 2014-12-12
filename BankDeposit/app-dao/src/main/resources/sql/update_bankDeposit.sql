update BANKDEPOSIT set depositName=:depositName, 
			depositMinTerm=:depositMinTerm, 
			depositMinAmount=:depositMinAmount, 
			depositCurrency=:depositCurrency,
			depositInterestRate=:depositInterestRate,
			depositAddConditions=:depositAddConditions 
where depositId=:depositId