/*
 * 작업자 : 이하린
 */

package org.kosa.bookmanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.kosa.bookmanagement.model.service.MemberService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SignUpController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private PasswordField passwordField1;
    @FXML
    private PasswordField passwordField2;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private Button signUpButton2;
    @FXML
    private Button idCheckButton;

    private MemberService memberService;
    private Stage signUpStage = null;
    private Stage loginStage = null;
    private Stage bookSearchStage = null;
    private boolean isIdChecked = false;
    private boolean isDuplicatedId = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signUpButton2.setOnMouseClicked(this::onSignUpButtonClicked);
        idCheckButton.setOnMouseClicked(this::onIdCheckButtonClicked);
    }

    public void setSignUpView(Stage view) {
        this.signUpStage = view;
    }

    public Stage getLoginView() {
        return loginStage;
    }

    public void setLoginView(Stage view) {
        loginStage = view;
    }

    public void setBookSearchView(Stage view) {
        bookSearchStage = view;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    private void onSignUpButtonClicked(MouseEvent event) {
        try {
            String id = idField.getText().trim();
            String password1 = passwordField1.getText().trim();
            String password2 = passwordField2.getText().trim();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();

            if (!isIdChecked) {
                showAlert(AlertType.ERROR, "입력 오류", "ID 중복 체크를 먼저 해주세요.");
                return;
            }

            if (isDuplicatedId) {
                showAlert(AlertType.ERROR, "입력 오류", "중복된 아이디로 가입할 수 없습니다.");
                return;
            }

            if (!password1.equals(password2)) {
                showAlert(AlertType.ERROR, "입력 오류", "비밀번호 확인값이 일치하지 않습니다.");
                return;
            }

            if (!inputValidation(id, password2, name, phone)) {
                return;
            }

            boolean isSuccess = memberService.registerMember(id, password2, name, phone);

            if (isSuccess) {
                showAlert(AlertType.INFORMATION, "회원가입 성공", "회원가입이 완료되었습니다.");
                idField.clear();
                passwordField1.clear();
                passwordField2.clear();
                nameField.clear();
                phoneField.clear();
                signUpStage.hide();

            } else {
                showAlert(AlertType.ERROR, "회원가입 실패", "회원가입에 실패하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "오류", "오류가 발생했습니다.");
        }
    }

    private void onIdCheckButtonClicked(MouseEvent event) {
        try {
            String id = idField.getText();
            System.out.println(id);
            if (!isValidId(id)) {
                return;
            }

            boolean existedId = memberService.isExistedId(id);
            isIdChecked = true;
            isDuplicatedId = existedId;

            if (existedId) {
                showAlert(AlertType.ERROR, "중복 아이디", "중복된 아이디입니다.");
            } else {
                showAlert(AlertType.INFORMATION, "사용 가능 아이디", "사용 가능한 아이디입니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "오류", "오류가 발생했습니다.");
        }
    }

    // 입력값 검증
    private boolean inputValidation(String id, String password, String name, String phone) {
        if (id.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            showAlert(AlertType.ERROR, "입력 오류", "모든 필드를 채워주세요.");
            return false;
        }

        if (!isValidId(id) || !isValidPassword(password) || !isValidName(name) || !isValidPhone(phone)) {
            return false;
        }
        return true;
    }

    // ID 유효성 검증
    private boolean isValidId(String id) {
        if (id.isEmpty() || id.length() < 5 || id.length() > 20) {
            showAlert(AlertType.ERROR, "입력 오류", "ID는 5자 이상 20자 이하로 입력해주세요.");
            return false;
        }

        if (!Pattern.matches("[a-zA-Z0-9]*", id)) {
            showAlert(AlertType.ERROR, "입력 오류", "ID는 영문자와 숫자만 포함해야 합니다.");
            return false;
        }
        return true;
    }

    // 비밀번호 유효성 검증
    private boolean isValidPassword(String password) {
        if (password.isEmpty() || password.length() < 8 || password.length() > 20) {
            showAlert(AlertType.ERROR, "입력 오류", "비밀번호는 8자 이상 20자 이하로 입력해주세요.");
            return false;
        }
        if (!Pattern.matches("[a-zA-Z0-9@#$%^&+=]*", password)) {
            showAlert(AlertType.ERROR, "입력 오류", "비밀번호는 영문자, 숫자, 특수문자(@#$%^&+=)만 포함해야 합니다.");
            return false;
        }
        return true;
    }

    // 이름 유효성 검증
    private boolean isValidName(String name) {
        if (!Pattern.matches("[가-힣a-zA-Z]*", name)) {
            showAlert(AlertType.ERROR, "입력 오류", "이름은 한글 또는 영문자만 포함해야 합니다.");
            return false;
        }
        return true;
    }

    // 전화번호 유효성 검증
    private boolean isValidPhone(String phone) {
        if (!Pattern.matches("\\d{11}", phone)) {
            showAlert(AlertType.ERROR, "입력 오류", "전화번호는 11자리 숫자여야 합니다.");
            return false;
        }
        return true;
    }

    // 알림 메시지 표시
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}