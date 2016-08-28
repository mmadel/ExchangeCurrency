/*package com.asset;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.stc.offers.offeractions.display.CommentDisplay;
import com.stc.offers.offeractions.display.CouponDisplay;
import com.stc.offers.offeractions.service.OfferActionsService;
import com.stc.offers.offeractions.util.ExchangeUtil;
import com.stc.offers.orm.Comments;
import com.stc.offers.util.ValidationUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

@Controller
@RequestMapping("VIEW")
public class OfferActionsController {

	@Autowired
	public OfferActionsService offerActionsService;
	
	@RenderMapping
	public String handleRenderRequest(RenderRequest request){
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(request));
		if(offerActionsService.offerIsAccessible(httpReq,request)){
			request.setAttribute("currencies", offerActionsService.getCurrencies(themeDisplay.getLocale()));
			request.setAttribute("offerAttachments", offerActionsService.getOfferAttchement(httpReq));
			request.setAttribute("hasGetOffer", offerActionsService.hasGetOffer(httpReq));
			boolean userIsSTC = ValidationUtil.STCUser(request);
			request.setAttribute("userIsSTC", userIsSTC);
			offerActionsService.offerViewed(httpReq);
			return offerActionsService.displayOffer(request);
		}
		return "error";
	}
	
	@ResourceMapping(value="rateOfferUrl")
	public String rateOffer (ResourceRequest request,ResourceResponse response){
		response.setContentType("text/html; charset=UTF-8");
		return offerActionsService.rateOffer(request);
	}
	

	@ResourceMapping(value = "addComment")
	public String addComment(Model model, ResourceRequest request,
			ResourceResponse response) {
		try{
		String commentText = request.getParameter("commentText");		
		String offerId = request.getParameter("offerId");
		System.out.print("offerid = "+offerId+" comment Text ="+commentText);
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		User user=themeDisplay.getUser();
		if(offerId!=null && commentText!=null && !commentText.isEmpty()){
		Comments comment=new Comments();
		comment.setCommentDate(Calendar.getInstance().getTime());
		comment.setCommentText(commentText);
		comment.setOfferId(Long.parseLong(offerId));
		comment.setUserId(user.getUserId());
		comment.setParentCommentID(0l);
		offerActionsService.addComment(comment);
		}
		List<CommentDisplay> commentlist=new ArrayList<CommentDisplay>();
	    commentlist=offerActionsService.getofferComments(Long.parseLong(offerId));
	 
		response.setContentType("text/html; charset=UTF-8");
		request.setAttribute("commentslist", commentlist);
		
		
		
		}catch (Exception e) {
			e.printStackTrace();			
		}
		return  "Comments";
	}
	
	@ResourceMapping(value = "addReply")
	public String addReply(Model model, ResourceRequest request,
			ResourceResponse response) {
		try{
			String replyText = request.getParameter("replyText");		
			String offerId = request.getParameter("offerId");
			String parentCommentId=request.getParameter("ParentCommentId");
			System.out.print("offerid = "+offerId+" comment Text ="+replyText);
			ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
			User user=themeDisplay.getUser();
			if(offerId!=null && replyText!=null &&! replyText.isEmpty() && parentCommentId!=null){
			Comments comment=new Comments();
			comment.setCommentDate(Calendar.getInstance().getTime());
			comment.setCommentText(replyText);
			comment.setOfferId(Long.parseLong(offerId));
			comment.setUserId(user.getUserId());
			comment.setParentCommentID(Long.parseLong(parentCommentId));
			offerActionsService.addComment(comment);
			}
			List<CommentDisplay> commentlist=new ArrayList<CommentDisplay>();
		    commentlist=offerActionsService.getofferComments(Long.parseLong(offerId));	
			response.setContentType("text/html; charset=UTF-8");
			request.setAttribute("commentslist", commentlist);
			
			}catch (Exception e) {
				e.printStackTrace();			
			}
			return "Comments";
	}
	
	@ResourceMapping(value = "addFeedBack")
	public void addFeedBack(Model model, ResourceRequest request,
			ResourceResponse response) {
		try{		
		String offerIdSt = request.getParameter("offerId");
		String feedBackText = request.getParameter("feedBackText");
		System.out.println(offerIdSt +" ######### feedBackText "+feedBackText);
		if(offerIdSt!=null && feedBackText!=null && !feedBackText.isEmpty()){
			ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
			User user=themeDisplay.getUser();
			offerActionsService.addFeedBack(request,user,Long.parseLong(offerIdSt), feedBackText);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@ResourceMapping(value = "getComments")
	public String getComments(Model model, ResourceRequest request,
			ResourceResponse response) {
		try{
		String offerId = request.getParameter("offerId");
		if(offerId!=null){
			List<CommentDisplay> commentlist=new ArrayList<CommentDisplay>();
		    commentlist=offerActionsService.getofferComments(Long.parseLong(offerId));
		 
			response.setContentType("text/html; charset=UTF-8");
			request.setAttribute("commentslist", commentlist);
			
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "Comments";
		
	}
	
	@ResourceMapping(value = "changeCurrency")
	public String changeCurrancey(Model model, ResourceRequest request,
			ResourceResponse response) {
		
		String targetCurrancy = request.getParameter("targetCurrancy");
		String currentCurrancy = request.getParameter("currentCurrancy");
		String priceBefore = request.getParameter("priceBefore");
		String priceAfter = request.getParameter("priceAfter");
		
		try{
			ExchangeUtil ex = new ExchangeUtil();
			DecimalFormat df = new DecimalFormat("###.#");
			priceBefore = df.format(ex.convertCurrancy(currentCurrancy,targetCurrancy , Double.parseDouble(priceBefore)));
			//priceBefore=String.valueOf();
			priceAfter = df.format(ex.convertCurrancy(currentCurrancy,targetCurrancy , Double.parseDouble(priceAfter)));
			//priceAfter=String.valueOf();
		
		}catch(Exception ex){
			
			
		}
		
		System.out.println("####################################################"+ targetCurrancy + currentCurrancy + priceAfter + priceBefore);
		
		request.setAttribute("currentCurrancy", currentCurrancy);
		request.setAttribute("priceBefore", priceBefore);
		request.setAttribute("priceAfter",priceAfter);
		request.setAttribute("targetCurrancy", targetCurrancy);
		
		
		response.setContentType("text/html; charset=UTF-8");
		
		return "currencyRate";
	}
	
	@ResourceMapping(value = "getCoupon")
	public String getCoupon(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) {
		try {
			CouponDisplay coupon=offerActionsService.getCoupon(resourceRequest, ParamUtil.getInteger(resourceRequest, "type"));
			resourceRequest.setAttribute("couponDisplay", coupon);
			boolean userIsSTC = ValidationUtil.STCUser(resourceRequest);
			resourceRequest.setAttribute("userIsSTC", userIsSTC);
			HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			long offerId = ParamUtil.getLong(httpReq, "offerId");
			resourceRequest.setAttribute("offer", offerActionsService.getOffer(offerId));
			resourceResponse.setContentType("text/html; charset=UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "coupon";
	}
	
	@ResourceMapping(value = "getOffer")
	public String getOffer(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse)
	{
		try
		{
			CouponDisplay coupon=offerActionsService.getOffer(resourceRequest);
			resourceRequest.setAttribute("couponDisplay", coupon);
			boolean userIsSTC = ValidationUtil.STCUser(resourceRequest);
			resourceRequest.setAttribute("userIsSTC", userIsSTC);
			HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			long offerId = ParamUtil.getLong(httpReq, "offerId");
			resourceRequest.setAttribute("offer", offerActionsService.getOffer(offerId));
			resourceResponse.setContentType("text/html; charset=UTF-8");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return "coupon";
	}
	

}
*/