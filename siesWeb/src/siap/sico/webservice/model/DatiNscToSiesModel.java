package siap.sico.webservice.model;

import java.util.Vector;

import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import f3b.model.GenericModel;

@SuppressWarnings("rawtypes")
public class DatiNscToSiesModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7272952016276356668L;

	private SentenzaModel mSentenzaModel;
	private SoggettoModel mSoggettoModel;
	private FascicoloSiepModel mFascicoloSiepModel;
	private Vector mReatoModel;
	private Vector mPenaComplessiva;
	private Vector mCircostanze;
	private Vector mMisuraSicurezza;
	private Vector mPeneAccessorie;
	private Vector mSanzioneSostitutiva;
	private Vector mBeneficio;
	private Vector mRevoca;
	private Vector mContinuazioni;
	private StatoProcedimentoModel mStatoProcedimentoModel;
	private SoggettoCertificatoModel mSoggettoCertificatoModel;

	// Costruttore
	public DatiNscToSiesModel() {
		this.mSentenzaModel = null;
		this.mSoggettoModel = null;
		this.mFascicoloSiepModel = null;
		this.mReatoModel = null;
		this.mPenaComplessiva = null;
		this.mCircostanze = null;
		this.mMisuraSicurezza = null;
		this.mPeneAccessorie = null;
		this.mSanzioneSostitutiva = null;
		this.mBeneficio = null;
		this.mRevoca = null;
		this.mContinuazioni = null;
		this.mStatoProcedimentoModel = null;
		this.mSoggettoCertificatoModel = null;
	}

	//
	// METODI GET()
	//
	public SentenzaModel getSentenzaModel() {
		return mSentenzaModel;
	}

	public SoggettoModel getSoggettoModel() {
		return mSoggettoModel;
	}

	public FascicoloSiepModel getFascicoloSiepModel() {
		return mFascicoloSiepModel;
	}

	public Vector getReatoModel() {
		return mReatoModel;
	}

	public Vector getPenaComplessivaModel() {
		return mPenaComplessiva;
	}

	public Vector getCircostanzeModel() {
		return mCircostanze;
	}

	public Vector getMisuraSicurezzaModel() {
		return mMisuraSicurezza;
	}

	public Vector getPeneAccessorieModel() {
		return mPeneAccessorie;
	}

	public Vector getSanzioneSostitutivaModel() {
		return mSanzioneSostitutiva;
	}

	public Vector getBeneficioModel() {
		return mBeneficio;
	}

	public Vector getRevocaModel() {
		return mRevoca;
	}

	public Vector getContinuazioniModel() {
		return mContinuazioni;
	}

	public StatoProcedimentoModel getStatoProcedimentoModel() {
		return mStatoProcedimentoModel;
	}

	public SoggettoCertificatoModel getSoggettoCertificatoModel() {
		return mSoggettoCertificatoModel;
	}

	//
	// METODI SET()
	//
	public void setSentenzaModel(SentenzaModel aValore) {
		mSentenzaModel = aValore;
	}

	public void setSoggettoModel(SoggettoModel aValore) {
		mSoggettoModel = aValore;
	}

	public void setFascicoloSiepModel(FascicoloSiepModel aValore) {
		mFascicoloSiepModel = aValore;
	}

	public void setReatoModel(Vector aValore) {
		mReatoModel = aValore;
	}

	public void setPenaComplessivaModel(Vector aValore) {
		mPenaComplessiva = aValore;
	}

	public void setCircostanzeModel(Vector aValore) {
		mCircostanze = aValore;
	}

	public void setMisuraSicurezzaModel(Vector aValore) {
		mMisuraSicurezza = aValore;
	}

	public void setPeneAccessorieModel(Vector aValore) {
		mPeneAccessorie = aValore;
	}

	public void setSanzioneSostitutivaModel(Vector aValore) {
		mSanzioneSostitutiva = aValore;
	}

	public void setBeneficioModel(Vector aValore) {
		mBeneficio = aValore;
	}

	public void setRevocaModel(Vector aValore) {
		mRevoca = aValore;
	}

	public void setContinuazioniModel(Vector aValore) {
		mContinuazioni = aValore;
	}

	public void setStatoProcedimentoModel(StatoProcedimentoModel aValore) {
		mStatoProcedimentoModel = aValore;
	}

	public void setSoggettoCertificatoModel(SoggettoCertificatoModel aValore) {
		mSoggettoCertificatoModel = aValore;
	}

}