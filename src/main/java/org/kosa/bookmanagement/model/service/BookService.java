/*
작업자 : 홍제기
 */

package org.kosa.bookmanagement.model.service;

import org.kosa.bookmanagement.model.dao.BookDAO;
import org.kosa.bookmanagement.model.dao.CopyDAO;
import org.kosa.bookmanagement.model.dto.BookDTO;
import org.kosa.bookmanagement.model.dto.CopyDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BookService {
	BookDAO bookDAO;
	CopyDAO copyDAO;
	
	BookDTO selectedBookInfo;
	
	public BookService(BookDAO bookDAO, CopyDAO copyDAO) {
		this.bookDAO = bookDAO;
		this.copyDAO = copyDAO;
	}
	
	// 도서 상세 화면에 출력할 도서 지정
	public void setSelectedBookInfo(BookDTO bookDTO) {
		this.selectedBookInfo = bookDTO;
	}
	
	// 도서 상세 화면에 출력할 도서 가져오기
	public BookDTO getSelectedBookInfo() {
		return selectedBookInfo;
	}
	
	// DAO에서 키워드 검색 결과를 Return
	public ArrayList<BookDTO> searchInfosByKeyword(String type, String keyword) throws SQLException {
		return bookDAO.searchInfosByKeyword(type, keyword);
	}

	// DAO에서 ISBN으로 정보 찾기
	public BookDTO searchBookByISBN(String ISBN) throws SQLException {
		return bookDAO.searchBookByISBN(ISBN);
	}

	// 대출 가능한 도서를 isbn-도서번호 형태로 반환
	public List<String> getAvaliableCopies(String isbn) throws SQLException {
		List<String> result = new ArrayList<>();

		for(CopyDTO copy : copyDAO.getAvailableBooksByISBN(isbn)){
			result.add(copy.getIsbn()+"-"+copy.getBookNumber()+" ("+copy.getPosition()+")");
		}

		return result;
    }

	// ISBN에 해당하는 모든 도서 번호 조회
	public List<Integer> getAllCopies(String isbn) throws SQLException {
		return copyDAO.getAllBooksByISBN(isbn);
	}

	// ISBN 삽입/수정
	public void updateISBN(BookDTO bookDTO, Set<Integer> authorList) throws SQLException{
		bookDAO.updateISBN(bookDTO, authorList);
	}

	// ISBN 삭제
	public void deleteISBN(String isbn) throws SQLException{
		bookDAO.deleteISBN(isbn);
	}

	// 사본 삽입/수정
	public void updateCopy(String ISBN, int number, int position) throws SQLException {
		copyDAO.updateCopy(ISBN, number, position);
	}

	// 사본 삭제
	public void deleteCopy(String ISBN, int number) throws SQLException {
		copyDAO.deleteCopy(ISBN, number);
	}
}
