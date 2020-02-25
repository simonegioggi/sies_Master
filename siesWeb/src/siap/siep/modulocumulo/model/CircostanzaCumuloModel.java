package siap.siep.modulocumulo.model;

/**
* <p>Title: CircostanzaCumuloModel</p>
* <p>Description: Classe Model che rappresenta Circostanza_Cumulo</p>
* @version 1.0
*/

import java.util.Date;
import java.math.BigDecimal;

import siap.siep.circostanza.model.CircostanzaModel;

import f3b.model.GenericModel;

public class CircostanzaCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -955966374922679333L;

	private BigDecimal mIdCircostanzaCumulo;
	private String mCodTipoCircostanza;
	private String mDescrTipoCircostanza;
	private String mCodFonte;
	private String mDescrFonte;
	private BigDecimal mAnnoFonte;
	private String mNumeroFonte;
	private String mArticolo;
	private String mCodSottonumerazione;
	private String mDescrSottonumerazione;
	private String mComma;
	private String mCommaQualificante;
	private String mDescrCommaQualificante;
	private String mLettera;
	private String mNumero;

	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	private String mFlagSentenzaApplicazPena;
	private String mCodBilanciamentoCircostanze;
	private String mDescrBilanciamentoCircostanze;
	private String mFlagGiudizioAbbreviato;
	private String mNoteBilanciamento;

	private BigDecimal mIdCircostanzaOrigine;
	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mTitIdTitoloCumulato;

	// COSTRUTTORE DI DEFAULT (null e "")
	public CircostanzaCumuloModel() {
		this.mIdCircostanzaCumulo = null;
		this.mCodTipoCircostanza = "";
		this.mDescrTipoCircostanza = "";
		this.mCodFonte = "";
		this.mDescrFonte = "";
		this.mAnnoFonte = null;
		this.mNumeroFonte = "";
		this.mArticolo = "";
		this.mCodSottonumerazione = "";
		this.mDescrSottonumerazione = "";
		this.mComma = "";
		this.mCommaQualificante = "";
		this.mDescrCommaQualificante = "";
		this.mLettera = "";
		this.mNumero = "";

		this.mNote = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";

		this.mFlagSentenzaApplicazPena = "";
		this.mCodBilanciamentoCircostanze = "";
		this.mDescrBilanciamentoCircostanze = "";
		this.mFlagGiudizioAbbreviato = "";
		this.mNoteBilanciamento = "";

		this.mIdCircostanzaOrigine = null;
		this.mFlagStato = "";
		this.mMotivoModifica = "";
		this.mTitIdTitoloCumulato = null;

	}

	// COSTRUTTORE DI COPIA che istanzia un nuovo model CircostanzaCumuloModel caricandolo con il contenuto
	// del model passato in input
	public CircostanzaCumuloModel(CircostanzaCumuloModel aModel) {
		this.mIdCircostanzaCumulo = aModel.mIdCircostanzaCumulo;
		this.mCodTipoCircostanza = aModel.mCodTipoCircostanza;
		this.mDescrTipoCircostanza = aModel.mDescrTipoCircostanza;
		this.mCodFonte = aModel.mCodFonte;
		this.mDescrFonte = aModel.mDescrFonte;
		this.mAnnoFonte = aModel.mAnnoFonte;
		this.mNumeroFonte = aModel.mNumeroFonte;
		this.mArticolo = aModel.mArticolo;
		this.mCodSottonumerazione = aModel.mCodSottonumerazione;
		this.mDescrSottonumerazione = aModel.mDescrSottonumerazione;
		this.mComma = aModel.mComma;
		this.mCommaQualificante = aModel.mCommaQualificante;
		this.mDescrCommaQualificante = aModel.mDescrCommaQualificante;
		this.mLettera = aModel.mLettera;
		this.mNumero = aModel.mNumero;

		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;

		this.mFlagSentenzaApplicazPena = aModel.mFlagSentenzaApplicazPena;
		this.mCodBilanciamentoCircostanze = aModel.mCodBilanciamentoCircostanze;
		this.mDescrBilanciamentoCircostanze = aModel.mDescrBilanciamentoCircostanze;
		this.mFlagGiudizioAbbreviato = aModel.mFlagGiudizioAbbreviato;
		this.mNoteBilanciamento = aModel.mNoteBilanciamento;

		this.mIdCircostanzaOrigine = aModel.mIdCircostanzaOrigine;
		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;

	}

	// COSTRUTTORE che istanzia il model CircostanzaCumulo a partire dal Model Circostanza
	// per il ribaltamento dei dati originari nel cumulo
	public CircostanzaCumuloModel(CircostanzaModel aModel) {
		this.mCodTipoCircostanza = aModel.getCodTipoCircostanza();
		this.mDescrTipoCircostanza = aModel.getDescrTipoCircostanza();

		this.mCodFonte = aModel.getCodFonte();
		this.mDescrFonte = aModel.getDescrFonte();
		this.mAnnoFonte = aModel.getAnnoFonte();
		this.mNumeroFonte = aModel.getNumeroFonte();
		this.mArticolo = aModel.getArticolo();
		this.mCodSottonumerazione = aModel.getCodSottonumerazione();
		this.mDescrSottonumerazione = aModel.getDescrSottonumerazione();
		this.mComma = aModel.getComma();
		this.mCommaQualificante = aModel.getCommaQualificante();
		this.mDescrCommaQualificante = aModel.getDescrCommaQualificante();
		this.mLettera = aModel.getLettera();
		this.mNumero = aModel.getNumero();

		this.mNote = aModel.getNote();
		this.mFlagSentenzaApplicazPena = aModel.getFlagSentenzaApplicazPena();
		this.mCodBilanciamentoCircostanze = aModel.getCodBilanciamentoCircostanze();
		this.mDescrBilanciamentoCircostanze = aModel.getDescrBilanciamentoCircostanze();
		this.mFlagGiudizioAbbreviato = aModel.getFlagGiudizioAbbreviato();
		this.mNoteBilanciamento = aModel.getNoteBilanciamento();

		this.mIdCircostanzaOrigine = aModel.getIdCircostanza();

	}

	// COSTRUTTORE che istanzia un nuovo Model caricandolo con i dati passati in input.
	// Utilizzato dai DAO
	public CircostanzaCumuloModel(BigDecimal aIdCircostanzaCumulo, String aCodTipoCircostanza,
			String aDescrTipoCircostanza, String aCodFonte, String aDescrFonte, BigDecimal aAnnoFonte,
			String aNumeroFonte, String aArticolo, String aCodSottonumerazione, String aDescrSottonumerazione,
			String aComma, String aCommaQualificante, String aDescrCommaQualificante, String aLettera,
			String aNumero,

			String aNote, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			String aFlagSentenzaApplicazPena, String aCodBilanciamentoCircostanze,
			String aDescrBilanciamentoCircostanze, String aFlagGiudizioAbbreviato, String aNoteBilanciamento,

			BigDecimal aIdCircostanzaOrigine, String aFlagStato, String aMotivoModifica,
			BigDecimal aTitIdTitoloCumulato

	) {
		this.mIdCircostanzaCumulo = aIdCircostanzaCumulo;
		this.mCodTipoCircostanza = aCodTipoCircostanza;
		this.mDescrTipoCircostanza = aDescrTipoCircostanza;
		this.mCodFonte = aCodFonte;
		this.mDescrFonte = aDescrFonte;
		this.mAnnoFonte = aAnnoFonte;
		this.mNumeroFonte = aNumeroFonte;
		this.mArticolo = aArticolo;
		this.mCodSottonumerazione = aCodSottonumerazione;
		this.mDescrSottonumerazione = aDescrSottonumerazione;
		this.mComma = aComma;
		this.mCommaQualificante = aCommaQualificante;
		this.mDescrCommaQualificante = aDescrCommaQualificante;
		this.mLettera = aLettera;
		this.mNumero = aNumero;

		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;

		this.mFlagSentenzaApplicazPena = aFlagSentenzaApplicazPena;
		this.mCodBilanciamentoCircostanze = aCodBilanciamentoCircostanze;
		this.mDescrBilanciamentoCircostanze = aDescrBilanciamentoCircostanze;
		this.mFlagGiudizioAbbreviato = aFlagGiudizioAbbreviato;
		this.mNoteBilanciamento = aNoteBilanciamento;

		this.mIdCircostanzaOrigine = aIdCircostanzaOrigine;
		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdCircostanzaCumulo() {
		return mIdCircostanzaCumulo;
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

	public String getArticolo() {
		return mArticolo;
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

	public String getCommaQualificante() {
		return mCommaQualificante;
	}

	public String getDescrCommaQualificante() {
		return mDescrCommaQualificante;
	}

	public String getLettera() {
		return mLettera;
	}

	public String getNumero() {
		return mNumero;
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

	public String getNoteBilanciamento() {
		return mNoteBilanciamento;
	}

	public BigDecimal getIdCircostanzaOrigine() {
		return mIdCircostanzaOrigine;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	//
	// METODI SET()
	//

	public void setIdCircostanzaCumulo(BigDecimal aValore) {
		mIdCircostanzaCumulo = aValore;
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

	public void setArticolo(String aValore) {
		mArticolo = aValore;
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

	public void setCommaQualificante(String aValore) {
		mCommaQualificante = aValore;
	}

	public void setDescrCommaQualificante(String aValore) {
		mDescrCommaQualificante = aValore;
	}

	public void setLettera(String aValore) {
		mLettera = aValore;
	}

	public void setNumero(String aValore) {
		mNumero = aValore;
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

	public void setNoteBilanciamento(String aValore) {
		mNoteBilanciamento = aValore;
	}

	public void setIdCircostanzaOrigine(BigDecimal aValore) {
		mIdCircostanzaOrigine = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdCircostanzaCumulo + " - " + mCodTipoCircostanza + " - " + mDescrTipoCircostanza + " - "
				+ mCodFonte + " - " + mDescrFonte + " - " + mAnnoFonte + " - " + mNumeroFonte + " - "
				+ mArticolo + " - " + mCodSottonumerazione + " - " + mDescrSottonumerazione + " - " + mComma
				+ " - " + mCommaQualificante + " - " + mDescrCommaQualificante + " - " + mLettera + " - "
				+ mNumero + " - " +

				mNote + " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - "
				+ mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mFlagSentenzaApplicazPena + " - "
				+ mCodBilanciamentoCircostanze + " - " + mDescrBilanciamentoCircostanze + " - "
				+ mFlagGiudizioAbbreviato + " - " + mNoteBilanciamento + " - " +

				mIdCircostanzaOrigine + " - " + mFlagStato + " - " + mMotivoModifica + " - "
				+ mTitIdTitoloCumulato;

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

		if (this.getDescrCommaQualificante() != null && !this.getDescrCommaQualificante().equals("")
				&& !this.getDescrCommaQualificante().equals("-"))
			lCirc += " " + this.getDescrCommaQualificante();

		if (this.getLettera() != null && !this.getLettera().equals(""))
			lCirc += " l. " + this.getLettera();
		if (this.getNumero() != null && !this.getNumero().equals(""))
			lCirc += " n. " + this.getNumero();

		return lCirc;
	}

}