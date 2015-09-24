package com.sas.mom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sas.mom.Entity.MarketingActivity;
import com.sas.mom.Entity.Offer;
import com.sas.mom.Entity.VarianceDetail;
import com.sas.mom.Entity.VarianceHead;

/**
 * Servlet implementation class CampaignMatrix
 */
@WebServlet("/CampaignMatrix")
public class CampaignMatrix extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CampaignMatrix() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String campCD = request.getParameter("campCD");
		if(campCD == null || campCD.isEmpty()) {
			campCD = "902291";
		}
		
		MarketingActivity activity = null;
		List<Offer> offerList = new ArrayList<Offer>();
		List<VarianceHead> varianceHeadList = new ArrayList<VarianceHead>();
		List<VarianceDetail> varianceList = new ArrayList<VarianceDetail>();
		try {
			activity = WebserviceProvider.getActivityDetails(campCD);
			offerList = WebserviceProvider.getOffersForMarketingActivityCD(campCD);
			varianceList = WebserviceProvider.getVarianceDetails(campCD);
			varianceHeadList = WebserviceProvider.getVarianceList(campCD);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
		}

		if(activity!=null)
			response.getOutputStream().println(activity.toString());
		
		for (Offer offer : offerList)
			response.getOutputStream().println(offer.toString());
		
		for(VarianceDetail variance : varianceList)
			response.getOutputStream().println(variance.toString());
		
		for(VarianceHead varianceHead : varianceHeadList) 
			response.getOutputStream().println(varianceHead.toString());
		
		response.flushBuffer();
	}

}
