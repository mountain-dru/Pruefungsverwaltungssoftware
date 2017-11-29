/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.graphic_tools;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Klasse 4AHIF DV-Nummer: 20130041 Katalognummer: 01
 *
 * @author Bejinariu Alexandru
 */
public class CustomStage<T> {

    //Instanzvariablen 
    private final Stage myStage;
    private final T controller;

    public CustomStage(Stage myStage, String title, Image icon, URL url, boolean expanable) throws IOException {
        this.myStage = myStage;
        //Stage zusammenbauen
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        Parent pane = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(pane);
        myStage.setTitle(title);
        myStage.setResizable(expanable);
        myStage.getIcons().add(icon);
        myStage.setScene(scene);
    }

    public Stage getStage() {
        return myStage;
    }

    public T getController() {
        return controller;
    }

}
