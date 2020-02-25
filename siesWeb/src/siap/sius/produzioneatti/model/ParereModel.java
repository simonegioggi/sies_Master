package siap.sius.produzioneatti.model;

/**
* <p>Title: ParereModel</p>
* <p>Description: Classe Model che rappresenta l'informazione "Richiesta Parere su Procedimento SIUS".</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class ParereModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -5950021050253455542L;
	// Dati provenienti da FASCICOLO_SIUS
	private BigDecimal mIdFascicoloSius;
	private BigDecimal mAnnoFas;
	private BigDecimal mProgrFas;

	// Dati provenienti da SOGGETTO
	private BigDecimal mIdSoggetto;
	private String mCognome;
	private String mNome;
	private Date mDataNascita;

	// Dati provenienti da EVENTO
	private BigDecimal mIdEvento;
	private String mCodTipoEvento;
	private String mDescrTipoEvento;
	private String mCodMotivo;
	private String mDescrMotivo;
	private String mCodEsito;
	private String mDescrEsito;
	private Date mDataEmissione;
	private Date mDataTrasmissioneAtti;
	private Date mDataRicezioneAtti;
	private String mCodUfficioEmittente;

	// Dati provenienti da GENERALE_PROCEDIMENTO
	private BigDecimal mIdGeneraleProcedimento;
	private String mCodOggettoProcedimento;
	private String mDescrOggettoProcedimento;

	// Utilizzata per definire intervallo date
	private Date mDataEmissione2;

	// 27/03/2007 Utilizzato per filtro Codice Utente.
	private String mCodiceUtente;

	// COSTRUTTORE DI DEFAULT
	public ParereModel() {
		mIdFascicoloSius = null;
		mAnnoFas = null;
		mProgrFas = null;
		mIdSoggetto = null;
		mCognome = "";
		mNome = "";
		mDataNascita = null;
		mIdEvento = null;
		mCodTipoEvento = "";
		mDescrTipoEvento = "";
		mCodMotivo = "";
		mDescrMotivo = "";
		mCodEsito = "";
		mDescrEsito = "";
		mDataEmissione = null;
		mDataTrasmissioneAtti = null;
		mDataRicezioneAtti = null;
		mCodUfficioEmittente = "";
		mIdGeneraleProcedimento = null;
		mCodOggettoProcedimento = "";
		mDescrOggettoProcedimento = "";
		mDataEmissione2 = null;
		mCodiceUtente = "";

	}

	// COSTRUTTORE DI COPIA
	public ParereModel(ParereModel aModel) {
		mIdFascicoloSius = aModel.mIdFascicoloSius;
		mAnnoFas = aModel.mAnnoFas;
		mProgrFas = aModel.mProgrFas;
		mIdSoggetto = aModel.mIdSoggetto;
		mCognome = aModel.mCognome;
		mNome = aModel.mNome;
		mDataNascita = aModel.mDataNascita;
		mIdEvento = aModel.mIdEvento;
		mCodTipoEvento = aModel.mCodTipoEvento;
		mDescrTipoEvento = aModel.mDescrTipoEvento;
		mCodMotivo = aModel.mCodMotivo;
		mDescrMotivo = aModel.mDescrMotivo;
		mCodEsito = aModel.mCodEsito;
		mDescrEsito = aModel.mDescrEsito;
		mDataEmissione = aModel.mDataEmissione;
		mDataTrasmissioneAtti = aModel.mDataTrasmissioneAtti;
		mDataRicezioneAtti = aModel.mDataRicezioneAtti;
		mCodUfficioEmittente = aModel.mCodUfficioEmittente;
		mIdGeneraleProcedimento = aModel.mIdGeneraleProcedimento;
		mCodOggettoProcedimento = aModel.mCodOggettoProcedimento;
		mDescrOggettoProcedimento = aModel.mDescrOggettoProcedimento;
		mDataEmissione2 = aModel.mDataEmissione2;
		mCodiceUtente = aModel.mCodiceUtente;

	}

	// COSTRUTTORE MODEL
	public ParereModel(BigDecimal aIdFascicoloSius, BigDecimal aAnnoFas, BigDecimal aProgrFas,
			BigDecimal aIdSoggetto, String aCognome, String aNome, Date aDataNascita, BigDecimal aIdEvento,
			String aCodTipoEvento, String aDescrTipoEvento, String aCodMotivo, String aDescrMotivo,
			String aCodEsito, String aDescrEsito, Date aDataEmissione, Date aDataTrasmissioneAtti,
			Date aDataRicezioneAtti, String aCodUfficioEmittente, BigDecimal aIdGeneraleProcedimento,
			String aCodOggettoProcedimento, String aDescrOggettoProcedimento, Date aDataEmissione2,
			String aCodiceUtente) {
		mIdFascicoloSius = aIdFascicoloSius;
		mAnnoFas = aAnnoFas;
		mProgrFas = aProgrFas;
		mIdSoggetto = aIdSoggetto;
		mCognome = aCognome;
		mNome = aNome;
		mDataNascita = aDataNascita;
		mIdEvento = aIdEvento;
		mCodTipoEvento = aCodTipoEvento;
		mDescrTipoEvento = aDescrTipoEvento;
		mCodMotivo = aCodMotivo;
		mDescrMotivo = aDescrMotivo;
		mCodEsito = aCodEsito;
		mDescrEsito = aDescrEsito;
		mDataEmissione = aDataEmissione;
		mDataTrasmissioneAtti = aDataTrasmissioneAtti;
		mDataRicezioneAtti = aDataRicezioneAtti;
		mCodUfficioEmittente = aCodUfficioEmittente;
		mIdGeneraleProcedimento = aIdGeneraleProcedimento;
		mCodOggettoProcedimento = aCodOggettoProcedimento;
		mDescrOggettoProcedimento = aDescrOggettoProcedimento;
		mDataEmissione2 = aDataEmissione2;
		mCodiceUtente = aCodiceUtente;

	}

	//
	// METODI GET()
	//
	public BigDecimal getIdFascicoloSius() {
		return mIdFascicoloSius;
	}

	public BigDecimal getAnnoFascicoloSius() {
		return mAnnoFas;
	}

	public BigDecimal getProgrFascicoloSius() {
		return mProgrFas;
	}

	public BigDecimal getIdSoggetto() {
		return mIdSoggetto;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public Date getDataNascita() {
		return mDataNascita;
	}

	public BigDecimal getIdEvento() {
		return mIdEvento;
	}

	public String getCodTipoEvento() {
		return mCodTipoEvento;
	}

	public String getDescrTipoEvento() {
		return mDescrTipoEvento;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

	public String getDescrMotivo() {
		return mDescrMotivo;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public Date getDataTrasmissioneAtti() {
		return mDataTrasmissioneAtti;
	}

	public Date getDataRicezioneAtti() {
		return mDataRicezioneAtti;
	}

	public String getCodUfficioEmittente() {
		return mCodUfficioEmittente;
	}

	public BigDecimal getIdGeneraleProcedimento() {
		return mIdGeneraleProcedimento;
	}

	public String getCodOggettoProcedimento() {
		return mCodOggettoProcedimento;
	}

	public String getDescrOggettoProcedimento() {
		return mDescrOggettoProcedimento;
	}

	public Date getDataEmissione2() {
		return mDataEmissione2;
	}

	public String getCodiceUtente() {
		return mCodiceUtente;
	}

	//
	// METODI SET()
	//
	public void setIdFascicoloSius(BigDecimal aValore) {
		mIdFascicoloSius = aValore;
	}

	public void setAnnoFascicoloSius(BigDecimal aValore) {
		mAnnoFas = aValore;
	}

	public void setProgrFascicoloSius(BigDecimal aValore) {
		mProgrFas = aValore;
	}

	public void setIdSoggetto(BigDecimal aValore) {
		mIdSoggetto = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public void setIdEvento(BigDecimal aValore) {
		mIdEvento = aValore;
	}

	public void setCodTipoEvento(String aValore) {
		mCodTipoEvento = aValore;
	}

	public void setDescrTipoEvento(String aValore) {
		mDescrTipoEvento = aValore;
	}

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

	public void setDescrMotivo(String aValore) {
		mDescrMotivo = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setDataTrasmissioneAtti(Date aValore) {
		mDataTrasmissioneAtti = aValore;
	}

	public void setDataRicezioneAtti(Date aValore) {
		mDataRicezioneAtti = aValore;
	}

	public void setCodUfficioEmittente(String aValore) {
		mCodUfficioEmittente = aValore;
	}

	public void setIdGeneraleProcedimento(BigDecimal aValore) {
		mIdGeneraleProcedimento = aValore;
	}

	public void setCodOggettoProcedimento(String aValore) {
		mCodOggettoProcedimento = aValore;
	}

	public void setDescrOggettoProcedimento(String aValore) {
		mDescrOggettoProcedimento = aValore;
	}

	public void setDataEmissione2(Date aValore) {
		mDataEmissione2 = aValore;
	}

	public void setCodiceUtente(String aValore) {
		mCodiceUtente = aValore;
	}

	@Override
	public String toString() {
		String lToString = mIdFascicoloSius + " - " + mAnnoFas + " - " + mProgrFas + " - " + mIdSoggetto
				+ " - " + mCognome + " - " + mNome + " - " + mDataNascita + " - " + mIdEvento + " - "
				+ mCodTipoEvento + " - " + mDescrTipoEvento + " - " + mCodMotivo + " - " + mDescrMotivo
				+ " - " + mCodEsito + " - " + mDescrEsito + " - " + mDataEmissione + " - "
				+ mDataTrasmissioneAtti + " - " + mDataRicezioneAtti + " - " + mIdGeneraleProcedimento + " - "
				+ mCodOggettoProcedimento + " - " + mDescrOggettoProcedimento + " - " + mDataEmissione2
				+ " - " + mCodiceUtente;
		return lToString;
	}
}
