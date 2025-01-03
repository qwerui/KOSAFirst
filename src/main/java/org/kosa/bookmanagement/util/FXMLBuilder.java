/*
작업자 : 홍제기
 */


package org.kosa.bookmanagement.util;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kosa.bookmanagement.Main;

import java.io.IOException;

// FXML을 로드하고 Scene이나 Stage를 생성하는 클래스
public class FXMLBuilder {
    private String url;
    private Initializable controller;

    //FXML 파일을 로드할 경로 지정, 필수 과정
    public FXMLBuilder setURL(String url) {
        this.url = url;
        return this;
    }

    //FXML 파일에 연결할 컨트롤러 자정, 선택 과정
    public FXMLBuilder setController(Initializable controller) {
        this.controller = controller;
        return this;
    }

    //Scene 객체로 생성할 때 사용
    public Scene BuildScene() throws IOException {
        Scene scene = null;


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(url));
        loader.setController(controller);
        Parent view = loader.load();
        scene = new Scene(view);


        url = null;
        controller = null;

        return scene;
    }

    //Stage 객체로 생성할 때 사용
    public Stage BuildStage() throws IOException {
        Stage stage = null;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(url));
        loader.setController(controller);
        Parent view = loader.load();
        Scene scene = new Scene(view);
        stage = new Stage();
        stage.setScene(scene);


        url = null;
        controller = null;

        return stage;
    }
}
