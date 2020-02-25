package siap.siep.posizione.model;

/**
* <p>Title: PosizioneGiuridicaModel</p>
* <p>Description: Classe Model che rappresenta il PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;

@SuppressWarnings("rawtypes")
public class PosizioneGiuridicaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4983166343252905715L;
	private BigDecimal mIdPosizioneGiuridica;
	private String mCodPosizioneGiuridica;
	private String mDescrPosizioneGiuridica;
	private Date mDataInizio;
	private Date mDataFine;
	private String mCodPosizioneProcessuale;
	private String mDescrPosizioneProcessuale;
	private String mNote;
	private String mLuogoProvaAffidamento;
	private String mLuogoLavoroSemiliberta;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mIdEventoRiferimento;
	// modifica per integrazione REGE-SIES
	private String mFlagIsDetenuto;

	private String mLuogoEspiazione;
	private String mAutoritaCompetente;
	private String mAutoritaCompetenteSede;
	private String mAutoritaCompetenteSedeDesc;
	private String mAutoritaCompetenteIndirizzo;
	private String mCodMaschera;
	private BigDecimal mAltCauIdAltraCausa;

	// private boolean mPrimaPosizione;

	// COSTRUTTORE DI DEFAULT
	public PosizioneGiuridicaModel() {
		this.mIdPosizioneGiuridica = null;
		this.mCodPosizioneGiuridica = "";
		this.mDescrPosizioneGiuridica = "";
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mCodPosizioneProcessuale = "";
		this.mDescrPosizioneProcessuale = "";
		this.mNote = "";
		this.mLuogoProvaAffidamento = "";
		this.mLuogoLavoroSemiliberta = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mIdEventoRiferimento = null;
		// this.mPrimaPosizione = false;
		// rege-sies
		this.mFlagIsDetenuto = "";
		this.mLuogoEspiazione = "";
		this.mAutoritaCompetente = "";
		this.mAutoritaCompetenteSede = "";
		this.mAutoritaCompetenteSedeDesc = "";
		this.mAutoritaCompetenteIndirizzo = "";
		this.mCodMaschera = "";
		this.mAltCauIdAltraCausa = null;

	}

	// COSTRUTTORE DI COPIA
	public PosizioneGiuridicaModel(PosizioneGiuridicaModel aModel) {
		this.mIdPosizioneGiuridica = aModel.mIdPosizioneGiuridica;
		this.mCodPosizioneGiuridica = aModel.mCodPosizioneGiuridica;
		this.mDescrPosizioneGiuridica = aModel.mDescrPosizioneGiuridica;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mCodPosizioneProcessuale = aModel.mCodPosizioneProcessuale;
		this.mDescrPosizioneProcessuale = aModel.mDescrPosizioneProcessuale;
		this.mNote = aModel.mNote;
		this.mLuogoProvaAffidamento = aModel.mLuogoProvaAffidamento;
		this.mLuogoLavoroSemiliberta = aModel.mLuogoLavoroSemiliberta;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mIdEventoRiferimento = aModel.mIdEventoRiferimento;
		// rege-sies
		this.mFlagIsDetenuto = aModel.mFlagIsDetenuto;

		// this.mPrimaPosizione = aModel.mPrimaPosizione;
		this.mLuogoEspiazione = aModel.mLuogoEspiazione;
		this.mAutoritaCompetente = aModel.mAutoritaCompetente;
		this.mAutoritaCompetenteSede = aModel.mAutoritaCompetenteSede;
		this.mAutoritaCompetenteSedeDesc = aModel.mAutoritaCompetenteSedeDesc;
		this.mAutoritaCompetenteIndirizzo = aModel.mAutoritaCompetenteIndirizzo;
		this.mCodMaschera = aModel.mCodMaschera;
		this.mAltCauIdAltraCausa = aModel.mAltCauIdAltraCausa;

	}

	// COSTRUTTORE MODEL
	public PosizioneGiuridicaModel(BigDecimal aIdPosizioneGiuridica, String aCodPosizioneGiuridica,
			String aDescrPosizioneGiuridica, Date aDataInizio, Date aDataFine,
			String aCodPosizioneProcessuale, String aDescrPosizioneProcessuale, String aNote,
			String aLuogoProvaAffidamento, String aLuogoLavoroSemiliberta, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aFasSieIdFascicoloSiep,
			BigDecimal aIdEventoRiferimento, String aFlagIsDetenuto, String aLuogoEspiazione,
			String aAutoritaCompetente, String aAutoritaCompetenteSede, String aAutoritaCompetenteIndirizzo,
			String aCodMaschera, BigDecimal aAltCauIdAltraCausa) {
		this.mIdPosizioneGiuridica = aIdPosizioneGiuridica;
		this.mCodPosizioneGiuridica = aCodPosizioneGiuridica;
		this.mDescrPosizioneGiuridica = aDescrPosizioneGiuridica;
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mCodPosizioneProcessuale = aCodPosizioneProcessuale;
		this.mDescrPosizioneProcessuale = aDescrPosizioneProcessuale;
		this.mNote = aNote;
		this.mLuogoProvaAffidamento = aLuogoProvaAffidamento;
		this.mLuogoLavoroSemiliberta = aLuogoLavoroSemiliberta;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mIdEventoRiferimento = aIdEventoRiferimento;
		// rege-sies
		this.mFlagIsDetenuto = aFlagIsDetenuto;

		// this.mPrimaPosizione = false;
		this.mLuogoEspiazione = aLuogoEspiazione;
		this.mAutoritaCompetente = aAutoritaCompetente;
		this.mAutoritaCompetenteSede = aAutoritaCompetenteSede;
		this.mAutoritaCompetenteIndirizzo = aAutoritaCompetenteIndirizzo;
		this.mCodMaschera = aCodMaschera;
		this.mAltCauIdAltraCausa = aAltCauIdAltraCausa;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdPosizioneGiuridica() {
		return mIdPosizioneGiuridica;
	}

	public String getCodPosizioneGiuridica() {
		return mCodPosizioneGiuridica;
	}

	public String getDescrPosizioneGiuridica() {
		return mDescrPosizioneGiuridica;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public String getCodPosizioneProcessuale() {
		return mCodPosizioneProcessuale;
	}

	public String getDescrPosizioneProcessuale() {
		return mDescrPosizioneProcessuale;
	}

	public String getNote() {
		return mNote;
	}

	public String getLuogoProvaAffidamento() {
		return mLuogoProvaAffidamento;
	}

	public String getLuogoLavoroSemiliberta() {
		return mLuogoLavoroSemiliberta;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getIdEventoRiferimento() {
		return mIdEventoRiferimento;
	}

	// rege-sies
	public String getFlagIsDetenuto() {
		return mFlagIsDetenuto;
	}

	public String getLuogoEspiazione() {
		return mLuogoEspiazione;
	}

	public String getAutoritaCompetente() {
		return mAutoritaCompetente;
	}

	public String getAutoritaCompetenteSede() {
		return mAutoritaCompetenteSede;
	}

	public String getAutoritaCompetenteSedeDesc() {
		return mAutoritaCompetenteSedeDesc;
	}

	public String getAutoritaCompetenteIndirizzo() {
		return mAutoritaCompetenteIndirizzo;
	}

	public String getCodMaschera() {
		return mCodMaschera;
	}

	public BigDecimal getAltCauIdAltraCausa() {
		return mAltCauIdAltraCausa;
	}

	// public boolean isPrimaPosizione() { return mPrimaPosizione; }
	//
	// METODI SET()
	//
	public void setIdPosizioneGiuridica(BigDecimal aValore) {
		mIdPosizioneGiuridica = aValore;
	}

	public void setCodPosizioneGiuridica(String aValore) {
		mCodPosizioneGiuridica = aValore;
	}

	public void setDescrPosizioneGiuridica(String aValore) {
		mDescrPosizioneGiuridica = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setCodPosizioneProcessuale(String aValore) {
		mCodPosizioneProcessuale = aValore;
	}

	public void setDescrPosizioneProcessuale(String aValore) {
		mDescrPosizioneProcessuale = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setLuogoProvaAffidamento(String aValore) {
		mLuogoProvaAffidamento = aValore;
	}

	public void setLuogoLavoroSemiliberta(String aValore) {
		mLuogoLavoroSemiliberta = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setIdEventoRiferimento(BigDecimal aValore) {
		mIdEventoRiferimento = aValore;
	}

	// rege-sies
	public void setFlagIsDetenuto(String aValore) {
		mFlagIsDetenuto = aValore;
	}

	public void setLuogoEspiazione(String aValore) {
		mLuogoEspiazione = aValore;
	}

	public void setAutoritaCompetente(String aValore) {
		mAutoritaCompetente = aValore;
	}

	public void setAutoritaCompetenteSede(String aValore) {
		mAutoritaCompetenteSede = aValore;
	}

	public void setAutoritaCompetenteSedeDesc(String aValore) {
		mAutoritaCompetenteSedeDesc = aValore;
	}

	public void setAutoritaCompetenteIndirizzo(String aValore) {
		mAutoritaCompetenteIndirizzo = aValore;
	}

	public void setCodMaschera(String aValore) {
		mCodMaschera = aValore;
	}

	public void setAltCauIdAltraCausa(BigDecimal aValore) {
		mAltCauIdAltraCausa = aValore;
	}

	// public void setPrimaPosizione(boolean aValore ) { mPrimaPosizione = aValore; }

	public boolean isMisAlt() {
		// L'Array contiene i codice corrispondenti
		// all'rv_abbreviation 'MIS_ALT' del dominio POSIZIONE_GIURIDICA
		// della CG_REF_CODES
		Collection lCollMisAlt = DecodificheManager.getInstance().getPosizioneGiuridicaMisAlt();

		String lFiltro = "";
		lFiltro = DecodificheUtils.getFiltrobyCode(lCollMisAlt, mCodPosizioneGiuridica);

		if (lFiltro.equals("MIS_ALT")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isLibero() {
		if (mCodPosizioneGiuridica != null && (mCodPosizioneGiuridica.equals("07") // LIBERO
				|| mCodPosizioneGiuridica.equals("10") // LIBERO
				|| mCodPosizioneGiuridica.equals("16") // LIBERO IN DIFFERIMENTO PENA
				|| mCodPosizioneGiuridica.equals("17") // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
				|| mCodPosizioneGiuridica.equals("46") // LIBERO in Sospensione
				|| mCodPosizioneGiuridica.equals("47") // LIBERO in Sospensione DPR 309/90
				|| mCodPosizioneGiuridica.equals("20") // EVASO
				|| mCodPosizioneGiuridica.equals("26") // ESPULSO
				|| mCodPosizioneGiuridica.equals("30") // ESTRADATO
		)) {
			return true;
		} else
			return false;
	}

	// 12-2010 AMBROSINO - Creazione di isDetenuto() usata in LoadInserisciConcessione
	public boolean isDetenuto() {
		if (mCodPosizioneGiuridica != null && (mCodPosizioneGiuridica.equals("01") // Custodia Cautelare per
																					// Questa Causa in Regime
																					// di Detenzione
				|| mCodPosizioneGiuridica.equals("03") // Espiazione Pena in Regime Carcerario
				|| mCodPosizioneGiuridica.equals("06") // Internato
				|| mCodPosizioneGiuridica.equals("09") // Internato
				|| mCodPosizioneGiuridica.equals("19") // Espiazione Pena Sostitutiva (Semidetenzione)
				|| mCodPosizioneGiuridica.equals("22") // Custodia Cautelare in Regime di Detenzione
				|| mCodPosizioneGiuridica.equals("24") // Espiazione Pena Definitiva in Carcere

		)) {
			return true;
		} else
			return false;
	}

	public boolean isMisSosp() {
		if (mCodPosizioneGiuridica != null && (mCodPosizioneGiuridica.equals("27") // Sospensione Pena Ex L.
																					// 207/03
				|| mCodPosizioneGiuridica.equals("31") // Sospensione Cautelativa 51 Ter (di Det. Domiciliare)
				|| mCodPosizioneGiuridica.equals("32") // Sospensione Cautelativa 51 Ter (di Aff. in Prova)
				|| mCodPosizioneGiuridica.equals("33") // Sospensione Cautelativa 51 Ter (di Semiliberta')
				|| mCodPosizioneGiuridica.equals("34") // Sospensione Cautelativa 51 Ter (di Det. Domiciliare
														// Speciale)
				|| mCodPosizioneGiuridica.equals("35") // Sospensione Cautelativa 51 Ter (di Sospensione Pena
														// Ex L. 207/03)
				|| mCodPosizioneGiuridica.equals("36") // Sospensione Provvisoria 51 Bis (di Det. Domiciliare)
				|| mCodPosizioneGiuridica.equals("37") // Sospensione Provvisoria 51 Bis (di Aff. in Prova)
				|| mCodPosizioneGiuridica.equals("38") // Sospensione Provvisoria 51 Bis (di Semiliberta')
				|| mCodPosizioneGiuridica.equals("39") // Sospensione Provvisoria 51 Bis (di Det. Domiciliare
														// Speciale)
				|| mCodPosizioneGiuridica.equals("40") // Sospensione Provvisoria 51 Bis (di Sospensione Pena
														// Ex L. 207/03)
				|| mCodPosizioneGiuridica.equals("45") // Sospensione Pena Ex L. 207/03 in Estensione
														// Provvisoria 51 Bis
		)) {
			return true;
		} else
			return false;
	}

	public boolean isMisuraAlternativa() {
		if (mCodPosizioneGiuridica != null && (mCodPosizioneGiuridica.equals("11") // Espiazione Pena in
																					// Regime di Liberazione
																					// Condizionale
				|| mCodPosizioneGiuridica.equals("12") // Espiazione Pena in Regime di Detenzione Domiciliare
				|| mCodPosizioneGiuridica.equals("13") // Espiazione Pena in Regime di Affidamento in Prova
				|| mCodPosizioneGiuridica.equals("14") // Espiazione Pena in Regime di Semiliberta'
				|| mCodPosizioneGiuridica.equals("15") // Espiazione Pena Sostitutiva (Liberta' Controllata)
				|| mCodPosizioneGiuridica.equals("25") // Espiazione Pena in Regime di Det. Domiciliare
														// Speciale
				|| mCodPosizioneGiuridica.equals("29") // Detenzione Domiciliare Provvisoria
				|| mCodPosizioneGiuridica.equals("41") // Espiazione Pena in Regime di Det.Domiciliare in
														// Prosec.Provv. 51 Bis
				|| mCodPosizioneGiuridica.equals("42") // Espiazione Pena in Regime di Aff. in Prova in
														// Prosec.Provv. 51 Bis
				|| mCodPosizioneGiuridica.equals("43") // Espiazione Pena in Regime di Semiliberta' in
														// Prosec.Provv. 51 Bis
				|| mCodPosizioneGiuridica.equals("44") // Espiazione Pena in Regime di Det. Dom.Speciale in
														// Est. Provv. 51 Bis
				|| mCodPosizioneGiuridica.equals("50") // Espiazione Pena Presso Domicilio
				|| mCodPosizioneGiuridica.equals("51") // Sospensione cautelativa 51 ter (Esecuzione presso
														// domicilio della pena detentiva)
				|| mCodPosizioneGiuridica.equals("52") // Sospensione cautelativa 51 bis (Esecuzione presso
														// domicilio della pena detentiva)
				|| mCodPosizioneGiuridica.equals("53") // Arresti domiciliari - Esecuzione presso domicilio
														// della pena detentiva
				|| mCodPosizioneGiuridica.equals("54") // Affidamento in Prova Provvisoria
		)) {
			return true;
		} else
			return false;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdPosizioneGiuridica + " - " + mCodPosizioneGiuridica + " - " + mDescrPosizioneGiuridica
				+ " - " + mDataInizio + " - " + mDataFine + " - " + mCodPosizioneProcessuale + " - "
				+ mDescrPosizioneProcessuale + " - " + mNote + " - " + mLuogoProvaAffidamento + " - "
				+ mLuogoLavoroSemiliberta + " - " + mCodOperatoreInserimento + " - " + mDataInserimento
				+ " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mFasSieIdFascicoloSiep + " - "
				+ mIdEventoRiferimento + " - " + mAltCauIdAltraCausa + " - " + mFlagIsDetenuto;
		// mPrimaPosizione;

		return lStr;
	}
}
