package de.saxsys.jfx.chattorama.scene.control;

import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_MESSAGE;
import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_NAME;
import static org.opendolphin.binding.JFXBinder.bind;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import org.opendolphin.binding.Converter;
import org.opendolphin.core.PresentationModel;

import com.aquafx_project.AquaFx;
import com.aquafx_project.controls.skin.styles.MacOSDefaultIcons;

import de.saxsys.jfx.chattorama.Configuration;
import de.saxsys.jfx.chattorama.Emoticons;
import de.saxsys.jfx.chattorama.ViewLoader;
import de.saxsys.jfx.chattorama.resource.ImageRegistry;
import de.saxsys.jfx.chattorama.resource.Images;

public class ChatView extends VBox {

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

	public ChatView(final PresentationModel postModel,
			final Converter withRelease) {

		// load messages view (FXML)
		new ViewLoader<ChatView>(ChatView.class, this);

		bind("text").of(nameField).to(ATTR_NAME).of(postModel, withRelease);
		bind(ATTR_NAME).of(postModel).to("text").of(nameField);

		bind("text").of(messageBoxText).to(ATTR_MESSAGE)
				.of(postModel, withRelease);
		bind(ATTR_MESSAGE).of(postModel).to("text").of(messageBoxText);

		style();
	}

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

	public void style() {

		AquaFx.createTextFieldStyler()
				.setSizeVariant(Configuration.AQUAFX_CONROL_SIZE_VARIANT)
				// .setType(TextFieldType.ROUND_RECT)
				.style(nameField);

		AquaFx.createButtonStyler()
				.setSizeVariant(Configuration.AQUAFX_CONROL_SIZE_VARIANT)
				// .setType(ButtonType.ROUND_RECT)
				.setIcon(MacOSDefaultIcons.SHARE).style(newButton);

		rhinoEmoticon.setGraphic(ImageRegistry.getImageView(Images.RHINO));
		bullEmoticon.setGraphic(ImageRegistry.getImageView(Images.BULL));
		eagleEmoticon.setGraphic(ImageRegistry.getImageView(Images.EAGLE));

		messageBoxText
				.setFont(Font
						.loadFont(
								getClass()
										.getResourceAsStream(
												"/de/saxsys/jfx/chattorama/fonts/Aegyptus313.ttf"),
								13));
		
		setOpacity(0.2);
	}

	private void append2MessageBox(Emoticons emoticon) {
		messageBoxText.appendText(emoticon.toString());
	}

}
