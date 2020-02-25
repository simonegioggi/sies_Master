package siap.siep.sanzionesostitutiva.model;

/**
* <p>Title: SanzioneSostResiduaModel</p>
* <p>Description: Classe Model che rappresenta il SanzioneSostResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.StringUtils;

public class SanzioneSostResiduaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6601895114426368929L;

	private BigDecimal mIdSanzioneSostResidua;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mEveIdEvento;
	private BigDecimal mPenResIdPenaResidua;
	private String mCodTipoSanzione;
	private String mDescrTipoSanzione;
	private Date mDataInizio;
	private Date mDataFinePresunta;
	private Date mDataFine;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mSanzionePecuniariaMulta;
	private BigDecimal mSanzionePecuniariaAmmenda;
	private String mFlagValidato;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mStringaSanzione;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public SanzioneSostResiduaModel() {
		this.mIdSanzioneSostResidua = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mEveIdEvento = null;
		this.mPenResIdPenaResidua = null;
		this.mCodTipoSanzione = "";
		this.mDescrTipoSanzione = "";
		this.mDataInizio = null;
		this.mDataFinePresunta = null;
		this.mDataFine = null;
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mSanzionePecuniariaMulta = null;
		this.mSanzionePecuniariaAmmenda = null;
		this.mFlagValidato = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mStringaSanzione = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public SanzioneSostResiduaModel(SanzioneSostResiduaModel aModel) {
		this.mIdSanzioneSostResidua = aModel.mIdSanzioneSostResidua;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mPenResIdPenaResidua = aModel.mPenResIdPenaResidua;
		this.mCodTipoSanzione = aModel.mCodTipoSanzione;
		this.mDescrTipoSanzione = aModel.mDescrTipoSanzione;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFinePresunta = aModel.mDataFinePresunta;
		this.mDataFine = aModel.mDataFine;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mSanzionePecuniariaMulta = aModel.mSanzionePecuniariaMulta;
		this.mSanzionePecuniariaAmmenda = aModel.mSanzionePecuniariaAmmenda;
		this.mFlagValidato = aModel.mFlagValidato;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public SanzioneSostResiduaModel(BigDecimal aIdSanzioneSostResidua, BigDecimal aFasSieIdFascicoloSiep,
			BigDecimal aEveIdEvento, BigDecimal aPenResIdPenaResidua, String aCodTipoSanzione,
			String aDescrTipoSanzione, Date aDataInizio, Date aDataFinePresunta, Date aDataFine,
			BigDecimal aNumAnni, BigDecimal aNumMesi, BigDecimal aNumGiorni,
			BigDecimal aSanzionePecuniariaMulta, BigDecimal aSanzionePecuniariaAmmenda, String aFlagValidato,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento) {
		this.mIdSanzioneSostResidua = aIdSanzioneSostResidua;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mEveIdEvento = aEveIdEvento;
		this.mPenResIdPenaResidua = aPenResIdPenaResidua;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mDescrTipoSanzione = aDescrTipoSanzione;
		this.mDataInizio = aDataInizio;
		this.mDataFinePresunta = aDataFinePresunta;
		this.mDataFine = aDataFine;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mSanzionePecuniariaMulta = aSanzionePecuniariaMulta;
		this.mSanzionePecuniariaAmmenda = aSanzionePecuniariaAmmenda;
		this.mFlagValidato = aFlagValidato;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdSanzioneSostResidua() {
		return mIdSanzioneSostResidua;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getPenResIdPenaResidua() {
		return mPenResIdPenaResidua;
	}

	public String getCodTipoSanzione() {
		return mCodTipoSanzione;
	}

	public String getDescrTipoSanzione() {
		return mDescrTipoSanzione;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFinePresunta() {
		return mDataFinePresunta;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public BigDecimal getNumAnni() {
		return mNumAnni;
	}

	public BigDecimal getNumMesi() {
		return mNumMesi;
	}

	public BigDecimal getNumGiorni() {
		return mNumGiorni;
	}

	public BigDecimal getSanzionePecuniariaMulta() {
		return mSanzionePecuniariaMulta;
	}

	public BigDecimal getSanzionePecuniariaAmmenda() {
		return mSanzionePecuniariaAmmenda;
	}

	public String getFlagValidato() {
		return mFlagValidato;
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

	public String getStringaSanzione() {
		return mStringaSanzione;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdSanzioneSostResidua(BigDecimal aValore) {
		mIdSanzioneSostResidua = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setPenResIdPenaResidua(BigDecimal aValore) {
		mPenResIdPenaResidua = aValore;
	}

	public void setCodTipoSanzione(String aValore) {
		mCodTipoSanzione = aValore;
	}

	public void setDescrTipoSanzione(String aValore) {
		mDescrTipoSanzione = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFinePresunta(Date aValore) {
		mDataFinePresunta = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setNumAnni(BigDecimal aValore) {
		mNumAnni = aValore;
	}

	public void setNumMesi(BigDecimal aValore) {
		mNumMesi = aValore;
	}

	public void setNumGiorni(BigDecimal aValore) {
		mNumGiorni = aValore;
	}

	public void setSanzionePecuniariaMulta(BigDecimal aValore) {
		mSanzionePecuniariaMulta = aValore;
	}

	public void setSanzionePecuniariaAmmenda(BigDecimal aValore) {
		mSanzionePecuniariaAmmenda = aValore;
	}

	public void setFlagValidato(String aValore) {
		mFlagValidato = aValore;
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

	/**
	 * Metodo per la costruzione della stringa da visualizzare sulle stampe
	 *
	 */
	public void calcolaStringaSanzione() {
		String lString = this.mDescrTipoSanzione;

		if (this.mNumAnni != null) {
			if (this.mNumAnni.intValue() != 0)
				lString += " Anni " + this.mNumAnni;
		}

		if (this.getNumMesi() != null) {
			if (this.getNumMesi().intValue() != 0)
				lString += " Mesi " + this.mNumMesi;
		}

		if (this.mNumGiorni != null) {
			if (this.mNumGiorni.intValue() != 0)
				lString += " Giorni " + this.mNumGiorni;
		}

		if (this.mSanzionePecuniariaMulta != null && this.mSanzionePecuniariaMulta.intValue() != 0) {
			lString += ": Multa " + StringUtils.toEuroFormat(mSanzionePecuniariaMulta) + " Euro";
		}

		if (this.mSanzionePecuniariaAmmenda != null && this.mSanzionePecuniariaAmmenda.intValue() != 0) {
			lString += ": Ammenda " + StringUtils.toEuroFormat(mSanzionePecuniariaAmmenda) + " Euro";
		}

		if (lString.length() > 1)
			this.mStringaSanzione = lString;
		else
			this.mStringaSanzione = null;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SanzioneSostResiduaModel:\n" + "[ mIdSanzioneSostResidua     = " + mIdSanzioneSostResidua
				+ " ]\n" + "[ mFasSieIdFascicoloSiep     = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mEveIdEvento               = " + mEveIdEvento + " ]\n"
				+ "[ mPenResIdPenaResidua       = " + mPenResIdPenaResidua + " ]\n"
				+ "[ mCodTipoSanzione           = " + mCodTipoSanzione + " ]\n"
				+ "[ mDescrTipoSanzione         = " + mDescrTipoSanzione + " ]\n"
				+ "[ mDataInizio                = " + mDataInizio + " ]\n" + "[ mDataFinePresunta          = "
				+ mDataFinePresunta + " ]\n" + "[ mDataFine                  = " + mDataFine + " ]\n"
				+ "[ mNumAnni                   = " + mNumAnni + " ]\n" + "[ mNumMesi                   = "
				+ mNumMesi + " ]\n" + "[ mNumGiorni                 = " + mNumGiorni + " ]\n"
				+ "[ mSanzionePecuniariaMulta   = " + mSanzionePecuniariaMulta + " ]\n"
				+ "[ mSanzionePecuniariaAmmenda = " + mSanzionePecuniariaAmmenda + " ]\n"
				+ "[ mFlagValidato              = " + mFlagValidato + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

}