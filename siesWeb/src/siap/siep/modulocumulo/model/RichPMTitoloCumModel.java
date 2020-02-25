package siap.siep.modulocumulo.model;

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class RichPMTitoloCumModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1694594690563202635L;
	private BigDecimal mRicIdRichiestePmInCumulo;
	private BigDecimal mTitIdTitoloCumulato;
  private String  	  mFlagInteroCumulo;
	// private BigDecimal mReaIdReatoCumulo;
	// private BigDecimal mMisIdMisuraSicurezzaCumulo;
	// private BigDecimal mPenIdPenaAccessoriaCumulo;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RichPMTitoloCumModel() {
		this.mRicIdRichiestePmInCumulo = null;
		this.mTitIdTitoloCumulato = null;
    this.mFlagInteroCumulo        =  "";
		// this.mReaIdReatoCumulo = null;
		// this.mMisIdMisuraSicurezzaCumulo = null;
		// this.mPenIdPenaAccessoriaCumulo = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public RichPMTitoloCumModel(RichPMTitoloCumModel aModel) {
		this.mRicIdRichiestePmInCumulo = aModel.mRicIdRichiestePmInCumulo;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
   	this.mFlagInteroCumulo        =  aModel.mFlagInteroCumulo;
		// this.mReaIdReatoCumulo = aModel.mReaIdReatoCumulo;
		// this.mMisIdMisuraSicurezzaCumulo = aModel.mMisIdMisuraSicurezzaCumulo;
		// this.mPenIdPenaAccessoriaCumulo = aModel.mPenIdPenaAccessoriaCumulo;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	  public RichPMTitoloCumModel (
    BigDecimal aRicIdRichiestePmInCumulo,
    BigDecimal aTitIdTitoloCumulato,
    String aFlagInteroCumulo
	// BigDecimal aReaIdReatoCumulo,
	// BigDecimal aMisIdMisuraSicurezzaCumulo,
	// BigDecimal aPenIdPenaAccessoriaCumulo

	) {
		this.mRicIdRichiestePmInCumulo = aRicIdRichiestePmInCumulo;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
    this.mFlagInteroCumulo        =  aFlagInteroCumulo;
		// this.mReaIdReatoCumulo = aReaIdReatoCumulo;
		// this.mMisIdMisuraSicurezzaCumulo = aMisIdMisuraSicurezzaCumulo;
		// this.mPenIdPenaAccessoriaCumulo = aPenIdPenaAccessoriaCumulo;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getRicIdRichiestePmInCumulo() {
		return mRicIdRichiestePmInCumulo;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}
  public String		 getFlagInteroCumulo()		        { return mFlagInteroCumulo; } 
	// public BigDecimal getReaIdReatoCumulo() { return mReaIdReatoCumulo; }
	// public BigDecimal getMisIdMisuraSicurezzaCumulo() { return mMisIdMisuraSicurezzaCumulo; }
	// public BigDecimal getPenIdPenaAccessoriaCumulo() { return mPenIdPenaAccessoriaCumulo; }

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setRicIdRichiestePmInCumulo(BigDecimal aValore) {
		mRicIdRichiestePmInCumulo = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}
  public void  setFlagInteroCumulo       	(String		aValore )    { mFlagInteroCumulo       = aValore; }
	// public void setReaIdReatoCumulo (BigDecimal aValore ) { mReaIdReatoCumulo = aValore; }
	// public void setMisIdMisuraSicurezzaCumulo(BigDecimal aValore ) { mMisIdMisuraSicurezzaCumulo= aValore;
	// }
	// public void setPenIdPenaAccessoriaCumulo (BigDecimal aValore ) { mPenIdPenaAccessoriaCumulo = aValore;
	// }

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "RichiestePmInCumuloModel:\n" +
           "[ mRicIdRichiestePmInCumulo     = "+mRicIdRichiestePmInCumulo+" ]\n"+
           "[ mTitIdTitoloCumulato       = "+mTitIdTitoloCumulato+" ]"+
    	   "[ mFlagInteroCumulo       = "+mFlagInteroCumulo+" ]";
          // "[ mTitIdTitoloCumulato = "+mTitIdTitoloCumulato+" ]\n"+
		// "[ mReaIdReatoCumulo = "+mReaIdReatoCumulo+" ]\n"+
		// "[ mMisIdMisuraSicurezzaCumulo= "+mMisIdMisuraSicurezzaCumulo+" ]\n"+
		// "[ mPenIdPenaAccessoriaCumulo = "+mPenIdPenaAccessoriaCumulo+" ]\n"+

		return lStr;
	}
}
