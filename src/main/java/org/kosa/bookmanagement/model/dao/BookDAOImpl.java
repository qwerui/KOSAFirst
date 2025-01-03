
/*
작업자 : 홍제기
 */

package org.kosa.bookmanagement.model.dao;


import org.kosa.bookmanagement.model.dto.BookDTO;
import org.kosa.bookmanagement.util.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public class BookDAOImpl implements BookDAO {
	
	// 도서 검색 메소드
	// 저자나 책 제목을 키워드로 조회한다.
	public ArrayList<BookDTO> searchInfosByKeyword(String type, String keyword) throws SQLException {

		ArrayList<BookDTO> result = new ArrayList<>();
		String query = "";

		if (type.equals("서명")) {
			query = "SELECT * FROM BOOKDETAIL WHERE title LIKE '%"+keyword+"%'";
		} else if (type.equals("저자")) {
			query = "SELECT * FROM BOOKDETAIL WHERE authors LIKE '%"+keyword+"%'";
		}
			
		try (Connection conn = DBManager.getInstance().getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {
			while(rs.next()){
				BookDTO bookDTO = new BookDTO();
				bookDTO.setIsbn(rs.getString("isbn"));
				bookDTO.setAuthor(rs.getString("authors"));
				bookDTO.setCategory(rs.getString("category_name"));
				bookDTO.setPublishYear(rs.getString("publish_year"));
				bookDTO.setPublisher(rs.getString("publisher"));
				bookDTO.setTitle(rs.getString("title"));
				result.add(bookDTO);
			}
		}

		return result;
	}

	// ISBN으로 책 정보 조회
	@Override
	public BookDTO searchBookByISBN(String ISBN) throws SQLException {

		String query = "SELECT * FROM BOOKDETAIL WHERE isbn = '"+ISBN+"'";

		BookDTO result = null;

		try (Connection conn = DBManager.getInstance().getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {
			if(rs.next()){
				result = new BookDTO();
				result.setIsbn(rs.getString("isbn"));
				result.setAuthor(rs.getString("authors"));
				result.setTitle(rs.getString("title"));
				result.setPublisher(rs.getString("publisher"));
				result.setCategory(rs.getString("category_name"));
				result.setPublishYear(rs.getString("publish_year"));
			}
		}

		return result;
	}

	// ISBN 삭제
	@Override
	public void deleteISBN(String ISBN) throws SQLException {
		String query = "{call delete_isbn(?)}";

		try(Connection conn = DBManager.getInstance().getConnection();
			CallableStatement cstmt = conn.prepareCall(query)){
				cstmt.setString(1, ISBN);
				cstmt.executeQuery();
		}
	}

	// ISBN 삽입/수정
	// 저자는 하나의 문자열로 만들어서 프로시저 내에서 Split 과정 수행
	@Override
	public void updateISBN(BookDTO bookDTO, Set<Integer> authorList) throws SQLException {
		String query = "{call update_isbn(?, ?, ?, ?, ?, ?)}";

		try(Connection conn = DBManager.getInstance().getConnection();
			CallableStatement cstmt = conn.prepareCall(query);) {
			cstmt.setString(1, bookDTO.getIsbn());
			cstmt.setString(2, bookDTO.getTitle());
			cstmt.setString(3, bookDTO.getPublisher());
			cstmt.setString(4, bookDTO.getPublishYear());
			cstmt.setString(5, bookDTO.getCategory());

			StringBuilder sb = new StringBuilder();
			for(int authorNumber : authorList){
				sb.append(authorNumber);
				sb.append(',');
			}
			sb.deleteCharAt(sb.length()-1);
			cstmt.setString(6, sb.toString());

			cstmt.executeQuery();
		}
	}


}