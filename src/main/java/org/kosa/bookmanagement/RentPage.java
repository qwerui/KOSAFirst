package org.kosa.bookmanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kosa.bookmanagement.controller.RentController;
import org.kosa.bookmanagement.model.dao.RentDAO;
import org.kosa.bookmanagement.model.dao.RentDAOImpl;
import org.kosa.bookmanagement.model.service.RentServiceImpl;
import org.kosa.bookmanagement.util.DBManager;

import java.sql.Connection;

public class RentPage {
    public void showRentStage() {
        try {
            Connection connection = DBManager.getInstance().getConnection();
            RentDAO rentDAO = new RentDAOImpl(connection);
            RentServiceImpl rentService = new RentServiceImpl(rentDAO);

            FXMLLoader loader = new FXMLLoader(RentPage.class.getResource("Rent.fxml"));
            Parent root = loader.load();

            RentController rentController = loader.getController();
            rentController.setRentService(rentService);
            rentController.loadRentalsData(); // 데이터를 로드

            Stage rentStage = new Stage();
            rentStage.setTitle("대출 목록");
            rentStage.setScene(new Scene(root));
            rentStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
