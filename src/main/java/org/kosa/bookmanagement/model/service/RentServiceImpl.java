/*
 * 작업자 : 장원석
 */

package org.kosa.bookmanagement.model.service;

import org.kosa.bookmanagement.model.dao.RentDAO;
import org.kosa.bookmanagement.model.dto.BookDTO;
import org.kosa.bookmanagement.model.dto.RentDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RentServiceImpl implements RentService{

    private RentDAO rentDAO;

    public RentServiceImpl(RentDAO rentDAO) {
        this.rentDAO = rentDAO;
    }


    @Override
    public boolean rentBook(RentDTO rent) {
        try {
            return rentDAO.insertRent(rent);
        } catch (SQLException e) {
            System.err.println("Error inserting rent: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<RentDTO> getRentByID(String id) {
        try {
            return rentDAO.getRentByID(id);
        } catch (SQLException e) {
            System.err.println("Error getting rent by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<RentDTO> getAllRents() {
        try {
            return rentDAO.getAllRents();
        } catch (SQLException e) {
            System.err.println("Error getting all rents: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean extendRent(int rentNumber) {
        try {
            return rentDAO.updateRentExtended(rentNumber);
        } catch (SQLException e) {
            System.err.println("Error extending rent: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean returnBook(int rentNumber) {
        try {
            return rentDAO.updateRentReturnDate(rentNumber);
        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRent(int rentNumber) {
        try {
            return rentDAO.deleteRent(rentNumber);
        } catch (SQLException e) {
            System.err.println("Error deleting rent: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isBookAlreadyRented(int bookNumber, String isbn) {
        try {
            return rentDAO.isBookAlreadyRented(bookNumber, isbn);
        } catch (SQLException e) {
            System.err.println("Error checking if book is already rented: " + e.getMessage());
            return false;
        }
    }
}
