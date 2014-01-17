package de.saxsys.jfx.chattorama;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Starter extends Application {

    public static void main(String...args){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Chattorama");

        Label label = new Label("Chattorama");
        stage.setScene(new Scene(label, 300, 200));

        stage.show();
    }
}
