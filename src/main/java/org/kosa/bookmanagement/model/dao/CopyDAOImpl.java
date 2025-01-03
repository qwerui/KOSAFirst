/*
작성자 : 홍제기
*/

package org.kosa.bookmanagement.model.dao;

import org.kosa.bookmanagement.model.dto.CopyDTO;
import org.kosa.bookmanagement.util.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CopyDAOImpl implements CopyDAO{

	// 대출 가능 도서 목록 조회
	@Override
	public ArrayList<CopyDTO> getAvailableBooksByISBN(String ISBN) throws SQLException {
		
		ArrayList<CopyDTO> result = new ArrayList<>();

		String query = "SELECT * FROM AVAILABLE_BOOK WHERE isbn = '"+ISBN+"'";

		try(Connection conn = DBManager.getInstance().getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query)){
			while(rs.next()){
				CopyDTO copyDTO = new CopyDTO();
				copyDTO.setIsbn(rs.getString("isbn"));
				copyDTO.setBookNumber(rs.getString("book_number"));
				copyDTO.setPosition(rs.getString("position"));
				result.add(copyDTO);
			}
		}
		
		return result;
	}

	// ISBN에 해당하는 모든 도서 번호 조회
	@Override
	public List<Integer> getAllBooksByISBN(String ISBN) throws SQLException {

		List<Integer> result = new ArrayList<>();

		String query = "SELECT book_number FROM COPY WHERE isbn = '"+ISBN+"'";

		try(Connection conn = DBManager.getInstance().getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				result.add(rs.getInt("book_number"));
			}
		}

		return result;
	}

	// 사본 삽입/수정
	@Override
	public void updateCopy(String ISBN, int number, int position) throws SQLException {
		String query = "{call update_copy(?, ?, ?)}";

		try(Connection conn = DBManager.getInstance().getConnection();
			CallableStatement cstmt = conn.prepareCall(query)){
			cstmt.setString(1, ISBN);
			cstmt.setInt(2, number);
			cstmt.setInt(3, position);
			cstmt.executeUpdate();
		}
	}

	// 사본 삭제
	@Override
	public void deleteCopy(String ISBN, int number) throws SQLException {
		String query = "{call delete_copy(?, ?)}";

		try(Connection conn = DBManager.getInstance().getConnection();
			CallableStatement cstmt = conn.prepareCall(query)){

			cstmt.setString(1, ISBN);
			cstmt.setInt(2, number);

			cstmt.executeUpdate();
		}
	}
}
