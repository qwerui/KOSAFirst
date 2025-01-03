/*
작업자 : 홍제기
 */

package org.kosa.bookmanagement.model.dao;
import org.kosa.bookmanagement.model.dto.AuthorDTO;
import org.kosa.bookmanagement.util.DBManager;

import java.sql.*;
import java.util.ArrayList;

// 저자 테이블에 접근하는 DAO
public class AuthorDAOImpl implements AuthorDAO {
    // 키워드로 저자 검색
    @Override
    public ArrayList<AuthorDTO> getAuthorsByKeyword(String keyword) throws SQLException {

        String query = "SELECT * FROM AUTHOR WHERE name LIKE '%"+keyword+"%'";

        ArrayList<AuthorDTO> result = new ArrayList<>();

        try(Connection conn = DBManager.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            while(rs.next()) {
                AuthorDTO authorDTO = new AuthorDTO();
                authorDTO.setAuthorName(rs.getString("name"));
                authorDTO.setAuthorNumber(rs.getInt("author_number"));
                result.add(authorDTO);
            }
        }

        return result;
    }

    // 저자 번호로 저자 검색
    @Override
    public AuthorDTO getAuthorByNumber(int number) throws SQLException {

        String query = "SELECT * FROM AUTHOR WHERE author_number = "+number;

        AuthorDTO result = null;

        try(Connection conn = DBManager.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            if(rs.next()) {
                result = new AuthorDTO();
                result.setAuthorName(rs.getString("name"));
                result.setAuthorNumber(rs.getInt("author_number"));
            }
        }

        return result;
    }

    // DB에 저자 추가
    @Override
    public void insertAuthor(String name) throws SQLException {
        String query = "INSERT INTO AUTHOR(name) VALUES(?)";

        try(Connection conn = DBManager.getInstance().getConnection();
            PreparedStatement psmt = conn.prepareStatement(query)){
            psmt.setString(1, name);
            psmt.executeUpdate();
        }
    }

    // 번호에 해당하는 저자를 DB에서 수정
    @Override
    public void modifyAuthor(int number, String name) throws SQLException {
        String query = "UPDATE AUTHOR SET name = ? WHERE author_number = ?";

        try(Connection conn = DBManager.getInstance().getConnection();
            PreparedStatement psmt = conn.prepareStatement(query)){
            psmt.setString(1, name);
            psmt.setInt(2, number);
            psmt.executeUpdate();
        }
    }
}
