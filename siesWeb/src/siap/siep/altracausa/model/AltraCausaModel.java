package siap.siep.altracausa.model;

/**
* <p>Title: AltraCausaModel</p>
* <p>Description: Classe Model che rappresenta il AltraCausa</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import f3b.model.GenericModel;

public class AltraCausaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7556508859243278050L;

	private BigDecimal mIdAltraCausa;
	private BigDecimal mAnno;
	private String mNumero;
	private Date mData;
	private String mCodLuogo;
	private String mDescrLuogo;
	private String mCodAutorita;
	private String mDescrAutorita;
	private Date mDataDecorrenza;
	private Date mDataScadenza;
	private String mCodTipoPosGiuridica;
	private String mDescrTipoPosGiuridica;
	// modifica relativa al tipo istituto
	private String mIstDetIdIstitutoDetenzione;
	// private String mCodTipoIstituto;
	private String mDescrTipoIstituto;
	private String mDescrizioneTipoIstituto;
	// private String mCodLuogoIstituto;
	private String mDescrLuogoIstituto;
	private String mAltroLuogo;
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
	// modifica relativa al tipo istituto
	private IstitutoDetenzioneModel mIstitutoDetenzione;

	// COSTRUTTORE DI DEFAULT
	public AltraCausaModel() {
		this.mIdAltraCausa = null;
		this.mAnno = null;
		this.mNumero = "";
		this.mData = null;
		this.mCodLuogo = "";
		this.mDescrLuogo = "";
		this.mCodAutorita = "";
		this.mDescrAutorita = "";
		this.mDataDecorrenza = null;
		this.mDataScadenza = null;
		this.mCodTipoPosGiuridica = "";
		this.mDescrTipoPosGiuridica = "";
		// modifica del 10/03/2004 per trattare l'id dell'istitituto come un BigDecimal
		// this.mIstDetIdIstitutoDetenzione = "";
		this.mIstDetIdIstitutoDetenzione = null;
		// this.mCodTipoIstituto = "";
		this.mDescrTipoIstituto = "";
		this.mDescrizioneTipoIstituto = "";
		// this.mCodLuogoIstituto = "";
		this.mDescrLuogoIstituto = "";
		this.mAltroLuogo = "";
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
		// modifica relativa al tipo istituto
		this.mIstitutoDetenzione = null;

	}

	// COSTRUTTORE DI COPIA
	public AltraCausaModel(AltraCausaModel aModel) {
		this.mIdAltraCausa = aModel.mIdAltraCausa;
		this.mAnno = aModel.mAnno;
		this.mNumero = aModel.mNumero;
		this.mData = aModel.mData;
		this.mCodLuogo = aModel.mCodLuogo;
		this.mDescrLuogo = aModel.mDescrLuogo;
		this.mCodAutorita = aModel.mCodAutorita;
		this.mDescrAutorita = aModel.mDescrAutorita;
		this.mDataDecorrenza = aModel.mDataDecorrenza;
		this.mDataScadenza = aModel.mDataScadenza;
		this.mCodTipoPosGiuridica = aModel.mCodTipoPosGiuridica;
		this.mDescrTipoPosGiuridica = aModel.mDescrTipoPosGiuridica;
		// modifica relativa al tipo istituto
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		// this.mCodTipoIstituto = aModel.mCodTipoIstituto;
		this.mDescrTipoIstituto = aModel.mDescrTipoIstituto;
		this.mDescrizioneTipoIstituto = aModel.mDescrizioneTipoIstituto;
		// this.mCodLuogoIstituto = aModel.mCodLuogoIstituto;
		this.mDescrLuogoIstituto = aModel.mDescrLuogoIstituto;
		this.mAltroLuogo = aModel.mAltroLuogo;
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
		// modifica relativa al tipo istituto
		this.mIstitutoDetenzione = aModel.mIstitutoDetenzione;
	}

	// COSTRUTTORE MODEL
	public AltraCausaModel(BigDecimal aIdAltraCausa, BigDecimal aAnno, String aNumero, Date aData,
			String aCodLuogo, String aDescrLuogo, String aCodAutorita, String aDescrAutorita,
			Date aDataDecorrenza, Date aDataScadenza, String aCodTipoPosGiuridica,
			String aDescrTipoPosGiuridica,
			// modifica relativa al tipo istituto
			String aIstDetIdIstitutoDetenzione,
			// String aCodTipoIstituto,
			// String aDescrTipoIstituto,
			// String aCodLuogoIstituto,
			// String aDescrLuogoIstituto,
			String aAltroLuogo, String aNote, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep,
			// modifica relativa al tipo istituto
			IstitutoDetenzioneModel aIstitutoDetenzione) {
		this.mIdAltraCausa = aIdAltraCausa;
		this.mAnno = aAnno;
		this.mNumero = aNumero;
		this.mData = aData;
		this.mCodLuogo = aCodLuogo;
		this.mDescrLuogo = aDescrLuogo;
		this.mCodAutorita = aCodAutorita;
		this.mDescrAutorita = aDescrAutorita;
		this.mDataDecorrenza = aDataDecorrenza;
		this.mDataScadenza = aDataScadenza;
		this.mCodTipoPosGiuridica = aCodTipoPosGiuridica;
		this.mDescrTipoPosGiuridica = aDescrTipoPosGiuridica;
		// modifica relativa al tipo istituto
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		// this.mCodTipoIstituto = aCodTipoIstituto;
		// this.mDescrTipoIstituto = aDescrTipoIstituto;
		// this.mCodLuogoIstituto = aCodLuogoIstituto;
		// this.mDescrLuogoIstituto = aDescrLuogoIstituto;
		this.mAltroLuogo = aAltroLuogo;
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
		// modifica relativa al tipo istituto
		this.mIstitutoDetenzione = aIstitutoDetenzione;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAltraCausa() {
		return mIdAltraCausa;
	}

	public BigDecimal getAnno() {
		return mAnno;
	}

	public String getNumero() {
		return mNumero;
	}

	public Date getData() {
		return mData;
	}

	public String getCodLuogo() {
		return mCodLuogo;
	}

	public String getDescrLuogo() {
		return mDescrLuogo;
	}

	public String getCodAutorita() {
		return mCodAutorita;
	}

	public String getDescrAutorita() {
		return mDescrAutorita;
	}

	public Date getDataDecorrenza() {
		return mDataDecorrenza;
	}

	public Date getDataScadenza() {
		return mDataScadenza;
	}

	public String getCodTipoPosGiuridica() {
		return mCodTipoPosGiuridica;
	}

	public String getDescrTipoPosGiuridica() {
		return mDescrTipoPosGiuridica;
	}

	// modifica relativa al tipo istituto
	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	// public String getCodTipoIstituto() { return mCodTipoIstituto; }
	public String getDescrTipoIstituto() {
		return mDescrTipoIstituto;
	}

	public String getDescrizioneTipoIstituto() {
		return mDescrizioneTipoIstituto;
	}

	// public String getCodLuogoIstituto() { return mCodLuogoIstituto; }
	public String getDescrLuogoIstituto() {
		return mDescrLuogoIstituto;
	}

	public String getAltroLuogo() {
		return mAltroLuogo;
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

	// modifica relativa al tipo istituto
	public IstitutoDetenzioneModel getIstitutoDetenzione() {
		return mIstitutoDetenzione;
	}

	//
	// METODI SET()
	//

	public void setIdAltraCausa(BigDecimal aValore) {
		mIdAltraCausa = aValore;
	}

	public void setAnno(BigDecimal aValore) {
		mAnno = aValore;
	}

	public void setNumero(String aValore) {
		mNumero = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setCodLuogo(String aValore) {
		mCodLuogo = aValore;
	}

	public void setDescrLuogo(String aValore) {
		mDescrLuogo = aValore;
	}

	public void setCodAutorita(String aValore) {
		mCodAutorita = aValore;
	}

	public void setDescrAutorita(String aValore) {
		mDescrAutorita = aValore;
	}

	public void setDataDecorrenza(Date aValore) {
		mDataDecorrenza = aValore;
	}

	public void setDataScadenza(Date aValore) {
		mDataScadenza = aValore;
	}

	public void setCodTipoPosGiuridica(String aValore) {
		mCodTipoPosGiuridica = aValore;
	}

	public void setDescrTipoPosGiuridica(String aValore) {
		mDescrTipoPosGiuridica = aValore;
	}

	// modifica relativa al tipo istituto
	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	// public void setCodTipoIstituto(String aValore ) { mCodTipoIstituto = aValore; }
	public void setDescrTipoIstituto(String aValore) {
		mDescrTipoIstituto = aValore;
	}

	public void setDescrizioneTipoIstituto(String aValore) {
		mDescrizioneTipoIstituto = aValore;
	}

	// public void setCodLuogoIstituto(String aValore ) { mCodLuogoIstituto = aValore; }
	public void setDescrLuogoIstituto(String aValore) {
		mDescrLuogoIstituto = aValore;
	}

	public void setAltroLuogo(String aValore) {
		mAltroLuogo = aValore;
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

	// modifica relativa al tipo istituto
	public void setIstitutoDetenzione(IstitutoDetenzioneModel aValore) {
		mIstitutoDetenzione = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdAltraCausa + " - " + mAnno + " - " + mNumero + " - " + mData + " - " + mCodLuogo
				+ " - " + mDescrLuogo + " - " + mCodAutorita + " - " + mDescrAutorita + " - "
				+ mDataDecorrenza + " - " + mDataScadenza + " - " + mCodTipoPosGiuridica + " - "
				+ mDescrTipoPosGiuridica + " - " +
				// modifica relativa al tipo istituto
				mIstDetIdIstitutoDetenzione + " - " +
				// mCodTipoIstituto +" - " +
				mDescrTipoIstituto + " - " + mDescrizioneTipoIstituto + " - " +
				// mCodLuogoIstituto +" - " +
				mDescrLuogoIstituto + " - " + mAltroLuogo + " - " + mNote + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mFasSieIdFascicoloSiep + " - ";
		// modifica relativa al tipo istituto
		if (mIstitutoDetenzione != null)
			lStr += "" + mIstitutoDetenzione;

		return lStr;
	}

	public boolean isLiberoAltraCausa() {
		if (mCodTipoPosGiuridica != null && ( // mCodTipoPosGiuridica.equals("77") // Espiazione pena per
												// Altra Causa in Misura di Sicurezza Applicata in Via
												// Provvisoria
												// ||
		mCodTipoPosGiuridica.equals("78") // Custodia Cautelare per Altra Causa - Regime di Arresti
											// Domiciliari
				|| mCodTipoPosGiuridica.equals("79") // Custodia Cautelare per Altra Causa - Regime Permanenza
														// in Casa
				|| mCodTipoPosGiuridica.equals("80") // Custodia Cautelare per Altra Causa - Collocamento in
														// Comunità
				|| mCodTipoPosGiuridica.equals("81") // Custodia Cautelare per Altra Causa - Regime di Arresti
														// Domiciliari ex art 89 dpr 309/90
		)) {
			return true;
		} else
			return false;
	}

}