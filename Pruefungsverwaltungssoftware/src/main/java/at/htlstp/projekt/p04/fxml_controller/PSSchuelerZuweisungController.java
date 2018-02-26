/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.fxml_controller;

import at.htlstp.projekt.p04.db.Hibernate_DAO;
import at.htlstp.projekt.p04.graphic_tools.Utilities;
import at.htlstp.projekt.p04.model.Gruppe;
import at.htlstp.projekt.p04.model.Klasse;
import at.htlstp.projekt.p04.model.PraPruefung;
import at.htlstp.projekt.p04.model.Schueler;
import at.htlstp.projekt.p04.pruefungsverwaltungssoftware.Verwaltungssoftware;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Dru
 */
public class PSSchuelerZuweisungController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private PSMenuController menucontroller;
    @FXML
    private ListView<Schueler> lst_teilnehmer;
    @FXML
    private ListView<Klasse> lst_klassen;
    @FXML
    private ListView<Schueler> lst_schueler;
    @FXML
    private ListView<Gruppe> lst_gruppen;

    private final ObservableList<Schueler> teilnehmer = FXCollections.observableArrayList();
    private final ObservableList<Schueler> schueler = FXCollections.observableArrayList();
    private final ObservableList<Klasse> klassen = FXCollections.observableArrayList();
    private final ObservableList<Gruppe> gruppen = FXCollections.observableArrayList();

    private PraPruefung aktPruefung;

    public void setMenucontroller(PSMenuController menucontroller) {
        this.menucontroller = menucontroller;
    }

  
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (menucontroller != null) {
            aktPruefung = menucontroller.getTbl_pruefungen().getSelectionModel().getSelectedItem();

            lst_gruppen.setItems(gruppen);
            lst_klassen.setItems(klassen);

            Comparator<Schueler> schuelerComparator = (s1, s2) -> {
                int diff = s1.getKlasse().compareTo(s2.getKlasse());
                if (diff == 0) {
                    return Integer.compare(s1.getSsdKatnr(), s2.getSsdKatnr());
                }
                return diff;
            };

            lst_schueler.setItems(schueler.sorted(schuelerComparator));
            lst_teilnehmer.setItems(teilnehmer.sorted(schuelerComparator));

            klassen.addAll(menucontroller.getSchuelerInKlassenByLehrer().keySet());
            lst_klassen.getSelectionModel().selectedItemProperty().addListener((e, oldV, newV) -> {
                if (newV != null) {
                    schueler.clear();
                    //Nicht verändern
                    List<Schueler> schuelerToAdd = new ArrayList<>(menucontroller.getSchuelerInKlassenByLehrer().get(newV));
                    schuelerToAdd.removeAll(teilnehmer);
                    schueler.addAll(schuelerToAdd);
                    lst_gruppen.getSelectionModel().select(null);
                }
            });

            lst_gruppen.getSelectionModel().selectedItemProperty().addListener((e, oldV, newV) -> {
                if (newV != null) {
                    schueler.clear();
                    List<Schueler> schuelerToAdd = new ArrayList<>(menucontroller.getSchuelerInGruppenByLehrer().get(newV));
                    schuelerToAdd.removeAll(teilnehmer);
                    schueler.addAll(schuelerToAdd);
                    lst_klassen.getSelectionModel().select(null);
                }
            });

            Stage myStage = (Stage) lst_schueler.getScene().getWindow();
            myStage.setOnCloseRequest(e -> closeStage(e));

            teilnehmer.addAll(Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(aktPruefung));
            gruppen.addAll(menucontroller.getSchuelerInGruppenByLehrer().keySet());
            lst_klassen.getSelectionModel().select(aktPruefung.getKlasse());

            lst_schueler.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                boolean disable = lst_klassen.getSelectionModel().getSelectedItem() == null
                        && lst_gruppen.getSelectionModel().getSelectedItem() == null;
                if (disable) {
                    schueler.clear();
                }
                return disable;
            }, lst_klassen.getSelectionModel().selectedItemProperty(),
                    lst_gruppen.getSelectionModel().selectedItemProperty()));

            //Styles hinzufügen 
            Verwaltungssoftware.installSchooltoolsStyleSheet(lst_klassen.getScene());
        }

    }

    @FXML
    private void onActionAdd(ActionEvent event) {
        Schueler toAdd = lst_schueler.getSelectionModel().getSelectedItem();
        if (toAdd == null) {
            Utilities.showMessageWithFixedWidth("Meldung",
                    "Kein Schüler selektiert!",
                    "Selektieren Sie einen Schüler in der mittleren  Liste, um ihn als Prüfungsteilnehmer einzutragen.",
                    Alert.AlertType.INFORMATION,
                    Verwaltungssoftware.schooltoolsLogo(), Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH, false);
            return;
        }
//        if (teilnehmer.contains(toAdd)) {
//            Utilities.showMessageWithFixedWidth("Meldung",
//                    "Schüler konnte nicht eingetragen werden!",
//                    "Der Schüler " + toAdd.getSsdVorname() + " " + toAdd.getSsdZuname() + " ist bereits als Prüfungsteilnehmer eingetragen.",
//                    Alert.AlertType.INFORMATION,
//                    Verwaltungssoftware.schooltoolsLogo(), Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH, false);
//            return;
//        }

        schueler.remove(toAdd);
        teilnehmer.add(toAdd);

    }

    @FXML
    private void onActionAddAll(ActionEvent event) {
        List<Schueler> toAdd = new ArrayList<>(schueler);
        schueler.clear();
        teilnehmer.addAll(toAdd);
    }

    @FXML
    private void onActionRemove(ActionEvent event) {
        Schueler toRemove = lst_teilnehmer.getSelectionModel().getSelectedItem();
        if (toRemove == null) {
            Utilities.showMessageWithFixedWidth("Meldung",
                    "Kein Schüler selektiert!",
                    "Selektieren Sie einen Schüler in der Teilnehmer-Liste, um ihn als Prüfungsteilnehmer auszutragen.",
                    Alert.AlertType.INFORMATION,
                    Verwaltungssoftware.schooltoolsLogo(), Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH, false);
            return;
        }
        teilnehmer.remove(toRemove);
        addSchuelerToSchuelerList(toRemove);

    }

    @FXML
    private void onActionSpeichern(ActionEvent event) {
        aktPruefung.setSchuelerSet(new HashSet<>(teilnehmer));
        Hibernate_DAO.getDaoInstance().updatePraktischePruefung(aktPruefung);
        Stage myStage = (Stage) lst_schueler.getScene().getWindow();
        myStage.setOnCloseRequest(null);
        myStage.fireEvent(new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST));   //Stage schließen 

        TableView<PraPruefung> tbl_pruefungen = menucontroller.getTbl_pruefungen(); 
        
        PraPruefung selected = tbl_pruefungen.getSelectionModel().getSelectedItem();
        tbl_pruefungen.getSelectionModel().select(null);
        tbl_pruefungen.getSelectionModel().select(selected);
        tbl_pruefungen.refresh();

    }

    @FXML
    private void onActionRemoveAll(ActionEvent event) {
        teilnehmer.forEach(sch -> addSchuelerToSchuelerList(sch));
        teilnehmer.clear();
    }

    public void closeStage(WindowEvent e) {
        List<Schueler> sortedSchueler = ((ArrayList<Schueler>) Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(aktPruefung));
        sortedSchueler.sort((s1, s2) -> Integer.compare(s1.getSsdId(), s2.getSsdId()));
        if (!sortedSchueler.equals(teilnehmer)) {
            //Änderungen
            e.consume();
            ButtonType answer = Utilities.showYesNoDialog("Wollen Sie die Änderungen verwerfen? Alle hinzugefügten/entfernten Teilnehmer werden nicht übernommen.",
                    "Meldung",
                    Alert.AlertType.INFORMATION,
                    Verwaltungssoftware.schooltoolsLogo(),
                    Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH);
            if (answer.equals(ButtonType.YES)) {
                Stage myStage = (Stage) lst_teilnehmer.getScene().getWindow();
                myStage.setOnCloseRequest(null);
                myStage.fireEvent(new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST));   //Stage schließen 
            }
        }
    }

    private void addSchuelerToSchuelerList(Schueler toRemove) {
        Gruppe grp = lst_gruppen.getSelectionModel().getSelectedItem();
        if (grp != null) {
            if (menucontroller.getSchuelerInGruppenByLehrer().get(grp).contains(toRemove)) {
                //Schüler ist in der selktierten Gruppe
                if (!schueler.contains(toRemove)) {
                    schueler.add(toRemove);
                }
                return;
            }
        }
        Klasse kls = lst_klassen.getSelectionModel().getSelectedItem();
        if (kls != null) {
            if (menucontroller.getSchuelerInKlassenByLehrer().get(kls).contains(toRemove)) {
                //Schüler ist in der selktierten Klasse 
                schueler.add(toRemove);
            }
            return;
        }
    }

    @FXML
    private void onActionAddDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            onActionAdd(null);
        }
    }

    @FXML
    private void onActionRemoveDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            onActionRemove(null);
        }
    }
}
