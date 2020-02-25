package siap.siep.scambiosanzione.model;

/**
* <p>Title: ScambioSanzioneModel</p>
* <p>Description: Classe Model che rappresenta il ScambioSanzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class ScambioSanzioneModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7220531624217944465L;

	private BigDecimal mIdScambioSanzione;
	private String mCodTipoDecisione;
	private String mDescrTipoDecisione;
	private String mCodNaturaSanzione;
	private String mDescrNaturaSanzione;
	private String mCodTipoSanzione;
	private String mDescrTipoSanzione;
	private Date mDataInizio;
	private Date mDataFine;
	private String mNote;
	private BigDecimal mAnnoRegistro;
	private BigDecimal mNumeroRegistro;
	private BigDecimal mChiaveAnnoFascicoloSius;
	private BigDecimal mChiaveProgrFascicoloSius;
	private String mCodUfficioSorveglianza;
	private String mDescrUfficioSorveglianza;
	private String mCodUfficioEmittente;
	private String mDescrUfficioEmittente;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private Date mDataEmissione;
	private BigDecimal mEveIdEvento;
	private BigDecimal mFasSieIdFascicoloSiep;

	private String mComuneUfficioSorveglianza;
	private String mComuneUfficioEmittente;
	// conversione della sanzione sostitutiva paolo c. 3/3/2008
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumAnniReclusione;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public ScambioSanzioneModel() {
		this.mIdScambioSanzione = null;
		this.mCodTipoDecisione = "";
		this.mDescrTipoDecisione = "";
		this.mCodNaturaSanzione = "";
		this.mDescrNaturaSanzione = "";
		this.mCodTipoSanzione = "";
		this.mDescrTipoSanzione = "";
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mNote = "";
		this.mAnnoRegistro = null;
		this.mNumeroRegistro = null;
		this.mChiaveAnnoFascicoloSius = null;
		this.mChiaveProgrFascicoloSius = null;
		this.mCodUfficioSorveglianza = "";
		this.mDescrUfficioSorveglianza = "";
		this.mCodUfficioEmittente = "";
		this.mDescrUfficioEmittente = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mDataEmissione = null;
		this.mEveIdEvento = null;
		this.mFasSieIdFascicoloSiep = null;
		// conversione della sanzione sostitutiva paolo c. 3/3/2008
		this.mComuneUfficioSorveglianza = null;
		this.mComuneUfficioEmittente = null;
		this.mNumGiorniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumAnniArresto = null;
		this.mNumGiorniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumAnniReclusione = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public ScambioSanzioneModel(ScambioSanzioneModel aModel) {
		this.mIdScambioSanzione = aModel.mIdScambioSanzione;
		this.mCodTipoDecisione = aModel.mCodTipoDecisione;
		this.mDescrTipoDecisione = aModel.mDescrTipoDecisione;
		this.mCodNaturaSanzione = aModel.mCodNaturaSanzione;
		this.mDescrNaturaSanzione = aModel.mDescrNaturaSanzione;
		this.mCodTipoSanzione = aModel.mCodTipoSanzione;
		this.mDescrTipoSanzione = aModel.mDescrTipoSanzione;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mNote = aModel.mNote;
		this.mAnnoRegistro = aModel.mAnnoRegistro;
		this.mNumeroRegistro = aModel.mNumeroRegistro;
		this.mChiaveAnnoFascicoloSius = aModel.mChiaveAnnoFascicoloSius;
		this.mChiaveProgrFascicoloSius = aModel.mChiaveProgrFascicoloSius;
		this.mCodUfficioSorveglianza = aModel.mCodUfficioSorveglianza;
		this.mDescrUfficioSorveglianza = aModel.mDescrUfficioSorveglianza;
		this.mCodUfficioEmittente = aModel.mCodUfficioEmittente;
		this.mDescrUfficioEmittente = aModel.mDescrUfficioEmittente;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;

		this.mComuneUfficioSorveglianza = aModel.mComuneUfficioSorveglianza;
		this.mComuneUfficioEmittente = aModel.mComuneUfficioEmittente;
		// conversione della sanzione sostitutiva paolo c. 3/3/2008
		this.mNumGiorniArresto = aModel.mNumGiorniArresto;
		this.mNumMesiArresto = aModel.mNumMesiArresto;
		this.mNumAnniArresto = aModel.mNumAnniArresto;
		this.mNumGiorniReclusione = aModel.mNumGiorniReclusione;
		this.mNumMesiReclusione = aModel.mNumMesiReclusione;
		this.mNumAnniReclusione = aModel.mNumAnniReclusione;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public ScambioSanzioneModel(BigDecimal aIdScambioSanzione, String aCodTipoDecisione,
			String aDescrTipoDecisione, String aCodNaturaSanzione, String aDescrNaturaSanzione,
			String aCodTipoSanzione, String aDescrTipoSanzione, Date aDataInizio, Date aDataFine,
			String aNote, BigDecimal aAnnoRegistro, BigDecimal aNumeroRegistro,
			BigDecimal aChiaveAnnoFascicoloSius, BigDecimal aChiaveProgrFascicoloSius,
			String aCodUfficioSorveglianza, String aDescrUfficioSorveglianza, String aCodUfficioEmittente,
			String aDescrUfficioEmittente, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			Date aDataEmissione, BigDecimal aEveIdEvento, BigDecimal aFasSieIdFascicoloSiep,
			// conversione della sanzione sostitutiva paolo c. 3/3/2008
			BigDecimal aNumGiorniArresto, BigDecimal aNumMesiArresto, BigDecimal aNumAnniArresto,
			BigDecimal aNumGiorniReclusione, BigDecimal aNumMesiReclusione, BigDecimal amNumAnniReclusione) {
		this.mIdScambioSanzione = aIdScambioSanzione;
		this.mCodTipoDecisione = aCodTipoDecisione;
		this.mDescrTipoDecisione = aDescrTipoDecisione;
		this.mCodNaturaSanzione = aCodNaturaSanzione;
		this.mDescrNaturaSanzione = aDescrNaturaSanzione;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mDescrTipoSanzione = aDescrTipoSanzione;
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mNote = aNote;
		this.mAnnoRegistro = aAnnoRegistro;
		this.mNumeroRegistro = aNumeroRegistro;
		this.mChiaveAnnoFascicoloSius = aChiaveAnnoFascicoloSius;
		this.mChiaveProgrFascicoloSius = aChiaveProgrFascicoloSius;
		this.mCodUfficioSorveglianza = aCodUfficioSorveglianza;
		this.mDescrUfficioSorveglianza = aDescrUfficioSorveglianza;
		this.mCodUfficioEmittente = aCodUfficioEmittente;
		this.mDescrUfficioEmittente = aDescrUfficioEmittente;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mDataEmissione = aDataEmissione;
		this.mEveIdEvento = aEveIdEvento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		// conversione della sanzione sostitutiva paolo c. 3/3/2008
		this.mNumGiorniArresto = aNumGiorniArresto;
		this.mNumMesiArresto = aNumMesiArresto;
		this.mNumAnniArresto = aNumAnniArresto;
		this.mNumGiorniReclusione = aNumGiorniReclusione;
		this.mNumMesiReclusione = aNumMesiReclusione;
		this.mNumAnniReclusione = amNumAnniReclusione;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdScambioSanzione() {
		return mIdScambioSanzione;
	}

	public String getCodTipoDecisione() {
		return mCodTipoDecisione;
	}

	public String getDescrTipoDecisione() {
		return mDescrTipoDecisione;
	}

	public String getCodNaturaSanzione() {
		return mCodNaturaSanzione;
	}

	public String getDescrNaturaSanzione() {
		return mDescrNaturaSanzione;
	}

	public String getCodTipoSanzione() {
		return mCodTipoSanzione;
	}

	public String getDescrTipoSanzione() {
		return mDescrTipoSanzione;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public String getNote() {
		return mNote;
	}

	public BigDecimal getAnnoRegistro() {
		return mAnnoRegistro;
	}

	public BigDecimal getNumeroRegistro() {
		return mNumeroRegistro;
	}

	public BigDecimal getChiaveAnnoFascicoloSius() {
		return mChiaveAnnoFascicoloSius;
	}

	public BigDecimal getChiaveProgrFascicoloSius() {
		return mChiaveProgrFascicoloSius;
	}

	public String getCodUfficioSorveglianza() {
		return mCodUfficioSorveglianza;
	}

	public String getDescrUfficioSorveglianza() {
		return mDescrUfficioSorveglianza;
	}

	public String getCodUfficioEmittente() {
		return mCodUfficioEmittente;
	}

	public String getDescrUfficioEmittente() {
		return mDescrUfficioEmittente;
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

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getComuneUfficioSorveglianza() {
		return mComuneUfficioSorveglianza;
	}

	public String getComuneUfficioEmittente() {
		return mComuneUfficioEmittente;
	}

	// conversione della sanzione sostitutiva paolo c. 3/3/2008
	public BigDecimal getNumGiorniArresto() {
		return mNumGiorniArresto;
	}

	public BigDecimal getNumMesiArresto() {
		return mNumMesiArresto;
	}

	public BigDecimal getNumAnniArresto() {
		return mNumAnniArresto;
	}

	public BigDecimal getNumGiorniReclusione() {
		return mNumGiorniReclusione;
	}

	public BigDecimal getNumMesiReclusione() {
		return mNumMesiReclusione;
	}

	public BigDecimal getNumAnniReclusione() {
		return mNumAnniReclusione;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdScambioSanzione(BigDecimal aValore) {
		mIdScambioSanzione = aValore;
	}

	public void setCodTipoDecisione(String aValore) {
		mCodTipoDecisione = aValore;
	}

	public void setDescrTipoDecisione(String aValore) {
		mDescrTipoDecisione = aValore;
	}

	public void setCodNaturaSanzione(String aValore) {
		mCodNaturaSanzione = aValore;
	}

	public void setDescrNaturaSanzione(String aValore) {
		mDescrNaturaSanzione = aValore;
	}

	public void setCodTipoSanzione(String aValore) {
		mCodTipoSanzione = aValore;
	}

	public void setDescrTipoSanzione(String aValore) {
		mDescrTipoSanzione = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setAnnoRegistro(BigDecimal aValore) {
		mAnnoRegistro = aValore;
	}

	public void setNumeroRegistro(BigDecimal aValore) {
		mNumeroRegistro = aValore;
	}

	public void setChiaveAnnoFascicoloSius(BigDecimal aValore) {
		mChiaveAnnoFascicoloSius = aValore;
	}

	public void setChiaveProgrFascicoloSius(BigDecimal aValore) {
		mChiaveProgrFascicoloSius = aValore;
	}

	public void setCodUfficioSorveglianza(String aValore) {
		mCodUfficioSorveglianza = aValore;
	}

	public void setDescrUfficioSorveglianza(String aValore) {
		mDescrUfficioSorveglianza = aValore;
	}

	public void setCodUfficioEmittente(String aValore) {
		mCodUfficioEmittente = aValore;
	}

	public void setDescrUfficioEmittente(String aValore) {
		mDescrUfficioEmittente = aValore;
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

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setComuneUfficioSorveglianza(String aValore) {
		mComuneUfficioSorveglianza = aValore;
	}

	public void setComuneUfficioEmittente(String aValore) {
		mComuneUfficioEmittente = aValore;
	}

	// conversione della sanzione sostitutiva paolo c. 3/3/2008
	public void setNumGiorniArresto(BigDecimal aValore) {
		mNumGiorniArresto = aValore;
	}

	public void setNumMesiArresto(BigDecimal aValore) {
		mNumMesiArresto = aValore;
	}

	public void setNumAnniArresto(BigDecimal aValore) {
		mNumAnniArresto = aValore;
	}

	public void setNumGiorniReclusione(BigDecimal aValore) {
		mNumGiorniReclusione = aValore;
	}

	public void setNumMesiReclusione(BigDecimal aValore) {
		mNumMesiReclusione = aValore;
	}

	public void setNumAnniReclusione(BigDecimal aValore) {
		mNumAnniReclusione = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "ScambioSanzioneModel:\n" + "[ mIdScambioSanzione         = " + mIdScambioSanzione + " ]\n"
				+ "[ mCodTipoDecisione          = " + mCodTipoDecisione + " ]\n"
				+ "[ mCodNaturaSanzione         = " + mCodNaturaSanzione + " ]\n"
				+ "[ mCodTipoSanzione           = " + mCodTipoSanzione + " ]\n"
				+ "[ mDataInizio                = " + mDataInizio + " ]\n" + "[ mDataFine                  = "
				+ mDataFine + " ]\n" + "[ mNote                      = " + mNote + " ]\n"
				+ "[ mAnnoRegistro              = " + mAnnoRegistro + " ]\n"
				+ "[ mNumeroRegistro            = " + mNumeroRegistro + " ]\n"
				+ "[ mChiaveAnnoFascicoloSius   = " + mChiaveAnnoFascicoloSius + " ]\n"
				+ "[ mChiaveProgrFascicoloSius  = " + mChiaveProgrFascicoloSius + " ]\n"
				+ "[ mCodUfficioSorveglianza    = " + mCodUfficioSorveglianza + " ]\n"
				+ "[ mCodUfficioEmittente       = " + mCodUfficioEmittente + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mDataEmissione             = " + mDataEmissione + " ]\n"
				+ "[ mEveIdEvento               = " + mEveIdEvento + " ]\n" + "[ mFasSieIdFascicoloSiep	 = "
				+ mFasSieIdFascicoloSiep + "  ]\n" +
				// conversione della sanzione sostitutiva paolo c. 3/3/2008
				"[ mNumGiorniArresto			 = " + mNumGiorniArresto + "  ]\n"
				+ "[ mNumMesiArresto 			 = " + mNumMesiArresto + "  ]\n"
				+ "[ mNumAnniArresto 			 = " + mNumAnniArresto + "  ]\n"
				+ "[ mNumGiorniReclusione 		 = " + mNumGiorniReclusione + "  ]\n"
				+ "[ mNumMesiReclusione 		 = " + mNumMesiReclusione + "  ]\n"
				+ "[ mNumAnniReclusione 		 = " + mNumAnniReclusione + " ]";

		return lStr;
	}

}