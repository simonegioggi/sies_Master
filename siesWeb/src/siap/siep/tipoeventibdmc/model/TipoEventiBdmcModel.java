package siap.siep.tipoeventibdmc.model;

/**
* <p>Title: TipoEventiBdmcModel</p>
* <p>Description: Classe Model che rappresenta il TipoEventiBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class TipoEventiBdmcModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4039925779756177390L;

	private BigDecimal mIdTipoEventiBdmc;
	private String mCodTipoEvento;
	private String mDescrTipoEvento;
	private String mCodProvvedimento;
	private String mDescrProvvedimento;
	private String mCodMotivo;
	private String mDescrMotivo;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public TipoEventiBdmcModel() {
		this.mIdTipoEventiBdmc = null;
		this.mCodTipoEvento = "";
		this.mDescrTipoEvento = "";
		this.mCodProvvedimento = "";
		this.mDescrProvvedimento = "";
		this.mCodMotivo = "";
		this.mDescrMotivo = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public TipoEventiBdmcModel(TipoEventiBdmcModel aModel) {
		this.mIdTipoEventiBdmc = aModel.mIdTipoEventiBdmc;
		this.mCodTipoEvento = aModel.mCodTipoEvento;
		this.mDescrTipoEvento = aModel.mDescrTipoEvento;
		this.mCodProvvedimento = aModel.mCodProvvedimento;
		this.mDescrProvvedimento = aModel.mDescrProvvedimento;
		this.mCodMotivo = aModel.mCodMotivo;
		this.mDescrMotivo = aModel.mDescrMotivo;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public TipoEventiBdmcModel(BigDecimal aIdTipoEventiBdmc, String aCodTipoEvento, String aDescrTipoEvento,
			String aCodProvvedimento, String aDescrProvvedimento, String aCodMotivo) {
		this.mIdTipoEventiBdmc = aIdTipoEventiBdmc;
		this.mCodTipoEvento = aCodTipoEvento;
		this.mDescrTipoEvento = aDescrTipoEvento;
		this.mCodProvvedimento = aCodProvvedimento;
		this.mDescrProvvedimento = aDescrProvvedimento;
		this.mCodMotivo = aCodMotivo;
		// this.mDescrMotivo = aDescrMotivo;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdTipoEventiBdmc() {
		return mIdTipoEventiBdmc;
	}

	public String getCodTipoEvento() {
		return mCodTipoEvento;
	}

	public String getDescrTipoEvento() {
		return mDescrTipoEvento;
	}

	public String getCodProvvedimento() {
		return mCodProvvedimento;
	}

	public String getDescrProvvedimento() {
		return mDescrProvvedimento;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

	public String getDescrMotivo() {
		return mDescrMotivo;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdTipoEventiBdmc(BigDecimal aValore) {
		mIdTipoEventiBdmc = aValore;
	}

	public void setCodTipoEvento(String aValore) {
		mCodTipoEvento = aValore;
	}

	public void setDescrTipoEvento(String aValore) {
		mDescrTipoEvento = aValore;
	}

	public void setCodProvvedimento(String aValore) {
		mCodProvvedimento = aValore;
	}

	public void setDescrProvvedimento(String aValore) {
		mDescrProvvedimento = aValore;
	}

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

	public void setDescrMotivo(String aValore) {
		mDescrMotivo = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "TipoEventiBdmcModel:\n" + "[ mIdTipoEventiBdmc = " + mIdTipoEventiBdmc + " ]\n"
				+ "[ mCodTipoEvento    = " + mCodTipoEvento + " ]\n" + "[ mCodProvvedimento = "
				+ mCodProvvedimento + " ]\n" + "[ mCodMotivo        = " + mCodMotivo + " ]";
		return lStr;
	}

}