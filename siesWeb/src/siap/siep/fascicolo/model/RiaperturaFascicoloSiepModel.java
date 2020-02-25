package siap.siep.fascicolo.model;

/**
* <p>Title: RiaperturaFascicoloSiepModel</p>
* <p>Description: Classe Model che rappresenta la riapertura del FascicoloSiep</p>
* <p>Copyright: Copyright (c) 2015</p>
* <p>Company: Intersistemi S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class RiaperturaFascicoloSiepModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 7414233593840613995L;
	private BigDecimal mIdRiaperturaFascicoloSiep;
	private String mCodMotivo;
	private String mDescrMotivo;
	private Date mDataRiapertura;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;

	// COSTRUTTORE DI DEFAULT
	public RiaperturaFascicoloSiepModel() {
		this.mIdRiaperturaFascicoloSiep = null;
		this.mCodMotivo = "";
		this.mDescrMotivo = "";
		this.mDataRiapertura = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;
	}

	// COSTRUTTORE DI COPIA
	public RiaperturaFascicoloSiepModel(RiaperturaFascicoloSiepModel aModel) {
		this.mIdRiaperturaFascicoloSiep = aModel.mIdRiaperturaFascicoloSiep;
		this.mCodMotivo = aModel.mCodMotivo;
		this.mDescrMotivo = aModel.mDescrMotivo;
		this.mDataRiapertura = aModel.mDataRiapertura;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
	}

	// COSTRUTTORE MODEL
	public RiaperturaFascicoloSiepModel(BigDecimal aIdRiaperturaFascicoloSiep, String aCodMotivo,
			String aDescrMotivo, Date aDataRiapertura, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, BigDecimal aFasSieIdFascicoloSiep) {
		this.mIdRiaperturaFascicoloSiep = aIdRiaperturaFascicoloSiep;
		this.mCodMotivo = aCodMotivo;
		this.mDescrMotivo = aDescrMotivo;
		this.mDataRiapertura = aDataRiapertura;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdRiaperturaFascicoloSiep() {
		return mIdRiaperturaFascicoloSiep;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

	public String getDescrMotivo() {
		return mDescrMotivo;
	}

	public Date getDataRiapertura() {
		return mDataRiapertura;
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

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	//
	// METODI SET()
	//
	public void setIdRiaperturaFascicoloSiep(BigDecimal aValore) {
		mIdRiaperturaFascicoloSiep = aValore;
	}

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

	public void setDescrMotivo(String aValore) {
		mDescrMotivo = aValore;
	}

	public void setDataRiapertura(Date aValore) {
		mDataRiapertura = aValore;
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

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	@Override
	public String toString() {
		String lToString = "[ " + this.mCodMotivo + " - " + this.mDescrMotivo + " - " + this.mDataRiapertura
				+ "]";
		return lToString;
	}
}