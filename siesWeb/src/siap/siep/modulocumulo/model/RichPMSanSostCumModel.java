package siap.siep.modulocumulo.model;

/**
* <p>Title: RichPMSanSostCumModel</p>
* <p>Description: Classe Model che rappresenta il RichPM_Sanzione_Sost_Cum</p>
*
* @author Intersistemi Italia S.p.A.
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class RichPMSanSostCumModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4331530331324000211L;
	private BigDecimal mRicIdRichiestePmInCumulo;
	private BigDecimal mSanIdSanSostCum;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RichPMSanSostCumModel() {
		this.mRicIdRichiestePmInCumulo = null;
		this.mSanIdSanSostCum = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public RichPMSanSostCumModel(RichPMSanSostCumModel aModel) {
		this.mRicIdRichiestePmInCumulo = aModel.mRicIdRichiestePmInCumulo;
		this.mSanIdSanSostCum = aModel.mSanIdSanSostCum;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public RichPMSanSostCumModel(BigDecimal aRicIdRichiestePmInCumulo, BigDecimal aSanIdSanSostCum) {
		this.mRicIdRichiestePmInCumulo = aRicIdRichiestePmInCumulo;
		this.mSanIdSanSostCum = aSanIdSanSostCum;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getRicIdRichiestePmInCumulo() {
		return mRicIdRichiestePmInCumulo;
	}

	public BigDecimal getSanIdSanSostCumulo() {
		return mSanIdSanSostCum;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setRicIdRichiestePmInCumulo(BigDecimal aValore) {
		mRicIdRichiestePmInCumulo = aValore;
	}

	public void setSanIdSanSostCumulo(BigDecimal aValore) {
		mSanIdSanSostCum = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "RichPMSanSostCumModel:\n" + "[ mRicIdRichiestePmInCumulo     = " + mRicIdRichiestePmInCumulo
				+ " ]\n" + "[ mSanIdSanSostCum	       		= " + mSanIdSanSostCum + " ]";

		return lStr;
	}
}
