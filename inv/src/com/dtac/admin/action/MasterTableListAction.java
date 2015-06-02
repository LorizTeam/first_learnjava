/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.dtac.admin.action;
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
import com.dtac.admin.data.DBMasterTable;
import com.dtac.admin.form.MasterTableForm;
/** 
 * MyEclipse Struts
 * Creation date: 12-04-2012
 */
public class MasterTableListAction extends LookupDispatchAction {

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("memberform.button.add",		"adddata");
		map.put("memberform.button.search",		"search");	 
		map.put("memberform.button.update",		"update");
		return map;
	}

	public ActionForward search(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
	throws Exception {	//01-10-2012
		String forwardText 	= "success";
		String loginId 		= "";
		//String appCode  	= "hr21";
		
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
			
			MasterTableForm masterTableForm = (MasterTableForm) form;
			String applTypeCode	= masterTableForm.getApplTypeCode();
			String grpCode 		= masterTableForm.getGrpCode();
			String status		= masterTableForm.getStatus();
			
			DBLogin dbLogin = new DBLogin();			
			if (!dbLogin.CheckAppAuth(loginId, applTypeCode+"00", "disp")) {
				request.setAttribute("alertMessage","You don't have authorize.");
				return mapping.findForward("alertmsg");
			}
			
			DBMasterTable dbMasterTable = new DBMasterTable();
			List grpList = dbMasterTable.GetMasterTableHDList(applTypeCode, "", "");			
			if (grpList.size() > 0) request.setAttribute("grpList", grpList);

			List typeList = dbMasterTable.GetMasterTableDTList(grpCode, "", "", status);
			if (typeList.size() > 0) request.setAttribute("typeList", typeList);
			
			request.setAttribute("grpCode", grpCode);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward(forwardText);
	}
	public ActionForward adddata(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
	throws Exception {	//01-10-2012
		String forwardText 	= "addnew";
		String loginId 		= "";
		//String appCode  	= "hr21";
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
			
			MasterTableForm masterTableForm = (MasterTableForm) form;
			String applTypeCode	= masterTableForm.getApplTypeCode();
			String grpCode 		= masterTableForm.getGrpCode();

			DBLogin dbLogin = new DBLogin();			
			if (!dbLogin.CheckAppAuth(loginId, applTypeCode+"00", "mant")) {
				request.setAttribute("alertMessage","You don't have authorize.");
				return mapping.findForward("alertmsg");
			}

			DBMasterTable dbMasterTable = new DBMasterTable();
			List grpList = dbMasterTable.GetMasterTableHDList(applTypeCode, grpCode, "");	
			if (grpList.size() == 1) {
				MasterTableForm typeInfo = (MasterTableForm) grpList.get(0);
				masterTableForm.setGrpName(typeInfo.getGrpName());
				masterTableForm.setTypeCode("");
				
			} else {
				forwardText = "success";
				request.setAttribute("grpList", grpList);
			}
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward(forwardText);
	}
	public ActionForward update(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
	throws Exception {	//01-10-2012
		String forwardText 	= "success";
		String loginId 		= "";
		//String appCode  	= "hr21";
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
			
			MasterTableForm masterTableForm = (MasterTableForm) form;
			String applTypeCode	= masterTableForm.getApplTypeCode();
			String grpCode 		= masterTableForm.getGrpCode();
			String typeCode		= masterTableForm.getTypeCode();
			
			DBLogin dbLogin = new DBLogin();			
			if (!dbLogin.CheckAppAuth(loginId, applTypeCode+"00", "mant")) {
				request.setAttribute("alertMessage","You don't have authorize.");
				return mapping.findForward("alertmsg");
			}

			DBMasterTable dbMasterTable = new DBMasterTable();
			List grpList = dbMasterTable.GetMasterTableHDList(applTypeCode, "", "");			
			if (grpList.size() > 0) request.setAttribute("grpList", grpList);

			List typeList = dbMasterTable.GetMasterTableDTList(grpCode, typeCode, "", "");
			if (typeList.size() == 1) {
				MasterTableForm typeInfo = (MasterTableForm) typeList.get(0);
				masterTableForm.setGrpName(typeInfo.getGrpName());
				masterTableForm.setEnName(typeInfo.getEnName());
				masterTableForm.setThName(typeInfo.getThName());
				masterTableForm.setStatus(typeInfo.getStatus());
				
				forwardText = "update";
			} else {
				
				request.setAttribute("typeList", typeList);
			}
			request.setAttribute("grpCode", grpCode);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward(forwardText);
	}
}