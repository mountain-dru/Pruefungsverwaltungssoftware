package at.htlstp.projekt.p04.model;
// Generated 13.12.2016 11:01:59 by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Gesetzkategorie generated by hbm2java
 */
@Entity
@Table(name = "gesetzkategorie", schema = "public"
)
public class Gesetzkategorie implements java.io.Serializable {

    private String gesKurzbez;
    private String gesLangbez;
    private Set<Kategorie> kategorien = new HashSet<Kategorie>(0);

    public Gesetzkategorie() {
    }

    public Gesetzkategorie(String gesKurzbez, String gesLangbez) {
        this.gesKurzbez = gesKurzbez;
        this.gesLangbez = gesLangbez;
    }

    public Gesetzkategorie(String gesKurzbez, String gesLangbez, Set<Kategorie> kategories) {
        this.gesKurzbez = gesKurzbez;
        this.gesLangbez = gesLangbez;
        this.kategorien = kategories;
    }

    @Id
    @Column(name = "ges_kurzbez", unique = true, nullable = false, length = 3)
    public String getGesKurzbez() {
        return this.gesKurzbez;
    }

    public void setGesKurzbez(String gesKurzbez) {
        this.gesKurzbez = gesKurzbez;
    }

    @Column(name = "ges_langbez", nullable = false, length = 60)
    public String getGesLangbez() {
        return this.gesLangbez;
    }

    public void setGesLangbez(String gesLangbez) {
        this.gesLangbez = gesLangbez;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gesetzkategorie")
    public Set<Kategorie> getKategorien() {
        return this.kategorien;
    }

    public void setKategorien(Set<Kategorie> kategories) {
        this.kategorien = kategories;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.gesKurzbez);
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
        final Gesetzkategorie other = (Gesetzkategorie) obj;
        if (!Objects.equals(this.gesKurzbez, other.gesKurzbez)) {
            return false;
        }
        return true;
    }

}