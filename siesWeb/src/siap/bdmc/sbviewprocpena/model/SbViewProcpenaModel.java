package siap.bdmc.sbviewprocpena.model;

/**
 * <p>Title: SbViewProcpenaModel</p>
 * <p>Description: Classe Model che rappresenta il SbViewProcpena</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class SbViewProcpenaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5388680394023495054L;

	private String mCodiUffiPmpm;
	private String mDescriUffiPmpm;
	private String mDescriComuUffiPmpm;
	private BigDecimal mAnnoRegiPmpm;
	private BigDecimal mNumeRegiPmpm;
	private String mCodiUffiGipp;
	private String mDescriUffiGipp;
	private String mDescriComuUffiGipp;
	private BigDecimal mAnnoRegiGipp;
	private BigDecimal mNumeRegiGipp;
	private String mCodiUffiDibb;
	private String mDescriUffiDibb;
	private String mDescriComuUffiDibb;
	private BigDecimal mAnnoRegiDibb;
	private BigDecimal mNumeRegiDibb;
	private String mCodiUffiCoap;
	private String mDescriUffiCoap;
	private String mDescriComuUffiCoap;
	private BigDecimal mNumeRegiCoap;
	private BigDecimal mAnnoRegiCoap;
	private Date mDataPassGiud;
	private String mFlagReclArreGigu;
	private BigDecimal mAnniPenaGigu;
	private BigDecimal mMesiPenaGigu;
	private BigDecimal mGiorPenaGigu;
	private Date mDataSent1gra;
	private BigDecimal mAnnoSent1gra;
	private BigDecimal mNumeSent1gra;
	private Date mDataSent2gra;
	private BigDecimal mAnnoSent2gra;
	private BigDecimal mNumeSent2gra;
	private Date mDataSentGippGupp;
	private BigDecimal mNumeSentGippGupp;
	private BigDecimal mAnnoSentGippGupp;
	private BigDecimal mAnniPenaDiba;
	private BigDecimal mMesiPenaDiba;
	private BigDecimal mGiorPenaDiba;
	private BigDecimal mAnniPenaAppe;
	private BigDecimal mMesiPenaAppe;
	private BigDecimal mGiorPenaAppe;
	private String mFlagReclArreDiba;
	private String mFlagReclArreAppe;
	private String mFlagArti0089;
	private String mFlagArti0090;
	private String mFlagArti0091;
	private String mFlagArti0092;
	private String mFlagArti0093;
	private String mFlagArti0094;
	private String mFlagArti0095;
	private String mFlagArti0096;
	private String mFlagArti0097;
	private String mFlagArti0098;
	private String mFlagArti0099;
	private String mFlagArti62;
	private String mArti0062Comm;
	private String mFlagArt62bi;
	private String mCodiMisuCust;
	private String mDescriMisuCust;
	private String mCodiIstiPena;
	private String mDescriIstiPena;
	private String mDescLuog;
	private BigDecimal mIdPren;
	private String mCodiSedeInst;
	private String mDescriSedeInst;
	private String mDescriComuSedeInst;
	private BigDecimal mNumeFascBdmc;
	private BigDecimal mAnnoFascBdmc;
	private String mFlagInfoSele;
	private Date mDataDeciCass;
	private BigDecimal mAnnoDeciCass;
	private BigDecimal mNumeDeciCass;
	private Date mDataArrivoAtto;// Data

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public SbViewProcpenaModel() {
		this.mCodiUffiPmpm = "";
		this.mDescriUffiPmpm = "";
		this.mDescriComuUffiPmpm = "";
		this.mAnnoRegiPmpm = null;
		this.mNumeRegiPmpm = null;
		this.mCodiUffiGipp = "";
		this.mDescriUffiGipp = "";
		this.mDescriComuUffiGipp = "";
		this.mAnnoRegiGipp = null;
		this.mNumeRegiGipp = null;
		this.mCodiUffiDibb = "";
		this.mDescriUffiDibb = "";
		this.mDescriComuUffiDibb = "";
		this.mAnnoRegiDibb = null;
		this.mNumeRegiDibb = null;
		this.mCodiUffiCoap = "";
		this.mDescriUffiCoap = "";
		this.mDescriComuUffiCoap = "";
		this.mNumeRegiCoap = null;
		this.mAnnoRegiCoap = null;
		this.mDataPassGiud = null;
		this.mFlagReclArreGigu = "";
		this.mAnniPenaGigu = null;
		this.mMesiPenaGigu = null;
		this.mGiorPenaGigu = null;
		this.mDataSent1gra = null;
		this.mAnnoSent1gra = null;
		this.mNumeSent1gra = null;
		this.mDataSent2gra = null;
		this.mAnnoSent2gra = null;
		this.mNumeSent2gra = null;
		this.mDataSentGippGupp = null;
		this.mNumeSentGippGupp = null;
		this.mAnnoSentGippGupp = null;
		this.mAnniPenaDiba = null;
		this.mMesiPenaDiba = null;
		this.mGiorPenaDiba = null;
		this.mAnniPenaAppe = null;
		this.mMesiPenaAppe = null;
		this.mGiorPenaAppe = null;
		this.mFlagReclArreDiba = "";
		this.mFlagReclArreAppe = "";
		this.mFlagArti0089 = "";
		this.mFlagArti0090 = "";
		this.mFlagArti0091 = "";
		this.mFlagArti0092 = "";
		this.mFlagArti0093 = "";
		this.mFlagArti0094 = "";
		this.mFlagArti0095 = "";
		this.mFlagArti0096 = "";
		this.mFlagArti0097 = "";
		this.mFlagArti0098 = "";
		this.mFlagArti0099 = "";
		this.mFlagArti62 = "";
		this.mArti0062Comm = "";
		this.mFlagArt62bi = "";
		this.mCodiMisuCust = "";
		this.mDescriMisuCust = "";
		this.mCodiIstiPena = "";
		this.mDescriIstiPena = "";
		this.mDescLuog = "";
		this.mIdPren = null;
		this.mCodiSedeInst = "";
		this.mDescriSedeInst = "";
		this.mDescriComuSedeInst = "";
		this.mNumeFascBdmc = null;
		this.mAnnoFascBdmc = null;
		this.mFlagInfoSele = "";
		this.mDataDeciCass = null;
		this.mAnnoDeciCass = null;
		this.mNumeDeciCass = null;
		this.mDataArrivoAtto = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public SbViewProcpenaModel(SbViewProcpenaModel aModel) {
		this.mCodiUffiPmpm = aModel.mCodiUffiPmpm;
		this.mDescriUffiPmpm = aModel.mDescriUffiPmpm;
		this.mDescriComuUffiPmpm = aModel.mDescriComuUffiPmpm;
		this.mAnnoRegiPmpm = aModel.mAnnoRegiPmpm;
		this.mNumeRegiPmpm = aModel.mNumeRegiPmpm;
		this.mCodiUffiGipp = aModel.mCodiUffiGipp;
		this.mDescriUffiGipp = aModel.mDescriUffiGipp;
		this.mDescriComuUffiGipp = aModel.mDescriComuUffiGipp;
		this.mAnnoRegiGipp = aModel.mAnnoRegiGipp;
		this.mNumeRegiGipp = aModel.mNumeRegiGipp;
		this.mCodiUffiDibb = aModel.mCodiUffiDibb;
		this.mDescriUffiDibb = aModel.mDescriUffiDibb;
		this.mDescriComuUffiDibb = aModel.mDescriComuUffiDibb;
		this.mAnnoRegiDibb = aModel.mAnnoRegiDibb;
		this.mNumeRegiDibb = aModel.mNumeRegiDibb;
		this.mCodiUffiCoap = aModel.mCodiUffiCoap;
		this.mDescriUffiCoap = aModel.mDescriUffiCoap;
		this.mDescriComuUffiCoap = aModel.mDescriComuUffiCoap;
		this.mNumeRegiCoap = aModel.mNumeRegiCoap;
		this.mAnnoRegiCoap = aModel.mAnnoRegiCoap;
		this.mDataPassGiud = aModel.mDataPassGiud;
		this.mFlagReclArreGigu = aModel.mFlagReclArreGigu;
		this.mAnniPenaGigu = aModel.mAnniPenaGigu;
		this.mMesiPenaGigu = aModel.mMesiPenaGigu;
		this.mGiorPenaGigu = aModel.mGiorPenaGigu;
		this.mDataSent1gra = aModel.mDataSent1gra;
		this.mAnnoSent1gra = aModel.mAnnoSent1gra;
		this.mNumeSent1gra = aModel.mNumeSent1gra;
		this.mDataSent2gra = aModel.mDataSent2gra;
		this.mAnnoSent2gra = aModel.mAnnoSent2gra;
		this.mNumeSent2gra = aModel.mNumeSent2gra;
		this.mDataSentGippGupp = aModel.mDataSentGippGupp;
		this.mNumeSentGippGupp = aModel.mNumeSentGippGupp;
		this.mAnnoSentGippGupp = aModel.mAnnoSentGippGupp;
		this.mAnniPenaDiba = aModel.mAnniPenaDiba;
		this.mMesiPenaDiba = aModel.mMesiPenaDiba;
		this.mGiorPenaDiba = aModel.mGiorPenaDiba;
		this.mAnniPenaAppe = aModel.mAnniPenaAppe;
		this.mMesiPenaAppe = aModel.mMesiPenaAppe;
		this.mGiorPenaAppe = aModel.mGiorPenaAppe;
		this.mFlagReclArreDiba = aModel.mFlagReclArreDiba;
		this.mFlagReclArreAppe = aModel.mFlagReclArreAppe;
		this.mFlagArti0089 = aModel.mFlagArti0089;
		this.mFlagArti0090 = aModel.mFlagArti0090;
		this.mFlagArti0091 = aModel.mFlagArti0091;
		this.mFlagArti0092 = aModel.mFlagArti0092;
		this.mFlagArti0093 = aModel.mFlagArti0093;
		this.mFlagArti0094 = aModel.mFlagArti0094;
		this.mFlagArti0095 = aModel.mFlagArti0095;
		this.mFlagArti0096 = aModel.mFlagArti0096;
		this.mFlagArti0097 = aModel.mFlagArti0097;
		this.mFlagArti0098 = aModel.mFlagArti0098;
		this.mFlagArti0099 = aModel.mFlagArti0099;
		this.mFlagArti62 = aModel.mFlagArti62;
		this.mArti0062Comm = aModel.mArti0062Comm;
		this.mFlagArt62bi = aModel.mFlagArt62bi;
		this.mCodiMisuCust = aModel.mCodiMisuCust;
		this.mDescriMisuCust = aModel.mDescriMisuCust;
		this.mCodiIstiPena = aModel.mCodiIstiPena;
		this.mDescriIstiPena = aModel.mDescriIstiPena;
		this.mDescLuog = aModel.mDescLuog;
		this.mIdPren = aModel.mIdPren;
		this.mCodiSedeInst = aModel.mCodiSedeInst;
		this.mDescriSedeInst = aModel.mDescriSedeInst;
		this.mDescriComuSedeInst = aModel.mDescriComuSedeInst;
		this.mNumeFascBdmc = aModel.mNumeFascBdmc;
		this.mAnnoFascBdmc = aModel.mAnnoFascBdmc;
		this.mFlagInfoSele = aModel.mFlagInfoSele;
		this.mDataDeciCass = aModel.mDataDeciCass;
		this.mAnnoDeciCass = aModel.mAnnoDeciCass;
		this.mNumeDeciCass = aModel.mNumeDeciCass;
		this.mDataArrivoAtto = aModel.mDataArrivoAtto;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public SbViewProcpenaModel(String aCodiUffiPmpm, String aDescriUffiPmpm, String aDescriComuUffiPmpm,
			BigDecimal aAnnoRegiPmpm, BigDecimal aNumeRegiPmpm, String aCodiUffiGipp, String aDescriUffiGipp,
			String aDescriComuUffiGipp, BigDecimal aAnnoRegiGipp, BigDecimal aNumeRegiGipp,
			String aCodiUffiDibb, String aDescriUffiDibb, String aDescriComuUffiDibb,
			BigDecimal aAnnoRegiDibb, BigDecimal aNumeRegiDibb, String aCodiUffiCoap, String aDescriUffiCoap,
			String aDescriComuUffiCoap, BigDecimal aNumeRegiCoap, BigDecimal aAnnoRegiCoap,
			Date aDataPassGiud, String aFlagReclArreGigu, BigDecimal aAnniPenaGigu, BigDecimal aMesiPenaGigu,
			BigDecimal aGiorPenaGigu, Date aDataSent1gra, BigDecimal aAnnoSent1gra, BigDecimal aNumeSent1gra,
			Date aDataSent2gra, BigDecimal aAnnoSent2gra, BigDecimal aNumeSent2gra, Date aDataSentGippGupp,
			BigDecimal aNumeSentGippGupp, BigDecimal aAnnoSentGippGupp, BigDecimal aAnniPenaDiba,
			BigDecimal aMesiPenaDiba, BigDecimal aGiorPenaDiba, BigDecimal aAnniPenaAppe,
			BigDecimal aMesiPenaAppe, BigDecimal aGiorPenaAppe, String aFlagReclArreDiba,
			String aFlagReclArreAppe, String aFlagArti0089, String aFlagArti0090, String aFlagArti0091,
			String aFlagArti0092, String aFlagArti0093, String aFlagArti0094, String aFlagArti0095,
			String aFlagArti0096, String aFlagArti0097, String aFlagArti0098, String aFlagArti0099,
			String aFlagArti62, String aArti0062Comm, String aFlagArt62bi, String aCodiMisuCust,
			String aDescriMisuCust, String aCodiIstiPena, String aDescriIstiPena, String aDescLuog,
			BigDecimal aIdPren, String aCodiSedeInst, String aDescriSedeInst, String aDescriComuSedeInst,
			BigDecimal aNumeFascBdmc, BigDecimal aAnnoFascBdmc, String aFlagInfoSele, Date aDataDeciCass,
			BigDecimal aAnnoDeciCass, BigDecimal aNumeDeciCass) {
		this.mCodiUffiPmpm = aCodiUffiPmpm;
		this.mDescriUffiPmpm = aDescriUffiPmpm;
		this.mDescriComuUffiPmpm = aDescriComuUffiPmpm;
		this.mAnnoRegiPmpm = aAnnoRegiPmpm;
		this.mNumeRegiPmpm = aNumeRegiPmpm;
		this.mCodiUffiGipp = aCodiUffiGipp;
		this.mDescriUffiGipp = aDescriUffiGipp;
		this.mDescriComuUffiGipp = aDescriComuUffiGipp;
		this.mAnnoRegiGipp = aAnnoRegiGipp;
		this.mNumeRegiGipp = aNumeRegiGipp;
		this.mCodiUffiDibb = aCodiUffiDibb;
		this.mDescriUffiDibb = aDescriUffiDibb;
		this.mDescriComuUffiDibb = aDescriComuUffiDibb;
		this.mAnnoRegiDibb = aAnnoRegiDibb;
		this.mNumeRegiDibb = aNumeRegiDibb;
		this.mCodiUffiCoap = aCodiUffiCoap;
		this.mDescriUffiCoap = aDescriUffiCoap;
		this.mDescriComuUffiCoap = aDescriComuUffiCoap;
		this.mNumeRegiCoap = aNumeRegiCoap;
		this.mAnnoRegiCoap = aAnnoRegiCoap;
		this.mDataPassGiud = aDataPassGiud;
		this.mFlagReclArreGigu = aFlagReclArreGigu;
		this.mAnniPenaGigu = aAnniPenaGigu;
		this.mMesiPenaGigu = aMesiPenaGigu;
		this.mGiorPenaGigu = aGiorPenaGigu;
		this.mDataSent1gra = aDataSent1gra;
		this.mAnnoSent1gra = aAnnoSent1gra;
		this.mNumeSent1gra = aNumeSent1gra;
		this.mDataSent2gra = aDataSent2gra;
		this.mAnnoSent2gra = aAnnoSent2gra;
		this.mNumeSent2gra = aNumeSent2gra;
		this.mDataSentGippGupp = aDataSentGippGupp;
		this.mNumeSentGippGupp = aNumeSentGippGupp;
		this.mAnnoSentGippGupp = aAnnoSentGippGupp;
		this.mAnniPenaDiba = aAnniPenaDiba;
		this.mMesiPenaDiba = aMesiPenaDiba;
		this.mGiorPenaDiba = aGiorPenaDiba;
		this.mAnniPenaAppe = aAnniPenaAppe;
		this.mMesiPenaAppe = aMesiPenaAppe;
		this.mGiorPenaAppe = aGiorPenaAppe;
		this.mFlagReclArreDiba = aFlagReclArreDiba;
		this.mFlagReclArreAppe = aFlagReclArreAppe;
		this.mFlagArti0089 = aFlagArti0089;
		this.mFlagArti0090 = aFlagArti0090;
		this.mFlagArti0091 = aFlagArti0091;
		this.mFlagArti0092 = aFlagArti0092;
		this.mFlagArti0093 = aFlagArti0093;
		this.mFlagArti0094 = aFlagArti0094;
		this.mFlagArti0095 = aFlagArti0095;
		this.mFlagArti0096 = aFlagArti0096;
		this.mFlagArti0097 = aFlagArti0097;
		this.mFlagArti0098 = aFlagArti0098;
		this.mFlagArti0099 = aFlagArti0099;
		this.mFlagArti62 = aFlagArti62;
		this.mArti0062Comm = aArti0062Comm;
		this.mFlagArt62bi = aFlagArt62bi;
		this.mCodiMisuCust = aCodiMisuCust;
		this.mDescriMisuCust = aDescriMisuCust;
		this.mCodiIstiPena = aCodiIstiPena;
		this.mDescriIstiPena = aDescriIstiPena;
		this.mDescLuog = aDescLuog;
		this.mIdPren = aIdPren;
		this.mCodiSedeInst = aCodiSedeInst;
		this.mDescriSedeInst = aDescriSedeInst;
		this.mDescriComuSedeInst = aDescriComuSedeInst;
		this.mNumeFascBdmc = aNumeFascBdmc;
		this.mAnnoFascBdmc = aAnnoFascBdmc;
		this.mFlagInfoSele = aFlagInfoSele;
		this.mDataDeciCass = aDataDeciCass;
		this.mAnnoDeciCass = aAnnoDeciCass;
		this.mNumeDeciCass = aNumeDeciCass;

	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public String getCodiUffiPmpm() {
		return mCodiUffiPmpm;
	}

	public String getDescriUffiPmpm() {
		return mDescriUffiPmpm;
	}

	public String getDescriComuUffiPmpm() {
		return mDescriComuUffiPmpm;
	}

	public BigDecimal getAnnoRegiPmpm() {
		return mAnnoRegiPmpm;
	}

	public BigDecimal getNumeRegiPmpm() {
		return mNumeRegiPmpm;
	}

	public String getCodiUffiGipp() {
		return mCodiUffiGipp;
	}

	public String getDescriUffiGipp() {
		return mDescriUffiGipp;
	}

	public String getDescriComuUffiGipp() {
		return mDescriComuUffiGipp;
	}

	public BigDecimal getAnnoRegiGipp() {
		return mAnnoRegiGipp;
	}

	public BigDecimal getNumeRegiGipp() {
		return mNumeRegiGipp;
	}

	public String getCodiUffiDibb() {
		return mCodiUffiDibb;
	}

	public String getDescriUffiDibb() {
		return mDescriUffiDibb;
	}

	public String getDescriComuUffiDibb() {
		return mDescriComuUffiDibb;
	}

	public BigDecimal getAnnoRegiDibb() {
		return mAnnoRegiDibb;
	}

	public BigDecimal getNumeRegiDibb() {
		return mNumeRegiDibb;
	}

	public String getCodiUffiCoap() {
		return mCodiUffiCoap;
	}

	public String getDescriUffiCoap() {
		return mDescriUffiCoap;
	}

	public String getDescriComuUffiCoap() {
		return mDescriComuUffiCoap;
	}

	public BigDecimal getNumeRegiCoap() {
		return mNumeRegiCoap;
	}

	public BigDecimal getAnnoRegiCoap() {
		return mAnnoRegiCoap;
	}

	public Date getDataPassGiud() {
		return mDataPassGiud;
	}

	public String getFlagReclArreGigu() {
		return mFlagReclArreGigu;
	}

	public BigDecimal getAnniPenaGigu() {
		return mAnniPenaGigu;
	}

	public BigDecimal getMesiPenaGigu() {
		return mMesiPenaGigu;
	}

	public BigDecimal getGiorPenaGigu() {
		return mGiorPenaGigu;
	}

	public Date getDataSent1gra() {
		return mDataSent1gra;
	}

	public BigDecimal getAnnoSent1gra() {
		return mAnnoSent1gra;
	}

	public BigDecimal getNumeSent1gra() {
		return mNumeSent1gra;
	}

	public Date getDataSent2gra() {
		return mDataSent2gra;
	}

	public BigDecimal getAnnoSent2gra() {
		return mAnnoSent2gra;
	}

	public BigDecimal getNumeSent2gra() {
		return mNumeSent2gra;
	}

	public Date getDataSentGippGupp() {
		return mDataSentGippGupp;
	}

	public BigDecimal getNumeSentGippGupp() {
		return mNumeSentGippGupp;
	}

	public BigDecimal getAnnoSentGippGupp() {
		return mAnnoSentGippGupp;
	}

	public BigDecimal getAnniPenaDiba() {
		return mAnniPenaDiba;
	}

	public BigDecimal getMesiPenaDiba() {
		return mMesiPenaDiba;
	}

	public BigDecimal getGiorPenaDiba() {
		return mGiorPenaDiba;
	}

	public BigDecimal getAnniPenaAppe() {
		return mAnniPenaAppe;
	}

	public BigDecimal getMesiPenaAppe() {
		return mMesiPenaAppe;
	}

	public BigDecimal getGiorPenaAppe() {
		return mGiorPenaAppe;
	}

	public String getFlagReclArreDiba() {
		return mFlagReclArreDiba;
	}

	public String getFlagReclArreAppe() {
		return mFlagReclArreAppe;
	}

	public String getFlagArti0089() {
		return mFlagArti0089;
	}

	public String getFlagArti0090() {
		return mFlagArti0090;
	}

	public String getFlagArti0091() {
		return mFlagArti0091;
	}

	public String getFlagArti0092() {
		return mFlagArti0092;
	}

	public String getFlagArti0093() {
		return mFlagArti0093;
	}

	public String getFlagArti0094() {
		return mFlagArti0094;
	}

	public String getFlagArti0095() {
		return mFlagArti0095;
	}

	public String getFlagArti0096() {
		return mFlagArti0096;
	}

	public String getFlagArti0097() {
		return mFlagArti0097;
	}

	public String getFlagArti0098() {
		return mFlagArti0098;
	}

	public String getFlagArti0099() {
		return mFlagArti0099;
	}

	public String getFlagArti62() {
		return mFlagArti62;
	}

	public String getArti0062Comm() {
		return mArti0062Comm;
	}

	public String getFlagArt62bi() {
		return mFlagArt62bi;
	}

	public String getCodiMisuCust() {
		return mCodiMisuCust;
	}

	public String getDescriMisuCust() {
		return mDescriMisuCust;
	}

	public String getCodiIstiPena() {
		return mCodiIstiPena;
	}

	public String getDescriIstiPena() {
		return mDescriIstiPena;
	}

	public String getDescLuog() {
		return mDescLuog;
	}

	public BigDecimal getIdPren() {
		return mIdPren;
	}

	public String getCodiSedeInst() {
		return mCodiSedeInst;
	}

	public String getDescriSedeInst() {
		return mDescriSedeInst;
	}

	public String getDescriComuSedeInst() {
		return mDescriComuSedeInst;
	}

	public BigDecimal getNumeFascBdmc() {
		return mNumeFascBdmc;
	}

	public BigDecimal getAnnoFascBdmc() {
		return mAnnoFascBdmc;
	}

	public String getFlagInfoSele() {
		return mFlagInfoSele;
	}

	public Date getDataDeciCass() {
		return mDataDeciCass;
	}

	public BigDecimal getAnnoDeciCass() {
		return mAnnoDeciCass;
	}

	public BigDecimal getNumeDeciCass() {
		return mNumeDeciCass;
	}

	public Date getDataArrivoAtto() {
		return mDataArrivoAtto;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setCodiUffiPmpm(String aValore) {
		mCodiUffiPmpm = aValore;
	}

	public void setDescriUffiPmpm(String aValore) {
		mDescriUffiPmpm = aValore;
	}

	public void setDescriComuUffiPmpm(String aValore) {
		mDescriComuUffiPmpm = aValore;
	}

	public void setAnnoRegiPmpm(BigDecimal aValore) {
		mAnnoRegiPmpm = aValore;
	}

	public void setNumeRegiPmpm(BigDecimal aValore) {
		mNumeRegiPmpm = aValore;
	}

	public void setCodiUffiGipp(String aValore) {
		mCodiUffiGipp = aValore;
	}

	public void setDescriUffiGipp(String aValore) {
		mDescriUffiGipp = aValore;
	}

	public void setDescriComuUffiGipp(String aValore) {
		mDescriComuUffiGipp = aValore;
	}

	public void setAnnoRegiGipp(BigDecimal aValore) {
		mAnnoRegiGipp = aValore;
	}

	public void setNumeRegiGipp(BigDecimal aValore) {
		mNumeRegiGipp = aValore;
	}

	public void setCodiUffiDibb(String aValore) {
		mCodiUffiDibb = aValore;
	}

	public void setDescriUffiDibb(String aValore) {
		mDescriUffiDibb = aValore;
	}

	public void setDescriComuUffiDibb(String aValore) {
		mDescriComuUffiDibb = aValore;
	}

	public void setAnnoRegiDibb(BigDecimal aValore) {
		mAnnoRegiDibb = aValore;
	}

	public void setNumeRegiDibb(BigDecimal aValore) {
		mNumeRegiDibb = aValore;
	}

	public void setCodiUffiCoap(String aValore) {
		mCodiUffiCoap = aValore;
	}

	public void setDescriUffiCoap(String aValore) {
		mDescriUffiCoap = aValore;
	}

	public void setDescriComuUffiCoap(String aValore) {
		mDescriComuUffiCoap = aValore;
	}

	public void setNumeRegiCoap(BigDecimal aValore) {
		mNumeRegiCoap = aValore;
	}

	public void setAnnoRegiCoap(BigDecimal aValore) {
		mAnnoRegiCoap = aValore;
	}

	public void setDataPassGiud(Date aValore) {
		mDataPassGiud = aValore;
	}

	public void setFlagReclArreGigu(String aValore) {
		mFlagReclArreGigu = aValore;
	}

	public void setAnniPenaGigu(BigDecimal aValore) {
		mAnniPenaGigu = aValore;
	}

	public void setMesiPenaGigu(BigDecimal aValore) {
		mMesiPenaGigu = aValore;
	}

	public void setGiorPenaGigu(BigDecimal aValore) {
		mGiorPenaGigu = aValore;
	}

	public void setDataSent1gra(Date aValore) {
		mDataSent1gra = aValore;
	}

	public void setAnnoSent1gra(BigDecimal aValore) {
		mAnnoSent1gra = aValore;
	}

	public void setNumeSent1gra(BigDecimal aValore) {
		mNumeSent1gra = aValore;
	}

	public void setDataSent2gra(Date aValore) {
		mDataSent2gra = aValore;
	}

	public void setAnnoSent2gra(BigDecimal aValore) {
		mAnnoSent2gra = aValore;
	}

	public void setNumeSent2gra(BigDecimal aValore) {
		mNumeSent2gra = aValore;
	}

	public void setDataSentGippGupp(Date aValore) {
		mDataSentGippGupp = aValore;
	}

	public void setNumeSentGippGupp(BigDecimal aValore) {
		mNumeSentGippGupp = aValore;
	}

	public void setAnnoSentGippGupp(BigDecimal aValore) {
		mAnnoSentGippGupp = aValore;
	}

	public void setAnniPenaDiba(BigDecimal aValore) {
		mAnniPenaDiba = aValore;
	}

	public void setMesiPenaDiba(BigDecimal aValore) {
		mMesiPenaDiba = aValore;
	}

	public void setGiorPenaDiba(BigDecimal aValore) {
		mGiorPenaDiba = aValore;
	}

	public void setAnniPenaAppe(BigDecimal aValore) {
		mAnniPenaAppe = aValore;
	}

	public void setMesiPenaAppe(BigDecimal aValore) {
		mMesiPenaAppe = aValore;
	}

	public void setGiorPenaAppe(BigDecimal aValore) {
		mGiorPenaAppe = aValore;
	}

	public void setFlagReclArreDiba(String aValore) {
		mFlagReclArreDiba = aValore;
	}

	public void setFlagReclArreAppe(String aValore) {
		mFlagReclArreAppe = aValore;
	}

	public void setFlagArti0089(String aValore) {
		mFlagArti0089 = aValore;
	}

	public void setFlagArti0090(String aValore) {
		mFlagArti0090 = aValore;
	}

	public void setFlagArti0091(String aValore) {
		mFlagArti0091 = aValore;
	}

	public void setFlagArti0092(String aValore) {
		mFlagArti0092 = aValore;
	}

	public void setFlagArti0093(String aValore) {
		mFlagArti0093 = aValore;
	}

	public void setFlagArti0094(String aValore) {
		mFlagArti0094 = aValore;
	}

	public void setFlagArti0095(String aValore) {
		mFlagArti0095 = aValore;
	}

	public void setFlagArti0096(String aValore) {
		mFlagArti0096 = aValore;
	}

	public void setFlagArti0097(String aValore) {
		mFlagArti0097 = aValore;
	}

	public void setFlagArti0098(String aValore) {
		mFlagArti0098 = aValore;
	}

	public void setFlagArti0099(String aValore) {
		mFlagArti0099 = aValore;
	}

	public void setFlagArti62(String aValore) {
		mFlagArti62 = aValore;
	}

	public void setArti0062Comm(String aValore) {
		mArti0062Comm = aValore;
	}

	public void setFlagArt62bi(String aValore) {
		mFlagArt62bi = aValore;
	}

	public void setCodiMisuCust(String aValore) {
		mCodiMisuCust = aValore;
	}

	public void setDescriMisuCust(String aValore) {
		mDescriMisuCust = aValore;
	}

	public void setCodiIstiPena(String aValore) {
		mCodiIstiPena = aValore;
	}

	public void setDescriIstiPena(String aValore) {
		mDescriIstiPena = aValore;
	}

	public void setDescLuog(String aValore) {
		mDescLuog = aValore;
	}

	public void setIdPren(BigDecimal aValore) {
		mIdPren = aValore;
	}

	public void setCodiSedeInst(String aValore) {
		mCodiSedeInst = aValore;
	}

	public void setDescriSedeInst(String aValore) {
		mDescriSedeInst = aValore;
	}

	public void setDescriComuSedeInst(String aValore) {
		mDescriComuSedeInst = aValore;
	}

	public void setNumeFascBdmc(BigDecimal aValore) {
		mNumeFascBdmc = aValore;
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		mAnnoFascBdmc = aValore;
	}

	public void setFlagInfoSele(String aValore) {
		mFlagInfoSele = aValore;
	}

	public void setDataDeciCass(Date aValore) {
		mDataDeciCass = aValore;
	}

	public void setAnnoDeciCass(BigDecimal aValore) {
		mAnnoDeciCass = aValore;
	}

	public void setNumeDeciCass(BigDecimal aValore) {
		mNumeDeciCass = aValore;
	}

	public void setDataArrivoAtto(Date aValore) {
		mDataArrivoAtto = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SbViewProcpenaModel:\n" + "[ mCodiUffiPmpm     = " + mCodiUffiPmpm + " ]\n"
				+ "[ mAnnoRegiPmpm     = " + mAnnoRegiPmpm + " ]\n" + "[ mNumeRegiPmpm     = " + mNumeRegiPmpm
				+ " ]\n" + "[ mCodiUffiGipp     = " + mCodiUffiGipp + " ]\n" + "[ mAnnoRegiGipp     = "
				+ mAnnoRegiGipp + " ]\n" + "[ mNumeRegiGipp     = " + mNumeRegiGipp + " ]\n"
				+ "[ mCodiUffiDibb     = " + mCodiUffiDibb + " ]\n" + "[ mAnnoRegiDibb     = " + mAnnoRegiDibb
				+ " ]\n" + "[ mNumeRegiDibb     = " + mNumeRegiDibb + " ]\n" + "[ mCodiUffiCoap     = "
				+ mCodiUffiCoap + " ]\n" + "[ mNumeRegiCoap     = " + mNumeRegiCoap + " ]\n"
				+ "[ mAnnoRegiCoap     = " + mAnnoRegiCoap + " ]\n" + "[ mDataPassGiud     = " + mDataPassGiud
				+ " ]\n" + "[ mFlagReclArreGigu = " + mFlagReclArreGigu + " ]\n" + "[ mAnniPenaGigu     = "
				+ mAnniPenaGigu + " ]\n" + "[ mMesiPenaGigu     = " + mMesiPenaGigu + " ]\n"
				+ "[ mGiorPenaGigu     = " + mGiorPenaGigu + " ]\n" + "[ mDataSent1gra     = " + mDataSent1gra
				+ " ]\n" + "[ mAnnoSent1gra     = " + mAnnoSent1gra + " ]\n" + "[ mNumeSent1gra     = "
				+ mNumeSent1gra + " ]\n" + "[ mDataSent2gra     = " + mDataSent2gra + " ]\n"
				+ "[ mAnnoSent2gra     = " + mAnnoSent2gra + " ]\n" + "[ mNumeSent2gra     = " + mNumeSent2gra
				+ " ]\n" + "[ mDataSentGippGupp = " + mDataSentGippGupp + " ]\n" + "[ mNumeSentGippGupp = "
				+ mNumeSentGippGupp + " ]\n" + "[ mAnnoSentGippGupp = " + mAnnoSentGippGupp + " ]\n"
				+ "[ mAnniPenaDiba     = " + mAnniPenaDiba + " ]\n" + "[ mMesiPenaDiba     = " + mMesiPenaDiba
				+ " ]\n" + "[ mGiorPenaDiba     = " + mGiorPenaDiba + " ]\n" + "[ mAnniPenaAppe     = "
				+ mAnniPenaAppe + " ]\n" + "[ mMesiPenaAppe     = " + mMesiPenaAppe + " ]\n"
				+ "[ mGiorPenaAppe     = " + mGiorPenaAppe + " ]\n" + "[ mFlagReclArreDiba = "
				+ mFlagReclArreDiba + " ]\n" + "[ mFlagReclArreAppe = " + mFlagReclArreAppe + " ]\n"
				+ "[ mFlagArti0089     = " + mFlagArti0089 + " ]\n" + "[ mFlagArti0090     = " + mFlagArti0090
				+ " ]\n" + "[ mFlagArti0091     = " + mFlagArti0091 + " ]\n" + "[ mFlagArti0092     = "
				+ mFlagArti0092 + " ]\n" + "[ mFlagArti0093     = " + mFlagArti0093 + " ]\n"
				+ "[ mFlagArti0094     = " + mFlagArti0094 + " ]\n" + "[ mFlagArti0095     = " + mFlagArti0095
				+ " ]\n" + "[ mFlagArti0096     = " + mFlagArti0096 + " ]\n" + "[ mFlagArti0097     = "
				+ mFlagArti0097 + " ]\n" + "[ mFlagArti0098     = " + mFlagArti0098 + " ]\n"
				+ "[ mFlagArti0099     = " + mFlagArti0099 + " ]\n" + "[ mFlagArti62       = " + mFlagArti62
				+ " ]\n" + "[ mArti0062Comm     = " + mArti0062Comm + " ]\n" + "[ mFlagArt62bi      = "
				+ mFlagArt62bi + " ]\n" + "[ mCodiMisuCust     = " + mCodiMisuCust + " ]\n"
				+ "[ mCodiIstiPena     = " + mCodiIstiPena + " ]\n" + "[ mDescLuog         = " + mDescLuog
				+ " ]\n" + "[ mIdPren           = " + mIdPren + " ]\n" + "[ mCodiSedeInst     = "
				+ mCodiSedeInst + " ]\n" + "[ mNumeFascBdmc     = " + mNumeFascBdmc + " ]\n"
				+ "[ mAnnoFascBdmc     = " + mAnnoFascBdmc + " ]\n" + "[ mFlagInfoSele     = " + mFlagInfoSele
				+ " ]\n" + "[ mDataDeciCass     = " + mDataDeciCass + " ]\n" + "[ mAnnoDeciCass     = "
				+ mAnnoDeciCass + " ]\n" + "[ mNumeDeciCass     = " + mNumeDeciCass + " ]\n"
				+ "[ mDataArrivoAtto     = " + mDataArrivoAtto + " ]";
		return lStr;
	}

	/**
	 * Trasformazione del ProcpenaModel in SentenzaModel gli attributi commentati non sono presenti nel
	 * ProcpenaModel
	 * 
	 * @return SentenzaModel
	 */
	public SentenzaModel toSentenza() throws F3BException {
		SentenzaModel lSentenza = new SentenzaModel();

		BigDecimal annoSentenza = null;
		String numeroSentenza = null;
		Date dataSentenza = new Date();
		// String descAutoritaEmittente = new String();
		String codAutoritaEmittente = new String();
		boolean flagAutorita = false;

		// Dati della sentenza di cassazione
		lSentenza.setAnnoSentenzaCassazione(this.getAnnoDeciCass());
		lSentenza.setCodTipoDecisioneCassazione("-");
		if (this.getNumeDeciCass() != null) {

			lSentenza.setNumeroSentenzaCassazione(this.getNumeDeciCass().toString());
			lSentenza.setCodTipoDecisioneCassazione("01");
		}
		/*
		 * if (this.getDataDeciCass() != null){ flagAutorita = true; annoSentenza = this.getAnnoDeciCass();
		 * numeroSentenza = this.getNumeDeciCass().toString(); dataSentenza = this.getDataDeciCass(); //???
		 * codAutoritaEmittente= ""; descAutoritaEmittente = "CORTE DI CASSAZIONE"; } if ((!flagAutorita)&&
		 * (this.getDataSent2gra() != null) ){ flagAutorita = true; annoSentenza = this.getAnnoSent2gra();
		 * numeroSentenza = this.getNumeSent2gra().toString(); dataSentenza = this.getDataSent2gra();
		 * codAutoritaEmittente= this.mCodiUffiCoap; descAutoritaEmittente =
		 * this.getDescriUffiCoap()+" di "+this.getDescriComuUffiCoap(); }
		 */
		// Eventuale sentenza di sencondo grado
		lSentenza.setDataProvvRif(this.getDataSent2gra());
		lSentenza.setAnnoProvvRif(this.getAnnoSent2gra());
		lSentenza.setCodLuogoProvvRif("-");
		lSentenza.setCodTipoAutoritaProvvRif("-");
		lSentenza.setCodTipoProvvRif("-");
		lSentenza.setCodBilanciamentoCircostanze("-");
		if (this.getNumeSent2gra() != null) {
			lSentenza.setNumeroProvvRif(this.getNumeSent2gra().toString());

			if (this.getCodiUffiCoap().length() != 0) {
				UfficioModel uffEmittente = UfficioUtils.getUfficioByCodUfficio(this.getCodiUffiCoap());

				lSentenza.setCodLuogoProvvRif(uffEmittente.getCodComune());
				lSentenza.setCodTipoAutoritaProvvRif(uffEmittente.getCodTipoUfficio());
			}
		}

		if ((!flagAutorita) && (this.getDataSent1gra() != null)) {
			flagAutorita = true;
			annoSentenza = this.getAnnoSent1gra();
			if (this.getNumeSent1gra() != null)
				numeroSentenza = this.getNumeSent1gra().toString();
			dataSentenza = this.getDataSent1gra();
			codAutoritaEmittente = this.mCodiUffiDibb;
			// descAutoritaEmittente = this.getDescriUffiDibb() + " di " + this.getDescriComuUffiDibb();
		}
		if ((!flagAutorita) && (this.getDataSentGippGupp() != null)) {
			flagAutorita = true;
			annoSentenza = this.getAnnoSentGippGupp();
			if (this.getNumeSentGippGupp() != null)
				numeroSentenza = this.getNumeSentGippGupp().toString();
			dataSentenza = this.getDataSentGippGupp();
			codAutoritaEmittente = this.mCodiUffiGipp;
			// descAutoritaEmittente = this.getDescriUffiGipp() + " di " + this.getDescriComuUffiGipp();
		}
		// Decodifica del codAutotità emittente in codTipoAutEmittente (GIP,CASAP...) e cod_luogo_emittente
		UfficioModel uffEmittente = new UfficioModel();
		if (codAutoritaEmittente.length() != 0) {
			uffEmittente = UfficioUtils.getUfficioByCodUfficio(codAutoritaEmittente);
			lSentenza.setCodLuogoEmittente(uffEmittente.getCodComune());
			lSentenza.setCodTipoAutoritaEmittente(uffEmittente.getCodTipoUfficio());
		}
		/*
		 * else throw new
		 * F3BException("Impossibile risalire all'autorità emittente della sentenza di primo grado");
		 */
		lSentenza.setCodTipoProvvedimento("01");
		// ??lSentenza.setDescrTipoProvvedimento(this.mDescrTipoProvvedimento);
		// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
		// lSentenza.setDataArrivoAtto(this.mDataArrivoAtto);
		lSentenza.setDataProvvedimento(dataSentenza);
		lSentenza.setDescrTipoAutoritaEmittente(
				uffEmittente.getDescrTipoUfficio() + " di " + uffEmittente.getDescrComune());
		// ??lSentenza.setCodLuogoEmittente(this.mCodLuogoEmittente);
		lSentenza.setDescrLuogoEmittente(this.mDescLuog);
		// ??lSentenza.setNumSezioneAutoritaEmittente(this.mNumSezioneAutoritaEmittente);
		lSentenza.setAnnoSentenza(annoSentenza);
		lSentenza.setNumeroSentenza(numeroSentenza);
		// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
		// lSentenza.setDataIrrevocabilita(this.mDataPassGiud);

		lSentenza.setDataSentenza(dataSentenza);
		if (lSentenza.getCodTipoAutoritaEmittente() != null
				&& lSentenza.getCodTipoAutoritaEmittente().compareTo("CAS") != 0) {
			lSentenza.setAnnoRegeCap(this.getAnnoRegiCoap());
			if (this.getNumeRegiCoap() != null)
				lSentenza.setNumeroRegeCap(this.getNumeRegiCoap().toString());
			lSentenza.setAnnoRegeDib(this.getAnnoRegiDibb());
			if (this.getNumeRegiDibb() != null)
				lSentenza.setNumeroRegeDib(this.getNumeRegiDibb().toString());
		} else {
			lSentenza.setAnnoRegeCasap(this.getAnnoRegiCoap());
			if (this.getNumeRegiCoap() != null)
				lSentenza.setNumeroRegeCasap(this.getNumeRegiCoap().toString());
			lSentenza.setAnnoRegeCas(this.getAnnoRegiDibb());
			if (this.getNumeRegiDibb() != null)
				lSentenza.setNumeroRegeCas(this.getNumeRegiDibb().toString());
		}
		lSentenza.setAnnoRegeGip(this.getAnnoRegiGipp());
		lSentenza.setNumeroRegeGip(this.getNumeRegiGipp().toString());
		lSentenza.setAnnoRegePm(this.getAnnoRegiPmpm());
		lSentenza.setNumeroRegePm(this.getNumeRegiPmpm().toString());
		lSentenza.setDataIscrizione(DateUtils.getSysDate());
		return lSentenza;
	}

}