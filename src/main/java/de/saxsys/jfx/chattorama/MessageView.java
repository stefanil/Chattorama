package de.saxsys.jfx.chattorama;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;

import com.aquafx_project.AquaFx;
import com.aquafx_project.controls.skin.styles.styler.LabelStyler;


public class MessageView  {

    String id;

    @FXML
    Label nameLabel;

    @FXML
    Label dateLabel;

    @FXML
    TextFlow messageBox;
    
    /**
     * 
     */
	public void initialize() {
		
		LabelStyler ls = AquaFx.createLabelStyler()
				.setSizeVariant(Configuration.AQUAFX_CONROL_SIZE_VARIANT);
		ls.style(nameLabel);
		ls.style(dateLabel);
		
	}
    
}
