package siap.sico.profilo.model;

/**
* <p>Title: ProfiloModel</p>
* <p>Description: Classe Model che rappresenta il Profilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class ProfiloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1321423279739356574L;

	/**
	 * Variabile statica, che è valorizzata con il codice 99, corrispondente al profilo dell'amminsitratore di
	 * sistema.
	 */
	public static final BigDecimal COD_PROFILO_SYS_ADMIN = new BigDecimal(99);

	private BigDecimal mCodProfilo;
	private String mDescrizione;
	private Date mDataFineValidita;

	// COSTRUTTORE DI DEFAULT
	public ProfiloModel() {
		this.mCodProfilo = null;
		this.mDescrizione = "";
		this.mDataFineValidita = null;
	}

	// COSTRUTTORE DI COPIA
	public ProfiloModel(ProfiloModel aModel) {
		this.mCodProfilo = aModel.mCodProfilo;
		this.mDescrizione = aModel.mDescrizione;
		this.mDataFineValidita = aModel.mDataFineValidita;
	}

	// COSTRUTTORE MODEL
	public ProfiloModel(BigDecimal aCodProfilo, String aDescrizione, Date aDataFineValidita) {
		this.mCodProfilo = aCodProfilo;
		this.mDescrizione = aDescrizione;
		this.mDataFineValidita = aDataFineValidita;
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

	public String toString() {
		String lStr = new String();

		lStr = "" + mCodProfilo + " - " + mDescrizione + " - " + mDataFineValidita;

		return lStr;
	}

}