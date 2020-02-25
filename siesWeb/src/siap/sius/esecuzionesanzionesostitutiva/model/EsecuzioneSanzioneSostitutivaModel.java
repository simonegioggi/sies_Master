package siap.sius.esecuzionesanzionesostitutiva.model;

/**
* <p>Title: EsecuzioneSanzioneSostitutivaModel</p>
* <p>Description: Classe Model che rappresenta l' EsecuzioneSanzioneSostitutiva</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class EsecuzioneSanzioneSostitutivaModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 3379972603622581036L;

	private BigDecimal mIdEsecuzioneSanzioneSost;
	private BigDecimal mAnnoS07;
	private BigDecimal mProgrS07;
	private Date mDataOrdinanza;
	private String mCodAutoritaEmittOrd;
	private String mDescrAutoritaEmittOrd;
	private String mCodTipoAutoritaEmittOrd;
	private String mDescrTipoAutoritaEmittOrd;
	private String mCodLuogoAutoritaEmittOrd;
	private String mDescrLuogoAutoritaEmittOrd;
	private String mCodTipoSanzione;
	private String mDescrTipoSanzione;
	private Date mDataInizioSanzione;
	private Date mDataTermineIniziale;
	private Date mDataTermineAttuale;
	private Date mDataDeclaratoriaESS;
	private BigDecimal mDepDecIdDepositoDecreto;
	private BigDecimal mDepOpidDepositoOrdinanzaPc;
	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mGenPridGeneraleProcedimento;
	private String mLuogoEsecuzioneSanzione;
	private BigDecimal mNumGiorniSanzione;
	private BigDecimal mNumMesiSanzione;
	private BigDecimal mNumAnniSanzione;

	// COSTRUTTORE DI DEFAULT
	public EsecuzioneSanzioneSostitutivaModel() {
		this.mIdEsecuzioneSanzioneSost = null;
		this.mAnnoS07 = null;
		this.mProgrS07 = null;
		this.mDataOrdinanza = null;
		this.mCodAutoritaEmittOrd = "";
		this.mDescrAutoritaEmittOrd = "";
		this.mCodTipoAutoritaEmittOrd = "";
		this.mDescrTipoAutoritaEmittOrd = "";
		this.mCodLuogoAutoritaEmittOrd = "";
		this.mDescrLuogoAutoritaEmittOrd = "";
		this.mCodTipoSanzione = "";
		this.mDescrTipoSanzione = "";
		this.mDataInizioSanzione = null;
		this.mDataTermineIniziale = null;
		this.mDataTermineAttuale = null;
		this.mDataDeclaratoriaESS = null;
		this.mDepDecIdDepositoDecreto = null;
		this.mDepOpidDepositoOrdinanzaPc = null;
		this.mNote = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mGenPridGeneraleProcedimento = null;
		this.mLuogoEsecuzioneSanzione = "";
		this.mNumGiorniSanzione = null;
		this.mNumMesiSanzione = null;
		this.mNumAnniSanzione = null;
	}

	// COSTRUTTORE DI COPIA
	public EsecuzioneSanzioneSostitutivaModel(EsecuzioneSanzioneSostitutivaModel aModel) {
		this.mIdEsecuzioneSanzioneSost = aModel.mIdEsecuzioneSanzioneSost;
		this.mAnnoS07 = aModel.mAnnoS07;
		this.mProgrS07 = aModel.mProgrS07;
		this.mDataOrdinanza = aModel.mDataOrdinanza;
		this.mCodAutoritaEmittOrd = aModel.mCodAutoritaEmittOrd;
		this.mDescrAutoritaEmittOrd = aModel.mDescrAutoritaEmittOrd;
		this.mCodTipoAutoritaEmittOrd = aModel.mCodTipoAutoritaEmittOrd;
		this.mDescrTipoAutoritaEmittOrd = aModel.mDescrTipoAutoritaEmittOrd;
		this.mCodLuogoAutoritaEmittOrd = aModel.mCodLuogoAutoritaEmittOrd;
		this.mDescrLuogoAutoritaEmittOrd = aModel.mDescrLuogoAutoritaEmittOrd;
		this.mCodTipoSanzione = aModel.mCodTipoSanzione;
		this.mDescrTipoSanzione = aModel.mDescrTipoSanzione;
		this.mDataInizioSanzione = aModel.mDataInizioSanzione;
		this.mDataTermineIniziale = aModel.mDataTermineIniziale;
		this.mDataTermineAttuale = aModel.mDataTermineAttuale;
		this.mDataDeclaratoriaESS = aModel.mDataDeclaratoriaESS;
		this.mDepDecIdDepositoDecreto = aModel.mDepDecIdDepositoDecreto;
		this.mDepOpidDepositoOrdinanzaPc = aModel.mDepOpidDepositoOrdinanzaPc;
		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mGenPridGeneraleProcedimento = aModel.mGenPridGeneraleProcedimento;
		this.mLuogoEsecuzioneSanzione = aModel.mLuogoEsecuzioneSanzione;
		this.mNumGiorniSanzione = aModel.mNumGiorniSanzione;
		this.mNumMesiSanzione = aModel.mNumMesiSanzione;
		this.mNumAnniSanzione = aModel.mNumAnniSanzione;
	}

	// COSTRUTTORE MODEL
	public EsecuzioneSanzioneSostitutivaModel(BigDecimal aIdEsecuzioneSanzioneSost, BigDecimal aAnnoS07,
			BigDecimal aProgrS07, Date aDataOrdinanza, String aCodAutoritaEmittOrd,
			String aDescrAutoritaEmittOrd, String aCodTipoAutoritaEmittOrd, String aDescrTipoAutoritaEmittOrd,
			String aCodLuogoAutoritaEmittOrd, String aDescrLuogoAutoritaEmittOrd, String aCodTipoSanzione,
			String aDescrTipoSanzione, Date aDataInizioSanzione, Date aDataTermineIniziale,
			Date aDataTermineAttuale, Date aDataDeclaratoriaESS, BigDecimal aDepDecIdDepositoDecreto,
			BigDecimal aDepOpidDepositoOrdinanzaPc, String aNote, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aGenPridGeneraleProcedimento,
			String aLuogoEsecuzioneSanzione, BigDecimal aNumGiorniSanzione, BigDecimal aNumMesiSanzione,
			BigDecimal aNumAnniSanzione) {
		this.mIdEsecuzioneSanzioneSost = aIdEsecuzioneSanzioneSost;
		this.mAnnoS07 = aAnnoS07;
		this.mProgrS07 = aProgrS07;
		this.mDataOrdinanza = aDataOrdinanza;
		this.mCodAutoritaEmittOrd = aCodAutoritaEmittOrd;
		this.mDescrAutoritaEmittOrd = aDescrAutoritaEmittOrd;
		this.mCodTipoAutoritaEmittOrd = aCodTipoAutoritaEmittOrd;
		this.mDescrTipoAutoritaEmittOrd = aDescrTipoAutoritaEmittOrd;
		this.mCodLuogoAutoritaEmittOrd = aCodLuogoAutoritaEmittOrd;
		this.mDescrLuogoAutoritaEmittOrd = aDescrLuogoAutoritaEmittOrd;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mDescrTipoSanzione = aDescrTipoSanzione;
		this.mDataInizioSanzione = aDataInizioSanzione;
		this.mDataTermineIniziale = aDataTermineIniziale;
		this.mDataTermineAttuale = aDataTermineAttuale;
		this.mDataDeclaratoriaESS = aDataDeclaratoriaESS;
		this.mDepDecIdDepositoDecreto = aDepDecIdDepositoDecreto;
		this.mDepOpidDepositoOrdinanzaPc = aDepOpidDepositoOrdinanzaPc;
		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mGenPridGeneraleProcedimento = aGenPridGeneraleProcedimento;
		this.mLuogoEsecuzioneSanzione = aLuogoEsecuzioneSanzione;
		this.mNumGiorniSanzione = aNumGiorniSanzione;
		this.mNumMesiSanzione = aNumMesiSanzione;
		this.mNumAnniSanzione = aNumAnniSanzione;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdEsecuzioneSanzioneSost() {
		return mIdEsecuzioneSanzioneSost;
	}

	public BigDecimal getAnnoS07() {
		return mAnnoS07;
	}

	public BigDecimal getProgrS07() {
		return mProgrS07;
	}

	public Date getDataOrdinanza() {
		return mDataOrdinanza;
	}

	public String getCodAutoritaEmittOrd() {
		return mCodAutoritaEmittOrd;
	}

	public String getDescrAutoritaEmittOrd() {
		return mDescrAutoritaEmittOrd;
	}

	public String getCodTipoAutoritaEmittOrd() {
		return mCodTipoAutoritaEmittOrd;
	}

	public String getDescrTipoAutoritaEmittOrd() {
		return mDescrTipoAutoritaEmittOrd;
	}

	public String getCodLuogoAutoritaEmittOrd() {
		return mCodLuogoAutoritaEmittOrd;
	}

	public String getDescrLuogoAutoritaEmittOrd() {
		return mDescrLuogoAutoritaEmittOrd;
	}

	public String getCodTipoSanzione() {
		return mCodTipoSanzione;
	}

	public String getDescrTipoSanzione() {
		return mDescrTipoSanzione;
	}

	public Date getDataInizioSanzione() {
		return mDataInizioSanzione;
	}

	public Date getDataTermineIniziale() {
		return mDataTermineIniziale;
	}

	public Date getDataTermineAttuale() {
		return mDataTermineAttuale;
	}

	public Date getDataDeclaratoriaESS() {
		return mDataDeclaratoriaESS;
	}

	public BigDecimal getDepDecIdDepositoDecreto() {
		return mDepDecIdDepositoDecreto;
	}

	public BigDecimal getDepOpidDepositoOrdinanzaPc() {
		return mDepOpidDepositoOrdinanzaPc;
	}

	public String getNote() {
		return mNote;
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

	public BigDecimal getGenPridGeneraleProcedimento() {
		return mGenPridGeneraleProcedimento;
	}

	public String getLuogoEsecuzioneSanzione() {
		return mLuogoEsecuzioneSanzione;
	}

	public BigDecimal getNumGiorniSanzione() {
		return mNumGiorniSanzione;
	}

	public BigDecimal getNumMesiSanzione() {
		return mNumMesiSanzione;
	}

	public BigDecimal getNumAnniSanzione() {
		return mNumAnniSanzione;
	}

	//
	// METODI SET()
	//

	public void setIdEsecuzioneSanzioneSost(BigDecimal aValore) {
		mIdEsecuzioneSanzioneSost = aValore;
	}

	public void setAnnoS07(BigDecimal aValore) {
		mAnnoS07 = aValore;
	}

	public void setProgrS07(BigDecimal aValore) {
		mProgrS07 = aValore;
	}

	public void setDataOrdinanza(Date aValore) {
		mDataOrdinanza = aValore;
	}

	public void setCodAutoritaEmittOrd(String aValore) {
		mCodAutoritaEmittOrd = aValore;
	}

	public void setDescrAutoritaEmittOrd(String aValore) {
		mDescrAutoritaEmittOrd = aValore;
	}

	public void setCodTipoAutoritaEmittOrd(String aValore) {
		mCodTipoAutoritaEmittOrd = aValore;
	}

	public void setDescrTipoAutoritaEmittOrd(String aValore) {
		mDescrTipoAutoritaEmittOrd = aValore;
	}

	public void setCodLuogoAutoritaEmittOrd(String aValore) {
		mCodLuogoAutoritaEmittOrd = aValore;
	}

	public void setDescrLuogoAutoritaEmittOrd(String aValore) {
		mDescrLuogoAutoritaEmittOrd = aValore;
	}

	public void setCodTipoSanzione(String aValore) {
		mCodTipoSanzione = aValore;
	}

	public void setDescrTipoSanzione(String aValore) {
		mDescrTipoSanzione = aValore;
	}

	public void setDataInizioSanzione(Date aValore) {
		mDataInizioSanzione = aValore;
	}

	public void setDataTermineIniziale(Date aValore) {
		mDataTermineIniziale = aValore;
	}

	public void setDataTermineAttuale(Date aValore) {
		mDataTermineAttuale = aValore;
	}

	public void setDataDeclaratoriaESS(Date aValore) {
		mDataDeclaratoriaESS = aValore;
	}

	public void setDepDecIdDepositoDecreto(BigDecimal aValore) {
		mDepDecIdDepositoDecreto = aValore;
	}

	public void setDepOpidDepositoOrdinanzaPc(BigDecimal aValore) {
		mDepOpidDepositoOrdinanzaPc = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
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

	public void setGenPridGeneraleProcedimento(BigDecimal aValore) {
		mGenPridGeneraleProcedimento = aValore;
	}

	public void setLuogoEsecuzioneSanzione(String aValore) {
		mLuogoEsecuzioneSanzione = aValore;
	}

	public void setNumGiorniSanzione(BigDecimal aValore) {
		mNumGiorniSanzione = aValore;
	}

	public void setNumMesiSanzione(BigDecimal aValore) {
		mNumMesiSanzione = aValore;
	}

	public void setNumAnniSanzione(BigDecimal aValore) {
		mNumAnniSanzione = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdEsecuzioneSanzioneSost + " - " + mAnnoS07 + " - " + mProgrS07 + " - " + mDataOrdinanza
				+ " - " + mCodAutoritaEmittOrd + " - " + mDescrAutoritaEmittOrd + " - "
				+ mCodTipoAutoritaEmittOrd + " - " + mDescrTipoAutoritaEmittOrd + " - "
				+ mCodLuogoAutoritaEmittOrd + " - " + mDescrLuogoAutoritaEmittOrd + " - " + mCodTipoSanzione
				+ " - " + mDescrTipoSanzione + " - " + mDataInizioSanzione + " - " + mDataTermineIniziale
				+ " - " + mDataTermineAttuale + " - " + mDataDeclaratoriaESS + " - "
				+ mDepDecIdDepositoDecreto + " - " + mDepOpidDepositoOrdinanzaPc + " - " + mNote + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mGenPridGeneraleProcedimento + " - " + mLuogoEsecuzioneSanzione + " - " + mNumGiorniSanzione
				+ " - " + mNumMesiSanzione + " - " + mNumAnniSanzione;

		return lStr;
	}

}