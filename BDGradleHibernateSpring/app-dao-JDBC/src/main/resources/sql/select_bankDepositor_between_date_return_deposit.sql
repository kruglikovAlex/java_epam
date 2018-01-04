select depositorId,
		depositorName,
		depositorDateDeposit,
		depositorAmountDeposit,
		depositorAmountPlusDeposit,
		depositorAmountMinusDeposit,
		depositorDateReturnDeposit,
		depositorMarkReturnDeposit
from BANKDEPOSITOR where depositorDateReturnDeposit between ? and ?
