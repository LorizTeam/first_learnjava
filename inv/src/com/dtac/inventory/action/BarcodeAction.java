/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.dtac.inventory.action;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping; 
import com.dtac.inventory.*; 
import com.dtac.inventory.data.DBMaterial;
import com.dtac.inventory.form.MaterialForm;
 



/** 
 * MyEclipse Struts
 * Creation date: 06-08-2007
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class BarcodeAction extends Action {
	/*
	 * Generated Methods
	 */

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		MaterialForm mform = (MaterialForm) form;
		String forwardText = null;
		HttpSession session = request.getSession();
		String userid   = (String) session.getAttribute("loginId");
		String text_1 = mform.getText_1();
		String text_2 = mform.getText_2();
		String text_3 = mform.getText_3();
		String text_4 = mform.getText_4();
		String text_5 = mform.getText_5();
		String text_6 = mform.getText_6();
		String text_7 = mform.getText_7();
		String text_8 = mform.getText_8();
		String text_9 = mform.getText_9();
		String text_10 = mform.getText_10();
		String text_11 = mform.getText_11();
		String text_12 = mform.getText_12();
		String text_13 = mform.getText_13();
		String text_14 = mform.getText_14();
		String text_15 = mform.getText_15();
		String text_16 = mform.getText_16();
		String text_17 = mform.getText_17();
		String text_18 = mform.getText_18();
		String text_19 = mform.getText_19();
		String text_20 = mform.getText_20();
		String text_21 = mform.getText_21();
//		 retrive page_id

			
			DBMaterial bkMasterDB = new DBMaterial();
			
			bkMasterDB.AddMaterialBarcode(userid, text_1, text_2, text_3, text_4, text_5, text_6, text_7, text_8, text_9, text_10, text_11, text_12, text_13, text_14, text_15, text_16, text_17, text_18, text_19, text_20, text_21);
		     request.setAttribute("userid", userid);
       
		     forwardText = "print";
		return mapping.findForward(forwardText);
	}
}