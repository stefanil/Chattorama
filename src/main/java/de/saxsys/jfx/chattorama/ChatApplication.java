package de.saxsys.jfx.chattorama;

import groovy.util.Eval;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.opendolphin.binding.Converter;
import org.opendolphin.core.ModelStoreEvent;
import org.opendolphin.core.ModelStoreListener;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;
import org.opendolphin.core.client.comm.OnFinishedHandlerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.opendolphin.binding.JFXBinder.bind;
import static de.saxsys.jfx.chattorama.ChatterConstants.*;

public class ChatApplication extends Application {


    static ClientDolphin clientDolphin;

    private PresentationModel postModel;

    private ChatView chatView;

    private ViewLoader<ChatView> chatViewLoader;


    public ChatApplication() {
        ClientAttribute nameAttribute = new ClientAttribute(ATTR_NAME, "");
        ClientAttribute postAttribute = new ClientAttribute(ATTR_MESSAGE, "");
        ClientAttribute dateAttribute = new ClientAttribute(ATTR_DATE, "");
        postModel = clientDolphin.presentationModel(PM_ID_INPUT, nameAttribute, postAttribute, dateAttribute);
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
//        scene.getStylesheets().add("/path/to/css");

        stage.show();


        clientDolphin.send(CMD_INIT, new OnFinishedHandlerAdapter() {
            @Override
            public void onFinished(List<ClientPresentationModel> presentationModels) {
                System.out.println(""+ presentationModels.size() + "bekommen");
                // visualisieren, dass wir die initialen Daten haben.

                FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000),root);
                fadeTransition.setFromValue(0.2);
                fadeTransition.setToValue(1);

                fadeTransition.play();

                longPoll();
            }
        });

    }

    private boolean channelBlocked = false;
    private void release() {
        if (!channelBlocked) return; // avoid too many unblocks
        channelBlocked = false;
        String url = "https://klondike.canoo.com/dolphin-grails/chatter/release";
        String result = Eval.x(url,"x.toURL().text").toString();
        System.out.println("release result = " + result);
    }

    private void longPoll() {
        channelBlocked = true;
        clientDolphin.send(CMD_POLL, new OnFinishedHandlerAdapter(){
            @Override
            public void onFinished(List<ClientPresentationModel> presentationModels) {
                longPoll();
            }
        });
    }

    private final Converter withRelease = value -> {
        release();
        return value;
    };

    private void setupBinding() {
        bind("text").of(chatView.nameField).to(ATTR_NAME).of(postModel, withRelease);
        bind(ATTR_NAME).of(postModel).to("text").of(chatView.nameField);

        bind("text").of(chatView.messageBox).to(ATTR_MESSAGE).of(postModel, withRelease);
        bind(ATTR_MESSAGE).of(postModel).to("text").of(chatView.messageBox);



        ObservableMap<String,HBox> messageViews = FXCollections.observableHashMap();

        ObservableList<HBox> messages = FXCollections.observableArrayList();
        chatView.liste.setItems(messages);


        clientDolphin.addModelStoreListener(TYPE_POST, new ModelStoreListener() {
            @Override
            public void modelStoreChanged(ModelStoreEvent event) {
                if (event.getType() == ModelStoreEvent.Type.ADDED) {
                    ViewLoader<MessageView> messageViewLoader = new ViewLoader<>(MessageView.class);

                    MessageView messageView = messageViewLoader.getController();
                    messageViewLoader.getRoot().setId(event.getPresentationModel().getId());


                    final PresentationModel presentationModel = event.getPresentationModel();


                    bind("text").of(messageView.nameLabel).to(ATTR_NAME).of(presentationModel, withRelease);
                    bind(ATTR_NAME).of(presentationModel).to("text").of(messageView.nameLabel);

                    bind("text").of(messageView.messageBox).to(ATTR_MESSAGE).of(presentationModel, withRelease);
                    bind(ATTR_MESSAGE).of(presentationModel).to("text").of(messageView.messageBox);

                    bind("text").of(messageView.dateLabel).to(ATTR_DATE).of(presentationModel, withRelease);
                    bind(ATTR_DATE).of(presentationModel).to("text").of(messageView.dateLabel);



                    HBox messageViewContainer = (HBox)messageViewLoader.getRoot();
                    messageViews.put(event.getPresentationModel().getId(),messageViewContainer);
                    messages.add(messageViewContainer);

                }
                if (event.getType() == ModelStoreEvent.Type.REMOVED) {
                    String id = event.getPresentationModel().getId();
                    if(messageViews.containsKey(id)){

                        // Erst hole die HBox der Message aus der Map
                        final HBox view = messageViews.get(id);
                        //... dann entferne sie aus der ListBox...
                        messages.remove(view);
                        //... und anschließend aus der Map
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

