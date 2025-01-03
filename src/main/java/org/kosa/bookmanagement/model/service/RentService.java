/*
 * 작업자 : 장원석
 */

package org.kosa.bookmanagement.model.service;

import org.kosa.bookmanagement.model.dto.RentDTO;

import java.util.List;

public interface RentService {

    /**
     * 대여 정보를 생성합니다.
     *
     * @param rent 대여 정보 객체
     * @return 대여 성공 시 true, 실패 시 false
     */
    boolean rentBook(RentDTO rent);

    /**
     * 특정 ID로 대여 정보를 조회합니다.
     *
     * @param id 회원 ID
     * @return 해당 ID에 해당하는 대여 정보 객체
     */
    List<RentDTO> getRentByID(String id);

    /**
     * 모든 대여 정보를 조회합니다.
     *
     * @return 대여 정보 리스트
     */
    List<RentDTO> getAllRents();

    /**
     * 대여 정보를 연장합니다.
     *
     * @param rentNumber 대출번호
     * @return 연장 성공 시 true, 실패 시 false
     */
    boolean extendRent(int rentNumber);

    /**
     * 대여된 책을 반납합니다.
     *
     * @param rentNumber 대출번호
     * @return 반납 성공 시 true, 실패 시 false
     */
    boolean returnBook(int rentNumber);

    /**
     * 대여 정보를 삭제합니다.
     *
     * @param rentNumber 대여 번호
     * @return 삭제 성공 시 true, 실패 시 false
     */
    boolean deleteRent(int rentNumber);

    /**
     * 특정 책이 이미 대여되었는지 확인합니다.
     *
     * @param bookNumber 책 번호
     * @param isbn 책 ISBN
     * @return 이미 대여된 경우 true, 그렇지 않은 경우 false
     */
    boolean isBookAlreadyRented(int bookNumber, String isbn);
}
