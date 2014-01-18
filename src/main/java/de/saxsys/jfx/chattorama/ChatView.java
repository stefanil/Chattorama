package de.saxsys.jfx.chattorama;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ChatView{


    @FXML
    TextField nameField;

    @FXML
    TextArea messageBox;

    @FXML
    Button newButton;

    @FXML
    ListView<HBox> liste;
}
