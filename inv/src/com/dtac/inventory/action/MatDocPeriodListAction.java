/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.dtac.inventory.action;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

import com.dtac.admin.data.DBLogin;
import com.dtac.admin.data.DBMonth;
import com.dtac.admin.data.DBYear;
import com.dtac.inventory.data.DBMatDocPeriod;
import com.dtac.inventory.data.DBMaterialPeriod;
import com.dtac.inventory.data.DBMaterialType;
import com.dtac.inventory.form.MatDocPeriodForm;
/** 
 * MyEclipse Struts
 * Creation date: 04-06-2012
 */
public class MatDocPeriodListAction extends LookupDispatchAction {

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
	    map.put("matdocperiodform.button.search", 	"search");
	    map.put("matdocperiodform.button.open", 	"openperiod");
	    map.put("matdocperiodform.button.close", 	"closeperiod");
	    
	    return map;
	}
	
	public ActionForward search(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
	throws Exception {	//04-06-2012
		String loginId	= "success";
		String appCode 	= "mm22";
		try {
			HttpSession session = request.getSession();
			if (session.isNew()) {
				session.invalidate();
				request.setAttribute("alertMessage","Session Timeout. Login again.");
				return mapping.findForward("relogin");
			} else {
				loginId = (String) session.getAttribute("loginId");
				if (loginId == null) {
					request.setAttribute("alertMessage","Please Login.");
					return mapping.findForward("relogin");
				}
			}
			DBLogin dbLogin = new DBLogin();			
			if (!dbLogin.CheckAppAuth(loginId,appCode,"disp")) {
				request.setAttribute("alertMessage","You don't have authorize.");
				return mapping.findForward("alertmsg");
			}
			
			MatDocPeriodForm matDocPeriodForm = (MatDocPeriodForm) form;
			String year 	= matDocPeriodForm.getYear();
			String month	= matDocPeriodForm.getMonth();
			

			DBYear dbYear 	= new DBYear();
			List yearList	= dbYear.GetYearList("", "");
			if (yearList.size() > 0) request.setAttribute("yearList", yearList);
						
			DBMonth dbMonth = new DBMonth();
			List monthList = dbMonth.GetMonthList();
			if (monthList.size() > 0) request.setAttribute("monthList", monthList);
			
			DBMatDocPeriod dbMatDocPeriod = new DBMatDocPeriod();
			List matDocPeriodList = dbMatDocPeriod.GetMatDocPeriodList(year, month);
			if (matDocPeriodList.size() > 0) request.setAttribute("matDocPeriodList", matDocPeriodList);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward("success");
	}
	public ActionForward openperiod(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) 
		throws Exception {	//10-10-2012
			String forwardText 	= "openperiod";
			String loginId		= "";
			String appCode 		= "mm22";
			try {
				HttpSession session = request.getSession();
				if (session.isNew()) {
					session.invalidate();
					request.setAttribute("alertMessage","Session Timeout. Login again.");
					return mapping.findForward("relogin");
				} else {
					loginId = (String) session.getAttribute("loginId");
					if (loginId == null) {
						request.setAttribute("alertMessage","Please Login.");
						return mapping.findForward("relogin");
					}
				}
				DBLogin dbLogin = new DBLogin();			
				if (!dbLogin.CheckAppAuth(loginId,appCode,"mant")) {
					request.setAttribute("alertMessage","You don't have authorize.");
					return mapping.findForward("alertmsg");
				}

				MatDocPeriodForm matDocPeriodForm = (MatDocPeriodForm) form;
				String year		= matDocPeriodForm.getYear();
				String month	= matDocPeriodForm.getMonth();
				String status	= "";
				DBMatDocPeriod dbMatDocPeriod = new DBMatDocPeriod();
				status = dbMatDocPeriod.CheckMatDocPeriodStatus(year, month);
				if(status.equals("AC")){
					forwardText 	= "success";
					request.setAttribute("alertMessage", "งวดเอกสารนี้ถูกเปิดอยู่แล้ว");
				}
				
				DBYear dbYear 	= new DBYear();
				List yearList	= dbYear.GetYearList("", "");
				if (yearList.size() > 0) request.setAttribute("yearList", yearList);
							
				DBMonth dbMonth = new DBMonth();
				List monthList = dbMonth.GetMonthList();
				if (monthList.size() > 0) request.setAttribute("monthList", monthList);
				
				
				List matDocPeriodList = dbMatDocPeriod.GetMatDocPeriodList(year, "");
				if (matDocPeriodList.size() > 0) request.setAttribute("matDocPeriodList", matDocPeriodList);
				
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			return  mapping.findForward(forwardText);
		}
	public ActionForward closeperiod(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) 
		throws Exception {	//10-10-2012
			String forwardText 	= "closeperiod";
			String loginId		= "";
			String appCode 		= "mm22";
			try {
				HttpSession session = request.getSession();
				if (session.isNew()) {
					session.invalidate();
					request.setAttribute("alertMessage","Session Timeout. Login again.");
					return mapping.findForward("relogin");
				} else {
					loginId = (String) session.getAttribute("loginId");
					if (loginId == null) {
						request.setAttribute("alertMessage","Please Login.");
						return mapping.findForward("relogin");
					}
				}
				DBLogin dbLogin = new DBLogin();			
				if (!dbLogin.CheckAppAuth(loginId,appCode,"mant")) {
					request.setAttribute("alertMessage","You don't have authorize.");
					return mapping.findForward("alertmsg");
				}

				MatDocPeriodForm matDocPeriodForm = (MatDocPeriodForm) form;
				String year		= matDocPeriodForm.getYear();
				String month	= matDocPeriodForm.getMonth();
				String status	= "";
				DBMatDocPeriod dbMatDocPeriod = new DBMatDocPeriod();
				status = dbMatDocPeriod.CheckMatDocPeriodStatus(year, month);
				if(status.equals("CL")){
					forwardText 	= "success";
					request.setAttribute("alertMessage", "งวดเอกสารนี้ถูกปิดไปแล้ว");
				}
				
				DBYear dbYear 	= new DBYear();
				List yearList	= dbYear.GetYearList("", "");
				if (yearList.size() > 0) request.setAttribute("yearList", yearList);
							
				DBMonth dbMonth = new DBMonth();
				List monthList = dbMonth.GetMonthList();
				if (monthList.size() > 0) request.setAttribute("monthList", monthList);
				
				
				List matDocPeriodList = dbMatDocPeriod.GetMatDocPeriodList(year, "");
				if (matDocPeriodList.size() > 0) request.setAttribute("matDocPeriodList", matDocPeriodList);
				
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			return  mapping.findForward(forwardText);
		}
}