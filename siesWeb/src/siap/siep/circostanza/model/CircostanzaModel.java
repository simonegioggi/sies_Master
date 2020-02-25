package siap.siep.circostanza.model;

/**
* <p>Title: CircostanzaModel</p>
* <p>Description: Classe Model che rappresenta il Circostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class CircostanzaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4272278320982064221L;

	private BigDecimal mIdCircostanza;
	private String mCodTipoCircostanza;
	private String mDescrTipoCircostanza;
	private String mCodFonte;
	private String mDescrFonte;
	private BigDecimal mAnnoFonte;
	private String mNumeroFonte;
	private String mCodSottonumerazione;
	private String mDescrSottonumerazione;
	private String mComma;
	private String mLettera;
	private String mNumero;
	private String mArticolo;
	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;

	// Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
	private String mFlagSentenzaApplicazPena;
	private String mCodBilanciamentoCircostanze;
	private String mDescrBilanciamentoCircostanze;
	private String mFlagGiudizioAbbreviato;
	// private BigDecimal mProgrCircostanze;
	private String mNoteBilanciamento;

	// ***************************************
	// Federica - a9-rr-078
	// aggiunto campo Comma-Qualificante
	private String mCommaQualificante;
	private String mDescrCommaQualificante;
	// ***************************************

	// COSTRUTTORE DI DEFAULT
	public CircostanzaModel() {
		this.mIdCircostanza = null;
		this.mCodTipoCircostanza = "";
		this.mDescrTipoCircostanza = "";
		this.mCodFonte = "";
		this.mDescrFonte = "";
		this.mAnnoFonte = null;
		this.mNumeroFonte = "";
		this.mCodSottonumerazione = "";
		this.mDescrSottonumerazione = "";
		this.mComma = "";
		this.mLettera = "";
		this.mNumero = "";
		this.mArticolo = "";
		this.mNote = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;

		// Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
		this.mFlagSentenzaApplicazPena = "";
		this.mCodBilanciamentoCircostanze = "";
		this.mDescrBilanciamentoCircostanze = "";
		this.mFlagGiudizioAbbreviato = "";
		// this.mProgrCircostanze = null;
		this.mNoteBilanciamento = "";
		// ***************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		this.mCommaQualificante = "";
		this.mDescrCommaQualificante = "";
		// ***************************************
	}

	// COSTRUTTORE DI COPIA
	public CircostanzaModel(CircostanzaModel aModel) {
		this.mIdCircostanza = aModel.mIdCircostanza;
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
		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		// Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
		this.mFlagSentenzaApplicazPena = aModel.mFlagSentenzaApplicazPena;
		this.mCodBilanciamentoCircostanze = aModel.mCodBilanciamentoCircostanze;
		this.mDescrBilanciamentoCircostanze = aModel.mDescrBilanciamentoCircostanze;
		this.mFlagGiudizioAbbreviato = aModel.mFlagGiudizioAbbreviato;
		// this.mProgrCircostanze = aModel.mProgrCircostanze;
		this.mNoteBilanciamento = aModel.mNoteBilanciamento;
		// ***************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		this.mCommaQualificante = aModel.mCommaQualificante;
		this.mDescrCommaQualificante = aModel.mDescrCommaQualificante;
		// ***************************************
	}

	// COSTRUTTORE MODEL
	public CircostanzaModel(BigDecimal aIdCircostanza, String aCodTipoCircostanza,
			String aDescrTipoCircostanza, String aCodFonte, String aDescrFonte, BigDecimal aAnnoFonte,
			String aNumeroFonte, String aCodSottonumerazione, String aDescrSottonumerazione, String aComma,
			String aLettera, String aNumero, String aArticolo, String aNote, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aFasSieIdFascicoloSiep,
			String aFlagSentenzaApplicazPena, String aCodBilanciamentoCircostanze,
			String aDescrBilanciamentoCircostanze, String aFlagGiudizioAbbreviato,
			// BigDecimal aProgrCircostanze,
			String aNoteBilanciamento,
			// ***************************************
			// Federica - a9-rr-078
			// aggiunto campo Comma-Qualificante
			String aCommaQualificante, String aDescrCommaQualificante
	// ***************************************
	) {
		this.mIdCircostanza = aIdCircostanza;
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
		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		// Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
		this.mFlagSentenzaApplicazPena = aFlagSentenzaApplicazPena;
		this.mCodBilanciamentoCircostanze = aCodBilanciamentoCircostanze;
		this.mDescrBilanciamentoCircostanze = aDescrBilanciamentoCircostanze;
		this.mFlagGiudizioAbbreviato = aFlagGiudizioAbbreviato;
		// this.mProgrCircostanze = aProgrCircostanze;
		this.mNoteBilanciamento = aNoteBilanciamento;
		// ***************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		this.mCommaQualificante = aCommaQualificante;
		this.mDescrCommaQualificante = aDescrCommaQualificante;
		// ***************************************
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdCircostanza() {
		return mIdCircostanza;
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

	public BigDecimal getAnnoFonte() {
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

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	// Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
	public String getFlagSentenzaApplicazPena() {
		return mFlagSentenzaApplicazPena;
	}

	public String getCodBilanciamentoCircostanze() {
		return mCodBilanciamentoCircostanze;
	}

	public String getDescrBilanciamentoCircostanze() {
		return mDescrBilanciamentoCircostanze;
	}

	public String getFlagGiudizioAbbreviato() {
		return mFlagGiudizioAbbreviato;
	}

	// public BigDecimal getProgrCircostanze() { return mProgrCircostanze; }
	public String getNoteBilanciamento() {
		return mNoteBilanciamento;
	}

	// ***************************************
	// Federica - a9-rr-078
	// aggiunto campo Comma-Qualificante
	public String getCommaQualificante() {
		return mCommaQualificante;
	}

	public String getDescrCommaQualificante() {
		return mDescrCommaQualificante;
	}
	// ***************************************

	//
	// METODI SET()
	//

	public void setIdCircostanza(BigDecimal aValore) {
		mIdCircostanza = aValore;
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

	public void setAnnoFonte(BigDecimal aValore) {
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

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	// Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
	public void setFlagSentenzaApplicazPena(String aValore) {
		mFlagSentenzaApplicazPena = aValore;
	}

	public void setCodBilanciamentoCircostanze(String aValore) {
		mCodBilanciamentoCircostanze = aValore;
	}

	public void setDescrBilanciamentoCircostanze(String aValore) {
		mDescrBilanciamentoCircostanze = aValore;
	}

	public void setFlagGiudizioAbbreviato(String aValore) {
		mFlagGiudizioAbbreviato = aValore;
	}

	// public void setProgrCircostanze(BigDecimal aValore) { mProgrCircostanze = aValore; }
	public void setNoteBilanciamento(String aValore) {
		mNoteBilanciamento = aValore;
	}

	// ***************************************
	// Federica - a9-rr-078
	// aggiunto campo Comma-Qualificante
	public void setCommaQualificante(String aValore) {
		mCommaQualificante = aValore;
	}

	public void setDescrCommaQualificante(String aValore) {
		mDescrCommaQualificante = aValore;
	}
	// ***************************************

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdCircostanza + " - " + mCodTipoCircostanza + " - " + mDescrTipoCircostanza + " - "
				+ mCodFonte + " - " + mDescrFonte + " - " + mAnnoFonte + " - " + mNumeroFonte + " - "
				+ mCodSottonumerazione + " - " + mDescrSottonumerazione + " - " + mComma + " - " + mLettera
				+ " - " + mNumero + " - " + mArticolo + " - " + mNote + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mFasSieIdFascicoloSiep + " - " + mFlagSentenzaApplicazPena + " - "
				+ mCodBilanciamentoCircostanze + " - " + mDescrBilanciamentoCircostanze + " - "
				+ mFlagGiudizioAbbreviato + " - " +
				// mProgrCircostanze +" - " +
				mNoteBilanciamento + " - " +
				// ***************************************
				// Federica - a9-rr-078
				// aggiunto campo Comma-Qualificante
				mCommaQualificante + " - " + mDescrCommaQualificante;
		// ***************************************

		return lStr;
	}

	/**
	 * Trasforma il reato in una stringa parlante costituita da tutti i valori del model
	 * 
	 * @return Stringa reato
	 */
	public String toStringCircostanza() {
		String lCirc = "";

		boolean lFlagAnnoNumero = false;
		if (this.getAnnoFonte() != null && this.getNumeroFonte() != null
				&& !this.getNumeroFonte().equals("")) {
			lFlagAnnoNumero = true;
		}
		if (lFlagAnnoNumero) {
			if (this.getDescrFonte() != null && !this.getDescrFonte().equals("")
					&& !this.getDescrFonte().equals("-"))
				lCirc += this.getDescrFonte() + " ";
			if (this.getAnnoFonte() != null)
				lCirc += this.getAnnoFonte().toString();
			if (this.getNumeroFonte() != null && !this.getNumeroFonte().equals(""))
				lCirc += "/" + this.getNumeroFonte();
		}

		if (this.getArticolo() != null && !this.getArticolo().equals(""))
			lCirc += "art." + this.getArticolo();
		if (this.getDescrSottonumerazione() != null && !this.getDescrSottonumerazione().equals("")
				&& !this.getDescrSottonumerazione().equals("-"))
			lCirc += " " + this.getDescrSottonumerazione();

		if (!lFlagAnnoNumero) {
			if (this.getDescrFonte() != null && !this.getDescrFonte().equals("")
					&& !this.getDescrFonte().equals("-"))
				lCirc += " " + this.getDescrFonte();
		}

		if (this.getComma() != null && !this.getComma().equals(""))
			lCirc += " c. " + this.getComma();
		// ***********************************************************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		if (this.getDescrCommaQualificante() != null && !this.getDescrCommaQualificante().equals("")
				&& !this.getDescrCommaQualificante().equals("-"))
			lCirc += " " + this.getDescrCommaQualificante();
		// ***********************************************************************************

		if (this.getLettera() != null && !this.getLettera().equals(""))
			lCirc += " l. " + this.getLettera();
		if (this.getNumero() != null && !this.getNumero().equals(""))
			lCirc += " n. " + this.getNumero();

		return lCirc;
	}

}