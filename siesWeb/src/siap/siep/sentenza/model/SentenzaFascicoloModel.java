package siap.siep.sentenza.model;

/**
* <p>Title: SentenzaModel</p>
* <p>Description: Classe Model che rappresenta il Sentenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class SentenzaFascicoloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2722183004892165669L;

	private BigDecimal mIdSentenza;
	private String mCodDpr;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private Date mDataProvvedimento;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private String mNumSezioneAutoritaEmittente;
	private Date mDataIrrevocabilita;
	private BigDecimal mAnnoSentenza;
	private String mNumSentenza;

	private BigDecimal mAnnoRegeGip;
	private String mNumeroRegeGip;
	private BigDecimal mAnnoRegeDib;
	private String mNumeroRegeDib;
	private BigDecimal mAnnoRegeCas;
	private String mNumeroRegeCas;
	private BigDecimal mAnnoRegeCap;
	private String mNumeroRegeCap;
	private BigDecimal mAnnoRegeCasap;
	private String mNumeroRegeCasap;
	private BigDecimal mAnnoRegistroGenerale;
	private String mNumeroRegistroGenerale;
	private BigDecimal mAnnoRegePm;
	private String mNumeroRegePm;

	/* #### */
	private BigDecimal mIdFascicoloSiep;
	private String mCodiceStatoFascicolo;
	private Date mDataIscrizione;
	private BigDecimal mChiaveAnnoFascicolo;
	private BigDecimal mChiaveNumeroFascicolo;
	private BigDecimal mSenIdSentenza;
	private BigDecimal mSogIdSoggetto;
	private String mChiaveUfficio;
	private String mDescrChiaveUfficio;
	private String mDescrComune;
	private String mFlagValidato;
	private String mDescrStatoFasc;

	// Attributo che serve per la cancellazione
	// private boolean mEsistonoFascicoliAssociati;

	// COSTRUTTORE DI DEFAULT
	public SentenzaFascicoloModel() {
		this.mIdSentenza = null;
		this.mCodDpr = "";
		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mDataProvvedimento = null;
		this.mCodTipoAutoritaEmittente = "";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";
		this.mNumSezioneAutoritaEmittente = "";
		this.mDataIrrevocabilita = null;
		this.mAnnoSentenza = null;
		this.mNumSentenza = "";

		this.mAnnoRegeGip = null;
		this.mNumeroRegeGip = null;
		this.mAnnoRegeDib = null;
		this.mNumeroRegeDib = null;
		this.mAnnoRegeCas = null;
		this.mNumeroRegeCas = null;
		this.mAnnoRegeCap = null;
		this.mNumeroRegeCap = null;
		this.mAnnoRegeCasap = null;
		this.mNumeroRegeCasap = null;
		this.mAnnoRegistroGenerale = null;
		this.mNumeroRegistroGenerale = null;
		this.mAnnoRegePm = null;
		this.mNumeroRegePm = null;

		/* #### */
		this.mIdFascicoloSiep = null;
		this.mCodiceStatoFascicolo = "";
		this.mDataIscrizione = null;
		this.mChiaveAnnoFascicolo = null;
		this.mChiaveNumeroFascicolo = null;
		this.mSenIdSentenza = null;
		this.mSogIdSoggetto = null;
		this.mChiaveUfficio = "";
		this.mDescrChiaveUfficio = "";
		this.mDescrComune = "";
		this.mFlagValidato = "";
		this.mDescrStatoFasc = "";
	}

	// COSTRUTTORE DI COPIA
	public SentenzaFascicoloModel(SentenzaFascicoloModel aModel) {
		this.mIdSentenza = aModel.mIdSentenza;
		this.mCodDpr = aModel.mCodDpr;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mDataProvvedimento = aModel.mDataProvvedimento;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mNumSezioneAutoritaEmittente = aModel.mNumSezioneAutoritaEmittente;
		this.mDataIrrevocabilita = aModel.mDataIrrevocabilita;
		this.mAnnoSentenza = aModel.mAnnoSentenza;
		this.mNumSentenza = aModel.mNumSentenza;

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
		this.mAnnoRegistroGenerale = aModel.mAnnoRegistroGenerale;
		this.mNumeroRegistroGenerale = aModel.mNumeroRegistroGenerale;
		this.mAnnoRegePm = aModel.mAnnoRegePm;
		this.mNumeroRegePm = aModel.mNumeroRegePm;
		/* #### */
		this.mIdFascicoloSiep = aModel.mIdFascicoloSiep;
		this.mCodiceStatoFascicolo = aModel.mCodiceStatoFascicolo;
		this.mDataIscrizione = aModel.mDataIscrizione;
		this.mChiaveAnnoFascicolo = aModel.mChiaveAnnoFascicolo;
		this.mChiaveNumeroFascicolo = aModel.mChiaveNumeroFascicolo;
		this.mSenIdSentenza = aModel.mSenIdSentenza;
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mChiaveUfficio = aModel.mChiaveUfficio;
		this.mDescrChiaveUfficio = aModel.mDescrChiaveUfficio;
		this.mDescrComune = aModel.mDescrComune;
		this.mFlagValidato = aModel.mFlagValidato;
		this.mDescrStatoFasc = aModel.mDescrStatoFasc;
	}

	// COSTRUTTORE MODEL
	public SentenzaFascicoloModel(BigDecimal aIdSentenza, String aCodDpr, String aCodTipoProvvedimento,
			String aDescrTipoProvvedimento, Date aDataProvvedimento, String aCodTipoAutoritaEmittente,
			String aDescrTipoAutoritaEmittente, String aCodLuogoEmittente, String aDescrLuogoEmittente,
			String aNumSezioneAutoritaEmittente, Date aDataIrrevocabilita, BigDecimal aAnnoSentenza,
			String aNumSentenza,

			BigDecimal aAnnoRegeGip, String aNumeroRegeGip, BigDecimal aAnnoRegeDib, String aNumeroRegeDib,
			BigDecimal aAnnoRegeCas, String aNumeroRegeCas, BigDecimal aAnnoRegeCap, String aNumeroRegeCap,
			BigDecimal aAnnoRegeCasap, String aNumeroRegeCasap, BigDecimal aAnnoRegistroGenerale,
			String aNumeroRegistroGenerale, BigDecimal aAnnoRegePm, String aNumeroRegePm,

			BigDecimal aIdFascicoloSiep, String aCodiceStatoFascicolo, Date aDataIscrizione,
			BigDecimal aChiaveAnnoFascicolo, BigDecimal aChiaveNumeroFascicolo, BigDecimal aSenIdSentenza,
			BigDecimal aSogIdSoggetto, String aChiaveUfficio, String aDescrChiaveUfficio, String aDescrComune,
			String aFlagValidato, String aDescrStatoFasc) {
		this.mIdSentenza = aIdSentenza;
		this.mCodDpr = aCodDpr;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mDataProvvedimento = aDataProvvedimento;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mNumSezioneAutoritaEmittente = aNumSezioneAutoritaEmittente;
		this.mDataIrrevocabilita = aDataIrrevocabilita;
		this.mAnnoSentenza = aAnnoSentenza;
		this.mNumSentenza = aNumSentenza;

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
		this.mAnnoRegistroGenerale = aAnnoRegistroGenerale;
		this.mNumeroRegistroGenerale = aNumeroRegistroGenerale;
		this.mAnnoRegePm = aAnnoRegePm;
		this.mNumeroRegePm = aNumeroRegePm;

		/* #### */
		this.mIdFascicoloSiep = aIdFascicoloSiep;
		this.mCodiceStatoFascicolo = aCodiceStatoFascicolo;
		this.mDataIscrizione = aDataIscrizione;
		this.mChiaveAnnoFascicolo = aChiaveAnnoFascicolo;
		this.mChiaveNumeroFascicolo = aChiaveNumeroFascicolo;
		this.mSenIdSentenza = aSenIdSentenza;
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mChiaveUfficio = aChiaveUfficio;
		this.mDescrChiaveUfficio = aDescrChiaveUfficio;
		this.mDescrComune = aDescrComune;
		this.mFlagValidato = aFlagValidato;
		this.mDescrStatoFasc = aDescrStatoFasc;
	}
	//
	// METODI GET()
	//

	public BigDecimal getIdSentenza() {
		return mIdSentenza;
	}

	public String getCodDpr() {
		return mCodDpr;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
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

	public Date getDataIrrevocabilita() {
		return mDataIrrevocabilita;
	}

	public BigDecimal getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public String getNumSentenza() {
		return mNumSentenza;
	}

	public BigDecimal getAnnoRegeGip() {
		return mAnnoRegeGip;
	}

	public String getNumeroRegeGip() {
		return mNumeroRegeGip;
	}

	public BigDecimal getAnnoRegeDib() {
		return mAnnoRegeDib;
	}

	public String getNumeroRegeDib() {
		return mNumeroRegeDib;
	}

	public BigDecimal getAnnoRegeCas() {
		return mAnnoRegeCas;
	}

	public String getNumeroRegeCas() {
		return mNumeroRegeCas;
	}

	public BigDecimal getAnnoRegeCap() {
		return mAnnoRegeCap;
	}

	public String getNumeroRegeCap() {
		return mNumeroRegeCap;
	}

	public BigDecimal getAnnoRegeCasap() {
		return mAnnoRegeCasap;
	}

	public String getNumeroRegeCasap() {
		return mNumeroRegeCasap;
	}

	public BigDecimal getAnnoRegistroGenerale() {
		return mAnnoRegistroGenerale;
	}

	public String getNumeroRegistroGenerale() {
		return mNumeroRegistroGenerale;
	}

	public BigDecimal getAnnoRegePm() {
		return mAnnoRegePm;
	}

	public String getNumeroRegePm() {
		return mNumeroRegePm;
	}

	/* #### */
	public BigDecimal getIdFascicoloSiep() {
		return mIdFascicoloSiep;
	}

	public String getCodiceStatoFascicolo() {
		return mCodiceStatoFascicolo;
	}

	public Date getDataIscrizione() {
		return mDataIscrizione;
	}

	public BigDecimal getChiaveAnnoFascicolo() {
		return mChiaveAnnoFascicolo;
	}

	public BigDecimal getChiaveNumeroFascicolo() {
		return mChiaveNumeroFascicolo;
	}

	public BigDecimal getSenIdSentenza() {
		return mSenIdSentenza;
	}

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public String getChiaveUfficio() {
		return mChiaveUfficio;
	}

	public String getDescrChiaveUfficio() {
		return mDescrChiaveUfficio;
	}

	public String getDescrComune() {
		return mDescrComune;
	}

	public String getFlagValidato() {
		return mFlagValidato;
	}

	public String getDescrStatoFasc() {
		return mDescrStatoFasc;
	}

	//
	// METODI SET()
	//

	public void setIdSentenza(BigDecimal aValore) {
		mIdSentenza = aValore;
	}

	public void setCodDpr(String aValore) {
		mCodDpr = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
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

	public void setDataIrrevocabilita(Date aValore) {
		mDataIrrevocabilita = aValore;
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumSentenza(String aValore) {
		mNumSentenza = aValore;
	}

	public void setAnnoRegeGip(BigDecimal aValore) {
		mAnnoRegeGip = aValore;
	}

	public void setNumeroRegeGip(String aValore) {
		mNumeroRegeGip = aValore;
	}

	public void setAnnoRegeDib(BigDecimal aValore) {
		mAnnoRegeDib = aValore;
	}

	public void setNumeroRegeDib(String aValore) {
		mNumeroRegeDib = aValore;
	}

	public void setAnnoRegeCas(BigDecimal aValore) {
		mAnnoRegeCas = aValore;
	}

	public void setNumeroRegeCas(String aValore) {
		mNumeroRegeCas = aValore;
	}

	public void setAnnoRegeCap(BigDecimal aValore) {
		mAnnoRegeCap = aValore;
	}

	public void setNumeroRegeCap(String aValore) {
		mNumeroRegeCap = aValore;
	}

	public void setAnnoRegeCasap(BigDecimal aValore) {
		mAnnoRegeCasap = aValore;
	}

	public void setNumeroRegeCasap(String aValore) {
		mNumeroRegeCasap = aValore;
	}

	public void setAnnoRegistroGenerale(BigDecimal aValore) {
		mAnnoRegistroGenerale = aValore;
	}

	public void setNumeroRegistroGenerale(String aValore) {
		mNumeroRegistroGenerale = aValore;
	}

	public void setAnnoRegePm(BigDecimal aValore) {
		mAnnoRegePm = aValore;
	}

	public void setNumeroRegePm(String aValore) {
		mNumeroRegePm = aValore;
	}

	/* #### */
	public void setIdFascicoloSiep(BigDecimal aValore) {
		mIdFascicoloSiep = aValore;
	}

	public void setCodiceStatoFascicolo(String aValore) {
		mCodiceStatoFascicolo = aValore;
	}

	public void setDataIscrizione(Date aValore) {
		mDataIscrizione = aValore;
	}

	public void setChiaveAnnoFascicolo(BigDecimal aValore) {
		mChiaveAnnoFascicolo = aValore;
	}

	public void setChiaveNumeroFascicolo(BigDecimal aValore) {
		mChiaveNumeroFascicolo = aValore;
	}

	public void setSenIdSentenza(BigDecimal aValore) {
		mSenIdSentenza = aValore;
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setChiaveUfficio(String aValore) {
		mChiaveUfficio = aValore;
	}

	public void setDescrChiaveUfficio(String aValore) {
		mDescrChiaveUfficio = aValore;
	}

	public void setDescrComune(String aValore) {
		mDescrComune = aValore;
	}

	public void setFlagValidato(String aValore) {
		mFlagValidato = aValore;
	}

	public void setDescrStatoFasc(String aValore) {
		mDescrStatoFasc = aValore;
	}

}