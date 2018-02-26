/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.services;

import at.htlstp.projekt.p04.model.PraPruefung;
import at.htlstp.projekt.p04.model.Schueler;
import at.htlstp.projekt.p04.pruefungsverwaltungssoftware.Verwaltungssoftware;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.concurrent.Task;

/**
 *
 * @author 20120069
 */
public class UserTask extends Task<Double> {

    private double fortschritt;
    private List<Schueler> schueler;
    private PraPruefung pruefung;
    private Runnable end; 

    public UserTask(List<Schueler> schueler, PraPruefung pr, Runnable end) {
        this.schueler = schueler;
        this.pruefung = pr;
        this.end = end; 
       }

    @Override
    protected Double call() throws Exception {
        try {
            Path path2 = Verwaltungssoftware.getDirectoryFromPruefung(pruefung).resolve(Paths.get("Skripts"));
            Runtime r = Runtime.getRuntime();
            Process p = null;
            p = r.exec("cmd /c Start \"\" /B del " + path2 + "\\activeusers.txt");
            p.waitFor();
            double d = 100 / schueler.size();
            try(BufferedReader bf = Files.newBufferedReader(Paths.get(path2.toAbsolutePath().toString(), "activeusers.txt"))){
              String line = null; 
                while((line = bf.readLine()) != null){
                p = r.exec("cmd /c Start \"\" /B " + path2 + "\\createuser.bat X" + line.replace(";", " "));       //Process p = r.exec("cmd /c start "+path+"\\test1234.bat"); öffnet neue cmd und gibt output aus 

                fortschritt += d;
                this.updateValue(fortschritt);
                Thread.sleep(500);
                }
            }
            this.updateValue(100.0);
            p.waitFor(5, TimeUnit.SECONDS);
            p.destroy();
            end.run();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
