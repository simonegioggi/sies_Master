package siap.siep.istruttoriacumulo.model;

/**
* <p>Title: EsitoArchiviazioniCumuloModel</p>
* <p>Description: Classe Model che rappresenta il EsitoArchiviazioniCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;
import java.math.BigDecimal;
import f3b.model.GenericModel;

public class EsitoArchiviazioniCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1993447699070210528L;

	private BigDecimal mIdEsitoArchiviazioniCumulo;
	private String mFlagArchiviato;
	private String mDescrizioneEsito;
	private String mChiaveUfficio;
	private BigDecimal mChiaveAnnoFascSiep;
	private BigDecimal mChiaveProgrFascSiep;
	private BigDecimal mFasIdFascicoloSiep;
	private BigDecimal mIstrIdIstruttoriaCumulo;
	private BigDecimal mEveIdEvento;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;

	private String mCodStatoFascAttuale;
	private String mDescrizione;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public EsitoArchiviazioniCumuloModel() {
		this.mIdEsitoArchiviazioniCumulo = null;
		this.mFlagArchiviato = "";
		this.mDescrizioneEsito = "";
		this.mChiaveUfficio = "";
		this.mChiaveAnnoFascSiep = null;
		this.mChiaveProgrFascSiep = null;
		this.mFasIdFascicoloSiep = null;
		this.mIstrIdIstruttoriaCumulo = null;
		this.mEveIdEvento = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";

		this.mCodStatoFascAttuale = "";
		this.mDescrizione = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public EsitoArchiviazioniCumuloModel(EsitoArchiviazioniCumuloModel aModel) {
		this.mIdEsitoArchiviazioniCumulo = aModel.mIdEsitoArchiviazioniCumulo;
		this.mFlagArchiviato = aModel.mFlagArchiviato;
		this.mDescrizioneEsito = aModel.mDescrizioneEsito;
		this.mChiaveUfficio = aModel.mChiaveUfficio;
		this.mChiaveAnnoFascSiep = aModel.mChiaveAnnoFascSiep;
		this.mChiaveProgrFascSiep = aModel.mChiaveProgrFascSiep;
		this.mFasIdFascicoloSiep = aModel.mFasIdFascicoloSiep;
		this.mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public EsitoArchiviazioniCumuloModel(BigDecimal aIdEsitoArchiviazioniCumulo, String aFlagArchiviato,
			String aDescrizioneEsito, String aChiaveUfficio, BigDecimal aChiaveAnnoFascSiep,
			BigDecimal aChiaveProgrFascSiep, BigDecimal aFasIdFascicoloSiep,
			BigDecimal aIstrIdIstruttoriaCumulo, BigDecimal aEveIdEvento, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento) {
		this.mIdEsitoArchiviazioniCumulo = aIdEsitoArchiviazioniCumulo;
		this.mFlagArchiviato = aFlagArchiviato;
		this.mDescrizioneEsito = aDescrizioneEsito;
		this.mChiaveUfficio = aChiaveUfficio;
		this.mChiaveAnnoFascSiep = aChiaveAnnoFascSiep;
		this.mChiaveProgrFascSiep = aChiaveProgrFascSiep;
		this.mFasIdFascicoloSiep = aFasIdFascicoloSiep;
		this.mIstrIdIstruttoriaCumulo = aIstrIdIstruttoriaCumulo;
		this.mEveIdEvento = aEveIdEvento;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdEsitoArchiviazioniCumulo() {
		return mIdEsitoArchiviazioniCumulo;
	}

	public String getFlagArchiviato() {
		return mFlagArchiviato;
	}

	public String getDescrizioneEsito() {
		return mDescrizioneEsito;
	}

	public String getChiaveUfficio() {
		return mChiaveUfficio;
	}

	public BigDecimal getChiaveAnnoFascSiep() {
		return mChiaveAnnoFascSiep;
	}

	public BigDecimal getChiaveProgrFascSiep() {
		return mChiaveProgrFascSiep;
	}

	public BigDecimal getFasIdFascicoloSiep() {
		return mFasIdFascicoloSiep;
	}

	public BigDecimal getIstrIdIstruttoriaCumulo() {
		return mIstrIdIstruttoriaCumulo;
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

	public String getCodStatoFascAttuale() {
		return mCodStatoFascAttuale;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdEsitoArchiviazioniCumulo(BigDecimal aValore) {
		mIdEsitoArchiviazioniCumulo = aValore;
	}

	public void setFlagArchiviato(String aValore) {
		mFlagArchiviato = aValore;
	}

	public void setDescrizioneEsito(String aValore) {
		mDescrizioneEsito = aValore;
	}

	public void setChiaveUfficio(String aValore) {
		mChiaveUfficio = aValore;
	}

	public void setChiaveAnnoFascSiep(BigDecimal aValore) {
		mChiaveAnnoFascSiep = aValore;
	}

	public void setChiaveProgrFascSiep(BigDecimal aValore) {
		mChiaveProgrFascSiep = aValore;
	}

	public void setFasIdFascicoloSiep(BigDecimal aValore) {
		mFasIdFascicoloSiep = aValore;
	}

	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		mIstrIdIstruttoriaCumulo = aValore;
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

	public void setCodStatoFascAttuale(String aValore) {
		mCodStatoFascAttuale = aValore;
	}

	public void setDescrizione(String aValore) {
		mDescrizione = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "EsitoArchiviazioniCumuloModel:\n" + "[ mIdEsitoArchiviazioniCumulo = "
				+ mIdEsitoArchiviazioniCumulo + " ]\n" + "[ mFlagArchiviato             = " + mFlagArchiviato
				+ " ]\n" + "[ mDescrizioneEsito           = " + mDescrizioneEsito + " ]\n"
				+ "[ mChiaveUfficio              = " + mChiaveUfficio + " ]\n"
				+ "[ mChiaveAnnoFascSiep         = " + mChiaveAnnoFascSiep + " ]\n"
				+ "[ mChiaveProgrFascSiep        = " + mChiaveProgrFascSiep + " ]\n"
				+ "[ mFasIdFascicoloSiep         = " + mFasIdFascicoloSiep + " ]\n"
				+ "[ mIstrIdIstruttoriaCumulo    = " + mIstrIdIstruttoriaCumulo + " ]\n"
				+ "[ mEveIdEvento      		  = " + mEveIdEvento + " ]\n" + "[ mCodOperatoreInserimento    = "
				+ mCodOperatoreInserimento + " ]\n" + "[ mDataInserimento            = " + mDataInserimento
				+ " ]\n" + "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]";
		return lStr;
	}

}