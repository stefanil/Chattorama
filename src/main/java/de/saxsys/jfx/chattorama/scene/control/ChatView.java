package de.saxsys.jfx.chattorama.scene.control;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import com.aquafx_project.AquaFx;
import com.aquafx_project.controls.skin.styles.MacOSDefaultIcons;

import de.saxsys.jfx.chattorama.Configuration;
import de.saxsys.jfx.chattorama.Emoticons;
import de.saxsys.jfx.chattorama.resource.ImageRegistry;
import de.saxsys.jfx.chattorama.resource.Images;

public class ChatView {

	@FXML
	public TextField nameField;

	@FXML
	public TextArea messageBoxText;

	@FXML
	public Button newButton;

	@FXML
	public ListView<HBox> liste;

	@FXML
	Button rhinoEmoticon;

	@FXML
	Button eagleEmoticon;

	@FXML
	Button bullEmoticon;

	@FXML
	void onRhinoClicked(MouseEvent event) {
		append2MessageBox(Emoticons.RHINO);
	}

	@FXML
	void onBullClicked(MouseEvent event) {
		append2MessageBox(Emoticons.BULL);
	}

	@FXML
	void onEagleClicked(MouseEvent event) {
		append2MessageBox(Emoticons.EAGLE);
	}

	public void initialize() {

		AquaFx.createTextFieldStyler()
				.setSizeVariant(Configuration.AQUAFX_CONROL_SIZE_VARIANT)
				// .setType(TextFieldType.ROUND_RECT)
				.style(nameField);

		AquaFx.createButtonStyler()
				.setSizeVariant(Configuration.AQUAFX_CONROL_SIZE_VARIANT)
				// .setType(ButtonType.ROUND_RECT)
				.setIcon(MacOSDefaultIcons.SHARE).style(newButton);

		// button + emoticon
		rhinoEmoticon.setGraphic(ImageRegistry.getImageView(Images.RHINO));
		bullEmoticon.setGraphic(ImageRegistry.getImageView(Images.BULL));
		eagleEmoticon.setGraphic(ImageRegistry.getImageView(Images.EAGLE));
	}

	public Node getMessageBox() {
		messageBoxText.setFont(Font.loadFont(
				getClass().getResourceAsStream(
						"/de/saxsys/jfx/chattorama/fonts/Aegyptus313.ttf"),
				13));
		return messageBoxText;
	}
	
	private void append2MessageBox(Emoticons emoticon) {
		messageBoxText.appendText(emoticon.toString());
	}

}
