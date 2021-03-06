package at.htlstp.projekt.p04.model;
// Generated 13.12.2016 11:01:59 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BewertungId generated by hbm2java
 */
@Embeddable
public class BewertungId implements java.io.Serializable {

    private int laufId;
    private int kompId;

    public BewertungId() {
    }

    public BewertungId(int laufId, int kompId) {
        this.laufId = laufId;
        this.kompId = kompId;
    }

    @Column(name = "lauf_id", nullable = false)
    public int getLaufId() {
        return this.laufId;
    }

    public void setLaufId(int laufId) {
        this.laufId = laufId;
    }

    @Column(name = "komp_id", nullable = false)
    public int getKompId() {
        return this.kompId;
    }

    public void setKompId(int kompId) {
        this.kompId = kompId;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof BewertungId)) {
            return false;
        }
        BewertungId castOther = (BewertungId) other;

        return (this.getLaufId() == castOther.getLaufId())
                && (this.getKompId() == castOther.getKompId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getLaufId();
        result = 37 * result + this.getKompId();
        return result;
    }

}
