<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/">
    <xsl:template match="/">
        <xsl:apply-templates select="soap-env:Envelope/soap-env:Body/*"/>
    </xsl:template>
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>