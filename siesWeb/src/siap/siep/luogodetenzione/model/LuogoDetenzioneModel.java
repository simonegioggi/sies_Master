package siap.siep.luogodetenzione.model;

/**
* <p>Title: LuogoDetenzioneModel</p>
* <p>Description: Classe Model che rappresenta il LuogoDetenzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;

public class LuogoDetenzioneModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 2344680810448812580L;
	private BigDecimal mIdLuogoDetenzione;
	// modifica relativa al tipo istituto
	private String mIstDetIdIstitutoDetenzione;
	// private String mCodTipoIstituto;

	// ---GDV--- Questi attributi sono mantenuti per la stampa
	private String mDescrTipoIstituto;
	private String mDescrLuogo;
	private String mIndirizzo;
	private String mDescr;
	// ---GDV--- Questi attributi sono mantenuti per la stampa

	// private String mCodLuogo;
	// private String mIndirizzo;
	private String mNote;
	private Date mDataInizioDetenzione;
	private Date mDataFineDetenzione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mFasSiuIdFascicoloSius;
	private BigDecimal mPosGiuIdPosizioneGiuridica;
	// modifica relativa al tipo istituto
	private String mAltroLuogo;
	// modifica relativa al tipo istituto
	private IstitutoDetenzioneModel mIstitutoDetenzione;

	// COSTRUTTORE DI DEFAULT
	public LuogoDetenzioneModel() {
		this.mIdLuogoDetenzione = null;
		// modifica relativa al tipo istituto
		// modifica del 10/03/2004 per trattare l'id dell'istitituto come un BigDecimal
		// this.mIstDetIdIstitutoDetenzione = "";
		this.mIstDetIdIstitutoDetenzione = null;
		// this.mCodTipoIstituto = "";
		this.mDescrTipoIstituto = "";
		// this.mCodLuogo = "";
		this.mDescrLuogo = "";
		this.mIndirizzo = "";
		// this.mDescr = "";
		this.mNote = "";
		this.mDataInizioDetenzione = null;
		this.mDataFineDetenzione = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mFasSiuIdFascicoloSius = null;
		this.mPosGiuIdPosizioneGiuridica = null;
		// modifica relativa al tipo istituto
		this.mAltroLuogo = "";
		// modifica relativa al tipo istituto
		this.mIstitutoDetenzione = null;

		// ---GDV--- Questi attributi sono mantenuti per la stampa
		this.mDescrTipoIstituto = "";
		this.mDescrLuogo = "";
		// ---GDV--- Questi attributi sono mantenuti per la stampa

	}

	// COSTRUTTORE DI COPIA
	public LuogoDetenzioneModel(LuogoDetenzioneModel aModel) {
		this.mIdLuogoDetenzione = aModel.mIdLuogoDetenzione;
		// modifica relativa al tipo istituto
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		// this.mCodTipoIstituto = aModel.mCodTipoIstituto;

		// ---GDV--- Questi attributi sono mantenuti per la stampa
		this.mDescrTipoIstituto = aModel.mDescrTipoIstituto;
		this.mDescrLuogo = aModel.mDescrLuogo;
		this.mDescr = aModel.mDescr;
		// ---GDV--- Questi attributi sono mantenuti per la stampa

		// this.mCodLuogo = aModel.mCodLuogo;
		// this.mIndirizzo = aModel.mIndirizzo;
		this.mNote = aModel.mNote;
		this.mDataInizioDetenzione = aModel.mDataInizioDetenzione;
		this.mDataFineDetenzione = aModel.mDataFineDetenzione;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mPosGiuIdPosizioneGiuridica = aModel.mPosGiuIdPosizioneGiuridica;
		// modifica relativa al tipo istituto
		this.mAltroLuogo = aModel.mAltroLuogo;
		// modifica relativa al tipo istituto
		this.mIstitutoDetenzione = aModel.mIstitutoDetenzione;

	}

	// COSTRUTTORE MODEL
	public LuogoDetenzioneModel(BigDecimal aIdLuogoDetenzione,
			// modifica relativa al tipo istituto
			String aIstDetIdIstitutoDetenzione,
			// String aCodTipoIstituto,
			// String aDescrTipoIstituto,
			// String aCodLuogo,
			// String aDescrLuogo,
			// String aIndirizzo,
			String aDescr, String aNote, Date aDataInizioDetenzione, Date aDataFineDetenzione,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aFasSiuIdFascicoloSius,
			BigDecimal aPosGiuIdPosizioneGiuridica,
			// modifica relativa al tipo istituto
			String aAltroLuogo,
			// modifica relativa al tipo istituto
			IstitutoDetenzioneModel aIstitutoDetenzione)

	{
		this.mIdLuogoDetenzione = aIdLuogoDetenzione;
		// modifica relativa al tipo istituto
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		// this.mCodTipoIstituto = aCodTipoIstituto;
		// this.mDescrTipoIstituto = aDescrTipoIstituto;
		// this.mCodLuogo = aCodLuogo;
		// this.mDescrLuogo = aDescrLuogo;
		// this.mIndirizzo = aIndirizzo;
		this.mDescr = aDescr;
		this.mNote = aNote;
		this.mDataInizioDetenzione = aDataInizioDetenzione;
		this.mDataFineDetenzione = aDataFineDetenzione;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mPosGiuIdPosizioneGiuridica = aPosGiuIdPosizioneGiuridica;
		// modifica relativa al tipo istituto
		this.mAltroLuogo = aAltroLuogo;
		// modifica relativa al tipo istituto
		this.mIstitutoDetenzione = aIstitutoDetenzione;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdLuogoDetenzione() {
		return mIdLuogoDetenzione;
	}

	// modifica relativa al tipo istituto
	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}
	// public String getCodTipoIstituto() { return mCodTipoIstituto; }

	// ---GDV--- Questi attributi sono mantenuti per la stampa
	public String getDescrTipoIstituto() {
		return mDescrTipoIstituto;
	}

	public String getDescrLuogo() {
		return mDescrLuogo;
	}

	public String getIndirizzo() {
		return mIndirizzo;
	}

	public String getDescr() {
		return mDescr;
	}
	// ---GDV--- Questi attributi sono mantenuti per la stampa

	// public String getCodLuogo() { return mCodLuogo; }
	public String getNote() {
		return mNote;
	}

	public Date getDataInizioDetenzione() {
		return mDataInizioDetenzione;
	}

	public Date getDataFineDetenzione() {
		return mDataFineDetenzione;
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

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public BigDecimal getPosGiuIdPosizioneGiuridica() {
		return mPosGiuIdPosizioneGiuridica;
	}

	// modifica relativa al tipo istituto
	public String getAltroLuogo() {
		return mAltroLuogo;
	}

	// modifica relativa al tipo istituto
	public IstitutoDetenzioneModel getIstitutoDetenzione() {
		return mIstitutoDetenzione;
	}

	//
	// METODI SET()
	//

	public void setIdLuogoDetenzione(BigDecimal aValore) {
		mIdLuogoDetenzione = aValore;
	}

	// modifica relativa al tipo istituto
	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}
	// public void setCodTipoIstituto(String aValore ) { mCodTipoIstituto = aValore; }

	// ---GDV--- Questi attributi sono mantenuti per la stampa
	public void setDescrTipoIstituto(String aValore) {
		mDescrTipoIstituto = aValore;
	}

	public void setDescrLuogo(String aValore) {
		mDescrLuogo = aValore;
	}

	public void setIndirizzo(String aValore) {
		mIndirizzo = aValore;
	}

	public void setDescr(String aValore) {
		mDescr = aValore;
	}
	// ---GDV--- Questi attributi sono mantenuti per la stampa

	// public void setCodLuogo(String aValore ) { mCodLuogo = aValore; }
	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setDataInizioDetenzione(Date aValore) {
		mDataInizioDetenzione = aValore;
	}

	public void setDataFineDetenzione(Date aValore) {
		mDataFineDetenzione = aValore;
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setPosGiuIdPosizioneGiuridica(BigDecimal aValore) {
		mPosGiuIdPosizioneGiuridica = aValore;
	}

	// modifica relativa al tipo istituto
	public void setAltroLuogo(String aValore) {
		mAltroLuogo = aValore;
	}

	// modifica relativa al tipo istituto
	public void setIstitutoDetenzione(IstitutoDetenzioneModel aValore) {
		mIstitutoDetenzione = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdLuogoDetenzione + " - " +
		// modifica relativa al tipo istituto
				mIstDetIdIstitutoDetenzione + " - " +
				// mCodTipoIstituto +" - " +
				mDescrTipoIstituto + " - " +
				// mCodLuogo +" - " +
				mDescrLuogo + " - " + mIndirizzo + " - " + mDescr + " - " + mNote + " - "
				+ mDataInizioDetenzione + " - " + mDataFineDetenzione + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mFasSieIdFascicoloSiep + " - " + mFasSiuIdFascicoloSius + " - "
				+ mPosGiuIdPosizioneGiuridica + " - " +
				// modifica relativa al tipo istituto
				mAltroLuogo;
		// modifica relativa al tipo istituto
		if (mIstitutoDetenzione != null)
			lStr += "" + mIstitutoDetenzione;

		return lStr;
	}
}
