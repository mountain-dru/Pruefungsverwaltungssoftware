package at.htlstp.projekt.p04.db;

import at.htlstp.projekt.p04.model.Gegenstand;
import at.htlstp.projekt.p04.model.Gruppe;
import at.htlstp.projekt.p04.model.Klasse;
import at.htlstp.projekt.p04.model.Lehrer;
import at.htlstp.projekt.p04.model.PraPruefung;
import at.htlstp.projekt.p04.model.Schueler;
import at.htlstp.projekt.p04.model.Titel;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author 20120115
 */
public interface IDAO extends Serializable {

    public Lehrer getLehrerByKurzbezeichnung(String kurzBez);
    public List<Schueler> getSchuelerByGruppe(Gruppe gruppe);
    public List<Schueler> getSchuelerByKlasse(Klasse klasse);
    public List<Gegenstand> getGegenstaendeInKlasseByLehrer(Lehrer lehrer, Klasse klasse);
    public List<Klasse> getKlassenByLehrer(Lehrer lehrer);
    public boolean persistPraktischePruefung(PraPruefung pr);
    public boolean updatePraktischePruefung(PraPruefung pr);
    public List<PraPruefung> getPreaktischePruefungenByLehrer(Lehrer lehrer);
    public boolean deletePraktischePruefung(PraPruefung pruefung);
    public List<Schueler> getSchuelerFromPruefung(PraPruefung pruefung);
    public List<Gruppe> getGruppenByLehrer(Lehrer lehrer);
    public List<PraPruefung> getPreaktischePruefungen();
}
