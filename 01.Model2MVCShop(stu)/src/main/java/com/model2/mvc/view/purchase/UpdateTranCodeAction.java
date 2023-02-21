package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdateTranCodeAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub

		System.out.println("UpdateTranCodeACtion START....");
		int tranNo=Integer.parseInt(request.getParameter("tranNo"));
		String tranCode=request.getParameter("tranCode");

		System.out.println("&&UTCBP tranCode=="+tranNo);
		System.out.println("&&UTCBP tranCode=="+tranCode);
		PurchaseVO purchaseVO=new PurchaseVO();
		purchaseVO.setTranNo(tranNo);
		purchaseVO.setTranCode(tranCode);
		
		PurchaseService service=new PurchaseServiceImpl();
		service.updateTranCode(purchaseVO);
		
		return "redirect:/listPurchase.do";
	}

}
