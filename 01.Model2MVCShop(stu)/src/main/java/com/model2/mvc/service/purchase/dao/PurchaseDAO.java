package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.dao.ProductDAO;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;

public class PurchaseDAO {
	//association
	UserService users = new UserServiceImpl();
	ProductService prods = new ProductServiceImpl();
	
	public PurchaseDAO() {
	}
	public void insertPurchase(PurchaseVO purchaseVO) throws Exception {
	
		Connection con = DBUtil.getConnection();
		
		String sql = "insert into transaction values (seq_transaction_tran_no.NEXTVAL"
				+ ",?,?,?,?,?"
				+ ",?,?,?,sysdate,?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, purchaseVO.getPurchaseProd().getProdNo());
		stmt.setString(2, purchaseVO.getBuyer().getUserId());
		stmt.setString(3, purchaseVO.getPaymentOption());
		stmt.setString(4, purchaseVO.getReceiverName());
		stmt.setString(5, purchaseVO.getReceiverPhone());
		stmt.setString(6, purchaseVO.getDivyAddr());
		stmt.setString(7, purchaseVO.getDivyRequest());
		stmt.setString(8, purchaseVO.getTranCode());
		stmt.setString(9, purchaseVO.getDivyDate());
		
		stmt.executeUpdate();
		System.out.println(sql);
		con.close();
	}
	
	public PurchaseVO findPurchase(int tran_no) throws Exception{
		Connection con = DBUtil.getConnection();
		String sql = "select * from transaction where tran_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tran_no);

		ResultSet rs = stmt.executeQuery();

		PurchaseVO purchaseVO = null;
		while (rs.next()) {
			purchaseVO = new PurchaseVO();
			purchaseVO.setTranNo(Integer.parseInt(rs.getString("tran_no")));;
	
		}
		
		con.close();

		return purchaseVO;
		
	}
	
	public HashMap<String,Object> getPurchaseList(SearchVO searchVO,String userId) throws Exception {
		
		Connection con = DBUtil.getConnection(); //DB연결
		System.out.println(userId);
		String sql = "select * from  transaction";
		sql += " where buyer_id = '"+userId+"'";
		sql += " order by tran_no";
		System.out.println(sql);
		PreparedStatement stmt = 
			con.prepareStatement(	sql,
														ResultSet.TYPE_SCROLL_INSENSITIVE,
														ResultSet.CONCUR_UPDATABLE);
		//ResultSet.TYPE_SCROLL_INSENSTIVE => rs.last를 사용하기위해 필수!!
		// => 커서가 지나간 다음 다시 되돌릴 수 있다.
		// => 변경사함 감지 못함.
		//ResultSet.CONCUR_UPDATABLE =>  데이터 업데이트 가능하도록
		ResultSet rs = stmt.executeQuery(); //실행

		rs.last(); //마지막 위치로 이동
		int total = rs.getRow(); //행 숫자 가져옴 즉, total 설정
		System.out.println("로우의 수:" + total); // total row 확인

		HashMap<String,Object> map = new HashMap<String,Object>(); //map변수에 해시맵 인스턴스생성
		map.put("count", new Integer(total));//"count" = (new 래퍼타입으로 변환후 오브젝트에 넣어줌)

		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
		//페이지 * 보여주는값 - 보여주는값 + 1
		//1 * 3 - 3 + 1 = 1 ,2*3-3+1 = 4, 3*3-3+1 = 7,
		//absolute(1) => 1번 인덱스로 커서 이동
		//absolute = 커서이동 1,4,7,10 ...
		System.out.println("searchVO.getPage():" + searchVO.getPage()); //페이지값 가져옴
		System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit()); //보여주는 갯수

		ArrayList<PurchaseVO> list = new ArrayList<PurchaseVO>(); //list = 어레이리스트 인스턴스 생성 
		if (total > 0) {  //값이 있으면 돌린다.
			for (int i = 0; i < searchVO.getPageUnit(); i++) { //보여주는 갯수 수만큼 반복
				PurchaseVO vo = new PurchaseVO(); //UserVO에 set한다.
			
				vo.setTranNo(rs.getInt("tran_no"));
				System.out.println("1");
				vo.setPurchaseProd(prods.getProduct(rs.getInt("prod_no")));
				System.out.println("2");
				vo.setPaymentOption(rs.getString("payment_option"));
				System.out.println("3");
				vo.setBuyer(users.getUser(rs.getString("buyer_id")));
				System.out.println("4");
				vo.setReceiverName(rs.getString("receiver_name"));
				System.out.println("5");
				vo.setReceiverPhone(rs.getString("receiver_phone"));
				System.out.println("6");
				vo.setDivyAddr(rs.getString("DEMAILADDR"));
				System.out.println("7");
				vo.setDivyRequest(rs.getString("dlvy_request"));
				System.out.println("8");
				vo.setTranCode(rs.getString("tran_status_code"));
				System.out.println("9");
				vo.setOrderDate(rs.getDate("order_data"));
				System.out.println("10");
				vo.setDivyDate(rs.getString("dlvy_date"));
				System.out.println("11");
				list.add(vo); //리스트에 넣는다.
				if (!rs.next()) // rs.next 다음값이 있으면 true=> !true= false 고로 break 패스
					break; //rs.next 다음값이 없으면 false => !false = true 고로 break;
			}
		} 
		//list = [vo,vo,vo]
		System.out.println("list.size() : "+ list.size()); //리스트 사이즈 출력
		map.put("list", list); //map에 "list" = list넣는다.
		System.out.println("map().size() : "+ map.size()); //맵사이즈 출력

		con.close(); //db 종료
			
		return map; //map 으로 반환 ["count"=Integer total,"list"=ArrayList list(User)]
	}
}
