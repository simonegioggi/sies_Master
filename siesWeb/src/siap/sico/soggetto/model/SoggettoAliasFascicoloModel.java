package siap.sico.soggetto.model;

import java.math.BigDecimal;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: SoggettoAliasFascicoloModel
 * </p>
 * <p>
 * Description: Model utilizzato per la ricerca dei soggetti/alias con fascicoli
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class SoggettoAliasFascicoloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4905344706118860904L;

	// Carica il model del soggetto
	private SoggettoModel mSoggetto;
	private String mNumeroFascicoliUfficioSedeCompetente;
	private BigDecimal mSogIdSoggettoAlias;
	private String mCognomeNomeLegatoAlias;
	private String mCognomeNomeSoggettoAlias;
	private String mLuogoProvinciaNascita;

	// COSTRUTTORE DI DEFAULT
	public SoggettoAliasFascicoloModel() {
		mSoggetto = null;
		mNumeroFascicoliUfficioSedeCompetente = "";
		mSogIdSoggettoAlias = null;
		mCognomeNomeLegatoAlias = "";
		mCognomeNomeSoggettoAlias = "";
		mLuogoProvinciaNascita = "";
	}

	// COSTRUTTORE DI COPIA
	public SoggettoAliasFascicoloModel(SoggettoAliasFascicoloModel aModel) {
		mSoggetto = new SoggettoModel(aModel.getSoggetto());
		mNumeroFascicoliUfficioSedeCompetente = aModel.mNumeroFascicoliUfficioSedeCompetente;
		mSogIdSoggettoAlias = aModel.mSogIdSoggettoAlias;
		mCognomeNomeLegatoAlias = aModel.mCognomeNomeLegatoAlias;
		mCognomeNomeSoggettoAlias = aModel.mCognomeNomeSoggettoAlias;
		mLuogoProvinciaNascita = aModel.mLuogoProvinciaNascita;
	}

	// COSTRUTTORE MODEL
	public SoggettoAliasFascicoloModel(SoggettoModel aSoggetto, String aNumeroFascicoliUfficioSedeCompetente,
			BigDecimal aSogIdSoggettoAlias, String aCognomeNomeLegatoAlias, String aCognomeNomeSoggettoAlias,
			String aLuogoProvinciaNascita) {
		mSoggetto = aSoggetto;
		mNumeroFascicoliUfficioSedeCompetente = aNumeroFascicoliUfficioSedeCompetente;
		mSogIdSoggettoAlias = aSogIdSoggettoAlias;
		mCognomeNomeLegatoAlias = aCognomeNomeLegatoAlias;
		mCognomeNomeSoggettoAlias = aCognomeNomeSoggettoAlias;
		mLuogoProvinciaNascita = aLuogoProvinciaNascita;
	}

	//
	// METODI GET()
	//
	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public String getNumeroFascicoliUfficioSedeCompetente() {
		return mNumeroFascicoliUfficioSedeCompetente;
	}

	public BigDecimal getSogIdSoggettoAlias() {
		return mSogIdSoggettoAlias;
	}

	public String getCognomeNomeLegatoAlias() {
		return mCognomeNomeLegatoAlias;
	}

	public String getCognomeNomeSoggettoAlias() {
		return mCognomeNomeSoggettoAlias;
	}

	public String getLuogoProvinciaNascita() {
		return mLuogoProvinciaNascita;
	}

	//
	// METODI SET()
	//
	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setNumeroFascicoliUfficioSedeCompetente(String aValore) {
		mNumeroFascicoliUfficioSedeCompetente = aValore;
	}

	public void setSogIdSoggettoAlias(BigDecimal aValore) {
		mSogIdSoggettoAlias = aValore;
	}

	public void setCognomeNomeLegatoAlias(String aValore) {
		mCognomeNomeLegatoAlias = aValore;
	}

	public void setCognomeNomeSoggettoAlias(String aValore) {
		mCognomeNomeSoggettoAlias = aValore;
	}

	public void setLuogoProvinciaNascita(String aValore) {
		mLuogoProvinciaNascita = aValore;
	}

}