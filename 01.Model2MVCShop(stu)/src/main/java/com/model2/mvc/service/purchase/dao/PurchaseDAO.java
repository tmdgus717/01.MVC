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
			purchaseVO.setTranNo(rs.getInt("tran_no"));
			purchaseVO.setPurchaseProd(prods.getProduct(rs.getInt("prod_no")));
			purchaseVO.setPaymentOption(rs.getString("payment_option"));
			purchaseVO.setBuyer(users.getUser(rs.getString("buyer_id")));
			purchaseVO.setReceiverName(rs.getString("receiver_name"));
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
			purchaseVO.setDivyAddr(rs.getString("DEMAILADDR"));
			purchaseVO.setDivyRequest(rs.getString("dlvy_request"));
			purchaseVO.setTranCode(rs.getString("tran_status_code"));
			purchaseVO.setOrderDate(rs.getDate("order_data"));
			purchaseVO.setDivyDate(rs.getString("dlvy_date"));
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
				vo.setPurchaseProd(prods.getProduct(rs.getInt("prod_no")));
				vo.setPaymentOption(rs.getString("payment_option"));
				vo.setBuyer(users.getUser(rs.getString("buyer_id")));
				vo.setReceiverName(rs.getString("receiver_name"));
				vo.setReceiverPhone(rs.getString("receiver_phone"));
				vo.setDivyAddr(rs.getString("DEMAILADDR"));
				vo.setDivyRequest(rs.getString("dlvy_request"));
				vo.setTranCode(rs.getString("tran_status_code"));
				vo.setOrderDate(rs.getDate("order_data"));
				vo.setDivyDate(rs.getString("dlvy_date"));
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
	public void updatePurchase(PurchaseVO purchaseVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "update transaction set PAYMENT_OPTION=?,RECEIVER_NAME=?,DEMAILADDR=?,RECEIVER_PHONE=?, DLVY_REQUEST=?,DLVY_DATE=? where tran_no=?";
		//DLVY_DATE=?
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, purchaseVO.getPaymentOption());
		System.out.println(purchaseVO.getPaymentOption());
		stmt.setString(2, purchaseVO.getReceiverName());
		System.out.println(purchaseVO.getReceiverName());
		stmt.setString(3, purchaseVO.getDivyAddr());
		stmt.setString(4, purchaseVO.getReceiverPhone());
		stmt.setString(5, purchaseVO.getDivyRequest());
		stmt.setString(6, purchaseVO.getDivyDate());
		stmt.setInt(7, purchaseVO.getTranNo());
		System.out.println(purchaseVO.getTranNo());
		stmt.executeUpdate();
		
		con.close();
	}	
	public void updateTranCodeByProd(int no, PurchaseVO purchaseVO) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		
		String sql = "update transaction set TRAN_STATUS_CODE=? where prod_no=?";
		//DLVY_DATE=?
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, purchaseVO.getTranCode());
		System.out.println(purchaseVO.getTranCode());
		stmt.setInt(2, no);
		stmt.executeUpdate();
		
		con.close();
	}
	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
		// TODO Auto-generated method stub
		Connection con = DBUtil.getConnection();
		
		String sql = "update transaction set TRAN_STATUS_CODE=? where tran_no=?";
		//DLVY_DATE=?
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, purchaseVO.getTranCode());
		System.out.println(purchaseVO.getTranCode());
		stmt.setInt(2, purchaseVO.getTranNo());
		System.out.println(purchaseVO.getTranNo());
		stmt.executeUpdate();
		
		con.close();
	}
}
