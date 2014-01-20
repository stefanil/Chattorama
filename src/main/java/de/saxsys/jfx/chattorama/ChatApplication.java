package de.saxsys.jfx.chattorama;

import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_DATE;
import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_MESSAGE;
import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_NAME;
import static de.saxsys.jfx.chattorama.ChatterConstants.CMD_INIT;
import static de.saxsys.jfx.chattorama.ChatterConstants.CMD_POLL;
import static de.saxsys.jfx.chattorama.ChatterConstants.CMD_POST;
import static de.saxsys.jfx.chattorama.ChatterConstants.PM_ID_INPUT;
import static de.saxsys.jfx.chattorama.ChatterConstants.TYPE_POST;
import static org.opendolphin.binding.JFXBinder.bind;
import groovy.util.Eval;

import java.util.List;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
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

public class ChatApplication extends Application {

	static ClientDolphin clientDolphin;

	private PresentationModel postModel;

	private ChatView chatView;

	private ViewLoader<ChatView> chatViewLoader;

	public ChatApplication() {
		ClientAttribute nameAttribute = new ClientAttribute(ATTR_NAME, "");
		ClientAttribute postAttribute = new ClientAttribute(ATTR_MESSAGE, "");
		ClientAttribute dateAttribute = new ClientAttribute(ATTR_DATE, "");
		postModel = clientDolphin.presentationModel(PM_ID_INPUT, nameAttribute,
				postAttribute, dateAttribute);
	}

	@Override
	public void start(Stage stage) throws Exception {


		chatViewLoader = new ViewLoader<>(ChatView.class);
		chatView = chatViewLoader.getController();

		setupBinding();
		addClientSideAction();

		Parent root = chatViewLoader.getRoot();
		root.setOpacity(0.2);

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle(getClass().getName());

		// styling ..
		// scene.getStylesheets().add("/path/to/css");
		AquaFx.style();
		// AquaFx.styleStage(stage, StageStyle.UNIFIED);
		chatView.initialize();

		
		
//		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//		@Override
//		public void handle(WindowEvent event) {
//			ImageRegistry.instance().dispose();
//		}
//	});
		
		stage.show();

		clientDolphin.send(CMD_INIT, new OnFinishedHandlerAdapter() {
			@Override
			public void onFinished(
					List<ClientPresentationModel> presentationModels) {
				System.out.println("" + presentationModels.size() + "bekommen");
				// visualisieren, dass wir die initialen Daten haben.

				FadeTransition fadeTransition = new FadeTransition(Duration
						.millis(1000), root);
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

	private void setupBinding() {

		// bind name field and message box

		bind("text").of(chatView.nameField).to(ATTR_NAME)
				.of(postModel, withRelease);
		bind(ATTR_NAME).of(postModel).to("text").of(chatView.nameField);

		Node node = chatView.getMessageBox();
		bind("text").of(node).to(ATTR_MESSAGE).of(postModel, withRelease);
		bind(ATTR_MESSAGE).of(postModel).to("text").of(node);

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
							ViewLoader<MessageView> messageViewLoader = new ViewLoader<>(
									MessageView.class);
							MessageView messageView = messageViewLoader
									.getController();
							messageViewLoader.getRoot().setId(
									event.getPresentationModel().getId());

							// take message view and that child
							HBox messageViewContainer = (HBox) messageViewLoader
									.getRoot();
							messageViews.put(event.getPresentationModel()
									.getId(), messageViewContainer);
							messages.add(messageViewContainer);

							// bind
							final PresentationModel presentationModel = event
									.getPresentationModel();

							bind(ATTR_NAME).of(presentationModel).to("text")
									.of(messageView.nameLabel);
							bind("text").of(messageView.nameLabel)
									.to(ATTR_NAME)
									.of(presentationModel, withRelease);

							bind(ATTR_MESSAGE)
									.of(presentationModel)
									.to("text")
									.of(messageView.messageBox.getChildren()
											.get(0));
							bind("text")
									.of(messageView.messageBox.getChildren()
											.get(0)).to(ATTR_MESSAGE)
									.of(presentationModel, withRelease);

							bind(ATTR_DATE).of(presentationModel).to("text")
									.of(messageView.dateLabel);
							bind("text").of(messageView.dateLabel)
									.to(ATTR_DATE)
									.of(presentationModel, withRelease);

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
//		 pm = clientDolphin.getAt("meineid")
//		 clientDolphin.apply(pm).to(postModel);
//		 release();

	}

	private void addClientSideAction() {
		chatView.newButton.setOnAction((ActionEvent event) -> {
			clientDolphin.send(CMD_POST);
		});
	}

}
