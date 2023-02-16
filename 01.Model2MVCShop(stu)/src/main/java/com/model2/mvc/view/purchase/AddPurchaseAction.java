package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;

public class AddPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		PurchaseVO purchaseVO = new PurchaseVO();

		HttpSession session = request.getSession();
		PurchaseVO vo = (PurchaseVO)session.getAttribute("purchase");
		

	
//		HttpSession session = request.getSe ssion();
//		PurchaseVO purchaseVO2 = (PurchaseVO)session.getAttribute("purchase");
		String tmp_date=request.getParameter("receiverDate");
		String date = tmp_date.replaceAll("-","");
		
		purchaseVO.setPurchaseProd(vo.getPurchaseProd());
		purchaseVO.setBuyer(vo.getBuyer());
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDivyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDivyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setTranCode("1");
		purchaseVO.setDivyDate(date);
		
		System.out.println(purchaseVO);
		
		PurchaseService purchaseService = new PurchaseServiceImpl();
		purchaseService.addPurchase(purchaseVO);
		
		session.setAttribute("purchaseVO", purchaseVO);
	
		return "redirect:/purchase/addPurchase.jsp";
	}
}
