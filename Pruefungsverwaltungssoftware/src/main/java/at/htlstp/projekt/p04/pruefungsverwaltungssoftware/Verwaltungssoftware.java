package at.htlstp.projekt.p04.pruefungsverwaltungssoftware;

import at.htlstp.projekt.p04.db.HibernateJPAUtil;
import at.htlstp.projekt.p04.db.Hibernate_DAO;
import at.htlstp.projekt.p04.fxml_controller.PSMenuController;
import at.htlstp.projekt.p04.graphic_tools.CustomStage;
import at.htlstp.projekt.p04.graphic_tools.Utilities;
import at.htlstp.projekt.p04.ldap.LDAPAuthentification;
import at.htlstp.projekt.p04.login.Login;
import at.htlstp.projekt.p04.model.Lehrer;
import at.htlstp.projekt.p04.model.PraPruefung;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Properties;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.persistence.EntityManager;

public class Verwaltungssoftware extends Application {

    static {
        Properties ps_configs = new Properties();
        Verwaltungssoftware vsObject = new Verwaltungssoftware();
        try (InputStream configFile = vsObject.getClass().getResource("/property/configs.properties").openStream()) {
            ps_configs.load(configFile);
            INFORMATION_MESSAGE_WIDTH = Double.parseDouble((String) (ps_configs.get("INFORMATION_MESSAGE_WIDTH")));
            DTF = DateTimeFormatter.ofPattern(ps_configs.getProperty("DTF_PATTERN"));
            STYLE_URL = ps_configs.getProperty("STYLE_PATH");
            LOGO_URL = ps_configs.getProperty("LOGO_PATH");
            OPERATING_SYSTEM = System.getProperty("os.name");
            PRUEFER_PATH = Paths.get(ps_configs.getProperty("PRUEFER_PATH"));
            SCHUELER_PATH = Paths.get(ps_configs.getProperty("SCHUELER_PATH"));
            SKRIPTS_PATH = Paths.get(ps_configs.getProperty("SKRIPTS_PATH"));

        } catch (IOException ex) {
            Utilities.showMessageForExceptions(ex, schooltoolsLogo(), true);
        }
    }

    public static DateTimeFormatter DTF;
    public static double INFORMATION_MESSAGE_WIDTH;
    private static String STYLE_URL;
    private static String LOGO_URL;
    public static String OPERATING_SYSTEM;
    public static Path SCHUELER_PATH;
    public static Path PRUEFER_PATH;
    public static Path SKRIPTS_PATH;

    public static void installSchooltoolsStyleSheet(Scene rootScene) {
        Verwaltungssoftware vsObject = new Verwaltungssoftware();
        rootScene.getStylesheets().add(vsObject.getClass().getResource(STYLE_URL).toString());
    }

    public static Image schooltoolsLogo() {
        Verwaltungssoftware vsObject = new Verwaltungssoftware();
        return new Image(vsObject.getClass().getResource(LOGO_URL).toString());
    }

    public static boolean deleteDirectory(File directory) {

        try {
            if (directory.exists()) {
                File[] files = directory.listFiles();
                if (files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isDirectory()) {
                            deleteDirectory(files[i]);
                        } else {
                            Files.delete(files[i].toPath());
                        }
                    }
                }
                Files.delete(directory.toPath());
            }
        } catch (IOException io) {
            Utilities.showMessageWithFixedWidth("Fehler",
                    "Ein Fehler ist beim Löschen des Prüfungsverzeichnisses aufgetreten",
                    "Vergewissern Sie sich, dass keine Dateien oder Verzeichnisse aus dem jeweiligen "
                    + " Prüfungsordner geöffnet sind und versuchen Sie es erneut.",
                    Alert.AlertType.ERROR,
                    Verwaltungssoftware.schooltoolsLogo(),
                    Verwaltungssoftware.INFORMATION_MESSAGE_WIDTH,
                    false);
            return false; 

        }
        return true; 

    }

    @Override
    public void start(Stage stage) {
        //Login 
        //Lehrer_Generator.main(null);
        Login login = new Login(LDAPAuthentification::isValidUser);
        login.showLogin();
        //Wird vom Controller beim gültigen Login geändert.
        login.closeProperty().addListener((i, oldV, newV) -> {

            //DB Hochfahren
            try {
                String kurzBezLehrer = login.getValidUser();
                //Datenbank hochladen 
                Class.forName("at.htlstp.projekt.p04.db.HibernateJPAUtil");
                Lehrer lr = Hibernate_DAO.getDaoInstance().getLehrerByKurzbezeichnung(kurzBezLehrer);
                EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
                System.out.println(em.createQuery("select le from Lehrer le where le.lehrerKb = :le").setParameter("le", "lul").getResultList());

                CustomStage<PSMenuController> customstage
                        = new CustomStage<>(stage,
                                "Prüfungsverwaltungssoftware",
                                Verwaltungssoftware.schooltoolsLogo(),
                                this.getClass().getResource("/fxml/PSMenu.fxml"),
                                false);

                customstage.getStage().show();
                customstage.getController().setLehrer(lr);
                customstage.getController().initialize(null, null);
                customstage.getStage().setOnCloseRequest(e -> HibernateJPAUtil.close());
            } catch (Exception ex) {
                Utilities.showMessageForExceptions(ex, Verwaltungssoftware.schooltoolsLogo(), true);
            }

        });
    }

    public enum PR_status {
        ANGELEGT, GESTARTET, BEENDET
    }

    public enum Unterrichtseinheit {
        U1(" 7:50"), U2(" 8:40"), U3(" 9:40"), U4("10:30"), U5("11:20"), U6("12:20"), U7("13:10"), U8("14:00"), U9("15:00"), U10("15:50");

        private Unterrichtseinheit(String beginn) {
            this.beginn = beginn;
        }
        private String beginn;

        public static Unterrichtseinheit getFromString(String ue) {
            for (Unterrichtseinheit u : Unterrichtseinheit.values()) {
                if (u.toString().equals(ue)) {
                    return u;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            String parts[] = beginn.split(":");
            int minutes = Integer.parseInt(parts[0].trim()) * 60 + Integer.parseInt(parts[1].trim());
            Duration d = Duration.ofMinutes(minutes);
            d = d.plus(50, ChronoUnit.MINUTES);
            String end = String.format("%2d:%02d", d.toMinutes() / 60, d.toMinutes() % 60);
            String bez = String.format("%2s", super.toString().substring(1)) + " (" + beginn + " - " + end + ")";
            return bez;
        }

    }

    public static String parseDateToString(DateTimeFormatter df, Date date) {
        LocalDate ld = new java.sql.Date(date.getTime()).toLocalDate();
        return ld.format(df);
    }

    public static void disableSaturdaySunday(DatePicker dpick) {
        Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.getDayOfWeek().equals(DayOfWeek.SATURDAY) || item.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    setDisable(true);
                }
            }
        };
        dpick.setDayCellFactory(dayCellFactory);

    }

    public static Path getDirectoryFromPruefung(PraPruefung pruefung) {
        Path p = Paths.get(Verwaltungssoftware.PRUEFER_PATH.toString() + "/"
                + pruefung.getLehrer().getLehrerKb().toUpperCase() + "_Pruefungen/"
                + pruefung.getName().replace(" ", "-") + "_" + pruefung.getKlasse());
        return p;
    }

    public static void createDirectoryForPruefung(PraPruefung pr) throws IOException {
        Path pathPruefung = Verwaltungssoftware.getDirectoryFromPruefung(pr);
        Files.createDirectory(pathPruefung);
        Files.createDirectory(pathPruefung.resolve(Paths.get("Angaben")));
        Files.createDirectory(pathPruefung.resolve(Paths.get("Abgaben")));
        Files.createDirectory(pathPruefung.resolve(Paths.get("Skripts")));
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
