package de.saxsys.jfx.chattorama.scene.control;

import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_DATE;
import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_MESSAGE;
import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_NAME;
import static org.opendolphin.binding.JFXBinder.bind;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;

import org.opendolphin.binding.Converter;
import org.opendolphin.core.PresentationModel;

import com.aquafx_project.AquaFx;
import com.aquafx_project.controls.skin.styles.styler.LabelStyler;

import de.saxsys.jfx.chattorama.Configuration;
import de.saxsys.jfx.chattorama.ViewLoader;

public class MessageView extends HBox {

	String id;

	@FXML
	public Label nameLabel;

	@FXML
	public Label dateLabel;

	@FXML
	public TextFlow messageBox;

	public MessageView(final PresentationModel presentationModel,
			Converter withRelease) {

		// load messages view (FXML)
		ViewLoader<MessageView> messageViewLoader = new ViewLoader<MessageView>(
				MessageView.class, this);
		messageViewLoader.getRoot().setId(presentationModel.getId());

		// style
		LabelStyler ls = AquaFx.createLabelStyler().setSizeVariant(
				Configuration.AQUAFX_CONROL_SIZE_VARIANT);
		ls.style(nameLabel);
		ls.style(dateLabel);

		// bind
		bind(ATTR_NAME).of(presentationModel).to("text").of(nameLabel);
		bind("text").of(nameLabel).to(ATTR_NAME)
				.of(presentationModel, withRelease);

		bind(ATTR_MESSAGE).of(presentationModel).to("text")
				.of(messageBox.getChildren().get(0));
		bind("text").of(messageBox.getChildren().get(0)).to(ATTR_MESSAGE)
				.of(presentationModel, withRelease);

		bind(ATTR_DATE).of(presentationModel).to("text").of(dateLabel);
		bind("text").of(dateLabel).to(ATTR_DATE)
				.of(presentationModel, withRelease);
	}

	public HBox getRoot() {
		return this;
	}

}
