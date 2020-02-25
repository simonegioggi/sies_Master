package siap.sius.fascicolo.model;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.soggetto.model.SoggettoModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: FascicoloSiusModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il FascicoloSius
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
public class FascicoloSiusModel extends GenericModel {

	/**
	 * MEV_65: aggiunto serialVersionUID
	 */
	private static final long serialVersionUID = -7186431380390596766L;

	private BigDecimal mIdFascicoloSius;
	private BigDecimal mChiaveAnno;
	private BigDecimal mChiaveAnnoS22;
	private BigDecimal mChiaveAnnoIniziale;
	private BigDecimal mChiaveAnnoFinale;

	private String mChiaveUfficio;
	private String mDescrTipoUfficio;
	private String mDescrComuneUfficio;
	private String mCodTipoUfficio;
	private BigDecimal mChiaveProgr;
	private BigDecimal mChiaveProgrS22;
	private BigDecimal mChiaveProgrIniziale;
	private BigDecimal mChiaveProgrFinale;

	private String mCodStatoFascicolo;
	private String mDescrStatoFascicolo;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private Date mDataInserimento;
	private Date mDataIscrizione;
	private String mCodOperatoreAggiornamento;
	private String mCodUfficioAggiornamento;
	private Date mDataAggiornamento;
	private BigDecimal mSogIdSoggetto;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mChiaveAnnoSIEP;
	private BigDecimal mChiaveProgrSIEP;
	private String mChiaveUfficioSIEP;
	private BigDecimal mFasSiuIdFascicoloSius;
	private BigDecimal mIdFascicoloSiusOrigine; // 15/01/2004.
	private Date mDataDefinizione;

	private SoggettoModel mSoggetto;
	private BigDecimal mNumFascicoli;
	private BigDecimal mNumeroFascicoliUnificati; // 29/04/2004

	private Date mDataIscrizioneIniziale; // 10/06/2004
	private Date mDataIscrizioneFinale; // 10/06/2004
	private Date mDataArrivoIniziale; // 16/06/2004
	private Date mDataArrivoFinale; // 16/06/2004
	private Date mDataAttoIniziale; // 16/06/2004
	private Date mDataAttoFinale; // 16/06/2004
	private Date mDataFinePendenza;
	private Date mDataDefinizioneIniziale;
	private Date mDataDefinizioneFinale;
	private Date mDataInizioPosizioneMateriale;

	// MEV10-s3: aggiunta proprietà per gestire etichetta minorenne/maggiorenne
	private String mVisibilitaMinorenne;

	// MEV_65: aggiunta variabile
	private String mCodCancelleria;

	// COSTRUTTORE DI DEFAULT
	public FascicoloSiusModel() {
		mIdFascicoloSius = null;
		mChiaveAnno = null;
		mChiaveAnnoS22 = null;
		mChiaveAnnoIniziale = null;
		mChiaveAnnoFinale = null;
		mChiaveUfficio = "";
		mDescrTipoUfficio = "";
		mDescrComuneUfficio = "";
		mCodTipoUfficio = "";
		mChiaveProgr = null;
		mChiaveProgrS22 = null;
		mChiaveProgrIniziale = null;
		mChiaveProgrFinale = null;
		mCodStatoFascicolo = "";
		mDescrStatoFascicolo = "";
		mCodOperatoreInserimento = "";
		mCodUfficioInserimento = "";
		mDataInserimento = null;
		mDataIscrizione = null;
		mCodOperatoreAggiornamento = "";
		mCodUfficioAggiornamento = "";
		mDataAggiornamento = null;
		mSogIdSoggetto = null;
		mFasSieIdFascicoloSiep = null;
		mChiaveAnnoSIEP = null;
		mChiaveProgrSIEP = null;
		mChiaveUfficioSIEP = "";
		mFasSiuIdFascicoloSius = null;
		mIdFascicoloSiusOrigine = null; // 15/01/2004.
		mDataDefinizione = null;
		mSoggetto = null;
		mNumFascicoli = null;
		mNumeroFascicoliUnificati = new BigDecimal(0); // 29/04/2004
		mDataIscrizioneIniziale = null; // 10/06/2004
		mDataIscrizioneFinale = null; // 10/06/2004
		mDataArrivoIniziale = null; // 16/06/2004
		mDataArrivoFinale = null; // 16/06/2004
		mDataAttoIniziale = null; // 16/06/2004
		mDataAttoFinale = null; // 16/06/2004
		mDataFinePendenza = null;
		mDataDefinizioneIniziale = null;
		mDataDefinizioneFinale = null;
		mDataInizioPosizioneMateriale = null;
		mVisibilitaMinorenne = "";
		mCodCancelleria = "";
	}

	// COSTRUTTORE DI COPIA
	public FascicoloSiusModel(FascicoloSiusModel aModel) {
		mIdFascicoloSius = aModel.mIdFascicoloSius;
		mChiaveAnno = aModel.mChiaveAnno;
		mChiaveAnnoS22 = aModel.mChiaveAnnoS22;
		mChiaveAnnoIniziale = aModel.mChiaveAnnoIniziale;
		mChiaveAnnoFinale = aModel.mChiaveAnnoFinale;
		mChiaveUfficio = aModel.mChiaveUfficio;
		mDescrTipoUfficio = aModel.mDescrTipoUfficio;
		mDescrComuneUfficio = aModel.mDescrComuneUfficio;
		mCodTipoUfficio = aModel.mCodTipoUfficio;
		mChiaveProgr = aModel.mChiaveProgr;
		mChiaveProgrS22 = aModel.mChiaveProgrS22;
		mChiaveProgrIniziale = aModel.mChiaveProgrIniziale;
		mChiaveProgrFinale = aModel.mChiaveProgrFinale;
		mCodStatoFascicolo = aModel.mCodStatoFascicolo;
		mDescrStatoFascicolo = aModel.mDescrStatoFascicolo;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mDataIscrizione = aModel.mDataIscrizione;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mSogIdSoggetto = aModel.mSogIdSoggetto;
		mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		mChiaveAnnoSIEP = aModel.mChiaveAnnoSIEP;
		mChiaveProgrSIEP = aModel.mChiaveProgrSIEP;
		mChiaveUfficioSIEP = aModel.mChiaveUfficioSIEP;
		mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		mIdFascicoloSiusOrigine = aModel.mIdFascicoloSiusOrigine;
		mDataDefinizione = aModel.mDataDefinizione;
		mSoggetto = aModel.mSoggetto;
		mNumFascicoli = aModel.mNumFascicoli;
		mNumeroFascicoliUnificati = aModel.mNumeroFascicoliUnificati; // 29/04/2004
		mDataIscrizioneIniziale = aModel.mDataIscrizioneIniziale; // 10/06/2004
		mDataIscrizioneFinale = aModel.mDataIscrizioneFinale; // 10/06/2004
		mDataArrivoIniziale = aModel.mDataArrivoIniziale; // 16/06/2004
		mDataArrivoFinale = aModel.mDataArrivoFinale; // 16/06/2004
		mDataAttoIniziale = aModel.mDataAttoIniziale; // 16/06/2004
		mDataAttoFinale = aModel.mDataAttoFinale; // 16/06/2004
		mDataInizioPosizioneMateriale = aModel.mDataInizioPosizioneMateriale;
		mDataFinePendenza = aModel.mDataFinePendenza;
		mDataDefinizioneIniziale = aModel.mDataDefinizioneIniziale;
		mDataDefinizioneFinale = aModel.mDataDefinizioneFinale;
		mVisibilitaMinorenne = aModel.mVisibilitaMinorenne;
		mCodCancelleria = aModel.mCodCancelleria;
	}

	// COSTRUTTORE MODEL
	public FascicoloSiusModel(BigDecimal aIdFascicoloSius, BigDecimal aChiaveAnno, BigDecimal aChiaveAnnoS22,
			BigDecimal aChiaveAnnoIniziale, BigDecimal aChiaveAnnoFinale, String aChiaveUfficio,
			String aDescrTipoUfficio, String aDescrComuneUfficio, String aCodTipoUfficio,
			BigDecimal aChiaveProgr, BigDecimal aChiaveProgrS22, BigDecimal aChiaveProgrIniziale,
			BigDecimal aChiaveProgrFinale, String aCodStatoFascicolo, String aDescrStatoFascicolo,
			String aCodOperatoreInserimento, String aCodUfficioInserimento, Date aDataInserimento,
			Date aDataIscrizione, String aCodOperatoreAggiornamento, String aCodUfficioAggiornamento,
			Date aDataAggiornamento, BigDecimal aSogIdSoggetto, BigDecimal aFasSieIdFascicoloSiep,
			BigDecimal aChiaveAnnoSIEP, BigDecimal aChiaveProgrSIEP, String aChiaveUfficioSIEP,
			BigDecimal aFasSiuIdFascicoloSius, BigDecimal aIdFascicoloSiusOrigine, Date aDataDefinizione,
			SoggettoModel aSoggetto, BigDecimal aNumFascicoli, BigDecimal aNumeroFascicoliUnificati, // 29/04/2004
			String aVisibilitaMinorenne, String aCodCancelleria) {
		mIdFascicoloSius = aIdFascicoloSius;
		mChiaveAnno = aChiaveAnno;
		mChiaveAnnoS22 = aChiaveAnnoS22;
		mChiaveAnnoIniziale = aChiaveAnnoIniziale;
		mChiaveAnnoFinale = aChiaveAnnoFinale;
		mChiaveUfficio = aChiaveUfficio;
		mDescrTipoUfficio = aDescrTipoUfficio;
		mDescrComuneUfficio = aDescrComuneUfficio;
		mCodTipoUfficio = aCodTipoUfficio;
		mChiaveProgr = aChiaveProgr;
		mChiaveProgrS22 = aChiaveProgrS22;
		mChiaveProgrIniziale = aChiaveProgrIniziale;
		mChiaveProgrFinale = aChiaveProgrFinale;
		mCodStatoFascicolo = aCodStatoFascicolo;
		mDescrStatoFascicolo = aDescrStatoFascicolo;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDataInserimento = aDataInserimento;
		mDataIscrizione = aDataIscrizione;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mSogIdSoggetto = aSogIdSoggetto;
		mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		mChiaveAnnoSIEP = aChiaveAnnoSIEP;
		mChiaveProgrSIEP = aChiaveProgrSIEP;
		mChiaveUfficioSIEP = aChiaveUfficioSIEP;
		mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		mIdFascicoloSiusOrigine = aIdFascicoloSiusOrigine;
		mDataDefinizione = aDataDefinizione;
		mSoggetto = null;
		mNumFascicoli = aNumFascicoli;
		mNumeroFascicoliUnificati = aNumeroFascicoliUnificati; // 29/04/2004
		mDataIscrizioneIniziale = null; // 10/06/2004
		mDataIscrizioneFinale = null; // 10/06/2004
		mDataArrivoIniziale = null; // 16/06/2004
		mDataArrivoFinale = null; // 16/06/2004
		mDataAttoIniziale = null; // 16/06/2004
		mDataAttoFinale = null; // 16/06/2004
		mDataInizioPosizioneMateriale = null;
		mDataFinePendenza = null;
		mDataDefinizioneIniziale = null;
		mDataDefinizioneFinale = null;
		mVisibilitaMinorenne = aVisibilitaMinorenne;
		mCodCancelleria = aCodCancelleria;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdFascicoloSius() {
		return mIdFascicoloSius;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveAnnoS22() {
		return mChiaveAnnoS22;
	}

	public BigDecimal getChiaveAnnoIniziale() {
		return mChiaveAnnoIniziale;
	}

	public BigDecimal getChiaveAnnoFinale() {
		return mChiaveAnnoFinale;
	}

	public String getChiaveUfficio() {
		return mChiaveUfficio;
	}

	public String getDescrTipoUfficio() {
		return mDescrTipoUfficio;
	}

	public String getDescrComuneUfficio() {
		return mDescrComuneUfficio;
	}

	public String getCodTipoUfficio() {
		return mCodTipoUfficio;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public BigDecimal getChiaveProgrS22() {
		return mChiaveProgrS22;
	}

	public BigDecimal getChiaveProgrIniziale() {
		return mChiaveProgrIniziale;
	}

	public BigDecimal getChiaveProgrFinale() {
		return mChiaveProgrFinale;
	}

	public String getCodStatoFascicolo() {
		return mCodStatoFascicolo;
	}

	public String getDescrStatoFascicolo() {
		return mDescrStatoFascicolo;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public Date getDataIscrizione() {
		return mDataIscrizione;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getChiaveAnnoSIEP() {
		return mChiaveAnnoSIEP;
	}

	public BigDecimal getChiaveProgrSIEP() {
		return mChiaveProgrSIEP;
	}

	public String getChiaveUfficioSIEP() {
		return mChiaveUfficioSIEP;
	}

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public BigDecimal getIdFascicoloSiusOrigine() {
		return mIdFascicoloSiusOrigine;
	}

	public Date getDataDefinizione() {
		return mDataDefinizione;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public BigDecimal getNumFascicoli() {
		return mNumFascicoli;
	}

	public BigDecimal getNumeroFascicoliUnificati() {
		return mNumeroFascicoliUnificati;
	} // 20/04/2004

	public Date getDataIscrizioneIniziale() {
		return mDataIscrizioneIniziale;
	} // 10/06/2004

	public Date getDataIscrizioneFinale() {
		return mDataIscrizioneFinale;
	} // 10/06/2004

	public Date getDataArrivoIniziale() {
		return mDataArrivoIniziale;
	} // 16/06/2004

	public Date getDataArrivoFinale() {
		return mDataArrivoFinale;
	} // 16/06/2004

	public Date getDataAttoIniziale() {
		return mDataAttoIniziale;
	} // 16/06/2004

	public Date getDataAttoFinale() {
		return mDataAttoFinale;
	} // 16/06/2004

	public Date getDataInizioPosizioneMateriale() {
		return mDataInizioPosizioneMateriale;
	}

	public Date getDataFinePendenza() {
		return mDataFinePendenza;
	}

	public Date getDataDefinizioneIniziale() {
		return mDataDefinizioneIniziale;
	}

	public Date getDataDefinizioneFinale() {
		return mDataDefinizioneFinale;
	}

	public String getVisibilitaMinorenne() {
		return mVisibilitaMinorenne;
	}

	public String getCodCancelleria() {
		return mCodCancelleria;
	}

	//
	// METODI SET()
	//

	public void setIdFascicoloSius(BigDecimal aValore) {
		mIdFascicoloSius = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveAnnoS22(BigDecimal aValore) {
		mChiaveAnnoS22 = aValore;
	}

	public void setChiaveAnnoIniziale(BigDecimal aValore) {
		mChiaveAnnoIniziale = aValore;
	}

	public void setChiaveAnnoFinale(BigDecimal aValore) {
		mChiaveAnnoFinale = aValore;
	}

	public void setChiaveUfficio(String aValore) {
		mChiaveUfficio = aValore;
	}

	public void setDescrTipoUfficio(String aValore) {
		mDescrTipoUfficio = aValore;
	}

	public void setDescrComuneUfficio(String aValore) {
		mDescrComuneUfficio = aValore;
	}

	public void setCodTipoUfficio(String aValore) {
		mCodTipoUfficio = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setChiaveProgrS22(BigDecimal aValore) {
		mChiaveProgrS22 = aValore;
	}

	public void setChiaveProgrIniziale(BigDecimal aValore) {
		mChiaveProgrIniziale = aValore;
	}

	public void setChiaveProgrFinale(BigDecimal aValore) {
		mChiaveProgrFinale = aValore;
	}

	public void setCodStatoFascicolo(String aValore) {
		mCodStatoFascicolo = aValore;
	}

	public void setDescrStatoFascicolo(String aValore) {
		mDescrStatoFascicolo = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setDataIscrizione(Date aValore) {
		mDataIscrizione = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setChiaveAnnoSIEP(BigDecimal aValore) {
		mChiaveAnnoSIEP = aValore;
	}

	public void setChiaveProgrSIEP(BigDecimal aValore) {
		mChiaveProgrSIEP = aValore;
	}

	public void setChiaveUfficioSIEP(String aValore) {
		mChiaveUfficioSIEP = aValore;
	}

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setIdFascicoloSiusOrigine(BigDecimal aValore) {
		mIdFascicoloSiusOrigine = aValore;
	}

	public void setDataDefinizione(Date aValore) {
		mDataDefinizione = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setNumFascicoli(BigDecimal aValore) {
		mNumFascicoli = aValore;
	}

	public void setNumeroFascicoliUnificati(BigDecimal aValore) {
		mNumeroFascicoliUnificati = aValore;
	} // 29/04/2004

	public void setDataIscrizioneIniziale(Date aValore) {
		mDataIscrizioneIniziale = aValore;
	} // 10/06/2004

	public void setDataIscrizioneFinale(Date aValore) {
		mDataIscrizioneFinale = aValore;
	} // 10/06/2004

	public void setDataArrivoIniziale(Date aValore) {
		mDataArrivoIniziale = aValore;
	} // 16/06/2004

	public void setDataArrivoFinale(Date aValore) {
		mDataArrivoFinale = aValore;
	} // 16/06/2004

	public void setDataAttoIniziale(Date aValore) {
		mDataAttoIniziale = aValore;
	} // 16/06/2004

	public void setDataAttoFinale(Date aValore) {
		mDataAttoFinale = aValore;
	} // 16/06/2004

	public void setDataInizioPosizioneMateriale(Date aValore) {
		mDataInizioPosizioneMateriale = aValore;
	}

	public void setDataFinePendenza(Date aValore) {
		mDataFinePendenza = aValore;
	}

	public void setDataDefinizioneIniziale(Date aValore) {
		mDataDefinizioneIniziale = aValore;
	}

	public void setDataDefinizioneFinale(Date aValore) {
		mDataDefinizioneFinale = aValore;
	}

	public void setVisibilitaMinorenne(String aValore) {
		mVisibilitaMinorenne = aValore;
	}

	public void setCodCancelleria(String aValore) {
		mCodCancelleria = aValore;
	}

	public String toString() {

		String lToString = mIdFascicoloSius + " - " + mChiaveAnno + " - " + mChiaveAnnoS22 + " - "
				+ mChiaveAnnoIniziale + " - " + mChiaveAnnoFinale + " - " + mChiaveUfficio + " - "
				+ mDescrTipoUfficio + " - " + mDescrComuneUfficio + " - " + mCodTipoUfficio + " - "
				+ mChiaveProgr + " - " + mChiaveProgrS22 + " - " + mChiaveProgrIniziale + " - "
				+ mChiaveProgrFinale + " - " + mCodStatoFascicolo + " - " + mDescrStatoFascicolo + " - "
				+ mCodOperatoreInserimento + " - " + mCodUfficioInserimento + " - " + mDataInserimento + " - "
				+ mDataIscrizione + " - " + mCodOperatoreAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDataAggiornamento + " - " + mSogIdSoggetto + " - " + mFasSieIdFascicoloSiep + " - "
				+ mChiaveAnnoSIEP + " - " + mChiaveProgrSIEP + " - " + mChiaveUfficioSIEP + " - "
				+ mNumFascicoli + " - " + mFasSiuIdFascicoloSius + " - " + mIdFascicoloSiusOrigine + " - "
				+ mDataDefinizione + " - " + mNumeroFascicoliUnificati + " - " + mDataIscrizioneIniziale
				+ " - " + mDataIscrizioneFinale + " - " + mDataArrivoIniziale + " - " + mDataArrivoFinale
				+ " - " + mDataAttoIniziale + " - " + mDataAttoFinale + " - " + // 10/06/2004
				mDataInizioPosizioneMateriale + " - " + mVisibilitaMinorenne + " - " + mCodCancelleria;

		return lToString;
	}

}