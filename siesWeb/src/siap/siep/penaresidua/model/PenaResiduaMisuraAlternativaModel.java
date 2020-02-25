package siap.siep.penaresidua.model;

/**
* <p>Title: PenaResiduaModel</p>
* <p>Description: Classe Model che rappresenta il PenaResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class PenaResiduaMisuraAlternativaModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -8431245436123892462L;
	private Date mDataInizio;
	private Date mDataFine;
	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private String mStringaReclusione;
	private String mStringaArresto;

	// COSTRUTTORE DI DEFAULT
	public PenaResiduaMisuraAlternativaModel() {

		this.mDataInizio = null;
		this.mDataFine = null;
		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
	}

	// COSTRUTTORE DI COPIA
	public PenaResiduaMisuraAlternativaModel(PenaResiduaMisuraAlternativaModel aModel) {
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mNumAnniReclusione = aModel.mNumAnniReclusione;
		this.mNumMesiReclusione = aModel.mNumMesiReclusione;
		this.mNumGiorniReclusione = aModel.mNumGiorniReclusione;
		this.mNumAnniArresto = aModel.mNumAnniArresto;
		this.mNumMesiArresto = aModel.mNumMesiArresto;
		this.mNumGiorniArresto = aModel.mNumGiorniArresto;
	}

	// METODI GET()

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public BigDecimal getNumAnniReclusione() {
		if (mNumAnniReclusione != null)
			return mNumAnniReclusione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiReclusione() {
		if (mNumMesiReclusione != null)
			return mNumMesiReclusione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniReclusione() {
		if (mNumGiorniReclusione != null)
			return mNumGiorniReclusione;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumAnniArresto() {
		if (mNumAnniArresto != null)
			return mNumAnniArresto;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumMesiArresto() {
		if (mNumMesiArresto != null)
			return mNumMesiArresto;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumGiorniArresto() {
		if (mNumGiorniArresto != null)
			return mNumGiorniArresto;
		else
			return new BigDecimal(0);
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	// METODI SET()

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setNumAnniReclusione(BigDecimal aValore) {
		mNumAnniReclusione = aValore;
	}

	public void setNumMesiReclusione(BigDecimal aValore) {
		mNumMesiReclusione = aValore;
	}

	public void setNumGiorniReclusione(BigDecimal aValore) {
		mNumGiorniReclusione = aValore;
	}

	public void setNumAnniArresto(BigDecimal aValore) {
		mNumAnniArresto = aValore;
	}

	public void setNumMesiArresto(BigDecimal aValore) {
		mNumMesiArresto = aValore;
	}

	public void setNumGiorniArresto(BigDecimal aValore) {
		mNumGiorniArresto = aValore;
	}

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public PenaResiduaModel getPenaResiduaModel() {
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setDataInizio(this.mDataInizio);
		lPenaRes.setDataFine(this.mDataFine);
		lPenaRes.setNumAnniReclusione(this.mNumAnniReclusione);
		lPenaRes.setNumMesiReclusione(this.mNumMesiReclusione);
		lPenaRes.setNumGiorniReclusione(this.mNumGiorniReclusione);
		lPenaRes.setNumAnniArresto(this.mNumAnniArresto);
		lPenaRes.setNumMesiArresto(this.mNumMesiArresto);
		lPenaRes.setNumGiorniArresto(this.mNumGiorniArresto);

		return lPenaRes;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mDataInizio + " - " + mDataFine + " - " + mNumAnniReclusione + " - " + mNumMesiReclusione
				+ " - " + mNumGiorniReclusione + " - " + mNumAnniArresto + " - " + mNumMesiArresto + " - "
				+ mNumGiorniArresto;

		return lStr;
	}

	/**
	 * calcolaStringaArresto per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaArresto() {
		String lStringArresto = "";
		if (this.getNumAnniArresto() != null) {
			if (this.getNumAnniArresto().intValue() != 0)
				lStringArresto = "Anni " + this.getNumAnniArresto();
		}
		if (this.getNumMesiArresto() != null) {
			if (this.getNumMesiArresto().intValue() != 0)
				lStringArresto += " Mesi " + this.getNumMesiArresto();
		}
		if (this.getNumGiorniArresto() != null) {
			if (this.getNumGiorniArresto().intValue() != 0)
				lStringArresto += " Giorni " + this.getNumGiorniArresto();
		}

		if (lStringArresto.length() > 1)
			this.mStringaArresto = lStringArresto;
		else
			this.mStringaArresto = null;
	}

	/**
	 * calcolaStringaReclusione per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaReclusione() {
		String lStringReclusione = "";
		if (this.getNumAnniReclusione() != null) {
			if (this.getNumAnniReclusione().intValue() != 0)
				lStringReclusione = "Anni " + this.getNumAnniReclusione();
		}

		if (this.getNumMesiReclusione() != null) {
			if (this.getNumMesiReclusione().intValue() != 0)
				lStringReclusione += " Mesi " + this.getNumMesiReclusione();
		}

		if (this.getNumGiorniReclusione() != null) {
			if (this.getNumGiorniReclusione().intValue() != 0)
				lStringReclusione += " Giorni " + this.getNumGiorniReclusione();
		}

		if (lStringReclusione.length() > 1)
			this.mStringaReclusione = lStringReclusione;
		else
			this.mStringaReclusione = null;
	}

}
