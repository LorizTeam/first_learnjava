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
import com.dtac.admin.data.DBMasterTable;
import com.dtac.inventory.data.DBVender;
import com.dtac.inventory.form.VenderForm;
/** 
 * MyEclipse Struts
 * Creation date: 12-04-2012
 */
public class VenderListStartAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
	throws Exception { //18-04-2012
		String forwardText  = "success";
		String loginId 		= "";
		String appCode  	= "mm07";
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
			
			VenderForm venderForm = (VenderForm) form;

			String venderCode = "";
			if (session.getAttribute("venderCode") != null) venderCode = (String) session.getAttribute("venderCode");

			DBVender dbVender = new DBVender();
			List venderList = dbVender.GetVenderMasterList("", "", "", "", "");
			if (venderList.size() > 0) request.setAttribute("venderList", venderList);

			DBMasterTable dbMasterTable = new DBMasterTable();
			List vendTypeList = dbMasterTable.GetMasterTableDTList("vdty", "", "", "AC");
			if (vendTypeList.size() > 0) request.setAttribute("vendTypeList", vendTypeList);
			
			venderForm.setVenderCode(venderCode);	
			venderForm.setStatus("AC");
			
			venderForm.initial();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}	
		return mapping.findForward(forwardText);
	}
}