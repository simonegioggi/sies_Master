package siap.siep.istruttoriacumulo.model;

/**
* <p>Title: RiepilogoPresoffertoCumuloModel</p>
* <p>Description: Classe Model Usato nella Stampa prospetto Dati Analitici Cumulo per riepilogo Presofferto</p>
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class RiepilogoPresoffertoCumuloModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 4951908565505036200L;
	private BigDecimal mTotAnniPresofferto;
	private BigDecimal mTotMesiPresofferto;
	private BigDecimal mTotGiorniPresofferto;

	private String mStringaPresofferto;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RiepilogoPresoffertoCumuloModel() {

		this.mTotAnniPresofferto = null;
		this.mTotMesiPresofferto = null;
		this.mTotGiorniPresofferto = null;

	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public RiepilogoPresoffertoCumuloModel(RiepilogoPresoffertoCumuloModel aModel) {

		this.mTotAnniPresofferto = aModel.mTotAnniPresofferto;
		this.mTotMesiPresofferto = aModel.mTotMesiPresofferto;
		this.mTotGiorniPresofferto = aModel.mTotGiorniPresofferto;

	}

	// ============================================================================
	// METODI GET()
	// ============================================================================

	public BigDecimal getTotAnniPresofferto() {
		return mTotAnniPresofferto;
	}

	public BigDecimal getTotMesiPresofferto() {
		return mTotMesiPresofferto;
	}

	public BigDecimal getTotGiorniPresofferto() {
		return mTotGiorniPresofferto;
	}

	public String getStringaPresofferto() {
		return mStringaPresofferto;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setTotAnniPresofferto(BigDecimal aValore) {
		mTotAnniPresofferto = aValore;
	}

	public void setTotMesiPresofferto(BigDecimal aValore) {
		mTotMesiPresofferto = aValore;
	}

	public void setTotGiorniPresofferto(BigDecimal aValore) {
		mTotGiorniPresofferto = aValore;
	}

	public void setStringaPresofferto(String aValore) {
		mStringaPresofferto = aValore;
	}

	public void calcolaStringaPresofferto() {
		String lStringaPres = "";

		if (this.mTotAnniPresofferto != null) {
			if (this.mTotAnniPresofferto.intValue() != 0)
				lStringaPres = "Anni " + this.mTotAnniPresofferto;
		}

		if (this.mTotMesiPresofferto != null) {
			if (this.mTotMesiPresofferto.intValue() != 0)
				lStringaPres += " Mesi " + this.mTotMesiPresofferto;
		}

		if (this.mTotGiorniPresofferto != null) {
			if (this.mTotGiorniPresofferto.intValue() != 0)
				lStringaPres += " Giorni " + this.mTotGiorniPresofferto;
		}

		if (lStringaPres.length() > 1)
			mStringaPresofferto = lStringaPres.trim();
		else
			mStringaPresofferto = null;

	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "RiepilogoPresoffertoCumuloModel:\n" + "[ mTotAnniPresofferto                     = "
				+ mTotAnniPresofferto + " ]\n" + "[ mTotMesiPresofferto                     = "
				+ mTotMesiPresofferto + " ]\n" + "[ mTotGiorniPresofferto                   = "
				+ mTotGiorniPresofferto + " ]";

		return lStr;
	}
}
