package at.htlstp.projekt.p04.login;

import java.io.IOException;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login {

    //Variaben 
    private LoginFileController controller;
    private String validUser; 
    private LoginValidator loginData;
    private Stage loginStage;
    private final ReadOnlyBooleanWrapper close = new ReadOnlyBooleanWrapper(false);

    // Konstruktoren 
    public Login(LoginValidator data) {
        this.loginData = data;
        this.loginStage = new Stage();
    }

    public Login() {
        this.loginStage = new Stage();
    }

    /*
    Kann nur innerhalb des Paketes aufgrufen werden. Dient dazu den Zugriff 
    auf das BooleanWrapper-Objekt close von außen zu schützen, wobei der 
    Controller die Berechtigung benötigt
     */
    void close() {
        loginStage.setOpacity(0.0);
        close.set(true);
        this.loginStage.close();
    }

    //Methoden 
    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginFile.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            controller.setLoginInfo(loginData, this, loginStage);
            Scene scene = new Scene(root);
            loginStage.setResizable(false);
            loginStage.getIcons().add( new Image(this.getClass().getResource("/images/ST.png").toString()));
            loginStage.setScene(scene);
              //Styles hinzufügen 
            scene.getStylesheets().add(this.getClass().getResource("/styles/Styles.css").toString());
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    /*Die MEthode soll nur vom Controller aufgerufen werden, wenn ein User sich 
    erfolgreich eingeloggt hat d.h. default Zugriff
     */

    void setValidUser(String validUser) {
        this.validUser = validUser;
    }
    
    //Getter und Setter 
    public boolean isClose() {
        return close.get();
    }

    public ReadOnlyBooleanProperty closeProperty() {
        return close;
    }

    public String getValidUser() {
        return validUser;
    }

    public Stage getLoginStage() {
        return loginStage;
    }



}
