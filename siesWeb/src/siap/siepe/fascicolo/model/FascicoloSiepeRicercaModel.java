package siap.siepe.fascicolo.model;

/**
* <p>Title: FascicoloSiepeRicercaModel</p>
* <p>Description: Classe Model che rappresenta il FascicoloSiepe</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

public class FascicoloSiepeRicercaModel extends FascicoloSiepeModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6738788418788321345L;

	private String mDescrTipoUfficio;
	private String mDescrComuneUfficio;
	private String mCodTipoUfficio;
	private BigDecimal mChiaveAnnoIniziale;
	private BigDecimal mChiaveProgrIniziale;
	private BigDecimal mChiaveAnnoFinale;
	private BigDecimal mChiaveProgrFinale;
	private Date mDataIscrizioneIniziale;
	private Date mDataIscrizioneFinale;
	private BigDecimal mNumFascicoli;

	// COSTRUTTORE DI DEFAULT
	public FascicoloSiepeRicercaModel() {
		super();
		this.mDescrTipoUfficio = "";
		this.mDescrComuneUfficio = "";
		this.mCodTipoUfficio = "";
		this.mChiaveAnnoIniziale = null;
		this.mChiaveProgrIniziale = null;
		this.mChiaveAnnoFinale = null;
		this.mChiaveProgrFinale = null;
		this.mDataIscrizioneIniziale = null;
		this.mDataIscrizioneFinale = null;
		this.mNumFascicoli = null;
	}

	// COSTRUTTORE DI COPIA
	public FascicoloSiepeRicercaModel(FascicoloSiepeRicercaModel aModel) {
		super(aModel);
		this.mDescrTipoUfficio = aModel.mDescrTipoUfficio;
		this.mDescrComuneUfficio = aModel.mDescrComuneUfficio;
		this.mCodTipoUfficio = aModel.mCodTipoUfficio;
		this.mChiaveAnnoIniziale = aModel.mChiaveAnnoIniziale;
		this.mChiaveProgrIniziale = aModel.mChiaveProgrIniziale;
		this.mChiaveAnnoFinale = aModel.mChiaveAnnoFinale;
		this.mChiaveProgrFinale = aModel.mChiaveProgrFinale;
		this.mDataIscrizioneIniziale = aModel.mDataIscrizioneIniziale;
		this.mDataIscrizioneFinale = aModel.mDataIscrizioneFinale;
		this.mNumFascicoli = aModel.mNumFascicoli;
	}

	// COSTRUTTORE MODEL
	public FascicoloSiepeRicercaModel(FascicoloSiepeModel aFascicoloSiepeModel, String aDescrTipoUfficio,
			String aDescrComuneUfficio, String aCodTipoUfficio, BigDecimal aChiaveAnnoIniziale,
			BigDecimal aChiaveProgrIniziale, BigDecimal aChiaveAnnoFinale, BigDecimal aChiaveProgrFinale,
			Date aDataIscrizioneIniziale, Date aDataIscrizioneFinale, BigDecimal aNumFascicoli) {
		super(aFascicoloSiepeModel);
		this.mDescrTipoUfficio = aDescrTipoUfficio;
		this.mDescrComuneUfficio = aDescrComuneUfficio;
		this.mCodTipoUfficio = aCodTipoUfficio;
		this.mChiaveAnnoIniziale = aChiaveAnnoIniziale;
		this.mChiaveProgrIniziale = aChiaveProgrIniziale;
		this.mChiaveAnnoFinale = aChiaveAnnoFinale;
		this.mChiaveProgrFinale = aChiaveProgrFinale;
		this.mDataIscrizioneIniziale = aDataIscrizioneIniziale;
		this.mDataIscrizioneFinale = aDataIscrizioneFinale;
		this.mNumFascicoli = aNumFascicoli;
	}

	//
	// METODI GET()
	//

	public String getDescrTipoUfficio() {
		return mDescrTipoUfficio;
	}

	public String getDescrComuneUfficio() {
		return mDescrComuneUfficio;
	}

	public String getCodTipoUfficio() {
		return mCodTipoUfficio;
	}

	public BigDecimal getChiaveAnnoIniziale() {
		return mChiaveAnnoIniziale;
	}

	public BigDecimal getChiaveProgrIniziale() {
		return mChiaveProgrIniziale;
	}

	public BigDecimal getChiaveAnnoFinale() {
		return mChiaveAnnoFinale;
	}

	public BigDecimal getChiaveProgrFinale() {
		return mChiaveProgrFinale;
	}

	public Date getDataIscrizioneIniziale() {
		return mDataIscrizioneIniziale;
	}

	public Date getDataIscrizioneFinale() {
		return mDataIscrizioneFinale;
	}

	public BigDecimal getNumFascicoli() {
		return mNumFascicoli;
	}

	//
	// METODI SET()
	//

	public void setDescrTipoUfficio(String aValore) {
		mDescrTipoUfficio = aValore;
	}

	public void setDescrComuneUfficio(String aValore) {
		mDescrComuneUfficio = aValore;
	}

	public void setCodTipoUfficio(String aValore) {
		mCodTipoUfficio = aValore;
	}

	public void setChiaveAnnoIniziale(BigDecimal aValore) {
		mChiaveAnnoIniziale = aValore;
	}

	public void setChiaveProgrIniziale(BigDecimal aValore) {
		mChiaveProgrIniziale = aValore;
	}

	public void setChiaveAnnoFinale(BigDecimal aValore) {
		mChiaveAnnoFinale = aValore;
	}

	public void setChiaveProgrFinale(BigDecimal aValore) {
		mChiaveProgrFinale = aValore;
	}

	public void setDataIscrizioneIniziale(Date aValore) {
		mDataIscrizioneIniziale = aValore;
	}

	public void setDataIscrizioneFinale(Date aValore) {
		mDataIscrizioneFinale = aValore;
	}

	public void setNumFascicoli(BigDecimal aValore) {
		mNumFascicoli = aValore;
	}

}