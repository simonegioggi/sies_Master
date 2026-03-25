package siap.siep.modulocumulo.model;

/**
* <p>Title: ComputiCumuloModel</p>
* <p>Description: Classe Model che rappresenta il ComputiCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.StringUtils;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.modulocumulo.action.ICostantiMisuraCautelareCumulo;
import siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils;

public class ComputiCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 442894440546115460L;

	private BigDecimal mIdComputiCumulo;
	private String mCodTipoAnnotazione;
	private String mDescrTipoAnnotazione;
	private String mCodCausaleComputo;
	private String mDescrCausaleComputo;
	private String mFlagPiuMeno;

	private Date mDataReclusioneDa;
	private Date mDataReclusioneA;
	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mImportoMulta;

	private Date mDataArrestoDa;
	private Date mDataArrestoA;
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mImportoAmmenda;

	private BigDecimal mNumGiorniMap;

	private String mCodDpr;
	private String mDescDpr;
	private Date mDataRichiesta;

	private String mNote;

	private String mCodTipoMisura;
	private String mDescrTipoMisura;
	private String mIstDetIdIstitutoDetenzione;
	private String mAltroLuogoDetenzione;

	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mTitIdTitoloCumulato;
	private BigDecimal mDatIdDatiFinaliCumulo;
	private BigDecimal mIstrIdIstruttoriaCumulo;
	private BigDecimal mStatIdStatoEsecTitCum;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private String mStringaReclusione;
	private String mStringaMulta;

	private String mStringaArresto;
	private String mStringaAmmenda;

	// dati Provvedimento Altra Autorità (Per annotazioni pagamento pena pecuniaria)
	private Date mDataEmissioneProvv;
	private Date mDataRicezioneProvv;
	private BigDecimal mAnnoProvv;
	private BigDecimal mProgrProvv;
  private String      mCodTipoProvv;

	private String mCodUfficioEmittenteProvv;
	private String mDescUfficioEmittenteProvv;
	private String mCodLuogoUfficioProvv;
	private String mDescLuogoUfficioEmittenteProvv;
	private String mSezioneProvv;

	// Dati Provvedimento Amnistia / Indulto (Attività del GE)
	private BigDecimal mReaIdReatoCum;

	// Dati Provvedimento Depenalizzazione (Attività del GE)
	private String mCodFonte;
	private String mDescrFonte;
	private BigDecimal mAnnoFonte;
	private String mNumeroFonte;
	private String mCodSottonumerazione;
	private String mDescrSottonumerazione;
	private String mComma;
	private String mLettera;
	private String mNumero;
	private String mArticolo;

	// Dati Provvedimento Sospensione (Attività del GE)
	private String mCodTipoRegistroOrdinanza;
	private String mDescrTipoRegistroOrdinanza;
	private Date mDataSospensioneInterruzione;
	private String mCodOggettoDecisione;
	private String mDescrOggettoDecisione;

	// Dati Provvedimento Interruzione (Attività del GE)
	private String mProtocollo;
	private String mAltraAutorita;
	private String mAltroLuogo;

	// Misure Alternative (Attività della SORV)
	private String mLuogoEsecMisura;
	private Date mDataInizioMisura;
	private Date mDataFineMisura;
	private BigDecimal mNumAnniMisura;
	private BigDecimal mNumMesiMisura;
	private BigDecimal mNumGiorniMisura;

	private Date mDataInizioRevoca;
	private BigDecimal mNumAnniRevocaReclusione;
	private BigDecimal mNumMesiRevocaReclusione;
	private BigDecimal mNumGiorniRevocaReclusione;
	private BigDecimal mNumAnniRevocaArresto;
	private BigDecimal mNumMesiRevocaArresto;
	private BigDecimal mNumGiorniRevocaArresto;
	private Date mDataIngressoIstituto;
	private Date mDataScarcerazione;
	private String mFlagDecisioneTribunale;
	private String mCodTDSCompetente;
	private String mDescrTDSCompetente;

	// Fungibilita
	private BigDecimal mAnnoProc; // Anno fascicolo SIEP, SIUS, SIGE
	private BigDecimal mProgrProc; // Progr fascicolo SIEP, SIUS, SIGE

	private BigDecimal mAnnoBDMC;
	private String mNumeroBDMC;
	private BigDecimal mAnnoRege;
	private String mNumeroRege;
	private String mTipoRege;
	private String mCodTipoAutoritaRege;
	private String mDescrTipoAutoritaRege;
	private String mCodLuogoAutoritaRege;
	private String mDescrLuogoAutoritaRege;
	private Date mDataEmissioneOrdRege;

	private BigDecimal mAnnoRegePM;
	private String mNumeroRegePM;
	private String mCodTipoUfficioPM;
	private String mDescrTipoUfficioPM;
	private String mCodSedeUfficioPM;
	private String mDescrSedeUfficioPM;

	private BigDecimal mAnnoSentenza;
	private String mNumeroSentenza;
	private Date mDataSentenza;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;

	private BigDecimal mChiaveAnnoSIEP;
	private BigDecimal mChiaveNumeroSIEP;
	private String mChiaveUfficioSIEP;
	
	// MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
	private String mFlagAppProvvisoria;	
	
	private IstitutoDetenzioneModel mIstitutoDetenzione;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public ComputiCumuloModel() {
		this.mIdComputiCumulo = null;
		this.mCodTipoAnnotazione = "";
		this.mDescrTipoAnnotazione = "";
		this.mCodCausaleComputo = "";
		this.mDescrCausaleComputo = "";
		this.mFlagPiuMeno = "";

		this.mDataReclusioneDa = null;
		this.mDataReclusioneA = null;
		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mImportoMulta = null;

		this.mDataArrestoDa = null;
		this.mDataArrestoA = null;
		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
		this.mImportoAmmenda = null;
		this.mNumGiorniMap = null;

		this.mCodDpr = "";
		this.mDescDpr = "";
		this.mNote = "";
		this.mDataRichiesta = null;

		this.mCodTipoMisura = "";
		this.mDescrTipoMisura = "";
		this.mIstDetIdIstitutoDetenzione = "";
		this.mAltroLuogoDetenzione = "";

		this.mFlagStato = "";
		this.mMotivoModifica = "";
		this.mTitIdTitoloCumulato = null;
		this.mDatIdDatiFinaliCumulo = null;
		this.mIstrIdIstruttoriaCumulo = null;
		this.mStatIdStatoEsecTitCum = null;

		this.mDataEmissioneProvv = null;
		this.mDataRicezioneProvv = null;
		this.mAnnoProvv = null;
		this.mProgrProvv = null;
    this.mCodTipoProvv			     =  "";

		this.mCodUfficioEmittenteProvv = "";
		this.mDescUfficioEmittenteProvv = "";
		this.mCodLuogoUfficioProvv = "";
		this.mDescLuogoUfficioEmittenteProvv = "";
		this.mSezioneProvv = "";
		this.mReaIdReatoCum = null;

		this.mCodFonte = "";
		this.mDescrFonte = "";
		this.mAnnoFonte = null;
		this.mNumeroFonte = "";
		this.mCodSottonumerazione = "";
		this.mDescrSottonumerazione = "";
		this.mComma = "";
		this.mLettera = "";
		this.mNumero = "";
		this.mArticolo = "";

		this.mCodTipoRegistroOrdinanza = "";
		this.mDescrTipoRegistroOrdinanza = "";
		this.mDataSospensioneInterruzione = null;
		this.mCodOggettoDecisione = "";
		this.mDescrOggettoDecisione = "";

		this.mProtocollo = "";
		this.mAltraAutorita = "";
		this.mAltroLuogo = "";

		this.mLuogoEsecMisura = "";
		this.mDataInizioMisura = null;
		this.mDataFineMisura = null;
		this.mNumAnniMisura = null;
		this.mNumMesiMisura = null;
		this.mNumGiorniMisura = null;

		this.mDataInizioRevoca = null;
		this.mNumAnniRevocaReclusione = null;
		this.mNumMesiRevocaReclusione = null;
		this.mNumGiorniRevocaReclusione = null;
		this.mNumAnniRevocaArresto = null;
		this.mNumMesiRevocaArresto = null;
		this.mNumGiorniRevocaArresto = null;

		this.mDataIngressoIstituto = null;
		this.mDataScarcerazione = null;
		this.mFlagDecisioneTribunale = null;
		this.mCodTDSCompetente = null;
		this.mDescrTDSCompetente = "";

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		//
		this.mAnnoProc = null;
		this.mProgrProc = null;

		this.mAnnoBDMC = null;
		this.mNumeroBDMC = "";
		this.mAnnoRege = null;
		this.mNumeroRege = "";
		this.mTipoRege = "";
		this.mCodTipoAutoritaRege = "";
		this.mDescrTipoAutoritaRege = "";
		this.mCodLuogoAutoritaRege = "";
		this.mDescrLuogoAutoritaRege = "";
		this.mDataEmissioneOrdRege = null;

		this.mAnnoRegePM = null;
		this.mNumeroRegePM = "";
		this.mCodTipoUfficioPM = "";
		this.mDescrTipoUfficioPM = "";
		this.mCodSedeUfficioPM = "";
		this.mDescrSedeUfficioPM = "";

		this.mAnnoSentenza = null;
		this.mNumeroSentenza = "";
		this.mDataSentenza = null;
		this.mCodTipoAutoritaEmittente = "";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";

		this.mChiaveAnnoSIEP = null;
		this.mChiaveNumeroSIEP = null;
		this.mChiaveUfficioSIEP = "";
		
		// MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
		this.mFlagAppProvvisoria = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public ComputiCumuloModel(ComputiCumuloModel aModel) {
		this.mIdComputiCumulo = aModel.mIdComputiCumulo;

		this.mCodTipoAnnotazione = aModel.mCodTipoAnnotazione;
		this.mDescrTipoAnnotazione = aModel.mDescrTipoAnnotazione;
		this.mCodCausaleComputo = aModel.mCodCausaleComputo;
		this.mDescrCausaleComputo = aModel.mDescrCausaleComputo;

		this.mFlagPiuMeno = aModel.mFlagPiuMeno;

		this.mDataReclusioneDa = aModel.mDataReclusioneDa;
		this.mDataReclusioneA = aModel.mDataReclusioneA;
		this.mNumAnniReclusione = aModel.mNumAnniReclusione;
		this.mNumMesiReclusione = aModel.mNumMesiReclusione;
		this.mNumGiorniReclusione = aModel.mNumGiorniReclusione;
		this.mImportoMulta = aModel.mImportoMulta;

		this.mDataArrestoDa = aModel.mDataArrestoDa;
		this.mDataArrestoA = aModel.mDataArrestoA;
		this.mNumAnniArresto = aModel.mNumAnniArresto;
		this.mNumMesiArresto = aModel.mNumMesiArresto;
		this.mNumGiorniArresto = aModel.mNumGiorniArresto;
		this.mImportoAmmenda = aModel.mImportoAmmenda;

		this.mNumGiorniMap = aModel.mNumGiorniMap;

		this.mCodDpr = aModel.mCodDpr;
		this.mDescDpr = aModel.mDescDpr;
		this.mDataRichiesta = aModel.mDataRichiesta;
		this.mNote = aModel.mNote;

		this.mCodTipoMisura = aModel.mCodTipoMisura;
		this.mDescrTipoMisura = aModel.mDescrTipoMisura;
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mAltroLuogoDetenzione = aModel.mAltroLuogoDetenzione;

		this.mFlagStato = aModel.mFlagStato;

		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mDatIdDatiFinaliCumulo = aModel.mDatIdDatiFinaliCumulo;
		this.mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;
		this.mStatIdStatoEsecTitCum = aModel.mStatIdStatoEsecTitCum;

		this.mDataEmissioneProvv = aModel.mDataEmissioneProvv;
		this.mDataRicezioneProvv = aModel.mDataRicezioneProvv;
		this.mAnnoProvv = aModel.mAnnoProvv;
		this.mProgrProvv = aModel.mProgrProvv;
    this.mCodTipoProvv			     =  aModel.mCodTipoProvv;

		this.mCodUfficioEmittenteProvv = aModel.mCodUfficioEmittenteProvv;
		this.mDescUfficioEmittenteProvv = aModel.mDescUfficioEmittenteProvv;
		this.mCodLuogoUfficioProvv = aModel.mCodLuogoUfficioProvv;
		this.mDescLuogoUfficioEmittenteProvv = aModel.mDescLuogoUfficioEmittenteProvv;
		this.mSezioneProvv = aModel.mSezioneProvv;

		this.mReaIdReatoCum = aModel.mReaIdReatoCum;

		this.mCodFonte = aModel.mCodFonte;
		this.mDescrFonte = aModel.mDescrFonte;
		this.mAnnoFonte = aModel.mAnnoFonte;
		this.mNumeroFonte = aModel.mNumeroFonte;
		this.mCodSottonumerazione = aModel.mCodSottonumerazione;
		this.mDescrSottonumerazione = aModel.mDescrSottonumerazione;
		this.mComma = aModel.mComma;
		this.mLettera = aModel.mLettera;
		this.mNumero = aModel.mNumero;
		this.mArticolo = aModel.mArticolo;

		this.mCodTipoRegistroOrdinanza = aModel.mCodTipoRegistroOrdinanza;
		this.mDescrTipoRegistroOrdinanza = aModel.mDescrTipoRegistroOrdinanza;
		this.mDataSospensioneInterruzione = aModel.mDataSospensioneInterruzione;
		this.mCodOggettoDecisione = aModel.mCodOggettoDecisione;
		this.mDescrOggettoDecisione = aModel.mDescrOggettoDecisione;

		this.mProtocollo = aModel.mProtocollo;
		this.mAltraAutorita = aModel.mAltraAutorita;
		this.mAltroLuogo = aModel.mAltroLuogo;

		this.mLuogoEsecMisura = aModel.mLuogoEsecMisura;
		this.mDataInizioMisura = aModel.mDataInizioMisura;
		this.mDataFineMisura = aModel.mDataFineMisura;
		this.mNumAnniMisura = aModel.mNumAnniMisura;
		this.mNumMesiMisura = aModel.mNumMesiMisura;
		this.mNumGiorniMisura = aModel.mNumGiorniMisura;

		this.mDataInizioRevoca = aModel.mDataInizioRevoca;
		this.mNumAnniRevocaReclusione = aModel.mNumAnniRevocaReclusione;
		this.mNumMesiRevocaReclusione = aModel.mNumMesiRevocaReclusione;
		this.mNumGiorniRevocaReclusione = aModel.mNumGiorniRevocaReclusione;
		this.mNumAnniRevocaArresto = aModel.mNumAnniRevocaArresto;
		this.mNumMesiRevocaArresto = aModel.mNumMesiRevocaArresto;
		this.mNumGiorniRevocaArresto = aModel.mNumGiorniRevocaArresto;

		this.mDataIngressoIstituto = aModel.mDataIngressoIstituto;

		this.mDataScarcerazione = aModel.mDataScarcerazione;
		this.mFlagDecisioneTribunale = aModel.mFlagDecisioneTribunale;
		this.mCodTDSCompetente = aModel.mCodTDSCompetente;
		this.mDescrTDSCompetente = aModel.mDescrTDSCompetente;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;

		//
		this.mAnnoProc = aModel.mAnnoProc;
		this.mProgrProc = aModel.mProgrProc;

		this.mAnnoBDMC = aModel.mAnnoBDMC;
		this.mNumeroBDMC = aModel.mNumeroBDMC;
		this.mAnnoRege = aModel.mAnnoRege;
		this.mNumeroRege = aModel.mNumeroRege;
		this.mTipoRege = aModel.mTipoRege;
		this.mCodTipoAutoritaRege = aModel.mCodTipoAutoritaRege;
		this.mDescrTipoAutoritaRege = aModel.mDescrTipoAutoritaRege;
		this.mCodLuogoAutoritaRege = aModel.mCodLuogoAutoritaRege;
		this.mDescrLuogoAutoritaRege = aModel.mDescrLuogoAutoritaRege;
		this.mDataEmissioneOrdRege = aModel.mDataEmissioneOrdRege;

		this.mAnnoRegePM = aModel.mAnnoRegePM;
		this.mNumeroRegePM = aModel.mNumeroRegePM;
		this.mCodTipoUfficioPM = aModel.mCodTipoUfficioPM;
		this.mDescrTipoUfficioPM = aModel.mDescrTipoUfficioPM;
		this.mCodSedeUfficioPM = aModel.mCodSedeUfficioPM;
		this.mDescrSedeUfficioPM = aModel.mDescrSedeUfficioPM;

		this.mAnnoSentenza = aModel.mAnnoSentenza;
		this.mNumeroSentenza = aModel.mNumeroSentenza;
		this.mDataSentenza = aModel.mDataSentenza;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;

		this.mChiaveAnnoSIEP = aModel.mChiaveAnnoSIEP;
		this.mChiaveNumeroSIEP = aModel.mChiaveNumeroSIEP;
		this.mChiaveUfficioSIEP = aModel.mChiaveUfficioSIEP;
		
		// MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
		this.mFlagAppProvvisoria = aModel.mFlagAppProvvisoria;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public ComputiCumuloModel(BigDecimal aIdComputiCumulo, String aCodTipoAnnotazione,
			String aDescrTipoAnnotazione, String aCodCausaleComputo, String aDescrCausaleComputo,
			String aFlagPiuMeno, Date aDataReclusioneDa, Date aDataReclusioneA, BigDecimal aNumAnniReclusione,
			BigDecimal aNumMesiReclusione, BigDecimal aNumGiorniReclusione, BigDecimal aImportoMulta,
			Date aDataArrestoDa, Date aDataArrestoA, BigDecimal aNumAnniArresto, BigDecimal aNumMesiArresto,
			BigDecimal aNumGiorniArresto, BigDecimal aImportoAmmenda, BigDecimal aNumGiorniMap,
			String aCodDpr, Date aDataRichiesta, String aNote,

			String aCodTipoMisura, String aDescrTipoMisura, String aIstDetIdIstitutoDetenzione,
			String aAltroLuogoDetenzione,

			String aFlagStato, String aMotivoModifica, BigDecimal aTitIdTitoloCumulato,
			BigDecimal aDatIdDatiFinaliCumulo, BigDecimal aIstrIdIstruttoriaCumulo,
			BigDecimal aStatIdStatoEsecTitCum,

			Date aDataEmissioneProvv, Date aDataRicezioneProvv, BigDecimal aAnnoProvv, BigDecimal aProgrProvv,
    String     aCodTipoProvv,

			String aCodUfficioEmittenteProvv, String aDescUfficioEmittenteProvv, String aCodLuogoUfficioProvv,
			String aDescLuogoUfficioEmittenteProvv, String aSezioneProvv,

			BigDecimal aReaIdReatoCum,

			String aCodFonte, String aDescrFonte, BigDecimal aAnnoFonte, String aNumeroFonte,
			String aCodSottonumerazione, String aDescrSottonumerazione, String aComma, String aLettera,
			String aNumero, String aArticolo,

			String aCodTipoRegistroOrdinanza, String aDescrTipoRegistroOrdinanza,
			Date aDataSospensioneInterruzione, String aCodOggettoDecisione, String aDescrOggettoDecisione,

			String aProtocollo, String aAltraAutorita, String aAltroLuogo,

			String aLuogoEsecMisura, Date aDataInizioMisura, Date aDataFineMisura, BigDecimal aNumAnniMisura,
			BigDecimal aNumMesiMisura, BigDecimal aNumGiorniMisura,

			Date aDataInizioRevoca, BigDecimal aNumAnniRevocaReclusione, BigDecimal aNumMesiRevocaReclusione,
			BigDecimal aNumGiorniRevocaReclusione, BigDecimal aNumAnniRevocaArresto,
			BigDecimal aNumMesiRevocaArresto, BigDecimal aNumGiorniRevocaArresto, Date aDataIngressoIstituto,
			Date aDataScarcerazione, String aFlagDecisioneTribunale, String aCodTDSCompetente,
			String aDescrTDSCompetente,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,

			//
			BigDecimal aAnnoProc, BigDecimal aProgrProc,

			BigDecimal aAnnoBDMC, String aNumeroBDMC, BigDecimal aAnnoRege, String aNumeroRege,
			String aTipoRege, String aCodTipoAutoritaRege, String aDescrTipoAutoritaRege,
			String aCodLuogoAutoritaRege, String aDescrLuogoAutoritaRege, Date aDataEmissioneOrdRege,

			BigDecimal aAnnoRegePM, String aNumeroRegePM, String aCodTipoUfficioPM,
			String aDescrTipoUfficioPM, String aCodSedeUfficioPM, String aDescrSedeUfficioPM,

			BigDecimal aChiaveAnnoSIEP, BigDecimal aChiaveNumeroSIEP, String aChiaveUfficioSIEP,

			BigDecimal aAnnoSentenza, String aNumeroSentenza, Date aDataSentenza,
			String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente, String aCodLuogoEmittente,
			String aDescrLuogoEmittente
			, String aFlagAppProvvisoria // MEV_2025-48 - ALTRO – Benefici con anticipazione effetti

	) {
		this.mIdComputiCumulo = aIdComputiCumulo;
		this.mCodTipoAnnotazione = aCodTipoAnnotazione;
		this.mDescrTipoAnnotazione = aDescrTipoAnnotazione;
		this.mCodCausaleComputo = aCodCausaleComputo;
		this.mDescrCausaleComputo = aDescrCausaleComputo;
		this.mFlagPiuMeno = aFlagPiuMeno;
		this.mDataReclusioneDa = aDataReclusioneDa;
		this.mDataReclusioneA = aDataReclusioneA;
		this.mNumAnniReclusione = aNumAnniReclusione;
		this.mNumMesiReclusione = aNumMesiReclusione;
		this.mNumGiorniReclusione = aNumGiorniReclusione;
		this.mImportoMulta = aImportoMulta;
		this.mDataArrestoDa = aDataArrestoDa;
		this.mDataArrestoA = aDataArrestoA;
		this.mNumAnniArresto = aNumAnniArresto;
		this.mNumMesiArresto = aNumMesiArresto;
		this.mNumGiorniArresto = aNumGiorniArresto;
		this.mImportoAmmenda = aImportoAmmenda;
		this.mNumGiorniMap = aNumGiorniMap;

		this.mCodDpr = aCodDpr;
		this.mDataRichiesta = aDataRichiesta;
		this.mNote = aNote;

		this.mCodTipoMisura = aCodTipoMisura;
		this.mDescrTipoMisura = aDescrTipoMisura;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mAltroLuogoDetenzione = aAltroLuogoDetenzione;

		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mDatIdDatiFinaliCumulo = aDatIdDatiFinaliCumulo;
		this.mIstrIdIstruttoriaCumulo = aIstrIdIstruttoriaCumulo;
		this.mStatIdStatoEsecTitCum = aStatIdStatoEsecTitCum;

		this.mDataEmissioneProvv = aDataEmissioneProvv;
		this.mDataRicezioneProvv = aDataRicezioneProvv;
		this.mAnnoProvv = aAnnoProvv;
		this.mProgrProvv = aProgrProvv;
    this.mCodTipoProvv			     =  aCodTipoProvv;

		this.mCodUfficioEmittenteProvv = aCodUfficioEmittenteProvv;
		this.mDescUfficioEmittenteProvv = aDescUfficioEmittenteProvv;
		this.mCodLuogoUfficioProvv = aCodLuogoUfficioProvv;
		this.mDescLuogoUfficioEmittenteProvv = aDescLuogoUfficioEmittenteProvv;
		this.mSezioneProvv = aSezioneProvv;

		this.mReaIdReatoCum = aReaIdReatoCum;

		this.mCodFonte = aCodFonte;
		this.mDescrFonte = aDescrFonte;
		this.mAnnoFonte = aAnnoFonte;
		this.mNumeroFonte = aNumeroFonte;
		this.mCodSottonumerazione = aCodSottonumerazione;
		this.mDescrSottonumerazione = aDescrSottonumerazione;
		this.mComma = aComma;
		this.mLettera = aLettera;
		this.mNumero = aNumero;
		this.mArticolo = aArticolo;

		this.mCodTipoRegistroOrdinanza = aCodTipoRegistroOrdinanza;
		this.mDescrTipoRegistroOrdinanza = aDescrTipoRegistroOrdinanza;
		this.mDataSospensioneInterruzione = aDataSospensioneInterruzione;
		this.mCodOggettoDecisione = aCodOggettoDecisione;
		this.mDescrOggettoDecisione = aDescrOggettoDecisione;

		this.mProtocollo = aProtocollo;
		this.mAltraAutorita = aAltraAutorita;
		this.mAltroLuogo = aAltroLuogo;

		this.mLuogoEsecMisura = aLuogoEsecMisura;
		this.mDataInizioMisura = aDataInizioMisura;
		this.mDataFineMisura = aDataFineMisura;
		this.mNumAnniMisura = aNumAnniMisura;
		this.mNumMesiMisura = aNumMesiMisura;
		this.mNumGiorniMisura = aNumGiorniMisura;

		this.mDataInizioRevoca = aDataInizioRevoca;
		this.mNumAnniRevocaReclusione = aNumAnniRevocaReclusione;
		this.mNumMesiRevocaReclusione = aNumMesiRevocaReclusione;
		this.mNumGiorniRevocaReclusione = aNumGiorniRevocaReclusione;
		this.mNumAnniRevocaArresto = aNumAnniRevocaArresto;
		this.mNumMesiRevocaArresto = aNumMesiRevocaArresto;
		this.mNumGiorniRevocaArresto = aNumGiorniRevocaArresto;

		this.mDataIngressoIstituto = aDataIngressoIstituto;
		this.mDataScarcerazione = aDataScarcerazione;
		this.mFlagDecisioneTribunale = aFlagDecisioneTribunale;
		this.mCodTDSCompetente = aCodTDSCompetente;
		this.mDescrTDSCompetente = aDescrTDSCompetente;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;

		//
		this.mAnnoProc = aAnnoProc;
		this.mProgrProc = aProgrProc;

		this.mAnnoBDMC = aAnnoBDMC;
		this.mNumeroBDMC = aNumeroBDMC;
		this.mAnnoRege = aAnnoRege;
		this.mNumeroRege = aNumeroRege;
		this.mTipoRege = aTipoRege;
		this.mCodTipoAutoritaRege = aCodTipoAutoritaRege;
		this.mDescrTipoAutoritaRege = aDescrTipoAutoritaRege;
		this.mCodLuogoAutoritaRege = aCodLuogoAutoritaRege;
		this.mDescrLuogoAutoritaRege = aDescrLuogoAutoritaRege;
		this.mDataEmissioneOrdRege = aDataEmissioneOrdRege;

		this.mAnnoRegePM = aAnnoRegePM;
		this.mNumeroRegePM = aNumeroRegePM;
		this.mCodTipoUfficioPM = aCodTipoUfficioPM;
		this.mDescrTipoUfficioPM = aDescrTipoUfficioPM;
		this.mCodSedeUfficioPM = aCodSedeUfficioPM;
		this.mDescrSedeUfficioPM = aDescrSedeUfficioPM;

		this.mAnnoSentenza = aAnnoSentenza;
		this.mNumeroSentenza = aNumeroSentenza;
		this.mDataSentenza = aDataSentenza;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;

		this.mChiaveAnnoSIEP = aChiaveAnnoSIEP;
		this.mChiaveNumeroSIEP = aChiaveNumeroSIEP;
		this.mChiaveUfficioSIEP = aChiaveUfficioSIEP;

		// MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
		this.mFlagAppProvvisoria = aFlagAppProvvisoria;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdComputiCumulo() {
		return mIdComputiCumulo;
	}

	public String getCodTipoAnnotazione() {
		return mCodTipoAnnotazione;
	}

	public String getDescrTipoAnnotazione() {
		return mDescrTipoAnnotazione;
	}

	public String getCodCausaleComputo() {
		return mCodCausaleComputo;
	}

	public String getDescrCausaleComputo() {
		return mDescrCausaleComputo;
	}

	public String getFlagPiuMeno() {
		return mFlagPiuMeno;
	}

	public Date getDataReclusioneDa() {
		return mDataReclusioneDa;
	}

	public Date getDataReclusioneA() {
		return mDataReclusioneA;
	}

	public BigDecimal getNumAnniReclusione() {
		return mNumAnniReclusione;
	}
  public String		 getCodTipoProvv()         		 { return mCodTipoProvv; }

	public BigDecimal getNumMesiReclusione() {
		return mNumMesiReclusione;
	}

	public BigDecimal getNumGiorniReclusione() {
		return mNumGiorniReclusione;
	}

	public BigDecimal getImportoMulta() {
		return mImportoMulta;
	}

	public Date getDataArrestoDa() {
		return mDataArrestoDa;
	}

	public Date getDataArrestoA() {
		return mDataArrestoA;
	}

	public BigDecimal getNumAnniArresto() {
		return mNumAnniArresto;
	}

	public BigDecimal getNumMesiArresto() {
		return mNumMesiArresto;
	}

	public BigDecimal getNumGiorniArresto() {
		return mNumGiorniArresto;
	}

	public BigDecimal getImportoAmmenda() {
		return mImportoAmmenda;
	}

	public BigDecimal getNumGiorniMap() {
		return mNumGiorniMap;
	}

	public String getCodDpr() {
		return mCodDpr;
	}

	public String getDescDpr() {
		return mDescDpr;
	}

	public Date getDataRichiesta() {
		return mDataRichiesta;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodTipoMisura() {
		return mCodTipoMisura;
	}

	public String getDescrTipoMisura() {
		return mDescrTipoMisura;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public String getAltroLuogoDetenzione() {
		return mAltroLuogoDetenzione;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public BigDecimal getDatIdDatiFinaliCumulo() {
		return mDatIdDatiFinaliCumulo;
	}

	public BigDecimal getIstrIdIstruttoriaCumulo() {
		return mIstrIdIstruttoriaCumulo;
	}

	public BigDecimal getStatIdStatoEsecTitCum() {
		return mStatIdStatoEsecTitCum;
	}

	public Date getDataEmissioneProvv() {
		return mDataEmissioneProvv;
	}

	public Date getDataRicezioneProvv() {
		return mDataRicezioneProvv;
	}

	public BigDecimal getAnnoProvv() {
		return mAnnoProvv;
	}

	public BigDecimal getProgrProvv() {
		return mProgrProvv;
	}

	public String getCodUfficioEmittenteProvv() {
		return mCodUfficioEmittenteProvv;
	}

	public String getDescUfficioEmittenteProvv() {
		return mDescUfficioEmittenteProvv;
	}

	public String getCodLuogoUfficioProvv() {
		return mCodLuogoUfficioProvv;
	}

	public String getDescluogoUfficioEmittenteProvv() {
		return mDescLuogoUfficioEmittenteProvv;
	}

	public String getSezioneProvv() {
		return mSezioneProvv;
	}

	public BigDecimal getReaIdReatoCum() {
		return mReaIdReatoCum;
	}

	public String getCodFonte() {
		return mCodFonte;
	}

	public String getDescrFonte() {
		return mDescrFonte;
	}

	public BigDecimal getAnnoFonte() {
		return mAnnoFonte;
	}

	public String getNumeroFonte() {
		return mNumeroFonte;
	}

	public String getCodSottonumerazione() {
		return mCodSottonumerazione;
	}

	public String getDescrSottonumerazione() {
		return mDescrSottonumerazione;
	}

	public String getComma() {
		return mComma;
	}

	public String getLettera() {
		return mLettera;
	}

	public String getNumero() {
		return mNumero;
	}

	public String getArticolo() {
		return mArticolo;
	}

	public String getCodTipoRegistroOrdinanza() {
		return mCodTipoRegistroOrdinanza;
	}

	public String getDescrTipoRegistroOrdinanza() {
		return mDescrTipoRegistroOrdinanza;
	}

	public Date getDataSospensioneInterruzione() {
		return mDataSospensioneInterruzione;
	}

	public String getCodOggettoDecisione() {
		return mCodOggettoDecisione;
	}

	public String getDescrOggettoDecisione() {
		return mDescrOggettoDecisione;
	}

	public String getProtocollo() {
		return mProtocollo;
	}

	public String getAltraAutorita() {
		return mAltraAutorita;
	}

	public String getAltroLuogo() {
		return mAltroLuogo;
	}

	public String getLuogoEsecMisura() {
		return mLuogoEsecMisura;
	}

	public Date getDataInizioMisura() {
		return mDataInizioMisura;
	}

	public Date getDataFineMisura() {
		return mDataFineMisura;
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

	public Date getDataInizioRevoca() {
		return mDataInizioRevoca;
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

	public Date getDataIngressoIstituto() {
		return mDataIngressoIstituto;
	}

	public Date getDataScarcerazione() {
		return mDataScarcerazione;
	}

	public String getFlagDecisioneTribunale() {
		return mFlagDecisioneTribunale;
	}

	public String getCodTDSCompetente() {
		return mCodTDSCompetente;
	}

	public String getDescrTDSCompetente() {
		return mDescrTDSCompetente;
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

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaMulta() {
		return mStringaMulta;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	public String getStringaAmmenda() {
		return mStringaAmmenda;
	}

	//
	public BigDecimal getAnnoProc() {
		return mAnnoProc;
	}

	public BigDecimal getProgrProc() {
		return mProgrProc;
	}

	public BigDecimal getAnnoBDMC() {
		return mAnnoBDMC;
	}

	public String getNumeroBDMC() {
		return mNumeroBDMC;
	}

	public BigDecimal getAnnoRege() {
		return mAnnoRege;
	}

	public String getNumeroRege() {
		return mNumeroRege;
	}

	public String getTipoRege() {
		return mTipoRege;
	}

	public String getCodTipoAutoritaRege() {
		return mCodTipoAutoritaRege;
	}

	public String getDescrTipoAutoritaRege() {
		return mDescrTipoAutoritaRege;
	}

	public String getCodLuogoAutoritaRege() {
		return mCodLuogoAutoritaRege;
	}

	public String getDescrLuogoAutoritaRege() {
		return mDescrLuogoAutoritaRege;
	}

	public Date getDataEmissioneOrdRege() {
		return mDataEmissioneOrdRege;
	}

	public BigDecimal getAnnoRegePM() {
		return mAnnoRegePM;
	}

	public String getNumeroRegePM() {
		return mNumeroRegePM;
	}

	public String getCodTipoUfficioPM() {
		return mCodTipoUfficioPM;
	}

	public String getDescrTipoUfficioPM() {
		return mDescrTipoUfficioPM;
	}

	public String getCodSedeUfficioPM() {
		return mCodSedeUfficioPM;
	}

	public String getDescrSedeUfficioPM() {
		return mDescrSedeUfficioPM;
	}

	public BigDecimal getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public String getNumeroSentenza() {
		return mNumeroSentenza;
	}

	public Date getDataSentenza() {
		return mDataSentenza;
	}

	public String getCodTipoAutoritaEmittente() {
		return mCodTipoAutoritaEmittente;
	}

	public String getDescrTipoAutoritaEmittente() {
		return mDescrTipoAutoritaEmittente;
	}

	public String getCodLuogoEmittente() {
		return mCodLuogoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	public BigDecimal getChiaveAnnoSIEP() {
		return mChiaveAnnoSIEP;
	}

	public BigDecimal getChiaveNumeroSIEP() {
		return mChiaveNumeroSIEP;
	}

	public String getChiaveUfficioSIEP() {
		return mChiaveUfficioSIEP;
	}

	// MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
    public String getFlagAppProvvisoria() {
        return mFlagAppProvvisoria;
    }	
	
	//
	public IstitutoDetenzioneModel getIstitutoDetenzione() {
		return mIstitutoDetenzione;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdComputiCumulo(BigDecimal aValore) {
		mIdComputiCumulo = aValore;
	}

	public void setCodTipoAnnotazione(String aValore) {
		mCodTipoAnnotazione = aValore;
	}

	public void setDescrTipoAnnotazione(String aValore) {
		mDescrTipoAnnotazione = aValore;
	}

	public void setCodCausaleComputo(String aValore) {
		mCodCausaleComputo = aValore;
	}

	public void setDescrCausaleComputo(String aValore) {
		mDescrCausaleComputo = aValore;
	}

	public void setFlagPiuMeno(String aValore) {
		mFlagPiuMeno = aValore;
	}

	public void setDataReclusioneDa(Date aValore) {
		mDataReclusioneDa = aValore;
	}

	public void setDataReclusioneA(Date aValore) {
		mDataReclusioneA = aValore;
	}

	public void setNumAnniReclusione(BigDecimal aValore) {
		mNumAnniReclusione = aValore;
	}
  public void  setCodTipoProvv     			(String		aValore )    { mCodTipoProvv		      = aValore; } 

	public void setNumMesiReclusione(BigDecimal aValore) {
		mNumMesiReclusione = aValore;
	}

	public void setNumGiorniReclusione(BigDecimal aValore) {
		mNumGiorniReclusione = aValore;
	}

	public void setImportoMulta(BigDecimal aValore) {
		mImportoMulta = aValore;
	}

	public void setDataArrestoDa(Date aValore) {
		mDataArrestoDa = aValore;
	}

	public void setDataArrestoA(Date aValore) {
		mDataArrestoA = aValore;
	}

	public void setNumAnniArresto(BigDecimal aValore) {
		mNumAnniArresto = aValore;
	}

	public void setNumMesiArresto(BigDecimal aValore) {
		mNumMesiArresto = aValore;
	}

	public void setNumGiorniArresto(BigDecimal aValore) {
		mNumGiorniArresto = aValore;
	}

	public void setImportoAmmenda(BigDecimal aValore) {
		mImportoAmmenda = aValore;
	}

	public void setNumGiorniMap(BigDecimal aValore) {
		mNumGiorniMap = aValore;
	}

	public void setCodDpr(String aValore) {
		mCodDpr = aValore;
	}

	public void setDescDpr(String aValore) {
		mDescDpr = aValore;
	}

	public void setDataRichiesta(Date aValore) {
		mDataRichiesta = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodTipoMisura(String aValore) {
		mCodTipoMisura = aValore;
	}

	public void setDescrTipoMisura(String aValore) {
		mDescrTipoMisura = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setAltroLuogoDetenzione(String aValore) {
		mAltroLuogoDetenzione = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setDatIdDatiFinaliCumulo(BigDecimal aValore) {
		mDatIdDatiFinaliCumulo = aValore;
	}

	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		mIstrIdIstruttoriaCumulo = aValore;
	}

	public void setStatIdStatoEsecTitCum(BigDecimal aValore) {
		mStatIdStatoEsecTitCum = aValore;
	}

	public void setDataEmissioneProvv(Date aValore) {
		mDataEmissioneProvv = aValore;
	}

	public void setDataRicezioneProvv(Date aValore) {
		mDataRicezioneProvv = aValore;
	}

	public void setAnnoProvv(BigDecimal aValore) {
		mAnnoProvv = aValore;
	}

	public void setProgrProvv(BigDecimal aValore) {
		mProgrProvv = aValore;
	}

	public void setCodUfficioEmittenteProvv(String aValore) {
		mCodUfficioEmittenteProvv = aValore;
	}

	public void setDescUfficioEmittenteProvv(String aValore) {
		mDescUfficioEmittenteProvv = aValore;
	}

	public void setCodLuogoUfficioProvv(String aValore) {
		mCodLuogoUfficioProvv = aValore;
	}

	public void setDescLuogoUfficioEmittenteProvv(String aValore) {
		mDescLuogoUfficioEmittenteProvv = aValore;
	}

	public void setSezioneProvv(String aValore) {
		mSezioneProvv = aValore;
	}

	public void setReaIdReatoCum(BigDecimal aValore) {
		mReaIdReatoCum = aValore;
	}

	public void setCodFonte(String aValore) {
		mCodFonte = aValore;
	}

	public void setDescrFonte(String aValore) {
		mDescrFonte = aValore;
	}

	public void setAnnoFonte(BigDecimal aValore) {
		mAnnoFonte = aValore;
	}

	public void setNumeroFonte(String aValore) {
		mNumeroFonte = aValore;
	}

	public void setCodSottonumerazione(String aValore) {
		mCodSottonumerazione = aValore;
	}

	public void setDescrSottonumerazione(String aValore) {
		mDescrSottonumerazione = aValore;
	}

	public void setComma(String aValore) {
		mComma = aValore;
	}

	public void setLettera(String aValore) {
		mLettera = aValore;
	}

	public void setNumero(String aValore) {
		mNumero = aValore;
	}

	public void setArticolo(String aValore) {
		mArticolo = aValore;
	}

	public void setCodTipoRegistroOrdinanza(String aValore) {
		mCodTipoRegistroOrdinanza = aValore;
	}

	public void setDescrTipoRegistroOrdinanza(String aValore) {
		mDescrTipoRegistroOrdinanza = aValore;
	}

	public void setDataSospensioneInterruzione(Date aValore) {
		mDataSospensioneInterruzione = aValore;
	}

	public void setCodOggettoDecisione(String aValore) {
		mCodOggettoDecisione = aValore;
	}

	public void setDescrOggettoDecisione(String aValore) {
		mDescrOggettoDecisione = aValore;
	}

	public void setProtocollo(String aValore) {
		mProtocollo = aValore;
	}

	public void setAltraAutorita(String aValore) {
		mAltraAutorita = aValore;
	}

	public void setAltroLuogo(String aValore) {
		mAltroLuogo = aValore;
	}

	public void setLuogoEsecMisura(String aValore) {
		mLuogoEsecMisura = aValore;
	}

	public void setDataInizioMisura(Date aValore) {
		mDataInizioMisura = aValore;
	}

	public void setDataFineMisura(Date aValore) {
		mDataFineMisura = aValore;
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

	public void setDataInizioRevoca(Date aValore) {
		mDataInizioRevoca = aValore;
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

	public void setDataIngressoIstituto(Date aValore) {
		mDataIngressoIstituto = aValore;
	}

	public void setDataScarcerazione(Date aValore) {
		mDataScarcerazione = aValore;
	}

	public void setFlagDecisioneTribunale(String aValore) {
		mFlagDecisioneTribunale = aValore;
	}

	public void setCodTDSCompetente(String aValore) {
		mCodTDSCompetente = aValore;
	}

	public void setDescrTDSCompetente(String aValore) {
		mDescrTDSCompetente = aValore;
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

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setStringaMulta(String aValore) {
		mStringaMulta = aValore;
	}

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaAmmenda(String aValore) {
		mStringaAmmenda = aValore;
	}

	public void setIstitutoDetenzione(IstitutoDetenzioneModel aValore) {
		mIstitutoDetenzione = aValore;
	}

	//
	public void setAnnoProc(BigDecimal aValore) {
		mAnnoProc = aValore;
	}

	public void setProgrProc(BigDecimal aValore) {
		mProgrProc = aValore;
	}

	public void setAnnoBDMC(BigDecimal aValore) {
		mAnnoBDMC = aValore;
	}

	public void setNumeroBDMC(String aValore) {
		mNumeroBDMC = aValore;
	}

	public void setAnnoRege(BigDecimal aValore) {
		mAnnoRege = aValore;
	}

	public void setNumeroRege(String aValore) {
		mNumeroRege = aValore;
	}

	public void setTipoRege(String aValore) {
		mTipoRege = aValore;
	}

	public void setCodTipoAutoritaRege(String aValore) {
		mCodTipoAutoritaRege = aValore;
	}

	public void setDescrTipoAutoritaRege(String aValore) {
		mDescrTipoAutoritaRege = aValore;
	}

	public void setCodLuogoAutoritaRege(String aValore) {
		mCodLuogoAutoritaRege = aValore;
	}

	public void setDescrLuogoAutoritaRege(String aValore) {
		mDescrLuogoAutoritaRege = aValore;
	}

	public void setDataEmissioneOrdRege(Date aValore) {
		mDataEmissioneOrdRege = aValore;
	}

	public void setAnnoRegePM(BigDecimal aValore) {
		mAnnoRegePM = aValore;
	}

	public void setNumeroRegePM(String aValore) {
		mNumeroRegePM = aValore;
	}

	public void setCodTipoUfficioPM(String aValore) {
		mCodTipoUfficioPM = aValore;
	}

	public void setDescrTipoUfficioPM(String aValore) {
		mDescrTipoUfficioPM = aValore;
	}

	public void setCodSedeUfficioPM(String aValore) {
		mCodSedeUfficioPM = aValore;
	}

	public void setDescrSedeUfficioPM(String aValore) {
		mDescrSedeUfficioPM = aValore;
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumeroSentenza(String aValore) {
		mNumeroSentenza = aValore;
	}

	public void setDataSentenza(Date aValore) {
		mDataSentenza = aValore;
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		mCodTipoAutoritaEmittente = aValore;
	}

	public void setDescrTipoAutoritaEmittente(String aValore) {
		mDescrTipoAutoritaEmittente = aValore;
	}

	public void setCodLuogoEmittente(String aValore) {
		mCodLuogoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	public void setChiaveAnnoSIEP(BigDecimal aValore) {
		mChiaveAnnoSIEP = aValore;
	}

	public void setChiaveNumeroSIEP(BigDecimal aValore) {
		mChiaveNumeroSIEP = aValore;
	}

	public void setChiaveUfficioSIEP(String aValore) {
		mChiaveUfficioSIEP = aValore;
	}

	// MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
    public void setFlagAppProvvisoria(String aValore) {
        mFlagAppProvvisoria = aValore;
    }
	//

	public boolean isQuantumReclusioneZero() {
		return ((mNumAnniReclusione == null || mNumAnniReclusione.intValue() == 0)
				&& (mNumMesiReclusione == null || mNumMesiReclusione.intValue() == 0)
				&& (mNumGiorniReclusione == null || mNumGiorniReclusione.intValue() == 0));
	}

	public boolean isQuantumArrestoZero() {
		return ((mNumAnniArresto == null || mNumAnniArresto.intValue() == 0)
				&& (mNumMesiArresto == null || mNumMesiArresto.intValue() == 0)
				&& (mNumGiorniArresto == null || mNumGiorniArresto.intValue() == 0));
	}

	public boolean isMultaZero() {
		return (mImportoMulta == null || mImportoMulta.compareTo(BigDecimal.ZERO) == 0);
	}

	public boolean isAmmendaZero() {
		return (mImportoAmmenda == null || mImportoAmmenda.compareTo(BigDecimal.ZERO) == 0);
	}

	public CalendarModel getReclusioneMultaAsCalendar() {
		CalendarModel lCalendar = new CalendarModel();

		lCalendar.setNumAnni(mNumAnniReclusione);
		lCalendar.setNumMesi(mNumMesiReclusione);
		lCalendar.setNumGiorni(mNumGiorniReclusione);

		if (mImportoMulta != null)
			lCalendar.setImportoMulta(mImportoMulta.doubleValue());

		return lCalendar;
	}

	public CalendarModel getArrestoAmmendaAsCalendar() {
		CalendarModel lCalendar = new CalendarModel();

		lCalendar.setNumAnni(mNumAnniArresto);
		lCalendar.setNumMesi(mNumMesiArresto);
		lCalendar.setNumGiorni(mNumGiorniArresto);

		if (mImportoAmmenda != null)
			lCalendar.setImportoAmmenda(mImportoAmmenda.doubleValue());

		return lCalendar;
	}

	public void calcolaStringaReclusione() {
		String lStringReclusione = "";
		if (mNumAnniReclusione != null && mNumAnniReclusione.intValue() != 0)
			lStringReclusione = "Anni " + mNumAnniReclusione;

		if (mNumMesiReclusione != null && mNumMesiReclusione.intValue() != 0)
			lStringReclusione += " Mesi " + mNumMesiReclusione;

		if (mNumGiorniReclusione != null && mNumGiorniReclusione.intValue() != 0)
			lStringReclusione += " Giorni " + mNumGiorniReclusione;

		if (lStringReclusione.length() > 1) {
			this.mStringaReclusione = lStringReclusione.trim();
		} else {
			this.mStringaReclusione = null;
		}
	}

	public void calcolaStringaMulta() {
		String lStrMulta = null;
		if (mImportoMulta != null && mImportoMulta.intValue() > 0)
			lStrMulta = "Multa Euro " + StringUtils.toEuroFormat(mImportoMulta);

		mStringaMulta = lStrMulta;
	}

	public void calcolaStringaArresto() {
		String lStringArresto = "";

		if (mNumAnniArresto != null && mNumAnniArresto.intValue() != 0)
			lStringArresto = "Anni " + mNumAnniArresto;

		if (mNumMesiArresto != null && mNumMesiArresto.intValue() != 0)
			lStringArresto += " Mesi " + mNumMesiArresto;

		if (mNumGiorniArresto != null && mNumGiorniArresto.intValue() != 0)
			lStringArresto += " Giorni " + mNumGiorniArresto;

		if (lStringArresto.length() > 1) {
			this.mStringaArresto = lStringArresto.trim();
		} else {
			this.mStringaArresto = null;
		}
	}

	public void calcolaStringaAmmenda() {
		String lStrAmmenda = null;

		if (mImportoAmmenda != null && mImportoAmmenda.intValue() > 0)
			lStrAmmenda = "Ammenda Euro " + StringUtils.toEuroFormat(mImportoAmmenda);

		mStringaAmmenda = lStrAmmenda;
	}

	public void calcolaStringheXStampa() {
		calcolaStringaReclusione();
		calcolaStringaMulta();
		calcolaStringaAmmenda();
		calcolaStringaArresto();
	}

	public boolean isMisuraDetentiva() {
		boolean isDetentiva = false;

		if (mCodTipoMisura == null || "".equals(mCodTipoMisura)) {
			isDetentiva = false;
		} else if (DecodificheUtils.containsCode(StatoEsecuzioneCumuloUtils.getCodiciMisureDetentive(),
				mCodTipoMisura)) {
			isDetentiva = true;
		}

		return isDetentiva;
	}

	public boolean isMisuraNonDetentiva() {
		boolean isNonDetentiva = false;

		if (mCodTipoMisura == null || "".equals(mCodTipoMisura)) {
			isNonDetentiva = false;
		} else if (DecodificheUtils.containsCode(StatoEsecuzioneCumuloUtils.getCodiciMisureNonDetentive(),
				mCodTipoMisura)) {
			isNonDetentiva = true;
		}

		return isNonDetentiva;
	}

	public String getTipoEspiazioneMC() {
		String lTipoEspiazione = "";

		if (mCodTipoMisura == null || "".equals(mCodTipoMisura) || "-".equals(mCodTipoMisura)) {
			lTipoEspiazione = "";
			if (mIstDetIdIstitutoDetenzione != null) {
				lTipoEspiazione = ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO;
			} else if (mAltroLuogoDetenzione != null && !"".equals(mAltroLuogoDetenzione)) {
				lTipoEspiazione = ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ALTRO;
			}
		} else if (DecodificheUtils.containsCode(StatoEsecuzioneCumuloUtils.getCodiciMisureNonDetentive(),
				mCodTipoMisura)) {
			lTipoEspiazione = ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ALTRO;
		} else if (DecodificheUtils.containsCode(StatoEsecuzioneCumuloUtils.getCodiciMisureDetentive(),
				mCodTipoMisura)) {
			lTipoEspiazione = ICostantiMisuraCautelareCumulo.VAL_TIPO_ESPIAZIONE_ISTITUTO;
		} else {
			lTipoEspiazione = "";
		}
		return lTipoEspiazione;
	}

	public String toString() {
		String lStr = new String();

		lStr = "ComputiCumuloModel:\n";
		lStr += mIdComputiCumulo != null ? "[ mIdComputiCumulo           = " + mIdComputiCumulo + " ]\n" : "";
		lStr += mCodTipoAnnotazione != null ? "[ mCodTipoAnnotazione        = " + mCodTipoAnnotazione + " ]\n"
				: "";
		lStr += mCodCausaleComputo != null ? "[ mCodCausaleComputo         = " + mCodCausaleComputo + " ]\n"
				: "";
		lStr += mFlagPiuMeno != null ? "[ mFlagPiuMeno               = " + mFlagPiuMeno + " ]\n" : "";
		lStr += mDataReclusioneDa != null ? "[ mDataReclusioneDa          = " + mDataReclusioneDa + " ]\n"
				: "";
		lStr += mDataReclusioneA != null ? "[ mDataReclusioneA           = " + mDataReclusioneA + " ]\n" : "";
		lStr += mNumAnniReclusione != null ? "[ mNumAnniReclusione         = " + mNumAnniReclusione + " ]\n"
				: "";
		lStr += mNumMesiReclusione != null ? "[ mNumMesiReclusione         = " + mNumMesiReclusione + " ]\n"
				: "";
		lStr += mNumGiorniReclusione != null
				? "[ mNumGiorniReclusione       = " + mNumGiorniReclusione + " ]\n"
				: "";
		lStr += mImportoMulta != null ? "[ mImportoMulta              = " + mImportoMulta + " ]\n" : "";
		lStr += mDataArrestoDa != null ? "[ mDataArrestoDa             = " + mDataArrestoDa + " ]\n" : "";
		lStr += mDataArrestoA != null ? "[ mDataArrestoA              = " + mDataArrestoA + " ]\n" : "";
		lStr += mNumAnniArresto != null ? "[ mNumAnniArresto            = " + mNumAnniArresto + " ]\n" : "";
		lStr += mNumMesiArresto != null ? "[ mNumMesiArresto            = " + mNumMesiArresto + " ]\n" : "";
		lStr += mNumGiorniArresto != null ? "[ mNumGiorniArresto          = " + mNumGiorniArresto + " ]\n"
				: "";
		lStr += mImportoAmmenda != null ? "[ mImportoAmmenda            = " + mImportoAmmenda + " ]\n" : "";
		lStr += mNumGiorniMap != null ? "[ mNumGiorniMap              = " + mNumGiorniMap + " ]\n" : "";
		lStr += mCodDpr != null ? "[ mCodDpr                    = " + mCodDpr + " ]\n" : "";
		lStr += mDescDpr != null ? "[ mDescDpr                   = " + mDescDpr + " ]\n" : "";
		lStr += mDataRichiesta != null ? "[ mDataRichiesta             = " + mDataRichiesta + " ]\n" : "";
		lStr += mNote != null ? "[ mNote                      = " + mNote + " ]\n" : "";

		lStr += mCodTipoMisura != null ? "[ mCodTipoMisura             = " + mCodTipoMisura + " ]\n" : "";
		lStr += mIstDetIdIstitutoDetenzione != null
				? "[ mIstDetIdIstitutoDetenzione= " + mIstDetIdIstitutoDetenzione + " ]\n"
				: "";
		lStr += mAltroLuogoDetenzione != null
				? "[ mAltroLuogoDetenzione      = " + mAltroLuogoDetenzione + " ]\n"
				: "";

		lStr += mDataEmissioneProvv != null ? "[ mDataEmissioneProvv      = " + mDataEmissioneProvv + " ]\n"
				: "";
		lStr += mDataRicezioneProvv != null ? "[ mDataRicezioneProvv      = " + mDataRicezioneProvv + " ]\n"
				: "";
		lStr += mAnnoProvv != null ? "[ mAnnoProvv      		  = " + mAnnoProvv + " ]\n" : "";
		lStr += mProgrProvv != null ? "[ mProgrProvv      		  = " + mProgrProvv + " ]\n" : "";
    lStr += mCodTipoProvv      		  !=null? "[ mCodTipoProvv     		  = "+mCodTipoProvv+" ]\n":"";

		lStr += mCodUfficioEmittenteProvv != null
				? "[ mCodUfficioEmittenteProvv	= " + mCodUfficioEmittenteProvv + " ]\n"
				: "";
		lStr += mDescUfficioEmittenteProvv != null
				? "[ mDescUfficioEmittenteProvv	= " + mDescUfficioEmittenteProvv + " ]\n"
				: "";
		lStr += mCodLuogoUfficioProvv != null
				? "[ mCodLuogoUfficioProvv	  = " + mCodLuogoUfficioProvv + " ]\n"
				: "";
		lStr += mDescLuogoUfficioEmittenteProvv != null
				? "[ mDescLuogoUfficioEmittenteProvv	= " + mDescLuogoUfficioEmittenteProvv + " ]\n"
				: "";
		lStr += mSezioneProvv != null ? "[ mSezioneProvv	  		  = " + mSezioneProvv + " ]\n" : "";

		lStr += mReaIdReatoCum != null ? "[ mReaIdReatoCum    		  = " + mReaIdReatoCum + " ]\n" : "";

		lStr += mCodFonte != null ? "[ mCodFonte	    		  = " + mCodFonte + " ]\n" : "";
		lStr += mDescrFonte != null ? "[ mDescrFonte    		  = " + mDescrFonte + " ]\n" : "";
		lStr += mAnnoFonte != null ? "[ mAnnoFonte	    		  = " + mAnnoFonte + " ]\n" : "";
		lStr += mNumeroFonte != null ? "[ mNumeroFonte    		  = " + mNumeroFonte + " ]\n" : "";
		lStr += mCodSottonumerazione != null ? "[ mCodSottonumerazione	  = " + mCodSottonumerazione + " ]\n"
				: "";
		lStr += mDescrSottonumerazione != null
				? "[ mDescrSottonumerazione	  = " + mDescrSottonumerazione + " ]\n"
				: "";
		lStr += mComma != null ? "[ mComma		    		  = " + mComma + " ]\n" : "";
		lStr += mLettera != null ? "[ mLettera	    		  = " + mLettera + " ]\n" : "";
		lStr += mNumero != null ? "[ mNumero	    		  = " + mNumero + " ]\n" : "";
		lStr += mArticolo != null ? "[ mArticolo	    		  = " + mArticolo + " ]\n" : "";

		lStr += mCodTipoRegistroOrdinanza != null
				? "[ mCodTipoRegistroOrdinanza = " + mCodTipoRegistroOrdinanza + " ]\n"
				: "";
		lStr += mDescrTipoRegistroOrdinanza != null
				? "[ mDescrTipoRegistroOrdinanza = " + mDescrTipoRegistroOrdinanza + " ]\n"
				: "";
		lStr += mDataSospensioneInterruzione != null
				? "[ mDataSospensioneInterruzione = " + mDataSospensioneInterruzione + " ]\n"
				: "";
		lStr += mCodOggettoDecisione != null ? "[ mCodOggettoDecisione 	  = " + mCodOggettoDecisione + " ]\n"
				: "";
		lStr += mDescrOggettoDecisione != null
				? "[ mDescrOggettoDecisione 	  = " + mDescrOggettoDecisione + " ]\n"
				: "";

		lStr += mProtocollo != null ? "[ mProtocollo			 	= " + mProtocollo + " ]\n" : "";
		lStr += mAltraAutorita != null ? "[ mAltraAutorita			 	= " + mAltraAutorita + " ]\n" : "";
		lStr += mAltroLuogo != null ? "[ mAltroLuogo			 	= " + mAltroLuogo + " ]\n" : "";

		lStr += mLuogoEsecMisura != null ? "[ mLuogoEsecMisura		 	= " + mLuogoEsecMisura + " ]\n" : "";
		lStr += mDataInizioMisura != null ? "[ mDataInizioMisura		 	= " + mDataInizioMisura + " ]\n"
				: "";
		lStr += mDataFineMisura != null ? "[ mDataFineMisura		 	= " + mDataInizioMisura + " ]\n" : "";
		lStr += mNumAnniMisura != null ? "[ mNumAnniMisura			 	= " + mNumAnniMisura + " ]\n" : "";
		lStr += mNumMesiMisura != null ? "[ mNumMesiMisura			 	= " + mNumMesiMisura + " ]\n" : "";
		lStr += mNumGiorniMisura != null ? "[ mNumGiorniMisura		 	= " + mNumGiorniMisura + " ]\n" : "";

		lStr += mDataInizioRevoca != null ? "[ mDataInizioRevoca		 	= " + mDataInizioRevoca + " ]\n"
				: "";
		lStr += mNumAnniRevocaReclusione != null
				? "[ mNumAnniRevocaReclusione 	= " + mNumAnniRevocaReclusione + " ]\n"
				: "";
		lStr += mNumMesiRevocaReclusione != null
				? "[ mNumMesiRevocaReclusione 	= " + mNumMesiRevocaReclusione + " ]\n"
				: "";
		lStr += mNumGiorniRevocaReclusione != null
				? "[ mNumGiorniRevocaReclusione = " + mNumGiorniRevocaReclusione + " ]\n"
				: "";
		lStr += mNumAnniRevocaArresto != null
				? "[ mNumAnniRevocaArresto 		= " + mNumAnniRevocaArresto + " ]\n"
				: "";
		lStr += mNumMesiRevocaArresto != null
				? "[ mNumMesiRevocaArresto 		= " + mNumMesiRevocaArresto + " ]\n"
				: "";
		lStr += mNumGiorniRevocaArresto != null
				? "[ mNumGiorniRevocaArresto	= " + mNumGiorniRevocaArresto + " ]\n"
				: "";

		lStr += mDataIngressoIstituto != null
				? "[ mDataIngressoIstituto	 	= " + mDataIngressoIstituto + " ]\n"
				: "";
		lStr += mDataScarcerazione != null ? "[ mDataScarcerazione		 	= " + mDataScarcerazione + " ]\n"
				: "";
		lStr += mFlagDecisioneTribunale != null
				? "[ mFlagDecisioneTribunale 	= " + mFlagDecisioneTribunale + " ]\n"
				: "";
		lStr += mCodTDSCompetente != null ? "[ mCodTDSCompetente 			= " + mCodTDSCompetente + " ]\n"
				: "";

		lStr += mFlagStato != null ? "[ mFlagStato                 = " + mFlagStato + " ]\n" : "";
		lStr += mMotivoModifica != null ? "[ mMotivoModifica            = " + mMotivoModifica + " ]\n" : "";
		lStr += mTitIdTitoloCumulato != null
				? "[ mTitIdTitoloCumulato       = " + mTitIdTitoloCumulato + " ]\n"
				: "";
		lStr += mDatIdDatiFinaliCumulo != null
				? "[ mDatIdDatiFinaliCumulo     = " + mDatIdDatiFinaliCumulo + " ]\n"
				: "";
		lStr += mIstrIdIstruttoriaCumulo != null
				? "[ mIstrIdIstruttoriaCumulo   = " + mIstrIdIstruttoriaCumulo + " ]\n"
				: "";
		lStr += mCodOperatoreInserimento != null
				? "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				: "";
		lStr += mDataInserimento != null ? "[ mDataInserimento           = " + mDataInserimento + " ]\n" : "";
		lStr += mCodUfficioInserimento != null
				? "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				: "";
		lStr += mCodOperatoreAggiornamento != null
				? "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				: "";
		lStr += mDataAggiornamento != null ? "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				: "";
		lStr += mCodUfficioAggiornamento != null
				? "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]\n"
				: "";

		//
		lStr += mAnnoProc != null ? "[ mAnnoProc      		  = " + mAnnoProc + " ]\n" : "";
		lStr += mProgrProc != null ? "[ mProgrProc      		  = " + mProgrProc + " ]\n" : "";

		lStr += mAnnoBDMC != null ? "[ mAnnoBDMC      		  = " + mAnnoBDMC + " ]\n" : "";
		lStr += mNumeroBDMC != null ? "[ mNumeroBDMC      		  = " + mNumeroBDMC + " ]\n" : "";
		lStr += mAnnoRege != null ? "[ mAnnoRege      		  = " + mAnnoRege + " ]\n" : "";
		lStr += mNumeroRege != null ? "[ mNumeroRege      		  = " + mNumeroRege + " ]\n" : "";
		lStr += mTipoRege != null ? "[ mTipoRege      		  = " + mTipoRege + " ]\n" : "";
		lStr += mCodTipoAutoritaRege != null ? "[ mCodTipoAutoritaRege     = " + mCodTipoAutoritaRege + " ]\n"
				: "";
		lStr += mDescrTipoAutoritaRege != null
				? "[ mdescrTipoAutoritaRege   = " + mDescrTipoAutoritaRege + " ]\n"
				: "";
		lStr += mCodLuogoAutoritaRege != null
				? "[ mCodLuogoAutoritaRege    = " + mCodLuogoAutoritaRege + " ]\n"
				: "";
		lStr += mDescrLuogoAutoritaRege != null
				? "[ mdescrLuogoAutoritaRege  = " + mDescrLuogoAutoritaRege + " ]\n"
				: "";
		lStr += mDataEmissioneOrdRege != null
				? "[ mDataEmissioneOrdRege    = " + mDataEmissioneOrdRege + " ]\n"
				: "";

		lStr += mAnnoRegePM != null ? "[ mAnnoRegePM      		  = " + mAnnoRegePM + " ]\n" : "";
		lStr += mNumeroRegePM != null ? "[ mNumeroRegePM      	  = " + mNumeroRegePM + " ]\n" : "";
		lStr += mCodTipoUfficioPM != null ? "[ mCodTipoUfficioPM     		= " + mCodTipoUfficioPM + " ]\n"
				: "";
		lStr += mDescrTipoUfficioPM != null ? "[ mdescrTipoUfficioPM   	= " + mDescrTipoUfficioPM + " ]\n"
				: "";
		lStr += mCodSedeUfficioPM != null ? "[ mCodSedeUfficioPM     	= " + mCodSedeUfficioPM + " ]\n" : "";
		lStr += mDescrSedeUfficioPM != null ? "[ mdescrSedeUfficioPM   	= " + mDescrSedeUfficioPM + " ]\n"
				: "";

		lStr += mAnnoSentenza != null ? "[ mAnnoSentenza     		  	= " + mAnnoSentenza + " ]\n" : "";
		lStr += mNumeroSentenza != null ? "[ mNumeroSentenza      	  	= " + mNumeroSentenza + " ]\n" : "";
		lStr += mDataSentenza != null ? "[ mDataSentenza      			= " + mDataSentenza + " ]\n" : "";
		lStr += mCodTipoAutoritaEmittente != null
				? "[ mCodTipoAutoritaEmittente     = " + mCodTipoAutoritaEmittente + " ]\n"
				: "";
		lStr += mDescrTipoAutoritaEmittente != null
				? "[ mdescrTipoAutoritaEmittente   = " + mDescrTipoAutoritaEmittente + " ]\n"
				: "";
		lStr += mCodLuogoEmittente != null ? "[ mCodLuogoEmittente    		= " + mCodLuogoEmittente + " ]\n"
				: "";
		lStr += mDescrLuogoEmittente != null
				? "[ mdescrLuogoEmittente  		= " + mDescrLuogoEmittente + " ]\n"
				: "";

		lStr += mChiaveAnnoSIEP != null ? "[ mAnnoSIEP      		  = " + mChiaveAnnoSIEP + " ]\n" : "";
		lStr += mChiaveNumeroSIEP != null ? "[ mNumeroSIEP      	 	  = " + mChiaveNumeroSIEP + " ]\n"
				: "";
		lStr += mChiaveUfficioSIEP != null ? "[ mChiaveUfficioSIEP       = " + mChiaveUfficioSIEP + " ]\n"
				: "";

		return lStr;
	}

}