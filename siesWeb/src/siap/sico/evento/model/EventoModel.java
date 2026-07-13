package siap.sico.evento.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;

/**
 * <p>
 * Title: EventoModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il Evento
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
public class EventoModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 6490564408248851843L;

	private BigDecimal mIdEvento;
	private String mCodTipoEvento;
	private String mDescrTipoEvento;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private String mCodMotivo;
	private String mDescrMotivo;
	private String mCodUfficioEmittente;
	private String mDescrUfficioEmittente;
	private String mCodTipoUfficioEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private String mCognomeSoggettoPresentante;
	private String mNomeSoggettoPresentante;
	private Date mDataEmissione;
	private String mCodEsito;
	private String mDescrEsito;
	private String mFlagPiuMeno;
	private Date mDataTrasmissioneAtti;
	private Date mDataRicezioneAtti;
	private String mCodUfficioDestinatario;
	private String mDescrUfficioDestinatario;
	private BigDecimal mAnnoProtocollo;
	private BigDecimal mProgrProtocollo;
	// private Blob mDocBlob;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mFasSiuIdFascicoloSius;
	// private BigDecimal mFasSiuSogIdSoggetto;
	private String mCodLuogoDestinatario;
	private String mDescrLuogoDestinatario;
	private BigDecimal mTenIdTenore;
	private BigDecimal mEveIdEvento;
	// ////
	private byte[] mDocPerTrasferimento;
	// ////
	private ByteArrayInputStream mDocBlobIn;
	private ByteArrayOutputStream mDocBlobOut;
	private String mFlagDocumentoRegistrato;
	private String mCodMagistrato;
	private String mDescrMagistrato;
	private String mCodTipoUfficioDestinatario;
	private String mDescrTipoUfficioDestinatario;
	private boolean mExistBlob;
	private BigDecimal mFasSiuIdFascicoloSiusDest;
	// STUB:2003-07-09 PM Aggiunto pro tempore
	private String mTemIdTemplate;
	private String mFlagStampaSiep;
	private String mFlagStampaSius;
	private String mFlagVideoSiep;
	private String mFlagVideoSius;
	private BigDecimal mDecIdDecretoOrdinanzaSiep;
	private String mLegge; // GDV aggiunto per l'RV_ABBREVIATION
	// GDV per la Stampa dello stato di Esecuzione
	private String mEventoCorrente;
	// Numero di documenti allegati associati all'evento
	private int mNumAllegati;
	// Numero di documenti allegati e validati associati all'evento
	private int mNumAllValidati;
	private String mDescrProvvedimento;

	// Identificativo del documento su Mercurio (Documentale Unico del Ministero della Giustizia),
	// valorizzato dopo l'archiviazione/firma tramite siap.mercurio.client.MercurioDocumentaleClient.
	// NOTA: richiede la colonna EVENTO.ID_DOC_MERCURIO (VARCHAR2(100)) e il relativo mapping MyBatis,
	// non ancora presenti a DB (cfr. stima_integrazione_mercurio.md §2.2/§4).
	private String mIdDocMercurio;
	private String mDescrizioneData; // STUB 21/10/2005 REWORK STATO ESECUZIONE.
	private Date mData; // STUB 21/10/2005 REWORK STATO ESECUZIONE.
	private BigDecimal mPenAccIdPenaAccessoria; // STUB 08/02/2006 NUOVE PENE
												// ACCESSORIE.
	private LicenzaLibAnticipataModel mLicenzaLibAnticipata;

	// ID Evento di Revoca Luigi
	private BigDecimal mEveIdEventoRevoca = null;

	private BigDecimal mAnnIdAnnotazioneManuale = null;
	private BigDecimal mPenIdPenaResidua = null;

	private BigDecimal mChiaveAnno = null;
	private BigDecimal mChiaveProgr = null;

	private String mCodUfficio;
	private Date mDataEspulsioneSanzSost;

	private Date mDataRichiesta; // Data pervenimento richiesta variazione
									// decorrenza scadenza

	// Campi aggiunti per la stampa di rideterminazione pena altro. Contiene le
	// stringhe
	// con il totale di Reclusione e Arresto rideterminata
	private String mTotalePeriodiReclusione;
	private String mTotalePeriodiArresto;

	// Campo aggiunto per accoppiare i provvedimenti di Esecuzione con il
	// sistema NSC
	protected BigDecimal mKeyEsecNsc;

	private Date mDataInvioAtti;
	private String mCodTipologiaInvioAtti;
	private String mDescrizioneInvioAtti;
	private String mDescrizioneTipologiaInvioAtti;

	// MEV26 CUMULO
	private BigDecimal mIstruIdIstruttoriaCumulo;

	private String mEstremiSoggRichIstr;

	// mev56
	private String mDescEsitoTemplate;

	// COSTRUTTORE DI DEFAULT
	public EventoModel() {
		this.mIdEvento = null;
		this.mCodTipoEvento = null;
		this.mDescrTipoEvento = null;
		this.mCodTipoProvvedimento = null;
		this.mDescrTipoProvvedimento = null;
		this.mCodMotivo = null;
		this.mDescrMotivo = null;
		this.mCodUfficioEmittente = null;
		this.mDescrUfficioEmittente = null;
		// MEV10-s3: aggiunta variabile
		this.mCodTipoUfficioEmittente = null;
		this.mCodLuogoEmittente = null;
		this.mDescrLuogoEmittente = null;
		this.mCognomeSoggettoPresentante = null;
		this.mNomeSoggettoPresentante = null;
		this.mDataEmissione = null;
		this.mCodEsito = null;
		this.mDescrEsito = null;
		this.mFlagPiuMeno = null;
		this.mDataTrasmissioneAtti = null;
		this.mDataRicezioneAtti = null;
		this.mCodUfficioDestinatario = null;
		this.mDescrUfficioDestinatario = null;
		this.mAnnoProtocollo = null;
		this.mProgrProtocollo = null;
		// this.mDocBlob = null;
		this.mCodOperatoreInserimento = null;
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = null;
		this.mDescrUfficioInserimento = null;
		this.mCodOperatoreAggiornamento = null;
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = null;
		this.mDescrUfficioAggiornamento = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mFasSiuIdFascicoloSius = null;
		// this.mFasSiuSogIdSoggetto = null;
		this.mCodLuogoDestinatario = null;
		this.mDescrLuogoDestinatario = null;
		this.mTenIdTenore = null;
		this.mEveIdEvento = null;
		this.mFlagDocumentoRegistrato = null;
		this.mCodMagistrato = null;
		this.mDescrMagistrato = null;
		this.mCodTipoUfficioDestinatario = null;
		this.mDescrTipoUfficioDestinatario = null;
		this.mExistBlob = false;
		this.mCognomeSoggettoPresentante = "";
		this.mNomeSoggettoPresentante = "";
		this.mFasSiuIdFascicoloSiusDest = null;
		// STUB:2003-07-09 PM Aggiunto pro tempore
		this.mTemIdTemplate = null;
		this.mFlagStampaSiep = null;
		this.mFlagStampaSius = null;
		this.mFlagVideoSiep = null;
		this.mFlagVideoSius = null;
		this.mDecIdDecretoOrdinanzaSiep = null;
		this.mEventoCorrente = null;
		this.mNumAllegati = 0;
		this.mNumAllValidati = 0;
		this.mLegge = "";
		this.mCodTipoUfficioEmittente = null;
		this.mDescrProvvedimento = null;
		this.mData = null; // STUB 21/10/2005 REWORK STATO ESECUZIONE.
		this.mDescrizioneData = null; // STUB 21/10/2005 REWORK STATO
										// ESECUZIONE.
		this.mPenAccIdPenaAccessoria = null; // STUB 08/02/2006 NUOVE PENE
												// ACCESSORIE.
		this.mLicenzaLibAnticipata = null;
		this.mEveIdEventoRevoca = null;
		this.mAnnIdAnnotazioneManuale = null;
		this.mPenIdPenaResidua = null;

		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mCodUfficio = "";
		this.mDataEspulsioneSanzSost = null;
		this.mDataRichiesta = null;
		this.mKeyEsecNsc = null;
		this.mDataInvioAtti = null;
		this.mCodTipologiaInvioAtti = null;
		this.mDescrizioneInvioAtti = null;
		this.mDescrizioneTipologiaInvioAtti = null;

		this.mIstruIdIstruttoriaCumulo = null;
		this.mEstremiSoggRichIstr = null;
		this.mDescEsitoTemplate = null;
	}

	// COSTRUTTORE DI COPIA
	public EventoModel(EventoModel aModel) {
		this.mIdEvento = aModel.mIdEvento;
		this.mCodTipoEvento = aModel.mCodTipoEvento;
		this.mDescrTipoEvento = aModel.mDescrTipoEvento;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mCodMotivo = aModel.mCodMotivo;
		this.mDescrMotivo = aModel.mDescrMotivo;
		this.mCodUfficioEmittente = aModel.mCodUfficioEmittente;
		this.mDescrUfficioEmittente = aModel.mDescrUfficioEmittente;
		// MEV10-s3: aggiunta impostazione di variabile
		this.mCodTipoUfficioEmittente = aModel.mCodTipoUfficioEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mCognomeSoggettoPresentante = aModel.mCognomeSoggettoPresentante;
		this.mNomeSoggettoPresentante = aModel.mNomeSoggettoPresentante;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mFlagPiuMeno = aModel.mFlagPiuMeno;
		this.mDataTrasmissioneAtti = aModel.mDataTrasmissioneAtti;
		this.mDataRicezioneAtti = aModel.mDataRicezioneAtti;
		this.mCodUfficioDestinatario = aModel.mCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aModel.mDescrUfficioDestinatario;
		this.mAnnoProtocollo = aModel.mAnnoProtocollo;
		this.mProgrProtocollo = aModel.mProgrProtocollo;
		// this.mDocBlob = aModel.mDocBlob;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		// this.mFasSiuSogIdSoggetto = aModel.mFasSiuSogIdSoggetto;
		this.mCodLuogoDestinatario = aModel.mCodLuogoDestinatario;
		this.mDescrLuogoDestinatario = aModel.mDescrLuogoDestinatario;
		this.mTenIdTenore = aModel.mTenIdTenore;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mFlagDocumentoRegistrato = aModel.mFlagDocumentoRegistrato;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mDescrMagistrato = aModel.mDescrMagistrato;
		this.mCodTipoUfficioDestinatario = aModel.mCodTipoUfficioDestinatario;
		this.mDescrTipoUfficioDestinatario = aModel.mDescrTipoUfficioDestinatario;
		this.mExistBlob = aModel.mExistBlob;
		this.mCognomeSoggettoPresentante = aModel.mCognomeSoggettoPresentante;
		this.mNomeSoggettoPresentante = aModel.mNomeSoggettoPresentante;
		this.mFasSiuIdFascicoloSiusDest = aModel.mFasSiuIdFascicoloSiusDest;
		// STUB:2003-07-09 PM Aggiunto pro tempore
		this.mTemIdTemplate = aModel.mTemIdTemplate;
		this.mFlagStampaSiep = aModel.mFlagStampaSiep;
		this.mFlagStampaSius = aModel.mFlagStampaSius;
		this.mFlagVideoSiep = aModel.mFlagVideoSiep;
		this.mFlagVideoSius = aModel.mFlagVideoSius;
		this.mDecIdDecretoOrdinanzaSiep = aModel.mDecIdDecretoOrdinanzaSiep;
		this.mEventoCorrente = aModel.mEventoCorrente;
		this.mNumAllegati = aModel.mNumAllegati;
		this.mNumAllValidati = aModel.mNumAllValidati;
		this.mDocPerTrasferimento = aModel.mDocPerTrasferimento;
		this.mDescrProvvedimento = aModel.mDescrProvvedimento;
		// STUB 21/10/2005 REWORK STATO ESECUZIONE.
		this.mData = aModel.mData;
		this.mDescrizioneData = aModel.mDescrizioneData;
		this.mPenAccIdPenaAccessoria = aModel.mPenAccIdPenaAccessoria; // STUB
																		// 08/02/2006
																		// NUOVE
																		// PENE
																		// ACCESSORIE.
		this.mLicenzaLibAnticipata = aModel.mLicenzaLibAnticipata;
		this.mEveIdEventoRevoca = aModel.mEveIdEventoRevoca;
		this.mAnnIdAnnotazioneManuale = aModel.mAnnIdAnnotazioneManuale;
		this.mPenIdPenaResidua = aModel.mPenIdPenaResidua;

		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mCodUfficio = aModel.mCodUfficio;
		this.mDataEspulsioneSanzSost = aModel.mDataEspulsioneSanzSost;
		this.mDataRichiesta = aModel.mDataRichiesta;
		this.mKeyEsecNsc = aModel.mKeyEsecNsc;
		this.mDataInvioAtti = aModel.mDataInvioAtti;
		this.mCodTipologiaInvioAtti = aModel.mCodTipologiaInvioAtti;
		this.mDescrizioneInvioAtti = aModel.mDescrizioneInvioAtti;
		this.mDescrizioneTipologiaInvioAtti = aModel.mDescrizioneTipologiaInvioAtti;

		this.mIstruIdIstruttoriaCumulo = aModel.mIstruIdIstruttoriaCumulo;
		this.mEstremiSoggRichIstr = aModel.mEstremiSoggRichIstr;
		this.mDescEsitoTemplate = aModel.mDescEsitoTemplate;
	}

	// COSTRUTTORE MODEL con parametri
	public EventoModel(BigDecimal aIdEvento, String aCodTipoEvento, String aDescrTipoEvento,
			String aCodTipoProvvedimento, String aDescrTipoProvvedimento, String aCodMotivo,
			String aDescrMotivo, String aCodUfficioEmittente, String aDescrUfficioEmittente,
			// MEV10-s3: aggiunta variabile
			String aCodTipoUfficioEmittente, String aCodLuogoEmittente, String aDescrLuogoEmittente,
			Date aDataEmissione, String aCodEsito, String aDescrEsito, String aFlagPiuMeno,
			Date aDataTrasmissioneAtti, Date aDataRicezioneAtti, String aCodUfficioDestinatario,
			String aDescrUfficioDestinatario, BigDecimal aAnnoProtocollo, BigDecimal aProgrProtocollo,
			// Blob aDocBlob,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aFasSiuIdFascicoloSius,
			// BigDecimal aFasSiuSogIdSoggetto,
			String aCodLuogoDestinatario, String aDescrLuogoDestinatario, BigDecimal aTenIdTenore,
			BigDecimal aEveIdEvento, String aFlagDocumentoRegistrato, String aCodMagistrato,
			String aDescrMagistrato, String aCodTipoUfficioDestinatario, String aCognomeSoggettoPresentante,
			String aNomeSoggettoPresentante, String aFlagStampaSiep, String aFlagStampaSius,
			String aFlagVideoSiep, String aFlagVideoSius, BigDecimal aDecIdDecretoOrdinanzaSiep,
			String aDescrProvvedimento, Date aData, String aDescrizioneData,
			BigDecimal aPenAccIdPenaAccessoria, // STUB 08/02/2006 NUOVE PENE
												// ACCESSORIE.
			LicenzaLibAnticipataModel aLicenzaLibAnticipata, BigDecimal aEveIdEventoRevoca,
			BigDecimal aAnnIdAnnotazioneManuale, BigDecimal aPenIdPenaResidua, String aCodUfficio,
			Date aDataEspulsioneSanzSost, Date aDataRichiesta, BigDecimal aKeyEsecNsc, Date aDateInvioAtti,
			String aCodTipologiaInvioAtti, String aDescrizioneInvioAtti,
			String aDescrizioneTipologiaInvioAtti, BigDecimal aIstruIdIstruttoriaCumulo,
			String aEstremiSoggRichIstr, String aDescEsitoTemplate) {
		this.mIdEvento = aIdEvento;
		this.mCodTipoEvento = aCodTipoEvento;
		this.mDescrTipoEvento = aDescrTipoEvento;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		// MEV10-s3: aggiunta impostazione di variabile
		this.mCodTipoUfficioEmittente = aCodTipoUfficioEmittente;
		this.mCodMotivo = aCodMotivo;
		this.mDescrMotivo = aDescrMotivo;
		this.mCodUfficioEmittente = aCodUfficioEmittente;
		this.mDescrUfficioEmittente = aDescrUfficioEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mCognomeSoggettoPresentante = aCognomeSoggettoPresentante;
		this.mNomeSoggettoPresentante = aNomeSoggettoPresentante;
		this.mDataEmissione = aDataEmissione;
		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mFlagPiuMeno = aFlagPiuMeno;
		this.mDataTrasmissioneAtti = aDataTrasmissioneAtti;
		this.mDataRicezioneAtti = aDataRicezioneAtti;
		this.mCodUfficioDestinatario = aCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aDescrUfficioDestinatario;
		this.mAnnoProtocollo = aAnnoProtocollo;
		this.mProgrProtocollo = aProgrProtocollo;
		// this.mDocBlob = aDocBlob;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		// this.mFasSiuSogIdSoggetto = aFasSiuSogIdSoggetto;
		this.mCodLuogoDestinatario = aCodLuogoDestinatario;
		this.mDescrLuogoDestinatario = aDescrLuogoDestinatario;
		this.mTenIdTenore = aTenIdTenore;
		this.mEveIdEvento = aEveIdEvento;
		this.mFlagDocumentoRegistrato = aFlagDocumentoRegistrato;
		this.mCodMagistrato = aCodMagistrato;
		this.mDescrMagistrato = aDescrMagistrato;
		// this.mDescrTipoUfficioDestinatario = aDescrTipoUfficioDestinatario;
		this.mFlagStampaSiep = aFlagStampaSiep;
		this.mFlagStampaSius = aFlagStampaSius;
		this.mFlagVideoSiep = aFlagVideoSiep;
		this.mFlagVideoSius = aFlagVideoSius;
		this.mDecIdDecretoOrdinanzaSiep = aDecIdDecretoOrdinanzaSiep;
		this.mNumAllegati = 0;
		this.mNumAllValidati = 0;
		this.mLegge = "";
		this.mDescrProvvedimento = aDescrProvvedimento;
		this.mPenAccIdPenaAccessoria = aPenAccIdPenaAccessoria;
		this.mData = aData;
		this.mDescrizioneData = aDescrizioneData;
		this.mLicenzaLibAnticipata = aLicenzaLibAnticipata;
		this.mEveIdEventoRevoca = aEveIdEventoRevoca;
		this.mAnnIdAnnotazioneManuale = aAnnIdAnnotazioneManuale;
		this.mPenIdPenaResidua = aPenIdPenaResidua;
		this.mCodUfficio = aCodUfficio;
		this.mDataEspulsioneSanzSost = aDataEspulsioneSanzSost;
		this.mDataRichiesta = aDataRichiesta;
		this.mKeyEsecNsc = aKeyEsecNsc;
		this.mDataInvioAtti = aDateInvioAtti;
		this.mCodTipologiaInvioAtti = aCodTipologiaInvioAtti;
		this.mDescrizioneInvioAtti = aDescrizioneInvioAtti;
		this.mDescrizioneTipologiaInvioAtti = aDescrizioneTipologiaInvioAtti;

		this.mIstruIdIstruttoriaCumulo = aIstruIdIstruttoriaCumulo;
		this.mEstremiSoggRichIstr = aEstremiSoggRichIstr;
		this.mDescEsitoTemplate = aDescEsitoTemplate;
	}

	public BigDecimal getIstruIdIstruttoriaCumulo() {
		return mIstruIdIstruttoriaCumulo;
	}

	public void setIstruidIstruttoriaCumulo(BigDecimal aValore) {
		mIstruIdIstruttoriaCumulo = aValore;
	}

	public Date getDataInvioAtti() {
		return this.mDataInvioAtti;
	}

	public String getCodTipologiaInvioAtti() {
		return this.mCodTipologiaInvioAtti;
	};

	public String getDescrizioneInvioAtti() {
		return this.mDescrizioneInvioAtti;
	};

	public String getDescrizioneTipologiaInvioAtti() {
		return this.mDescrizioneTipologiaInvioAtti;
	};

	// Costruttore attraverso EventoNotificaModel
	public EventoModel(EventoNotificaModel aModel) {
		this(aModel.getEvento());
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdEvento() {
		return mIdEvento;
	}

	public String getCodTipoEvento() {
		return mCodTipoEvento;
	}

	public String getDescrTipoEvento() {
		return mDescrTipoEvento;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

	public String getDescrMotivo() {
		return mDescrMotivo;
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

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public String getFlagPiuMeno() {
		return mFlagPiuMeno;
	}

	public Date getDataTrasmissioneAtti() {
		return mDataTrasmissioneAtti;
	}

	public Date getDataRicezioneAtti() {
		return mDataRicezioneAtti;
	}

	public String getCodUfficioDestinatario() {
		return mCodUfficioDestinatario;
	}

	public String getDescrUfficioDestinatario() {
		return mDescrUfficioDestinatario;
	}

	public BigDecimal getAnnoProtocollo() {
		return mAnnoProtocollo;
	}

	public BigDecimal getProgrProtocollo() {
		return mProgrProtocollo;
	}

	// public Blob getDocBlob() { return mDocBlob; }
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

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	// public BigDecimal getFasSiuSogIdSoggetto() { return mFasSiuSogIdSoggetto;
	// }
	public String getCodLuogoDestinatario() {
		return mCodLuogoDestinatario;
	}

	public String getDescrLuogoDestinatario() {
		return mDescrLuogoDestinatario;
	}

	public BigDecimal getTenIdTenore() {
		return mTenIdTenore;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public ByteArrayInputStream getDocBlobIn() {
		return mDocBlobIn;
	}

	public ByteArrayOutputStream getDocBlobOut() {
		return mDocBlobOut;
	}

	public String getFlagDocumentoRegistrato() {
		return mFlagDocumentoRegistrato;
	}

	public String getIdDocMercurio() {
		return mIdDocMercurio;
	}

	public void setIdDocMercurio(String aIdDocMercurio) {
		mIdDocMercurio = aIdDocMercurio;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getDescrMagistrato() {
		return mDescrMagistrato;
	}

	public String getCodTipoUfficioDestinatario() {
		return mCodTipoUfficioDestinatario;
	}

	public String getDescrTipoUfficioDestinatario() {
		return mDescrTipoUfficioDestinatario;
	}

	public boolean getExistBlob() {
		return mExistBlob;
	}

	public String getCognomeSoggettoPresentante() {
		return mCognomeSoggettoPresentante;
	}

	public String getNomeSoggettoPresentante() {
		return mNomeSoggettoPresentante;
	}

	public BigDecimal getFasSiuIdFascicoloSiusDest() {
		return mFasSiuIdFascicoloSiusDest;
	}

	// STUB:2003-07-09 PM Aggiunto pro tempore
	public String getTemIdTemplate() {
		return mTemIdTemplate;
	}

	public String getFlagStampaSiep() {
		return mFlagStampaSiep;
	}

	public String getFlagStampaSius() {
		return mFlagStampaSius;
	}

	public String getFlagVideoSiep() {
		return mFlagVideoSiep;
	}

	public String getFlagVideoSius() {
		return mFlagVideoSius;
	}

	public BigDecimal getDecIdDecretoOrdinanzaSiep() {
		return mDecIdDecretoOrdinanzaSiep;
	}

	public String getEventoCorrente() {
		return mEventoCorrente;
	}

	public int getNumAllegati() {
		return mNumAllegati;
	}

	public int getNumAllValidati() {
		return mNumAllValidati;
	}

	public String getLegge() {
		return mLegge;
	}

	public String getCodTipoUfficioEmittente() {
		return mCodTipoUfficioEmittente;
	}

	public byte[] getDocPerTrasferimento() {
		return mDocPerTrasferimento;
	}

	public String getDescrProvvedimento() {
		return mDescrProvvedimento;
	}

	public Date getData() {
		return mData;
	}

	public String getDescrizioneData() {
		return mDescrizioneData;
	}

	public BigDecimal getPenAccIdPenaAccessoria() {
		return mPenAccIdPenaAccessoria;
	}

	public LicenzaLibAnticipataModel getLicenzaLibAnticipata() {
		return mLicenzaLibAnticipata;
	}

	public BigDecimal getEveIdEventoRevoca() {
		return mEveIdEventoRevoca;
	}

	public BigDecimal getAnnIdAnnotazioneManuale() {
		return mAnnIdAnnotazioneManuale;
	}

	public BigDecimal getPenIdPenaResidua() {
		return mPenIdPenaResidua;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getCodUfficio() {
		return mCodUfficio;
	}

	public Date getDataEspulsioneSanzSost() {
		return mDataEspulsioneSanzSost;
	}

	public Date getDataRichiesta() {
		return mDataRichiesta;
	}

	public String getTotalePeriodiReclusione() {
		return mTotalePeriodiReclusione;
	}

	public String getTotalePeriodiArresto() {
		return mTotalePeriodiArresto;
	}

	public BigDecimal getKeyEsecNsc() {
		return mKeyEsecNsc;
	}

	public String getEstremiSoggRichIstr() {
		return mEstremiSoggRichIstr;
	}

	public String getDescEsitoTemplate() {
		return mDescEsitoTemplate;
	}

	//
	// METODI SET()
	//
	public void setIdEvento(BigDecimal aValore) {
		mIdEvento = aValore;
	}

	public void setCodTipoEvento(String aValore) {
		mCodTipoEvento = aValore;
	}

	public void setDescrTipoEvento(String aValore) {
		mDescrTipoEvento = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

	public void setDescrMotivo(String aValore) {
		mDescrMotivo = aValore;
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

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setFlagPiuMeno(String aValore) {
		mFlagPiuMeno = aValore;
	}

	public void setDataTrasmissioneAtti(Date aValore) {
		mDataTrasmissioneAtti = aValore;
	}

	public void setDataRicezioneAtti(Date aValore) {
		mDataRicezioneAtti = aValore;
	}

	public void setCodUfficioDestinatario(String aValore) {
		mCodUfficioDestinatario = aValore;
	}

	public void setDescrUfficioDestinatario(String aValore) {
		mDescrUfficioDestinatario = aValore;
	}

	public void setAnnoProtocollo(BigDecimal aValore) {
		mAnnoProtocollo = aValore;
	}

	public void setProgrProtocollo(BigDecimal aValore) {
		mProgrProtocollo = aValore;
	}

	// public void setDocBlob(Blob aValore ) { mDocBlob = aValore; }
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	// public void setFasSiuSogIdSoggetto(BigDecimal aValore ) {
	// mFasSiuSogIdSoggetto = aValore; }
	public void setCodLuogoDestinatario(String aValore) {
		mCodLuogoDestinatario = aValore;
	}

	public void setDescrLuogoDestinatario(String aValore) {
		mDescrLuogoDestinatario = aValore;
	}

	public void setTenIdTenore(BigDecimal aValore) {
		mTenIdTenore = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setDocBlobIn(ByteArrayInputStream aValore) {
		mDocBlobIn = aValore;
	}

	public void setDocBlobOut(ByteArrayOutputStream aValore) {
		mDocBlobOut = aValore;
	}

	public void setFlagDocumentoRegistrato(String aValore) {
		mFlagDocumentoRegistrato = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setDescrMagistrato(String aValore) {
		mDescrMagistrato = aValore;
	}

	public void setCodTipoUfficioDestinatario(String aValore) {
		mCodTipoUfficioDestinatario = aValore;
	}

	public void setDescrTipoUfficioDestinatario(String aValore) {
		mDescrTipoUfficioDestinatario = aValore;
	}

	public void setExistBlob(boolean aValore) {
		mExistBlob = aValore;
	}

	public void setCognomeSoggettoPresentante(String aValore) {
		mCognomeSoggettoPresentante = aValore;
	}

	public void setNomeSoggettoPresentante(String aValore) {
		mNomeSoggettoPresentante = aValore;
	}

	public void setFasSiuIdFascicoloSiusDest(BigDecimal aValore) {
		mFasSiuIdFascicoloSiusDest = aValore;
	}

	// STUB:2003-07-09 PM Aggiunto pro tempore
	public void setTemIdTemplate(String aValore) {
		mTemIdTemplate = aValore;
	}

	public void setFlagStampaSiep(String aValore) {
		mFlagStampaSiep = aValore;
	}

	public void setFlagStampaSius(String aValore) {
		mFlagStampaSius = aValore;
	}

	public void setFlagVideoSiep(String aValore) {
		mFlagVideoSiep = aValore;
	}

	public void setFlagVideoSius(String aValore) {
		mFlagVideoSius = aValore;
	}

	public void setDecIdDecretoOrdinanzaSiep(BigDecimal aValore) {
		mDecIdDecretoOrdinanzaSiep = aValore;
	}

	public void setEventoCorrente(String aValore) {
		mEventoCorrente = aValore;
	}

	public void setNumAllegati(int aValore) {
		mNumAllegati = aValore;
	}

	public void setNumAllValidati(int aValore) {
		mNumAllValidati = aValore;
	}

	public void setLegge(String aValore) {
		mLegge = aValore;
	}

	public void setCodTipoUfficioEmittente(String aValore) {
		mCodTipoUfficioEmittente = aValore;
	}

	public void setDocPerTrasferimento(byte[] aValore) {
		mDocPerTrasferimento = aValore;
	}

	public void setDescrProvvedimento(String aValore) {
		mDescrProvvedimento = aValore;
	}

	public void setDescrizioneData(String aValore) {
		mDescrizioneData = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setPenAccIdPenaAccessoria(BigDecimal aValore) {
		mPenAccIdPenaAccessoria = aValore;
	}

	public void setLicenzaLibAnticipata(LicenzaLibAnticipataModel aValore) {
		mLicenzaLibAnticipata = aValore;
	}

	public void setEveIdEventoRevoca(BigDecimal aValore) {
		mEveIdEventoRevoca = aValore;
	}

	public void setAnnIdAnnotazioneManuale(BigDecimal aValore) {
		mAnnIdAnnotazioneManuale = aValore;
	}

	public void setPenIdPenaResidua(BigDecimal aValore) {
		mPenIdPenaResidua = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setDataEspulsioneSanzSost(Date aValore) {
		mDataEspulsioneSanzSost = aValore;
	}

	public void setDataRichiesta(Date aValore) {
		mDataRichiesta = aValore;
	}

	public void setTotalePeriodiReclusione(String aValore) {
		mTotalePeriodiReclusione = aValore;
	}

	public void setTotalePeriodiArresto(String aValore) {
		mTotalePeriodiArresto = aValore;
	}

	public void setKeyEsecNsc(BigDecimal aValore) {
		mKeyEsecNsc = aValore;
	}

	public void setDataInvioAtti(Date aDataInvioAtti) {
		mDataInvioAtti = aDataInvioAtti;
	}

	public void setCodTipologiaInvioAtti(String aTipologiaInvioAtti) {
		this.mCodTipologiaInvioAtti = aTipologiaInvioAtti;
	};

	public void setDescrizioneInvioAtti(String aDescrizioneInvioAtti) {
		this.mDescrizioneInvioAtti = aDescrizioneInvioAtti;
	};

	public void setDescrizioneTipologiaInvioAtti(String aDescrizioneTipologiaInvioAtti) {
		this.mDescrizioneTipologiaInvioAtti = aDescrizioneTipologiaInvioAtti;
	};

	public void setEstremiSoggRichIstr(String aValore) {
		mEstremiSoggRichIstr = aValore;
	}

	public void setDescEsitoTemplate(String mDescEsitoTemplate) {
		this.mDescEsitoTemplate = mDescEsitoTemplate;
	}

	public String toString() {
		String lStr = new String();

		lStr = "EventoModel:\n" + "[ mIdEvento                   = " + mIdEvento + " ]\n"
				+ "[ mCodTipoEvento              = " + mCodTipoEvento + " ]\n"
				+ "[ mDescrTipoEvento            = " + mDescrTipoEvento + " ]\n"
				+ "[ mCodTipoProvvedimento       = " + mCodTipoProvvedimento + " ]\n"
				+ "[ mDescrTipoProvvedimento     = " + mDescrTipoProvvedimento + " ]\n"
				+ "[ mCodMotivo                  = " + mCodMotivo + " ]\n"
				+ "[ mDescrMotivo                = " + mDescrMotivo + " ]\n"
				+ "[ mCodUfficioEmittente        = " + mCodUfficioEmittente + " ]\n"
				+ "[ mDescrUfficioEmittente      = " + mDescrUfficioEmittente + " ]\n"
				+ "[ mCodTipoUfficioEmittente    = " + mCodTipoUfficioEmittente + " ]\n"
				+ "[ mCodLuogoEmittente          = " + mCodLuogoEmittente + " ]\n"
				+ "[ mDescrLuogoEmittente        = " + mDescrLuogoEmittente + " ]\n"
				+ "[ mDataEmissione              = " + mDataEmissione + " ]\n"
				+ "[ mCodEsito                   = " + mCodEsito + " ]\n" + "[ mDescrEsito                 = "
				+ mDescrEsito + " ]\n" + "[ mFlagPiuMeno                = " + mFlagPiuMeno + " ]\n"
				+ "[ mDataTrasmissioneAtti       = " + mDataTrasmissioneAtti + " ]\n"
				+ "[ mDataRicezioneAtti          = " + mDataRicezioneAtti + " ]\n"
				+ "[ mCodUfficioDestinatario     = " + mCodUfficioDestinatario + " ]\n"
				+ "[ mDescrUfficioDestinatario   = " + mDescrUfficioDestinatario + " ]\n"
				+ "[ mAnnoProtocollo             = " + mAnnoProtocollo + " ]\n"
				+ "[ mProgrProtocollo            = " + mProgrProtocollo + " ]\n"
				+ "[ mCodOperatoreInserimento    = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento            = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]\n"
				+ "[ mDescrUfficioInserimento    = " + mDescrUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento  = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento          = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mDescrUfficioAggiornamento  = " + mDescrUfficioAggiornamento + " ]\n"
				+ "[ mFasSieIdFascicoloSiep      = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mFasSiuIdFascicoloSius      = " + mFasSiuIdFascicoloSius + " ]\n"
				+ "[ mCodLuogoDestinatario       = " + mCodLuogoDestinatario + " ]\n"
				+ "[ mDescrLuogoDestinatario     = " + mDescrLuogoDestinatario + " ]\n"
				+ "[ mTenIdTenore                = " + mTenIdTenore + " ]\n"
				+ "[ mEveIdEvento                = " + mEveIdEvento + " ]\n"
				+ "[ mFlagDocumentoRegistrato    = " + mFlagDocumentoRegistrato + " ]\n"
				+ "[ mCodMagistrato              = " + mCodMagistrato + " ]\n"
				+ "[ mCodTipoUfficioDestinatario = " + mCodTipoUfficioDestinatario + " ]\n"
				+ "[ mCognomeSoggettoPresentante = " + mCognomeSoggettoPresentante + " ]\n"
				+ "[ mNomeSoggettoPresentante    = " + mNomeSoggettoPresentante + " ]\n"
				+ "[ mFasSiuIdFascicoloSiusDest  = " + mFasSiuIdFascicoloSiusDest + " ]\n"
				+ "[ mTemIdTemplate              = " + mTemIdTemplate + " ]\n"
				+ "[ mFlagStampaSiep             = " + mFlagStampaSiep + " ]\n"
				+ "[ mFlagStampaSius             = " + mFlagStampaSius + " ]\n"
				+ "[ mFlagVideoSiep              = " + mFlagVideoSiep + " ]\n"
				+ "[ mFlagVideoSius              = " + mFlagVideoSius + " ]\n"
				+ "[ mEventoCorrente             = " + mEventoCorrente + " ]\n"
				+ "[ mDecIdDecretoOrdinanzaSiep  = " + mDecIdDecretoOrdinanzaSiep + " ]\n"
				+ "[ mLegge                      = " + mLegge + " ]\n" + "[ mNumAllegati                = "
				+ mNumAllegati + " ]\n" + "[ mNumAllValidati             = " + mNumAllValidati + " ]\n"
				+ "[ mDescrProvvedimento         = " + mDescrProvvedimento + " ]\n"
				+ "[ mData                       = " + mData + " ]\n" + "[ mDescrizioneData            = "
				+ mDescrizioneData + " ]\n" + "[ mDecIdDecretoOrdinanzaSiep  = " + mDecIdDecretoOrdinanzaSiep
				+ " ]\n" + "[ mPenAccIdPenaAccessoria     = " + mPenAccIdPenaAccessoria + " ]\n"
				+ "[ mEveIdEventoRevoca          = " + mEveIdEventoRevoca + " ]\n"
				+ "[ mAnnIdAnnotazioneManuale    = " + mAnnIdAnnotazioneManuale + " ]\n"
				+ "[ mPenIdPenaResidua           = " + mPenIdPenaResidua + " ]\n"
				+ "[ mDataEspulsioneSanzSost     = " + mDataEspulsioneSanzSost + "]\n"
				+ "[ mDataRichiesta              = " + mDataRichiesta + " ]\n"
				+ "[ mIstrIdIstruttoriaCumulo    = " + mIstruIdIstruttoriaCumulo + " ]\n"
				+ "[ mEstremiSoggRichIstr        = " + mEstremiSoggRichIstr + " ]\n"
				+ "[ mDescEsitoTemplate        = " + mDescEsitoTemplate + " ]\n"
				+ "[ mKeyEsecNsc                  = " + mKeyEsecNsc + " ]";

		return lStr;
	}

	public String toString2() {
		String lStr = new String();

		lStr = "" + mIdEvento + " - " + mCodTipoEvento + " - " + mDescrTipoEvento + " - "
				+ mCodTipoProvvedimento + " - " + mDescrTipoProvvedimento + " - " + mCodMotivo + " - "
				+ mDescrMotivo + " - " + mCodUfficioEmittente + " - " + mDescrUfficioEmittente + " - "
				+ mCodTipoUfficioEmittente + " - " + mCodLuogoEmittente + " - " + mDescrLuogoEmittente + " - "
				+
				// mSoggettoPresentante +" - " +
				mDataEmissione + " - " + mCodEsito + " - " + mDescrEsito + " - " + mFlagPiuMeno + " - "
				+ mDataTrasmissioneAtti + " - " + mDataRicezioneAtti + " - " + mCodUfficioDestinatario + " - "
				+ mDescrUfficioDestinatario + " - " + mAnnoProtocollo + " - " + mProgrProtocollo + " - " +
				// mDocBlob +" - " +
				mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mFasSieIdFascicoloSiep + " - " + mFasSiuIdFascicoloSius + " - " +
				// mFasSiuSogIdSoggetto +" - " +
				mCodLuogoDestinatario + " - " + mDescrLuogoDestinatario + " - " + mTenIdTenore + " - "
				+ mEveIdEvento + " - " + mFlagDocumentoRegistrato + " - " + mCodMagistrato + " - "
				+ mCodTipoUfficioDestinatario + " - " + mCognomeSoggettoPresentante + " - "
				+ mNomeSoggettoPresentante + " - " + mFasSiuIdFascicoloSiusDest + " - " +
				// STUB:2003-07-09 PM Aggiunto pro tempore
				mTemIdTemplate + " - " + mFlagStampaSiep + " - " + mFlagStampaSius + " - " + mFlagVideoSiep
				+ " - " + mEventoCorrente + " - " + mFlagVideoSius + " - " + mDecIdDecretoOrdinanzaSiep
				+ " - " + mLegge + " - " + mNumAllegati + " - " + mNumAllValidati + " - "
				+ mDescrProvvedimento + " - " + mData + " - " + mDescrizioneData + " - "
				+ mDecIdDecretoOrdinanzaSiep + " - " + mPenAccIdPenaAccessoria + " - " + // STUB 08/02/2006
				mEveIdEvento + " - " + mEveIdEventoRevoca + " - " + mAnnIdAnnotazioneManuale + " - "
				+ mPenIdPenaResidua + " - " + mCodUfficio + " - " + mDataEspulsioneSanzSost + " - "
				+ mKeyEsecNsc;

		return lStr;
	}

}