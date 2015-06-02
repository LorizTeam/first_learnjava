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
import com.dtac.inventory.data.DBMaterial;
import com.dtac.inventory.data.DBMaterialType;
import com.dtac.inventory.data.DBWarehouseAuth;
import com.dtac.inventory.form.MaterialForm;
import com.dtac.inventory.form.MaterialTakeForm;
import com.dtac.utils.DateUtil;
/** 
 * MyEclipse Struts
 * Creation date: 12-04-2012
 */
public class MaterialListStartAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
	throws Exception {	//30-04-2012
		String forwardText 	= "success";
		String loginId 		= "";
		String appCode  	= "mm23";		
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

			MaterialForm materialForm = (MaterialForm) form;
			materialForm.initial();
		//	MaterialTakeForm materialTakeForm = (MaterialTakeForm) form;
			String wahoCode 	= materialForm.getStock1();
			if(wahoCode==null) wahoCode = "";
			String alertMessage = "";
			DBWarehouseAuth dbWarehouseAuth = new DBWarehouseAuth();
			
			List warehouseAuth = dbWarehouseAuth.GetWarehouseAuth(wahoCode, loginId, "AC");
			if (warehouseAuth.size() == 1) {				
				alertMessage = "คุณไม่มีสิทธิ์ทำงานคลังสินค้า";
			} else {
				warehouseAuth = dbWarehouseAuth.GetWarehouseAuthList("A1","B1","C1","D1","", loginId, "AC");
				if (warehouseAuth.size() > 0) request.setAttribute("warehouseAuth", warehouseAuth);
			}
			
			String matTypeCode = "rm";
			String matGrpCode	= "";
			if (session.getAttribute("matTypeCode") != null) matTypeCode = (String) session.getAttribute("matTypeCode");
			if (matTypeCode.equals("rm")) matGrpCode = "01";
			
			DBMaterialType dbMaterialType = new DBMaterialType();
			List matTypeList = dbMaterialType.GetMaterialTypeList("");
			if (matTypeList.size() > 0) request.setAttribute("matTypeList", matTypeList);

			List matGrpList = dbMaterialType.GetMaterialGrpList("", "", "AC");
			if (matGrpList.size() > 0) request.setAttribute("matGrpList", matGrpList);

			DBMaterial dbMaterial = new DBMaterial();
			List materialList = dbMaterial.GetMaterialList("", "","", "", "", "", "", "", "", "", "", "");
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
			
			DateUtil dateUtil = new DateUtil();
			String curDate = dateUtil.CnvToDDMMYYYYThaiYear(dateUtil.CnvToYYYYMMDD(dateUtil.curDate(), '/'));
			materialForm.setDate(curDate);
			materialForm.setMatTypeCode(matTypeCode);
			materialForm.setMatGrpCode(matGrpCode);
			materialForm.setMatStatus("AC");
			
			String textBoxId = "start";
			request.setAttribute("textBoxId", textBoxId);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return  mapping.findForward(forwardText);
	}
}