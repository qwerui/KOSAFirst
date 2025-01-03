/*
 * 작업자 : 장원석
 */

package org.kosa.bookmanagement.model.dao;

import org.kosa.bookmanagement.model.dto.RentDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OverdueDAOImpl implements OverdueDAO{

    private Connection conn;

    public OverdueDAOImpl(Connection conn) {
        this.conn = conn;
    }

@Override
public List<RentDTO> getAllOverdueRents() throws SQLException {
    List<RentDTO> overdueRentList = new ArrayList<>();

    String sql = "WITH RentSubset AS (" +
            "    SELECT * " +
            "    FROM rent " +
            "    WHERE return_date IS NULL AND SYSDATE > rent_date + (14 * (extended + 1)) " +
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
            "ORDER BY rs.rent_date";

    try (PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            int rentNumber = rs.getInt("rent_number");
            String id = rs.getString("id");
            int bookNumber = rs.getInt("book_number");
            String isbn = rs.getString("isbn");
            String title = rs.getString("title");
            Date rentDate = rs.getDate("rent_date");
            Date returnDate = rs.getDate("return_date");
            String authors = rs.getString("authors");
            int extended = rs.getInt("extended");

            RentDTO rentDTO = new RentDTO(rentNumber, id, bookNumber, isbn, title, authors, rentDate, returnDate, extended);
            overdueRentList.add(rentDTO);
        }
    }

    return overdueRentList;
}

    @Override
    public List<RentDTO> getOverdueRentByID(String id) throws SQLException {
        String sql = "SELECT ID, BOOK_NUMBER, ISBN, RENT_DATE, RETURN_DATE, EXTENDED " +
                "FROM RENT " +
                "WHERE ID = ? AND RETURN_DATE IS NULL AND " +
                "(RENT_DATE + (EXTENDED * 14 + 14)) < SYSDATE";

        List<RentDTO> overdueRentByID = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RentDTO rent = new RentDTO();
                    rent.setId(rs.getString("ID"));
                    rent.setBookNumber(rs.getInt("BOOK_NUMBER"));
                    rent.setIsbn(rs.getString("ISBN"));
                    rent.setRentDate(Date.valueOf(rs.getDate("RENT_DATE").toLocalDate()));
                    rent.setReturnDate(rs.getDate("RETURN_DATE") != null ? Date.valueOf(rs.getDate("RETURN_DATE").toLocalDate()) : null);
                    rent.setExtended(rs.getInt("EXTENDED"));
                    overdueRentByID.add(rent);
                }
            }
        }

        return overdueRentByID;
    }

    @Override
    public void updateAllOverdueStatus() throws SQLException {

    }

    @Override
    public boolean updateOverdueStatus(int rentNumber, boolean isOverdue) throws SQLException {
        return false;
    }

    @Override
    public List<RentDTO> getOverdueRentByISBN(String isbn) throws SQLException {
        String sql = "SELECT ID, BOOK_NUMBER, ISBN, RENT_DATE, RETURN_DATE, EXTENDED " +
                "FROM RENT " +
                "WHERE ISBN = ? AND RETURN_DATE IS NULL AND " +
                "(RENT_DATE + (EXTENDED * 14 + 14)) < SYSDATE";

        List<RentDTO> overdueRentals = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RentDTO rent = new RentDTO();
                    rent.setId(rs.getString("ID"));
                    rent.setBookNumber(rs.getInt("BOOK_NUMBER"));
                    rent.setIsbn(rs.getString("ISBN"));
                    rent.setRentDate(Date.valueOf(rs.getDate("RENT_DATE").toLocalDate()));
                    rent.setReturnDate(rs.getDate("RETURN_DATE") != null ? Date.valueOf(rs.getDate("RETURN_DATE").toLocalDate()) : null);
                    rent.setExtended(rs.getInt("EXTENDED"));
                    overdueRentals.add(rent);
                }
            }
        }

        return overdueRentals;
    }

}
