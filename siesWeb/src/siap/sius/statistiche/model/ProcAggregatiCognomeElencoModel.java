package siap.sius.statistiche.model;

import java.util.Date;

import f3b.model.GenericModel;

public class ProcAggregatiCognomeElencoModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -4894539179649426165L;
	private String mIniziale1Lettera = null;
	private String mIniziale2Lettere = null;
	private String mIniziale3Lettere = null;
	private String mCognome = null;
	private String mNome = null;
	private Date mDataNascita = null;
	private String mLuogoNascita = null;
	private String mChiaveAnno = null;
	private String mChiaveProgressivo = null;
	private String mPosizioneGiuridica = null;
	private String mContenuto = null;
	private String mStatoProcedimento = null;

	public String getIniziale1Lettera() {
		return mIniziale1Lettera;
	}

	public void setIniziale1Lettera(String aIniziale1Lettera) {
		this.mIniziale1Lettera = aIniziale1Lettera;
	}

	public String getIniziale2Lettere() {
		return mIniziale2Lettere;
	}

	public void setIniziale2Lettere(String aIniziale2Lettere) {
		this.mIniziale2Lettere = aIniziale2Lettere;
	}

	public String getIniziale3Lettere() {
		return mIniziale3Lettere;
	}

	public void setIniziale3Lettere(String aIniziale3Lettere) {
		this.mIniziale3Lettere = aIniziale3Lettere;
	}

	public String getCognome() {
		return mCognome;
	}

	public void setCognome(String aCognome) {
		this.mCognome = aCognome;
	}

	public String getNome() {
		return mNome;
	}

	public void setNome(String aNome) {
		this.mNome = aNome;
	}

	public Date getDataNascita() {
		return mDataNascita;
	}

	public void setDataNascita(Date aDataNascita) {
		this.mDataNascita = aDataNascita;
	}

	public String getLuogoNascita() {
		return mLuogoNascita;
	}

	public void setLuogoNascita(String aLuogoNascita) {
		this.mLuogoNascita = aLuogoNascita;
	}

	public String getChiaveAnno() {
		return mChiaveAnno;
	}

	public void setChiaveAnno(String aChiaveAnno) {
		this.mChiaveAnno = aChiaveAnno;
	}

	public String getChiaveProgressivo() {
		return mChiaveProgressivo;
	}

	public void setChiaveProgressivo(String aChiaveProgressivo) {
		this.mChiaveProgressivo = aChiaveProgressivo;
	}

	public String getPosizioneGiuridica() {
		return mPosizioneGiuridica;
	}

	public void setPosizioneGiuridica(String aPosizioneGiuridica) {
		this.mPosizioneGiuridica = aPosizioneGiuridica;
	}

	public String getContenuto() {
		return mContenuto;
	}

	public void setContenuto(String aContenuto) {
		this.mContenuto = aContenuto;
	}

	public String getStatoProcedimento() {
		return mStatoProcedimento;
	}

	public void setStatoProcedimento(String aStatoProcedimento) {
		this.mStatoProcedimento = aStatoProcedimento;
	}
}
