package siap.sico.provvedimentisiesnsc.model;

/**
* <p>Title: ProvvSiesNscModel</p>
* <p>Description: Classe Model che rappresenta il ProvvSiesNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.model.GenericModel;

public class ProvvSiesNscModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1834706471482270972L;

	private String mProvvDomain;
	private String mProvvCodcentr;
	private String mProvvNscCat;
	private String mProvvNscDesCat;
	private String mProvvNscNat;
	private String mProvvNscDesNat;
	private String mProvvSiesOggetto;
	private String mProvvSiesDesOggetto;
	private String mProvvSiesMotivo;
	private String mProvvSiesDesMotivo;
	private String mProvvSiesEsito;
	private String mProvvSiesDesEsito;
	private String mProvvVal1;
	private String mProvvVal2;
	private String mProvvVal3;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public ProvvSiesNscModel() {
		this.mProvvDomain = "";
		this.mProvvCodcentr = "";
		this.mProvvNscCat = "";
		this.mProvvNscDesCat = "";
		this.mProvvNscNat = "";
		this.mProvvNscDesNat = "";
		this.mProvvSiesOggetto = "";
		this.mProvvSiesDesOggetto = "";
		this.mProvvSiesMotivo = "";
		this.mProvvSiesDesMotivo = "";
		this.mProvvSiesEsito = "";
		this.mProvvSiesDesEsito = "";
		this.mProvvVal1 = "";
		this.mProvvVal2 = "";
		this.mProvvVal3 = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public ProvvSiesNscModel(ProvvSiesNscModel aModel) {
		this.mProvvDomain = aModel.mProvvDomain;
		this.mProvvCodcentr = aModel.mProvvCodcentr;
		this.mProvvNscCat = aModel.mProvvNscCat;
		this.mProvvNscDesCat = aModel.mProvvNscDesCat;
		this.mProvvNscNat = aModel.mProvvNscNat;
		this.mProvvNscDesNat = aModel.mProvvNscDesNat;
		this.mProvvSiesOggetto = aModel.mProvvSiesOggetto;
		this.mProvvSiesDesOggetto = aModel.mProvvSiesDesOggetto;
		this.mProvvSiesMotivo = aModel.mProvvSiesMotivo;
		this.mProvvSiesDesMotivo = aModel.mProvvSiesDesMotivo;
		this.mProvvSiesEsito = aModel.mProvvSiesEsito;
		this.mProvvSiesDesEsito = aModel.mProvvSiesDesEsito;
		this.mProvvVal1 = aModel.mProvvVal1;
		this.mProvvVal2 = aModel.mProvvVal2;
		this.mProvvVal3 = aModel.mProvvVal3;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public ProvvSiesNscModel(String aProvvDomain, String aProvvCodcentr, String aProvvNscCat,
			String aProvvNscDesCat, String aProvvNscNat, String aProvvNscDesNat, String aProvvSiesOggetto,
			String aProvvSiesDesOggetto, String aProvvSiesMotivo, String aProvvSiesDesMotivo,
			String aProvvSiesEsito, String aProvvSiesDesEsito, String aProvvVal1, String aProvvVal2,
			String aProvvVal3) {
		this.mProvvDomain = aProvvDomain;
		this.mProvvCodcentr = aProvvCodcentr;
		this.mProvvNscCat = aProvvNscCat;
		this.mProvvNscDesCat = aProvvNscDesCat;
		this.mProvvNscNat = aProvvNscNat;
		this.mProvvNscDesNat = aProvvNscDesNat;
		this.mProvvSiesOggetto = aProvvSiesOggetto;
		this.mProvvSiesDesOggetto = aProvvSiesDesOggetto;
		this.mProvvSiesMotivo = aProvvSiesMotivo;
		this.mProvvSiesDesMotivo = aProvvSiesDesMotivo;
		this.mProvvSiesEsito = aProvvSiesEsito;
		this.mProvvSiesDesEsito = aProvvSiesDesEsito;
		this.mProvvVal1 = aProvvVal1;
		this.mProvvVal2 = aProvvVal2;
		this.mProvvVal3 = aProvvVal3;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public String getProvvDomain() {
		return mProvvDomain;
	}

	public String getProvvCodcentr() {
		return mProvvCodcentr;
	}

	public String getProvvNscCat() {
		return mProvvNscCat;
	}

	public String getProvvNscDesCat() {
		return mProvvNscDesCat;
	}

	public String getProvvNscNat() {
		return mProvvNscNat;
	}

	public String getProvvNscDesNat() {
		return mProvvNscDesNat;
	}

	public String getProvvSiesOggetto() {
		return mProvvSiesOggetto;
	}

	public String getProvvSiesDesOggetto() {
		return mProvvSiesDesOggetto;
	}

	public String getProvvSiesMotivo() {
		return mProvvSiesMotivo;
	}

	public String getProvvSiesDesMotivo() {
		return mProvvSiesDesMotivo;
	}

	public String getProvvSiesEsito() {
		return mProvvSiesEsito;
	}

	public String getProvvSiesDesEsito() {
		return mProvvSiesDesEsito;
	}

	public String getProvvVal1() {
		return mProvvVal1;
	}

	public String getProvvVal2() {
		return mProvvVal2;
	}

	public String getProvvVal3() {
		return mProvvVal3;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setProvvDomain(String aValore) {
		mProvvDomain = aValore;
	}

	public void setProvvCodcentr(String aValore) {
		mProvvCodcentr = aValore;
	}

	public void setProvvNscCat(String aValore) {
		mProvvNscCat = aValore;
	}

	public void setProvvNscDesCat(String aValore) {
		mProvvNscDesCat = aValore;
	}

	public void setProvvNscNat(String aValore) {
		mProvvNscNat = aValore;
	}

	public void setProvvNscDesNat(String aValore) {
		mProvvNscDesNat = aValore;
	}

	public void setProvvSiesOggetto(String aValore) {
		mProvvSiesOggetto = aValore;
	}

	public void setProvvSiesDesOggetto(String aValore) {
		mProvvSiesDesOggetto = aValore;
	}

	public void setProvvSiesMotivo(String aValore) {
		mProvvSiesMotivo = aValore;
	}

	public void setProvvSiesDesMotivo(String aValore) {
		mProvvSiesDesMotivo = aValore;
	}

	public void setProvvSiesEsito(String aValore) {
		mProvvSiesEsito = aValore;
	}

	public void setProvvSiesDesEsito(String aValore) {
		mProvvSiesDesEsito = aValore;
	}

	public void setProvvVal1(String aValore) {
		mProvvVal1 = aValore;
	}

	public void setProvvVal2(String aValore) {
		mProvvVal2 = aValore;
	}

	public void setProvvVal3(String aValore) {
		mProvvVal3 = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "ProvvSiesNscModel:\n" + "[ mProvvDomain         = " + mProvvDomain + " ]\n"
				+ "[ mProvvCodcentr       = " + mProvvCodcentr + " ]\n" + "[ mProvvNscCat         = "
				+ mProvvNscCat + " ]\n" + "[ mProvvNscDesCat      = " + mProvvNscDesCat + " ]\n"
				+ "[ mProvvNscNat         = " + mProvvNscNat + " ]\n" + "[ mProvvNscDesNat      = "
				+ mProvvNscDesNat + " ]\n" + "[ mProvvSiesOggetto    = " + mProvvSiesOggetto + " ]\n"
				+ "[ mProvvSiesDesOggetto = " + mProvvSiesDesOggetto + " ]\n" + "[ mProvvSiesMotivo     = "
				+ mProvvSiesMotivo + " ]\n" + "[ mProvvSiesDesMotivo  = " + mProvvSiesDesMotivo + " ]\n"
				+ "[ mProvvSiesEsito      = " + mProvvSiesEsito + " ]\n" + "[ mProvvSiesDesEsito   = "
				+ mProvvSiesDesEsito + " ]\n" + "[ mProvvVal1           = " + mProvvVal1 + " ]\n"
				+ "[ mProvvVal2           = " + mProvvVal2 + " ]\n" + "[ mProvvVal3           = " + mProvvVal3
				+ " ]";
		return lStr;
	}

}