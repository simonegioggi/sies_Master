package siap.siep.modulocumulo.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.model.GenericModel;

import siap.sico.evento.model.EventoModel;

/**
 * <p>
 * Title: StatoEsecTitoloCumulatoModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il StatoEsecTitoloCumulato
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
public class StatoEsecTitoloCumulatoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8973911068049243671L;

	private BigDecimal mIdStatoEsecTitoloCumulato;
	private String mCodTipoEvento;
	private String mDescrTipoEvento;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private String mCodMotivo;
	private String mDescrMotivo;

	// Campi della tabella MOTIVO_EVENTO
	private String mCodMotivoRevoca;
	private String mDescrMotivoRevoca;
	private String mCodMotivoRevocaPm;
	private String mDescrMotivoRevocaPm;

	private String mCodUfficioEmittente;
	private String mDescrUfficioEmittente;
	private String mCodAutoritaEmittente;
	private String mDescrAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;

	private Date mDataEmissione;
	private String mCodEsito;
	private String mDescrEsito;
	private String mCodEsitoTenore;
	private String mDescrEsitoTenore;
	private BigDecimal mAnnoProcedimento;
	private BigDecimal mProgrProcedimento;
	private BigDecimal mAnnoProvvedimento;
	private BigDecimal mProgrProvvedimento;
	private String mNote;
	private BigDecimal mTitIdTitoloCumulato;
	private BigDecimal mIstrIdIstruttoriaCumulo;
	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mIdEventoOrigine;
	private BigDecimal mEveIdEventoOrigine;

	// Campi della Tabella NUOVA_ISTANZA
	private String mCodContenutoIstanza;
	private String mDescrContenutoIstanza;
	private Date mDataIstanza;
	private String mFlagIstanzaPresdep;
	private String mCodStatoIstanza;
	private String mDescrStatoIstanza;

	private String mFlagTipoSosp;

	private String mCodTipoIstante;
	private String mCodTipoUfficioAltro;
	private String mDescrTipoUfficioAltro;
	private String mCodTipoAutoritaAltro;
	private String mDescrTipoAutoritaAltro;
	private String mCodLuogoAltro;
	private String mDescrLuogoAltro;
	private String mCodUfficioAltro;
	private String mSezioneAltro;
	private Date mDataEmissioneAltro;

	// Campi per gestireun eventuale destinatario di trasmissione (es istanza trasmessa al TDS)
	private String mCodTipoUfficioDestinatario;
	private String mDescrTipoUfficioDestinatario;
	private String mCodLuogoDestinatario;
	private String mDescrLuogoDestinatario;
	private String mCodUfficioDestinatario;
	private String mDescrUfficioDestinatario;
	private Date mDataTrasmissione;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private String mStringaPeriodiLA;

	private Vector<ComputiCumuloModel> mListaComputi;
	private Vector<NotificaCumuloModel> mListaNotifiche;
	private Vector<LibAnticipataCumuloModel> mListaLibAnticipate;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public StatoEsecTitoloCumulatoModel() {
		this.mIdStatoEsecTitoloCumulato = null;
		this.mCodTipoEvento = "";
		this.mDescrTipoEvento = "";
		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mCodMotivo = "";
		this.mDescrMotivo = "";
		this.mCodMotivoRevoca = "";
		this.mDescrMotivoRevoca = "";
		this.mCodMotivoRevocaPm = "";
		this.mDescrMotivoRevocaPm = "";

		this.mCodUfficioEmittente = "";
		this.mDescrUfficioEmittente = "";
		this.mCodAutoritaEmittente = "";
		this.mDescrAutoritaEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";
		this.mDataEmissione = null;
		this.mCodEsito = "";
		this.mDescrEsito = "";
		this.mCodEsitoTenore = "";
		this.mDescrEsitoTenore = "";
		this.mAnnoProcedimento = null;
		this.mProgrProcedimento = null;
		this.mAnnoProvvedimento = null;
		this.mProgrProvvedimento = null;
		this.mNote = "";

		this.mCodContenutoIstanza = "";
		this.mDescrContenutoIstanza = "";
		this.mDataIstanza = null;
		this.mFlagIstanzaPresdep = "";
		this.mCodStatoIstanza = "";
		this.mDescrStatoIstanza = "";
		this.mCodTipoUfficioDestinatario = "";
		this.mDescrTipoUfficioDestinatario = "";
		this.mCodLuogoDestinatario = "";
		this.mDescrLuogoDestinatario = "";
		this.mCodUfficioDestinatario = "";
		this.mDescrUfficioDestinatario = "";
		this.mDataTrasmissione = null;

		this.mFlagTipoSosp = "";

		this.mCodTipoIstante = "";
		this.mCodTipoUfficioAltro = "";
		this.mDescrTipoUfficioAltro = "";
		this.mCodTipoAutoritaAltro = "";
		this.mDescrTipoAutoritaAltro = "";
		this.mCodLuogoAltro = "";
		this.mDescrLuogoAltro = "";
		this.mCodUfficioAltro = "";
		this.mSezioneAltro = "";
		this.mDataEmissioneAltro = null;

		this.mTitIdTitoloCumulato = null;
		this.mIstrIdIstruttoriaCumulo = null;
		this.mFlagStato = "";
		this.mMotivoModifica = "";
		this.mIdEventoOrigine = null;
		this.mEveIdEventoOrigine = null;

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public StatoEsecTitoloCumulatoModel(StatoEsecTitoloCumulatoModel aModel) {
		this.mIdStatoEsecTitoloCumulato = aModel.mIdStatoEsecTitoloCumulato;
		this.mCodTipoEvento = aModel.mCodTipoEvento;
		this.mDescrTipoEvento = aModel.mDescrTipoEvento;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mCodMotivo = aModel.mCodMotivo;
		this.mDescrMotivo = aModel.mDescrMotivo;
		this.mCodMotivoRevoca = aModel.mCodMotivoRevoca;
		this.mDescrMotivoRevoca = aModel.mDescrMotivoRevoca;
		this.mCodMotivoRevocaPm = aModel.mCodMotivoRevocaPm;
		this.mDescrMotivoRevocaPm = aModel.mDescrMotivoRevocaPm;

		this.mCodUfficioEmittente = aModel.mCodUfficioEmittente;
		this.mDescrUfficioEmittente = aModel.mDescrUfficioEmittente;
		this.mCodAutoritaEmittente = aModel.mCodAutoritaEmittente;
		this.mDescrAutoritaEmittente = aModel.mDescrAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mDataEmissione = aModel.mDataEmissione;

		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mCodEsitoTenore = aModel.mCodEsitoTenore;
		this.mDescrEsitoTenore = aModel.mDescrEsitoTenore;
		this.mAnnoProcedimento = aModel.mAnnoProcedimento;
		this.mProgrProcedimento = aModel.mProgrProcedimento;
		this.mAnnoProvvedimento = aModel.mAnnoProvvedimento;
		this.mProgrProvvedimento = aModel.mProgrProvvedimento;
		this.mNote = aModel.mNote;

		this.mCodContenutoIstanza = aModel.mCodContenutoIstanza;
		this.mDescrContenutoIstanza = aModel.mDescrContenutoIstanza;
		this.mDataIstanza = aModel.mDataIstanza;
		this.mFlagIstanzaPresdep = aModel.mFlagIstanzaPresdep;
		this.mCodStatoIstanza = aModel.mCodStatoIstanza;
		this.mDescrStatoIstanza = aModel.mDescrStatoIstanza;
		this.mCodTipoUfficioDestinatario = aModel.mCodTipoUfficioDestinatario;
		this.mDescrTipoUfficioDestinatario = aModel.mDescrTipoUfficioDestinatario;
		this.mCodLuogoDestinatario = aModel.mCodLuogoDestinatario;
		this.mDescrLuogoDestinatario = aModel.mDescrLuogoDestinatario;
		this.mCodUfficioDestinatario = aModel.mCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aModel.mDescrUfficioDestinatario;
		this.mDataTrasmissione = aModel.mDataTrasmissione;

		this.mFlagTipoSosp = aModel.mFlagTipoSosp;

		this.mCodTipoIstante = aModel.mCodTipoIstante;
		this.mCodTipoUfficioAltro = aModel.mCodTipoUfficioAltro;
		this.mDescrTipoUfficioAltro = aModel.mDescrTipoUfficioAltro;
		this.mCodTipoAutoritaAltro = aModel.mCodTipoAutoritaAltro;
		this.mDescrTipoAutoritaAltro = aModel.mDescrTipoAutoritaAltro;
		this.mCodLuogoAltro = aModel.mCodLuogoAltro;
		this.mDescrLuogoAltro = aModel.mDescrLuogoAltro;
		this.mCodUfficioAltro = aModel.mCodUfficioAltro;
		this.mSezioneAltro = aModel.mSezioneAltro;
		this.mDataEmissioneAltro = aModel.mDataEmissioneAltro;

		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;
		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mIdEventoOrigine = aModel.mIdEventoOrigine;
		this.mEveIdEventoOrigine = aModel.mEveIdEventoOrigine;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public StatoEsecTitoloCumulatoModel(BigDecimal aIdStatoEsecTitoloCumulato, String aCodTipoEvento,
			String aDescrTipoEvento, String aCodTipoProvvedimento, String aDescrTipoProvvedimento,
			String aCodMotivo, String aDescrMotivo, String aCodMotivoRevoca, String aDescrMotivoRevoca,

			String aCodUfficioEmittente, String aDescrUfficioEmittente, String aCodAutoritaEmittente,
			String aDescrAutoritaEmittente, String aCodLuogoEmittente, String aDescrLuogoEmittente,
			Date aDataEmissione,

			String aCodEsito, String aDescrEsito, String aCodEsitoTenore, String aDescrEsitoTenore,
			BigDecimal aAnnoProcedimento, BigDecimal aProgrProcedimento, BigDecimal aAnnoProvvedimento,
			BigDecimal aProgrProvvedimento, String aNote,

			String aCodMotivoRevocaPm, String aDescrMotivoRevocaPm, String aCodContenutoIstanza,
			String aDescrContenutoIstanza, Date aDataIstanza, String aFlagIstanzaPresdep,
			String aCodStatoIstanza, String aDescrStatoIstanza, String aCodTipoUfficioDestinatario,
			String aDescrTipoUfficioDestinatario, String aCodLuogoDestinatario,
			String aDescrLuogoDestinatario, String aCodUfficioDestinatario, String aDescrUfficioDestinatario,
			Date aDataTrasmissione,

			String aFlagTipoSosp,

			String aCodTipoIstante, String aCodTipoUfficioAltro, String aDescrTipoUfficioAltro,
			String aCodTipoAutoritaAltro, String aDescrTipoAutoritaAltro, String aCodLuogoAltro,
			String aDescrLuogoAltro, String aCodUfficioAltro, String aSezioneAltro, Date aDataEmissioneAltro,

			BigDecimal aTitIdTitoloCumulato, BigDecimal aIstrIdIstruttoriaCumulo, String aFlagStato,
			String aMotivoModifica, BigDecimal aIdEventoOrigine, BigDecimal aEveIdEventoOrigine,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdStatoEsecTitoloCumulato = aIdStatoEsecTitoloCumulato;
		this.mCodTipoEvento = aCodTipoEvento;
		this.mDescrTipoEvento = aDescrTipoEvento;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mCodMotivo = aCodMotivo;
		this.mDescrMotivo = aDescrMotivo;
		this.mCodMotivoRevoca = aCodMotivoRevoca;
		this.mDescrMotivoRevoca = aDescrMotivoRevoca;

		this.mCodUfficioEmittente = aCodUfficioEmittente;
		this.mDescrUfficioEmittente = aDescrUfficioEmittente;
		this.mCodAutoritaEmittente = aCodAutoritaEmittente;
		this.mDescrAutoritaEmittente = aDescrAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mDataEmissione = aDataEmissione;

		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mCodEsitoTenore = aCodEsitoTenore;
		this.mDescrEsitoTenore = aDescrEsitoTenore;
		this.mAnnoProcedimento = aAnnoProcedimento;
		this.mProgrProcedimento = aProgrProcedimento;
		this.mAnnoProvvedimento = aAnnoProvvedimento;
		this.mProgrProvvedimento = aProgrProvvedimento;
		this.mNote = aNote;

		this.mCodMotivoRevocaPm = aCodMotivoRevocaPm;
		this.mDescrMotivoRevocaPm = aDescrMotivoRevocaPm;
		this.mCodContenutoIstanza = aCodContenutoIstanza;
		this.mDescrContenutoIstanza = aDescrContenutoIstanza;
		this.mDataIstanza = aDataIstanza;
		this.mFlagIstanzaPresdep = aFlagIstanzaPresdep;
		this.mCodStatoIstanza = aCodStatoIstanza;
		this.mDescrStatoIstanza = aDescrStatoIstanza;
		this.mCodTipoUfficioDestinatario = aCodTipoUfficioDestinatario;
		this.mDescrTipoUfficioDestinatario = aDescrTipoUfficioDestinatario;
		this.mCodLuogoDestinatario = aCodLuogoDestinatario;
		this.mDescrLuogoDestinatario = aDescrLuogoDestinatario;
		this.mCodUfficioDestinatario = aCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aDescrUfficioDestinatario;
		this.mDataTrasmissione = aDataTrasmissione;

		this.mFlagTipoSosp = aFlagTipoSosp;

		this.mCodTipoIstante = aCodTipoIstante;
		this.mCodTipoUfficioAltro = aCodTipoUfficioAltro;
		this.mDescrTipoUfficioAltro = aDescrTipoUfficioAltro;
		this.mCodTipoAutoritaAltro = aCodTipoAutoritaAltro;
		this.mDescrTipoAutoritaAltro = aDescrTipoAutoritaAltro;
		this.mCodLuogoAltro = aCodLuogoAltro;
		this.mDescrLuogoAltro = aDescrLuogoAltro;
		this.mCodUfficioAltro = aCodUfficioAltro;
		this.mSezioneAltro = aSezioneAltro;
		this.mDataEmissioneAltro = aDataEmissioneAltro;

		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mIstrIdIstruttoriaCumulo = aIstrIdIstruttoriaCumulo;
		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mIdEventoOrigine = aIdEventoOrigine;
		this.mEveIdEventoOrigine = aEveIdEventoOrigine;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	public StatoEsecTitoloCumulatoModel(EventoModel aModel) {
		this.mIdStatoEsecTitoloCumulato = null;
		this.mCodTipoEvento = aModel.getCodTipoEvento();
		this.mCodTipoProvvedimento = aModel.getCodTipoProvvedimento();
		this.mCodMotivo = aModel.getCodMotivo();
		this.mCodUfficioEmittente = aModel.getCodUfficioEmittente();
		this.mCodLuogoEmittente = aModel.getCodLuogoEmittente();
		this.mDataEmissione = aModel.getDataEmissione();
		this.mCodEsito = aModel.getCodEsito();
		// this.mCodEsitoTenore = aModel.mCodEsitoTenore;
		// this.mDescrEsitoTenore = aModel.mDescrEsitoTenore;
		// this.mAnnoProcedimento = aModel.mAnnoProcedimento;
		// this.mProgrProcedimento = aModel.mProgrProcedimento;
		// this.mAnnoProvvedimento = aModel.mAnnoProvvedimento;
		// this.mProgrProvvedimento = aModel.mProgrProvvedimento;
		// this.mNote = aModel.mNote;
		// this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		// this.mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;
		// this.mFlagStato = aModel.mFlagStato;
		// this.mMotivoModifica = aModel.mMotivoModifica;
		this.mIdEventoOrigine = aModel.getIdEvento();
		this.mEveIdEventoOrigine = aModel.getEveIdEvento();

	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdStatoEsecTitoloCumulato() {
		return mIdStatoEsecTitoloCumulato;
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

	public String getCodMotivoRevoca() {
		return mCodMotivoRevoca;
	}

	public String getDescrMotivoRevoca() {
		return mDescrMotivoRevoca;
	}

	public String getCodMotivoRevocaPm() {
		return mCodMotivoRevocaPm;
	}

	public String getDescrMotivoRevocaPm() {
		return mDescrMotivoRevocaPm;
	}

	public String getCodUfficioEmittente() {
		return mCodUfficioEmittente;
	}

	public String getDescrUfficioEmittente() {
		return mDescrUfficioEmittente;
	}

	public String getCodAutoritaEmittente() {
		return mCodAutoritaEmittente;
	}

	public String getDescrAutoritaEmittente() {
		return mDescrAutoritaEmittente;
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

	public String getCodEsitoTenore() {
		return mCodEsitoTenore;
	}

	public String getDescrEsitoTenore() {
		return mDescrEsitoTenore;
	}

	public BigDecimal getAnnoProcedimento() {
		return mAnnoProcedimento;
	}

	public BigDecimal getProgrProcedimento() {
		return mProgrProcedimento;
	}

	public BigDecimal getAnnoProvvedimento() {
		return mAnnoProvvedimento;
	}

	public BigDecimal getProgrProvvedimento() {
		return mProgrProvvedimento;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodContenutoIstanza() {
		return mCodContenutoIstanza;
	}

	public String getDescrContenutoIstanza() {
		return mDescrContenutoIstanza;
	}

	public Date getDataIstanza() {
		return mDataIstanza;
	}

	public String getFlagIstanzaPresdep() {
		return mFlagIstanzaPresdep;
	}

	public String getCodStatoIstanza() {
		return mCodStatoIstanza;
	}

	public String getDescrStatoIstanza() {
		return mDescrStatoIstanza;
	}

	public String getCodTipoUfficioDestinatario() {
		return mCodTipoUfficioDestinatario;
	}

	public String getDescrTipoUfficioDestinatario() {
		return mDescrTipoUfficioDestinatario;
	}

	public String getCodLuogoDestinatario() {
		return mCodLuogoDestinatario;
	}

	public String getDescrLuogoDestinatario() {
		return mDescrLuogoDestinatario;
	}

	public String getCodUfficioDestinatario() {
		return mCodUfficioDestinatario;
	}

	public String getDescrUfficioDestinatario() {
		return mDescrUfficioDestinatario;
	}

	public Date getDataTrasmissione() {
		return mDataTrasmissione;
	}

	public String getFlagTipoSosp() {
		return mFlagTipoSosp;
	}

	public String getCodTipoIstante() {
		return mCodTipoIstante;
	}

	public String getCodTipoUfficioAltro() {
		return mCodTipoUfficioAltro;
	}

	public String getDescrTipoUfficioAltro() {
		return mDescrTipoUfficioAltro;
	}

	public String getCodTipoAutoritaAltro() {
		return mCodTipoAutoritaAltro;
	}

	public String getDescrTipoAutoritaAltro() {
		return mDescrTipoAutoritaAltro;
	}

	public String getCodLuogoAltro() {
		return mCodLuogoAltro;
	}

	public String getDescrLuogoAltro() {
		return mDescrLuogoAltro;
	}

	public String getCodUfficioAltro() {
		return mCodUfficioAltro;
	}

	public String getSezioneAltro() {
		return mSezioneAltro;
	}

	public Date getDataEmissioneAltro() {
		return mDataEmissioneAltro;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public BigDecimal getIstrIdIstruttoriaCumulo() {
		return mIstrIdIstruttoriaCumulo;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public BigDecimal getIdEventoOrigine() {
		return mIdEventoOrigine;
	}

	public BigDecimal getEveIdEventoOrigine() {
		return mEveIdEventoOrigine;
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

	public String getStringaPeriodoLA() {
		return mStringaPeriodiLA;
	}

	public Vector<ComputiCumuloModel> getListaComputi() {
		return mListaComputi;
	}

	public Vector<NotificaCumuloModel> getListaNotifiche() {
		return mListaNotifiche;
	}

	public Vector<LibAnticipataCumuloModel> getListaLiberazioniAnticipate() {
		return mListaLibAnticipate;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdStatoEsecTitoloCumulato(BigDecimal aValore) {
		mIdStatoEsecTitoloCumulato = aValore;
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

	public void setCodMotivoRevoca(String aValore) {
		mCodMotivoRevoca = aValore;
	}

	public void setDescrMotivoRevoca(String aValore) {
		mDescrMotivoRevoca = aValore;
	}

	public void setCodMotivoRevocaPm(String aValore) {
		mCodMotivoRevocaPm = aValore;
	}

	public void setDescrMotivoRevocaPm(String aValore) {
		mDescrMotivoRevocaPm = aValore;
	}

	public void setCodUfficioEmittente(String aValore) {
		mCodUfficioEmittente = aValore;
	}

	public void setDescrUfficioEmittente(String aValore) {
		mDescrUfficioEmittente = aValore;
	}

	public void setCodAutoritaEmittente(String aValore) {
		mCodAutoritaEmittente = aValore;
	}

	public void setDescrAutoritaEmittente(String aValore) {
		mDescrAutoritaEmittente = aValore;
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

	public void setCodEsitoTenore(String aValore) {
		mCodEsitoTenore = aValore;
	}

	public void setDescrEsitoTenore(String aValore) {
		mDescrEsitoTenore = aValore;
	}

	public void setAnnoProcedimento(BigDecimal aValore) {
		mAnnoProcedimento = aValore;
	}

	public void setProgrProcedimento(BigDecimal aValore) {
		mProgrProcedimento = aValore;
	}

	public void setAnnoProvvedimento(BigDecimal aValore) {
		mAnnoProvvedimento = aValore;
	}

	public void setProgrProvvedimento(BigDecimal aValore) {
		mProgrProvvedimento = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodContenutoIstanza(String aValore) {
		mCodContenutoIstanza = aValore;
	}

	public void setDescrContenutoIstanza(String aValore) {
		mDescrContenutoIstanza = aValore;
	}

	public void setDataIstanza(Date aValore) {
		mDataIstanza = aValore;
	}

	public void setFlagIstanzaPresdep(String aValore) {
		mFlagIstanzaPresdep = aValore;
	}

	public void setCodStatoIstanza(String aValore) {
		mCodStatoIstanza = aValore;
	}

	public void setDescrStatoIstanza(String aValore) {
		mDescrStatoIstanza = aValore;
	}

	public void setCodTipoUfficioDestinatario(String aValore) {
		mCodTipoUfficioDestinatario = aValore;
	}

	public void setDescrTipoUfficioDestinatario(String aValore) {
		mDescrTipoUfficioDestinatario = aValore;
	}

	public void setCodLuogoDestinatario(String aValore) {
		mCodLuogoDestinatario = aValore;
	}

	public void setDescrLuogoDestinatario(String aValore) {
		mDescrLuogoDestinatario = aValore;
	}

	public void setCodUfficioDestinatario(String aValore) {
		mCodUfficioDestinatario = aValore;
	}

	public void setDescrUfficioDestinatario(String aValore) {
		mDescrUfficioDestinatario = aValore;
	}

	public void setDataTrasmissione(Date aValore) {
		mDataTrasmissione = aValore;
	}

	public void setFlagTipoSosp(String aValore) {
		mFlagTipoSosp = aValore;
	}

	public void setCodTipoIstante(String aValore) {
		mCodTipoIstante = aValore;
	}

	public void setCodTipoUfficioAltro(String aValore) {
		mCodTipoUfficioAltro = aValore;
	}

	public void setDescrTipoUfficioAltro(String aValore) {
		mDescrTipoUfficioAltro = aValore;
	}

	public void setCodTipoAutoritaAltro(String aValore) {
		mCodTipoAutoritaAltro = aValore;
	}

	public void setDescrTipoAutoritaAltro(String aValore) {
		mDescrTipoAutoritaAltro = aValore;
	}

	public void setCodLuogoAltro(String aValore) {
		mCodLuogoAltro = aValore;
	}

	public void setDescrLuogoAltro(String aValore) {
		mDescrLuogoAltro = aValore;
	}

	public void setCodUfficioAltro(String aValore) {
		mCodUfficioAltro = aValore;
	}

	public void setSezioneAltro(String aValore) {
		mSezioneAltro = aValore;
	}

	public void setDataEmissioneAltro(Date aValore) {
		mDataEmissioneAltro = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		mIstrIdIstruttoriaCumulo = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setIdEventoOrigine(BigDecimal aValore) {
		mIdEventoOrigine = aValore;
	}

	public void setEveIdEventoOrigine(BigDecimal aValore) {
		mEveIdEventoOrigine = aValore;
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

	public void setStringaPeriodoLA(String aValore) {
		mStringaPeriodiLA = aValore;
	}

	public void setListaComputi(Vector<ComputiCumuloModel> aValore) {
		mListaComputi = aValore;
	}

	public void setListaNotifiche(Vector<NotificaCumuloModel> aValore) {
		mListaNotifiche = aValore;
	}

	public void setListaLiberazioniAnticipate(Vector<LibAnticipataCumuloModel> aValore) {
		mListaLibAnticipate = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "StatoEsecTitoloCumulatoModel:\n" + "[ mIdStatoEsecTitoloCumulato = "
				+ mIdStatoEsecTitoloCumulato + " ]\n" + "[ mCodTipoEvento             = " + mCodTipoEvento
				+ " ]\n" + "[ mCodTipoProvvedimento      = " + mCodTipoProvvedimento + " ]\n"
				+ "[ mCodMotivo                 = " + mCodMotivo + " ]\n" + "[ mCodMotivoRevoca           = "
				+ mCodMotivoRevoca + " ]\n" + "[ mCodMotivoRevocaPm         = " + mCodMotivoRevocaPm + " ]\n"
				+

				"[ mCodUfficioEmittente       = " + mCodUfficioEmittente + " ]\n"
				+ "[ mCodAutoritaEmittente      = " + mCodAutoritaEmittente + " ]\n"
				+ "[ mCodLuogoEmittente         = " + mCodLuogoEmittente + " ]\n"
				+ "[ mDataEmissione             = " + mDataEmissione + " ]\n"
				+ "[ mCodEsito                  = " + mCodEsito + " ]\n" + "[ mCodEsitoTenore            = "
				+ mCodEsitoTenore + " ]\n" + "[ mAnnoProcedimento          = " + mAnnoProcedimento + " ]\n"
				+ "[ mProgrProcedimento         = " + mProgrProcedimento + " ]\n"
				+ "[ mAnnoProvvedimento         = " + mAnnoProvvedimento + " ]\n"
				+ "[ mProgrProvvedimento        = " + mProgrProvvedimento + " ]\n"
				+ "[ mNote                      = " + mNote + " ]\n" +

				"[ mCodContenutoIstanza        = " + mCodContenutoIstanza + " ]\n"
				+ "[ mDataIstanza                = " + mDataIstanza + " ]\n"
				+ "[ mFlagIstanzaPresdep         = " + mFlagIstanzaPresdep + " ]\n"
				+ "[ mCodStatoIstanza            = " + mCodStatoIstanza + " ]\n"
				+ "[ mCodTipoUfficioDestinatario = " + mCodTipoUfficioDestinatario + " ]\n"
				+ "[ mCodLuogoDestinatario       = " + mCodLuogoDestinatario + " ]\n"
				+ "[ mCodUfficioDestinatario     = " + mCodUfficioDestinatario + " ]\n"
				+ "[ mDataTrasmissione           = " + mDataTrasmissione + " ]\n" +

				"[ mFlagTipoSosp               = " + mFlagTipoSosp + " ]\n" +

				"[ mCodTipoIstante             = " + mCodTipoIstante + " ]\n"
				+ "[ mCodTipoUfficioAltro        = " + mCodTipoUfficioAltro + " ]\n"
				+ "[ mCodTipoAutoritaAltro       = " + mCodTipoAutoritaAltro + " ]\n"
				+ "[ mCodLuogoAltro              = " + mCodLuogoAltro + " ]\n"
				+ "[ mCodUfficioAltro            = " + mCodUfficioAltro + " ]\n"
				+ "[ mSezioneAltro               = " + mSezioneAltro + " ]\n"
				+ "[ mDataEmissioneAltro         = " + mDataEmissioneAltro + " ]\n" +

				"[ mTitIdTitoloCumulato       = " + mTitIdTitoloCumulato + " ]\n"
				+ "[ mIstrIdIstruttoriaCumulo   = " + mIstrIdIstruttoriaCumulo + " ]\n"
				+ "[ mFlagStato                 = " + mFlagStato + " ]\n" + "[ mMotivoModifica            = "
				+ mMotivoModifica + " ]\n" + "[ mIdEventoOrigine           = " + mIdEventoOrigine + " ]\n"
				+ "[ mEveIdEventoOrigine        = " + mEveIdEventoOrigine + " ]\n" +

				"[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

}