/*
 * To change pruefung license header, choose License Headers in Project Properties.
 * To change pruefung template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.fxml_controller;

import at.htlstp.projekt.p04.db.Hibernate_DAO;
import at.htlstp.projekt.p04.db.IDAO;
import at.htlstp.projekt.p04.graphic_tools.Utilities;
import at.htlstp.projekt.p04.model.Gegenstand;
import at.htlstp.projekt.p04.model.Klasse;
import at.htlstp.projekt.p04.model.PraPruefung;
import at.htlstp.projekt.p04.pruefungsverwaltungssoftware.Verwaltungssoftware;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Dru
 */
//java -jar target/Pruefungsverwaltungssoftware-1.0.jar
public class PSPruefungController implements Initializable {

    @FXML
    private Label lbl_pr_lehrername;
    @FXML
    private ChoiceBox<Klasse> chbox_pr_klasse;
    @FXML
    private ChoiceBox<Gegenstand> chbox_pr_gegenstand;
    @FXML
    private ChoiceBox<Verwaltungssoftware.Unterrichtseinheit> chbox_pr_ue;
    @FXML
    private DatePicker dpicker_pr_date;

    @FXML
    private TextField fld_pr_name;
    private PSMenuController menuController;
    @FXML
    private Button btn_speichern;

    private PraPruefung pruefung;

    private final Tooltip tip_name
            = new Tooltip("Der Name einer Prüfung darf nicht leer sein. Es sind nur Buchstaben, Zahlen und die zwei Sonderzeichen \".\" and \"-\" zugelassen.");
    private final Tooltip tip_date = new Tooltip("Das Prüfungsdatum darf nicht in der Vergangenheit\n"
            + " liegen, leer oder an einem Samstag/Sonntag sein!");

    public void setMenuController(PSMenuController menuController) {
        this.menuController = menuController;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (menuController != null) {
            Verwaltungssoftware.disableSaturdaySunday(dpicker_pr_date);
            IDAO instace = Hibernate_DAO.getDaoInstance();
            lbl_pr_lehrername.setText(menuController.getLehrer().getLehrerVorname() + " " + menuController.getLehrer().getLehrerZuname());
            chbox_pr_klasse.setItems(FXCollections.observableArrayList(menuController.getSchuelerInKlassenByLehrer().keySet()).sorted());
            chbox_pr_ue.setItems(FXCollections.observableArrayList(Verwaltungssoftware.Unterrichtseinheit.values()));

            chbox_pr_klasse.valueProperty().addListener((l, oldV, newV) -> {
                chbox_pr_gegenstand.setItems(FXCollections.observableArrayList(
                        instace.getGegenstaendeInKlasseByLehrer(menuController.getLehrer(), newV)).sorted());
                chbox_pr_gegenstand.getSelectionModel().selectFirst();
            });

            Stage myStage = (Stage) lbl_pr_lehrername.getScene().getWindow();
            myStage.setOnCloseRequest(e -> onActionClose(e));

            fld_pr_name.textProperty().addListener((e, oldV, newV) -> {
                if (!isNameValid(newV)) {
                    Tooltip.install(fld_pr_name, tip_name);
                    fld_pr_name.setStyle("-fx-border-width: 2px;"
                            + "-fx-border-color: red");
                } else {
                    Tooltip.uninstall(fld_pr_name, tip_name);
                    fld_pr_name.setStyle(null);
                }
            });

            dpicker_pr_date.valueProperty().addListener((e, oldV, newV) -> {
                if (!isDateValid(newV)) {
                    Tooltip.install(dpicker_pr_date, tip_date);
                    dpicker_pr_date.setStyle("-fx-border-width: 2px;"
                            + "-fx-border-color: red");
                } else {
                    Tooltip.uninstall(dpicker_pr_date, tip_date);
                    dpicker_pr_date.setStyle(null);
                }

            });

            //Styles hinzufügen 
            Verwaltungssoftware.installSchooltoolsStyleSheet(dpicker_pr_date.getScene());
        }

    }

    public void initializeNewPruefung() {
        chbox_pr_gegenstand.getSelectionModel().selectFirst();
        chbox_pr_klasse.getSelectionModel().selectFirst();
        chbox_pr_ue.getSelectionModel().selectFirst();
        dpicker_pr_date.setValue(LocalDate.now());
        fld_pr_name.setText(null);
        fld_pr_name.requestFocus();
        pruefung = null;
    }

    public void initializePruefung(PraPruefung pr) {
        chbox_pr_gegenstand.getSelectionModel().select(pr.getGegenstand());
        chbox_pr_klasse.getSelectionModel().select(pr.getKlasse());
        chbox_pr_ue.getSelectionModel().select(pr.getUnterrichtsstunde());
        dpicker_pr_date.setValue(new java.sql.Date(pr.getDatum().getTime()).toLocalDate());
        fld_pr_name.setText(pr.getName());
        fld_pr_name.requestFocus();
        pruefung = pr;
    }

    @FXML
    private void onActionSpeichern(ActionEvent event) {
        if (!testInput()) {
            Utilities.showMessageWithFixedWidth("Medlung",
                    "Einige Eingaben beinhalten Fehler",
                    "Vergewissern Sie sich, dass alle Eingabengfelder richtige Daten aufweisen.",
                    Alert.AlertType.WARNING,
                    Verwaltungssoftware.schooltoolsLogo(),
                    Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                    false);
            return;
        }

        Date fromLocal = Date.from(dpicker_pr_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (pruefung == null) {
            pruefung = getPraPruefungFromScene();
            pruefung.setInternet(false);
            Hibernate_DAO.getDaoInstance().persistPraktischePruefung(pruefung);
            menuController.getTbl_pruefungen().getItems().add(pruefung);
            try {
                Verwaltungssoftware.createDirectoryForPruefung(pruefung);
                Path skripts = Verwaltungssoftware.SKRIPTS_PATH;
                Files.list(skripts)
                        .forEach(file -> copySkript(skripts.resolve(file).toAbsolutePath(), pruefung));

            } catch (IOException io) {
                System.out.println(io.getMessage());
            }
//               
        } else {
            //Prüfung existiert bereits
            Path pruefungsPath = Verwaltungssoftware.getDirectoryFromPruefung(pruefung);
            String oldname = pruefung.getName();
            Klasse oldKlasse = pruefung.getKlasse();

            if (!oldKlasse.toString().equals(chbox_pr_klasse.getValue().toString()) || !oldname.equals(fld_pr_name.getText())) {
                try {
                    pruefung.setName(fld_pr_name.getText());
                    pruefung.setKlasse(chbox_pr_klasse.getValue());
                    Path newDirectory = Verwaltungssoftware.getDirectoryFromPruefung(pruefung);
                    //Erstellt den Pfad zum entsprechenden Verzeichnis 
                    Files.move(pruefungsPath, newDirectory);    //Umbennen
                } catch (IOException ex) {
                    //User-freundliche Fehlermeldung
                    Utilities.showMessageWithFixedWidth("Fehler",
                            "Ein Fehler ist beim Umändern des Verzeichnisnamens aufgetreten",
                            "Vergewissern Sie sich, dass keine Dateien oder Verzeichnisse aus dem jeweiligen "
                            + " Prüfungsordner geöffnet sind und versuchen Sie es erneut.",
                            Alert.AlertType.ERROR,
                            Verwaltungssoftware.schooltoolsLogo(),
                            Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                            false);
                    pruefung.setName(oldname);
                    pruefung.setKlasse(oldKlasse);
                    return;
                }
            }
            pruefung.setUnterrichtsstunde(chbox_pr_ue.getValue());
            pruefung.setDatum(fromLocal);
            pruefung.setGegenstand(chbox_pr_gegenstand.getValue());
            Hibernate_DAO.getDaoInstance().updatePraktischePruefung(pruefung);
            menuController.getTbl_pruefungen().refresh();

        }

        //Wichtig
        menuController.getTbl_pruefungen().getSelectionModel().select(pruefung);
        Stage myStage = (Stage) lbl_pr_lehrername.getScene().getWindow();
        myStage.setOnCloseRequest(null);
        myStage.fireEvent(new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST));   //Stage schließen 

    }

    @FXML
    private void onActionAbbrechen(ActionEvent event
    ) {

        if (pruefung != null) {
            PraPruefung scenePruefung = getPraPruefungFromScene();
            boolean notEqual = false;
            if (!Objects.equals(pruefung.getName(), scenePruefung.getName())) {
                notEqual = true;
            }
            if (pruefung.getUnterrichtsstunde() != scenePruefung.getUnterrichtsstunde()) {
                notEqual = true;
            }

            if (!Objects.equals(new java.sql.Date(pruefung.getDatum().getTime()).toLocalDate(),
                    new java.sql.Date(scenePruefung.getDatum().getTime()).toLocalDate())) {
                notEqual = true;
            }
            if (!Objects.equals(pruefung.getGegenstand(), scenePruefung.getGegenstand())) {
                notEqual = true;
            }
            if (!Objects.equals(pruefung.getLehrer(), scenePruefung.getLehrer())) {
                notEqual = true;
            }
            if (!Objects.equals(pruefung.getKlasse(), scenePruefung.getKlasse())) {
                notEqual = true;
            }
            if (!notEqual) {
                Stage myStage = (Stage) lbl_pr_lehrername.getScene().getWindow();
                myStage.setOnCloseRequest(null);
                myStage.fireEvent(new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST));
                return;
            }
        }

        ButtonType answer = Utilities.showYesNoDialog("Wollen Sie die Änderungen verwerfen?",
                "Meldung",
                Alert.AlertType.INFORMATION,
                Verwaltungssoftware.schooltoolsLogo(),
                Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH);
        if (answer.equals(ButtonType.YES)) {
            Stage myStage = (Stage) lbl_pr_lehrername.getScene().getWindow();
            myStage.setOnCloseRequest(null);
            myStage.fireEvent(new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST));   //Stage schließen 
        }

    }

    public boolean testInput() {
        return !fld_pr_name.textProperty().getValue().isEmpty()
                && (isDateValid(dpicker_pr_date.getValue()));
    }

    private void onActionClose(WindowEvent e) {
        if (e != null) {
            e.consume();   //Schließen des Fenster verhindern
        }
        onActionAbbrechen(null);
    }

    public PraPruefung getPraPruefungFromScene() {
        Date fromLocal = Date.from(dpicker_pr_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        PraPruefung newP = new PraPruefung(
                fld_pr_name.getText(),
                chbox_pr_ue.getValue(),
                fromLocal,
                Verwaltungssoftware.PR_status.ANGELEGT,
                chbox_pr_gegenstand.getValue(),
                menuController.getLehrer(),
                chbox_pr_klasse.getValue());
        newP.setInternet(false);
        return newP;
    }

    public static boolean isDateValid(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                && !date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                && !date.isBefore(LocalDate.now());

        //hallo
    }

    private void copySkript(Path toAbsolutePath, PraPruefung pruefung) {
        Path src = toAbsolutePath;
        Path dest = Verwaltungssoftware.getDirectoryFromPruefung(pruefung).resolve("skripts").resolve(src.getFileName());
        try {
            Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Utilities.showMessageForExceptions(ex, Verwaltungssoftware.schooltoolsLogo(), true);
        }
    }

    private static boolean isNameValid(String newV) {
        return (newV != null
                && !newV.isEmpty()
                && newV.matches("[A-Za-zäöüÄÖÜ\\d\\-\\.\\s]*"));
    }
}
