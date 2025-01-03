/*
 * 작업자 : 홍제기
 */


package org.kosa.bookmanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.kosa.bookmanagement.model.dto.BookDTO;
import org.kosa.bookmanagement.model.dto.RentDTO;
import org.kosa.bookmanagement.model.service.BookService;
import org.kosa.bookmanagement.model.service.RentService;
import org.kosa.bookmanagement.util.MemberState;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

// 도서 상세 정보화면을 컨트롤하는 컨트롤러
public class BookDetailController implements Initializable{

    private BookService bookService;
	private RentService rentService;
	
	@FXML private Label titleLabel;
	@FXML private Label authorLabel;
	@FXML private Label publisherLabel;
	@FXML private Label publishYearLabel;
	@FXML private Label classficationLabel;
	@FXML private Label isbnLabel;

	@FXML private VBox bookNumberBox;
	
	// 뷰에 연결시 호출되는 초기화 메소드
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
	
	// 도서 상세 정보를 출력
	public void updateDetail() {
        BookDTO bookDTO = bookService.getSelectedBookInfo();
		
		titleLabel.setText(bookDTO.getTitle());
		authorLabel.setText(bookDTO.getAuthor());
		publisherLabel.setText(bookDTO.getPublisher());
		publishYearLabel.setText(bookDTO.getPublishYear());
		classficationLabel.setText(bookDTO.getCategory());
		isbnLabel.setText(bookDTO.getIsbn());
		
		bookNumberBox.getChildren().clear();

		try {
			for(String str : bookService.getAvaliableCopies(bookDTO.getIsbn())){
				Hyperlink hyperlink = new Hyperlink();
				hyperlink.setText(str);

				// 로그인 아닐 시 비활성화
				if(MemberState.getLoggedInMemberId() == null){
					hyperlink.setDisable(true);
				}

				// 클릭 시 대출
				hyperlink.setOnMouseClicked(evt->rentBook(hyperlink.getText()));

				bookNumberBox.getChildren().add(hyperlink);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}

	public void setRentService(RentService rentService) {
		this.rentService = rentService;
	}

	// 하이퍼링크 클릭 시 대출 시도
	private void rentBook(String copyString){

		// 링크에 있는 정보를 이용
		// 0번 인덱스 : ISBN
		// 1번 인덱스 : 도서번호
		// 2번 인덱스 : 서적 위치 (사용안함)
		String[] copyInfos = copyString.split("[- ]");

		int bookNumber = Integer.parseInt(copyInfos[1]);
		String isbn = copyInfos[0];

		// 대출 상태 확인 -> 대출 시도
		// 요청 결과에 따라 알림 창 출력
		if(!rentService.isBookAlreadyRented(bookNumber, isbn)){
			RentDTO rentDTO = new RentDTO();
			rentDTO.setBookNumber(bookNumber);
			rentDTO.setIsbn(isbn);
			rentDTO.setId(MemberState.getLoggedInMemberId());
			rentDTO.setRentDate(new Date(new java.util.Date().getTime()));

			if(rentService.rentBook(rentDTO)){
				Alert positiveAlert = new Alert(Alert.AlertType.INFORMATION);
				positiveAlert.setHeaderText("성공적으로 대출되었습니다.");
				positiveAlert.show();
			} else {
				Alert negativeAlert = new Alert(Alert.AlertType.ERROR);
				negativeAlert.setHeaderText("도서 대출 중 오류가 발생했습니다.");
				negativeAlert.show();
			}
		} else {
			Alert negativeAlert = new Alert(Alert.AlertType.ERROR);
			negativeAlert.setHeaderText("도서가 이미 대출 중입니다.");
			negativeAlert.show();
		}

		// 대출 성공/실패 상관없이 클릭한 링크는 제거되어야함
		bookNumberBox.getChildren().removeIf(obj -> {
			Hyperlink hyperlink = (Hyperlink)obj;
			return hyperlink.getText().equals(copyString);
		});
	}

}
