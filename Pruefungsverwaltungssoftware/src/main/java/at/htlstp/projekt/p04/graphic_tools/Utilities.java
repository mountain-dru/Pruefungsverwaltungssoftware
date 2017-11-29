package at.htlstp.projekt.p04.graphic_tools;

//Imports 
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/*
  * Klasse 4AHIF
 * DV-Nummer: 20130041
 * Katalognummer: 01
 * @author Bejinariu Alexandru
 */
public class Utilities {

    //Statische Methoden - werden von unterschiedliche Klassen dieses Pakets verwendet 
    /**
     * Erstellt und blendet ein Alert-Fenster ein. Empfohlemene Verwendung:
     * Aufzeigen von aufgetretenen Ereignissen während des Programmablaufs
     * Methode darf nur von grafischen Threads aufgerufen werden
     *
     * @param title Title des Alert Fensters
     * @param headerMessage Title innerhalb des Fensters
     * @param message einzublendender Text
     * @param type Typ
     * @param expanded bei true wird das Fenster mithilfe einer TextArea
     * erweitert
     */
    private static void showMessage(String title, String headerMessage, String message, Alert.AlertType type, Image icon, boolean expanded, boolean wrap, Double fixedWindowWidth) {
        Alert alert = new Alert(type);
        alert.initOwner(null);
        alert.setTitle(title);
        alert.setHeaderText(headerMessage);
        Optional<String> str = Arrays.asList(message.split("\n")).stream().max((s1, s2) -> Integer.compare(s1.length(), s2.length()));

        if (wrap) {
            alert.getDialogPane().setMinWidth(fixedWindowWidth);
        } else {
            alert.getDialogPane().setMinWidth(str.get().length() * (12 / 1.85));
        }

        if (icon != null) {
            Stage dialog = (Stage) alert.getDialogPane().getScene().getWindow();
            dialog.getIcons().add(icon);
        }

        if (expanded) {     //Bei true wird der Text in einer TextArea gepackt(Scrollable - daher beliebig lang)

            alert.setContentText("Mehr Details: ");
            TextArea messageNode = new TextArea(message);
            messageNode.setEditable(false);
            messageNode.setWrapText(true);
            messageNode.setMaxHeight(Double.MAX_VALUE);

            GridPane.setVgrow(messageNode, Priority.ALWAYS);
            GridPane.setHgrow(messageNode, Priority.ALWAYS);
            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(messageNode, 0, 0);
            expContent.getStyleClass().add("grid");

            AnchorPane wrapper = new AnchorPane(expContent);
            AnchorPane.setTopAnchor(expContent, 10.0);
            AnchorPane.setRightAnchor(expContent, 0.0);
            AnchorPane.setLeftAnchor(expContent, 0.0);
            wrapper.getStyleClass().add("anchor");

            alert.getDialogPane().setExpandableContent(wrapper);
            alert.getDialogPane().setExpanded(true);

        } else {
            alert.setContentText(message);
        }
        //Anzeigen und warten bis der User die Information gelesen hat 
        alert.showAndWait();
    }

    public static ButtonType showYesNoDialog(String text, String title, Alert.AlertType windowtype, Image icon, Double fixedWidth) {

        Alert alert = new Alert(windowtype);
        alert.initOwner(null);
        alert.setTitle(title);
        alert.getDialogPane().setMinWidth(fixedWidth);
        alert.getDialogPane().setMaxWidth(fixedWidth);
        alert.setHeaderText(text);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);
        if (icon != null) {
            Stage dialog = (Stage) alert.getDialogPane().getScene().getWindow();
            dialog.getIcons().add(icon);
        }

        alert.showAndWait();
        return alert.getResult();
    }

    //Overloaded methodes 
    /**
     * Gibt Informationen zu einer aufgetretenen Exception in einem grafischen
     * Fenster aus
     *
     * @param e die Exception
     */
    public static void showMessageForExceptions(Exception e, Image icon, boolean expanded) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();
        Utilities.showMessage("Info", "Exception aufgetreten", exceptionText, Alert.AlertType.ERROR, icon, expanded, false, null);
    }

    public static void showMessageForExceptionsWithFixedWidth(Exception e, Image icon, boolean expanded, Double fixedWindowWidth) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();
        Utilities.showMessage("Info", "Exception aufgetreten", exceptionText, Alert.AlertType.ERROR, icon, expanded, true, fixedWindowWidth);
    }

    public static void showMessageWithFixedWidth(String title, String headerMessage, String message, Alert.AlertType type, Image icon, Double fixedWindowWidth, boolean expanded) {
        Utilities.showMessage(title, headerMessage, message, type, icon, expanded, true, fixedWindowWidth);
    }

    public static void showNormalMessage(String title, String headerMessage, String message, Alert.AlertType type, Image icon, boolean expanded) {
        Utilities.showMessage(title, headerMessage, message, type, icon, expanded, false, null);
    }

}
