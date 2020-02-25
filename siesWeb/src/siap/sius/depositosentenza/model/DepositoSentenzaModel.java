package siap.sius.depositosentenza.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: DepositoSentenzaModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il DepositoSentenza
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class DepositoSentenzaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 77210402894451256L;

	private BigDecimal mIdDepositoSentenza;
	private BigDecimal mAnnoSentenza;
	private BigDecimal mNumSentenza;
	private String mCodTipoSentenza;
	private Date mDataEmissione;
	private Date mDataDeposito;
	private String mCodMagistrato;
	private String mAltriDestinatari;
	private Date mDataParerePg;
	private String mCodTipoParerePg;
	private Date mDataRicorsoImpugnazione;
	private Date mDataInvioAttiImpugnazione;
	private Date mDataSentenzaImpugnazione;
	private String mTenoreSentenzaImpugnazione;
	private String mNote;
	private String mSentenzeRiferimento;
	private String mCodProcuraEsecuzione;
	private String mCodUfficioComp;
	private BigDecimal mIdEventoGenerato;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private Date mDataAggiornamento;
	private BigDecimal mGenPridGeneraleProcedimento;
	private String mUlterioreDescrizione;
	private String mCodNaturaProvvedimento;
	private String mOggettoProcedimento;
	private Date mDataUdienza;

	private String mAnnoDataEmissione;
	private String mMeseDataEmissione;
	private String mGiornoDataEmissione;

	// COSTRUTTORE DI DEFAULT
	public DepositoSentenzaModel() {
		this.mIdDepositoSentenza = null;
		this.mAnnoSentenza = null;
		this.mNumSentenza = null;
		this.mCodTipoSentenza = "";
		this.mDataEmissione = null;
		this.mDataDeposito = null;
		this.mCodMagistrato = "";
		this.mAltriDestinatari = "";
		this.mDataParerePg = null;
		this.mCodTipoParerePg = "";
		this.mDataRicorsoImpugnazione = null;
		this.mDataInvioAttiImpugnazione = null;
		this.mDataSentenzaImpugnazione = null;
		this.mTenoreSentenzaImpugnazione = "";
		this.mNote = "";
		this.mSentenzeRiferimento = "";
		this.mCodProcuraEsecuzione = "";
		this.mCodUfficioComp = "";
		this.mIdEventoGenerato = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mGenPridGeneraleProcedimento = null;
		this.mUlterioreDescrizione = "";
		this.mCodNaturaProvvedimento = "";
		this.mOggettoProcedimento = "";
		this.mDataUdienza = null;
	}

	// COSTRUTTORE DI COPIA
	public DepositoSentenzaModel(DepositoSentenzaModel aModel) {
		this.mIdDepositoSentenza = aModel.mIdDepositoSentenza;
		this.mAnnoSentenza = aModel.mAnnoSentenza;
		this.mNumSentenza = aModel.mNumSentenza;
		this.mCodTipoSentenza = aModel.mCodTipoSentenza;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mDataDeposito = aModel.mDataDeposito;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mAltriDestinatari = aModel.mAltriDestinatari;
		this.mDataParerePg = aModel.mDataParerePg;
		this.mCodTipoParerePg = aModel.mCodTipoParerePg;
		this.mDataRicorsoImpugnazione = aModel.mDataRicorsoImpugnazione;
		this.mDataInvioAttiImpugnazione = aModel.mDataInvioAttiImpugnazione;
		this.mDataSentenzaImpugnazione = aModel.mDataInvioAttiImpugnazione;
		this.mTenoreSentenzaImpugnazione = aModel.mTenoreSentenzaImpugnazione;
		this.mNote = aModel.mNote;
		this.mSentenzeRiferimento = aModel.mSentenzeRiferimento;
		this.mCodProcuraEsecuzione = aModel.mCodProcuraEsecuzione;
		this.mCodUfficioComp = aModel.mCodUfficioComp;
		this.mIdEventoGenerato = aModel.mIdEventoGenerato;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mGenPridGeneraleProcedimento = aModel.mGenPridGeneraleProcedimento;
		this.mUlterioreDescrizione = aModel.mUlterioreDescrizione;
		this.mCodNaturaProvvedimento = aModel.mCodNaturaProvvedimento;
		this.mOggettoProcedimento = aModel.mOggettoProcedimento;
		this.mDataUdienza = aModel.mDataUdienza;
	}

	// COSTRUTTORE MODEL
	public DepositoSentenzaModel(BigDecimal aIdDepositoSentenza, BigDecimal aAnnoSentenza,
			BigDecimal aNumSentenza, String aCodTipoSentenza, Date aDataEmissione, Date aDataDeposito,
			String aCodMagistrato, String aAltriDestinatari, Date aDataParerePg, String aCodTipoParerePg,
			Date aDataRicorsoImpugnazione, Date aDataInvioAttiImpugnazione, Date aDataSentenzaImpugnazione,
			String aTenoreSentenzaImpugnazione, String aNote, String aSentenzeRiferimento,
			String aCodProcuraEsecuzione, String aCodUfficioComp, BigDecimal aIdEventoGenerato,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, Date aDataAggiornamento,
			BigDecimal aGenPridGeneraleProcedimento, String aUlterioreDescrizione,
			String aCodNaturaProvvedimento, String aOggettoProcedimento, Date aDataUdienza) {
		this.mIdDepositoSentenza = aIdDepositoSentenza;
		this.mAnnoSentenza = aAnnoSentenza;
		this.mNumSentenza = aNumSentenza;
		this.mCodTipoSentenza = aCodTipoSentenza;
		this.mDataEmissione = aDataEmissione;
		this.mDataDeposito = aDataDeposito;
		this.mCodMagistrato = aCodMagistrato;
		this.mAltriDestinatari = aAltriDestinatari;
		this.mDataParerePg = aDataParerePg;
		this.mCodTipoParerePg = aCodTipoParerePg;
		this.mDataRicorsoImpugnazione = aDataRicorsoImpugnazione;
		this.mDataInvioAttiImpugnazione = aDataInvioAttiImpugnazione;
		this.mDataSentenzaImpugnazione = aDataInvioAttiImpugnazione;
		this.mTenoreSentenzaImpugnazione = aTenoreSentenzaImpugnazione;
		this.mNote = aNote;
		this.mSentenzeRiferimento = aSentenzeRiferimento;
		this.mCodProcuraEsecuzione = aCodProcuraEsecuzione;
		this.mCodUfficioComp = aCodUfficioComp;
		this.mIdEventoGenerato = aIdEventoGenerato;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mGenPridGeneraleProcedimento = aGenPridGeneraleProcedimento;
		this.mUlterioreDescrizione = aUlterioreDescrizione;
		this.mCodNaturaProvvedimento = aCodNaturaProvvedimento;
		this.mOggettoProcedimento = aOggettoProcedimento;
		this.mDataUdienza = aDataUdienza;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdDepositoSentenza() {
		return mIdDepositoSentenza;
	}

	public BigDecimal getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public BigDecimal getNumSentenza() {
		return mNumSentenza;
	}

	public String getCodTipoSentenza() {
		return mCodTipoSentenza;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public Date getDataDeposito() {
		return mDataDeposito;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getAltriDestinatari() {
		return mAltriDestinatari;
	}

	public Date getDataParerePg() {
		return mDataParerePg;
	}

	public String getCodTipoParerePg() {
		return mCodTipoParerePg;
	}

	public Date getDataRicorsoImpugnazione() {
		return mDataRicorsoImpugnazione;
	}

	public Date getDataInvioAttiImpugnazione() {
		return mDataInvioAttiImpugnazione;
	}

	public Date getDataSentenzaImpugnazione() {
		return mDataSentenzaImpugnazione;
	}

	public String getTenoreSentenzaImpugnazione() {
		return mTenoreSentenzaImpugnazione;
	}

	public String getNote() {
		return mNote;
	}

	public String getSentenzeRiferimento() {
		return mSentenzeRiferimento;
	}

	public String getCodProcuraEsecuzione() {
		return mCodProcuraEsecuzione;
	}

	public String getCodUfficioComp() {
		return mCodUfficioComp;
	}

	public BigDecimal getIdEventoGenerato() {
		return mIdEventoGenerato;
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

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public BigDecimal getGenPridGeneraleProcedimento() {
		return mGenPridGeneraleProcedimento;
	}

	public String getUlterioreDescrizione() {
		return mUlterioreDescrizione;
	}

	public String getCodNaturaProvvedimento() {
		return mCodNaturaProvvedimento;
	}

	public String getOggettoProcedimento() {
		return mOggettoProcedimento;
	}

	public Date getDataUdienza() {
		return mDataUdienza;
	}

	public String getAnnoDataEmissione() {
		return mAnnoDataEmissione;
	}

	public String getMeseDataEmissione() {
		return mMeseDataEmissione;
	}

	public String getGiornoDataEmissione() {
		return mGiornoDataEmissione;
	}

	//
	// METODI SET()
	//
	public void setIdDepositoSentenza(BigDecimal aValore) {
		mIdDepositoSentenza = aValore;
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumSentenza(BigDecimal aValore) {
		mNumSentenza = aValore;
	}

	public void setCodTipoSentenza(String aValore) {
		mCodTipoSentenza = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setDataDeposito(Date aValore) {
		mDataDeposito = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setAltriDestinatari(String aValore) {
		mAltriDestinatari = aValore;
	}

	public void setDataParerePg(Date aValore) {
		mDataParerePg = aValore;
	}

	public void setCodTipoParerePg(String aValore) {
		mCodTipoParerePg = aValore;
	}

	public void setDataRicorsoImpugnazione(Date aValore) {
		mDataRicorsoImpugnazione = aValore;
	}

	public void setDataInvioAttiImpugnazione(Date aValore) {
		mDataInvioAttiImpugnazione = aValore;
	}

	public void setDataSentenzaImpugnazione(Date aValore) {
		mDataSentenzaImpugnazione = aValore;
	}

	public void setTenoreSentenzaImpugnazione(String aValore) {
		mTenoreSentenzaImpugnazione = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setSentenzeRiferimento(String aValore) {
		mSentenzeRiferimento = aValore;
	}

	public void setCodProcuraEsecuzione(String aValore) {
		mCodProcuraEsecuzione = aValore;
	}

	public void setCodUfficioComp(String aValore) {
		mCodUfficioComp = aValore;
	}

	public void setIdEventoGenerato(BigDecimal aValore) {
		mIdEventoGenerato = aValore;
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

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setGenPridGeneraleProcedimento(BigDecimal aValore) {
		mGenPridGeneraleProcedimento = aValore;
	}

	public void setUlterioreDescrizione(String aValore) {
		mUlterioreDescrizione = aValore;
	}

	public void setCodNaturaProvvedimento(String aValore) {
		mCodNaturaProvvedimento = aValore;
	}

	public void setOggettoProcedimento(String aValore) {
		mOggettoProcedimento = aValore;
	}

	public void setDataUdienza(Date aValore) {
		mDataUdienza = aValore;
	}

	public void setAnnoDataEmissione(String aValore) {
		mAnnoDataEmissione = aValore;
	}

	public void setMeseDataEmissione(String aValore) {
		mMeseDataEmissione = aValore;
	}

	public void setGiornoDataEmissione(String aValore) {
		mGiornoDataEmissione = aValore;
	}

}