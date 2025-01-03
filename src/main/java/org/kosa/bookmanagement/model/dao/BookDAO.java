package org.kosa.bookmanagement.model.dao;

import org.kosa.bookmanagement.model.dto.BookDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

public interface BookDAO {
	public ArrayList<BookDTO> searchInfosByKeyword(String type, String keyword) throws SQLException;
	public BookDTO searchBookByISBN(String ISBN) throws SQLException;
	public void deleteISBN(String ISBN) throws SQLException;
	public void updateISBN(BookDTO bookDTO, Set<Integer> authorList) throws SQLException;
}


