package f3b.security.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class ProfileModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 882401718476621452L;

	private BigDecimal mProfileId;
	private String mDescription;
	private Date mDateEndValidity;

	private FunctionModel mRadixFunction;

	// COSTRUTTORE DI DEFAULT
	/**
	 * Costruttore di classe.
	 */
	public ProfileModel() {
		this.mProfileId = null;
		this.mDescription = "";
		this.mDateEndValidity = null;

		this.mRadixFunction = null;
	}

	// COSTRUTTORE DI COPIA
	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aModel
	 *            istanza della classe <code>ProfileModel</code>.
	 */
	public ProfileModel(ProfileModel aModel) {
		this.mProfileId = aModel.mProfileId;
		this.mDescription = aModel.mDescription;
		this.mDateEndValidity = aModel.mDateEndValidity;

		this.mRadixFunction = aModel.mRadixFunction;
	}

	// COSTRUTTORE MODEL
	/**
	 * Costruttore di classe con parametri.
	 * <p>
	 * 
	 * @param aProfileId
	 *            id del profilo.
	 * @param aDescription
	 *            descrizione.
	 * @param aDateEndValidity
	 *            data fine validità.
	 */
	public ProfileModel(BigDecimal aProfileId, String aDescription, Date aDateEndValidity) {
		this.mProfileId = aProfileId;
		this.mDescription = aDescription;
		this.mDateEndValidity = aDateEndValidity;

		this.mRadixFunction = null;
	}

	//
	// METODI GET()
	//
	/**
	 * Ritorna l'id del profilo.
	 * <p>
	 * 
	 * @return l'id del profilo.
	 */
	public BigDecimal getProfileId() {
		return mProfileId;
	}

	/**
	 * Ritorna la descrizione.
	 * <p>
	 * 
	 * @return la descrizione.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Ritorna la data di fine validità.
	 * <p>
	 * 
	 * @return la data di fine validità.
	 */
	public Date getDateEndValidity() {
		return mDateEndValidity;
	}

	/**
	 * Ritorna la funzione radice.
	 * <p>
	 * 
	 * @return funzione radice.
	 */
	public FunctionModel getRadixFunction() {
		return mRadixFunction;
	}

	//
	// METODI SET()
	//
	/**
	 * Imposta l'id del profilo.
	 * <p>
	 * 
	 * @param aValue
	 *            l'id del profilo.
	 */
	public void setProfileId(BigDecimal aValue) {
		mProfileId = aValue;
	}

	/**
	 * Imposta la descrizione.
	 * <p>
	 * 
	 * @param aValue
	 *            descrizione.
	 */
	public void setDescription(String aValue) {
		mDescription = aValue;
	}

	/**
	 * Imposta la data di fine validità.
	 * <p>
	 * 
	 * @param aValue
	 *            data di fine validità.
	 */
	public void setDateEndValidity(Date aValue) {
		mDateEndValidity = aValue;
	}

	/**
	 * Imposta la funzione radice.
	 * <p>
	 * 
	 * @param aValue
	 *            funzione radice.
	 */
	public void setRadixFunction(FunctionModel aValue) {
		mRadixFunction = aValue;
	}

	/**
	 * Verifica se esiste la funzione per il profilo.
	 * <p>
	 * 
	 * @param aFunctionId
	 *            id della funzione
	 * @return l'esito della verifica.
	 */
	public boolean existFunction(BigDecimal aFunctionId) {

		if (mRadixFunction == null)
			return false;

		return mRadixFunction.existFunction(aFunctionId);
	}

	/**
	 * Verifica l'uguaglianza di una istanza di <code>ProfileModel</code> con quella corrente.
	 * <p>
	 * 
	 * @param aObj
	 *            istanza da verificare.
	 * @return l'esito della verifica.
	 */
	public boolean equals(Object aObj) {

		boolean lIsEquals = false;

		if ((aObj != null) && (aObj instanceof ProfileModel)) {
			ProfileModel lModel = (ProfileModel) aObj;

			lIsEquals = mProfileId.equals(lModel.getProfileId());
		}

		return lIsEquals;
	}

	/**
	 * Ritorna il valore di tutti gli attributi di classe formattati.
	 * <p>
	 * 
	 * @return valore degli attributi.
	 */
	public String toString() {

		String lToString = this.mProfileId + " - " + this.mDescription + " - " + this.mDateEndValidity;

		return lToString;
	}

	public boolean isSiep() {

		if (mProfileId != null && (mProfileId.compareTo(new BigDecimal(30)) == 0
				|| mProfileId.compareTo(new BigDecimal(4)) == 0
				|| mProfileId.compareTo(new BigDecimal(40)) == 0
				|| mProfileId.compareTo(new BigDecimal(50)) == 0)) {
			return true;
		} else
			return false;
	}

	public boolean isSius() {

		if (mProfileId != null && (mProfileId.compareTo(new BigDecimal(31)) == 0
				|| mProfileId.compareTo(new BigDecimal(32)) == 0
				|| mProfileId.compareTo(new BigDecimal(41)) == 0
				|| mProfileId.compareTo(new BigDecimal(42)) == 0
				|| mProfileId.compareTo(new BigDecimal(14)) == 0
				|| mProfileId.compareTo(new BigDecimal(24)) == 0
				|| mProfileId.compareTo(new BigDecimal(51)) == 0
				|| mProfileId.compareTo(new BigDecimal(52)) == 0

		))

		{
			return true;
		} else
			return false;
	}

	public boolean isSiepe() {

		if (mProfileId != null && (mProfileId.compareTo(new BigDecimal(15)) == 0
				|| mProfileId.compareTo(new BigDecimal(33)) == 0
				|| mProfileId.compareTo(new BigDecimal(43)) == 0
				|| mProfileId.compareTo(new BigDecimal(53)) == 0

		))

		{
			return true;
		} else
			return false;
	}

	public boolean isSige() {

		if (mProfileId != null && (mProfileId.compareTo(new BigDecimal(16)) == 0
				|| mProfileId.compareTo(new BigDecimal(36)) == 0
				|| mProfileId.compareTo(new BigDecimal(46)) == 0
				|| mProfileId.compareTo(new BigDecimal(56)) == 0

		))

		{
			return true;
		} else
			return false;
	}

}