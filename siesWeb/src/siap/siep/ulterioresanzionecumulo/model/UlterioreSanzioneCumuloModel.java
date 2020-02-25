package siap.siep.ulterioresanzionecumulo.model;

/**
* <p>Title: UlterioreSanzioneCumuloModel</p>
* <p>Description: Classe Model che rappresenta il UlterioreSanzioneCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class UlterioreSanzioneCumuloModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -3256060395007932718L;

	private BigDecimal mIdUlterioreSanzioneCumulo;
	private String mCodTipoUlterioreSanzione;
	private String mDescrTipoUlterioreSanzione;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mSanzione;
	private Date mDataInserimento;
	private Date mDataAggiornamento;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mCumIdCumulo;

	// COSTRUTTORE DI DEFAULT
	public UlterioreSanzioneCumuloModel() {
		this.mIdUlterioreSanzioneCumulo = null;
		this.mCodTipoUlterioreSanzione = "";
		this.mDescrTipoUlterioreSanzione = "";
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mSanzione = null;
		this.mDataInserimento = null;
		this.mDataAggiornamento = null;
		this.mCodOperatoreInserimento = "";
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mCumIdCumulo = null;
	}

	// COSTRUTTORE DI COPIA
	public UlterioreSanzioneCumuloModel(UlterioreSanzioneCumuloModel aModel) {
		this.mIdUlterioreSanzioneCumulo = aModel.mIdUlterioreSanzioneCumulo;
		this.mCodTipoUlterioreSanzione = aModel.mCodTipoUlterioreSanzione;
		this.mDescrTipoUlterioreSanzione = aModel.mDescrTipoUlterioreSanzione;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mSanzione = aModel.mSanzione;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mCumIdCumulo = aModel.mCumIdCumulo;
	}

	// COSTRUTTORE MODEL
	public UlterioreSanzioneCumuloModel(BigDecimal aIdUlterioreSanzioneCumulo,
			String aCodTipoUlterioreSanzione, String aDescrTipoUlterioreSanzione, BigDecimal aNumAnni,
			BigDecimal aNumMesi, BigDecimal aNumGiorni, BigDecimal aSanzione, Date aDataInserimento,
			Date aDataAggiornamento, String aCodOperatoreInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aCumIdCumulo) {
		this.mIdUlterioreSanzioneCumulo = aIdUlterioreSanzioneCumulo;
		this.mCodTipoUlterioreSanzione = aCodTipoUlterioreSanzione;
		this.mDescrTipoUlterioreSanzione = aDescrTipoUlterioreSanzione;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mSanzione = aSanzione;
		this.mDataInserimento = aDataInserimento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mCumIdCumulo = aCumIdCumulo;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdUlterioreSanzioneCumulo() {
		return mIdUlterioreSanzioneCumulo;
	}

	public String getCodTipoUlterioreSanzione() {
		return mCodTipoUlterioreSanzione;
	}

	public String getDescrTipoUlterioreSanzione() {
		return mDescrTipoUlterioreSanzione;
	}

	public BigDecimal getNumAnni() {
		return mNumAnni;
	}

	public BigDecimal getNumMesi() {
		return mNumMesi;
	}

	public BigDecimal getNumGiorni() {
		return mNumGiorni;
	}

	public BigDecimal getSanzione() {
		return mSanzione;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
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

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getCumIdCumulo() {
		return mCumIdCumulo;
	}

	//
	// METODI SET()
	//

	public void setIdUlterioreSanzioneCumulo(BigDecimal aValore) {
		mIdUlterioreSanzioneCumulo = aValore;
	}

	public void setCodTipoUlterioreSanzione(String aValore) {
		mCodTipoUlterioreSanzione = aValore;
	}

	public void setDescrTipoUlterioreSanzione(String aValore) {
		mDescrTipoUlterioreSanzione = aValore;
	}

	public void setNumAnni(BigDecimal aValore) {
		mNumAnni = aValore;
	}

	public void setNumMesi(BigDecimal aValore) {
		mNumMesi = aValore;
	}

	public void setNumGiorni(BigDecimal aValore) {
		mNumGiorni = aValore;
	}

	public void setSanzione(BigDecimal aValore) {
		mSanzione = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
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

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setCumIdCumulo(BigDecimal aValore) {
		mCumIdCumulo = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdUlterioreSanzioneCumulo + " - " + mCodTipoUlterioreSanzione + " - "
				+ mDescrTipoUlterioreSanzione + " - " + mNumAnni + " - " + mNumMesi + " - " + mNumGiorni
				+ " - " + mSanzione + " - " + mDataInserimento + " - " + mDataAggiornamento + " - "
				+ mCodOperatoreInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mCodUfficioAggiornamento + " - "
				+ mDescrUfficioAggiornamento + " - " + mFasSieIdFascicoloSiep + " - " + mCumIdCumulo;

		return lStr;
	}

}
