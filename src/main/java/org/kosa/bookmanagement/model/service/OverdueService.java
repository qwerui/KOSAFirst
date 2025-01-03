/*
 * 작업자 : 장원석
 */

package org.kosa.bookmanagement.model.service;

import org.kosa.bookmanagement.model.dto.RentDTO;

import java.util.List;

public interface OverdueService {
    /**
     * 모든 연체된 대여 정보를 조회합니다.
     *
     * @return 연체된 대여 정보 리스트
     */
    List<RentDTO> getAllOverdueRents();

    /**
     * 특정 회원 ID로 연체된 대여 정보를 조회합니다.
     *
     * @param id 회원 ID
     * @return 연체된 대여 정보 리스트
     */
    List<RentDTO> getOverdueRentByID(String id);

    /**
     * 특정 ISBN으로 연체된 대여 정보를 조회합니다.
     *
     * @param isbn 책 ISBN
     * @return 연체된 대여 정보 리스트
     */
    List<RentDTO> getOverdueRentByISBN(String isbn);

    /**
     * 모든 대여의 연체 상태를 업데이트합니다.
     */
    void updateAllOverdueStatus();

    /**
     * 특정 대여의 연체 상태를 업데이트합니다.
     *
     * @param rentNumber 대여 번호
     * @param isOverdue 연체 여부
     * @return 업데이트 성공 시 true, 실패 시 false
     */
    boolean updateOverdueStatus(int rentNumber, boolean isOverdue);
}

