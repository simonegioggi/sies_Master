package siap.siep.scadenzario.model;

import java.math.BigDecimal;
import java.util.Date;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ScadenzarioModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il Scadenzario
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ScadenzarioModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7176868976250477604L;

	private BigDecimal mIdScadenzario;
	private String mCodTipoScadenzario;
	private String mDescrTipoScadenzario;
	private Date mDataInizioScadenza;
	private Date mDataFineScadenza;
	private String mFlagVisto;
	private Date mDataVisto;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mGiorniResidui;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private String mTipoRic;
	private BigDecimal mNotIdNotifica;
	private BigDecimal mEveIdEvento;
	private String mCodStatoNotifica;
	private String mDescrStatoNotifica;
	private FascicoloSiepModel mFascMod;
	private Date mDataIrr;
	private BigDecimal mChiaveProgrIniziale;
	private BigDecimal mChiaveAnnoIniziale;
	private BigDecimal mChiaveProgrFinale;
	private BigDecimal mChiaveAnnoFinale;
	private Date mDataEmissioneIniziale;
	private Date mDataEmissioneFinale;
	private String[] mCodiciStatoNotifica;
	private String mTitoloReport;
	// 27/03/2015
	private FascMsToFascSiepModel mFascMsToFascSiepMod;
	// MEV_39 (mapping per il nuovo campo RIF_FASC_SIEP_ORIG in tabella SCADENZARIO_SIEP)
	private BigDecimal mIdFascicoloSiepOrigine;
	// aggiunto campo per descrivere il tipo di MS
	private String mDescrTipoMS;
	// aggiunto campo per la data di scadenza comunicazione
	private Date mDataScadenzaComunicazione;

	// MEV_2023-33
	private BollettinoPagopaModel mBollettinoModel = null;
	
	// MEV-2026_1
	private Date mDataFinePenaVirtuale;
	
	// COSTRUTTORE DI DEFAULT
	public ScadenzarioModel() {

		this.mIdScadenzario = null;
		this.mCodTipoScadenzario = "";
		this.mDescrTipoScadenzario = "";
		this.mDataInizioScadenza = null;
		this.mDataFineScadenza = null;
		this.mFlagVisto = "";
		this.mDataVisto = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mGiorniResidui = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mNumAnni = null;
		this.mNumGiorni = null;
		this.mNumMesi = null;
		this.mTipoRic = "";
		this.mNotIdNotifica = null;
		this.mEveIdEvento = null;
		this.mCodStatoNotifica = "N";
		this.mDescrStatoNotifica = null;
		this.mFascMod = null;
		this.mDataIrr = null;
		this.mChiaveProgrIniziale = null;
		this.mChiaveAnnoIniziale = null;
		this.mChiaveProgrFinale = null;
		this.mChiaveAnnoFinale = null;
		this.mDataEmissioneIniziale = null;
		this.mDataEmissioneFinale = null;
		this.mCodiciStatoNotifica = null;
		this.mTitoloReport = null;
		this.mFascMsToFascSiepMod = null;
		// MEV_39
		this.mIdFascicoloSiepOrigine = null;
		this.mDescrTipoMS = null;
		this.mDataScadenzaComunicazione = null;
		// MEV_2023-33
		this.mBollettinoModel = null;		
		// MEV-2026_1
		this.mDataFinePenaVirtuale = null;
	}

	// COSTRUTTORE DI COPIA
	public ScadenzarioModel(ScadenzarioModel aModel) {

		this.mIdScadenzario = aModel.mIdScadenzario;
		this.mCodTipoScadenzario = aModel.mCodTipoScadenzario;
		this.mDescrTipoScadenzario = aModel.mDescrTipoScadenzario;
		this.mDataInizioScadenza = aModel.mDataInizioScadenza;
		this.mDataFineScadenza = aModel.mDataFineScadenza;
		this.mFlagVisto = aModel.mFlagVisto;
		this.mDataVisto = aModel.mDataVisto;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mGiorniResidui = aModel.mGiorniResidui;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mFascMod = aModel.mFascMod;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mNumMesi = aModel.mNumMesi;
		this.mTipoRic = aModel.mTipoRic;
		this.mNotIdNotifica = aModel.mNotIdNotifica;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mCodStatoNotifica = aModel.mCodStatoNotifica;
		this.mDescrStatoNotifica = aModel.mDescrStatoNotifica;
		this.mFascMod = aModel.mFascMod;
		this.mDataIrr = aModel.mDataIrr;
		this.mChiaveProgrIniziale = aModel.mChiaveProgrIniziale;
		this.mChiaveAnnoIniziale = aModel.mChiaveAnnoIniziale;
		this.mChiaveProgrFinale = aModel.mChiaveProgrFinale;
		this.mChiaveAnnoFinale = aModel.mChiaveAnnoFinale;
		this.mDataEmissioneIniziale = aModel.mDataEmissioneIniziale;
		this.mDataEmissioneFinale = aModel.mDataEmissioneFinale;
		this.mCodiciStatoNotifica = aModel.mCodiciStatoNotifica;
		this.mTitoloReport = aModel.mTitoloReport;
		this.mFascMsToFascSiepMod = aModel.mFascMsToFascSiepMod;
		// MEV_39
		this.mIdFascicoloSiepOrigine = aModel.mIdFascicoloSiepOrigine;
		this.mDescrTipoMS = aModel.mDescrTipoMS;
		this.mDataScadenzaComunicazione = aModel.mDataScadenzaComunicazione;
        // MEV_2023-33
        this.mBollettinoModel = aModel.mBollettinoModel;
        // MEV-2026_1
        this.mDataFinePenaVirtuale = aModel.mDataFinePenaVirtuale;
    }

	// COSTRUTTORE MODEL
	public ScadenzarioModel(BigDecimal aIdScadenzario, String aCodTipoScadenzario,
			String aDescrTipoScadenzario, Date aDataInizioScadenza, Date aDataFineScadenza, String aFlagVisto,
			Date aDataVisto, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, BigDecimal aGiorniResidui,
			String aDescrUfficioAggiornamento, BigDecimal aFasSieIdFascicoloSiep, FascicoloSiepModel aFascMod,
			Date aDataIrr, BigDecimal aNumAnni, BigDecimal aNumGiorni, BigDecimal aNumMesi, String aTipoRic,
			BigDecimal aNotIdNotifica, BigDecimal aEveIdEvento, String aCodStatoNotifica,
			FascMsToFascSiepModel aFascMsToFascSiepMod,
			// MEV_39
			BigDecimal aIdFascicoloSiepOrigine, String aDescrTipoMS, Date aDataScadenzaComunicazione
		  // MEV_2023-33
			, BollettinoPagopaModel aBollettinoModel
	        // MEV-2026_1
	        , Date aDataFinePenaVirtuale
	    ) {

		this.mIdScadenzario = aIdScadenzario;
		this.mCodTipoScadenzario = aCodTipoScadenzario;
		this.mDescrTipoScadenzario = aDescrTipoScadenzario;
		this.mDataInizioScadenza = aDataInizioScadenza;
		this.mDataFineScadenza = aDataFineScadenza;
		this.mFlagVisto = aFlagVisto;
		this.mDataVisto = aDataVisto;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mGiorniResidui = aGiorniResidui;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mNumAnni = aNumAnni;
		this.mNumGiorni = aNumGiorni;
		this.mNumMesi = aNumMesi;
		this.mTipoRic = aTipoRic;
		this.mNotIdNotifica = aNotIdNotifica;
		this.mEveIdEvento = aEveIdEvento;
		this.mCodStatoNotifica = aCodStatoNotifica;
		this.mDescrStatoNotifica = null;
		this.mFascMod = aFascMod;
		this.mDataIrr = aDataIrr;
		this.mChiaveProgrIniziale = null;
		this.mChiaveAnnoIniziale = null;
		this.mChiaveProgrFinale = null;
		this.mChiaveAnnoFinale = null;
		this.mDataEmissioneIniziale = null;
		this.mDataEmissioneFinale = null;
		this.mCodiciStatoNotifica = null;
		this.mTitoloReport = null;
		this.mFascMsToFascSiepMod = aFascMsToFascSiepMod;
		// mev_39
		this.mIdFascicoloSiepOrigine = aIdFascicoloSiepOrigine;
		this.mDescrTipoMS = aDescrTipoMS;
		this.mDataScadenzaComunicazione = aDataScadenzaComunicazione;
		// MEV_2023-33
		this.mBollettinoModel = aBollettinoModel;
        // MEV-2026_1
		this.mDataFinePenaVirtuale = aDataFinePenaVirtuale;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdScadenzario() {
		return mIdScadenzario;
	}

	public String getCodTipoScadenzario() {
		return mCodTipoScadenzario;
	}

	public String getDescrTipoScadenzario() {
		return mDescrTipoScadenzario;
	}

	public Date getDataInizioScadenza() {
		return mDataInizioScadenza;
	}

	public Date getDataFineScadenza() {
		return mDataFineScadenza;
	}

	public String getFlagVisto() {
		return mFlagVisto;
	}

	public Date getDataVisto() {
		return mDataVisto;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public BigDecimal getGiorniResidui() {
		return mGiorniResidui;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getNumAnni() {
		return mNumAnni;
	}

	public BigDecimal getNumMesi() {
		return mNumMesi;
	}

	public BigDecimal getNumGiorni() {
		return mNumGiorni;
	}

	public String getTipoRic() {
		return mTipoRic;
	}

	public BigDecimal getNotIdNotifica() {
		return mNotIdNotifica;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getCodStatoNotifica() {
		return mCodStatoNotifica;
	}

	public String getDescrStatoNotifica() {

		if ("N".equals(mCodStatoNotifica)) {
			return "Attesa notifica";
		} else if ("A".equals(mCodStatoNotifica)) {
			return "Attesa notifica avvocati";
		} else if ("M".equals(mCodStatoNotifica)) {
			return "Mancata notifica";
		} else if ("O".equals(mCodStatoNotifica)) {
			return "Sollecito";
		} else if ("I".equals(mCodStatoNotifica)) {
			return "Richiesta informazioni 8 bis";
		} else if ("R".equals(mCodStatoNotifica)) {
			return "Rinnovazione notifica 8 bis";
		}
		// AMBROSINO a6-rr-238
		else if ("PP".equals(mCodStatoNotifica)) {
			return "Pervenuto";
		} else if ("PR".equals(mCodStatoNotifica)) {
			return "Pervenuto Rinnovo";
		} else if ("NP".equals(mCodStatoNotifica)) {
			return "Non Pervenuto";
		} else if ("NS".equals(mCodStatoNotifica)) {
			return "Sollecito Non Pervenuto";
		}
		return "";
	}

	public FascicoloSiepModel getFascicoloModel() {
		return mFascMod;
	}

	public Date getDataIrr() {
		return mDataIrr;
	}

	public BigDecimal getChiaveProgrIniziale() {
		return mChiaveProgrIniziale;
	}

	public BigDecimal getChiaveAnnoIniziale() {
		return mChiaveAnnoIniziale;
	}

	public BigDecimal getChiaveProgrFinale() {
		return mChiaveProgrFinale;
	}

	public BigDecimal getChiaveAnnoFinale() {
		return mChiaveAnnoFinale;
	}

	public Date getDataEmissioneIniziale() {
		return mDataEmissioneIniziale;
	}

	public Date getDataEmissioneFinale() {
		return mDataEmissioneFinale;
	}

	public String[] getCodiciStatoNotifica() {
		return mCodiciStatoNotifica;
	}

	public String getTitoloReport() {

		if ("S".equals(mCodStatoNotifica)) {
			return "Consultazione Scadenzario L.165/98";
		} else {
			return "Decreti in corso di Definzione";
		}
	}

	public FascMsToFascSiepModel getFascMsToFascSiep() {
		return mFascMsToFascSiepMod;
	}

	// MEV_39
	public BigDecimal getIdFascicoloSiepOrigine() {
		return mIdFascicoloSiepOrigine;
	}

	public String getDescrTipoMS() {
		return mDescrTipoMS;
	}

	public Date getDataScadenzaComunicazione() {
		return mDataScadenzaComunicazione;
	}
	
	//MEV_2023-33
	public BollettinoPagopaModel getBollettinoModel(){
	  return mBollettinoModel;
	}
	
	// MEV-2026_1
    public Date getDataFinePenaVirtuale() {
        return mDataFinePenaVirtuale;
    }	
	
	//
	// METODI SET()
	//

	public void setIdScadenzario(BigDecimal aValore) {
		mIdScadenzario = aValore;
	}

	public void setCodTipoScadenzario(String aValore) {
		mCodTipoScadenzario = aValore;
	}

	public void setDescrTipoScadenzario(String aValore) {
		mDescrTipoScadenzario = aValore;
	}

	public void setDataInizioScadenza(Date aValore) {
		mDataInizioScadenza = aValore;
	}

	public void setDataFineScadenza(Date aValore) {
		mDataFineScadenza = aValore;
	}

	public void setFlagVisto(String aValore) {
		mFlagVisto = aValore;
	}

	public void setDataVisto(Date aValore) {
		mDataVisto = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setGiorniResidui(BigDecimal aValore) {
		mGiorniResidui = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setNumAnni(BigDecimal aValore) {
		mNumAnni = aValore;
	}

	public void setNumMesi(BigDecimal aValore) {
		mNumMesi = aValore;
	}

	public void setNumGiorni(BigDecimal aValore) {
		mNumGiorni = aValore;
	}

	public void setTipoRic(String aValore) {
		mTipoRic = aValore;
	}

	public void setNotIdNotifica(BigDecimal aValore) {
		mNotIdNotifica = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setCodStatoNotifica(String aValore) {
		mCodStatoNotifica = aValore;
	}

	public void setDescrStatoNotifica(String aValore) {
		mDescrStatoNotifica = aValore;
	}

	public void setFascicoloModel(FascicoloSiepModel aValore) {
		mFascMod = aValore;
	}

	public void setDataIrr(Date aValore) {
		mDataIrr = aValore;
	}

	public void setChiaveProgrIniziale(BigDecimal aValore) {
		mChiaveProgrIniziale = aValore;
	}

	public void setChiaveAnnoIniziale(BigDecimal aValore) {
		mChiaveAnnoIniziale = aValore;
	}

	public void setChiaveProgrFinale(BigDecimal aValore) {
		mChiaveProgrFinale = aValore;
	}

	public void setChiaveAnnoFinale(BigDecimal aValore) {
		mChiaveAnnoFinale = aValore;
	}

	public void setDataEmissioneIniziale(Date aValore) {
		mDataEmissioneIniziale = aValore;
	}

	public void setDataEmissioneFinale(Date aValore) {
		mDataEmissioneFinale = aValore;
	}

	public void setCodiciStatoNotifica(String[] aValore) {
		mCodiciStatoNotifica = aValore;
	}

	public void setTitoloReport(String aValore) {
		mTitoloReport = aValore;
	}

	public void setFasMsToFascSiep(FascMsToFascSiepModel aValore) {
		mFascMsToFascSiepMod = aValore;
	}

	// MEV_39
	public void setIdFascicoloSiepOrigine(BigDecimal aValore) {
		mIdFascicoloSiepOrigine = aValore;
	}

	public void setDescrTipoMS(String aValore) {
		mDescrTipoMS = aValore;
	}

	public void setDataScadenzaComunicazione(Date aValore) {
		mDataScadenzaComunicazione = aValore;
	}
	
	 //MEV_2023-33
  public void setBollettinoModel (BollettinoPagopaModel aValore){
    mBollettinoModel = aValore;
  }
    // MEV-2026_1
    public void setDataFinePenaVirtuale(Date aValore) {
      mDataFinePenaVirtuale = aValore;
    }
	
	public String toString() {

		String lStr = new String();
		lStr = "" + mIdScadenzario + " - " + mCodTipoScadenzario + " - " + mDescrTipoScadenzario + " - "
				+ mDataInizioScadenza + " - " + mDataFineScadenza + " - " + mFlagVisto + " - " + mDataVisto
				+ " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento
				+ " - " + mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - "
				+ mDataAggiornamento + " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento
				+ " - " + mGiorniResidui + " - " + mFasSieIdFascicoloSiep + " - " + mNumAnni + " - "
				+ mNumMesi + " - " + mNumGiorni + " - " + mTipoRic + " - " + mNotIdNotifica + " - "
				+ mEveIdEvento + " - " + mCodStatoNotifica + " - " + mDescrStatoNotifica + " - " +
				// AMBROSINO 10-02-2011 - aggiunti campi
				mDataEmissioneIniziale + " - " + mDataEmissioneFinale + " - " + mFascMod + " - " + mDataIrr
				+ " - " + mChiaveProgrIniziale + " - " + mChiaveAnnoIniziale + " - " + mChiaveProgrFinale
				+ " - " + mChiaveAnnoFinale + " - " + mCodiciStatoNotifica + " - " + mFascMsToFascSiepMod
				// MEV_39
				+ " - " + mIdFascicoloSiepOrigine + " - " + mTitoloReport + " - " + mDescrTipoMS
				+ mDataScadenzaComunicazione;

		return lStr;
	}

}