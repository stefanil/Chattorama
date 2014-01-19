package de.saxsys.jfx.chattorama;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;

import com.aquafx_project.AquaFx;
import com.aquafx_project.controls.skin.styles.MacOSDefaultIcons;

import de.saxsys.jfx.chattorama.resource.ImageRegistry;
import de.saxsys.jfx.chattorama.resource.Images;

public class ChatView {

	@FXML
	TextField nameField;

	@FXML
	TextFlow messageBox;	

	@FXML
	Button newButton;

	@FXML
	ListView<HBox> liste;
	
	@FXML
    private Button smileEmoticon;
	
	@FXML
    private Button cryEmoticon;

    @FXML
    private Button rhinoEmoticon;
    
    @FXML
    private Button laughEmoticon;

    @FXML
    private Button eagleEmoticon;

    @FXML
    private Button bullEmoticon;

    @FXML
    void onCryClicked(ActionEvent event) {

    }

    @FXML
    void onSmileClicked(ActionEvent event) {

    }

    @FXML
    void onLaughClicked(ActionEvent event) {

    }

    @FXML
    void onRhinoClicked(ActionEvent event) {

    }

    @FXML
    void onBullClicked(ActionEvent event) {

    }

    @FXML
    void onEagleClicked(ActionEvent event) {

    }
    
	public void style() {
		
		AquaFx.createTextFieldStyler()
				.setSizeVariant(Configuration.AQUAFX_CONROL_SIZE_VARIANT)
//				.setType(TextFieldType.ROUND_RECT)
				.style(nameField);
		
		// both does not work 4 TextFlow
//		AquaFx.createTextAreaStyler()
//				.setSizeVariant(Configuration.AQUAFX_CONROL_SIZE_VARIANT)
//				.style(messageBox);
//		// set size variant, where there is no style creator implemented yet in aquafx
//		AquaFx.resizeControl(messageBox, Configuration.AQUAFX_CONROL_SIZE_VARIANT);
		AquaFx.setGroupBox(messageBox);
		
		AquaFx.createButtonStyler()
				.setSizeVariant(Configuration.AQUAFX_CONROL_SIZE_VARIANT)
//				.setType(ButtonType.ROUND_RECT)
				.setIcon(MacOSDefaultIcons.SHARE)
				.style(newButton);
//		newButton.setGraphic(ImageRegistry.instance().getImageView(Images.SEND_POST));
		
		// default button + image view
		smileEmoticon.setGraphic(ImageRegistry.getImageView(Images.SMILE));
		cryEmoticon.setGraphic(ImageRegistry.getImageView(Images.CRY));
		laughEmoticon.setGraphic(ImageRegistry.getImageView(Images.LAUGH));
		
		// button + emoticon
		rhinoEmoticon.setGraphic(ImageRegistry.getImageView(Images.RHINO));
		bullEmoticon.setGraphic(ImageRegistry.getImageView(Images.BULL));
		eagleEmoticon.setGraphic(ImageRegistry.getImageView(Images.EAGLE));
		
	}

}
