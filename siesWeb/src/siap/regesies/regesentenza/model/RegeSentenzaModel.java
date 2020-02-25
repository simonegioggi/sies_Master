package siap.regesies.regesentenza.model;

import java.util.Date;

import siap.regesies.model.RegeModel;
import siap.siep.sentenza.model.SentenzaModel;

/**
 * <p>
 * Title: RegeSentenzaModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il RegeSentenza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 */
public class RegeSentenzaModel extends RegeModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 5464432110354206768L;

	private String mIdFile;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private int mAnnoRegePm;
	private String mNumeroRegePm;
	private Date mDataArrivoAtto;
	private Date mDataIscrizione;

	private Date mDataProvvedimento;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private String mNumSezioneAutoritaEmittente;
	private int mAnnoSentenza;
	private String mNumeroSentenza;
	private Date mDataIrrevocabilita;
	private String mFlagSentenzaApplicazPena;
	private String mCodTipoProvvRif;
	private String mDescrTipoProvvRif;
	private Date mDataProvvRif;
	private String mCodTipoAutoritaProvvRif;
	private String mDescrTipoAutoritaProvvRif;
	private int mAnnoProvvRif;
	private String mNumeroProvvRif;
	private String mCodLuogoProvvRif;
	private String mDescrLuogoProvvRif;
	private String mNumSezioneAutoritaProvvRif;
	private String mCodTipoDecisioneCassazione;
	private String mDescrTipoDecisioneCassazione;
	private int mAnnoSentenzaCassazione;
	private String mNumeroSentenzaCassazione;
	private int mAnnoRaccoltaGenerale;
	private String mNumeroRaccoltaGenerale;
	private int mAnnoRegistro35;
	private String mNumRegistro35;
	private String mNote;
	private String mDescrNumCampionePenale;
	private int mAnnoRegeGip;
	private String mNumeroRegeGip;
	private int mAnnoRegeDib;
	private String mNumeroRegeDib;
	private int mAnnoRegeCas;
	private String mNumeroRegeCas;
	private int mAnnoRegeCap;
	private String mNumeroRegeCap;
	private int mAnnoRegeCasap;
	private String mNumeroRegeCasap;
	private String mNotaDispositivo;
	private String mCodTipoRito;
	private String mDescrTipoRito;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mFlagGiudizioAbbreviato;
	private String mNote1DecisioneCassazione;
	private String mNote2DecisioneCassazione;

	// Attributi per l'elenco dei provveddimenti
	private String mFlagPiuSoggetti;
	private int mCountSoggetti;

	// COSTRUTTORE DI DEFAULT
	public RegeSentenzaModel() {
		this.mIdFile = "";
		this.mCodTipoProvvedimento = "-";
		this.mDescrTipoProvvedimento = "";
		this.mAnnoRegePm = 0;
		this.mNumeroRegePm = "";
		this.mDataArrivoAtto = null;
		this.mDataIscrizione = null;

		this.mDataProvvedimento = null;
		this.mCodTipoAutoritaEmittente = "-";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodLuogoEmittente = "-";
		this.mDescrLuogoEmittente = "";
		this.mNumSezioneAutoritaEmittente = "";
		this.mAnnoSentenza = 0;
		this.mNumeroSentenza = "";
		this.mDataIrrevocabilita = null;
		this.mFlagSentenzaApplicazPena = "";
		this.mCodTipoProvvRif = "-";
		this.mDescrTipoProvvRif = "";
		this.mDataProvvRif = null;
		this.mCodTipoAutoritaProvvRif = "-";
		this.mDescrTipoAutoritaProvvRif = "";
		this.mAnnoProvvRif = 0;
		this.mNumeroProvvRif = "";
		this.mCodLuogoProvvRif = "-";
		this.mDescrLuogoProvvRif = "";
		this.mNumSezioneAutoritaProvvRif = "";
		this.mCodTipoDecisioneCassazione = "-";
		this.mDescrTipoDecisioneCassazione = "";
		this.mAnnoSentenzaCassazione = 0;
		this.mNumeroSentenzaCassazione = "";
		this.mAnnoRaccoltaGenerale = 0;
		this.mNumeroRaccoltaGenerale = "";
		this.mAnnoRegistro35 = 0;
		this.mNumRegistro35 = "";
		this.mNote = "";
		this.mDescrNumCampionePenale = "";
		this.mAnnoRegeGip = 0;
		this.mNumeroRegeGip = "";
		this.mAnnoRegeDib = 0;
		this.mNumeroRegeDib = "";
		this.mAnnoRegeCas = 0;
		this.mNumeroRegeCas = "";
		this.mAnnoRegeCap = 0;
		this.mNumeroRegeCap = "";
		this.mAnnoRegeCasap = 0;
		this.mNumeroRegeCasap = "";
		this.mNotaDispositivo = "";
		this.mCodTipoRito = "-";
		this.mDescrTipoRito = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFlagGiudizioAbbreviato = "";
		this.mFlagPiuSoggetti = "N";
		this.mCountSoggetti = 0;
		this.mNote1DecisioneCassazione = "";
		this.mNote2DecisioneCassazione = "";
	}

	// COSTRUTTORE DI COPIA
	public RegeSentenzaModel(RegeSentenzaModel aModel) {
		this.mIdFile = aModel.mIdFile;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mAnnoRegePm = aModel.mAnnoRegePm;
		this.mNumeroRegePm = aModel.mNumeroRegePm;
		this.mDataArrivoAtto = aModel.mDataArrivoAtto;
		this.mDataIscrizione = aModel.mDataIscrizione;

		this.mDataProvvedimento = aModel.mDataProvvedimento;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mNumSezioneAutoritaEmittente = aModel.mNumSezioneAutoritaEmittente;
		this.mAnnoSentenza = aModel.mAnnoSentenza;
		this.mNumeroSentenza = aModel.mNumeroSentenza;
		this.mDataIrrevocabilita = aModel.mDataIrrevocabilita;
		this.mFlagSentenzaApplicazPena = aModel.mFlagSentenzaApplicazPena;
		this.mCodTipoProvvRif = aModel.mCodTipoProvvRif;
		this.mDescrTipoProvvRif = aModel.mDescrTipoProvvRif;
		this.mDataProvvRif = aModel.mDataProvvRif;
		this.mCodTipoAutoritaProvvRif = aModel.mCodTipoAutoritaProvvRif;
		this.mDescrTipoAutoritaProvvRif = aModel.mDescrTipoAutoritaProvvRif;
		this.mAnnoProvvRif = aModel.mAnnoProvvRif;
		this.mNumeroProvvRif = aModel.mNumeroProvvRif;
		this.mCodLuogoProvvRif = aModel.mCodLuogoProvvRif;
		this.mDescrLuogoProvvRif = aModel.mDescrLuogoProvvRif;
		this.mNumSezioneAutoritaProvvRif = aModel.mNumSezioneAutoritaProvvRif;
		this.mCodTipoDecisioneCassazione = aModel.mCodTipoDecisioneCassazione;
		this.mDescrTipoDecisioneCassazione = aModel.mDescrTipoDecisioneCassazione;
		this.mAnnoSentenzaCassazione = aModel.mAnnoSentenzaCassazione;
		this.mNumeroSentenzaCassazione = aModel.mNumeroSentenzaCassazione;
		this.mAnnoRaccoltaGenerale = aModel.mAnnoRaccoltaGenerale;
		this.mNumeroRaccoltaGenerale = aModel.mNumeroRaccoltaGenerale;
		this.mAnnoRegistro35 = aModel.mAnnoRegistro35;
		this.mNumRegistro35 = aModel.mNumRegistro35;
		this.mNote = aModel.mNote;
		this.mDescrNumCampionePenale = aModel.mDescrNumCampionePenale;
		this.mAnnoRegeGip = aModel.mAnnoRegeGip;
		this.mNumeroRegeGip = aModel.mNumeroRegeGip;
		this.mAnnoRegeDib = aModel.mAnnoRegeDib;
		this.mNumeroRegeDib = aModel.mNumeroRegeDib;
		this.mAnnoRegeCas = aModel.mAnnoRegeCas;
		this.mNumeroRegeCas = aModel.mNumeroRegeCas;
		this.mAnnoRegeCap = aModel.mAnnoRegeCap;
		this.mNumeroRegeCap = aModel.mNumeroRegeCap;
		this.mAnnoRegeCasap = aModel.mAnnoRegeCasap;
		this.mNumeroRegeCasap = aModel.mNumeroRegeCasap;
		this.mNotaDispositivo = aModel.mNotaDispositivo;
		this.mCodTipoRito = aModel.mCodTipoRito;
		this.mDescrTipoRito = aModel.mDescrTipoRito;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFlagGiudizioAbbreviato = aModel.mFlagGiudizioAbbreviato;
		this.mFlagPiuSoggetti = aModel.mFlagPiuSoggetti;
		this.mCountSoggetti = aModel.mCountSoggetti;
		this.mNote1DecisioneCassazione = aModel.mNote1DecisioneCassazione;
		this.mNote2DecisioneCassazione = aModel.mNote2DecisioneCassazione;
	}

	// COSTRUTTORE MODEL
	public RegeSentenzaModel(String aIdFile, String aCodTipoProvvedimento, String aDescrTipoProvvedimento,
			int aAnnoRegePm, String aNumeroRegePm, Date aDataArrivoAtto, Date aDataIscrizione,

			Date aDataProvvedimento, String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente,
			String aCodLuogoEmittente, String aDescrLuogoEmittente, String aNumSezioneAutoritaEmittente,
			int aAnnoSentenza, String aNumeroSentenza, Date aDataIrrevocabilita,
			String aFlagSentenzaApplicazPena, String aCodTipoProvvRif, String aDescrTipoProvvRif,
			Date aDataProvvRif, String aCodTipoAutoritaProvvRif, String aDescrTipoAutoritaProvvRif,
			int aAnnoProvvRif, String aNumeroProvvRif, String aCodLuogoProvvRif, String aDescrLuogoProvvRif,
			String aNumSezioneAutoritaProvvRif, String aCodTipoDecisioneCassazione,
			String aDescrTipoDecisioneCassazione, int aAnnoSentenzaCassazione,
			String aNumeroSentenzaCassazione, int aAnnoRaccoltaGenerale, String aNumeroRaccoltaGenerale,
			int aAnnoRegistro35, String aNumRegistro35, String aNote, String aDescrNumCampionePenale,
			int aAnnoRegeGip, String aNumeroRegeGip, int aAnnoRegeDib, String aNumeroRegeDib,
			int aAnnoRegeCas, String aNumeroRegeCas, int aAnnoRegeCap, String aNumeroRegeCap,
			int aAnnoRegeCasap, String aNumeroRegeCasap, String aNotaDispositivo, String aCodTipoRito,
			String aDescrTipoRito, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			String aFlagGiudizioAbbreviato) {
		this.mIdFile = aIdFile;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mAnnoRegePm = aAnnoRegePm;
		this.mNumeroRegePm = aNumeroRegePm;
		this.mDataArrivoAtto = aDataArrivoAtto;
		this.mDataIscrizione = aDataIscrizione;
		this.mDataProvvedimento = aDataProvvedimento;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mNumSezioneAutoritaEmittente = aNumSezioneAutoritaEmittente;
		this.mAnnoSentenza = aAnnoSentenza;
		this.mNumeroSentenza = aNumeroSentenza;
		this.mDataIrrevocabilita = aDataIrrevocabilita;
		this.mFlagSentenzaApplicazPena = aFlagSentenzaApplicazPena;
		this.mCodTipoProvvRif = aCodTipoProvvRif;
		this.mDescrTipoProvvRif = aDescrTipoProvvRif;
		this.mDataProvvRif = aDataProvvRif;
		this.mCodTipoAutoritaProvvRif = aCodTipoAutoritaProvvRif;
		this.mDescrTipoAutoritaProvvRif = aDescrTipoAutoritaProvvRif;
		this.mAnnoProvvRif = aAnnoProvvRif;
		this.mNumeroProvvRif = aNumeroProvvRif;
		this.mCodLuogoProvvRif = aCodLuogoProvvRif;
		this.mDescrLuogoProvvRif = aDescrLuogoProvvRif;
		this.mNumSezioneAutoritaProvvRif = aNumSezioneAutoritaProvvRif;
		this.mCodTipoDecisioneCassazione = aCodTipoDecisioneCassazione;
		this.mDescrTipoDecisioneCassazione = aDescrTipoDecisioneCassazione;
		this.mAnnoSentenzaCassazione = aAnnoSentenzaCassazione;
		this.mNumeroSentenzaCassazione = aNumeroSentenzaCassazione;
		this.mAnnoRaccoltaGenerale = aAnnoRaccoltaGenerale;
		this.mNumeroRaccoltaGenerale = aNumeroRaccoltaGenerale;
		this.mAnnoRegistro35 = aAnnoRegistro35;
		this.mNumRegistro35 = aNumRegistro35;
		this.mNote = aNote;
		this.mDescrNumCampionePenale = aDescrNumCampionePenale;
		this.mAnnoRegeGip = aAnnoRegeGip;
		this.mNumeroRegeGip = aNumeroRegeGip;
		this.mAnnoRegeDib = aAnnoRegeDib;
		this.mNumeroRegeDib = aNumeroRegeDib;
		this.mAnnoRegeCas = aAnnoRegeCas;
		this.mNumeroRegeCas = aNumeroRegeCas;
		this.mAnnoRegeCap = aAnnoRegeCap;
		this.mNumeroRegeCap = aNumeroRegeCap;
		this.mAnnoRegeCasap = aAnnoRegeCasap;
		this.mNumeroRegeCasap = aNumeroRegeCasap;
		this.mNotaDispositivo = aNotaDispositivo;
		this.mCodTipoRito = aCodTipoRito;
		this.mDescrTipoRito = aDescrTipoRito;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFlagGiudizioAbbreviato = aFlagGiudizioAbbreviato;
	}

	//
	// METODI GET()
	//

	public String getIdFile() {
		return mIdFile;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public int getAnnoRegePm() {
		return mAnnoRegePm;
	}

	public String getNumeroRegePm() {
		return mNumeroRegePm;
	}

	public Date getDataArrivoAtto() {
		return mDataArrivoAtto;
	}

	public Date getDataIscrizione() {
		return mDataIscrizione;
	}

	public Date getDataProvvedimento() {
		return mDataProvvedimento;
	}

	public String getCodTipoAutoritaEmittente() {
		return mCodTipoAutoritaEmittente;
	}

	public String getDescrTipoAutoritaEmittente() {
		return mDescrTipoAutoritaEmittente;
	}

	public String getCodLuogoEmittente() {
		return mCodLuogoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	public String getNumSezioneAutoritaEmittente() {
		return mNumSezioneAutoritaEmittente;
	}

	public int getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public String getNumeroSentenza() {
		return mNumeroSentenza;
	}

	public Date getDataIrrevocabilita() {
		return mDataIrrevocabilita;
	}

	public String getFlagSentenzaApplicazPena() {
		return mFlagSentenzaApplicazPena;
	}

	public String getCodTipoProvvRif() {
		return mCodTipoProvvRif;
	}

	public String getDescrTipoProvvRif() {
		return mDescrTipoProvvRif;
	}

	public Date getDataProvvRif() {
		return mDataProvvRif;
	}

	public String getCodTipoAutoritaProvvRif() {
		return mCodTipoAutoritaProvvRif;
	}

	public String getDescrTipoAutoritaProvvRif() {
		return mDescrTipoAutoritaProvvRif;
	}

	public int getAnnoProvvRif() {
		return mAnnoProvvRif;
	}

	public String getNumeroProvvRif() {
		return mNumeroProvvRif;
	}

	public String getCodLuogoProvvRif() {
		return mCodLuogoProvvRif;
	}

	public String getDescrLuogoProvvRif() {
		return mDescrLuogoProvvRif;
	}

	public String getNumSezioneAutoritaProvvRif() {
		return mNumSezioneAutoritaProvvRif;
	}

	public String getCodTipoDecisioneCassazione() {
		return mCodTipoDecisioneCassazione;
	}

	public String getDescrTipoDecisioneCassazione() {
		return mDescrTipoDecisioneCassazione;
	}

	public int getAnnoSentenzaCassazione() {
		return mAnnoSentenzaCassazione;
	}

	public String getNumeroSentenzaCassazione() {
		return mNumeroSentenzaCassazione;
	}

	public int getAnnoRaccoltaGenerale() {
		return mAnnoRaccoltaGenerale;
	}

	public String getNumeroRaccoltaGenerale() {
		return mNumeroRaccoltaGenerale;
	}

	public int getAnnoRegistro35() {
		return mAnnoRegistro35;
	}

	public String getNumRegistro35() {
		return mNumRegistro35;
	}

	public String getNote() {
		return mNote;
	}

	public String getDescrNumCampionePenale() {
		return mDescrNumCampionePenale;
	}

	public int getAnnoRegeGip() {
		return mAnnoRegeGip;
	}

	public String getNumeroRegeGip() {
		return mNumeroRegeGip;
	}

	public int getAnnoRegeDib() {
		return mAnnoRegeDib;
	}

	public String getNumeroRegeDib() {
		return mNumeroRegeDib;
	}

	public int getAnnoRegeCas() {
		return mAnnoRegeCas;
	}

	public String getNumeroRegeCas() {
		return mNumeroRegeCas;
	}

	public int getAnnoRegeCap() {
		return mAnnoRegeCap;
	}

	public String getNumeroRegeCap() {
		return mNumeroRegeCap;
	}

	public int getAnnoRegeCasap() {
		return mAnnoRegeCasap;
	}

	public String getNumeroRegeCasap() {
		return mNumeroRegeCasap;
	}

	public String getNotaDispositivo() {
		return mNotaDispositivo;
	}

	public String getCodTipoRito() {
		return mCodTipoRito;
	}

	public String getDescrTipoRito() {
		return mDescrTipoRito;
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

	public String getFlagGiudizioAbbreviato() {
		return mFlagGiudizioAbbreviato;
	}

	public String getFlagPiuSoggetti() {
		return mFlagPiuSoggetti;
	}

	public int getCountSoggetti() {
		return this.mCountSoggetti;
	}

	public String getNote1DecisioneCassazione() {
		return this.mNote1DecisioneCassazione;
	}

	public String getNote2DecisioneCassazione() {
		return this.mNote2DecisioneCassazione;
	}

	//
	// METODI SET()
	//

	public void setIdFile(String aValore) {
		mIdFile = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setAnnoRegePm(int aValore) {
		mAnnoRegePm = aValore;
	}

	public void setNumeroRegePm(String aValore) {
		mNumeroRegePm = aValore;
	}

	public void setDataArrivoAtto(Date aValore) {
		mDataArrivoAtto = aValore;
	}

	public void setDataIscrizione(Date aValore) {
		mDataIscrizione = aValore;
	}

	public void setDataProvvedimento(Date aValore) {
		mDataProvvedimento = aValore;
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		mCodTipoAutoritaEmittente = aValore;
	}

	public void setDescrTipoAutoritaEmittente(String aValore) {
		mDescrTipoAutoritaEmittente = aValore;
	}

	public void setCodLuogoEmittente(String aValore) {
		mCodLuogoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	public void setNumSezioneAutoritaEmittente(String aValore) {
		mNumSezioneAutoritaEmittente = aValore;
	}

	public void setAnnoSentenza(int aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumeroSentenza(String aValore) {
		mNumeroSentenza = aValore;
	}

	public void setDataIrrevocabilita(Date aValore) {
		mDataIrrevocabilita = aValore;
	}

	public void setFlagSentenzaApplicazPena(String aValore) {
		mFlagSentenzaApplicazPena = aValore;
	}

	public void setCodTipoProvvRif(String aValore) {
		mCodTipoProvvRif = aValore;
	}

	public void setDescrTipoProvvRif(String aValore) {
		mDescrTipoProvvRif = aValore;
	}

	public void setDataProvvRif(Date aValore) {
		mDataProvvRif = aValore;
	}

	public void setCodTipoAutoritaProvvRif(String aValore) {
		mCodTipoAutoritaProvvRif = aValore;
	}

	public void setDescrTipoAutoritaProvvRif(String aValore) {
		mDescrTipoAutoritaProvvRif = aValore;
	}

	public void setAnnoProvvRif(int aValore) {
		mAnnoProvvRif = aValore;
	}

	public void setNumeroProvvRif(String aValore) {
		mNumeroProvvRif = aValore;
	}

	public void setCodLuogoProvvRif(String aValore) {
		mCodLuogoProvvRif = aValore;
	}

	public void setDescrLuogoProvvRif(String aValore) {
		mDescrLuogoProvvRif = aValore;
	}

	public void setNumSezioneAutoritaProvvRif(String aValore) {
		mNumSezioneAutoritaProvvRif = aValore;
	}

	public void setCodTipoDecisioneCassazione(String aValore) {
		mCodTipoDecisioneCassazione = aValore;
	}

	public void setDescrTipoDecisioneCassazione(String aValore) {
		mDescrTipoDecisioneCassazione = aValore;
	}

	public void setAnnoSentenzaCassazione(int aValore) {
		mAnnoSentenzaCassazione = aValore;
	}

	public void setNumeroSentenzaCassazione(String aValore) {
		mNumeroSentenzaCassazione = aValore;
	}

	public void setAnnoRaccoltaGenerale(int aValore) {
		mAnnoRaccoltaGenerale = aValore;
	}

	public void setNumeroRaccoltaGenerale(String aValore) {
		mNumeroRaccoltaGenerale = aValore;
	}

	public void setAnnoRegistro35(int aValore) {
		mAnnoRegistro35 = aValore;
	}

	public void setNumRegistro35(String aValore) {
		mNumRegistro35 = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setDescrNumCampionePenale(String aValore) {
		mDescrNumCampionePenale = aValore;
	}

	public void setAnnoRegeGip(int aValore) {
		mAnnoRegeGip = aValore;
	}

	public void setNumeroRegeGip(String aValore) {
		mNumeroRegeGip = aValore;
	}

	public void setAnnoRegeDib(int aValore) {
		mAnnoRegeDib = aValore;
	}

	public void setNumeroRegeDib(String aValore) {
		mNumeroRegeDib = aValore;
	}

	public void setAnnoRegeCas(int aValore) {
		mAnnoRegeCas = aValore;
	}

	public void setNumeroRegeCas(String aValore) {
		mNumeroRegeCas = aValore;
	}

	public void setAnnoRegeCap(int aValore) {
		mAnnoRegeCap = aValore;
	}

	public void setNumeroRegeCap(String aValore) {
		mNumeroRegeCap = aValore;
	}

	public void setAnnoRegeCasap(int aValore) {
		mAnnoRegeCasap = aValore;
	}

	public void setNumeroRegeCasap(String aValore) {
		mNumeroRegeCasap = aValore;
	}

	public void setNotaDispositivo(String aValore) {
		mNotaDispositivo = aValore;
	}

	public void setCodTipoRito(String aValore) {
		mCodTipoRito = aValore;
	}

	public void setDescrTipoRito(String aValore) {
		mDescrTipoRito = aValore;
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

	public void setFlagGiudizioAbbreviato(String aValore) {
		mFlagGiudizioAbbreviato = aValore;
	}

	public void setFlagPiuSoggetti(String aValore) {
		mFlagPiuSoggetti = aValore;
	}

	public void setCountSoggetti(int aValore) {
		mCountSoggetti = aValore;
	}

	public void setNote1DecisioneCassazione(String aValore) {
		mNote1DecisioneCassazione = aValore;
	}

	public void setNote2DecisioneCassazione(String aValore) {
		mNote2DecisioneCassazione = aValore;
	}

	/**
	 * Trasformazione del RegeSentenzaModel in SentenzaModel gli attributi commentati non sono presenti nel
	 * RegeSentenzaModel
	 * 
	 * @return SentenzaModel
	 */
	public SentenzaModel toSentenza() {
		SentenzaModel lSentenza = new SentenzaModel();
		// -- lSentenza.setIdSentenza(null);
		lSentenza.setCodTipoProvvedimento(this.mCodTipoProvvedimento);
		lSentenza.setDescrTipoProvvedimento(this.mDescrTipoProvvedimento);
		// -- lSentenza.setAnnoRegistroGenerale(new BigDecimal(this.mAnnoRegistroGenerale));
		// -- lSentenza.setNumeroRegistroGenerale(this.mNumeroRegistroGenerale);
		lSentenza.setAnnoRegePm(toBigDecimal(mAnnoRegePm));
		lSentenza.setNumeroRegePm(this.mNumeroRegePm);
		// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
		// lSentenza.setDataArrivoAtto(this.mDataArrivoAtto);
		lSentenza.setDataIscrizione(this.mDataIscrizione);
		lSentenza.setDataProvvedimento(this.mDataProvvedimento);
		lSentenza.setCodTipoAutoritaEmittente(this.mCodTipoAutoritaEmittente);
		lSentenza.setDescrTipoAutoritaEmittente(this.mDescrTipoAutoritaEmittente);
		lSentenza.setCodLuogoEmittente(this.mCodLuogoEmittente);
		lSentenza.setDescrLuogoEmittente(this.mDescrLuogoEmittente);
		lSentenza.setNumSezioneAutoritaEmittente(this.mNumSezioneAutoritaEmittente);
		lSentenza.setAnnoSentenza(toBigDecimal(mAnnoSentenza));
		lSentenza.setNumeroSentenza(this.mNumeroSentenza);
		// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
		// lSentenza.setDataIrrevocabilita(this.mDataIrrevocabilita);
		lSentenza.setDataSentenza(this.mDataProvvedimento);
		// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
		// lSentenza.setFlagSentenzaApplicazPena(this.mFlagSentenzaApplicazPena);
		lSentenza.setCodTipoProvvRif(this.mCodTipoProvvRif);
		lSentenza.setDescrTipoProvvRif(this.mDescrTipoProvvRif);
		lSentenza.setDataProvvRif(this.mDataProvvRif);
		lSentenza.setCodTipoAutoritaProvvRif(this.mCodTipoAutoritaProvvRif);
		lSentenza.setDescrTipoAutoritaProvvRif(this.mDescrTipoAutoritaProvvRif);
		lSentenza.setAnnoProvvRif(toBigDecimal(mAnnoProvvRif));
		lSentenza.setNumeroProvvRif(this.mNumeroProvvRif);
		lSentenza.setCodLuogoProvvRif(this.mCodLuogoProvvRif);
		lSentenza.setDescrLuogoProvvRif(this.mDescrLuogoProvvRif);
		lSentenza.setNumSezioneAutoritaProvvRif(this.mNumSezioneAutoritaProvvRif);
		lSentenza.setCodTipoDecisioneCassazione(this.mCodTipoDecisioneCassazione);
		lSentenza.setDescrTipoDecisioneCassazione(this.mDescrTipoDecisioneCassazione);
		lSentenza.setNote1DecisioneCassazione(this.mNote1DecisioneCassazione);
		lSentenza.setNote2DecisioneCassazione(this.mNote2DecisioneCassazione);
		lSentenza.setAnnoSentenzaCassazione(toBigDecimal(mAnnoSentenzaCassazione));
		lSentenza.setNumeroSentenzaCassazione(this.mNumeroSentenzaCassazione);
		lSentenza.setAnnoRaccoltaGenerale(toBigDecimal(mAnnoRaccoltaGenerale));
		lSentenza.setNumeroRaccoltaGenerale(this.mNumeroRaccoltaGenerale);
		// --lSentenza.setFlagAltreSentenze(this.mFlagAltreSentenze);
		// --lSentenza.setDescrAltreSentenze(this.mDescrAltreSentenze);
		lSentenza.setAnnoRegistro35(toBigDecimal(mAnnoRegistro35));
		lSentenza.setNumRegistro35(this.mNumRegistro35);
		lSentenza.setNote(this.mNote);
		// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
		// lSentenza.setDescrNumCampionePenale(this.mDescrNumCampionePenale);
		lSentenza.setAnnoRegeGip(toBigDecimal(mAnnoRegeGip));
		lSentenza.setNumeroRegeGip(this.mNumeroRegeGip);
		lSentenza.setAnnoRegeDib(toBigDecimal(mAnnoRegeDib));
		lSentenza.setNumeroRegeDib(this.mNumeroRegeDib);
		lSentenza.setAnnoRegeCas(toBigDecimal(mAnnoRegeCas));
		lSentenza.setNumeroRegeCas(this.mNumeroRegeCas);
		lSentenza.setAnnoRegeCap(toBigDecimal(mAnnoRegeCap));
		lSentenza.setNumeroRegeCap(this.mNumeroRegeCap);
		lSentenza.setAnnoRegeCasap(toBigDecimal(mAnnoRegeCasap));
		lSentenza.setNumeroRegeCasap(this.mNumeroRegeCasap);
		lSentenza.setCodOperatoreInserimento(this.mCodOperatoreInserimento);
		lSentenza.setDataInserimento(this.mDataInserimento);
		lSentenza.setCodUfficioInserimento(this.mCodUfficioInserimento);
		lSentenza.setDescrUfficioInserimento(this.mDescrUfficioInserimento);
		lSentenza.setCodOperatoreAggiornamento(this.mCodOperatoreAggiornamento);
		lSentenza.setDataAggiornamento(this.mDataAggiornamento);
		lSentenza.setCodUfficioAggiornamento(this.mCodUfficioAggiornamento);
		lSentenza.setDescrUfficioAggiornamento(this.mDescrUfficioAggiornamento);
		// -- lSentenza.setCodBilanciamentoCircostanze(this.mCodBilanciamentoCircostanze);
		// -- lSentenza.setDescrBilanciamentoCircostanze(this.mDescrBilanciamentoCircostanze);
		lSentenza.setFlagGiudizioAbbreviato(this.mFlagGiudizioAbbreviato);
		lSentenza.setCodBilanciamentoCircostanze("-");

		// -- lSentenza.setDataProvvedimentoIniziale(this.mDataProvvedimentoIniziale);
		// -- lSentenza.setDataProvvedimentoFinale(this.mDataProvvedimentoFinale);
		// -- lSentenza.setDataIrrevocabilitaIniziale(this.mDataIrrevocabilitaIniziale);
		// -- lSentenza.setDataIrrevocabilitaFinale(this.mDataIrrevocabilitaFinale);
		// -- lSentenza.setCodOrdinamento(this.mCodOrdinamento);

		return lSentenza;
	}

}