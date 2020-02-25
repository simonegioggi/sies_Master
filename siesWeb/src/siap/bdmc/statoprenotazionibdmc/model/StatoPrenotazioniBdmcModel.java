package siap.bdmc.statoprenotazionibdmc.model;

/**
* <p>Title: StatoPrenotazioniBdmcModel</p>
* <p>Description: Classe Model che rappresenta il StatoPrenotazioniBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class StatoPrenotazioniBdmcModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3817605551984451353L;

	private BigDecimal mStatoPrenotazioniBdmc;
	private BigDecimal mIdMisuraCautelareBdmc;
	private Date mDataTrasmissione;
	private BigDecimal mEsitoId;
	private String mEsitoMsg;
	private BigDecimal mIdPrenotazione;
	private BigDecimal mProgPeriPres;
	private String mTipoTrasmissione;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public StatoPrenotazioniBdmcModel() {
		this.mStatoPrenotazioniBdmc = null;
		this.mIdMisuraCautelareBdmc = null;
		this.mDataTrasmissione = null;
		this.mEsitoId = null;
		this.mEsitoMsg = "";
		this.mIdPrenotazione = null;
		this.mProgPeriPres = null;
		this.mTipoTrasmissione = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public StatoPrenotazioniBdmcModel(StatoPrenotazioniBdmcModel aModel) {
		this.mStatoPrenotazioniBdmc = aModel.mStatoPrenotazioniBdmc;
		this.mIdMisuraCautelareBdmc = aModel.mIdMisuraCautelareBdmc;
		this.mDataTrasmissione = aModel.mDataTrasmissione;
		this.mEsitoId = aModel.mEsitoId;
		this.mEsitoMsg = aModel.mEsitoMsg;
		this.mIdPrenotazione = aModel.mIdPrenotazione;
		this.mProgPeriPres = aModel.mProgPeriPres;
		this.mTipoTrasmissione = aModel.mTipoTrasmissione;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public StatoPrenotazioniBdmcModel(BigDecimal aStatoPrenotazioniBdmc, BigDecimal aIdMisuraCautelareBdmc,
			Date aDataTrasmissione, BigDecimal aEsitoId, String aEsitoMsg, BigDecimal aIdPrenotazione,
			BigDecimal aProgPeriPres, String aTipoTrasmissione) {
		this.mStatoPrenotazioniBdmc = aStatoPrenotazioniBdmc;
		this.mIdMisuraCautelareBdmc = aIdMisuraCautelareBdmc;
		this.mDataTrasmissione = aDataTrasmissione;
		this.mEsitoId = aEsitoId;
		this.mEsitoMsg = aEsitoMsg;
		this.mIdPrenotazione = aIdPrenotazione;
		this.mProgPeriPres = aProgPeriPres;
		this.mTipoTrasmissione = aTipoTrasmissione;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getStatoPrenotazioniBdmc() {
		return mStatoPrenotazioniBdmc;
	}

	public BigDecimal getIdMisuraCautelareBdmc() {
		return mIdMisuraCautelareBdmc;
	}

	public Date getDataTrasmissione() {
		return mDataTrasmissione;
	}

	public BigDecimal getEsitoId() {
		return mEsitoId;
	}

	public String getEsitoMsg() {
		return mEsitoMsg;
	}

	public BigDecimal getIdPrenotazione() {
		return mIdPrenotazione;
	}

	public BigDecimal getProgPeriPres() {
		return mProgPeriPres;
	}

	public String getTipoTrasmissione() {
		return mTipoTrasmissione;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setStatoPrenotazioniBdmc(BigDecimal aValore) {
		mStatoPrenotazioniBdmc = aValore;
	}

	public void setIdMisuraCautelareBdmc(BigDecimal aValore) {
		mIdMisuraCautelareBdmc = aValore;
	}

	public void setDataTrasmissione(Date aValore) {
		mDataTrasmissione = aValore;
	}

	public void setEsitoId(BigDecimal aValore) {
		mEsitoId = aValore;
	}

	public void setEsitoMsg(String aValore) {
		mEsitoMsg = aValore;
	}

	public void setIdPrenotazione(BigDecimal aValore) {
		mIdPrenotazione = aValore;
	}

	public void setProgPeriPres(BigDecimal aValore) {
		mProgPeriPres = aValore;
	}

	public void setTipoTrasmissione(String aValore) {
		mTipoTrasmissione = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "StatoPrenotazioniBdmcModel:\n" + "[ mStatoPrenotazioniBdmc = " + mStatoPrenotazioniBdmc
				+ " ]\n" + "[ mIdMisuraCautelareBdmc = " + mIdMisuraCautelareBdmc + " ]\n"
				+ "[ mDataTrasmissione      = " + mDataTrasmissione + " ]\n" + "[ mEsitoId               = "
				+ mEsitoId + " ]\n" + "[ mEsitoMsg              = " + mEsitoMsg + " ]\n"
				+ "[ mIdPrenotazione        = " + mIdPrenotazione + " ]\n" + "[ mProgPeriPres          = "
				+ mProgPeriPres + " ]\n" + "[ mTipoTrasmissione      = " + mTipoTrasmissione + " ]";
		return lStr;
	}

}