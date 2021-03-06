/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.dtac.admin.action;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dtac.admin.data.DBApplication;
import com.dtac.admin.data.DBLogin;
import com.dtac.admin.form.MemberAuthForm;
/** 
 * MyEclipse Struts
 * Creation date: 20-08-2012
 */
public class AuthReport1StartAction extends Action {
  
	public ActionForward execute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
	throws Exception {	//23-08-2012
		String loginId		= "";
		String appCode 		= "ad12";
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

			MemberAuthForm memberAuthForm = (MemberAuthForm) form;
			memberAuthForm.initial();
			
			DBApplication dbApplication = new DBApplication();
			List applTypeList = dbApplication.GetApplTypeList();
			if (applTypeList.size() > 0) request.setAttribute("applTypeList", applTypeList);
			
			memberAuthForm.setReportNo("1");
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward("success");
	}
}