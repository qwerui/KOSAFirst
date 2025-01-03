/*
 * 작업자 : 홍제기
 */

package org.kosa.bookmanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.kosa.bookmanagement.model.dto.BookDTO;
import org.kosa.bookmanagement.model.service.AuthorService;
import org.kosa.bookmanagement.model.service.BookService;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

// 도서 관리 화면을 컨트롤하는 컨트롤러
public class BookManagementController implements Initializable {

	@FXML
	private TextField inputISBN;
	@FXML
	private TextField inputTitle;
	@FXML
	private TextField inputAuthor;
	@FXML
	private TextField inputPublisher;
	@FXML
	private ChoiceBox<String> categoryChoice;
	@FXML
	private TextField inputPublishYear;

	@FXML
	private Button addAuthorButton;
	@FXML
	private Button resetAuthorButton;

	@FXML
	private Button selectISBNButton;
	@FXML
	private Button updateISBNButton;
	@FXML
	private Button deleteISBNButton;

	@FXML
	private Button bookInsertButton;
	@FXML
	private Button bookDeleteButton;

	@FXML
	private TextArea resultTextArea;

	@FXML
	private TextField inputBookNumber;
	@FXML
	private ChoiceBox<String> positionChoice;
	@FXML
	private Button selectBookNumberButton;

	private BookService bookService;
	private Stage authorManagementView;
	private AuthorService authorService;

	private StringBuilder sb;

	private HashMap<String, String> categoryMap;
	private HashMap<String, Integer> positionMap;

	// 뷰에 연결시 호출되는 초기화 메소드
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		sb = new StringBuilder();

		addAuthorButton.setOnMouseClicked(evt -> onAddAuthorButtonClicked(evt));
		resetAuthorButton.setOnMouseClicked(evt -> {
			authorService.clearSelectedAuthor();
			inputAuthor.clear();
		});

		bookInsertButton.setOnMouseClicked(evt -> onBookInsertButtonClicked(evt));
		bookDeleteButton.setOnMouseClicked(evt -> onBookDeleteButtonClicked(evt));

		selectISBNButton.setOnMouseClicked(evt -> onSelectISBNButtonClicked(evt));
		updateISBNButton.setOnMouseClicked(evt -> onUpdateISBNButtonClicked(evt));
		deleteISBNButton.setOnMouseClicked(evt -> onDeleteISBNButtonClicked(evt));

		selectBookNumberButton.setOnMouseClicked(evt -> onSelectBookNumberButtonClicked(evt));

		categoryChoice.setValue("총류");
		categoryChoice.getItems().add("총류");
		categoryChoice.getItems().add("철학");
		categoryChoice.getItems().add("종교");
		categoryChoice.getItems().add("사회과학");
		categoryChoice.getItems().add("자연과학");
		categoryChoice.getItems().add("기술과학");
		categoryChoice.getItems().add("예술");
		categoryChoice.getItems().add("언어");
		categoryChoice.getItems().add("문학");
		categoryChoice.getItems().add("역사");

		categoryMap = new HashMap<>();
		categoryMap.put("총류", "000");
		categoryMap.put("철학", "100");
		categoryMap.put("종교", "200");
		categoryMap.put("사회과학", "300");
		categoryMap.put("자연과학", "400");
		categoryMap.put("기술과학", "500");
		categoryMap.put("예술", "600");
		categoryMap.put("언어", "700");
		categoryMap.put("문학", "800");
		categoryMap.put("역사", "900");

		positionChoice.setValue("종합열람실");
		positionChoice.getItems().add("종합열람실");
		positionChoice.getItems().add("제1서고");
		positionChoice.getItems().add("제2서고");
		positionChoice.getItems().add("E북");

		positionMap = new HashMap<>();
		positionMap.put("종합열람실", 1);
		positionMap.put("제1서고", 2);
		positionMap.put("제2서고", 3);
		positionMap.put("E북", 4);

	}

	// 저자 추가 버튼 클릭 이벤트
	// 도서에 저자를 추가하거나 DB에 저자를 추가하는 창 추가
	private void onAddAuthorButtonClicked(MouseEvent evt) {

		if(authorManagementView == null){
			return;
		}

		if(authorManagementView.isShowing()){
			return;
		} else {
			authorManagementView.show();
		}

	}

	public void setBookService(BookService bookService){
		this.bookService = bookService;
	}
	public void setAuthorService(AuthorService authorService) {this.authorService = authorService;}

	public void setAuthorManagementView(Stage authorView){
		authorManagementView = authorView;

		// 저자 관리 화면 닫을 때 저자 필드 업데이트
		authorManagementView.setOnHiding(evt->{
			sb.setLength(0);
			for(String str : authorService.getSelectedAuthorNames()) {
				if(!sb.isEmpty()){
					sb.append(", ");
				}
				sb.append(str);
			}
			inputAuthor.setText(sb.toString());
		});
	}

	// 도서 추가 버튼 클릭 이벤트
	// isbn에 해당하는 도서를 사본 테이블에 추가
	private void onBookInsertButtonClicked(MouseEvent evt){
		String isbn = inputISBN.getText();

		if (isISBNNotValid()) {
			return;
		}

		try {
			if(inputBookNumber.getLength() == 0)
			{
				bookService.updateCopy(isbn, 0, positionMap.get(positionChoice.getValue()));
			}
			else if(inputBookNumber.getText().matches("^[0-9]*$"))
			{
				bookService.updateCopy(isbn, Integer.parseInt(inputBookNumber.getText()), positionMap.get(positionChoice.getValue()));
			}
			else
			{
				resultTextArea.setText("[실패] : 도서번호가 유효하지 않습니다.");
				return;
			}

			resultTextArea.setText("성공적으로 도서를 추가했습니다.");
		}catch (SQLException e){
			System.out.println(e.toString());
			if(e.getErrorCode() == 2291){
				resultTextArea.setText("[실패] : ISBN이 존재하지 않습니다.");
			} else {
				resultTextArea.setText("[실패] : 데이터베이스 오류");
			}
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}

	}

	// 도서 삭제 버튼 클릭 이벤트
	// isbn에 해당하는 도서를 사본 테이블에서 삭제
	private void onBookDeleteButtonClicked(MouseEvent evt){
		String isbn = inputISBN.getText();

		if (isISBNNotValid()) {
			return;
		}

		try {
			if(inputBookNumber.getLength() == 0)
			{
				bookService.deleteCopy(isbn, 0);
			}
			else if(inputBookNumber.getText().matches("^[0-9]*$"))
			{
				bookService.deleteCopy(isbn, Integer.parseInt(inputBookNumber.getText()));
			}
			else
			{
				resultTextArea.setText("[실패] : 도서번호가 유효하지 않습니다.");
				return;
			}

			resultTextArea.setText("성공적으로 도서를 삭제했습니다.");
		}catch (SQLException e){
			resultTextArea.setText("[실패] : 데이터베이스 오류");
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	// ISBN 행의 조회 버튼 클릭 메소드
	// 해당하는 ISBN이 존재하면 모든 TextField가 채워진다.
	private void onSelectISBNButtonClicked(MouseEvent evt) {
		
		String isbn = inputISBN.getText();

		if (isISBNNotValid()) {
			return;
		}

		BookDTO foundBook = null;

		try {
			foundBook = bookService.searchBookByISBN(isbn);
		} catch (SQLException e) {
			resultTextArea.setText("[실패] : 데이터베이스 오류.");
		}

		if(foundBook == null){
			resultTextArea.setText("[실패] : 해당하는 ISBN을 가진 도서가 없음.");
			return;
		}

		inputTitle.setText(foundBook.getTitle());
		inputAuthor.setText(foundBook.getAuthor());
		inputPublisher.setText(foundBook.getPublisher());
		inputPublishYear.setText(foundBook.getPublishYear());
		categoryChoice.setValue(foundBook.getCategory());

		resultTextArea.setText("[요청 성공]");
	}

	// ISBN 행의 추가/수정 버튼 클릭 메소드
	// 해당하는 ISBN을 Insert/Update
	private void onUpdateISBNButtonClicked(MouseEvent evt) {
		
		String isbn = inputISBN.getText();

		if (isInputFieldNotValid()) {
			return;
		}

		try {
			BookDTO bookDTO = new BookDTO();
			bookDTO.setIsbn(inputISBN.getText());
			bookDTO.setPublisher(inputPublisher.getText());
			bookDTO.setPublishYear(inputPublishYear.getText());
			bookDTO.setTitle(inputTitle.getText());
			bookDTO.setCategory(categoryMap.get(categoryChoice.getValue()));

			bookService.updateISBN(bookDTO, authorService.getSelectedAuthorNumbers());

			resultTextArea.setText("성공적으로 추가/수정되었습니다.");

		} catch (SQLException e) {
			resultTextArea.setText("[실패] : 데이터베이스 오류");
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	// ISBN 행의 삭제 버튼 클릭 메소드
	// 해당하는 ISBN을 BookInfo 테이블에서 삭제한다.
	// Book 테이블에 1개라도 존재하는 경우 삭제 불가능
	// 삭제 실패 시 ISBN을 갖는 도서번호 출력
	private void onDeleteISBNButtonClicked(MouseEvent evt) {

		String isbn = inputISBN.getText();

		if (isISBNNotValid()) {
			return;
		}

		try{
			bookService.deleteISBN(isbn);
			resultTextArea.setText("성공적으로 삭제되었습니다.");
		}catch (SQLException e){
			resultTextArea.setText("[실패] : 데이터베이스 오류");
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}


	}

	// 전체 번호 조회 버튼 클릭 이벤트
	// ISBN에 해당하는 도서 번호 목록 조회
	private void onSelectBookNumberButtonClicked(MouseEvent evt) {

		if(isISBNNotValid()){
			return;
		}

		try{
			List<Integer> allCopies = bookService.getAllCopies(inputISBN.getText());
			sb.setLength(0);
			for(int bookNumber : allCopies){
				sb.append(bookNumber);
				sb.append(", ");
			}
			resultTextArea.setText(sb.toString());
		} catch (SQLException e){
			resultTextArea.setText("[실패] : 데이터베이스 오류");
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	// isbn 유효성 검사
	private boolean isISBNNotValid() {
		if (inputISBN.getText().length() != 13 || !inputISBN.getText().matches("^[a-zA-Z0-9]*$")) {
			resultTextArea.setText("[실패] : ISBN이 유효하지 않습니다.");
			return true;
		}
		return false;
	}

	// InputField 유효성 검사
	private boolean isInputFieldNotValid() {

		if (isISBNNotValid()) {
			return true;
		}

		if(inputAuthor.getLength() == 0) {
			resultTextArea.setText("[실패] : 저자가 유효하지 않습니다.");
			return true;
		}

		if(inputPublisher.getLength() == 0) {
			resultTextArea.setText("[실패] : 출판사가 유효하지 않습니다.");
			return true;
		}

		if(inputPublishYear.getLength() == 0 || !inputPublishYear.getText().matches("\\d+")) {
			resultTextArea.setText("[실패] : 발행년도가 유효하지 않습니다.");
			return true;
		}

		if(inputTitle.getLength() == 0) {
			resultTextArea.setText("[실패] : 서명이 유효하지 않습니다.");
			return true;
		}

		return false;
	}
}
