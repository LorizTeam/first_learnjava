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
import com.dtac.admin.data.DBMasterTable;
import com.dtac.center.data.DBAddress;
import com.dtac.inventory.data.DBVender;
import com.dtac.inventory.form.VenderForm;
/**
 * MyEclipse Struts 
 * Creation date: 12-04-2012
 */
public class VenderListAction extends LookupDispatchAction {

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("venderform.button.adddata", "adddata");
		map.put("venderform.button.search", "search");
		map.put("venderform.button.view", 	"view");
		return map;
	}

	public ActionForward search(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
	throws Exception { //12-07-2012
		String forwardText 	= "success";
		String loginId 		= "";
		String appCode 		= "mm07";
		try {
			HttpSession session = request.getSession();
			if (session.isNew()) {
				session.invalidate();
				request.setAttribute("alertMessage", "Session Timeout. Login again.");
				return mapping.findForward("relogin");
			} else {
				loginId = (String) session.getAttribute("loginId");
				if (loginId == null) {
					request.setAttribute("alertMessage", "Please Login.");
					return mapping.findForward("relogin");
				}
			}
			DBLogin dbLogin = new DBLogin();
			if (!dbLogin.CheckAppAuth(loginId, appCode, "disp")) {
				request.setAttribute("alertMessage", "You don't have authorize.");
				return mapping.findForward("alertmsg");
			}

			VenderForm venderForm = (VenderForm) form;
			String venderCode 	= new String(venderForm.getVenderCode().getBytes("ISO8859_1"), "utf-8");
			String venderName 	= new String(venderForm.getVenderName().getBytes("ISO8859_1"), "utf-8");
			String searchName 	= new String(venderForm.getSearchName().getBytes("ISO8859_1"), "utf-8");
			String venderTypeCode = venderForm.getVenderTypeCode();
			String status		= venderForm.getStatus();
			
			DBVender dbVender = new DBVender(); 
			List venderList = dbVender.GetVenderMasterList(venderCode, venderName, searchName, venderTypeCode, status);
			if (venderList.size() > 0) request.setAttribute("venderList", venderList);

			DBMasterTable dbMasterTable = new DBMasterTable();
			List vendTypeList = dbMasterTable.GetMasterTableDTList("vdty", "", "", "AC");
			if (vendTypeList.size() > 0) request.setAttribute("vendTypeList", vendTypeList);

			venderForm.setVenderCode(venderCode);
			venderForm.setVenderName(venderName);
			venderForm.setSearchName(searchName);			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return mapping.findForward(forwardText);
	}
	public ActionForward view(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
	throws Exception { // 12-07-2012
		String forwardText 	= "view";
		String loginId 		= "";
		String appCode 		= "mm07";
		try {
			HttpSession session = request.getSession();
			if (session.isNew()) {
				session.invalidate();
				request.setAttribute("alertMessage", "Session Timeout. Login again.");
				return mapping.findForward("relogin");
			} else {
				loginId = (String) session.getAttribute("loginId");
				if (loginId == null) {
					request.setAttribute("alertMessage", "Please Login.");
					return mapping.findForward("relogin");
				}
			}
			DBLogin dbLogin = new DBLogin();
			if (!dbLogin.CheckAppAuth(loginId, appCode, "disp")) {
				request.setAttribute("alertMessage", "You don't have authorize.");
				return mapping.findForward("alertmsg");
			}

			VenderForm venderForm = (VenderForm) form;
			String venderCode 	= new String (venderForm.getVenderCode().getBytes("ISO8859_1"), "utf-8");
//			String custName 	= new String(customerForm.getCustName().getBytes("ISO8859_1"), "utf-8");
//			String searchName 	= new String(customerForm.getSearchName().getBytes("ISO8859_1"), "utf-8");
			String venderTypeCode	= venderForm.getVenderTypeCode();
			String pic1			= "";
			String pic2			= "";
			String pic3			= "";
			String pic4			= "";
			String pic5			= "";	
			
			DBVender dbVender= new DBVender();
			List venderList = dbVender.GetVenderMaster(venderCode);
			if (venderList.size() == 1) {
				VenderForm venInfo = (VenderForm) venderList.get(0);
				venderForm.setVenderCode(venInfo.getVenderCode());
				venderForm.setVenderName(venInfo.getVenderName());
				venderForm.setSearchName(venInfo.getSearchName());
				venderForm.setVenderTypeCode(venInfo.getVenderTypeCode());
				venderForm.setVenderTaxId(venInfo.getVenderTaxId());
				venderForm.setCreditLimit(venInfo.getCreditLimit());
				venderForm.setCreditAvail(venInfo.getCreditAvail());		
				venderForm.setStatus(venInfo.getStatus());
				
				venderForm.setPic1(venInfo.getPic1());
				venderForm.setPic2(venInfo.getPic2());
				venderForm.setPic3(venInfo.getPic3());
				venderForm.setPic4(venInfo.getPic4());
				venderForm.setPic5(venInfo.getPic5());
				
				pic1= venInfo.getPic1();
				pic2= venInfo.getPic2();
				pic3= venInfo.getPic3();
				pic4= venInfo.getPic4();
				pic5= venInfo.getPic5();

				DBAddress dbAddress = new DBAddress();
				List regiAddrList = dbAddress.GetAddressList("vend", venderCode, "regi", "01");
				if (regiAddrList.size() == 1) {
					venderForm.set_regiAddress(regiAddrList);
				} else
					venderForm.regi_initial();
								
			} else {
				forwardText = "success";
				request.setAttribute("alertMessage", "select Vender.");
				
				venderList = dbVender.GetVenderMasterList("", "", "", venderTypeCode, "AC");
				if (venderList.size() > 0) request.setAttribute("venderList", venderList);		
				
			}

			DBMasterTable dbMasterTable = new DBMasterTable();			
			List vendTypeList = dbMasterTable.GetMasterTableDTList("vdty", "", "", "AC");
			if (vendTypeList.size() > 0) request.setAttribute("vendTypeList", vendTypeList);

			venderForm.setVenderCode(venderCode);
			
			request.setAttribute("pic1", pic1);
			request.setAttribute("pic2", pic2);
			request.setAttribute("pic3", pic3);
			request.setAttribute("pic4", pic4);
			request.setAttribute("pic5", pic5);
			request.setAttribute("venderCode", venderCode);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return mapping.findForward(forwardText);
	}
	public ActionForward adddata(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
	throws Exception { // 12-07-2012
		String forwardText 	= "addnew";
		String loginId 		= "";
		String appCode 		= "mm07";
		try {
			HttpSession session = request.getSession();
			if (session.isNew()) {
				session.invalidate();
				request.setAttribute("alertMessage", "Session Timeout. Login again.");
				return mapping.findForward("relogin");
			} else {
				loginId = (String) session.getAttribute("loginId");
				if (loginId == null) {
					request.setAttribute("alertMessage", "Please Login.");
					return mapping.findForward("relogin");
				}
			}
			DBLogin dbLogin = new DBLogin();
			if (!dbLogin.CheckAppAuth(loginId, appCode, "mant")) {
				request.setAttribute("alertMessage", "You don't have authorize.");
				return mapping.findForward("alertmsg");
			}

			VenderForm venderForm = (VenderForm) form;			
			String venderCode = new String(venderForm.getVenderCode().getBytes("ISO8859_1"), "utf-8");
			String venderName = new String(venderForm.getVenderName().getBytes("ISO8859_1"), "utf-8");
			String searchName = new String(venderForm.getSearchName().getBytes("ISO8859_1"), "utf-8");

			DBMasterTable dbMasterTable = new DBMasterTable();
			List vendTypeList = dbMasterTable.GetMasterTableDTList("vdty", "", "", "AC");
			if (vendTypeList.size() > 0) request.setAttribute("vendTypeList", vendTypeList);

			venderForm.setVenderCode(venderCode);
			venderForm.setVenderName(venderName);
			venderForm.setSearchName(searchName);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return mapping.findForward(forwardText);
	}
}