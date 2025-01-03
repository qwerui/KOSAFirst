/*
작성자 : 홍제기
 */


package org.kosa.bookmanagement.model.service;

import org.kosa.bookmanagement.model.dao.AuthorDAO;
import org.kosa.bookmanagement.model.dto.AuthorDTO;

import java.sql.SQLException;
import java.util.*;

// 저자와 관련된 로직
public class AuthorService {
    AuthorDAO authorDAO;

    HashMap<Integer, String> selectedAuthors;

    public AuthorService(AuthorDAO authorDAO) {
        selectedAuthors = new HashMap<>();
        this.authorDAO = authorDAO;
    }

    public void clearSelectedAuthor() {selectedAuthors.clear();}

    // 저자 선택, 도서 관리 화면에는 저자 관리 화면을 닫으면 업데이트된다.
    public void selectAuthor(int number, String name) {
        selectedAuthors.put(number, name);
    }

    // 선택한 저자 번호 목록 가져오기
    public Set<Integer> getSelectedAuthorNumbers() {
        return selectedAuthors.keySet();
    }

    // 선택한 저자 이름 목록 가져오기
    public Collection<String> getSelectedAuthorNames() {
        return selectedAuthors.values();
    }

    // 번호로 저자정보 가져오기
    public AuthorDTO getAuthorByNumber(int number) throws SQLException {
        return authorDAO.getAuthorByNumber(number);
    }

    // 키워드로 저자 검색
    public ArrayList<AuthorDTO> getAuthorsByKeyword(String keyword) throws SQLException{
        return authorDAO.getAuthorsByKeyword(keyword);
    }

    // 저자 추가
    public void insertAuthor(String name) throws SQLException {
        authorDAO.insertAuthor(name);
    }

    // 저자 수정
    public void modifyAuthor(int number, String name) throws SQLException {
        authorDAO.modifyAuthor(number, name);
    }
}
