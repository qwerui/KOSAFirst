/*
 * 작업자 : 장원석
 */

package org.kosa.bookmanagement.model.service;

import org.kosa.bookmanagement.model.dao.OverdueDAO;
import org.kosa.bookmanagement.model.dto.RentDTO;
import org.kosa.bookmanagement.util.RentUtils;

import java.sql.SQLException;
import java.util.List;

public class OverdueServiceImpl implements OverdueService {
    private OverdueDAO overdueDAO;

    public OverdueServiceImpl(OverdueDAO overdueDAO) {
        this.overdueDAO = overdueDAO;
    }

    @Override
    public List<RentDTO> getAllOverdueRents() {
        try {
            return overdueDAO.getAllOverdueRents();
        } catch (SQLException e) {
            System.err.println("Error getting all overdue rents: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<RentDTO> getOverdueRentByID(String id) {
        try {
            return overdueDAO.getOverdueRentByID(id);
        } catch (SQLException e) {
            System.err.println("Error getting overdue rent by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<RentDTO> getOverdueRentByISBN(String isbn) {
        try {
            return overdueDAO.getOverdueRentByISBN(isbn);
        } catch (SQLException e) {
            System.err.println("Error getting overdue rent by ISBN: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateAllOverdueStatus() {
        try {
            List<RentDTO> allRents = overdueDAO.getAllOverdueRents();
            for (RentDTO rent : allRents) {
                boolean overdue = RentUtils.isOverdue(rent);
                overdueDAO.updateOverdueStatus(rent.getRentNumber(), overdue);
            }
        } catch (SQLException e) {
            System.err.println("Error updating all overdue statuses: " + e.getMessage());
        }
    }

    @Override
    public boolean updateOverdueStatus(int rentNumber, boolean isOverdue) {
        try {
            return overdueDAO.updateOverdueStatus(rentNumber, isOverdue);
        } catch (SQLException e) {
            System.err.println("Error updating overdue status: " + e.getMessage());
            return false;
        }
    }
}
