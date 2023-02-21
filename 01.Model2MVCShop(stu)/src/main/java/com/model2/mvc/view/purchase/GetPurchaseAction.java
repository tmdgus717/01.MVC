package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;


public class GetPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		int tranNo=Integer.parseInt(request.getParameter("tranNo"));
		System.out.println("tranNo �Դϴ� : "+tranNo);
		
		PurchaseService service=new PurchaseServiceImpl();
		PurchaseVO vo=service.getPuchase(tranNo);
		
		request.setAttribute("vo", vo);

		return "forward:/purchase/getPurchase.jsp";
	}

}
