package de.saxsys.jfx.chattorama;

import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_DATE;
import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_MESSAGE;
import static de.saxsys.jfx.chattorama.ChatterConstants.ATTR_NAME;
import static de.saxsys.jfx.chattorama.ChatterConstants.CMD_POLL;
import static de.saxsys.jfx.chattorama.ChatterConstants.CMD_POST;
import static de.saxsys.jfx.chattorama.ChatterConstants.TYPE_POST;
import static org.opendolphin.binding.JFXBinder.bind;
import groovy.json.StringEscapeUtils;
import groovy.util.Eval;

import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.opendolphin.binding.Converter;
import org.opendolphin.core.ModelStoreEvent;
import org.opendolphin.core.ModelStoreListener;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;
import org.opendolphin.core.client.comm.OnFinishedHandlerAdapter;

import com.aquafx_project.AquaFx;

import de.saxsys.jfx.chattorama.resource.ImageRegistry;

public class ChatApplication extends Application {

	static ClientDolphin clientDolphin;

	private PresentationModel postModel;

	private ChatView chatView;

	private ViewLoader<ChatView> chatViewLoader;

	public ChatApplication() {
		// ClientAttribute nameAttribute = new ClientAttribute(ATTR_NAME, "");
		// ClientAttribute postAttribute = new ClientAttribute(ATTR_MESSAGE,
		// "");
		// ClientAttribute dateAttribute = new ClientAttribute(ATTR_DATE, "");
		// postModel = clientDolphin.presentationModel(PM_ID_INPUT,
		// nameAttribute, postAttribute, dateAttribute);
	}

	@Override
	public void start(Stage stage) throws Exception {

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				ImageRegistry.instance().dispose();
			}
		});

		chatViewLoader = new ViewLoader<>(ChatView.class);
		chatView = chatViewLoader.getController();
		
		
		

		// setupBinding();
		// addClientSideAction();

		// FXCollections.observableArrayList(new ArrayList<Node>())

		Text cry = new Text(Emoticons.CRY.toString());
		cry.setFont(Font.loadFont(
				getClass().getResourceAsStream(
						"/de/saxsys/jfx/chattorama/fonts/android-emoji.ttf"), 36));
		chatView.messageBox.getChildren().add(cry);
		
		Text laugh = new Text(Emoticons.LAUGH.toString());
		laugh.setFont(Font.loadFont(
				getClass().getResourceAsStream(
						"/de/saxsys/jfx/chattorama/fonts/android-emoji.ttf"), 36));
		chatView.messageBox.getChildren().add(laugh);
		
		Text smile = new Text(Emoticons.SMILE.toString());
		smile.setFont(Font.loadFont(
				getClass().getResourceAsStream(
						"/de/saxsys/jfx/chattorama/fonts/android-emoji.ttf"), 36));
		chatView.messageBox.getChildren().add(smile);
		
		
		
		
		
		Text text = (Text) chatView.messageBox.getChildren().get(0);
		// text.setFont(Font.font("Aegyptus", FontWeight.NORMAL,
		// FontPosture.REGULAR, 36));
		text.setFont(Font.loadFont(
				getClass().getResourceAsStream(
						"/de/saxsys/jfx/chattorama/fonts/Aegyptus313.ttf"), 36));

		String string = new Character((char) Integer.valueOf("f625", 16)
				.intValue()).toString();

		// new Character("😃".toCharArray()[0], 16);
		char[] charArray = "😃".toCharArray();
		// int digit = Character.digit(charArray[0], 16);

		
		
		// string = Integer.toHexString(Integer.parseInt("f625",16));
		text.setText("Hi "
				// + new String("😃".getBytes("UTF-8"), "UTF-8") + /*
				// "😃".getBytes("UTF8") + string
				// + */ "\uF603"
				// + "\uf3bdf"
				// + StringEscapeUtils.unescapeJava("\\u30FD")
//				+ new String("F3000".getBytes("UTF-16"), "UTF-16")
//				+ Integer.toHexString(Integer.parseInt("F3000",16))
				+ convertHex2CharString("0xF3000")
				+ convertHex2CharString("0x130EF")
				+ StringEscapeUtils.unescapeJava("\u0041")/* .toCharArray()[0] */
				+ StringEscapeUtils.unescapeJava("\u20ac") + "!");
		// bind("text").of(text).to(ATTR_MESSAGE).of(postModel, withRelease);
		// bind(ATTR_MESSAGE).of(postModel).to("text").of(text);

		Parent root = chatViewLoader.getRoot();
		root.setOpacity(0.2);

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle(getClass().getName());
		// scene.getStylesheets().add("/path/to/css");

		// apply aquafx styling
		AquaFx.style();

		// just use this if offering a menu bar (because of the black line)
		// AquaFx.styleStage(stage, StageStyle.UNIFIED);
		chatView.style();

		stage.show();

		// clientDolphin.send(CMD_INIT, new OnFinishedHandlerAdapter() {
		// @Override
		// public void onFinished(List<ClientPresentationModel>
		// presentationModels) {
		// System.out.println(""+ presentationModels.size() + "bekommen");
		// // visualisieren, dass wir die initialen Daten haben.
		//
		// FadeTransition fadeTransition = new
		// FadeTransition(Duration.millis(1000),root);
		// fadeTransition.setFromValue(0.2);
		// fadeTransition.setToValue(1);
		//
		// fadeTransition.play();
		//
		// longPoll();
		// }
		// });

	}

	// TODO remove
	private String convertHex2CharString(String hexString) {
		return new String(Character.toChars(Integer.parseInt(hexString.substring(2), 16)));
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

		// bind textfield and message box

		bind("text").of(chatView.nameField).to(ATTR_NAME)
				.of(postModel, withRelease);
		bind(ATTR_NAME).of(postModel).to("text").of(chatView.nameField);

		bind("text").of(chatView.messageBox).to(ATTR_MESSAGE)
				.of(postModel, withRelease);
		bind(ATTR_MESSAGE).of(postModel).to("text").of(chatView.messageBox);

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
