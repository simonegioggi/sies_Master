package siap.siep.modulocumulo.model;

/**
* <p>Title: RichPMPenAccCumModel</p>
* <p>Description: Classe Model che rappresenta il RichPM_PenAcc_Cum</p>
*
* @author Intersistemi Italia S.p.A.
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class RichPMPenAccCumModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 3543626366280146516L;
	private BigDecimal mRicIdRichiestePmInCumulo;
	private BigDecimal mPenIdPenAccCumulo;
	private String mFlagCondono;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RichPMPenAccCumModel() {
		this.mRicIdRichiestePmInCumulo = null;
		this.mPenIdPenAccCumulo = null;
		this.mFlagCondono = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public RichPMPenAccCumModel(RichPMPenAccCumModel aModel) {
		this.mRicIdRichiestePmInCumulo = aModel.mRicIdRichiestePmInCumulo;
		this.mPenIdPenAccCumulo = aModel.mPenIdPenAccCumulo;
		this.mFlagCondono = aModel.mFlagCondono;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public RichPMPenAccCumModel(BigDecimal aRicIdRichiestePmInCumulo, BigDecimal aPenIdPenAccCumulo,
			String aFlagCondono) {
		this.mRicIdRichiestePmInCumulo = aRicIdRichiestePmInCumulo;
		this.mPenIdPenAccCumulo = aPenIdPenAccCumulo;
		this.mFlagCondono = aFlagCondono;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getRicIdRichiestePmInCumulo() {
		return mRicIdRichiestePmInCumulo;
	}

	public BigDecimal getPenIdPenaAccessoriaCumulo() {
		return mPenIdPenAccCumulo;
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

	public void setPenIdPenaAccessoriaCumulo(BigDecimal aValore) {
		mPenIdPenAccCumulo = aValore;
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

		lStr = "RichPMPenAccCumModel:\n" + "[ mRicIdRichiestePmInCumulo     = " + mRicIdRichiestePmInCumulo
				+ " ]\n" + "[ mPenIdPenAccCumulo	        = " + mPenIdPenAccCumulo + " ]\n"
				+ "[ mFlagCondono	        		= " + mFlagCondono + " ]";

		return lStr;
	}
}
