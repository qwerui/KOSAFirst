/*
 * 작업자 : 이하린
 */

package org.kosa.bookmanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.kosa.bookmanagement.RentPage;
import org.kosa.bookmanagement.model.dao.MemberDAOImpl;
import org.kosa.bookmanagement.model.dto.MemberDTO;
import org.kosa.bookmanagement.model.service.MemberService;
import org.kosa.bookmanagement.model.service.RentServiceImpl;
import org.kosa.bookmanagement.util.MemberState;


import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class MyPageController implements Initializable {

    @FXML
    private PasswordField passwordField1;
    @FXML
    private PasswordField passwordField2;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private Button modifyButton;
    @FXML
    private Button rentListButton;
    @FXML
    private Button deleteAccountButton;

    private MemberService memberService;
    private Stage myPageStage = null;
    private BookSearchController bookSearchController = null;

    private RentServiceImpl rentService;

    public void setRentService(RentServiceImpl rentService) {
        this.rentService = rentService;
    }

    // 뷰에 연결시 호출되는 초기화 메소드
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modifyButton.setOnMouseClicked(this::onModifyButtonClicked);
        rentListButton.setOnMouseClicked(this::onRentListButtonClicked);
        deleteAccountButton.setOnMouseClicked(evt -> {
            try {
                onDeleteAccountButtonClicked(evt);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setMyPageView(Stage view) {
        myPageStage = view;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    public void setBookSearchController(BookSearchController bookSearchController) {
        this.bookSearchController = bookSearchController;
    }
    
    private void onModifyButtonClicked(MouseEvent mouseEvent) {
        try {
            String id = MemberState.getLoggedInMemberId();
            String password1 = passwordField1.getText().trim();
            String password2 = passwordField2.getText().trim();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();

            if (!inputValidation(id, password1, password2, name, phone)) {
                return;
            }

            boolean isSuccess = memberService.modifyMember(id, password2, name, phone);

            if (isSuccess) {
                showAlert(AlertType.INFORMATION, "정보 수정 성공", "회원 정보 수정이 완료되었습니다.");
                myPageStage.close();
                passwordField1.clear();
                passwordField2.clear();
                nameField.clear();
                phoneField.clear();
            } else {
                showAlert(AlertType.ERROR, "정보 수정 실패", "회원 정보 수정에 실패하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "오류", "오류가 발생했습니다.");
        }
    }

    private void onRentListButtonClicked(MouseEvent mouseEvent) {
        RentPage app = new RentPage();
        app.showRentStage();
    }

    private void onDeleteAccountButtonClicked(MouseEvent mouseEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "탈퇴하시겠습니까?");
        alert.setTitle("탈퇴");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean isSuccess = memberService.deleteMember(MemberState.getLoggedInMemberId());

            if (isSuccess) {
                showAlert(Alert.AlertType.INFORMATION, "탈퇴 완료", "탈퇴가 완료되었습니다.");
                myPageStage.close();
                MemberState.clear();
                bookSearchController.updateLoginButton();
            } else {
                showAlert(Alert.AlertType.ERROR, "탈퇴 실패", "탈퇴가 불가합니다. 대출 내역을 확인해 주세요.");
            }
        }
    }

    // 입력값 검증
    private boolean inputValidation(String id, String password1, String password2, String name, String phone) {
        if (id.isEmpty() || password1.isEmpty() || password2.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            showAlert(AlertType.ERROR, "입력 오류", "모든 필드를 채워주세요.");
            return false;
        }

        if (password1.length() < 8 || password1.length() > 20) {
            showAlert(AlertType.ERROR, "입력 오류", "비밀번호는 8자 이상 20자 이하로 입력해주세요.");
            return false;
        }

        if (password2.length() < 8 || password2.length() > 20) {
            showAlert(AlertType.ERROR, "입력 오류", "비밀번호는 8자 이상 20자 이하로 입력해주세요.");
            return false;
        }

        if (!password1.equals(password2)) {
            showAlert(AlertType.ERROR, "입력 오류", "비밀번호 확인값이 일치하지 않습니다.");
            return false;
        }

        if (!Pattern.matches("[a-zA-Z0-9@#$%^&+=]*", password2)) {
            showAlert(AlertType.ERROR, "입력 오류", "비밀번호는 영문자, 숫자, 특수문자(@#$%^&+=)만 포함해야 합니다.");
            return false;
        }

        if (!Pattern.matches("[가-힣a-zA-Z]*", name)) {
            showAlert(AlertType.ERROR, "입력 오류", "이름은 한글 또는 영문자만 포함해야 합니다.");
            return false;
        }

        if (!Pattern.matches("\\d{11}", phone)) {
            showAlert(AlertType.ERROR, "입력 오류", "전화번호는 11자리 숫자여야 합니다.");
            return false;
        }

        return true;
    }

    // 회원정보 수정 창에 사용자 이름, 번호를 로드
    public void setFields() throws SQLException {
        MemberDAOImpl memberDAOImpl = new MemberDAOImpl();
        String id = MemberState.getLoggedInMemberId();
        MemberDTO memberDTO = memberDAOImpl.getMemberById(id);

        nameField.setText(memberDTO.getName());
        phoneField.setText(memberDTO.getPhone());
    }

    // 알림 메시지 표시
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}