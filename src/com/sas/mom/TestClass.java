package com.sas.mom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.sas.mom.Entity.Offer;

public class TestClass {

	public static void main(String[] args) throws FileNotFoundException, TransformerException, SQLException {

		String inputXML = "<MARKETINGACTIVITY>" + getInputXML() + "</MARKETINGACTIVITY>";
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
			int countVariancesTab2 = docEle.getElementsByTagName("REWE_MCB_VOLUME").getLength();

			Node reweVariantList = docEle.getElementsByTagName("MARKETINGACTIVITY_REWE_MCB_VOLUME").item(0);
			Node startDttmNode = docEle.getElementsByTagName("PLANNEDSTARTDATE").item(0);
			Node endDttmNode = docEle.getElementsByTagName("PLANNEDENDDATE").item(0);

			if (countVariancesTab2 == 0) {
				// Init Variance Table
				System.out.println(countVariancesTab1 + " Varianten werden erstellt");

				for (int i = 0; i < countVariancesTab1; i++) {
					Element reweTGElement = (Element) docEle.getElementsByTagName("REWE_TARGETGROUP").item(i);

					int countOffers = Integer.parseInt(reweTGElement.getElementsByTagName("NOOFOFFER").item(0)
							.getTextContent());
					String variantNm = reweTGElement.getElementsByTagName("VARIANCE").item(0).getTextContent();
					String targetGroup = reweTGElement.getElementsByTagName("TARGETGROUP").item(0).getTextContent();

					for (int j = 0; j < countOffers; j++) {
						Element newVariant = xmlDoc.createElement("REWE_MCB_VOLUME");
						Element nVariantPK = xmlDoc.createElement("PK");
						Element nVariantNM = xmlDoc.createElement("VARIANCE");
						Element nVariantOffer = xmlDoc.createElement("OFFER");
						Element nVariantStartDttm = xmlDoc.createElement("VALIDFROM");
						Element nVariantEndDttm = xmlDoc.createElement("VALIDTILL");
						Element nVariantRabattID = xmlDoc.createElement("RABATTID");
						Element nVariantEAN = xmlDoc.createElement("EANCODE");
						Element nVariantTG = xmlDoc.createElement("TARGETSEGMENT");

						nVariantNM.setTextContent(variantNm);
						nVariantTG.setTextContent(targetGroup);
						nVariantOffer.setTextContent("Angebot " + (j + 1));

						if (startDttmNode != null) {
							System.out.println("start datum gefunden");
							for (int h = 0; h < startDttmNode.getChildNodes().getLength(); h++) {
								nVariantStartDttm.appendChild(startDttmNode.getChildNodes().item(h).cloneNode(true));
							}
						}

						if (endDttmNode != null) {
							for (int h = 0; h < endDttmNode.getChildNodes().getLength(); h++) {
								nVariantEndDttm.appendChild(endDttmNode.getChildNodes().item(h).cloneNode(true));
							}
						}


						String rabattIDs = "$AL$";
						for (int k = 1; k < 7; k++) {
							rabattIDs += ((int) Math.ceil(Math.random() * 100000) + 99999) + "$AL$";
						}

						nVariantRabattID.setTextContent(rabattIDs);

						newVariant.appendChild(nVariantPK);
						newVariant.appendChild(nVariantNM);
						newVariant.appendChild(nVariantOffer);
						newVariant.appendChild(nVariantStartDttm);
						newVariant.appendChild(nVariantEndDttm);
						newVariant.appendChild(nVariantRabattID);
						newVariant.appendChild(nVariantEAN);
						newVariant.appendChild(nVariantTG);
						reweVariantList.appendChild(newVariant);
					}

				}

			}

			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(docEle), new StreamResult(writer));
			outputXML = writer.getBuffer().toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		getOffersForMarketingActivityCD("902291");
		System.out.println("Init Table Returning: \n" + outputXML);
		// return outputXML;
	}

	static private String getInputXML() throws FileNotFoundException {
		return (new Scanner(new File("C:\\Users\\A107E76\\Documents\\WebService Development\\XMLSample2.xml")))
				.useDelimiter("\\Z").next();
	}
	
	
	private static List<Offer> getOffersForMarketingActivityCD(String activityCd) throws SQLException {
		List<Offer> offerList = new ArrayList<Offer>();
		Statement statement = DBConnectionProvider.connect().createStatement();

		// Get EntityTypeID
		// Query: SELECT "ENTITYTYPEID" FROM "dbo"."ALOM_ENTITYTYPE" where
		// entitytypename = 'MARKETINGACTIVITY'
		// EntityTypeID for MarketingActivity = 230396323080180#
		// EntityTypeID for Offer = 251131843865063

		String queryTemplate = "SELECT * FROM dbo.ALENT_OFFER WHERE OFFERID IN (SELECT CONTAINSID FROM dbo.ALOM_ASSEMBLY WHERE CONTAINERTYPEID = '230396323080180' AND CONTAINSTYPEID = '251131843865063' AND CONTAINERID = (SELECT MARKETINGACTIVITYID FROM dbo.ALENT_MARKETINGACTIVITY WHERE ACTIVITYNUMBER = '"
				+ activityCd + "'))";

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

}
