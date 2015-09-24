package com.sas.mom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.sas.mom.Entity.MarketingActivity;
import com.sas.mom.Entity.Offer;
import com.sas.mom.Entity.VarianceDetail;
import com.sas.mom.Entity.VarianceHead;

@WebService(targetNamespace = "http://mom.sas.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, parameterStyle = ParameterStyle.WRAPPED)
public class WebserviceProvider {

	@WebMethod(operationName = "EchoService")
	@WebResult(partName = "parameters", name = "EchoResult")
	public String echo(
			@WebParam(partName = "parameters", name = "EchoInput", targetNamespace = "http://mom.sas.com/") String input) {
		String result = "Webservice Provider echo:" + input;
		System.out.println(result);
		return result;
	}

	@WebMethod(operationName = "InvokeService")
	@WebResult(partName = "parameters", name = "ServiceResult")
	public String invokeService(
			@WebParam(partName = "parameters", name = "ServiceCommand", targetNamespace = "http://mom.sas.com/") String cmd,
			@WebParam(partName = "parameters", name = "ServiceInput", targetNamespace = "http://mom.sas.com/") String input) {
		String returnValue = "";

		if (cmd.equalsIgnoreCase("<COMMAND>ECHO</COMMAND>")) {
			returnValue = "Command: ECHO \t Input :" + input;
		} else if (cmd.equalsIgnoreCase("<COMMAND>INIT_VARIANCE_TABLE</COMMAND>")) {
			returnValue = initVarianceTable(input);
		} else if (cmd.equalsIgnoreCase("<COMMAND>LIST_OFFER</COMMAND>")) {
			returnValue = getOfferList();
		} else if (cmd.equalsIgnoreCase("<COMMAND>LIST_CAMPAIGN_RABATTS</COMMAND>")) {
			returnValue = getRabattList();
		} else {
			returnValue = "Command: " + cmd + " Input: " + input;
		}

		System.out.println("Invoke Service returning with: \n" + cmd + " Input: " + input);
		return returnValue;
	}

	private String getOfferList() {
		String resultXml = "<TreeNodes><TreeNode NodeData=\"1\" Text=\"Angebote\" Selectionmode=\"None\">";
		String activityID = "";
		try {
			activityID = getLastCode();
			List<Offer> offerList = getOffersForMarketingActivityCD(activityID);
			for (Offer offer : offerList) {
				resultXml += "\n<TreeNode NodeData=\"" + offer.getId() + "\" Text=\"" + offer.getName()
						+ "\" Selectionmode=\"SingleSelect\" Tooltip=\"" + offer.toString() + "\"/>";
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
		}

		resultXml += "\n</TreeNode></TreeNodes>";

		return resultXml;
	}

	private String getRabattList() {

		String resultXml = "<TreeNodes>"
				+ "<TreeNode NodeData=\"1\" Text=\"RabattID Suchergebnisse\" Selectionmode=\"None\">"
				+ "<TreeNode NodeData=\"2\" Text=\"PB KPSM7 BSKT10 Warenkorb\" Selectionmode=\"None\">"
				+ "<TreeNode NodeData=\"3\" Text=\"112376\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Online\" />"
				+ "<TreeNode NodeData=\"4\" Text=\"112377\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Kaufleute\"/>"
				+ "<TreeNode NodeData=\"5\" Text=\"112378\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Go\"/>"
				+ "<TreeNode NodeData=\"6\" Text=\"112379\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Dortmund\"/>"
				+ "<TreeNode NodeData=\"7\" Text=\"112380\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: PETZ\"/>"
				+ "<TreeNode NodeData=\"8\" Text=\"112381\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Filialen\"/>"
				+ "</TreeNode>"

				+ "<TreeNode NodeData=\"11\" Text=\"PB KPSM7 CAT7K KategorieWurst\" Selectionmode=\"None\">"
				+ "<TreeNode NodeData=\"12\" Text=\"198376\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Online\"/>"
				+ "<TreeNode NodeData=\"13\" Text=\"198377\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Kaufleute\"/>"
				+ "<TreeNode NodeData=\"14\" Text=\"198378\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Go\"/>"
				+ "<TreeNode NodeData=\"15\" Text=\"198379\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Dortmund\"/>"
				+ "<TreeNode NodeData=\"16\" Text=\"198380\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: PETZ\"/>"
				+ "<TreeNode NodeData=\"17\" Text=\"198381\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Filialen\"/>"
				+ "</TreeNode>"

				+ "<TreeNode NodeData=\"21\" Text=\"PB KPSM7 CAT7K KategorieFood\" Selectionmode=\"None\">"
				+ "<TreeNode NodeData=\"22\" Text=\"562376\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Online\"/>"
				+ "<TreeNode NodeData=\"23\" Text=\"562377\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Kaufleute\"/>"
				+ "<TreeNode NodeData=\"24\" Text=\"562378\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Go\"/>"
				+ "<TreeNode NodeData=\"25\" Text=\"562379\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Dortmund\"/>"
				+ "<TreeNode NodeData=\"26\" Text=\"562380\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: PETZ\"/>"
				+ "<TreeNode NodeData=\"27\" Text=\"562381\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Filialen\"/>"
				+ "</TreeNode>"

				+ "<TreeNode NodeData=\"31\" Text=\"kPSM3 5fach Punkte auf Ihren Einkauf!\" Selectionmode=\"None\">"
				+ "<TreeNode NodeData=\"32\" Text=\"141394\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Online\"/>"
				+ "<TreeNode NodeData=\"33\" Text=\"141395\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Kaufleute\"/>"
				+ "<TreeNode NodeData=\"34\" Text=\"141396\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Go\"/>"
				+ "<TreeNode NodeData=\"35\" Text=\"141397\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Dortmund\"/>"
				+ "<TreeNode NodeData=\"36\" Text=\"141398\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: PETZ\"/>"
				+ "<TreeNode NodeData=\"37\" Text=\"141399\" Selectionmode=\"MultiSelect\" Tooltip=\"VB: Rewe Filialen\"/>"
				+ "</TreeNode>" + "</TreeNode>" + "</TreeNodes>";
		return resultXml;
	}

	/**
	 * 
	 * @param inputXML
	 * @return outputXML
	 */
	private String initVarianceTable(String input) {

		String inputXML = "<MARKETINGACTIVITY>" + input + "</MARKETINGACTIVITY>";
		System.out.println(input);
		String outputXML = "";

		// Parse XML
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document xmlDoc = null;
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = null;

		try {
			builder = factory.newDocumentBuilder();
			xmlDoc = builder.parse(new InputSource(new StringReader(inputXML)));
			Element docEle = xmlDoc.getDocumentElement();

			int countVariancesTab1 = docEle.getElementsByTagName("REWE_TARGETGROUP").getLength();

			Node reweVariantList = docEle.getElementsByTagName("MARKETINGACTIVITY_REWE_MCB_VOLUME").item(0);
			Node startDttmNode = docEle.getElementsByTagName("PLANNEDSTARTDATE").item(0);
			Node endDttmNode = docEle.getElementsByTagName("PLANNEDENDDATE").item(0);
			Node activityCdNode = docEle.getElementsByTagName("ACTIVITYNUMBER").item(0);
			if (!activityCdNode.getTextContent().isEmpty()) {
				Node matrixLinkNode = docEle.getElementsByTagName("LINK_TO_MATRIX").item(0);
				matrixLinkNode.setTextContent("<a target=\"_blank\" href=\"http://skopockmt:9980/REWEServices/?campCD="
						+ activityCdNode.getTextContent().trim() + "\">Öffnen</a>");
				
				//FIXME  save Activity Number for other Calls
				PrintWriter out = new PrintWriter("C:\\Temp\\mom_ws_cache_cd.txt");
				out.print(activityCdNode.getTextContent());
				out.close();
				
			}

			for (int i = 0; i < countVariancesTab1; i++) {
				Element reweTGElement = (Element) docEle.getElementsByTagName("REWE_TARGETGROUP").item(i);
				Node tgEntryID = reweTGElement.getElementsByTagName("PK").item(0);

				// if this entry was not saved before - remove the key
				if (tgEntryID.getTextContent().contains("-")) {
					tgEntryID.setTextContent("");
				}

				int countOffers = Integer.parseInt(reweTGElement.getElementsByTagName("NOOFOFFER").item(0)
						.getTextContent());
				String variantNm = reweTGElement.getElementsByTagName("VARIANCE").item(0).getTextContent();
				String targetGroup = reweTGElement.getElementsByTagName("TARGETGROUP").item(0).getTextContent();
				String targetStrategie = reweTGElement.getElementsByTagName("TARGETSTRATEGY").item(0).getTextContent();

				int existingEntryCnt = 0;
				// Search for existing entries and Complete EAN Code:
				for (int k = 0; k < reweVariantList.getChildNodes().getLength(); k++) {
					Node n = reweVariantList.getChildNodes().item(k);
					String varNm = "";
					String varTg = "";
					String varTs = "";
					String varOffer = "";
					String varRabatt = "";
					Node eanNode = null;

					for (int s = 0; s < n.getChildNodes().getLength(); s++) {

						if (n.getChildNodes().item(s).getNodeName().equalsIgnoreCase("VARIANCE")) {
							varNm = n.getChildNodes().item(s).getTextContent();
						} else if (n.getChildNodes().item(s).getNodeName().equalsIgnoreCase("TARGETSEGMENT")) {
							varTg = n.getChildNodes().item(s).getTextContent();
						} else if (n.getChildNodes().item(s).getNodeName().equalsIgnoreCase("TARGETSTRATEGY")) {
							varTs = n.getChildNodes().item(s).getTextContent();
						} else if (n.getChildNodes().item(s).getNodeName().equalsIgnoreCase("OFFER")) {
							varOffer = n.getChildNodes().item(s).getTextContent();
						}

						else if (n.getChildNodes().item(s).getNodeName().equalsIgnoreCase("RABATTID")) {
							varRabatt = n.getChildNodes().item(s).getTextContent();
						}

						else if (n.getChildNodes().item(s).getNodeName().equalsIgnoreCase("EANCODE")) {
							eanNode = n.getChildNodes().item(s);
						}

					}

					if (varNm.equals(variantNm) && varTg.equals(targetGroup) && varTs.equals(targetStrategie))
						existingEntryCnt++;

					if (!varOffer.isEmpty() && !varRabatt.isEmpty())
						eanNode.setTextContent("14" + ((int) Math.ceil(Math.random() * 100000) + 99999) + "000000"
								+ ((int) Math.ceil(Math.random() * 100000) + 99999));
				}

				if ((countOffers - existingEntryCnt) < 1)
					continue;

				for (int j = 0; j < (countOffers - existingEntryCnt); j++) {
					Element newVariant = xmlDoc.createElement("REWE_MCB_VOLUME");
					Element nVariantPK = xmlDoc.createElement("PK");
					Element nVariantNM = xmlDoc.createElement("VARIANCE");
					Element nVariantOffer = xmlDoc.createElement("OFFER");
					Element nVariantStartDttm = xmlDoc.createElement("VALIDFROM");
					Element nVariantEndDttm = xmlDoc.createElement("VALIDTILL");
					Element nVariantRabattID = xmlDoc.createElement("RABATTID");
					Element nVariantEAN = xmlDoc.createElement("EANCODE");
					Element nVariantTG = xmlDoc.createElement("TARGETSEGMENT");
					Element nVariantTGTxt = xmlDoc.createElement("TARGETSEGMENT_TXT");
					Element nVariantTS = xmlDoc.createElement("TARGETSTRATEGY");

					nVariantNM.setTextContent(variantNm);
					nVariantTG.setTextContent(targetGroup);
					nVariantTS.setTextContent(targetStrategie);
					nVariantTGTxt.setTextContent(variantNm + "_" + targetGroup + " (" + targetStrategie + ")");

					if (startDttmNode != null) {
						for (int h = 0; h < startDttmNode.getChildNodes().getLength(); h++) {
							nVariantStartDttm.appendChild(startDttmNode.getChildNodes().item(h).cloneNode(true));
						}
					}

					if (endDttmNode != null) {
						for (int h = 0; h < endDttmNode.getChildNodes().getLength(); h++) {
							nVariantEndDttm.appendChild(endDttmNode.getChildNodes().item(h).cloneNode(true));
						}
					}

					newVariant.appendChild(nVariantPK);
					newVariant.appendChild(nVariantNM);
					newVariant.appendChild(nVariantOffer);
					newVariant.appendChild(nVariantStartDttm);
					newVariant.appendChild(nVariantEndDttm);
					newVariant.appendChild(nVariantRabattID);
					newVariant.appendChild(nVariantEAN);
					newVariant.appendChild(nVariantTG);
					newVariant.appendChild(nVariantTS);
					newVariant.appendChild(nVariantTGTxt);
					reweVariantList.appendChild(newVariant);
				}

			}

			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(docEle), new StreamResult(writer));
			outputXML = writer.getBuffer().toString();

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}

		return outputXML;
	}

	public static MarketingActivity getActivityDetails(String activityCd) throws SQLException {
		// SELECT "FACETTYPEID", "facettypename" FROM dbo.ALOM_FACETTYPE where
		// facettypename LIKE 'REWE%'
		// 179287256712928 REWE_CAMPINFO

		// SELECT * FROM dbo.ALFCT_REWE_CAMPINFO WHERE PK IN (SELECT FACETID
		// FROM dbo.ALOM_ASSOENTITYFACET WHERE FACETTYPEID = '179287256712928'
		// AND ENTITYTYPEID = '230396323080180' AND ENTITYID = (SELECT
		// MARKETINGACTIVITYID FROM dbo.ALENT_MARKETINGACTIVITY WHERE
		// ACTIVITYNUMBER = '--- CODE HERE ---' ))

		MarketingActivity activity = new MarketingActivity();
		Statement queryStatement1 = DBConnectionProvider.connect().createStatement();
		Statement queryStatement2 = DBConnectionProvider.connect().createStatement();

		String queryTemplate1 = "select * FROM dbo.ALENT_MARKETINGACTIVITY WHERE ACTIVITYLEVEL = '2' AND ACTIVITYNUMBER = '"
				+ activityCd + "'";
		String queryTemplate2 = "SELECT * FROM dbo.ALFCT_REWE_CAMPINFO WHERE PK IN (SELECT FACETID FROM dbo.ALOM_ASSOENTITYFACET WHERE FACETTYPEID = '179287256712928' AND ENTITYTYPEID = '230396323080180' AND ENTITYID = (SELECT MARKETINGACTIVITYID FROM dbo.ALENT_MARKETINGACTIVITY WHERE ACTIVITYNUMBER = '"
				+ activityCd + "' ))";

		ResultSet activityResult = queryStatement1.executeQuery(queryTemplate1);
		ResultSet campInfoResult = queryStatement2.executeQuery(queryTemplate2);

		if (activityResult.next()) {
			activity.setId(activityResult.getString("MARKETINGACTIVITYID"));
			activity.setCd(activityResult.getString("ACTIVITYNUMBER"));
			activity.setName(activityResult.getString("NAME"));
			activity.setPath(activityResult.getString("ACTIVITYPATH").replace("$ALP$", "  \\  "));
			activity.setStartDate(activityResult.getString("PLANNEDSTARTDATE"));
			activity.setEndDate(activityResult.getString("PLANNEDENDDATE"));
			activity.setLastUpdate(activityResult.getString("LASTUPDATEDON"));
			List<String> vbs = new ArrayList<String>();

			if (campInfoResult.next()) {
				String vbTxt = campInfoResult.getString("REWE_VB");
				if (vbTxt != null && vbTxt.length() > 0) {
					String[] vbArry = vbTxt.replace("$AL$", ";;").split(";;");
					for (int i = 0; i < vbArry.length; i++) {
						if (!vbArry[i].trim().isEmpty())
							vbs.add(vbArry[i]);
					}
				}
			}
			activity.setVertriebsBereiche(vbs);
		}

		return activity;
	}

	/**
	 * 
	 * @param campID
	 * @return
	 */
	public static List<Offer> getOffersForMarketingActivityCD(String activityCd) throws SQLException {
		List<Offer> offerList = new ArrayList<Offer>();
		Statement statement = DBConnectionProvider.connect().createStatement();

		// Get EntityTypeID
		// Query: SELECT "ENTITYTYPEID" FROM "dbo"."ALOM_ENTITYTYPE" where
		// entitytypename = 'MARKETINGACTIVITY'
		// EntityTypeID for MarketingActivity = 230396323080180#
		// EntityTypeID for Offer = 251131843865063

		String queryTemplate = "SELECT * FROM dbo.ALENT_OFFER WHERE OFFERID IN (SELECT CONTAINSID FROM dbo.ALOM_ASSEMBLY WHERE CONTAINERTYPEID = '230396323080180' AND CONTAINSTYPEID = '251131843865063' AND CONTAINERID = (SELECT MARKETINGACTIVITYID FROM dbo.ALENT_MARKETINGACTIVITY WHERE ACTIVITYNUMBER = '"
				+ activityCd + "')) ORDER BY OFFERNAME ASC";

		ResultSet assignedOffersResult = statement.executeQuery(queryTemplate);

		while (assignedOffersResult.next()) {
			Offer tmpOffer = new Offer();
			tmpOffer.setId(assignedOffersResult.getString("OFFERID"));
			tmpOffer.setCode(assignedOffersResult.getString("OFFERCODE"));
			tmpOffer.setName(assignedOffersResult.getString("OFFERNAME"));
			tmpOffer.setDesc(assignedOffersResult.getString("OFFERDESCRIPTION"));
			offerList.add(tmpOffer);
		}

		statement.close();
		DBConnectionProvider.disconnect();
		return offerList;
	}

	public static List<VarianceHead> getVarianceList(String activityCd) throws SQLException {
		// SELECT "ENTITYTYPEID" FROM "dbo"."ALOM_ENTITYTYPE" where
		// entitytypename = 'MARKETINGACTIVITY'
		// ENTITYTYPEID = 230396323080180

		// SELECT "FACETTYPEID", "facettypename" FROM dbo.ALOM_FACETTYPE where
		// facettypename LIKE 'REWE%'
		// 93070811606145 REWE_TARGETGROUP

		// SELECT * FROM dbo.ALFCT_REWE_TARGETGROUP WHERE PK IN (SELECT FACETID
		// FROM dbo.ALOM_ASSOENTITYFACET WHERE FACETTYPEID = '93070811606145'
		// AND ENTITYTYPEID = '230396323080180' AND ENTITYID = (SELECT
		// MARKETINGACTIVITYID FROM dbo.ALENT_MARKETINGACTIVITY WHERE
		// ACTIVITYNUMBER = '--- CODE HERE ---' ))

		List<VarianceHead> varianceHeader = new ArrayList<VarianceHead>();
		Statement statement = DBConnectionProvider.connect().createStatement();

		String queryTemplate = "SELECT * FROM dbo.ALFCT_REWE_TARGETGROUP WHERE PK IN (SELECT FACETID FROM dbo.ALOM_ASSOENTITYFACET WHERE FACETTYPEID = '93070811606145' AND ENTITYTYPEID = '230396323080180' AND ENTITYID = (SELECT MARKETINGACTIVITYID FROM dbo.ALENT_MARKETINGACTIVITY WHERE ACTIVITYNUMBER = '"
				+ activityCd + "' )) ORDER BY VARIANCE ASC";

		ResultSet assignedVariancesResult = statement.executeQuery(queryTemplate);

		while (assignedVariancesResult.next()) {
			VarianceHead varianceHead = new VarianceHead();
			varianceHead.setPk(assignedVariancesResult.getString("PK"));
			varianceHead.setName(assignedVariancesResult.getString("VARIANCE"));
			varianceHead.setStrategie(assignedVariancesResult.getString("TARGETSTRATEGY"));
			varianceHead.setTargetGroup(assignedVariancesResult.getString("TARGETGROUP"));
			varianceHead.setOfferCount(parseInt(assignedVariancesResult.getString("NOOFOFFER")));
			varianceHead.setPlannedVolume(parseInt(assignedVariancesResult.getString("PLANNEDVOLUME")));
			varianceHead.setActualVolume(parseInt(assignedVariancesResult.getString("ACTUALVOLUME")));
			varianceHead.setDesc(assignedVariancesResult.getString("DESCRIPTION"));
			varianceHeader.add(varianceHead);
		}

		return varianceHeader;
	}

	public static List<VarianceDetail> getVarianceDetails(String activityCd) throws SQLException {
		// Get EntityTypeID for OFFERS
		// Query: SELECT "ENTITYTYPEID" FROM "dbo"."ALOM_ENTITYTYPE" where
		// entitytypename = 'OFFER'
		// ENTITYTYPEID = 251131843865063

		// SELECT "ENTITYTYPEID" FROM "dbo"."ALOM_ENTITYTYPE" where
		// entitytypename = 'MARKETINGACTIVITY'
		// ENTITYTYPEID = 230396323080180

		// SELECT "FACETTYPEID", "facettypename" FROM dbo.ALOM_FACETTYPE where
		// facettypename LIKE 'REWE%'
		// 80599709762575 REWE_MCB_VOLUME

		// SELECT * FROM dbo.ALFCT_REWE_MCB_VOLUME WHERE PK IN (SELECT FACETID
		// FROM dbo.ALOM_ASSOENTITYFACET WHERE FACETTYPEID = '80599709762575'
		// AND ENTITYTYPEID = '230396323080180' AND ENTITYID = (SELECT
		// MARKETINGACTIVITYID FROM dbo.ALENT_MARKETINGACTIVITY WHERE
		// ACTIVITYNUMBER = '--- CODE HERE ---' ))

		List<VarianceDetail> varianceDetails = new ArrayList<VarianceDetail>();
		Statement statement = DBConnectionProvider.connect().createStatement();

		String queryTemplate = "SELECT * FROM dbo.ALFCT_REWE_MCB_VOLUME WHERE PK IN (SELECT FACETID FROM dbo.ALOM_ASSOENTITYFACET WHERE FACETTYPEID = '80599709762575' AND ENTITYTYPEID = '230396323080180' AND ENTITYID = (SELECT MARKETINGACTIVITYID FROM dbo.ALENT_MARKETINGACTIVITY WHERE ACTIVITYNUMBER = '"
				+ activityCd + "' )) ORDER BY VARIANCE ASC";

		ResultSet assignedVariancesResult = statement.executeQuery(queryTemplate);

		while (assignedVariancesResult.next()) {
			VarianceDetail variance = new VarianceDetail();
			variance.setPk(assignedVariancesResult.getString("PK"));
			variance.setName(assignedVariancesResult.getString("VARIANCE"));
			variance.setOfferNm(assignedVariancesResult.getString("OFFER").replace("$AL$", "").trim());
			variance.setTargetGroup(assignedVariancesResult.getString("TARGETSEGMENT"));
			String rabattIdsTxt = assignedVariancesResult.getString("RABATTID");
			List<String> rabattIdList = new ArrayList<String>();
			if (rabattIdsTxt != null && rabattIdsTxt.length() > 4) {
				String[] rabattIds = rabattIdsTxt.substring(4).replace("$AL$", ";;").split(";;");
				for (int i = 0; i < rabattIds.length; i++) {
					rabattIdList.add(rabattIds[i]);
				}
			}
			variance.setRabattIds(rabattIdList);
			variance.setValidFrom(assignedVariancesResult.getString("VALIDFROM").substring(0, 10));
			variance.setValidTo(assignedVariancesResult.getString("VALIDTILL").substring(0, 10));
			variance.setEan(assignedVariancesResult.getString("EANCODE"));
			varianceDetails.add(variance);
		}

		statement.close();
		DBConnectionProvider.disconnect();
		return varianceDetails;
	}

	public static int parseInt(String val) {
		int result = 0;

		try {
			result = Integer.parseInt(val);
		} catch (Exception e) {
			result = 0;
		}

		return result;
	}

	public static String parseXmlTag(String xmlInput, String tagNm) {
		String startTag = "<" + tagNm + ">";
		String endTag = "</" + tagNm + ">";
		int startPos = xmlInput.indexOf(startTag);
		int endPos = xmlInput.indexOf(endTag);
		System.out.println("Start Tag: " + startTag);
		System.out.println("Start Pos: " + startPos);

		startPos += startTag.length();

		return xmlInput.substring(startPos, endPos);
	}
	
	public static String getLastCode() throws FileNotFoundException{
		String output = new Scanner(new File("C:\\Temp\\mom_ws_cache_cd.txt")).useDelimiter("\\Z").next();
		return output;
	}
	

	public static List<VarianceDetail> getVarianceDetails(String activityCd, String varianceName, String targetGroup, String strategy) throws SQLException {
		// Get EntityTypeID for OFFERS
		// Query: SELECT "ENTITYTYPEID" FROM "dbo"."ALOM_ENTITYTYPE" where
		// entitytypename = 'OFFER'
		// ENTITYTYPEID = 251131843865063

		// SELECT "ENTITYTYPEID" FROM "dbo"."ALOM_ENTITYTYPE" where
		// entitytypename = 'MARKETINGACTIVITY'
		// ENTITYTYPEID = 230396323080180

		// SELECT "FACETTYPEID", "facettypename" FROM dbo.ALOM_FACETTYPE where
		// facettypename LIKE 'REWE%'
		// 80599709762575 REWE_MCB_VOLUME

		// SELECT * FROM dbo.ALFCT_REWE_MCB_VOLUME WHERE PK IN (SELECT FACETID
		// FROM dbo.ALOM_ASSOENTITYFACET WHERE FACETTYPEID = '80599709762575'
		// AND ENTITYTYPEID = '230396323080180' AND ENTITYID = (SELECT
		// MARKETINGACTIVITYID FROM dbo.ALENT_MARKETINGACTIVITY WHERE
		// ACTIVITYNUMBER = '--- CODE HERE ---' ))

		List<VarianceDetail> varianceDetails = new ArrayList<VarianceDetail>();
		Statement statement = DBConnectionProvider.connect().createStatement();

		String queryTemplate = "SELECT * FROM dbo.ALFCT_REWE_MCB_VOLUME WHERE PK IN (SELECT FACETID FROM dbo.ALOM_ASSOENTITYFACET WHERE FACETTYPEID = '80599709762575' AND ENTITYTYPEID = '230396323080180' AND ENTITYID = (SELECT MARKETINGACTIVITYID FROM dbo.ALENT_MARKETINGACTIVITY WHERE ACTIVITYNUMBER = '"
				+ activityCd + "' )) and VARIANCE = '" + varianceName + "' and TARGETSEGMENT = '" + targetGroup + "' and TARGETSTRATEGY = '" + strategy + "' ORDER BY VALIDFROM ASC";

		ResultSet assignedVariancesResult = statement.executeQuery(queryTemplate);

		while (assignedVariancesResult.next()) {
			VarianceDetail variance = new VarianceDetail();
			variance.setPk(assignedVariancesResult.getString("PK"));
			variance.setName(assignedVariancesResult.getString("VARIANCE"));
			variance.setOfferNm(assignedVariancesResult.getString("OFFER").replace("$AL$", "").trim());
			variance.setTargetGroup(assignedVariancesResult.getString("TARGETSEGMENT"));
			variance.setTargetstrategy(assignedVariancesResult.getString("TARGETSTRATEGY"));
			String rabattIdsTxt = assignedVariancesResult.getString("RABATTID");
			List<String> rabattIdList = new ArrayList<String>();
			if (rabattIdsTxt != null && rabattIdsTxt.length() > 4) {
				String[] rabattIds = rabattIdsTxt.substring(4).replace("$AL$", ";;").split(";;");
				for (int i = 0; i < rabattIds.length; i++) {
					rabattIdList.add(rabattIds[i]);
				}
			}
			variance.setRabattIds(rabattIdList);
			variance.setValidFrom(assignedVariancesResult.getString("VALIDFROM").substring(0, 10));
			variance.setValidTo(assignedVariancesResult.getString("VALIDTILL").substring(0, 10));
			variance.setEan(assignedVariancesResult.getString("EANCODE"));
			varianceDetails.add(variance);
		}

		statement.close();
		DBConnectionProvider.disconnect();
		return varianceDetails;
	}

	public static List<VarianceDetail> getVarianceByCriteria(String activityCd, String strategy, String targetGroup,
			String variance) throws SQLException {
		List<VarianceHead> tmpHeads = getVarianceList(activityCd);
		List<VarianceDetail> resultSet = new ArrayList<VarianceDetail>();

		for (VarianceHead vh : tmpHeads) {
			if (vh.getStrategie().trim().equalsIgnoreCase(strategy.trim()) && vh.getTargetGroup().trim().equalsIgnoreCase(targetGroup.trim())
					&& vh.getName().trim().equalsIgnoreCase(variance.trim())) {
				List<VarianceDetail> tmpResultSet = getVarianceDetails(activityCd, vh.getName(), vh.getTargetGroup(),  vh.getStrategie());
				for (VarianceDetail tmpVd : tmpResultSet) {
					tmpVd.setHead(vh);
				}

				resultSet.addAll(tmpResultSet);
			}
		}

		return resultSet;
	}
}
