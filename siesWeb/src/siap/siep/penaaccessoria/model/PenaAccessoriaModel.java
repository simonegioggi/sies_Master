package siap.siep.penaaccessoria.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * PenaAccessoriaModel - Classe Model che rappresenta il PenaAccessoria
 *
 * @version 1.0
 */
public class PenaAccessoriaModel extends GenericModel {

	private static final long serialVersionUID = 4226511611216544573L;

	private BigDecimal mIdPenaAccessoria;
	private String mCodTipoPenaAccessoria;
	private String mDescrTipoPenaAccessoria;
	private String mDurata;
	private String mDescrDurata;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private String mFlagCondonata;
	private Date mDataDpr;
	private String mNumDpr;
	private String mFlagDichiarazioneFalsita;
	private String mFlagRevocaCondono;
	private Date mDataSentenzaRevoca;
	private BigDecimal mAnnoSentenzaRevoca;
	private String mNumeroSentenzaRevoca;
	private String mCodTipoUfficioSentenzaRevo;
	private String mDescrTipoUfficioSentenzaRevo;
	private String mCodLuogoSentenzaRevoca;
	private String mDescrLuogoSentenzaRevoca;
	private BigDecimal mAnnoRegePmRevoca;
	private String mNumeroRegePmRevoca;
	private BigDecimal mAnnoRegeGipRevoca;
	private String mNumeroRegeGipRevoca;
	private BigDecimal mAnnoRegeDibRevoca;
	private String mNumeroRegeDibRevoca;
	private BigDecimal mAnnoRegeCasRevoca;
	private String mNumeroRegeCasRevoca;
	private BigDecimal mAnnoRegeCapRevoca;
	private String mNumeroRegeCapRevoca;
	private BigDecimal mAnnoRegeCasapRevoca;
	private String mNumeroRegeCasapRevoca;
	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private BigDecimal mAnnoOrdinanzaGE;
	private BigDecimal mNumeroOrdinanzaGE;
	private Date mDataOrdinanzaGE;
	private String mEstremiCondono;
	// private Date mDataDPRCondono;
	// private BigDecimal mNumeroDPRCondono;
	private BigDecimal mIdPenaAccessoriaOrigine;
	private String mCodNuovoTipoPenaAccessoria;
	private String mDescrNuovoTipoPenaAccessoria;
	private BigDecimal mNumeroEventiCorrelati;
	private String mCodTipoUfficioOrdinanzaGE;
	private String mDescrTipoUfficioOrdinanzaGE;
	private String mCodLuogoUfficioOrdinanzaGE;
	private String mDescrLuogoUfficioOrdinanzaGE;
	private Date mDataOrdinanzaPA;
	private BigDecimal mAnnoOrdinanzaPA;
	private BigDecimal mNumeroOrdinanzaPA;
	private String mCodTipoUfficioOrdinanzaPA;
	private String mDescrTipoUfficioOrdinanzaPA;
	private String mCodLuogoUfficioOrdinanzaPA;
	private String mDescrLuogoUfficioOrdinanzaPA;
	private String mDescrAltrePA; // 04/04/2006
	private String mCodFonteGE; // 04/04/2006
	private String mDescrFonteGE; // 04/04/2006
	private String mAnnoFonteGE; // 04/04/2006
	private String mNumeroFonteGE; // 04/04/2006
	private String mCodSottonumerazioneGE; // 04/04/2006
	private String mDescrSottonumerazioneGE; // 04/04/2006
	private String mCommaGE; // 04/04/2006
	private String mLetteraGE; // 04/04/2006
	private String mNumeroGE; // 04/04/2006
	private String mArticoloGE; // 04/04/2006

	private BigDecimal mBenIdBeneficio;

	// COSTRUTTORE DI DEFAULT
	public PenaAccessoriaModel() {

		this.mIdPenaAccessoria = null;
		this.mCodTipoPenaAccessoria = "-";
		this.mDescrTipoPenaAccessoria = null;
		this.mDurata = "-";
		this.mDescrDurata = null;
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mFlagCondonata = "-";
		this.mDataDpr = null;
		this.mNumDpr = null;
		this.mFlagDichiarazioneFalsita = null;
		this.mFlagRevocaCondono = "";
		this.mDataSentenzaRevoca = null;
		this.mAnnoSentenzaRevoca = null;
		this.mNumeroSentenzaRevoca = "";
		this.mCodTipoUfficioSentenzaRevo = "-";
		this.mDescrTipoUfficioSentenzaRevo = "";
		this.mCodLuogoSentenzaRevoca = "-";
		this.mDescrLuogoSentenzaRevoca = "";
		this.mAnnoRegePmRevoca = null;
		this.mNumeroRegePmRevoca = "";
		this.mAnnoRegeGipRevoca = null;
		this.mNumeroRegeGipRevoca = "";
		this.mAnnoRegeDibRevoca = null;
		this.mNumeroRegeDibRevoca = "";
		this.mAnnoRegeCasRevoca = null;
		this.mNumeroRegeCasRevoca = "";
		this.mAnnoRegeCapRevoca = null;
		this.mNumeroRegeCapRevoca = "";
		this.mAnnoRegeCasapRevoca = null;
		this.mNumeroRegeCasapRevoca = "";
		this.mNote = null;
		this.mCodOperatoreInserimento = null;
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "-";
		this.mDescrUfficioInserimento = null;
		this.mCodOperatoreAggiornamento = null;
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "-";
		this.mDescrUfficioAggiornamento = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mAnnoOrdinanzaGE = null;
		this.mNumeroOrdinanzaGE = null;
		this.mDataOrdinanzaGE = null;
		this.mEstremiCondono = "";
		this.mIdPenaAccessoriaOrigine = null;
		this.mCodNuovoTipoPenaAccessoria = "-";
		this.mDescrNuovoTipoPenaAccessoria = "";
		this.mNumeroEventiCorrelati = null;
		this.mCodTipoUfficioOrdinanzaGE = "-";
		this.mDescrTipoUfficioOrdinanzaGE = "";
		this.mCodLuogoUfficioOrdinanzaGE = "-";
		this.mDescrLuogoUfficioOrdinanzaGE = "";
		this.mDataOrdinanzaPA = null;
		this.mAnnoOrdinanzaPA = null;
		this.mNumeroOrdinanzaPA = null;
		this.mCodTipoUfficioOrdinanzaPA = "-";
		this.mDescrTipoUfficioOrdinanzaPA = "";
		this.mCodLuogoUfficioOrdinanzaPA = "-";
		this.mDescrLuogoUfficioOrdinanzaPA = "";
		this.mDescrAltrePA = ""; // 04/04/2006
		this.mCodFonteGE = "-"; // 04/04/2006
		this.mDescrFonteGE = ""; // 04/04/2006
		this.mAnnoFonteGE = ""; // 04/04/2006
		this.mNumeroFonteGE = ""; // 04/04/2006
		this.mCodSottonumerazioneGE = "-"; // 04/04/2006
		this.mDescrSottonumerazioneGE = ""; // 04/04/2006
		this.mCommaGE = ""; // 04/04/2006
		this.mLetteraGE = ""; // 04/04/2006
		this.mNumeroGE = ""; // 04/04/2006
		this.mArticoloGE = ""; // 04/04/2006
		this.mBenIdBeneficio = null;
	}

	// COSTRUTTORE DI COPIA
	public PenaAccessoriaModel(PenaAccessoriaModel aModel) {

		this.mIdPenaAccessoria = aModel.mIdPenaAccessoria;
		this.mCodTipoPenaAccessoria = aModel.mCodTipoPenaAccessoria;
		this.mDescrTipoPenaAccessoria = aModel.mDescrTipoPenaAccessoria;
		this.mDurata = aModel.mDurata;
		this.mDescrDurata = aModel.mDescrDurata;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mFlagCondonata = aModel.mFlagCondonata;
		this.mDataDpr = aModel.mDataDpr;
		this.mNumDpr = aModel.mNumDpr;
		this.mFlagDichiarazioneFalsita = aModel.mFlagDichiarazioneFalsita;
		this.mFlagRevocaCondono = aModel.mFlagRevocaCondono;
		this.mDataSentenzaRevoca = aModel.mDataSentenzaRevoca;
		this.mAnnoSentenzaRevoca = aModel.mAnnoSentenzaRevoca;
		this.mNumeroSentenzaRevoca = aModel.mNumeroSentenzaRevoca;
		this.mCodTipoUfficioSentenzaRevo = aModel.mCodTipoUfficioSentenzaRevo;
		this.mDescrTipoUfficioSentenzaRevo = aModel.mDescrTipoUfficioSentenzaRevo;
		this.mCodLuogoSentenzaRevoca = aModel.mCodLuogoSentenzaRevoca;
		this.mDescrLuogoSentenzaRevoca = aModel.mDescrLuogoSentenzaRevoca;
		this.mAnnoRegePmRevoca = aModel.mAnnoRegePmRevoca;
		this.mNumeroRegePmRevoca = aModel.mNumeroRegePmRevoca;
		this.mAnnoRegeGipRevoca = aModel.mAnnoRegeGipRevoca;
		this.mNumeroRegeGipRevoca = aModel.mNumeroRegeGipRevoca;
		this.mAnnoRegeDibRevoca = aModel.mAnnoRegeDibRevoca;
		this.mNumeroRegeDibRevoca = aModel.mNumeroRegeDibRevoca;
		this.mAnnoRegeCasRevoca = aModel.mAnnoRegeCasRevoca;
		this.mNumeroRegeCasRevoca = aModel.mNumeroRegeCasRevoca;
		this.mAnnoRegeCapRevoca = aModel.mAnnoRegeCapRevoca;
		this.mNumeroRegeCapRevoca = aModel.mNumeroRegeCapRevoca;
		this.mAnnoRegeCasapRevoca = aModel.mAnnoRegeCasapRevoca;
		this.mNumeroRegeCasapRevoca = aModel.mNumeroRegeCasapRevoca;
		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mAnnoOrdinanzaGE = aModel.mAnnoOrdinanzaGE;
		this.mNumeroOrdinanzaGE = aModel.mNumeroOrdinanzaGE;
		this.mDataOrdinanzaGE = aModel.mDataOrdinanzaGE;
		this.mEstremiCondono = aModel.mEstremiCondono;
		this.mIdPenaAccessoriaOrigine = aModel.mIdPenaAccessoriaOrigine;
		this.mCodNuovoTipoPenaAccessoria = aModel.mCodNuovoTipoPenaAccessoria;
		this.mDescrNuovoTipoPenaAccessoria = aModel.mDescrNuovoTipoPenaAccessoria;
		this.mNumeroEventiCorrelati = aModel.mNumeroEventiCorrelati;
		this.mCodTipoUfficioOrdinanzaGE = aModel.mCodTipoUfficioOrdinanzaGE;
		this.mDescrTipoUfficioOrdinanzaGE = aModel.mDescrTipoUfficioOrdinanzaGE;
		this.mCodLuogoUfficioOrdinanzaGE = aModel.mCodLuogoUfficioOrdinanzaGE;
		this.mDescrLuogoUfficioOrdinanzaGE = aModel.mDescrLuogoUfficioOrdinanzaGE;
		this.mDataOrdinanzaPA = aModel.mDataOrdinanzaPA;
		this.mAnnoOrdinanzaPA = aModel.mAnnoOrdinanzaPA;
		this.mNumeroOrdinanzaPA = aModel.mNumeroOrdinanzaPA;
		this.mCodTipoUfficioOrdinanzaPA = aModel.mCodTipoUfficioOrdinanzaPA;
		this.mDescrTipoUfficioOrdinanzaPA = aModel.mDescrTipoUfficioOrdinanzaPA;
		this.mCodLuogoUfficioOrdinanzaPA = aModel.mCodLuogoUfficioOrdinanzaPA;
		this.mDescrLuogoUfficioOrdinanzaPA = aModel.mDescrLuogoUfficioOrdinanzaPA;
		this.mDescrAltrePA = aModel.mDescrAltrePA; // 04/04/2006
		this.mCodFonteGE = aModel.mCodFonteGE; // 04/04/2006
		this.mDescrFonteGE = aModel.mDescrFonteGE; // 04/04/2006
		this.mAnnoFonteGE = aModel.mAnnoFonteGE; // 04/04/2006
		this.mNumeroFonteGE = aModel.mNumeroFonteGE; // 04/04/2006
		this.mCodSottonumerazioneGE = aModel.mCodSottonumerazioneGE; // 04/04/2006
		this.mDescrSottonumerazioneGE = aModel.mDescrSottonumerazioneGE; // 04/04/2006
		this.mCommaGE = aModel.mCommaGE; // 04/04/2006
		this.mLetteraGE = aModel.mLetteraGE; // 04/04/2006
		this.mNumeroGE = aModel.mNumeroGE; // 04/04/2006
		this.mArticoloGE = aModel.mArticoloGE; // 04/04/2006
		this.mBenIdBeneficio = aModel.mBenIdBeneficio;
	}

	// COSTRUTTORE MODEL
	public PenaAccessoriaModel(BigDecimal aIdPenaAccessoria, String aCodTipoPenaAccessoria,
			String aDescrTipoPenaAccessoria, String aDurata, String aDescrDurata, BigDecimal aNumAnni,
			BigDecimal aNumMesi, BigDecimal aNumGiorni, String aFlagCondonata, Date aDataDpr, String aNumDpr,
			String aFlagDichiarazioneFalsita, String aFlagRevocaCondono, Date aDataSentenzaRevoca,
			BigDecimal aAnnoSentenzaRevoca, String aNumeroSentenzaRevoca, String aCodTipoUfficioSentenzaRevo,
			String aDescrTipoUfficioSentenzaRevo, String aCodLuogoSentenzaRevoca,
			String aDescrLuogoSentenzaRevoca, BigDecimal aAnnoRegePmRevoca, String aNumeroRegePmRevoca,
			BigDecimal aAnnoRegeGipRevoca, String aNumeroRegeGipRevoca, BigDecimal aAnnoRegeDibRevoca,
			String aNumeroRegeDibRevoca, BigDecimal aAnnoRegeCasRevoca, String aNumeroRegeCasRevoca,
			BigDecimal aAnnoRegeCapRevoca, String aNumeroRegeCapRevoca, BigDecimal aAnnoRegeCasapRevoca,
			String aNumeroRegeCasapRevoca, String aNote, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aFasSieIdFascicoloSiep, Date aDataInizioValidita,
			Date aDataFineValidita, BigDecimal aAnnoOrdinanzaGE, BigDecimal aNumeroOrdinanzaGE,
			Date aDataOrdinanzaGE, String aEstremiCondono, BigDecimal aIdPenaAccessoriaOrigine,
			String aCodNuovoTipoPenaAccessoria, String aDescrNuovoTipoPenaAccessoria,
			BigDecimal aNumeroEventiCorrelati, String aCodTipoUfficioOrdinanzaGE,
			String aDescrTipoUfficioOrdinanzaGE, String aCodLuogoUfficioOrdinanzaGE,
			String aDescrLuogoUfficioOrdinanzaGE, Date aDataOrdinanzaPA, BigDecimal aAnnoOrdinanzaPA,
			BigDecimal aNumeroOrdinanzaPA, String aCodTipoUfficioOrdinanzaPA,
			String aDescrTipoUfficioOrdinanzaPA, String aCodLuogoUfficioOrdinanzaPA,
			String aDescrLuogoUfficioOrdinanzaPA, String aDescrAltrePA, // 04/04/2006
			String aCodFonteGE, // 04/04/2006
			String aDescrFonteGE, // 04/04/2006
			String aAnnoFonteGE, // 04/04/2006
			String aNumeroFonteGE, // 04/04/2006
			String aCodSottonumerazioneGE, // 04/04/2006
			String aDescrSottonumerazioneGE, // 04/04/2006
			String aCommaGE, // 04/04/2006
			String aLetteraGE, // 04/04/2006
			String aNumeroGE, // 04/04/2006
			String aArticoloGE, // 04/04/2006
			BigDecimal aBenIdBeneficio) {

		this.mIdPenaAccessoria = aIdPenaAccessoria;
		this.mCodTipoPenaAccessoria = aCodTipoPenaAccessoria;
		this.mDescrTipoPenaAccessoria = aDescrTipoPenaAccessoria;
		this.mDurata = aDurata;
		this.mDescrDurata = aDescrDurata;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mFlagCondonata = aFlagCondonata;
		this.mDataDpr = aDataDpr;
		this.mNumDpr = aNumDpr;
		this.mFlagDichiarazioneFalsita = aFlagDichiarazioneFalsita;
		this.mFlagRevocaCondono = aFlagRevocaCondono;
		this.mDataSentenzaRevoca = aDataSentenzaRevoca;
		this.mAnnoSentenzaRevoca = aAnnoSentenzaRevoca;
		this.mNumeroSentenzaRevoca = aNumeroSentenzaRevoca;
		this.mCodTipoUfficioSentenzaRevo = aCodTipoUfficioSentenzaRevo;
		this.mDescrTipoUfficioSentenzaRevo = aDescrTipoUfficioSentenzaRevo;
		this.mCodLuogoSentenzaRevoca = aCodLuogoSentenzaRevoca;
		this.mDescrLuogoSentenzaRevoca = aDescrLuogoSentenzaRevoca;
		this.mAnnoRegePmRevoca = aAnnoRegePmRevoca;
		this.mNumeroRegePmRevoca = aNumeroRegePmRevoca;
		this.mAnnoRegeGipRevoca = aAnnoRegeGipRevoca;
		this.mNumeroRegeGipRevoca = aNumeroRegeGipRevoca;
		this.mAnnoRegeDibRevoca = aAnnoRegeDibRevoca;
		this.mNumeroRegeDibRevoca = aNumeroRegeDibRevoca;
		this.mAnnoRegeCasRevoca = aAnnoRegeCasRevoca;
		this.mNumeroRegeCasRevoca = aNumeroRegeCasRevoca;
		this.mAnnoRegeCapRevoca = aAnnoRegeCapRevoca;
		this.mNumeroRegeCapRevoca = aNumeroRegeCapRevoca;
		this.mAnnoRegeCasapRevoca = aAnnoRegeCasapRevoca;
		this.mNumeroRegeCasapRevoca = aNumeroRegeCasapRevoca;
		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mAnnoOrdinanzaGE = aAnnoOrdinanzaGE;
		this.mNumeroOrdinanzaGE = aNumeroOrdinanzaGE;
		this.mDataOrdinanzaGE = aDataOrdinanzaGE;
		this.mEstremiCondono = aEstremiCondono;
		this.mIdPenaAccessoriaOrigine = aIdPenaAccessoriaOrigine;
		this.mCodNuovoTipoPenaAccessoria = aCodNuovoTipoPenaAccessoria;
		this.mDescrNuovoTipoPenaAccessoria = aDescrNuovoTipoPenaAccessoria;
		this.mNumeroEventiCorrelati = aNumeroEventiCorrelati;
		this.mCodTipoUfficioOrdinanzaGE = aCodTipoUfficioOrdinanzaGE;
		this.mDescrTipoUfficioOrdinanzaGE = aDescrTipoUfficioOrdinanzaGE;
		this.mCodLuogoUfficioOrdinanzaGE = aCodLuogoUfficioOrdinanzaGE;
		this.mDescrLuogoUfficioOrdinanzaGE = aDescrLuogoUfficioOrdinanzaGE;
		this.mDataOrdinanzaPA = aDataOrdinanzaPA;
		this.mAnnoOrdinanzaPA = aAnnoOrdinanzaPA;
		this.mNumeroOrdinanzaPA = aNumeroOrdinanzaPA;
		this.mCodTipoUfficioOrdinanzaPA = aCodTipoUfficioOrdinanzaPA;
		this.mDescrTipoUfficioOrdinanzaPA = aDescrTipoUfficioOrdinanzaPA;
		this.mCodLuogoUfficioOrdinanzaPA = aCodLuogoUfficioOrdinanzaPA;
		this.mDescrLuogoUfficioOrdinanzaPA = aDescrLuogoUfficioOrdinanzaPA;
		this.mDescrAltrePA = aDescrAltrePA; // 04/04/2006
		this.mCodFonteGE = aCodFonteGE; // 04/04/2006
		this.mDescrFonteGE = aDescrFonteGE; // 04/04/2006
		this.mAnnoFonteGE = aAnnoFonteGE; // 04/04/2006
		this.mNumeroFonteGE = aNumeroFonteGE; // 04/04/2006
		this.mCodSottonumerazioneGE = aCodSottonumerazioneGE; // 04/04/2006
		this.mDescrSottonumerazioneGE = aDescrSottonumerazioneGE; // 04/04/2006
		this.mCommaGE = aCommaGE; // 04/04/2006
		this.mLetteraGE = aLetteraGE; // 04/04/2006
		this.mNumeroGE = aNumeroGE; // 04/04/2006
		this.mArticoloGE = aArticoloGE; // 04/04/2006
		this.mBenIdBeneficio = aBenIdBeneficio;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdPenaAccessoria() {
		return mIdPenaAccessoria;
	}

	public String getCodTipoPenaAccessoria() {
		return mCodTipoPenaAccessoria;
	}

	public String getDescrTipoPenaAccessoria() {
		return mDescrTipoPenaAccessoria;
	}

	public String getDurata() {
		return mDurata;
	}

	public String getDescrDurata() {
		return mDescrDurata;
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

	public String getFlagCondonata() {
		return mFlagCondonata;
	}

	public Date getDataDpr() {
		return mDataDpr;
	}

	public String getNumDpr() {
		return mNumDpr;
	}

	public String getFlagDichiarazioneFalsita() {
		return mFlagDichiarazioneFalsita;
	}

	public String getFlagRevocaCondono() {
		return mFlagRevocaCondono;
	}

	public Date getDataSentenzaRevoca() {
		return mDataSentenzaRevoca;
	}

	public BigDecimal getAnnoSentenzaRevoca() {
		return mAnnoSentenzaRevoca;
	}

	public String getNumeroSentenzaRevoca() {
		return mNumeroSentenzaRevoca;
	}

	public String getCodTipoUfficioSentenzaRevo() {
		return mCodTipoUfficioSentenzaRevo;
	}

	public String getDescrTipoUfficioSentenzaRevo() {
		return mDescrTipoUfficioSentenzaRevo;
	}

	public String getCodLuogoSentenzaRevoca() {
		return mCodLuogoSentenzaRevoca;
	}

	public String getDescrLuogoSentenzaRevoca() {
		return mDescrLuogoSentenzaRevoca;
	}

	public BigDecimal getAnnoRegePmRevoca() {
		return mAnnoRegePmRevoca;
	}

	public String getNumeroRegePmRevoca() {
		return mNumeroRegePmRevoca;
	}

	public BigDecimal getAnnoRegeGipRevoca() {
		return mAnnoRegeGipRevoca;
	}

	public String getNumeroRegeGipRevoca() {
		return mNumeroRegeGipRevoca;
	}

	public BigDecimal getAnnoRegeDibRevoca() {
		return mAnnoRegeDibRevoca;
	}

	public String getNumeroRegeDibRevoca() {
		return mNumeroRegeDibRevoca;
	}

	public BigDecimal getAnnoRegeCasRevoca() {
		return mAnnoRegeCasRevoca;
	}

	public String getNumeroRegeCasRevoca() {
		return mNumeroRegeCasRevoca;
	}

	public BigDecimal getAnnoRegeCapRevoca() {
		return mAnnoRegeCapRevoca;
	}

	public String getNumeroRegeCapRevoca() {
		return mNumeroRegeCapRevoca;
	}

	public BigDecimal getAnnoRegeCasapRevoca() {
		return mAnnoRegeCasapRevoca;
	}

	public String getNumeroRegeCasapRevoca() {
		return mNumeroRegeCasapRevoca;
	}

	public String getNote() {
		return mNote;
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

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public BigDecimal getAnnoOrdinanzaGE() {
		return mAnnoOrdinanzaGE;
	}

	public BigDecimal getNumeroOrdinanzaGE() {
		return mNumeroOrdinanzaGE;
	}

	public Date getDataOrdinanzaGE() {
		return mDataOrdinanzaGE;
	}

	public String getEstremiCondono() {
		return mEstremiCondono;
	}

	public BigDecimal getIdPenaAccessoriaOrigine() {
		return mIdPenaAccessoriaOrigine;
	}

	public String getCodNuovoTipoPenaAccessoria() {
		return mCodNuovoTipoPenaAccessoria;
	}

	public String getDescrNuovoTipoPenaAccessoria() {
		return mDescrNuovoTipoPenaAccessoria;
	}

	public BigDecimal getNumeroEventiCorrelati() {
		return mNumeroEventiCorrelati;
	}

	public String getCodTipoUfficioOrdinanzaGE() {
		return mCodTipoUfficioOrdinanzaGE;
	}

	public String getDescrTipoUfficioOrdinanzaGE() {
		return mDescrTipoUfficioOrdinanzaGE;
	}

	public String getCodLuogoUfficioOrdinanzaGE() {
		return mCodLuogoUfficioOrdinanzaGE;
	}

	public String getDescrLuogoUfficioOrdinanzaGE() {
		return mDescrLuogoUfficioOrdinanzaGE;
	}

	public Date getDataOrdinanzaPA() {
		return mDataOrdinanzaPA;
	}

	public BigDecimal getAnnoOrdinanzaPA() {
		return mAnnoOrdinanzaPA;
	}

	public BigDecimal getNumeroOrdinanzaPA() {
		return mNumeroOrdinanzaPA;
	}

	public String getCodTipoUfficioOrdinanzaPA() {
		return mCodTipoUfficioOrdinanzaPA;
	}

	public String getDescrTipoUfficioOrdinanzaPA() {
		return mDescrTipoUfficioOrdinanzaPA;
	}

	public String getCodLuogoUfficioOrdinanzaPA() {
		return mCodLuogoUfficioOrdinanzaPA;
	}

	public String getDescrLuogoUfficioOrdinanzaPA() {
		return mDescrLuogoUfficioOrdinanzaPA;
	}

	public String getDescrAltrePA() {
		return mDescrAltrePA;
	} // 04/04/2006

	public String getCodFonteGE() {
		return mCodFonteGE;
	} // 04/04/2006

	public String getDescrFonteGE() {
		return mDescrFonteGE;
	} // 04/04/2006

	public String getAnnoFonteGE() {
		return mAnnoFonteGE;
	} // 04/04/2006

	public String getNumeroFonteGE() {
		return mNumeroFonteGE;
	} // 04/04/2006

	public String getCodSottonumerazioneGE() {
		return mCodSottonumerazioneGE;
	} // 04/04/2006

	public String getDescrSottonumerazioneGE() {
		return mDescrSottonumerazioneGE;
	} // 04/04/2006

	public String getCommaGE() {
		return mCommaGE;
	} // 04/04/2006

	public String getLetteraGE() {
		return mLetteraGE;
	} // 04/04/2006

	public String getNumeroGE() {
		return mNumeroGE;
	} // 04/04/2006

	public String getArticoloGE() {
		return mArticoloGE;
	} // 04/04/2006

	public BigDecimal getBenIdBeneficio() {
		return mBenIdBeneficio;
	}

	//
	// METODI SET()
	//

	public void setIdPenaAccessoria(BigDecimal aValore) {
		mIdPenaAccessoria = aValore;
	}

	public void setCodTipoPenaAccessoria(String aValore) {
		mCodTipoPenaAccessoria = aValore;
	}

	public void setDescrTipoPenaAccessoria(String aValore) {
		mDescrTipoPenaAccessoria = aValore;
	}

	public void setDurata(String aValore) {
		mDurata = aValore;
	}

	public void setDescrDurata(String aValore) {
		mDescrDurata = aValore;
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

	public void setFlagCondonata(String aValore) {
		mFlagCondonata = aValore;
	}

	public void setDataDpr(Date aValore) {
		mDataDpr = aValore;
	}

	public void setNumDpr(String aValore) {
		mNumDpr = aValore;
	}

	public void setFlagDichiarazioneFalsita(String aValore) {
		mFlagDichiarazioneFalsita = aValore;
	}

	public void setFlagRevocaCondono(String aValore) {
		mFlagRevocaCondono = aValore;
	}

	public void setDataSentenzaRevoca(Date aValore) {
		mDataSentenzaRevoca = aValore;
	}

	public void setAnnoSentenzaRevoca(BigDecimal aValore) {
		mAnnoSentenzaRevoca = aValore;
	}

	public void setNumeroSentenzaRevoca(String aValore) {
		mNumeroSentenzaRevoca = aValore;
	}

	public void setCodTipoUfficioSentenzaRevo(String aValore) {
		mCodTipoUfficioSentenzaRevo = aValore;
	}

	public void setDescrTipoUfficioSentenzaRevo(String aValore) {
		mDescrTipoUfficioSentenzaRevo = aValore;
	}

	public void setCodLuogoSentenzaRevoca(String aValore) {
		mCodLuogoSentenzaRevoca = aValore;
	}

	public void setDescrLuogoSentenzaRevoca(String aValore) {
		mDescrLuogoSentenzaRevoca = aValore;
	}

	public void setAnnoRegePmRevoca(BigDecimal aValore) {
		mAnnoRegePmRevoca = aValore;
	}

	public void setNumeroRegePmRevoca(String aValore) {
		mNumeroRegePmRevoca = aValore;
	}

	public void setAnnoRegeGipRevoca(BigDecimal aValore) {
		mAnnoRegeGipRevoca = aValore;
	}

	public void setNumeroRegeGipRevoca(String aValore) {
		mNumeroRegeGipRevoca = aValore;
	}

	public void setAnnoRegeDibRevoca(BigDecimal aValore) {
		mAnnoRegeDibRevoca = aValore;
	}

	public void setNumeroRegeDibRevoca(String aValore) {
		mNumeroRegeDibRevoca = aValore;
	}

	public void setAnnoRegeCasRevoca(BigDecimal aValore) {
		mAnnoRegeCasRevoca = aValore;
	}

	public void setNumeroRegeCasRevoca(String aValore) {
		mNumeroRegeCasRevoca = aValore;
	}

	public void setAnnoRegeCapRevoca(BigDecimal aValore) {
		mAnnoRegeCapRevoca = aValore;
	}

	public void setNumeroRegeCapRevoca(String aValore) {
		mNumeroRegeCapRevoca = aValore;
	}

	public void setAnnoRegeCasapRevoca(BigDecimal aValore) {
		mAnnoRegeCasapRevoca = aValore;
	}

	public void setNumeroRegeCasapRevoca(String aValore) {
		mNumeroRegeCasapRevoca = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
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

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setAnnoOrdinanzaGE(BigDecimal aValore) {
		mAnnoOrdinanzaGE = aValore;
	}

	public void setNumeroOrdinanzaGE(BigDecimal aValore) {
		mNumeroOrdinanzaGE = aValore;
	}

	public void setDataOrdinanzaGE(Date aValore) {
		mDataOrdinanzaGE = aValore;
	}

	public void setEstremiCondono(String aValore) {
		mEstremiCondono = aValore;
	}

	public void setIdPenaAccessoriaOrigine(BigDecimal aValore) {
		mIdPenaAccessoriaOrigine = aValore;
	}

	public void setCodNuovoTipoPenaAccessoria(String aValore) {
		mCodNuovoTipoPenaAccessoria = aValore;
	}

	public void setDescrNuovoTipoPenaAccessoria(String aValore) {
		mDescrNuovoTipoPenaAccessoria = aValore;
	}

	public void setNumeroEventiCorrelati(BigDecimal aValore) {
		mNumeroEventiCorrelati = aValore;
	}

	public void setCodTipoUfficioOrdinanzaGE(String aValore) {
		mCodTipoUfficioOrdinanzaGE = aValore;
	}

	public void setDescrTipoUfficioOrdinanzaGE(String aValore) {
		mDescrTipoUfficioOrdinanzaGE = aValore;
	}

	public void setCodLuogoUfficioOrdinanzaGE(String aValore) {
		mCodLuogoUfficioOrdinanzaGE = aValore;
	}

	public void setDescrLuogoUfficioOrdinanzaGE(String aValore) {
		mDescrLuogoUfficioOrdinanzaGE = aValore;
	}

	public void setDataOrdinanzaPA(Date aValore) {
		mDataOrdinanzaPA = aValore;
	}

	public void setAnnoOrdinanzaPA(BigDecimal aValore) {
		mAnnoOrdinanzaPA = aValore;
	}

	public void setNumeroOrdinanzaPA(BigDecimal aValore) {
		mNumeroOrdinanzaPA = aValore;
	}

	public void setCodTipoUfficioOrdinanzaPA(String aValore) {
		mCodTipoUfficioOrdinanzaPA = aValore;
	}

	public void setDescrTipoUfficioOrdinanzaPA(String aValore) {
		mDescrTipoUfficioOrdinanzaPA = aValore;
	}

	public void setCodLuogoUfficioOrdinanzaPA(String aValore) {
		mCodLuogoUfficioOrdinanzaPA = aValore;
	}

	public void setDescrLuogoUfficioOrdinanzaPA(String aValore) {
		mDescrLuogoUfficioOrdinanzaPA = aValore;
	}

	public void setDescrAltrePA(String aValore) {
		mDescrAltrePA = aValore;
	} // 04/04/2006

	public void setCodFonteGE(String aValore) {
		mCodFonteGE = aValore;
	} // 04/04/2006

	public void setDescrFonteGE(String aValore) {
		mDescrFonteGE = aValore;
	} // 04/04/2006

	public void setAnnoFonteGE(String aValore) {
		mAnnoFonteGE = aValore;
	} // 04/04/2006

	public void setNumeroFonteGE(String aValore) {
		mNumeroFonteGE = aValore;
	} // 04/04/2006

	public void setCodSottonumerazioneGE(String aValore) {
		mCodSottonumerazioneGE = aValore;
	} // 04/04/2006

	public void setDescrSottonumerazioneGE(String aValore) {
		mDescrSottonumerazioneGE = aValore;
	} // 04/04/2006

	public void setCommaGE(String aValore) {
		mCommaGE = aValore;
	} // 04/04/2006

	public void setLetteraGE(String aValore) {
		mLetteraGE = aValore;
	} // 04/04/2006

	public void setNumeroGE(String aValore) {
		mNumeroGE = aValore;
	} // 04/04/2006

	public void setArticoloGE(String aValore) {
		mArticoloGE = aValore;
	} // 04/04/2006

	public void setBenIdBeneficio(BigDecimal aValore) {
		mBenIdBeneficio = aValore;
	}

	@Override
	public String toString() {

		String lStr = new String();

		lStr = "" + mIdPenaAccessoria + " - " + mCodTipoPenaAccessoria + " - " + mDescrTipoPenaAccessoria
				+ " - " + mDurata + " - " + mDescrDurata + " - " + mNumAnni + " - " + mNumMesi + " - "
				+ mNumGiorni + " - " + mFlagCondonata + " - " + mDataDpr + " - " + mNumDpr + " - "
				+ mFlagDichiarazioneFalsita + " - " + mFlagRevocaCondono + " - " + mDataSentenzaRevoca + " - "
				+ mAnnoSentenzaRevoca + " - " + mNumeroSentenzaRevoca + " - " + mCodTipoUfficioSentenzaRevo
				+ " - " + mDescrTipoUfficioSentenzaRevo + " - " + mCodLuogoSentenzaRevoca + " - "
				+ mDescrLuogoSentenzaRevoca + " - " + mAnnoRegePmRevoca + " - " + mNumeroRegePmRevoca + " - "
				+ mAnnoRegeGipRevoca + " - " + mNumeroRegeGipRevoca + " - " + mAnnoRegeDibRevoca + " - "
				+ mNumeroRegeDibRevoca + " - " + mAnnoRegeCasRevoca + " - " + mNumeroRegeCasRevoca + " - "
				+ mAnnoRegeCapRevoca + " - " + mNumeroRegeCapRevoca + " - " + mAnnoRegeCasapRevoca + " - "
				+ mNumeroRegeCasapRevoca + " - " + mNote + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mFasSieIdFascicoloSiep + " - "
				+ mDataInizioValidita + " - " + mDataFineValidita + " - " + mAnnoOrdinanzaGE + " - "
				+ mNumeroOrdinanzaGE + " - " + mDataOrdinanzaGE + " - " + mEstremiCondono + " - "
				+ mIdPenaAccessoriaOrigine + " - " + mCodNuovoTipoPenaAccessoria + " - "
				+ mDescrNuovoTipoPenaAccessoria + " - " + mNumeroEventiCorrelati + " - "
				+ mCodTipoUfficioOrdinanzaGE + " - " + mCodLuogoUfficioOrdinanzaGE + " - " + mDataOrdinanzaPA
				+ " - " + mAnnoOrdinanzaPA + " - " + mNumeroOrdinanzaPA + " - " + mCodTipoUfficioOrdinanzaPA
				+ " - " + mCodLuogoUfficioOrdinanzaPA + " - " + mDescrAltrePA + " - " + mCodFonteGE + " - "
				+ mDescrFonteGE + " - " + mAnnoFonteGE + " - " + mNumeroFonteGE + " - "
				+ mCodSottonumerazioneGE + " - " + mDescrSottonumerazioneGE + " - " + mCommaGE + " - "
				+ mLetteraGE + " - " + mNumeroGE + " - " + mArticoloGE + " - " + mBenIdBeneficio;

		return lStr;
	}

}