package siap.regesies.regecircostanza.model;

import java.util.Date;

import siap.regesies.model.RegeModel;
import siap.siep.circostanza.model.CircostanzaModel;

/**
 * <p>
 * Title: RegeCircostanzaModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il RegeCircostanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class RegeCircostanzaModel extends RegeModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 6893428678068936832L;

	private String mIdFile;
	private int mProgrCircostanza;
	private String mCodTipoCircostanza;
	private String mDescrTipoCircostanza;
	private String mCodFonte;
	private String mDescrFonte;
	private int mAnnoFonte;
	private String mNumeroFonte;
	private String mCodSottonumerazione;
	private String mDescrSottonumerazione;
	private String mComma;
	private String mLettera;
	private String mNumero;
	private String mArticolo;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public RegeCircostanzaModel() {
		this.mIdFile = "";
		this.mProgrCircostanza = 0;
		this.mCodTipoCircostanza = "";
		this.mDescrTipoCircostanza = "";
		this.mCodFonte = "";
		this.mDescrFonte = "";
		this.mAnnoFonte = 0;
		this.mNumeroFonte = "";
		this.mCodSottonumerazione = "";
		this.mDescrSottonumerazione = "";
		this.mComma = "";
		this.mLettera = "";
		this.mNumero = "";
		this.mArticolo = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
	}

	// COSTRUTTORE DI COPIA
	public RegeCircostanzaModel(RegeCircostanzaModel aModel) {
		this.mIdFile = aModel.mIdFile;
		this.mProgrCircostanza = aModel.mProgrCircostanza;
		this.mCodTipoCircostanza = aModel.mCodTipoCircostanza;
		this.mDescrTipoCircostanza = aModel.mDescrTipoCircostanza;
		this.mCodFonte = aModel.mCodFonte;
		this.mDescrFonte = aModel.mDescrFonte;
		this.mAnnoFonte = aModel.mAnnoFonte;
		this.mNumeroFonte = aModel.mNumeroFonte;
		this.mCodSottonumerazione = aModel.mCodSottonumerazione;
		this.mDescrSottonumerazione = aModel.mDescrSottonumerazione;
		this.mComma = aModel.mComma;
		this.mLettera = aModel.mLettera;
		this.mNumero = aModel.mNumero;
		this.mArticolo = aModel.mArticolo;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
	}

	// COSTRUTTORE MODEL
	public RegeCircostanzaModel(String aIdFile, int aProgrCircostanza, String aCodTipoCircostanza,
			String aDescrTipoCircostanza, String aCodFonte, String aDescrFonte, int aAnnoFonte,
			String aNumeroFonte, String aCodSottonumerazione, String aDescrSottonumerazione, String aComma,
			String aLettera, String aNumero, String aArticolo, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdFile = aIdFile;
		this.mProgrCircostanza = aProgrCircostanza;
		this.mCodTipoCircostanza = aCodTipoCircostanza;
		this.mDescrTipoCircostanza = aDescrTipoCircostanza;
		this.mCodFonte = aCodFonte;
		this.mDescrFonte = aDescrFonte;
		this.mAnnoFonte = aAnnoFonte;
		this.mNumeroFonte = aNumeroFonte;
		this.mCodSottonumerazione = aCodSottonumerazione;
		this.mDescrSottonumerazione = aDescrSottonumerazione;
		this.mComma = aComma;
		this.mLettera = aLettera;
		this.mNumero = aNumero;
		this.mArticolo = aArticolo;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		// this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
	}

	public CircostanzaModel toCircostanza() {
		CircostanzaModel lCirc = new CircostanzaModel();
		// lCirc.setIdFile(this.mIdFile);
		// lCirc.setProgrCircostanza(new BigDecimal(this.mProgrCircostanza));
		lCirc.setCodTipoCircostanza(this.mCodTipoCircostanza);
		lCirc.setDescrTipoCircostanza(this.mDescrTipoCircostanza);
		lCirc.setCodFonte(this.mCodFonte);
		lCirc.setDescrFonte(this.mDescrFonte);
		lCirc.setAnnoFonte(toBigDecimal(this.mAnnoFonte));
		lCirc.setNumeroFonte(this.mNumeroFonte);
		lCirc.setCodSottonumerazione(this.mCodSottonumerazione);
		lCirc.setDescrSottonumerazione(this.mDescrSottonumerazione);
		lCirc.setComma(this.mComma);
		lCirc.setLettera(this.mLettera);
		lCirc.setNumero(this.mNumero);
		lCirc.setArticolo(this.mArticolo);
		lCirc.setCodOperatoreInserimento(this.mCodOperatoreInserimento);
		lCirc.setDataInserimento(this.mDataInserimento);
		lCirc.setCodUfficioInserimento(this.mCodUfficioInserimento);
		lCirc.setDescrUfficioInserimento(this.mDescrUfficioInserimento);
		lCirc.setCodOperatoreAggiornamento(this.mCodOperatoreAggiornamento);
		lCirc.setDataAggiornamento(this.mDataAggiornamento);
		lCirc.setCodUfficioAggiornamento(this.mCodUfficioAggiornamento);
		lCirc.setDescrUfficioAggiornamento(this.mDescrUfficioAggiornamento);

		return lCirc;
	}

	//
	// METODI GET()
	//

	public String getIdFile() {
		return mIdFile;
	}

	public int getProgrCircostanza() {
		return mProgrCircostanza;
	}

	public String getCodTipoCircostanza() {
		return mCodTipoCircostanza;
	}

	public String getDescrTipoCircostanza() {
		return mDescrTipoCircostanza;
	}

	public String getCodFonte() {
		return mCodFonte;
	}

	public String getDescrFonte() {
		return mDescrFonte;
	}

	public int getAnnoFonte() {
		return mAnnoFonte;
	}

	public String getNumeroFonte() {
		return mNumeroFonte;
	}

	public String getCodSottonumerazione() {
		return mCodSottonumerazione;
	}

	public String getDescrSottonumerazione() {
		return mDescrSottonumerazione;
	}

	public String getComma() {
		return mComma;
	}

	public String getLettera() {
		return mLettera;
	}

	public String getNumero() {
		return mNumero;
	}

	public String getArticolo() {
		return mArticolo;
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

	//
	// METODI SET()
	//

	public void setIdFile(String aValore) {
		mIdFile = aValore;
	}

	public void setProgrCircostanza(int aValore) {
		mProgrCircostanza = aValore;
	}

	public void setCodTipoCircostanza(String aValore) {
		mCodTipoCircostanza = aValore;
	}

	public void setDescrTipoCircostanza(String aValore) {
		mDescrTipoCircostanza = aValore;
	}

	public void setCodFonte(String aValore) {
		mCodFonte = aValore;
	}

	public void setDescrFonte(String aValore) {
		mDescrFonte = aValore;
	}

	public void setAnnoFonte(int aValore) {
		mAnnoFonte = aValore;
	}

	public void setNumeroFonte(String aValore) {
		mNumeroFonte = aValore;
	}

	public void setCodSottonumerazione(String aValore) {
		mCodSottonumerazione = aValore;
	}

	public void setDescrSottonumerazione(String aValore) {
		mDescrSottonumerazione = aValore;
	}

	public void setComma(String aValore) {
		mComma = aValore;
	}

	public void setLettera(String aValore) {
		mLettera = aValore;
	}

	public void setNumero(String aValore) {
		mNumero = aValore;
	}

	public void setArticolo(String aValore) {
		mArticolo = aValore;
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

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdFile + " - " + mProgrCircostanza + " - " + mCodTipoCircostanza + " - "
				+ mDescrTipoCircostanza + " - " + mCodFonte + " - " + mDescrFonte + " - " + mAnnoFonte + " - "
				+ mNumeroFonte + " - " + mCodSottonumerazione + " - " + mDescrSottonumerazione + " - "
				+ mComma + " - " + mLettera + " - " + mNumero + " - " + mArticolo + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento;

		return lStr;
	}

}