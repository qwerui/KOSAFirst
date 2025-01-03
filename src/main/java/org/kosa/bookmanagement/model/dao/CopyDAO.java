package org.kosa.bookmanagement.model.dao;

import org.kosa.bookmanagement.model.dto.CopyDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CopyDAO {
	ArrayList<CopyDTO> getAvailableBooksByISBN(String ISBN) throws SQLException;
	public List<Integer> getAllBooksByISBN(String ISBN) throws SQLException;
	public void updateCopy(String ISBN, int number, int position) throws SQLException;
	public void deleteCopy(String ISBN, int number) throws SQLException;
}
