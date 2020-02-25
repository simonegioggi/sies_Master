package siap.siep.misurasicurezza.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * Model che mappa il contenuto della tabella FASC_MS_TO_FASC_SIEP che serve per legare un fascicolo di classe
 * IV (esecuzione Misure di Sicurezza) a un fascicoli di classe I
 * 
 * @author d.fiorletta
 *
 */
public class FascMsToFascSiepModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5396716143162069135L;

	private BigDecimal mIdFascMsToFascSiep;

	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mChiaveAnnoSiep;
	private BigDecimal mChiaveProgrSiep;
	private String mChiaveUfficioSiep;
	private String mDescTipoUfficioSiep;
	private String mDescComuneUfficioSiep;

	private String mCodTipoRelazioneMS;

	private Date mDataCumulo;

	private BigDecimal mFasSieIdFascicoloCollegato;
	private BigDecimal mChiaveAnnoSiepCollegato;
	private BigDecimal mChiaveProgrSiepCollegato;
	private String mChiaveUfficioSiepCollegato;
	private String mDescTipoUfficioSiepCollegato;
	private String mDescComuneUfficioSiepCollegato;

	private BigDecimal mMesIdMessaggio;
	private BigDecimal mEveIdEvento;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	/**
   * 
   */
	public FascMsToFascSiepModel() {
		this.mIdFascMsToFascSiep = null;

		this.mFasSieIdFascicoloSiep = null;
		this.mChiaveAnnoSiep = null;
		this.mChiaveProgrSiep = null;
		this.mChiaveUfficioSiep = "";
		this.mDescTipoUfficioSiep = "";
		this.mDescComuneUfficioSiep = "";

		this.mDataCumulo = null;
		this.mCodTipoRelazioneMS = null;

		this.mFasSieIdFascicoloCollegato = null;
		this.mChiaveAnnoSiepCollegato = null;
		this.mChiaveProgrSiepCollegato = null;
		this.mChiaveUfficioSiepCollegato = "";
		this.mDescTipoUfficioSiepCollegato = "";
		this.mDescComuneUfficioSiepCollegato = "";

		this.mMesIdMessaggio = null;
		this.mEveIdEvento = null;

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
	}

	/**
	 * 
	 * @param aIdFascMsToFascSiep
	 * @param aFasSieIdFascicoloSiep
	 * @param aChiaveAnnoSiep
	 * @param aChiaveProgrSiep
	 * @param aChiaveUfficioSiep
	 * @param aDescTipoUfficioSiep
	 * @param aDescComuneUfficioSiep
	 * @param aCodTipoRelazioneMS
	 * @param aDataCumulo
	 * @param aFasSieIdFascicoloCollegato
	 * @param aChiaveAnnoSiepCollegato
	 * @param aChiaveProgrSiepCollegato
	 * @param aChiaveUfficioSiepCollegato
	 * @param aDescTipoUfficioSiepCollegato
	 * @param aDescComuneUfficioSiepCollegato
	 * @param aMesIdMessaggio
	 * @param aEveIdEvento
	 * @param aCodOperatoreInserimento
	 * @param aDataInserimento
	 * @param aCodUfficioInserimento
	 * @param aDescrUfficioInserimento
	 * @param aCodOperatoreAggiornamento
	 * @param aDataAggiornamento
	 * @param aCodUfficioAggiornamento
	 * @param aDescrUfficioAggiornamento
	 */
	public FascMsToFascSiepModel(BigDecimal aIdFascMsToFascSiep, BigDecimal aFasSieIdFascicoloSiep,
			BigDecimal aChiaveAnnoSiep, BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep,
			String aDescTipoUfficioSiep, String aDescComuneUfficioSiep,

			String aCodTipoRelazioneMS,

			Date aDataCumulo,

			BigDecimal aFasSieIdFascicoloCollegato, BigDecimal aChiaveAnnoSiepCollegato,
			BigDecimal aChiaveProgrSiepCollegato, String aChiaveUfficioSiepCollegato,
			String aDescTipoUfficioSiepCollegato, String aDescComuneUfficioSiepCollegato,

			BigDecimal aMesIdMessaggio, BigDecimal aEveIdEvento,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento) {
		this.mIdFascMsToFascSiep = aIdFascMsToFascSiep;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mChiaveAnnoSiep = aChiaveAnnoSiep;
		this.mChiaveProgrSiep = aChiaveProgrSiep;
		this.mChiaveUfficioSiep = aChiaveUfficioSiep;
		this.mDescTipoUfficioSiep = aDescTipoUfficioSiep;
		this.mDescComuneUfficioSiep = aDescComuneUfficioSiep;

		this.mCodTipoRelazioneMS = aCodTipoRelazioneMS;

		this.mDataCumulo = aDataCumulo;

		this.mFasSieIdFascicoloCollegato = aFasSieIdFascicoloCollegato;
		this.mChiaveAnnoSiepCollegato = aChiaveAnnoSiepCollegato;
		this.mChiaveProgrSiepCollegato = aChiaveProgrSiepCollegato;
		this.mChiaveUfficioSiepCollegato = aChiaveUfficioSiepCollegato;
		this.mDescTipoUfficioSiepCollegato = aDescTipoUfficioSiepCollegato;
		this.mDescComuneUfficioSiepCollegato = aDescComuneUfficioSiepCollegato;

		this.mMesIdMessaggio = aMesIdMessaggio;
		this.mEveIdEvento = aEveIdEvento;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
	}

	// =============================================
	// Metodi Getter
	// =============================================
	public BigDecimal getIdFascMsToFascSiep() {
		return mIdFascMsToFascSiep;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getChiaveAnnoSiep() {
		return mChiaveAnnoSiep;
	}

	public BigDecimal getChiaveProgrSiep() {
		return mChiaveProgrSiep;
	}

	public String getChiaveUfficioSiep() {
		return mChiaveUfficioSiep;
	}

	public String getDescTipoUfficioSiep() {
		return mDescTipoUfficioSiep;
	}

	public String getDescComuneUfficioSiep() {
		return mDescComuneUfficioSiep;
	}

	public String getCodTipoRelazioneMS() {
		return mCodTipoRelazioneMS;
	}

	public Date getDataCumulo() {
		return mDataCumulo;
	}

	public BigDecimal getFasSieIdFascicoloCollegato() {
		return mFasSieIdFascicoloCollegato;
	}

	public BigDecimal getChiaveAnnoSiepCollegato() {
		return mChiaveAnnoSiepCollegato;
	}

	public BigDecimal getChiaveProgrSiepCollegato() {
		return mChiaveProgrSiepCollegato;
	}

	public String getChiaveUfficioSiepCollegato() {
		return mChiaveUfficioSiepCollegato;
	}

	public String getDescTipoUfficioSiepCollegato() {
		return mDescTipoUfficioSiepCollegato;
	}

	public String getDescComuneUfficioSiepCollegato() {
		return mDescComuneUfficioSiepCollegato;
	}

	public BigDecimal getMesIdMessaggio() {
		return mMesIdMessaggio;
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

	// =============================================
	// Metodi Setter
	// =============================================
	public void setIdFascMsToFascSiep(BigDecimal aValore) {
		this.mIdFascMsToFascSiep = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		this.mFasSieIdFascicoloSiep = aValore;
	}

	public void setChiaveAnnoSiep(BigDecimal aValore) {
		this.mChiaveAnnoSiep = aValore;
	}

	public void setChiaveProgrSiep(BigDecimal aValore) {
		this.mChiaveProgrSiep = aValore;
	}

	public void setChiaveUfficioSiep(String aValore) {
		this.mChiaveUfficioSiep = aValore;
	}

	public void setDescTipoUfficioSiep(String aValore) {
		this.mDescTipoUfficioSiep = aValore;
	}

	public void setDescComuneUfficioSiep(String aValore) {
		this.mDescComuneUfficioSiep = aValore;
	}

	public void setCodTipoRelazioneMS(String aValore) {
		this.mCodTipoRelazioneMS = aValore;
	}

	public void setDataCumulo(Date aValore) {
		this.mDataCumulo = aValore;
	}

	public void setFasSieIdFascicoloCollegato(BigDecimal aValore) {
		this.mFasSieIdFascicoloCollegato = aValore;
	}

	public void setChiaveAnnoSiepCollegato(BigDecimal aValore) {
		this.mChiaveAnnoSiepCollegato = aValore;
	}

	public void setChiaveProgrSiepCollegato(BigDecimal aValore) {
		this.mChiaveProgrSiepCollegato = aValore;
	}

	public void setChiaveUfficioSiepCollegato(String aValore) {
		this.mChiaveUfficioSiepCollegato = aValore;
	}

	public void setDescTipoUfficioSiepCollegato(String aValore) {
		this.mDescTipoUfficioSiepCollegato = aValore;
	}

	public void setDescComuneUfficioSiepCollegato(String aValore) {
		this.mDescComuneUfficioSiepCollegato = aValore;
	}

	public void setMesIdMessaggio(BigDecimal aValore) {
		this.mMesIdMessaggio = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		this.mEveIdEvento = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		this.mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		this.mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		this.mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		this.mDescrUfficioInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		this.mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		this.mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		this.mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		this.mDescrUfficioAggiornamento = aValore;
	}

	/**
	 * 
	 * @return
	 */
	public String toString2() {
		String lToString = "[ mIdFascMsToFascSiep        = " + mIdFascMsToFascSiep + "]\n"
				+ "[ mFasSieIdFascicoloSiep     = " + mFasSieIdFascicoloSiep + "]\n"
				+ "[ mChiaveAnnoSiep            = " + mChiaveAnnoSiep + "]\n"
				+ "[ mChiaveProgrSiep           = " + mChiaveProgrSiep + "]\n"
				+ "[ mChiaveUfficioSiep         = " + mChiaveUfficioSiep + "]\n"
				+ "[ mDescTipoUfficioSiep       = " + mDescTipoUfficioSiep + "]\n"
				+ "[ mDescComuneUfficioSiep     = " + mDescComuneUfficioSiep + "]\n"
				+ "[ mCodTipoRelazioneMS        = " + mCodTipoRelazioneMS + "]\n" +

				"[ mDataCumulo                = " + mDataCumulo + "]\n" +

				"[ mFasSieIdFascicoloCollegato = " + mFasSieIdFascicoloCollegato + "]\n"
				+ "[ mChiaveAnnoSiepCollegato        = " + mChiaveAnnoSiepCollegato + "]\n"
				+ "[ mChiaveProgrSiepCollegato       = " + mChiaveProgrSiepCollegato + "]\n"
				+ "[ mChiaveUfficioSiepCollegato     = " + mChiaveUfficioSiepCollegato + "]\n"
				+ "[ mDescTipoUfficioSiepCollegato   = " + mDescTipoUfficioSiepCollegato + "]\n"
				+ "[ mDescComuneUfficioSiepCollegato = " + mDescComuneUfficioSiepCollegato + "]\n"
				+ "[ mMesIdMessaggio            = " + mMesIdMessaggio + "]\n"
				+ "[ mEveIdEvento               = " + mEveIdEvento + "]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + "]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + "]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + "]\n"
				+ "[ mDescrUfficioInserimento   = " + mDescrUfficioInserimento + "]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + "]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + "]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + "]\n"
				+ "[ mDescrUfficioAggiornamento = " + mDescrUfficioAggiornamento + "]";

		return lToString;
	}

}