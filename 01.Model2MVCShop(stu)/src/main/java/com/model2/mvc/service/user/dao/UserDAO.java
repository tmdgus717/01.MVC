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
		
		Connection con = DBUtil.getConnection(); //DB연결
		
		String sql = "select * from USERS ";
		if (searchVO.getSearchCondition() != null) { //서치컨디션 비교
			if (searchVO.getSearchCondition().equals("0")) { //0이면 user_id
				sql += " where USER_ID='" + searchVO.getSearchKeyword() //ex) user
						+ "'";
			} else if (searchVO.getSearchCondition().equals("1")) { //1이면 user_name
				sql += " where USER_NAME='" + searchVO.getSearchKeyword() //ex) user
						+ "'";
			}
		}
		sql += " order by USER_ID";

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

		ArrayList<UserVO> list = new ArrayList<UserVO>(); //list = 어레이리스트 인스턴스 생성 
		if (total > 0) {  //값이 있으면 돌린다.
			for (int i = 0; i < searchVO.getPageUnit(); i++) { //보여주는 갯수 수만큼 반복
				UserVO vo = new UserVO(); //UserVO에 set한다.
				vo.setUserId(rs.getString("USER_ID"));
				vo.setUserName(rs.getString("USER_NAME"));
				vo.setPassword(rs.getString("PASSWORD"));
				vo.setRole(rs.getString("ROLE"));
				vo.setSsn(rs.getString("SSN"));
				vo.setPhone(rs.getString("CELL_PHONE"));
				vo.setAddr(rs.getString("ADDR"));
				vo.setEmail(rs.getString("EMAIL"));
				vo.setRegDate(rs.getDate("REG_DATE"));

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