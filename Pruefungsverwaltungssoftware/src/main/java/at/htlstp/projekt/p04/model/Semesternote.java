package at.htlstp.projekt.p04.model;
// Generated 13.12.2016 11:01:59 by Hibernate Tools 4.3.1

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

/**
 * Semesternote generated by hbm2java
 */
@Entity
@Table(name = "semesternote", schema = "public"
)
@SequenceGenerator(name="sem_seq", sequenceName="sem_seq", allocationSize=1)
public class Semesternote implements java.io.Serializable {

    private int semId;
    private Gegenstand gegenstand;
    private Lehrer lehrer;
    private Schueler schueler;
    private String semNote;
    private int semSemester;
    private int semJahrgang;
    private Set<Mahnung> mahnungen = new HashSet<Mahnung>(0);

    public Semesternote() {
    }

    public Semesternote(Gegenstand gegenstand, Lehrer lehrer, Schueler schueler, String semNote, int semSemester, int semJahrgang) {
        this.gegenstand = gegenstand;
        this.lehrer = lehrer;
        this.schueler = schueler;
        this.semNote = semNote;
        this.semSemester = semSemester;
        this.semJahrgang = semJahrgang;
    }

    public Semesternote(int semId, Gegenstand gegenstand, Lehrer lehrer, Schueler schueler, String semNote, int semSemester, int semJahrgang) {
        this.semId = semId;
        this.gegenstand = gegenstand;
        this.lehrer = lehrer;
        this.schueler = schueler;
        this.semNote = semNote;
        this.semSemester = semSemester;
        this.semJahrgang = semJahrgang;
    }

    public Semesternote(int semId, Gegenstand gegenstand, Lehrer lehrer, Schueler schueler, String semNote, int semSemester, int semJahrgang, Set<Mahnung> mahnungs) {
        this.semId = semId;
        this.gegenstand = gegenstand;
        this.lehrer = lehrer;
        this.schueler = schueler;
        this.semNote = semNote;
        this.semSemester = semSemester;
        this.semJahrgang = semJahrgang;
        this.mahnungen = mahnungs;
    }

    @Id
    @GeneratedValue(generator = "sem_seq")
    @Column(name = "sem_id", unique = true, nullable = false)
    public int getSemId() {
        return this.semId;
    }

    public void setSemId(int semId) {
        this.semId = semId;
    }

    @ManyToOne
    @JoinColumn(name = "sem_geg_kurzbez", nullable = false)
    public Gegenstand getGegenstand() {
        return this.gegenstand;
    }

    public void setGegenstand(Gegenstand gegenstand) {
        this.gegenstand = gegenstand;
    }

    @ManyToOne
    @JoinColumn(name = "sem_lehrer_kb", nullable = false)
    public Lehrer getLehrer() {
        return this.lehrer;
    }

    public void setLehrer(Lehrer lehrer) {
        this.lehrer = lehrer;
    }

    @ManyToOne
    @JoinColumn(name = "sem_ssd_id", nullable = false)
    public Schueler getSchueler() {
        return this.schueler;
    }

    public void setSchueler(Schueler schueler) {
        this.schueler = schueler;
    }

    @Column(name = "sem_note", nullable = false, length = 2)
    public String getSemNote() {
        return this.semNote;
    }

    public void setSemNote(String semNote) {
        this.semNote = semNote;
    }

    @Column(name = "sem_semester", nullable = false)
    public int getSemSemester() {
        return this.semSemester;
    }

    public void setSemSemester(int semSemester) {
        this.semSemester = semSemester;
    }

    @Column(name = "sem_jahrgang", nullable = false)
    public int getSemJahrgang() {
        return this.semJahrgang;
    }

    public void setSemJahrgang(int semJahrgang) {
        this.semJahrgang = semJahrgang;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "semesternote")
    public Set<Mahnung> getMahnungen() {
        return this.mahnungen;
    }

    public void setMahnungen(Set<Mahnung> mahnungen) {
        this.mahnungen = mahnungen;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.semId;
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
        final Semesternote other = (Semesternote) obj;
        if (this.semId != other.semId) {
            return false;
        }
        return true;
    }

}
