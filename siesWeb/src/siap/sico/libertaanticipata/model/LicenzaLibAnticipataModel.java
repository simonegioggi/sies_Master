package siap.sico.libertaanticipata.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * LicenzaLibanticipataModel - Classe Model che rappresenta il LicenzaLibanticipata
 *
 * @version 1.0
 */
public class LicenzaLibAnticipataModel extends GenericModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4398226880746754726L;

	private BigDecimal mIdLicenzaLibanticipata;
	private String mCodTipoLicenza;
	private String mDescrTipoLicenza;
	private BigDecimal mNumeroGiorni;
	private Date mDataInizio;
	private String mOraInizio;
	private Date mDataFine;
	private String mOraFine;
	private String mLuogoSvolgimentoProva;
	private Date mDataDetenzRifDa;
	private Date mDataDetenzRifA;
	private String mFlagInfrazioneObblighi;
	private Date mDataInfrazioneObblighi;
	private String mDescrInfrazioneObblighi;
	private String mFlagScomputo;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSiuIdFascicoloSius;
	private BigDecimal mEveIdEvento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private String mFlagConcesso;
	private String mFlagElaborato;
	private String mFlagScorta;
	private String mCodStatoPermesso;
	private String mDescrStatoPermesso;
	private BigDecimal mNumeroOre;
	private BigDecimal mAnnoSius;
	private String mNumeroSius;
	private BigDecimal mAnnoOrdinanza;
	private BigDecimal mNumeroOrdinanza;
	private String mCodUfficioEmittente;
	private String mDescrUfficioEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	// MEV10-s3: aggiunta variabile
	private String mCodTipoUfficioEmittente;
	private Date mDataEmissioneOrdinanza;
	private String mGiorniScomputati;
	private String mCodEsito;
	private String mDescrEsito;
	private Date mDataAnnotazioneEsito;
	private String mAnnotazione;
	private BigDecimal mNumeroGiorniNoFruiti;
	private BigDecimal mNumeroOreNoFruite;
	private boolean mConProvvedimentoValidato;
	private BigDecimal mNumeroMesi;
	private BigDecimal mSommaRisarcDanni;
	// MERGE v10: aggiunti parametri per recupero informazioni del
	// Rimedio Risarcitorio al quale è legato un Reclamo Rimedio Risarcitorio
	private BigDecimal mAnnoProvvRR;
	private BigDecimal mNumeroProvvRR;
	private String mCodTipoUfficioEmittenteRR;
	private String mDescrUfficioEmittenteRR;
	private String mDescrLuogoEmittenteRR;
	private Date mDataEmissioneRR;
	// *****************************
	// MEV_2025-48: aggiunta nuova sezione - codice motivo detenzione
	private String mCodMotivoDetenzione;
	private String mDescrMotivoDetenzione;

	// COSTRUTTORE DI DEFAULT
	public LicenzaLibAnticipataModel() {

		this.mIdLicenzaLibanticipata = null;
		this.mCodTipoLicenza = "";
		this.mDescrTipoLicenza = "";
		this.mNumeroGiorni = null;
		this.mDataInizio = null;
		this.mOraInizio = "";
		this.mDataFine = null;
		this.mOraFine = "";
		this.mLuogoSvolgimentoProva = "";
		this.mDataDetenzRifDa = null;
		this.mDataDetenzRifA = null;
		this.mFlagInfrazioneObblighi = "";
		this.mDataInfrazioneObblighi = null;
		this.mDescrInfrazioneObblighi = "";
		this.mFlagScomputo = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSiuIdFascicoloSius = null;
		this.mEveIdEvento = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mFlagConcesso = "";
		this.mFlagElaborato = "";
		this.mFlagScorta = "";
		this.mCodStatoPermesso = "";
		this.mDescrStatoPermesso = "";
		this.mNumeroOre = null;
		this.mAnnoSius = null;
		this.mNumeroSius = "";
		this.mAnnoOrdinanza = null;
		this.mNumeroOrdinanza = null;
		this.mCodUfficioEmittente = "";
		this.mDescrUfficioEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";
		// MEV10-s3: aggiunta impostazione di proprietà
		this.mCodTipoUfficioEmittente = "";
		this.mDataEmissioneOrdinanza = null;
		this.mGiorniScomputati = "";
		this.mCodEsito = "";
		this.mDescrEsito = "";
		this.mDataAnnotazioneEsito = null;
		this.mAnnotazione = "";
		this.mNumeroGiorniNoFruiti = null;
		this.mNumeroOreNoFruite = null;
		this.mConProvvedimentoValidato = false;
		this.mNumeroMesi = null;
		this.mSommaRisarcDanni = null;
		// MEV_2025-48
		this.mCodMotivoDetenzione = "";
	}

	// COSTRUTTORE DI COPIA
	public LicenzaLibAnticipataModel(LicenzaLibAnticipataModel aModel) {

		this.mIdLicenzaLibanticipata = aModel.mIdLicenzaLibanticipata;
		this.mCodTipoLicenza = aModel.mCodTipoLicenza;
		this.mDescrTipoLicenza = aModel.mDescrTipoLicenza;
		this.mNumeroGiorni = aModel.mNumeroGiorni;
		this.mDataInizio = aModel.mDataInizio;
		this.mOraInizio = aModel.mOraInizio;
		this.mDataFine = aModel.mDataFine;
		this.mOraFine = aModel.mOraFine;
		this.mLuogoSvolgimentoProva = aModel.mLuogoSvolgimentoProva;
		this.mDataDetenzRifDa = aModel.mDataDetenzRifDa;
		this.mDataDetenzRifA = aModel.mDataDetenzRifA;
		this.mFlagInfrazioneObblighi = aModel.mFlagInfrazioneObblighi;
		this.mDataInfrazioneObblighi = aModel.mDataInfrazioneObblighi;
		this.mDescrInfrazioneObblighi = aModel.mDescrInfrazioneObblighi;
		this.mFlagScomputo = aModel.mFlagScomputo;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mFlagConcesso = aModel.mFlagConcesso;
		this.mFlagElaborato = aModel.mFlagElaborato;
		this.mFlagScorta = aModel.mFlagScorta;
		this.mCodStatoPermesso = aModel.mCodStatoPermesso;
		this.mDescrStatoPermesso = aModel.mDescrStatoPermesso;
		this.mNumeroOre = aModel.mNumeroOre;
		this.mAnnoSius = aModel.mAnnoSius;
		this.mNumeroSius = aModel.mNumeroSius;
		this.mAnnoOrdinanza = aModel.mAnnoOrdinanza;
		this.mNumeroOrdinanza = aModel.mNumeroOrdinanza;
		this.mCodUfficioEmittente = aModel.mCodUfficioEmittente;
		this.mDescrUfficioEmittente = aModel.mDescrUfficioEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		// MEV10-s3: aggiunta impostazione di proprietà
		this.mCodTipoUfficioEmittente = aModel.mCodTipoUfficioEmittente;
		this.mDataEmissioneOrdinanza = aModel.mDataEmissioneOrdinanza;
		this.mGiorniScomputati = aModel.mGiorniScomputati;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mDataAnnotazioneEsito = aModel.mDataAnnotazioneEsito;
		this.mAnnotazione = aModel.mAnnotazione;
		this.mNumeroGiorniNoFruiti = aModel.mNumeroGiorniNoFruiti;
		this.mNumeroOreNoFruite = aModel.mNumeroOreNoFruite;
		this.mConProvvedimentoValidato = aModel.mConProvvedimentoValidato;
		this.mNumeroMesi = aModel.mNumeroMesi;
		this.mSommaRisarcDanni = aModel.mSommaRisarcDanni;
		// MEV_2025-48
		this.mCodMotivoDetenzione = aModel.mCodMotivoDetenzione;
	}

	// COSTRUTTORE MODEL
	public LicenzaLibAnticipataModel(BigDecimal aIdLicenzaLibanticipata, String aCodTipoLicenza,
			String aDescrTipoLicenza, BigDecimal aNumeroGiorni, Date aDataInizio, String aOraInizio,
			Date aDataFine, String aOraFine, String aLuogoSvolgimentoProva, Date aDataDetenzRifDa,
			Date aDataDetenzRifA, String aFlagInfrazioneObblighi, Date aDataInfrazioneObblighi,
			String aDescrInfrazioneObblighi, String aFlagScomputo, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aFasSiuIdFascicoloSius, BigDecimal aEveIdEvento,
			BigDecimal aFasSieIdFascicoloSiep, String aFlagConcesso, String aFlagElaborato,
			String aFlagScorta, String aCodStatoPermesso, String aDescrStatoPermesso, BigDecimal aNumeroOre,
			BigDecimal aAnnoSius, String aNumeroSius, BigDecimal aAnnoOrdinanza, BigDecimal aNumeroOrdinanza,
			String aCodUfficioEmittente, String aDescrUfficioEmittente, String aCodLuogoEmittente,
			String aDescrLuogoEmittente, String aCodTipoUfficioEmittente, Date aDataEmissioneOrdinanza,
			String aGiorniScomputati, String aCodEsito, String aDescrEsito, Date aDataAnnotazioneEsito,
			String aAnnotazione, BigDecimal aNumeroGiorniNoFruiti, BigDecimal aNumeroOreNoFruite,
			BigDecimal aNumeroMesi, BigDecimal aSommaRisarcDanni, String aCodMotivoDetenzione) {

		this.mIdLicenzaLibanticipata = aIdLicenzaLibanticipata;
		this.mCodTipoLicenza = aCodTipoLicenza;
		this.mDescrTipoLicenza = aDescrTipoLicenza;
		this.mNumeroGiorni = aNumeroGiorni;
		this.mDataInizio = aDataInizio;
		this.mOraInizio = aOraInizio;
		this.mDataFine = aDataFine;
		this.mOraFine = aOraFine;
		this.mLuogoSvolgimentoProva = aLuogoSvolgimentoProva;
		this.mDataDetenzRifDa = aDataDetenzRifDa;
		this.mDataDetenzRifA = aDataDetenzRifA;
		this.mFlagInfrazioneObblighi = aFlagInfrazioneObblighi;
		this.mDataInfrazioneObblighi = aDataInfrazioneObblighi;
		this.mDescrInfrazioneObblighi = aDescrInfrazioneObblighi;
		this.mFlagScomputo = aFlagScomputo;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mEveIdEvento = aEveIdEvento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mFlagConcesso = aFlagConcesso;
		this.mFlagElaborato = aFlagElaborato;
		this.mFlagScorta = aFlagScorta;
		this.mCodStatoPermesso = aCodStatoPermesso;
		this.mDescrStatoPermesso = aDescrStatoPermesso;
		this.mNumeroOre = aNumeroOre;
		this.mAnnoSius = aAnnoSius;
		this.mNumeroSius = aNumeroSius;
		this.mAnnoOrdinanza = aAnnoOrdinanza;
		this.mNumeroOrdinanza = aNumeroOrdinanza;
		this.mCodUfficioEmittente = aCodUfficioEmittente;
		this.mDescrUfficioEmittente = aDescrUfficioEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		// MEV10-s3: aggiunta impostazione di proprietà
		this.mCodTipoUfficioEmittente = aCodTipoUfficioEmittente;
		this.mDataEmissioneOrdinanza = aDataEmissioneOrdinanza;
		this.mGiorniScomputati = aGiorniScomputati;
		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mDataAnnotazioneEsito = aDataAnnotazioneEsito;
		this.mAnnotazione = aAnnotazione;
		this.mNumeroGiorniNoFruiti = aNumeroGiorniNoFruiti;
		this.mNumeroOreNoFruite = aNumeroOreNoFruite;
		this.mConProvvedimentoValidato = false;
		this.mNumeroMesi = aNumeroMesi;
		this.mSommaRisarcDanni = aSommaRisarcDanni;
		// MEV_2025-48
		this.mCodMotivoDetenzione = aCodMotivoDetenzione;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdLicenzaLibanticipata() {
		return mIdLicenzaLibanticipata;
	}

	public String getCodTipoLicenza() {
		return mCodTipoLicenza;
	}

	public String getDescrTipoLicenza() {
		return mDescrTipoLicenza;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public String getOraInizio() {
		return mOraInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public String getOraFine() {
		return mOraFine;
	}

	public String getLuogoSvolgimentoProva() {
		return mLuogoSvolgimentoProva;
	}

	public Date getDataDetenzRifDa() {
		return mDataDetenzRifDa;
	}

	public Date getDataDetenzRifA() {
		return mDataDetenzRifA;
	}

	public String getFlagInfrazioneObblighi() {
		return mFlagInfrazioneObblighi;
	}

	public Date getDataInfrazioneObblighi() {
		return mDataInfrazioneObblighi;
	}

	public String getDescrInfrazioneObblighi() {
		return mDescrInfrazioneObblighi;
	}

	public String getFlagScomputo() {
		return mFlagScomputo;
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

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getFlagConcesso() {
		return mFlagConcesso;
	}

	public String getFlagElaborato() {
		return mFlagElaborato;
	}

	public String getFlagScorta() {
		return mFlagScorta;
	}

	public String getCodStatoPermesso() {
		return mCodStatoPermesso;
	}

	public String getDescrStatoPermesso() {
		return mDescrStatoPermesso;
	}

	public BigDecimal getNumeroOre() {
		return mNumeroOre;
	}

	public BigDecimal getAnnoSius() {
		return mAnnoSius;
	}

	public String getNumeroSius() {
		return mNumeroSius;
	}

	public BigDecimal getAnnoOrdinanza() {
		return mAnnoOrdinanza;
	}

	public BigDecimal getNumeroOrdinanza() {
		return mNumeroOrdinanza;
	}

	public String getCodUfficioEmittente() {
		return mCodUfficioEmittente;
	}

	public String getDescrUfficioEmittente() {
		return mDescrUfficioEmittente;
	}

	public String getCodLuogoEmittente() {
		return mCodLuogoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	// MEV10-s3: aggiunta impostazione di proprietà
	public String getCodTipoUfficioEmittente() {
		return mCodTipoUfficioEmittente;
	}

	public Date getDataEmissioneOrdinanza() {
		return mDataEmissioneOrdinanza;
	}

	public String getGiorniScomputati() {
		return mGiorniScomputati;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public Date getDataAnnotazioneEsito() {
		return mDataAnnotazioneEsito;
	}

	public String getAnnotazione() {
		return mAnnotazione;
	}

	public BigDecimal getNumeroGiorniNoFruiti() {
		return mNumeroGiorniNoFruiti;
	}

	public BigDecimal getNumeroOreNoFruite() {
		return mNumeroOreNoFruite;
	}

	public boolean isConProvvedimentoValidato() {
		return mConProvvedimentoValidato;
	}

	public BigDecimal getNumeroGiorni() {
		if (mNumeroGiorni != null)
			return mNumeroGiorni;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getNumeroMesi() {
		if (mNumeroMesi != null)
			return mNumeroMesi;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getSommaRisarcDanni() {
		return mSommaRisarcDanni;
	}

	// MERGE v10: aggiunti parametri per recupero informazioni del
	// Rimedio Risarcitorio al quale è legato un Reclamo Rimedio Risarcitorio
	public BigDecimal getAnnoProvvRR() {
		return mAnnoProvvRR;
	}

	public String getDescrUfficioEmittenteRR() {
		return mDescrUfficioEmittenteRR;
	}

	public String getCodTipoUfficioEmittenteRR() {
		return mCodTipoUfficioEmittenteRR;
	}

	public String getDescrLuogoEmittenteRR() {
		return mDescrLuogoEmittenteRR;
	}

	public String getCodMotivoDetenzione() {
		return mCodMotivoDetenzione;
	}

	public String getDescrMotivoDetenzione() {
		return mDescrMotivoDetenzione;
	}

	//
	// METODI SET()
	//
	public void setIdLicenzaLibanticipata(BigDecimal aValore) {
		mIdLicenzaLibanticipata = aValore;
	}

	public void setCodTipoLicenza(String aValore) {
		mCodTipoLicenza = aValore;
	}

	public void setDescrTipoLicenza(String aValore) {
		mDescrTipoLicenza = aValore;
	}

	public void setNumeroGiorni(BigDecimal aValore) {
		mNumeroGiorni = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setOraInizio(String aValore) {
		mOraInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setOraFine(String aValore) {
		mOraFine = aValore;
	}

	public void setLuogoSvolgimentoProva(String aValore) {
		mLuogoSvolgimentoProva = aValore;
	}

	public void setDataDetenzRifDa(Date aValore) {
		mDataDetenzRifDa = aValore;
	}

	public void setDataDetenzRifA(Date aValore) {
		mDataDetenzRifA = aValore;
	}

	public void setFlagInfrazioneObblighi(String aValore) {
		mFlagInfrazioneObblighi = aValore;
	}

	public void setDataInfrazioneObblighi(Date aValore) {
		mDataInfrazioneObblighi = aValore;
	}

	public void setDescrInfrazioneObblighi(String aValore) {
		mDescrInfrazioneObblighi = aValore;
	}

	public void setFlagScomputo(String aValore) {
		mFlagScomputo = aValore;
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setFlagConcesso(String aValore) {
		mFlagConcesso = aValore;
	}

	public void setFlagElaborato(String aValore) {
		mFlagElaborato = aValore;
	}

	public void setFlagScorta(String aValore) {
		mFlagScorta = aValore;
	}

	public void setCodStatoPermesso(String aValore) {
		mCodStatoPermesso = aValore;
	}

	public void setDescrStatoPermesso(String aValore) {
		mDescrStatoPermesso = aValore;
	}

	public void setNumeroOre(BigDecimal aValore) {
		mNumeroOre = aValore;
	}

	public void setAnnoSius(BigDecimal aValore) {
		mAnnoSius = aValore;
	}

	public void setNumeroSius(String aValore) {
		mNumeroSius = aValore;
	}

	public void setAnnoOrdinanza(BigDecimal aValore) {
		mAnnoOrdinanza = aValore;
	}

	public void setNumeroOrdinanza(BigDecimal aValore) {
		mNumeroOrdinanza = aValore;
	}

	public void setCodUfficioEmittente(String aValore) {
		mCodUfficioEmittente = aValore;
	}

	public void setDescrUfficioEmittente(String aValore) {
		mDescrUfficioEmittente = aValore;
	}

	public void setCodLuogoEmittente(String aValore) {
		mCodLuogoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	// MEV10-s3: aggiunta impostazione di proprietà
	public void setCodTipoUfficioEmittente(String aValore) {
		mCodTipoUfficioEmittente = aValore;
	}

	public void setDataEmissioneOrdinanza(Date aValore) {
		mDataEmissioneOrdinanza = aValore;
	}

	public void setGiorniScomputati(String aValore) {
		mGiorniScomputati = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setDataAnnotazioneEsito(Date aValore) {
		mDataAnnotazioneEsito = aValore;
	}

	public void setAnnotazione(String aValore) {
		mAnnotazione = aValore;
	}

	public void setNumeroGiorniNoFruiti(BigDecimal aValore) {
		mNumeroGiorniNoFruiti = aValore;
	}

	public void setNumeroOreNoFruite(BigDecimal aValore) {
		mNumeroOreNoFruite = aValore;
	}

	public void setConProvvedimentoValidato(boolean aValore) {
		mConProvvedimentoValidato = aValore;
	}

	public void setNumeroMesi(BigDecimal aValore) {
		mNumeroMesi = aValore;
	}

	public void setSommaRisarcDanni(BigDecimal aValore) {
		mSommaRisarcDanni = aValore;
	}

	public void setAnnoProvvRR(BigDecimal mAnnoProvvRR) {
		this.mAnnoProvvRR = mAnnoProvvRR;
	}

	public BigDecimal getNumeroProvvRR() {
		return mNumeroProvvRR;
	}

	public void setNumeroProvvRR(BigDecimal mNumeroProvvRR) {
		this.mNumeroProvvRR = mNumeroProvvRR;
	}

	public void setCodTipoUfficioEmittenteRR(String mCodTipoUfficioEmittenteRR) {
		this.mCodTipoUfficioEmittenteRR = mCodTipoUfficioEmittenteRR;
	}

	public void setDescrUfficioEmittenteRR(String mDescrUfficioEmittenteRR) {
		this.mDescrUfficioEmittenteRR = mDescrUfficioEmittenteRR;
	}

	public void setDescrLuogoEmittenteRR(String mDescrLuogoEmittenteRR) {
		this.mDescrLuogoEmittenteRR = mDescrLuogoEmittenteRR;
	}

	public Date getDataEmissioneRR() {
		return mDataEmissioneRR;
	}

	public void setDataEmissioneRR(Date mDataEmissioneRR) {
		this.mDataEmissioneRR = mDataEmissioneRR;
	}
	// **********************************************

	public void setCodMotivoDetenzione(String mCodMotivoDetenzione) {
		this.mCodMotivoDetenzione = mCodMotivoDetenzione;
	}

	public void setDescrMotivoDetenzione(String mDescrMotivoDetenzione) {
		this.mDescrMotivoDetenzione = mDescrMotivoDetenzione;
	}

	public String toString2() {

		String lStr = new String();

		lStr = "" + mIdLicenzaLibanticipata + " - " + mCodTipoLicenza + " - " + mDescrTipoLicenza + " - "
				+ mNumeroGiorni + " - " + mDataInizio + " - " + mOraInizio + " - " + mDataFine + " - "
				+ mOraFine + " - " + mLuogoSvolgimentoProva + " - " + mDataDetenzRifDa + " - "
				+ mDataDetenzRifA + " - " + mFlagInfrazioneObblighi + " - " + mDataInfrazioneObblighi + " - "
				+ mDescrInfrazioneObblighi + " - " + mFlagScomputo + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mFasSiuIdFascicoloSius + " - " + mEveIdEvento
				+ " - " + mFasSieIdFascicoloSiep + " - " + mFlagConcesso + " - " + mFlagElaborato + " - "
				+ mFlagScorta + " - " + mCodStatoPermesso + " - " + mDescrStatoPermesso + " - " + mNumeroOre
				+ " - " + mAnnoSius + " - " + mNumeroSius + " - " + mAnnoOrdinanza + " - " + mNumeroOrdinanza
				+ " - " + mCodUfficioEmittente + " - " + mDescrUfficioEmittente + " - " + mCodLuogoEmittente
				+ " - " + mDescrLuogoEmittente + " - "
				// MEV10-s3: aggiunta proprietà
				+ mCodTipoUfficioEmittente + " - " + mDataEmissioneOrdinanza + " - " + mGiorniScomputati
				+ " - " + mCodEsito + " - " + mDescrEsito + " - " + mDataAnnotazioneEsito + " - "
				+ mConProvvedimentoValidato + " - " + mNumeroMesi
				// MEV_2025-48
				+ mCodMotivoDetenzione;

		return lStr;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {

		String lStr = new String();

		lStr = "LicenzaLibanticipataModel:\n" + "[ mIdLicenzaLibanticipata    = " + mIdLicenzaLibanticipata
				+ " ]\n" + "[ mCodTipoLicenza            = " + mCodTipoLicenza + " ]\n"
				+ "[ mDescrTipoLicenza          = " + mDescrTipoLicenza + " ]\n"
				+ "[ mNumeroGiorni              = " + mNumeroGiorni + " ]\n"
				+ "[ mDataInizio                = " + mDataInizio + " ]\n" + "[ mOraInizio                 = "
				+ mOraInizio + " ]\n" + "[ mDataFine                  = " + mDataFine + " ]\n"
				+ "[ mOraFine                   = " + mOraFine + " ]\n" + "[ mLuogoSvolgimentoProva     = "
				+ mLuogoSvolgimentoProva + " ]\n" + "[ mDataDetenzRifDa           = " + mDataDetenzRifDa
				+ " ]\n" + "[ mDataDetenzRifA            = " + mDataDetenzRifA + " ]\n"
				+ "[ mFlagInfrazioneObblighi    = " + mFlagInfrazioneObblighi + " ]\n"
				+ "[ mDataInfrazioneObblighi    = " + mDataInfrazioneObblighi + " ]\n"
				+ "[ mDescrInfrazioneObblighi   = " + mDescrInfrazioneObblighi + " ]\n"
				+ "[ mFlagScomputo              = " + mFlagScomputo + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mDescrUfficioInserimento   = " + mDescrUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mDescrUfficioAggiornamento = " + mDescrUfficioAggiornamento + " ]\n"
				+ "[ mFasSiuIdFascicoloSius     = " + mFasSiuIdFascicoloSius + " ]\n"
				+ "[ mEveIdEvento               = " + mEveIdEvento + " ]\n"
				+ "[ mFasSieIdFascicoloSiep     = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mFlagConcesso              = " + mFlagConcesso + " ]\n"
				+ "[ mFlagElaborato             = " + mFlagElaborato + " ]\n"
				+ "[ mFlagScorta                = " + mFlagScorta + " ]\n" + "[ mCodStatoPermesso          = "
				+ mCodStatoPermesso + " ]\n" + "[ mDescrStatoPermesso        = " + mDescrStatoPermesso
				+ " ]\n" + "[ mNumeroOre                 = " + mNumeroOre + " ]\n"
				+ "[ mAnnoSius                  = " + mAnnoSius + " ]\n" + "[ mNumeroSius                = "
				+ mNumeroSius + " ]\n" + "[ mAnnoOrdinanza             = " + mAnnoOrdinanza + " ]\n"
				+ "[ mNumeroOrdinanza           = " + mNumeroOrdinanza + " ]\n"
				+ "[ mCodUfficioEmittente       = " + mCodUfficioEmittente + " ]\n"
				+ "[ mDescrUfficioEmittente     = " + mDescrUfficioEmittente + " ]\n"
				+ "[ mCodLuogoEmittente         = " + mCodLuogoEmittente + " ]\n"
				+ "[ mDescrLuogoEmittente       = " + mDescrLuogoEmittente + " ]\n"
				// MEV10-s3: aggiunta proprietà
				+ "[ mCodTipoUfficioEmittente   = " + mCodTipoUfficioEmittente + " ]\n"
				+ "[ mDataEmissioneOrdinanza    = " + mDataEmissioneOrdinanza + " ]\n"
				+ "[ mGiorniScomputati          = " + mGiorniScomputati + " ]\n"
				+ "[ mCodEsito                  = " + mCodEsito + " ]\n" + "[ mDescrEsito                = "
				+ mDescrEsito + " ]\n" + "[ mDataAnnotazioneEsito      = " + mDataAnnotazioneEsito + " ]\n"
				+ "[ mAnnotazione               = " + mAnnotazione + " ]\n"
				+ "[ mNumeroGiorniNoFruiti      = " + mNumeroGiorniNoFruiti + " ]\n"
				+ "[ mNumeroOreNoFruite         = " + mNumeroOreNoFruite + " ]\n"
				+ "[ mConProvvedimentoValidato  = " + mConProvvedimentoValidato + " ]\n"
				+ "[ mNumeroMesi                = " + mNumeroMesi + " ]\n" + "[ mSommaRisarcDanni          = "
				+ mSommaRisarcDanni + " ]\n"
				// MEV_2025-48
				+ "[ mCodMotivoDetenzione 		= " + mCodMotivoDetenzione + " ]";

		return lStr;
	}

}