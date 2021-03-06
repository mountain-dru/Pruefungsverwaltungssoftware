/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.fxml_controller;

import at.htlstp.projekt.p04.db.Hibernate_DAO;
import at.htlstp.projekt.p04.graphic_tools.CustomStage;
import at.htlstp.projekt.p04.graphic_tools.Utilities;
import at.htlstp.projekt.p04.model.Gegenstand;
import at.htlstp.projekt.p04.model.Gruppe;
import at.htlstp.projekt.p04.model.Klasse;
import at.htlstp.projekt.p04.model.Lehrer;
import at.htlstp.projekt.p04.model.PraPruefung;
import at.htlstp.projekt.p04.model.Schueler;
import at.htlstp.projekt.p04.pruefungsverwaltungssoftware.Verwaltungssoftware;
import at.htlstp.projekt.p04.services.UserDelTask;
import at.htlstp.projekt.p04.services.UserTask;
import client.ConnectionParameter;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

/**
 * FXML Controller class
 *
 * @author Dru
 */
public class PSMenuController implements Initializable {

    //12 Zeichen Benutzername, 30 Zeichen Mindestzeichen beim Namen
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
    @FXML
    private Label lbl_User;
    @FXML
    private ProgressIndicator pgi_User;

    @FXML
    private Button btn_SchuelerDaten;

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
//           List<Schueler> schs = Hibernate_DAO.getDaoInstance().getSchuelerByKlasse(new Klasse("5AHIF"));
//           for(Schueler sch: schs){
//               System.out.println("5AHIF;" + sch.getSsdKatnr() + ";" + sch.getSsdZuname() + ";" + sch.getSsdVorname());
//           }

            //Alle Klassen und Schüler im Vorhinein laden
            Hibernate_DAO.getDaoInstance().getKlassenByLehrer(lehrer).forEach((kl) -> {
                schuelerInKlassenByLehrer.put(kl, Hibernate_DAO.getDaoInstance().getSchuelerByKlasse(kl));
            });

            //Alle Gruppen und dazugehörige Schüler laden
            Hibernate_DAO.getDaoInstance().getGruppenByLehrer(lehrer).forEach((gl) -> {
                schuelerInGruppenByLehrer.put(gl, Hibernate_DAO.getDaoInstance().getSchuelerByGruppe(gl));
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
            pruefungen.addAll(Hibernate_DAO.getDaoInstance().getPreaktischePruefungenByLehrer(lehrer));
            tbl_pruefungen.setItems(pruefungen);
            pruefungen.forEach(pruefung -> pruefung.setSchuelerSet(new HashSet<>(Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(pruefung))));

            Verwaltungssoftware.installSchooltoolsStyleSheet(tbl_pruefungen.getScene());
            tbl_pruefungen.getSelectionModel().selectedItemProperty().addListener((ob, oVal, nVal) -> {
                if (nVal != null) {
                    lbl_User.setVisible(true);
                    pgi_User.setVisible(true);
                    switch (nVal.getStatus()) {
                        case ANGELEGT:
                            lbl_User.setText("Usererstellung: ");
                            pgi_User.setDisable(false);
                            pgi_User.setProgress(-1);
                            break;
                        case GESTARTET:
                            lbl_User.setText("Usererstellung: ");
                            pgi_User.setDisable(false);
                            pgi_User.setProgress(1);
                            break;
                        case BEENDET:
                            lbl_User.setText("Userentfernung: ");
                            pgi_User.setProgress(1);
                            pgi_User.setDisable(true);
                            break;
                        default:
                            break;
                    }
                } else {
                    lbl_User.setVisible(false);
                    pgi_User.setVisible(false);
                }

            });
            if (!tbl_pruefungen.getItems().isEmpty()) {
                tbl_pruefungen.getSelectionModel().selectFirst();
                tbl_pruefungen.getSelectionModel().select(null);
            }

            btn_NeuePruefung.disableProperty().bind(tbl_pruefungen.disableProperty());

            BooleanBinding anglegtUndSelektiert = Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                return !(sel != null
                        && !tbl_pruefungen.isDisabled()
                        && sel.getStatus().equals(Verwaltungssoftware.PR_status.ANGELEGT));
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty(), tbl_pruefungen.disableProperty());

            btn_PruefungBearbeiten.disableProperty().bind(anglegtUndSelektiert);
            btn_SchuelerZuweisen.disableProperty().bind(anglegtUndSelektiert);
            btn_AngabenInternet.disableProperty().bind(anglegtUndSelektiert);

            btn_SchuelerDaten.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                return !(sel != null
                        && !tbl_pruefungen.isDisabled()
                        && !sel.getSchuelerSet().isEmpty()
                        && !sel.getStatus().equals(Verwaltungssoftware.PR_status.BEENDET));
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty(), tbl_pruefungen.disabledProperty()));

            btn_SchuelerDaten.textProperty().bind(Bindings.createStringBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                if (sel == null) {
                    return "Schülerdaten";
                } else if (sel.getStatus().equals(Verwaltungssoftware.PR_status.ANGELEGT)) {
                    return "Schülerdaten erzeugen";
                } else if (sel.getStatus().equals(Verwaltungssoftware.PR_status.GESTARTET)) {
                    return "Schülerdaten anzeigen";
                } else {
                    return "Schülerdaten";
                }

            }, tbl_pruefungen.getSelectionModel().selectedItemProperty()));

            btn_PruefungStarten.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                if (sel == null) {
                    return true;
                }
                Path pathToSchuelerList = Verwaltungssoftware.getDirectoryFromPruefung(sel).resolve("Skripts/activeusers.txt");     //Schuelerdaten müssen bereits angelegt worden sein.
                return !(pathToSchuelerList.toFile().exists()
                        && !tbl_pruefungen.isDisabled()
                        && !sel.getSchuelerSet().isEmpty()
                        && sel.getStatus().equals(Verwaltungssoftware.PR_status.ANGELEGT));
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty(), tbl_pruefungen.disabledProperty()));

            btn_VerzKopieren.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                return !(sel != null
                        && sel.getStatus().equals(Verwaltungssoftware.PR_status.GESTARTET)
                        && !tbl_pruefungen.isDisabled());
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty(), tbl_pruefungen.disabledProperty()));

            btn_PruefungLoeschen.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                return !(sel != null
                        && !sel.getStatus().equals(Verwaltungssoftware.PR_status.GESTARTET)
                        && !tbl_pruefungen.isDisabled());
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty(), tbl_pruefungen.disabledProperty()));

            btn_PruefungBeenden.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                return !(sel != null
                        && sel.getStatus().equals(Verwaltungssoftware.PR_status.GESTARTET)
                        && !tbl_pruefungen.isDisabled());
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty(), tbl_pruefungen.disabledProperty()));

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
        PraPruefung aktPruefung = tbl_pruefungen.getSelectionModel().getSelectedItem();
        
        LocalDateTime aktTime = LocalDateTime.now();
        String name = "Abgaben_" + aktTime.format(DateTimeFormatter.ofPattern("hh-mm-ss"));
        Path abgaben = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).resolve("Abgaben");
        Path newAbgaben = abgaben.resolve(name);
        Path schuelerPath = Verwaltungssoftware.SCHUELER_PATH;
        try {
            Files.createDirectory(newAbgaben);
            Set<Schueler> schuelerSet = new TreeSet<>(Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(aktPruefung));
            for (Schueler sch : schuelerSet) {
                String username = sch.userString();
                Path userDirectory = schuelerPath.resolve(username);
            
                FileUtils.copyDirectory(userDirectory.toFile(), newAbgaben.resolve(sch.getKlasse().getKlaBez()
                        + "_" + String.format("%02d", sch.getSsdKatnr())
                        + "_" + sch.getSsdZuname()
                        + "_" + sch.getSsdVorname()).toFile(), file -> !(file.isDirectory() && "Angaben".equals(file.getName()))  );

            }
        } catch (IOException io) {
            Utilities.showMessageForExceptions(io, Verwaltungssoftware.schooltoolsLogo(), false);
        }

    }

    @FXML
    private void onActionPruefungStarten(ActionEvent event) {
        PraPruefung aktPruefung = tbl_pruefungen.getSelectionModel().getSelectedItem();
        if (!checkIfSelected("Meldung", "Sie müssen eine Zeile in der Tabelle  selektieren, um eine Prüfung zu starten")) {
            return;
        }
        tbl_pruefungen.disableProperty().set(true);

        UserTask task = new UserTask(Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(aktPruefung), aktPruefung);
        Thread t = new Thread(task);
        task.valueProperty().addListener((o, oldV, newV) -> {
            if (newV != null) {
                pgi_User.setProgress(newV / 100.0);
                System.out.println("UPDATE");
            }

        });
        lbl_User.setText("Usererstellung: ");

        task.progressProperty().addListener((on, oldV, newV) -> {
            if ((double) newV == 1L) {
                if (aktPruefung.getInternet()) {
                    Task<Void> internet = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            this.updateProgress(0.5, 1.0);
                            Path userFile = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).resolve(Paths.get("Skripts/activeusers.txt"));
                            System.out.println(userFile.toFile().exists());
                            try (Socket sock = new Socket(InetAddress.getByName("10.138.138.138"), 42000);
                                    ObjectInputStream socketIn = new ObjectInputStream(sock.getInputStream());
                                    ObjectOutputStream socketOut = new ObjectOutputStream(sock.getOutputStream());
                                    BufferedReader bf = Files.newBufferedReader(userFile)) {

                                socketOut.writeObject(ConnectionParameter.UNLOCK_INTERNET);
                                Map<String, String> userMap = new HashMap<>();
                                String line = null;
                                while ((line = bf.readLine()) != null) {
                                    userMap.put(line.split(";")[0], line.split(";")[1]);
                                }
                                socketOut.writeObject(userMap);
                                socketOut.writeObject(getWebsitesFromPruefung(aktPruefung));
                                socketOut.writeObject(aktPruefung.getName());
                                ConnectionParameter response = (ConnectionParameter) socketIn.readObject();

                                if (response.equals(ConnectionParameter.SUCCEDDED)) {
                                    this.updateProgress(1.0, 1.0);
                                } else {
                                    this.updateProgress(0.9, 1.0);
                                }
                            } catch (IOException io) {
                                Utilities.showMessageForExceptions(io, Verwaltungssoftware.schooltoolsLogo(), false);
                            }
                            this.updateProgress(1.0, 1.0);
                            return null;
                        }
                    };
                    Thread proxy = new Thread(internet);

                    internet.progressProperty()
                            .addListener((o, oldVa, newVa) -> {
                                double value = (double) newVa;
                                if (value == 1.0) {
                                    Utilities.showMessageWithFixedWidth("Information",
                                            "Initialisierung abgeschlossen",
                                            "Interzugriff wurde vollständig eingerichtet",
                                            Alert.AlertType.INFORMATION, Verwaltungssoftware.schooltoolsLogo(),
                                            Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                                            false);
                                    start(aktPruefung);

                                } else if (value == 0.5) {
                                    Utilities.showMessageWithFixedWidth("Information",
                                            "Initialisierung des Internets",
                                            "Der Vorgang wird etwa 30 Sekunden beanspruchen",
                                            Alert.AlertType.INFORMATION, Verwaltungssoftware.schooltoolsLogo(),
                                            Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                                            false);
                                } else if (value == 0.9) {
                                    Utilities.showMessageWithFixedWidth("Fehler",
                                            "Initialisierung abgebrochen",
                                            "Der Internetzugriff konnte nicht eingerichtet werden",
                                            Alert.AlertType.ERROR, Verwaltungssoftware.schooltoolsLogo(),
                                            Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                                            false);
                                }
                            }
                            );
                    proxy.start();

                } else {
                    start(aktPruefung);
                }
            }
        });
        t.start();

    }

    private void start(PraPruefung aktPruefung) {
        aktPruefung.setStatus(Verwaltungssoftware.PR_status.GESTARTET);
        tbl_pruefungen.disableProperty().set(false);
        tbl_pruefungen.getSelectionModel().select(null);
        tbl_pruefungen.getSelectionModel().select(aktPruefung);
        tbl_pruefungen.refresh();
        Hibernate_DAO.getDaoInstance().updatePraktischePruefung(aktPruefung);
    }

    private void end(PraPruefung aktPruefung) {
        aktPruefung.setStatus(Verwaltungssoftware.PR_status.BEENDET);
        tbl_pruefungen.disableProperty().set(false);
        tbl_pruefungen.getSelectionModel().select(null);
        tbl_pruefungen.getSelectionModel().select(aktPruefung);
        tbl_pruefungen.refresh();
        Hibernate_DAO.getDaoInstance().updatePraktischePruefung(aktPruefung);
    }

    @FXML
    private void onActionPruefungBeenden(ActionEvent event) {
        onActionVerzeichnisseKopieren(null);
        PraPruefung aktPruefung = tbl_pruefungen.getSelectionModel().getSelectedItem();
        if (!checkIfSelected("Meldung", "Sie müssen eine Zeile in der Tabelle  selektieren, um eine Prüfung zu beenden")) {
            return;
        }
        tbl_pruefungen.disableProperty().set(true);

        UserDelTask task = new UserDelTask(Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(aktPruefung), aktPruefung);
        Thread t = new Thread(task);
        task.valueProperty().addListener((o, oldV, newV) -> {
            if (newV != null) {
                pgi_User.setProgress(newV / 100.0);
                System.out.println("DELETE");
            }

        });
        lbl_User.setText("Userentfernung: ");
        task.progressProperty().addListener((on, oldV, newV) -> {
            if ((double) newV == 1L) {
                if (aktPruefung.getInternet()) {
                    Task<Void> internet = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {

                            Path userFile = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).resolve(Paths.get("Skripts/activeusers.txt"));
                            try (Socket sock = new Socket(InetAddress.getByName("10.138.138.138"), 42000);
                                    ObjectInputStream socketIn = new ObjectInputStream(sock.getInputStream());
                                    ObjectOutputStream socketOut = new ObjectOutputStream(sock.getOutputStream());
                                    BufferedReader bf = Files.newBufferedReader(userFile)) {

                                socketOut.writeObject(ConnectionParameter.END_EXAM);
                                List<String> schueler = new ArrayList<>();
                                String line = null;
                                while ((line = bf.readLine()) != null) {
                                    schueler.add(line.split(";")[0]);
                                }

                                socketOut.writeObject(schueler);
                                socketOut.writeObject(aktPruefung.getName());
                                Object response = socketIn.readObject();
                                if (response instanceof Map) {
                                    createLogs((Map<String, Set<String>>) response, aktPruefung);
                                    this.updateProgress(1.0, 1.0);
                                } else if (response instanceof ConnectionParameter) {
                                    this.updateProgress(0.9, 1.0);
                                }

                            } catch (IOException io) {
                                Utilities.showMessageForExceptions(io, Verwaltungssoftware.schooltoolsLogo(), false);
                            }

                            return null;
                        }

                        private void createLogs(Map<String, Set<String>> map, PraPruefung akt) {
                            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(
                                    Verwaltungssoftware.getDirectoryFromPruefung(akt).toAbsolutePath().toString(), "Internetzugriffsprotokoll.txt"))) {
                                map.forEach((user, set) -> {
                                    if (!set.isEmpty()) {
                                        try {
                                            Set<String> websites = new HashSet<>(getWebsitesFromPruefung(akt));
                                            Schueler schueler = akt.getSchuelerSet()
                                                    .stream()
                                                    .filter(sch -> sch.getSsdKatnr() == Integer.parseInt(user.substring(5, 7)))
                                                    .findFirst()
                                                    .get();

                                            bw.write(user + "(" + schueler.getSsdVorname() + " " + schueler.getSsdZuname() + ")");
                                            bw.newLine();
                                            bw.newLine();

                                            Map<Boolean, List<String>> accepted = set.stream().collect(Collectors.partitioningBy(str -> {
                                                for (String website : websites) {
                                                    if (str.contains(website)) {        //Es handelt sich um den Logeintrag einer zugelassenen Website
                                                        return true;
                                                    }
                                                }
                                                return false;
                                            }));
                                            bw.write("Folgende zugelassenen Websites wurden augerufen: ");
                                            bw.newLine();
                                            for (String url : accepted.get(true)) {
                                                String parts[] = url.split("TCP_STATUS: ");
                                                bw.write(parts[0].trim());
                                                bw.newLine();
                                            }
                                            bw.newLine();
                                            bw.write("Folgende Websites wurden ebenfalls aufgerufen: ");
                                            bw.newLine();
                                            for (String url : accepted.get(false)) {
                                                String parts[] = url.split("TCP_STATUS: ");
                                                bw.write(parts[0].trim() + ". Zugriff erlaubt: " + ("TCP_DENIED".equals(parts[1]) ? "NEIN" : "JA"));
                                                bw.newLine();
                                            }
                                            bw.write("---------------------------------------------------------------------------------");
                                            bw.newLine();

                                        } catch (IOException io) {
                                            Utilities.showMessageForExceptions(io, Verwaltungssoftware.schooltoolsLogo(), false);
                                        }
                                    }
                                });

                            } catch (IOException io) {
                                Utilities.showMessageForExceptions(io, Verwaltungssoftware.schooltoolsLogo(), false);
                            }

                        }

                    };
                    Thread proxy = new Thread(internet);
                    internet.progressProperty().addListener((o, oldVa, newVa) -> {
                        if ((double) newVa == 1.0) {
                            end(aktPruefung);
                        } else if ((double) newVa == 0.9) {
                            Utilities.showMessageWithFixedWidth("Fehler",
                                    "Ein Fehler ist beim Beenden der Prüfung passiert",
                                    "Die Internetkonfigurationen konnten nicht vollständig entfernt werden.",
                                    Alert.AlertType.ERROR, Verwaltungssoftware.schooltoolsLogo(),
                                    Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                                    false);
                        }
                    });
                    proxy.start();

                } else {
                    end(aktPruefung);
                }
            }
        });
        t.start();

        Hibernate_DAO.getDaoInstance().updatePraktischePruefung(aktPruefung);

    }

    @FXML
    private void onActionPruefungLoeschen(ActionEvent event) {

        PraPruefung aktPruefung = tbl_pruefungen.getSelectionModel().getSelectedItem();
        if (!checkIfSelected("Meldung", "Sie müssen eine Zeile in der Tabelle  selektieren, um eine Prüfung zu löschen")) {
            return;
        }

        ButtonType answer = Utilities.showYesNoDialog("Sollen die Prüfung und die dazugehörigen Daten wirklich endgültig gelöscht werden?",
                "Meldung",
                Alert.AlertType.INFORMATION,
                Verwaltungssoftware.schooltoolsLogo(),
                Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH);
        if (answer.equals(ButtonType.YES)) {
            Path toDelete = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung);

            if (Verwaltungssoftware.deleteDirectory(toDelete.toFile())) {
                Hibernate_DAO.getDaoInstance().deletePraktischePruefung(aktPruefung);
                pruefungen.remove(aktPruefung);
                tbl_pruefungen.refresh();
            }

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

    @FXML
    private void onSchuelerDaten(ActionEvent event) {

        PraPruefung aktPruefung = tbl_pruefungen.getSelectionModel().getSelectedItem();
        if (!checkIfSelected("Meldung", "Sie müssen eine Zeile in der Tabelle  selektieren, um eine Prüfung zu starten")) {
            return;
        }
        Path userFile = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).resolve(Paths.get("Skripts/activeusers.txt"));
        Path userPruefer = Paths.get(Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).toAbsolutePath().toString(), "users.txt");

        if (aktPruefung.getStatus().equals(Verwaltungssoftware.PR_status.GESTARTET)) {
            try {
                Desktop.getDesktop().open(userPruefer.toFile());
            } catch (IOException ex) {
                Logger.getLogger(PSMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }

        Runtime r = Runtime.getRuntime();
        Process p = null;
        List<Schueler> schuelerList = Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(aktPruefung);
        schuelerList.sort((s1, s2) -> Integer.compare(s1.getSsdKatnr(), s2.getSsdKatnr()));
        int longestName = schuelerList.stream().mapToInt(sch -> sch.getSsdZuname().length()).max().getAsInt();
        int maxUsernameLength = 10;
        int lineLength = 58;

        try (BufferedWriter bw_user = Files.newBufferedWriter(userFile);
                BufferedWriter bw_pruefer = Files.newBufferedWriter(userPruefer)) {
            String line = null;
            String user = null, passwd = null;
            bw_pruefer.write(String.format("|KNR |Klasse | %-17s | %-12s | %8s |", "Vorname/Nachname", "Benutzername", "Passwort"));
            bw_pruefer.newLine();
            for (int i = 0; i < (2 + lineLength); i++) {
                bw_pruefer.write("-");
            }
            bw_pruefer.newLine();

            Random rd = new Random();
            for (Schueler sch : schuelerList) {
                bw_pruefer.write("|");
                for (int i = 0; i < lineLength; i++) {
                    bw_pruefer.write(" ");
                }
                bw_pruefer.write("|");
                bw_pruefer.newLine();

                user = sch.userString();
                passwd = "";
                for (int i = 0; i < 8; i++) {
                    passwd += (char) (97 + (rd.nextInt(26)));
                }
                bw_user.write(user.length() > maxUsernameLength ? user.substring(0, maxUsernameLength) : user);
                bw_user.write(";" + passwd);
                bw_user.newLine();
                String newLine = String.format("| %02d   %5s   %-" + (longestName >= 17 ? longestName : 17) + "s   %-" + (maxUsernameLength >= 12 ? maxUsernameLength : 12) + "s   %8s |",
                        sch.getSsdKatnr(),
                        sch.getKlasse().getKlaBez(),
                        sch.getSsdZuname(),
                        user.length() > maxUsernameLength ? user.substring(0, maxUsernameLength) : user,
                        passwd.trim());
                bw_pruefer.write(newLine);
                bw_pruefer.newLine();
                bw_pruefer.write("|");
                for (int i = 0; i < 14; i++) {
                    bw_pruefer.write(" ");
                }
                String vorname = sch.getSsdVorname();
                bw_pruefer.write(vorname);
                for (int i = 0; i < lineLength - 14 - vorname.length(); i++) {
                    bw_pruefer.write(" ");
                }
                bw_pruefer.write("|");
                bw_pruefer.newLine();

                for (int i = 0; i < (lineLength + 2); i++) {
                    bw_pruefer.write("-");
                }
                bw_pruefer.newLine();

            }

            bw_pruefer.flush();
            bw_pruefer.close();
            Desktop.getDesktop().open(userPruefer.toFile());

            PraPruefung selected = tbl_pruefungen.getSelectionModel().getSelectedItem();
            tbl_pruefungen.getSelectionModel().select(null);
            tbl_pruefungen.getSelectionModel().select(selected);
            tbl_pruefungen.refresh();

        } catch (IOException ex) {
            Utilities.showMessageForExceptions(ex, null, false);
        }
    }

    private List<String> getWebsitesFromPruefung(PraPruefung aktPruefung) {
        Path websitesFile = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).resolve("websites.txt");
        List<String> websites = new ArrayList<>();
        try {
            Path move = Paths.get(Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).toAbsolutePath().toString(), "Skripts", "websites.txt");
            Files.move(websitesFile, move);
            websites = Files.lines(move)
                    .map(str -> str.split("\\s")[0])
                    .collect(Collectors.toList());
        } catch (IOException io) {
            System.out.println(io);
        }
        return websites;
    }

}
