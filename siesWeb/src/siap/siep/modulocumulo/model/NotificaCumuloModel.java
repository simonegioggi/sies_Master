package siap.siep.modulocumulo.model;

/**
* <p>Title: NotificaCumuloModel</p>
* <p>Description: Classe Model che rappresenta il NotificaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

import siap.sico.ufficio.model.UfficioModel;
import siap.siep.notifica.model.NotificaModel;

public class NotificaCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7523564075432253085L;

	private BigDecimal mIdNotificaCumulo;
	private String mCodTipoNotifica;
	private String mDescrTipoNotifica;
	private Date mDataAvvenutaNotifica;
	private Date mDataInvio;
	private String mCodEsito;
	private String mDescrEsito;
	private String mNote;

	private BigDecimal mStatIdStatoEsecTitCum;

	private BigDecimal mAutEstIdAutoritaEsterna;
	private BigDecimal mSogIdSoggetto;
	private BigDecimal mAvvIdAvvocatoFascicoloSiep;
	private String mUffCodUfficio;
	private BigDecimal mCssIdCssa;
	private String mIstDetIdIstitutoDetenzione;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private UfficioModel mUfficio;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public NotificaCumuloModel() {
		this.mIdNotificaCumulo = null;
		this.mCodTipoNotifica = "";
		this.mDescrTipoNotifica = "";
		this.mDataAvvenutaNotifica = null;
		this.mDataInvio = null;
		this.mCodEsito = "";
		this.mDescrEsito = "";
		this.mNote = "";
		this.mStatIdStatoEsecTitCum = null;
		this.mAutEstIdAutoritaEsterna = null;
		this.mSogIdSoggetto = null;
		this.mAvvIdAvvocatoFascicoloSiep = null;
		this.mUffCodUfficio = "";
		this.mCssIdCssa = null;
		this.mIstDetIdIstitutoDetenzione = "";

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public NotificaCumuloModel(NotificaCumuloModel aModel) {
		this.mIdNotificaCumulo = aModel.mIdNotificaCumulo;
		this.mCodTipoNotifica = aModel.mCodTipoNotifica;
		this.mDescrTipoNotifica = aModel.mDescrTipoNotifica;
		this.mDataAvvenutaNotifica = aModel.mDataAvvenutaNotifica;
		this.mDataInvio = aModel.mDataInvio;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mNote = aModel.mNote;
		this.mStatIdStatoEsecTitCum = aModel.mStatIdStatoEsecTitCum;
		this.mAutEstIdAutoritaEsterna = aModel.mAutEstIdAutoritaEsterna;
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mAvvIdAvvocatoFascicoloSiep = aModel.mAvvIdAvvocatoFascicoloSiep;
		this.mUffCodUfficio = aModel.mUffCodUfficio;
		this.mCssIdCssa = aModel.mCssIdCssa;
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public NotificaCumuloModel(BigDecimal aIdNotificaCumulo, String aCodTipoNotifica,
			String aDescrTipoNotifica, Date aDataAvvenutaNotifica, Date aDataInvio, String aCodEsito,
			String aDescrEsito, String aNote, BigDecimal aStatIdStatoEsecTitCum,
			BigDecimal aAutEstIdAutoritaEsterna, BigDecimal aSogIdSoggetto,
			BigDecimal aAvvIdAvvocatoFascicoloSiep, String aUffCodUfficio, BigDecimal aCssIdCssa,
			String aIstDetIdIstitutoDetenzione, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento) {
		this.mIdNotificaCumulo = aIdNotificaCumulo;
		this.mCodTipoNotifica = aCodTipoNotifica;
		this.mDescrTipoNotifica = aDescrTipoNotifica;
		this.mDataAvvenutaNotifica = aDataAvvenutaNotifica;
		this.mDataInvio = aDataInvio;
		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mNote = aNote;
		this.mStatIdStatoEsecTitCum = aStatIdStatoEsecTitCum;
		this.mAutEstIdAutoritaEsterna = aAutEstIdAutoritaEsterna;
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mAvvIdAvvocatoFascicoloSiep = aAvvIdAvvocatoFascicoloSiep;
		this.mUffCodUfficio = aUffCodUfficio;
		this.mCssIdCssa = aCssIdCssa;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	public NotificaCumuloModel(NotificaModel aModel) {
		// this.mIdNotificaCumulo = aModel.mIdNotificaCumulo;
		this.mCodTipoNotifica = aModel.getCodTipoNotifica();
		this.mDataAvvenutaNotifica = aModel.getDataAvvenutaNotifica();
		this.mDataInvio = aModel.getDataInvio();
		this.mCodEsito = aModel.getCodEsito();
		this.mNote = aModel.getNote();
		// this.mAutEstIdAutoritaEsterna = aModel.mAutEstIdAutoritaEsterna;
		// this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		// this.mAvvIdAvvocatoFascicoloSiep = aModel.mAvvIdAvvocatoFascicoloSiep;
		// this.mUffCodUfficio = aModel.mUffCodUfficio;
		// this.mCssIdCssa = aModel.mCssIdCssa;
		// this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		// this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		// this.mDataInserimento = aModel.mDataInserimento;
		// this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		// this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		// this.mDataAggiornamento = aModel.mDataAggiornamento;
		// this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdNotificaCumulo() {
		return mIdNotificaCumulo;
	}

	public String getCodTipoNotifica() {
		return mCodTipoNotifica;
	}

	public String getDescrTipoNotifica() {
		return mDescrTipoNotifica;
	}

	public Date getDataAvvenutaNotifica() {
		return mDataAvvenutaNotifica;
	}

	public Date getDataInvio() {
		return mDataInvio;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public String getNote() {
		return mNote;
	}

	public BigDecimal getStatIdStatoEsecTitCum() {
		return mStatIdStatoEsecTitCum;
	}

	public String getUffCodUfficio() {
		return mUffCodUfficio;
	}

	public BigDecimal getCssIdCssa() {
		return mCssIdCssa;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	// FIXME I seguenti campi sono da verificare. Non devono puntare le strutture dati originarie
	// legate al fascicolo di origine es:
	// AVV_ID_AVVOCATO_FASCIOLO_SIEP deve puntare direttamente la tabella AVVOCATO
	// SOGG_ID_SOGGETTO Puntare SOGG_ID_SOGGETTO_CUMULATO
	// AUT_ESTERNA copiare i campi della tabella AUTORITA_ESTERNA (COD_TIPO_AUTORITA, DESCRIZIONE,COD_SEDE)
	public BigDecimal getAutEstIdAutoritaEsterna() {
		return mAutEstIdAutoritaEsterna;
	}

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public BigDecimal getAvvIdAvvocatoFascicoloSiep() {
		return mAvvIdAvvocatoFascicoloSiep;
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

	public UfficioModel getUfficio() {
		return mUfficio;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdNotificaCumulo(BigDecimal aValore) {
		mIdNotificaCumulo = aValore;
	}

	public void setCodTipoNotifica(String aValore) {
		mCodTipoNotifica = aValore;
	}

	public void setDescrTipoNotifica(String aValore) {
		mDescrTipoNotifica = aValore;
	}

	public void setDataAvvenutaNotifica(Date aValore) {
		mDataAvvenutaNotifica = aValore;
	}

	public void setDataInvio(Date aValore) {
		mDataInvio = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setStatIdStatoEsecTitCum(BigDecimal aValore) {
		mStatIdStatoEsecTitCum = aValore;
	}

	public void setAutEstIdAutoritaEsterna(BigDecimal aValore) {
		mAutEstIdAutoritaEsterna = aValore;
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setAvvIdAvvocatoFascicoloSiep(BigDecimal aValore) {
		mAvvIdAvvocatoFascicoloSiep = aValore;
	}

	public void setUffCodUfficio(String aValore) {
		mUffCodUfficio = aValore;
	}

	public void setCssIdCssa(BigDecimal aValore) {
		mCssIdCssa = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
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

	public void setUfficio(UfficioModel aValore) {
		mUfficio = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "NotificaCumuloModel:\n" + "[ mIdNotificaCumulo             = " + mIdNotificaCumulo + " ]\n"
				+ "[ mCodTipoNotifica              = " + mCodTipoNotifica + " ]\n"
				+ "[ mDataAvvenutaNotifica         = " + mDataAvvenutaNotifica + " ]\n"
				+ "[ mDataInvio                    = " + mDataInvio + " ]\n"
				+ "[ mCodEsito                     = " + mCodEsito + " ]\n"
				+ "[ mNote                         = " + mNote + " ]\n" + "[ mStatIdStatoEsecTitCum        = "
				+ mStatIdStatoEsecTitCum + " ]\n" + "[ mAutEstIdAutoritaEsterna      = "
				+ mAutEstIdAutoritaEsterna + " ]\n" + "[ mSogIdSoggetto                = " + mSogIdSoggetto
				+ " ]\n" + "[ mAvvIdAvvocatoFascicoloSiep   = " + mAvvIdAvvocatoFascicoloSiep + " ]\n"
				+ "[ mUffCodUfficio                = " + mUffCodUfficio + " ]\n"
				+ "[ mCssIdCssa                    = " + mCssIdCssa + " ]\n"
				+ "[ mIstDetIdIstitutoDetenzione   = " + mIstDetIdIstitutoDetenzione + " ]\n"
				+ "[ mCodOperatoreInserimento      = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento              = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento        = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento    = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento            = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento      = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

}