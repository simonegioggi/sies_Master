package siap.siep.sentenza.model;

/**
* <p>Title: SentenzaModel</p>
* <p>Description: Classe Model che rappresenta il Sentenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;
import java.math.BigDecimal;

import f3b.model.GenericModel;

public class SentenzaSoggettoFascicoloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2130946057242442999L;

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

	/* */
	private String mCognome;
	private String mNome;
	private BigDecimal mAnnoNascita;
	private Date mDataNascita;
	private String mCodComuneNascita;
	private String mDescrComuneNascita;
	private String mCodProvinciaNascita;
	private String mDescrProvinciaNascita;
	private String mCodiceCUI;

	// Attributo che serve per la cancellazione
	// private boolean mEsistonoFascicoliAssociati;

	// COSTRUTTORE DI DEFAULT
	public SentenzaSoggettoFascicoloModel() {
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

		this.mCognome = "";
		this.mNome = "";
		this.mAnnoNascita = null;
		this.mDataNascita = null;
		this.mCodComuneNascita = "";
		this.mDescrComuneNascita = "";
		this.mCodProvinciaNascita = "";
		this.mDescrProvinciaNascita = "";
		this.mCodiceCUI = "";
	}

	// COSTRUTTORE DI COPIA
	public SentenzaSoggettoFascicoloModel(SentenzaSoggettoFascicoloModel aModel) {
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

		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mAnnoNascita = aModel.mAnnoNascita;
		this.mDataNascita = aModel.mDataNascita;
		this.mCodComuneNascita = aModel.mCodComuneNascita;
		this.mDescrComuneNascita = aModel.mDescrComuneNascita;
		this.mCodProvinciaNascita = aModel.mCodProvinciaNascita;
		this.mDescrProvinciaNascita = aModel.mDescrProvinciaNascita;
		this.mCodiceCUI = aModel.mCodiceCUI;
	}

	// COSTRUTTORE MODEL
	public SentenzaSoggettoFascicoloModel(BigDecimal aIdSentenza, String aCodDpr,
			String aCodTipoProvvedimento, String aDescrTipoProvvedimento, Date aDataProvvedimento,
			String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente, String aCodLuogoEmittente,
			String aDescrLuogoEmittente, String aNumSezioneAutoritaEmittente, Date aDataIrrevocabilita,
			BigDecimal aAnnoSentenza, String aNumSentenza, BigDecimal aIdFascicoloSiep,
			String aCodiceStatoFascicolo, Date aDataIscrizione, BigDecimal aChiaveAnnoFascicolo,
			BigDecimal aChiaveNumeroFascicolo, BigDecimal aSenIdSentenza, BigDecimal aSogIdSoggetto,
			String aChiaveUfficio, String aDescrChiaveUfficio, String aDescrComune, String aFlagValidato,
			String aDescrStatoFasc,

			String aCognome, String aNome, BigDecimal aAnnoNascita, Date aDataNascita,
			String aCodComuneNascita, String aDescrComuneNascita, String aCodProvinciaNascita,
			String aDescrProvinciaNascita, String aCodiceCUI) {
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

		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mAnnoNascita = aAnnoNascita;
		this.mDataNascita = aDataNascita;
		this.mCodComuneNascita = aCodComuneNascita;
		this.mDescrComuneNascita = aDescrComuneNascita;
		this.mCodProvinciaNascita = aCodProvinciaNascita;
		this.mDescrProvinciaNascita = aDescrProvinciaNascita;
		this.mCodiceCUI = aCodiceCUI;
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

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public BigDecimal getAnnoNascita() {
		return mAnnoNascita;
	}

	public Date getDataNascita() {
		return mDataNascita;
	}

	public String getCodComuneNascita() {
		return mCodComuneNascita;
	}

	public String getDescrComuneNascita() {
		return mDescrComuneNascita;
	}

	public String getCodProvinciaNascita() {
		return mCodProvinciaNascita;
	}

	public String getDescrProvinciaNascita() {
		return mDescrProvinciaNascita;
	}

	public String getCodiceCUI() {
		return mCodiceCUI;
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
	/* #### */

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setAnnoNascita(BigDecimal aValore) {
		mAnnoNascita = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public void setCodComuneNascita(String aValore) {
		mCodComuneNascita = aValore;
	}

	public void setDescrComuneNascita(String aValore) {
		mDescrComuneNascita = aValore;
	}

	public void setCodProvinciaNascita(String aValore) {
		mCodProvinciaNascita = aValore;
	}

	public void setDescrProvinciaNascita(String aValore) {
		mDescrProvinciaNascita = aValore;
	}

	public void setCodiceCUI(String aValore) {
		mCodiceCUI = aValore;
	}

}