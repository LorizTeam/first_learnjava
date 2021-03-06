/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.dtac.employee.action;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import java.util.Date;
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
import com.dtac.employee.data.DBCalendar;
import com.dtac.employee.form.CalendarForm;
import com.dtac.utils.DateUtil;
/** 
 * MyEclipse Struts
 * Creation date: 14-08-2012
 */
public class CalendarUpdateAction extends LookupDispatchAction {

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
	    map.put("calendarform.button.update", "update");
	    map.put("calendarform.button.cancel", "cancel");
	    return map;
	}
	
	public ActionForward update(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
	throws Exception {	//28-03-2010
		String forwardText 	= "success";
		String loginId		= "";
		String appCode 		= "hr32";
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

			CalendarForm calendarForm = (CalendarForm) form;
			String workDate		= calendarForm.getWorkDate();
			String salTypeCode 	= calendarForm.getSalTypeCode();
			String dayTypeCode	= calendarForm.getDayTypeCode();
			String timeStart	= calendarForm.getTimeStart();
			String timeStop		= calendarForm.getTimeStop();							
			String dayRemark	= new String(calendarForm.getDayRemark().getBytes("ISO8859_1"),"utf-8");

			SimpleDateFormat format = new SimpleDateFormat("kk:mm:ss");
			Date tmp_timeStart= new Date();
			Date tmp_timeStop = new Date();
			
			if (timeStart.equals("")) timeStart = "00:00:00"; 
			else
				try {
					tmp_timeStart = format.parse(timeStart);
					if (!format.format(tmp_timeStart).equals(timeStart)) {
						forwardText = "repeat";
						request.setAttribute("alertMessage", "check start time");
					}
				} catch (ParseException  illExc) {
					forwardText = "repeat";
					request.setAttribute("alertMessage", "check start time");
				}
				
			if (timeStop.equals("")) timeStop = "00:00:00";
			else
				try {
					tmp_timeStop = format.parse(timeStop);
					if (!format.format(tmp_timeStop).equals(timeStop)) {
						forwardText = "repeat";
						request.setAttribute("alertMessage", "check end time");
					}
				} catch (ParseException illExc) {
					forwardText = "repeat";
					request.setAttribute("alertMessage", "check end time");
				}


			if (forwardText.equals("success")) {
				//String year		= calendarForm.getYear();
				String engYear	= calendarForm.getEngYear();
				String month	= calendarForm.getMonth();

				DBYear dbYear 	= new DBYear();
				List yearList	= dbYear.GetYearList("", "");
				if (yearList.size() > 0) request.setAttribute("yearList", yearList);
				
				DBMonth dbMonth = new DBMonth();
				List monthList = dbMonth.GetMonthList();
				if (monthList.size() > 0) request.setAttribute("monthList", monthList);
				
				DateUtil dateUtil = new DateUtil();
				int maxDayInMonth = dateUtil.maxDayForMonth(engYear, month);
				String days = String.valueOf(maxDayInMonth);
				
				DBCalendar dbCalendar = new DBCalendar();
				dbCalendar.UpdateCalendar(salTypeCode, workDate, dayTypeCode, dayRemark, timeStart, timeStop);
				
				List calendarList = dbCalendar.GetCalendarList(engYear, month, days, salTypeCode);
				if (calendarList.size() > 0) request.setAttribute("calendarList", calendarList);
				
				calendarForm.setWorkDate("");
			} 
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward(forwardText);
	}
	public ActionForward cancel(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
	throws Exception {	//14-10-2011
		String loginId	= "";
		String appCode 	= "hr32";
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
			
			CalendarForm calendarForm = (CalendarForm) form;
			String salTypeCode 	= calendarForm.getSalTypeCode();
			//String year		= calendarForm.getYear();
			String month	= calendarForm.getMonth();
			String days		= "";
			String engYear	= calendarForm.getEngYear(); //String.valueOf(Integer.parseInt(year)-543);
			
			DBYear dbYear 	= new DBYear();
			List yearList	= dbYear.GetYearList("", "");
			if (yearList.size() > 0) request.setAttribute("yearList", yearList);
			
			DBMonth dbMonth = new DBMonth();
			List monthList = dbMonth.GetMonthList();
			if (monthList.size() > 0) request.setAttribute("monthList", monthList);
						
			DateUtil dateUtil = new DateUtil();
			int maxDayInMonth = dateUtil.maxDayForMonth(engYear, month);
			days = String.valueOf(maxDayInMonth);
			
			DBCalendar dbCalendar = new DBCalendar();			
			List calendarList = dbCalendar.GetCalendarList(engYear, month, days, salTypeCode);
			if (calendarList.size() > 0) request.setAttribute("calendarList", calendarList);
			
			calendarForm.setWorkDate("");
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward("success");
	}
}