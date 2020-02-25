package siap.sius.tenore.model;

/**
* <p>Title: TenoreModel</p>
* <p>Description: Classe Model che rappresenta il Tenore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class TenoreModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1419606141751380451L;

	private BigDecimal mIdTenore;
	private String mCodEsitoTenore;
	private String mDescrEsitoTenore;
	private Date mData;
	private String mCodMagistrato;
	private String mDescrMagistrato;
	private String mNote;
	private String mCodOggettoTenore;
	private String mDescrOggettoTenore;
	private BigDecimal mProgrTenore;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mGenPridGeneraleProcedimento;
	private BigDecimal mDepOpidDepositoOrdinanzaPc;
	private BigDecimal mImpIdImpugnazione;
	private BigDecimal mDepDecIdDepositoDecreto;
	private String mCodDettaglioOggetto;
	private String mDescrDettaglioOggetto;
	private Date mDataFine;
	private String mAbbrOggettoTenore; // STUB 06/05/2004 Abbreviazione Oggetto TENORE
	private BigDecimal mDepIdDepositoSentenza;

	// COSTRUTTORE DI DEFAULT
	public TenoreModel() {
		this.mIdTenore = null;
		this.mCodEsitoTenore = "";
		this.mDescrEsitoTenore = "";
		this.mData = null;
		this.mCodMagistrato = "";
		this.mDescrMagistrato = "";
		this.mNote = "";
		this.mCodOggettoTenore = "";
		this.mDescrOggettoTenore = "";
		this.mProgrTenore = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mGenPridGeneraleProcedimento = null;
		this.mDepOpidDepositoOrdinanzaPc = null;
		this.mImpIdImpugnazione = null;
		this.mDepDecIdDepositoDecreto = null;
		this.mCodDettaglioOggetto = "-";
		this.mDescrDettaglioOggetto = "";
		this.mDataFine = null;
		this.mAbbrOggettoTenore = ""; // STUB 06/05/2004
		this.mDepIdDepositoSentenza = null;
	}

	// COSTRUTTORE DI COPIA
	public TenoreModel(TenoreModel aModel) {
		this.mIdTenore = aModel.mIdTenore;
		this.mCodEsitoTenore = aModel.mCodEsitoTenore;
		this.mDescrEsitoTenore = aModel.mDescrEsitoTenore;
		this.mData = aModel.mData;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mDescrMagistrato = aModel.mDescrMagistrato;
		this.mNote = aModel.mNote;
		this.mCodOggettoTenore = aModel.mCodOggettoTenore;
		this.mDescrOggettoTenore = aModel.mDescrOggettoTenore;
		this.mProgrTenore = aModel.mProgrTenore;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mGenPridGeneraleProcedimento = aModel.mGenPridGeneraleProcedimento;
		this.mDepOpidDepositoOrdinanzaPc = aModel.mDepOpidDepositoOrdinanzaPc;
		this.mImpIdImpugnazione = aModel.mImpIdImpugnazione;
		this.mDepDecIdDepositoDecreto = aModel.mDepDecIdDepositoDecreto;
		this.mCodDettaglioOggetto = aModel.mCodDettaglioOggetto;
		this.mDescrDettaglioOggetto = aModel.mDescrDettaglioOggetto;
		this.mDataFine = aModel.mDataFine;
		this.mAbbrOggettoTenore = aModel.mAbbrOggettoTenore;
		this.mDepIdDepositoSentenza = aModel.mDepIdDepositoSentenza;
	}

	// COSTRUTTORE MODEL
	public TenoreModel(BigDecimal aIdTenore, String aCodEsitoTenore, String aDescrEsitoTenore, Date aData,
			String aCodMagistrato, String aDescrMagistrato, String aNote, String aCodOggettoTenore,
			String aDescrOggettoTenore, BigDecimal aProgrTenore, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aGenPridGeneraleProcedimento,
			BigDecimal aDepOpidDepositoOrdinanzaPc, BigDecimal aImpIdImpugnazione,
			BigDecimal aDepDecIdDepositoDecreto, String aCodDettaglioOggetto, String aDescrDettaglioOggetto,
			Date aDataFine, String aAbbrOggettoTenore, BigDecimal aDepIdDepositoSentenza) {
		this.mIdTenore = aIdTenore;
		this.mCodEsitoTenore = aCodEsitoTenore;
		this.mDescrEsitoTenore = aDescrEsitoTenore;
		this.mData = aData;
		this.mCodMagistrato = aCodMagistrato;
		this.mDescrMagistrato = aDescrMagistrato;
		this.mNote = aNote;
		this.mCodOggettoTenore = aCodOggettoTenore;
		this.mDescrOggettoTenore = aDescrOggettoTenore;
		this.mProgrTenore = aProgrTenore;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mGenPridGeneraleProcedimento = aGenPridGeneraleProcedimento;
		this.mDepOpidDepositoOrdinanzaPc = aDepOpidDepositoOrdinanzaPc;
		this.mImpIdImpugnazione = aImpIdImpugnazione;
		this.mDepDecIdDepositoDecreto = aDepDecIdDepositoDecreto;
		this.mCodDettaglioOggetto = aCodDettaglioOggetto;
		this.mDescrDettaglioOggetto = aDescrDettaglioOggetto;
		this.mDataFine = aDataFine;
		this.mAbbrOggettoTenore = aAbbrOggettoTenore; // STUB 06/05/2004
		this.mDepIdDepositoSentenza = aDepIdDepositoSentenza;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdTenore() {
		return mIdTenore;
	}

	public String getCodEsitoTenore() {
		return mCodEsitoTenore;
	}

	public String getDescrEsitoTenore() {
		return mDescrEsitoTenore;
	}

	public Date getData() {
		return mData;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getDescrMagistrato() {
		return mDescrMagistrato;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodOggettoTenore() {
		return mCodOggettoTenore;
	}

	public String getDescrOggettoTenore() {
		return mDescrOggettoTenore;
	}

	public BigDecimal getProgrTenore() {
		return mProgrTenore;
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

	public BigDecimal getDepOpidDepositoOrdinanzaPc() {
		return mDepOpidDepositoOrdinanzaPc;
	}

	public BigDecimal getImpIdImpugnazione() {
		return mImpIdImpugnazione;
	}

	public BigDecimal getDepDecIdDepositoDecreto() {
		return mDepDecIdDepositoDecreto;
	}

	public String getCodDettaglioOggetto() {
		return mCodDettaglioOggetto;
	}

	public String getDescrDettaglioOggetto() {
		return mDescrDettaglioOggetto;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public String getAbbrOggettoTenore() {
		return mAbbrOggettoTenore;
	}

	public BigDecimal getDepIdDepositoSentenza() {
		return mDepIdDepositoSentenza;
	}

	//
	// METODI SET()
	//
	public void setIdTenore(BigDecimal aValore) {
		mIdTenore = aValore;
	}

	public void setCodEsitoTenore(String aValore) {
		mCodEsitoTenore = aValore;
	}

	public void setDescrEsitoTenore(String aValore) {
		mDescrEsitoTenore = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setDescrMagistrato(String aValore) {
		mDescrMagistrato = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodOggettoTenore(String aValore) {
		mCodOggettoTenore = aValore;
	}

	public void setDescrOggettoTenore(String aValore) {
		mDescrOggettoTenore = aValore;
	}

	public void setProgrTenore(BigDecimal aValore) {
		mProgrTenore = aValore;
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

	public void setDepOpidDepositoOrdinanzaPc(BigDecimal aValore) {
		mDepOpidDepositoOrdinanzaPc = aValore;
	}

	public void setImpIdImpugnazione(BigDecimal aValore) {
		mImpIdImpugnazione = aValore;
	}

	public void setDepDecIdDepositoDecreto(BigDecimal aValore) {
		mDepDecIdDepositoDecreto = aValore;
	}

	public void setCodDettaglioOggetto(String aValore) {
		mCodDettaglioOggetto = aValore;
	}

	public void setDescrDettaglioOggetto(String aValore) {
		mDescrDettaglioOggetto = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setAbbrOggettoTenore(String aValore) {
		mAbbrOggettoTenore = aValore;
	}

	public void setDepIdDepositoSentenza(BigDecimal aValore) {
		mDepIdDepositoSentenza = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + "mIdTenore=" + mIdTenore + " - " + "mCodEsitoTenore=" + mCodEsitoTenore + " - "
				+ "mDescrEsitoTenore=" + mDescrEsitoTenore + " - " + "mData=" + mData + " - "
				+ "mCodMagistrato=" + mCodMagistrato + " - " + "mDescrMagistrato=" + mDescrMagistrato + " - "
				+ "mNote=" + mDescrMagistrato + " - " + "mCodOggettoTenore=" + mCodOggettoTenore + " - "
				+ "mDescrOggettoTenore=" + mDescrOggettoTenore + " - " + "mProgrTenore=" + mProgrTenore
				+ " - " + "mCodOperatoreInserimento=" + mCodOperatoreInserimento + " - " + "mDataInserimento="
				+ mDataInserimento + " - " + "mCodUfficioInserimento=" + mCodUfficioInserimento + " - "
				+ "mDescrUfficioInserimento=" + mDescrUfficioInserimento + " - "
				+ "mCodOperatoreAggiornamento=" + mCodOperatoreAggiornamento + " - " + "mDataAggiornamento="
				+ mDataAggiornamento + " - " + "mCodUfficioAggiornamento=" + mCodUfficioAggiornamento + " - "
				+ "mDescrUfficioAggiornamento=" + mDescrUfficioAggiornamento + " - "
				+ "mGenPridGeneraleProcedimento=" + mGenPridGeneraleProcedimento + " - "
				+ "mDepOpidDepositoOrdinanzaPc=" + mDepOpidDepositoOrdinanzaPc + " - " + "mImpIdImpugnazione="
				+ mImpIdImpugnazione + " - " + "mDepDecIdDepositoDecreto=" + mDepDecIdDepositoDecreto + " - "
				+ "mCodDettaglioOggetto=" + mCodDettaglioOggetto + " - " + "mDescrDettaglioOggetto="
				+ mDescrDettaglioOggetto + " - " + "mAbbrOggettoTenore=" + mAbbrOggettoTenore + " - "
				+ "mDataFine=" + mDataFine + " - " + "mDepIdDepositoSentenza=" + mDepIdDepositoSentenza;
		return lStr;
	}

}