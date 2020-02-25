package siap.sius.udienzaprocedimento.model;

/**
* <p>Title: UdienzaProcedimentoModel</p>
* <p>Description: Classe Model che rappresenta il UdienzaProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class UdienzaProcedimentoModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 2171227998602718669L;

	private BigDecimal mIdUdienzaProcedimento;
	private String mFlagRinviata;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mGenPridGeneraleProcedimento;
	private BigDecimal mUdiIdUdienza;
	private BigDecimal mUdiIdUdienzaRinvio;
	private BigDecimal mEveIdEvento;

	// COSTRUTTORE DI DEFAULT
	public UdienzaProcedimentoModel() {
		this.mIdUdienzaProcedimento = null;
		this.mFlagRinviata = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mGenPridGeneraleProcedimento = null;
		this.mUdiIdUdienza = null;
		this.mUdiIdUdienzaRinvio = null;
		this.mEveIdEvento = null;
	}

	// COSTRUTTORE DI COPIA
	public UdienzaProcedimentoModel(UdienzaProcedimentoModel aModel) {
		this.mIdUdienzaProcedimento = aModel.mIdUdienzaProcedimento;
		this.mFlagRinviata = aModel.mFlagRinviata;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mGenPridGeneraleProcedimento = aModel.mGenPridGeneraleProcedimento;
		this.mUdiIdUdienza = aModel.mUdiIdUdienza;
		this.mUdiIdUdienzaRinvio = aModel.mUdiIdUdienzaRinvio;
		this.mEveIdEvento = aModel.mEveIdEvento;
	}

	// COSTRUTTORE MODEL
	public UdienzaProcedimentoModel(BigDecimal aIdUdienzaProcedimento, String aFlagRinviata,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aGenPridGeneraleProcedimento, BigDecimal aUdiIdUdienza, BigDecimal aUdiIdUdienzaRinvio,
			BigDecimal aEveIdEvento) {
		this.mIdUdienzaProcedimento = aIdUdienzaProcedimento;
		this.mFlagRinviata = aFlagRinviata;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mGenPridGeneraleProcedimento = aGenPridGeneraleProcedimento;
		this.mUdiIdUdienza = aUdiIdUdienza;
		this.mUdiIdUdienzaRinvio = aUdiIdUdienzaRinvio;
		this.mEveIdEvento = aEveIdEvento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdUdienzaProcedimento() {
		return mIdUdienzaProcedimento;
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

	public BigDecimal getGenPridGeneraleProcedimento() {
		return mGenPridGeneraleProcedimento;
	}

	public BigDecimal getUdiIdUdienza() {
		return mUdiIdUdienza;
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

	public void setIdUdienzaProcedimento(BigDecimal aValore) {
		mIdUdienzaProcedimento = aValore;
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

	public void setGenPridGeneraleProcedimento(BigDecimal aValore) {
		mGenPridGeneraleProcedimento = aValore;
	}

	public void setUdiIdUdienza(BigDecimal aValore) {
		mUdiIdUdienza = aValore;
	}

	public void setUdiIdUdienzaRinvio(BigDecimal aValore) {
		mUdiIdUdienzaRinvio = aValore;
	}

	public void seEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdUdienzaProcedimento + " - " + mFlagRinviata + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mGenPridGeneraleProcedimento + " - "
				+ mUdiIdUdienza + " - " + mUdiIdUdienzaRinvio + " - " + mEveIdEvento;
		return lStr;
	}

}