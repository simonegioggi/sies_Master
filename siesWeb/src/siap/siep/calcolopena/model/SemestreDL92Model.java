package siap.siep.calcolopena.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * Model che veicola i risultati dell'elaborazione del conteggio del calcolo delle LA da concedere per
 * semestri secondo quanto previsto dal DL 92/2024 Pena Virtuale
 *
 * @since MEV_2024-092
 *
 * MEV-2026_1 - Si modifica il Model per la persistenza su DB aggiungenedo ID FK e dati INS e AGG
 */
public class SemestreDL92Model extends GenericModel {

	private static final long serialVersionUID = -3682000784946215279L;

	// MEV-2026_1 - Si aggiungo in campi per gestire il slvataggio del bean su DB
	private BigDecimal mIdSemestriLaDl92;
	private BigDecimal mCalcIdCalcoloPenaDl92;
	// MEV-2026_1 - FINE

	// SemestriDL92Model: {progressivo, ResiduoAA, ResiduoMM, ResiduoGG, LAApplicata, DataMaturazioneLA,
	// DataScadenzaPena}
	private BigDecimal mProgressivo; // Progressivo semestre
	private BigDecimal mNumSemestriMaturati; // default 1 tranne per il record del presofferto
	private BigDecimal mResiduoNumAnni;
	private BigDecimal mResiduoNumMesi;
	private BigDecimal mResiduoNumGiorni;
	private BigDecimal mLAApplicate;
	private String mIsPresofferto; // indica se il record si riferisce al presofferto

	private Date mDataMaturazioneLA;
	private Date mNuovaDataScadenzaPena; // Fine pena rideterminato avendo applicato i gg di LA

	// dato calcolato sono i gg del presofferto che non maturano LA
	private BigDecimal mGiorniResiduiPresofferto;

	private String mIsCompreso = "S"; // S/N indica se il semestre va consideratoai fini delle LA

	// MEV-2026_1 - Si aggiungo in campi per gestire il slvataggio del bean su DB
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	// MEV-2026_1 - FINE

	/**
	 * Costruttore di default
	 */
	public SemestreDL92Model() {
		this.mIdSemestriLaDl92 = null;
		this.mCalcIdCalcoloPenaDl92 = null;
		this.mProgressivo = null;
		this.mNumSemestriMaturati = null;
		this.mResiduoNumAnni = null;
		this.mResiduoNumMesi = null;
		this.mResiduoNumGiorni = null;
		this.mLAApplicate = null;
		this.mIsPresofferto = null;
		this.mDataMaturazioneLA = null;
		this.mNuovaDataScadenzaPena = null;
		this.mGiorniResiduiPresofferto = null;
		this.mIsCompreso = "S";

		this.mCodOperatoreInserimento = null;
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = null;
		this.mCodOperatoreAggiornamento = null;
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = null;
	}

	/**
	 * Costruttore di copia
	 */
	public SemestreDL92Model(SemestreDL92Model aModel) {
		this.mIdSemestriLaDl92 = aModel.getIdSemestriLaDl92();
		this.mCalcIdCalcoloPenaDl92 = aModel.getCalcIdCalcoloPenaDl92();
		this.mProgressivo = aModel.getProgressivo();
		this.mNumSemestriMaturati = aModel.getNumSemestriMaturati();
		this.mResiduoNumAnni = aModel.getResiduoNumAnni();
		this.mResiduoNumMesi = aModel.getResiduoNumMesi();
		this.mResiduoNumGiorni = aModel.getResiduoNumGiorni();
		this.mLAApplicate = aModel.getLAApplicate();
		this.mIsPresofferto = aModel.getIsPresofferto();
		this.mDataMaturazioneLA = aModel.getDataMaturazioneLA();
		this.mNuovaDataScadenzaPena = aModel.getNuovaDataScadenzaPena();
		this.mGiorniResiduiPresofferto = aModel.getGiorniResiduiPresofferto();
		this.mIsCompreso = aModel.getIsCompreso();

		this.mCodOperatoreInserimento = aModel.getCodOperatoreInserimento();
		this.mDataInserimento = aModel.getDataInserimento();
		this.mCodUfficioInserimento = aModel.getCodUfficioInserimento();
		this.mCodOperatoreAggiornamento = aModel.getCodOperatoreAggiornamento();
		this.mDataAggiornamento = aModel.getDataAggiornamento();
		this.mCodUfficioAggiornamento = aModel.getCodUfficioAggiornamento();
	}

	// ================
	// Metodi Getter
	// ================
	// MEV-2026_1 - Si aggiungono in campi per gestire il salvataggio del bean su DB
	public BigDecimal getIdSemestriLaDl92() {
		return mIdSemestriLaDl92;
	}

	public BigDecimal getCalcIdCalcoloPenaDl92() {
		return mCalcIdCalcoloPenaDl92;
	}
	// MEV-2026_1 - FINE

	public BigDecimal getProgressivo() {
		return mProgressivo;
	}

	public BigDecimal getResiduoNumAnni() {
		return mResiduoNumAnni;
	}

	public BigDecimal getResiduoNumMesi() {
		return mResiduoNumMesi;
	}

	public BigDecimal getResiduoNumGiorni() {
		return mResiduoNumGiorni;
	}

	public BigDecimal getLAApplicate() {
		return mLAApplicate;
	}

	public Date getDataMaturazioneLA() {
		return mDataMaturazioneLA;
	}

	public Date getNuovaDataScadenzaPena() {
		return mNuovaDataScadenzaPena;
	}

	public BigDecimal getGiorniResiduiPresofferto() {
		return mGiorniResiduiPresofferto;
	}

	public BigDecimal getNumSemestriMaturati() {
		return mNumSemestriMaturati;
	}

	public String getIsPresofferto() {
		return mIsPresofferto;
	}

	public String getIsCompreso() {
		return mIsCompreso;
	}

	// MEV-2026_1 - Si aggiungono in campi per gestire il salvataggio del bean su DB
	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
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
	// MEV-2026_1 - FINE

	// ================
	// Metodi Setter
	// ================
	// MEV-2026_1 - Si aggiungono in campi per gestire il salvataggio del bean su DB
	public void setIdSemestriLaDl92(BigDecimal mIdSemestriLaDl92) {
		this.mIdSemestriLaDl92 = mIdSemestriLaDl92;
	}

	public void setCalcIdCalcoloPenaDl92(BigDecimal mCalcIdCalcoloPenaDl92) {
		this.mCalcIdCalcoloPenaDl92 = mCalcIdCalcoloPenaDl92;
	}
	// MEV-2026_1 - FINE

	public void setProgressivo(BigDecimal mProgressivo) {
		this.mProgressivo = mProgressivo;
	}

	public void setResiduoNumAnni(BigDecimal mResiduoNumAnni) {
		this.mResiduoNumAnni = mResiduoNumAnni;
	}

	public void setResiduoNumMesi(BigDecimal mResiduoNumMesi) {
		this.mResiduoNumMesi = mResiduoNumMesi;
	}

	public void setResiduoNumGiorni(BigDecimal mResiduoNumGiorni) {
		this.mResiduoNumGiorni = mResiduoNumGiorni;
	}

	public void setLAApplicate(BigDecimal mLAApplicate) {
		this.mLAApplicate = mLAApplicate;
	}

	public void setDataMaturazioneLA(Date mDataMaturazioneLA) {
		this.mDataMaturazioneLA = mDataMaturazioneLA;
	}

	public void setNuovaDataScadenzaPena(Date mNuovaDataScadenzaPena) {
		this.mNuovaDataScadenzaPena = mNuovaDataScadenzaPena;
	}

	public void setGiorniResiduiPresofferto(BigDecimal mGiorniResiduiPresofferto) {
		this.mGiorniResiduiPresofferto = mGiorniResiduiPresofferto;
	}

	public void setNumSemestriMaturati(BigDecimal mNumSemestriMaturati) {
		this.mNumSemestriMaturati = mNumSemestriMaturati;
	}

	public void setIsPresofferto(String mIsPresofferto) {
		this.mIsPresofferto = mIsPresofferto;
	}

	public void setIsCompreso(String mIsCompreso) {
		this.mIsCompreso = mIsCompreso;
	}

	// MEV-2026_1 - Si aggiungono in campi per gestire il salvataggio del bean su DB
	public void setCodOperatoreInserimento(String mCodOperatoreInserimento) {
		this.mCodOperatoreInserimento = mCodOperatoreInserimento;
	}

	public void setDataInserimento(Date mDataInserimento) {
		this.mDataInserimento = mDataInserimento;
	}

	public void setCodUfficioInserimento(String mCodUfficioInserimento) {
		this.mCodUfficioInserimento = mCodUfficioInserimento;
	}

	public void setCodOperatoreAggiornamento(String mCodOperatoreAggiornamento) {
		this.mCodOperatoreAggiornamento = mCodOperatoreAggiornamento;
	}

	public void setDataAggiornamento(Date mDataAggiornamento) {
		this.mDataAggiornamento = mDataAggiornamento;
	}

	public void setCodUfficioAggiornamento(String mCodUfficioAggiornamento) {
		this.mCodUfficioAggiornamento = mCodUfficioAggiornamento;
	}
	// MEV-2026_1 - FINE

}