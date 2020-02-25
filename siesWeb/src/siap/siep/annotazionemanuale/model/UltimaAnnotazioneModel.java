package siap.siep.annotazionemanuale.model;

/**
* <p>Title: EventoModel</p>
* <p>Description: Classe Model che rappresenta il Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SiapStringUtil;
import f3b.model.GenericModel;

@SuppressWarnings("rawtypes")
public class UltimaAnnotazioneModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1692804127985932849L;

	private BigDecimal mIdEvento;
	private String mCodTipoEvento;
	private String mDescrTipoEvento;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private String mCodMotivo;
	private String mDescrMotivo;
	private String mCodUfficioEmittente;
	private String mDescrUfficioEmittente;
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
	private String mCodLuogoDestinatario;
	private String mDescrLuogoDestinatario;
	private BigDecimal mTenIdTenore;
	private BigDecimal mEveIdEvento;
	private ByteArrayInputStream mDocBlobIn;
	private ByteArrayOutputStream mDocBlobOut;
	private String mFlagDocumentoRegistrato;
	private String mCodMagistrato;
	private String mDescrMagistrato;
	private String mCodTipoUfficioDestinatario;
	private String mDescrTipoUfficioDestinatario;
	private boolean mExistBlob;
	private BigDecimal mFasSiuIdFascicoloSiusDest;
	private String mTemIdTemplate;
	private String mFlagStampaSiep;
	private String mFlagStampaSius;
	private String mFlagVideoSiep;
	private String mFlagVideoSius;
	private String mLegge; // GDV aggiunto per l'RV_ABBREVIATION

	// GDV per la Stampa dello stato di Esecuzione
	private String mEventoCorrente;

	// Numero di documenti allegati associati all'evento
	private int mNumAllegati;
	private Vector mVectAnnotazioneManuale;

	// Contengono lea stringhe GG MM AA del totale delle annotazioni assciate
	private String mTotalePeriodoReclusione;
	private String mTotalePeriodoArresto;

	private BigDecimal mAnnIdAnnotazioneManuale = null;
	private BigDecimal mPenIdPenaResidua = null;

	// COSTRUTTORE DI DEFAULT
	public UltimaAnnotazioneModel() {
		this.mIdEvento = null;
		this.mCodTipoEvento = null;
		this.mDescrTipoEvento = null;
		this.mCodTipoProvvedimento = null;
		this.mDescrTipoProvvedimento = null;
		this.mCodMotivo = null;
		this.mDescrMotivo = null;
		this.mCodUfficioEmittente = null;
		this.mDescrUfficioEmittente = null;
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
		this.mEventoCorrente = null;
		this.mNumAllegati = 0;
		this.mLegge = "";
		this.mVectAnnotazioneManuale = null;

		this.mTotalePeriodoReclusione = null;
		this.mTotalePeriodoReclusione = null;

		this.mAnnIdAnnotazioneManuale = null;
		this.mPenIdPenaResidua = null;
	}

	// COSTRUTTORE DI COPIA
	public UltimaAnnotazioneModel(EventoModel aModel) {
		this.mIdEvento = aModel.getIdEvento();
		this.mCodTipoEvento = aModel.getCodTipoEvento();
		this.mDescrTipoEvento = aModel.getDescrTipoEvento();
		this.mCodTipoProvvedimento = aModel.getCodTipoProvvedimento();
		this.mDescrTipoProvvedimento = aModel.getDescrTipoProvvedimento();
		this.mCodMotivo = aModel.getCodMotivo();
		this.mDescrMotivo = aModel.getDescrMotivo();
		this.mCodUfficioEmittente = aModel.getCodUfficioEmittente();
		this.mDescrUfficioEmittente = aModel.getDescrUfficioEmittente();
		this.mCodLuogoEmittente = aModel.getCodLuogoEmittente();
		this.mDescrLuogoEmittente = aModel.getDescrLuogoEmittente();
		this.mCognomeSoggettoPresentante = aModel.getCognomeSoggettoPresentante();
		this.mNomeSoggettoPresentante = aModel.getNomeSoggettoPresentante();
		this.mDataEmissione = aModel.getDataEmissione();
		this.mCodEsito = aModel.getCodEsito();
		this.mDescrEsito = aModel.getDescrEsito();
		this.mFlagPiuMeno = aModel.getFlagPiuMeno();
		this.mDataTrasmissioneAtti = aModel.getDataTrasmissioneAtti();
		this.mDataRicezioneAtti = aModel.getDataRicezioneAtti();
		this.mCodUfficioDestinatario = aModel.getCodUfficioDestinatario();
		this.mDescrUfficioDestinatario = aModel.getDescrUfficioDestinatario();
		this.mAnnoProtocollo = aModel.getAnnoProtocollo();
		this.mProgrProtocollo = aModel.getProgrProtocollo();
		// this.mDocBlob = aModel.getDocBlob();
		this.mCodOperatoreInserimento = aModel.getCodOperatoreInserimento();
		this.mDataInserimento = aModel.getDataInserimento();
		this.mCodUfficioInserimento = aModel.getCodUfficioInserimento();
		this.mDescrUfficioInserimento = aModel.getDescrUfficioInserimento();
		this.mCodOperatoreAggiornamento = aModel.getCodOperatoreAggiornamento();
		this.mDataAggiornamento = aModel.getDataAggiornamento();
		this.mCodUfficioAggiornamento = aModel.getCodUfficioAggiornamento();
		this.mDescrUfficioAggiornamento = aModel.getDescrUfficioAggiornamento();
		this.mFasSieIdFascicoloSiep = aModel.getFasSieIdFascicoloSiep();
		this.mFasSiuIdFascicoloSius = aModel.getFasSiuIdFascicoloSius();
		// this.mFasSiuSogIdSoggetto = aModel.getFasSiuSogIdSoggetto();
		this.mCodLuogoDestinatario = aModel.getCodLuogoDestinatario();
		this.mDescrLuogoDestinatario = aModel.getDescrLuogoDestinatario();
		this.mTenIdTenore = aModel.getTenIdTenore();
		this.mEveIdEvento = aModel.getEveIdEvento();
		this.mFlagDocumentoRegistrato = aModel.getFlagDocumentoRegistrato();
		this.mCodMagistrato = aModel.getCodMagistrato();
		this.mDescrMagistrato = aModel.getDescrMagistrato();
		this.mCodTipoUfficioDestinatario = aModel.getCodTipoUfficioDestinatario();
		this.mDescrTipoUfficioDestinatario = aModel.getDescrTipoUfficioDestinatario();
		this.mExistBlob = aModel.getExistBlob();
		this.mCognomeSoggettoPresentante = aModel.getCognomeSoggettoPresentante();
		this.mNomeSoggettoPresentante = aModel.getNomeSoggettoPresentante();
		this.mFasSiuIdFascicoloSiusDest = aModel.getFasSiuIdFascicoloSiusDest();
		// STUB:2003-07-09 PM Aggiunto pro tempore
		this.mTemIdTemplate = aModel.getTemIdTemplate();
		this.mFlagStampaSiep = aModel.getFlagStampaSiep();
		this.mFlagStampaSius = aModel.getFlagStampaSius();
		this.mFlagVideoSiep = aModel.getFlagVideoSiep();
		this.mFlagVideoSius = aModel.getFlagVideoSius();
		this.mEventoCorrente = aModel.getEventoCorrente();
		this.mNumAllegati = aModel.getNumAllegati();
		// this.mVectAnnotazioneManuale =
		this.mAnnIdAnnotazioneManuale = aModel.getAnnIdAnnotazioneManuale();
		this.mPenIdPenaResidua = aModel.getPenIdPenaResidua();
	}

	public UltimaAnnotazioneModel(EventoNotificaModel aModel) {
		this.mIdEvento = aModel.getEvento().getIdEvento();
		this.mCodTipoEvento = aModel.getEvento().getCodTipoEvento();
		this.mDescrTipoEvento = aModel.getEvento().getDescrTipoEvento();
		this.mCodTipoProvvedimento = aModel.getEvento().getCodTipoProvvedimento();
		this.mDescrTipoProvvedimento = aModel.getEvento().getDescrTipoProvvedimento();
		this.mCodMotivo = aModel.getEvento().getCodMotivo();
		this.mDescrMotivo = aModel.getEvento().getDescrMotivo();
		this.mCodUfficioEmittente = aModel.getEvento().getCodUfficioEmittente();
		this.mDescrUfficioEmittente = aModel.getEvento().getDescrUfficioEmittente();
		this.mCodLuogoEmittente = aModel.getEvento().getCodLuogoEmittente();
		this.mDescrLuogoEmittente = aModel.getEvento().getDescrLuogoEmittente();
		// this.mSoggettoPresentante = aModel.getEvento().getSoggettoPresentante();
		this.mDataEmissione = aModel.getEvento().getDataEmissione();
		this.mCodEsito = aModel.getEvento().getCodEsito();
		this.mDescrEsito = aModel.getEvento().getDescrEsito();
		this.mFlagPiuMeno = aModel.getEvento().getFlagPiuMeno();
		this.mDataTrasmissioneAtti = aModel.getEvento().getDataTrasmissioneAtti();
		this.mDataRicezioneAtti = aModel.getEvento().getDataRicezioneAtti();
		this.mCodUfficioDestinatario = aModel.getEvento().getCodUfficioDestinatario();
		this.mDescrUfficioDestinatario = aModel.getEvento().getDescrUfficioDestinatario();
		this.mAnnoProtocollo = aModel.getEvento().getAnnoProtocollo();
		this.mProgrProtocollo = aModel.getEvento().getProgrProtocollo();
		// this.mDocBlob = aModel.getEvento().getDocBlob();
		this.mCodOperatoreInserimento = aModel.getEvento().getCodOperatoreInserimento();
		this.mDataInserimento = aModel.getEvento().getDataInserimento();
		this.mCodUfficioInserimento = aModel.getEvento().getCodUfficioInserimento();
		this.mDescrUfficioInserimento = aModel.getEvento().getDescrUfficioInserimento();
		this.mCodOperatoreAggiornamento = aModel.getEvento().getCodOperatoreAggiornamento();
		this.mDataAggiornamento = aModel.getEvento().getDataAggiornamento();
		this.mCodUfficioAggiornamento = aModel.getEvento().getCodUfficioAggiornamento();
		this.mDescrUfficioAggiornamento = aModel.getEvento().getDescrUfficioAggiornamento();
		this.mFasSieIdFascicoloSiep = aModel.getEvento().getFasSieIdFascicoloSiep();
		this.mFasSiuIdFascicoloSius = aModel.getEvento().getFasSiuIdFascicoloSius();
		this.mCodLuogoDestinatario = aModel.getEvento().getCodLuogoDestinatario();
		this.mDescrLuogoDestinatario = aModel.getEvento().getDescrLuogoDestinatario();
		this.mTenIdTenore = aModel.getEvento().getTenIdTenore();
		this.mEveIdEvento = aModel.getEvento().getEveIdEvento();
		this.mExistBlob = aModel.getEvento().getExistBlob();
		this.mCognomeSoggettoPresentante = aModel.getEvento().getCognomeSoggettoPresentante();
		this.mNomeSoggettoPresentante = aModel.getEvento().getNomeSoggettoPresentante();
		this.mFasSiuIdFascicoloSiusDest = aModel.getEvento().getFasSiuIdFascicoloSiusDest();
		this.mTemIdTemplate = aModel.getEvento().getTemIdTemplate();
		this.mFlagStampaSiep = aModel.getEvento().getFlagStampaSiep();
		this.mFlagStampaSius = aModel.getEvento().getFlagStampaSius();
		this.mFlagVideoSiep = aModel.getEvento().getFlagVideoSiep();
		this.mFlagVideoSius = aModel.getEvento().getFlagVideoSius();
		this.mNumAllegati = aModel.getEvento().getNumAllegati();
		this.mLegge = aModel.getEvento().getLegge();
		this.mAnnIdAnnotazioneManuale = aModel.getEvento().getAnnIdAnnotazioneManuale();
		this.mPenIdPenaResidua = aModel.getEvento().getPenIdPenaResidua();
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

	// public BigDecimal getFasSiuSogIdSoggetto() { return mFasSiuSogIdSoggetto; }
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

	public String getEventoCorrente() {
		return mEventoCorrente;
	}

	public int getNumAllegati() {
		return mNumAllegati;
	}

	public String getLegge() {
		return mLegge;
	}

	public Vector getVectAnnotazioneManuale() {
		return mVectAnnotazioneManuale;
	}

	public String getTotalePeriodoReclusione() {
		return mTotalePeriodoReclusione;
	}

	public String getTotalePeriodoArresto() {
		return mTotalePeriodoArresto;
	}

	public BigDecimal getAnnIdAnnotazioneManuale() {
		return mAnnIdAnnotazioneManuale;
	}

	public BigDecimal getPenIdPenaResidua() {
		return mPenIdPenaResidua;
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

	// public void setFasSiuSogIdSoggetto(BigDecimal aValore ) { mFasSiuSogIdSoggetto = aValore; }
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

	public void setEventoCorrente(String aValore) {
		mEventoCorrente = aValore;
	}

	public void setNumAllegati(int aValore) {
		mNumAllegati = aValore;
	}

	public void setLegge(String aValore) {
		mLegge = aValore;
	}

	public void setVectAnnotazioneManuale(Vector aValore) {
		mVectAnnotazioneManuale = aValore;
	}

	public void setTotalePeriodoReclusione(String aValore) {
		mTotalePeriodoReclusione = aValore;
	}

	public void setTotalePeriodoArresto(String aValore) {
		mTotalePeriodoArresto = aValore;
	}

	public void setAnnIdAnnotazioneManuale(BigDecimal aValore) {
		mAnnIdAnnotazioneManuale = aValore;
	}

	public void setPenIdPenaResidua(BigDecimal aValore) {
		mPenIdPenaResidua = aValore;
	}

	public void calcolaStringaTotPeriodiRecArr() {
		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalReclusione = new CalendarModel();
		CalendarModel lCalArresto = new CalendarModel();

		CalendarModel lCalRecTmp = new CalendarModel();
		CalendarModel lCalArrTmp = new CalendarModel();

		AnnotazioneManualeModel lAnnMan = null;

		for (Iterator i = mVectAnnotazioneManuale.iterator(); i.hasNext();) {
			lAnnMan = (AnnotazioneManualeModel) i.next();

			if (lAnnMan == null)
				continue;

			lCalRecTmp.setNumAnni(lAnnMan.getNumAnniReclusione());
			lCalRecTmp.setNumMesi(lAnnMan.getNumMesiReclusione());
			lCalRecTmp.setNumGiorni(lAnnMan.getNumGiorniReclusione());

			lCalReclusione = lCalUtil.sommaGiorni(lCalReclusione, lCalRecTmp);

			lCalArrTmp.setNumAnni(lAnnMan.getNumAnniArresto());
			lCalArrTmp.setNumMesi(lAnnMan.getNumMesiArresto());
			lCalArrTmp.setNumGiorni(lAnnMan.getNumGiorniArresto());

			lCalArresto = lCalUtil.sommaGiorni(lCalArresto, lCalArrTmp);

		}

		lCalReclusione = lCalUtil.ricalcolaGAM(lCalReclusione);
		lCalArresto = lCalUtil.ricalcolaGAM(lCalArresto);

		mTotalePeriodoReclusione = SiapStringUtil.formattaQuantum(lCalReclusione.getNumAnni(),
				lCalReclusione.getNumMesi(), lCalReclusione.getNumGiorni());
		mTotalePeriodoArresto = SiapStringUtil.formattaQuantum(lCalArresto.getNumAnni(),
				lCalArresto.getNumMesi(), lCalArresto.getNumGiorni());
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdEvento + " - " + mCodTipoEvento + " - " + mDescrTipoEvento + " - "
				+ mCodTipoProvvedimento + " - " + mDescrTipoProvvedimento + " - " + mCodMotivo + " - "
				+ mDescrMotivo + " - " + mCodUfficioEmittente + " - " + mDescrUfficioEmittente + " - "
				+ mCodLuogoEmittente + " - " + mDescrLuogoEmittente + " - " +
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
				+ " - " + mEventoCorrente + " - " + mFlagVideoSius + " - " + mLegge + " - " + mNumAllegati
				+ " - " + mAnnIdAnnotazioneManuale + " - " + mPenIdPenaResidua;

		return lStr;
	}

}