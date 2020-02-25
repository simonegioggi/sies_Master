package siap.siep.modulocumulo.model;

import java.math.BigDecimal;

/**
* <p>Title: LibAnticipataCumuloModel</p>
* <p>Description: Classe Model che rappresenta il LibAnticipataCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;
import java.util.Vector;

import f3b.model.GenericModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;

public class LibAnticipataCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 4830121307011560884L;
	private BigDecimal mIdLibAnticipataCumulo;
	private String mCodTipoLicenza;
	private String mDescrTipoLicenza;
	private BigDecimal mNumeroGiorni;
	private BigDecimal mSommaRisarcDanni;
	private String mFlagConcesso;
	private String mTipoLa;
	private String mFlagElaborato;

	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mTitIdTitoloCumulato;
	private BigDecimal mIdLibanticipataOrigine;
	private BigDecimal mStatIdStatoEsecTitCum;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private Vector<PeriodoLibAntCumuloModel> mListaPeriodiLibAnticipate;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public LibAnticipataCumuloModel() {
		this.mIdLibAnticipataCumulo = null;
		this.mCodTipoLicenza = "";
		this.mDescrTipoLicenza = "";
		this.mNumeroGiorni = null;
		this.mSommaRisarcDanni = null;
		this.mFlagConcesso = "";
		this.mTipoLa = "";
		this.mFlagElaborato = "";
		this.mFlagStato = "";
		this.mMotivoModifica = "";
		this.mTitIdTitoloCumulato = null;
		this.mIdLibanticipataOrigine = null;
		this.mStatIdStatoEsecTitCum = null;
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
	public LibAnticipataCumuloModel(LibAnticipataCumuloModel aModel) {
		this.mIdLibAnticipataCumulo = aModel.mIdLibAnticipataCumulo;
		this.mCodTipoLicenza = aModel.mCodTipoLicenza;
		this.mDescrTipoLicenza = aModel.mDescrTipoLicenza;
		this.mNumeroGiorni = aModel.mNumeroGiorni;
		this.mSommaRisarcDanni = aModel.mSommaRisarcDanni;
		this.mFlagConcesso = aModel.mFlagConcesso;
		this.mTipoLa = aModel.mTipoLa;
		this.mFlagElaborato = aModel.mFlagElaborato;
		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mIdLibanticipataOrigine = aModel.mIdLibanticipataOrigine;
		this.mStatIdStatoEsecTitCum = aModel.mStatIdStatoEsecTitCum;
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
	public LibAnticipataCumuloModel(BigDecimal aIdLibAnticipataCumulo, String aCodTipoLicenza,
			String aDescrTipoLicenza, BigDecimal aNumeroGiorni, BigDecimal aSommaRisarcDanni,
			String aFlagConcesso, String aTipoLa, String aFlagElaborato,

			String aFlagStato, String aMotivoModifica, BigDecimal aTitIdTitoloCumulato,
			BigDecimal aIdLibanticipataOrigine, BigDecimal aStatIdStatoEsecTitCum,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdLibAnticipataCumulo = aIdLibAnticipataCumulo;
		this.mCodTipoLicenza = aCodTipoLicenza;
		this.mDescrTipoLicenza = aDescrTipoLicenza;
		this.mNumeroGiorni = aNumeroGiorni;
		this.mSommaRisarcDanni = aSommaRisarcDanni;
		this.mFlagConcesso = aFlagConcesso;
		this.mTipoLa = aTipoLa;
		this.mFlagElaborato = aFlagElaborato;

		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mIdLibanticipataOrigine = aIdLibanticipataOrigine;
		this.mStatIdStatoEsecTitCum = aStatIdStatoEsecTitCum;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input relativo alla Table LicenzaLibAnticipataModel
	 * 
	 * @param aModel
	 ****************************************************************************/
	public LibAnticipataCumuloModel(LicenzaLibAnticipataModel aModel) {
		this.mIdLibAnticipataCumulo = null;
		this.mCodTipoLicenza = aModel.getCodTipoLicenza();
		this.mDescrTipoLicenza = aModel.getDescrTipoLicenza();
		this.mNumeroGiorni = aModel.getNumeroGiorni();
		this.mSommaRisarcDanni = aModel.getSommaRisarcDanni();
		this.mFlagConcesso = aModel.getFlagConcesso();

		// Rimappatura dei codici tipo LA.
		// In SIEP sono previsti 7 casi (null,LA,LAU,LS,LSU,LI,LIU)
		// Ai fini del Cumulo ne servono solo 3 LA,LS,LI
		if (aModel.getDescrStatoPermesso() == null // vecchie LA ordinarie
				|| "LA".equals(aModel.getDescrStatoPermesso()) // nuove LA ordinarie
				|| "LAU".equals(aModel.getDescrStatoPermesso()) // nuove LA ordinarie iscritte SIUS senza
																// periodi
		) {
			this.mTipoLa = "LA"; // LA Ordinaria
		} else if ("LS".equals(aModel.getDescrStatoPermesso()) // nuove LA speciali
				|| "LSU".equals(aModel.getDescrStatoPermesso()) // nuove LA speciali iscritte SIUS senza
																// periodi
		) {
			this.mTipoLa = "LS"; // LA Speciale
		} else if ("LI".equals(aModel.getDescrStatoPermesso()) // nuove LA integrazione
				|| "LIU".equals(aModel.getDescrStatoPermesso()) // nuove LA integrazione iscritte SIUS senza
																// periodi
		) {
			this.mTipoLa = "LI"; // LA di integrazione
		}

		// this.mTipoLa = aModel.getDescrStatoPermesso();

		this.mFlagElaborato = aModel.getFlagElaborato();
		// this.mFlagStato = aModel.mFlagStato;
		// this.mMotivoModifica = aModel.mMotivoModifica;
		// this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		// this.mStatIdStatoEsecTitCum = aModel.mStatIdStatoEsecTitCum;
		this.mIdLibanticipataOrigine = aModel.getIdLicenzaLibanticipata();

	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdLibAnticipataCumulo() {
		return mIdLibAnticipataCumulo;
	}

	public String getCodTipoLicenza() {
		return mCodTipoLicenza;
	}

	public String getDescrTipoLicenza() {
		return mDescrTipoLicenza;
	}

	public BigDecimal getNumeroGiorni() {
		return mNumeroGiorni;
	}

	public BigDecimal getSommaRisarcDanni() {
		return mSommaRisarcDanni;
	}

	public String getFlagConcesso() {
		return mFlagConcesso;
	}

	public String getTipoLa() {
		return mTipoLa;
	}

	public String getFlagElaborato() {
		return mFlagElaborato;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public BigDecimal getIdLibanticipataOrigine() {
		return mIdLibanticipataOrigine;
	}

	public BigDecimal getStatIdStatoEsecTitoloCum() {
		return mStatIdStatoEsecTitCum;
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

	public Vector<PeriodoLibAntCumuloModel> getListaPeriodiLibAnticipate() {
		return mListaPeriodiLibAnticipate;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdLibAnticipataCumulo(BigDecimal aValore) {
		mIdLibAnticipataCumulo = aValore;
	}

	public void setCodTipoLicenza(String aValore) {
		mCodTipoLicenza = aValore;
	}

	public void setDescrTipoLicenza(String aValore) {
		mDescrTipoLicenza = aValore;
	}

	public void setNumeroGiorni(BigDecimal aValore) {
		mNumeroGiorni = aValore;
	}

	public void setSommaRisarcDanni(BigDecimal aValore) {
		mSommaRisarcDanni = aValore;
	}

	public void setFlagConcesso(String aValore) {
		mFlagConcesso = aValore;
	}

	public void setTipoLa(String aValore) {
		mTipoLa = aValore;
	}

	public void setFlagElaborato(String aValore) {
		mFlagElaborato = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setIdLibanticipataOrigine(BigDecimal aValore) {
		mIdLibanticipataOrigine = aValore;
	}

	public void setStatIdStatoEsecTitoloCum(BigDecimal aValore) {
		mStatIdStatoEsecTitCum = aValore;
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

	public void setListaPeriodiLibAnticipate(Vector<PeriodoLibAntCumuloModel> aValore) {
		mListaPeriodiLibAnticipate = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "LibAnticipataCumuloModel:\n" + "[ mIdLibAnticipataCumulo     = " + mIdLibAnticipataCumulo
				+ " ]\n" + "[ mCodTipoLicenza            = " + mCodTipoLicenza + " ]\n"
				+ "[ mNumeroGiorni              = " + mNumeroGiorni + " ]\n"
				+ "[ mSommaRisarcDanni          = " + mSommaRisarcDanni + " ]\n"
				+ "[ mFlagConcesso              = " + mFlagConcesso + " ]\n"
				+ "[ mTipoLa                    = " + mTipoLa + " ]\n" + "[ mFlagElaborato             = "
				+ mFlagElaborato + " ]\n" + "[ mFlagStato                 = " + mFlagStato + " ]\n"
				+ "[ mMotivoModifica            = " + mMotivoModifica + " ]\n"
				+ "[ mTitIdTitoloCumulato       = " + mTitIdTitoloCumulato + " ]\n"
				+ "[ mIdLibanticipataOrigine    = " + mIdLibanticipataOrigine + " ]\n"
				+ "[ mStatIdStatoEsecTitCum  	 = " + mStatIdStatoEsecTitCum + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}
}
