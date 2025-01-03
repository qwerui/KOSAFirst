/*
 * 작업자 : 이하린
 */

package org.kosa.bookmanagement.model.dao;

import org.kosa.bookmanagement.model.dto.MemberDTO;
import org.kosa.bookmanagement.util.DBManager;
import org.kosa.bookmanagement.util.MemberState;
import org.kosa.bookmanagement.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MemberDAOImpl implements MemberDAO {

    @Override
    public boolean insertMember(MemberDTO member) throws SQLException {
        String sql = "INSERT INTO MEMBER (id, password, name, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getId());
            ps.setString(2, PasswordUtil.hashPassword(member.getPassword()));
            ps.setString(3, member.getName());
            ps.setString(4, member.getPhone());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public MemberDTO getMemberById(String id) throws SQLException {
        String sql = "SELECT * FROM MEMBER WHERE id = ?";
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    String name = rs.getString("name");
                    String phone = rs.getString("phone");
                    int typeNumber = rs.getInt("type_number");
                    return new MemberDTO(id, password, name, phone, typeNumber);
                }
            }
        }
        return null;
    }

    @Override
    public boolean updateMember(MemberDTO member) throws SQLException {
        String sql = "UPDATE MEMBER SET password = ?, name = ?, phone = ? WHERE id = ?";
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, PasswordUtil.hashPassword(member.getPassword()));
            ps.setString(2, member.getName());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteMember(String id) throws SQLException {
        String sql = "SELECT return_date FROM rent WHERE id = ?";
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, MemberState.getLoggedInMemberId());
            ResultSet rs = ps.executeQuery();

            boolean canDelete = true;

            while (rs.next()) {
                // 반납되지 않은 도서가 있는 경우
                if (rs.getDate("return_date") == null) {
                    canDelete = false;
                    break;
                }
            }

            if (canDelete) {
                // 대출 기록이 없거나, 반납된 도서가 있는 경우
                String updateSql = "UPDATE member SET type_number = 2 WHERE id = ?";
                try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                    psUpdate.setString(1, MemberState.getLoggedInMemberId());
                    return psUpdate.executeUpdate() > 0;
                }
            } else {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
