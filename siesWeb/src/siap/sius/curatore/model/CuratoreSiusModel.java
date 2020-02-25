package siap.sius.curatore.model;

/**
* <p>Title: CuratoreSiusModel</p>
* <p>Description: Classe Model che rappresenta il MagistratoRelatore</p>
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sige.curatore.model.CuratoreModel;
import f3b.model.GenericModel;

public class CuratoreSiusModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 5460805666299440587L;

	private Date mDataInizio;
	private Date mDataFine;
	private String mFlagTipo;
	private String mDescrTipo;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mCurIdCuratore;
	private BigDecimal mFasSiuIdFascicoloSius;
	private CuratoreModel mCuratore;

	// COSTRUTTORE DI DEFAULT
	public CuratoreSiusModel() {
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mFlagTipo = "";
		this.mDescrTipo = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mCurIdCuratore = null;
		this.mFasSiuIdFascicoloSius = null;
		this.mCuratore = null;
	}

	// COSTRUTTORE DI COPIA
	public CuratoreSiusModel(CuratoreSiusModel aModel) {
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mFlagTipo = aModel.mFlagTipo;
		this.mDescrTipo = aModel.mDescrTipo;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mCurIdCuratore = aModel.mCurIdCuratore;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mCuratore = aModel.mCuratore;
	}

	// COSTRUTTORE MODEL
	public CuratoreSiusModel(Date aDataInizio, Date aDataFine, String aFlagTipo, String aDescrTipo,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aCurIdCuratore,
			BigDecimal aFasSiuIdFascicoloSius) {
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mFlagTipo = aFlagTipo;
		this.mDescrTipo = aDescrTipo;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mCurIdCuratore = aCurIdCuratore;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mCuratore = null;
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

	public String getFlagTipo() {
		return mFlagTipo;
	}

	public String getDescrTipo() {
		return mDescrTipo;
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

	public BigDecimal getCurIdCuratore() {
		return mCurIdCuratore;
	}

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public CuratoreModel getCuratore() {
		return mCuratore;
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

	public void setFlagTipo(String aValore) {
		mFlagTipo = aValore;
	}

	public void setDescrTipo(String aValore) {
		mDescrTipo = aValore;
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

	public void setCurIdCuratore(BigDecimal aValore) {
		mCurIdCuratore = aValore;
	}

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setCuratore(CuratoreModel aValore) {
		mCuratore = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mDataInizio + " - " + mDataFine + " - " + mFlagTipo + " - " + mDescrTipo + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mCurIdCuratore + " - " + mFasSiuIdFascicoloSius + " - ";
		if (this.mCuratore != null) {
			lStr += " CURATORE ";
			lStr += mCuratore.toString();
		}

		return lStr;
	}

}