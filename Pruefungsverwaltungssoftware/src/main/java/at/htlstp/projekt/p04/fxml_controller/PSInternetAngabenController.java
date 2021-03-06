/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.fxml_controller;

import at.htlstp.projekt.p04.db.Hibernate_DAO;
import at.htlstp.projekt.p04.graphic_tools.Utilities;
import at.htlstp.projekt.p04.model.PraPruefung;
import at.htlstp.projekt.p04.pruefungsverwaltungssoftware.Verwaltungssoftware;
import at.htlstp.projekt.p04.services.FileWatcher;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Dru
 */
public class PSInternetAngabenController implements Initializable {

    @FXML
    private ToggleGroup tgroup_inet;
    @FXML
    private AnchorPane pane_scene;
    private List<PraPruefung> pruefungen = new ArrayList<>();
    private PSMenuController menucontroller;
    @FXML
    private TreeView<String> trview_ue;
    @FXML
    private Label lbl_name;
    @FXML
    private Label lbl_datum;
    @FXML
    private Label lbl_ur;
    @FXML
    private RadioButton radio_inet_no;
    @FXML
    private RadioButton radio_inet_yes;
    @FXML
    private ImageView imgview_inet_yes;
    @FXML
    private ImageView imgview_inet_no;
    @FXML
    private Button btn_info_angaben;
    @FXML
    private ListView<String> lst_angaben;
    @FXML
    private ListView<String> lst_websites;
    @FXML
    private TextField fld_newWebsite;
    @FXML
    private Button btn_newWebsite;
    @FXML
    private Button btn_websiteInfo;
    @FXML
    private Button btn_refresh;
    @FXML
    private AnchorPane pane_Inet;

    public void setMenucontroller(PSMenuController menucontroller) {
        this.menucontroller = menucontroller;
    }

    FileWatcher fw;
    private TreeItem<String> aktTreeItem;
    private PraPruefung aktPruefung;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (menucontroller != null) {
            //Aktuell selektiert Prüfung speichern
            aktPruefung = menucontroller.getTbl_pruefungen().getSelectionModel().getSelectedItem();
            //Initialisierungsarbeiten
            imgview_inet_no.setImage(getYesNoImageView(false).getImage());
            imgview_inet_yes.setImage(getYesNoImageView(true).getImage());

            //WICHTIG - immer beim Initializieren aufrufen  
            List<PraPruefung> allPruefungen = Hibernate_DAO.getDaoInstance().getPreaktischePruefungen();

            //Terminkalender einbleden und konfigurieren
            Verwaltungssoftware.installSchooltoolsStyleSheet(pane_scene.getScene());
            DatePicker data = new DatePicker();
            Verwaltungssoftware.disableSaturdaySunday(data);
            DatePickerSkin datePickerSkin = new DatePickerSkin(data);
            Node popupContent = datePickerSkin.getPopupContent();
            pane_scene.getChildren().add(popupContent);
            AnchorPane.setTopAnchor(popupContent, 95.0);
            AnchorPane.setLeftAnchor(popupContent, 445.0);

            trview_ue.showRootProperty().set(false);
            data.valueProperty().addListener((e, oldV, newV) -> {
                //treeView bei jeder neuen Selektion eines neuen Datums neu rendern
                Date fromLocal = Date.from(newV.atStartOfDay(ZoneId.systemDefault()).toInstant());
                TreeItem<String> rootItem = new TreeItem<>(
                        Verwaltungssoftware.parseDateToString(Verwaltungssoftware.DTF, fromLocal));

                for (Verwaltungssoftware.Unterrichtseinheit ue : Verwaltungssoftware.Unterrichtseinheit.values()) {
                    TreeItem<String> item = new TreeItem<>(ue.toString());
                    List<PraPruefung> pruefungenDatum = allPruefungen
                            .stream()
                            .filter(pra -> new java.sql.Date(pra.getDatum().getTime()).toLocalDate().equals(newV))
                            .collect(Collectors.toList());

                    for (PraPruefung pr : pruefungenDatum) {
                        if (ue.equals(pr.getUnterrichtsstunde())) {
                            TreeItem<String> treeItem = new TreeItem<>(pr.getName() + "(" + pr.getLehrer().getLehrerKb() + ")");
                            treeItem.setGraphic(getYesNoImageView(pr.getInternet()));
                            item.getChildren().add(treeItem);
                            aktTreeItem = pr.equals(aktPruefung) ? treeItem : aktTreeItem;
                        }

                    }
                    rootItem.getChildren().add(item);
                    rootItem.setExpanded(true);
                }
                trview_ue.rootProperty().set(rootItem);
                trview_ue.refresh();
            });

            data.setValue(new java.sql.Date(aktPruefung.getDatum().getTime()).toLocalDate());

            lbl_name.setText(aktPruefung.getName());
            lbl_datum.setText(Verwaltungssoftware.parseDateToString(Verwaltungssoftware.DTF, aktPruefung.getDatum()));
            lbl_ur.setText(aktPruefung.getUnterrichtsstunde().toString());

            ImageView btnImage = new ImageView(this.getClass().getResource("/images/info.png").toString());
            ImageView btnImage_copy = new ImageView(this.getClass().getResource("/images/info.png").toString());
            ImageView refreshImage = new ImageView(this.getClass().getResource("/images/refresh.png").toString());
            btnImage.setFitWidth(18.0);
            btnImage.setFitHeight(18.0);
            btnImage_copy.setFitWidth(18.0);
            btnImage_copy.setFitHeight(18.0);
            refreshImage.setFitWidth(18.0);
            refreshImage.setFitHeight(18.0);

            btn_info_angaben.setGraphic(btnImage);
            btn_websiteInfo.setGraphic(btnImage_copy);
            btn_refresh.setGraphic(refreshImage);
            //Internetoption
            radio_inet_yes.selectedProperty().addListener((d, oldV, newV) -> {
                if (newV) {
                    onActionRefresh(null);
                } else {
                    lst_websites.getItems().clear();
                    fld_newWebsite.setText(null);
                }
                aktPruefung.setInternet(newV);
                aktTreeItem.setGraphic(getYesNoImageView(newV));
                allPruefungen.forEach(pr -> {
                    if (pr.getId().equals(aktPruefung.getId())) {
                        pr.setInternet(aktPruefung.getInternet());   //Internetoption auch in der TreeView Liste updaten
                    }
                });
            });
            lst_websites.setItems(FXCollections.observableArrayList());
            pane_Inet.disableProperty().bind(Bindings.createBooleanBinding(() -> !radio_inet_yes.isSelected(), radio_inet_yes.selectedProperty()));
            if (aktPruefung.getInternet()) {
                radio_inet_yes.selectedProperty().set(true);

            } else {
                radio_inet_no.selectedProperty().set(true);
            }
           

            //Angaben
            Path angabenPath = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).resolve("Angaben");
            //WatchService
            lst_angaben.setItems(FXCollections.observableArrayList());
            fw = new FileWatcher(angabenPath);
            lst_angaben.itemsProperty().bind(fw.valueProperty());
            Thread start = new Thread(fw);
            start.start();

            Stage myStage = (Stage) btn_info_angaben.getScene().getWindow();
            myStage.setOnCloseRequest(e -> onActionClose(e));
            trview_ue.showRootProperty().set(false);
        }
    }

    public ImageView getYesNoImageView(boolean yes) {
        ImageView image = new ImageView(new Image(this.getClass().getResource("/images/" + (yes ? "internet" : "no_internet") + ".png").toString()));
        image.setFitHeight(16.0);
        image.setFitWidth(16.0);
        return image;
    }

    @FXML
    private void onActionAbgabeHinzufuegen(ActionEvent event) {
        Path angabenPath = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).resolve("Angaben");
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:"));
        List<File> selectFiles = fc.showOpenMultipleDialog(pane_scene.getScene().getWindow());
        if (selectFiles != null) {
            for (File file : selectFiles) {
                Path pFile = file.toPath();
                try {
                    Files.copy(pFile, angabenPath.resolve(pFile.getFileName()));
                } catch (FileAlreadyExistsException f) {
                    ButtonType answer = Utilities.showYesNoDialog(
                            "Die Datei mit dem Namen \"" + pFile.getFileName() + "\" existiert bereits im Angabenverzeichnis auf. Soll diese Datei ersetzt werden?",
                            "Kopieren einer Datei fehlgeschlagen!",
                            Alert.AlertType.WARNING,
                            Verwaltungssoftware.schooltoolsLogo(),
                            Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH
                    );
                    if (answer.equals(ButtonType.YES)) {
                        try {
                            Files.copy(pFile, angabenPath.resolve(pFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException ex) {
                            Utilities.showMessageWithFixedWidth("Fehler",
                                    "Ein Fehler trat beim Löschen der alten Datei aus dem Angabenverzeichnis.",
                                    "Um die Datei mit dem gleichen Namen in das Angabenverzeichnis zu kopieren, löschen oder schließen Sie die alte Datei und probieren Sie es erneut!",
                                    Alert.AlertType.WARNING,
                                    Verwaltungssoftware.schooltoolsLogo(),
                                    Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                                    false
                            );
                        }
                    }

                } catch (IOException ex) {
                    Utilities.showMessageForExceptions(ex, Verwaltungssoftware.schooltoolsLogo(), true);
                }
            }
        }
    }

    @FXML
    private void onActionInfoAngaben(ActionEvent event) {
        Utilities.showMessageWithFixedWidth("Info",
                "Angaben hinzufügen",
                "In dieser Liste werden alle Dokumente aus dem dazugehörigen Angabeverzeichnis abgebildet."
                + "Angaben können auch manuell in den Ordner kopiert werden. Mithilfe des Buttons mit der Beschriftung \"Datei auswählen\" "
                + "können Sie einzelne oder mehrere Dateien am Computer suchen und die Verwaltungssoftware wird "
                + "jeweils eine Kopie der Dokumente in das Abgabeverzeichnis erstellen. ",
                Alert.AlertType.INFORMATION,
                Verwaltungssoftware.schooltoolsLogo(),
                Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                false);
    }

    private void onActionClose(WindowEvent e) {
        fw.stopWatcher();
        Hibernate_DAO.getDaoInstance().updatePraktischePruefung(aktPruefung);
    }

    @FXML
    private void onActionWebsitesInfo(ActionEvent event) {
        Utilities.showMessageWithFixedWidth("Info",
                "Websites freigeben",
                "In dieser Liste werden alle Einträge der Datei \"websites.txt\" aus dem Prüfugnsverzeichnis "
                + "abgebildet. Sie können die Datei manuell verändern und in jede Zeile den Namen einer Internetseite eintragen oder das Textfeld nutzen, um neue"
                + "Websites hinzuzufügen (zB. www.primefaces.org). Wenn Sie die Datei ändern, sollten Sie die Liste mithilfe des Refresh-Buttons aktualisieren",
                Alert.AlertType.INFORMATION,
                Verwaltungssoftware.schooltoolsLogo(),
                Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                false);
    }

    @FXML
    private void onActionRefresh(ActionEvent event) {
        Path websites = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).resolve("websites.txt");
        List<String> websitesList = new ArrayList<>();
        if (websites.toFile().exists()) {
            try (BufferedReader bf = Files.newBufferedReader(websites)) {
                String website = null;
                while ((website = bf.readLine()) != null) {
                    websitesList.add(website.split("\\s")[0]);
                }
            } catch (IOException io) {
                Utilities.showMessageForExceptions(io, Verwaltungssoftware.schooltoolsLogo(), false);
            }
            lst_websites.getItems().clear();
            lst_websites.getItems().addAll(websitesList);
        } else {
            try {
                Files.createFile(websites);
            } catch (IOException ex) {
                Utilities.showMessageWithFixedWidth("Fehler", "Problem beim Anlegen einer Datei.", "Die Datei \"webistes.txt\" konnte im Prüfungsordner nicht angelegt werden. "
                        + "Versichern Sie sich, dass der Prüfungsordner existiert. ",
                        Alert.AlertType.ERROR,
                        Verwaltungssoftware.schooltoolsLogo(),
                        Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                        false);
            }
        }

    }

    @FXML
    private void onActionAddWebsite(ActionEvent event) {
        String name = fld_newWebsite.getText();
        if (name != null && !name.isEmpty()) {
            lst_websites.getItems().add(name);
            Path websites = Verwaltungssoftware.getDirectoryFromPruefung(aktPruefung).resolve("websites.txt");
            try {
                if (!websites.toFile().exists()) {
                    Files.createFile(websites);
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(websites.toFile(), true))) {
                    bw.write(name);
                    bw.newLine();
                }
            } catch (IOException io) {
                Utilities.showMessageWithFixedWidth("Fehler", "Problem beim Schreiben in eine Datei.", "Die Datei \"webistes.txt\" konnte im Prüfungsordner nicht überschrieben werden. "
                        + "Versichern Sie sich, dass der Prüfungsordner und die Datei existieren.",
                        Alert.AlertType.ERROR,
                        Verwaltungssoftware.schooltoolsLogo(),
                        Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                        false);
            }
            fld_newWebsite.setText(null);
        }
    }

}
