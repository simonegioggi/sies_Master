package siap.sico.misuraalternativa.model;

/**
* <p>Title: MisuraAlternativaModel</p>
* <p>Description: Classe Model che rappresenta il MisuraAlternativa</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class MisuraAlternativaPrecedenteModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3901007919164487147L;

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

	// COSTRUTTORE DI DEFAULT
	public MisuraAlternativaPrecedenteModel() {
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
	}

	// COSTRUTTORE DI COPIA
	public MisuraAlternativaPrecedenteModel(MisuraAlternativaModel aModel) {
		this.mIdMisuraAlternativa = aModel.getIdMisuraAlternativa();
		this.mCodTipoDecisione = aModel.getCodTipoDecisione();
		this.mDescrTipoDecisione = aModel.getDescrTipoDecisione();
		this.mCodNaturaDecisione = aModel.getCodNaturaDecisione();
		this.mDescrNaturaDecisione = aModel.getDescrNaturaDecisione();
		this.mCodTipoMisura = aModel.getCodTipoMisura();
		this.mDescrTipoMisura = aModel.getDescrTipoMisura();
		this.mDataDecisione = aModel.getDataDecisione();
		this.mCodMagistrato = aModel.getCodMagistrato();
		this.mDescrMagistrato = aModel.getDescrMagistrato();
		this.mCodUfficioSorveglianza = aModel.getCodUfficioSorveglianza();
		this.mDescrUfficioSorveglianza = aModel.getDescrUfficioSorveglianza();
		this.mCssIdCssa = aModel.getCssIdCssa();
		this.mDescrLuogoProva = aModel.getDescrLuogoProva();
		this.mNumAnniMisura = aModel.getNumAnniMisura();
		this.mNumMesiMisura = aModel.getNumMesiMisura();
		this.mNumGiorniMisura = aModel.getNumGiorniMisura();
		this.mDataInizioMisura = aModel.getDataInizioMisura();
		this.mDataFineMisura = aModel.getDataFineMisura();
		this.mChiaveAnnoFascicoloSius = aModel.getChiaveAnnoFascicoloSius();
		this.mChiaveUfficioFascicoloSius = aModel.getChiaveUfficioFascicoloSius();
		this.mChiaveProgrFascicoloSius = aModel.getChiaveProgrFascicoloSius();
		this.mAnnoRegistro = aModel.getAnnoRegistro();
		this.mNumeroRegistro = aModel.getNumeroRegistro();
		this.mCodOperatoreInserimento = aModel.getCodOperatoreInserimento();
		this.mDataInserimento = aModel.getDataInserimento();
		this.mCodUfficioInserimento = aModel.getCodUfficioInserimento();
		this.mDescrUfficioInserimento = aModel.getDescrUfficioInserimento();
		this.mCodOperatoreAggiornamento = aModel.getCodOperatoreAggiornamento();
		this.mDataAggiornamento = aModel.getDataAggiornamento();
		this.mCodUfficioAggiornamento = aModel.getCodUfficioAggiornamento();
		this.mDescrUfficioAggiornamento = aModel.getDescrUfficioAggiornamento();
		this.mFasSieIdFascicoloSiep = aModel.getFasSieIdFascicoloSiep();
		this.mEveIdEvento = aModel.getEveIdEvento();
		this.mNote = aModel.getNote();
		this.mDataScarcerazione = aModel.getDataScarcerazione();
		this.mDataIngressoIstituto = aModel.getDataIngressoIstituto();
		this.mMisuraAlternativaCorrente = aModel.getMisuraAlternativaCorrente();
		this.mDataFineMisuraPiuUno = aModel.getDataFineMisuraPiuUno();
		this.mCodTipoUfficioScarcerazione = aModel.getCodTipoUfficioScarcerazione();
		this.mDescrChiaveUfficioFascicoloSius = aModel.getDescrChiaveUfficioFascicoloSius();
		this.mFlagUfficioInserimento = aModel.getFlagUfficioInserimento();
		this.mNumAnniRevocaReclusione = aModel.getNumAnniRevocaReclusione();
		this.mNumMesiRevocaReclusione = aModel.getNumMesiRevocaReclusione();
		this.mNumGiorniRevocaReclusione = aModel.getNumGiorniRevocaReclusione();
		this.mNumAnniRevocaArresto = aModel.getNumAnniRevocaArresto();
		this.mNumMesiRevocaArresto = aModel.getNumMesiRevocaArresto();
		this.mNumGiorniRevocaArresto = aModel.getNumGiorniRevocaArresto();
		this.mDataInizioRevoca = aModel.getDataInizioRevoca();
		this.mFlagPeriodoEspiato = aModel.getFlagPeriodoEspiato();

		this.mAnnoAltroTitolo = aModel.getAnnoAltroTitolo();
		this.mNumAltroTitolo = aModel.getNumAltroTitolo();
		this.mDataAltroTitolo = aModel.getDataAltroTitolo();
		this.mCodLuogoAltroTitolo = aModel.getCodLuogoAltroTitolo();
		this.mCodAutoritaAltroTitolo = aModel.getCodAutoritaAltroTitolo();

		this.mDescLuogoAltroTitolo = aModel.getDescLuogoAltroTitolo();
		this.mDescAutoritaAltroTitolo = aModel.getDescAutoritaAltroTitolo();

		this.mFlagCheck = aModel.getFlagCheck();
		this.mLegge = aModel.getLegge();

		this.mDataScadenzaProroga = aModel.getDataScadenzaProroga();
		this.mFlagDecisioneTribunale = aModel.getFlagDecisioneTribunale();
		this.mCodTdsCompetente = aModel.getCodTdsCompetente();
		this.mFlagSituazione = aModel.getFlagSituazione();
		this.mDescTdsCompetente = aModel.getDescTdsCompetente();
		this.mDescSedeTdsCompetente = aModel.getDescSedeTdsCompetente();
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

	public String toString() {
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