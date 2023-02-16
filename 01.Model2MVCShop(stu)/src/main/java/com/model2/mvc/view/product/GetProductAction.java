package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;

public class GetProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String prodNo=request.getParameter("prodNo");
		System.out.println("prodNo ¿‘¥œ¥Ÿ : "+prodNo);
		ProductService service=new ProductServiceImpl();
		ProductVO vo=service.getProduct(Integer.parseInt(prodNo));
		
		request.setAttribute("vo", vo);
		
		return "forward:/product/getProduct.jsp";
	}

}
