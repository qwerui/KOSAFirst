/*
작업자 : 홍제기, 이하린
fxml 로드 참조 소스 : https://m.blog.naver.com/PostView.naver?blogId=take6948&logNo=221910035199&fromRecommendationType=category&targetRecommendationDetailCode=1000
 */

package org.kosa.bookmanagement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kosa.bookmanagement.controller.*;
import org.kosa.bookmanagement.model.dao.*;
import org.kosa.bookmanagement.model.service.*;
import org.kosa.bookmanagement.util.DBManager;
import org.kosa.bookmanagement.util.FXMLBuilder;

import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {

        //컨트롤러 생성
        BookSearchController bookSearchController = new BookSearchController();
        BookManagementController bookManagementController = new BookManagementController();
        BookDetailController bookDetailController = new BookDetailController();
        LoginController loginController = new LoginController();
        SignUpController signUpController = new SignUpController();
        AuthorManagementController authorManagementController = new AuthorManagementController();
        MyPageController myPageController = new MyPageController();

        //서비스 생성
        BookService bookService = new BookService(new BookDAOImpl(), new CopyDAOImpl());
        MemberService memberService = new MemberService(new MemberDAOImpl());
        AuthorService authorService = new AuthorService(new AuthorDAOImpl());

        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RentDAO rentDAO = new RentDAOImpl(connection);
        RentServiceImpl rentService = new RentServiceImpl(rentDAO);

        //컨트롤러에 서비스 연결
        bookSearchController.setBookService(bookService);
        bookManagementController.setBookService(bookService);
        bookManagementController.setAuthorService(authorService);
        authorManagementController.setAuthorService(authorService);
        bookDetailController.setBookService(bookService);
        bookDetailController.setRentService(rentService);
        myPageController.setMemberService(memberService);
        myPageController.setRentService(rentService);

        // FXML 빌더 생성
        FXMLBuilder fxmlBuilder = new FXMLBuilder();

        // 도서 검색 화면 생성
        try {
            Scene bookSearchScene = fxmlBuilder.setURL("BookSearch.fxml").setController(bookSearchController).BuildScene();
            primaryStage.setScene(bookSearchScene);
            primaryStage.show();

            // 로그인, 로그아웃 시 창 전환용 세팅
            bookSearchController.setBookSearchStage(primaryStage);
            loginController.setBookSearchView(primaryStage);
            loginController.setBookSearchController(bookSearchController);
            signUpController.setBookSearchView(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 도서 관리 화면 생성
        try {
            Stage bookManagementStage = fxmlBuilder.setURL("BookManagement.fxml").setController(bookManagementController).BuildStage();

            // 검색 화면에서 사용할 도서관리 뷰 주입
            bookSearchController.setBookManagementView(bookManagementStage);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        //도서 상세 화면 생성
        try {
            Stage bookDetailStage = fxmlBuilder.setURL("BookDetail.fxml").setController(bookDetailController).BuildStage();
            bookDetailStage.setOnShowing(evt->bookDetailController.updateDetail());

            // 검색 화면에서 사용할 도서관리 뷰 주입
            bookSearchController.setBookDetailView(bookDetailStage);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        // 로그인 화면 생성
        try {
            loginController.setMemberService(memberService);

            Stage loginStage = fxmlBuilder.setURL("Login.fxml").setController(loginController).BuildStage();
            loginController.setLoginView(loginStage); // loginStage 설정

            // 검색 화면에서 사용할 로그인 뷰 주입
            bookSearchController.setLoginView(loginStage);
            loginController.setMyPageController(myPageController);

            // 회원가입 화면 생성
            signUpController.setMemberService(memberService);

            Stage signUpStage = fxmlBuilder.setURL("SignUp.fxml").setController(signUpController).BuildStage();
            loginController.setSignUpView(signUpStage); // signUpStage 설정

            // SignUpController에 로그인 창과 회원가입 창을 설정
            signUpController.setSignUpView(signUpStage);
            signUpController.setLoginView(loginStage);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        // 마이 페이지 화면 생성
        try {
            myPageController.setMemberService(memberService);
            myPageController.setRentService(rentService);

            Stage myPageStage = fxmlBuilder.setURL("MyPage.fxml").setController(myPageController).BuildStage();
            myPageController.setMyPageView(myPageStage);

            // 검색 화면에서 사용할 마이 페이지 뷰 주입
            bookSearchController.setMyPageView(myPageStage);
            myPageController.setBookSearchController(bookSearchController);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        // 저자 관리 화면 생성
        try {
            Stage authorStage = fxmlBuilder.setURL("AuthorManagement.fxml").setController(authorManagementController).BuildStage();
            bookManagementController.setAuthorManagementView(authorStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}