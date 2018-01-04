select deposit.*, depositor.*
from BANKDEPOSIT as deposit
	left join
	(select dep.depositId as depId,
			sum(dep.depositorAmountDeposit) as sumAmount,
			sum(dep.depositorAmountPlusDeposit) as sumPlusAmount,
			sum(dep.depositorAmountMinusDeposit) as sumMinusAmount,
			count(dep.depositorId) as numDepositors
	 from BANKDEPOSITOR as dep
	 group by depId) as depositor
	 on deposit.depositId = depositor.depId
where depositor.depositorMarkReturn = ?