select depositorId,
		depositorName,
		depositorIdDeposit,
		depositorDateDeposit,
		depositorAmountDeposit,
		depositorAmountPlusDeposit,
		depositorAmountMinusDeposit,
		depositorDateReturnDeposit,
		depositorMarkReturnDeposit
from BANKDEPOSITOR where depositorId=?
