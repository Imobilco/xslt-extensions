<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:imob-nse="xalan://ru.imobilco.extensions.xalan.NodeSetExtensions"
	>

	<xsl:output method="html"/>

	<xsl:template match="/document">
		<xsl:value-of select="concat('String output:', imob-nse:toString(div), '&#x0a;')"/>
		<xsl:value-of select="concat('JS output:', imob-nse:toJavaScriptString(div), '&#x0a;')" disable-output-escaping="yes"/>
		<xsl:value-of select="concat('JS output string:', imob-nse:toJavaScriptString('hel&quot;lo'), '&#x0a;')"/>
		<xsl:value-of select="concat('JS output number:', imob-nse:toJavaScriptString(number(1)), '&#x0a;')"/>

		<xsl:value-of select="concat('JSON:', imob-nse:toJSON(div/p), '&#x0a;')" disable-output-escaping="yes"/>

	</xsl:template>
</xsl:stylesheet>