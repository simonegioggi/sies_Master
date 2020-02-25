package siap.sico.magistrato.model;

/**
* <p>Title: MagistratoModel</p>
* <p>Description: Classe Model che rappresenta il Magistrato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;

public class MagistratoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3283171442232961966L;

	private String mCodMagistrato;
	private String mDescrMagistrato;
	private String mCognome;
	private String mNome;
	private String mFlagStato;
	private String mCodUfficioAppartenenza;
	private String mDescrUfficioAppartenenza;
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mEMailUfficio;
	private String mEMailPrivata;
	private String mNumCellulare;
	private Date mDataNascita;

	// COSTRUTTORE DI DEFAULT
	public MagistratoModel() {
		this.mCodMagistrato = "";
		this.mDescrMagistrato = "";
		this.mCognome = "";
		this.mNome = "";
		this.mFlagStato = "";
		this.mCodUfficioAppartenenza = "";
		this.mDescrUfficioAppartenenza = "";
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mEMailUfficio = "";
		this.mEMailPrivata = "";
		this.mNumCellulare = "";
		this.mDataNascita = null;

	}

	// COSTRUTTORE DI COPIA
	public MagistratoModel(MagistratoModel aModel) {
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mDescrMagistrato = aModel.mDescrMagistrato;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mFlagStato = aModel.mFlagStato;
		this.mCodUfficioAppartenenza = aModel.mCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aModel.mDescrUfficioAppartenenza;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mEMailUfficio = aModel.mEMailUfficio;
		this.mEMailPrivata = aModel.mEMailPrivata;
		this.mNumCellulare = aModel.mNumCellulare;
		this.mDataNascita = aModel.mDataNascita;

	}

	// COSTRUTTORE MODEL
	public MagistratoModel(String aCodMagistrato, String aDescrMagistrato, String aCognome, String aNome,
			String aFlagStato, String aCodUfficioAppartenenza, String aDescrUfficioAppartenenza,
			Date aDataInizioValidita, Date aDataFineValidita, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, String aEMailUfficio, String aEMailPrivata,
			String aNumCellulare, Date aDataNascita) {
		this.mCodMagistrato = aCodMagistrato;
		this.mDescrMagistrato = aDescrMagistrato;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mFlagStato = aFlagStato;
		this.mCodUfficioAppartenenza = aCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aDescrUfficioAppartenenza;
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mEMailUfficio = aEMailUfficio;
		this.mEMailPrivata = aEMailPrivata;
		this.mNumCellulare = aNumCellulare;
		this.mDataNascita = aDataNascita;
	}

	//
	// METODI GET()
	//

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getDescrMagistrato() {
		return mDescrMagistrato;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getCodUfficioAppartenenza() {
		return mCodUfficioAppartenenza;
	}

	public String getDescrUfficioAppartenenza() {
		return mDescrUfficioAppartenenza;
	}

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
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

	public String getEMailUfficio() {
		return mEMailUfficio;
	}

	public String getEMailPrivata() {
		return mEMailPrivata;
	}

	public String getNumCellulare() {
		return mNumCellulare;
	}

	public Date getDataNascita() {
		return mDataNascita;
	}

	//
	// METODI SET()
	//

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setDescrMagistrato(String aValore) {
		mDescrMagistrato = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setCodUfficioAppartenenza(String aValore) {
		mCodUfficioAppartenenza = aValore;
	}

	public void setDescrUfficioAppartenenza(String aValore) {
		mDescrUfficioAppartenenza = aValore;
	}

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
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

	public void setEMailUfficio(String aValore) {
		mEMailUfficio = aValore;
	}

	public void setEMailPrivata(String aValore) {
		mEMailPrivata = aValore;
	}

	public void setNumCellulare(String aValore) {
		mNumCellulare = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mCodMagistrato + " - " + mDescrMagistrato + " - " + mCognome + " - " + mNome + " - "
				+ mFlagStato + " - " + mCodUfficioAppartenenza + " - " + mDescrUfficioAppartenenza + " - "
				+ mDataInizioValidita + " - " + mDataFineValidita + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mEMailUfficio + " - " + mEMailPrivata + " - " + mNumCellulare + " - "
				+ mDataNascita;

		return lStr;
	}

}