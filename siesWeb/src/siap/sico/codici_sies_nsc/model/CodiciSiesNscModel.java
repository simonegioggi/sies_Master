package siap.sico.codici_sies_nsc.model;

/**
* <p>Title: CodiciSiesNscModel</p>
* <p>Description: Classe Model che rappresenta il CodiciSiesNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.model.GenericModel;

public class CodiciSiesNscModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5492755804477254577L;

	private String mCoDomain;
	private String mCoCodcentr;
	private String mCoNsc;
	private String mCoNscDes;
	private String mCoSies;
	private String mCoSiesDes;
	private String mCoVal1;
	private String mCoVal2;
	private String mCoVal3;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public CodiciSiesNscModel() {
		this.mCoDomain = "";
		this.mCoCodcentr = "";
		this.mCoNsc = "";
		this.mCoNscDes = "";
		this.mCoSies = "";
		this.mCoSiesDes = "";
		this.mCoVal1 = "";
		this.mCoVal2 = "";
		this.mCoVal3 = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public CodiciSiesNscModel(CodiciSiesNscModel aModel) {
		this.mCoDomain = aModel.mCoDomain;
		this.mCoCodcentr = aModel.mCoCodcentr;
		this.mCoNsc = aModel.mCoNsc;
		this.mCoNscDes = aModel.mCoNscDes;
		this.mCoSies = aModel.mCoSies;
		this.mCoSiesDes = aModel.mCoSiesDes;
		this.mCoVal1 = aModel.mCoVal1;
		this.mCoVal2 = aModel.mCoVal2;
		this.mCoVal3 = aModel.mCoVal3;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public CodiciSiesNscModel(String aCoDomain, String aCoCodcentr, String aCoNsc, String aCoNscDes,
			String aCoSies, String aCoSiesDes, String aCoVal1, String aCoVal2, String aCoVal3) {
		this.mCoDomain = aCoDomain;
		this.mCoCodcentr = aCoCodcentr;
		this.mCoNsc = aCoNsc;
		this.mCoNscDes = aCoNscDes;
		this.mCoSies = aCoSies;
		this.mCoSiesDes = aCoSiesDes;
		this.mCoVal1 = aCoVal1;
		this.mCoVal2 = aCoVal2;
		this.mCoVal3 = aCoVal3;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public String getCoDomain() {
		return mCoDomain;
	}

	public String getCoCodcentr() {
		return mCoCodcentr;
	}

	public String getCoNsc() {
		return mCoNsc;
	}

	public String getCoNscDes() {
		return mCoNscDes;
	}

	public String getCoSies() {
		return mCoSies;
	}

	public String getCoSiesDes() {
		return mCoSiesDes;
	}

	public String getCoVal1() {
		return mCoVal1;
	}

	public String getCoVal2() {
		return mCoVal2;
	}

	public String getCoVal3() {
		return mCoVal3;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setCoDomain(String aValore) {
		mCoDomain = aValore;
	}

	public void setCoCodcentr(String aValore) {
		mCoCodcentr = aValore;
	}

	public void setCoNsc(String aValore) {
		mCoNsc = aValore;
	}

	public void setCoNscDes(String aValore) {
		mCoNscDes = aValore;
	}

	public void setCoSies(String aValore) {
		mCoSies = aValore;
	}

	public void setCoSiesDes(String aValore) {
		mCoSiesDes = aValore;
	}

	public void setCoVal1(String aValore) {
		mCoVal1 = aValore;
	}

	public void setCoVal2(String aValore) {
		mCoVal2 = aValore;
	}

	public void setCoVal3(String aValore) {
		mCoVal3 = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "CodiciSiesNscModel:\n" + "[ mCoDomain   = " + mCoDomain + " ]\n" + "[ mCoCodcentr = "
				+ mCoCodcentr + " ]\n" + "[ mCoNsc      = " + mCoNsc + " ]\n" + "[ mCoNscDes   = " + mCoNscDes
				+ " ]\n" + "[ mCoSies     = " + mCoSies + " ]\n" + "[ mCoSiesDes  = " + mCoSiesDes + " ]\n"
				+ "[ mCoVal1     = " + mCoVal1 + " ]\n" + "[ mCoVal2     = " + mCoVal2 + " ]\n"
				+ "[ mCoVal3     = " + mCoVal3 + " ]";
		return lStr;
	}

}