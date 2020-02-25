package siap.sius.udienzaprocedimento.model;

/**
* <p>Title: UdienzaProcedimentoModel</p>
* <p>Description: Classe Model che rappresenta il UdienzaProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class ProcedimentixUdienzaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1695658255650832231L;
	// Udienza
	private BigDecimal mIdUdienza;
	private Integer mNumCollegio;

	// Fascicolo SIUS
	private BigDecimal mIdFasSIUS;
	private BigDecimal mChiaveAnnoFasSIUS;
	private BigDecimal mChiaveProgrFasSIUS;
	private String mCodStatoFasSIUS;
	private String mDescrStatoFasSIUS;

	// Soggetto
	private BigDecimal mIdSoggetto;
	private String mCognomeSog;
	private String mNomeSog;
	private Date mDataNascitaSog;
	private String mCodComuneNascitaSog;
	private String mDescrComuneNascitaSog;
	private String mDescrComuneNascitaEsteroSog;

	// Generale Procedimento
	private BigDecimal mIdGeneraleProcedimento;
	private String mCodOggettoProcedimento;
	private String mDescrOggettoProcedimento;
	private String mSezioneProcedimento;

	// Udienza Procedimento
	private String mFlagRinviata;
	private String mEsitoProvvedimento;
	private String mMotivoProvvedimento;
	private String mDescStatoUdienza;
	private String mDescPosizioneGiuridicaSius;
	private String mDescPosizioneGiuridica;

	// Evento
	private BigDecimal mIdEvento;
	private String mCodEsito;

	// Magistrato
	private String mCognomeMagistrato;
	private String mNomeMagistrato;
	private String mCodMagistrato;

	// Esperto in funzione di Magistrato Relatore
	private BigDecimal mIdEsperto;

	// COSTRUTTORE DI DEFAULT
	public ProcedimentixUdienzaModel() {
		this.mIdUdienza = null;
		this.mNumCollegio = null;
		this.mIdFasSIUS = null;
		this.mChiaveAnnoFasSIUS = null;
		this.mChiaveProgrFasSIUS = null;
		this.mCodStatoFasSIUS = "";
		this.mDescrStatoFasSIUS = "";
		this.mIdSoggetto = null;
		this.mCognomeSog = "";
		this.mNomeSog = "";
		this.mDataNascitaSog = null;
		this.mCodComuneNascitaSog = "";
		this.mDescrComuneNascitaSog = "";
		this.mDescrComuneNascitaEsteroSog = "";
		this.mIdGeneraleProcedimento = null;
		this.mCodOggettoProcedimento = "";
		this.mDescrOggettoProcedimento = "";
		this.mSezioneProcedimento = "";
		this.mFlagRinviata = "";
		this.mEsitoProvvedimento = "";
		this.mMotivoProvvedimento = "";
		this.mDescStatoUdienza = "";
		this.mDescPosizioneGiuridicaSius = "";
		this.mDescPosizioneGiuridica = "";
		this.mIdEvento = null;
		this.mCodEsito = "";
		this.mCognomeMagistrato = "";
		this.mNomeMagistrato = "";
		this.mCodMagistrato = "";
		this.mIdEsperto = null;
	}

	// COSTRUTTORE DI COPIA
	public ProcedimentixUdienzaModel(ProcedimentixUdienzaModel aModel) {
		this.mIdUdienza = aModel.mIdUdienza;
		this.mNumCollegio = aModel.mNumCollegio;
		this.mIdFasSIUS = aModel.mIdFasSIUS;
		this.mChiaveAnnoFasSIUS = aModel.mChiaveAnnoFasSIUS;
		this.mChiaveProgrFasSIUS = aModel.mChiaveProgrFasSIUS;
		this.mCodStatoFasSIUS = aModel.mCodStatoFasSIUS;
		this.mDescrStatoFasSIUS = aModel.mDescrStatoFasSIUS;
		this.mIdSoggetto = aModel.mIdSoggetto;
		this.mCognomeSog = aModel.mCognomeSog;
		this.mNomeSog = aModel.mNomeSog;
		this.mDataNascitaSog = aModel.mDataNascitaSog;
		this.mCodComuneNascitaSog = aModel.mCodComuneNascitaSog;
		this.mDescrComuneNascitaSog = aModel.mDescrComuneNascitaSog;
		this.mDescrComuneNascitaEsteroSog = aModel.mDescrComuneNascitaEsteroSog;
		this.mIdGeneraleProcedimento = aModel.mIdGeneraleProcedimento;
		this.mCodOggettoProcedimento = aModel.mCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aModel.mDescrOggettoProcedimento;
		this.mSezioneProcedimento = aModel.mSezioneProcedimento;
		this.mFlagRinviata = aModel.mFlagRinviata;
		this.mEsitoProvvedimento = aModel.mEsitoProvvedimento;
		this.mMotivoProvvedimento = aModel.mMotivoProvvedimento;
		this.mDescStatoUdienza = aModel.mDescStatoUdienza;
		this.mDescPosizioneGiuridicaSius = aModel.mDescPosizioneGiuridicaSius;
		this.mDescPosizioneGiuridica = aModel.mDescPosizioneGiuridica;
		this.mIdEvento = aModel.mIdEvento;
		this.mCodEsito = aModel.mCodEsito;
		this.mCognomeMagistrato = aModel.mCognomeMagistrato;
		this.mNomeMagistrato = aModel.mNomeMagistrato;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mIdEsperto = aModel.mIdEsperto;

	}

	// COSTRUTTORE MODEL
	public ProcedimentixUdienzaModel(BigDecimal aIdUdienza, Integer aNumCollegio, BigDecimal aIdFasSIUS,
			BigDecimal aChiaveAnnoFasSIUS, BigDecimal aChiaveProgrFasSIUS, String aCodStatoFascicolo,
			String aDescrStatoFascicolo, BigDecimal aIdSoggetto, String aCognomeSog, String aNomeSog,
			Date aDataNascitaSog, String aCodComuneNascitaSog, String aDescrComuneNascitaSog,
			String aDescrComuneNascitaEsteroSog, BigDecimal aIdGeneraleProcedimento,
			String aCodOggettoProcedimento, String aDescrOggettoProcedimento, String aSezioneProcedimento,
			String aFlagRinviata, String aEsitoProvvedimento, String aMotivoProvvedimento,
			String aDescStatoUdienza, String aDescPosizioneGiuridicaSius, String aDescPosizioneGiuridica,
			BigDecimal aIdEvento, String aCognomeMagistrato, String aCodEsito, String aNomeMagistrato,
			String aCodMagistrato, BigDecimal aIdEsperto) {
		this.mIdUdienza = aIdUdienza;
		this.mNumCollegio = aNumCollegio;
		this.mIdFasSIUS = aIdFasSIUS;
		this.mChiaveAnnoFasSIUS = aChiaveAnnoFasSIUS;
		this.mChiaveProgrFasSIUS = aChiaveProgrFasSIUS;
		this.mCodStatoFasSIUS = aCodStatoFascicolo;
		this.mDescrStatoFasSIUS = aDescrStatoFascicolo;
		this.mIdSoggetto = aIdSoggetto;
		this.mCognomeSog = aCognomeSog;
		this.mNomeSog = aNomeSog;
		this.mDataNascitaSog = aDataNascitaSog;
		this.mCodComuneNascitaSog = aCodComuneNascitaSog;
		this.mDescrComuneNascitaSog = aDescrComuneNascitaSog;
		this.mDescrComuneNascitaEsteroSog = aDescrComuneNascitaEsteroSog;
		this.mIdGeneraleProcedimento = aIdGeneraleProcedimento;
		this.mCodOggettoProcedimento = aCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aDescrOggettoProcedimento;
		this.mSezioneProcedimento = aSezioneProcedimento;
		this.mFlagRinviata = aFlagRinviata;
		this.mEsitoProvvedimento = aEsitoProvvedimento;
		this.mMotivoProvvedimento = aMotivoProvvedimento;
		this.mDescStatoUdienza = aDescStatoUdienza;
		this.mDescPosizioneGiuridicaSius = aDescPosizioneGiuridicaSius;
		this.mDescPosizioneGiuridica = aDescPosizioneGiuridica;
		this.mIdEvento = aIdEvento;
		this.mCodEsito = aCodEsito;
		this.mCognomeMagistrato = aCognomeMagistrato;
		this.mNomeMagistrato = aNomeMagistrato;
		this.mCodMagistrato = aCodMagistrato;
		this.mIdEsperto = aIdSoggetto;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdUdienza() {
		return mIdUdienza;
	}

	public Integer getNumCollegio() {
		return mNumCollegio;
	}

	public BigDecimal getIdFasSIUS() {
		return mIdFasSIUS;
	}

	public BigDecimal getChiaveAnnoFasSIUS() {
		return mChiaveAnnoFasSIUS;
	}

	public BigDecimal getChiaveProgrFasSIUS() {
		return mChiaveProgrFasSIUS;
	}

	public String getCodStatoFasSIUS() {
		return mCodStatoFasSIUS;
	}

	public String getDescrStatoFasSIUS() {
		return mDescrStatoFasSIUS;
	}

	public BigDecimal getIdSoggetto() {
		return mIdSoggetto;
	}

	public String getCognomeSog() {
		return mCognomeSog;
	}

	public String getNomeSog() {
		return mNomeSog;
	}

	public Date getDataNascitaSog() {
		return mDataNascitaSog;
	}

	public String getCodComuneNascitaSog() {
		return mCodComuneNascitaSog;
	}

	public String getDescrComuneNascitaSog() {
		return mDescrComuneNascitaSog;
	}

	public String getDescrComuneNascitaEsteroSog() {
		return mDescrComuneNascitaEsteroSog;
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

	public String getSezioneProcedimento() {
		return mSezioneProcedimento;
	}

	public String getFlagRinviata() {
		return mFlagRinviata;
	}

	public String getEsitoProvvedimento() {
		return mEsitoProvvedimento;
	}

	public String getMotivoProvvedimento() {
		return mMotivoProvvedimento;
	}

	public String getDescStatoUdienza() {
		return mDescStatoUdienza;
	}

	public String getDescPosizioneGiuridicaSius() {
		return mDescPosizioneGiuridicaSius;
	}

	public String getDescPosizioneGiuridica() {
		return mDescPosizioneGiuridica;
	}

	public BigDecimal getIdEvento() {
		return mIdEvento;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getCognomeMagistrato() {
		return mCognomeMagistrato;
	}

	public String getNomeMagistrato() {
		return mNomeMagistrato;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public BigDecimal getIdEsperto() {
		return mIdEsperto;
	}

	//
	// METODI SET()
	//

	public void setIdUdienza(BigDecimal aValore) {
		mIdUdienza = aValore;
	}

	public void setNumCollegio(Integer aValore) {
		mNumCollegio = aValore;
	}

	public void setIdFasSIUS(BigDecimal aValore) {
		mIdFasSIUS = aValore;
	}

	public void setChiaveAnnoFasSIUS(BigDecimal aValore) {
		mChiaveAnnoFasSIUS = aValore;
	}

	public void setChiaveProgrFasSIUS(BigDecimal aValore) {
		mChiaveProgrFasSIUS = aValore;
	}

	public void setCodStatoFasSIUS(String aValore) {
		mCodStatoFasSIUS = aValore;
	}

	public void setDescrStatoFasSIUS(String aValore) {
		mDescrStatoFasSIUS = aValore;
	}

	public void setIdSoggetto(BigDecimal aValore) {
		mIdSoggetto = aValore;
	}

	public void setCognomeSog(String aValore) {
		mCognomeSog = aValore;
	}

	public void setNomeSog(String aValore) {
		mNomeSog = aValore;
	}

	public void setDataNascitaSog(Date aValore) {
		mDataNascitaSog = aValore;
	}

	public void setCodComuneNascitaSog(String aValore) {
		mCodComuneNascitaSog = aValore;
	}

	public void setDescrComuneNascitaSog(String aValore) {
		mDescrComuneNascitaSog = aValore;
	}

	public void setDescrComuneNascitaEsteroSog(String aValore) {
		mDescrComuneNascitaEsteroSog = aValore;
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

	public void setSezioneProcedimento(String aValore) {
		mSezioneProcedimento = aValore;
	}

	public void setFlagRinviata(String aValore) {
		mFlagRinviata = aValore;
	}

	public void setEsitoProvvedimento(String aValore) {
		mEsitoProvvedimento = aValore;
	}

	public void setMotivoProvvedimento(String aValore) {
		mMotivoProvvedimento = aValore;
	}

	public void setDescStatoUdienza(String aValore) {
		mDescStatoUdienza = aValore;
	}

	public void setDescPosizioneGiuridicaSius(String aValore) {
		mDescPosizioneGiuridicaSius = aValore;
	}

	public void setDescPosizioneGiuridica(String aValore) {
		mDescPosizioneGiuridica = aValore;
	}

	public void setIdEvento(BigDecimal aValore) {
		mIdEvento = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setCognomeMagistrato(String aValore) {
		mCognomeMagistrato = aValore;
	}

	public void setNomeMagistrato(String aValore) {
		mNomeMagistrato = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setIdEsperto(BigDecimal aValore) {
		mIdEsperto = aValore;
	}

	/*
	 * public String toString() { String lStr = new String();
	 * 
	 * lStr = "" + mIdUdienza +" - " + mIdFasSIUS +" - " + mChiaveAnnoFasSIUS +" - " + mChiaveProgrFasSIUS
	 * +" - " + this.mCodStatoFasSIUS +" - " + this.mDescrStatoFasSIUS +" - " + mIdSoggetto +" - " +
	 * mCognomeSog +" - " + mNomeSog +" - " + mDataNascitaSog +" - " + mCodComuneNascitaSog +" - " +
	 * mDescrComuneNascitaSog +" - " + mDescrComuneNascitaEsteroSog +" - " + mIdGeneraleProcedimento +" - " +
	 * mCodOggettoProcedimento +" - " + mDescrOggettoProcedimento +" - " + mSezioneProcedimento + " - " +
	 * mFlagRinviata + " - " + mEsitoProvvedimento + " - " + mMotivoProvvedimento + " - " + mDescStatoUdienza
	 * + " - " + mDescPosizioneGiuridicaSius + " - " + mDescPosizioneGiuridica + " - " + mIdEvento + " - " +
	 * mCodEsito + " - " + mCognomeMagistrato + " - " + mNomeMagistrato + " - " + mCodMagistrato + " - " +
	 * mIdEsperto;
	 * 
	 * return lStr; }
	 */
}
