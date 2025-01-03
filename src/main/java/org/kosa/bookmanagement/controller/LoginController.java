/*
 * 작업자 : 이하린
 */

package org.kosa.bookmanagement.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.kosa.bookmanagement.model.service.MemberService;
import org.kosa.bookmanagement.util.MemberState;

public class LoginController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;

    private MemberService memberService;
    private Stage signUpStage = null;
    private Stage loginStage = null;
    private Stage bookSearchStage = null;
    private BookSearchController bookSearchController;
    private MyPageController myPageController = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnMouseClicked(this::onLoginButtonClicked);
        signUpButton.setOnMouseClicked(this::onSignUpButtonClicked);
    }

    public void setSignUpView(Stage view) {
        signUpStage = view;
    }

    public void setLoginView(Stage view) {
        loginStage = view;
    }

    public void setBookSearchView(Stage view) {
        bookSearchStage = view;
    }

    public void setBookSearchController(BookSearchController controller) {
        this.bookSearchController = controller;
    }

    public void setMyPageController(MyPageController myPageController) {
        this.myPageController = myPageController;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    private void onLoginButtonClicked(MouseEvent event) {
        try {
            String id = idField.getText();
            String password = passwordField.getText();

            boolean isLoggedIn = memberService.loginMember(id, password);

            if (isLoggedIn) {
                MemberState.setLoggedInMemberId(id); // 로그인 상태 저장
                showAlert(AlertType.INFORMATION, "로그인 성공", "로그인에 성공했습니다.");
                loginStage.close();
                myPageController.setFields();
                if (bookSearchController != null) {
                    bookSearchController.handleLoginSuccess();
                    // 로그인 후 필드에 남은 사용자 입력값 삭제
                    idField.clear();
                    passwordField.clear();
                }
            } else {
                if (!MemberState.isMember()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "탈퇴 계정으로, 이용이 불가합니다. 재가입을 진행해 주세요.");
                    alert.setTitle("탈퇴 계정");
                    alert.showAndWait();
                    idField.clear();
                    passwordField.clear();
                    loginStage.close();
                } else {
                    showAlert(AlertType.ERROR, "로그인 실패", "로그인에 실패했습니다. 아이디와 비밀번호를 확인해 주세요.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "오류", "오류가 발생했습니다.");
        }
    }

    private void onSignUpButtonClicked(MouseEvent event) {
        if (signUpStage == null) {
            return;
        }

        if (signUpStage.isShowing()) {
            return;
        } else {
            loginStage.hide();
            signUpStage.show();
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}