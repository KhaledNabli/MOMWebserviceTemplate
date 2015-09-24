<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:MOMHelper="urn:HelperClass" version="1.0">
  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes" standalone="yes"/>

  <!-- - - - - - - - - - - - - - - - - - - - - - - - -->
  <!--                                               -->
  <!-- IMPORTANT: DO NOT CHANGE THIS FILE IN ANY WAY -->
  <!--                                               -->
  <!-- - - - - - - - - - - - - - - - - - - - - - - - -->

  <xsl:include href="CIGetNamedValue.xslt"/>



  <xsl:variable name="folderSeparator" select="string('\')"/>

  <!-- by default ignore all elements -->
  <xsl:template match="*"/>

  <!-- drop through listTreatmentsOutput -->
  <xsl:template match="listTreatmentsOutput">
    <xsl:apply-templates/>
  </xsl:template>

  <!-- should never see this, but drop through if do -->
  <xsl:template match="Envelope">
    <xsl:apply-templates/>
  </xsl:template>

  <!-- should never see this, but drop through if do -->
  <xsl:template match="Body">
    <xsl:apply-templates/>
  </xsl:template>


  <!-- There should be just one root of the tree, but we require a dummy node
		under that. This node is disregarded by the web service pop up, but allows us
		to present a meaningful name at the root of the tree -->
  <xsl:param name="treeName">
    <xsl:call-template name="GetLocalString">
      <xsl:with-param name="sourceid" select="'Integration_MA_SelectionData_Label_Treatments'"></xsl:with-param>
    </xsl:call-template>
  </xsl:param>

  <xsl:template match="TreeNodes">
    <xsl:element name="TreeNodes">
      <xsl:element name="TreeNode">

        <!-- generate the general purpose attributes for this TreeNode dependent on position in the tree -->
        <xsl:call-template name="WriteTreeAttributes">
          <xsl:with-param name="Name" select="$treeName"></xsl:with-param>
        </xsl:call-template>

        <xsl:apply-templates/>
      </xsl:element>
    </xsl:element>
  </xsl:template>

  <!-- process TreeNode -->
  <xsl:template match="TreeNode">
    <xsl:element name="TreeNode">

      <!-- generate the attributes for this TreeNode dependent on -->
      <xsl:call-template name="WriteTreeAttributes" />

      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>

  <xsl:template name="WriteTreeAttributes">
    <!-- If param Name is defined, it defines the node Text value, otherwise taken
			from the value of the Name element -->
    <xsl:param name="Name"/>

    <!-- The NodeData attribute is always an empty string -->
    <xsl:attribute name="NodeData">
      <xsl:value-of select="''"/>
    </xsl:attribute>

    <!-- If param Name is defined, it defines the node Text value, otherwise taken
			from the value of the Name element -->
    <xsl:choose>
      <xsl:when test="$Name">
        <xsl:attribute name="Text" >
          <xsl:value-of select="$Name"/>
        </xsl:attribute>
      </xsl:when>
      <xsl:otherwise>
        <xsl:attribute name="Text" >
          <xsl:value-of select="Name"/>
        </xsl:attribute>
      </xsl:otherwise>
    </xsl:choose>


    <!-- SelectionMode dependent on this leaf (treatment) or tree node (part of path) -->
    <xsl:call-template name="WriteSelectionModeAttribute"></xsl:call-template>

    <!-- PostbackType always 'None' -->
    <!-- <xsl:attribute name="PostbackType">None</xsl:attribute>  -->

  </xsl:template>

  <!-- The SelectionMode attribute is MultiSelect if we are at a leaf node (treatment) 
		or None for an intermedidate TreeNode (part or the path). 				-->
  <xsl:template name="WriteSelectionModeAttribute">
    <xsl:choose>
      <!-- If we are at a leaf (treatment) the NamedStringList is defined. -->
      <xsl:when test="NamedStringList">
        <xsl:attribute name="Selectionmode">MultiSelect</xsl:attribute>

        <xsl:call-template name="WriteTooltipAttribute"/>

      </xsl:when>
      <xsl:otherwise>
        <xsl:attribute name="Selectionmode">None</xsl:attribute>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- the Tooltip attribute is written only for a leaf node (treatment) -->
  <xsl:template name="WriteTooltipAttribute">
    <!-- If we are at a leaf (treatment) the NamedStringList is defined. -->
    <xsl:if test="NamedStringList">

      <xsl:variable name="folderTxt">
        <xsl:call-template name="GetLocalString">
          <xsl:with-param name="sourceid" select="'Integration_MA_SelectionData_TT_Folder'"></xsl:with-param>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="folderVal">
        <xsl:call-template name="getNamedValueString">
          <xsl:with-param name="name" select="'TreatmentFolder'"></xsl:with-param>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="codeTxt">
        <xsl:call-template name="GetLocalString">
          <xsl:with-param name="sourceid" select="'Integration_MA_SelectionData_TT_Code'"/>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="codeVal">
        <xsl:call-template name="getNamedValueString">
          <xsl:with-param name="name" select="'TreatmentCode'"/>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="descriptionTxt">
        <xsl:call-template name="GetLocalString">
          <xsl:with-param name="sourceid" select="'Integration_MA_SelectionData_TT_Description'"/>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="descriptionVal">
        <xsl:call-template name="getNamedValueString">
          <xsl:with-param name="name" select="'TreatmentDescription'"></xsl:with-param>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="referenceTxt">
        <xsl:call-template name="GetLocalString">
          <xsl:with-param name="sourceid" select="'Integration_MA_SelectionData_TT_Reference'"/>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="referenceVal">
        <xsl:call-template name="getNamedValueString">
          <!-- S0858311					<xsl:with-param name="name" select="'TreatmentURL'"/>
 -->
          <xsl:with-param name="name" select="'TreatmentURLDescription'"/>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="referenceURLTxt">
        <xsl:call-template name="GetLocalString">
          <xsl:with-param name="sourceid" select="'Integration_MA_SelectionData_TT_ReferenceURL'"/>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="referenceURLVal">
        <xsl:call-template name="getNamedValueString">
          <xsl:with-param name="name" select="'TreatmentURL'"/>
        </xsl:call-template>
      </xsl:variable>


      <xsl:variable name="cr">
      </xsl:variable>
      <xsl:variable name="tooltip">
        <xsl:value-of select="concat($folderTxt,		':',$folderVal,			'&#xD;',
											$codeTxt,			':',$codeVal,			'&#xD;',
											$descriptionTxt,	':',$descriptionVal,	'&#xD;',
											$referenceTxt,		':',$referenceVal,		'&#xD;',
											$referenceURLTxt,	':',$referenceURLVal)"/>
      </xsl:variable>

      <xsl:attribute name="Tooltip">
        <xsl:value-of select="$tooltip"/>;
      </xsl:attribute>

    </xsl:if>
  </xsl:template>

  <!-- this template refuses to work in an included file -->

  <xsl:key name="getLocalString" match="translation_strings/string" use="@id"/>
  <xsl:template name="GetLocalString">
    <xsl:param name="sourceid"/>

    <!--<xsl:for-each select="document('CILocalStrings.xml')">-->
    <xsl:variable name="retrievedValue">
      <!--<xsl:for-each select="key('getLocalString', $sourceid)">
          <xsl:value-of select="@translation"/>
        </xsl:for-each>-->
      <xsl:value-of select="MOMHelper:getResString($sourceid)" />
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="$retrievedValue != ''">
        <xsl:value-of select="$retrievedValue"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$sourceid"/>
      </xsl:otherwise>
    </xsl:choose>
    <!--</xsl:for-each>-->

  </xsl:template>




</xsl:stylesheet>
