package at.htlstp.projekt.p04.model;
// Generated 13.12.2016 11:01:59 by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Mahnung generated by hbm2java
 */
@Entity
@Table(name = "mahnung", schema = "public"
)
@SequenceGenerator(name="mahn_seq", sequenceName="mahn_seq", allocationSize=1)
public class Mahnung implements java.io.Serializable {

    private int mahnId;
    private Gegenstand gegenstand;
    private Lehrer lehrer;
    private Schueler schueler;
    private Semesternote semesternote;
    private boolean mahnAusgegeben;
    private Date mahnAusgegebenTimestamp;
    private Date mahnErhaltenTimestamp;

    public Mahnung() {
    }

    public Mahnung(Gegenstand gegenstand, Lehrer lehrer, Schueler schueler, Semesternote semesternote, boolean mahnAusgegeben, Date mahnAusgegebenTimestamp) {
        this.gegenstand = gegenstand;
        this.lehrer = lehrer;
        this.schueler = schueler;
        this.semesternote = semesternote;
        this.mahnAusgegeben = mahnAusgegeben;
        this.mahnAusgegebenTimestamp = mahnAusgegebenTimestamp;
    }

    public Mahnung(int mahnId, Gegenstand gegenstand, Lehrer lehrer, Schueler schueler, Semesternote semesternote, boolean mahnAusgegeben, Date mahnAusgegebenTimestamp) {
        this.mahnId = mahnId;
        this.gegenstand = gegenstand;
        this.lehrer = lehrer;
        this.schueler = schueler;
        this.semesternote = semesternote;
        this.mahnAusgegeben = mahnAusgegeben;
        this.mahnAusgegebenTimestamp = mahnAusgegebenTimestamp;
    }

    public Mahnung(int mahnId, Gegenstand gegenstand, Lehrer lehrer, Schueler schueler, Semesternote semesternote, boolean mahnAusgegeben, Date mahnAusgegebenTimestamp, Date mahnErhaltenTimestamp) {
        this.mahnId = mahnId;
        this.gegenstand = gegenstand;
        this.lehrer = lehrer;
        this.schueler = schueler;
        this.semesternote = semesternote;
        this.mahnAusgegeben = mahnAusgegeben;
        this.mahnAusgegebenTimestamp = mahnAusgegebenTimestamp;
        this.mahnErhaltenTimestamp = mahnErhaltenTimestamp;
    }

    @Id
    @GeneratedValue(generator = "mahn_seq")
    @Column(name = "mahn_id", unique = true, nullable = false)
    public int getMahnId() {
        return this.mahnId;
    }

    public void setMahnId(int mahnId) {
        this.mahnId = mahnId;
    }

    @ManyToOne
    @JoinColumn(name = "mahn_geg_kurzbez", nullable = false)
    public Gegenstand getGegenstand() {
        return this.gegenstand;
    }

    public void setGegenstand(Gegenstand gegenstand) {
        this.gegenstand = gegenstand;
    }

    @ManyToOne
    @JoinColumn(name = "mahn_lehrer_kb", nullable = false)
    public Lehrer getLehrer() {
        return this.lehrer;
    }

    public void setLehrer(Lehrer lehrer) {
        this.lehrer = lehrer;
    }

    @ManyToOne
    @JoinColumn(name = "mahn_ssd_id", nullable = false)
    public Schueler getSchueler() {
        return this.schueler;
    }

    public void setSchueler(Schueler schueler) {
        this.schueler = schueler;
    }

    @ManyToOne
    @JoinColumn(name = "mahn_sem_id", nullable = false)
    public Semesternote getSemesternote() {
        return this.semesternote;
    }

    public void setSemesternote(Semesternote semesternote) {
        this.semesternote = semesternote;
    }

    @Column(name = "mahn_ausgegeben", nullable = false)
    public boolean isMahnAusgegeben() {
        return this.mahnAusgegeben;
    }

    public void setMahnAusgegeben(boolean mahnAusgegeben) {
        this.mahnAusgegeben = mahnAusgegeben;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "mahn_ausgegeben_timestamp", nullable = false, length = 13)
    public Date getMahnAusgegebenTimestamp() {
        return this.mahnAusgegebenTimestamp;
    }

    public void setMahnAusgegebenTimestamp(Date mahnAusgegebenTimestamp) {
        this.mahnAusgegebenTimestamp = mahnAusgegebenTimestamp;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "mahn_erhalten_timestamp", length = 13)
    public Date getMahnErhaltenTimestamp() {
        return this.mahnErhaltenTimestamp;
    }

    public void setMahnErhaltenTimestamp(Date mahnErhaltenTimestamp) {
        this.mahnErhaltenTimestamp = mahnErhaltenTimestamp;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.mahnId;
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
        final Mahnung other = (Mahnung) obj;
        if (this.mahnId != other.mahnId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(mahnId);
    }

}
