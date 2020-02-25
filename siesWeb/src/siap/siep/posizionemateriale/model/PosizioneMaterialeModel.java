package siap.siep.posizionemateriale.model;

/**
* <p>Title: PosizioneMaterialeModel</p>
* <p>Description: Classe Model che rappresenta il PosizioneMateriale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;

public class PosizioneMaterialeModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 5221828136410674174L;
	private String mCodPosizioneMateriale;
	private String mCodUfficio;
	private String mDescrUfficio;
	private String mDescPosizioneMateriale;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private Date mDataFineValidita;

	private String mFiltroDataValidita;

	// COSTRUTTORE DI DEFAULT
	public PosizioneMaterialeModel() {
		this.mCodPosizioneMateriale = "";
		this.mCodUfficio = "";
		this.mDescrUfficio = "";
		this.mDescPosizioneMateriale = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mDataFineValidita = null;

		this.mFiltroDataValidita = null;
	}

	// COSTRUTTORE DI COPIA
	public PosizioneMaterialeModel(PosizioneMaterialeModel aModel) {
		this.mCodPosizioneMateriale = aModel.mCodPosizioneMateriale;
		this.mCodUfficio = aModel.mCodUfficio;
		this.mDescrUfficio = aModel.mDescrUfficio;
		this.mDescPosizioneMateriale = aModel.mDescPosizioneMateriale;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mDataFineValidita = aModel.mDataFineValidita;

		this.mFiltroDataValidita = aModel.mFiltroDataValidita;
	}

	// COSTRUTTORE MODEL
	public PosizioneMaterialeModel(String aCodPosizioneMateriale, String aCodUfficio, String aDescrUfficio,
			String aDescPosizioneMateriale, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento) {
		this.mCodPosizioneMateriale = aCodPosizioneMateriale;
		this.mCodUfficio = aCodUfficio;
		this.mDescrUfficio = aDescrUfficio;
		this.mDescPosizioneMateriale = aDescPosizioneMateriale;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mDataFineValidita = null;

		this.mFiltroDataValidita = null;
	}

	//
	// METODI GET()
	//

	public String getCodPosizioneMateriale() {
		return mCodPosizioneMateriale;
	}

	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getDescrUfficio() {
		return mDescrUfficio;
	}

	public String getDescPosizioneMateriale() {
		return mDescPosizioneMateriale;
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

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public String getFiltroDataValidita() {
		return mFiltroDataValidita;
	}

	//
	// METODI SET()
	//

	public void setCodPosizioneMateriale(String aValore) {
		mCodPosizioneMateriale = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setDescrUfficio(String aValore) {
		mDescrUfficio = aValore;
	}

	public void setDescPosizioneMateriale(String aValore) {
		mDescPosizioneMateriale = aValore;
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

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setFiltroDataValidita(String aValore) {
		mFiltroDataValidita = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mCodPosizioneMateriale + " - " + mCodUfficio + " - " + mDescrUfficio + " - "
				+ mDescPosizioneMateriale + " - " + mCodOperatoreInserimento + " - " + mDataInserimento
				+ " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDataFineValidita + " - " + mFiltroDataValidita;

		return lStr;
	}
}
