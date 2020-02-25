package siap.siep.archiviazione.model;

/**
* <p>Title: ArchiviazioneModel</p>
* <p>Description: Classe Model che rappresenta il Archiviazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.cssa.model.CSSAModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import f3b.model.GenericModel;

public class ArchiviazioneModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5469018408860569736L;

	private BigDecimal mIdArchiviazione;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private Date mDataEmissione;
	private Date mDataRicezione;
	private BigDecimal mAnnoNota;
	private String mNumNota;
	private String mCodProvvedimento;
	private String mDescrProvvedimento;
	private BigDecimal mAnnoProvvedimento;
	private String mNumProvvedimento;
	private String mCodTipoProvvedimentoArc;
	private String mDescrTipoProvvedimentoArc;
	private Date mDataDefinizione;
	private String mCodOggettoDefinizione;
	private String mDescrOggettoDefinizione;
	private String mCodTipoEmittente;
	private String mDescrTipoEmittente;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private String mIndirizzoEmittente;
	private String mAltraAutorita;
	private String mNote;
	private String mFlagAnnullamento;
	private Date mDataAnnullamento;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mEveIdEvento;
	private String mIstDetIdIstitutoDetenzione;
	private BigDecimal mCssIdCssa;
	private IstitutoDetenzioneModel mIstitutoDetenzione;
	private CSSAModel mCssa;
	// 02/04/2015
	private BigDecimal mChiaveAnno;
	private BigDecimal mChiaveProgr;
	// 22-06-2015
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public ArchiviazioneModel() {
		this.mIdArchiviazione = null;
		this.mCodTipoProvvedimento = "-";
		this.mDescrTipoProvvedimento = "";
		this.mDataEmissione = null;
		this.mDataRicezione = null;
		this.mAnnoNota = null;
		this.mNumNota = "";
		this.mCodProvvedimento = "-";
		this.mDescrProvvedimento = "";
		this.mAnnoProvvedimento = null;
		this.mNumProvvedimento = "";
		this.mCodTipoProvvedimentoArc = "-";
		this.mDescrTipoProvvedimentoArc = "";
		this.mDataDefinizione = null;
		this.mCodOggettoDefinizione = "-";
		this.mDescrOggettoDefinizione = "";
		this.mCodTipoEmittente = "-";
		this.mDescrTipoEmittente = "";
		this.mCodTipoAutoritaEmittente = "-";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodLuogoEmittente = "-";
		this.mDescrLuogoEmittente = "";
		this.mIndirizzoEmittente = "";
		this.mAltraAutorita = "";
		this.mNote = "";
		this.mFlagAnnullamento = "N";
		this.mDataAnnullamento = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mEveIdEvento = null;
		this.mIstDetIdIstitutoDetenzione = null;
		this.mCssIdCssa = null;
		this.mIstitutoDetenzione = null;
		this.mCssa = null;

		this.mChiaveAnno = null;
		this.mChiaveProgr = null;

		this.mCodOperatoreAggiornamento = null;
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = null;
	}

	// COSTRUTTORE DI COPIA
	public ArchiviazioneModel(ArchiviazioneModel aModel) {
		this.mIdArchiviazione = aModel.mIdArchiviazione;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mDataRicezione = aModel.mDataRicezione;
		this.mAnnoNota = aModel.mAnnoNota;
		this.mNumNota = aModel.mNumNota;
		this.mCodProvvedimento = aModel.mCodProvvedimento;
		this.mDescrProvvedimento = aModel.mDescrProvvedimento;
		this.mAnnoProvvedimento = aModel.mAnnoProvvedimento;
		this.mNumProvvedimento = aModel.mNumProvvedimento;
		this.mCodTipoProvvedimentoArc = aModel.mCodTipoProvvedimentoArc;
		this.mDescrTipoProvvedimentoArc = aModel.mDescrTipoProvvedimentoArc;
		this.mDataDefinizione = aModel.mDataDefinizione;
		this.mCodOggettoDefinizione = aModel.mCodOggettoDefinizione;
		this.mDescrOggettoDefinizione = aModel.mDescrOggettoDefinizione;
		this.mCodTipoEmittente = aModel.mCodTipoEmittente;
		this.mDescrTipoEmittente = aModel.mDescrTipoEmittente;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mIndirizzoEmittente = aModel.mIndirizzoEmittente;
		this.mAltraAutorita = aModel.mAltraAutorita;
		this.mNote = aModel.mNote;
		this.mFlagAnnullamento = aModel.mFlagAnnullamento;
		this.mDataAnnullamento = aModel.mDataAnnullamento;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mCssIdCssa = aModel.mCssIdCssa;
		this.mIstitutoDetenzione = aModel.mIstitutoDetenzione;
		this.mCssa = aModel.mCssa;

		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveProgr = aModel.mChiaveProgr;

		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	// COSTRUTTORE MODEL
	public ArchiviazioneModel(BigDecimal aIdArchiviazione, String aCodTipoProvvedimento,
			String aDescrTipoProvvedimento, Date aDataEmissione, Date aDataRicezione, BigDecimal aAnnoNota,
			String aNumNota, String aCodProvvedimento, String aDescrProvvedimento,
			BigDecimal aAnnoProvvedimento, String aNumProvvedimento, String aCodTipoProvvedimentoArc,
			String aDescrTipoProvvedimentoArc, Date aDataDefinizione, String aCodOggettoDefinizione,
			String aDescrOggettoDefinizione, String aCodTipoEmittente, String aDescrTipoEmittente,
			String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente, String aCodLuogoEmittente,
			String aDescrLuogoEmittente, String aIndirizzoEmittente, String aAltraAutorita, String aNote,
			String aFlagAnnullamento, Date aDataAnnullamento, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aEveIdEvento, String aIstDetIdIstitutoDetenzione,
			BigDecimal aCssIdCssa,

			BigDecimal aChiaveAnno, BigDecimal aChiaveProgr, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdArchiviazione = aIdArchiviazione;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mDataEmissione = aDataEmissione;
		this.mDataRicezione = aDataRicezione;
		this.mAnnoNota = aAnnoNota;
		this.mNumNota = aNumNota;
		this.mCodProvvedimento = aCodProvvedimento;
		this.mDescrProvvedimento = aDescrProvvedimento;
		this.mAnnoProvvedimento = aAnnoProvvedimento;
		this.mNumProvvedimento = aNumProvvedimento;
		this.mCodTipoProvvedimentoArc = aCodTipoProvvedimentoArc;
		this.mDescrTipoProvvedimentoArc = aDescrTipoProvvedimentoArc;
		this.mDataDefinizione = aDataDefinizione;
		this.mCodOggettoDefinizione = aCodOggettoDefinizione;
		this.mDescrOggettoDefinizione = aDescrOggettoDefinizione;
		this.mCodTipoEmittente = aCodTipoEmittente;
		this.mDescrTipoEmittente = aDescrTipoEmittente;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mIndirizzoEmittente = aIndirizzoEmittente;
		this.mAltraAutorita = aAltraAutorita;
		this.mNote = aNote;
		this.mFlagAnnullamento = aFlagAnnullamento;
		this.mDataAnnullamento = aDataAnnullamento;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mEveIdEvento = aEveIdEvento;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mCssIdCssa = aCssIdCssa;
		this.mIstitutoDetenzione = null;
		this.mCssa = null;

		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;

		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdArchiviazione() {
		return mIdArchiviazione;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public Date getDataRicezione() {
		return mDataRicezione;
	}

	public BigDecimal getAnnoNota() {
		return mAnnoNota;
	}

	public String getNumNota() {
		return mNumNota;
	}

	public String getCodProvvedimento() {
		return mCodProvvedimento;
	}

	public String getDescrProvvedimento() {
		return mDescrProvvedimento;
	}

	public BigDecimal getAnnoProvvedimento() {
		return mAnnoProvvedimento;
	}

	public String getNumProvvedimento() {
		return mNumProvvedimento;
	}

	public String getCodTipoProvvedimentoArc() {
		return mCodTipoProvvedimentoArc;
	}

	public String getDescrTipoProvvedimentoArc() {
		return mDescrTipoProvvedimentoArc;
	}

	public Date getDataDefinizione() {
		return mDataDefinizione;
	}

	public String getCodOggettoDefinizione() {
		return mCodOggettoDefinizione;
	}

	public String getDescrOggettoDefinizione() {
		return mDescrOggettoDefinizione;
	}

	public String getCodTipoEmittente() {
		return mCodTipoEmittente;
	}

	public String getDescrTipoEmittente() {
		return mDescrTipoEmittente;
	}

	public String getCodTipoAutoritaEmittente() {
		return mCodTipoAutoritaEmittente;
	}

	public String getDescrTipoAutoritaEmittente() {
		return mDescrTipoAutoritaEmittente;
	}

	public String getCodLuogoEmittente() {
		return mCodLuogoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	public String getIndirizzoEmittente() {
		return mIndirizzoEmittente;
	}

	public String getAltraAutorita() {
		return mAltraAutorita;
	}

	public String getNote() {
		return mNote;
	}

	public String getFlagAnnullamento() {
		return mFlagAnnullamento;
	}

	public Date getDataAnnullamento() {
		return mDataAnnullamento;
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

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public BigDecimal getCssIdCssa() {
		return mCssIdCssa;
	}

	public IstitutoDetenzioneModel getIstitutoDetenzione() {
		return mIstitutoDetenzione;
	}

	public CSSAModel getCssa() {
		return mCssa;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
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

	//
	// METODI SET()
	//
	public void setIdArchiviazione(BigDecimal aValore) {
		mIdArchiviazione = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setDataRicezione(Date aValore) {
		mDataRicezione = aValore;
	}

	public void setAnnoNota(BigDecimal aValore) {
		mAnnoNota = aValore;
	}

	public void setNumNota(String aValore) {
		mNumNota = aValore;
	}

	public void setCodProvvedimento(String aValore) {
		mCodProvvedimento = aValore;
	}

	public void setDescrProvvedimento(String aValore) {
		mDescrProvvedimento = aValore;
	}

	public void setAnnoProvvedimento(BigDecimal aValore) {
		mAnnoProvvedimento = aValore;
	}

	public void setNumProvvedimento(String aValore) {
		mNumProvvedimento = aValore;
	}

	public void setCodTipoProvvedimentoArc(String aValore) {
		mCodTipoProvvedimentoArc = aValore;
	}

	public void setDescrTipoProvvedimentoArc(String aValore) {
		mDescrTipoProvvedimentoArc = aValore;
	}

	public void setDataDefinizione(Date aValore) {
		mDataDefinizione = aValore;
	}

	public void setCodOggettoDefinizione(String aValore) {
		mCodOggettoDefinizione = aValore;
	}

	public void setDescrOggettoDefinizione(String aValore) {
		mDescrOggettoDefinizione = aValore;
	}

	public void setCodTipoEmittente(String aValore) {
		mCodTipoEmittente = aValore;
	}

	public void setDescrTipoEmittente(String aValore) {
		mDescrTipoEmittente = aValore;
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		mCodTipoAutoritaEmittente = aValore;
	}

	public void setDescrTipoAutoritaEmittente(String aValore) {
		mDescrTipoAutoritaEmittente = aValore;
	}

	public void setCodLuogoEmittente(String aValore) {
		mCodLuogoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	public void setIndirizzoEmittente(String aValore) {
		mIndirizzoEmittente = aValore;
	}

	public void setAltraAutorita(String aValore) {
		mAltraAutorita = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setFlagAnnullamento(String aValore) {
		mFlagAnnullamento = aValore;
	}

	public void setDataAnnullamento(Date aValore) {
		mDataAnnullamento = aValore;
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

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setCssIdCssa(BigDecimal aValore) {
		mCssIdCssa = aValore;
	}

	public void setIstitutoDetenzione(IstitutoDetenzioneModel aValore) {
		mIstitutoDetenzione = aValore;
	}

	public void setCssa(CSSAModel aValore) {
		mCssa = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
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

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdArchiviazione + " - " + mCodTipoProvvedimento + " - " + mDescrTipoProvvedimento + " - "
				+ mDataEmissione + " - " + mDataRicezione + " - " + mAnnoNota + " - " + mNumNota + " - "
				+ mCodProvvedimento + " - " + mDescrProvvedimento + " - " + mAnnoProvvedimento + " - "
				+ mNumProvvedimento + " - " + mCodTipoProvvedimentoArc + " - " + mDescrTipoProvvedimentoArc
				+ " - " + mDataDefinizione + " - " + mCodOggettoDefinizione + " - " + mDescrOggettoDefinizione
				+ " - " + mCodTipoEmittente + " - " + mDescrTipoEmittente + " - " + mCodTipoAutoritaEmittente
				+ " - " + mDescrTipoAutoritaEmittente + " - " + mCodLuogoEmittente + " - "
				+ mDescrLuogoEmittente + " - " + mIndirizzoEmittente + " - " + mAltraAutorita + " - " + mNote
				+ " - " + mFlagAnnullamento + " - " + mDataAnnullamento + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mFasSieIdFascicoloSiep + " - " + mEveIdEvento + " - " +

				// 02-04-2015
				mChiaveAnno + " - " + mChiaveProgr + " - " +

				mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " +
				//
				mIstDetIdIstitutoDetenzione + " - " + mCssIdCssa + " - ";
		if (mIstitutoDetenzione != null)
			lStr += "" + mIstitutoDetenzione;

		if (mCssa != null)
			lStr += "" + mCssa;

		return lStr;
	}

}