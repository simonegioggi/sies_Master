package siap.siep.altrigradigiudizio.model;

/**
* <p>Title: AltriGradiGiudizioModel</p>
* <p>Description: Classe Model che rappresenta il AltriGradiGiudizio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AltriGradiGiudizioModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 8647451031350813656L;

	private BigDecimal mIdAltrigradigiudizio;
	private Date mDataSentenzaIGrado;
	private BigDecimal mAnnoSentenzaIGrado;
	private String mNumeroSentenzaIGrado;
	private String mCodAutEmittSentIGrado;
	private String mDescrAutEmittSentIGrado;
	private String mCodLuoEmittSentIGrado;
	private String mDescrLuoEmittSentIGrado;
	private String mNumSezEmittSentIGrado;
	private String mCodTipoSentenzaIiGrado;
	private String mDescrTipoSentenzaIiGrado;
	private Date mDataSentenzaIiGrado;
	private BigDecimal mAnnoSentenzaIiGrado;
	private String mNumeroSentenzaIiGrado;
	private String mCodAutEmittSentIiGrado;
	private String mDescrAutEmittSentIiGrado;
	private String mCodLuoEmittSentIiGrado;
	private String mDescrLuoEmittSentIiGrado;
	private String mNumSezEmittSentIiGrado;
	private BigDecimal mAnnoRegGenCassaz;
	private String mNumeroRegGenCassaz;
	private BigDecimal mAnnoSentenzaCassaz;
	private String mNumeroSentenzaCassaz;
	private BigDecimal mAnnoRaccGenealeIiGrado;
	private String mNumeroRaccGenealeIiGrado;
	private String mCodTipoDecisioneCassazione;
	private String mDescrTipoDecisioneCassazione;
	private BigDecimal mSenIdSentenza;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mCodTipoRito;
	private String mDescrTipoRito;
	private boolean mEsistonoFascicoliAssociati;

	// COSTRUTTORE DI DEFAULT
	public AltriGradiGiudizioModel() {
		this.mIdAltrigradigiudizio = null;
		this.mDataSentenzaIGrado = null;
		this.mAnnoSentenzaIGrado = null;
		this.mNumeroSentenzaIGrado = "";
		this.mCodAutEmittSentIGrado = "";
		this.mDescrAutEmittSentIGrado = "";
		this.mCodLuoEmittSentIGrado = "";
		this.mDescrLuoEmittSentIGrado = "";
		this.mNumSezEmittSentIGrado = "";
		this.mCodTipoSentenzaIiGrado = "";
		this.mDescrTipoSentenzaIiGrado = "";
		this.mDataSentenzaIiGrado = null;
		this.mAnnoSentenzaIiGrado = null;
		this.mNumeroSentenzaIiGrado = "";
		this.mCodAutEmittSentIiGrado = "";
		this.mDescrAutEmittSentIiGrado = "";
		this.mCodLuoEmittSentIiGrado = "";
		this.mDescrLuoEmittSentIiGrado = "";
		this.mNumSezEmittSentIiGrado = "";
		this.mAnnoRegGenCassaz = null;
		this.mNumeroRegGenCassaz = "";
		this.mAnnoSentenzaCassaz = null;
		this.mNumeroSentenzaCassaz = "";
		this.mAnnoRaccGenealeIiGrado = null;
		this.mNumeroRaccGenealeIiGrado = "";
		this.mCodTipoDecisioneCassazione = "";
		this.mDescrTipoDecisioneCassazione = "";
		this.mSenIdSentenza = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mCodTipoRito = "";
		this.mDescrTipoRito = "";
	}

	// COSTRUTTORE DI COPIA
	public AltriGradiGiudizioModel(AltriGradiGiudizioModel aModel) {
		this.mIdAltrigradigiudizio = aModel.mIdAltrigradigiudizio;
		this.mDataSentenzaIGrado = aModel.mDataSentenzaIGrado;
		this.mAnnoSentenzaIGrado = aModel.mAnnoSentenzaIGrado;
		this.mNumeroSentenzaIGrado = aModel.mNumeroSentenzaIGrado;
		this.mCodAutEmittSentIGrado = aModel.mCodAutEmittSentIGrado;
		this.mDescrAutEmittSentIGrado = aModel.mDescrAutEmittSentIGrado;
		this.mCodLuoEmittSentIGrado = aModel.mCodLuoEmittSentIGrado;
		this.mDescrLuoEmittSentIGrado = aModel.mDescrLuoEmittSentIGrado;
		this.mNumSezEmittSentIGrado = aModel.mNumSezEmittSentIGrado;
		this.mCodTipoSentenzaIiGrado = aModel.mCodTipoSentenzaIiGrado;
		this.mDescrTipoSentenzaIiGrado = aModel.mDescrTipoSentenzaIiGrado;
		this.mDataSentenzaIiGrado = aModel.mDataSentenzaIiGrado;
		this.mAnnoSentenzaIiGrado = aModel.mAnnoSentenzaIiGrado;
		this.mNumeroSentenzaIiGrado = aModel.mNumeroSentenzaIiGrado;
		this.mCodAutEmittSentIiGrado = aModel.mCodAutEmittSentIiGrado;
		this.mDescrAutEmittSentIiGrado = aModel.mDescrAutEmittSentIiGrado;
		this.mCodLuoEmittSentIiGrado = aModel.mCodLuoEmittSentIiGrado;
		this.mDescrLuoEmittSentIiGrado = aModel.mDescrLuoEmittSentIiGrado;
		this.mNumSezEmittSentIiGrado = aModel.mNumSezEmittSentIiGrado;
		this.mAnnoRegGenCassaz = aModel.mAnnoRegGenCassaz;
		this.mNumeroRegGenCassaz = aModel.mNumeroRegGenCassaz;
		this.mAnnoSentenzaCassaz = aModel.mAnnoSentenzaCassaz;
		this.mNumeroSentenzaCassaz = aModel.mNumeroSentenzaCassaz;
		this.mAnnoRaccGenealeIiGrado = aModel.mAnnoRaccGenealeIiGrado;
		this.mNumeroRaccGenealeIiGrado = aModel.mNumeroRaccGenealeIiGrado;
		this.mCodTipoDecisioneCassazione = aModel.mCodTipoDecisioneCassazione;
		this.mDescrTipoDecisioneCassazione = aModel.mDescrTipoDecisioneCassazione;
		this.mSenIdSentenza = aModel.mSenIdSentenza;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mCodTipoRito = aModel.mCodTipoRito;
		this.mDescrTipoRito = aModel.mDescrTipoRito;
		this.mEsistonoFascicoliAssociati = aModel.getEsistonoFascicoliAssociati();
	}

	// COSTRUTTORE MODEL
	public AltriGradiGiudizioModel(BigDecimal aIdAltrigradigiudizio, Date aDataSentenzaIGrado,
			BigDecimal aAnnoSentenzaIGrado, String aNumeroSentenzaIGrado, String aCodAutEmittSentIGrado,
			String aDescrAutEmittSentIGrado, String aCodLuoEmittSentIGrado, String aDescrLuoEmittSentIGrado,
			String aNumSezEmittSentIGrado, String aCodTipoSentenzaIiGrado, String aDescrTipoSentenzaIiGrado,
			Date aDataSentenzaIiGrado, BigDecimal aAnnoSentenzaIiGrado, String aNumeroSentenzaIiGrado,
			String aCodAutEmittSentIiGrado, String aDescrAutEmittSentIiGrado, String aCodLuoEmittSentIiGrado,
			String aDescrLuoEmittSentIiGrado, String aNumSezEmittSentIiGrado, BigDecimal aAnnoRegGenCassaz,
			String aNumeroRegGenCassaz, BigDecimal aAnnoSentenzaCassaz, String aNumeroSentenzaCassaz,
			BigDecimal aAnnoRaccGenealeIiGrado, String aNumeroRaccGenealeIiGrado,
			String aCodTipoDecisioneCassazione, String aDescrTipoDecisioneCassazione,
			BigDecimal aSenIdSentenza, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			String aCodTipoRito, String aDescrTipoRito) {
		this.mIdAltrigradigiudizio = aIdAltrigradigiudizio;
		this.mDataSentenzaIGrado = aDataSentenzaIGrado;
		this.mAnnoSentenzaIGrado = aAnnoSentenzaIGrado;
		this.mNumeroSentenzaIGrado = aNumeroSentenzaIGrado;
		this.mCodAutEmittSentIGrado = aCodAutEmittSentIGrado;
		this.mDescrAutEmittSentIGrado = aDescrAutEmittSentIGrado;
		this.mCodLuoEmittSentIGrado = aCodLuoEmittSentIGrado;
		this.mDescrLuoEmittSentIGrado = aDescrLuoEmittSentIGrado;
		this.mNumSezEmittSentIGrado = aNumSezEmittSentIGrado;
		this.mCodTipoSentenzaIiGrado = aCodTipoSentenzaIiGrado;
		this.mDescrTipoSentenzaIiGrado = aDescrTipoSentenzaIiGrado;
		this.mDataSentenzaIiGrado = aDataSentenzaIiGrado;
		this.mAnnoSentenzaIiGrado = aAnnoSentenzaIiGrado;
		this.mNumeroSentenzaIiGrado = aNumeroSentenzaIiGrado;
		this.mCodAutEmittSentIiGrado = aCodAutEmittSentIiGrado;
		this.mDescrAutEmittSentIiGrado = aDescrAutEmittSentIiGrado;
		this.mCodLuoEmittSentIiGrado = aCodLuoEmittSentIiGrado;
		this.mDescrLuoEmittSentIiGrado = aDescrLuoEmittSentIiGrado;
		this.mNumSezEmittSentIiGrado = aNumSezEmittSentIiGrado;
		this.mAnnoRegGenCassaz = aAnnoRegGenCassaz;
		this.mNumeroRegGenCassaz = aNumeroRegGenCassaz;
		this.mAnnoSentenzaCassaz = aAnnoSentenzaCassaz;
		this.mNumeroSentenzaCassaz = aNumeroSentenzaCassaz;
		this.mAnnoRaccGenealeIiGrado = aAnnoRaccGenealeIiGrado;
		this.mNumeroRaccGenealeIiGrado = aNumeroRaccGenealeIiGrado;
		this.mCodTipoDecisioneCassazione = aCodTipoDecisioneCassazione;
		this.mDescrTipoDecisioneCassazione = aDescrTipoDecisioneCassazione;
		this.mSenIdSentenza = aSenIdSentenza;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mCodTipoRito = aCodTipoRito;
		this.mDescrTipoRito = aDescrTipoRito;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAltrigradigiudizio() {
		return mIdAltrigradigiudizio;
	}

	public Date getDataSentenzaIGrado() {
		return mDataSentenzaIGrado;
	}

	public BigDecimal getAnnoSentenzaIGrado() {
		return mAnnoSentenzaIGrado;
	}

	public String getNumeroSentenzaIGrado() {
		return mNumeroSentenzaIGrado;
	}

	public String getCodAutEmittSentIGrado() {
		return mCodAutEmittSentIGrado;
	}

	public String getDescrAutEmittSentIGrado() {
		return mDescrAutEmittSentIGrado;
	}

	public String getCodLuoEmittSentIGrado() {
		return mCodLuoEmittSentIGrado;
	}

	public String getDescrLuoEmittSentIGrado() {
		return mDescrLuoEmittSentIGrado;
	}

	public String getNumSezEmittSentIGrado() {
		return mNumSezEmittSentIGrado;
	}

	public String getCodTipoSentenzaIiGrado() {
		return mCodTipoSentenzaIiGrado;
	}

	public String getDescrTipoSentenzaIiGrado() {
		return mDescrTipoSentenzaIiGrado;
	}

	public Date getDataSentenzaIiGrado() {
		return mDataSentenzaIiGrado;
	}

	public BigDecimal getAnnoSentenzaIiGrado() {
		return mAnnoSentenzaIiGrado;
	}

	public String getNumeroSentenzaIiGrado() {
		return mNumeroSentenzaIiGrado;
	}

	public String getCodAutEmittSentIiGrado() {
		return mCodAutEmittSentIiGrado;
	}

	public String getDescrAutEmittSentIiGrado() {
		return mDescrAutEmittSentIiGrado;
	}

	public String getCodLuoEmittSentIiGrado() {
		return mCodLuoEmittSentIiGrado;
	}

	public String getDescrLuoEmittSentIiGrado() {
		return mDescrLuoEmittSentIiGrado;
	}

	public String getNumSezEmittSentIiGrado() {
		return mNumSezEmittSentIiGrado;
	}

	public BigDecimal getAnnoRegGenCassaz() {
		return mAnnoRegGenCassaz;
	}

	public String getNumeroRegGenCassaz() {
		return mNumeroRegGenCassaz;
	}

	public BigDecimal getAnnoSentenzaCassaz() {
		return mAnnoSentenzaCassaz;
	}

	public String getNumeroSentenzaCassaz() {
		return mNumeroSentenzaCassaz;
	}

	public BigDecimal getAnnoRaccGenealeIiGrado() {
		return mAnnoRaccGenealeIiGrado;
	}

	public String getNumeroRaccGenealeIiGrado() {
		return mNumeroRaccGenealeIiGrado;
	}

	public String getCodTipoDecisioneCassazione() {
		return mCodTipoDecisioneCassazione;
	}

	public String getDescrTipoDecisioneCassazione() {
		return mDescrTipoDecisioneCassazione;
	}

	public BigDecimal getSenIdSentenza() {
		return mSenIdSentenza;
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

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
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

	//
	// METODI SET()
	//

	public void setIdAltrigradigiudizio(BigDecimal aValore) {
		mIdAltrigradigiudizio = aValore;
	}

	public void setDataSentenzaIGrado(Date aValore) {
		mDataSentenzaIGrado = aValore;
	}

	public void setAnnoSentenzaIGrado(BigDecimal aValore) {
		mAnnoSentenzaIGrado = aValore;
	}

	public void setNumeroSentenzaIGrado(String aValore) {
		mNumeroSentenzaIGrado = aValore;
	}

	public void setCodAutEmittSentIGrado(String aValore) {
		mCodAutEmittSentIGrado = aValore;
	}

	public void setDescrAutEmittSentIGrado(String aValore) {
		mDescrAutEmittSentIGrado = aValore;
	}

	public void setCodLuoEmittSentIGrado(String aValore) {
		mCodLuoEmittSentIGrado = aValore;
	}

	public void setDescrLuoEmittSentIGrado(String aValore) {
		mDescrLuoEmittSentIGrado = aValore;
	}

	public void setNumSezEmittSentIGrado(String aValore) {
		mNumSezEmittSentIGrado = aValore;
	}

	public void setCodTipoSentenzaIiGrado(String aValore) {
		mCodTipoSentenzaIiGrado = aValore;
	}

	public void setDescrTipoSentenzaIiGrado(String aValore) {
		mDescrTipoSentenzaIiGrado = aValore;
	}

	public void setDataSentenzaIiGrado(Date aValore) {
		mDataSentenzaIiGrado = aValore;
	}

	public void setAnnoSentenzaIiGrado(BigDecimal aValore) {
		mAnnoSentenzaIiGrado = aValore;
	}

	public void setNumeroSentenzaIiGrado(String aValore) {
		mNumeroSentenzaIiGrado = aValore;
	}

	public void setCodAutEmittSentIiGrado(String aValore) {
		mCodAutEmittSentIiGrado = aValore;
	}

	public void setDescrAutEmittSentIiGrado(String aValore) {
		mDescrAutEmittSentIiGrado = aValore;
	}

	public void setCodLuoEmittSentIiGrado(String aValore) {
		mCodLuoEmittSentIiGrado = aValore;
	}

	public void setDescrLuoEmittSentIiGrado(String aValore) {
		mDescrLuoEmittSentIiGrado = aValore;
	}

	public void setNumSezEmittSentIiGrado(String aValore) {
		mNumSezEmittSentIiGrado = aValore;
	}

	public void setAnnoRegGenCassaz(BigDecimal aValore) {
		mAnnoRegGenCassaz = aValore;
	}

	public void setNumeroRegGenCassaz(String aValore) {
		mNumeroRegGenCassaz = aValore;
	}

	public void setAnnoSentenzaCassaz(BigDecimal aValore) {
		mAnnoSentenzaCassaz = aValore;
	}

	public void setNumeroSentenzaCassaz(String aValore) {
		mNumeroSentenzaCassaz = aValore;
	}

	public void setAnnoRaccGenealeIiGrado(BigDecimal aValore) {
		mAnnoRaccGenealeIiGrado = aValore;
	}

	public void setNumeroRaccGenealeIiGrado(String aValore) {
		mNumeroRaccGenealeIiGrado = aValore;
	}

	public void setCodTipoDecisioneCassazione(String aValore) {
		mCodTipoDecisioneCassazione = aValore;
	}

	public void setDescrTipoDecisioneCassazione(String aValore) {
		mDescrTipoDecisioneCassazione = aValore;
	}

	public void setSenIdSentenza(BigDecimal aValore) {
		mSenIdSentenza = aValore;
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

	public void setCodTipoRito(String aValore) {
		mCodTipoRito = aValore;
	}

	public void setDescrTipoRito(String aValore) {
		mDescrTipoRito = aValore;
	}

	public void setEsistonoFascicoliAssociati(boolean aValore) {
		mEsistonoFascicoliAssociati = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdAltrigradigiudizio + " - " + mDataSentenzaIGrado + " - " + mAnnoSentenzaIGrado + " - "
				+ mNumeroSentenzaIGrado + " - " + mCodAutEmittSentIGrado + " - " + mDescrAutEmittSentIGrado
				+ " - " + mCodLuoEmittSentIGrado + " - " + mDescrLuoEmittSentIGrado + " - "
				+ mNumSezEmittSentIGrado + " - " + mCodTipoSentenzaIiGrado + " - " + mDescrTipoSentenzaIiGrado
				+ " - " + mDataSentenzaIiGrado + " - " + mAnnoSentenzaIiGrado + " - " + mNumeroSentenzaIiGrado
				+ " - " + mCodAutEmittSentIiGrado + " - " + mDescrAutEmittSentIiGrado + " - "
				+ mCodLuoEmittSentIiGrado + " - " + mDescrLuoEmittSentIiGrado + " - "
				+ mNumSezEmittSentIiGrado + " - " + mAnnoRegGenCassaz + " - " + mNumeroRegGenCassaz + " - "
				+ mAnnoSentenzaCassaz + " - " + mNumeroSentenzaCassaz + " - " + mAnnoRaccGenealeIiGrado
				+ " - " + mNumeroRaccGenealeIiGrado + " - " + mCodTipoDecisioneCassazione + " - "
				+ mDescrTipoDecisioneCassazione + " - " + mSenIdSentenza + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " + mCodTipoRito;

		return lStr;
	}

}