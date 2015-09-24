<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes" omit-xml-declaration="yes" standalone="yes"/>

	<!-- - - - - - - - - - - - - - - - - - - - - - - - -->
	<!--                                               -->
	<!-- IMPORTANT: DO NOT CHANGE THIS FILE IN ANY WAY -->
	<!--                                               -->
	<!-- - - - - - - - - - - - - - - - - - - - - - - - -->

	<!-- template to recover the value of a (string) named value keyed on value. Structure is as follows
		<TreeNode>
		<NamedStringList>
		<NamedValue>
		<Name>TreatmentName</Name>
		<Value>Kens Treatment Nov 17 second one</Value>
		Expect to use this key from current location TreeNode
	-->
	<xsl:template name="getNamedValueString">
		<xsl:param name="name"/>
		<xsl:for-each select="NamedStringList/NamedValue">
			<xsl:if test="$name=Name">
				<xsl:value-of select="Value"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>		
	
	
</xsl:stylesheet>
