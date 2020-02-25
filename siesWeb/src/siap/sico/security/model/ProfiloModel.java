package siap.sico.security.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class ProfiloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5626813077269372513L;

	private BigDecimal mCodProfilo;
	private String mDescrizione;
	private Date mDataFineValidita;

	private FunzioneModel mFunzioneRadice;

	// COSTRUTTORE DI DEFAULT
	public ProfiloModel() {
		this.mCodProfilo = null;
		this.mDescrizione = "";
		this.mDataFineValidita = null;

		this.mFunzioneRadice = null;
	}

	// COSTRUTTORE DI COPIA
	public ProfiloModel(ProfiloModel aModel) {
		this.mCodProfilo = aModel.mCodProfilo;
		this.mDescrizione = aModel.mDescrizione;
		this.mDataFineValidita = aModel.mDataFineValidita;

		this.mFunzioneRadice = aModel.mFunzioneRadice;
	}

	// COSTRUTTORE MODEL
	public ProfiloModel(BigDecimal aCodProfilo, String aDescrizione, Date aDataFineValidita) {
		this.mCodProfilo = aCodProfilo;
		this.mDescrizione = aDescrizione;
		this.mDataFineValidita = aDataFineValidita;

		this.mFunzioneRadice = null;
	}

	//
	// METODI GET()
	//
	public BigDecimal getCodProfilo() {
		return mCodProfilo;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public FunzioneModel getFunzioneRadice() {
		return mFunzioneRadice;
	}

	//
	// METODI SET()
	//
	public void setCodProfilo(BigDecimal aValore) {
		mCodProfilo = aValore;
	}

	public void setDescrizione(String aValore) {
		mDescrizione = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setFunzioneRadice(FunzioneModel aValore) {
		mFunzioneRadice = aValore;
	}

	public boolean esisteFunzione(BigDecimal aIdFunzione) {
		if (mFunzioneRadice == null)
			return false;

		return mFunzioneRadice.esisteFunzione(aIdFunzione);
	}

	public boolean equals(Object aObj) {
		boolean lIsEquals = false;

		if (aObj != null && aObj instanceof ProfiloModel) {
			ProfiloModel lModel = (ProfiloModel) aObj;

			lIsEquals = mCodProfilo.equals(lModel.getCodProfilo());
		}

		return lIsEquals;
	}

	public String toString() {
		String lToString = this.mCodProfilo + " - " + this.mDescrizione + " - " + this.mDataFineValidita
				+ " - ";

		return lToString;
	}

}