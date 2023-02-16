package com.model2.mvc.service.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.user.vo.UserVO;


public class UserDAO {
	
	public UserDAO(){
	}

	public void insertUser(UserVO userVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "insert into USERS values (?,?,?,'user',?,?,?,?,sysdate)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, userVO.getUserId());
		stmt.setString(2, userVO.getUserName());
		stmt.setString(3, userVO.getPassword());
		stmt.setString(4, userVO.getSsn());
		stmt.setString(5, userVO.getPhone());
		stmt.setString(6, userVO.getAddr());
		stmt.setString(7, userVO.getEmail());
		stmt.executeUpdate();
		
		con.close();
	}

	public UserVO findUser(String userId) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "select * from USERS where USER_ID=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, userId);

		ResultSet rs = stmt.executeQuery();

		UserVO userVO = null;
		while (rs.next()) {
			userVO = new UserVO();
			userVO.setUserId(rs.getString("USER_ID"));
			userVO.setUserName(rs.getString("USER_NAME"));
			userVO.setPassword(rs.getString("PASSWORD"));
			userVO.setRole(rs.getString("ROLE"));
			userVO.setSsn(rs.getString("SSN"));
			userVO.setPhone(rs.getString("CELL_PHONE"));
			userVO.setAddr(rs.getString("ADDR"));
			userVO.setEmail(rs.getString("EMAIL"));
			userVO.setRegDate(rs.getDate("REG_DATE"));
		}
		
		con.close();

		return userVO;
	}

	public HashMap<String,Object> getUserList(SearchVO searchVO) throws Exception {
		
		Connection con = DBUtil.getConnection(); //DB����
		
		String sql = "select * from USERS ";
		if (searchVO.getSearchCondition() != null) { //��ġ����� ��
			if (searchVO.getSearchCondition().equals("0")) { //0�̸� user_id
				sql += " where USER_ID='" + searchVO.getSearchKeyword() //ex) user
						+ "'";
			} else if (searchVO.getSearchCondition().equals("1")) { //1�̸� user_name
				sql += " where USER_NAME='" + searchVO.getSearchKeyword() //ex) user
						+ "'";
			}
		}
		sql += " order by USER_ID";

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

		ArrayList<UserVO> list = new ArrayList<UserVO>(); //list = ��̸���Ʈ �ν��Ͻ� ���� 
		if (total > 0) {  //���� ������ ������.
			for (int i = 0; i < searchVO.getPageUnit(); i++) { //�����ִ� ���� ����ŭ �ݺ�
				UserVO vo = new UserVO(); //UserVO�� set�Ѵ�.
				vo.setUserId(rs.getString("USER_ID"));
				vo.setUserName(rs.getString("USER_NAME"));
				vo.setPassword(rs.getString("PASSWORD"));
				vo.setRole(rs.getString("ROLE"));
				vo.setSsn(rs.getString("SSN"));
				vo.setPhone(rs.getString("CELL_PHONE"));
				vo.setAddr(rs.getString("ADDR"));
				vo.setEmail(rs.getString("EMAIL"));
				vo.setRegDate(rs.getDate("REG_DATE"));

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

	public void updateUser(UserVO userVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "update USERS set USER_NAME=?,CELL_PHONE=?,ADDR=?,EMAIL=? where USER_ID=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, userVO.getUserName());
		stmt.setString(2, userVO.getPhone());
		stmt.setString(3, userVO.getAddr());
		stmt.setString(4, userVO.getEmail());
		stmt.setString(5, userVO.getUserId());
		stmt.executeUpdate();
		
		con.close();
	}
}