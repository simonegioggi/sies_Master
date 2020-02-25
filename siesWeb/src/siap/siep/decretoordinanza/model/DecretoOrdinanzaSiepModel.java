package siap.siep.decretoordinanza.model;

/**
* <p>Title: DecretoOrdinanzaSiepModel</p>
* <p>Description: Classe Model che rappresenta il DecretoOrdinanzaSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class DecretoOrdinanzaSiepModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5788896315699288326L;

	private BigDecimal mIdDecretoOrdinanzaSiep;
	private Date mDataRicezioneProvvedimento;
	private Date mDataEmissioneProvvedimento;
	private String mCodTipoRegistroOrdinanza;
	private String mDescrTipoRegistroOrdinanza;
	private BigDecimal mAnnoRegistro;
	private BigDecimal mNumRegistro;
	private BigDecimal mAnnoProvvedimento;
	private BigDecimal mNumProvvedimento;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private Date mDataSospensioneEsecuzione;
	private Date mDataDifferimento;
	private Date mDataRinvio;
	private Date mDataFineInterruzione;
	private Date mDataDepositoIstanza;
	private Date mDataInterruzionePena;
	private String mCodOggettoDecisione;
	private String mDescrOggettoDecisione;
	private String mMotivazioni;
	private String mNote;
	private String mFlagScarcerareScarcerato;
	private String mFlagPresentanteIstanza;
	private String mCodContenutoDecreto;
	private String mDescrContenutoDecreto;
	private String mCodOggettoProcedimento;
	private String mDescrOggettoProcedimento;
	private String mFlagDataInterruzioneInvalid;
	private String mProtocollo;
	private String mAltraAutorita;
	private String mAltroLuogo;
	private BigDecimal mIdEventoGenerato;
	private String mFlagElaborato;
	private Date mDataEspulsione;
	private String mFlagDecisioneTribunale;
	private String mCodEsito;
	private String mDescrEsito;
	private BigDecimal mNumAnniRinvio;
	private BigDecimal mNumMesiRinvio;
	private BigDecimal mNumGiorniRinvio;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private Date mDataInserimento;
	private String mCodOperatoreAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private Date mDataAggiornamento;
	private Date mDataRevocaSospensione;
	private BigDecimal mFasSieIdFascicoloSiep;
	private String mStringaRinvio;
	// 01-09-2015 - MEV_2 (Misure Sicurezza) STRP_2
	private BigDecimal mAnno_Reg_Gen;
	private BigDecimal mNumero_Reg_Gen;
	private String mTipo_Reg_Gen;

	// COSTRUTTORE DI DEFAULT
	public DecretoOrdinanzaSiepModel() {
		this.mIdDecretoOrdinanzaSiep = null;
		this.mDataRicezioneProvvedimento = null;
		this.mDataEmissioneProvvedimento = null;
		this.mCodTipoRegistroOrdinanza = "";
		this.mDescrTipoRegistroOrdinanza = "";
		this.mAnnoRegistro = null;
		this.mNumRegistro = null;
		this.mAnnoProvvedimento = null;
		this.mNumProvvedimento = null;
		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mCodTipoAutoritaEmittente = "";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";
		this.mDataSospensioneEsecuzione = null;
		this.mDataDifferimento = null;
		this.mDataRinvio = null;
		this.mDataFineInterruzione = null;
		this.mDataDepositoIstanza = null;
		this.mDataInterruzionePena = null;
		this.mCodOggettoDecisione = "";
		this.mDescrOggettoDecisione = "";
		this.mMotivazioni = "";
		this.mNote = "";
		this.mFlagScarcerareScarcerato = "";
		this.mFlagPresentanteIstanza = "";
		this.mCodContenutoDecreto = "";
		this.mDescrContenutoDecreto = "";
		this.mCodOggettoProcedimento = "";
		this.mDescrOggettoProcedimento = "";
		this.mFlagDataInterruzioneInvalid = "";
		this.mProtocollo = "";
		this.mAltraAutorita = "";
		this.mAltroLuogo = "";
		this.mIdEventoGenerato = null;
		this.mFlagElaborato = "";
		this.mDataEspulsione = null;
		this.mFlagDecisioneTribunale = "";
		this.mCodEsito = "";
		this.mDescrEsito = "";
		this.mNumAnniRinvio = null;
		this.mNumMesiRinvio = null;
		this.mNumGiorniRinvio = null;
		this.mCodOperatoreInserimento = "";
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mDataInserimento = null;
		this.mCodOperatoreAggiornamento = "";
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mDataRevocaSospensione = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mAnno_Reg_Gen = null;
		this.mNumero_Reg_Gen = null;
		this.mTipo_Reg_Gen = null;
	}

	// COSTRUTTORE DI COPIA
	public DecretoOrdinanzaSiepModel(DecretoOrdinanzaSiepModel aModel) {
		this.mIdDecretoOrdinanzaSiep = aModel.mIdDecretoOrdinanzaSiep;
		this.mDataRicezioneProvvedimento = aModel.mDataRicezioneProvvedimento;
		this.mDataEmissioneProvvedimento = aModel.mDataEmissioneProvvedimento;
		this.mCodTipoRegistroOrdinanza = aModel.mCodTipoRegistroOrdinanza;
		this.mDescrTipoRegistroOrdinanza = aModel.mDescrTipoRegistroOrdinanza;
		this.mAnnoRegistro = aModel.mAnnoRegistro;
		this.mNumRegistro = aModel.mNumRegistro;
		this.mAnnoProvvedimento = aModel.mAnnoProvvedimento;
		this.mNumProvvedimento = aModel.mNumProvvedimento;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mDataSospensioneEsecuzione = aModel.mDataSospensioneEsecuzione;
		this.mDataDifferimento = aModel.mDataDifferimento;
		this.mDataRinvio = aModel.mDataRinvio;
		this.mDataFineInterruzione = aModel.mDataFineInterruzione;
		this.mDataDepositoIstanza = aModel.mDataDepositoIstanza;
		this.mDataInterruzionePena = aModel.mDataInterruzionePena;
		this.mCodOggettoDecisione = aModel.mCodOggettoDecisione;
		this.mDescrOggettoDecisione = aModel.mDescrOggettoDecisione;
		this.mMotivazioni = aModel.mMotivazioni;
		this.mNote = aModel.mNote;
		this.mFlagScarcerareScarcerato = aModel.mFlagScarcerareScarcerato;
		this.mFlagPresentanteIstanza = aModel.mFlagPresentanteIstanza;
		this.mCodContenutoDecreto = aModel.mCodContenutoDecreto;
		this.mDescrContenutoDecreto = aModel.mDescrContenutoDecreto;
		this.mCodOggettoProcedimento = aModel.mCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aModel.mDescrOggettoProcedimento;
		this.mFlagDataInterruzioneInvalid = aModel.mFlagDataInterruzioneInvalid;
		this.mProtocollo = aModel.mProtocollo;
		this.mAltraAutorita = aModel.mAltraAutorita;
		this.mAltroLuogo = aModel.mAltroLuogo;
		this.mIdEventoGenerato = aModel.mIdEventoGenerato;
		this.mFlagElaborato = aModel.mFlagElaborato;
		this.mDataEspulsione = aModel.mDataEspulsione;
		this.mFlagDecisioneTribunale = aModel.mFlagDecisioneTribunale;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mNumAnniRinvio = aModel.mNumAnniRinvio;
		this.mNumMesiRinvio = aModel.mNumMesiRinvio;
		this.mNumGiorniRinvio = aModel.mNumGiorniRinvio;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mDataRevocaSospensione = aModel.mDataRevocaSospensione;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mAnno_Reg_Gen = aModel.mAnno_Reg_Gen;
		this.mNumero_Reg_Gen = aModel.mNumero_Reg_Gen;
		this.mTipo_Reg_Gen = aModel.mTipo_Reg_Gen;
	}

	// COSTRUTTORE MODEL
	public DecretoOrdinanzaSiepModel(BigDecimal aIdDecretoOrdinanzaSiep, Date aDataRicezioneProvvedimento,
			Date aDataEmissioneProvvedimento, String aCodTipoRegistroOrdinanza,
			String aDescrTipoRegistroOrdinanza, BigDecimal aAnnoRegistro, BigDecimal aNumRegistro,
			BigDecimal aAnnoProvvedimento, BigDecimal aNumProvvedimento, String aCodTipoProvvedimento,
			String aDescrTipoProvvedimento, String aCodTipoAutoritaEmittente,
			String aDescrTipoAutoritaEmittente, String aCodLuogoEmittente, String aDescrLuogoEmittente,
			Date aDataSospensioneEsecuzione, Date aDataDifferimento, Date aDataRinvio,
			Date aDataFineInterruzione, Date aDataDepositoIstanza, Date aDataInterruzionePena,
			String aCodOggettoDecisione, String aDescrOggettoDecisione, String aMotivazioni, String aNote,
			String aFlagScarcerareScarcerato, String aFlagPresentanteIstanza, String aCodContenutoDecreto,
			String aDescrContenutoDecreto, String aCodOggettoProcedimento, String aDescrOggettoProcedimento,
			String aFlagDataInterruzioneInvalid, String aProtocollo, String aAltraAutorita,
			String aAltroLuogo, BigDecimal aIdEventoGenerato, String aFlagElaborato, Date aDataEspulsione,
			String aFlagDecisioneTribunale, String aCodEsito, String aDescrEsito, BigDecimal aNumAnniRinvio,
			BigDecimal aNumMesiRinvio, BigDecimal aNumGiorniRinvio, String aCodOperatoreInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, Date aDataInserimento,
			String aCodOperatoreAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, Date aDataAggiornamento, Date aDataRevocaSospensione,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aAnno_Reg_Gen, BigDecimal aNumero_Reg_Gen,
			String aTipo_Reg_Gen) {
		this.mIdDecretoOrdinanzaSiep = aIdDecretoOrdinanzaSiep;
		this.mDataRicezioneProvvedimento = aDataRicezioneProvvedimento;
		this.mDataEmissioneProvvedimento = aDataEmissioneProvvedimento;
		this.mCodTipoRegistroOrdinanza = aCodTipoRegistroOrdinanza;
		this.mDescrTipoRegistroOrdinanza = aDescrTipoRegistroOrdinanza;
		this.mAnnoRegistro = aAnnoRegistro;
		this.mNumRegistro = aNumRegistro;
		this.mAnnoProvvedimento = aAnnoProvvedimento;
		this.mNumProvvedimento = aNumProvvedimento;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mDataSospensioneEsecuzione = aDataSospensioneEsecuzione;
		this.mDataDifferimento = aDataDifferimento;
		this.mDataRinvio = aDataRinvio;
		this.mDataFineInterruzione = aDataFineInterruzione;
		this.mDataDepositoIstanza = aDataDepositoIstanza;
		this.mDataInterruzionePena = aDataInterruzionePena;
		this.mCodOggettoDecisione = aCodOggettoDecisione;
		this.mDescrOggettoDecisione = aDescrOggettoDecisione;
		this.mMotivazioni = aMotivazioni;
		this.mNote = aNote;
		this.mFlagScarcerareScarcerato = aFlagScarcerareScarcerato;
		this.mFlagPresentanteIstanza = aFlagPresentanteIstanza;
		this.mCodContenutoDecreto = aCodContenutoDecreto;
		this.mDescrContenutoDecreto = aDescrContenutoDecreto;
		this.mCodOggettoProcedimento = aCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aDescrOggettoProcedimento;
		this.mFlagDataInterruzioneInvalid = aFlagDataInterruzioneInvalid;
		this.mProtocollo = aProtocollo;
		this.mAltraAutorita = aAltraAutorita;
		this.mAltroLuogo = aAltroLuogo;
		this.mIdEventoGenerato = aIdEventoGenerato;
		this.mFlagElaborato = aFlagElaborato;
		this.mDataEspulsione = aDataEspulsione;
		this.mFlagDecisioneTribunale = aFlagDecisioneTribunale;
		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mNumAnniRinvio = aNumAnniRinvio;
		this.mNumMesiRinvio = aNumMesiRinvio;
		this.mNumGiorniRinvio = aNumGiorniRinvio;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mDataRevocaSospensione = aDataRevocaSospensione;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mAnno_Reg_Gen = aAnno_Reg_Gen;
		this.mNumero_Reg_Gen = aNumero_Reg_Gen;
		this.mTipo_Reg_Gen = aTipo_Reg_Gen;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdDecretoOrdinanzaSiep() {
		return mIdDecretoOrdinanzaSiep;
	}

	public Date getDataRicezioneProvvedimento() {
		return mDataRicezioneProvvedimento;
	}

	public Date getDataEmissioneProvvedimento() {
		return mDataEmissioneProvvedimento;
	}

	public String getCodTipoRegistroOrdinanza() {
		return mCodTipoRegistroOrdinanza;
	}

	public String getDescrTipoRegistroOrdinanza() {
		return mDescrTipoRegistroOrdinanza;
	}

	public BigDecimal getAnnoRegistro() {
		return mAnnoRegistro;
	}

	public BigDecimal getNumRegistro() {
		return mNumRegistro;
	}

	public BigDecimal getAnnoProvvedimento() {
		return mAnnoProvvedimento;
	}

	public BigDecimal getNumProvvedimento() {
		return mNumProvvedimento;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
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

	public Date getDataSospensioneEsecuzione() {
		return mDataSospensioneEsecuzione;
	}

	public Date getDataDifferimento() {
		return mDataDifferimento;
	}

	public Date getDataRinvio() {
		return mDataRinvio;
	}

	public Date getDataFineInterruzione() {
		return mDataFineInterruzione;
	}

	public Date getDataDepositoIstanza() {
		return mDataDepositoIstanza;
	}

	public Date getDataInterruzionePena() {
		return mDataInterruzionePena;
	}

	public String getCodOggettoDecisione() {
		return mCodOggettoDecisione;
	}

	public String getDescrOggettoDecisione() {
		return mDescrOggettoDecisione;
	}

	public String getMotivazioni() {
		return mMotivazioni;
	}

	public String getNote() {
		return mNote;
	}

	public String getFlagScarcerareScarcerato() {
		return mFlagScarcerareScarcerato;
	}

	public String getFlagPresentanteIstanza() {
		return mFlagPresentanteIstanza;
	}

	public String getCodContenutoDecreto() {
		return mCodContenutoDecreto;
	}

	public String getDescrContenutoDecreto() {
		return mDescrContenutoDecreto;
	}

	public String getCodOggettoProcedimento() {
		return mCodOggettoProcedimento;
	}

	public String getDescrOggettoProcedimento() {
		return mDescrOggettoProcedimento;
	}

	public String getFlagDataInterruzioneInvalid() {
		return mFlagDataInterruzioneInvalid;
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

	public BigDecimal getIdEventoGenerato() {
		return mIdEventoGenerato;
	}

	public String getFlagElaborato() {
		return mFlagElaborato;
	}

	public Date getDataEspulsione() {
		return mDataEspulsione;
	}

	public String getFlagDecisioneTribunale() {
		return mFlagDecisioneTribunale;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public BigDecimal getNumAnniRinvio() {
		return mNumAnniRinvio;
	}

	public BigDecimal getNumMesiRinvio() {
		return mNumMesiRinvio;
	}

	public BigDecimal getNumGiorniRinvio() {
		return mNumGiorniRinvio;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public Date getDataRevocaSospensione() {
		return mDataRevocaSospensione;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getStringaRinvio() {
		return mStringaRinvio;
	}

	public BigDecimal getAnnoRegGen() {
		return mAnno_Reg_Gen;
	}

	public BigDecimal getNumeroRegGen() {
		return mNumero_Reg_Gen;
	}

	public String getTipoRegGen() {
		return mTipo_Reg_Gen;
	}

	//
	// METODI SET()
	//

	public void setIdDecretoOrdinanzaSiep(BigDecimal aValore) {
		mIdDecretoOrdinanzaSiep = aValore;
	}

	public void setDataRicezioneProvvedimento(Date aValore) {
		mDataRicezioneProvvedimento = aValore;
	}

	public void setDataEmissioneProvvedimento(Date aValore) {
		mDataEmissioneProvvedimento = aValore;
	}

	public void setCodTipoRegistroOrdinanza(String aValore) {
		mCodTipoRegistroOrdinanza = aValore;
	}

	public void setDescrTipoRegistroOrdinanza(String aValore) {
		mDescrTipoRegistroOrdinanza = aValore;
	}

	public void setAnnoRegistro(BigDecimal aValore) {
		mAnnoRegistro = aValore;
	}

	public void setNumRegistro(BigDecimal aValore) {
		mNumRegistro = aValore;
	}

	public void setAnnoProvvedimento(BigDecimal aValore) {
		mAnnoProvvedimento = aValore;
	}

	public void setNumProvvedimento(BigDecimal aValore) {
		mNumProvvedimento = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
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

	public void setDataSospensioneEsecuzione(Date aValore) {
		mDataSospensioneEsecuzione = aValore;
	}

	public void setDataDifferimento(Date aValore) {
		mDataDifferimento = aValore;
	}

	public void setDataRinvio(Date aValore) {
		mDataRinvio = aValore;
	}

	public void setDataFineInterruzione(Date aValore) {
		mDataFineInterruzione = aValore;
	}

	public void setDataDepositoIstanza(Date aValore) {
		mDataDepositoIstanza = aValore;
	}

	public void setDataInterruzionePena(Date aValore) {
		mDataInterruzionePena = aValore;
	}

	public void setCodOggettoDecisione(String aValore) {
		mCodOggettoDecisione = aValore;
	}

	public void setDescrOggettoDecisione(String aValore) {
		mDescrOggettoDecisione = aValore;
	}

	public void setMotivazioni(String aValore) {
		mMotivazioni = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setFlagScarcerareScarcerato(String aValore) {
		mFlagScarcerareScarcerato = aValore;
	}

	public void setFlagPresentanteIstanza(String aValore) {
		mFlagPresentanteIstanza = aValore;
	}

	public void setCodContenutoDecreto(String aValore) {
		mCodContenutoDecreto = aValore;
	}

	public void setDescrContenutoDecreto(String aValore) {
		mDescrContenutoDecreto = aValore;
	}

	public void setCodOggettoProcedimento(String aValore) {
		mCodOggettoProcedimento = aValore;
	}

	public void setDescrOggettoProcedimento(String aValore) {
		mDescrOggettoProcedimento = aValore;
	}

	public void setFlagDataInterruzioneInvalid(String aValore) {
		mFlagDataInterruzioneInvalid = aValore;
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

	public void setIdEventoGenerato(BigDecimal aValore) {
		mIdEventoGenerato = aValore;
	}

	public void setFlagElaborato(String aValore) {
		mFlagElaborato = aValore;
	}

	public void setDataEspulsione(Date aValore) {
		mDataEspulsione = aValore;
	}

	public void setFlagDecisioneTribunale(String aValore) {
		mFlagDecisioneTribunale = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setNumAnniRinvio(BigDecimal aValore) {
		mNumAnniRinvio = aValore;
	}

	public void setNumMesiRinvio(BigDecimal aValore) {
		mNumMesiRinvio = aValore;
	}

	public void setNumGiorniRinvio(BigDecimal aValore) {
		mNumGiorniRinvio = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setDataRevocaSospensione(Date aValore) {
		mDataRevocaSospensione = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setStringaRinvio(String aValore) {
		mStringaRinvio = aValore;
	}

	public void setAnnoRegGen(BigDecimal aValore) {
		mAnno_Reg_Gen = aValore;
	}

	public void setNumeroRegGen(BigDecimal aValore) {
		mNumero_Reg_Gen = aValore;
	}

	public void setTipoRegGen(String aValore) {
		mTipo_Reg_Gen = aValore;
	}

	/*
	 * public String toString() { String lStr = new String();
	 * 
	 * lStr = "" + mIdDecretoOrdinanzaSiep +" - " + mDataRicezioneProvvedimento +" - " +
	 * mDataEmissioneProvvedimento +" - " + mCodTipoRegistroOrdinanza +" - " + mDescrTipoRegistroOrdinanza
	 * +" - " + mAnnoRegistro +" - " + mNumRegistro +" - " + mAnnoProvvedimento +" - " + mNumProvvedimento
	 * +" - " + mCodTipoProvvedimento +" - " + mDescrTipoProvvedimento +" - " + mCodTipoAutoritaEmittente
	 * +" - " + mDescrTipoAutoritaEmittente +" - " + mCodLuogoEmittente +" - " + mDescrLuogoEmittente +" - " +
	 * mDataSospensioneEsecuzione +" - " + mDataDifferimento +" - " + mDataRinvio +" - " +
	 * mDataFineInterruzione +" - " + mDataDepositoIstanza +" - " + mDataInterruzionePena +" - " +
	 * mCodOggettoDecisione +" - " + mDescrOggettoDecisione +" - " + mMotivazioni +" - " + mNote +" - " +
	 * mFlagScarcerareScarcerato +" - " + mFlagPresentanteIstanza +" - " + mCodContenutoDecreto +" - " +
	 * mDescrContenutoDecreto +" - " + mCodOggettoProcedimento +" - " + mDescrOggettoProcedimento +" - " +
	 * mFlagDataInterruzioneInvalid +" - " + mProtocollo +" - " + mAltraAutorita +" - " + mAltroLuogo +" - " +
	 * mIdEventoGenerato +" - " + mFlagElaborato +" - " + mDataEspulsione +" - " + mFlagDecisioneTribunale
	 * +" - " + mCodEsito +" - " + mDescrEsito +" - " + mNumAnniRinvio +" - " + mNumMesiRinvio +" - " +
	 * mNumGiorniRinvio +" - " + mCodOperatoreInserimento +" - " + mCodUfficioInserimento +" - " +
	 * mDescrUfficioInserimento +" - " + mDataInserimento +" - " + mCodOperatoreAggiornamento +" - " +
	 * mCodUfficioAggiornamento +" - " + mDescrUfficioAggiornamento +" - " + mDataAggiornamento +" - " +
	 * mDataRevocaSospensione +" - " + mFasSieIdFascicoloSiep +" - " + mAnno_Reg_Gen +" - " + mNumero_Reg_Gen
	 * +" - " + mTipo_Reg_Gen;
	 * 
	 * return lStr; }
	 */
	public void calcolaStringaRinvio() {
		String lStringaRinvio = "";
		if (this.getNumAnniRinvio() != null) {
			if (this.getNumAnniRinvio().intValue() != 0)
				lStringaRinvio = "Anni " + this.getNumAnniRinvio();
		}
		if (this.getNumMesiRinvio() != null) {
			if (this.getNumMesiRinvio().intValue() != 0)
				lStringaRinvio += " Mesi " + this.getNumMesiRinvio();
		}
		if (this.getNumGiorniRinvio() != null) {
			if (this.getNumGiorniRinvio().intValue() != 0)
				lStringaRinvio += " Giorni " + this.getNumGiorniRinvio();
		}

		if (lStringaRinvio.length() > 1)
			this.mStringaRinvio = lStringaRinvio;
		else
			this.mStringaRinvio = null;
	}

}