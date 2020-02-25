package siap.sico.w_magistrato.model;

/**
* <p>Title: WMagistratoModel</p>
* <p>Description: Classe Model che rappresenta il WMagistrato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;

public class WMagistratoModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 1992428672488645383L;

	private String mCodMagistrato;
	private String mDescrMagistrato;
	private String mCognome;
	private String mNome;
	private Date mDataNascita;
	private String mDescLuogoNascita;
	private Date mDataCaricamento;

	// COSTRUTTORE DI DEFAULT
	public WMagistratoModel() {
		this.mCodMagistrato = "";
		this.mDescrMagistrato = "";
		this.mCognome = "";
		this.mNome = "";
		this.mDataNascita = null;
		this.mDescLuogoNascita = "";
		this.mDataCaricamento = null;
	}

	// COSTRUTTORE DI COPIA
	public WMagistratoModel(WMagistratoModel aModel) {
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mDescrMagistrato = aModel.mDescrMagistrato;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mDataNascita = aModel.mDataNascita;
		this.mDescLuogoNascita = aModel.mDescLuogoNascita;
		this.mDataCaricamento = aModel.mDataCaricamento;
	}

	// COSTRUTTORE MODEL
	public WMagistratoModel(String aCodMagistrato, String aDescrMagistrato, String aCognome, String aNome,
			Date aDataNascita, String aDescLuogoNascita, Date aDataCaricamento) {
		this.mCodMagistrato = aCodMagistrato;
		this.mDescrMagistrato = aDescrMagistrato;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mDataNascita = aDataNascita;
		this.mDescLuogoNascita = aDescLuogoNascita;
		this.mDataCaricamento = aDataCaricamento;
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

	public Date getDataNascita() {
		return mDataNascita;
	}

	public String getDescLuogoNascita() {
		return mDescLuogoNascita;
	}

	public Date getDataCaricamento() {
		return mDataCaricamento;
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

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public void setDescLuogoNascita(String aValore) {
		mDescLuogoNascita = aValore;
	}

	public void setDataCaricamento(Date aValore) {
		mDataCaricamento = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mCodMagistrato + " - " + mDescrMagistrato + " - " + mCognome + " - " + mNome + " - "
				+ mDataNascita + " - " + mDescLuogoNascita + " - " + mDataCaricamento;

		return lStr;
	}

}