select NULL as depositorId,
       	NUll as depositorName,
       	NULL as depositorDateDeposit,
       	SUM(depositorAmountDeposit) as depositorAmountDeposit,
       	SUM(depositorAmountPlusDeposit) as depositorAmountPlusDeposit,
       	SUM(depositorAmountMinusDeposit) as depositorAmountMinusDeposit,
       	NULL as depositorDateReturnDeposit,
       	NULL as depositorMarkReturnDeposit
from BANKDEPOSITOR where depositorIdDeposit=? and depositorDateDeposit between ? and ?
