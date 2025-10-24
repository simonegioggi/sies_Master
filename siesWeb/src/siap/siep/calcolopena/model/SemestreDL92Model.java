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
 */
public class SemestreDL92Model extends GenericModel {

	private static final long serialVersionUID = -3682000784946215279L;

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

	// datao calcolato sono i gg del presofferto che non maturano LA
	private BigDecimal mGiorniResiduiPresofferto;

	private String mIsCompreso = "S"; // S/N indica se il semestre va consideratoai fini delle LA

	// Metodi Getter
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

	// Metodi Setter
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
}
