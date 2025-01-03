/*
 * 작업자: 장원석
 */

package org.kosa.bookmanagement.model.dao;

import org.kosa.bookmanagement.model.dto.RentDTO;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class RentDAOImpl implements RentDAO {

	private Connection conn;

	public RentDAOImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean insertRent(RentDTO rent) throws SQLException {
		String sql = "INSERT INTO RENT (RENT_DATE, RETURN_DATE, EXTENDED, ID, BOOK_NUMBER, ISBN) VALUES (?, ?, ?, ?, ?, ?)";

		LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
		Date rentDate = Date.valueOf(currentDate);

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setDate(1, rentDate);
			pstmt.setDate(2, null);
			pstmt.setInt(3, 0);
			pstmt.setString(4, rent.getId());
			pstmt.setInt(5, rent.getBookNumber());
			pstmt.setString(6, rent.getIsbn());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		}
	}

	@Override
	public List<RentDTO> getRentByID(String id) throws SQLException {
		List<RentDTO> list = new ArrayList<>();

		String sql = "WITH RentSubset AS (" +
				"    SELECT * " +
				"    FROM rent " +
				"    WHERE id = ? " +
				"), AuthorList AS (" +
				"    SELECT ab.isbn, " +
				"           LISTAGG(a.name, ', ') WITHIN GROUP (ORDER BY a.name) AS authors " +
				"    FROM author_book ab " +
				"    JOIN author a ON ab.author_number = a.author_number " +
				"    GROUP BY ab.isbn " +
				") " +
				"SELECT rs.rent_number, rs.book_number, rs.isbn, rs.id, b.title, rs.rent_date, rs.return_date, al.authors, rs.extended " +
				"FROM RentSubset rs " +
				"JOIN book b ON rs.isbn = b.isbn " +
				"JOIN AuthorList al ON rs.isbn = al.isbn " +
				"ORDER BY " +
				"    CASE " +
				"        WHEN rs.return_date IS NULL AND SYSDATE > rs.rent_date + (14 * (rs.extended + 1)) THEN 0 " + // 연체
				"        WHEN rs.return_date IS NULL THEN 1 " + // 미반납
				"        ELSE 2 " + // 반납
				"    END, " +
				"    rs.rent_date";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, id);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				int rentNumber = resultSet.getInt("rent_number");
				int bookNumber = resultSet.getInt("book_number");
				String isbn = resultSet.getString("isbn");
				String title = resultSet.getString("title");
				String authors = resultSet.getString("authors");
				Date rentDate = resultSet.getDate("rent_date");
				Date returnDate = resultSet.getDate("return_date");
				int extended = resultSet.getInt("extended");

				RentDTO rentDTO = new RentDTO(rentNumber, bookNumber, isbn, title, authors, rentDate, returnDate, extended);
				list.add(rentDTO);
			}
		}
		return list;
	}

	@Override
	public List<RentDTO> getAllRents() throws SQLException {
		String sql = "SELECT * FROM RENT";
		List<RentDTO> list = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				RentDTO rent = new RentDTO(
						rs.getInt("RENT_NUMBER"),
						rs.getDate("RENT_DATE"),
						rs.getDate("RETURN_DATE"),
						rs.getInt("EXTENDED"),
						rs.getString("ID"),
						rs.getInt("BOOK_NUMBER"),
						rs.getString("ISBN")
				);
				list.add(rent);
			}
		}
		return list;
	}

	@Override
	public boolean updateRentExtended(int rentNumber) throws SQLException {
		String selectSql = "SELECT EXTENDED FROM RENT WHERE RENT_NUMBER = ? AND RETURN_DATE IS NULL";
		String updateSql = "UPDATE RENT SET EXTENDED = EXTENDED + 1 WHERE RENT_NUMBER = ?";

		try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
			selectStmt.setInt(1, rentNumber);

			try (ResultSet rs = selectStmt.executeQuery()) {
				if (rs.next()) {
					int extended = rs.getInt("EXTENDED");
					System.out.println("Current extension count: " + extended); // 디버깅 메시지 추가
					if (extended < 2) {
						try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
							updateStmt.setInt(1, rentNumber);
							int rowsAffected = updateStmt.executeUpdate();
							System.out.println("Rows affected: " + rowsAffected); // 디버깅 메시지 추가
							return rowsAffected > 0; // Update successful
						}
					} else {
						System.out.println("Maximum extension count reached."); // 디버깅 메시지 추가
						return false; // Maximum extension count reached
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateRentReturnDate(int rentNumber) throws SQLException {
		String updateSql = "UPDATE RENT SET RETURN_DATE = ? WHERE RENT_NUMBER = ? AND RETURN_DATE IS NULL";

		try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
			Date currentDate = new Date(System.currentTimeMillis());
			pstmt.setDate(1, currentDate);
			pstmt.setInt(2, rentNumber);

			int rowsAffected = pstmt.executeUpdate();
			System.out.println("Rows affected: " + rowsAffected); // 디버깅 메시지 추가
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteRent(int rentNumber) throws SQLException {
		String sql = "DELETE FROM RENT WHERE RENT_NUMBER = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, rentNumber);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		}
	}

	@Override
	public boolean isBookAlreadyRented(int bookNumber, String isbn) throws SQLException {
		String selectSql = "SELECT 1 FROM RENT WHERE BOOK_NUMBER = ? AND ISBN = ? AND RETURN_DATE IS NULL";

		try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
			pstmt.setInt(1, bookNumber);
			pstmt.setString(2, isbn);

			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next(); // 결과가 있으면 이미 대출 중
			}
		}
	}
}