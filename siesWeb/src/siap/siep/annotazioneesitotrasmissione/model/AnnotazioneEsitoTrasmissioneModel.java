package siap.siep.annotazioneesitotrasmissione.model;

/**
* <p>Title: AnnotazioneEsitoTrasmissioneModel</p>
* <p>Description: Classe Model che rappresenta il AnnotazioneEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AnnotazioneEsitoTrasmissioneModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8629100918984054523L;

	private BigDecimal mIdEsitoTrasmissione;
	private Date mDataTrasmissione;
	private String mOggettoTrasmissione;
	private String mDescrOggettoTrasmissione;

	private String mCodUfficioDestinatario;
	private String mDescrUfficioDestinatario;

	private String mCodUfficioInoltrante;
	private String mDescrUfficioInoltrante;

	private String mCodUfficioInoltro;
	private String mDescrTipoUfficioInoltro;
	private String mDescrComuneUfficioInoltro;

	private String mCodUfficioEsito;
	private String mDescrTipoUfficioEsito;
	private String mDescrComuneUfficioEsito;

	private Date mDataEsito;
	private String mCodEsito;
	private String mDescrEsito;
	private String mNoteEsito;
	private BigDecimal mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mChiaveUfficio;
	private String mDescrChiaveUfficio;
	private BigDecimal mEveIdEvento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mMesIdMessaggioRichiesta;
	private BigDecimal mMesIdMessaggioEsito;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public AnnotazioneEsitoTrasmissioneModel() {
		this.mIdEsitoTrasmissione = null;
		this.mDataTrasmissione = null;
		this.mOggettoTrasmissione = "";
		this.mDescrOggettoTrasmissione = "";
		this.mCodUfficioDestinatario = "";
		this.mDescrUfficioDestinatario = "";
		this.mCodUfficioInoltrante = "";
		this.mDescrUfficioInoltrante = "";
		this.mCodUfficioInoltro = "";
		this.mDescrTipoUfficioInoltro = "";
		this.mDescrComuneUfficioInoltro = "";
		this.mCodUfficioEsito = "";
		this.mDescrTipoUfficioEsito = "";
		this.mDescrComuneUfficioEsito = "";
		this.mDataEsito = null;
		this.mCodEsito = "";
		this.mDescrEsito = "";
		this.mNoteEsito = "";
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mChiaveUfficio = "";
		this.mDescrChiaveUfficio = "";
		this.mEveIdEvento = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mMesIdMessaggioRichiesta = null;
		this.mMesIdMessaggioEsito = null;
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
	public AnnotazioneEsitoTrasmissioneModel(AnnotazioneEsitoTrasmissioneModel aModel) {
		this.mIdEsitoTrasmissione = aModel.mIdEsitoTrasmissione;
		this.mDataTrasmissione = aModel.mDataTrasmissione;
		this.mOggettoTrasmissione = aModel.mOggettoTrasmissione;
		this.mDescrOggettoTrasmissione = aModel.mDescrOggettoTrasmissione;
		this.mCodUfficioDestinatario = aModel.mCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aModel.mDescrUfficioDestinatario;
		this.mCodUfficioInoltrante = aModel.mCodUfficioInoltrante;
		this.mDescrUfficioInoltrante = aModel.mDescrUfficioInoltrante;
		this.mCodUfficioInoltro = aModel.mCodUfficioInoltro;
		this.mDescrTipoUfficioInoltro = aModel.mDescrTipoUfficioInoltro;
		this.mDescrComuneUfficioInoltro = aModel.mDescrComuneUfficioInoltro;
		this.mCodUfficioEsito = aModel.mCodUfficioEsito;
		this.mDescrTipoUfficioEsito = aModel.mDescrTipoUfficioEsito;
		this.mDescrComuneUfficioEsito = aModel.mDescrComuneUfficioEsito;
		this.mDataEsito = aModel.mDataEsito;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mNoteEsito = aModel.mNoteEsito;
		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mChiaveUfficio = aModel.mChiaveUfficio;
		this.mDescrChiaveUfficio = aModel.mDescrChiaveUfficio;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mMesIdMessaggioRichiesta = aModel.mMesIdMessaggioRichiesta;
		this.mMesIdMessaggioEsito = aModel.mMesIdMessaggioEsito;
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
	public AnnotazioneEsitoTrasmissioneModel(BigDecimal aIdEsitoTrasmissione, Date aDataTrasmissione,
			String aOggettoTrasmissione, String aDescrOggettoTrasmissione, String aCodUfficioDestinatario,
			String aDescrUfficioDestinatario, String aCodUfficioInoltrante, String aDescrUfficioInoltrante,
			String aCodUfficioInoltro, String aDescrTipoUfficioInoltro, String aDescrComuneUfficioInoltro,
			String aCodUfficioEsito, String aDescrTipoUfficioEsito, String aDescrComuneUfficioEsito,
			Date aDataEsito, String aCodEsito, String aDescrEsito, String aNoteEsito, BigDecimal aChiaveAnno,
			BigDecimal aChiaveProgr, String aChiaveUfficio, String aDescrChiaveUfficio,
			BigDecimal aEveIdEvento, BigDecimal aFasSieIdFascicoloSiep, BigDecimal aMesIdMessaggioRichiesta,
			BigDecimal aMesIdMessaggioEsito, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento) {
		this.mIdEsitoTrasmissione = aIdEsitoTrasmissione;
		this.mDataTrasmissione = aDataTrasmissione;
		this.mOggettoTrasmissione = aOggettoTrasmissione;
		this.mDescrOggettoTrasmissione = aDescrOggettoTrasmissione;
		this.mCodUfficioDestinatario = aCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aDescrUfficioDestinatario;
		this.mCodUfficioInoltrante = aCodUfficioInoltrante;
		this.mDescrUfficioInoltrante = aDescrUfficioInoltrante;
		this.mCodUfficioInoltro = aCodUfficioInoltro;
		this.mDescrTipoUfficioInoltro = aDescrTipoUfficioInoltro;
		this.mDescrComuneUfficioInoltro = aDescrComuneUfficioInoltro;
		this.mCodUfficioEsito = aCodUfficioEsito;
		this.mDescrTipoUfficioEsito = aDescrTipoUfficioEsito;
		this.mDescrComuneUfficioEsito = aDescrComuneUfficioEsito;
		this.mDataEsito = aDataEsito;
		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mNoteEsito = aNoteEsito;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mChiaveUfficio = aChiaveUfficio;
		this.mDescrChiaveUfficio = aDescrChiaveUfficio;
		this.mEveIdEvento = aEveIdEvento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mMesIdMessaggioRichiesta = aMesIdMessaggioRichiesta;
		this.mMesIdMessaggioEsito = aMesIdMessaggioEsito;
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
	public BigDecimal getIdEsitoTrasmissione() {
		return mIdEsitoTrasmissione;
	}

	public Date getDataTrasmissione() {
		return mDataTrasmissione;
	}

	public String getOggettoTrasmissione() {
		return mOggettoTrasmissione;
	}

	public String getDescrOggettoTrasmissione() {
		return mDescrOggettoTrasmissione;
	}

	public String getCodUfficioDestinatario() {
		return mCodUfficioDestinatario;
	}

	public String getDescrUfficioDestinatario() {
		return mDescrUfficioDestinatario;
	}

	public String getCodUfficioInoltrante() {
		return mCodUfficioInoltrante;
	}

	public String getDescrUfficioInoltrante() {
		return mDescrUfficioInoltrante;
	}

	public String getCodUfficioInoltro() {
		return mCodUfficioInoltro;
	}

	public String getDescrTipoUfficioInoltro() {
		return mDescrTipoUfficioInoltro;
	}

	public String getDescrComuneUfficioInoltro() {
		return mDescrComuneUfficioInoltro;
	}

	public String getCodUfficioEsito() {
		return mCodUfficioEsito;
	}

	public String getDescrTipoUfficioEsito() {
		return mDescrTipoUfficioEsito;
	}

	public String getDescrComuneUfficioEsito() {
		return mDescrComuneUfficioEsito;
	}

	public Date getDataEsito() {
		return mDataEsito;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public String getNoteEsito() {
		return mNoteEsito;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getChiaveUfficio() {
		return mChiaveUfficio;
	}

	public String getDescrChiaveUfficio() {
		return mDescrChiaveUfficio;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getMesIdMessaggioRichiesta() {
		return mMesIdMessaggioRichiesta;
	}

	public BigDecimal getMesIdMessaggioEsito() {
		return mMesIdMessaggioEsito;
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

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdEsitoTrasmissione(BigDecimal aValore) {
		mIdEsitoTrasmissione = aValore;
	}

	public void setDataTrasmissione(Date aValore) {
		mDataTrasmissione = aValore;
	}

	public void setOggettoTrasmissione(String aValore) {
		mOggettoTrasmissione = aValore;
	}

	public void setDescrOggettoTrasmissione(String aValore) {
		mDescrOggettoTrasmissione = aValore;
	}

	public void setCodUfficioDestinatario(String aValore) {
		mCodUfficioDestinatario = aValore;
	}

	public void setDescrUfficioDestinatario(String aValore) {
		mDescrUfficioDestinatario = aValore;
	}

	public void setCodUfficioInoltrante(String aValore) {
		mCodUfficioInoltrante = aValore;
	}

	public void setDescrUfficioInoltrante(String aValore) {
		mDescrUfficioInoltrante = aValore;
	}

	public void setCodUfficioInoltro(String aValore) {
		mCodUfficioInoltro = aValore;
	}

	public void setDescrTipoUfficioInoltro(String aValore) {
		mDescrTipoUfficioInoltro = aValore;
	}

	public void setDescrComuneUfficioInoltro(String aValore) {
		mDescrComuneUfficioInoltro = aValore;
	}

	public void setCodUfficioEsito(String aValore) {
		mCodUfficioEsito = aValore;
	}

	public void setDescrTipoUfficioEsito(String aValore) {
		mDescrTipoUfficioEsito = aValore;
	}

	public void setDescrComuneUfficioEsito(String aValore) {
		mDescrComuneUfficioEsito = aValore;
	}

	public void setDataEsito(Date aValore) {
		mDataEsito = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setNoteEsito(String aValore) {
		mNoteEsito = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setChiaveUfficio(String aValore) {
		mChiaveUfficio = aValore;
	}

	public void setDescrChiaveUfficio(String aValore) {
		mDescrChiaveUfficio = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setMesIdMessaggioRichiesta(BigDecimal aValore) {
		mMesIdMessaggioRichiesta = aValore;
	}

	public void setMesIdMessaggioEsito(BigDecimal aValore) {
		mMesIdMessaggioEsito = aValore;
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

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "AnnotazioneEsitoTrasmissioneModel:\n" + "[ mIdEsitoTrasmissione       = "
				+ mIdEsitoTrasmissione + " ]\n" + "[ mDataTrasmissione          = " + mDataTrasmissione
				+ " ]\n" + "[ mOggettoTrasmissione       = " + mOggettoTrasmissione + " ]\n"
				+ "[ mCodUfficioDestinatario    = " + mCodUfficioDestinatario + " ]\n"
				+ "[ mCodUfficioInoltrante      = " + mCodUfficioInoltrante + " ]\n"
				+ "[ mCodUfficioInoltro         = " + mCodUfficioInoltro + " ]\n"
				+ "[ mCodUfficioEsito           = " + mCodUfficioEsito + " ]\n"
				+ "[ mDataEsito                 = " + mDataEsito + " ]\n" + "[ mCodEsito                  = "
				+ mCodEsito + " ]\n" + "[ mNoteEsito                 = " + mNoteEsito + " ]\n"
				+ "[ mChiaveAnno                = " + mChiaveAnno + " ]\n" + "[ mChiaveProgr               = "
				+ mChiaveProgr + " ]\n" + "[ mChiaveUfficio             = " + mChiaveUfficio + " ]\n"
				+ "[ mEveIdEvento               = " + mEveIdEvento + " ]\n"
				+ "[ mFasSieIdFascicoloSiep     = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mMesIdMessaggioRichiesta   = " + mMesIdMessaggioRichiesta + " ]\n"
				+ "[ mMesIdMessaggioEsito       = " + mMesIdMessaggioEsito + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

}