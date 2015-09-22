<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/bankDeposits">
        <html>
            <body>
                Deposits list:<br/>{
                <xsl:for-each select="bankDeposit">
                    <p>
                        Bank Deposit:<br/>
                        ID: <B><xsl:value-of select="depositId"/></B><br/>
                        NAME: <B><xsl:value-of select="depositName"/></B><br/>
                        TERM: <B><xsl:value-of select="depositMinTerm"/></B><br/>
                        AMOUNT: <B><xsl:value-of select="depositMinAmount"/></B><br/>
                        CURRENCY: <B><xsl:value-of select="depositCurrency"/></B><br/>
                        CONDITIONS: <B><i><xsl:value-of select="depositAddConditions"/></i></B><br/>
                        Depositors list:{<br/>
                        <xsl:for-each select="bankDepositors/bankDepositor">
                            ID: <B><xsl:value-of select="depositorId"/></B><br/>
                            NAME: <B><xsl:value-of select="depositorName"/></B><br/>
                            DATE DEPOSIT: <B><xsl:value-of select="depositorDateDeposit"/></B><br/>
                            AMOUNT: <B><xsl:value-of select="depositorAmounDeposit"/></B><br/>
                            AMOUNT PLUS: <B><xsl:value-of select="depositorAmountPlusDeposit"/></B><br/>
                            AMOUNT MINUS: <B><xsl:value-of select="depositorAmountMinusDeposit"/></B><br/>
                            DATE RETURN DEPOSIT: <B><xsl:value-of select="depositorDateReturnDeposit"/></B><br/>
                            RETURN DEPOSIT:
                            <B>
                                <xsl:variable name="mark"
                                              select="depositorMarkReturnDeposit"/>
                                <xsl:choose>
                                    <xsl:when test="$mark= 0">
                                        NO
                                    </xsl:when>
                                    <xsl:otherwise>
                                        YES
                                    </xsl:otherwise>
                                </xsl:choose>
                            </B><br/>
                        </xsl:for-each>
                    }</p>
                </xsl:for-each>
                }
            </body>
        </html>

    </xsl:template>

</xsl:stylesheet>