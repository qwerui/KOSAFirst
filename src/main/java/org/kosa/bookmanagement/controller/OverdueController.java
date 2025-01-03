/*
 * 작업자 : 장원석
 */


package org.kosa.bookmanagement.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.kosa.bookmanagement.model.dto.RentDTO;
import org.kosa.bookmanagement.model.service.OverdueServiceImpl;
import org.kosa.bookmanagement.model.service.RentServiceImpl;
import org.kosa.bookmanagement.util.MemberState;
import org.kosa.bookmanagement.util.RentUtils;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OverdueController implements Initializable {

    @FXML
    private Button returnButton;
    @FXML
    private TableView<RentDTO> overdueTable;
    @FXML
    private TableColumn<RentDTO, String> titleColumn;
    @FXML
    private TableColumn<RentDTO, String> authorColumn;
    @FXML
    private TableColumn<RentDTO, Date> rentDateColumn;
    @FXML
    private TableColumn<RentDTO, String> returnDateColumn;
    @FXML
    private TableColumn<RentDTO, Integer> extendedColumn;
    @FXML
    private TableColumn<RentDTO, String> idColumn;

    private ObservableList<RentDTO> overduesData;
    private RentServiceImpl rentServiceImpl;
    private OverdueServiceImpl overdueServiceImpl;

    public void setRentService(RentServiceImpl rentServiceImpl) {
        this.rentServiceImpl = rentServiceImpl;
    }

    public void setOverdueService(OverdueServiceImpl overdueService) {
        this.overdueServiceImpl = overdueService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
        rentDateColumn.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        extendedColumn.setCellValueFactory(new PropertyValueFactory<>("extended"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        returnDateColumn.setCellValueFactory(cellData -> {
            RentDTO rent = cellData.getValue();
            Date returnDate = RentUtils.calculateReturnDate(rent);
            return new SimpleStringProperty(returnDate.toString());
        });

        overdueTable.setRowFactory(tv -> new TableRow<RentDTO>() {
            @Override
            protected void updateItem(RentDTO rent, boolean empty) {
                super.updateItem(rent, empty);

                if (empty || rent == null) {
                    setText(null);
                    setStyle(""); // 스타일 초기화
                    setOnMouseClicked(null);  // 클릭 이벤트 초기화
                    setDisable(false);        // 행 활성화 초기화
                } else {
                    setStyle(""); // 스타일 초기화
                    setOnMouseClicked(event -> handleRowSelect(event));  // 클릭 이벤트 활성화
                    setDisable(false);        // 행 활성화
                }
            }
        });

        returnButton.setDisable(true);
    }

    public void loadRentalsData() {
        try {
            List<RentDTO> overdues = overdueServiceImpl.getAllOverdueRents();
            overduesData = FXCollections.observableArrayList(overdues);
            overdueTable.setItems(overduesData);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRowSelect(MouseEvent event) {
        RentDTO selectedRent = overdueTable.getSelectionModel().getSelectedItem();
        returnButton.setDisable(selectedRent == null);

    }

    @FXML
    private void handleReturn() {
        RentDTO selectedRent = overdueTable.getSelectionModel().getSelectedItem();
        if (selectedRent != null) {
            // 선택된 항목을 콘솔에 출력
            System.out.println("Selected RentPage: " + selectedRent);

            boolean success = rentServiceImpl.returnBook(selectedRent.getRentNumber());
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("반납 성공");
                alert.setHeaderText(null);
                alert.setContentText("책이 성공적으로 반납되었습니다.");
                alert.showAndWait();
                loadRentalsData();  // 데이터를 다시 로드하여 테이블을 갱신합니다.
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("반납 실패");
                alert.setHeaderText(null);
                alert.setContentText("책 반납에 실패하였습니다.");
                alert.showAndWait();
            }
        } else {
            System.out.println("No item selected.");
        }
    }
}
