/*
 * 작업자 : 이하린
 */

package org.kosa.bookmanagement.model.service;

import org.kosa.bookmanagement.model.dao.MemberDAO;
import org.kosa.bookmanagement.model.dto.MemberDTO;
import org.kosa.bookmanagement.util.MemberState;
import org.kosa.bookmanagement.util.PasswordUtil;

import java.sql.SQLException;

public class MemberService {
    MemberDAO memberDAO;

    public MemberService(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public boolean registerMember(String id, String password, String name, String phone) throws SQLException {
        MemberDTO member = new MemberDTO(id, password, name, phone);
        return memberDAO.insertMember(member);
    }

    public boolean loginMember(String id, String password) throws SQLException {
        MemberDTO member = memberDAO.getMemberById(id);
        if (member != null) {
            boolean isPasswordMatch = PasswordUtil.checkPassword(password, member.getPassword());
            if (isPasswordMatch) {
                // 탈퇴 계정인 경우
                if (member.getTypeNumber() == 2) {
                    MemberState.setIsMember(false);
                    return false;
                } else {
                    MemberState.setLoggedInMemberId(member.getId());
                    MemberState.setIsMember(true); // 로그인 성공 시 회원 상태 설정
                    if (member.getTypeNumber() == 1) {
                        MemberState.setIsAdmin(true);
                    }
                    System.out.println("사용자 일치, 로그인 성공");
                    System.out.println("로그인 사용자 : " + member.getName());
                    return true;
                }
            }
        }
        MemberState.setIsMember(true); // 로그인 실패 시 기본값 유지
        return false;
    }

    public boolean isExistedId(String id) throws SQLException {
        return memberDAO.getMemberById(id) != null;
    }

    public boolean modifyMember(String id, String password, String name, String phone) throws SQLException {
        MemberDTO member = new MemberDTO(id, password, name, phone);
        return memberDAO.updateMember(member);
    }

    public boolean deleteMember(String id) throws SQLException {
        return memberDAO.deleteMember(id);
    }
}
