package siap.siep.sentenza.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: SentenzaModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il Sentenza
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
public class SentenzaModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 8552696039368102028L;

	private BigDecimal mIdSentenza;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private BigDecimal mAnnoRegistroGenerale;
	private String mNumeroRegistroGenerale;
	private BigDecimal mAnnoRegePm;
	private String mNumeroRegePm;
	// private Date mDataArrivoAtto;

	// Aggiunta DAta Iscrizione per mev a7-rr-315
	private Date mDataIscrizione;

	private Date mDataProvvedimento;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private String mNumSezioneAutoritaEmittente;
	private BigDecimal mAnnoSentenza;
	private String mNumeroSentenza;
	private BigDecimal mAnnoProvvedimento;
	private String mNumeroProvvedimento;
	// private Date mDataIrrevocabilita;
	private Date mDataSentenza;
	// private String mFlagSentenzaApplicazPena;
	private String mCodTipoProvvRif;
	private String mDescrTipoProvvRif;
	private Date mDataProvvRif;
	private String mCodTipoAutoritaProvvRif;
	private String mDescrTipoAutoritaProvvRif;
	private BigDecimal mAnnoProvvRif;
	private String mNumeroProvvRif;
	private String mCodLuogoProvvRif;
	private String mDescrLuogoProvvRif;
	private String mNumSezioneAutoritaProvvRif;
	private String mCodTipoDecisioneCassazione;
	private String mDescrTipoDecisioneCassazione;
	private String mNote1DecisioneCassazione;
	private String mNote2DecisioneCassazione;
	private BigDecimal mAnnoSentenzaCassazione;
	private String mNumeroSentenzaCassazione;
	private BigDecimal mAnnoRaccoltaGenerale;
	private String mNumeroRaccoltaGenerale;
	private String mFlagAltreSentenze;
	private String mDescrAltreSentenze;
	private BigDecimal mAnnoRegistro35;
	private String mNumRegistro35;
	private String mNote;
	// private String mDescrNumCampionePenale;
	private BigDecimal mAnnoRegeGip;
	private String mNumeroRegeGip;
	private BigDecimal mAnnoRegeDib;
	private String mNumeroRegeDib;
	private BigDecimal mAnnoRegeCas;
	private String mNumeroRegeCas;
	private BigDecimal mAnnoRegeCap;
	private String mNumeroRegeCap;
	private BigDecimal mAnnoRegeCasap;
	private String mNumeroRegeCasap;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mCodBilanciamentoCircostanze;
	private String mDescrBilanciamentoCircostanze;
	private String mFlagGiudizioAbbreviato;

	private String mCodOrdinamento;

	private Date mDataProvvedimentoIniziale;
	private Date mDataProvvedimentoFinale;
	private Date mDataIrrevocabilitaIniziale;
	private Date mDataIrrevocabilitaFinale;

	private BigDecimal mNumeroRGNRIniziale;
	private BigDecimal mAnnoRGNRIniziale;
	private BigDecimal mNumeroRGNRFinale;
	private BigDecimal mAnnoRGNRFinale;

	// private boolean mFlagDuplicatoAltroGiudizio;
	// private boolean mFlagDuplicatoCassazione;

	private String mCodTipoRito;
	private String mDescrTipoRito;

	// Attributo che serve per la cancellazione
	private boolean mEsistonoFascicoliAssociati;

	// NUOVI CAMPI PER REVISIONE SENTENZA
	private String mCodTipoProvvedimentoRif;
	private String mDescrTipoProvvedimentoRif;
	private String mCodTipoProvvedimentoAltro;
	private String mDescrTipoProvvedimentoAltro;
	private String mCodSedeNotiziaReato;
	private String mDescrSedeNotiziaReato;

	private String mFlagVisibilita;

	// Descrizione del Provvedimento di Cumulo
	// visualizzata nel dettaglio del Fascicolo Sige
	private String mDescrProvvCumulo;

	// MEV_39
	// Identifica se la Misura di Sicurezza è Applicata Provvisoriamente
	// valore "PR" oppure se è Disposta Fuori Sentenza valore "FS"
	private String mTipologiaProcMS;

	// MEV_66: aggiunte quattro nuove proprietà (quattro nuovi campi in DB) ed effettuate modifiche in tutto
	// il model
	private BigDecimal mAnnoRegeGup;
	private String mNumeroRegeGup;
	private BigDecimal mAnnoRegeCapsm;
	private String mNumeroRegeCapsm;

	// COSTRUTTORE DI DEFAULT
	public SentenzaModel() {
		mIdSentenza = null;
		mCodTipoProvvedimento = "";
		mDescrTipoProvvedimento = "";
		mAnnoRegistroGenerale = null;
		mNumeroRegistroGenerale = "";
		mAnnoRegePm = null;
		mNumeroRegePm = "";
		// mDataArrivoAtto = null;
		mDataProvvedimento = null;
		mCodTipoAutoritaEmittente = "";
		mDescrTipoAutoritaEmittente = "";
		mCodLuogoEmittente = "";
		mDescrLuogoEmittente = "";
		mNumSezioneAutoritaEmittente = "";
		mAnnoSentenza = null;
		mNumeroSentenza = "";
		mAnnoProvvedimento = null;
		mNumeroProvvedimento = "";
		// mDataIrrevocabilita = null;
		mDataSentenza = null;
		// mFlagSentenzaApplicazPena = "";
		mCodTipoProvvRif = "";
		mDescrTipoProvvRif = "";
		mDataProvvRif = null;
		mCodTipoAutoritaProvvRif = "";
		mDescrTipoAutoritaProvvRif = "";
		mAnnoProvvRif = null;
		mNumeroProvvRif = "";
		mCodLuogoProvvRif = "";
		mDescrLuogoProvvRif = "";
		mNumSezioneAutoritaProvvRif = "";
		mCodTipoDecisioneCassazione = "";
		mDescrTipoDecisioneCassazione = "";
		mNote1DecisioneCassazione = "";
		mNote2DecisioneCassazione = "";
		mAnnoSentenzaCassazione = null;
		mNumeroSentenzaCassazione = "";
		mAnnoRaccoltaGenerale = null;
		mNumeroRaccoltaGenerale = "";
		mFlagAltreSentenze = "";
		mDescrAltreSentenze = "";
		mAnnoRegistro35 = null;
		mNumRegistro35 = "";
		mNote = "";
		// mDescrNumCampionePenale = "";
		mAnnoRegeGip = null;
		mNumeroRegeGip = null;
		mAnnoRegeDib = null;
		mNumeroRegeDib = null;
		mAnnoRegeCas = null;
		mNumeroRegeCas = null;
		mAnnoRegeCap = null;
		mNumeroRegeCap = null;
		mAnnoRegeCasap = null;
		mNumeroRegeCasap = null;
		mCodOperatoreInserimento = null;
		mDataInserimento = null;
		mCodUfficioInserimento = null;
		mDescrUfficioInserimento = null;
		mCodOperatoreAggiornamento = null;
		mDataAggiornamento = null;
		mCodUfficioAggiornamento = null;
		mDescrUfficioAggiornamento = null;
		mCodBilanciamentoCircostanze = "";
		mDescrBilanciamentoCircostanze = "";
		mFlagGiudizioAbbreviato = "";
		mDataIscrizione = null;

		mDataIrrevocabilitaIniziale = null;
		mDataIrrevocabilitaFinale = null;

		mNumeroRGNRIniziale = null;
		mAnnoRGNRIniziale = null;
		mNumeroRGNRFinale = null;
		mAnnoRGNRFinale = null;

		mCodOrdinamento = "";

		mCodTipoRito = "-";
		mDescrTipoRito = "";
		mCodTipoProvvedimentoRif = "";
		mCodTipoProvvedimentoAltro = "";
		mCodSedeNotiziaReato = "";
		mDescrTipoProvvedimentoRif = "";
		mDescrTipoProvvedimentoAltro = "";
		mDescrSedeNotiziaReato = "";
		mFlagVisibilita = "";

		mAnnoRegeGup = null;
		mNumeroRegeGup = null;
		mAnnoRegeCapsm = null;
		mNumeroRegeCapsm = null;
		mDescrProvvCumulo = "";
		mTipologiaProcMS = "";
	}

	// COSTRUTTORE DI COPIA
	public SentenzaModel(SentenzaModel aModel) {
		mIdSentenza = aModel.mIdSentenza;
		mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		mAnnoRegistroGenerale = aModel.mAnnoRegistroGenerale;
		mNumeroRegistroGenerale = aModel.mNumeroRegistroGenerale;
		mAnnoRegePm = aModel.mAnnoRegePm;
		mNumeroRegePm = aModel.mNumeroRegePm;
		// mDataArrivoAtto = aModel.mDataArrivoAtto;
		mDataProvvedimento = aModel.mDataProvvedimento;
		mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		mNumSezioneAutoritaEmittente = aModel.mNumSezioneAutoritaEmittente;
		mAnnoSentenza = aModel.mAnnoSentenza;
		mNumeroSentenza = aModel.mNumeroSentenza;
		mAnnoProvvedimento = aModel.mAnnoProvvedimento;
		mNumeroProvvedimento = aModel.mNumeroProvvedimento;
		// mDataIrrevocabilita = aModel.mDataIrrevocabilita;
		mDataSentenza = aModel.mDataSentenza;
		// mFlagSentenzaApplicazPena = aModel.mFlagSentenzaApplicazPena;
		mCodTipoProvvRif = aModel.mCodTipoProvvRif;
		mDescrTipoProvvRif = aModel.mDescrTipoProvvRif;
		mDataProvvRif = aModel.mDataProvvRif;
		mCodTipoAutoritaProvvRif = aModel.mCodTipoAutoritaProvvRif;
		mDescrTipoAutoritaProvvRif = aModel.mDescrTipoAutoritaProvvRif;
		mAnnoProvvRif = aModel.mAnnoProvvRif;
		mNumeroProvvRif = aModel.mNumeroProvvRif;
		mCodLuogoProvvRif = aModel.mCodLuogoProvvRif;
		mDescrLuogoProvvRif = aModel.mDescrLuogoProvvRif;
		mNumSezioneAutoritaProvvRif = aModel.mNumSezioneAutoritaProvvRif;
		mCodTipoDecisioneCassazione = aModel.mCodTipoDecisioneCassazione;
		mDescrTipoDecisioneCassazione = aModel.mDescrTipoDecisioneCassazione;
		mNote1DecisioneCassazione = aModel.mNote1DecisioneCassazione;
		mNote2DecisioneCassazione = aModel.mNote2DecisioneCassazione;
		mAnnoSentenzaCassazione = aModel.mAnnoSentenzaCassazione;
		mNumeroSentenzaCassazione = aModel.mNumeroSentenzaCassazione;
		mAnnoRaccoltaGenerale = aModel.mAnnoRaccoltaGenerale;
		mNumeroRaccoltaGenerale = aModel.mNumeroRaccoltaGenerale;
		mFlagAltreSentenze = aModel.mFlagAltreSentenze;
		mDescrAltreSentenze = aModel.mDescrAltreSentenze;
		mAnnoRegistro35 = aModel.mAnnoRegistro35;
		mNumRegistro35 = aModel.mNumRegistro35;
		mNote = aModel.mNote;
		// mDescrNumCampionePenale = aModel.mDescrNumCampionePenale;
		mAnnoRegeGip = aModel.mAnnoRegeGip;
		mNumeroRegeGip = aModel.mNumeroRegeGip;
		mAnnoRegeDib = aModel.mAnnoRegeDib;
		mNumeroRegeDib = aModel.mNumeroRegeDib;
		mAnnoRegeCas = aModel.mAnnoRegeCas;
		mNumeroRegeCas = aModel.mNumeroRegeCas;
		mAnnoRegeCap = aModel.mAnnoRegeCap;
		mNumeroRegeCap = aModel.mNumeroRegeCap;
		mAnnoRegeCasap = aModel.mAnnoRegeCasap;
		mNumeroRegeCasap = aModel.mNumeroRegeCasap;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		mCodBilanciamentoCircostanze = aModel.mCodBilanciamentoCircostanze;
		mDescrBilanciamentoCircostanze = aModel.mDescrBilanciamentoCircostanze;
		mFlagGiudizioAbbreviato = aModel.mFlagGiudizioAbbreviato;

		mDataIrrevocabilitaIniziale = aModel.mDataIrrevocabilitaIniziale;
		mDataIrrevocabilitaFinale = aModel.mDataIrrevocabilitaFinale;

		mNumeroRGNRIniziale = aModel.mNumeroRGNRIniziale;
		mAnnoRGNRIniziale = aModel.mAnnoRGNRIniziale;
		mNumeroRGNRFinale = aModel.mNumeroRGNRFinale;
		mAnnoRGNRFinale = aModel.mAnnoRGNRFinale;

		mCodOrdinamento = aModel.mCodOrdinamento;

		mCodTipoRito = aModel.mCodTipoRito;
		mDescrTipoRito = aModel.mDescrTipoRito;

		mDataIscrizione = aModel.getDataIscrizione();
		mEsistonoFascicoliAssociati = aModel.getEsistonoFascicoliAssociati();

		mCodTipoProvvedimentoRif = aModel.getCodTipoProvvedimentoRif();
		mCodTipoProvvedimentoAltro = aModel.getCodTipoProvvedimentoAltro();
		mCodSedeNotiziaReato = aModel.getCodSedeNotiziaReato();
		mDescrTipoProvvedimentoRif = aModel.getDescrTipoProvvedimentoRif();
		mDescrTipoProvvedimentoAltro = aModel.getDescrTipoProvvedimentoAltro();
		mDescrSedeNotiziaReato = aModel.getDescrSedeNotiziaReato();

		mFlagVisibilita = aModel.mFlagVisibilita;

		mAnnoRegeGup = aModel.mAnnoRegeGup;
		mNumeroRegeGup = aModel.mNumeroRegeGup;

		mAnnoRegeCapsm = aModel.mAnnoRegeCapsm;
		mNumeroRegeCapsm = aModel.mNumeroRegeCapsm;

		mDescrProvvCumulo = aModel.mDescrProvvCumulo;

		mTipologiaProcMS = aModel.mTipologiaProcMS;
	}

	// COSTRUTTORE MODEL
	public SentenzaModel(BigDecimal aIdSentenza, String aCodTipoProvvedimento, String aDescrTipoProvvedimento,
			BigDecimal aAnnoRegistroGenerale, String aNumeroRegistroGenerale, BigDecimal aAnnoRegePm,
			String aNumeroRegePm,
			// Date aDataArrivoAtto,
			Date aDataProvvedimento, String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente,
			String aCodLuogoEmittente, String aDescrLuogoEmittente, String aNumSezioneAutoritaEmittente,
			BigDecimal aAnnoSentenza, String aNumeroSentenza, BigDecimal aAnnoProvvedimento,
			String aNumeroProvvedimento,
			// Date aDataIrrevocabilita,
			Date aDataSentenza, String aFlagSentenzaApplicazPena, String aCodTipoProvvRif,
			String aDescrTipoProvvRif, Date aDataProvvRif, String aCodTipoAutoritaProvvRif,
			String aDescrTipoAutoritaProvvRif, BigDecimal aAnnoProvvRif, String aNumeroProvvRif,
			String aCodLuogoProvvRif, String aDescrLuogoProvvRif, String aNumSezioneAutoritaProvvRif,
			String aCodTipoDecisioneCassazione, String aDescrTipoDecisioneCassazione,
			String aNote1DecisioneCassazione, String aNote2DecisioneCassazione,
			BigDecimal aAnnoSentenzaCassazione, String aNumeroSentenzaCassazione,
			BigDecimal aAnnoRaccoltaGenerale, String aNumeroRaccoltaGenerale, String aFlagAltreSentenze,
			String aDescrAltreSentenze, BigDecimal aAnnoRegistro35, String aNumRegistro35, String aNote,
			String aDescrNumCampionePenale, BigDecimal aAnnoRegeGip, String aNumeroRegeGip,
			BigDecimal aAnnoRegeDib, String aNumeroRegeDib, BigDecimal aAnnoRegeCas, String aNumeroRegeCas,
			BigDecimal aAnnoRegeCap, String aNumeroRegeCap, BigDecimal aAnnoRegeCasap,
			String aNumeroRegeCasap, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aCodBilanciamentoCircostanze,
			String aDescrBilanciamentoCircostanze,
			// String aFlagGiudizioAbbreviato,
			String aCodOrdinamento, Date aDataIscrizione, String aCodTipoProvvedimentoRif,
			String aCodTipoProvvedimentoAltro, String aCodSedeNotiziaReato, String aDescrTipoProvvedimentoRif,
			String aDescrTipoProvvedimentoAltro, String aDescrSedeNotiziaReato, String aFlagVisibilita,
			String aDescrProvvCumulo, String aTipologiaProcMS, BigDecimal aAnnoRegeGup, String aNumeroRegeGup,
			BigDecimal aAnnoRegeCapsm, String aNumeroRegeCapsm) {
		mIdSentenza = aIdSentenza;
		mCodTipoProvvedimento = aCodTipoProvvedimento;
		mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		mAnnoRegistroGenerale = aAnnoRegistroGenerale;
		mNumeroRegistroGenerale = aNumeroRegistroGenerale;
		mAnnoRegePm = aAnnoRegePm;
		mNumeroRegePm = aNumeroRegePm;
		// mDataArrivoAtto = aDataArrivoAtto;
		mDataProvvedimento = aDataProvvedimento;
		mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		mCodLuogoEmittente = aCodLuogoEmittente;
		mDescrLuogoEmittente = aDescrLuogoEmittente;
		mNumSezioneAutoritaEmittente = aNumSezioneAutoritaEmittente;
		mAnnoSentenza = aAnnoSentenza;
		mNumeroSentenza = aNumeroSentenza;
		mAnnoProvvedimento = aAnnoProvvedimento;
		mNumeroProvvedimento = aNumeroProvvedimento;
		// mDataIrrevocabilita = aDataIrrevocabilita;
		mDataSentenza = aDataSentenza;
		// mFlagSentenzaApplicazPena = aFlagSentenzaApplicazPena;
		mCodTipoProvvRif = aCodTipoProvvRif;
		mDescrTipoProvvRif = aDescrTipoProvvRif;
		mDataProvvRif = aDataProvvRif;
		mCodTipoAutoritaProvvRif = aCodTipoAutoritaProvvRif;
		mDescrTipoAutoritaProvvRif = aDescrTipoAutoritaProvvRif;
		mAnnoProvvRif = aAnnoProvvRif;
		mNumeroProvvRif = aNumeroProvvRif;
		mCodLuogoProvvRif = aCodLuogoProvvRif;
		mDescrLuogoProvvRif = aDescrLuogoProvvRif;
		mNumSezioneAutoritaProvvRif = aNumSezioneAutoritaProvvRif;
		mCodTipoDecisioneCassazione = aCodTipoDecisioneCassazione;
		mDescrTipoDecisioneCassazione = aDescrTipoDecisioneCassazione;
		mNote1DecisioneCassazione = aNote1DecisioneCassazione;
		mNote2DecisioneCassazione = aNote2DecisioneCassazione;
		mAnnoSentenzaCassazione = aAnnoSentenzaCassazione;
		mNumeroSentenzaCassazione = aNumeroSentenzaCassazione;
		mAnnoRaccoltaGenerale = aAnnoRaccoltaGenerale;
		mNumeroRaccoltaGenerale = aNumeroRaccoltaGenerale;
		mFlagAltreSentenze = aFlagAltreSentenze;
		mDescrAltreSentenze = aDescrAltreSentenze;
		mAnnoRegistro35 = aAnnoRegistro35;
		mNumRegistro35 = aNumRegistro35;
		mNote = aNote;
		// mDescrNumCampionePenale = aDescrNumCampionePenale;
		mAnnoRegeGip = aAnnoRegeGip;
		mNumeroRegeGip = aNumeroRegeGip;
		mAnnoRegeDib = aAnnoRegeDib;
		mNumeroRegeDib = aNumeroRegeDib;
		mAnnoRegeCas = aAnnoRegeCas;
		mNumeroRegeCas = aNumeroRegeCas;
		mAnnoRegeCap = aAnnoRegeCap;
		mNumeroRegeCap = aNumeroRegeCap;
		mAnnoRegeCasap = aAnnoRegeCasap;
		mNumeroRegeCasap = aNumeroRegeCasap;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		// mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mCodBilanciamentoCircostanze = aCodBilanciamentoCircostanze;
		mDescrBilanciamentoCircostanze = aDescrBilanciamentoCircostanze;
		// mFlagGiudizioAbbreviato = aFlagGiudizioAbbreviato;
		/* #### */
		mCodOrdinamento = aCodOrdinamento;
		mDataIscrizione = aDataIscrizione;

		mFlagVisibilita = aFlagVisibilita;

		mAnnoRegeGup = aAnnoRegeGup;
		mNumeroRegeGup = aNumeroRegeGup;

		mAnnoRegeCapsm = aAnnoRegeCapsm;
		mNumeroRegeCapsm = aNumeroRegeCapsm;

		mDescrProvvCumulo = aDescrProvvCumulo;

		mTipologiaProcMS = aTipologiaProcMS;
	}

	// COSTRUTTORE MODEL 2
	public SentenzaModel(BigDecimal aIdSentenza, String aCodTipoProvvedimento, String aDescrTipoProvvedimento,
			BigDecimal aAnnoRegistroGenerale, String aNumeroRegistroGenerale, BigDecimal aAnnoRegePm,
			String aNumeroRegePm, Date aDataArrivoAtto, Date aDataProvvedimento,
			String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente, String aCodLuogoEmittente,
			String aDescrLuogoEmittente, String aNumSezioneAutoritaEmittente, BigDecimal aAnnoSentenza,
			String aNumeroSentenza, Date aDataIrrevocabilita, Date aDataSentenza,
			String aFlagSentenzaApplicazPena, String aCodTipoProvvRif, String aDescrTipoProvvRif,
			Date aDataProvvRif, String aCodTipoAutoritaProvvRif, String aDescrTipoAutoritaProvvRif,
			BigDecimal aAnnoProvvRif, String aNumeroProvvRif, String aCodLuogoProvvRif,
			String aDescrLuogoProvvRif, String aNumSezioneAutoritaProvvRif,
			String aCodTipoDecisioneCassazione, String aDescrTipoDecisioneCassazione,
			String aNote1DecisioneCassazione, String aNote2DecisioneCassazione,
			BigDecimal aAnnoSentenzaCassazione, String aNumeroSentenzaCassazione,
			BigDecimal aAnnoRaccoltaGenerale, String aNumeroRaccoltaGenerale, String aFlagAltreSentenze,
			String aDescrAltreSentenze, BigDecimal aAnnoRegistro35, String aNumRegistro35, String aNote,
			String aDescrNumCampionePenale, BigDecimal aAnnoRegeGip, String aNumeroRegeGip,
			BigDecimal aAnnoRegeDib, String aNumeroRegeDib, BigDecimal aAnnoRegeCas, String aNumeroRegeCas,
			BigDecimal aAnnoRegeCap, String aNumeroRegeCap, BigDecimal aAnnoRegeCasap,
			String aNumeroRegeCasap, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aCodBilanciamentoCircostanze,
			String aDescrBilanciamentoCircostanze, String aFlagGiudizioAbbreviato, String aCodOrdinamento,
			String aCodTipoRito, String aDescrTipoRito, Date aDataIscrizione, String aCodTipoProvvedimentoRif,
			String aCodTipoProvvedimentoAltro, String aCodSedeNotiziaReato, String aDescrTipoProvvedimentoRif,
			String aDescrTipoProvvedimentoAltro, String aDescrSedeNotiziaReato, String aFlagVisibilita,
			BigDecimal aAnnoRegeGup, String aNumeroRegeGup, BigDecimal aAnnoRegeCapsm,
			String aNumeroRegeCapsm) {
		mIdSentenza = aIdSentenza;
		mCodTipoProvvedimento = aCodTipoProvvedimento;
		mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		mAnnoRegistroGenerale = aAnnoRegistroGenerale;
		mNumeroRegistroGenerale = aNumeroRegistroGenerale;
		mAnnoRegePm = aAnnoRegePm;
		mNumeroRegePm = aNumeroRegePm;
		// mDataArrivoAtto = aDataArrivoAtto;
		mDataProvvedimento = aDataProvvedimento;
		mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		mCodLuogoEmittente = aCodLuogoEmittente;
		mDescrLuogoEmittente = aDescrLuogoEmittente;
		mNumSezioneAutoritaEmittente = aNumSezioneAutoritaEmittente;
		mAnnoSentenza = aAnnoSentenza;
		mNumeroSentenza = aNumeroSentenza;
		// mDataIrrevocabilita = aDataIrrevocabilita;
		mDataSentenza = aDataSentenza;
		// mFlagSentenzaApplicazPena = aFlagSentenzaApplicazPena;
		mCodTipoProvvRif = aCodTipoProvvRif;
		mDescrTipoProvvRif = aDescrTipoProvvRif;
		mDataProvvRif = aDataProvvRif;
		mCodTipoAutoritaProvvRif = aCodTipoAutoritaProvvRif;
		mDescrTipoAutoritaProvvRif = aDescrTipoAutoritaProvvRif;
		mAnnoProvvRif = aAnnoProvvRif;
		mNumeroProvvRif = aNumeroProvvRif;
		mCodLuogoProvvRif = aCodLuogoProvvRif;
		mDescrLuogoProvvRif = aDescrLuogoProvvRif;
		mNumSezioneAutoritaProvvRif = aNumSezioneAutoritaProvvRif;
		mCodTipoDecisioneCassazione = aCodTipoDecisioneCassazione;
		mDescrTipoDecisioneCassazione = aDescrTipoDecisioneCassazione;
		mNote1DecisioneCassazione = aNote1DecisioneCassazione;
		mNote2DecisioneCassazione = aNote2DecisioneCassazione;
		mAnnoSentenzaCassazione = aAnnoSentenzaCassazione;
		mNumeroSentenzaCassazione = aNumeroSentenzaCassazione;
		mAnnoRaccoltaGenerale = aAnnoRaccoltaGenerale;
		mNumeroRaccoltaGenerale = aNumeroRaccoltaGenerale;
		mFlagAltreSentenze = aFlagAltreSentenze;
		mDescrAltreSentenze = aDescrAltreSentenze;
		mAnnoRegistro35 = aAnnoRegistro35;
		mNumRegistro35 = aNumRegistro35;
		mNote = aNote;
		// mDescrNumCampionePenale = aDescrNumCampionePenale;
		mAnnoRegeGip = aAnnoRegeGip;
		mNumeroRegeGip = aNumeroRegeGip;
		mAnnoRegeDib = aAnnoRegeDib;
		mNumeroRegeDib = aNumeroRegeDib;
		mAnnoRegeCas = aAnnoRegeCas;
		mNumeroRegeCas = aNumeroRegeCas;
		mAnnoRegeCap = aAnnoRegeCap;
		mNumeroRegeCap = aNumeroRegeCap;
		mAnnoRegeCasap = aAnnoRegeCasap;
		mNumeroRegeCasap = aNumeroRegeCasap;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		// mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mCodBilanciamentoCircostanze = aCodBilanciamentoCircostanze;
		mDescrBilanciamentoCircostanze = aDescrBilanciamentoCircostanze;
		mFlagGiudizioAbbreviato = aFlagGiudizioAbbreviato;

		mCodOrdinamento = aCodOrdinamento;

		mCodTipoRito = aCodTipoRito;
		mDescrTipoRito = aDescrTipoRito;

		mDataIscrizione = aDataIscrizione;

		mCodTipoProvvedimentoRif = aCodTipoProvvedimentoRif;
		mCodTipoProvvedimentoAltro = aCodTipoProvvedimentoAltro;
		mCodSedeNotiziaReato = aCodSedeNotiziaReato;
		mDescrTipoProvvedimentoRif = aDescrTipoProvvedimentoRif;
		mDescrTipoProvvedimentoAltro = aDescrTipoProvvedimentoAltro;
		mDescrSedeNotiziaReato = aDescrSedeNotiziaReato;

		mFlagVisibilita = aFlagVisibilita;

		mAnnoRegeGup = aAnnoRegeGup;
		mNumeroRegeGup = aNumeroRegeGup;

		mAnnoRegeCapsm = aAnnoRegeCapsm;
		mNumeroRegeCapsm = aNumeroRegeCapsm;
	}

	public String getCellSentenza() {
		StringBuffer sb = new StringBuffer();
		String emesso = "Emessa da: ";

		if (mDescrTipoProvvedimento.endsWith("o") || mDescrTipoProvvedimento.endsWith("O")
				|| mDescrTipoProvvedimento.contains("Decreto")) {
			emesso = "Emesso da: ";
		}

		sb.append("<font class=\"label\">" + mDescrTipoProvvedimento + "</font>\n");
		// Modifica del 24/11/2016
		// MEV_15_S4 // Se il provvedimento è "CUMULO", "DECRETO DI ARCHIVIAZIONE", "ORDINANZA"
		// visualizzo Anno e Numero Provvedimento
		if (getCodTipoProvvedimento() != null && (getCodTipoProvvedimento().equals("13")
				|| getCodTipoProvvedimento().equals("63") || getCodTipoProvvedimento().equals("03"))) {
			sb.append("<font class=\"campo\">" + StringUtils.toStringJSP(getAnnoProvvedimento(), "-") + " / "
					+ StringUtils.toStringJSP(getNumeroProvvedimento(), "-") + "</font>\n");
		} else {
			sb.append("<font class=\"campo\">" + StringUtils.toStringJSP(getAnnoSentenza(), "-") + " / "
					+ StringUtils.toStringJSP(getNumeroSentenza(), "-") + "</font>\n");
		}
		sb.append("<font class=\"label\">del</font>\n");
		sb.append("<font class=\"campo\">" + DateUtils.getDateToString(getDataProvvedimento(), "dd-MM-yyyy")
				+ "</font>\n");
		sb.append("<br>\n");
		sb.append("<font class=\"label\">" + emesso + "</font>\n");
		sb.append("<font class=\"campo\">" + StringUtils.toStringJSP(getDescrTipoAutoritaEmittente())
				+ "</font>\n");
		sb.append("<font class=\"label\">di</font>\n");
		sb.append("<font class=\"campo\">" + StringUtils.toStringJSP(getDescrLuogoEmittente()) + "</font>\n");
		return sb.toString();
	}

	public String getLabelSentenza() {
		StringBuffer sb = new StringBuffer();
		String emissione = " Emessa da ";
		if (getDescrTipoProvvedimento().endsWith("o") || getDescrTipoProvvedimento().endsWith("O")
				|| mDescrTipoProvvedimento.contains("Decreto")) {
			emissione = " Emesso da ";
		}

		// Modifica del 24/11/2016
		// MEV_15_S4 // Se il provvedimento è "CUMULO", "DECRETO DI ARCHIVIAZIONE", "ORDINANZA"
		// visualizzo Anno e Numero Provvedimento
		if (getCodTipoProvvedimento() != null && (getCodTipoProvvedimento().equals("13")
				|| getCodTipoProvvedimento().equals("63") || getCodTipoProvvedimento().equals("03"))) {
			sb.append(getDescrTipoProvvedimento() + " " + StringUtils.toStringJSP(getAnnoProvvedimento(), "-")
					+ " / " + StringUtils.toStringJSP(getNumeroProvvedimento(), "-"));
		} else {
			sb.append(getDescrTipoProvvedimento() + " " + StringUtils.toStringJSP(getAnnoSentenza(), "-")
					+ " / " + StringUtils.toStringJSP(getNumeroSentenza(), "-"));
		}
		sb.append(" del " + DateUtils.getDateToString(getDataProvvedimento(), "dd-MM-yyyy") + " - ");
		sb.append(emissione + StringUtils.toStringJSP(getDescrTipoAutoritaEmittente()));
		sb.append(" di " + StringUtils.toStringJSP(getDescrLuogoEmittente()));
		return sb.toString();
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdSentenza() {
		return mIdSentenza;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public BigDecimal getAnnoRegistroGenerale() {
		return mAnnoRegistroGenerale;
	}

	public String getNumeroRegistroGenerale() {
		return mNumeroRegistroGenerale;
	}

	public BigDecimal getAnnoRegePm() {
		return mAnnoRegePm;
	}

	public String getNumeroRegePm() {
		return mNumeroRegePm;
	}

	// public Date getDataArrivoAtto() { return mDataArrivoAtto; }
	public Date getDataProvvedimento() {
		return mDataProvvedimento;
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

	public String getNumSezioneAutoritaEmittente() {
		return mNumSezioneAutoritaEmittente;
	}

	public BigDecimal getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public String getNumeroSentenza() {
		return mNumeroSentenza;
	}

	public BigDecimal getAnnoProvvedimento() {
		return mAnnoProvvedimento;
	}

	public String getNumeroProvvedimento() {
		return mNumeroProvvedimento;
	}

	// public Date getDataIrrevocabilita() { return mDataIrrevocabilita; }
	public Date getDataSentenza() {
		return mDataSentenza;
	}

	// public String getFlagSentenzaApplicazPena() { return mFlagSentenzaApplicazPena; }
	public String getCodTipoProvvRif() {
		return mCodTipoProvvRif;
	}

	public String getDescrTipoProvvRif() {
		return mDescrTipoProvvRif;
	}

	public Date getDataProvvRif() {
		return mDataProvvRif;
	}

	public String getCodTipoAutoritaProvvRif() {
		return mCodTipoAutoritaProvvRif;
	}

	public String getDescrTipoAutoritaProvvRif() {
		return mDescrTipoAutoritaProvvRif;
	}

	public BigDecimal getAnnoProvvRif() {
		return mAnnoProvvRif;
	}

	public String getNumeroProvvRif() {
		return mNumeroProvvRif;
	}

	public String getCodLuogoProvvRif() {
		return mCodLuogoProvvRif;
	}

	public String getDescrLuogoProvvRif() {
		return mDescrLuogoProvvRif;
	}

	public String getNumSezioneAutoritaProvvRif() {
		return mNumSezioneAutoritaProvvRif;
	}

	public String getCodTipoDecisioneCassazione() {
		return mCodTipoDecisioneCassazione;
	}

	public String getDescrTipoDecisioneCassazione() {
		return mDescrTipoDecisioneCassazione;
	}

	public String getNote1DecisioneCassazione() {
		return mNote1DecisioneCassazione;
	}

	public String getNote2DecisioneCassazione() {
		return mNote2DecisioneCassazione;
	}

	public BigDecimal getAnnoSentenzaCassazione() {
		return mAnnoSentenzaCassazione;
	}

	public String getNumeroSentenzaCassazione() {
		return mNumeroSentenzaCassazione;
	}

	public BigDecimal getAnnoRaccoltaGenerale() {
		return mAnnoRaccoltaGenerale;
	}

	public String getNumeroRaccoltaGenerale() {
		return mNumeroRaccoltaGenerale;
	}

	public String getFlagAltreSentenze() {
		return mFlagAltreSentenze;
	}

	public String getDescrAltreSentenze() {
		return mDescrAltreSentenze;
	}

	public BigDecimal getAnnoRegistro35() {
		return mAnnoRegistro35;
	}

	public String getNumRegistro35() {
		return mNumRegistro35;
	}

	public String getNote() {
		return mNote;
	}

	// public String getDescrNumCampionePenale() { return mDescrNumCampionePenale; }
	public BigDecimal getAnnoRegeGip() {
		return mAnnoRegeGip;
	}

	public String getNumeroRegeGip() {
		return mNumeroRegeGip;
	}

	public BigDecimal getAnnoRegeDib() {
		return mAnnoRegeDib;
	}

	public String getNumeroRegeDib() {
		return mNumeroRegeDib;
	}

	public BigDecimal getAnnoRegeCas() {
		return mAnnoRegeCas;
	}

	public String getNumeroRegeCas() {
		return mNumeroRegeCas;
	}

	public BigDecimal getAnnoRegeCap() {
		return mAnnoRegeCap;
	}

	public String getNumeroRegeCap() {
		return mNumeroRegeCap;
	}

	public BigDecimal getAnnoRegeCasap() {
		return mAnnoRegeCasap;
	}

	public String getNumeroRegeCasap() {
		return mNumeroRegeCasap;
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

	public Date getDataIscrizione() {
		return mDataIscrizione;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public Date getDataProvvedimentoIniziale() {
		return mDataProvvedimentoIniziale;
	}

	public Date getDataProvvedimentoFinale() {
		return mDataProvvedimentoFinale;
	}

	public String getCodBilanciamentoCircostanze() {
		return mCodBilanciamentoCircostanze;
	}

	public String getDescrBilanciamentoCircostanze() {
		return mDescrBilanciamentoCircostanze;
	}

	public String getFlagGiudizioAbbreviato() {
		return mFlagGiudizioAbbreviato;
	}

	public Date getDataIrrevocabilitaIniziale() {
		return mDataIrrevocabilitaIniziale;
	}

	public Date getDataIrrevocabilitaFinale() {
		return mDataIrrevocabilitaFinale;
	}

	public BigDecimal getNumeroRGNRIniziale() {
		return mNumeroRGNRIniziale;
	}

	public BigDecimal getAnnoRGNRIniziale() {
		return mAnnoRGNRIniziale;
	}

	public BigDecimal getNumeroRGNRFinale() {
		return mNumeroRGNRFinale;
	}

	public BigDecimal getAnnoRGNRFinale() {
		return mAnnoRGNRFinale;
	}

	public String getCodOrdinamento() {
		return mCodOrdinamento;
	}

	public String getCodTipoRito() {
		return mCodTipoRito;
	}

	public String getDescrTipoRito() {
		return mDescrTipoRito;
	}

	public boolean getEsistonoFascicoliAssociati() {
		return mEsistonoFascicoliAssociati;
	}

	public String getCodTipoProvvedimentoRif() {
		return mCodTipoProvvedimentoRif;
	}

	public String getCodTipoProvvedimentoAltro() {
		return mCodTipoProvvedimentoAltro;
	}

	public String getCodSedeNotiziaReato() {
		return mCodSedeNotiziaReato;
	}

	public String getDescrTipoProvvedimentoRif() {
		return mDescrTipoProvvedimentoRif;
	}

	public String getDescrTipoProvvedimentoAltro() {
		return mDescrTipoProvvedimentoAltro;
	}

	public String getDescrSedeNotiziaReato() {
		return mDescrSedeNotiziaReato;
	}

	public String getFlagVisibilita() {
		return mFlagVisibilita;
	}

	public String getTipologiaProcMS() {
		return mTipologiaProcMS;
	}

	public boolean isAltroGiudizio() {
		if (mCodTipoProvvRif != null && !mCodTipoProvvRif.equals("") && !mCodTipoProvvRif.equals("-"))
			return true;
		else
			return false;
	}

	public boolean isSentenzaCassazione() {
		if (mCodTipoDecisioneCassazione != null && !mCodTipoDecisioneCassazione.equals("")
				&& !mCodTipoDecisioneCassazione.equals("-"))
			return true;
		else
			return false;
	}

	public String getDescrProvvCumulo() {
		return mDescrProvvCumulo;
	}

	public BigDecimal getAnnoRegeGup() {
		return mAnnoRegeGup;
	}

	public String getNumeroRegeGup() {
		return mNumeroRegeGup;
	}

	public BigDecimal getAnnoRegeCapsm() {
		return mAnnoRegeCapsm;
	}

	public String getNumeroRegeCapsm() {
		return mNumeroRegeCapsm;
	}

	//
	// METODI SET()
	//
	public void setIdSentenza(BigDecimal aValore) {
		mIdSentenza = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setAnnoRegistroGenerale(BigDecimal aValore) {
		mAnnoRegistroGenerale = aValore;
	}

	public void setNumeroRegistroGenerale(String aValore) {
		mNumeroRegistroGenerale = aValore;
	}

	public void setAnnoRegePm(BigDecimal aValore) {
		mAnnoRegePm = aValore;
	}

	public void setNumeroRegePm(String aValore) {
		mNumeroRegePm = aValore;
	}

	// public void setDataArrivoAtto(Date aValore ) { mDataArrivoAtto = aValore; }
	public void setDataProvvedimento(Date aValore) {
		mDataProvvedimento = aValore;
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

	public void setNumSezioneAutoritaEmittente(String aValore) {
		mNumSezioneAutoritaEmittente = aValore;
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumeroSentenza(String aValore) {
		mNumeroSentenza = aValore;
	}

	public void setAnnoProvvedimento(BigDecimal aValore) {
		mAnnoProvvedimento = aValore;
	}

	public void setNumeroProvvedimento(String aValore) {
		mNumeroProvvedimento = aValore;
	}

	// public void setDataIrrevocabilita(Date aValore ) { mDataIrrevocabilita = aValore; }
	public void setDataSentenza(Date aValore) {
		mDataSentenza = aValore;
	}

	// public void setFlagSentenzaApplicazPena(String aValore ) { mFlagSentenzaApplicazPena = aValore; }
	public void setCodTipoProvvRif(String aValore) {
		mCodTipoProvvRif = aValore;
	}

	public void setDescrTipoProvvRif(String aValore) {
		mDescrTipoProvvRif = aValore;
	}

	public void setDataProvvRif(Date aValore) {
		mDataProvvRif = aValore;
	}

	public void setCodTipoAutoritaProvvRif(String aValore) {
		mCodTipoAutoritaProvvRif = aValore;
	}

	public void setDescrTipoAutoritaProvvRif(String aValore) {
		mDescrTipoAutoritaProvvRif = aValore;
	}

	public void setAnnoProvvRif(BigDecimal aValore) {
		mAnnoProvvRif = aValore;
	}

	public void setNumeroProvvRif(String aValore) {
		mNumeroProvvRif = aValore;
	}

	public void setCodLuogoProvvRif(String aValore) {
		mCodLuogoProvvRif = aValore;
	}

	public void setDescrLuogoProvvRif(String aValore) {
		mDescrLuogoProvvRif = aValore;
	}

	public void setNumSezioneAutoritaProvvRif(String aValore) {
		mNumSezioneAutoritaProvvRif = aValore;
	}

	public void setCodTipoDecisioneCassazione(String aValore) {
		mCodTipoDecisioneCassazione = aValore;
	}

	public void setDescrTipoDecisioneCassazione(String aValore) {
		mDescrTipoDecisioneCassazione = aValore;
	}

	public void setNote1DecisioneCassazione(String aValore) {
		mNote1DecisioneCassazione = aValore;
	}

	public void setNote2DecisioneCassazione(String aValore) {
		mNote2DecisioneCassazione = aValore;
	}

	public void setAnnoSentenzaCassazione(BigDecimal aValore) {
		mAnnoSentenzaCassazione = aValore;
	}

	public void setNumeroSentenzaCassazione(String aValore) {
		mNumeroSentenzaCassazione = aValore;
	}

	public void setAnnoRaccoltaGenerale(BigDecimal aValore) {
		mAnnoRaccoltaGenerale = aValore;
	}

	public void setNumeroRaccoltaGenerale(String aValore) {
		mNumeroRaccoltaGenerale = aValore;
	}

	public void setFlagAltreSentenze(String aValore) {
		mFlagAltreSentenze = aValore;
	}

	public void setDescrAltreSentenze(String aValore) {
		mDescrAltreSentenze = aValore;
	}

	public void setAnnoRegistro35(BigDecimal aValore) {
		mAnnoRegistro35 = aValore;
	}

	public void setNumRegistro35(String aValore) {
		mNumRegistro35 = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	// public void setDescrNumCampionePenale(String aValore ) { mDescrNumCampionePenale = aValore; }
	public void setAnnoRegeGip(BigDecimal aValore) {
		mAnnoRegeGip = aValore;
	}

	public void setNumeroRegeGip(String aValore) {
		mNumeroRegeGip = aValore;
	}

	public void setAnnoRegeDib(BigDecimal aValore) {
		mAnnoRegeDib = aValore;
	}

	public void setNumeroRegeDib(String aValore) {
		mNumeroRegeDib = aValore;
	}

	public void setAnnoRegeCas(BigDecimal aValore) {
		mAnnoRegeCas = aValore;
	}

	public void setNumeroRegeCas(String aValore) {
		mNumeroRegeCas = aValore;
	}

	public void setAnnoRegeCap(BigDecimal aValore) {
		mAnnoRegeCap = aValore;
	}

	public void setNumeroRegeCap(String aValore) {
		mNumeroRegeCap = aValore;
	}

	public void setAnnoRegeCasap(BigDecimal aValore) {
		mAnnoRegeCasap = aValore;
	}

	public void setNumeroRegeCasap(String aValore) {
		mNumeroRegeCasap = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setDataIscrizione(Date aValore) {
		mDataIscrizione = aValore;
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

	public void setDataProvvedimentoIniziale(Date aValore) {
		mDataProvvedimentoIniziale = aValore;
	}

	public void setDataProvvedimentoFinale(Date aValore) {
		mDataProvvedimentoFinale = aValore;
	}

	public void setNumeroRGNRIniziale(BigDecimal aValore) {
		mNumeroRGNRIniziale = aValore;
	}

	public void setAnnoRGNRIniziale(BigDecimal aValore) {
		mAnnoRGNRIniziale = aValore;
	}

	public void setNumeroRGNRFinale(BigDecimal aValore) {
		mNumeroRGNRFinale = aValore;
	}

	public void setAnnoRGNRFinale(BigDecimal aValore) {
		mAnnoRGNRFinale = aValore;
	}

	public void setCodBilanciamentoCircostanze(String aValore) {
		mCodBilanciamentoCircostanze = aValore;
	}

	public void setDescrBilanciamentoCircostanze(String aValore) {
		mDescrBilanciamentoCircostanze = aValore;
	}

	public void setFlagGiudizioAbbreviato(String aValore) {
		mFlagGiudizioAbbreviato = aValore;
	}

	public void setDataIrrevocabilitaIniziale(Date aValore) {
		mDataIrrevocabilitaIniziale = aValore;
	}

	public void setDataIrrevocabilitaFinale(Date aValore) {
		mDataIrrevocabilitaFinale = aValore;
	}

	public void setCodTipoRito(String aValore) {
		mCodTipoRito = aValore;
	}

	public void setDescrTipoRito(String aValore) {
		mDescrTipoRito = aValore;
	}

	public void setEsistonoFascicoliAssociati(boolean aValore) {
		mEsistonoFascicoliAssociati = aValore;
	}

	public void setCodOrdinamento(String aValore) {
		mCodOrdinamento = aValore;
	}

	public void setCodTipoProvvedimentoRif(String aValore) {
		mCodTipoProvvedimentoRif = aValore;
	}

	public void setCodTipoProvvedimentoAltro(String aValore) {
		mCodTipoProvvedimentoAltro = aValore;
	}

	public void setCodSedeNotiziaReato(String aValore) {
		mCodSedeNotiziaReato = aValore;
	}

	public void setDescrTipoProvvedimentoRif(String aValore) {
		mDescrTipoProvvedimentoRif = aValore;
	}

	public void setDescrTipoProvvedimentoAltro(String aValore) {
		mDescrTipoProvvedimentoAltro = aValore;
	}

	public void setDescrSedeNotiziaReato(String aValore) {
		mDescrSedeNotiziaReato = aValore;
	}

	public void setFlagVisibilita(String aValore) {
		mFlagVisibilita = aValore;
	}

	public void setDescrProvvCumulo(String aValore) {
		mDescrProvvCumulo = aValore;
	}

	public void setTipologiaProcMS(String aValore) {
		mTipologiaProcMS = aValore;
	}

	public void setAnnoRegeGup(BigDecimal aValore) {
		mAnnoRegeGup = aValore;
	}

	public void setNumeroRegeGup(String aValore) {
		mNumeroRegeGup = aValore;
	}

	public void setAnnoRegeCapsm(BigDecimal aValore) {
		mAnnoRegeCapsm = aValore;
	}

	public void setNumeroRegeCapsm(String aValore) {
		mNumeroRegeCapsm = aValore;
	}

	public BigDecimal getAnnoRegGen() {

		BigDecimal lAnnoRegGen = null;

		if (getAnnoRegeCap() != null) {
			lAnnoRegGen = getAnnoRegeCap();
		} else if (getAnnoRegeCas() != null) {
			lAnnoRegGen = getAnnoRegeCas();
		} else if (getAnnoRegeDib() != null) {
			lAnnoRegGen = getAnnoRegeDib();
		} else if (getAnnoRegeCasap() != null) {
			lAnnoRegGen = getAnnoRegeCasap();
		} else if (getAnnoRegeGip() != null) {
			lAnnoRegGen = getAnnoRegeGip();
		} else if (getAnnoRegeGup() != null) {
			lAnnoRegGen = getAnnoRegeGup();
		} else if (getAnnoRegeCas() != null) {
			lAnnoRegGen = getAnnoRegeCas();
		} else if (getAnnoRegeDib() != null) {
			lAnnoRegGen = getAnnoRegeDib();
		} else if (getAnnoRegeCasap() != null) {
			lAnnoRegGen = getAnnoRegeCasap();
		} else if (getAnnoRegeGip() != null) {
			lAnnoRegGen = getAnnoRegeGip();
		} else if (getAnnoRegeCapsm() != null) {
			lAnnoRegGen = getAnnoRegeCapsm();
		}
		return lAnnoRegGen;
	}

	public String getNumeroRegGen() {
		String lNumRegGen = null;

		if (getNumeroRegeCap() != null) {
			lNumRegGen = getNumeroRegeCap();
		} else if (getNumeroRegeCas() != null) {
			lNumRegGen = getNumeroRegeCas();
		} else if (getNumeroRegeDib() != null) {
			lNumRegGen = getNumeroRegeDib();
		} else if (getNumeroRegeCasap() != null) {
			lNumRegGen = getNumeroRegeCasap();
		} else if (getNumeroRegeGip() != null) {
			lNumRegGen = getNumeroRegeGip();
		} else if (getNumeroRegeGup() != null) {
			lNumRegGen = getNumeroRegeGup();
		} else if (getAnnoRegeCas() != null) {
			lNumRegGen = getNumeroRegeCas();
		} else if (getAnnoRegeDib() != null) {
			lNumRegGen = getNumeroRegeDib();
		} else if (getAnnoRegeCasap() != null) {
			lNumRegGen = getNumeroRegeCasap();
		} else if (getAnnoRegeGip() != null) {
			lNumRegGen = getNumeroRegeGip();
		} else if (getNumeroRegeCapsm() != null) {
			lNumRegGen = getNumeroRegeCapsm();
		}
		return lNumRegGen;
	}

	public String getTipoRegGen() {
		String lTipoRegGen = null;

		if (getAnnoRegeCap() != null) {
			lTipoRegGen = "CAP";
		} else if (getAnnoRegeCas() != null) {
			lTipoRegGen = "CAS";
		} else if (getAnnoRegeDib() != null) {
			lTipoRegGen = "DIB";
		} else if (getAnnoRegeCasap() != null) {
			lTipoRegGen = "CASAP";
		} else if (getAnnoRegeGip() != null) {
			lTipoRegGen = "GIP";
		} else if (getAnnoRegeGup() != null) {
			lTipoRegGen = "GUP";
		} else if (getAnnoRegeCapsm() != null) {
			lTipoRegGen = "CAPSM";
		}

		return lTipoRegGen;
	}

	public String getStringRegGen() {
		String lRegGen = null;
		if (getAnnoRegGen() != null && getNumeroRegGen() != null && getTipoRegGen() != null) {
			lRegGen = getAnnoRegGen() + "/" + getNumeroRegGen() + " " + getTipoRegGen();
		}
		return lRegGen;
	}

}