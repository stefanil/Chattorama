package de.saxsys.jfx.chattorama;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
//import sun.net.idn.StringPrep;

public class MessageView  {

    String id;

    @FXML
    Label nameLabel;

    @FXML
    Label dateLabel;

    @FXML
//    TextArea messageBox;
    TextFlow messageBox;
}
