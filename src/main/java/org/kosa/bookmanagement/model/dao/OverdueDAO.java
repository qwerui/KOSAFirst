/*
 * 작업자 : 장원석
 */

package org.kosa.bookmanagement.model.dao;

import org.kosa.bookmanagement.model.dto.RentDTO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface OverdueDAO {

    /**
     * 모든 연체된 대여 정보를 반환합니다.
     *
     * @return List<RentDTO> 연체된 대여 정보 리스트
     * @throws SQLException 데이터베이스 접근 오류 발생 시
     * @throws SQLException 데이터베이스 접근 오류 발생 시
     */
    List<RentDTO> getAllOverdueRents() throws SQLException;

    /**
     * 특정 회원 ID에 해당하는 연체된 대여 정보를 반환합니다.
     *
     * @param id 회원 ID
     * @return List<RentDTO> 연체된 대여 정보 리스트
     * @throws SQLException 데이터베이스 접근 오류 발생 시
     */
    List<RentDTO> getOverdueRentByID(String id) throws SQLException;

    /**
     * 특정 ISBN으로 연체된 대여 정보를 조회합니다.
     *
     * @param isbn 책 ISBN
     * @return 연체된 대여 정보 리스트
     * @throws SQLException 데이터베이스 접근 오류 발생 시
     */
    List<RentDTO> getOverdueRentByISBN(String isbn) throws SQLException;

    /**
     * 모든 대여의 연체 상태를 업데이트합니다.
     */
    void updateAllOverdueStatus() throws SQLException;

    /**
     * 특정 대여의 연체 상태를 업데이트합니다.
     *
     * @param rentNumber 대여 번호
     * @param isOverdue 연체 여부
     * @return 업데이트 성공 시 true, 실패 시 false
     */
    boolean updateOverdueStatus(int rentNumber, boolean isOverdue) throws SQLException;
}

