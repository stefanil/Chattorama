package de.saxsys.jfx.chattorama;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientModelStore;
import org.opendolphin.core.client.comm.BlindCommandBatcher;
import org.opendolphin.core.client.comm.HttpClientConnector;
import org.opendolphin.core.client.comm.JavaFXUiThreadHandler;
import org.opendolphin.core.comm.JsonCodec;

public class Starter{

    public static void main(String...args){
        ClientDolphin clientDolphin = new ClientDolphin();
        clientDolphin.setClientModelStore(new ClientModelStore(clientDolphin));
        BlindCommandBatcher batcher = new BlindCommandBatcher();
        batcher.setMergeValueChanges(true);
        HttpClientConnector connector = new HttpClientConnector(clientDolphin, batcher, "https://klondike.canoo.com/dolphin-grails/dolphin/");
        connector.setCodec(new JsonCodec());
        connector.setUiThreadHandler(new JavaFXUiThreadHandler());
        clientDolphin.setClientConnector(connector);

        ChatApplication.clientDolphin = clientDolphin;
        Application.launch(ChatApplication.class);
    }
}
