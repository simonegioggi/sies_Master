package siap.sius.remissionedebito.model;

/**
* <p>Title: RichiestaRemissioneModel</p>
* <p>Description: Classe Model che rappresenta il RichiestaRemissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.evento.model.EventoModel;

public class RichiestaRemissioneModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 4336594747673209434L;
	private BigDecimal mIdRichiestaRemissione;
	private BigDecimal mAnnoPartita;
	private BigDecimal mNumPartita;
	private String mNumExCampione;
	private String mProtCircosrizioneDoganale;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;

	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private Date mDataEmissione;
	private String mCodAutoritaEmittenteProvv;
	private String mDescrAutoritaEmittenteProvv;
	private String mCodLuogoEmittenteProvv;
	private String mDescrLuogoEmittenteProvv;
	private String mFlagSpeseCarcere;
	private BigDecimal mImportoSpeseCarcere;
	private String mFlagSpeseProcedimento;
	private BigDecimal mImportoSpeseProcedimento;

	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mEveIdEvento;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSiuIdFascicoloSius;
	private String mNote;

	private EventoModel mEventoModel;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RichiestaRemissioneModel() {
		this.mIdRichiestaRemissione = null;
		this.mAnnoPartita = null;
		this.mNumPartita = null;
		this.mNumExCampione = "";
		this.mProtCircosrizioneDoganale = "";
		this.mCodTipoAutoritaEmittente = "";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";

		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mDataEmissione = null;
		this.mCodAutoritaEmittenteProvv = "";
		this.mDescrAutoritaEmittenteProvv = "";
		this.mCodLuogoEmittenteProvv = "";
		this.mDescrLuogoEmittenteProvv = "";
		this.mFlagSpeseCarcere = "";
		this.mImportoSpeseCarcere = null;
		this.mFlagSpeseProcedimento = "";
		this.mImportoSpeseProcedimento = null;

		this.mFasSieIdFascicoloSiep = null;
		this.mEveIdEvento = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSiuIdFascicoloSius = null;
		this.mNote = "";
		this.mEventoModel = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public RichiestaRemissioneModel(RichiestaRemissioneModel aModel) {
		this.mIdRichiestaRemissione = aModel.mIdRichiestaRemissione;
		this.mAnnoPartita = aModel.mAnnoPartita;
		this.mNumPartita = aModel.mNumPartita;
		this.mNumExCampione = aModel.mNumExCampione;
		this.mProtCircosrizioneDoganale = aModel.mProtCircosrizioneDoganale;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;

		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mCodAutoritaEmittenteProvv = aModel.mCodAutoritaEmittenteProvv;
		this.mDescrAutoritaEmittenteProvv = aModel.mDescrAutoritaEmittenteProvv;
		this.mCodLuogoEmittenteProvv = aModel.mCodLuogoEmittenteProvv;
		this.mDescrLuogoEmittenteProvv = aModel.mDescrLuogoEmittenteProvv;
		this.mFlagSpeseCarcere = aModel.mFlagSpeseCarcere;
		this.mImportoSpeseCarcere = aModel.mImportoSpeseCarcere;
		this.mFlagSpeseProcedimento = aModel.mFlagSpeseProcedimento;
		this.mImportoSpeseProcedimento = aModel.mImportoSpeseProcedimento;

		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mNote = aModel.mNote;
		this.mEventoModel = aModel.mEventoModel;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public RichiestaRemissioneModel(BigDecimal aIdRichiestaRemissione, BigDecimal aAnnoPartita,
			BigDecimal aNumPartita, String aNumExCampione, String aProtCircosrizioneDoganale,
			String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente, String aCodLuogoEmittente,
			String aDescrLuogoEmittente,

			String aCodTipoProvvedimento, String aDescrTipoProvvedimento, Date aDataEmissione,
			String aCodAutoritaEmittenteProvv, String aDescrAutoritaEmittenteProvv,
			String aCodLuogoEmittenteProvv, String aDescrLuogoEmittenteProvv, String aFlagSpeseCarcere,
			BigDecimal aImportoSpeseCarcere, String aFlagSpeseProcedimento,
			BigDecimal aImportoSpeseProcedimento,

			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aEveIdEvento, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			BigDecimal aFasSiuIdFascicoloSius, String aNote)

	{
		this.mIdRichiestaRemissione = aIdRichiestaRemissione;
		this.mAnnoPartita = aAnnoPartita;
		this.mNumPartita = aNumPartita;
		this.mNumExCampione = aNumExCampione;
		this.mProtCircosrizioneDoganale = aProtCircosrizioneDoganale;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;

		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mDataEmissione = aDataEmissione;
		this.mCodAutoritaEmittenteProvv = aCodAutoritaEmittenteProvv;
		this.mDescrAutoritaEmittenteProvv = aDescrAutoritaEmittenteProvv;
		this.mCodLuogoEmittenteProvv = aCodLuogoEmittenteProvv;
		this.mDescrLuogoEmittenteProvv = aDescrLuogoEmittenteProvv;
		this.mFlagSpeseCarcere = aFlagSpeseCarcere;
		this.mImportoSpeseCarcere = aImportoSpeseCarcere;
		this.mFlagSpeseProcedimento = aFlagSpeseProcedimento;
		this.mImportoSpeseProcedimento = aImportoSpeseProcedimento;

		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mEveIdEvento = aEveIdEvento;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		// this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mNote = aNote;

		this.mEventoModel = null;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdRichiestaRemissione() {
		return mIdRichiestaRemissione;
	}

	public BigDecimal getAnnoPartita() {
		return mAnnoPartita;
	}

	public BigDecimal getNumPartita() {
		return mNumPartita;
	}

	public String getNumExCampione() {
		return mNumExCampione;
	}

	public String getProtCircosrizioneDoganale() {
		return mProtCircosrizioneDoganale;
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

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public String getCodAutoritaEmittenteProvv() {
		return mCodAutoritaEmittenteProvv;
	}

	public String getDescrAutoritaEmittenteProvv() {
		return mDescrAutoritaEmittenteProvv;
	}

	public String getCodLuogoEmittenteProvv() {
		return mCodLuogoEmittenteProvv;
	}

	public String getDescrLuogoEmittenteProvv() {
		return mDescrLuogoEmittenteProvv;
	}

	public String getFlagSpeseCarcere() {
		return mFlagSpeseCarcere;
	}

	public BigDecimal getImportoSpeseCarcere() {
		return mImportoSpeseCarcere;
	}

	public String getFlagSpeseProcedimento() {
		return mFlagSpeseProcedimento;
	}

	public BigDecimal getImportoSpeseProcedimento() {
		return mImportoSpeseProcedimento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
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

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public String getNote() {
		return mNote;
	}

	public EventoModel getEventoModel() {
		return mEventoModel;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdRichiestaRemissione(BigDecimal aValore) {
		mIdRichiestaRemissione = aValore;
	}

	public void setAnnoPartita(BigDecimal aValore) {
		mAnnoPartita = aValore;
	}

	public void setNumPartita(BigDecimal aValore) {
		mNumPartita = aValore;
	}

	public void setNumExCampione(String aValore) {
		mNumExCampione = aValore;
	}

	public void setProtCircosrizioneDoganale(String aValore) {
		mProtCircosrizioneDoganale = aValore;
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

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setCodAutoritaEmittenteProvv(String aValore) {
		mCodAutoritaEmittenteProvv = aValore;
	}

	public void setDescrAutoritaEmittenteProvv(String aValore) {
		mDescrAutoritaEmittenteProvv = aValore;
	}

	public void setCodLuogoEmittenteProvv(String aValore) {
		mCodLuogoEmittenteProvv = aValore;
	}

	public void setDescrLuogoEmittenteProvv(String aValore) {
		mDescrLuogoEmittenteProvv = aValore;
	}

	public void setFlagSpeseCarcere(String aValore) {
		mFlagSpeseCarcere = aValore;
	}

	public void setImportoSpeseCarcere(BigDecimal aValore) {
		mImportoSpeseCarcere = aValore;
	}

	public void setFlagSpeseProcedimento(String aValore) {
		mFlagSpeseProcedimento = aValore;
	}

	public void setImportoSpeseProcedimento(BigDecimal aValore) {
		mImportoSpeseProcedimento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setEventoModel(EventoModel aValore) {
		mEventoModel = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "RichiestaRemissioneModel:\n" + "[ mIdRichiestaRemissione      = " + mIdRichiestaRemissione
				+ " ]\n" + "[ mAnnoPartita                 = " + mAnnoPartita + " ]\n"
				+ "[ mNumPartita                  = " + mNumPartita + " ]\n"
				+ "[ mNumExCampione               = " + mNumExCampione + " ]\n"
				+ "[ mProtCircosrizioneDoganale   = " + mProtCircosrizioneDoganale + " ]\n"
				+ "[ mCodTipoAutoritaEmittente    = " + mCodTipoAutoritaEmittente + " ]\n"
				+ "[ mCodLuogoEmittente           = " + mCodLuogoEmittente + " ]\n" +

				"[ mCodTipoProvvedimento        = " + mCodTipoProvvedimento + " ]\n"
				+ "[ mDataEmissione           	   = " + mDataEmissione + " ]\n"
				+ "[ mCodAutoritaEmittenteProvv   = " + mCodAutoritaEmittenteProvv + " ]\n"
				+ "[ mCodLuogoEmittenteProvv      = " + mCodLuogoEmittenteProvv + " ]\n"
				+ "[ mFlagSpeseCarcere            = " + mFlagSpeseCarcere + " ]\n"
				+ "[ mImportoSpeseCarcere         = " + mImportoSpeseCarcere + " ]\n"
				+ "[ mFlagSpeseProcedimento       = " + mFlagSpeseProcedimento + " ]\n"
				+ "[ mImportoSpeseProcedimento    = " + mImportoSpeseProcedimento + " ]\n" +

				"[ mFasSieIdFascicoloSiep       = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mEveIdEvento                 = " + mEveIdEvento + " ]\n"
				+ "[ mCodOperatoreInserimento     = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento             = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento       = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento   = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento           = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento     = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mFasSiuIdFascicoloSius     	 = " + mFasSiuIdFascicoloSius + " ]\n"
				+ "[ mNote     				 					 = " + mNote + " ]\n" + "" + mEventoModel;

		return lStr;
	}
}
