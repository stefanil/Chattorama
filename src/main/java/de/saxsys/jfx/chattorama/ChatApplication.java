package de.saxsys.jfx.chattorama;

import de.saxsys.jfx.mvvm.di.FXMLLoaderWrapper;
import de.saxsys.jfx.mvvm.viewloader.*;
import groovy.lang.Closure;
import groovy.util.Eval;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransitionBuilder;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.PaneBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.opendolphin.binding.Converter;
import org.opendolphin.binding.JFXBinder;
import org.opendolphin.core.ModelStoreEvent;
import org.opendolphin.core.ModelStoreListener;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;
import org.opendolphin.core.client.comm.OnFinishedHandlerAdapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.List;

import static org.opendolphin.binding.JFXBinder.bind;
import static de.saxsys.jfx.chattorama.ChatterConstants.*;

public class ChatApplication extends Application {


    static ClientDolphin clientDolphin;

    private PresentationModel postModel;


    public ChatApplication() {
        ClientAttribute nameAttribute = new ClientAttribute(ATTR_NAME, "");
        ClientAttribute postAttribute = new ClientAttribute(ATTR_MESSAGE, "");
        ClientAttribute dateAttribute = new ClientAttribute(ATTR_DATE, "");
        postModel = clientDolphin.presentationModel(PM_ID_INPUT, nameAttribute, postAttribute, dateAttribute);
    }

    @Override
    public void start(Stage stage) throws Exception {

        String pathToFXML = "/"
                + ChatView.class.getPackage().getName().replaceAll("\\.","/") + "/"
                + ChatView.class.getSimpleName() + ".fxml";

        URL location = getClass().getResource(pathToFXML);



        FXMLLoader fxmlLoader = new FXMLLoader(location);

        fxmlLoader.load();

        ChatView chatView = fxmlLoader.getController();

        setupBinding(chatView);
        addClientSideAction();


        Scene scene = new Scene(fxmlLoader.getRoot());
        stage.setScene(scene);
        stage.setTitle(getClass().getName());
//        scene.getStylesheets().add("/path/to/css");

        stage.show();


        clientDolphin.send(CMD_INIT, new OnFinishedHandlerAdapter() {
            @Override
            public void onFinished(List<ClientPresentationModel> presentationModels) {
                System.out.println(""+ presentationModels.size() + "bekommen");
                // visualisieren, dass wir die initialen Daten haben.

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

    private void setupBinding(ChatView chatView) {
        bind("text").of(chatView.nameField).to(ATTR_NAME).of(postModel, withRelease);
        bind(ATTR_NAME).of(postModel).to("text").of(chatView.nameField);

        bind("text").of(chatView.messageBox).to(ATTR_MESSAGE).of(postModel, withRelease);
        bind(ATTR_MESSAGE).of(postModel).to("text").of(chatView.messageBox);


        clientDolphin.addModelStoreListener(TYPE_POST, new ModelStoreListener() {
            @Override
            public void modelStoreChanged(ModelStoreEvent event) {
                if (event.getType() == ModelStoreEvent.Type.ADDED) {
                    System.out.println(" wir haben den pm bekommen:  " + event.getPresentationModel().getId());
                }
                if (event.getType() == ModelStoreEvent.Type.REMOVED) {
                    System.out.println(" wir haben den pm geloescht:  " + event.getPresentationModel().getId());
                }
            }
        });

        // on select : pm per id:
        // pm = clientDolphin.getAt("meineid")
        // clientDolphin.apply(pm).to(postModel);
        // release();



    }

    private void addClientSideAction() {
//        newButton.setOnAction((ActionEvent event) -> {
//            clientDolphin.send(CMD_POST);
//        });
    }

}

