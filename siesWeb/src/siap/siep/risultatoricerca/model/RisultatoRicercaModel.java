package siap.siep.risultatoricerca.model;

/**
* <p>Title: RisultatoRicercaModel</p>
* <p>Description: Classe Model che rappresenta il RisultatoRicerca</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class RisultatoRicercaModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = 2036030393598911878L;
	private BigDecimal mIdRicerca;
	private String mCodUfficio;
	private String mDescrUfficio;
	private BigDecimal mIdFascicoloSiep;
	private BigDecimal mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mCognome;
	private String mNome;
	private String mLuogoNascita;
	private Date mDataNascita;
	private Date mDataReato;
	private Date mDataFinePena;
	private BigDecimal mNumAnniPenaRes;
	private BigDecimal mNumMesiPenaRes;
	private BigDecimal mNumGiorniPenaRes;
	private String mCodPosizioneGiuridica;
	private String mDescrPosizioneGiuridica;
	private String mCodUtente;
	private String mNazionalita;

	// COSTRUTTORE DI DEFAULT
	public RisultatoRicercaModel() {
		this.mIdRicerca = null;
		this.mCodUfficio = "";
		this.mDescrUfficio = "";
		this.mIdFascicoloSiep = null;
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mCognome = "";
		this.mNome = "";
		this.mLuogoNascita = "";
		this.mDataNascita = null;
		this.mDataReato = null;
		this.mDataFinePena = null;
		this.mNumAnniPenaRes = null;
		this.mNumMesiPenaRes = null;
		this.mNumGiorniPenaRes = null;
		this.mCodPosizioneGiuridica = "";
		this.mDescrPosizioneGiuridica = "";
		this.mCodUtente = "";
		this.mNazionalita = "";

	}

	// COSTRUTTORE DI COPIA
	public RisultatoRicercaModel(RisultatoRicercaModel aModel) {
		this.mIdRicerca = aModel.mIdRicerca;
		this.mCodUfficio = aModel.mCodUfficio;
		this.mDescrUfficio = aModel.mDescrUfficio;
		this.mIdFascicoloSiep = aModel.mIdFascicoloSiep;
		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mLuogoNascita = aModel.mLuogoNascita;
		this.mDataNascita = aModel.mDataNascita;
		this.mDataReato = aModel.mDataReato;
		this.mDataFinePena = aModel.mDataFinePena;
		this.mNumAnniPenaRes = aModel.mNumAnniPenaRes;
		this.mNumMesiPenaRes = aModel.mNumMesiPenaRes;
		this.mNumGiorniPenaRes = aModel.mNumGiorniPenaRes;
		this.mCodPosizioneGiuridica = aModel.mCodPosizioneGiuridica;
		this.mDescrPosizioneGiuridica = aModel.mDescrPosizioneGiuridica;
		this.mCodUtente = aModel.mCodUtente;
		this.mNazionalita = aModel.mNazionalita;
	}

	// COSTRUTTORE MODEL
	public RisultatoRicercaModel(BigDecimal aIdRicerca, String aCodUfficio, String aDescrUfficio,
			BigDecimal aIdFascicoloSiep, BigDecimal aChiaveAnno, BigDecimal aChiaveProgr, String aCognome,
			String aNome, String aLuogoNascita, Date aDataNascita, Date aDataReato, Date aDataFinePena,
			BigDecimal aNumAnniPenaRes, BigDecimal aNumMesiPenaRes, BigDecimal aNumGiorniPenaRes,
			String aCodPosizioneGiuridica, String aDescrPosizioneGiuridica, String aCodUtente,
			String aNazionalita) {
		this.mIdRicerca = aIdRicerca;
		this.mCodUfficio = aCodUfficio;
		this.mDescrUfficio = aDescrUfficio;
		this.mIdFascicoloSiep = aIdFascicoloSiep;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mLuogoNascita = aLuogoNascita;
		this.mDataNascita = aDataNascita;
		this.mDataReato = aDataReato;
		this.mDataFinePena = aDataFinePena;
		this.mNumAnniPenaRes = aNumAnniPenaRes;
		this.mNumMesiPenaRes = aNumMesiPenaRes;
		this.mNumGiorniPenaRes = aNumGiorniPenaRes;
		this.mCodPosizioneGiuridica = aCodPosizioneGiuridica;
		this.mDescrPosizioneGiuridica = aDescrPosizioneGiuridica;
		this.mCodUtente = aCodUtente;
		this.mNazionalita = aNazionalita;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdRicerca() {
		return mIdRicerca;
	}

	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getDescrUfficio() {
		return mDescrUfficio;
	}

	public BigDecimal getIdFascicoloSiep() {
		return mIdFascicoloSiep;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getLuogoNascita() {
		return mLuogoNascita;
	}

	public Date getDataNascita() {
		return mDataNascita;
	}

	public Date getDataReato() {
		return mDataReato;
	}

	public Date getDataFinePena() {
		return mDataFinePena;
	}

	public BigDecimal getNumAnniPenaRes() {
		return mNumAnniPenaRes;
	}

	public BigDecimal getNumMesiPenaRes() {
		return mNumMesiPenaRes;
	}

	public BigDecimal getNumGiorniPenaRes() {
		return mNumGiorniPenaRes;
	}

	public String getCodPosizioneGiuridica() {
		return mCodPosizioneGiuridica;
	}

	public String getDescrPosizioneGiuridica() {
		return mDescrPosizioneGiuridica;
	}

	public String getCodUtente() {
		return mCodUtente;
	}

	public String getNazionalita() {
		return mNazionalita;
	}

	//
	// METODI SET()
	//

	public void setIdRicerca(BigDecimal aValore) {
		mIdRicerca = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setDescrUfficio(String aValore) {
		mDescrUfficio = aValore;
	}

	public void setIdFascicoloSiep(BigDecimal aValore) {
		mIdFascicoloSiep = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setLuogoNascita(String aValore) {
		mLuogoNascita = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public void setDataReato(Date aValore) {
		mDataReato = aValore;
	}

	public void setDataFinePena(Date aValore) {
		mDataFinePena = aValore;
	}

	public void setNumAnniPenaRes(BigDecimal aValore) {
		mNumAnniPenaRes = aValore;
	}

	public void setNumMesiPenaRes(BigDecimal aValore) {
		mNumMesiPenaRes = aValore;
	}

	public void setNumGiorniPenaRes(BigDecimal aValore) {
		mNumGiorniPenaRes = aValore;
	}

	public void setCodPosizioneGiuridica(String aValore) {
		mCodPosizioneGiuridica = aValore;
	}

	public void setDescrPosizioneGiuridica(String aValore) {
		mDescrPosizioneGiuridica = aValore;
	}

	public void setCodUtente(String aValore) {
		mCodUtente = aValore;
	}

	public void setNazionalita(String aValore) {
		mNazionalita = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdRicerca + " - " + mCodUfficio + " - " + mDescrUfficio + " - " + mIdFascicoloSiep
				+ " - " + mChiaveAnno + " - " + mChiaveProgr + " - " + mCognome + " - " + mNome + " - "
				+ mLuogoNascita + " - " + mDataNascita + " - " + mDataReato + " - " + mDataFinePena + " - "
				+ mNumAnniPenaRes + " - " + mNumMesiPenaRes + " - " + mNumGiorniPenaRes + " - "
				+ mCodPosizioneGiuridica + " - " + mDescrPosizioneGiuridica + " - " + mCodUtente + " - "
				+ mNazionalita;

		return lStr;
	}
}
