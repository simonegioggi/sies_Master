package siap.bdmc.fascicolosiepbdmc.model;

/**
* <p>Title: FascicoloSiepBdmcModel</p>
* <p>Description: Classe Model che rappresenta il FascicoloSiepBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class FascicoloSiepBdmcModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1793384838744899483L;

	private BigDecimal mIdFascicoloBdmc;
	private BigDecimal mChiaveAnnoBdmc;
	private String mChiaveUfficioBdmc;
	private BigDecimal mChiaveProgrBdmc;
	private BigDecimal mChiaveAnnoSiep;
	private String mChiaveUfficioSiep;
	private BigDecimal mChiaveProgrSiep;
	private String mFlagTrasmissione;
	private Date mDataTrasmissione;
	private Date mDataDisattivazione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mTipoMisura;

	private BigDecimal mIdEvento;
	private String mCodComune;
	private String mAltroLuogo;
	private String mFlagOrdineEsecuzione;
	private String mIstitutoDetenzione;

	// private String mDescrUfficioAggiornamento;
	// private String mCodUfficioEmittente;
	// private String mDescrLuogoEmittente;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public FascicoloSiepBdmcModel() {
		this.mIdFascicoloBdmc = null;
		this.mChiaveAnnoBdmc = null;
		this.mChiaveUfficioBdmc = "";
		this.mChiaveProgrBdmc = null;
		this.mChiaveAnnoSiep = null;
		this.mChiaveUfficioSiep = "";
		this.mChiaveProgrSiep = null;
		this.mFlagTrasmissione = "";
		this.mDataTrasmissione = null;
		this.mDataDisattivazione = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mTipoMisura = "";
		this.mIdEvento = null;
		this.mCodComune = "";
		this.mAltroLuogo = "";
		this.mFlagOrdineEsecuzione = "";
		this.mIstitutoDetenzione = "";
		// this.mDescrUfficioAggiornamento = "";
		// this.mCodUfficioEmittente = "";
		// this.mDescrLuogoEmittente = "";

	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public FascicoloSiepBdmcModel(FascicoloSiepBdmcModel aModel) {
		this.mIdFascicoloBdmc = aModel.mIdFascicoloBdmc;
		this.mChiaveAnnoBdmc = aModel.mChiaveAnnoBdmc;
		this.mChiaveUfficioBdmc = aModel.mChiaveUfficioBdmc;
		this.mChiaveProgrBdmc = aModel.mChiaveProgrBdmc;
		this.mChiaveAnnoSiep = aModel.mChiaveAnnoSiep;
		this.mChiaveUfficioSiep = aModel.mChiaveUfficioSiep;
		this.mChiaveProgrSiep = aModel.mChiaveProgrSiep;
		this.mFlagTrasmissione = aModel.mFlagTrasmissione;
		this.mDataTrasmissione = aModel.mDataTrasmissione;
		this.mDataDisattivazione = aModel.mDataDisattivazione;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mTipoMisura = aModel.mTipoMisura;
		this.mIdEvento = aModel.mIdEvento;
		this.mCodComune = aModel.mCodComune;
		this.mAltroLuogo = aModel.mAltroLuogo;
		this.mFlagOrdineEsecuzione = aModel.mFlagOrdineEsecuzione;
		this.mIstitutoDetenzione = aModel.mIstitutoDetenzione;
		// this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		// inserimenti successivi per la gestione delle combo
		// this.mCodUfficioEmittente = aModel.mCodUfficioEmittente;
		// this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;

	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public FascicoloSiepBdmcModel(BigDecimal aIdFascicoloBdmc, BigDecimal aChiaveAnnoBdmc,
			String aChiaveUfficioBdmc, BigDecimal aChiaveProgrBdmc, BigDecimal aChiaveAnnoSiep,
			String aChiaveUfficioSiep, BigDecimal aChiaveProgrSiep, String aFlagTrasmissione,
			Date aDataTrasmissione, Date aDataDisattivazione, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento,
			// String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aTipoMisura, BigDecimal aIdEvento, String aCodComune, String aAltroLuogo,
			String aFlagOrdineEsecuzione, String aIstitutoDetenzione)
	// String aDescrUfficioAggiornamento)
	// String aCodUfficioEmittente,
	// String aDescrLuogoEmittente)

	{
		this.mIdFascicoloBdmc = aIdFascicoloBdmc;
		this.mChiaveAnnoBdmc = aChiaveAnnoBdmc;
		this.mChiaveUfficioBdmc = aChiaveUfficioBdmc;
		this.mChiaveProgrBdmc = aChiaveProgrBdmc;
		this.mChiaveAnnoSiep = aChiaveAnnoSiep;
		this.mChiaveUfficioSiep = aChiaveUfficioSiep;
		this.mChiaveProgrSiep = aChiaveProgrSiep;
		this.mFlagTrasmissione = aFlagTrasmissione;
		this.mDataTrasmissione = aDataTrasmissione;
		this.mDataDisattivazione = aDataDisattivazione;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		// this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mTipoMisura = aTipoMisura;
		this.mIdEvento = aIdEvento;
		this.mCodComune = aCodComune;
		this.mAltroLuogo = aAltroLuogo;
		this.mFlagOrdineEsecuzione = aFlagOrdineEsecuzione;
		this.mIstitutoDetenzione = aIstitutoDetenzione;
		// this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		// this.mCodUfficioEmittente = aCodUfficioEmittente;
		// this.mDescrLuogoEmittente = aDescrLuogoEmittente;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdFascicoloBdmc() {
		return mIdFascicoloBdmc;
	}

	public BigDecimal getChiaveAnnoBdmc() {
		return mChiaveAnnoBdmc;
	}

	public String getChiaveUfficioBdmc() {
		return mChiaveUfficioBdmc;
	}

	public BigDecimal getChiaveProgrBdmc() {
		return mChiaveProgrBdmc;
	}

	public BigDecimal getChiaveAnnoSiep() {
		return mChiaveAnnoSiep;
	}

	public String getChiaveUfficioSiep() {
		return mChiaveUfficioSiep;
	}

	public BigDecimal getChiaveProgrSiep() {
		return mChiaveProgrSiep;
	}

	public String getFlagTrasmissione() {
		return mFlagTrasmissione;
	}

	public Date getDataTrasmissione() {
		return mDataTrasmissione;
	}

	public Date getDataDisattivazione() {
		return mDataDisattivazione;
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

	public String getTipoMisura() {
		return mTipoMisura;
	}

	public BigDecimal getIdEvento() {
		return mIdEvento;
	}

	public String getCodComune() {
		return mCodComune;
	}

	public String getAltroLuogo() {
		return mAltroLuogo;
	}

	public String getFlagOrdineEsecuzione() {
		return mFlagOrdineEsecuzione;
	}

	public String getIstitutoDetenzione() {
		return mIstitutoDetenzione;
	}
	// public String getDescrUfficioAggiornamento() { return mDescrUfficioAggiornamento; }

	// public String getDescrLuogoEmittente() { return mDescrLuogoEmittente; }
	// public String getCodTipoAutoritaEmittente() { return mCodUfficioEmittente; }

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdFascicoloBdmc(BigDecimal aValore) {
		mIdFascicoloBdmc = aValore;
	}

	public void setChiaveAnnoBdmc(BigDecimal aValore) {
		mChiaveAnnoBdmc = aValore;
	}

	public void setChiaveUfficioBdmc(String aValore) {
		mChiaveUfficioBdmc = aValore;
	}

	public void setChiaveProgrBdmc(BigDecimal aValore) {
		mChiaveProgrBdmc = aValore;
	}

	public void setChiaveAnnoSiep(BigDecimal aValore) {
		mChiaveAnnoSiep = aValore;
	}

	public void setChiaveUfficioSiep(String aValore) {
		mChiaveUfficioSiep = aValore;
	}

	public void setChiaveProgrSiep(BigDecimal aValore) {
		mChiaveProgrSiep = aValore;
	}

	public void setFlagTrasmissione(String aValore) {
		mFlagTrasmissione = aValore;
	}

	public void setDataTrasmissione(Date aValore) {
		mDataTrasmissione = aValore;
	}

	public void setDataDisattivazione(Date aValore) {
		mDataDisattivazione = aValore;
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

	public void setTipoMisura(String aValore) {
		mTipoMisura = aValore;
	}

	public void setIdEvento(BigDecimal aValore) {
		mIdEvento = aValore;
	}

	public void setCodComune(String aValore) {
		mCodComune = aValore;
	}

	public void setAltroLuogo(String aValore) {
		mAltroLuogo = aValore;
	}

	public void setFlagOrdineEsecuzione(String aValore) {
		mFlagOrdineEsecuzione = aValore;
	}

	public void setIstitutoDetenzione(String aValore) {
		mIstitutoDetenzione = aValore;
	}

	// public void setDescrUfficioAggiornamento (String aValore ) { mDescrUfficioAggiornamento = aValore; }

	// public void setDescrLuogoEmittente(String aValore ) { mDescrLuogoEmittente = aValore; }
	// public void setCodTipoAutoritaEmittente(String aValore ) { mCodUfficioEmittente = aValore; }

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "FascicoloSiepBdmcModel:\n" + "[ mIdFascicoloBdmc           = " + mIdFascicoloBdmc + " ]\n"
				+ "[ mChiaveAnnoBdmc            = " + mChiaveAnnoBdmc + " ]\n"
				+ "[ mChiaveUfficioBdmc         = " + mChiaveUfficioBdmc + " ]\n"
				+ "[ mChiaveProgrBdmc           = " + mChiaveProgrBdmc + " ]\n"
				+ "[ mChiaveAnnoSiep            = " + mChiaveAnnoSiep + " ]\n"
				+ "[ mChiaveUfficioSiep         = " + mChiaveUfficioSiep + " ]\n"
				+ "[ mChiaveProgrSiep           = " + mChiaveProgrSiep + " ]\n"
				+ "[ mFlagTrasmissione          = " + mFlagTrasmissione + " ]\n"
				+ "[ mDataTrasmissione          = " + mDataTrasmissione + " ]\n"
				+ "[ mDataDisattivazione        = " + mDataDisattivazione + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mTipoMisura   			 = " + mTipoMisura + " ]\n" + "[ mIdEvento					 = "
				+ mIdEvento + " ]\n" + "[ mCodComune					 =  " + mCodComune + " ]\n"
				+ "[ mAltroLuogo				=  " + mAltroLuogo + " ]\n" + "[ mFlagOrdineEsecuzione		="
				+ mFlagOrdineEsecuzione + " ]\n" + "[ mIstitutoDetenzione		=  " + mIstitutoDetenzione
				+ " ]";

		return lStr;
	}

}