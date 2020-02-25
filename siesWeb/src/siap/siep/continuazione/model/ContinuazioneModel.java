package siap.siep.continuazione.model;

/**
* <p>Title: ContinuazioneModel</p>
* <p>Description: Classe Model che rappresenta il Continuazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class ContinuazioneModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6943603823798078102L;

	private BigDecimal mIdContinuazione;
	private BigDecimal mProgrContinuazione;
	private String mCodTipoContinuazione;
	private String mDescrTipoContinuazione;
	private String mCodTipoAutorita;
	private String mDescrTipoAutorita;
	private String mCodLuogoAutorita;
	private String mDescrLuogoAutorita;
	private Date mDataSentenza;
	private BigDecimal mAnnoSentenza;
	private String mNumSentenza;
	private BigDecimal mAnnoRegePm;
	private String mNumRegePm;
	private BigDecimal mAnnoRegeGip;
	private String mNumRegeGip;
	private BigDecimal mAnnoRegeDib;
	private String mNumRegeDib;
	private BigDecimal mAnnoRegeCas;
	private String mNumRegeCas;
	private BigDecimal mAnnoRegeCap;
	private String mNumRegeCap;
	private BigDecimal mAnnoRegeCasap;
	private String mNumRegeCasap;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mPenComIdPenaComplessiva;

	// COSTRUTTORE DI DEFAULT
	public ContinuazioneModel() {
		this.mIdContinuazione = null;
		this.mProgrContinuazione = null;
		this.mCodTipoContinuazione = "";
		this.mDescrTipoContinuazione = "";
		this.mCodTipoAutorita = "";
		this.mDescrTipoAutorita = "";
		this.mCodLuogoAutorita = "";
		this.mDescrLuogoAutorita = "";
		this.mDataSentenza = null;
		this.mAnnoSentenza = null;
		this.mNumSentenza = "";
		this.mAnnoRegePm = null;
		this.mNumRegePm = "";
		this.mAnnoRegeGip = null;
		this.mNumRegeGip = "";
		this.mAnnoRegeDib = null;
		this.mNumRegeDib = "";
		this.mAnnoRegeCas = null;
		this.mNumRegeCas = "";
		this.mAnnoRegeCap = null;
		this.mNumRegeCap = "";
		this.mAnnoRegeCasap = null;
		this.mNumRegeCasap = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mPenComIdPenaComplessiva = null;
	}

	// COSTRUTTORE DI COPIA
	public ContinuazioneModel(ContinuazioneModel aModel) {
		this.mIdContinuazione = aModel.mIdContinuazione;
		this.mProgrContinuazione = aModel.mProgrContinuazione;
		this.mCodTipoContinuazione = aModel.mCodTipoContinuazione;
		this.mDescrTipoContinuazione = aModel.mDescrTipoContinuazione;
		this.mCodTipoAutorita = aModel.mCodTipoAutorita;
		this.mDescrTipoAutorita = aModel.mDescrTipoAutorita;
		this.mCodLuogoAutorita = aModel.mCodLuogoAutorita;
		this.mDescrLuogoAutorita = aModel.mDescrLuogoAutorita;
		this.mDataSentenza = aModel.mDataSentenza;
		this.mAnnoSentenza = aModel.mAnnoSentenza;
		this.mNumSentenza = aModel.mNumSentenza;
		this.mAnnoRegePm = aModel.mAnnoRegePm;
		this.mNumRegePm = aModel.mNumRegePm;
		this.mAnnoRegeGip = aModel.mAnnoRegeGip;
		this.mNumRegeGip = aModel.mNumRegeGip;
		this.mAnnoRegeDib = aModel.mAnnoRegeDib;
		this.mNumRegeDib = aModel.mNumRegeDib;
		this.mAnnoRegeCas = aModel.mAnnoRegeCas;
		this.mNumRegeCas = aModel.mNumRegeCas;
		this.mAnnoRegeCap = aModel.mAnnoRegeCap;
		this.mNumRegeCap = aModel.mNumRegeCap;
		this.mAnnoRegeCasap = aModel.mAnnoRegeCasap;
		this.mNumRegeCasap = aModel.mNumRegeCasap;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mPenComIdPenaComplessiva = aModel.mPenComIdPenaComplessiva;
	}

	// COSTRUTTORE MODEL
	public ContinuazioneModel(BigDecimal aIdContinuazione, BigDecimal aProgrContinuazione,
			String aCodTipoContinuazione, String aDescrTipoContinuazione, String aCodTipoAutorita,
			String aDescrTipoAutorita, String aCodLuogoAutorita, String aDescrLuogoAutorita,
			Date aDataSentenza, BigDecimal aAnnoSentenza, String aNumSentenza, BigDecimal aAnnoRegePm,
			String aNumRegePm, BigDecimal aAnnoRegeGip, String aNumRegeGip, BigDecimal aAnnoRegeDib,
			String aNumRegeDib, BigDecimal aAnnoRegeCas, String aNumRegeCas, BigDecimal aAnnoRegeCap,
			String aNumRegeCap, BigDecimal aAnnoRegeCasap, String aNumRegeCasap,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aPenComIdPenaComplessiva) {
		this.mIdContinuazione = aIdContinuazione;
		this.mProgrContinuazione = aProgrContinuazione;
		this.mCodTipoContinuazione = aCodTipoContinuazione;
		this.mDescrTipoContinuazione = aDescrTipoContinuazione;
		this.mCodTipoAutorita = aCodTipoAutorita;
		this.mDescrTipoAutorita = aDescrTipoAutorita;
		this.mCodLuogoAutorita = aCodLuogoAutorita;
		this.mDescrLuogoAutorita = aDescrLuogoAutorita;
		this.mDataSentenza = aDataSentenza;
		this.mAnnoSentenza = aAnnoSentenza;
		this.mNumSentenza = aNumSentenza;
		this.mAnnoRegePm = aAnnoRegePm;
		this.mNumRegePm = aNumRegePm;
		this.mAnnoRegeGip = aAnnoRegeGip;
		this.mNumRegeGip = aNumRegeGip;
		this.mAnnoRegeDib = aAnnoRegeDib;
		this.mNumRegeDib = aNumRegeDib;
		this.mAnnoRegeCas = aAnnoRegeCas;
		this.mNumRegeCas = aNumRegeCas;
		this.mAnnoRegeCap = aAnnoRegeCap;
		this.mNumRegeCap = aNumRegeCap;
		this.mAnnoRegeCasap = aAnnoRegeCasap;
		this.mNumRegeCasap = aNumRegeCasap;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mPenComIdPenaComplessiva = aPenComIdPenaComplessiva;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdContinuazione() {
		return mIdContinuazione;
	}

	public BigDecimal getProgrContinuazione() {
		return mProgrContinuazione;
	}

	public String getCodTipoContinuazione() {
		return mCodTipoContinuazione;
	}

	public String getDescrTipoContinuazione() {
		return mDescrTipoContinuazione;
	}

	public String getCodTipoAutorita() {
		return mCodTipoAutorita;
	}

	public String getDescrTipoAutorita() {
		return mDescrTipoAutorita;
	}

	public String getCodLuogoAutorita() {
		return mCodLuogoAutorita;
	}

	public String getDescrLuogoAutorita() {
		return mDescrLuogoAutorita;
	}

	public Date getDataSentenza() {
		return mDataSentenza;
	}

	public BigDecimal getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public String getNumSentenza() {
		return mNumSentenza;
	}

	public BigDecimal getAnnoRegePm() {
		return mAnnoRegePm;
	}

	public String getNumRegePm() {
		return mNumRegePm;
	}

	public BigDecimal getAnnoRegeGip() {
		return mAnnoRegeGip;
	}

	public String getNumRegeGip() {
		return mNumRegeGip;
	}

	public BigDecimal getAnnoRegeDib() {
		return mAnnoRegeDib;
	}

	public String getNumRegeDib() {
		return mNumRegeDib;
	}

	public BigDecimal getAnnoRegeCas() {
		return mAnnoRegeCas;
	}

	public String getNumRegeCas() {
		return mNumRegeCas;
	}

	public BigDecimal getAnnoRegeCap() {
		return mAnnoRegeCap;
	}

	public String getNumRegeCap() {
		return mNumRegeCap;
	}

	public BigDecimal getAnnoRegeCasap() {
		return mAnnoRegeCasap;
	}

	public String getNumRegeCasap() {
		return mNumRegeCasap;
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

	public BigDecimal getPenComIdPenaComplessiva() {
		return mPenComIdPenaComplessiva;
	}

	//
	// METODI SET()
	//

	public void setIdContinuazione(BigDecimal aValore) {
		mIdContinuazione = aValore;
	}

	public void setProgrContinuazione(BigDecimal aValore) {
		mProgrContinuazione = aValore;
	}

	public void setCodTipoContinuazione(String aValore) {
		mCodTipoContinuazione = aValore;
	}

	public void setDescrTipoContinuazione(String aValore) {
		mDescrTipoContinuazione = aValore;
	}

	public void setCodTipoAutorita(String aValore) {
		mCodTipoAutorita = aValore;
	}

	public void setDescrTipoAutorita(String aValore) {
		mDescrTipoAutorita = aValore;
	}

	public void setCodLuogoAutorita(String aValore) {
		mCodLuogoAutorita = aValore;
	}

	public void setDescrLuogoAutorita(String aValore) {
		mDescrLuogoAutorita = aValore;
	}

	public void setDataSentenza(Date aValore) {
		mDataSentenza = aValore;
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumSentenza(String aValore) {
		mNumSentenza = aValore;
	}

	public void setAnnoRegePm(BigDecimal aValore) {
		mAnnoRegePm = aValore;
	}

	public void setNumRegePm(String aValore) {
		mNumRegePm = aValore;
	}

	public void setAnnoRegeGip(BigDecimal aValore) {
		mAnnoRegeGip = aValore;
	}

	public void setNumRegeGip(String aValore) {
		mNumRegeGip = aValore;
	}

	public void setAnnoRegeDib(BigDecimal aValore) {
		mAnnoRegeDib = aValore;
	}

	public void setNumRegeDib(String aValore) {
		mNumRegeDib = aValore;
	}

	public void setAnnoRegeCas(BigDecimal aValore) {
		mAnnoRegeCas = aValore;
	}

	public void setNumRegeCas(String aValore) {
		mNumRegeCas = aValore;
	}

	public void setAnnoRegeCap(BigDecimal aValore) {
		mAnnoRegeCap = aValore;
	}

	public void setNumRegeCap(String aValore) {
		mNumRegeCap = aValore;
	}

	public void setAnnoRegeCasap(BigDecimal aValore) {
		mAnnoRegeCasap = aValore;
	}

	public void setNumRegeCasap(String aValore) {
		mNumRegeCasap = aValore;
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

	public void setPenComIdPenaComplessiva(BigDecimal aValore) {
		mPenComIdPenaComplessiva = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdContinuazione + " - " + mProgrContinuazione + " - " + mCodTipoContinuazione + " - "
				+ mDescrTipoContinuazione + " - " + mCodTipoAutorita + " - " + mDescrTipoAutorita + " - "
				+ mCodLuogoAutorita + " - " + mDescrLuogoAutorita + " - " + mDataSentenza + " - "
				+ mAnnoSentenza + " - " + mNumSentenza + " - " + mAnnoRegePm + " - " + mNumRegePm + " - "
				+ mAnnoRegeGip + " - " + mNumRegeGip + " - " + mAnnoRegeDib + " - " + mNumRegeDib + " - "
				+ mAnnoRegeCas + " - " + mNumRegeCas + " - " + mAnnoRegeCap + " - " + mNumRegeCap + " - "
				+ mAnnoRegeCasap + " - " + mNumRegeCasap + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mPenComIdPenaComplessiva;

		return lStr;
	}

}