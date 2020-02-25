package siap.sico.libertaanticipata.model;

/**
* <p>Title: PeriodoLibAnticipataModel</p>
* <p>Description: Classe Model che rappresenta il PeriodoLibanticipata</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class PeriodoLibAnticipataModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7961156412507311298L;

	private BigDecimal mIdPeriodoLibanticipata;
	private Date mDataInizio;
	private Date mDataFine;
	private String mFlagConcesso;
	private Date mDataInserimento;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mLicIdLicenzaLibanticipata;

	// COSTRUTTORE DI DEFAULT
	public PeriodoLibAnticipataModel() {
		this.mIdPeriodoLibanticipata = null;
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mFlagConcesso = "";
		this.mDataInserimento = null;
		this.mCodOperatoreInserimento = "";
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mLicIdLicenzaLibanticipata = null;
	}

	// COSTRUTTORE DI COPIA
	public PeriodoLibAnticipataModel(PeriodoLibAnticipataModel aModel) {
		this.mIdPeriodoLibanticipata = aModel.mIdPeriodoLibanticipata;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mFlagConcesso = aModel.mFlagConcesso;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mLicIdLicenzaLibanticipata = aModel.mLicIdLicenzaLibanticipata;
	}

	// COSTRUTTORE MODEL
	public PeriodoLibAnticipataModel(BigDecimal aIdPeriodoLibanticipata, Date aDataInizio, Date aDataFine,
			String aFlagConcesso, Date aDataInserimento, String aCodOperatoreInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aLicIdLicenzaLibanticipata) {
		this.mIdPeriodoLibanticipata = aIdPeriodoLibanticipata;
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mFlagConcesso = aFlagConcesso;
		this.mDataInserimento = aDataInserimento;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mLicIdLicenzaLibanticipata = aLicIdLicenzaLibanticipata;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdPeriodoLibanticipata() {
		return mIdPeriodoLibanticipata;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public String getFlagConcesso() {
		return mFlagConcesso;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
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

	public BigDecimal getLicIdLicenzaLibanticipata() {
		return mLicIdLicenzaLibanticipata;
	}

	//
	// METODI SET()
	//

	public void setIdPeriodoLibanticipata(BigDecimal aValore) {
		mIdPeriodoLibanticipata = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setFlagConcesso(String aValore) {
		mFlagConcesso = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
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

	public void setLicIdLicenzaLibanticipata(BigDecimal aValore) {
		mLicIdLicenzaLibanticipata = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdPeriodoLibanticipata + " - " + mDataInizio + " - " + mDataFine + " - " + mFlagConcesso
				+ " - " + mDataInserimento + " - " + mCodOperatoreInserimento + " - " + mCodUfficioInserimento
				+ " - " + mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - "
				+ mDataAggiornamento + " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento
				+ " - " + mLicIdLicenzaLibanticipata;

		return lStr;
	}

	public String toString2() {
		String lStr = new String();

		lStr = "PeriodoLibAnticipataModel:\n" + "[ mIdPeriodoLibanticipata    = " + mIdPeriodoLibanticipata
				+ " ]\n" + "[ mDataInizio                = "
				+ DateUtils.getDateToString(mDataInizio, "dd/MM/yyyy") + " ]\n"
				+ "[ mDataFine                  = " + DateUtils.getDateToString(mDataFine, "dd/MM/yyyy")
				+ " ]\n" + "[ mFlagConcesso              = " + mFlagConcesso + " ]\n"
				+ "[ mLicIdLicenzaLibanticipata = " + mLicIdLicenzaLibanticipata + " ]";

		return lStr;
	}

}