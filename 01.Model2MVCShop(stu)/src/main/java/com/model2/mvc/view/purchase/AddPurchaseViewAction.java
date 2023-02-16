package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.vo.UserVO;

public class AddPurchaseViewAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		System.out.println("prodNo ¿‘¥œ¥Ÿ : "+prodNo);
		ProductService service=new ProductServiceImpl();
		ProductVO pvo=service.getProduct(prodNo);
		

		HttpSession session = request.getSession();
		UserVO uvo = (UserVO)session.getAttribute("user");
		System.out.println(uvo);
		
		PurchaseVO purchaseVO = new PurchaseVO();
		purchaseVO.setPurchaseProd(pvo);
		purchaseVO.setBuyer(uvo);
		session.setAttribute("purchase", purchaseVO);
				
		return "forward:/purchase/addPurchaseView.jsp";
	}

}
