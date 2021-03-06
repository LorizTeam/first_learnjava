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

import com.dtac.admin.data.DBMasterTable;
import com.dtac.inventory.data.DBMaterial;
import com.dtac.inventory.data.DBMaterialType;
import com.dtac.inventory.form.MaterialForm;
import com.dtac.inventory.form.MaterialTakeForm;
import com.dtac.utils.DateUtil;
/** 
 * MyEclipse Struts
 * Creation date: 07-05-2012
 */
public class MaterialLOCKSetupAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
	throws Exception { //18-04-2012
		String forwardText  = "success";
		String loginId 		= "";
		//String appCode  	= "sa11";
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
			/*DBLogin dbLogin = new DBLogin();			
			if (!dbLogin.CheckAppAuth(loginId,appCode,"disp")) {
				request.setAttribute("alertMessage","You don't have authorize.");
				return mapping.findForward("alertmsg");
			}*/
			
			MaterialForm materialForm = (MaterialForm) form;			
			materialForm.initial();
			
			String formName  = request.getParameter("formName");
			String matCode		= "";
			String matName		= "";
			String matSearchName= "";
			String matTypeCode	= ""; 
			
			String matGrpCode   = "";
			if (request.getParameter("matGrp") != null) matGrpCode	= request.getParameter("matGrp"); 
			String matStatus	= "AC";
	 
			
			DBMaterialType dbMaterialType = new DBMaterialType();
			String matTypeName = dbMaterialType.GetMaterialTypeName(matTypeCode);
			List matTypeList = dbMaterialType.GetMaterialTypeList("");
			if (matTypeList.size() > 0) request.setAttribute("matTypeList", matTypeList);

			String matGrpName = dbMaterialType.GetMaterialGrpName(matTypeCode, matGrpCode, "");
			List matGrpList = dbMaterialType.GetMaterialGrpList("", "", "");
			if (matGrpList.size() > 0 && !matTypeCode.equals("")) request.setAttribute("matGrpList", matGrpList);
			
			DateUtil dateUtil = new DateUtil();
			String docDate = dateUtil.CnvToDDMMYYYYThaiYear(dateUtil.CnvToYYYYMMDD(dateUtil.curDate(), '/'));

		//	materialTakeForm.setDocNo("*****");
			materialForm.setDate(docDate);

			DBMaterial dbMaterial = new DBMaterial();
			
			List materialList = dbMaterial.GetMaterialListLock("", "", "", "","", "", "", "", "", "", "", "");
			if (materialList.size() > 0) request.setAttribute("materialList", materialList);
			
			DBMasterTable dbMasterTable = new DBMasterTable();
			List unitList = dbMasterTable.GetMasterTableDTList("unit", "", "", "AC");
			if (unitList.size() > 0) request.setAttribute("unitList", unitList);
			
			List brandList = dbMasterTable.GetMasterTableDTList("bran", "", "", "AC");
			if (brandList.size() > 0) request.setAttribute("brandList", brandList);
			
			List colorList = dbMasterTable.GetMasterTableDTList("colo", "", "", "AC");
			if (colorList.size() > 0) request.setAttribute("colorList", colorList);

			List stuffList = dbMasterTable.GetMasterTableDTList("stuf", "", "", "AC");
			if (stuffList.size() > 0) request.setAttribute("stuffList", stuffList);
			
			List sizeList = dbMasterTable.GetMasterTableDTList("size", "", "", "AC");
			if (sizeList.size() > 0) request.setAttribute("sizeList", sizeList);
			
			String docTypeCode 	= "";
			if (session.getAttribute("docTypeCode") != null) docTypeCode = (String) session.getAttribute("docTypeCode");
			
			materialForm.setDocTypeCode(docTypeCode);
			materialForm.setMatTypeName(matTypeName);
			materialForm.setMatTypeCode(matTypeCode);
			materialForm.setMatGrpCode(matGrpCode);
			materialForm.setMatGrpName(matGrpName);
			materialForm.setMatStatus(matStatus);
			materialForm.setFormName(formName);	
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}	
		return mapping.findForward(forwardText);
	}
}