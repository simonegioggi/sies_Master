package siap.sige.udienzaprocedimento.model;

/**
* <p>Title: UdienzaProcedimentoSigeModel</p>
* <p>Description: Classe Model che rappresenta UdienzaProcedimentoSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sige.udienza.model.UdienzaSigeModel;

public class UdienzaProcedimentoSigeModel extends GenericModel {

	private static final long serialVersionUID = 5489345635220220218L;
	private BigDecimal mIdUdienzaProcedimentoSige;
	private String mFlagRinviata;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mCodUfficioAggiornamento;
	private BigDecimal mFasIdFascicoloSige;
	private BigDecimal mUdiIdUdienzaSige;
	private Date mDataUdienzaSige;
	private BigDecimal mUdiIdUdienzaRinvio;
	private BigDecimal mEveIdEvento;
	private UdienzaSigeModel udienzaSige;
	// MEV_65: aggiunta proprieta'
	private String mListaDateUdienzaSige;

	// COSTRUTTORE DI DEFAULT
	public UdienzaProcedimentoSigeModel() {
		mIdUdienzaProcedimentoSige = null;
		mFlagRinviata = "";
		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodUfficioInserimento = "";
		mDescrUfficioInserimento = "";
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
		mCodUfficioAggiornamento = "";
		mDescrUfficioAggiornamento = "";
		mFasIdFascicoloSige = null;
		mUdiIdUdienzaSige = null;
		mDataUdienzaSige = null;
		mUdiIdUdienzaRinvio = null;
		mEveIdEvento = null;
		mListaDateUdienzaSige = null;
	}

	// COSTRUTTORE DI COPIA
	public UdienzaProcedimentoSigeModel(UdienzaProcedimentoSigeModel aModel) {
		mIdUdienzaProcedimentoSige = aModel.mIdUdienzaProcedimentoSige;
		mFlagRinviata = aModel.mFlagRinviata;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		mFasIdFascicoloSige = aModel.mFasIdFascicoloSige;
		mUdiIdUdienzaSige = aModel.mUdiIdUdienzaSige;
		mDataUdienzaSige = aModel.mDataUdienzaSige;
		mUdiIdUdienzaRinvio = aModel.mUdiIdUdienzaRinvio;
		mEveIdEvento = aModel.mEveIdEvento;
		mListaDateUdienzaSige = aModel.mListaDateUdienzaSige;
	}

	// COSTRUTTORE MODEL
	public UdienzaProcedimentoSigeModel(BigDecimal aIdUdienzaProcedimentoSige, String aFlagRinviata,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasIdFascicoloSige, BigDecimal aUdiIdUdienzaSige, Date aDataUdienzaSige,
			BigDecimal aUdiIdUdienzaRinvio, BigDecimal aEveIdEvento) {
		mIdUdienzaProcedimentoSige = aIdUdienzaProcedimentoSige;
		mFlagRinviata = aFlagRinviata;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mFasIdFascicoloSige = aFasIdFascicoloSige;
		mUdiIdUdienzaSige = aUdiIdUdienzaSige;
		mDataUdienzaSige = aDataUdienzaSige;
		mUdiIdUdienzaRinvio = aUdiIdUdienzaRinvio;
		mEveIdEvento = aEveIdEvento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdUdienzaProcedimentoSige() {
		return mIdUdienzaProcedimentoSige;
	}

	public String getFlagRinviata() {
		return mFlagRinviata;
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

	public BigDecimal getFasIdFascicoloSige() {
		return mFasIdFascicoloSige;
	}

	public BigDecimal getUdiIdUdienzaSige() {
		return mUdiIdUdienzaSige;
	}

	public Date getDataUdienzaSige() {
		return mDataUdienzaSige;
	}

	public BigDecimal getUdiIdUdienzaRinvio() {
		return mUdiIdUdienzaRinvio;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	//
	// METODI SET()
	//
	public void setIdUdienzaProcedimentoSige(BigDecimal aValore) {
		mIdUdienzaProcedimentoSige = aValore;
	}

	public void setFlagRinviata(String aValore) {
		mFlagRinviata = aValore;
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

	public void setFasIdFascicoloSige(BigDecimal aValore) {
		mFasIdFascicoloSige = aValore;
	}

	public void setUdiIdUdienzaSige(BigDecimal aValore) {
		mUdiIdUdienzaSige = aValore;
	}

	public void setDataUdienzaSige(Date aValore) {
		mDataUdienzaSige = aValore;
	}

	public void setUdiIdUdienzaRinvio(BigDecimal aValore) {
		mUdiIdUdienzaRinvio = aValore;
	}

	public void seEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdUdienzaProcedimentoSige + " - " + mFlagRinviata + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - " + mFasIdFascicoloSige
				+ " - " + mUdiIdUdienzaSige + " - " + mDataUdienzaSige + " - " + mUdiIdUdienzaRinvio + " - "
				+ mEveIdEvento + " - " + mListaDateUdienzaSige;
		return lStr;
	}

	public UdienzaSigeModel getUdienzaSige() {
		return udienzaSige;
	}

	public void setUdienzaSige(UdienzaSigeModel aValore) {
		udienzaSige = aValore;
	}

	public String getListaDateUdienzaSige() {
		return mListaDateUdienzaSige;
	}

	public void setListaDateUdienzaSige(String aValore) {
		mListaDateUdienzaSige = aValore;
	}

}