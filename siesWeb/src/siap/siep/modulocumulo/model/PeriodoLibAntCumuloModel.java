package siap.siep.modulocumulo.model;

import java.math.BigDecimal;

/**
* <p>Title: PeriodoLibAntCumuloModel</p>
* <p>Description: Classe Model che rappresenta il PeriodoLibAntCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;

public class PeriodoLibAntCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4974409411687742454L;
	private BigDecimal mIdPeriodoLibAntCumulo;
	private Date mDataInizio;
	private Date mDataFine;

	private BigDecimal mLibIdLibAnticipataCumulo;
	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mIdPeriodoLibantOrigine;

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
	public PeriodoLibAntCumuloModel() {
		this.mIdPeriodoLibAntCumulo = null;
		this.mDataInizio = null;
		this.mDataFine = null;

		this.mLibIdLibAnticipataCumulo = null;
		this.mFlagStato = "";
		this.mMotivoModifica = "";
		this.mIdPeriodoLibantOrigine = null;

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
	public PeriodoLibAntCumuloModel(PeriodoLibAntCumuloModel aModel) {
		this.mIdPeriodoLibAntCumulo = aModel.mIdPeriodoLibAntCumulo;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;

		this.mLibIdLibAnticipataCumulo = aModel.mLibIdLibAnticipataCumulo;
		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mIdPeriodoLibantOrigine = aModel.mIdPeriodoLibantOrigine;

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
	public PeriodoLibAntCumuloModel(BigDecimal aIdPeriodoLibAntCumulo, Date aDataInizio, Date aDataFine,
			BigDecimal aLibIdLibAnticipataCumulo, String aFlagStato, String aMotivoModifica,
			BigDecimal aIdPeriodoLibantOrigine, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento) {
		this.mIdPeriodoLibAntCumulo = aIdPeriodoLibAntCumulo;
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mLibIdLibAnticipataCumulo = aLibIdLibAnticipataCumulo;
		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mIdPeriodoLibantOrigine = aIdPeriodoLibantOrigine;
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
	public BigDecimal getIdPeriodoLibAntCumulo() {
		return mIdPeriodoLibAntCumulo;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public BigDecimal getLibIdLibAnticipataCumulo() {
		return mLibIdLibAnticipataCumulo;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public BigDecimal getIdPeriodoLibantOrigine() {
		return mIdPeriodoLibantOrigine;
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
	public void setIdPeriodoLibAntCumulo(BigDecimal aValore) {
		mIdPeriodoLibAntCumulo = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setLibIdLibAnticipataCumulo(BigDecimal aValore) {
		mLibIdLibAnticipataCumulo = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setIdPeriodoLibantOrigine(BigDecimal aValore) {
		mIdPeriodoLibantOrigine = aValore;
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
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "PeriodoLibAntCumuloModel:\n" + "[ mIdPeriodoLibAntCumulo     = " + mIdPeriodoLibAntCumulo
				+ " ]\n" + "[ mDataInizio                = " + mDataInizio + " ]\n"
				+ "[ mDataFine                  = " + mDataFine + " ]\n" + "[ mLibIdLibAnticipataCumulo  = "
				+ mLibIdLibAnticipataCumulo + " ]\n" + "[ mFlagStato                 = " + mFlagStato + " ]\n"
				+ "[ mMotivoModifica            = " + mMotivoModifica + " ]\n"
				+ "[ mIdPeriodoLibantOrigine    = " + mIdPeriodoLibantOrigine + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}
}
