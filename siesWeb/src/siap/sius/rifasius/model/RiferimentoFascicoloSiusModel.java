package siap.sius.rifasius.model;

/**
* <p>Title: RiferimentoFascicoloSiusModel</p>
* <p>Description: Classe Model che rappresenta il RiferimentoFascicoloSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class RiferimentoFascicoloSiusModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 2035857774269112881L;
	private BigDecimal mIdRiferimentoFascicoloSius;
	private BigDecimal mAnnoFascicoloSius;
	private BigDecimal mProgrFascicoloSius;
	private String mCodUffFascicoloSius;
	private String mDescrUffFascicoloSius;
	private Date mDataRicezione;
	private String mCodOggettoProcedimento;
	private String mDescrOggettoProcedimento;
	private BigDecimal mFasSieIdFascicoloSiep;

	// COSTRUTTORE DI DEFAULT
	public RiferimentoFascicoloSiusModel() {
		this.mIdRiferimentoFascicoloSius = null;
		this.mAnnoFascicoloSius = null;
		this.mProgrFascicoloSius = null;
		this.mCodUffFascicoloSius = "";
		this.mDescrUffFascicoloSius = "";
		this.mDataRicezione = null;
		this.mCodOggettoProcedimento = "";
		this.mDescrOggettoProcedimento = "";
		this.mFasSieIdFascicoloSiep = null;
	}

	// COSTRUTTORE DI COPIA
	public RiferimentoFascicoloSiusModel(RiferimentoFascicoloSiusModel aModel) {
		this.mIdRiferimentoFascicoloSius = aModel.mIdRiferimentoFascicoloSius;
		this.mAnnoFascicoloSius = aModel.mAnnoFascicoloSius;
		this.mProgrFascicoloSius = aModel.mProgrFascicoloSius;
		this.mCodUffFascicoloSius = aModel.mCodUffFascicoloSius;
		this.mDescrUffFascicoloSius = aModel.mDescrUffFascicoloSius;
		this.mDataRicezione = aModel.mDataRicezione;
		this.mCodOggettoProcedimento = aModel.mCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aModel.mDescrOggettoProcedimento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
	}

	// COSTRUTTORE MODEL
	public RiferimentoFascicoloSiusModel(BigDecimal aIdRiferimentoFascicoloSius,
			BigDecimal aAnnoFascicoloSius, BigDecimal aProgrFascicoloSius, String aCodUffFascicoloSius,
			String aDescrUffFascicoloSius, Date aDataRicezione, String aCodOggettoProcedimento,
			String aDescrOggettoProcedimento, BigDecimal aFasSieIdFascicoloSiep) {
		this.mIdRiferimentoFascicoloSius = aIdRiferimentoFascicoloSius;
		this.mAnnoFascicoloSius = aAnnoFascicoloSius;
		this.mProgrFascicoloSius = aProgrFascicoloSius;
		this.mCodUffFascicoloSius = aCodUffFascicoloSius;
		this.mDescrUffFascicoloSius = aDescrUffFascicoloSius;
		this.mDataRicezione = aDataRicezione;
		this.mCodOggettoProcedimento = aCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aDescrOggettoProcedimento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdRiferimentoFascicoloSius() {
		return mIdRiferimentoFascicoloSius;
	}

	public BigDecimal getAnnoFascicoloSius() {
		return mAnnoFascicoloSius;
	}

	public BigDecimal getProgrFascicoloSius() {
		return mProgrFascicoloSius;
	}

	public String getCodUffFascicoloSius() {
		return mCodUffFascicoloSius;
	}

	public String getDescrUffFascicoloSius() {
		return mDescrUffFascicoloSius;
	}

	public Date getDataRicezione() {
		return mDataRicezione;
	}

	public String getCodOggettoProcedimento() {
		return mCodOggettoProcedimento;
	}

	public String getDescrOggettoProcedimento() {
		return mDescrOggettoProcedimento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	//
	// METODI SET()
	//
	public void setIdRiferimentoFascicoloSius(BigDecimal aValore) {
		mIdRiferimentoFascicoloSius = aValore;
	}

	public void setAnnoFascicoloSius(BigDecimal aValore) {
		mAnnoFascicoloSius = aValore;
	}

	public void setProgrFascicoloSius(BigDecimal aValore) {
		mProgrFascicoloSius = aValore;
	}

	public void setCodUffFascicoloSius(String aValore) {
		mCodUffFascicoloSius = aValore;
	}

	public void setDescrUffFascicoloSius(String aValore) {
		mDescrUffFascicoloSius = aValore;
	}

	public void setDataRicezione(Date aValore) {
		mDataRicezione = aValore;
	}

	public void setCodOggettoProcedimento(String aValore) {
		mCodOggettoProcedimento = aValore;
	}

	public void setDescrOggettoProcedimento(String aValore) {
		mDescrOggettoProcedimento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdRiferimentoFascicoloSius + " - " + mAnnoFascicoloSius + " - " + mProgrFascicoloSius
				+ " - " + mCodUffFascicoloSius + " - " + mDescrUffFascicoloSius + " - " + mDataRicezione
				+ " - " + mCodOggettoProcedimento + " - " + mDescrOggettoProcedimento + " - "
				+ mFasSieIdFascicoloSiep;

		return lStr;
	}
}
