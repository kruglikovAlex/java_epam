select depositId,
	depositName,
	depositMinTerm,
	depositMinAmount,
	depositCurrency,
	depositInterestRate,
	depositAddConditions
from BANKDEPOSIT where depositMinTerm between ? and ?