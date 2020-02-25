package siap.sius.esecuzionemisurasicurezza.model;

/**
* <p>Title: EsecuzioneMisuraSicurezzaModel</p>
* <p>Description: Classe Model che rappresenta l' EsecuzioneMisuraSicurezza</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class EsecuzioneMisuraSicurezzaModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -4810348599266621350L;

	private BigDecimal mIdEsecuzioneMisuraSicurezza;
	private BigDecimal mAnnoS07;
	private BigDecimal mProgrS07;
	private Date mDataOrdinanza;
	private String mCodAutoritaEmittOrd;
	private String mDescrAutoritaEmittOrd;
	private String mCodTipoAutoritaEmittOrd;
	private String mDescrTipoAutoritaEmittOrd;
	private String mCodLuogoAutoritaEmittOrd;
	private String mDescrLuogoAutoritaEmittOrd;
	private String mCodTipoMisura;
	private String mDescrTipoMisura;
	private Date mDataInizioMisura;
	private Date mDataTermineIniziale;
	private Date mDataTermineAttuale;
	private Date mDataDeclaratoriaEMS;
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
	private String mLuogoEsecuzioneMisura;
	private BigDecimal mNumGiorniMisura;
	private BigDecimal mNumMesiMisura;
	private BigDecimal mNumAnniMisura;

	// COSTRUTTORE DI DEFAULT
	public EsecuzioneMisuraSicurezzaModel() {
		this.mIdEsecuzioneMisuraSicurezza = null;
		this.mAnnoS07 = null;
		this.mProgrS07 = null;
		this.mDataOrdinanza = null;
		this.mCodAutoritaEmittOrd = "";
		this.mDescrAutoritaEmittOrd = "";
		this.mCodTipoAutoritaEmittOrd = "";
		this.mDescrTipoAutoritaEmittOrd = "";
		this.mCodLuogoAutoritaEmittOrd = "";
		this.mDescrLuogoAutoritaEmittOrd = "";
		this.mCodTipoMisura = "";
		this.mDescrTipoMisura = "";
		this.mDataInizioMisura = null;
		this.mDataTermineIniziale = null;
		this.mDataTermineAttuale = null;
		this.mDataDeclaratoriaEMS = null;
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
		this.mLuogoEsecuzioneMisura = "";
		this.mNumGiorniMisura = null;
		this.mNumMesiMisura = null;
		this.mNumAnniMisura = null;
	}

	// COSTRUTTORE DI COPIA
	public EsecuzioneMisuraSicurezzaModel(EsecuzioneMisuraSicurezzaModel aModel) {
		this.mIdEsecuzioneMisuraSicurezza = aModel.mIdEsecuzioneMisuraSicurezza;
		this.mAnnoS07 = aModel.mAnnoS07;
		this.mProgrS07 = aModel.mProgrS07;
		this.mDataOrdinanza = aModel.mDataOrdinanza;
		this.mCodAutoritaEmittOrd = aModel.mCodAutoritaEmittOrd;
		this.mDescrAutoritaEmittOrd = aModel.mDescrAutoritaEmittOrd;
		this.mCodTipoAutoritaEmittOrd = aModel.mCodTipoAutoritaEmittOrd;
		this.mDescrTipoAutoritaEmittOrd = aModel.mDescrTipoAutoritaEmittOrd;
		this.mCodLuogoAutoritaEmittOrd = aModel.mCodLuogoAutoritaEmittOrd;
		this.mDescrLuogoAutoritaEmittOrd = aModel.mDescrLuogoAutoritaEmittOrd;
		this.mCodTipoMisura = aModel.mCodTipoMisura;
		this.mDescrTipoMisura = aModel.mDescrTipoMisura;
		this.mDataInizioMisura = aModel.mDataInizioMisura;
		this.mDataTermineIniziale = aModel.mDataTermineIniziale;
		this.mDataTermineAttuale = aModel.mDataTermineAttuale;
		this.mDataDeclaratoriaEMS = aModel.mDataDeclaratoriaEMS;
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
		this.mLuogoEsecuzioneMisura = aModel.mLuogoEsecuzioneMisura;
		this.mNumGiorniMisura = aModel.mNumGiorniMisura;
		this.mNumMesiMisura = aModel.mNumMesiMisura;
		this.mNumAnniMisura = aModel.mNumAnniMisura;
	}

	// COSTRUTTORE MODEL
	public EsecuzioneMisuraSicurezzaModel(BigDecimal aIdEsecuzioneMisuraSicurezza, BigDecimal aAnnoS07,
			BigDecimal aProgrS07, Date aDataOrdinanza, String aCodAutoritaEmittOrd,
			String aDescrAutoritaEmittOrd, String aCodTipoAutoritaEmittOrd, String aDescrTipoAutoritaEmittOrd,
			String aCodLuogoAutoritaEmittOrd, String aDescrLuogoAutoritaEmittOrd, String aCodTipoMisura,
			String aDescrTipoMisura, Date aDataInizioMisura, Date aDataTermineIniziale,
			Date aDataTermineAttuale, Date aDataDeclaratoriaEMS, BigDecimal aDepDecIdDepositoDecreto,
			BigDecimal aDepOpidDepositoOrdinanzaPc, String aNote, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aGenPridGeneraleProcedimento,
			String aLuogoEsecuzioneMisura, BigDecimal aNumGiorniMisura, BigDecimal aNumMesiMisura,
			BigDecimal aNumAnniMisura) {
		this.mIdEsecuzioneMisuraSicurezza = aIdEsecuzioneMisuraSicurezza;
		this.mAnnoS07 = aAnnoS07;
		this.mProgrS07 = aProgrS07;
		this.mDataOrdinanza = aDataOrdinanza;
		this.mCodAutoritaEmittOrd = aCodAutoritaEmittOrd;
		this.mDescrAutoritaEmittOrd = aDescrAutoritaEmittOrd;
		this.mCodTipoAutoritaEmittOrd = aCodTipoAutoritaEmittOrd;
		this.mDescrTipoAutoritaEmittOrd = aDescrTipoAutoritaEmittOrd;
		this.mCodLuogoAutoritaEmittOrd = aCodLuogoAutoritaEmittOrd;
		this.mDescrLuogoAutoritaEmittOrd = aDescrLuogoAutoritaEmittOrd;
		this.mCodTipoMisura = aCodTipoMisura;
		this.mDescrTipoMisura = aDescrTipoMisura;
		this.mDataInizioMisura = aDataInizioMisura;
		this.mDataTermineIniziale = aDataTermineIniziale;
		this.mDataTermineAttuale = aDataTermineAttuale;
		this.mDataDeclaratoriaEMS = aDataDeclaratoriaEMS;
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
		this.mLuogoEsecuzioneMisura = aLuogoEsecuzioneMisura;
		this.mNumGiorniMisura = aNumGiorniMisura;
		this.mNumMesiMisura = aNumMesiMisura;
		this.mNumAnniMisura = aNumAnniMisura;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdEsecuzioneMisuraSicurezza() {
		return mIdEsecuzioneMisuraSicurezza;
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

	public String getCodTipoMisura() {
		return mCodTipoMisura;
	}

	public String getDescrTipoMisura() {
		return mDescrTipoMisura;
	}

	public Date getDataInizioMisura() {
		return mDataInizioMisura;
	}

	public Date getDataTermineIniziale() {
		return mDataTermineIniziale;
	}

	public Date getDataTermineAttuale() {
		return mDataTermineAttuale;
	}

	public Date getDataDeclaratoriaEMS() {
		return mDataDeclaratoriaEMS;
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

	public String getLuogoEsecuzioneMisura() {
		return mLuogoEsecuzioneMisura;
	}

	public BigDecimal getNumGiorniMisura() {
		return mNumGiorniMisura;
	}

	public BigDecimal getNumMesiMisura() {
		return mNumMesiMisura;
	}

	public BigDecimal getNumAnniMisura() {
		return mNumAnniMisura;
	}

	//
	// METODI SET()
	//

	public void setIdEsecuzioneMisuraSicurezza(BigDecimal aValore) {
		mIdEsecuzioneMisuraSicurezza = aValore;
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

	public void setCodTipoMisura(String aValore) {
		mCodTipoMisura = aValore;
	}

	public void setDescrTipoMisura(String aValore) {
		mDescrTipoMisura = aValore;
	}

	public void setDataInizioMisura(Date aValore) {
		mDataInizioMisura = aValore;
	}

	public void setDataTermineIniziale(Date aValore) {
		mDataTermineIniziale = aValore;
	}

	public void setDataTermineAttuale(Date aValore) {
		mDataTermineAttuale = aValore;
	}

	public void setDataDeclaratoriaEMS(Date aValore) {
		mDataDeclaratoriaEMS = aValore;
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

	public void setLuogoEsecuzioneMisura(String aValore) {
		mLuogoEsecuzioneMisura = aValore;
	}

	public void setNumGiorniMisura(BigDecimal aValore) {
		mNumGiorniMisura = aValore;
	}

	public void setNumMesiMisura(BigDecimal aValore) {
		mNumMesiMisura = aValore;
	}

	public void setNumAnniMisura(BigDecimal aValore) {
		mNumAnniMisura = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdEsecuzioneMisuraSicurezza + " - " + mAnnoS07 + " - " + mProgrS07 + " - "
				+ mDataOrdinanza + " - " + mCodAutoritaEmittOrd + " - " + mDescrAutoritaEmittOrd + " - "
				+ mCodTipoAutoritaEmittOrd + " - " + mDescrTipoAutoritaEmittOrd + " - "
				+ mCodLuogoAutoritaEmittOrd + " - " + mDescrLuogoAutoritaEmittOrd + " - " + mCodTipoMisura
				+ " - " + mDescrTipoMisura + " - " + mDataInizioMisura + " - " + mDataTermineIniziale + " - "
				+ mDataTermineAttuale + " - " + mDataDeclaratoriaEMS + " - " + mDepDecIdDepositoDecreto
				+ " - " + mDepOpidDepositoOrdinanzaPc + " - " + mNote + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mGenPridGeneraleProcedimento + " - " + mLuogoEsecuzioneMisura + " - " + mNumGiorniMisura
				+ " - " + mNumMesiMisura + " - " + mNumAnniMisura;

		return lStr;
	}

}