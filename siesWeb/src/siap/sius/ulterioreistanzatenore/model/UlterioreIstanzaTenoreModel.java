package siap.sius.ulterioreistanzatenore.model;

/**
* <p>Title: UlterioreIstanzaTenoreModel</p>
* <p>Description: Classe Model che rappresenta il UlterioreIstanzaTenore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class UlterioreIstanzaTenoreModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8062765198110213753L;

	private BigDecimal mIdUlterioreIstanzaTenore;
	private String mCodOggettoTenore;
	private String mDescrOggettoTenore;
	private Date mData;
	private String mCodDettaglioOggetto;
	private String mDescrDettaglioOggetto;
	private String mCodMagistrato;
	private String mDescrMagistrato;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mUltIstIdUlterioreIstanza;

	private Integer mProgrTenore;
	private String mCodEsitoTenore;

	// COSTRUTTORE DI DEFAULT
	public UlterioreIstanzaTenoreModel() {
		this.mIdUlterioreIstanzaTenore = null;
		this.mCodOggettoTenore = "";
		this.mDescrOggettoTenore = "";
		this.mData = null;
		this.mCodDettaglioOggetto = "";
		this.mDescrDettaglioOggetto = "";
		this.mCodMagistrato = "";
		this.mDescrMagistrato = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mUltIstIdUlterioreIstanza = null;

		this.mProgrTenore = null;
		this.mCodEsitoTenore = null;

	}

	// COSTRUTTORE DI COPIA
	public UlterioreIstanzaTenoreModel(UlterioreIstanzaTenoreModel aModel) {
		this.mIdUlterioreIstanzaTenore = aModel.mIdUlterioreIstanzaTenore;
		this.mCodOggettoTenore = aModel.mCodOggettoTenore;
		this.mDescrOggettoTenore = aModel.mDescrOggettoTenore;
		this.mData = aModel.mData;
		this.mCodDettaglioOggetto = aModel.mCodDettaglioOggetto;
		this.mDescrDettaglioOggetto = aModel.mDescrDettaglioOggetto;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mDescrMagistrato = aModel.mDescrMagistrato;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mUltIstIdUlterioreIstanza = aModel.mUltIstIdUlterioreIstanza;

		this.mProgrTenore = aModel.mProgrTenore;
		this.mCodEsitoTenore = aModel.mCodEsitoTenore;

	}

	// COSTRUTTORE MODEL
	public UlterioreIstanzaTenoreModel(BigDecimal aIdUlterioreIstanzaTenore, String aCodOggettoTenore,
			String aDescrOggettoTenore, Date aData, String aCodDettaglioOggetto,
			String aDescrDettaglioOggetto, String aCodMagistrato, String aDescrMagistrato,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aUltIstIdUlterioreIstanza) {
		this.mIdUlterioreIstanzaTenore = aIdUlterioreIstanzaTenore;
		this.mCodOggettoTenore = aCodOggettoTenore;
		this.mDescrOggettoTenore = aDescrOggettoTenore;
		this.mData = aData;
		this.mCodDettaglioOggetto = aCodDettaglioOggetto;
		this.mDescrDettaglioOggetto = aDescrDettaglioOggetto;
		this.mCodMagistrato = aCodMagistrato;
		this.mDescrMagistrato = aDescrMagistrato;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mUltIstIdUlterioreIstanza = aUltIstIdUlterioreIstanza;
		/*
		 * this.mProgrTenore = aProgrTenore; this.mCodEsitoTenore = aCodEsitoTenore;
		 */

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdUlterioreIstanzaTenore() {
		return mIdUlterioreIstanzaTenore;
	}

	public String getCodOggettoTenore() {
		return mCodOggettoTenore;
	}

	public String getDescrOggettoTenore() {
		return mDescrOggettoTenore;
	}

	public Date getData() {
		return mData;
	}

	public String getCodDettaglioOggetto() {
		return mCodDettaglioOggetto;
	}

	public String getDescrDettaglioOggetto() {
		return mDescrDettaglioOggetto;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getDescrMagistrato() {
		return mDescrMagistrato;
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

	public BigDecimal getUltIstIdUlterioreIstanza() {
		return mUltIstIdUlterioreIstanza;
	}

	public Integer getProgrTenore() {
		return mProgrTenore;
	}

	public String getCodEsitoTenore() {
		return mCodEsitoTenore;
	}

	//
	// METODI SET()
	//

	public void setIdUlterioreIstanzaTenore(BigDecimal aValore) {
		mIdUlterioreIstanzaTenore = aValore;
	}

	public void setCodOggettoTenore(String aValore) {
		mCodOggettoTenore = aValore;
	}

	public void setDescrOggettoTenore(String aValore) {
		mDescrOggettoTenore = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setCodDettaglioOggetto(String aValore) {
		mCodDettaglioOggetto = aValore;
	}

	public void setDescrDettaglioOggetto(String aValore) {
		mDescrDettaglioOggetto = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setDescrMagistrato(String aValore) {
		mDescrMagistrato = aValore;
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

	public void setUltIstIdUlterioreIstanza(BigDecimal aValore) {
		mUltIstIdUlterioreIstanza = aValore;
	}

	public void setProgrTenore(Integer aValore) {
		mProgrTenore = aValore;
	}

	public void setCodEsitoTenore(String aValore) {
		mCodEsitoTenore = aValore;
	}

}