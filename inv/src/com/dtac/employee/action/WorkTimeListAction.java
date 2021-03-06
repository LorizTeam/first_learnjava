/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.dtac.employee.action;
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
import com.dtac.admin.data.DBMemberType;
import com.dtac.admin.form.MemberTypeForm;
import com.dtac.employee.data.DBEmployee;
import com.dtac.employee.data.DBPeriod;
import com.dtac.employee.data.DBWorkTime;
import com.dtac.employee.form.EmployeeForm;
import com.dtac.employee.form.PeriodForm;
import com.dtac.employee.form.WorkTimeForm;
import com.dtac.utils.DateUtil;
/** 
 * MyEclipse Struts
 * Creation date: 20-08-2012
 */
public class WorkTimeListAction extends LookupDispatchAction {

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("timeattendform.button.view",		"view");
		map.put("timeattendform.button.search",		"search");	 
		return map;
	}

	public ActionForward search(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
	throws Exception { //20-08-2012
		String forwardText 	= "success";
		String loginId 		= "";
		String appCode  	= "hr42";
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
			if (!dbLogin.CheckAppAuth(loginId, appCode, "disp")) {
				request.setAttribute("alertMessage","You don't have authorize.");
				return mapping.findForward("alertmsg");
			}

			WorkTimeForm workTimeForm = (WorkTimeForm) form;
			String empID		= workTimeForm.getEmpID();
			String empNameT		= new String(workTimeForm.getEmpNameT().getBytes("ISO8859_1"),"utf-8");
			String empLastNameT	= new String(workTimeForm.getEmpLastNameT().getBytes("ISO8859_1"),"utf-8");
			String empTypeCode	= workTimeForm.getEmpTypeCode();			
			String empDeptCode	= workTimeForm.getEmpDeptCode();
			//String startTime	= workTimeForm.getStartTime();
			//String stopTime		= workTimeForm.getStopTime();
			String workStatus	= workTimeForm.getWorkStatus();
							
			DBEmployee dbEmployee = new DBEmployee();
			List employeeList = dbEmployee.GetEmployeeList(empID, empNameT, empLastNameT, empTypeCode, empDeptCode, "", workStatus);
			if (employeeList.size() > 0) request.setAttribute("employeeList", employeeList);
			
			DBMemberType dbMemberType = new DBMemberType();
			List memberTypeList = dbMemberType.GetMemberTypeList("", "", "");
			if (memberTypeList.size() > 0) request.setAttribute("memberTypeList", memberTypeList);
			
			workTimeForm.setEmpNameT(empNameT);
			workTimeForm.setEmpLastNameT(empLastNameT);
			session.setAttribute("empTypeCode", empTypeCode);
			session.setAttribute("empDeptCode", empDeptCode);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward(forwardText);
	}
	public ActionForward view(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
	throws Exception { //20-08-2012
		String forwardText 	= "view";
		String loginId 		= "";
		String appCode  	= "hr42";
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
			if (!dbLogin.CheckAppAuth(loginId, appCode, "disp")) {
				request.setAttribute("alertMessage","You don't have authorize.");
				return mapping.findForward("alertmsg");
			}
			 				 
			WorkTimeForm workTimeForm = (WorkTimeForm) form;
			String empID		= workTimeForm.getEmpID();
			String empNameT		= new String(workTimeForm.getEmpNameT().getBytes("ISO8859_1"),"utf-8");
			String empLastNameT	= new String(workTimeForm.getEmpLastNameT().getBytes("ISO8859_1"),"utf-8");
			String empTypeCode	= workTimeForm.getEmpTypeCode();			
			String empDeptCode	= workTimeForm.getEmpDeptCode();
	     	String workDate		= workTimeForm.getWorkDate();	     	
			String salTypeCode	= "";			
			String alertMessage = "";
			
			DateUtil dateUtil = new DateUtil();
			if (workDate.trim().length() != 10) alertMessage = "check work date";
			else if (!dateUtil.isValidDateStr(dateUtil.CnvToYYYYMMDDEngYear(workDate, '-'))) alertMessage = "check work date";

			DBEmployee dbEmployee = new DBEmployee();
			if (!dbEmployee.CheckEmployee(empID, "")) alertMessage = "check employee";
			
	     	DBPeriod dbPeriod = new DBPeriod();
	     	DBMemberType dbMemberType = new DBMemberType();
			List memberTypeList = dbMemberType.GetMemberTypeList(salTypeCode, empTypeCode, "");
			if (memberTypeList.size() == 1) {
				MemberTypeForm memInfo = (MemberTypeForm) memberTypeList.get(0);				
				salTypeCode = memInfo.getMemberGrpCode();				
				workTimeForm.setSalTypeCode(salTypeCode);
				workTimeForm.setSalTypeName(memInfo.getMemberGrpName());
				
				if (alertMessage.equals("")) {
					List periodList = dbPeriod.GetPeriod(salTypeCode, workDate);
					if (periodList.size() == 1) {
						PeriodForm periodInfo = (PeriodForm) periodList.get(0);
						workTimeForm.setYear(periodInfo.getYear());
						workTimeForm.setMonth(periodInfo.getMonth());
						workTimeForm.setPeriodNo(periodInfo.getPeriodNo());				
							
					} else {
						alertMessage = "check period in menu 1.3";
					}
				}
			} else {
				alertMessage = "เช็คประเภทพนักงาน";
			}
			List employeeList;
			if (alertMessage.equals("")) {
				employeeList = dbEmployee.GetEmployee(empID);
				if (employeeList.size() == 1) {
					EmployeeForm empInfo = (EmployeeForm) employeeList.get(0);
					workTimeForm.setEmpNameT(empInfo.getEmpNameT());
					workTimeForm.setEmpLastNameT(empInfo.getEmpLastNameT());
					workTimeForm.setEmpDeptCode(empInfo.getEmpDeptCode());
					workTimeForm.setEmpTypeCode(empInfo.getEmpTypeCode());					
					workTimeForm.setWorkStatus(empInfo.getStatus());
					
					DBWorkTime dbworktime = new DBWorkTime();				
					if (!dbworktime.Checkworktime(empID, workDate))	dbworktime.InsertWorkTime(workDate, empID);
					
					List workTimeList = dbworktime.GetWorkTimeList(empID, "", "", "", "", workDate);
					if (workTimeList.size() == 1) {
						WorkTimeForm wtForm = (WorkTimeForm) workTimeList.get(0);
						workTimeForm.setEmpID(wtForm.getEmpID());
						workTimeForm.setWorkDate(wtForm.getWorkDate());
						workTimeForm.setWorkTimeStart(wtForm.getWorkTimeStart());
						workTimeForm.setWorkTimeStop(wtForm.getWorkTimeStop());
						workTimeForm.setStartTime(wtForm.getStartTime());
						workTimeForm.setStopTime(wtForm.getStopTime());
						workTimeForm.setAmlate(wtForm.getAmlate());
						workTimeForm.setPunchCard(wtForm.getPunchCard());
						workTimeForm.setEmpTypeCode(wtForm.getEmpTypeCode());
						workTimeForm.setR(wtForm.getR());
						workTimeForm.setW(wtForm.getW());
						workTimeForm.setB(wtForm.getB());
						workTimeForm.setV(wtForm.getV());
						workTimeForm.setC(wtForm.getC());
						workTimeForm.setA(wtForm.getA());
						workTimeForm.setL(wtForm.getL());
						workTimeForm.setX(wtForm.getX());
						
						workTimeForm.setXw(wtForm.getXw());
						workTimeForm.setXb(wtForm.getXb());
						workTimeForm.setXv(wtForm.getXv());
						workTimeForm.setXx(wtForm.getXx());
						
						workTimeForm.setOtN1(wtForm.getOtN1());
						workTimeForm.setOtN1_5(wtForm.getOtN1_5());
						workTimeForm.setOtN2(wtForm.getOtN2());
						workTimeForm.setOtN3(wtForm.getOtN3());
						workTimeForm.setWorkStatus(wtForm.getWorkStatus());
					}
				}
			}			
			if (!alertMessage.equals("")) {
				forwardText = "success";
				request.setAttribute("alertMessage", alertMessage);
				
				workTimeForm.setEmpNameT(empNameT);
				workTimeForm.setEmpLastNameT(empLastNameT);
				
					    	
				memberTypeList = dbMemberType.GetMemberTypeList("", "", "");
				if (memberTypeList.size() > 0) request.setAttribute("memberTypeList", memberTypeList);
				
				employeeList = dbEmployee.GetEmployeeList("", "", "", empTypeCode, empDeptCode, "", "AC");
				if (employeeList.size() > 0) request.setAttribute("employeeList", employeeList);
				
			}
			//System.out.println("workDate"+workDate);	
			session.setAttribute("workDate", workDate);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward(forwardText);
	}	
}	