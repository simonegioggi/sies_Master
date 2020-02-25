package siap.sige.tenore.model;

/**
* <p>Title: TenoreSigeModel</p>
* <p>Description: Classe Model che rappresenta il TenoreSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.F3BException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;

public class TenoreSigeModel extends GenericModel {

	/**
	*
	*/
	private static final long serialVersionUID = -8485354899413475603L;

	private BigDecimal mIdTenoreSige;
	private String mCodOggettoSige;
	private String mDescrOggettoSige;
	private String mCodEsitoSige;
	private String mDescrEsitoSige;
	private Date mData;
	private Date mDataFine;
	private BigDecimal mRicSigIdRichiestaSige;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private Date mDataInserimento;
	private String mCodOperatoreAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private Date mDataAggiornamento;
	private BigDecimal mReaIdReato;
	private BigDecimal mSenIdSentenza;
	private BigDecimal mProvIdProvvedimentoSige;
	private BigDecimal mFasIdFascicoloSige;
	private BigDecimal mTenSenReaId;
	private String mNote;
	private String mCodEsitoTenSenRea;
	private String mDescrEsitoTenSenRea;
	private String mFlagOggetto;
	// 23/11/2018 (email Nunzia del 22/11/2018 )ANOMALIA SIGE-STEP4 (OCCORRE ESTRARRE ANCHE LA DESCRIZIONE
	// DELL'OGGETTO PROCEDIMENTO)
	private String mDescrContenutoSige;
			 // 25/03/2018 intervento per richieste 11.2.1
			 private 	String  mCodContenutoSige;

	// COSTRUTTORE DI DEFAULT
	public TenoreSigeModel() {
		mIdTenoreSige = null;
		mCodOggettoSige = "";
		mDescrOggettoSige = "";
		mCodEsitoSige = "";
		mDescrEsitoSige = "";
		mData = null;
		mDataFine = null;
		mRicSigIdRichiestaSige = null;
		mCodOperatoreInserimento = "";
		mCodUfficioInserimento = "";
		mDescrUfficioInserimento = "";
		mDataInserimento = null;
		mCodOperatoreAggiornamento = "";
		mCodUfficioAggiornamento = "";
		mDescrUfficioAggiornamento = "";
		mDataAggiornamento = null;
		mSenIdSentenza = null;
		mReaIdReato = null;
		mProvIdProvvedimentoSige = null;
		mFasIdFascicoloSige = null;
		mTenSenReaId = null;
		mNote = "";
		mCodEsitoTenSenRea = "";
		mDescrEsitoTenSenRea = "";
		mFlagOggetto = "";
		mDescrContenutoSige = "";
		mCodContenutoSige ="";
	}

	// COSTRUTTORE DI COPIA
	public TenoreSigeModel(TenoreSigeModel aModel) {
		mIdTenoreSige = aModel.mIdTenoreSige;
		mCodOggettoSige = aModel.mCodOggettoSige;
		mDescrOggettoSige = aModel.mDescrOggettoSige;
		mCodEsitoSige = aModel.mCodEsitoSige;
		mDescrEsitoSige = aModel.mDescrEsitoSige;
		mData = aModel.mData;
		mDataFine = aModel.mDataFine;
		mRicSigIdRichiestaSige = aModel.mRicSigIdRichiestaSige;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mSenIdSentenza = aModel.mSenIdSentenza;
		mReaIdReato = aModel.mReaIdReato;
		mProvIdProvvedimentoSige = aModel.mProvIdProvvedimentoSige;
		mFasIdFascicoloSige = aModel.mFasIdFascicoloSige;
		mTenSenReaId = aModel.mTenSenReaId;
		mNote = aModel.mNote;
		mCodEsitoTenSenRea = aModel.mCodEsitoTenSenRea;
		mDescrEsitoTenSenRea = aModel.mDescrEsitoTenSenRea;
		mFlagOggetto = aModel.mFlagOggetto;
		mDescrContenutoSige = aModel.mDescrContenutoSige;
		mCodContenutoSige  = aModel.mCodContenutoSige;
	}

	// COSTRUTTORE MODEL
	public TenoreSigeModel(
				   BigDecimal	aIdTenoreSige,
				   String	 	aCodOggettoSige,
				   String	 	aDescrOggettoSige,
				   String	 	aCodEsitoSige,
				   String	 	aDescrEsitoSige,
				   Date	 		aData,
				   Date	 		aDataFine,
				   BigDecimal	aRicSigIdRichiestaSige,
				   String	 	aCodOperatoreInserimento,
				   String	 	aCodUfficioInserimento,
				   String	 	aDescrUfficioInserimento,
				   Date	 		aDataInserimento,
				   String	 	aCodOperatoreAggiornamento,
				   String	 	aCodUfficioAggiornamento,
				   String	 	aDescrUfficioAggiornamento,
				   Date	 		aDataAggiornamento,
				   BigDecimal	aSenIdSentenza,
				   BigDecimal	aReaIdReato,
				   BigDecimal	aProvIdProvvedimentoSige,
				   BigDecimal	aFasIdFascicoloSige,
				   BigDecimal	aTenSenReaId,
				   String 		aNote,
				   String	 	aCodEsitoTenSenRea,
				   String	 	aDescrEsitoTenSenRea,
				   String       aDescrContenutoSige,
				   String       aCodContenutoSige) {
		mIdTenoreSige = aIdTenoreSige;
		mCodOggettoSige = aCodOggettoSige;
		mDescrOggettoSige = aDescrOggettoSige;
		mCodEsitoSige = aCodEsitoSige;
		mDescrEsitoSige = aDescrEsitoSige;
		mData = aData;
		mDataFine = aDataFine;
		mRicSigIdRichiestaSige = aRicSigIdRichiestaSige;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
		mDataInserimento = aDataInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mSenIdSentenza = aSenIdSentenza;
		mReaIdReato = aReaIdReato;
		mProvIdProvvedimentoSige = aProvIdProvvedimentoSige;
		mFasIdFascicoloSige = aFasIdFascicoloSige;
		mTenSenReaId = aTenSenReaId;
		mNote = aNote;
		mCodEsitoTenSenRea = aCodEsitoTenSenRea;
		mDescrEsitoTenSenRea = aDescrEsitoTenSenRea;
		mDescrContenutoSige = aDescrContenutoSige;
		mCodContenutoSige = aCodContenutoSige;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdTenoreSige() {
		return mIdTenoreSige;
	}

	public String getCodOggettoSige() {
		return mCodOggettoSige;
	}

	public String getDescrOggettoSige() {
		return mDescrOggettoSige;
	}

	public String getCodEsitoSige() {
		return mCodEsitoSige;
	}

	public String getDescrEsitoSige() {
		return mDescrEsitoSige;
	}

	public Date getData() {
		return mData;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public BigDecimal getRicSigIdRichiestaSige() {
		return mRicSigIdRichiestaSige;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public BigDecimal getIdSentenza() {
		return mSenIdSentenza;
	}

	public BigDecimal getIdReato() {
		return mReaIdReato;
	}

	public BigDecimal getProvIdProvvedimentoSige() {
		return mProvIdProvvedimentoSige;
	}

	public BigDecimal getFasIdFascicoloSige() {
		return mFasIdFascicoloSige;
	}

	public BigDecimal getTenSenReaId() {
		return mTenSenReaId;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodEsitoTenSenRea() {
		return mCodEsitoTenSenRea;
	}

	public String getDescrEsitoTenSenRea() {
		return mDescrEsitoTenSenRea;
	}

	// 23/11/2018 (email Nunzia del 22/11/2018 )ANOMALIA SIGE-STEP4 (OCCORRE ESTRARRE ANCHE LA DESCRIZIONE
	// DELL'OGGETTO PROCEDIMENTO)
	public String getDescrContenutoSige() {
		return mDescrContenutoSige;
	}

	public String getFlagOggetto() {
		return mFlagOggetto;
	}

	// 25/03/2019 intervento per richiesta 11.2.1
	public String getCodContenutoSige() {
		return mCodContenutoSige;
	}

	//
	// METODI SET()
	//

	public void setIdTenoreSige(BigDecimal aValore) {
		mIdTenoreSige = aValore;
	}

	public void setCodOggettoSige(String aValore) {
		mCodOggettoSige = aValore;
	}

	public void setDescrOggettoSige(String aValore) {
		mDescrOggettoSige = aValore;
	}

	public void setCodEsitoSige(String aValore) {
		mCodEsitoSige = aValore;
	}

	public void setDescrEsitoSige(String aValore) {
		mDescrEsitoSige = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setRicSigIdRichiestaSige(BigDecimal aValore) {
		mRicSigIdRichiestaSige = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setIdSentenza(BigDecimal aValore) {
		mSenIdSentenza = aValore;
	}

	public void setIdReato(BigDecimal aValore) {
		mReaIdReato = aValore;
	}

	public void setProvIdProvvedimentoSige(BigDecimal aValore) {
		mProvIdProvvedimentoSige = aValore;
	}

	public void setFasIdFascicoloSige(BigDecimal aValore) {
		mFasIdFascicoloSige = aValore;
	}

	public void setTenSenReaId(BigDecimal aValore) {
		mTenSenReaId = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodEsitoTenSenRea(String aValore) {
		mCodEsitoTenSenRea = aValore;
	}

	public void setDescrEsitoTenSenRea(String aValore) {
		mDescrEsitoTenSenRea = aValore;
	}

	public void setFlagOggetto(String aValore) {
		mFlagOggetto = aValore;
	}

	// 23/11/2018 (email Nunzia del 22/11/2018 )ANOMALIA SIGE-STEP4 (OCCORRE ESTRARRE ANCHE LA DESCRIZIONE
	// DELL'OGGETTO PROCEDIMENTO)
	public void setDescrContenutoSige(String aValore) {
		mDescrContenutoSige = aValore;
	}

	// 25/03/2019 intervento per richiesta 11.2.1
	public void setCodContenutoSige(String aValore) {
		mCodContenutoSige= aValore;
	}

	public TenoreSigeModel decodifica() throws F3BException {
		try {
			// Decodifica Oggetto
			/*
			 * if (mCodOggettoSige != null && mCodOggettoSige.trim().length() > 0)
			 * setDescrOggettoSige(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().
			 * getOggettoSige(), mCodOggettoSige));
			 */
			// Decodifica Esito TENORE SIGE
			if (mCodEsitoSige != null && mCodEsitoSige.trim().length() > 0)
				setDescrEsitoSige(DecodificheUtils
						.getDescbyCode(DecodificheManager.getInstance().getEsitoTenoreSige(), mCodEsitoSige));

			// Decodifica Esito TENORE_SENTENZA_REATO
			if (mCodEsitoTenSenRea != null && mCodEsitoTenSenRea.trim().length() > 0)
				setDescrEsitoTenSenRea(DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getEsitoTenoreSige(), mCodEsitoTenSenRea));

		} catch (Exception e) {
			throw new F3BException(F3BException.EX_OPERATION_FAILED, "Errore nella trascodifica codice ( "
					+ getClass().getName() + ".decodifica()) -> " + e.getMessage());
		}
		return this;
	}

}