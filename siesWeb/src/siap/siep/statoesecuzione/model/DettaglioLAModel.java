package siap.siep.statoesecuzione.model;

import java.math.BigDecimal;

import f3b.model.GenericModel;

/**
 * Model creato per contenere i dati delle LA concesse sul singolo Evento da visualizzare nello stato di
 * esecuzione. Il model contiene i dati aggregati per tipologia LA sul singolo evento. Se sul singolo evento
 * sono stati concesse LA di natura differente (LA,LS,...) dovrà essere utilizzato un model per ogni
 * Tipologia.
 * 
 * Il model viene alimentato da StatoEsecuzioneLA
 * 
 * @author d.fiorletta
 *
 */
public class DettaglioLAModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3635992363880140736L;

	public static final String LA_ORDINARIA = "LA";
	public static final String LA_SPECIALE = "LS";
	public static final String LA_INTEGRAZIONE = "LI";
	public static final String LA_RIGETTATI = "R";
	public static final String LA_INAMMISSIBILI = "I";
	public static final String LA_NONLUOGO = "NLP";
	public static final String RD_RISARCIMENTO = "RD";
	public static final String RD_SOMMALIQUIDATA = "SL";

	private String mTipoLA = null; // LA, LS, LI, I, R.. Vedi costanti
	private String mDescTipoLA = null;
	private BigDecimal mTotGiorni = null;
	private BigDecimal mSommaLiquidata = null;
	private String mDescrPeriodi = null;

	public String getTipoLA() {
		return mTipoLA;
	}

	public String getDescTipoLA() {
		return mDescTipoLA;
	}

	public BigDecimal getTotGiorni() {
		return mTotGiorni;
	}

	public BigDecimal getSommaLiquidata() {
		return mSommaLiquidata;
	}

	public String getDescrPeriodi() {
		return mDescrPeriodi;
	}

	public void setTipoLA(String aValore) {
		this.mTipoLA = aValore;
	}

	public void setDescTipoLA(String aValore) {
		this.mDescTipoLA = aValore;
	}

	public void setTotGiorni(BigDecimal aValore) {
		this.mTotGiorni = aValore;
	}

	public void setSommaLiquidata(BigDecimal aValore) {
		this.mSommaLiquidata = aValore;
	}

	public void setDescrPeriodi(String aValore) {
		this.mDescrPeriodi = aValore;
	}

	public String toString() {
		String lStr = "";
		lStr = "DettaglioLAModel:\n" + "[ mTipoLA         = " + mTipoLA + " ]\n" + "[ mDescTipoLA     = "
				+ mDescTipoLA + " ]\n" + "[ mTotGiorni      = " + mTotGiorni + " ]\n" + "[ mSommaLiquidata = "
				+ mSommaLiquidata + " ]\n" + "[ mDescrPeriodi   = " + mDescrPeriodi + " ]";

		return lStr;
	}

}