/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.services;

import at.htlstp.projekt.p04.db.Hibernate_DAO;
import at.htlstp.projekt.p04.graphic_tools.Utilities;
import at.htlstp.projekt.p04.model.PraPruefung;
import at.htlstp.projekt.p04.model.Schueler;
import at.htlstp.projekt.p04.pruefungsverwaltungssoftware.Verwaltungssoftware;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javafx.concurrent.Task;

/**
 *
 * @author 20120069
 */
public class UserDelTask extends Task<Double> {

    private double fortschritt;
    private List<Schueler> schueler;
    private PraPruefung pruefung;

    public UserDelTask(List<Schueler> schueler, PraPruefung pr) {
        this.schueler = schueler;
        this.pruefung = pr;
    }

    @Override
    protected Double call() throws Exception {
        try {

            Runtime r = Runtime.getRuntime();
            Process p2 = null;
            double d = 100.0 / schueler.size();
            Path skriptsPath = Verwaltungssoftware.getDirectoryFromPruefung(pruefung).resolve("Skripts");
            for (Schueler s : Hibernate_DAO.getDaoInstance().getSchuelerFromPruefung(pruefung)) {
                p2 = r.exec("cmd /c Start \"\" /B " + skriptsPath.toAbsolutePath().toString() + "\\endscript.bat X" + s.userString());      //Process p = r.exec("cmd /c start "+path+"\\test1234.bat"); öffnet neue cmd und gibt output aus
                System.out.println("del " + s.toString());
                fortschritt += d;
                this.updateValue(fortschritt);
                Thread.sleep(500);
            }

            p2.waitFor();
            p2.destroy();
            p2 = r.exec("cmd /c Start \"\" /B del " + skriptsPath + "\\activeusers.txt");
            p2.destroy();
            updateProgress(1L, 1L);
            
        } catch (IOException | InterruptedException ex) {
            Utilities.showMessageForExceptions(ex, Verwaltungssoftware.schooltoolsLogo(), false);
        }

        return null;
    }

}
