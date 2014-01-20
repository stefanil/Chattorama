package de.saxsys.jfx.chattorama.scene.control;

import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;

import com.aquafx_project.AquaFx;
import com.aquafx_project.controls.skin.styles.styler.LabelStyler;

import de.saxsys.jfx.chattorama.Configuration;

public class MessageView extends Control {

	String id;

	@FXML
	public Label nameLabel;

	@FXML
	public Label dateLabel;

	@FXML
	public TextFlow messageBox;

	public MessageView() {
		// set skin class using css
		getStyleClass().add("custom-control");
	}

	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource("custom-control.css").toExternalForm();
	}

	/**
     * 
     */
	public void initialize() {

		LabelStyler ls = AquaFx.createLabelStyler().setSizeVariant(
				Configuration.AQUAFX_CONROL_SIZE_VARIANT);
		ls.style(nameLabel);
		ls.style(dateLabel);

	}

}
