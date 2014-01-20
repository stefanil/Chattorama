package de.saxsys.jfx.chattorama;

import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_DATE;
import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_MESSAGE;
import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_NAME;
import static de.saxsys.jfx.chattorama.ChatterConstants.CMD_INIT;
import static de.saxsys.jfx.chattorama.ChatterConstants.CMD_POLL;
import static de.saxsys.jfx.chattorama.ChatterConstants.CMD_POST;
import static de.saxsys.jfx.chattorama.ChatterConstants.PM_ID_INPUT;
import static de.saxsys.jfx.chattorama.ChatterConstants.TYPE_POST;
import groovy.util.Eval;

import java.util.List;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.opendolphin.binding.Converter;
import org.opendolphin.core.ModelStoreEvent;
import org.opendolphin.core.ModelStoreListener;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;
import org.opendolphin.core.client.comm.OnFinishedHandlerAdapter;

import com.aquafx_project.AquaFx;

import de.saxsys.jfx.chattorama.scene.control.ChatView;
import de.saxsys.jfx.chattorama.scene.control.MessageView;

public class ChatApplication extends Application {

	static ClientDolphin clientDolphin;

	private PresentationModel postModel;

	private ChatView chatView;

	public ChatApplication() {
		ClientAttribute nameAttribute = new ClientAttribute(ATTR_NAME, "");
		ClientAttribute postAttribute = new ClientAttribute(ATTR_MESSAGE, "");
		ClientAttribute dateAttribute = new ClientAttribute(ATTR_DATE, "");
		postModel = clientDolphin.presentationModel(PM_ID_INPUT, nameAttribute,
				postAttribute, dateAttribute);
	}

	@Override
	public void start(Stage stage) throws Exception {

		// AquaFx.styleStage(stage, StageStyle.UNIFIED);
		// styling ..
		AquaFx.style();

		chatView = new ChatView(postModel, withRelease);

		createMessageView();
		addClientSideAction();

		Scene scene = new Scene(chatView);
		// scene.getStylesheets().add("/path/to/css");
		stage.setScene(scene);
		stage.setTitle(getClass().getName());

		stage.show();

		clientDolphin.send(CMD_INIT, new OnFinishedHandlerAdapter() {
			@Override
			public void onFinished(
					List<ClientPresentationModel> presentationModels) {

				System.out.println("" + presentationModels.size() + "get");

				// visualize
				FadeTransition fadeTransition = new FadeTransition(Duration
						.millis(1000), chatView);
				fadeTransition.setFromValue(0.2);
				fadeTransition.setToValue(1);
				fadeTransition.play();
				longPoll();
			}
		});

	}

	private boolean channelBlocked = false;

	private void release() {
		if (!channelBlocked)
			return; // avoid too many unblocks
		channelBlocked = false;
		String url = "https://klondike.canoo.com/dolphin-grails/chatter/release";
		String result = Eval.x(url, "x.toURL().text").toString();
		System.out.println("release result = " + result);
	}

	private void longPoll() {
		channelBlocked = true;
		clientDolphin.send(CMD_POLL, new OnFinishedHandlerAdapter() {
			@Override
			public void onFinished(
					List<ClientPresentationModel> presentationModels) {
				longPoll();
			}
		});
	}

	private final Converter withRelease = value -> {
		release();
		return value;
	};

	private void createMessageView() {

		ObservableMap<String, HBox> messageViews = FXCollections
				.observableHashMap();
		ObservableList<HBox> messages = FXCollections.observableArrayList();
		chatView.liste.setItems(messages);

		// add listener for handling presentation model changes
		clientDolphin.addModelStoreListener(TYPE_POST,
				new ModelStoreListener() {
					@Override
					public void modelStoreChanged(ModelStoreEvent event) {
						// if new post was added
						if (event.getType() == ModelStoreEvent.Type.ADDED) {

							// load messages view
							MessageView messageView = new MessageView(event
									.getPresentationModel(), withRelease);
							messages.add(messageView);
							messageViews.put(event.getPresentationModel()
									.getId(), messageView);
						}
						if (event.getType() == ModelStoreEvent.Type.REMOVED) {
							String id = event.getPresentationModel().getId();
							if (messageViews.containsKey(id)) {

								// Erst hole die HBox der Message aus der Map
								final HBox view = messageViews.get(id);
								// ... dann entferne sie aus der ListBox...
								messages.remove(view);
								// ... und anschließend aus der Map
								messageViews.remove(id);
							}

						}
					}
				});

		// on select : pm per id:
		// pm = clientDolphin.getAt("meineid")
		// clientDolphin.apply(pm).to(postModel);
		// release();

	}

	private void addClientSideAction() {
		chatView.newButton.setOnAction((ActionEvent event) -> {
			clientDolphin.send(CMD_POST);
		});
	}

}
