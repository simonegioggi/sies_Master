package siap.sico.magistratocompetente.model;

/**
* <p>Title: MagistratoCompetenteModel</p>
* <p>Description: Classe Model che rappresenta il MagistratoCompetente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class MagistratoCompetenteModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 3651930071438217473L;

	private Date mDataInizio;
	private Date mDataFine;
	private String mCodRuoloMagistrato;
	private String mDescrRuoloMagistrato;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mMagCodMagistrato;
	private BigDecimal mFasSieIdFascicoloSiep;

	// COSTRUTTORE DI DEFAULT
	public MagistratoCompetenteModel() {
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mCodRuoloMagistrato = "";
		this.mDescrRuoloMagistrato = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mMagCodMagistrato = "";
		this.mFasSieIdFascicoloSiep = null;
	}

	// COSTRUTTORE DI COPIA
	public MagistratoCompetenteModel(MagistratoCompetenteModel aModel) {
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mCodRuoloMagistrato = aModel.mCodRuoloMagistrato;
		this.mDescrRuoloMagistrato = aModel.mDescrRuoloMagistrato;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mMagCodMagistrato = aModel.mMagCodMagistrato;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
	}

	// COSTRUTTORE MODEL
	public MagistratoCompetenteModel(Date aDataInizio, Date aDataFine, String aCodRuoloMagistrato,
			String aDescrRuoloMagistrato, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			String aMagCodMagistrato, BigDecimal aFasSieIdFascicoloSiep) {
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mCodRuoloMagistrato = aCodRuoloMagistrato;
		this.mDescrRuoloMagistrato = aDescrRuoloMagistrato;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mMagCodMagistrato = aMagCodMagistrato;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
	}

	//
	// METODI GET()
	//

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public String getCodRuoloMagistrato() {
		return mCodRuoloMagistrato;
	}

	public String getDescrRuoloMagistrato() {
		return mDescrRuoloMagistrato;
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

	public String getMagCodMagistrato() {
		return mMagCodMagistrato;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	//
	// METODI SET()
	//

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setCodRuoloMagistrato(String aValore) {
		mCodRuoloMagistrato = aValore;
	}

	public void setDescrRuoloMagistrato(String aValore) {
		mDescrRuoloMagistrato = aValore;
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

	public void setMagCodMagistrato(String aValore) {
		mMagCodMagistrato = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mDataInizio + " - " + mDataFine + " - " + mCodRuoloMagistrato + " - "
				+ mDescrRuoloMagistrato + " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - "
				+ mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mMagCodMagistrato + " - "
				+ mFasSieIdFascicoloSiep;

		return lStr;
	}

}