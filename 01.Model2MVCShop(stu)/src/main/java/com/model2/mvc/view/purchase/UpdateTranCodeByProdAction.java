package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdateTranCodeByProdAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		String tranCode=request.getParameter("tranCode");
		
		System.out.println("&&UTCBP tranCode"+tranCode);
		PurchaseVO purchaseVO=new PurchaseVO();
		purchaseVO.setTranCode(tranCode);
		
		PurchaseService service=new PurchaseServiceImpl();
		service.updateTranCodeByProd(prodNo, purchaseVO);
		
		return "redirect:/listProduct.do?menu=manage";
	}

}
