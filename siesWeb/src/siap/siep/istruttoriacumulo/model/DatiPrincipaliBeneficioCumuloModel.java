package siap.siep.istruttoriacumulo.model;

import java.math.BigDecimal;

import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: DatiPrincipaliBeneficioCumuloModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il TitoloCumulato
 * </p>
 * 
 * @version 1.0
 */
public class DatiPrincipaliBeneficioCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4344760179271251925L;

	private BigDecimal mIdBeneficioCumulo;
	private String mCodNaturaBeneficio;
	private String mDescrNaturaBeneficio;
	private String mCodTipoBeneficio;
	private String mDescrTipoBeneficio;
	private String mNoteBeneficio;
	private BigDecimal mNumAnniSospensione;

	private String mCodDpr;
	private String mDescrDpr;

	private String mStringaReclusione;
	private BigDecimal mImportoMulta;
	private String mStringaArresto;
	private BigDecimal mImportoAmmenda;

	private String mStringaTitoloRef;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public DatiPrincipaliBeneficioCumuloModel() {
		this.mIdBeneficioCumulo = null;
		this.mCodNaturaBeneficio = "";
		this.mDescrNaturaBeneficio = "";
		this.mCodTipoBeneficio = "";
		this.mDescrTipoBeneficio = "";
		this.mNoteBeneficio = "";
		this.mNumAnniSospensione = null;
		this.mCodDpr = "";
		this.mDescrDpr = "";
		this.mStringaReclusione = "";
		this.mImportoMulta = null;
		this.mStringaArresto = "";
		this.mImportoAmmenda = null;
		this.mStringaTitoloRef = "";

	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public DatiPrincipaliBeneficioCumuloModel(DatiPrincipaliBeneficioCumuloModel aModel) {
		this.mIdBeneficioCumulo = aModel.mIdBeneficioCumulo;
		this.mCodNaturaBeneficio = aModel.mCodNaturaBeneficio;
		this.mDescrNaturaBeneficio = aModel.mDescrNaturaBeneficio;
		this.mCodTipoBeneficio = aModel.mCodTipoBeneficio;
		this.mDescrTipoBeneficio = aModel.mDescrTipoBeneficio;
		this.mNoteBeneficio = aModel.mNoteBeneficio;
		this.mNumAnniSospensione = aModel.mNumAnniSospensione;
		this.mCodDpr = aModel.mCodDpr;
		this.mDescrDpr = aModel.mDescrDpr;
		this.mStringaReclusione = aModel.mStringaReclusione;
		this.mImportoMulta = aModel.mImportoMulta;
		this.mStringaArresto = aModel.mStringaArresto;
		this.mImportoAmmenda = aModel.mImportoAmmenda;
		this.mStringaTitoloRef = aModel.mStringaTitoloRef;

	}

	/**
	 * Costruttore che inizializza il model estraendo i dati da un record BeneficioCumulo
	 * 
	 * @param aModel
	 */
	public DatiPrincipaliBeneficioCumuloModel(BeneficioCumuloModel aModel) {
		this.mIdBeneficioCumulo = aModel.getIdBeneficioCumulo();
		this.mCodNaturaBeneficio = aModel.getCodNaturaBeneficio();
		this.mDescrNaturaBeneficio = aModel.getDescrNaturaBeneficio();
		this.mCodTipoBeneficio = aModel.getCodTipoBeneficio();
		this.mDescrTipoBeneficio = aModel.getDescrTipoBeneficio();
		this.mNoteBeneficio = aModel.getNote();
		this.mNumAnniSospensione = aModel.getNumAnniSospensione();
		this.mCodDpr = aModel.getCodDpr();
		this.mDescrDpr = aModel.getDescrDpr();
		this.mStringaReclusione = aModel.getStringaReclusione();
		this.mImportoMulta = aModel.getImportoMulta();
		this.mStringaArresto = aModel.getStringaArresto();
		this.mImportoAmmenda = aModel.getImportoAmmenda();
	}
	
	/**
	 * Costruttore che inizializza il model estraendo i dati da un record ComputiCumulo
	 * 
	 * @param aModel
	 */
	public DatiPrincipaliBeneficioCumuloModel(ComputiCumuloModel aModel) {
		this.mIdBeneficioCumulo = null;
		this.mCodNaturaBeneficio = "C";
		this.mDescrNaturaBeneficio = "Concesso";
		this.mCodTipoBeneficio = null;
		this.mDescrTipoBeneficio = null;
		this.mNoteBeneficio = aModel.getNote();
		this.mNumAnniSospensione = null;
		this.mCodDpr = aModel.getCodDpr();
		this.mDescrDpr = aModel.getDescDpr();
		this.mStringaReclusione = aModel.getStringaReclusione();
		this.mImportoMulta = aModel.getImportoMulta();
		this.mStringaArresto = aModel.getStringaArresto();
		this.mImportoAmmenda = aModel.getImportoAmmenda();
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdBeneficioCumulo() {
		return mIdBeneficioCumulo;
	}

	public String getCodNaturaBeneficio() {
		return mCodNaturaBeneficio;
	}

	public String getDescrNaturaBeneficio() {
		return mDescrNaturaBeneficio;
	}

	public String getCodTipoBeneficio() {
		return mCodTipoBeneficio;
	}

	public String getDescrTipoBeneficio() {
		return mDescrTipoBeneficio;
	}

	public String getNoteBeneficio() {
		return mNoteBeneficio;
	}

	public BigDecimal getNumAnniSospensione() {
		return mNumAnniSospensione;
	}

	public String getCodDpr() {
		return mCodDpr;
	}

	public String getDescrDpr() {
		return mDescrDpr;
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public BigDecimal getImportoMulta() {
		return mImportoMulta;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	public BigDecimal getImportoAmmenda() {
		return mImportoAmmenda;
	}

	public String getStringaTitoloRef() {
		return mStringaTitoloRef;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================

	public void setIdBeneficioCumulo(BigDecimal aValore) {
		mIdBeneficioCumulo = aValore;
	}

	public void setCodNaturaBeneficio(String aValore) {
		mCodNaturaBeneficio = aValore;
	}

	public void setDescrNaturaBeneficio(String aValore) {
		mDescrNaturaBeneficio = aValore;
	}

	public void setCodTipoBeneficio(String aValore) {
		mCodTipoBeneficio = aValore;
	}

	public void setDescrTipoBeneficio(String aValore) {
		mDescrTipoBeneficio = aValore;
	}

	public void setNoteBeneficio(String aValore) {
		mNoteBeneficio = aValore;
	}

	public void setNumAnniSospensione(BigDecimal aValore) {
		mNumAnniSospensione = aValore;
	}

	public void setCodDpr(String aValore) {
		mCodDpr = aValore;
	}

	public void setDescrDpr(String aValore) {
		mDescrDpr = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setImportoMulta(BigDecimal aValore) {
		mImportoMulta = aValore;
	}

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setImportoAmmenda(BigDecimal aValore) {
		mImportoAmmenda = aValore;
	}

	public void setStringaTitoloRef(String aValore) {
		mStringaTitoloRef = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "TitoloCumulatoModel:\n" + "[ mIdTitoloCumulato  	= " + mIdBeneficioCumulo + " ]\n"
				+ "[ mCodNaturaBeneficio   = " + mCodNaturaBeneficio + " ]\n" + "[ mDescrNaturaBeneficio = "
				+ mDescrNaturaBeneficio + " ]\n" + "[ mCodTipoBeneficio     = " + mCodTipoBeneficio + " ]\n"
				+ "[ mDescrTipoBeneficio   = " + mDescrTipoBeneficio + " ]\n" + "[ mNoteBeneficio        = "
				+ mNoteBeneficio + " ]";

		return lStr;
	}

}