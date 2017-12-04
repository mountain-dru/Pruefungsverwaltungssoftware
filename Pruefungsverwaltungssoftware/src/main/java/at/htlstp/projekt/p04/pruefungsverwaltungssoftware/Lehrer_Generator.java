/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.pruefungsverwaltungssoftware;

import at.htlstp.projekt.p04.db.DAO;
import at.htlstp.projekt.p04.db.HibernateJPAUtil;
import at.htlstp.projekt.p04.model.Lehrer;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Dru
 */
public class Lehrer_Generator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Path p = Paths.get("src/main/java/at/htlstp/projekt/p04/pruefungsverwaltungssoftware/lehrer.csv");

        try {
            Class.forName("at.htlstp.projekt.p04.db.HibernateJPAUtil");
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        List<Lehrer> list = em.createQuery("select l from Lehrer l", Lehrer.class).getResultList();
        try (BufferedWriter writer = Files.newBufferedWriter(p)) {
            for (Lehrer l : list) {
                //Kürzel, vorname, nachname 
                writer.write(l.getLehrerKb() + ";" + l.getLehrerVorname() + ";" + l.getLehrerZuname());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
