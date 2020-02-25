package siap.siep.fascicolo.model;

import java.util.List;

import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel;
import siap.siep.sospensione.model.SospensioneModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: DatiSiepPerTrasferimentoModel
 * </p>
 * <p>
 * Description: Altri Dati Siep per il trasferimento del FASCICOLO_SIEP
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DatiSiepPerTrasferimentoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2167612454037524210L;

	private List mListPenaCumulo;
	private List mListCumulo;
	private List mListUltSanCumulo;
	private List mListLicLibAnticipata;
	private List mListMisuraAlternativa;
	private List mListResidenzaFasSiep;
	private List mListAnnotazioneManuale;
	private List mListPenaResidua;
	private List mListFungibilita;
	private List mListSanzioneSostResidua;
	private MagistratoCompetenteMagistratoModel mMagistratoCompetente;
	private List mListRichiesteConversioniPP;
	private List mListNuovaIstanza; // 30/08/2010
	private List mListAltriGradiGiudizio; // 01/09/2010 <AgdgFascicoloSiepModel>
	private List mListMsToFascSiep; // 29/01/2015

	private List mListCompetenze; // 06/2015

	private List<SentenzaRiunitaFascSiepModel> mListSentenzeRiunite; // 26/09/2014 Lista delle Sentenze
																		// Riunite associate al fascicolo
	private List<SospensioneModel> mListSospensioni;
	private List<DecretoOrdinanzaSiepModel> mListDecretiOrd; // 07/2017

	// COSTRUTTORE DI DEFAULT
	public DatiSiepPerTrasferimentoModel() {
		mListPenaCumulo = null;
		mListCumulo = null;
		mListUltSanCumulo = null;
		mListLicLibAnticipata = null;
		mListMisuraAlternativa = null;
		mListResidenzaFasSiep = null;
		mListAnnotazioneManuale = null;
		mListPenaResidua = null;
		mListFungibilita = null;
		mListSanzioneSostResidua = null;
		this.mMagistratoCompetente = new MagistratoCompetenteMagistratoModel();
		mListRichiesteConversioniPP = null;
		mListNuovaIstanza = null; // 30/08/2010
		mListAltriGradiGiudizio = null; // 01/10/2010
		mListSentenzeRiunite = null;
		mListMsToFascSiep = null;
		mListCompetenze = null;
		mListSospensioni = null;
	}

	// COSTRUTTORE DI COPIA
	public DatiSiepPerTrasferimentoModel(List aListPenaCumulo, List aListCumulo, List aListUltSanCumulo,
			List aListLicLibAnticipata, List aListMisuraAlternativa, List aListResidenzaFasSiep,
			List aListAnnotazioneManuale, List aListPenaResidua, List aListFungibilita,
			List aListSanzioneSostResidua, MagistratoCompetenteMagistratoModel aMagistratoCompetente,
			List aListRichiesteConversioniPP, List aListNuovaIstanza, List aListAltriGradiGiudizio,
			List aListSentenzeRiunite, List aListMsToFascSiep, List aListCompetenze,
			List<SospensioneModel> aListSospensioni) {
		mListPenaCumulo = aListPenaCumulo;
		mListCumulo = aListCumulo;
		mListUltSanCumulo = aListUltSanCumulo;
		mListLicLibAnticipata = aListLicLibAnticipata;
		mListMisuraAlternativa = aListMisuraAlternativa;
		mListResidenzaFasSiep = aListResidenzaFasSiep;
		mListAnnotazioneManuale = aListAnnotazioneManuale;
		mListPenaResidua = aListPenaResidua;
		mListFungibilita = aListFungibilita;
		mListSanzioneSostResidua = aListSanzioneSostResidua;
		mMagistratoCompetente = aMagistratoCompetente;
		mListRichiesteConversioniPP = aListRichiesteConversioniPP;
		mListNuovaIstanza = aListNuovaIstanza; // 30/08/2010
		mListAltriGradiGiudizio = aListAltriGradiGiudizio; // 01/10/2010
		mListSentenzeRiunite = aListSentenzeRiunite;
		mListMsToFascSiep = aListMsToFascSiep;
		mListCompetenze = aListCompetenze;
		mListSospensioni = aListSospensioni;
	}

	//
	// METODI GET()
	//
	public List getListPenaCumulo() {
		return mListPenaCumulo;
	}

	public List getListCumulo() {
		return mListCumulo;
	}

	public List getListUltSanCumulo() {
		return mListUltSanCumulo;
	}

	public List getListLicLibAnticipata() {
		return mListLicLibAnticipata;
	}

	public List getListMisuraAlternativa() {
		return mListMisuraAlternativa;
	}

	public List getListResidenzaFasSiep() {
		return mListResidenzaFasSiep;
	}

	public List getListAnnotazioneManuale() {
		return mListAnnotazioneManuale;
	}

	public List getListPenaResidua() {
		return mListPenaResidua;
	}

	public List getListFungibilita() {
		return mListFungibilita;
	}

	public List getListSanzioneSostResidua() {
		return mListSanzioneSostResidua;
	}

	public MagistratoCompetenteMagistratoModel getMagistratoCompetente() {
		return mMagistratoCompetente;
	}

	public List getListRichiesteConversioniPP() {
		return mListRichiesteConversioniPP;
	}

	public List getListNuovaIstanza() {
		return mListNuovaIstanza;
	} // 30/08/2010

	public List getListAltriGradiGiudizio() {
		return mListAltriGradiGiudizio;
	} // 01/09/2010
	// public List getListAGDGFasSiep() { return mListAGDGFasSiep; } // 01/09/2010

	public List getListSentenzeRiunite() {
		return mListSentenzeRiunite;
	}

	public List getListFascMsToFascSiep() {
		return mListMsToFascSiep;
	}

	public List getListCompetenze() {
		return mListCompetenze;
	}

	public List<SospensioneModel> getListSospensioni() {
		return mListSospensioni;
	}

	public List<DecretoOrdinanzaSiepModel> getListDecretiOrd() {
		return mListDecretiOrd;
	}

	//
	// METODI SET()
	//
	public void setListPenaCumulo(List aValore) {
		mListPenaCumulo = aValore;
	}

	public void setListCumulo(List aValore) {
		mListCumulo = aValore;
	}

	public void setListUltSanCumulo(List aValore) {
		mListUltSanCumulo = aValore;
	}

	public void setListLicLibAnticipata(List aValore) {
		mListLicLibAnticipata = aValore;
	}

	public void setListMisuraAlternativa(List aValore) {
		mListMisuraAlternativa = aValore;
	}

	public void setListResidenzaFasSiep(List aValore) {
		mListResidenzaFasSiep = aValore;
	}

	public void setListAnnotazioneManuale(List aValore) {
		mListAnnotazioneManuale = aValore;
	}

	public void setListPenaResidua(List aValore) {
		mListPenaResidua = aValore;
	}

	public void setListFungibilita(List aValore) {
		mListFungibilita = aValore;
	}

	public void setListSanzioneSostResidua(List aValore) {
		mListSanzioneSostResidua = aValore;
	}

	public void setMagistratoCompetente(MagistratoCompetenteMagistratoModel aValore) {
		mMagistratoCompetente = aValore;
	}

	public void setListRichiesteConversioniPP(List aValore) {
		mListRichiesteConversioniPP = aValore;
	}

	public void setListNuovaIstanza(List aValore) {
		mListNuovaIstanza = aValore;
	} // 30/08/2010
	// public void setListAGDGFasSiep(List aValore) { mListAGDGFasSiep = aValore; } // 01/09/2010

	public void setListAltriGradiGiudizio(List aValore) {
		mListAltriGradiGiudizio = aValore;
	} // 01/09/2010

	public void setListSentenzeRiunite(List aValore) {
		mListSentenzeRiunite = aValore;
	}

	public void setListMsToFascSiep(List aValore) {
		mListMsToFascSiep = aValore;
	}

	public void setListCompetenze(List aValore) {
		mListCompetenze = aValore;
	}

	public void setListSospensioni(List<SospensioneModel> aValore) {
		mListSospensioni = aValore;
	}

	public void setListDecretiOrd(List<DecretoOrdinanzaSiepModel> aValore) {
		mListDecretiOrd = aValore;
	}

}