/*
 * 작업자: 장원석
 */

package org.kosa.bookmanagement.model.dao;

import org.kosa.bookmanagement.model.dto.RentDTO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface RentDAO {

	boolean insertRent(RentDTO rent) throws SQLException;

	List<RentDTO> getRentByID(String id) throws SQLException;

	List<RentDTO> getAllRents() throws SQLException;

	boolean updateRentExtended(int rentNumber) throws SQLException;

	boolean updateRentReturnDate(int rentNumber) throws SQLException;

	boolean deleteRent(int rentNumber) throws SQLException;

	boolean isBookAlreadyRented(int bookNumber, String isbn) throws SQLException;
}
