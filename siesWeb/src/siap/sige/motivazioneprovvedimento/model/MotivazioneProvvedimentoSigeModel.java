package siap.sige.motivazioneprovvedimento.model;

/**
* <p>Title: MotivazioneProvvedimentoSigeModel</p>
* <p>Description: Classe Model che rappresenta MotivazioneProvvedimentoSige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

@SuppressWarnings("rawtypes")
public class MotivazioneProvvedimentoSigeModel extends GenericModel implements Comparable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3669748408169314819L;
	private BigDecimal mIdMotivazioneProvvedSige;
	private String mCodTipoMotivazione;
	private String mDescrMotivazione;
	private String mAltraMotivazione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mProSigIdProvvedSige;
	private BigDecimal mProgrMotivazione;
	private BigDecimal mEveIdEvento;

	// COSTRUTTORE DI DEFAULT
	public MotivazioneProvvedimentoSigeModel() {
		this.mIdMotivazioneProvvedSige = null;
		this.mCodTipoMotivazione = "";
		this.mDescrMotivazione = "";
		this.mAltraMotivazione = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mProSigIdProvvedSige = null;
		this.mProgrMotivazione = null;
		this.mEveIdEvento = null;
	}

	// COSTRUTTORE DI COPIA
	public MotivazioneProvvedimentoSigeModel(MotivazioneProvvedimentoSigeModel aModel) {
		this.mIdMotivazioneProvvedSige = aModel.mIdMotivazioneProvvedSige;
		this.mCodTipoMotivazione = aModel.mCodTipoMotivazione;
		this.mDescrMotivazione = aModel.mDescrMotivazione;
		this.mAltraMotivazione = aModel.mAltraMotivazione;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mProSigIdProvvedSige = aModel.mProSigIdProvvedSige;
		this.mProgrMotivazione = aModel.mProgrMotivazione;
		this.mEveIdEvento = aModel.mEveIdEvento;
	}

	// COSTRUTTORE MODEL
	public MotivazioneProvvedimentoSigeModel(BigDecimal aIdMotivazioneProvvedSige, String aCodTipoMotivazione,
			String aDescrMotivazione, String aAltraMotivazione, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aProSigIdProvvedSige, BigDecimal aProgrMotivazione,
			BigDecimal aEveIdEvento) {
		this.mIdMotivazioneProvvedSige = aIdMotivazioneProvvedSige;
		this.mCodTipoMotivazione = aCodTipoMotivazione;
		this.mDescrMotivazione = aDescrMotivazione;
		this.mAltraMotivazione = aAltraMotivazione;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mProSigIdProvvedSige = aProSigIdProvvedSige;
		this.mProgrMotivazione = aProgrMotivazione;
		this.mEveIdEvento = aEveIdEvento;
	}

	public MotivazioneProvvedimentoSigeModel(String aCodice) {
		this();
		mCodTipoMotivazione = aCodice;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdMotivazioneProvvedSige() {
		return mIdMotivazioneProvvedSige;
	}

	public String getCodTipoMotivazione() {
		return mCodTipoMotivazione;
	}

	public String getDescrMotivazione() {
		return mDescrMotivazione;
	}

	public String getAltraMotivazione() {
		return mAltraMotivazione;
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

	public BigDecimal getProSigIdProvvedSige() {
		return mProSigIdProvvedSige;
	}

	public BigDecimal getProgrMotivazione() {
		return mProgrMotivazione;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}
	//
	// METODI SET()
	//

	public void setIdMotivazioneProvvedSige(BigDecimal aValore) {
		mIdMotivazioneProvvedSige = aValore;
	}

	public void setCodTipoMotivazione(String aValore) {
		mCodTipoMotivazione = aValore;
	}

	public void setDescrMotivazione(String aValore) {
		mDescrMotivazione = aValore;
	}

	public void setAltraMotivazione(String aValore) {
		mAltraMotivazione = aValore;
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

	public void setDescrUfficioInseriemento(String aValore) {
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

	public void setProSigIdProvvedSige(BigDecimal aValore) {
		mProSigIdProvvedSige = aValore;
	}

	public void setProgrMotivazione(BigDecimal aValore) {
		mProgrMotivazione = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdMotivazioneProvvedSige + " - " + mCodTipoMotivazione + " - " + mDescrMotivazione
				+ " - " + mAltraMotivazione + " - " + mCodOperatoreInserimento + " - " + mDataInserimento
				+ " - " + mCodUfficioInserimento + " - " +
				// mDescrUfficioInseriemento +" - " +
				mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " +
				// mDescrUfficioAggiornamento +" - " +
				mProSigIdProvvedSige + " - " + mProgrMotivazione + " - " + mEveIdEvento;
		return lStr;
	}

	// Ordinamento per COD TIPO MOTIVAZIONE
	@Override
	public int compareTo(Object o) {
		int lRet = 0;
		MotivazioneProvvedimentoSigeModel lObj = (MotivazioneProvvedimentoSigeModel) o;

		if (mCodTipoMotivazione == null || lObj.getCodTipoMotivazione() == null)
			throw new IllegalArgumentException("Codice Motivazione non valorizzato ");

		// Confronto tra codice
		lRet = mCodTipoMotivazione.compareToIgnoreCase(lObj.getCodTipoMotivazione());
		return lRet;
	}
}
