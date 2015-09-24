<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.sas.mom.*"%>
<%@ page import="com.sas.mom.Entity.*"%>
<%@ page import="java.util.List, java.util.ArrayList"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Kampagnen Matrix Generator</title>

<style type="text/css">
body {
	font-family: verdana, sans-serif;
	font-style: normal;
	font-size: x-small;
}

h3 {
	font-size: medium;
}

table {
	border-collapse: collapse;
}

th, td {
	border: 1px solid black;
	padding: 2px;
}

.noBorder {
	border: 0px;
}

.headRow {
	background-color: #E82C0C;
	color: #ffffff;
}
</style>
</head>
<body>
	<%
		String campCD = request.getParameter("campCD");
		if (campCD == null || campCD.isEmpty()) {
			campCD = "902291";
		}

		MarketingActivity activity = null;
		List<VarianceHead> varianceHeadList = new ArrayList<VarianceHead>();
		try {
			activity = WebserviceProvider.getActivityDetails(campCD);
			varianceHeadList = WebserviceProvider.getVarianceList(campCD);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			return;
		}

		List<String> strategieTitles = new ArrayList<String>();
		List<String> targetGroupList = new ArrayList<String>();
		List<String> targetGroupVariance = new ArrayList<String>();
		List<String> varianceTxtList = new ArrayList<String>();

		int countPrio = 0;
		for (VarianceHead vh : varianceHeadList) {
			if (!strategieTitles.contains(vh.getStrategie()))
				strategieTitles.add(vh.getStrategie());

			if (!targetGroupVariance.contains("(" + vh.getName() + ") " + vh.getTargetGroup())) {
				targetGroupList.add(vh.getTargetGroup());
				varianceTxtList.add(vh.getName());
				targetGroupVariance.add("(" + vh.getName() + ") " + vh.getTargetGroup());
			}
		}

		int[] stratgieOfferCnt = new int[strategieTitles.size()];
		int[] strategieVolumes = new int[strategieTitles.size()];
		for (VarianceHead vh : varianceHeadList) {
			stratgieOfferCnt[strategieTitles.indexOf(vh.getStrategie())] = Math.max(vh.getOfferCount(),
					stratgieOfferCnt[strategieTitles.indexOf(vh.getStrategie())]);

			strategieVolumes[strategieTitles.indexOf(vh.getStrategie())] += vh.getPlannedVolume();
		}

		// i targetGroupVariance
		// j Strategie
		// k Offer

		// prepareDate
		VarianceDetail[][][] dataStrore = new VarianceDetail[targetGroupVariance.size()][strategieTitles.size()][16];
		for (int i = 0; i < targetGroupVariance.size(); i++) {
			for (int j = 0; j < strategieTitles.size(); j++) {
				List<VarianceDetail> vdList = WebserviceProvider.getVarianceByCriteria(campCD,
						strategieTitles.get(j), targetGroupList.get(i), varianceTxtList.get(i));

				for (int k = 0; k < 16; k++) {
					if (k < vdList.size()) {
						System.out.println("i=" + i + ",j=" + j + ",k=" + k + ",variance=" + vdList.get(k));
						;
						dataStrore[i][j][k] = vdList.get(k);
					} else {
						dataStrore[i][j][k] = new VarianceDetail();
					}
				}
			}
		}
	%>


	<%
		if (activity.getCd() != null) {
	%>
	<h2><%=activity.getName()%></h2>
	<p>
		Erscheint von
		<%=activity.getStartDate().substring(0, 10)%>
		bis
		<%=activity.getEndDate().substring(0, 10)%><br /> Letze Änderung am:
		<%=activity.getLastUpdate().substring(0, 10)%><br />
		Vertriebsbereiche:
		<%=activity.getVertriebsBereiche()%><br />
	</p>
	<br />
	<br />


	<%
		} else {
	%>

	<h1>Kampagne nicht gefunden</h1>
	<p>Bitte prüfen Sie ob diese Kampagne bereits angelegt und
		gespeichert wurde.</p>

	<%
		}
		if (varianceHeadList.size() > 0) {
	%>
	<table>
		<tr>
			<!-- Kopfzeile wird Rot Makiert -->
			<td colspan="3" class="noBorder">
				<!-- bleibt leer -->
			</td>
			<%
				for (int j = 0; j < strategieTitles.size(); j++) {
			%>
			<th align="center" colspan="<%=(stratgieOfferCnt[j] * 3) + 1%>"
				align="center" class="headRow"><%=strategieTitles.get(j)%></th>
			<td width="10" class="noBorder">
				<!-- leer -->
			</td>
			<%
				}
			%>
		</tr>
		<tr>
			<th width="30" align="center">Prio</th>
			<th width="50" align="center">Zielgruppe</th>
			<th width="40" align="center">Parameter</th>
			<%
				for (int j = 0; j < strategieTitles.size(); j++) {
						for (int k = 0; k < stratgieOfferCnt[j]; k++) {
			%>
			<td colspan="3" align="center">Coupon <%=k + 1%></td>
			<%
				}
			%>
			<th width="40" align="center">Auflage</th>
			<td width="10" class="noBorder">
				<!-- leer -->
			</td>
			<%
				}
			%>
		</tr>
		<%
			for (int i = 0; i < targetGroupVariance.size(); i++) {
		%>
		<tr>

			<td rowspan="4" align="center"><%=i + 1%></td>
			<td rowspan="4" align="center"><%=targetGroupVariance.get(i)%></td>
			<th rowspan="1" align="center">Angebot</th>
			<%
				for (int j = 0; j < strategieTitles.size(); j++) {
							String plannedVolume = "";
							for (int k = 0; k < stratgieOfferCnt[j]; k++) {
								if (dataStrore[i][j][k].getHead() != null)
									plannedVolume = "" + dataStrore[i][j][k].getHead().getPlannedVolume();

								boolean emptyPlace = dataStrore[i][j][k].getOfferNm() == null;
			%>
			<th colspan="3" <%=emptyPlace ? "class=\"noBorder\"" : ""%>
				align="center"><%=emptyPlace ? "" : dataStrore[i][j][k].getOfferNm()%></th>
			<%
				}
			%>
			<td rowspan="4" <%=plannedVolume == "" ? "class=\"noBorder\"" : ""%>
				align="center"><%=plannedVolume%></td>
			<td rowspan="4" class="noBorder">
				<!-- leer -->
			</td>
			<%
				}
			%>

		</tr>
		<tr>
			<th rowspan="1" align="center">Laufzeit</th>
			<%
				for (int j = 0; j < strategieTitles.size(); j++) {
							for (int k = 0; k < stratgieOfferCnt[j]; k++) {
								if (dataStrore[i][j][k].getOfferNm() == null) {
			%>
			<td colspan="3" class="noBorder"></td>
			<%
				} else {
			%>
			<td align="center"><%=dataStrore[i][j][k].getValidFrom()%></td>
			<td align="center">bis</td>
			<td align="center"><%=dataStrore[i][j][k].getValidTo()%></td>
			<%
				}
							}
						}
			%>
		</tr>
		<tr>
			<th align="center" rowspan="1">Barcode</th>
			<%
				for (int j = 0; j < strategieTitles.size(); j++) {
							for (int k = 0; k < stratgieOfferCnt[j]; k++) {
								boolean emptyPlace = dataStrore[i][j][k].getOfferNm() == null;
			%>
			<td align="center" <%=emptyPlace ? "class=\"noBorder\"" : ""%>
				colspan="3">&nbsp;<%=emptyPlace ? "" : dataStrore[i][j][k].getEan()%></td>
			<%
				}
						}
			%>
		</tr>
		<tr>
			<th align="center" rowspan="1">Rabatt IDs</th>
			<%
				for (int j = 0; j < strategieTitles.size(); j++) {
							for (int k = 0; k < stratgieOfferCnt[j]; k++) {
								boolean emptyPlace = dataStrore[i][j][k].getOfferNm() == null;
			%>
			<td align="center" <%=emptyPlace ? "class=\"noBorder\"" : ""%>
				colspan="3"><%=emptyPlace ? "" : dataStrore[i][j][k].getRabattIdTxt()%></td>
			<%
				}
						}
			%>
		</tr>

		<%
			}
		%>
		<tr>
			<!-- Summenzeile -->
			<td colspan="3" class="noBorder">
				<!-- bleibt leer -->
			</td>
			<%
				for (int j = 0; j < strategieTitles.size(); j++) {
			%>
			<th colspan="<%=(stratgieOfferCnt[j] * 3)%>" align="right"
				class="noBorder">Summe: &nbsp; &nbsp;</th>
			<th colspan="1" class="noBorder"><%=strategieVolumes[j]%></th>
			<td width="10" class="noBorder">
				<!-- leer -->
			</td>
			<%
				}
			%>
		</tr>
	</table>

	<%
		} else {
	%>

	<h4>
		Keine Varianten angegeben
		</h1>
		<p>Bitte prüfen Sie ob diese Varianten bereits angelegt und
			gespeichert wurden.</p>

		<%
			}
		%>
	
</body>
</html>