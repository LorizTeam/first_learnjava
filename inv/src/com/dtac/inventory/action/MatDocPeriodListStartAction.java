/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.dtac.inventory.action;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dtac.admin.data.DBLogin;
import com.dtac.admin.data.DBMonth;
import com.dtac.admin.data.DBYear;
import com.dtac.inventory.data.DBMatDocPeriod;
import com.dtac.inventory.form.MatDocPeriodForm;
import com.dtac.utils.DateUtil;
/** 
 * MyEclipse Struts
 * Creation date: 30-05-2012
 */
public class MatDocPeriodListStartAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
	throws Exception {	//12-10-2011
		String loginId	= "";
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
			matDocPeriodForm.initial();
			
			String year	= "";			
			if (session.getAttribute("year") != null) year = (String) session.getAttribute("year");

			DateUtil dateUtil = new DateUtil();
			if (year.equals("")) year = dateUtil.curTHYear();

			DBYear dbYear 	= new DBYear();
			List yearList	= dbYear.GetYearList("", "");
			if (yearList.size() > 0) request.setAttribute("yearList", yearList);
						
			DBMonth dbMonth = new DBMonth();
			List monthList = dbMonth.GetMonthList();
			if (monthList.size() > 0) request.setAttribute("monthList", monthList);
			
			DBMatDocPeriod dbMatDocPeriod = new DBMatDocPeriod();
			dbMatDocPeriod.AddAutoMatDocPeriod(year);			
			
			List matDocPeriodList = dbMatDocPeriod.GetMatDocPeriodList(year, "");
			if (matDocPeriodList.size() > 0) request.setAttribute("matDocPeriodList", matDocPeriodList);

			matDocPeriodForm.setYear(year);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward("success");
	}
}