package siap.sius.depositodecreto.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.ufficio.model.UfficioModel;

/**
 * <p>
 * Title: DepositoDecretoModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il DepositoDecreto
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
public class DepositoDecretoModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -2057270908061067016L;

	private BigDecimal mIdDepositoDecreto;
	private BigDecimal mAnnoS72;
	private BigDecimal mNumS72;
	private String mCodTipoDecreto;
	private String mDescrTipoDecreto;
	private Date mDataEmissione;
	private Date mDataDeposito;
	private String mCodMagistrato;
	private String mDescrMagistrato;
	private String mAltriDestinatari;
	private Date mDataParerePg;
	private String mCodTipoParerePg;
	private String mDescrTipoParerePg;
	private Date mDataRicorsoImpugnazione;
	private Date mDataInvioAttiImpugnazione;
	private Date mDataSentenzaImpugnazione;
	private String mTenoreSentenzaImpugnazione;
	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mGenPridGeneraleProcedimento;
	private String mSentenzeRiferimento;
	private BigDecimal mIdEventoGenerato;
	// nuovi campi Luigi 17-11-2003
	private String mCodUfficioComp;
	private String mDescrUfficioComp;
	private String mCodProcuraEsecuzione;
	private String mDescrProcuraEsecuzione;
	private String mLuogoSvolgimentoProva;
	private UfficioModel mUfficioCompetente;
	private UfficioModel mProcuraEsecuzione;
	// nuovi campi Luigi 27-01-2004
	private String mCodTdsComp;
	private String mDescrTdsComp;
	private String mIstDetIdIstitutoDetenzione;
	private String mStatusPersona;
	private String mTotOreRaggiungimento;
	private String mAnnoProcRevocato;
	private String mProgrProcRevocato;
	private String mCodProcuraRevocato;
	private String mDescrProcuraRevocato;
	private Date mDataCompFoglioComplementare;
	// Nuovi campi per Sospensione Sanzioni Sostitutive
	private Date mDataSospensioneSS;
	private BigDecimal mGiorniRecuperoSS;
	private String mFlagRecuperoSS;
	private Date mDataScadenzaSospensioneSS;
	private BigDecimal mSospensioneGGSS;
	private BigDecimal mSospensioneMMSS;
	private BigDecimal mSospensioneAASS;
	private String mFlagNominaComActa;
	private String mDescrCommActa;
	private String mCodTipoControlloEsecuzione;
	private String mDescrTipoControlloEsecuzione;
	// 07/2014
	private BigDecimal mNumGiorniRevocaLA;
	// DL 92 2014 Violazione CEDU
	private BigDecimal mNumGiorniRiduzionePena;
	private BigDecimal mSommaRisarcimentoDanni;
	// 02/2015 Mis.Sic
	private String mFlagElaborato;
	// MEV_2019-09 aggiunti campi DATA_TERMINE_EMISSIONE e NUM_GIORNI_TERMINE_EMISSIONE
	private Date mDataTermineEmissione;
	private BigDecimal mNumGiorniTermineEmissione;

	// COSTRUTTORE DI DEFAULT
	public DepositoDecretoModel() {

		this.mIdDepositoDecreto = null;
		this.mAnnoS72 = null;
		this.mNumS72 = null;
		this.mCodTipoDecreto = "";
		this.mDescrTipoDecreto = "";
		this.mDataEmissione = null;
		this.mDataDeposito = null;
		this.mCodMagistrato = "";
		this.mDescrMagistrato = "";
		this.mAltriDestinatari = "";
		this.mDataParerePg = null;
		this.mCodTipoParerePg = "";
		this.mDescrTipoParerePg = "";
		this.mDataRicorsoImpugnazione = null;
		this.mDataInvioAttiImpugnazione = null;
		this.mDataSentenzaImpugnazione = null;
		this.mTenoreSentenzaImpugnazione = "";
		this.mNote = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mGenPridGeneraleProcedimento = null;
		this.mSentenzeRiferimento = "";
		this.mIdEventoGenerato = null;
		// nuovi campi Luigi 17-11-2003
		this.mCodUfficioComp = "";
		mDescrUfficioComp = "";
		this.mCodProcuraEsecuzione = "";
		mDescrProcuraEsecuzione = "";
		this.mLuogoSvolgimentoProva = "";
		this.mUfficioCompetente = null;
		this.mProcuraEsecuzione = null;
		// nuovi campi Luigi 27-01-2004
		this.mCodTdsComp = "";
		this.mDescrTdsComp = "";
		// modifica del 10/03/2004 per trattare l'id dell'istitituto come un BigDecimal
		// this.mIstDetIdIstitutoDetenzione = "";
		this.mIstDetIdIstitutoDetenzione = null;
		this.mStatusPersona = "";
		this.mTotOreRaggiungimento = "";
		this.mAnnoProcRevocato = "";
		this.mProgrProcRevocato = "";
		this.mCodProcuraRevocato = "";
		this.mDescrProcuraRevocato = "";
		this.mDataCompFoglioComplementare = null;
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		this.mDataSospensioneSS = null;
		this.mGiorniRecuperoSS = null;
		this.mFlagRecuperoSS = "";
		this.mDataScadenzaSospensioneSS = null;
		this.mSospensioneGGSS = null;
		this.mSospensioneMMSS = null;
		this.mSospensioneAASS = null;
		this.mFlagNominaComActa = null;
		this.mDescrCommActa = null;
		this.mCodTipoControlloEsecuzione = null;
		this.mDescrTipoControlloEsecuzione = null;
		// 07/2014
		this.mNumGiorniRevocaLA = null;
		// DL 92 2014 Violazione CEDU
		this.mNumGiorniRiduzionePena = null;
		this.mSommaRisarcimentoDanni = null;
		// 02/2015
		this.mFlagElaborato = "";
		// MEV_2019-09 aggiunti campi DATA_TERMINE_EMISSIONE e NUM_GIORNI_TERMINE_EMISSIONE
		this.mDataTermineEmissione = null;
		this.mNumGiorniTermineEmissione = null;
	}

	// COSTRUTTORE DI COPIA
	public DepositoDecretoModel(DepositoDecretoModel aModel) {

		this.mIdDepositoDecreto = aModel.mIdDepositoDecreto;
		this.mAnnoS72 = aModel.mAnnoS72;
		this.mNumS72 = aModel.mNumS72;
		this.mCodTipoDecreto = aModel.mCodTipoDecreto;
		this.mDescrTipoDecreto = aModel.mDescrTipoDecreto;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mDataDeposito = aModel.mDataDeposito;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mDescrMagistrato = aModel.mDescrMagistrato;
		this.mAltriDestinatari = aModel.mAltriDestinatari;
		this.mDataParerePg = aModel.mDataParerePg;
		this.mCodTipoParerePg = aModel.mCodTipoParerePg;
		this.mDescrTipoParerePg = aModel.mDescrTipoParerePg;
		this.mDataRicorsoImpugnazione = aModel.mDataRicorsoImpugnazione;
		this.mDataInvioAttiImpugnazione = aModel.mDataInvioAttiImpugnazione;
		this.mDataSentenzaImpugnazione = aModel.mDataSentenzaImpugnazione;
		this.mTenoreSentenzaImpugnazione = aModel.mTenoreSentenzaImpugnazione;
		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mGenPridGeneraleProcedimento = aModel.mGenPridGeneraleProcedimento;
		this.mSentenzeRiferimento = aModel.mSentenzeRiferimento;
		this.mIdEventoGenerato = aModel.mIdEventoGenerato;
		// nuovi campi Luigi 17-11-2003
		this.mCodUfficioComp = aModel.mCodUfficioComp;
		mDescrUfficioComp = aModel.mDescrUfficioComp;
		this.mCodProcuraEsecuzione = aModel.mCodProcuraEsecuzione;
		mDescrProcuraEsecuzione = aModel.mDescrProcuraEsecuzione;
		this.mLuogoSvolgimentoProva = aModel.mLuogoSvolgimentoProva;
		this.mUfficioCompetente = aModel.mUfficioCompetente;
		this.mProcuraEsecuzione = aModel.mProcuraEsecuzione;
		// nuovi campi Luigi 27-01-2004
		this.mCodTdsComp = aModel.mCodTdsComp;
		this.mDescrTdsComp = aModel.mDescrTdsComp;
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mStatusPersona = aModel.mStatusPersona;
		this.mTotOreRaggiungimento = aModel.mTotOreRaggiungimento;
		this.mAnnoProcRevocato = aModel.mAnnoProcRevocato;
		this.mProgrProcRevocato = aModel.mProgrProcRevocato;
		this.mCodProcuraRevocato = aModel.mCodProcuraRevocato;
		this.mDescrProcuraRevocato = aModel.mDescrProcuraRevocato;
		this.mDataCompFoglioComplementare = aModel.mDataCompFoglioComplementare;
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		this.mDataSospensioneSS = aModel.mDataSospensioneSS;
		this.mGiorniRecuperoSS = aModel.mGiorniRecuperoSS;
		this.mFlagRecuperoSS = aModel.mFlagRecuperoSS;
		this.mDataScadenzaSospensioneSS = aModel.mDataScadenzaSospensioneSS;
		this.mSospensioneGGSS = aModel.mSospensioneGGSS;
		this.mSospensioneMMSS = aModel.mSospensioneMMSS;
		this.mSospensioneAASS = aModel.mSospensioneAASS;
		this.mFlagNominaComActa = aModel.mFlagNominaComActa;
		this.mDescrCommActa = aModel.mDescrCommActa;
		this.mCodTipoControlloEsecuzione = aModel.mCodTipoControlloEsecuzione;
		this.mDescrTipoControlloEsecuzione = aModel.mDescrTipoControlloEsecuzione;
		// 07/2014
		this.mNumGiorniRevocaLA = aModel.mNumGiorniRevocaLA;
		// DL 92 2014 Violazione CEDU
		this.mNumGiorniRiduzionePena = aModel.mNumGiorniRiduzionePena;
		this.mSommaRisarcimentoDanni = aModel.mSommaRisarcimentoDanni;
		// 02/2015 Mis.Sic.
		this.mFlagElaborato = aModel.mFlagElaborato;
		// MEV_2019-09 aggiunti campi DATA_TERMINE_EMISSIONE e NUM_GIORNI_TERMINE_EMISSIONE
		this.mDataTermineEmissione = aModel.mDataTermineEmissione;
		this.mNumGiorniTermineEmissione = aModel.mNumGiorniTermineEmissione;
	}

	// COSTRUTTORE MODEL
	public DepositoDecretoModel(BigDecimal aIdDepositoDecreto, BigDecimal aAnnoS72, BigDecimal aNumS72,
			String aCodTipoDecreto, String aDescrTipoDecreto, Date aDataEmissione, Date aDataDeposito,
			String aCodMagistrato, String aDescrMagistrato, String aAltriDestinatari, Date aDataParerePg,
			String aCodTipoParerePg, String aDescrTipoParerePg, Date aDataRicorsoImpugnazione,
			Date aDataInvioAttiImpugnazione, Date aDataSentenzaImpugnazione,
			String aTenoreSentenzaImpugnazione, String aNote, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aGenPridGeneraleProcedimento,
			String aSentenzeRiferimento, BigDecimal aIdEventoGenerato,
			// nuovi campi Luigi 17-11-2003
			String aCodUfficioComp, String aDescrUfficioComp, String aCodProcuraEsecuzione,
			String aDescrProcuraEsecuzione, String aLuogoSvolgimentoProva,
			// nuovi campi Luigi 27-01-2004
			String aCodTdsComp, String aDescrTdsComp, String aIstDetIdIstitutoDetenzione,
			String aStatusPersona, String aTotOreRaggiungimento, String aAnnoProcRevocato,
			String aProgrProcRevocato, String aUfficioProcRevocato, String aDescrProcuraRevocato,
			Date aDataCompFoglioComplementare,
			// Nuovi campi per Sospensione Sanzioni Sostitutive
			Date aDataSospensioneSS, BigDecimal aGiorniRecuperoSS, String aFlagRecuperoSS,
			Date aDataScadenzaSospensioneSS, BigDecimal aSospensioneGGSS, BigDecimal aSospensioneMMSS,
			BigDecimal aSospensioneAASS, String aFlagNominaComActa, String aDescrCommActa,
			String aCodTipoControlloEsecuzione, String aDescrTipoControlloEsecuzione,
			// 07/2014
			BigDecimal aNumGiorniRevocaLA,
			// DL 92 2014 Violazione CEDU
			BigDecimal aNumGiorniRiduzionePena, BigDecimal aSommaRisarcimentoDanni,
			// 02/2015 Mis.Sic.
			String aFlagElaborato,
			// MEV_2019-09 aggiunti campi DATA_TERMINE_EMISSIONE e NUM_GIORNI_TERMINE_EMISSIONE
			Date aDataTermineEmissione, BigDecimal aNumGiorniTermineEmissione) {

		this.mIdDepositoDecreto = aIdDepositoDecreto;
		this.mAnnoS72 = aAnnoS72;
		this.mNumS72 = aNumS72;
		this.mCodTipoDecreto = aCodTipoDecreto;
		this.mDescrTipoDecreto = aDescrTipoDecreto;
		this.mDataEmissione = aDataEmissione;
		this.mDataDeposito = aDataDeposito;
		this.mCodMagistrato = aCodMagistrato;
		this.mDescrMagistrato = aDescrMagistrato;
		this.mAltriDestinatari = aAltriDestinatari;
		this.mDataParerePg = aDataParerePg;
		this.mCodTipoParerePg = aCodTipoParerePg;
		this.mDescrTipoParerePg = aDescrTipoParerePg;
		this.mDataRicorsoImpugnazione = aDataRicorsoImpugnazione;
		this.mDataInvioAttiImpugnazione = aDataInvioAttiImpugnazione;
		this.mDataSentenzaImpugnazione = aDataSentenzaImpugnazione;
		this.mTenoreSentenzaImpugnazione = aTenoreSentenzaImpugnazione;
		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mGenPridGeneraleProcedimento = aGenPridGeneraleProcedimento;
		this.mSentenzeRiferimento = aSentenzeRiferimento;
		this.mIdEventoGenerato = aIdEventoGenerato;
		// nuovi campi Luigi 17-11-2003
		this.mCodUfficioComp = aCodUfficioComp;
		mDescrUfficioComp = aDescrUfficioComp;
		this.mCodProcuraEsecuzione = aCodProcuraEsecuzione;
		mDescrProcuraEsecuzione = aDescrProcuraEsecuzione;
		this.mLuogoSvolgimentoProva = aLuogoSvolgimentoProva;
		this.mUfficioCompetente = null;
		this.mProcuraEsecuzione = null;
		// nuovi campi Luigi 27-01-2004
		this.mCodTdsComp = aCodTdsComp;
		this.mDescrTdsComp = aDescrTdsComp;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mStatusPersona = aStatusPersona;
		this.mTotOreRaggiungimento = aTotOreRaggiungimento;
		this.mAnnoProcRevocato = aAnnoProcRevocato;
		this.mProgrProcRevocato = aProgrProcRevocato;
		this.mCodProcuraRevocato = aUfficioProcRevocato;
		this.mDescrProcuraRevocato = aDescrProcuraRevocato;
		this.mDataCompFoglioComplementare = aDataCompFoglioComplementare;
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		this.mDataSospensioneSS = aDataSospensioneSS;
		this.mGiorniRecuperoSS = aGiorniRecuperoSS;
		this.mFlagRecuperoSS = aFlagRecuperoSS;
		this.mDataScadenzaSospensioneSS = aDataScadenzaSospensioneSS;
		this.mSospensioneGGSS = aSospensioneGGSS;
		this.mSospensioneMMSS = aSospensioneMMSS;
		this.mSospensioneAASS = aSospensioneAASS;
		this.mFlagNominaComActa = aFlagNominaComActa;
		this.mDescrCommActa = aDescrCommActa;
		this.mCodTipoControlloEsecuzione = aCodTipoControlloEsecuzione;
		this.mDescrTipoControlloEsecuzione = aDescrTipoControlloEsecuzione;
		// 07/2014
		this.mNumGiorniRevocaLA = aNumGiorniRevocaLA;
		// DL 92 2014 Violazione CEDU
		this.mNumGiorniRiduzionePena = aNumGiorniRiduzionePena;
		this.mSommaRisarcimentoDanni = aSommaRisarcimentoDanni;
		// 02/2015 Mis.Sic.
		this.mFlagElaborato = aFlagElaborato;
		// MEV_2019-09 aggiunti campi DATA_TERMINE_EMISSIONE e NUM_GIORNI_TERMINE_EMISSIONE
		this.mDataTermineEmissione = aDataTermineEmissione;
		this.mNumGiorniTermineEmissione = aNumGiorniTermineEmissione;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdDepositoDecreto() {
		return mIdDepositoDecreto;
	}

	public BigDecimal getAnnoS72() {
		return mAnnoS72;
	}

	public BigDecimal getNumS72() {
		return mNumS72;
	}

	public String getCodTipoDecreto() {
		return mCodTipoDecreto;
	}

	public String getDescrTipoDecreto() {
		return mDescrTipoDecreto;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public Date getDataDeposito() {
		return mDataDeposito;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getDescrMagistrato() {
		return mDescrMagistrato;
	}

	public String getAltriDestinatari() {
		return mAltriDestinatari;
	}

	public Date getDataParerePg() {
		return mDataParerePg;
	}

	public String getCodTipoParerePg() {
		return mCodTipoParerePg;
	}

	public String getDescrTipoParerePg() {
		return mDescrTipoParerePg;
	}

	public Date getDataRicorsoImpugnazione() {
		return mDataRicorsoImpugnazione;
	}

	public Date getDataInvioAttiImpugnazione() {
		return mDataInvioAttiImpugnazione;
	}

	public Date getDataSentenzaImpugnazione() {
		return mDataSentenzaImpugnazione;
	}

	public String getTenoreSentenzaImpugnazione() {
		return mTenoreSentenzaImpugnazione;
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

	public BigDecimal getGenPridGeneraleProcedimento() {
		return mGenPridGeneraleProcedimento;
	}

	public String getSentenzeRiferimento() {
		return mSentenzeRiferimento;
	}

	public BigDecimal getIdEventoGenerato() {
		return mIdEventoGenerato;
	}

	// nuovi campi Luigi 17-11-2003
	public String getCodUfficioCompetente() {
		return mCodUfficioComp;
	}

	public String getDescrUfficioCompetente() {
		return mDescrUfficioComp;
	}

	public String getCodProcuraEsecuzione() {
		return mCodProcuraEsecuzione;
	}

	public String getDescrProcuraEsecuzione() {
		return mDescrProcuraEsecuzione;
	}

	public String getLuogoSvolgimentoProva() {
		return mLuogoSvolgimentoProva;
	}

	public UfficioModel getUfficioCompetente() {
		return mUfficioCompetente;
	}

	public UfficioModel getProcuraEsecuzione() {
		return mProcuraEsecuzione;
	}

	// nuovi campi Luigi 27-01-2004
	public String getCodTdsComp() {
		return mCodTdsComp;
	}

	public String getDescrTdsComp() {
		return mDescrTdsComp;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public String getStatusPersona() {
		return mStatusPersona;
	}

	public String getTotOreRaggiungimento() {
		return mTotOreRaggiungimento;
	}

	public String getAnnoProcRevocato() {
		return mAnnoProcRevocato;
	}

	public String getProgrProcRevocato() {
		return mProgrProcRevocato;
	}

	public String getCodProcuraRevocato() {
		return mCodProcuraRevocato;
	}

	public String getDescrProcuraRevocato() {
		return mDescrProcuraRevocato;
	}

	public Date getDataCompFoglioComplementare() {
		return mDataCompFoglioComplementare;
	}

	// Nuovi campi per Sospensione Sanzioni Sostitutive
	public Date getDataSospensioneSS() {
		return mDataSospensioneSS;
	}

	public BigDecimal getGiorniRecuperoSS() {
		return mGiorniRecuperoSS;
	}

	public String getFlagRecuperoSS() {
		return mFlagRecuperoSS;
	}

	public Date getDataScadenzaSospensioneSS() {
		return mDataScadenzaSospensioneSS;
	}

	public BigDecimal getSospensioneGGSS() {
		if (mSospensioneGGSS != null)
			return mSospensioneGGSS;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getSospensioneMMSS() {
		if (mSospensioneMMSS != null)
			return mSospensioneMMSS;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getSospensioneAASS() {
		if (mSospensioneAASS != null)
			return mSospensioneAASS;
		else
			return new BigDecimal(0);
	}

	public String getFlagNominaComActa() {
		return mFlagNominaComActa;
	}

	public String getDescrCommActa() {
		return mDescrCommActa;
	}

	public String getCodTipoControlloEsecuzione() {
		return mCodTipoControlloEsecuzione;
	}

	public String getDescrTipoControlloEsecuzione() {
		return mDescrTipoControlloEsecuzione;
	}

	// 07/2014
	public BigDecimal getNumeroGiorniRevocaLA() {
		return mNumGiorniRevocaLA;
	}

	// DL 92 2014 Violazione CEDU
	public BigDecimal getNumGiorniRiduzionePena() {
		if (mNumGiorniRiduzionePena != null)
			return mNumGiorniRiduzionePena;
		else
			return new BigDecimal(0);
	}

	public BigDecimal getSommaRisarcimentoDanni() {
		if (mSommaRisarcimentoDanni != null)
			return mSommaRisarcimentoDanni;
		else
			return new BigDecimal(0);
	}

	// 02/2015 Mis.Sic.
	public String getFlagElaborato() {
		return mFlagElaborato;
	}

	// MEV_2019-09 aggiunto campo DATA_TERMINE_EMISSIONE
	public Date getDataTermineEmissione() {
		return mDataTermineEmissione;
	}

	// MEV_2019-09 aggiunto campo NUM_GIORNI_TERMINE_EMISSIONE
	public BigDecimal getNumGiorniTermineEmissione() {
		return mNumGiorniTermineEmissione;
	}

	//
	// METODI SET()
	//
	public void setIdDepositoDecreto(BigDecimal aValore) {
		mIdDepositoDecreto = aValore;
	}

	public void setAnnoS72(BigDecimal aValore) {
		mAnnoS72 = aValore;
	}

	public void setNumS72(BigDecimal aValore) {
		mNumS72 = aValore;
	}

	public void setCodTipoDecreto(String aValore) {
		mCodTipoDecreto = aValore;
	}

	public void setDescrTipoDecreto(String aValore) {
		mDescrTipoDecreto = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setDataDeposito(Date aValore) {
		mDataDeposito = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setDescrMagistrato(String aValore) {
		mDescrMagistrato = aValore;
	}

	public void setAltriDestinatari(String aValore) {
		mAltriDestinatari = aValore;
	}

	public void setDataParerePg(Date aValore) {
		mDataParerePg = aValore;
	}

	public void setCodTipoParerePg(String aValore) {
		mCodTipoParerePg = aValore;
	}

	public void setDescrTipoParerePg(String aValore) {
		mDescrTipoParerePg = aValore;
	}

	public void setDataRicorsoImpugnazione(Date aValore) {
		mDataRicorsoImpugnazione = aValore;
	}

	public void setDataInvioAttiImpugnazione(Date aValore) {
		mDataInvioAttiImpugnazione = aValore;
	}

	public void setDataSentenzaImpugnazione(Date aValore) {
		mDataSentenzaImpugnazione = aValore;
	}

	public void setTenoreSentenzaImpugnazione(String aValore) {
		mTenoreSentenzaImpugnazione = aValore;
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

	public void setGenPridGeneraleProcedimento(BigDecimal aValore) {
		mGenPridGeneraleProcedimento = aValore;
	}

	public void setSentenzeRiferimento(String aValore) {
		mSentenzeRiferimento = aValore;
	}

	public void setIdEventoGenerato(BigDecimal aValore) {
		mIdEventoGenerato = aValore;
	}

	// nuovi campi Luigi 17-11-2003
	public void setCodUfficioCompetente(String aValore) {
		mCodUfficioComp = aValore;
	}

	public void setDescrUfficioCompetente(String aValore) {
		mDescrUfficioComp = aValore;
	}

	public void setCodProcuraEsecuzione(String aValore) {
		mCodProcuraEsecuzione = aValore;
	}

	public void setDescrProcuraEsecuzione(String aValore) {
		mDescrProcuraEsecuzione = aValore;
	}

	public void setLuogoSvolgimentoProva(String aValore) {
		mLuogoSvolgimentoProva = aValore;
	}

	public void setUfficioCompetente(UfficioModel aValore) {
		mUfficioCompetente = aValore;
	}

	public void setProcuraEsecuzione(UfficioModel aValore) {
		mProcuraEsecuzione = aValore;
	}

	// nuovi campi Luigi 27-01-2004
	public void setCodTdsComp(String aValore) {
		mCodTdsComp = aValore;
	}

	public void setDescrTdsComp(String aValore) {
		mDescrTdsComp = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setStatusPersona(String aValore) {
		mStatusPersona = aValore;
	}

	public void setTotOreRaggiungimento(String aValore) {
		mTotOreRaggiungimento = aValore;
	}

	public void setAnnoProcRevocato(String aValore) {
		mAnnoProcRevocato = aValore;
	}

	public void setProgrProcRevocato(String aValore) {
		mProgrProcRevocato = aValore;
	}

	public void setUfficioProcRevocato(String aValore) {
		mCodProcuraRevocato = aValore;
	}

	public void setDescrProcuraRevocato(String aValore) {
		mDescrProcuraRevocato = aValore;
	}

	public void setDataCompFoglioComplementare(Date aValore) {
		mDataCompFoglioComplementare = aValore;
	}

	// Nuovi campi per Sospensione Sanzioni Sostitutive
	public void setDataSospensioneSS(Date aValore) {
		mDataSospensioneSS = aValore;
	}

	public void setGiorniRecuperoSS(BigDecimal aValore) {
		mGiorniRecuperoSS = aValore;
	}

	public void setFlagRecuperoSS(String aValore) {
		mFlagRecuperoSS = aValore;
	}

	public void setDataScadenzaSospensioneSS(Date aValore) {
		mDataScadenzaSospensioneSS = aValore;
	}

	public void setSospensioneGGSS(BigDecimal aValore) {
		mSospensioneGGSS = aValore;
	}

	public void setSospensioneMMSS(BigDecimal aValore) {
		mSospensioneMMSS = aValore;
	}

	public void setSospensioneAASS(BigDecimal aValore) {
		mSospensioneAASS = aValore;
	}

	public void setFlagNominaComActa(String aValore) {
		mFlagNominaComActa = aValore;
	}

	public void setDescrCommActa(String aValore) {
		mDescrCommActa = aValore;
	}

	public void setCodTipoControlloEsecuzione(String aValore) {
		mCodTipoControlloEsecuzione = aValore;
	}

	public void setDescrTipoControlloEsecuzione(String aValore) {
		mDescrTipoControlloEsecuzione = aValore;
	}

	public void setNumeroGiorniRevocaLA(BigDecimal aValore) {
		mNumGiorniRevocaLA = aValore;
	}

	// DL 92 2014 Violazione CEDU
	public void setNumeroGiorniRiduzionePena(BigDecimal aValore) {
		mNumGiorniRiduzionePena = aValore;
	}

	public void setSommaRisarcimentoDanni(BigDecimal aValore) {
		mSommaRisarcimentoDanni = aValore;
	}

	// 02/2015 Mis.Sic.
	public void setFlagElaborato(String aValore) {
		mFlagElaborato = aValore;
	}

	// MEV_2019-09 aggiunto campo DATA_TERMINE_EMISSIONE
	public void setDataTermineEmissione(Date aValore) {
		mDataTermineEmissione = aValore;
	}

	// MEV_2019-09 aggiunto campo NUM_GIORNI_TERMINE_EMISSIONE
	public void setNumGiorniTermineEmissione(BigDecimal aValore) {
		mNumGiorniTermineEmissione = aValore;
	}

	// Metodo toString.
	public String toString() {

		String lStr = new String();
		lStr = "" + mIdDepositoDecreto + " - " + mAnnoS72 + " - " + mNumS72 + " - " + mCodTipoDecreto + " - "
				+ mDescrTipoDecreto + " - " + mDataEmissione + " - " + mDataDeposito + " - " + mCodMagistrato
				+ " - " + mDescrMagistrato + " - " + mAltriDestinatari + " - " + mDataParerePg + " - "
				+ mCodTipoParerePg + " - " + mDescrTipoParerePg + " - " + mDataRicorsoImpugnazione + " - "
				+ mDataInvioAttiImpugnazione + " - " + mDataSentenzaImpugnazione + " - "
				+ mTenoreSentenzaImpugnazione + " - " + mNote + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mGenPridGeneraleProcedimento + " - "
				+ mSentenzeRiferimento + " - " + mIdEventoGenerato + " - " +
				// nuovi campi Luigi 17-11-2003
				mCodUfficioComp + " - " + mCodProcuraEsecuzione + " - " + mLuogoSvolgimentoProva + " - " +
				// nuovi campi Luigi 27-01-2004
				mCodTdsComp + " - " + mDescrTdsComp + " - " + mIstDetIdIstitutoDetenzione + " - "
				+ mStatusPersona + " - " + mTotOreRaggiungimento + " - " + mAnnoProcRevocato + " - "
				+ mProgrProcRevocato + " - " + mCodProcuraRevocato + " - " + mDataCompFoglioComplementare
				+ " - " +
				// Nuovi campi per Sospensione Sanzioni Sostitutive
				mDataSospensioneSS + " - " + mGiorniRecuperoSS + " - " + mFlagRecuperoSS + " - "
				+ mDataScadenzaSospensioneSS + " - " + mSospensioneGGSS + " - " + mSospensioneMMSS + " - "
				+ mSospensioneAASS + " - " + mFlagNominaComActa + " - " + mDescrCommActa + " - "
				+ mCodTipoControlloEsecuzione + " - " + mDescrTipoControlloEsecuzione + " - "
				+ mNumGiorniRevocaLA + " - " + mNumGiorniRiduzionePena + " - " + mSommaRisarcimentoDanni
				+ " - " + mFlagElaborato // 02/2015 Mis.Sic.
				// MEV_2019-09 aggiunti campi DATA_TERMINE_EMISSIONE e NUM_GIORNI_TERMINE_EMISSIONE
				+ " - " + mDataTermineEmissione + " - " + mNumGiorniTermineEmissione;

		if (this.mUfficioCompetente != null)
			lStr += " - UFFICIO COMP.: " + mUfficioCompetente;
		if (this.mProcuraEsecuzione != null)
			lStr += " - PROCURA ESEC.: " + mProcuraEsecuzione;

		return lStr;
	}

}