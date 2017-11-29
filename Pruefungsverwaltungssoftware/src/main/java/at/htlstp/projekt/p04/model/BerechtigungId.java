package at.htlstp.projekt.p04.model;
// Generated 13.12.2016 11:01:59 by Hibernate Tools 4.3.1

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BerechtigungId generated by hbm2java
 */
@Embeddable
public class BerechtigungId implements java.io.Serializable {

    private String lehrerKb;
    private int grpId;

    public BerechtigungId() {
    }

    public BerechtigungId(String lehrerKb, int grpId) {
        this.lehrerKb = lehrerKb;
        this.grpId = grpId;
    }

    @Column(name = "lehrer_kb", nullable = false, length = 4)
    public String getLehrerKb() {
        return this.lehrerKb;
    }

    public void setLehrerKb(String lehrerKb) {
        this.lehrerKb = lehrerKb;
    }

    @Column(name = "grp_id", nullable = false)
    public int getGrpId() {
        return this.grpId;
    }

    public void setGrpId(int grpId) {
        this.grpId = grpId;
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
        final BerechtigungId other = (BerechtigungId) obj;
        if (this.grpId != other.grpId) {
            return false;
        }
        if (!Objects.equals(this.lehrerKb, other.lehrerKb)) {
            return false;
        }
        return true;
    }

    

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + (getLehrerKb() == null ? 0 : this.getLehrerKb().hashCode());
        result = 37 * result + this.getGrpId();
        return result;
    }

    @Override
    public String toString() {
        return lehrerKb + grpId;
    }

}