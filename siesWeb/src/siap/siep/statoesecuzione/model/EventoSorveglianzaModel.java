package siap.siep.statoesecuzione.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.model.GenericModel;

/**
 * EventoSorveglianzaModel
 *
 * @author Giselda De Vita
 *
 */
@SuppressWarnings("rawtypes")
public class EventoSorveglianzaModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 9025380685005635790L;

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
	private String mDescrizioneData; // STUB 21/10/2005 REWORK STATO ESECUZIONE.
	private Date mData; // STUB 21/10/2005 REWORK STATO ESECUZIONE.

	private BigDecimal mNumeroRegistro = null;
	private BigDecimal mAnnoRegistro = null;
	private String mFlagElaborato = null;

	// private UfficioModel mUfficio;
	private Vector mTenori = null;
	private Vector mPeriodiLA;

	public EventoSorveglianzaModel() {
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
		this.mCodTipoUfficioEmittente = null;
		this.mData = null; // STUB 21/10/2005 REWORK STATO ESECUZIONE.
		this.mDescrizioneData = null; // STUB 21/10/2005 REWORK STATO ESECUZIONE.
		this.mAnnoRegistro = null;
		this.mNumeroRegistro = null;
		this.mFlagElaborato = null;
	}

	public EventoSorveglianzaModel(EventoSorveglianzaModel aModel) {
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
		this.mData = aModel.mData;
		this.mDescrizioneData = aModel.mDescrizioneData;
		this.mAnnoRegistro = aModel.mAnnoRegistro;
		this.mNumeroRegistro = aModel.mNumeroRegistro;
		this.mFlagElaborato = aModel.mFlagElaborato;
		// 20190920 [SG]: aggiunto set di proprietà post collaudo 11.3
		this.mCodTipoUfficioEmittente = aModel.getCodTipoUfficioEmittente();
	}

	// COSTRUTTORE DI COPIA
	public EventoSorveglianzaModel(siap.sico.evento.model.EventoModel aModel) {
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
		this.mData = aModel.getDataEmissione();
		this.mDescrizioneData = aModel.getDescrizioneData();
		this.mAnnoRegistro = null;
		this.mNumeroRegistro = null;
		this.mFlagElaborato = null;
		// 20190920 [SG]: aggiunto set di proprietà post collaudo 11.3
		this.mCodTipoUfficioEmittente = aModel.getCodTipoUfficioEmittente();
	}

	/**
	 * Motodi Get sugli attibuti
	 *
	 * @return
	 */

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

	public String getCodTipoUfficioEmittente() {
		return mCodTipoUfficioEmittente;
	}

	public Date getData() {
		return mData;
	}

	public String getDescrizioneData() {
		return mDescrizioneData;
	}

	public BigDecimal getAnnoRegistro() {
		return mAnnoRegistro;
	}

	public BigDecimal getNumeroRegistro() {
		return mNumeroRegistro;
	}

	public String getFlagElaborato() {
		return mFlagElaborato;
	}

	/**
	 * Metosdi Set sugli attributi
	 *
	 * @param aValore
	 */
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

	public void setCodTipoUfficioEmittente(String aValore) {
		mCodTipoUfficioEmittente = aValore;
	}

	public void setDescrizioneData(String aValore) {
		mDescrizioneData = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setAnnoRegistro(BigDecimal annoRegistro) {
		mAnnoRegistro = annoRegistro;
	}

	public void setNumeroRegistro(BigDecimal numeroRegistro) {
		mNumeroRegistro = numeroRegistro;
	}

	public void setFlagElaborato(String flagelaborato) {
		mFlagElaborato = flagelaborato;
	}

	public Vector getTenori() {
		return mTenori;
	}

	public void setTenori(Vector tenori) {
		mTenori = tenori;
	}

	public Vector getPeriodiLA() {
		return mPeriodiLA;
	}

	public void setPeriodiLA(Vector periodiLA) {
		mPeriodiLA = periodiLA;
	}

}