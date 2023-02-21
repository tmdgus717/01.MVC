package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;


public class UpdateProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		
		ProductVO prodVO=new ProductVO();
		prodVO.setProdNo(prodNo);
		prodVO.setProdName(request.getParameter("prodName"));
		prodVO.setProdDetail(request.getParameter("prodDetail"));
		prodVO.setManuDate(request.getParameter("manuDate"));
		prodVO.setPrice(Integer.parseInt(request.getParameter("price")));
		prodVO.setFileName(request.getParameter("fileName"));
		
		ProductService service=new ProductServiceImpl();
		service.updateProduct(prodVO);
		
		HttpSession session=request.getSession();
//		int sessionId=((ProductVO)session.getAttribute("user")).getProdNo();
//	
//		if(sessionId == prodNo){
//			session.setAttribute("prod", prodVO);
//		}
		session.setAttribute("prod", prodVO);
		return "forward:/getProduct.do?prodNo="+prodNo;
	}

}
