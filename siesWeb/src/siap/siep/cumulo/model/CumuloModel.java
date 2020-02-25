package siap.siep.cumulo.model;

/**
* <p>Title: CumuloModel</p>
* <p>Description: Classe Model che rappresenta il Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.model.GenericModel;

public class CumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8544940283436008717L;

	private BigDecimal mIdCumulo;
	private BigDecimal mIdFascicoloSiepCumulato;
	private BigDecimal mChiaveAnnoFasCumulato;
	private BigDecimal mChiaveProgrFasCumulato;
	private String mCodTipoUfficioFasCumulato;
	private String mDescrTipoUfficioFasCumulato;
	private String mCodLuogoUfficioFasCumulato;
	private String mDescrLuogoUfficioFasCumulato;
	private String mCodUfficioFasCumulato;
	private String mDescrUfficioFasCumulato;
	private String mCodTipoCumulo;
	private String mDescrTipoCumulo;
	private Date mDataRichiestaFascicolo;
	private Date mDataPervenimentoFascicolo;
	private Date mDataCumulo;
	private String mCodMotivoSospensioneCumulo;
	private String mDescrMotivoSospensioneCumulo;
	private Date mDataSospensioneCumulo;
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
	private String mFlagTipoStampa;
	private String mDescrTipoStampa;
	private BigDecimal mSenIdSentenza;
	private String mFlagValidato;

	private SentenzaModel mSentenza;
	private FascicoloSiepModel mFascicoloSiep;
	/*
	 * modifica del 08-06-2006 -- Dario -- Viviana -- aggiunto l'id dell'evento per permettere la
	 * cancellazione e l'annulamento sia dell'evento che del cumulo!!
	 */
	private BigDecimal mEveIdEvento;
	private String mPrimoCumulo;

	private BigDecimal mIstrIdIstruttoriaCumulo;

	// COSTRUTTORE DI DEFAULT
	public CumuloModel() {
		this.mIdCumulo = null;
		this.mIdFascicoloSiepCumulato = null;
		this.mChiaveAnnoFasCumulato = null;
		this.mChiaveProgrFasCumulato = null;
		this.mCodTipoUfficioFasCumulato = "";
		this.mDescrTipoUfficioFasCumulato = "";
		this.mCodLuogoUfficioFasCumulato = "";
		this.mDescrLuogoUfficioFasCumulato = "";
		this.mCodUfficioFasCumulato = "";
		this.mDescrUfficioFasCumulato = "";
		this.mCodTipoCumulo = "";
		this.mDescrTipoCumulo = "";
		this.mDataRichiestaFascicolo = null;
		this.mDataPervenimentoFascicolo = null;
		this.mDataCumulo = null;
		this.mCodMotivoSospensioneCumulo = "";
		this.mDescrMotivoSospensioneCumulo = "";
		this.mDataSospensioneCumulo = null;
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
		this.mFlagTipoStampa = "";
		this.mDescrTipoStampa = "";
		this.mSenIdSentenza = null;
		this.mFlagValidato = "";

		this.mEveIdEvento = null;
		this.mPrimoCumulo = "";
		this.mIstrIdIstruttoriaCumulo = null;

		this.mSentenza = null;
		this.mFascicoloSiep = null;

	}

	// COSTRUTTORE DI COPIA
	public CumuloModel(CumuloModel aModel) {
		this.mIdCumulo = aModel.mIdCumulo;
		this.mIdFascicoloSiepCumulato = aModel.mIdFascicoloSiepCumulato;
		this.mChiaveAnnoFasCumulato = aModel.mChiaveAnnoFasCumulato;
		this.mChiaveProgrFasCumulato = aModel.mChiaveProgrFasCumulato;
		this.mCodTipoUfficioFasCumulato = aModel.mCodTipoUfficioFasCumulato;
		this.mDescrTipoUfficioFasCumulato = aModel.mDescrTipoUfficioFasCumulato;
		this.mCodLuogoUfficioFasCumulato = aModel.mCodLuogoUfficioFasCumulato;
		this.mDescrLuogoUfficioFasCumulato = aModel.mDescrLuogoUfficioFasCumulato;
		this.mCodUfficioFasCumulato = aModel.mCodUfficioFasCumulato;
		this.mDescrUfficioFasCumulato = aModel.mDescrUfficioFasCumulato;
		this.mCodTipoCumulo = aModel.mCodTipoCumulo;
		this.mDescrTipoCumulo = aModel.mDescrTipoCumulo;
		this.mDataRichiestaFascicolo = aModel.mDataRichiestaFascicolo;
		this.mDataPervenimentoFascicolo = aModel.mDataPervenimentoFascicolo;
		this.mDataCumulo = aModel.mDataCumulo;
		this.mCodMotivoSospensioneCumulo = aModel.mCodMotivoSospensioneCumulo;
		this.mDescrMotivoSospensioneCumulo = aModel.mDescrMotivoSospensioneCumulo;
		this.mDataSospensioneCumulo = aModel.mDataSospensioneCumulo;
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
		this.mFlagTipoStampa = aModel.mFlagTipoStampa;
		this.mDescrTipoStampa = aModel.mDescrTipoStampa;
		this.mSenIdSentenza = aModel.mSenIdSentenza;
		this.mFlagValidato = aModel.mFlagValidato;

		this.mSentenza = aModel.mSentenza;
		this.mFascicoloSiep = aModel.mFascicoloSiep;

		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mPrimoCumulo = aModel.mPrimoCumulo;

		this.mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;

	}

	// COSTRUTTORE MODEL
	public CumuloModel(BigDecimal aIdCumulo, BigDecimal aIdFascicoloSiepCumulato,
			BigDecimal aChiaveAnnoFasCumulato, BigDecimal aChiaveProgrFasCumulato,
			String aCodTipoUfficioFasCumulato, String aDescrTipoUfficioFasCumulato,
			String aCodLuogoUfficioFasCumulato, String aDescrLuogoUfficioFasCumulato,
			String aCodUfficioFasCumulato, String aDescrUfficioFasCumulato, String aCodTipoCumulo,
			String aDescrTipoCumulo, Date aDataRichiestaFascicolo, Date aDataPervenimentoFascicolo,
			Date aDataCumulo, String aCodMotivoSospensioneCumulo, String aDescrMotivoSospensioneCumulo,
			Date aDataSospensioneCumulo, String aNote, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, String aFlagTipoStampa, String aDescrTipoStampa,
			BigDecimal aSenIdSentenza, String aFlagValidato, BigDecimal aEveIdEvento, String aPrimoCumulo,
			BigDecimal aIstrIdIstruttoriaCumulo) {
		this.mIdCumulo = aIdCumulo;
		this.mIdFascicoloSiepCumulato = aIdFascicoloSiepCumulato;
		this.mChiaveAnnoFasCumulato = aChiaveAnnoFasCumulato;
		this.mChiaveProgrFasCumulato = aChiaveProgrFasCumulato;
		this.mCodTipoUfficioFasCumulato = aCodTipoUfficioFasCumulato;
		this.mDescrTipoUfficioFasCumulato = aDescrTipoUfficioFasCumulato;
		this.mCodLuogoUfficioFasCumulato = aCodLuogoUfficioFasCumulato;
		this.mDescrLuogoUfficioFasCumulato = aDescrLuogoUfficioFasCumulato;
		this.mCodUfficioFasCumulato = aCodUfficioFasCumulato;
		this.mDescrUfficioFasCumulato = aDescrUfficioFasCumulato;
		this.mCodTipoCumulo = aCodTipoCumulo;
		this.mDescrTipoCumulo = aDescrTipoCumulo;
		this.mDataRichiestaFascicolo = aDataRichiestaFascicolo;
		this.mDataPervenimentoFascicolo = aDataPervenimentoFascicolo;
		this.mDataCumulo = aDataCumulo;
		this.mCodMotivoSospensioneCumulo = aCodMotivoSospensioneCumulo;
		this.mDescrMotivoSospensioneCumulo = aDescrMotivoSospensioneCumulo;
		this.mDataSospensioneCumulo = aDataSospensioneCumulo;
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
		this.mFlagTipoStampa = aFlagTipoStampa;
		this.mDescrTipoStampa = aDescrTipoStampa;
		this.mSenIdSentenza = aSenIdSentenza;
		this.mFlagValidato = aFlagValidato;
		this.mSentenza = null;

		this.mEveIdEvento = aEveIdEvento;
		this.mPrimoCumulo = aPrimoCumulo;

		this.mIstrIdIstruttoriaCumulo = aIstrIdIstruttoriaCumulo;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdCumulo() {
		return mIdCumulo;
	}

	public BigDecimal getIdFascicoloSiepCumulato() {
		return mIdFascicoloSiepCumulato;
	}

	public BigDecimal getChiaveAnnoFasCumulato() {
		return mChiaveAnnoFasCumulato;
	}

	public BigDecimal getChiaveProgrFasCumulato() {
		return mChiaveProgrFasCumulato;
	}

	public String getCodTipoUfficioFasCumulato() {
		return mCodTipoUfficioFasCumulato;
	}

	public String getDescrTipoUfficioFasCumulato() {
		return mDescrTipoUfficioFasCumulato;
	}

	public String getCodLuogoUfficioFasCumulato() {
		return mCodLuogoUfficioFasCumulato;
	}

	public String getDescrLuogoUfficioFasCumulato() {
		return mDescrLuogoUfficioFasCumulato;
	}

	public String getCodUfficioFasCumulato() {
		return mCodUfficioFasCumulato;
	}

	public String getDescrUfficioFasCumulato() {
		return mDescrUfficioFasCumulato;
	}

	public String getCodTipoCumulo() {
		return mCodTipoCumulo;
	}

	public String getDescrTipoCumulo() {
		return mDescrTipoCumulo;
	}

	public Date getDataRichiestaFascicolo() {
		return mDataRichiestaFascicolo;
	}

	public Date getDataPervenimentoFascicolo() {
		return mDataPervenimentoFascicolo;
	}

	public Date getDataCumulo() {
		return mDataCumulo;
	}

	public String getCodMotivoSospensioneCumulo() {
		return mCodMotivoSospensioneCumulo;
	}

	public String getDescrMotivoSospensioneCumulo() {
		return mDescrMotivoSospensioneCumulo;
	}

	public Date getDataSospensioneCumulo() {
		return mDataSospensioneCumulo;
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

	public String getFlagTipoStampa() {
		return mFlagTipoStampa;
	}

	public String getDescrTipoStampa() {
		return mDescrTipoStampa;
	}

	public BigDecimal getSenIdSentenza() {
		return mSenIdSentenza;
	}

	public String getFlagValidato() {
		return mFlagValidato;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getPrimoCumulo() {
		return mPrimoCumulo;
	}

	public BigDecimal getIstrIdIstruttoriaCumulo() {
		return mIstrIdIstruttoriaCumulo;
	}

	public SentenzaModel getSentenza() {
		return mSentenza;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	//
	// METODI SET()
	//

	public void setIdCumulo(BigDecimal aValore) {
		mIdCumulo = aValore;
	}

	public void setIdFascicoloSiepCumulato(BigDecimal aValore) {
		mIdFascicoloSiepCumulato = aValore;
	}

	public void setChiaveAnnoFasCumulato(BigDecimal aValore) {
		mChiaveAnnoFasCumulato = aValore;
	}

	public void setChiaveProgrFasCumulato(BigDecimal aValore) {
		mChiaveProgrFasCumulato = aValore;
	}

	public void setCodTipoUfficioFasCumulato(String aValore) {
		mCodTipoUfficioFasCumulato = aValore;
	}

	public void setDescrTipoUfficioFasCumulato(String aValore) {
		mDescrTipoUfficioFasCumulato = aValore;
	}

	public void setCodLuogoUfficioFasCumulato(String aValore) {
		mCodLuogoUfficioFasCumulato = aValore;
	}

	public void setDescrLuogoUfficioFasCumulato(String aValore) {
		mDescrLuogoUfficioFasCumulato = aValore;
	}

	public void setCodUfficioFasCumulato(String aValore) {
		mCodUfficioFasCumulato = aValore;
	}

	public void setDescrUfficioFasCumulato(String aValore) {
		mDescrUfficioFasCumulato = aValore;
	}

	public void setCodTipoCumulo(String aValore) {
		mCodTipoCumulo = aValore;
	}

	public void setDescrTipoCumulo(String aValore) {
		mDescrTipoCumulo = aValore;
	}

	public void setDataRichiestaFascicolo(Date aValore) {
		mDataRichiestaFascicolo = aValore;
	}

	public void setDataPervenimentoFascicolo(Date aValore) {
		mDataPervenimentoFascicolo = aValore;
	}

	public void setDataCumulo(Date aValore) {
		mDataCumulo = aValore;
	}

	public void setCodMotivoSospensioneCumulo(String aValore) {
		mCodMotivoSospensioneCumulo = aValore;
	}

	public void setDescrMotivoSospensioneCumulo(String aValore) {
		mDescrMotivoSospensioneCumulo = aValore;
	}

	public void setDataSospensioneCumulo(Date aValore) {
		mDataSospensioneCumulo = aValore;
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

	public void setFlagTipoStampa(String aValore) {
		mFlagTipoStampa = aValore;
	}

	public void setDescrTipoStampa(String aValore) {
		mDescrTipoStampa = aValore;
	}

	public void setSenIdSentenza(BigDecimal aValore) {
		mSenIdSentenza = aValore;
	}

	public void setFlagValidato(String aValore) {
		mFlagValidato = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setPrimoCumulo(String aValore) {
		mPrimoCumulo = aValore;
	}

	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		mIstrIdIstruttoriaCumulo = aValore;
	}

	public void setSentenza(SentenzaModel aValore) {
		mSentenza = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "CumuloModel:\n" + "[ mIdCumulo                   = " + mIdCumulo + " ]\n"
				+ "[ mIdFascicoloSiepCumulato    = " + mIdFascicoloSiepCumulato + " ]\n"
				+ "[ mIstrIdIstruttoriaCumulo    = " + mIstrIdIstruttoriaCumulo + " ]\n"
				+ "[ mChiaveAnnoFasCumulato      = " + mChiaveAnnoFasCumulato + " ]\n"
				+ "[ mChiaveProgrFasCumulato     = " + mChiaveProgrFasCumulato + " ]\n"
				+ "[ mCodTipoUfficioFasCumulato  = " + mCodTipoUfficioFasCumulato + " ]\n"
				+ "[ mDescrTipoUfficioFasCumulato  = " + mDescrTipoUfficioFasCumulato + " ]\n"
				+ "[ mCodLuogoUfficioFasCumulato = " + mCodLuogoUfficioFasCumulato + " ]\n"
				+ "[ mDescrLuogoUfficioFasCumulato = " + mDescrLuogoUfficioFasCumulato + " ]\n"
				+ "[ mCodUfficioFasCumulato      = " + mCodUfficioFasCumulato + " ]\n"
				+ "[ mDescrUfficioFasCumulato    = " + mDescrUfficioFasCumulato + " ]\n"
				+ "[ mCodTipoCumulo              = " + mCodTipoCumulo + " ]\n"
				+ "[ mDescrTipoCumulo            = " + mDescrTipoCumulo + " ]\n"
				+ "[ mDataRichiestaFascicolo     = " + mDataRichiestaFascicolo + " ]\n"
				+ "[ mDataPervenimentoFascicolo  = " + mDataPervenimentoFascicolo + " ]\n"
				+ "[ mDataCumulo                 = " + mDataCumulo + " ]\n"
				+ "[ mCodMotivoSospensioneCumulo = " + mCodMotivoSospensioneCumulo + " ]\n"
				+ "[ mDescrMotivoSospensioneCumulo = " + mDescrMotivoSospensioneCumulo + " ]\n"
				+ "[ mDataSospensioneCumulo      = " + mDataSospensioneCumulo + " ]\n"
				+ "[ mNote                       = " + mNote + " ]\n" + "[ mCodOperatoreInserimento    = "
				+ mCodOperatoreInserimento + " ]\n" + "[ mDataInserimento            = " + mDataInserimento
				+ " ]\n" + "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]\n"
				+ "[ mDescrUfficioInserimento    = " + mDescrUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento  = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento          = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mDescrUfficioAggiornamento   = " + mDescrUfficioAggiornamento + " ]\n"
				+ "[ mFasSieIdFascicoloSiep      = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mFlagTipoStampa             = " + mFlagTipoStampa + " ]\n"
				+ "[ mDescrTipoStampa            = " + mDescrTipoStampa + " ]\n"
				+ "[ mSenIdSentenza              = " + mSenIdSentenza + " ]\n"
				+ "[ mFlagValidato               = " + mFlagValidato + " ]\n"
				+ "[ mEveIdEvento                = " + mEveIdEvento + " ]\n"
				+ "[ mPrimoCumulo                = " + mPrimoCumulo + " ]";
		return lStr;
	}

}