package at.htlstp.projekt.p04.model;
// Generated 13.12.2016 11:01:59 by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.transaction.Transactional;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Formula;

/**
 * LaufendeNote generated by hbm2java
 */
@Entity
@Table(name = "laufende_note", schema = "public"
)
@SequenceGenerator(name="lauf_seq", sequenceName="lauf_seq", allocationSize=1)
public class LaufendeNote implements java.io.Serializable {

   
    //private String note;
    
    private int laufId;
    private Gegenstand gegenstand;
    private Kategorie kategorie;
    private Lehrer lehrer;
    private Schueler schueler;
    private String laufBemerkung;
    private String laufBezeichnung;
    private boolean laufGelesen;
    private String laufSecretBemerkung;
    private boolean laufSetze5;
    private Date laufTimestamp;
    private boolean laufInvalid;
    private Set<Bewertung> bewertungen = new HashSet<>(0);
    

    public LaufendeNote() {
    }

    public LaufendeNote(Gegenstand gegenstand, Lehrer lehrer, Schueler schueler, String laufBezeichnung, boolean laufGelesen, boolean laufSetze5, Date laufTimestamp, boolean laufInvalid) {
        this.gegenstand = gegenstand;
        this.lehrer = lehrer;
        this.schueler = schueler;
        this.laufBezeichnung = laufBezeichnung;
        this.laufGelesen = laufGelesen;
        this.laufSetze5 = laufSetze5;
        this.laufTimestamp = laufTimestamp;
        this.laufInvalid = laufInvalid;
    }
    
    public LaufendeNote(Gegenstand gegenstand, Kategorie kategorie, Lehrer lehrer, Schueler schueler, String laufBemerkung, String laufBezeichnung, boolean laufGelesen, String laufSecretBemerkung, boolean laufSetze5, Date laufTimestamp, boolean laufInvalid, Set<Bewertung> bewertungs) {
        this.gegenstand = gegenstand;
        this.kategorie = kategorie;
        this.lehrer = lehrer;
        this.schueler = schueler;
        this.laufBemerkung = laufBemerkung;
        this.laufBezeichnung = laufBezeichnung;
        this.laufGelesen = laufGelesen;
        this.laufSecretBemerkung = laufSecretBemerkung;
        this.laufSetze5 = laufSetze5;
        this.laufTimestamp = laufTimestamp;
        this.laufInvalid = laufInvalid;
        this.bewertungen = bewertungs;
    }
    
    public LaufendeNote(int laufId, Gegenstand gegenstand, Lehrer lehrer, Schueler schueler, String laufBezeichnung, boolean laufGelesen, boolean laufSetze5, Date laufTimestamp, boolean laufInvalid) {
        this.laufId = laufId;
        this.gegenstand = gegenstand;
        this.lehrer = lehrer;
        this.schueler = schueler;
        this.laufBezeichnung = laufBezeichnung;
        this.laufGelesen = laufGelesen;
        this.laufSetze5 = laufSetze5;
        this.laufTimestamp = laufTimestamp;
        this.laufInvalid = laufInvalid;
    }

    public LaufendeNote(int laufId, Gegenstand gegenstand, Kategorie kategorie, Lehrer lehrer, Schueler schueler, String laufBemerkung, String laufBezeichnung, boolean laufGelesen, String laufSecretBemerkung, boolean laufSetze5, Date laufTimestamp, boolean laufInvalid, Set<Bewertung> bewertungs) {
        this.laufId = laufId;
        this.gegenstand = gegenstand;
        this.kategorie = kategorie;
        this.lehrer = lehrer;
        this.schueler = schueler;
        this.laufBemerkung = laufBemerkung;
        this.laufBezeichnung = laufBezeichnung;
        this.laufGelesen = laufGelesen;
        this.laufSecretBemerkung = laufSecretBemerkung;
        this.laufSetze5 = laufSetze5;
        this.laufTimestamp = laufTimestamp;
        this.laufInvalid = laufInvalid;
        this.bewertungen = bewertungs;
    }

    @Id
    @GeneratedValue(generator = "lauf_seq")
    @Column(name = "lauf_id", unique = true, nullable = false)
    public int getLaufId() {
        return this.laufId;
    }

    public void setLaufId(int laufId) {
        this.laufId = laufId;
    }

    @ManyToOne
    @JoinColumn(name = "lauf_geg_kurzbez", nullable = false)
    public Gegenstand getGegenstand() {
        return this.gegenstand;
    }

    public void setGegenstand(Gegenstand gegenstand) {
        this.gegenstand = gegenstand;
    }

    @ManyToOne
    @JoinColumn(name = "lauf_kat_id")
    public Kategorie getKategorie() {
        return this.kategorie;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    @ManyToOne
    @JoinColumn(name = "lauf_lehrer_kb", nullable = false)
    public Lehrer getLehrer() {
        return this.lehrer;
    }

    public void setLehrer(Lehrer lehrer) {
        this.lehrer = lehrer;
    }

    @ManyToOne
    @JoinColumn(name = "lauf_ssd_id", nullable = false)
    public Schueler getSchueler() {
        return this.schueler;
    }

    public void setSchueler(Schueler schueler) {
        this.schueler = schueler;
    }

    @Column(name = "lauf_bemerkung")
    public String getLaufBemerkung() {
        return this.laufBemerkung;
    }

    public void setLaufBemerkung(String laufBemerkung) {
        this.laufBemerkung = laufBemerkung;
    }

    @Column(name = "lauf_bezeichnung", nullable = false)
    public String getLaufBezeichnung() {
        return this.laufBezeichnung;
    }

    public void setLaufBezeichnung(String laufBezeichnung) {
        this.laufBezeichnung = laufBezeichnung;
    }

    @Column(name = "lauf_gelesen", nullable = false)
    public boolean isLaufGelesen() {
        return this.laufGelesen;
    }

    public void setLaufGelesen(boolean laufGelesen) {
        this.laufGelesen = laufGelesen;
    }

    @Column(name = "lauf_secret_bemerkung")
    public String getLaufSecretBemerkung() {
        return this.laufSecretBemerkung;
    }

    public void setLaufSecretBemerkung(String laufSecretBemerkung) {
        this.laufSecretBemerkung = laufSecretBemerkung;
    }

    @Column(name = "lauf_setze5", nullable = false)
    public boolean isLaufSetze5() {
        return this.laufSetze5;
    }

    public void setLaufSetze5(boolean laufSetze5) {
        this.laufSetze5 = laufSetze5;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "lauf_timestamp", nullable = false, length = 13)
    public Date getLaufTimestamp() {
        return this.laufTimestamp;
    }

    public void setLaufTimestamp(Date laufTimestamp) {
        this.laufTimestamp = laufTimestamp;
    }

    @Column(name = "lauf_invalid", nullable = false)
    public boolean isLaufInvalid() {
        return this.laufInvalid;
    }

    public void setLaufInvalid(boolean laufInvalid) {
        this.laufInvalid = laufInvalid;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "laufendeNote")
    public Set<Bewertung> getBewertungen() {
        return this.bewertungen;
    }

    public void setBewertungen(Set<Bewertung> bewertungen) {
        this.bewertungen = bewertungen;
    }
    
    @Transient
    public String getNote() {
        //TODO: Berechnung
        for(Bewertung bewertung : this.bewertungen) {
            return bewertung.getBewertung();
        }
        return "fail";
    }
    
    // reio 13.10.
    @Transient
    public double getValue() {
        return 0.0;
    }
    
    /*@Transactional
    @Transient
    public String getNote() {
        
        Hibernate.initialize(this.bewertungen);
        for(Bewertung bewertung : this.bewertungen) {
            return bewertung.getBewertung();
        }
    
        
        return "nix";
    }
    
    public void setNote(String note) {
        this.note = note;
    }*/

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.laufId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LaufendeNote other = (LaufendeNote) obj;
        if (this.laufId != other.laufId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return laufBezeichnung;
    }

    
}