/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.fxml_controller;

import at.htlstp.projekt.p04.db.DAO;
import at.htlstp.projekt.p04.graphic_tools.CustomStage;
import at.htlstp.projekt.p04.graphic_tools.Utilities;
import at.htlstp.projekt.p04.model.Gegenstand;
import at.htlstp.projekt.p04.model.Gruppe;
import at.htlstp.projekt.p04.model.Klasse;
import at.htlstp.projekt.p04.model.Lehrer;
import at.htlstp.projekt.p04.model.PraPruefung;
import at.htlstp.projekt.p04.model.Schueler;
import at.htlstp.projekt.p04.pruefungsverwaltungssoftware.Verwaltungssoftware;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dru
 */
public class PSMenuController implements Initializable {
    
    @FXML
    private Button btn_SchuelerZuweisen;
    @FXML
    private Button btn_AngabenInternet;
    
    @FXML
    private Label lbl_Pruefer;
    @FXML
    private Button btn_NeuePruefung;
    @FXML
    private Button btn_VerzKopieren;
    @FXML
    private Button btn_PruefungStarten;
    @FXML
    private Button btn_PruefungBeenden;
    @FXML
    private Button btn_PruefungLoeschen;
    @FXML
    private Button btn_PruefungBearbeiten;
    @FXML
    private TableView<PraPruefung> tbl_pruefungen;
    @FXML
    private TableColumn<PraPruefung, String> tcol_name;
    @FXML
    private TableColumn<PraPruefung, String> tcol_date;
    @FXML
    private TableColumn<PraPruefung, Verwaltungssoftware.Unterrichtseinheit> tcol_beginn;
    @FXML
    private TableColumn<PraPruefung, Klasse> tcol_klasse;
    @FXML
    private TableColumn<PraPruefung, Gegenstand> tcol_gegenstand;
    @FXML
    private TableColumn<PraPruefung, Verwaltungssoftware.PR_status> tcol_status;

    //Können sich nicht verändern 
    private final Map<Klasse, List<Schueler>> schuelerInKlassenByLehrer = new HashMap<>();
    private final Map<Gruppe, List<Schueler>> schuelerInGruppenByLehrer = new HashMap<>();
    private Lehrer lehrer;
    //----------------------

    private final ObservableList<PraPruefung> pruefungen = FXCollections.observableArrayList();
    
    public TableView<PraPruefung> getTbl_pruefungen() {
        return tbl_pruefungen;
    }
    
    public Map<Klasse, List<Schueler>> getSchuelerInKlassenByLehrer() {
        return schuelerInKlassenByLehrer;
    }
    
    public Map<Gruppe, List<Schueler>> getSchuelerInGruppenByLehrer() {
        return schuelerInGruppenByLehrer;
    }
    
    public Lehrer getLehrer() {
        return lehrer;
    }
    
    public void setLehrer(Lehrer lr) {
        this.lehrer = lr;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (lehrer != null) {

            //Alle Klassen und Schüler im Vorhinein laden
            DAO.getDaoInstance().getKlassenByLehrer(lehrer).forEach((kl) -> {
                schuelerInKlassenByLehrer.put(kl, DAO.getDaoInstance().getSchuelerByKlasse(kl));
            });

            //Alle Gruppen und dazugehörige Schüler laden
            DAO.getDaoInstance().getGruppenByLehrer(lehrer).forEach((gl) -> {
                schuelerInGruppenByLehrer.put(gl, DAO.getDaoInstance().getSchuelerByGruppe(gl));
            });

            //Scene zusammenbauen 
            lbl_Pruefer.setText("Prüfer/in: " + lehrer.getTitel().getTitBez() + " " + lehrer.getLehrerVorname() + " " + lehrer.getLehrerZuname());
            tcol_name.setCellValueFactory(pr -> new SimpleStringProperty(pr.getValue().getName()));
            tcol_beginn.setCellValueFactory(pr -> new SimpleObjectProperty<>(pr.getValue().getUnterrichtsstunde()));
            tcol_date.setCellValueFactory(pr -> new SimpleObjectProperty<>(Verwaltungssoftware.parseDateToString(Verwaltungssoftware.DTF, pr.getValue().getDatum())));
            tcol_gegenstand.setCellValueFactory(pr -> new SimpleObjectProperty<>(pr.getValue().getGegenstand()));
            tcol_klasse.setCellValueFactory(pr -> new SimpleObjectProperty<>(pr.getValue().getKlasse()));
            tcol_status.setCellValueFactory(pr -> new SimpleObjectProperty<>(pr.getValue().getStatus()));
            tbl_pruefungen.setId("pr_table");

            //Alle praktischen Prüfung aus der Datenbank laden und anzeigen 
            pruefungen.addAll(DAO.getDaoInstance().getPreaktischePruefungenByLehrer(lehrer));
            tbl_pruefungen.setItems(pruefungen);
            
            Verwaltungssoftware.installSchooltoolsStyleSheet(tbl_pruefungen.getScene());
        }
    }
    
    @FXML
    private void onActionNeuePruefung(ActionEvent event) {
        Stage dialogNeuePruefung = new Stage();
        try {
            CustomStage<PSPruefungController> customstage
                    = new CustomStage<>(dialogNeuePruefung,
                            "Neue Prüfung anlegen",
                            Verwaltungssoftware.schooltoolsLogo(),
                            this.getClass().getResource("/fxml/PSPruefung.fxml"),
                            false);
            customstage.getStage().show();
            //Reihenfolge der Aufrufe ist wichtig 
            customstage.getController().setMenuController(this);
            customstage.getController().initialize(null, null);
            customstage.getController().initializeNewPruefung();
        } catch (Exception ex) {
            Utilities.showMessageForExceptions(ex, Verwaltungssoftware.schooltoolsLogo(), true);
        }
    }
    
    @FXML
    private void onActionSchuelerZuweisung(ActionEvent event) {
        if (!checkIfSelected("Meldung", "Um Schüler einer Prüfung hinzuzufügen, muss eine Zeile in der Tabelle selektiert werden!")) {
            return;
        }
        
        Stage dialogSchuelerZuweisung = new Stage();
        try {
            CustomStage<PSSchuelerZuweisungController> customstage
                    = new CustomStage<>(dialogSchuelerZuweisung,
                            "Schüler zuweisen",
                            Verwaltungssoftware.schooltoolsLogo(),
                            this.getClass().getResource("/fxml/PSSchuelerZuweisung.fxml"),
                            false);
            customstage.getStage().show();
            customstage.getController().setMenucontroller(this);
            customstage.getController().initialize(null, null);
        } catch (Exception ex) {
            Utilities.showMessageForExceptions(ex, Verwaltungssoftware.schooltoolsLogo(), true);
        }
    }
    
    @FXML
    private void onActionAngabenInternet(ActionEvent event) {
        if (!checkIfSelected("Meldung", "Sie müssen eine Zeile in der Tabelle  selektieren, um die Optionen zu konfigurieren")) {
            return;
        }
        Stage dialogAngabenInternet = new Stage();
        try {
            CustomStage<PSInternetAngabenController> customstage
                    = new CustomStage<>(dialogAngabenInternet,
                            "Angaben und Internetoptionen",
                            Verwaltungssoftware.schooltoolsLogo(),
                            this.getClass().getResource("/fxml/PSInternetAngaben.fxml"),
                            false);
            customstage.getStage().show();
            customstage.getController().setMenucontroller(this);
            customstage.getController().initialize(null, null);
        } catch (Exception ex) {
            Utilities.showMessageForExceptions(ex, Verwaltungssoftware.schooltoolsLogo(), true);
        }
    }
    
    @FXML
    private void onActionVerzeichnisseKopieren(ActionEvent event) {
    }
    
    @FXML
    private void onActionPruefungStarten(ActionEvent event) {
    }
    
    @FXML
    private void onActionPruefungBeenden(ActionEvent event) {
    }
    
    @FXML
    private void onActionPruefungLoeschen(ActionEvent event) {
        
        PraPruefung aktPruefung = tbl_pruefungen.getSelectionModel().getSelectedItem();
        if (!checkIfSelected("Meldung", "Sie müssen eine Zeile in der Tabelle  selektieren, um eine Prüfung zu löschen")) {
            return;
        }
        
        ButtonType answer = Utilities.showYesNoDialog("Soll die Prüfung und die dazugehörigen Daten  wirklich endgültig gelöscht werden?",
                "Meldung",
                Alert.AlertType.INFORMATION,
                Verwaltungssoftware.schooltoolsLogo(),
                Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH);
        if (answer.equals(ButtonType.YES)) {
            DAO.getDaoInstance().deletePraktischePruefung(aktPruefung);
            pruefungen.remove(aktPruefung);
            tbl_pruefungen.refresh();
        }
    }
    
    @FXML
    private void onActionPruefungBearbeiten(ActionEvent event) {
        if (!checkIfSelected("Meldung", "Um eine Prüfung zu bearbeiten, müssen Sie eine Zeile in der Tabelle selektieren!")) {
            return;
        }
        Stage dialogPruefung = new Stage();
        try {
            CustomStage<PSPruefungController> customstage
                    = new CustomStage<>(dialogPruefung,
                            "Prüfung bearbeiten",
                            Verwaltungssoftware.schooltoolsLogo(),
                            this.getClass().getResource("/fxml/PSPruefung.fxml"),
                            false);
            customstage.getStage().show();
            customstage.getController().setMenuController(this);
            customstage.getController().initialize(null, null);
            customstage.getController().initializePruefung(tbl_pruefungen.getSelectionModel().getSelectedItem());
            
        } catch (Exception ex) {
            Utilities.showMessageForExceptions(ex, Verwaltungssoftware.schooltoolsLogo(), true);
        }
        
    }
    
    public boolean checkIfSelected(String messageTitle, String text) {
        PraPruefung aktPruefung = tbl_pruefungen.getSelectionModel().getSelectedItem();
        if (aktPruefung == null) {
            Utilities.showMessageWithFixedWidth(messageTitle,
                    "Keine Prüfung selektiert!",
                    text,
                    Alert.AlertType.INFORMATION,
                    Verwaltungssoftware.schooltoolsLogo(), Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                    false);
            
        }
        return aktPruefung != null;
    }
    
}
