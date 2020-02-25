package siap.siepe.ricezioneatti.model;

/**
* <p>Title: CruscottoModel</p>
* <p>Description: Classe Model che rappresenta il Cruscotto
 * cioè l'aggregato di dati visualizzati attraverso la funzione Cruscotto,
 * questi dati rappresentano dei totalizzatori sullo stato degli Atti ricevuti dall'Ufficio UEPRE.
 * </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class CruscottoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4528533982117470524L;

	private Date mDataRicezione;
	private BigDecimal mNumAttiNuovi;
	private BigDecimal mNumAttiPresiInVisione;
	private BigDecimal mNumAttiPresiInCarico;
	private BigDecimal mNumAttiRicevuti;
	private BigDecimal mNumAttiRestituiti;

	// COSTRUTTORE DI DEFAULT
	public CruscottoModel() {
		this.mDataRicezione = null;
		this.mNumAttiNuovi = null;
		this.mNumAttiPresiInVisione = null;
		this.mNumAttiPresiInCarico = null;
		this.mNumAttiRicevuti = null;
		this.mNumAttiRestituiti = null;
	}

	// COSTRUTTORE DI COPIA
	public CruscottoModel(CruscottoModel aModel) {
		this.mDataRicezione = aModel.mDataRicezione;
		this.mNumAttiNuovi = aModel.mNumAttiNuovi;
		this.mNumAttiPresiInVisione = aModel.mNumAttiPresiInVisione;
		this.mNumAttiPresiInCarico = aModel.mNumAttiPresiInCarico;
		this.mNumAttiRicevuti = aModel.mNumAttiRicevuti;
		this.mNumAttiRestituiti = aModel.mNumAttiRestituiti;
	}

	// COSTRUTTORE MODEL
	public CruscottoModel(Date aDataRicezione, BigDecimal aNumAttiNuovi, BigDecimal aNumAttiPresiInVisione,
			BigDecimal aNumAttiPresiInCarico, BigDecimal aNumAttiRicevuti, BigDecimal aNumAttiRestituiti) {
		this.mDataRicezione = aDataRicezione;
		this.mNumAttiNuovi = aNumAttiNuovi;
		this.mNumAttiPresiInVisione = aNumAttiPresiInVisione;
		this.mNumAttiPresiInCarico = aNumAttiPresiInCarico;
		this.mNumAttiRicevuti = aNumAttiRicevuti;
		this.mNumAttiRestituiti = aNumAttiRestituiti;
	}

	/**
	 * Costruttore per data.
	 * 
	 * @param aDataRicezione
	 */
	public CruscottoModel(Date aDataRicezione) {
		this.mDataRicezione = aDataRicezione;
		this.mNumAttiNuovi = new BigDecimal(0);
		this.mNumAttiPresiInVisione = new BigDecimal(0);
		this.mNumAttiPresiInCarico = new BigDecimal(0);
		this.mNumAttiRicevuti = new BigDecimal(0);
		this.mNumAttiRestituiti = new BigDecimal(0);
	}

	//
	// METODI GET()
	//
	public Date getDataRicezione() {
		return mDataRicezione;
	}

	public BigDecimal getNumAttiNuovi() {
		return mNumAttiNuovi;
	}

	public BigDecimal getNumAttiPresiInVisione() {
		return mNumAttiPresiInVisione;
	}

	public BigDecimal getNumAttiPresiInCarico() {
		return mNumAttiPresiInCarico;
	}

	public BigDecimal getNumAttiRicevuti() {
		return mNumAttiRicevuti;
	}

	public BigDecimal getNumAttiRestituiti() {
		return mNumAttiRestituiti;
	}

	//
	// METODI SET()
	//

	public void setDataRicezione(Date aValore) {
		mDataRicezione = aValore;
	}

	public void setINumAttiNuovi(BigDecimal aValore) {
		mNumAttiNuovi = aValore;
	}

	public void setINumAttiPresiInVisione(BigDecimal aValore) {
		mNumAttiPresiInVisione = aValore;
	}

	public void setINumAttiPresiInCarico(BigDecimal aValore) {
		mNumAttiPresiInCarico = aValore;
	}

	public void setINumAttiRicevuti(BigDecimal aValore) {
		mNumAttiRicevuti = aValore;
	}

	public void setINumAttiRestituiti(BigDecimal aValore) {
		mNumAttiRestituiti = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mDataRicezione + " - " + mNumAttiNuovi + " - " + mNumAttiPresiInVisione + " - "
				+ mNumAttiPresiInCarico + " - " + mNumAttiRicevuti + " - " + mNumAttiRicevuti;

		return lStr;
	}

}