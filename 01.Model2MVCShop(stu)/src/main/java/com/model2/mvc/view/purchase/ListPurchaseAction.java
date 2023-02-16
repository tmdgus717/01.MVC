package com.model2.mvc.view.purchase;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;


public class ListPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SearchVO searchVO=new SearchVO(); //��ġ �ν��Ͻ� ����
		
		int page=1; //default page == 1 �� ����
		if(request.getParameter("page") != null) // page���� ������
			page=Integer.parseInt(request.getParameter("page")); //page ��ȯ 
		
		searchVO.setPage(page); //1.page = 1 => 2.page���� request.getParameter("page")
 		searchVO.setSearchCondition(request.getParameter("searchCondition")); //1.null? 
		searchVO.setSearchKeyword(request.getParameter("searchKeyword")); //1.null => 2.user
		
		String pageUnit=getServletContext().getInitParameter("pageSize"); // xml�� ���� ���� "3"
		searchVO.setPageUnit(Integer.parseInt(pageUnit)); // int ������ ��ȯ �� �־���
		
		PurchaseService service=new PurchaseServiceImpl(); //���� �ν��Ͻ� ����
		HttpSession session=request.getSession();
		UserVO userVO=(UserVO)session.getAttribute("user");
		System.out.println(userVO.getUserId());
		HashMap<String,Object> map=service.getPurchaseList(searchVO,userVO.getUserId()); //���Ÿ���Ʈ
		//		return userDAO.getUserList(searchVO); 
		//		map�� count=total,list=user����
		request.setAttribute("map", map); //map => ["count"=total,"list"=UserList]
		request.setAttribute("searchVO", searchVO); //searchVO
		
		//searchVO�� map ������ ������ jsp���̵�
		return "forward:/purchase/listPurchase.jsp"; // forward�������� listUser�� ����.
	}

}
