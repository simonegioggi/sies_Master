package siap.sico.misuraalternativa.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * MisuraAlternativaModel - Classe Model che rappresenta il MisuraAlternativa
 *
 * @version 1.0
 */
public class MisuraAlternativaModel extends GenericModel {

	private static final long serialVersionUID = -265010365629056250L;

	private BigDecimal mIdMisuraAlternativa;
	private String mCodTipoDecisione;
	private String mDescrTipoDecisione;
	private String mCodNaturaDecisione;
	private String mDescrNaturaDecisione;
	private String mCodTipoMisura;
	private String mDescrTipoMisura;
	private Date mDataDecisione;
	private String mCodMagistrato;
	private String mDescrMagistrato;
	private String mCodUfficioSorveglianza;
	private String mDescrUfficioSorveglianza;
	private BigDecimal mCssIdCssa;
	private String mDescrLuogoProva;
	private BigDecimal mNumAnniMisura;
	private BigDecimal mNumMesiMisura;
	private BigDecimal mNumGiorniMisura;
	private Date mDataInizioMisura;
	private Date mDataFineMisura;
	// Gdv
	private Date mDataFineMisuraPiuUno;
	private BigDecimal mChiaveAnnoFascicoloSius;
	private String mChiaveUfficioFascicoloSius;
	private BigDecimal mChiaveProgrFascicoloSius;
	private BigDecimal mAnnoRegistro;
	private BigDecimal mNumeroRegistro;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mEveIdEvento;
	private String mNote;
	private Date mDataScarcerazione;
	private Date mDataIngressoIstituto;
	private String mStringaReclusione;
	private String mCodTipoUfficioScarcerazione;
	private String mFlagUfficioInserimento;
	private String mDescrChiaveUfficioFascicoloSius;
	private String mFlagCheck;
	// --- GDV aggiunto per la stampa
	private String mMisuraAlternativaCorrente;
	// --- GDV
	private String mLegge;
	private String mStringaRevocaArresto;
	private String mStringaRevocaReclusione;
	private String mStringaMisura;
	private BigDecimal mNumAnniRevocaReclusione;
	private BigDecimal mNumMesiRevocaReclusione;
	private BigDecimal mNumGiorniRevocaReclusione;
	private BigDecimal mNumAnniRevocaArresto;
	private BigDecimal mNumMesiRevocaArresto;
	private BigDecimal mNumGiorniRevocaArresto;
	private Date mDataInizioRevoca;
	private String mFlagPeriodoEspiato;
	private BigDecimal mAnnoAltroTitolo;
	private String mNumAltroTitolo;
	private Date mDataAltroTitolo;
	private String mCodLuogoAltroTitolo;
	private String mCodAutoritaAltroTitolo;
	private String mDescAutoritaAltroTitolo;
	private String mDescLuogoAltroTitolo;
	private Date mDataScadenzaProroga;
	private String mFlagDecisioneTribunale;
	private String mCodTdsCompetente;
	private String mFlagSituazione;
	private String mDescTdsCompetente;
	private String mDescSedeTdsCompetente;
	// DL 146/2013 nuovi campi aggiunti per gestire la registrazione della MA
	// concessa su altro titolo e di cui si dispone la prosecuzione
	// n.b. MaAt sta per Misura alternativa altro titolo
	private String mCodTipoDecisioneMaAt;
	private String mDescrTipoDecisioneMaAt;
	private String mCodTipoMisuraMaAt;
	private String mDescrTipoMisuraMaAt;
	private Date mDataDecisioneMaAt;
	private BigDecimal mChiaveAnnoFascicoloSiusMaAt;
	private BigDecimal mChiaveProgrFascicoloSiusMaAt;
	private String mChiaveUfficioFascicoloSiusMaAt;
	private String mDescrChiaveUfficioFascicoloSiusMaAt;
	private BigDecimal mAnnoRegistroMaAt;
	private BigDecimal mNumeroRegistroMaAt;
	private String mIs51Bis;
	private BigDecimal mFlFormaMisura;
	private String mDescrizioneComunita;
	// MEV_9
	private Date mDataEsecutivita;

	// COSTRUTTORE DI DEFAULT
	public MisuraAlternativaModel() {

		this.mIdMisuraAlternativa = null;
		this.mCodTipoDecisione = "";
		this.mDescrTipoDecisione = "";
		this.mCodNaturaDecisione = "";
		this.mDescrNaturaDecisione = "";
		this.mCodTipoMisura = "";
		this.mDescrTipoMisura = "";
		this.mDataDecisione = null;
		this.mCodMagistrato = "";
		this.mDescrMagistrato = "";
		this.mCodUfficioSorveglianza = "";
		this.mDescrUfficioSorveglianza = "";
		this.mCssIdCssa = null;
		this.mDescrLuogoProva = "";
		this.mNumAnniMisura = null;
		this.mNumMesiMisura = null;
		this.mNumGiorniMisura = null;
		this.mDataInizioMisura = null;
		this.mDataFineMisura = null;
		this.mChiaveAnnoFascicoloSius = null;
		this.mChiaveUfficioFascicoloSius = "";
		this.mChiaveProgrFascicoloSius = null;
		this.mAnnoRegistro = null;
		this.mNumeroRegistro = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mEveIdEvento = null;
		this.mNote = "";
		this.mDataScarcerazione = null;
		this.mDataIngressoIstituto = null;
		this.mMisuraAlternativaCorrente = null;
		this.mDataFineMisuraPiuUno = null;
		this.mCodTipoUfficioScarcerazione = "";
		this.mDescrChiaveUfficioFascicoloSius = "";
		this.mFlagUfficioInserimento = "";
		this.mNumAnniRevocaReclusione = null;
		this.mNumMesiRevocaReclusione = null;
		this.mNumGiorniRevocaReclusione = null;
		this.mNumAnniRevocaArresto = null;
		this.mNumMesiRevocaArresto = null;
		this.mNumGiorniRevocaArresto = null;
		this.mDataInizioRevoca = null;
		this.mFlagPeriodoEspiato = "";
		this.mAnnoAltroTitolo = null;
		this.mNumAltroTitolo = "";
		this.mDataAltroTitolo = null;
		this.mCodLuogoAltroTitolo = "-";
		this.mCodAutoritaAltroTitolo = "-";
		this.mDescAutoritaAltroTitolo = "";
		this.mDescLuogoAltroTitolo = "";
		this.mLegge = "";
		this.mFlagCheck = "N";
		this.mDataScadenzaProroga = null;
		this.mFlagDecisioneTribunale = "";
		this.mCodTdsCompetente = "-";
		this.mFlagSituazione = "";
		this.mDescTdsCompetente = "";
		this.mDescSedeTdsCompetente = "";
		this.mCodTipoDecisioneMaAt = "";
		this.mDescrTipoDecisioneMaAt = "";
		this.mCodTipoMisuraMaAt = "";
		this.mDescrTipoMisuraMaAt = "";
		this.mDataDecisioneMaAt = null;
		this.mChiaveAnnoFascicoloSiusMaAt = null;
		this.mChiaveProgrFascicoloSiusMaAt = null;
		this.mChiaveUfficioFascicoloSiusMaAt = "";
		this.mDescrChiaveUfficioFascicoloSiusMaAt = "";
		this.mAnnoRegistroMaAt = null;
		this.mNumeroRegistroMaAt = null;
		// MEV_9
		this.mDataEsecutivita = null;
	}

	// COSTRUTTORE DI COPIA
	public MisuraAlternativaModel(MisuraAlternativaModel aModel) {

		this.mIdMisuraAlternativa = aModel.mIdMisuraAlternativa;
		this.mCodTipoDecisione = aModel.mCodTipoDecisione;
		this.mDescrTipoDecisione = aModel.mDescrTipoDecisione;
		this.mCodNaturaDecisione = aModel.mCodNaturaDecisione;
		this.mDescrNaturaDecisione = aModel.mDescrNaturaDecisione;
		this.mCodTipoMisura = aModel.mCodTipoMisura;
		this.mDescrTipoMisura = aModel.mDescrTipoMisura;
		this.mDataDecisione = aModel.mDataDecisione;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mDescrMagistrato = aModel.mDescrMagistrato;
		this.mCodUfficioSorveglianza = aModel.mCodUfficioSorveglianza;
		this.mDescrUfficioSorveglianza = aModel.mDescrUfficioSorveglianza;
		this.mCssIdCssa = aModel.mCssIdCssa;
		this.mDescrLuogoProva = aModel.mDescrLuogoProva;
		this.mNumAnniMisura = aModel.mNumAnniMisura;
		this.mNumMesiMisura = aModel.mNumMesiMisura;
		this.mNumGiorniMisura = aModel.mNumGiorniMisura;
		this.mDataInizioMisura = aModel.mDataInizioMisura;
		this.mDataFineMisura = aModel.mDataFineMisura;
		this.mChiaveAnnoFascicoloSius = aModel.mChiaveAnnoFascicoloSius;
		this.mChiaveUfficioFascicoloSius = aModel.mChiaveUfficioFascicoloSius;
		this.mChiaveProgrFascicoloSius = aModel.mChiaveProgrFascicoloSius;
		this.mAnnoRegistro = aModel.mAnnoRegistro;
		this.mNumeroRegistro = aModel.mNumeroRegistro;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mNote = aModel.mNote;
		this.mDataScarcerazione = aModel.mDataScarcerazione;
		this.mDataIngressoIstituto = aModel.mDataIngressoIstituto;
		this.mMisuraAlternativaCorrente = aModel.mMisuraAlternativaCorrente;
		this.mDataFineMisuraPiuUno = aModel.mDataFineMisuraPiuUno;
		this.mCodTipoUfficioScarcerazione = aModel.mCodTipoUfficioScarcerazione;
		this.mDescrChiaveUfficioFascicoloSius = aModel.mDescrChiaveUfficioFascicoloSius;
		this.mFlagUfficioInserimento = aModel.mFlagUfficioInserimento;
		this.mNumAnniRevocaReclusione = aModel.mNumAnniRevocaReclusione;
		this.mNumMesiRevocaReclusione = aModel.mNumMesiRevocaReclusione;
		this.mNumGiorniRevocaReclusione = aModel.mNumGiorniRevocaReclusione;
		this.mNumAnniRevocaArresto = aModel.mNumAnniRevocaArresto;
		this.mNumMesiRevocaArresto = aModel.mNumMesiRevocaArresto;
		this.mNumGiorniRevocaArresto = aModel.mNumGiorniRevocaArresto;
		this.mDataInizioRevoca = aModel.mDataInizioRevoca;
		this.mFlagPeriodoEspiato = aModel.mFlagPeriodoEspiato;
		this.mAnnoAltroTitolo = aModel.mAnnoAltroTitolo;
		this.mNumAltroTitolo = aModel.mNumAltroTitolo;
		this.mDataAltroTitolo = aModel.mDataAltroTitolo;
		this.mCodLuogoAltroTitolo = aModel.mCodLuogoAltroTitolo;
		this.mCodAutoritaAltroTitolo = aModel.mCodAutoritaAltroTitolo;
		this.mDescLuogoAltroTitolo = aModel.mDescLuogoAltroTitolo;
		this.mDescAutoritaAltroTitolo = aModel.mDescAutoritaAltroTitolo;
		this.mFlagCheck = aModel.mFlagCheck;
		this.mLegge = aModel.mLegge;
		this.mDataScadenzaProroga = aModel.mDataScadenzaProroga;
		this.mFlagDecisioneTribunale = aModel.mFlagDecisioneTribunale;
		this.mCodTdsCompetente = aModel.mCodTdsCompetente;
		this.mFlagSituazione = aModel.mFlagSituazione;
		this.mDescTdsCompetente = aModel.mDescTdsCompetente;
		this.mDescSedeTdsCompetente = aModel.mDescSedeTdsCompetente;
		this.mCodTipoDecisioneMaAt = aModel.mCodTipoDecisioneMaAt;
		this.mDescrTipoDecisioneMaAt = aModel.mDescrTipoDecisioneMaAt;
		this.mCodTipoMisuraMaAt = aModel.mCodTipoMisuraMaAt;
		this.mDescrTipoMisuraMaAt = aModel.mDescrTipoMisuraMaAt;
		this.mDataDecisioneMaAt = aModel.mDataDecisioneMaAt;
		this.mChiaveAnnoFascicoloSiusMaAt = aModel.mChiaveAnnoFascicoloSiusMaAt;
		this.mChiaveProgrFascicoloSiusMaAt = aModel.mChiaveProgrFascicoloSiusMaAt;
		this.mChiaveUfficioFascicoloSiusMaAt = aModel.mChiaveUfficioFascicoloSiusMaAt;
		this.mDescrChiaveUfficioFascicoloSiusMaAt = aModel.mDescrChiaveUfficioFascicoloSiusMaAt;
		this.mAnnoRegistroMaAt = aModel.mAnnoRegistroMaAt;
		this.mNumeroRegistroMaAt = aModel.mNumeroRegistroMaAt;
		this.mFlFormaMisura = aModel.mFlFormaMisura;
		this.mDescrizioneComunita = aModel.mDescrizioneComunita;
		// MEV_9
		this.mDataEsecutivita = aModel.mDataEsecutivita;
	}

	// COSTRUTTORE MODEL
	public MisuraAlternativaModel(BigDecimal aIdMisuraAlternativa, String aCodTipoDecisione,
			String aDescrTipoDecisione, String aCodNaturaDecisione, String aDescrNaturaDecisione,
			String aCodTipoMisura, String aDescrTipoMisura, Date aDataDecisione, String aCodMagistrato,
			String aDescrMagistrato, String aCodUfficioSorveglianza, String aDescrUfficioSorveglianza,
			BigDecimal aCssIdCssa, String aDescrLuogoProva, BigDecimal aNumAnniMisura,
			BigDecimal aNumMesiMisura, BigDecimal aNumGiorniMisura, Date aDataInizioMisura,
			Date aDataFineMisura, BigDecimal aChiaveAnnoFascicoloSius, String aChiaveUfficioFascicoloSius,
			BigDecimal aChiaveProgrFascicoloSius, BigDecimal aAnnoRegistro, BigDecimal aNumeroRegistro,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aEveIdEvento, String aNote, Date aDataScarcerazione,
			Date aDataIngressoIstituto, String aCodTipoUfficioScarcerazione, String aFlagUfficioInserimento,
			BigDecimal aNumAnniRevocaReclusione, BigDecimal aNumMesiRevocaReclusione,
			BigDecimal aNumGiorniRevocaReclusione, BigDecimal aNumAnniRevocaArresto,
			BigDecimal aNumMesiRevocaArresto, BigDecimal aNumGiorniRevocaArresto, Date aDataInizioRevoca,
			String aFlagPeriodoEspiato, BigDecimal aAnnoAltroTitolo, String aNumAltroTitolo,
			Date aDataAltroTitolo, String aCodLuogoAltroTitolo, String aCodAutoritaAltroTitolo,
			Date aDataScadenzaProroga, String aFlagDecisioneTribunale, String aCodTdsCompetente,
			String aFlagSituazione, String aDescTdsCompetente, String aDescSedeTdsCompetente,
			String aCodTipoDecisioneMaAt, String aDescrTipoDecisioneMaAt, String aCodTipoMisuraMaAt,
			String aDescrTipoMisuraMaAt, Date aDataDecisioneMaAt, BigDecimal aChiaveAnnoFascicoloSiusMaAt,
			BigDecimal aChiaveProgrFascicoloSiusMaAt, String aChiaveUfficioFascicoloSiusMaAt,
			String aDescrChiaveUfficioFascicoloSiusMaAt, BigDecimal aAnnoRegistroMaAt,
			BigDecimal aNumeroRegistroMaAt, BigDecimal flFormaMisura, String descrizioneComunita,
			// MEV_9
			Date aDataEsecutivita) {

		this.mIdMisuraAlternativa = aIdMisuraAlternativa;
		this.mCodTipoDecisione = aCodTipoDecisione;
		this.mDescrTipoDecisione = aDescrTipoDecisione;
		this.mCodNaturaDecisione = aCodNaturaDecisione;
		this.mDescrNaturaDecisione = aDescrNaturaDecisione;
		this.mCodTipoMisura = aCodTipoMisura;
		this.mDescrTipoMisura = aDescrTipoMisura;
		this.mDataDecisione = aDataDecisione;
		this.mCodMagistrato = aCodMagistrato;
		this.mDescrMagistrato = aDescrMagistrato;
		this.mCodUfficioSorveglianza = aCodUfficioSorveglianza;
		this.mDescrUfficioSorveglianza = aDescrUfficioSorveglianza;
		this.mCssIdCssa = aCssIdCssa;
		this.mDescrLuogoProva = aDescrLuogoProva;
		this.mNumAnniMisura = aNumAnniMisura;
		this.mNumMesiMisura = aNumMesiMisura;
		this.mNumGiorniMisura = aNumGiorniMisura;
		this.mDataInizioMisura = aDataInizioMisura;
		this.mDataFineMisura = aDataFineMisura;
		this.mChiaveAnnoFascicoloSius = aChiaveAnnoFascicoloSius;
		this.mChiaveUfficioFascicoloSius = aChiaveUfficioFascicoloSius;
		this.mChiaveProgrFascicoloSius = aChiaveProgrFascicoloSius;
		this.mAnnoRegistro = aAnnoRegistro;
		this.mNumeroRegistro = aNumeroRegistro;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mEveIdEvento = aEveIdEvento;
		this.mNote = aNote;
		this.mDataScarcerazione = aDataScarcerazione;
		this.mDataIngressoIstituto = aDataIngressoIstituto;
		this.mCodTipoUfficioScarcerazione = aCodTipoUfficioScarcerazione;
		this.mFlagUfficioInserimento = aFlagUfficioInserimento;
		this.mNumAnniRevocaReclusione = aNumAnniRevocaReclusione;
		this.mNumMesiRevocaReclusione = aNumMesiRevocaReclusione;
		this.mNumGiorniRevocaReclusione = aNumGiorniRevocaReclusione;
		this.mNumAnniRevocaArresto = aNumAnniRevocaArresto;
		this.mNumMesiRevocaArresto = aNumMesiRevocaArresto;
		this.mNumGiorniRevocaArresto = aNumGiorniRevocaArresto;
		this.mDataInizioRevoca = aDataInizioRevoca;
		this.mFlagPeriodoEspiato = aFlagPeriodoEspiato;
		this.mAnnoAltroTitolo = aAnnoAltroTitolo;
		this.mNumAltroTitolo = aNumAltroTitolo;
		this.mDataAltroTitolo = aDataAltroTitolo;
		this.mCodLuogoAltroTitolo = aCodLuogoAltroTitolo;
		this.mCodAutoritaAltroTitolo = aCodAutoritaAltroTitolo;
		this.mFlagCheck = "N";
		this.mDataScadenzaProroga = aDataScadenzaProroga;
		this.mFlagDecisioneTribunale = aFlagDecisioneTribunale;
		this.mCodTdsCompetente = aCodTdsCompetente;
		this.mFlagSituazione = aFlagSituazione;
		this.mDescTdsCompetente = aDescTdsCompetente;
		this.mDescSedeTdsCompetente = aDescSedeTdsCompetente;
		this.mCodTipoDecisioneMaAt = aCodTipoDecisioneMaAt;
		this.mDescrTipoDecisioneMaAt = aDescrTipoDecisioneMaAt;
		this.mCodTipoMisuraMaAt = aCodTipoMisuraMaAt;
		this.mDescrTipoMisuraMaAt = aDescrTipoMisuraMaAt;
		this.mDataDecisioneMaAt = aDataDecisioneMaAt;
		this.mChiaveAnnoFascicoloSiusMaAt = aChiaveAnnoFascicoloSiusMaAt;
		this.mChiaveProgrFascicoloSiusMaAt = aChiaveProgrFascicoloSiusMaAt;
		this.mChiaveUfficioFascicoloSiusMaAt = aChiaveUfficioFascicoloSiusMaAt;
		this.mDescrChiaveUfficioFascicoloSiusMaAt = aDescrChiaveUfficioFascicoloSiusMaAt;
		this.mAnnoRegistroMaAt = aAnnoRegistroMaAt;
		this.mNumeroRegistroMaAt = aNumeroRegistroMaAt;
		// MEV_9
		this.mDataEsecutivita = aDataEsecutivita;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdMisuraAlternativa() {
		return mIdMisuraAlternativa;
	}

	public String getCodTipoDecisione() {
		return mCodTipoDecisione;
	}

	public String getDescrTipoDecisione() {
		return mDescrTipoDecisione;
	}

	public String getCodNaturaDecisione() {
		return mCodNaturaDecisione;
	}

	public String getDescrNaturaDecisione() {
		return mDescrNaturaDecisione;
	}

	public String getCodTipoMisura() {
		return mCodTipoMisura;
	}

	public String getDescrTipoMisura() {
		return mDescrTipoMisura;
	}

	public Date getDataDecisione() {
		return mDataDecisione;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getDescrMagistrato() {
		return mDescrMagistrato;
	}

	public String getCodUfficioSorveglianza() {
		return mCodUfficioSorveglianza;
	}

	public String getDescrUfficioSorveglianza() {
		return mDescrUfficioSorveglianza;
	}

	public BigDecimal getCssIdCssa() {
		return mCssIdCssa;
	}

	public String getDescrLuogoProva() {
		return mDescrLuogoProva;
	}

	public BigDecimal getNumAnniMisura() {
		return mNumAnniMisura;
	}

	public BigDecimal getNumMesiMisura() {
		return mNumMesiMisura;
	}

	public BigDecimal getNumGiorniMisura() {
		return mNumGiorniMisura;
	}

	public Date getDataInizioMisura() {
		return mDataInizioMisura;
	}

	public Date getDataFineMisura() {
		return mDataFineMisura;
	}

	public Date getDataFineMisuraPiuUno() {
		return mDataFineMisuraPiuUno;
	}

	public BigDecimal getChiaveAnnoFascicoloSius() {
		return mChiaveAnnoFascicoloSius;
	}

	public String getChiaveUfficioFascicoloSius() {
		return mChiaveUfficioFascicoloSius;
	}

	public BigDecimal getChiaveProgrFascicoloSius() {
		return mChiaveProgrFascicoloSius;
	}

	public BigDecimal getAnnoRegistro() {
		return mAnnoRegistro;
	}

	public BigDecimal getNumeroRegistro() {
		return mNumeroRegistro;
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

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getNote() {
		return mNote;
	}

	public Date getDataScarcerazione() {
		return mDataScarcerazione;
	}

	public Date getDataIngressoIstituto() {
		return mDataIngressoIstituto;
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getMisuraAlternativaCorrente() {
		return mMisuraAlternativaCorrente;
	}

	public String getCodTipoUfficioScarcerazione() {
		return mCodTipoUfficioScarcerazione;
	}

	public String getDescrChiaveUfficioFascicoloSius() {
		return mDescrChiaveUfficioFascicoloSius;
	}

	public String getFlagUfficioInserimento() {
		return mFlagUfficioInserimento;
	}

	public BigDecimal getNumAnniRevocaReclusione() {
		return mNumAnniRevocaReclusione;
	}

	public BigDecimal getNumMesiRevocaReclusione() {
		return mNumMesiRevocaReclusione;
	}

	public BigDecimal getNumGiorniRevocaReclusione() {
		return mNumGiorniRevocaReclusione;
	}

	public BigDecimal getNumAnniRevocaArresto() {
		return mNumAnniRevocaArresto;
	}

	public BigDecimal getNumMesiRevocaArresto() {
		return mNumMesiRevocaArresto;
	}

	public BigDecimal getNumGiorniRevocaArresto() {
		return mNumGiorniRevocaArresto;
	}

	public Date getDataInizioRevoca() {
		return mDataInizioRevoca;
	}

	public String getFlagPeriodoEspiato() {
		return mFlagPeriodoEspiato;
	}

	public BigDecimal getAnnoAltroTitolo() {
		return mAnnoAltroTitolo;
	}

	public String getNumAltroTitolo() {
		return mNumAltroTitolo;
	}

	public Date getDataAltroTitolo() {
		return mDataAltroTitolo;
	}

	public String getCodLuogoAltroTitolo() {
		return mCodLuogoAltroTitolo;
	}

	public String getCodAutoritaAltroTitolo() {
		return mCodAutoritaAltroTitolo;
	}

	public String getDescLuogoAltroTitolo() {
		return mDescLuogoAltroTitolo;
	}

	public String getDescAutoritaAltroTitolo() {
		return mDescAutoritaAltroTitolo;
	}

	public String getFlagCheck() {
		return mFlagCheck;
	}

	public String getStringaRevocaReclusione() {
		return mStringaRevocaReclusione;
	}

	public String getStringaRevocaArresto() {
		return mStringaRevocaArresto;
	}

	public String getStringaMisura() {
		return mStringaMisura;
	}

	public String getLegge() {
		return mLegge;
	}

	public Date getDataScadenzaProroga() {
		return mDataScadenzaProroga;
	}

	public String getFlagDecisioneTribunale() {
		return mFlagDecisioneTribunale;
	}

	public String getCodTdsCompetente() {
		return mCodTdsCompetente;
	}

	public String getFlagSituazione() {
		return mFlagSituazione;
	}

	public String getDescTdsCompetente() {
		return mDescTdsCompetente;
	}

	public String getDescSedeTdsCompetente() {
		return mDescSedeTdsCompetente;
	}

	public String getCodTipoDecisioneMaAt() {
		return mCodTipoDecisioneMaAt;
	}

	public String getDescrTipoDecisioneMaAt() {
		return mDescrTipoDecisioneMaAt;
	}

	public String getCodTipoMisuraMaAt() {
		return mCodTipoMisuraMaAt;
	}

	public String getDescrTipoMisuraMaAt() {
		return mDescrTipoMisuraMaAt;
	}

	public Date getDataDecisioneMaAt() {
		return mDataDecisioneMaAt;
	}

	public BigDecimal getChiaveAnnoFascicoloSiusMaAt() {
		return mChiaveAnnoFascicoloSiusMaAt;
	}

	public BigDecimal getChiaveProgrFascicoloSiusMaAt() {
		return mChiaveProgrFascicoloSiusMaAt;
	}

	public String getChiaveUfficioFascicoloSiusMaAt() {
		return mChiaveUfficioFascicoloSiusMaAt;
	}

	public String getDescrChiaveUfficioFascicoloSiusMaAt() {
		return mDescrChiaveUfficioFascicoloSiusMaAt;
	}

	public BigDecimal getAnnoRegistroMaAt() {
		return mAnnoRegistroMaAt;
	}

	public BigDecimal getNumeroRegistroMaAt() {
		return mNumeroRegistroMaAt;
	}

	public String getIs51Bis() {
		return mIs51Bis;
	}

	public String getDescrizioneComunita() {
		return mDescrizioneComunita;
	}

	public BigDecimal getFlFormaMisura() {
		return mFlFormaMisura;
	}

	// MEV_9
	public Date getDataEsecutivita() {
		return mDataEsecutivita;
	}

	//
	// METODI SET()
	//

	public void setIdMisuraAlternativa(BigDecimal aValore) {
		mIdMisuraAlternativa = aValore;
	}

	public void setCodTipoDecisione(String aValore) {
		mCodTipoDecisione = aValore;
	}

	public void setDescrTipoDecisione(String aValore) {
		mDescrTipoDecisione = aValore;
	}

	public void setCodNaturaDecisione(String aValore) {
		mCodNaturaDecisione = aValore;
	}

	public void setDescrNaturaDecisione(String aValore) {
		mDescrNaturaDecisione = aValore;
	}

	public void setCodTipoMisura(String aValore) {
		mCodTipoMisura = aValore;
	}

	public void setDescrTipoMisura(String aValore) {
		mDescrTipoMisura = aValore;
	}

	public void setDataDecisione(Date aValore) {
		mDataDecisione = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setDescrMagistrato(String aValore) {
		mDescrMagistrato = aValore;
	}

	public void setCodUfficioSorveglianza(String aValore) {
		mCodUfficioSorveglianza = aValore;
	}

	public void setDescrUfficioSorveglianza(String aValore) {
		mDescrUfficioSorveglianza = aValore;
	}

	public void setCssIdCssa(BigDecimal aValore) {
		mCssIdCssa = aValore;
	}

	public void setDescrLuogoProva(String aValore) {
		mDescrLuogoProva = aValore;
	}

	public void setNumAnniMisura(BigDecimal aValore) {
		mNumAnniMisura = aValore;
	}

	public void setNumMesiMisura(BigDecimal aValore) {
		mNumMesiMisura = aValore;
	}

	public void setNumGiorniMisura(BigDecimal aValore) {
		mNumGiorniMisura = aValore;
	}

	public void setDataInizioMisura(Date aValore) {
		mDataInizioMisura = aValore;
	}

	// GDV per la stampa modificato questo metodo con l'aggiunta di Datafine MisuraPiuUNo
	public void setDataFineMisura(Date aValore) {
		mDataFineMisura = aValore;
		if (aValore != null)
			mDataFineMisuraPiuUno = DateUtils.getDayAfter(aValore);
	}

	public void setChiaveAnnoFascicoloSius(BigDecimal aValore) {
		mChiaveAnnoFascicoloSius = aValore;
	}

	public void setChiaveUfficioFascicoloSius(String aValore) {
		mChiaveUfficioFascicoloSius = aValore;
	}

	public void setChiaveProgrFascicoloSius(BigDecimal aValore) {
		mChiaveProgrFascicoloSius = aValore;
	}

	public void setAnnoRegistro(BigDecimal aValore) {
		mAnnoRegistro = aValore;
	}

	public void setNumeroRegistro(BigDecimal aValore) {
		mNumeroRegistro = aValore;
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

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setDataScarcerazione(Date aValore) {
		mDataScarcerazione = aValore;
	}

	public void setDataIngressoIstituto(Date aValore) {
		mDataIngressoIstituto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setStringaRevocaReclusione(String aValore) {
		mStringaRevocaReclusione = aValore;
	}

	public void setStringaRevocaArresto(String aValore) {
		mStringaRevocaArresto = aValore;
	}

	public void setStringaMisura(String aValore) {
		mStringaMisura = aValore;
	}

	public void setMisuraAlternativaCorrente(String aValore) {
		mMisuraAlternativaCorrente = aValore;
	}

	public void setCodTipoUfficioScarcerazione(String aValore) {
		mCodTipoUfficioScarcerazione = aValore;
	}

	public void setDescrChiaveUfficioFascicoloSius(String aValore) {
		mDescrChiaveUfficioFascicoloSius = aValore;
	}

	public void setFlagUfficioInserimento(String aValore) {
		mFlagUfficioInserimento = aValore;
	}

	public void setNumAnniRevocaReclusione(BigDecimal aValore) {
		mNumAnniRevocaReclusione = aValore;
	}

	public void setNumMesiRevocaReclusione(BigDecimal aValore) {
		mNumMesiRevocaReclusione = aValore;
	}

	public void setNumGiorniRevocaReclusione(BigDecimal aValore) {
		mNumGiorniRevocaReclusione = aValore;
	}

	public void setNumAnniRevocaArresto(BigDecimal aValore) {
		mNumAnniRevocaArresto = aValore;
	}

	public void setNumMesiRevocaArresto(BigDecimal aValore) {
		mNumMesiRevocaArresto = aValore;
	}

	public void setNumGiorniRevocaArresto(BigDecimal aValore) {
		mNumGiorniRevocaArresto = aValore;
	}

	public void setDataInizioRevoca(Date aValore) {
		mDataInizioRevoca = aValore;
	}

	public void setFlagPeriodoEspiato(String aValore) {
		mFlagPeriodoEspiato = aValore;
	}

	public void setAnnoAltroTitolo(BigDecimal aValore) {
		mAnnoAltroTitolo = aValore;
	}

	public void setNumAltroTitolo(String aValore) {
		mNumAltroTitolo = aValore;
	}

	public void setDataAltroTitolo(Date aValore) {
		mDataAltroTitolo = aValore;
	}

	public void setCodLuogoAltroTitolo(String aValore) {
		mCodLuogoAltroTitolo = aValore;
	}

	public void setCodAutoritaAltroTitolo(String aValore) {
		mCodAutoritaAltroTitolo = aValore;
	}

	public void setDescLuogoAltroTitolo(String aValore) {
		mDescLuogoAltroTitolo = aValore;
	}

	public void setDescAutoritaAltroTitolo(String aValore) {
		mDescAutoritaAltroTitolo = aValore;
	}

	public void setFlagCheck(String aValore) {
		mFlagCheck = aValore;
	}

	public void setLegge(String aValore) {
		mLegge = aValore;
	}

	public void setDataScadenzaProroga(Date aValore) {
		mDataScadenzaProroga = aValore;
	}

	public void setFlagDecisioneTribunale(String aValore) {
		mFlagDecisioneTribunale = aValore;
	}

	public void setCodTdsCompetente(String aValore) {
		mCodTdsCompetente = aValore;
	}

	public void setFlagSituazione(String aValore) {
		mFlagSituazione = aValore;
	}

	public void setDescTdsCompetente(String aValore) {
		mDescTdsCompetente = aValore;
	}

	public void setDescSedeTdsCompetente(String aValore) {
		mDescSedeTdsCompetente = aValore;
	}

	public void setCodTipoDecisioneMaAt(String aValore) {
		mCodTipoDecisioneMaAt = aValore;
	}

	public void setDescrTipoDecisioneMaAt(String aValore) {
		mDescrTipoDecisioneMaAt = aValore;
	}

	public void setCodTipoMisuraMaAt(String aValore) {
		mCodTipoMisuraMaAt = aValore;
	}

	public void setDescrTipoMisuraMaAt(String aValore) {
		mDescrTipoMisuraMaAt = aValore;
	}

	public void setDataDecisioneMaAt(Date aValore) {
		mDataDecisioneMaAt = aValore;
	}

	public void setChiaveAnnoFascicoloSiusMaAt(BigDecimal aValore) {
		mChiaveAnnoFascicoloSiusMaAt = aValore;
	}

	public void setChiaveProgrFascicoloSiusMaAt(BigDecimal aValore) {
		mChiaveProgrFascicoloSiusMaAt = aValore;
	}

	public void setChiaveUfficioFascicoloSiusMaAt(String aValore) {
		mChiaveUfficioFascicoloSiusMaAt = aValore;
	}

	public void setDescrChiaveUfficioFascicoloSiusMaAt(String aValore) {
		mDescrChiaveUfficioFascicoloSiusMaAt = aValore;
	}

	public void setAnnoRegistroMaAt(BigDecimal aValore) {
		mAnnoRegistroMaAt = aValore;
	}

	public void setNumeroRegistroMaAt(BigDecimal aValore) {
		mNumeroRegistroMaAt = aValore;
	}

	public void setIs51Bis(String aValore) {
		mIs51Bis = aValore;
	}

	public void setDescrizioneComunita(String mDescizioneComunita) {
		this.mDescrizioneComunita = mDescizioneComunita;
	}

	public void setFlFormaMisura(BigDecimal mFlFormaMisura) {
		this.mFlFormaMisura = mFlFormaMisura;
	}

	// MEV_9
	public void setDataEsecutivita(Date aValore) {
		mDataEsecutivita = aValore;
	}

	/**
	 * calcolaStringaReclusione per la Stampa in cui serve la stringa composta di anni mesi giorni
	 *
	 * @return
	 */
	public void calcolaStringaReclusione() {

		String lStringReclusione = "";
		if (this.getNumAnniMisura() != null) {
			if (this.getNumAnniMisura().intValue() != 0)
				lStringReclusione = "Anni " + this.getNumAnniMisura();
		}
		if (this.getNumMesiMisura() != null) {
			if (this.getNumAnniMisura() != null && this.getNumAnniMisura().intValue() != 0)
				lStringReclusione += " ";
			if (this.getNumMesiMisura().intValue() != 0)
				lStringReclusione += "Mesi " + this.getNumMesiMisura();
		}
		if (this.getNumGiorniMisura() != null) {
			if ((this.getNumAnniMisura() != null && this.getNumAnniMisura().intValue() != 0)
					|| (this.getNumMesiMisura() != null && this.getNumMesiMisura().intValue() != 0))
				lStringReclusione += " ";
			if (this.getNumGiorniMisura().intValue() != 0)
				lStringReclusione += "Giorni " + this.getNumGiorniMisura();
		}

		if (lStringReclusione.length() > 1)
			this.mStringaReclusione = lStringReclusione;
		else
			this.mStringaReclusione = null;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {

		String lStr = new String();

		lStr = "MisuraAlternativaModel:\n" + "[ mIdMisuraAlternativa         = " + mIdMisuraAlternativa
				+ " ]\n" + "[ mCodTipoDecisione            = " + mCodTipoDecisione + " ]\n"
				+ "[ mDescrTipoDecisione          = " + mDescrTipoDecisione + " ]\n"
				+ "[ mCodNaturaDecisione          = " + mCodNaturaDecisione + " ]\n"
				+ "[ mDescrNaturaDecisione        = " + mDescrNaturaDecisione + " ]\n"
				+ "[ mCodTipoMisura               = " + mCodTipoMisura + " ]\n"
				+ "[ mDescrTipoMisura             = " + mDescrTipoMisura + " ]\n"
				+ "[ mDataDecisione               = " + mDataDecisione + " ]\n"
				+ "[ mCodMagistrato               = " + mCodMagistrato + " ]\n"
				+ "[ mDescrMagistrato             = " + mDescrMagistrato + " ]\n"
				+ "[ mCodUfficioSorveglianza      = " + mCodUfficioSorveglianza + " ]\n"
				+ "[ mDescrUfficioSorveglianza    = " + mDescrUfficioSorveglianza + " ]\n"
				+ "[ mCssIdCssa                   = " + mCssIdCssa + " ]\n"
				+ "[ mDescrLuogoProva             = " + mDescrLuogoProva + " ]\n"
				+ "[ mNumAnniMisura               = " + mNumAnniMisura + " ]\n"
				+ "[ mNumMesiMisura               = " + mNumMesiMisura + " ]\n"
				+ "[ mNumGiorniMisura             = " + mNumGiorniMisura + " ]\n"
				+ "[ mDataInizioMisura            = " + mDataInizioMisura + " ]\n"
				+ "[ mDataFineMisura              = " + mDataFineMisura + " ]\n"
				+ "[ mChiaveAnnoFascicoloSius     = " + mChiaveAnnoFascicoloSius + " ]\n"
				+ "[ mChiaveUfficioFascicoloSius  = " + mChiaveUfficioFascicoloSius + " ]\n"
				+ "[ mDescrChiaveUfficioFascicoloSius = " + mDescrChiaveUfficioFascicoloSius + " ]\n"
				+ "[ mChiaveProgrFascicoloSius    = " + mChiaveProgrFascicoloSius + " ]\n"
				+ "[ mAnnoRegistro                = " + mAnnoRegistro + " ]\n"
				+ "[ mNumeroRegistro              = " + mNumeroRegistro + " ]\n"
				+ "[ mNote                        = " + mNote + " ]\n" + "[ mDataScarcerazione           = "
				+ mDataScarcerazione + " ]\n" + "[ mDataIngressoIstituto        = " + mDataIngressoIstituto
				+ " ]\n" + "[ mCodTipoUfficioScarcerazione = " + mCodTipoUfficioScarcerazione + " ]\n"
				+ "[ mFlagUfficioInserimento      = " + mFlagUfficioInserimento + " ]\n"
				+ "[ mDataInizioRevoca            = " + mDataInizioRevoca + " ]\n"
				+ "[ mNumAnniRevocaReclusione     = " + mNumAnniRevocaReclusione + " ]\n"
				+ "[ mNumMesiRevocaReclusione     = " + mNumMesiRevocaReclusione + " ]\n"
				+ "[ mNumGiorniRevocaReclusione   = " + mNumGiorniRevocaReclusione + " ]\n"
				+ "[ mNumAnniRevocaArresto        = " + mNumAnniRevocaArresto + " ]\n"
				+ "[ mNumMesiRevocaArresto        = " + mNumMesiRevocaArresto + " ]\n"
				+ "[ mNumGiorniRevocaArresto      = " + mNumGiorniRevocaArresto + " ]\n"
				+ "[ mAnnoAltroTitolo             = " + mAnnoAltroTitolo + " ]\n"
				+ "[ mNumAltroTitolo              = " + mNumAltroTitolo + " ]\n"
				+ "[ mDataAltroTitolo             = " + mDataAltroTitolo + " ]\n"
				+ "[ mCodLuogoAltroTitolo         = " + mCodLuogoAltroTitolo + " ]\n"
				+ "[ mDescLuogoAltroTitolo        = " + mDescLuogoAltroTitolo + " ]\n"
				+ "[ mCodAutoritaAltroTitolo      = " + mCodAutoritaAltroTitolo + " ]\n"
				+ "[ mDescAutoritaAltroTitolo     = " + mDescAutoritaAltroTitolo + " ]\n"
				+ "[ mFlagPeriodoEspiato          = " + mFlagPeriodoEspiato + " ]\n"
				+ "[ mCodOperatoreInserimento     = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento             = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento       = " + mCodUfficioInserimento + " ]\n"
				+ "[ mDescrUfficioInserimento     = " + mDescrUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento   = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento           = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento     = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mDescrUfficioAggiornamento   = " + mDescrUfficioAggiornamento + " ]\n"
				+ "[ mFasSieIdFascicoloSiep       = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mEveIdEvento                 = " + mEveIdEvento + " ]\n"
				+ "[ mDataScadenzaProroga         = " + mDataScadenzaProroga + " ]\n"
				+ "[ mFlagDecisioneTribunale      = " + mFlagDecisioneTribunale + " ]\n"
				+ "[ mCodTdsCompetente            = " + mCodTdsCompetente + " ]\n"
				+ "[ mFlagSituazione              = " + mFlagSituazione + " ]\n"
				+ "[ mDescTdsCompetente           = " + mDescTdsCompetente + " ]\n"
				+ "[ mDescSedeTdsCompetente       = " + mDescSedeTdsCompetente + " ]\n"
				+ "[ mCodTipoDecisioneMaAt       = " + mCodTipoDecisioneMaAt + " ]\n"
				+ "[ mDescrTipoDecisioneMaAt     = " + mDescrTipoDecisioneMaAt + " ]\n"
				+ "[ mCodTipoMisuraMaAt          = " + mCodTipoMisuraMaAt + " ]\n"
				+ "[ mDescrTipoMisuraMaAt        = " + mDescrTipoMisuraMaAt + " ]\n"
				+ "[ mDataDecisioneMaAt          = " + mDataDecisioneMaAt + " ]\n"
				+ "[ mAnnoFascicoloSiusMaAt      = " + mChiaveAnnoFascicoloSiusMaAt + " ]\n"
				+ "[ mProgrFascicoloSiusMaAt     = " + mChiaveProgrFascicoloSiusMaAt + " ]\n"
				+ "[ mUfficioFascicoloSiusMaAt   = " + mChiaveUfficioFascicoloSiusMaAt + " ]\n"
				+ "[ mDescrUfficioFascicoloSiusMaAt = " + mDescrChiaveUfficioFascicoloSiusMaAt + " ]\n"
				+ "[ mDataEsecutivita            = " + mDataEsecutivita + " ]\n"
				+ "[ mAnnoRegistroMaAt           = " + mAnnoRegistroMaAt + " ]\n"
				+ "[ mNumeroRegistroMaAt         = " + mNumeroRegistroMaAt + " ]";

		return lStr;
	}

	public String toString2() {

		String lStr = new String();

		lStr = "" + mIdMisuraAlternativa + " - " + mCodTipoDecisione + " - " + mDescrTipoDecisione + " - "
				+ mCodNaturaDecisione + " - " + mDescrNaturaDecisione + " - " + mCodTipoMisura + " - "
				+ mDescrTipoMisura + " - " + mDataDecisione + " - " + mCodMagistrato + " - "
				+ mDescrMagistrato + " - " + mCodUfficioSorveglianza + " - " + mDescrUfficioSorveglianza
				+ " - " + mCssIdCssa + " - " + mDescrLuogoProva + " - " + mNumAnniMisura + " - "
				+ mNumMesiMisura + " - " + mNumGiorniMisura + " - " + mDataInizioMisura + " - "
				+ mDataFineMisura + " - " + mChiaveAnnoFascicoloSius + " - " + mChiaveUfficioFascicoloSius
				+ " - " + mChiaveProgrFascicoloSius + " - " + mAnnoRegistro + " - " + mNumeroRegistro + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mFasSieIdFascicoloSiep + " - " + mEveIdEvento + " - " + mNote + " - " + mDataScarcerazione
				+ " - " + mDataIngressoIstituto + " - " + mCodTipoUfficioScarcerazione + " - "
				+ mDescrChiaveUfficioFascicoloSius + " - " + mFlagUfficioInserimento + " - "
				+ mNumAnniRevocaReclusione + " - " + mNumMesiRevocaReclusione + " - "
				+ mNumGiorniRevocaReclusione + " - " + mNumAnniRevocaArresto + " - " + mNumMesiRevocaArresto
				+ " - " + mNumGiorniRevocaArresto + " - " + mDataInizioRevoca + " - " + mFlagPeriodoEspiato
				+ " - " + mAnnoAltroTitolo + " - " + mNumAltroTitolo + " - " + mDataAltroTitolo + " - "
				+ mCodLuogoAltroTitolo + " - " + mCodAutoritaAltroTitolo + " - " + mDescLuogoAltroTitolo
				+ " - " + mDescAutoritaAltroTitolo + " - " + mFlagCheck + " - " + mDataScadenzaProroga + " - "
				+ mFlagDecisioneTribunale + " - " + mCodTdsCompetente + " - " + mFlagSituazione + " - "
				+ mDescTdsCompetente + " - " + mDescSedeTdsCompetente;

		return lStr;
	}

	/**
	 * calcolaStringaMisura per la Stampa in cui serve la stringa composta di anni mesi giorni
	 *
	 * @return
	 */
	public void calcolaStringaMisura() {

		String lStringMisura = "";
		if (this.mNumAnniMisura != null) {
			if (this.mNumAnniMisura.intValue() != 0)
				lStringMisura = "Anni " + this.mNumAnniMisura;
		}
		if (this.mNumMesiMisura != null) {
			if (this.mNumMesiMisura.intValue() != 0)
				lStringMisura += " Mesi " + this.mNumMesiMisura;
		}
		if (this.mNumGiorniMisura != null) {
			if (this.mNumGiorniMisura.intValue() != 0)
				lStringMisura += " Giorni " + this.mNumGiorniMisura;
		}

		if (lStringMisura.length() > 1)
			this.mStringaMisura = lStringMisura;
		else
			this.mStringaMisura = null;
	}

	/**
	 * calcolaStringaArresto per la Stampa in cui serve la stringa composta di anni mesi giorni
	 *
	 * @return
	 */
	public void calcolaStringaRevocaArresto() {

		String lStringArresto = "";
		if (this.mNumAnniRevocaArresto != null) {
			if (this.mNumAnniRevocaArresto.intValue() != 0)
				lStringArresto = "Anni " + this.mNumAnniRevocaArresto;
		}
		if (this.mNumMesiRevocaArresto != null) {
			if (this.mNumMesiRevocaArresto.intValue() != 0)
				lStringArresto += " Mesi " + this.mNumMesiRevocaArresto;
		}
		if (this.mNumGiorniRevocaArresto != null) {
			if (this.mNumGiorniRevocaArresto.intValue() != 0)
				lStringArresto += " Giorni " + this.mNumGiorniRevocaArresto;
		}

		if (lStringArresto.length() > 1)
			this.mStringaRevocaArresto = lStringArresto;
		else
			this.mStringaRevocaArresto = null;
	}

	/**
	 * calcolaStringaReclusione per la Stampa in cui serve la stringa composta di anni mesi giorni
	 *
	 * @return
	 */
	public void calcolaStringaRevocaReclusione() {

		String lStringReclusione = "";
		if (this.mNumAnniRevocaReclusione != null) {
			if (this.mNumAnniRevocaReclusione.intValue() != 0)
				lStringReclusione = "Anni " + this.mNumAnniRevocaReclusione;
		}
		if (this.mNumMesiRevocaReclusione != null) {
			if (this.mNumMesiRevocaReclusione.intValue() != 0)
				lStringReclusione += " Mesi " + this.mNumMesiRevocaReclusione;
		}
		if (this.mNumGiorniRevocaReclusione != null) {
			if (this.mNumGiorniRevocaReclusione.intValue() != 0)
				lStringReclusione += " Giorni " + this.mNumGiorniRevocaReclusione;
		}

		if (lStringReclusione.length() > 1)
			this.mStringaRevocaReclusione = lStringReclusione;
		else
			this.mStringaRevocaReclusione = null;
	}

}