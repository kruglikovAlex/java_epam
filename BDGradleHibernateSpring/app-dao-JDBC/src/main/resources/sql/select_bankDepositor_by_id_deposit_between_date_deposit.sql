select depositorId,
		depositorName,
		depositorDateDeposit,
		depositorAmountDeposit,
		depositorAmountPlusDeposit,
		depositorAmountMinusDeposit,
		depositorDateReturnDeposit,
		depositorMarkReturnDeposit
from BANKDEPOSITOR where depositorIdDeposit=? and depositorDateDeposit between ? and ?
