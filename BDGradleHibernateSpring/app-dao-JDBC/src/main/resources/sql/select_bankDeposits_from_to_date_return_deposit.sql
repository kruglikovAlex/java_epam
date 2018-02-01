select deposit.*, depositor.*
from BANKDEPOSIT as deposit
	left join
	(select dep.depositId as depId
	 from BANKDEPOSITOR as dep
	 where dep.depositorDateReturnDeposit between ? and ?
	 group by depId) as depositor
	 on deposit.depositId = depositor.depId
where deposit.depositId = depositor.depId