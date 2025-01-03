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
import org.kosa.bookmanagement.model.service.RentServiceImpl;
import org.kosa.bookmanagement.util.MemberState;
import org.kosa.bookmanagement.util.RentUtils;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class RentController implements Initializable {

    @FXML
    private Button extendButton;
    @FXML
    private Button returnButton;
    @FXML
    private TableView<RentDTO> rentalsTable;
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
    private TableColumn<RentDTO, String> statusColumn;

    private ObservableList<RentDTO> rentalsData;
    private RentServiceImpl rentServiceImpl;

    public void setRentService(RentServiceImpl rentService) {
        this.rentServiceImpl = rentService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
        rentDateColumn.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        extendedColumn.setCellValueFactory(new PropertyValueFactory<>("extended"));

        returnDateColumn.setCellValueFactory(cellData -> {
            RentDTO rent = cellData.getValue();
            Date returnDate = RentUtils.calculateReturnDate(rent);
            return new SimpleStringProperty(returnDate.toString());
        });

        statusColumn.setCellValueFactory(cellData -> {
            RentDTO rent = cellData.getValue();
            if (rent.getReturnDate() != null) {
                return new SimpleStringProperty("반납");
            } else if (RentUtils.isOverdue(rent)) {
                return new SimpleStringProperty("연체");
            } else {
                return new SimpleStringProperty("미반납");
            }
        });

        rentalsTable.setRowFactory(tv -> new TableRow<RentDTO>() {
            @Override
            protected void updateItem(RentDTO rent, boolean empty) {
                super.updateItem(rent, empty);

                if (empty || rent == null) {
                    setText(null);
                    setStyle(""); // 스타일 초기화
                    setOnMouseClicked(null);  // 클릭 이벤트 초기화
                    setDisable(false);        // 행 활성화 초기화
                } else {
                    if (rent.getReturnDate() != null) {  // 이미 반납된 경우
                        setStyle("-fx-background-color: lightgray;"); // 회색 배경색 적용
                        setOnMouseClicked(null);  // 클릭 이벤트 비활성화
                        setDisable(true);         // 행 비활성화
                    } else if (RentUtils.isOverdue(rent)) { // 연체된 경우
                        setStyle("-fx-background-color: AQUAMARINE;"); // 붉은 배경색 적용
                        setOnMouseClicked(event -> handleRowSelect(event));  // 클릭 이벤트 활성화
                        setDisable(false);        // 행 활성화
                    } else {
                        setStyle(""); // 스타일 초기화
                        setOnMouseClicked(event -> handleRowSelect(event));  // 클릭 이벤트 활성화
                        setDisable(false);        // 행 활성화
                    }
                }
            }
        });

        extendButton.setDisable(true);
        returnButton.setDisable(true);
    }


    public void loadRentalsData() {
        try {
            String userId = MemberState.getLoggedInMemberId();  // Replace with actual user ID
            List<RentDTO> rentals = rentServiceImpl.getRentByID(userId);
            rentalsData = FXCollections.observableArrayList(rentals);
            rentalsTable.setItems(rentalsData);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRowSelect(MouseEvent event) {
        RentDTO selectedRent = rentalsTable.getSelectionModel().getSelectedItem();
        if (selectedRent != null) {
            if (RentUtils.isOverdue(selectedRent)) {
                extendButton.setDisable(true);
                returnButton.setDisable(true);
            } else {
                extendButton.setDisable(false);
                returnButton.setDisable(false);
            }
        } else {
            extendButton.setDisable(true);
            returnButton.setDisable(true);
        }
    }

    @FXML
    private void handleExtend() {
        RentDTO selectedRent = rentalsTable.getSelectionModel().getSelectedItem();
        if (selectedRent != null) {
            // 선택된 항목을 콘솔에 출력
            System.out.println("Selected RentPage: " + selectedRent);

            boolean success = rentServiceImpl.extendRent(selectedRent.getRentNumber());
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("연장 성공");
                alert.setHeaderText(null);
                alert.setContentText("대출 기간이 성공적으로 연장되었습니다.");
                alert.showAndWait();
                loadRentalsData();  // 데이터를 다시 로드하여 테이블을 갱신합니다.
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("연장 실패");
                alert.setHeaderText(null);
                alert.setContentText("대출 기간 연장에 실패하였습니다. 최대 연장 횟수(2회)를 초과하였거나 이미 반납된 책입니다.");
                alert.showAndWait();
            }
        } else {
            System.out.println("No item selected.");
        }
    }

    @FXML
    private void handleReturn() {
        RentDTO selectedRent = rentalsTable.getSelectionModel().getSelectedItem();
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
