package de.saxsys.jfx.chattorama;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class ViewLoader<T>{

    private FXMLLoader fxmlLoader;

    public ViewLoader(Class<T> viewType, T view){
        URL location = getUrl(viewType);
        fxmlLoader  = new FXMLLoader(location);
        fxmlLoader.setRoot(view);
        fxmlLoader.setController(view);
        load();
    }

    public ViewLoader(Class<T> viewType) {
        URL location = getUrl(viewType);

        fxmlLoader  = new FXMLLoader(location);

        load();
    }

    private void load() {
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private URL getUrl(Class<T> viewType) {
        String pathToFXML = "/"
                + viewType.getPackage().getName().replaceAll("\\.","/") + "/"
                + viewType.getSimpleName() + ".fxml";

        return getClass().getResource(pathToFXML);
    }

    public T getController(){
        return fxmlLoader.getController();
    }

    public Parent getRoot(){
        return fxmlLoader.getRoot();
    }

}
