/*
 * 작업자 : 홍제기, 이하린
 */

package org.kosa.bookmanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kosa.bookmanagement.OverduePage;
import org.kosa.bookmanagement.RentPage;
import org.kosa.bookmanagement.model.dto.BookDTO;
import org.kosa.bookmanagement.model.service.BookService;
import org.kosa.bookmanagement.util.MemberState;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

// 도서 검색 화면을 컨트롤하는 컨트롤러
public class BookSearchController implements Initializable {

	@FXML
	private ChoiceBox<String> searchOption;
	@FXML
	private TextField inputSearch;
	@FXML
	private Button searchButton;

	@FXML
	private Button loginButton;
	@FXML
	private Button myPageButton;
	@FXML
	private Button bookManagementButton;
	@FXML
	private Button overdueButton;

	@FXML
	private VBox searchResultBox;

	private Stage bookManagementStage = null;
	private Stage bookDetailStage = null;
	private Stage loginStage = null;
	private Stage myPageStage = null;
	private Stage bookSearchStage;
	private BookService bookService = null;
	private boolean openMyPageAfterLogin = false;

	// 뷰에 연결시 호출되는 초기화 메소드
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOption.setValue("서명");
		searchOption.getItems().add("서명");
		searchOption.getItems().add("저자");

		inputSearch.setOnKeyPressed(evt -> {
			if(evt.getCode() == KeyCode.ENTER) {
				requestSearch();
			}
		});

		searchButton.setOnMouseClicked(evt -> requestSearch());

		loginButton.setOnMouseClicked(evt -> onLoginButtonClicked(evt));
		myPageButton.setOnMouseClicked(evt -> onMyPageButtonClicked(evt));
		bookManagementButton.setOnMouseClicked(evt -> onBookManagementButtonClicked(evt));
		overdueButton.setOnMouseClicked(evt -> onOverdueButtonClicked(evt));

		updateLoginButton();
	}

	public void setBookSearchStage(Stage stage) {
		this.bookSearchStage = stage;
	}

	public void setBookManagementView(Stage view) {
		bookManagementStage = view;
	}

	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}

	public void setBookDetailView(Stage view) {
		bookDetailStage = view;
	}

	public void setLoginView(Stage view) {
		loginStage = view;
	}
	public void setMyPageView(Stage view) {
		myPageStage = view;
	}
	// 로그인 버튼을 눌렀을 때 실행할 이벤트 메소드
	// 로그인 화면을 보여준다.
	private void onLoginButtonClicked(MouseEvent evt) {
		if (loginButton.getText().equals("로그아웃")){
			logout();
			return;
		}

		if (loginStage == null) {
			return;
		} else {
			loginStage.show();
		}
	}

	// 마이페이지 버튼을 눌렀을 때 실행할 이벤트 메소드
	// 마이페이지 화면을 보여준다.
	private void onMyPageButtonClicked(MouseEvent evt) {
		if (MemberState.getLoggedInMemberId() == null) {
			openMyPageAfterLogin = true;
			loginStage.show();
		} else {
			myPageStage.show();
		}
		if(myPageStage == null) {
			return;
		}
	}

	// 도서관리 버튼을 눌렀을 때 실행할 이벤트 메소드
	// 도서관리 화면을 보여준다.
	private void onBookManagementButtonClicked(MouseEvent evt) {

		if(bookManagementStage == null) {
			return;
		}

		if (bookManagementStage.isShowing()) {
			return;
		} else {
			bookManagementStage.show();
		}
	}

	private void onOverdueButtonClicked(MouseEvent evt) {

		OverduePage app = new OverduePage();
		app.showOverdueStage();
	}

	// 도서 검색 메소드
	// 찾은 도서들을 제목(저자)의 형태로 하이퍼링크를 생성한다.
	private void requestSearch() {

		ArrayList<BookDTO> bookInfos = null;

		searchResultBox.getChildren().clear();

		try {
			bookInfos = bookService.searchInfosByKeyword(searchOption.getValue(), inputSearch.getText());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		if(bookInfos != null) {
			for (var bookInfo : bookInfos) {
				Hyperlink hyperlink = new Hyperlink();
				hyperlink.setText(bookInfo.getTitle() + " (" + bookInfo.getAuthor() + ")");
				hyperlink.setOnMouseClicked(evt -> openBookDetail(bookInfo));
				searchResultBox.getChildren().add(hyperlink);
			}
		}
	}

	// 도서 상세 화면 열기
	private void openBookDetail(BookDTO bookDTO) {
		if(bookDetailStage == null) {
			return;
		}

		if(bookDetailStage.isShowing()) {
			return;
		} else {
			bookService.setSelectedBookInfo(bookDTO);
			bookDetailStage.show();
		}
	}

	// 로그인 버튼 상태 변경
	public void updateLoginButton() {
		if (MemberState.getLoggedInMemberId() == null) {
			loginButton.setText("로그인");
			bookManagementButton.setVisible(false);
			overdueButton.setVisible(false);
		} else {
			if (MemberState.isAdmin()) {
				bookManagementButton.setVisible(true);
				overdueButton.setVisible(true);
			} else {
				bookManagementButton.setVisible(false);
				overdueButton.setVisible(false);
			}
			loginButton.setText("로그아웃");
		}
	}

	// 로그 아웃 메소드
	public void logout() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "로그아웃 하시겠습니까?", ButtonType.YES, ButtonType.NO);
//		loginStage.close();
		alert.showAndWait().ifPresent(type -> {
			if (type == ButtonType.YES) {
				MemberState.clear();
				updateLoginButton();
				bookSearchStage.show();
			} else {
				bookSearchStage.show();
			}
		});
	}

	// 로그인 성공 후 호출될 메소드
	public void handleLoginSuccess() {
		updateLoginButton();

		if (openMyPageAfterLogin) {
			openMyPageAfterLogin = false;
			if (myPageStage != null) {
				myPageStage.show();
			}
		} else {
			if (bookSearchStage != null) {
				bookSearchStage.show();
			}
		}
	}
}