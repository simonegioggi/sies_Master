package siap.siep.modulocumulo.model;

/**
* <p>Title: RichPMBeneficioCumModel</p>
* <p>Description: Classe Model che rappresenta il RichPM_Beneficio_Cum</p>
*
* @author Intersistemi Italia S.p.A.
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class RichPMBeneficioCumModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -1123179808977822523L;
	private BigDecimal mRicIdRichiestePmInCumulo;
	private BigDecimal mBenIdBeneficioCum;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RichPMBeneficioCumModel() {
		this.mRicIdRichiestePmInCumulo = null;
		this.mBenIdBeneficioCum = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public RichPMBeneficioCumModel(RichPMBeneficioCumModel aModel) {
		this.mRicIdRichiestePmInCumulo = aModel.mRicIdRichiestePmInCumulo;
		this.mBenIdBeneficioCum = aModel.mBenIdBeneficioCum;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public RichPMBeneficioCumModel(BigDecimal aRicIdRichiestePmInCumulo, BigDecimal aBenIdBeneficioCum) {
		this.mRicIdRichiestePmInCumulo = aRicIdRichiestePmInCumulo;
		this.mBenIdBeneficioCum = aBenIdBeneficioCum;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getRicIdRichiestePmInCumulo() {
		return mRicIdRichiestePmInCumulo;
	}

	public BigDecimal getBenIdBeneficioCum() {
		return mBenIdBeneficioCum;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setRicIdRichiestePmInCumulo(BigDecimal aValore) {
		mRicIdRichiestePmInCumulo = aValore;
	}

	public void setBenIdBeneficioCum(BigDecimal aValore) {
		mBenIdBeneficioCum = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "RichPMBeneficioCumModel:\n" + "[ mRicIdRichiestePmInCumulo     = " + mRicIdRichiestePmInCumulo
				+ " ]\n" + "[ mBenIdBeneficioCum	        = " + mBenIdBeneficioCum + " ]";

		return lStr;
	}
}
