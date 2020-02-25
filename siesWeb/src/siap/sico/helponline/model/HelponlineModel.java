package siap.sico.helponline.model;

/**
* <p>Title: HelponlineModel</p>
* <p>Description: Classe Model che rappresenta il Helponline</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class HelponlineModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4615087235595026005L;

	private BigDecimal mFunIdFunzione;
	private String mNomePagina;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public HelponlineModel() {
		this.mFunIdFunzione = null;
		this.mNomePagina = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public HelponlineModel(HelponlineModel aModel) {
		this.mFunIdFunzione = aModel.mFunIdFunzione;
		this.mNomePagina = aModel.mNomePagina;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public HelponlineModel(BigDecimal aFunIdFunzione, String aNomePagina) {
		this.mFunIdFunzione = aFunIdFunzione;
		this.mNomePagina = aNomePagina;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================

	public BigDecimal getFunIdFunzione() {
		return mFunIdFunzione;
	}

	public String getNomePagina() {
		return mNomePagina;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setFunIdFunzione(BigDecimal aValore) {
		mFunIdFunzione = aValore;
	}

	public void setNomePagina(String aValore) {
		mNomePagina = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "HelponlineModel:\n" + "[ mFunIdFunzione = " + mFunIdFunzione + " ]\n" + "[ mNomePagina    = "
				+ mNomePagina + " ]";
		return lStr;
	}

}