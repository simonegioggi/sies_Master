package siap.sige.magistratosezione.model;

/**
* <p>Title: MagistratoSezioneModel</p>
* <p>Description: Classe Model che rappresenta il MagistratoSezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sige.sezione.model.SezioneModel;

public class MagistratoSezioneModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 8108350093000353373L;
	private String mMagCodMagistrato;
	private BigDecimal mSezIdSezione;
	// private String mCodUfficioAppartenenza;
	// private String mDescrUfficioAppartenenza;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private Date mDataInizioAssegnazione;
	private Date mDataFineAssegnazione;
	private String mFlagValidoSN;

	private SezioneModel mSezione;

	// COSTRUTTORE DI DEFAULT
	public MagistratoSezioneModel() {
		super();
	}

	// COSTRUTTORE DI COPIA
	public MagistratoSezioneModel(MagistratoSezioneModel aModel) {
		this.mMagCodMagistrato = aModel.mMagCodMagistrato;
		this.mSezIdSezione = aModel.mSezIdSezione;
		// this.mCodUfficioAppartenenza = aModel.mCodUfficioAppartenenza;
		// this.mDescrUfficioAppartenenza = aModel.mDescrUfficioAppartenenza;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mDataInizioAssegnazione = aModel.mDataInizioAssegnazione;
		this.mDataFineAssegnazione = aModel.mDataFineAssegnazione;
		this.mFlagValidoSN = aModel.mFlagValidoSN;

		this.mSezione = aModel.mSezione;
	}

	// COSTRUTTORE MODEL
	public MagistratoSezioneModel(String aMagCodMagistrato, BigDecimal aSezIdSezione,
			// String aCodUfficioAppartenenza,
			// String aDescrUfficioAppartenenza,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, Date aDataInizioAssegnazione, Date aDataFineAssegnazione,
			String aFlagValidoSN, String aDescrUfficioAggiornamento) {
		this.mMagCodMagistrato = aMagCodMagistrato;
		this.mSezIdSezione = aSezIdSezione;
		// this.mCodUfficioAppartenenza = aCodUfficioAppartenenza;
		// this.mDescrUfficioAppartenenza = aDescrUfficioAppartenenza;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mDataInizioAssegnazione = aDataInizioAssegnazione;
		this.mDataFineAssegnazione = aDataFineAssegnazione;
		this.mFlagValidoSN = aFlagValidoSN;
	}

	//
	// METODI GET()
	//
	public String getMagCodMagistrato() {
		return mMagCodMagistrato;
	}

	public BigDecimal getSezIdSezione() {
		return mSezIdSezione;
	}

	// public String getCodUfficioAppartenenza() { return mCodUfficioAppartenenza; }
	// public String getDescrUfficioAppartenenza() { return mDescrUfficioAppartenenza; }
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

	public Date getDataInizioAssegnazione() {
		return mDataInizioAssegnazione;
	}

	public Date getDataFineAssegnazione() {
		return mDataFineAssegnazione;
	}

	public String getFlagValidoSN() {
		return mFlagValidoSN;
	}

	public SezioneModel getSezione() {
		return mSezione;
	}

	//
	// METODI SET()
	//
	public void setMagCodMagistrato(String aValore) {
		mMagCodMagistrato = aValore;
	}

	public void setSezIdSezione(BigDecimal aValore) {
		mSezIdSezione = aValore;
	}

	// public void setCodUfficioAppartenenza(String aValore) { mCodUfficioAppartenenza = aValore; }
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

	public void setDataInizioAssegnazione(Date aValore) {
		mDataInizioAssegnazione = aValore;
	}

	public void setDataFineAssegnazione(Date aValore) {
		mDataFineAssegnazione = aValore;
	}

	public void setFlagValidoSN(String aValore) {
		mFlagValidoSN = aValore;
	}

	public void setSezione(SezioneModel aValore) {
		mSezione = aValore;
	}
}
