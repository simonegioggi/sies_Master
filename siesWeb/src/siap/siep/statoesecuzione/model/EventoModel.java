package siap.siep.statoesecuzione.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.model.GenericModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;

/**
 * Model per lo Stato Esecuzione
 * 
 * @author Giselda De Vita
 *
 */
@SuppressWarnings("rawtypes")
public class EventoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7646021410688812826L;

	private BigDecimal mIdEvento;
	private String mCodTipoEvento;
	private String mDescrTipoEvento;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private String mCodMotivo;
	private String mDescrMotivo;
	private String mCodUfficioEmittente;
	private String mDescrUfficioEmittente = null;
	private String mCodTipoUfficioEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente = null;
	private String mDescrizioneData;
	private String mDataInizioMisura;
	private String mDataFineMisura;
	private Date mDataTrasmisssioneAtti;
	private Date mData;
	private Date mDataArresto = null;
	private String mStringDataArresto = null;
	private String mAnnoUnione = null;
	private String mNumeroUnione = null;
	private String mUfficioUnione = null;
	private Date mDataUnione = null;

	private String mEventoCorrente = "N";
	private String mIsIstanza = null;
	private String mStringaAnnotazioniTotale = null;
	private String mStringaDopoProvvedimento = null;
	// 20/05/2014 Nuova L.A. - DL 146/2013
	private String mStringaDopoProvvedimentoLaspe = null;
	private String mStringaDopoProvvedimentoLaint = null;

	private String mLegge; // Paolo Cherubini 16/06/2011

	private Vector mLibAnticipate = null;

	private String mFraseUfficio = null;

	// MEV29 - Per visualizzazione corretta LA + periodi
	private Vector<DettaglioLAModel> mDettagliLibAnticipate = null;

	private FungibilitaModel mFungibilita = null;

	/**
	 * Attributo da utilizzare solo nel caso in cui si tratti di un'ordinanza/decreto SIUS senza alcun
	 * provveidmento SIEP associato
	 */
	private String mIsSius = null;

	// private UfficioModel mUfficio;

	// private TotalePeriodoModel mTotalePeriodo;

	private EventoSorveglianzaModel mEventoSorveglianzaModel;

	private Vector mAnnotazioniManuali;
	// private Vector mTenori;

	private String mNotifica1;
	private String mNotifica2;
	private String mNotifica3;

	private PenaResiduaModel mPenaResidua;
	private MisuraAlternativaModel mMisuraAlternativa;

	// private String mCognomeSoggettoPresentante;
	// private String mNomeSoggettoPresentante;
	private Date mDataEmissione;

	private String mCodEsito;
	private String mDescrEsito;
	private String mFlagPiuMeno;

	private String mStringaPenaResidua;
	private String mStringaPenaResiduaQuantum;
	private String mStringaMisuraDate;
	private String mStringaDecorrenzaPenaResidua;
	private String mStringLiberazioneAnticipata = null;
	//
	private String mStringPeriodoLiberazioneAnticipata = null;
	//
	private String mStringDetrazioneLiberazioneAnticipata = null;

	private String mStringaLA;
	private String mStringaRevocaDal;
	private String mFamiglia;

	// 20/05/2014 Nuova L.A. - DL 146/2013
	private String mStringLiberazioneAnticipataLA = null;
	private String mStringLiberazioneAnticipataLASpec = null;
	private String mStringLiberazioneAnticipataLAInt = null;
	private String mStringPeriodoLiberazioneAnticipataLA = null;
	private String mStringPeriodoLiberazioneAnticipataLASpec = null;
	private String mStringPeriodoLiberazioneAnticipataLAInt = null;

	private String mStringRisarcimentoDL92 = null;

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
		this.mCodLuogoEmittente = null;
		this.mDescrLuogoEmittente = null;
		// this.mCognomeSoggettoPresentante = null;
		// this.mNomeSoggettoPresentante = null;
		this.mDataEmissione = null;
		this.mCodEsito = null;
		this.mDescrEsito = null;
		this.mFlagPiuMeno = null;
		this.mCodTipoUfficioEmittente = null;
		this.mData = null; // STUB 21/10/2005 REWORK STATO ESECUZIONE.
		this.mDescrizioneData = null; // STUB 21/10/2005 REWORK STATO ESECUZIONE.
		this.mStringaDecorrenzaPenaResidua = null;
		this.mFamiglia = null;
		this.mDataTrasmisssioneAtti = null;
		this.mLegge = ""; // Paolo Cherubini 16/06/2011
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
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mFlagPiuMeno = aModel.mFlagPiuMeno;
		this.mData = aModel.mData;
		this.mDescrizioneData = aModel.mDescrizioneData;
		this.mStringaDecorrenzaPenaResidua = aModel.mStringaDecorrenzaPenaResidua;
		this.mFamiglia = aModel.mFamiglia;
		this.mDataTrasmisssioneAtti = aModel.mDataTrasmisssioneAtti;
	}

	// COSTRUTTORE DI COPIA
	public EventoModel(siap.sico.evento.model.EventoModel aModel) {
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
		this.mDataEmissione = aModel.getDataEmissione();
		this.mCodEsito = aModel.getCodEsito();
		this.mDescrEsito = aModel.getDescrEsito();
		this.mFlagPiuMeno = aModel.getFlagPiuMeno();
		this.mData = aModel.getData();
		this.mDescrizioneData = aModel.getDescrizioneData();
		this.mDataTrasmisssioneAtti = aModel.getDataTrasmissioneAtti();
	}

	/*
	 * Costruttore attraverso EventoNotificaModel public EventoModel(EventoNotificaModel aModel) {
	 * this(aModel.getEvento()); }
	 */

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

	public String getCodTipoUfficioEmittente() {
		return mCodTipoUfficioEmittente;
	}

	public Date getData() {
		return mData;
	}

	public String getDescrizioneData() {
		return mDescrizioneData;
	}

	public String getEventoCorrente() {
		return mEventoCorrente;
	}

	public String getStringaLA() {
		return mStringaLA;
	}

	public String getStringaMisuraDate() {
		return mStringaMisuraDate;
	}

	public String getStringaPenaResidua() {
		return mStringaPenaResidua;
	}

	public String getStringaPenaResiduaQuantum() {
		return mStringaPenaResiduaQuantum;
	}

	public String getStringaDecorrenzaPenaResidua() {
		return mStringaDecorrenzaPenaResidua;
	}

	public String getStringaRevocaDal() {
		return mStringaRevocaDal;
	}

	// Oggetti
	public EventoSorveglianzaModel getEventoSorveglianza() {
		return mEventoSorveglianzaModel;
	}

	public PenaResiduaModel getPenaResidua() {
		return mPenaResidua;
	}

	public MisuraAlternativaModel getMisuraAlternativa() {
		return mMisuraAlternativa;
	}

	public String getDataFineMisura() {
		return mDataFineMisura;
	}

	public String getMDataInizioMisura() {
		return mDataInizioMisura;
	}

	public String getLegge() {
		return mLegge;
	} // Paolo Cherubini 16/06/2011

	public Vector<DettaglioLAModel> getDettagliLibAnticipate() {
		return mDettagliLibAnticipate;
	}

	public FungibilitaModel getFungibilita() {
		return mFungibilita;
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

	public void setCodTipoUfficioEmittente(String aValore) {
		mCodTipoUfficioEmittente = aValore;
	}

	public void setDescrizioneData(String aValore) {
		mDescrizioneData = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setStringaLA(String stringaLA) {
		mStringaLA = stringaLA;
	}

	public void setStringaMisuraDate(String stringaMisuraDate) {
		mStringaMisuraDate = stringaMisuraDate;
	}

	public void setStringaPenaResidua(String stringaPenaResidua) {
		mStringaPenaResidua = stringaPenaResidua;
	}

	public void setStringaPenaResiduaQuantum(String stringaPenaResiduaQuantum) {
		mStringaPenaResiduaQuantum = stringaPenaResiduaQuantum;
	}

	public void setStringaDecorrenzaPenaResidua(String stringaDecorrenzaPenaResidua) {
		mStringaDecorrenzaPenaResidua = stringaDecorrenzaPenaResidua;
	}

	public void setStringaRevocaDal(String stringaRevocaDal) {
		mStringaRevocaDal = stringaRevocaDal;
	}

	// Oggetti
	public void setEventoSorveglianza(EventoSorveglianzaModel aValore) {
		mEventoSorveglianzaModel = aValore;
	}

	public void setPenaResidua(PenaResiduaModel aValore) {
		mPenaResidua = aValore;
	}

	public void setMisuraAlternativa(MisuraAlternativaModel aValore) {
		mMisuraAlternativa = aValore;
	}

	public void setLegge(String aValore) {
		mLegge = aValore;
	} // Paolo Cherubini 16/06/2011

	public void setDataFineMisura(String dataFineMisura) {
		mDataFineMisura = dataFineMisura;
	}

	public void setMDataInizioMisura(String dataInizioMisura) {
		mDataInizioMisura = dataInizioMisura;
	}

	public void setDettagliLibAnticipate(Vector<DettaglioLAModel> aListaDettagli) {
		mDettagliLibAnticipate = aListaDettagli;
	}

	public void setFungibilita(FungibilitaModel aFungibilita) {
		mFungibilita = aFungibilita;
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
				+ "[ mData                       = " + mData + " ]\n" + "[ mDescrizioneData            = "
				+ mDescrizioneData + " ]\n" + "[ mDataFineMisura             = " + mDataFineMisura + " ]\n"
				+ "[ mStringDataArresto             = " + mStringDataArresto + " ]\n"
				+ "[ mLegge                      = " + mLegge + " ]\n" + // Paolo Cherubini 16/06/2011
				"[ mDataInizioMisura           = " + mDataInizioMisura + " ]\n";

		return lStr;
	}

	public String getIsIstanza() {
		return mIsIstanza;
	}

	public void setIsIstanza(String isIstanza) {
		mIsIstanza = isIstanza;
	}

	public String getIsSius() {
		return mIsSius;
	}

	public void setIsSius(String isSius) {
		mIsSius = isSius;
	}

	public Vector getLibAnticipate() {
		return mLibAnticipate;
	}

	public void setLibAnticipate(Vector libAnticipate) {
		mLibAnticipate = libAnticipate;
	}

	public String getFamiglia() {
		return mFamiglia;
	}

	public void setFamiglia(String famiglia) {
		mFamiglia = famiglia;
	}

	public Date getDataTrasmisssioneAtti() {
		return mDataTrasmisssioneAtti;
	}

	public void setDataTrasmisssioneAtti(Date dataTrasmisssioneAtti) {
		mDataTrasmisssioneAtti = dataTrasmisssioneAtti;
	}

	public String getAnnoUnione() {
		return mAnnoUnione;
	}

	public void setAnnoUnione(String annoUnione) {
		mAnnoUnione = annoUnione;
	}

	public Date getDataUnione() {
		return mDataUnione;
	}

	public void setDataUnione(Date dataUnione) {
		mDataUnione = dataUnione;
	}

	public Date getDataArresto() {
		return mDataArresto;
	}

	public void setDataArresto(Date dataArresto) {
		mDataArresto = dataArresto;
	}

	public String getNumeroUnione() {
		return mNumeroUnione;
	}

	public void setNumeroUnione(String numeroUnione) {
		mNumeroUnione = numeroUnione;
	}

	public String getUfficioUnione() {
		return mUfficioUnione;
	}

	public void setUfficioUnione(String ufficioUnione) {
		mUfficioUnione = ufficioUnione;
	}

	public String getFraseUfficio() {
		return mFraseUfficio;
	}

	public void setFraseUfficio(String fraseUfficio) {
		mFraseUfficio = fraseUfficio;
	}

	public String getStringLiberazioneAnticipata() {
		return mStringLiberazioneAnticipata;
	}

	public void setStringLiberazioneAnticipata(String stringLiberazioneAnticipata) {
		mStringLiberazioneAnticipata = stringLiberazioneAnticipata;
	}

	// 20/05/2014 Nuova L.A. - DL 146/2013
	public String getStringPeriodoLiberazioneAnticipata() {
		return mStringPeriodoLiberazioneAnticipata;
	}

	public void setStringPeriodoLiberazioneAnticipata(String stringPeriodoLiberazioneAnticipata) {
		mStringPeriodoLiberazioneAnticipata = stringPeriodoLiberazioneAnticipata;
	}

	// periodo L.A.
	public String getStringPeriodoLiberazioneAnticipataLA() {
		return mStringPeriodoLiberazioneAnticipataLA;
	}

	public void setStringPeriodoLiberazioneAnticipataLA(String stringPeriodoLiberazioneAnticipataLA) {
		mStringPeriodoLiberazioneAnticipataLA = stringPeriodoLiberazioneAnticipataLA;
	}

	// periodo L.A. SPECIALE
	public String getStringPeriodoLiberazioneAnticipataLASPE() {
		return mStringPeriodoLiberazioneAnticipataLASpec;
	}

	public void setStringPeriodoLiberazioneAnticipataLASPE(String stringPeriodoLiberazioneAnticipataLAS) {
		mStringPeriodoLiberazioneAnticipataLASpec = stringPeriodoLiberazioneAnticipataLAS;
	}

	// periodo L.A. INTEGRAZIONE
	public String getStringPeriodoLiberazioneAnticipataLAINT() {
		return mStringPeriodoLiberazioneAnticipataLAInt;
	}

	public void setStringPeriodoLiberazioneAnticipataLAINT(String stringPeriodoLiberazioneAnticipataLAI) {
		mStringPeriodoLiberazioneAnticipataLAInt = stringPeriodoLiberazioneAnticipataLAI;
	}

	// Giorni L.A.
	public String getStringLiberazioneAnticipataLA() {
		return mStringLiberazioneAnticipataLA;
	}

	public void setStringLiberazioneAnticipataLA(String stringNewLiberazioneAnticipata) {
		mStringLiberazioneAnticipataLA = stringNewLiberazioneAnticipata;
	}

	// Giorni L.A. SPECIALE
	public String getStringLiberazioneAnticipataLASpec() {
		return mStringLiberazioneAnticipataLASpec;
	}

	public void setStringLiberazioneAnticipataLASpec(String stringNewLiberazioneAnticipataSpe) {
		mStringLiberazioneAnticipataLASpec = stringNewLiberazioneAnticipataSpe;
	}

	// Giorni L.A. INTEGRAZIONE
	public String getStringLiberazioneAnticipataLAInt() {
		return mStringLiberazioneAnticipataLAInt;
	}

	public void setStringLiberazioneAnticipataLAInt(String stringNewLiberazioneAnticipataI) {
		mStringLiberazioneAnticipataLAInt = stringNewLiberazioneAnticipataI;
	}

	public String getStringRisarcimentoDL92() {
		return mStringRisarcimentoDL92;
	}

	public void setStringRisarcimentoDL92(String aValore) {
		mStringRisarcimentoDL92 = aValore;
	}

	// END L.A.
	public String getNotifica1() {
		return mNotifica1;
	}

	public void setNotifica1(String notifica1) {
		mNotifica1 = notifica1;
	}

	public String getNotifica2() {
		return mNotifica2;
	}

	public void setNotifica2(String notifica2) {
		mNotifica2 = notifica2;
	}

	public String getStringDetrazioneLiberazioneAnticipata() {
		return mStringDetrazioneLiberazioneAnticipata;
	}

	public void setStringDetrazioneLiberazioneAnticipata(String stringDetrazioneLiberazioneAnticipata) {
		mStringDetrazioneLiberazioneAnticipata = stringDetrazioneLiberazioneAnticipata;
	}

	public String getStringDataArresto() {
		return mStringDataArresto;
	}

	public void setStringDataArresto(String stringStringDataArresto) {
		mStringDataArresto = stringStringDataArresto;
	}

	public Vector getAnnotazioniManuali() {
		return mAnnotazioniManuali;
	}

	public void setAnnotazioniManuali(Vector annotazioniManuali) {
		mAnnotazioniManuali = annotazioniManuali;
	}

	public String getStringaAnnotazioniTotale() {
		return mStringaAnnotazioniTotale;
	}

	public void setStringaAnnotazioniTotale(String stringaAnnotazioniTotale) {
		mStringaAnnotazioniTotale = stringaAnnotazioniTotale;
	}

	public String getStringaDopoProvvedimento() {
		return mStringaDopoProvvedimento;
	}

	public void setStringaDopoProvvedimento(String stringaDopoProvvedimento) {
		mStringaDopoProvvedimento = stringaDopoProvvedimento;
	}

	// 20/05/2014 Nuova L.A. - DL 146/2013
	public String getStringaDopoProvvedimentoLASPE() {
		return mStringaDopoProvvedimentoLaspe;
	}

	public void setStringaDopoProvvedimentoLASPE(String stringaDopoProvvedimentospe) {
		mStringaDopoProvvedimentoLaspe = stringaDopoProvvedimentospe;
	}

	public String getStringaDopoProvvedimentoLAINT() {
		return mStringaDopoProvvedimentoLaint;
	}

	public void setStringaDopoProvvedimentoLAINT(String stringaDopoProvvedimentoint) {
		mStringaDopoProvvedimentoLaint = stringaDopoProvvedimentoint;
	}
	// End DL 146

	public String getNotifica3() {
		return mNotifica3;
	}

	public void setNotifica3(String notifica3) {
		mNotifica3 = notifica3;
	}

}