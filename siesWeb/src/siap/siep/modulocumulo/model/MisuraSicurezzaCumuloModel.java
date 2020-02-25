package siap.siep.modulocumulo.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: MisuraSicurezzaCumuloModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il MisuraSicurezzaCumulo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class MisuraSicurezzaCumuloModel extends GenericModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 72581634500277716L;

	private BigDecimal mIdMisuraSicurezzaCumulo;
	private String mCodNatura;
	private String mDescrNatura;
	private String mCodTipo;
	private String mDescrTipo;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mAnnoReg38;
	private BigDecimal mNumReg38;

	private String mFlagAnnullaMisura;
	private Date mDataFineValidita;
	private BigDecimal mMisIdMisuraSicurezzaCumulo;
	private BigDecimal mMisIdMisuraSicurezzaOrigine;
	private String mIstDetIdIstitutoDetenzione;
	private String mLuogoEsecuzioneMisura;

	private BigDecimal mAnnoFascicoloSiepIV;
	private BigDecimal mNumeroFascicoloSiepIV;
	private String mCodAutoritaEmittenteIV;
	private String mDescrAutoritaEmittenteIV;
	private String mCodLuogoEmittenteIV;
	private String mDescrLuogoEmittenteIV;
	private String mFlagStatoMisura;
	private String mDescrFlagStatoMisura;

	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mTitIdTitoloCumulato;
	private BigDecimal mIdMisuraSicurezzaOrigine;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private String mFlagDatiFinali;
	// private String mFlagCreaProcedimento;

	private TitoloCumulatoModel mTitoloCumulato;

	// mev56 Inizio ***************
	private String mNumOrdDec;
	private String mAnnoOrdDec;
	private Date mDataEmissione;
	private String mLuogoEmittente;
	private String mCodEsito;
	private String mDescEsitoTemplate;

	// mev56 Fine ***************

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public MisuraSicurezzaCumuloModel() {
		this.mIdMisuraSicurezzaCumulo = null;
		this.mCodNatura = "";
		this.mDescrNatura = "";
		this.mCodTipo = "";
		this.mDescrTipo = "";
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mAnnoReg38 = null;
		this.mNumReg38 = null;

		this.mFlagAnnullaMisura = null;
		this.mDataFineValidita = null;
		this.mMisIdMisuraSicurezzaCumulo = null;
		this.mMisIdMisuraSicurezzaOrigine = null;
		this.mIstDetIdIstitutoDetenzione = null;
		this.mLuogoEsecuzioneMisura = null;

		this.mAnnoFascicoloSiepIV = null;
		this.mNumeroFascicoloSiepIV = null;
		this.mCodAutoritaEmittenteIV = "";
		this.mDescrAutoritaEmittenteIV = "";
		this.mCodLuogoEmittenteIV = "";
		this.mDescrLuogoEmittenteIV = "";
		this.mFlagStatoMisura = "";
		this.mDescrFlagStatoMisura = "";

		this.mMotivoModifica = "";
		this.mFlagStato = "";
		this.mTitIdTitoloCumulato = null;
		this.mIdMisuraSicurezzaOrigine = null;

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";

		this.mFlagDatiFinali = "";

		this.mTitoloCumulato = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public MisuraSicurezzaCumuloModel(MisuraSicurezzaCumuloModel aModel) {
		this.mIdMisuraSicurezzaCumulo = aModel.mIdMisuraSicurezzaCumulo;
		this.mCodNatura = aModel.mCodNatura;
		this.mDescrNatura = aModel.mDescrNatura;
		this.mCodTipo = aModel.mCodTipo;
		this.mDescrTipo = aModel.mDescrTipo;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mAnnoReg38 = aModel.mAnnoReg38;
		this.mNumReg38 = aModel.mNumReg38;

		this.mFlagAnnullaMisura = aModel.mFlagAnnullaMisura;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mMisIdMisuraSicurezzaCumulo = aModel.mMisIdMisuraSicurezzaCumulo;
		this.mMisIdMisuraSicurezzaOrigine = aModel.mMisIdMisuraSicurezzaOrigine;
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mLuogoEsecuzioneMisura = aModel.mLuogoEsecuzioneMisura;

		this.mAnnoFascicoloSiepIV = aModel.mAnnoFascicoloSiepIV;
		this.mNumeroFascicoloSiepIV = aModel.mNumeroFascicoloSiepIV;
		this.mCodAutoritaEmittenteIV = aModel.mCodAutoritaEmittenteIV;
		this.mDescrAutoritaEmittenteIV = aModel.mDescrAutoritaEmittenteIV;
		this.mCodLuogoEmittenteIV = aModel.mCodLuogoEmittenteIV;
		this.mDescrLuogoEmittenteIV = aModel.mDescrLuogoEmittenteIV;
		this.mFlagStatoMisura = aModel.mFlagStatoMisura;
		this.mDescrFlagStatoMisura = aModel.mDescrFlagStatoMisura;

		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mFlagStato = aModel.mFlagStato;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mIdMisuraSicurezzaOrigine = aModel.mIdMisuraSicurezzaOrigine;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;

		this.mFlagDatiFinali = aModel.mFlagDatiFinali;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public MisuraSicurezzaCumuloModel(BigDecimal aIdMisuraSicurezzaCumulo, String aCodNatura,
			String aDescrNatura, String aCodTipo, String aDescrTipo, BigDecimal aNumAnni, BigDecimal aNumMesi,
			BigDecimal aNumGiorni, BigDecimal aAnnoReg38, BigDecimal aNumReg38,

			String aFlagAnnullaMisura, Date aDataFineValidita, BigDecimal aMisIdMisuraSicurezzaCumulo,
			BigDecimal aMisIdMisuraSicurezzaOrigine, String aIstDetIdIstitutoDetenzione,
			String aLuogoEsecuzioneMisura,

			BigDecimal aAnnoFascicoloSiepIV, BigDecimal aNumeroFascicoloSiepIV,
			String aCodAutoritaEmittenteIV, String aDescrAutoritaEmittenteIV, String aCodLuogoEmittenteIV,
			String aDescrluogoEmittenteIV, String aFlagStatoMisura, String aDescrFlagStatoMisura,

			String aMotivoModifica, String aFlagStato, BigDecimal aTitIdTitoloCumulato,
			BigDecimal aIdMisuraSicurezzaOrigine, String aFlagDatiFinali,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdMisuraSicurezzaCumulo = aIdMisuraSicurezzaCumulo;
		this.mCodNatura = aCodNatura;
		this.mDescrNatura = aDescrNatura;
		this.mCodTipo = aCodTipo;
		this.mDescrTipo = aDescrTipo;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mAnnoReg38 = aAnnoReg38;
		this.mNumReg38 = aNumReg38;

		this.mFlagAnnullaMisura = aFlagAnnullaMisura;
		this.mDataFineValidita = aDataFineValidita;
		this.mMisIdMisuraSicurezzaCumulo = aMisIdMisuraSicurezzaCumulo;
		this.mMisIdMisuraSicurezzaOrigine = aMisIdMisuraSicurezzaOrigine;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mLuogoEsecuzioneMisura = aLuogoEsecuzioneMisura;

		this.mAnnoFascicoloSiepIV = aAnnoFascicoloSiepIV;
		this.mNumeroFascicoloSiepIV = aNumeroFascicoloSiepIV;
		this.mCodAutoritaEmittenteIV = aCodAutoritaEmittenteIV;
		this.mDescrAutoritaEmittenteIV = aDescrAutoritaEmittenteIV;
		this.mCodLuogoEmittenteIV = aCodLuogoEmittenteIV;
		this.mDescrLuogoEmittenteIV = aDescrluogoEmittenteIV;
		this.mFlagStatoMisura = aFlagStatoMisura;
		this.mDescrFlagStatoMisura = aDescrFlagStatoMisura;

		this.mMotivoModifica = aMotivoModifica;
		this.mFlagStato = aFlagStato;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mIdMisuraSicurezzaOrigine = aIdMisuraSicurezzaOrigine;
		this.mFlagDatiFinali = aFlagDatiFinali;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdMisuraSicurezzaCumulo() {
		return mIdMisuraSicurezzaCumulo;
	}

	public String getCodNatura() {
		return mCodNatura;
	}

	public String getDescrNatura() {
		return mDescrNatura;
	}

	public String getCodTipo() {
		return mCodTipo;
	}

	public String getDescrTipo() {
		return mDescrTipo;
	}

	public BigDecimal getNumAnni() {
		return mNumAnni;
	}

	public BigDecimal getNumMesi() {
		return mNumMesi;
	}

	public BigDecimal getNumGiorni() {
		return mNumGiorni;
	}

	public BigDecimal getAnnoReg38() {
		return mAnnoReg38;
	}

	public BigDecimal getNumReg38() {
		return mNumReg38;
	}

	public String getFlagAnnullaMisura() {
		return mFlagAnnullaMisura;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public BigDecimal getMisIdMisuraSicurezzaCumulo() {
		return mMisIdMisuraSicurezzaCumulo;
	}

	public BigDecimal getMisIdMisuraSicurezzaOrigine() {
		return mMisIdMisuraSicurezzaOrigine;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public String getLuogoEsecuzioneMisura() {
		return mLuogoEsecuzioneMisura;
	}

	public BigDecimal getAnnoFascicoloSiepIV() {
		return mAnnoFascicoloSiepIV;
	}

	public BigDecimal getNumeroFascicoloSiepIV() {
		return mNumeroFascicoloSiepIV;
	}

	public String getCodAutoritaEmittenteIV() {
		return mCodAutoritaEmittenteIV;
	}

	public String getDescrAutoritaEmittenteIV() {
		return mDescrAutoritaEmittenteIV;
	}

	public String getCodLuogoEmittenteIV() {
		return mCodLuogoEmittenteIV;
	}

	public String getDescrLuogoEmittenteIV() {
		return mDescrLuogoEmittenteIV;
	}

	public String getFlagStatoMisura() {
		return mFlagStatoMisura;
	}

	public String getDescrFlagStatoMisura() {
		return mDescrFlagStatoMisura;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public BigDecimal getIdMisuraSicurezzaOrigine() {
		return mIdMisuraSicurezzaOrigine;
	}

	public String getFlagDatiFinali() {
		return mFlagDatiFinali;
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

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public TitoloCumulatoModel getTitoloCumulato() {
		return mTitoloCumulato;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdMisuraSicurezzaCumulo(BigDecimal aValore) {
		mIdMisuraSicurezzaCumulo = aValore;
	}

	public void setCodNatura(String aValore) {
		mCodNatura = aValore;
	}

	public void setDescrNatura(String aValore) {
		mDescrNatura = aValore;
	}

	public void setCodTipo(String aValore) {
		mCodTipo = aValore;
	}

	public void setDescrTipo(String aValore) {
		mDescrTipo = aValore;
	}

	public void setNumAnni(BigDecimal aValore) {
		mNumAnni = aValore;
	}

	public void setNumMesi(BigDecimal aValore) {
		mNumMesi = aValore;
	}

	public void setNumGiorni(BigDecimal aValore) {
		mNumGiorni = aValore;
	}

	public void setAnnoReg38(BigDecimal aValore) {
		mAnnoReg38 = aValore;
	}

	public void setNumReg38(BigDecimal aValore) {
		mNumReg38 = aValore;
	}

	public void setFlagAnnullaMisura(String aValore) {
		mFlagAnnullaMisura = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setMisIdMisuraSicurezzaCumulo(BigDecimal aValore) {
		mMisIdMisuraSicurezzaCumulo = aValore;
	}

	public void setMisIdMisuraSicurezzaOrigine(BigDecimal aValore) {
		mMisIdMisuraSicurezzaOrigine = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setLuogoEsecuzioneMisura(String aValore) {
		mLuogoEsecuzioneMisura = aValore;
	}

	public void setAnnoFascicoloSiepIV(BigDecimal aValore) {
		mAnnoFascicoloSiepIV = aValore;
	}

	public void setNumeroFascicoloSiepIV(BigDecimal aValore) {
		mNumeroFascicoloSiepIV = aValore;
	}

	public void setCodAutoritaEmittenteIV(String aValore) {
		mCodAutoritaEmittenteIV = aValore;
	}

	public void setDescrAutoritaEmittenteIV(String aValore) {
		mDescrAutoritaEmittenteIV = aValore;
	}

	public void setCodLuogoEmittenteIV(String aValore) {
		mCodLuogoEmittenteIV = aValore;
	}

	public void setDescrluogoEmittenteIV(String aValore) {
		mDescrLuogoEmittenteIV = aValore;
	}

	public void setFlagStatoMisura(String aValore) {
		mFlagStatoMisura = aValore;
	}

	public void setDescrFlagStatoMisura(String aValore) {
		mDescrFlagStatoMisura = aValore;
	}

	public void setFlagDatiFinali(String aValore) {
		mFlagDatiFinali = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setIdMisuraSicurezzaOrigine(BigDecimal aValore) {
		mIdMisuraSicurezzaOrigine = aValore;
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

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setTitoloCumulato(TitoloCumulatoModel aValore) {
		mTitoloCumulato = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "MisuraSicurezzaCumuloModel:\n" + "[ mIdMisuraSicurezzaCumulo   = " + mIdMisuraSicurezzaCumulo
				+ " ]\n" + "[ mCodNatura                 = " + mCodNatura + " ]\n"
				+ "[ mDescrNatura               = " + mDescrNatura + " ]\n"
				+ "[ mCodTipo                   = " + mCodTipo + " ]\n" + "[ mDescrTipo                 = "
				+ mDescrTipo + " ]\n" + "[ mNumAnni                   = " + mNumAnni + " ]\n"
				+ "[ mNumMesi                   = " + mNumMesi + " ]\n" + "[ mNumGiorni                 = "
				+ mNumGiorni + " ]\n" + "[ mAnnoReg38                 = " + mAnnoReg38 + " ]\n"
				+ "[ mNumReg38                  = " + mNumReg38 + " ]\n" + "[ mFlagAnnullaMisura 		 = "
				+ mFlagAnnullaMisura + " ]\n" + "[ mDataFineValidita 		 = " + mDataFineValidita + " ]\n"
				+ "[ mMisIdMisuraSicurezzaCumulo  = " + mMisIdMisuraSicurezzaCumulo + " ]\n"
				+ "[ mMisIdMisuraSicurezzaOrigine = " + mMisIdMisuraSicurezzaOrigine + " ]\n"
				+ "[ mIstDetIdIstitutoDetenzione  = " + mIstDetIdIstitutoDetenzione + " ]\n"
				+ "[ mLuogoEsecuzioneMisura     = " + mLuogoEsecuzioneMisura + " ]\n"
				+ "[ mAnnoFascicoloSiepIV          = " + mAnnoFascicoloSiepIV + " ]\n"
				+ "[ mNumeroFascicoloSiepIV        = " + mNumeroFascicoloSiepIV + " ]\n"
				+ "[ mCodAutoritaEmittenteIV        = " + mCodAutoritaEmittenteIV + " ]\n"
				+ "[ mDescrAutoritaEmittenteIV      = " + mDescrAutoritaEmittenteIV + " ]\n"
				+ "[ mCodLuogoEmittenteIV          = " + mCodLuogoEmittenteIV + " ]\n"
				+ "[ mDescrLuogoEmittenteIV      	= " + mDescrLuogoEmittenteIV + " ]\n"
				+ "[ mFlagStatoMisura              = " + mFlagStatoMisura + " ]\n"
				+ "[ mDescrFlagStatoMisura         = " + mDescrFlagStatoMisura + " ]\n"
				+ "[ mMotivoModifica            = " + mMotivoModifica + " ]\n"
				+ "[ mFlagStato                 = " + mFlagStato + " ]\n" + "[ mTitIdTitoloCumulato       = "
				+ mTitIdTitoloCumulato + " ]\n" + "[ mIdMisuraSicurezzaOrigine  = "
				+ mIdMisuraSicurezzaOrigine + " ]\n" + "[ mFlagDatiFinali            = " + mFlagDatiFinali
				+ " ]\n" + "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

	public String getNumOrdDec() {
		return mNumOrdDec;
	}

	public void setNumOrdDec(String mNumOrdDec) {
		this.mNumOrdDec = mNumOrdDec;
	}

	public String getAnnoOrdDec() {
		return mAnnoOrdDec;
	}

	public void setAnnoOrdDec(String mAnnoOrdDec) {
		this.mAnnoOrdDec = mAnnoOrdDec;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public void setDataEmissione(Date mDataEmissione) {
		this.mDataEmissione = mDataEmissione;
	}

	public String getLuogoEmittente() {
		return mLuogoEmittente;
	}

	public void setLuogoEmittente(String mLuogoEmittente) {
		this.mLuogoEmittente = mLuogoEmittente;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public void setCodEsito(String mCodEsito) {
		this.mCodEsito = mCodEsito;
	}

	public String getDescEsitoTemplate() {
		return mDescEsitoTemplate;
	}

	public void setDescEsitoTemplate(String mDescEsitoTemplate) {
		this.mDescEsitoTemplate = mDescEsitoTemplate;
	}

}