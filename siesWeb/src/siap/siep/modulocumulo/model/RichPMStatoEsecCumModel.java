package siap.siep.modulocumulo.model;

/**
* <p>Title: RichPMStatoEsecCumModel</p>
* <p>Description: Classe Model che rappresenta il RichPM_Stato_Esecuzione_Cum</p>
*
* @author Intersistemi Italia S.p.A.
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class RichPMStatoEsecCumModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 3524398396554549068L;
	private BigDecimal mRicIdRichiestePmInCumulo;
	private BigDecimal mStatIdStatoEsecCum;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RichPMStatoEsecCumModel() {
		this.mRicIdRichiestePmInCumulo = null;
		this.mStatIdStatoEsecCum = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public RichPMStatoEsecCumModel(RichPMStatoEsecCumModel aModel) {
		this.mRicIdRichiestePmInCumulo = aModel.mRicIdRichiestePmInCumulo;
		this.mStatIdStatoEsecCum = aModel.mStatIdStatoEsecCum;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public RichPMStatoEsecCumModel(BigDecimal aRicIdRichiestePmInCumulo, BigDecimal aStatIdStatoEsecCum) {
		this.mRicIdRichiestePmInCumulo = aRicIdRichiestePmInCumulo;
		this.mStatIdStatoEsecCum = aStatIdStatoEsecCum;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getRicIdRichiestePmInCumulo() {
		return mRicIdRichiestePmInCumulo;
	}

	public BigDecimal getStatIdStatoEsecCumulo() {
		return mStatIdStatoEsecCum;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setRicIdRichiestePmInCumulo(BigDecimal aValore) {
		mRicIdRichiestePmInCumulo = aValore;
	}

	public void setStatIdStatoEsecCumulo(BigDecimal aValore) {
		mStatIdStatoEsecCum = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "RichPMStatoEsecCumModel:\n" + "[ mRicIdRichiestePmInCumulo     = " + mRicIdRichiestePmInCumulo
				+ " ]\n" + "[ mStatIdStatoEsecCum	       	= " + mStatIdStatoEsecCum + " ]";

		return lStr;
	}
}
