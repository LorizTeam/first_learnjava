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
import com.dtac.inventory.data.DBMaterialType;
import com.dtac.inventory.data.DBWarehouseAuth;
import com.dtac.inventory.form.MaterialTypeForm;
/** 
 * MyEclipse Struts
 * Creation date: 12-04-2012
 */
public class MaterialGrpAddNewAction extends LookupDispatchAction {

	protected Map getKeyMethodMap() {
		Map map = new HashMap();		
		map.put("mattypeform.button.adddata",	"adddata");
		map.put("mattypeform.button.cancel",	"cancel");
		return map;
	}

	public ActionForward cancel(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
	throws Exception { //13-04-2012
		String forwardText = "success";
		String loginId 		= "";
		String appCode  	= "mm21";
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

			MaterialTypeForm materialTypeForm = (MaterialTypeForm) form;			
			String matTypeCode	= materialTypeForm.getMatTypeCode();
			String matGrpCode 	= materialTypeForm.getMatGrpCode();
			String matGrpStatus = materialTypeForm.getMatGrpStatus();
			
			DBMaterialType dbMaterialType = new DBMaterialType();
			List matTypeList = dbMaterialType.GetMaterialTypeList("");
			if (matTypeList.size() > 0) request.setAttribute("matTypeList", matTypeList);

			List matGrpList = dbMaterialType.GetMaterialGrpList("", "", "");
			if (matGrpList.size() > 0) request.setAttribute("matGrpList", matGrpList);
							
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward(forwardText);
	}
	public ActionForward adddata(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
	throws Exception { //12-04-2012
		String forwardText = "success";
		String loginId 		= "";
		String appCode  	= "mm21";
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
       			
			
			MaterialTypeForm materialTypeForm = (MaterialTypeForm) form;			
			String matTypeCode	= materialTypeForm.getMatTypeCode();
			String matGrpCode = materialTypeForm.getMatGrpCode();
			String matGrpName = new String(materialTypeForm.getMatGrpName().getBytes("ISO8859_1"),"utf-8");
			String matGrpStatus = materialTypeForm.getMatGrpStatus();
			
			DBMaterialType dbAddSupMat = new DBMaterialType();
			dbAddSupMat.AddSubMaterialGrp(matTypeCode, matGrpCode, matGrpName, "AC");
			
			List matTypeList = dbAddSupMat.GetMaterialTypeList("");
			if (matTypeList.size() > 0) request.setAttribute("matTypeList", matTypeList);
			
			List matGrpList = dbAddSupMat.GetMaterialGrpList("", "", "");
			if (matGrpList.size() > 0) request.setAttribute("matGrpList", matGrpList);
			
			DBWarehouseAuth dbWarehouseAuth = new DBWarehouseAuth();
			List warehouseAuth = dbWarehouseAuth.GetWarehouseAuth("", loginId, "AC");
			if (warehouseAuth.size() > 0) request.setAttribute("warehouseAuth", warehouseAuth);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward(forwardText);
	}

}