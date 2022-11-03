package com.gyojincompany.mvcboard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.gyojincompany.mvcboard.dto.BoardDto;
import com.gyojincompany.mvcboard.util.Constant;

public class BoardDao {
	
	DataSource dataSource;
	
	JdbcTemplate template;

	public BoardDao() {
		super();
		// TODO Auto-generated constructor stub
		
		this.template = Constant.template;
		
//		try {
//			Context context = new InitialContext();
//			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle11g");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
	}
	
	public ArrayList<BoardDto> list() { //게시판 전체 글 목록을 반환하는 메서드
		
		//JDBCtemplate 이용
		String sql = "SELECT * FROM mvc_board ORDER BY bgroup DESC, bstep ASC";
		
		ArrayList<BoardDto> dtos = (ArrayList<BoardDto>) template.query(sql, new BeanPropertyRowMapper(BoardDto.class));
		
		
		//JDBC 오리지널
//		ArrayList<BoardDto> dtos = new ArrayList<BoardDto>();
//		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			conn = dataSource.getConnection();
//			String sql = "SELECT * FROM mvc_board ORDER BY bgroup DESC, bstep ASC";
//			//게시글 번호의 내림차순 정렬(최근글이 가장 위에 오도록 함)
//			pstmt = conn.prepareStatement(sql);//sql문 객체 생성
//			rs = pstmt.executeQuery();//SQL을 실행하여 결과값을 반환
//			
//			while(rs.next()) {
//				int bid = rs.getInt("bid");
//				String bname = rs.getString("bname");
//				String btitle = rs.getString("btitle");
//				String bcontent = rs.getString("bcontent");				
//				Timestamp bdate = rs.getTimestamp("bdate");
//				int bhit = rs.getInt("bhit");
//				int bgroup = rs.getInt("bgroup");
//				int bstep = rs.getInt("bstep");
//				int bindent = rs.getInt("bindent");
//				
////				BoardDto dto = new BoardDto();
////				dto.setBid(bid);
////				dto.setBname(bname);
////				dto.setBtitle(btitle);
////				dto.setBcontent(bcontent);
////				dto.setBdate(bdate);
////				dto.setBhit(bhit);
////				dto.setBgroup(bgroup);
////				dto.setBstep(bstep);
////				dto.setBindent(bindent);
//				
//				BoardDto dto = new BoardDto(bid, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);
//				dtos.add(dto);				
//			}				
//				
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				if(rs != null) {
//					rs.close();
//				}
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		return dtos;
		
	}
	
	public void write(final String bname, final String btitle, final String bcontent) {
		
		this.template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				String sql = "INSERT INTO mvc_board(bid, bname, btitle, bcontent, bhit, bgroup, bstep, bindent) VALUES (MVC_BOARD_SEQ.nextval, ?, ?, ?, 0, MVC_BOARD_SEQ.currval, 0, 0)";
				PreparedStatement pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, bname);
				pstmt.setString(2, btitle);
				pstmt.setString(3, bcontent);				
				
				return pstmt;
			}
		});
		
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;		
//		
//		try {
//			conn = dataSource.getConnection();
//			String sql = "INSERT INTO mvc_board(bid, bname, btitle, bcontent, bhit, bgroup, bstep, bindent) VALUES (MVC_BOARD_SEQ.nextval, ?, ?, ?, 0, MVC_BOARD_SEQ.currval, 0, 0)";
//			
//			pstmt = conn.prepareStatement(sql);//sql문 객체 생성
//			
//			pstmt.setString(1, bname);
//			pstmt.setString(2, btitle);
//			pstmt.setString(3, bcontent);
//			//sql 문 완성
//			
//			pstmt.executeUpdate();//완성된 SQL문 실행
//						
//				
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {				
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
	}
	
	public BoardDto content_view(String cid) {
		
		upHit(cid);
		
		BoardDto dto = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			String sql = "SELECT * FROM mvc_board WHERE bid=?";
			//게시글 번호의 내림차순 정렬(최근글이 가장 위에 오도록 함)
			pstmt = conn.prepareStatement(sql);//sql문 객체 생성
			pstmt.setString(1, cid);			
			rs = pstmt.executeQuery();//SQL을 실행하여 결과값을 반환
			
			if(rs.next()) {
				int bid = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");				
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				int bgroup = rs.getInt("bgroup");
				int bstep = rs.getInt("bstep");
				int bindent = rs.getInt("bindent");
				
				dto = new BoardDto(bid, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);
								
			}				
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return dto;
	}
	
	public void modify(String bname, String btitle, String bcontent, String bid) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;		
		
		try {
			conn = dataSource.getConnection();
			String sql = "UPDATE mvc_board SET bname=?, btitle=?, bcontent=? WHERE bid=?";
			
			pstmt = conn.prepareStatement(sql);//sql문 객체 생성
			
			pstmt.setString(1, bname);
			pstmt.setString(2, btitle);
			pstmt.setString(3, bcontent);
			pstmt.setString(4, bid);
			//sql 문 완성
			
			pstmt.executeUpdate();//완성된 SQL문 실행
						
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {				
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void delete(String bid) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;		
		
		try {
			conn = dataSource.getConnection();
			String sql = "DELETE FROM mvc_board WHERE bid=?";
			
			pstmt = conn.prepareStatement(sql);//sql문 객체 생성
			
			pstmt.setString(1, bid);
			//sql 문 완성
			
			pstmt.executeUpdate();//완성된 SQL문 실행
						
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {				
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void upHit(String bid) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;		
		
		try {
			conn = dataSource.getConnection();
			String sql = "UPDATE mvc_board SET bhit=bhit+1 WHERE bid=?";
			
			pstmt = conn.prepareStatement(sql);//sql문 객체 생성
			
			pstmt.setString(1, bid);
			//sql 문 완성
			
			pstmt.executeUpdate();//완성된 SQL문 실행
						
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {				
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public int board_count() {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int count = 0;
		
		try {
			conn = dataSource.getConnection();
			String sql = "SELECT * FROM mvc_board";
			//게시글 번호의 내림차순 정렬(최근글이 가장 위에 오도록 함)
			pstmt = conn.prepareStatement(sql);//sql문 객체 생성
			rs = pstmt.executeQuery();//SQL을 실행하여 결과값을 반환
			
			while(rs.next()) {
				count = count + 1;
			}				
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return count;
		
	}
	
	public void reply(String bid, String bname, String btitle, String bcontent, String bgroup, String bstep, String bindent) {
		
		reply_sort(bgroup, bstep);
		
		Connection conn = null;
		PreparedStatement pstmt = null;		
		
		try {
			conn = dataSource.getConnection();
			String sql = "INSERT INTO mvc_board(bid, bname, btitle, bcontent, bhit, bgroup, bstep, bindent) VALUES (MVC_BOARD_SEQ.nextval, ?, ?, ?, 0, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);//sql문 객체 생성
			
			pstmt.setString(1, bname);
			pstmt.setString(2, btitle);
			pstmt.setString(3, bcontent);
			pstmt.setString(4, bgroup);
			pstmt.setInt(5, Integer.parseInt(bstep)+1);
			pstmt.setInt(6, Integer.parseInt(bindent)+1);
			
			//sql 문 완성
			
			pstmt.executeUpdate();//완성된 SQL문 실행
						
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {				
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void reply_sort(String bgroup, String bstep) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;		
		
		try {
			conn = dataSource.getConnection();
			String sql = "UPDATE mvc_board SET bstep=bstep+1 WHERE bgroup=? and bstep>?";
			
			pstmt = conn.prepareStatement(sql);//sql문 객체 생성
			
			pstmt.setString(1, bgroup);
			pstmt.setString(2, bstep);
			//sql 문 완성
			
			pstmt.executeUpdate();//완성된 SQL문 실행
						
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {				
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
