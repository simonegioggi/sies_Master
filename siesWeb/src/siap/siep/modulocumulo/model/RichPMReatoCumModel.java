package siap.siep.modulocumulo.model;

/**
* <p>Title: RichPMReatoCumModel</p>
* <p>Description: Classe Model che rappresenta il RichPM_Reato_Cum</p>
*
* @author Intersistemi Italia S.p.A.
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class RichPMReatoCumModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -6267804768784413872L;
	private BigDecimal mRicIdRichiestePmInCumulo;
	private BigDecimal mReaIdReatoCumulo;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RichPMReatoCumModel() {
		this.mRicIdRichiestePmInCumulo = null;
		this.mReaIdReatoCumulo = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public RichPMReatoCumModel(RichPMReatoCumModel aModel) {
		this.mRicIdRichiestePmInCumulo = aModel.mRicIdRichiestePmInCumulo;
		this.mReaIdReatoCumulo = aModel.mReaIdReatoCumulo;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public RichPMReatoCumModel(BigDecimal aRicIdRichiestePmInCumulo, BigDecimal aReaIdReatoCumulo) {
		this.mRicIdRichiestePmInCumulo = aRicIdRichiestePmInCumulo;
		this.mReaIdReatoCumulo = aReaIdReatoCumulo;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getRicIdRichiestePmInCumulo() {
		return mRicIdRichiestePmInCumulo;
	}

	public BigDecimal getReaIdReatoCumulo() {
		return mReaIdReatoCumulo;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setRicIdRichiestePmInCumulo(BigDecimal aValore) {
		mRicIdRichiestePmInCumulo = aValore;
	}

	public void setReaIdReatoCumulo(BigDecimal aValore) {
		mReaIdReatoCumulo = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "RichPMReatoCumModel:\n" + "[ mRicIdRichiestePmInCumulo     = " + mRicIdRichiestePmInCumulo
				+ " ]\n" + "[ mReaIdReatoCumulo	       		= " + mReaIdReatoCumulo + " ]";

		return lStr;
	}
}
