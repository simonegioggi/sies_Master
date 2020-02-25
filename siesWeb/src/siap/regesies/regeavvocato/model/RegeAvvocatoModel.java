package siap.regesies.regeavvocato.model;

/**
* <p>Title: RegeAvvocatoModel</p>
* <p>Description: Classe Model che rappresenta il RegeAvvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;

public class RegeAvvocatoModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -8674856074805457384L;

	private String mIdFile;
	private int mProgrAvvocato;
	private String mCognome;
	private String mNome;
	private String mForo;
	private String mCodTipoAvvocato;
	private String mDescrTipoAvvocato;
	private Date mDataInizioValidita;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public RegeAvvocatoModel() {
		this.mIdFile = "";
		this.mProgrAvvocato = 0;
		this.mCognome = "";
		this.mNome = "";
		this.mForo = "";
		this.mCodTipoAvvocato = "";
		this.mDescrTipoAvvocato = "";
		this.mDataInizioValidita = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
	}

	// COSTRUTTORE DI COPIA
	public RegeAvvocatoModel(RegeAvvocatoModel aModel) {
		this.mIdFile = aModel.mIdFile;
		this.mProgrAvvocato = aModel.mProgrAvvocato;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mForo = aModel.mForo;
		this.mCodTipoAvvocato = aModel.mCodTipoAvvocato;
		this.mDescrTipoAvvocato = aModel.mDescrTipoAvvocato;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
	}

	// COSTRUTTORE MODEL
	public RegeAvvocatoModel(String aIdFile, int aProgrAvvocato, String aCognome, String aNome, String aForo,
			String aCodTipoAvvocato, String aDescrTipoAvvocato, Date aDataInizioValidita,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento) {
		this.mIdFile = aIdFile;
		this.mProgrAvvocato = aProgrAvvocato;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mForo = aForo;
		this.mCodTipoAvvocato = aCodTipoAvvocato;
		this.mDescrTipoAvvocato = aDescrTipoAvvocato;
		this.mDataInizioValidita = aDataInizioValidita;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	//
	// METODI GET()
	//

	public String getIdFile() {
		return mIdFile;
	}

	public int getProgrAvvocato() {
		return mProgrAvvocato;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getForo() {
		return mForo;
	}

	public String getCodTipoAvvocato() {
		return mCodTipoAvvocato;
	}

	public String getDescrTipoAvvocato() {
		return mDescrTipoAvvocato;
	}

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
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

	//
	// METODI SET()
	//

	public void setIdFile(String aValore) {
		mIdFile = aValore;
	}

	public void setProgrAvvocato(int aValore) {
		mProgrAvvocato = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setForo(String aValore) {
		mForo = aValore;
	}

	public void setCodTipoAvvocato(String aValore) {
		mCodTipoAvvocato = aValore;
	}

	public void setDescrTipoAvvocato(String aValore) {
		mDescrTipoAvvocato = aValore;
	}

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
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

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdFile + " - " + mProgrAvvocato + " - " + mCognome + " - " + mNome + " - " + mForo
				+ " - " + mCodTipoAvvocato + " - " + mDescrTipoAvvocato + " - " + mDataInizioValidita + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento;

		return lStr;
	}

}