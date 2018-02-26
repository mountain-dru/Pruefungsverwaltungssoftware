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
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
import javafx.scene.control.ProgressIndicator;
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
    @FXML
    private Label lbl_User;
    @FXML
    private ProgressIndicator pgi_User;
    @FXML
    private Button btn_daten;

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

            BooleanBinding anglegtUndSelektiert = Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                return !(sel != null && sel.getStatus().equals(Verwaltungssoftware.PR_status.ANGELEGT));
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty());

            btn_PruefungBearbeiten.disableProperty().bind(anglegtUndSelektiert);
            btn_SchuelerZuweisen.disableProperty().bind(anglegtUndSelektiert);
            btn_AngabenInternet.disableProperty().bind(anglegtUndSelektiert);

            btn_daten.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                System.out.println("AUFGERUFEN");
                return !(sel != null && !sel.getSchuelerSet().isEmpty()
                        && !sel.getStatus().equals(Verwaltungssoftware.PR_status.BEENDET));
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty()));

            btn_PruefungStarten.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                if (sel == null) return true; 
                Path pathToSchuelerList = Verwaltungssoftware.getDirectoryFromPruefung(sel).resolve("Skripts/activeusers.txt");     //Schuelerdaten müssen bereits angelegt worden sein.
                return !(pathToSchuelerList.toFile().exists()
                        && !sel.getSchuelerSet().isEmpty()
                        && sel.getStatus().equals(Verwaltungssoftware.PR_status.ANGELEGT));
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty()));

            btn_VerzKopieren.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                return !(sel != null && sel.getStatus().equals(Verwaltungssoftware.PR_status.GESTARTET));
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty()));

            btn_PruefungLoeschen.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                return !(sel != null && !sel.getStatus().equals(Verwaltungssoftware.PR_status.GESTARTET) && pgi_User.getProgress() == 1.0);
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty(), pgi_User.progressProperty()));

            btn_PruefungBeenden.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                PraPruefung sel = tbl_pruefungen.getSelectionModel().getSelectedItem();
                return !(sel != null && sel.getStatus().equals(Verwaltungssoftware.PR_status.GESTARTET) && pgi_User.getProgress() == 1.0);       //verzeichnisse kopieren 
            }, tbl_pruefungen.getSelectionModel().selectedItemProperty(), pgi_User.progressProperty()));

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
        PraPruefung aktPruefung = tbl_pruefungen.getSelectionModel().getSelectedItem();
        if (!checkIfSelected("Meldung", "Sie müssen eine Zeile in der Tabelle  selektieren, um eine Prüfung zu starten")) {
            return;
        }

        Runnable run = () -> {
            aktPruefung.setStatus(Verwaltungssoftware.PR_status.GESTARTET);
            tbl_pruefungen.getSelectionModel().select(null);
            tbl_pruefungen.getSelectionModel().select(aktPruefung);
            tbl_pruefungen.refresh();
        };

        UserTask task = new UserTask(Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(aktPruefung), aktPruefung, run);
        Thread t = new Thread(task);
        task.valueProperty().addListener((o, oldV, newV) -> {
            if (newV != null) {
                pgi_User.setProgress(newV / 100.0);
                System.out.println("UPDATE");
            }

        });
        lbl_User.setText("Usererstellung: ");
        t.start();

        Hibernate_DAO.getDaoInstance().updatePraktischePruefung(aktPruefung);

    }

    @FXML
    private void onActionPruefungBeenden(ActionEvent event) {
        PraPruefung aktPruefung = tbl_pruefungen.getSelectionModel().getSelectedItem();
        if (!checkIfSelected("Meldung", "Sie müssen eine Zeile in der Tabelle  selektieren, um eine Prüfung zu beenden")) {
            return;
        }
        Runnable run = () -> {
            aktPruefung.setStatus(Verwaltungssoftware.PR_status.BEENDET);
            tbl_pruefungen.getSelectionModel().select(null);
            tbl_pruefungen.getSelectionModel().select(aktPruefung);
            tbl_pruefungen.refresh();
        };
        UserDelTask task = new UserDelTask(Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(aktPruefung), aktPruefung, run);
        Thread t = new Thread(task);
        task.valueProperty().addListener((o, oldV, newV) -> {
            if (newV != null) {
                pgi_User.setProgress(newV / 100.0);
                System.out.println("DELETE");
            }

        });
        lbl_User.setText("Userentfernung: ");
        t.start();
        Hibernate_DAO.getDaoInstance().updatePraktischePruefung(aktPruefung);


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
            Hibernate_DAO.getDaoInstance().deletePraktischePruefung(aktPruefung);
            pruefungen.remove(aktPruefung);
            tbl_pruefungen.refresh();
            Path toDelete = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung);
            Verwaltungssoftware.deleteDirectory(toDelete.toFile());
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

        Runtime r = Runtime.getRuntime();
        Process p = null;
//            p = r.exec("cmd /c Start \"\" /B del " + skripts + "\\activeusers.txt");
//            p.waitFor();
        List<Schueler> schuelerList = Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(aktPruefung);
        schuelerList.sort((s1, s2) -> Integer.compare(s1.getSsdKatnr(), s2.getSsdKatnr()));
//            for (Schueler sch : schuelerList) {
//                p = r.exec("cmd /c Start \"\" /B " + skripts + "\\generateuserdata.bat X" + sch.userString());
//                Thread.sleep(100);
//            }
//            p.waitFor();

//        UserTask task = new UserTask(Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(aktPruefung), aktPruefung);
//        Thread t = new Thread(task);
//        task.valueProperty().addListener((o, oldV, newV) -> {
//            if (newV != null) {
//                pgi_User.setProgress(newV / 100.0);
//                System.out.println("UPDATE");
//            }
//
//        });
//       t.start();
        Path userFile = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).resolve(Paths.get("Skripts/activeusers.txt"));
        Path userPruefer = Paths.get(Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).toAbsolutePath().toString(), "user.txt");
        try (BufferedWriter bw_user = Files.newBufferedWriter(userFile);
                BufferedWriter bw_pruefer = Files.newBufferedWriter(userPruefer)) {
            String line = null;
            String user = null, passwd = null;
            bw_pruefer.write(String.format("|KNR |Klasse | %-30s | %-25s | %8s |", "Vorname/Nachname", "Benutzername", "Passwort"));
            bw_pruefer.newLine();
            for (int i = 0; i < 86; i++) {
                bw_pruefer.write("-");
            }
            bw_pruefer.newLine();

            Random rd = new Random();
            for (Schueler sch : schuelerList) {
                bw_pruefer.write("|");
                for (int i = 0; i < 84; i++) {
                    bw_pruefer.write(" ");
                }
                bw_pruefer.write("|");
                bw_pruefer.newLine();

                user = sch.userString();
                passwd = "";
                for (int i = 0; i < 8; i++) {
                    passwd += (char) (97 + (rd.nextInt(26)));
                }

                bw_user.write(user + ";" + passwd);
                bw_user.newLine();
                String newLine = String.format("| %02d   %5s   %-30s   %-25s   %8s |",
                        sch.getSsdKatnr(),
                        sch.getKlasse().getKlaBez(),
                        sch.getSsdVorname() + " " + sch.getSsdZuname(),
                        user,
                        passwd.trim());
                bw_pruefer.write(newLine);
                bw_pruefer.newLine();
                bw_pruefer.write("|");
                for (int i = 0; i < 84; i++) {
                    bw_pruefer.write(" ");
                }
                bw_pruefer.write("|");
                bw_pruefer.newLine();

                for (int i = 0; i < 86; i++) {
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

}
