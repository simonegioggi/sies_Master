package siap.sius.motivazionedecreto.model;

/**
* <p>Title: MotivazioneDecretoModel</p>
* <p>Description: Classe Model che rappresenta il MotivazioneDecreto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class MotivazioneDecretoModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 763063979050112940L;
	private BigDecimal mIdMotivazioneDecreto;
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
	private BigDecimal mDepDecIdDepositoDecreto;
	private BigDecimal mProgrMotivazione;
	private BigDecimal mEveIdEvento;

	// COSTRUTTORE DI DEFAULT
	public MotivazioneDecretoModel() {
		this.mIdMotivazioneDecreto = null;
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
		this.mDepDecIdDepositoDecreto = null;
		this.mProgrMotivazione = null;
		this.mEveIdEvento = null;
	}

	// COSTRUTTORE DI COPIA
	public MotivazioneDecretoModel(MotivazioneDecretoModel aModel) {
		this.mIdMotivazioneDecreto = aModel.mIdMotivazioneDecreto;
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
		this.mDepDecIdDepositoDecreto = aModel.mDepDecIdDepositoDecreto;
		this.mProgrMotivazione = aModel.mProgrMotivazione;
		this.mEveIdEvento = aModel.mEveIdEvento;
	}

	// COSTRUTTORE MODEL
	public MotivazioneDecretoModel(BigDecimal aIdMotivazioneDecreto, String aCodTipoMotivazione,
			String aDescrMotivazione, String aAltraMotivazione, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aDepDecIdDepositoDecreto,
			BigDecimal aProgrMotivazione, BigDecimal aEveIdEvento) {
		this.mIdMotivazioneDecreto = aIdMotivazioneDecreto;
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
		this.mDepDecIdDepositoDecreto = aDepDecIdDepositoDecreto;
		this.mProgrMotivazione = aProgrMotivazione;
		this.mEveIdEvento = aEveIdEvento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdMotivazioneDecreto() {
		return mIdMotivazioneDecreto;
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

	public BigDecimal getDepDecIdDepositoDecreto() {
		return mDepDecIdDepositoDecreto;
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

	public void setIdMotivazioneDecreto(BigDecimal aValore) {
		mIdMotivazioneDecreto = aValore;
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

	public void setDepDecIdDepositoDecreto(BigDecimal aValore) {
		mDepDecIdDepositoDecreto = aValore;
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

		lStr = "" + mIdMotivazioneDecreto + " - " + mCodTipoMotivazione + " - " + mDescrMotivazione + " - "
				+ mAltraMotivazione + " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - "
				+ mCodUfficioInserimento + " - " +
				// mDescrUfficioInseriemento +" - " +
				mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " +
				// mDescrUfficioAggiornamento +" - " +
				mDepDecIdDepositoDecreto + " - " + mProgrMotivazione + " - " + mEveIdEvento;
		return lStr;
	}
}
