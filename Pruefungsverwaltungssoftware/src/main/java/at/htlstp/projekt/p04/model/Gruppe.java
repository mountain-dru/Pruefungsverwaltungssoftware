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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Gruppe generated by hbm2java
 */
@Entity
@Table(name = "gruppe", schema = "public")
@SequenceGenerator(name="grp_seq", sequenceName="grp_seq", allocationSize=1)
public class Gruppe implements java.io.Serializable, Comparable<Gruppe> {

	private int grpId;
	private Gegenstand gegenstand;
	private String grpName;
	private Set<Schueler> schueler = new HashSet<>(0);
	private Set<Berechtigung> berechtigungen = new HashSet<>(0);

	public Gruppe() {
	}

	public Gruppe(int grpId, Gegenstand gegenstand, String grpName) {
		this.grpId = grpId;
		this.gegenstand = gegenstand;
		this.grpName = grpName;
	}

	public Gruppe(int grpId, Gegenstand gegenstand, String grpName, Set<Schueler> schuelers, Set<Berechtigung> berechtigungs) {
		this.grpId = grpId;
		this.gegenstand = gegenstand;
		this.grpName = grpName;
		this.schueler = schuelers;
		this.berechtigungen = berechtigungs;
	}

	@Id
	@GeneratedValue(generator = "grp_seq")
	@Column(name = "grp_id", unique = true, nullable = false)
	public int getGrpId() {
		return this.grpId;
	}

	public void setGrpId(int grpId) {
		this.grpId = grpId;
	}

	@ManyToOne
	@JoinColumn(name = "grp_geg_kurzbez", nullable = false)
	public Gegenstand getGegenstand() {
		return this.gegenstand;
	}

	public void setGegenstand(Gegenstand gegenstand) {
		this.gegenstand = gegenstand;
	}

	@Column(name = "grp_name", nullable = false, length = 50)
	public String getGrpName() {
		return this.grpName;
	}

	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ssd_mitglied_grp", schema = "public", joinColumns = {
		@JoinColumn(name = "grp_id", nullable = false, updatable = false)}, inverseJoinColumns = {
		@JoinColumn(name = "ssd_id", nullable = false, updatable = false)})
	public Set<Schueler> getSchueler() {
		return this.schueler;
	}

	public void setSchueler(Set<Schueler> schueler) {
		this.schueler = schueler;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gruppe")
	public Set<Berechtigung> getBerechtigungen() {
		return this.berechtigungen;
	}

	public void setBerechtigungen(Set<Berechtigung> berechtigungen) {
		this.berechtigungen = berechtigungen;
	}

	@Override
	public String toString() {
		return grpName;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 37 * hash + this.grpId;
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
		final Gruppe other = (Gruppe) obj;
		return this.grpId == other.grpId;
	}

	@Override
	public int compareTo(Gruppe g) {
		return grpName.compareTo(g.grpName);
	}
}