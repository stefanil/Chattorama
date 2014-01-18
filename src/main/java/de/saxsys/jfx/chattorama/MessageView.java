package de.saxsys.jfx.chattorama;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import sun.net.idn.StringPrep;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageView  {

    String id;

    @FXML
    Label nameLabel;

    @FXML
    Label dateLabel;

    @FXML
    TextArea messageBox;
}
