/*
 * 작업자 : 이하린
 */

package org.kosa.bookmanagement.model.dao;

import org.kosa.bookmanagement.model.dto.MemberDTO;
import java.sql.SQLException;

public interface MemberDAO {
    boolean insertMember(MemberDTO member) throws SQLException;
    MemberDTO getMemberById(String id) throws SQLException;
    boolean updateMember(MemberDTO member) throws SQLException;
    boolean deleteMember(String id) throws SQLException;
}