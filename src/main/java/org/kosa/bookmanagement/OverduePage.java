package org.kosa.bookmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kosa.bookmanagement.controller.OverdueController;
import org.kosa.bookmanagement.controller.RentController;
import org.kosa.bookmanagement.model.dao.OverdueDAO;
import org.kosa.bookmanagement.model.dao.OverdueDAOImpl;
import org.kosa.bookmanagement.model.dao.RentDAO;
import org.kosa.bookmanagement.model.dao.RentDAOImpl;
import org.kosa.bookmanagement.model.service.OverdueServiceImpl;
import org.kosa.bookmanagement.model.service.RentServiceImpl;
import org.kosa.bookmanagement.util.DBManager;

import java.sql.Connection;

public class OverduePage {
    public void showOverdueStage() {
        try {
            Connection connection = DBManager.getInstance().getConnection();

            RentDAO rentDAO = new RentDAOImpl(connection);
            OverdueDAO overdueDAO = new OverdueDAOImpl(connection);
            RentServiceImpl rentService = new RentServiceImpl(rentDAO);
            OverdueServiceImpl overdueService = new OverdueServiceImpl(overdueDAO);

            FXMLLoader loader = new FXMLLoader(RentPage.class.getResource("Overdue.fxml"));
            Parent root = loader.load();

            OverdueController overdueController = loader.getController();
            overdueController.setRentService(rentService);
            overdueController.setOverdueService(overdueService);
            overdueController.loadRentalsData(); // 데이터를 로드

            Stage overdueStage = new Stage();
            overdueStage.setTitle("연체 목록");
            overdueStage.setScene(new Scene(root));
            overdueStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
