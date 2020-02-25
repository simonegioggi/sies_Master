package siap.siep.modulocumulo.model;

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class RichPMMisSicCumModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -391089170058232940L;
	private BigDecimal mRicIdRichiestePmInCumulo;
	private BigDecimal mMisIdMisSicCumulo;
	private String mFlagCondono;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RichPMMisSicCumModel() {
		this.mRicIdRichiestePmInCumulo = null;
		this.mMisIdMisSicCumulo = null;
		this.mFlagCondono = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public RichPMMisSicCumModel(RichPMMisSicCumModel aModel) {
		this.mRicIdRichiestePmInCumulo = aModel.mRicIdRichiestePmInCumulo;
		this.mMisIdMisSicCumulo = aModel.mMisIdMisSicCumulo;
		this.mFlagCondono = aModel.mFlagCondono;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public RichPMMisSicCumModel(BigDecimal aRicIdRichiestePmInCumulo, BigDecimal aMisIdMisSicCumulo,
			String aFlagCondono) {
		this.mRicIdRichiestePmInCumulo = aRicIdRichiestePmInCumulo;
		this.mMisIdMisSicCumulo = aMisIdMisSicCumulo;
		this.mFlagCondono = aFlagCondono;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getRicIdRichiestePmInCumulo() {
		return mRicIdRichiestePmInCumulo;
	}

	public BigDecimal getMisIdMisSicCumulo() {
		return mMisIdMisSicCumulo;
	}

	public String getFlagCondono() {
		return mFlagCondono;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setRicIdRichiestePmInCumulo(BigDecimal aValore) {
		mRicIdRichiestePmInCumulo = aValore;
	}

	public void setMisIdMisSicCumulo(BigDecimal aValore) {
		mMisIdMisSicCumulo = aValore;
	}

	public void setFlagCondono(String aValore) {
		mFlagCondono = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "RichPMMisSicCumModel:\n" + "[ mRicIdRichiestePmInCumulo     = " + mRicIdRichiestePmInCumulo
				+ " ]\n" + "[ mMisIdMisSicCumulo	        = " + mMisIdMisSicCumulo + " ]\n"
				+ "[ mFlagCondono	        		= " + mFlagCondono + " ]";

		return lStr;
	}
}
