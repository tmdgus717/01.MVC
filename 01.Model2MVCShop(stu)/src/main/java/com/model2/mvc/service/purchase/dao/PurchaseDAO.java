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
		
		Connection con = DBUtil.getConnection(); //DB����
		System.out.println(userId);
		String sql = "select * from  transaction";
		sql += " where buyer_id = '"+userId+"'";
		sql += " order by tran_no";
		System.out.println(sql);
		PreparedStatement stmt = 
			con.prepareStatement(	sql,
														ResultSet.TYPE_SCROLL_INSENSITIVE,
														ResultSet.CONCUR_UPDATABLE);
		//ResultSet.TYPE_SCROLL_INSENSTIVE => rs.last�� ����ϱ����� �ʼ�!!
		// => Ŀ���� ������ ���� �ٽ� �ǵ��� �� �ִ�.
		// => ������� ���� ����.
		//ResultSet.CONCUR_UPDATABLE =>  ������ ������Ʈ �����ϵ���
		ResultSet rs = stmt.executeQuery(); //����

		rs.last(); //������ ��ġ�� �̵�
		int total = rs.getRow(); //�� ���� ������ ��, total ����
		System.out.println("�ο��� ��:" + total); // total row Ȯ��

		HashMap<String,Object> map = new HashMap<String,Object>(); //map������ �ؽø� �ν��Ͻ�����
		map.put("count", new Integer(total));//"count" = (new ����Ÿ������ ��ȯ�� ������Ʈ�� �־���)

		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
		//������ * �����ִ°� - �����ִ°� + 1
		//1 * 3 - 3 + 1 = 1 ,2*3-3+1 = 4, 3*3-3+1 = 7,
		//absolute(1) => 1�� �ε����� Ŀ�� �̵�
		//absolute = Ŀ���̵� 1,4,7,10 ...
		System.out.println("searchVO.getPage():" + searchVO.getPage()); //�������� ������
		System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit()); //�����ִ� ����

		ArrayList<PurchaseVO> list = new ArrayList<PurchaseVO>(); //list = ��̸���Ʈ �ν��Ͻ� ���� 
		if (total > 0) {  //���� ������ ������.
			for (int i = 0; i < searchVO.getPageUnit(); i++) { //�����ִ� ���� ����ŭ �ݺ�
				PurchaseVO vo = new PurchaseVO(); //UserVO�� set�Ѵ�.
			
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
				list.add(vo); //����Ʈ�� �ִ´�.
				if (!rs.next()) // rs.next �������� ������ true=> !true= false ��� break �н�
					break; //rs.next �������� ������ false => !false = true ��� break;
			}
		} 
		//list = [vo,vo,vo]
		System.out.println("list.size() : "+ list.size()); //����Ʈ ������ ���
		map.put("list", list); //map�� "list" = list�ִ´�.
		System.out.println("map().size() : "+ map.size()); //�ʻ����� ���

		con.close(); //db ����
			
		return map; //map ���� ��ȯ ["count"=Integer total,"list"=ArrayList list(User)]
	}
}
