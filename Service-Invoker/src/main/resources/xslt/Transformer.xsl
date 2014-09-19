<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output indent="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns="urn:hl7-org:v3">
            <soapenv:Header/>
            <soapenv:Body>
                <xsl:copy-of select="*"/>
            </soapenv:Body>
        </soapenv:Envelope>
    </xsl:template>

</xsl:stylesheet>