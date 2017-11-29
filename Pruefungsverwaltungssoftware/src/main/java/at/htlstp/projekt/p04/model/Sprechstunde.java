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
 * Sprechstunde generated by hbm2java
 */
@Entity
@Table(name = "sprechstunde", schema = "public"
)
@SequenceGenerator(name="spr_seq", sequenceName="spr_seq", allocationSize=1)
public class Sprechstunde implements java.io.Serializable {

    private int sprId;
    private Lehrer lehrer;
    private String sprTag;
    private Date sprBeginnZeit;
    private Date sprEndeZeit;

    public Sprechstunde() {
    }

    public Sprechstunde(Lehrer lehrer, String sprTag, Date sprBeginnZeit, Date sprEndeZeit) {
        this.lehrer = lehrer;
        this.sprTag = sprTag;
        this.sprBeginnZeit = sprBeginnZeit;
    }
    
    public Sprechstunde(int sprId, Lehrer lehrer, String sprTag, Date sprBeginnZeit, Date sprEndeZeit) {
        this.sprId = sprId;
        this.lehrer = lehrer;
        this.sprTag = sprTag;
        this.sprBeginnZeit = sprBeginnZeit;
    }

    @Id
    @GeneratedValue(generator = "spr_seq")
    @Column(name = "spr_id", unique = true, nullable = false)
    public int getSprId() {
        return this.sprId;
    }

    public void setSprId(int sprId) {
        this.sprId = sprId;
    }

    @ManyToOne
    @JoinColumn(name = "spr_lehrer_kb", nullable = false)
    public Lehrer getLehrer() {
        return this.lehrer;
    }

    public void setLehrer(Lehrer lehrer) {
        this.lehrer = lehrer;
    }

    @Column(name = "spr_tag", nullable = false, length = 10)
    public String getSprTag() {
        return this.sprTag;
    }

    public void setSprTag(String sprTag) {
        this.sprTag = sprTag;
    }

    @Temporal(TemporalType.TIME)
    @Column(name = "spr_beginn_zeit", nullable = false, length = 15)
    public Date getSprBeginnZeit() {
        return this.sprBeginnZeit;
    }

    public void setSprBeginnZeit(Date sprBeginnZeit) {
        this.sprBeginnZeit = sprBeginnZeit;
    }

    @Temporal(TemporalType.TIME)
    @Column(name = "spr_ende_zeit", nullable = false, length = 15)
    public Date getSprEndeZeit() {
        return this.sprEndeZeit;
    }
    
    public void setSprEndeZeit(Date sprEndeZeit) {
        this.sprEndeZeit = sprEndeZeit;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.sprId;
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
        final Sprechstunde other = (Sprechstunde) obj;
        if (this.sprId != other.sprId) {
            return false;
        }
        return true;
    }

}
