/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.projekt.p04.model;

import at.htlstp.projekt.p04.pruefungsverwaltungssoftware.Verwaltungssoftware;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 *
 * @author Dru
 */
@Entity
@Table(name = "prapruefung", schema = "public")
@SequenceGenerator(name = "pr_seq", sequenceName = "pr_seq")
public class PraPruefung implements Serializable {

    //Variablendeklaration
    private Integer id;
    private String name;
    private Verwaltungssoftware.Unterrichtseinheit unterrichtsstunde;
    private Date datum;
    private Boolean internet;
    private Verwaltungssoftware.PR_status status;

    @Id
    @Column(name = "pr_id")
    @GeneratedValue(generator = "pr_seq")
    public Integer getId() {
        return id;
    }

    @Column(name = "pr_name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    @Column(name = "pr_unterrichtsstunde", nullable = false)
    public Verwaltungssoftware.Unterrichtseinheit getUnterrichtsstunde() {
        return unterrichtsstunde;
    }

    @Column(name = "pr_status", nullable = false)
    public Verwaltungssoftware.PR_status getStatus() {
        return status;
    }

    @Column(name = "pr_internet")
    public Boolean getInternet() {
        return internet;
    }

    @Temporal(value = TemporalType.DATE)
    @Column(name = "pr_datum", nullable = false)
    public Date getDatum() {
        return datum;
    }

    public PraPruefung() {
    }
    //Beziehungen, Setter, weitere Konstruktoren

    private Gegenstand gegenstand;
    private Lehrer lehrer;
    private Klasse klasse;
    private Set<Schueler> schuelerSet = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "pr_gegenstand", nullable = false)
    public Gegenstand getGegenstand() {
        return gegenstand;
    }

    public void setGegenstand(Gegenstand gegenstand) {
        this.gegenstand = gegenstand;
    }

    @ManyToOne
    @JoinColumn(name = "pr_lehrer", nullable = false)
    public Lehrer getLehrer() {
        return lehrer;
    }

    public void setLehrer(Lehrer lehrer) {
        this.lehrer = lehrer;
    }

    @ManyToOne
    @JoinColumn(name = "pr_klasse", nullable = false)
    public Klasse getKlasse() {
        return klasse;
    }

    public void setKlasse(Klasse klasse) {
        this.klasse = klasse;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "pr_schueler", schema = "public", joinColumns = {
        @JoinColumn(name = "pr_id", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "ssd_id", nullable = false, updatable = false)})
    public Set<Schueler> getSchuelerSet() {
        return schuelerSet;
    }

    public void setSchuelerSet(Set<Schueler> schuelerSet) {
        this.schuelerSet = schuelerSet;
    }

    public PraPruefung(String name, Verwaltungssoftware.Unterrichtseinheit unterrichtsstunde, Date datum, Verwaltungssoftware.PR_status status, Gegenstand gegenstand, Lehrer lehrer, Klasse klasse) {
        this.name = name;
        this.unterrichtsstunde = unterrichtsstunde;
        this.datum = datum;
        this.status = status;
        this.gegenstand = gegenstand;
        this.lehrer = lehrer;
        this.klasse = klasse;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final PraPruefung other = (PraPruefung) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PraPruefung{" + "id=" + id + ", name=" + name + ", unterrichtsstunde=" + unterrichtsstunde + ", datum=" + datum + ", internet=" + internet + ", status=" + status + ", gegenstand=" + gegenstand + ", lehrer=" + lehrer + ", klasse=" + klasse + '}';
    }

    public void setUnterrichtsstunde(Verwaltungssoftware.Unterrichtseinheit unterrichtsstunde) {
        this.unterrichtsstunde = unterrichtsstunde;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public void setStatus(Verwaltungssoftware.PR_status status) {
        this.status = status;
    }

    public void setInternet(Boolean internet) {
        this.internet = internet;
    }

}
