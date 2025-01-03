package org.kosa.bookmanagement.model.dao;

import org.kosa.bookmanagement.model.dto.AuthorDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AuthorDAO {
    ArrayList<AuthorDTO> getAuthorsByKeyword(String keyword) throws SQLException;
    AuthorDTO getAuthorByNumber(int number) throws SQLException;
    void insertAuthor(String name) throws SQLException;
    void modifyAuthor(int number, String name) throws SQLException;
}
