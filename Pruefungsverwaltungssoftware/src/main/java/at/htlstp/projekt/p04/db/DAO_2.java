//package at.htlstp.projekt.p04.db;
//
//import at.htlstp.projekt.p04.db.IDAO;
//import at.htlstp.projekt.p04.model.Berechtigung;
//import at.htlstp.projekt.p04.model.BerechtigungId;
//import at.htlstp.projekt.p04.model.Bewertung;
//import at.htlstp.projekt.p04.model.BewertungId;
//import at.htlstp.projekt.p04.model.Direktionsdaten;
//import at.htlstp.projekt.p04.model.Gegenstand;
//import at.htlstp.projekt.p04.model.Gesetzkategorie;
//import at.htlstp.projekt.p04.model.Gruppe;
//import at.htlstp.projekt.p04.model.Kategorie;
//import at.htlstp.projekt.p04.model.Klasse;
//import at.htlstp.projekt.p04.model.Kompetenzbereich;
//import at.htlstp.projekt.p04.model.LaufendeNote;
//import at.htlstp.projekt.p04.model.Lehrer;
//import at.htlstp.projekt.p04.model.Lehrfach;
//import at.htlstp.projekt.p04.model.Mahnung;
//import at.htlstp.projekt.p04.model.News;
//import at.htlstp.projekt.p04.model.Schueler;
//import at.htlstp.projekt.p04.model.Semesternote;
//import at.htlstp.projekt.p04.model.Sprechstunde;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//import org.hibernate.Hibernate;
//import org.hibernate.HibernateException;
//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
///**
// *
// * @author Michael Scherr 5BHIF
// */
//public class DAO implements IDAO {
//
//    @Override
//    public Schueler getSchuelerByAnmeldenr(int ssdAnmeldenr) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Schueler schueler = null;
//
//        try {
//            String abfrage = "from Schueler s where s.ssdAnmeldenr = :ssdAnmeldenr";
//            Query qu = s.createQuery(abfrage).setParameter("ssdAnmeldenr", ssdAnmeldenr);
//            schueler = (Schueler) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//
//        return schueler;
//    }
//
//    @Override
//    public List<Schueler> getSchueler() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Schueler> schueler;
//
//        try {
//            Query qu = s.createQuery("from Schueler");
//            schueler = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return schueler;
//    }
//
//    @Override
//    public List<Klasse> getKlassen() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Klasse> klassen;
//
//        try {
//            Query qu = s.createQuery("from Klasse");
//            klassen = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return klassen;
//    }
//
//    @Override
//    public List<Gegenstand> getGegenstaende() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Gegenstand> gegenstaende;
//
//        try {
//            Query qu = s.createQuery("from Gegenstand order by gegKurzbez");
//            gegenstaende = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return gegenstaende;
//    }
//
//    @Override
//    public List<Lehrer> getLehrer() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Lehrer> lehrer;
//
//        try {
//            Query qu = s.createQuery("from Lehrer");
//            lehrer = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return lehrer;
//    }
//
//    @Override
//    public List<News> getNews() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<News> news;
//
//        try {
//            Query qu = s.createQuery("from News");
//            news = qu.list();
//            news.sort((a, b) -> b.getNewsTimestamp().compareTo(a.getNewsTimestamp()));
//        } finally {
//            s.close();
//        }
//
//        return news;
//    }
//
//    @Override
//    public List<Semesternote> getSemesternoten() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Semesternote> semesternoten;
//
//        try {
//            Query qu = s.createQuery("from Semesternote");
//            semesternoten = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return semesternoten;
//    }
//
//    @Override
//    public List<Semesternote> getSemesternotenFromSchueler(Schueler schueler, int semester) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Semesternote> semesternoten = new ArrayList<>();
//
//        try {
//            if (schueler == null) {
//                return semesternoten;
//            }
//
//            Query qu = s.createQuery("from Semesternote s "
//                    + "where s.schueler.ssdId = :ssdId");
//            qu.setParameter("ssdId", schueler.getSsdId());
//            semesternoten = ((List<Semesternote>) qu.list()).stream()
//                    .filter(sem -> sem.getSemSemester() == semester)
//                    .sorted((a, b) -> a.getSemId() - b.getSemId())
//                    .collect(Collectors.toList());
//        } finally {
//            s.close();
//        }
//
//        return semesternoten;
//    }
//
//    @Override
//    public List<News> getNews(int anzahl) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<News> news;
//
//        try {
//            if (anzahl < 0) {
//                throw new IllegalArgumentException("Anzahl darf nicht negativ sein!");
//            }
//
//            Query qu = s.createQuery("from News");
//            news = qu.list();
//            news = news.stream()
//                    .sorted((a, b) -> b.getNewsTimestamp().compareTo(a.getNewsTimestamp()))
//                    .limit(anzahl)
//                    .collect(Collectors.toList());
//        } finally {
//            s.close();
//        }
//
//        return news;
//    }
//
//    @Override
//    public List<Schueler> getSchuelerFromKlasse(Klasse klasse) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Schueler> schueler = new ArrayList<>();
//
//        try {
//            if (klasse == null) {
//                return schueler;
//            }
//
//            Query qu = s.createQuery("from Schueler s "
//                    + "where s.klasse.klaBez = :kla");
//            qu.setParameter("kla", klasse.getKlaBez());
//            schueler = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return schueler;
//    }
//
//    @Override
//    public Lehrer getLehrer(String lehrerKb) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Lehrer lehrer = null;
//
//        try {
//            String abfrage = "from Lehrer l where l.lehrerKb = :lehrerKb";
//            Query qu = s.createQuery(abfrage).setParameter("lehrerKb", lehrerKb.toUpperCase());
//            lehrer = (Lehrer) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//
//        return lehrer;
//    }
//
//    @Override
//    public Schueler getSchuelerById(int ssdId) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Schueler schueler;
//
//        try {
//            Query qu = s.createQuery("from Schueler where ssdId = :id");
//            qu.setInteger("id", ssdId);
//            schueler = (Schueler) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//
//        return schueler;
//    }
//
//    // Liefert alle Klassen welche von diesem Lehrer unterrichet werden
//    @Override
//    public List<Klasse> getKlassen(Lehrer lehrer) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Klasse> klassen = new ArrayList<>();
//
//        try {
//            if (lehrer == null) {
//                return klassen;
//            }
//
//            Query qu = s.createQuery("select distinct klasse "
//                    + "from Lehrfach l "
//                    + "where l.lehrer.lehrerKb = :l");
//            qu.setParameter("l", lehrer.getLehrerKb());
//            klassen = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return klassen;
//    }
//
//    //Gesetzkategorien
//    @Override
//    public List<Gesetzkategorie> getGesetzkategorien() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        String sql = "from Gesetzkategorie";
//        List<Gesetzkategorie> gesetzeskategorien;
//
//        try {
//            Query q = s.createQuery(sql);
//            gesetzeskategorien = q.list();
//            return gesetzeskategorien;
//        } catch (HibernateException ex) {
//            System.out.println("Fehler bei getGesetzkategorien():" + ex.getLocalizedMessage());
//            return null;
//        } finally {
//            s.close();
//        }
//    }
//
//    //Gegenstände vom Lehrer
//    //fertig
//    @Override
//    public List<Gegenstand> getGegenstaende(Lehrer lehrer) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Gegenstand> gegenstaende = new ArrayList<>();
//
//        try {
//            if (lehrer == null) {
//                return gegenstaende;
//            }
//
//            Query qu = s.createQuery("select distinct gegenstand "
//                    + "from Lehrfach l "
//                    + "where l.lehrer.lehrerKb = :l");
//            qu.setString("l", lehrer.getLehrerKb());
//            gegenstaende = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return gegenstaende;
//    }
//
//    //fertig
//    @Override
//    public boolean isLehrer(String username) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        boolean isLehr = false;
//        Lehrer lehrer;
//
//        try {
//            Query qu = s.createQuery("from Lehrer l where l.lehrerKb=:user");
//            qu.setString("user", username);
//            lehrer = (Lehrer) qu.uniqueResult();
//            if (lehrer != null) {
//                isLehr = true;
//            }
//        } finally {
//            s.close();
//        }
//
//        return isLehr;
//
//    }
//
//    //fertig
//    @Override
//    public Gesetzkategorie getGesetzkategorie(String gesKurzbez) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Gesetzkategorie gesKat;
//
//        try {
//            Query qu = s.createQuery("from Gesetzkategorie g where g.gesKurzbez=:gk");
//            qu.setString("gk", gesKurzbez);
//            gesKat = (Gesetzkategorie) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//
//        return gesKat;
//    }
//
//    @Override
//    public List<Mahnung> getMahnungenForGruppe(Gruppe gruppe, int semester) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Mahnung> mahnungen = new ArrayList<>();
//
//        try {
//            if (gruppe == null || gruppe.getGegenstand() == null) {
//                return mahnungen;
//            }
//
//            Query qu = s.createQuery("select g.mahnungen from Gegenstand g "
//                    + "where g.gegKurzbez = :grpGegBez");
//            qu.setParameter("grpGegBez", gruppe.getGegenstand().getGegKurzbez());
//            mahnungen = qu.list();
//
//            s.refresh(gruppe);
//            Hibernate.initialize(gruppe.getSchueler());
//            // im aktuellen Schuljahr + Semester
//            mahnungen = mahnungen.stream()
//                    .filter(m -> isDatumInSemester(m.getMahnAusgegebenTimestamp(), semester)
//                    && gruppe.getSchueler().contains(m.getSchueler()))
//                    .collect(Collectors.toList());
//        } finally {
//            s.close();
//        }
//
//        return mahnungen;
//    }
//
//    @Override
//    public List<Schueler> getSchuelerFromGruppe(Gruppe gruppe) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Schueler> schueler = new ArrayList<>();
//
//        try {
//            if (gruppe == null) {
//                return schueler;
//            }
//
//            Query qu = s.createQuery("select schueler "
//                    + "from Gruppe g "
//                    + "where g.grpId = :grp");
//            qu.setParameter("grp", gruppe.getGrpId());
//            schueler = qu.list();
//            Collections.sort(schueler, (a, b)
//                    -> ((Integer) a.getSsdKatnr()).compareTo(b.getSsdKatnr()));
//        } finally {
//            s.close();
//        }
//
//        return schueler;
//    }
//
//    @Override
//    public List<LaufendeNote> getLaufendeNotenFromSchueler(Schueler schueler, Gegenstand gegenstand) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<LaufendeNote> laufendeNoten = new ArrayList<>();
//
//        try {
//            if (schueler == null || gegenstand == null) {
//                return laufendeNoten;
//            }
//
//            Query qu = s.createQuery("from LaufendeNote l "
//                    + "where l.gegenstand.gegKurzbez = :geg "
//                    + "and l.schueler.ssdId = :sch");
//            qu.setInteger("sch", schueler.getSsdId());
//            qu.setParameter("geg", gegenstand.getGegKurzbez());
//
//            laufendeNoten = qu.list();
//            // TODO : DATUMSEINSCHRÄNKUNG
//            /*Date benotungsBeginn = getBenotungsBeginn();
//            Date benotungsEnde = getBenotungsEnde();
//
//            laufendeNoten = laufendeNoten.stream()
//                    .filter(l -> isDatumInSemester(l.getLaufTimestamp(), SOMMER_UND_WINTER_SEMESTER))
//                    .collect(Collectors.toList());*/
//        } finally {
//            s.close();
//        }
//
//        return laufendeNoten;
//    }
//
//    @Override
//    public List<Kategorie> getKategorienFromLehrer(Lehrer lehrer) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Kategorie> kategorien = new ArrayList<>();
//
//        try {
//            if (lehrer == null) {
//                return kategorien;
//            }
//
//            Query qu = s.createQuery("from Kategorie k "
//                    + "where k.lehrer = :lehrer");
//            qu.setParameter("lehrer", lehrer);
//            kategorien = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return kategorien;
//    }
//
//    @Override
//    public List<Kategorie> getOeffentlicheKategorien() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Kategorie> kategorie;
//
//        try {
//            Query qu = s.createQuery("from Kategorie k where k.katOeffentlichflag = true");
//            kategorie = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return kategorie;
//    }
//
//    @Override
//    public Kategorie getKategorie(int katId) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Kategorie kategorie;
//
//        try {
//            Query qu = s.createQuery("from Kategorie k where k.katId = :id");
//            qu.setInteger("id", katId);
//            kategorie = (Kategorie) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//
//        return kategorie;
//    }
//
//    @Override
//    public List<Gruppe> getGruppenFromLehrer(Lehrer lehrer) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Gruppe> gruppen = new ArrayList<>();
//
//        try {
//            if (lehrer == null) {
//                return gruppen;
//            }
//
//            Query qu = s.createQuery("select gruppe from Berechtigung b "
//                    + "where b.lehrer.lehrerKb = :l");
//            qu.setParameter("l", lehrer.getLehrerKb());
//            gruppen = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return gruppen;
//    }
//
//    @Override
//    public List<Lehrer> getLehrer(Gegenstand gegenstand, Klasse... klassen) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Lehrer> lehrer = new ArrayList<>();
//
//        try {
//            if (klassen == null || gegenstand == null) {
//                return lehrer;
//            }
//
//            Query qu = s.createQuery("select distinct lehrer from Lehrfach l "
//                    + "where l.gegenstand.gegKurzbez = :g "
//                    + "and l.klasse in (:k)");
//            qu.setParameter("g", gegenstand.getGegKurzbez());
//            qu.setParameterList("k", klassen);
//            lehrer = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return lehrer;
//    }
//
//    @Override
//    public Serializable importKategorie(Lehrer lehrer, Kategorie kategorie) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Serializable id = null;
//        Transaction tx = null;
//
//        try {
//            if (lehrer == null || kategorie == null) {
//                return null;
//            }
//
//            tx = s.beginTransaction();
//            Lehrer l = (Lehrer) s.load(Lehrer.class,
//                    lehrer.getLehrerKb());
//            l.getKategorien().add(kategorie);
//            Kategorie katNeu = new Kategorie(kategorie);
//            katNeu.setLehrer(lehrer);
//            katNeu.setKatOeffentlichflag(false);
//            id = addKategorie(katNeu);
//            s.save(l);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//            id = null;
//        } finally {
//            s.close();
//        }
//
//        return id;
//    }
//
//    @Override
//    public List<Kompetenzbereich> getKompetenzbereicheFromGegenstand(Gegenstand gegenstand) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Kompetenzbereich> kompetenzbereiche = new ArrayList<>();
//
//        try {
//            if (gegenstand == null) {
//                return kompetenzbereiche;
//            }
//
//            Query qu = s.createQuery("select g.kompetenzbereiche "
//                    + "from Gegenstand g "
//                    + "where g.gegKurzbez = :geg");
//            qu.setParameter("geg", gegenstand.getGegKurzbez());
//            kompetenzbereiche = qu.list();
//        } finally {
//            s.close();
//        }
//
//        return kompetenzbereiche;
//    }
//
//    @Override
//    public Gegenstand getGegenstandByKurzBez(String kurzBez) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Gegenstand gegenstand = null;
//
//        try {
//            if (kurzBez == null) {
//                return gegenstand;
//            }
//
//            Query qu = s.createQuery("from Gegenstand g "
//                    + "where g.gegKurzbez = :geg");
//            qu.setParameter("geg", kurzBez);
//            gegenstand = (Gegenstand) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//        return gegenstand;
//    }
//
//    @Override
//    public Lehrer getMasterlehrerFromGruppe(Gruppe gruppe) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Lehrer lehrer = null;
//
//        try {
//            if (gruppe == null) {
//                return lehrer;
//            }
//
//            Query qu = s.createQuery("select b.lehrer "
//                    + "from Berechtigung b "
//                    + "where b.gruppe.grpId = :grp "
//                    + "and b.masterlehrer = true");
//            qu.setParameter("grp", gruppe.getGrpId());
//            lehrer = (Lehrer) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//        return lehrer;
//    }
//
//    @Override
//    public List<Gegenstand> getGegenstaendeFromSchueler(Schueler schueler) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Gegenstand> gegenstaende = new ArrayList<>();
//
//        try {
//            if (schueler == null) {
//                return gegenstaende;
//            }
//
//            s.refresh(schueler);
//            Set<Gruppe> gruppenSet = schueler.getGruppen();
//            Hibernate.initialize(gruppenSet);
//
//            if (gruppenSet.isEmpty()) {
//                return gegenstaende;
//            }
//            Query qu1 = s.createQuery("select g.grpId "
//                    + "from Gruppe g "
//                    + "where g.grpId in (:sGrp)");
//
//            List<Integer> ids = gruppenSet.stream()
//                    .map(sch -> sch.getGrpId())
//                    .collect(Collectors.toList());
//
//            qu1.setParameterList("sGrp", ids);
//            List<Integer> grpIds = qu1.list();
//
//            Query qu2 = s.createQuery("select g.gegenstand "
//                    + "from Gruppe g "
//                    + "where g.grpId in (:grpIds)");
//            qu2.setParameterList("grpIds", grpIds);
//            gegenstaende = qu2.list();
//        } finally {
//            s.close();
//        }
//        return gegenstaende;
//    }
//
//    @Override
//    public Serializable addKategorie(Kategorie kategorie) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        Serializable id = null;
//
//        try {
//            if (kategorie == null) {
//                return null;
//            }
//
//            tx = s.beginTransaction();
//            id = s.save(kategorie);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return id;
//    }
//
//    @Override
//    public boolean editKategorie(Kategorie kategorie) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (kategorie == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            s.update(kategorie);
//            tx.commit();
//            success = true;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    @Override
//    public boolean removeKategorie(Kategorie kategorie) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        if (kategorie.getLaufendeNoten().isEmpty()) {
//            try {
//                if (kategorie == null) {
//                    return false;
//                }
//                s.refresh(kategorie);
//                Hibernate.initialize(kategorie.getLaufendeNoten());
//
//                tx = s.beginTransaction();
//                s.delete(kategorie);
//                tx.commit();
//                success = true;
//            } catch (HibernateException e) {
//                if (tx != null) {
//                    tx.rollback();
//                }
//                e.printStackTrace();
//            } finally {
//                s.close();
//
//            }
////            return success;
//        }
//
////        try {
////            tx = s.beginTransaction();
////            s.delete(kategorie);
////            tx.commit();
////            success = true;
////        } catch (HibernateException e) {
////            if (tx != null) {
////                tx.rollback();
////            }
////            e.printStackTrace();
////        } finally {
////            s.close();
////        }
//        return success;
//    }
//
//    @Override
//    public Serializable addGruppe(Gruppe gruppe) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        Serializable id = null;
//
//        try {
//            if (gruppe == null) {
//                return null;
//            }
//
//            tx = s.beginTransaction();
//            id = s.save(gruppe);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return id;
//    }
//
//    @Override
//    public boolean editGruppe(Gruppe gruppe) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (gruppe == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            s.update(gruppe);
//            tx.commit();
//            success = true;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    @Override
//    public boolean removeGruppe(Gruppe gruppe) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (gruppe == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            removeGruppeFromSchueler(gruppe);
//            removeGruppeFromBerechtigung(gruppe);
//            s.delete(gruppe);
//            tx.commit();
//            success = true;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    public boolean removeGruppeFromSchueler(Gruppe gruppe) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (gruppe == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            Query qu = s.createSQLQuery("delete from ssd_mitglied_grp "
//                    + "where grp_id = :id");
//            qu.setParameter("id", gruppe.getGrpId());
//            boolean stat = qu.executeUpdate() > 0;
//            tx.commit();
//            success = stat;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    public boolean removeGruppeFromBerechtigung(Gruppe gruppe) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (gruppe == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            Query qu = s.createSQLQuery("delete from berechtigung "
//                    + "where grp_id = :id");
//            qu.setParameter("id", gruppe.getGrpId());
//            boolean stat = qu.executeUpdate() > 0;
//            tx.commit();
//            success = stat;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    @Override
//    public Serializable addBerechtigung(Berechtigung berechtigung) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        Serializable id = null;
//
//        try {
//            if (berechtigung == null) {
//                return null;
//            }
//
//            BerechtigungId berechtigungId = new BerechtigungId(berechtigung.getLehrer().getLehrerKb(), berechtigung.getGruppe().getGrpId());
//            berechtigung.setId(berechtigungId);
//
//            tx = s.beginTransaction();
//            id = s.save(berechtigung);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return id;
//    }
//
//    @Override
//    public boolean editBerechtigung(Berechtigung berechtigung) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (berechtigung == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            s.update(berechtigung);
//            tx.commit();
//            success = true;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    @Override
//    public boolean removeBerechtigung(Berechtigung berechtigung) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (berechtigung == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            s.delete(berechtigung);
//            tx.commit();
//            success = true;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    @Override
//    public List<Klasse> getKlassen(Lehrer lehrer, Gegenstand gegenstand) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Klasse> klassen = new ArrayList<>();
//
//        try {
//            if (lehrer == null || gegenstand == null) {
//                return klassen;
//            }
//
//            Query qu = s.createQuery("select distinct klasse "
//                    + "from Lehrfach l "
//                    + "where l.lehrer.lehrerKb = :l "
//                    + "and l.gegenstand.gegKurzbez = :g");
//            qu.setParameter("l", lehrer.getLehrerKb());
//            qu.setParameter("g", gegenstand.getGegKurzbez());
//            klassen = qu.list();
//        } finally {
//            s.close();
//        }
//        return klassen;
//    }
//
//    @Override
//    public Date getKonfDatumFromSemester(int semester) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Date date = null;
//        String sql = null;
//
//        try {
//            if (semester == 1 || semester == 2) {
//                sql = "select dirKonfdatumsem" + semester + " "
//                        + "from Direktionsdaten";
//            } else {
//                throw new IllegalArgumentException("Semester \"" + semester + "\" existiert nicht !");
//            }
//            Query qu = s.createQuery(sql);
//            date = (Date) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//        return date;
//    }
//
//    @Override
//    public Date getBenotungsBeginn() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Date date = null;
//
//        try {
//            Query qu = s.createQuery("select d.dirSchulbeginn from Direktionsdaten d");
//            date = (Date) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//        return date;
//    }
//
//    public Date getZeugnisDatumSem1() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Date date = null;
//
//        try {
//            Query qu = s.createQuery("select d.dirZeugnisdatumsem1 from Direktionsdaten d");
//            date = (Date) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//        return date;
//    }
//
//    @Override
//    public Date getBenotungsEnde() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Date date = null;
//
//        try {
//            Query qu = s.createQuery("select d.dirZeugnisdatumsem2 from Direktionsdaten d");
//            date = (Date) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//        return date;
//    }
//
//    @Override
//    public List<LaufendeNote> getLaufendeNotenFromSchueler(Schueler schueler) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<LaufendeNote> laufendeNoten = new ArrayList<>();
//
//        try {
//            if (schueler == null) {
//                return laufendeNoten;
//            }
//
//            Query qu = s.createQuery("from LaufendeNote l "
//                    + "where l.schueler.ssdId = :sch");
//            qu.setInteger("sch", schueler.getSsdId());
//            laufendeNoten = qu.list();
//        } finally {
//            s.close();
//        }
//        return laufendeNoten;
//    }
//
//    @Override
//    public List<LaufendeNote> getLaufendeNotenFromSchueler(Schueler schueler, Gegenstand gegenstand, int semester) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<LaufendeNote> laufendeNoten = new ArrayList<>();
//
//        try {
//            if (schueler == null || gegenstand == null) {
//                return laufendeNoten;
//            }
//
//            Query qu = s.createQuery("from LaufendeNote l "
//                    + "where l.gegenstand.gegKurzbez = :geg "
//                    + "and l.schueler.ssdId = :sch");
//            qu.setInteger("sch", schueler.getSsdId());
//            qu.setParameter("geg", gegenstand.getGegKurzbez());
//
//            laufendeNoten = qu.list();
//
//            // im aktuellen Schuljahr + Semester
//            laufendeNoten = laufendeNoten.stream()
//                    .filter(l -> isDatumInSemester(l.getLaufTimestamp(), semester))
//                    .collect(Collectors.toList());
//        } finally {
//            s.close();
//        }
//        return laufendeNoten;
//    }
//
//    @Override
//    public Set<Gruppe> getGruppenFromSchueler(Schueler schueler) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//
//        try {
//            s.refresh(schueler);
//            Hibernate.initialize(schueler.getGruppen());
//            return schueler.getGruppen();
//        } finally {
//            s.close();
//        }
//    }
//
//    @Override
//    public List<Berechtigung> getBerechtigungenFromGruppe(Gruppe gruppe) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Berechtigung> berechtigungen = new ArrayList<>();
//
//        try {
//            if (gruppe == null) {
//                return berechtigungen;
//            }
//
//            Query qu = s.createQuery("from Berechtigung b "
//                    + "where b.gruppe.grpId = :id");
//            qu.setParameter("id", gruppe.getGrpId());
//            berechtigungen = qu.list();
//        } finally {
//            s.close();
//        }
//        return berechtigungen;
//    }
//
//    @Override
//    public List<Mahnung> getMahnung(Schueler schueler, Lehrer lehrer, Gegenstand gegenstand, int semester) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Mahnung> mahnungen = new ArrayList<>();
//
//        try {
//            if (schueler == null || gegenstand == null || lehrer == null) {
//                return mahnungen;
//            }
//
//            s.refresh(schueler);
//            s.refresh(lehrer);
//            s.refresh(gegenstand);
//            Set<Mahnung> mahnSchueler = schueler.getMahnungen();
//            Set<Mahnung> mahnLehrer = lehrer.getMahnungen();
//            Set<Mahnung> mahnGegenstand = gegenstand.getMahnungen();
//            Hibernate.initialize(mahnSchueler);
//            Hibernate.initialize(mahnLehrer);
//            Hibernate.initialize(mahnGegenstand);
//
//            if (mahnSchueler.isEmpty() || mahnLehrer.isEmpty() || mahnGegenstand.isEmpty()) {
//                return mahnungen;
//            }
//
//            Query qu = s.createQuery("from Mahnung m "
//                    + "where m.mahnId in (:sch) "
//                    + "and m.mahnId in (:geg) "
//                    + "and m.mahnId in (:lehrer)");
//            qu.setParameterList("sch", mahnSchueler
//                    .stream()
//                    .map(x -> x.getMahnId())
//                    .collect(Collectors.toList()));
//
//            qu.setParameterList("geg", mahnGegenstand
//                    .stream()
//                    .map(x -> x.getMahnId())
//                    .collect(Collectors.toList()));
//
//            qu.setParameterList("lehrer", mahnLehrer
//                    .stream()
//                    .map(x -> x.getMahnId())
//                    .collect(Collectors.toList()));
//
//            mahnungen = ((List<Mahnung>) qu.list()).stream()
//                    .filter(m
//                            -> isDatumInSemester(m.getMahnAusgegebenTimestamp(),
//                            semester))
//                    .sorted((a, b) -> a.getMahnAusgegebenTimestamp()
//                    .compareTo(b.getMahnAusgegebenTimestamp()))
//                    .collect(Collectors.toList());
//
//        } finally {
//            s.close();
//        }
//        return mahnungen;
//    }
//
//    @Override
//    public List<Kompetenzbereich> getKompetenzbereicheFromGegenstand(Gegenstand gegenstand, int schulstufe) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Kompetenzbereich> kompetenzbereiche = new ArrayList<>();
//
//        try {
//            if (gegenstand == null || schulstufe >= 1 && schulstufe <= 5) {
//                return kompetenzbereiche;
//            }
//
//            kompetenzbereiche = getKompetenzbereicheFromGegenstand(gegenstand);
//            kompetenzbereiche = kompetenzbereiche.stream()
//                    .filter(k -> k.getKompJahrgang() == schulstufe)
//                    .collect(Collectors.toList());
//        } finally {
//            s.close();
//        }
//        return kompetenzbereiche;
//    }
//
//    @Override
//    public Serializable addMahnung(Mahnung mahnung) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        Serializable id = null;
//
//        try {
//            if (mahnung == null) {
//                return null;
//            }
//
//            tx = s.beginTransaction();
//            id = s.save(mahnung);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return id;
//    }
//
//    @Override
//    public boolean editMahnung(Mahnung mahnung) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (mahnung == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            s.update(mahnung);
//            tx.commit();
//            success = true;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    @Override
//    public Serializable addLehrer(Lehrer lehrer) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        Serializable id = null;
//
//        try {
//            if (lehrer == null) {
//                return null;
//            }
//
//            tx = s.beginTransaction();
//            id = s.save(lehrer);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return id;
//    }
//
//    @Override
//    public boolean editLehrer(Lehrer lehrer) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (lehrer == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            s.update(lehrer);
//            tx.commit();
//            success = true;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    @Override
//    public List<Kategorie> getKategorienFromGegenenstandAndLehrer(Gegenstand gegenstand, Lehrer lehrer) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Kategorie> kategorien = new ArrayList<>();
//
//        try {
//            if (lehrer == null || gegenstand == null) {
//                return kategorien;
//            }
//
//            Query qu = s.createQuery("from Kategorie k "
//                    + "where k.lehrer = :lehrer");
//            qu.setParameter("lehrer", lehrer);
//            kategorien = qu.list();
//            kategorien = kategorien.stream()
//                    .filter(k -> k.getGegenstaende().contains(gegenstand))
//                    .collect(Collectors.toList());
//        } finally {
//            s.close();
//        }
//        return kategorien;
//    }
//
//    @Override
//    public Lehrer getDirektor() {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Kategorie> kategorien = new ArrayList<>();
//        Lehrer lehrer = null;
//
//        try {
//            Query qu = s.createQuery("from Direktionsdaten");
//            Direktionsdaten dir = (Direktionsdaten) qu.uniqueResult();
//            lehrer = dir.getLehrer();
//        } finally {
//            s.close();
//        }
//        return lehrer;
//    }
//
//    @Override
//    public String getSemesterBezeichnung(int semester) {
//        GregorianCalendar now = new GregorianCalendar();
//        int jahr = now.get(Calendar.YEAR);
//
//        switch (semester) {
//            case VORIGES_SOMMER_SEMESTER:
//                return "Sommersemester " + (jahr - 1);
//            case WINTER_SEMESTER:
//                return "Wintersemester " + jahr;
//            case SOMMER_SEMESTER:
//                return "Sommersemester " + jahr;
//            default:
//                throw new IllegalArgumentException("Semester \"" + semester + "\" existiert nicht!");
//        }
//    }
//
//    public boolean isDatumInSemester(Date datum, int semester) {
//        Date zeugnisDatum = getZeugnisDatumSem1();
//
//        switch (semester) {
//            case WINTER_SEMESTER:
//                return datum.before(zeugnisDatum) && datum.after(getBenotungsBeginn());
//
//            case SOMMER_SEMESTER:
//                return datum.after(zeugnisDatum) && datum.before(getBenotungsEnde());
//
//            case SOMMER_UND_WINTER_SEMESTER:
//                return datum.after(getBenotungsBeginn()) && datum.before(getBenotungsEnde());
//
//            case VORIGES_SOMMER_SEMESTER:
//                GregorianCalendar vorjahrEnde = new GregorianCalendar();
//                vorjahrEnde.setTime(getBenotungsEnde());
//                vorjahrEnde.add(GregorianCalendar.YEAR, -1);
//
//                GregorianCalendar vorjahrKonfDatum = new GregorianCalendar();
//                vorjahrKonfDatum.setTime(zeugnisDatum);
//                vorjahrKonfDatum.add(GregorianCalendar.YEAR, -1);
//
//                return datum.before(vorjahrEnde.getTime()) && datum.after(vorjahrKonfDatum.getTime());
//
//            default:
//                throw new IllegalArgumentException("Semester \"" + semester + "\" existiert nicht!");
//        }
//
//    }
//
//    @Override
//    public Klasse getStammklasseFromLehrer(Lehrer lehrer) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Klasse stammklasse;
//
//        try {
//            if (lehrer == null) {
//                return null;
//            }
//
//            Query qu = s.createQuery("from Klasse k "
//                    + "where k.lehrer.lehrerKb = :lehrer");
//
//            qu.setParameter("lehrer", lehrer.getLehrerKb());
//
//            stammklasse = (Klasse) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//        return stammklasse;
//    }
//
//    @Override
//    public boolean removeLehrerFromGruppe(Lehrer lehrer, Gruppe gruppe) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (lehrer == null || gruppe == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            Query qu = s.createQuery("delete from Berechtigung b "
//                    + "where b.gruppe.grpId = :grp "
//                    + "and b.lehrer.lehrerKb = :lehrer");
//
//            qu.setParameter("grp", gruppe.getGrpId());
//            qu.setParameter("lehrer", lehrer.getLehrerKb());
//
//            boolean stat = qu.executeUpdate() > 0;
//            tx.commit();
//            success = stat;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    @Override
//    public int getAktuellesSemester() {
//        Date zeugnisDatum = getZeugnisDatumSem1();
//        return new Date().before(zeugnisDatum) ? WINTER_SEMESTER : SOMMER_SEMESTER;
//    }
//
//    @Override
//    public Serializable addSprechstunde(Sprechstunde sprechstunde) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        Serializable id = null;
//
//        try {
//            if (sprechstunde == null) {
//                return null;
//            }
//
//            tx = s.beginTransaction();
//            id = s.save(sprechstunde);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return id;
//    }
//
//    @Override
//    public Serializable editSprechstunde(Sprechstunde sprechstunde) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//
//        try {
//            if (sprechstunde == null) {
//                return null;
//            }
//
//            tx = s.beginTransaction();
//            s.update(sprechstunde);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return sprechstunde;
//    }
//
//    @Override
//    public boolean removeSprechstunde(Sprechstunde sprechstunde) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (sprechstunde == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            s.delete(sprechstunde);
//            tx.commit();
//            success = true;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//
//        }
//        return success;
//    }
//
//    @Override
//    public List<Kompetenzbereich> getKompetenzbereicheFromLaufendeNote(LaufendeNote laufendeNote) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Kompetenzbereich> kompetenzbereiche = new ArrayList<>();
//
//        try {
//            if (laufendeNote == null) {
//                return kompetenzbereiche;
//            }
//
//            Query qu = s.createQuery("select b.kompetenzbereich "
//                    + "from Bewertung b "
//                    + "where b.laufendeNote.laufId = :lnId");
//            qu.setParameter("lnId", laufendeNote.getLaufId());
//            kompetenzbereiche = qu.list();
//        } finally {
//            s.close();
//        }
//        return kompetenzbereiche;
//    }
//
//    @Override
//    public List<LaufendeNote> getUngeleseneNoten(Schueler schueler) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<LaufendeNote> noten = new ArrayList<>();
//
//        try {
//            if (schueler == null) {
//                return noten;
//            }
//
//            s.refresh(schueler);
//            noten = new ArrayList<>(schueler.getLaufendeNoten());
//            Hibernate.initialize(noten);
//            noten = noten.stream()
//                    .filter(ln -> !ln.isLaufGelesen())
//                    .collect(Collectors.toList());
//        } finally {
//            s.close();
//        }
//        return noten;
//    }
//
//    @Override
//    public boolean setNotenGelesen(List<LaufendeNote> noten) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        boolean success = false;
//
//        try {
//            if (noten == null || noten.isEmpty()) {
//                return false;
//            }
//
//            for (LaufendeNote laufendeNote : noten) {
//                s.refresh(laufendeNote);
//                laufendeNote.setLaufGelesen(true);
//                s.update(laufendeNote);
//            }
//            success = true;
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    @Override
//    public boolean removeMahnung(Mahnung mahnung) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (mahnung == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            s.delete(mahnung);
//            tx.commit();
//            success = true;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    @Override
//    public boolean removeLaufendeNote(LaufendeNote laufendeNote) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (laufendeNote == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//
//            // Bewertungen zuerst löschen
//            s.refresh(laufendeNote);
//            Set<Bewertung> bewertungen = laufendeNote.getBewertungen();
//            Hibernate.initialize(bewertungen);
//            bewertungen.stream()
//                    .forEach(s::delete);
//
//            s.delete(laufendeNote);
//            tx.commit();
//            success = true;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//
//        }
//        return success;
//    }
//
//    @Override
//    public List<Lehrfach> getLehrfaecherFromLehrer(Lehrer lehrer) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        List<Lehrfach> lehrfaecher = new ArrayList<>();
//
//        try {
//            if (lehrer == null) {
//                return lehrfaecher;
//            }
//
//            Query qu = s.createQuery("from Lehrfach l "
//                    + "where l.lehrer.lehrerKb = :id");
//            qu.setParameter("id", lehrer.getLehrerKb());
//            lehrfaecher = qu.list();
//        } finally {
//            s.close();
//        }
//        return lehrfaecher;
//    }
//
//    @Override
//    public Kompetenzbereich getKompetenzbereichById(int kompId) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Kompetenzbereich kompetenzbereich;
//
//        try {
//            Query qu = s.createQuery("from Kompetenzbereich k "
//                    + "where k.kompId = :id");
//            qu.setParameter("id", kompId);
//            kompetenzbereich = (Kompetenzbereich) qu.uniqueResult();
//        } finally {
//            s.close();
//        }
//        return kompetenzbereich;
//    }
//
//    @Override
//    public Integer addLaufendeNote(LaufendeNote laufendeNote) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        Serializable id = null;
//
//        try {
//            if (laufendeNote == null) {
//                return null;
//            }
//
//            tx = s.beginTransaction();
//            id = s.save(laufendeNote);
//            laufendeNote.setLaufId((int) id);
//            System.out.println("ID: " + id);
//            Set<Bewertung> bewertungen = laufendeNote.getBewertungen();
//            bewertungen.stream()
//                    .forEach(b -> {
//                        b.setId(new BewertungId(laufendeNote.getLaufId(), b.getKompetenzbereich().getKompId()));
//                        s.save(b);
//                    });
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return (Integer) id;
//    }
//
//    @Override
//    public boolean editLaufendeNote(LaufendeNote laufendeNote) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        boolean success = false;
//
//        try {
//            if (laufendeNote == null) {
//                return false;
//            }
//
//            tx = s.beginTransaction();
//            s.update(laufendeNote);
//            tx.commit();
//            success = true;
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return success;
//    }
//
//    @Override
//    public Map<Gegenstand, List<Kompetenzbereich>> getAusstehendePruefungen(Schueler schueler, int semester) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Map<Gegenstand, List<Kompetenzbereich>> pruefungen = new HashMap<>();
//
//        try {
//            if (schueler == null) {
//                return pruefungen;
//            }
//
//            s.refresh(schueler);
//            Hibernate.initialize(schueler.getPruefungen());
//
//            schueler.getPruefungen().stream()
//                    .filter(p -> isDatumInSemester(p.getPruefDatum(), semester)
//                    || !p.getPruefAbsolviert())
//                    .forEach(p -> {
//                        List<Kompetenzbereich> kompetenzbereiche
//                                = new ArrayList<>(p.getKompetenzbereiche());
//
//                        if (!kompetenzbereiche.isEmpty()) {
//                            Query qu = s.createQuery("select k.gegenstand "
//                                    + "from Kompetenzbereich k "
//                                    + "where k.kompId in (:ids)");
//                            qu.setParameterList("ids",
//                                    kompetenzbereiche);
//                            List<Gegenstand> gegenstaende = qu.list();
//                            gegenstaende.stream()
//                                    .forEach(g -> pruefungen.put(g, kompetenzbereiche));
//                        }
//                    });
//        } finally {
//            s.close();
//        }
//        return pruefungen;
//    }
//
//    @Override
//    public Map<Schueler, Integer> getLaufendeNotenAnzahlFromSchueler(List<Schueler> schueler, Gegenstand gegenstand) {
//        Session s = HibernateUtil.getSessionFactory().openSession();
//        Map<Schueler, Integer> anzahlLaufendeNoten = new HashMap<>();
//
//        try {
//            if (schueler == null || gegenstand == null) {
//                return anzahlLaufendeNoten;
//            }
//
//            List<Integer> listschueler = new ArrayList<>();
//            for (Schueler sch : schueler) {
//                listschueler.add(sch.getSsdId());
//            }
//            Query qu = s.createQuery("from LaufendeNote l "
//                    + "where l.gegenstand.gegKurzbez = :geg "
//                    + "and l.schueler.ssdId in (:sch)");
//            qu.setParameterList("sch", listschueler);
//            qu.setParameter("geg", gegenstand.getGegKurzbez());
//
//            List<LaufendeNote> laufendeNote = qu.list();
//            for (LaufendeNote ln : laufendeNote) {
//                if (anzahlLaufendeNoten.containsKey(ln.getSchueler())) {
//                    int anzahl = anzahlLaufendeNoten.get(ln.getSchueler());
//                    anzahl++;
//                    anzahlLaufendeNoten.put(ln.getSchueler(), anzahl);
//                } else {
//                    anzahlLaufendeNoten.put(ln.getSchueler(), 1);
//                }
//
//            }
//        } catch (HibernateException e) {
//            e.printStackTrace();
//        } finally {
//            s.close();
//        }
//        return anzahlLaufendeNoten;
//    }
//}
