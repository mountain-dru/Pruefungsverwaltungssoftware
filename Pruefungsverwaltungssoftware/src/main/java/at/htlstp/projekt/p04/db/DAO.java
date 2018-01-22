/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.db;

import at.htlstp.projekt.p04.model.Gegenstand;
import at.htlstp.projekt.p04.model.Gruppe;
import at.htlstp.projekt.p04.model.Klasse;
import at.htlstp.projekt.p04.model.Lehrer;
import at.htlstp.projekt.p04.model.PraPruefung;
import at.htlstp.projekt.p04.model.Schueler;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 *
 * @author Dru
 */
public class DAO implements IDAO, AutoCloseable {

    private static DAO daoInstance;

    private DAO() {
    }

    public static DAO getDaoInstance() {
        if (daoInstance == null) {
            synchronized (DAO.class) {      //Threadsicher, nur bei der Erzeugung 
                daoInstance = new DAO();
            }
        }
        return daoInstance;
    }

    @Override
    public List<Schueler> getSchuelerByGruppe(Gruppe gruppe) {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            gruppe = em.find(Gruppe.class, gruppe.getGrpId());
            if (gruppe != null) {
                TypedQuery<Schueler> query = em.createQuery("select DISTINCT(sch) from Schueler sch where :ge member of sch.gruppen", Schueler.class)
                        .setParameter("ge", gruppe);
                return new ArrayList<>(query.getResultList());
            }
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Schueler> getSchuelerByKlasse(Klasse klasse) {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            klasse = em.find(Klasse.class, klasse.getKlaBez());
            if (klasse != null) {
                TypedQuery<Schueler> query = em.createQuery("select DISTINCT(sch) from Schueler sch where :kl = sch.klasse", Schueler.class)
                        .setParameter("kl", klasse);
                return new ArrayList<>(query.getResultList());
            }
            return new ArrayList<>();

        } finally {
            em.close();
        }
    }

    @Override
    public List<Klasse> getKlassenByLehrer(Lehrer lehrer) {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            lehrer = getLehrerByKurzbezeichnung(lehrer.getLehrerKb());
            if (lehrer != null) {
                TypedQuery<Klasse> query = em.createQuery("select DISTINCT(fach.klasse) from Lehrfach fach where fach.lehrer = :le", Klasse.class)
                        .setParameter("le", lehrer);
                return new ArrayList<>(query.getResultList());
            }
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean persistPraktischePruefung(PraPruefung pr) {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(pr);
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
            System.out.println(e);
            return false;
        } finally {

            em.close();
        }
    }

    @Override
    public boolean updatePraktischePruefung(PraPruefung pr) {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(pr);
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
            System.out.println(e);
            return false;
        } finally {

            em.close();
        }
    }

    @Override
    public boolean deletePraktischePruefung(PraPruefung pruefung) {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.remove(em.find(PraPruefung.class, pruefung.getId()));
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
            System.out.println(e);
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public List<PraPruefung> getPreaktischePruefungenByLehrer(Lehrer lehrer) {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            lehrer = getLehrerByKurzbezeichnung(lehrer.getLehrerKb());
            if (lehrer != null) {
                TypedQuery<PraPruefung> query = em.createQuery("select p from PraPruefung p where p.lehrer = :le", PraPruefung.class)
                        .setParameter("le", lehrer);
                return query.getResultList();
            }
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    @Override
    public List<PraPruefung> getPreaktischePruefungen() {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<PraPruefung> query = em.createQuery("select p from PraPruefung p", PraPruefung.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void close() throws Exception {
        HibernateJPAUtil.close();
    }

    @Override
    public List<Gegenstand> getGegenstaendeInKlasseByLehrer(Lehrer lehrer, Klasse klasse) {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            lehrer = em.find(Lehrer.class, lehrer.getLehrerKb());
            klasse = em.find(Klasse.class, klasse.getKlaBez());
            if (lehrer != null && klasse != null) {
                TypedQuery<Gegenstand> gegenstaende = em.createQuery("select DISTINCT(l.gegenstand) from Lehrfach l where l.lehrer = :le and l.klasse = :kl", Gegenstand.class)
                        .setParameter("le", lehrer)
                        .setParameter("kl", klasse);
                return gegenstaende.getResultList();
            }
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public PraPruefung findPraktischePruefungById(PraPruefung aktPruefung) {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(PraPruefung.class, aktPruefung.getId());
        } finally {
            em.close();
        }
    }

    @Override
    public Lehrer getLehrerByKurzbezeichnung(String kurzBezeichung) {
        EntityManager entityManager = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Lehrer> lehrerQuery = entityManager.createQuery("select le from Lehrer le where le.lehrerKb = :lehrerKurzbezeichnung", Lehrer.class);
            lehrerQuery.setParameter("lehrerKurzbezeichnung", kurzBezeichung);
            Lehrer ergebnis = lehrerQuery.getSingleResult();
            return ergebnis;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Schueler> getSchuelerFromPruefung(PraPruefung pruefung) {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            pruefung = em.find(PraPruefung.class, pruefung.getId());
            if (pruefung != null) {
                TypedQuery<Schueler> query = em.createQuery("select sch from PraPruefung p join p.schuelerSet sch where p = :pruefung)", Schueler.class)
                        .setParameter("pruefung", pruefung);
                return new ArrayList<>(query.getResultList());
            }
            return new ArrayList<>();
        } finally {
            em.close();
        }

    }

    /*
    Alle Gruppen von den Schülern von der Klasse holen -> Distinct 
    Gegenstand von der Gruppe wird von dem Lehrer unterrichtet in der Klasse? 
    
     */
    @Override
    public List<Gruppe> getGruppenByLehrer(Lehrer lehrer) {
        EntityManager em = HibernateJPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            lehrer = em.find(Lehrer.class, lehrer.getLehrerKb());
            if (lehrer != null) {
                TypedQuery<Gruppe> gruppen = em.createQuery("select ber.gruppe from Berechtigung ber where ber.lehrer = :lehrer", Gruppe.class);
                return new ArrayList<>(gruppen.setParameter("lehrer", lehrer).getResultList());
            }
            return new ArrayList<>();

        } finally {
            em.close();
        }
    }

    //Klasse(kurz);Katalognummer(2stellig);Nachname;Vorname..
}
