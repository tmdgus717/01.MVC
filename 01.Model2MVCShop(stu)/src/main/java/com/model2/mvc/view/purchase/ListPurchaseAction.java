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
		SearchVO searchVO=new SearchVO(); //서치 인스턴스 생성
		
		int page=1; //default page == 1 로 설정
		if(request.getParameter("page") != null) // page값이 들어오면
			page=Integer.parseInt(request.getParameter("page")); //page 변환 
		
		searchVO.setPage(page); //1.page = 1 => 2.page값은 request.getParameter("page")
 		searchVO.setSearchCondition(request.getParameter("searchCondition")); //1.null? 
		searchVO.setSearchKeyword(request.getParameter("searchKeyword")); //1.null => 2.user
		
		String pageUnit=getServletContext().getInitParameter("pageSize"); // xml을 통해 들어옴 "3"
		searchVO.setPageUnit(Integer.parseInt(pageUnit)); // int 형으로 변환 후 넣어줌
		
		PurchaseService service=new PurchaseServiceImpl(); //구매 인스턴스 생성
		HttpSession session=request.getSession();
		UserVO userVO=(UserVO)session.getAttribute("user");
		System.out.println(userVO.getUserId());
		HashMap<String,Object> map=service.getPurchaseList(searchVO,userVO.getUserId()); //구매리스트
		//		return userDAO.getUserList(searchVO); 
		//		map에 count=total,list=user정보
		request.setAttribute("map", map); //map => ["count"=total,"list"=UserList]
		request.setAttribute("searchVO", searchVO); //searchVO
		
		//searchVO와 map 정보를 가지고 jsp로이동
		return "forward:/purchase/listPurchase.jsp"; // forward형식으로 listUser로 간다.
	}

}
