package siap.siep.statoprocedimento.model;

/**
* <p>Title: StatoProcedimentoModel</p>
* <p>Description: Classe Model che rappresenta il StatoProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class StatoProcedimentoModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 3603214062070716731L;

	private BigDecimal mProgressivo;
	private String mCodStatoProcedimento;
	private String mDescrStatoProcedimento;
	private Date mData;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mEveIdEvento;

	// COSTRUTTORE DI DEFAULT
	public StatoProcedimentoModel() {
		this.mProgressivo = null;
		this.mCodStatoProcedimento = "";
		this.mDescrStatoProcedimento = "";
		this.mData = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mEveIdEvento = null;
	}

	// COSTRUTTORE DI COPIA
	public StatoProcedimentoModel(StatoProcedimentoModel aModel) {
		this.mProgressivo = aModel.mProgressivo;
		this.mCodStatoProcedimento = aModel.mCodStatoProcedimento;
		this.mDescrStatoProcedimento = aModel.mDescrStatoProcedimento;
		this.mData = aModel.mData;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mEveIdEvento = aModel.mEveIdEvento;
	}

	// COSTRUTTORE MODEL
	public StatoProcedimentoModel(BigDecimal aProgressivo, String aCodStatoProcedimento,
			String aDescrStatoProcedimento, Date aData, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aEveIdEvento) {
		this.mProgressivo = aProgressivo;
		this.mCodStatoProcedimento = aCodStatoProcedimento;
		this.mDescrStatoProcedimento = aDescrStatoProcedimento;
		this.mData = aData;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mEveIdEvento = aEveIdEvento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getProgressivo() {
		return mProgressivo;
	}

	public String getCodStatoProcedimento() {
		return mCodStatoProcedimento;
	}

	public String getDescrStatoProcedimento() {
		return mDescrStatoProcedimento;
	}

	public Date getData() {
		return mData;
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

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	//
	// METODI SET()
	//

	public void setProgressivo(BigDecimal aValore) {
		mProgressivo = aValore;
	}

	public void setCodStatoProcedimento(String aValore) {
		mCodStatoProcedimento = aValore;
	}

	public void setDescrStatoProcedimento(String aValore) {
		mDescrStatoProcedimento = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
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

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mProgressivo + " - " + mCodStatoProcedimento + " - " + mDescrStatoProcedimento + " - "
				+ mData + " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - "
				+ mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - " + mFasSieIdFascicoloSiep
				+ " - " + mEveIdEvento;

		return lStr;
	}

	/*
	 * Codici per RICHIESTA GENERICA Viene chiamato nel dettaglio procedimento, a seconda se il metodo ritorna
	 * true o false nello Stato Procedimento vengono inseriti i destinatari della trasmissione.
	 */
	public boolean isCodiceTrasmissione() {
		if (mCodStatoProcedimento != null && (mCodStatoProcedimento.equals("0141")
				|| mCodStatoProcedimento.equals("0142") || mCodStatoProcedimento.equals("0143")
				|| mCodStatoProcedimento.equals("0144") || mCodStatoProcedimento.equals("0145")
				|| mCodStatoProcedimento.equals("0146") || mCodStatoProcedimento.equals("0147")
				|| mCodStatoProcedimento.equals("0148") || mCodStatoProcedimento.equals("0149")
				|| mCodStatoProcedimento.equals("0150") || mCodStatoProcedimento.equals("0151")
				|| mCodStatoProcedimento.equals("0152") || mCodStatoProcedimento.equals("0153")
				|| mCodStatoProcedimento.equals("0154") || mCodStatoProcedimento.equals("0253"))) {
			return true;
		}
		return false;
	}

}