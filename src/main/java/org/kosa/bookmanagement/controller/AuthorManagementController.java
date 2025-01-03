/*
작업자 : 홍제기
 */

package org.kosa.bookmanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import org.kosa.bookmanagement.model.dto.AuthorDTO;
import org.kosa.bookmanagement.model.service.AuthorService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

// 저자 관리 창을 컨트롤하는 컨트롤러
public class AuthorManagementController implements Initializable {

    @FXML
    private TextField searchAuthorField;
    @FXML
    private Button searchAuthorButton;
    @FXML
    private VBox searchResultBox;

    @FXML
    private Button findAuthorButton;
    @FXML
    private TextField authorNumberField;
    @FXML
    private TextField beforeAuthorField;
    @FXML
    private TextField afterAuthorField;

    @FXML
    private Button addAuthorButton;
    @FXML
    private Button modifyAuthorButton;

    @FXML
    private TextArea resultArea;

    private AuthorService authorService;

    private StringBuilder sb;

    // 뷰 로드 시 최초 실행 메소드, 초기화에 이용
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sb = new StringBuilder();

        searchAuthorButton.setOnMouseClicked(evt -> requestSeachAuthorByKeyword());
        searchAuthorField.setOnKeyPressed(evt -> {
            if(evt.getCode()== KeyCode.ENTER){
                requestSeachAuthorByKeyword();
            }
        });

        findAuthorButton.setOnMouseClicked(evt -> requestSearchAuthorByNumber());
        addAuthorButton.setOnMouseClicked(evt -> insertAuthor());
        modifyAuthorButton.setOnMouseClicked(evt -> modifyAuthor());
    }

    public void setAuthorService(AuthorService authorService) {
        this.authorService = authorService;
    }

    // 이름이 키워드에 해당하는 저자 목록 출력
    private void requestSeachAuthorByKeyword() {
        ArrayList<AuthorDTO> result = null;

        try{
            result = authorService.getAuthorsByKeyword(searchAuthorField.getText());
        }catch (SQLException e) {
            resultArea.setText("[실패] : 데이터베이스 에러");
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        if(result == null){
            return;
        }

        searchResultBox.getChildren().clear();

        for(AuthorDTO author : result) {
            Hyperlink hyperlink = new Hyperlink();
            // 저자 선택 시 선택 저자 목록에 추가
            // 우측의 요청 결과 화면에 선택한 저자의 목록을 표시
            hyperlink.setOnMouseClicked(evt->{
                authorService.selectAuthor(author.getAuthorNumber(), author.getAuthorName());
                sb.setLength(0);
                sb.append("선택 목록 : ");
                for(String selected : authorService.getSelectedAuthorNames()){
                    sb.append(selected);
                    sb.append(", ");
                }
                sb.setLength(sb.length()-1);
                resultArea.setText(sb.toString());
            });
            hyperlink.setText(author.getAuthorNumber()+". "+author.getAuthorName());
            searchResultBox.getChildren().add(hyperlink);
        }
    }

    // 저자 번호에 해당하는 저자 정보 요청
    // 데이터를 찾으면 기존 저자에 입력
    private void requestSearchAuthorByNumber() {
        if(authorNumberField.getLength() == 0) {
            resultArea.setText("저자 번호를 입력해주세요.");
            return;
        }

        try{
            AuthorDTO foundAuthor = authorService.getAuthorByNumber(Integer.parseInt(authorNumberField.getText()));
            beforeAuthorField.setText(foundAuthor.getAuthorName());
        } catch (NumberFormatException e) {
            resultArea.setText("저자 번호를 올바르게 입력해주세요.");
        } catch (SQLException e) {
            resultArea.setText("[실패] : 데이터베이스 에러");
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    // 번호에 해당하는 DB의 저자를 수정하는 메소드
    private void modifyAuthor() {
        if(authorNumberField.getLength() == 0) {
            resultArea.setText("저자 번호를 입력해주세요.");
            return;
        }

        if(afterAuthorField.getLength() == 0){
            resultArea.setText("새 저자를 입력해주세요.");
            return;
        }

        try{
            authorService.modifyAuthor(Integer.parseInt(authorNumberField.getText()), afterAuthorField.getText());
        }catch(NumberFormatException e){
            resultArea.setText("저자 번호를 올바르게 입력해주세요.");
        }catch(SQLException e) {
            resultArea.setText("[실패] : 데이터베이스 에러");
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        resultArea.setText("성공적으로 수정되었습니다.");
    }

    // DB에 저자를 추가하는 메소드
    private void insertAuthor() {

        if(afterAuthorField.getLength() == 0){
            resultArea.setText("새 저자를 입력해주세요.");
            return;
        }

        try{
            authorService.insertAuthor(afterAuthorField.getText());
        } catch (SQLException e) {
            resultArea.setText("[실패] : 데이터베이스 에러");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        resultArea.setText("성공적으로 입력되었습니다.");
    }

}
