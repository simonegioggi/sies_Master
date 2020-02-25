package siap.sige.fascicolo.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: RicercaFascicoloSigeModel
 * </p>
 * <p>
 * Description: Classe Model contenente i parametri di ricerca FascicoloSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class RicercaFascicoloSigeModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -636427892377846778L;

	private String mChiaveUfficio;
	private String mChiaveUfficioInserimento;
	private BigDecimal mChiaveAnnoIniziale;
	private BigDecimal mChiaveAnnoFinale;

	private BigDecimal mChiaveProgrIniziale;
	private BigDecimal mChiaveProgrFinale;

	private Date mDataIscrizioneIniziale;
	private Date mDataIscrizioneFinale;

	private String mCodTipoAtto;
	private String mCodOggettoSige;
	private String mCodMagistrato;
	private BigDecimal mIdSezione;
	private Date mDataFinePendenza;
	private Date mDataDefinizioneIniziale;
	private Date mDataDefinizioneFinale;

	private String mCodTipoRito; // Aggiunto il 26/11/2009

	// MEV_65: aggiunte variabili per gestire nuova funzionalita' --> Ricerca Soggetti Sige Per Posizione
	// Giuridica + Ricerca Procedimenti Sige Con Ricorso/Opposizione
	private String mCodPosizioneGiuridica;
	private String mCodNazione;
	private String mDescPosizioneGiuridica;
	private String mDescMagistrato;
	private String mDescSezione;
	private String mDescNazione;
	private String mTipoRicorso;
	private BigDecimal mChiaveAnnoRicorso;
	private BigDecimal mChiaveProgrRicorso;
	private String mStatoValidazione;
	private String mDescTipoRicorso;
	private Date mDataArrivoCancelleriaIniziale;
	private Date mDataArrivoCancelleriaFinale;

	// COSTRUTTORE DI DEFAULT
	public RicercaFascicoloSigeModel() {

		mChiaveUfficio = "";
		mChiaveUfficioInserimento = "";
		mChiaveAnnoIniziale = null;
		mChiaveAnnoFinale = null;
		mChiaveProgrIniziale = null;
		mChiaveProgrFinale = null;
		mDataIscrizioneIniziale = null;
		mDataIscrizioneFinale = null;
		mCodTipoAtto = "";
		mCodOggettoSige = "";
		mCodMagistrato = "";
		mIdSezione = null;
		mDataFinePendenza = null;
		mDataDefinizioneIniziale = null;
		mDataDefinizioneFinale = null;
		mCodTipoRito = "";
		// MEV_65
		mCodPosizioneGiuridica = "";
		mCodNazione = "";
		mDescPosizioneGiuridica = "";
		mDescMagistrato = "";
		mDescSezione = "";
		mDescNazione = "";
		mTipoRicorso = "";
		mChiaveAnnoRicorso = null;
		mChiaveProgrRicorso = null;
		mStatoValidazione = "";
		mDescTipoRicorso = "";
		mDataArrivoCancelleriaIniziale = null;
		mDataArrivoCancelleriaFinale = null;
	}

	// COSTRUTTORE DI COPIA
	public RicercaFascicoloSigeModel(RicercaFascicoloSigeModel aModel) {

		mChiaveUfficio = aModel.mChiaveUfficio;
		mChiaveUfficioInserimento = aModel.mChiaveUfficioInserimento;
		mChiaveAnnoIniziale = aModel.mChiaveAnnoIniziale;
		mChiaveAnnoFinale = aModel.mChiaveAnnoFinale;
		mChiaveProgrIniziale = aModel.mChiaveProgrIniziale;
		mChiaveProgrFinale = aModel.mChiaveProgrFinale;
		mDataIscrizioneIniziale = aModel.mDataIscrizioneIniziale;
		mDataIscrizioneFinale = aModel.mDataIscrizioneFinale;
		mCodTipoAtto = aModel.mCodTipoAtto;
		mCodOggettoSige = aModel.mCodOggettoSige;
		mCodMagistrato = aModel.mCodMagistrato;
		mIdSezione = aModel.mIdSezione;
		mDataFinePendenza = aModel.mDataFinePendenza;
		mDataDefinizioneIniziale = aModel.mDataDefinizioneIniziale;
		mDataDefinizioneFinale = aModel.mDataDefinizioneFinale;
		mCodTipoRito = aModel.mCodTipoRito;
		// MEV_65
		mCodPosizioneGiuridica = aModel.mCodPosizioneGiuridica;
		mCodNazione = aModel.mCodNazione;
		mDescPosizioneGiuridica = aModel.mDescPosizioneGiuridica;
		mDescMagistrato = aModel.mDescMagistrato;
		mDescSezione = aModel.mDescSezione;
		mDescNazione = aModel.mDescNazione;
		mTipoRicorso = aModel.mTipoRicorso;
		mChiaveAnnoRicorso = aModel.mChiaveAnnoRicorso;
		mChiaveProgrRicorso = aModel.mChiaveProgrRicorso;
		mStatoValidazione = aModel.mStatoValidazione;
		mDescTipoRicorso = aModel.mDescTipoRicorso;
		mDataArrivoCancelleriaIniziale = aModel.mDataArrivoCancelleriaIniziale;
		mDataArrivoCancelleriaFinale = aModel.mDataArrivoCancelleriaFinale;
	}

	// COSTRUTTORE MODEL
	public RicercaFascicoloSigeModel(String aChiaveUfficio, String aChiaveUfficioInserimento,
			BigDecimal aChiaveAnnoIniziale, BigDecimal aChiaveAnnoFinale, BigDecimal aChiaveProgrIniziale,
			BigDecimal aChiaveProgrFinale, Date aDataIscrizioneIniziale, Date aDataIscrizioneFinale,
			String aCodTipoAtto, String aCodOggettoSige, String aCodMagistrato, BigDecimal aIdSezione,
			Date aDataFinePendenza, Date aDataDefinizioneIniziale, Date aDataDefinizioneFinale,
			String aCodTipoRito,
			// MEV_65
			String aCodPosizioneGiuridica, String aCodNazione, String aDescPosizioneGiuridica,
			String aDescMagistrato, String aDescSezione, String aDescNazione, String aTipoRicorso,
			BigDecimal aChiaveAnnoRicorso, BigDecimal aChiaveProgrRicorso, String aStatoValidazione,
			String aDescTipoRicorso, Date aDataArrivoCancelleriaIniziale, Date aDataArrivoCancelleriaFinale) {

		mChiaveUfficio = aChiaveUfficio;
		mChiaveUfficioInserimento = aChiaveUfficioInserimento;
		mChiaveAnnoIniziale = aChiaveAnnoIniziale;
		mChiaveAnnoFinale = aChiaveAnnoFinale;
		mChiaveProgrIniziale = aChiaveProgrIniziale;
		mChiaveProgrFinale = aChiaveProgrFinale;
		mDataIscrizioneIniziale = aDataIscrizioneIniziale;
		mDataIscrizioneFinale = aDataIscrizioneFinale;
		mCodTipoAtto = aCodTipoAtto;
		mCodOggettoSige = aCodOggettoSige;
		mCodMagistrato = aCodMagistrato;
		mIdSezione = aIdSezione;
		mDataFinePendenza = aDataFinePendenza;
		mDataDefinizioneIniziale = aDataDefinizioneIniziale;
		mDataDefinizioneFinale = aDataDefinizioneFinale;
		mCodTipoRito = aCodTipoRito;
		mCodPosizioneGiuridica = aCodPosizioneGiuridica;
		mCodNazione = aCodNazione;
		mDescPosizioneGiuridica = aDescPosizioneGiuridica;
		mDescMagistrato = aDescMagistrato;
		mDescSezione = aDescSezione;
		mDescNazione = aDescNazione;
		mTipoRicorso = aTipoRicorso;
		mChiaveAnnoRicorso = aChiaveAnnoRicorso;
		mChiaveProgrRicorso = aChiaveProgrRicorso;
		mStatoValidazione = aStatoValidazione;
		mDescTipoRicorso = aDescTipoRicorso;
		mDataArrivoCancelleriaIniziale = aDataArrivoCancelleriaIniziale;
		mDataArrivoCancelleriaFinale = aDataArrivoCancelleriaFinale;
	}

	//
	// METODI GET()
	//
	public String getChiaveUfficio() {
		return mChiaveUfficio;
	}

	public String getChiaveUfficioInserimento() {
		return mChiaveUfficioInserimento;
	}

	public BigDecimal getChiaveAnnoIniziale() {
		return mChiaveAnnoIniziale;
	}

	public BigDecimal getChiaveAnnoFinale() {
		return mChiaveAnnoFinale;
	}

	public BigDecimal getChiaveProgrIniziale() {
		return mChiaveProgrIniziale;
	}

	public BigDecimal getChiaveProgrFinale() {
		return mChiaveProgrFinale;
	}

	public Date getDataIscrizioneIniziale() {
		return mDataIscrizioneIniziale;
	}

	public Date getDataIscrizioneFinale() {
		return mDataIscrizioneFinale;
	}

	public String getCodTipoAtto() {
		return mCodTipoAtto;
	}

	public String getCodOggettoSige() {
		return mCodOggettoSige;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public BigDecimal getIdSezione() {
		return mIdSezione;
	}

	public Date getDataFinePendenza() {
		return mDataFinePendenza;
	}

	public Date getDataDefinizioneIniziale() {
		return mDataDefinizioneIniziale;
	}

	public Date getDataDefinizioneFinale() {
		return mDataDefinizioneFinale;
	}

	public String getCodTipoRito() {
		return mCodTipoRito;
	}

	// MEV_65
	public String getCodPosizioneGiuridica() {
		return mCodPosizioneGiuridica;
	}

	public String getCodNazione() {
		return mCodNazione;
	}

	public String getDescPosizioneGiuridica() {
		return mDescPosizioneGiuridica;
	}

	public String getDescMagistrato() {
		return mDescMagistrato;
	}

	public String getDescSezione() {
		return mDescSezione;
	}

	public String getDescNazione() {
		return mDescNazione;
	}

	public String getTipoRicorso() {
		return mTipoRicorso;
	}

	public BigDecimal getChiaveAnnoRicorso() {
		return mChiaveAnnoRicorso;
	}

	public BigDecimal getChiaveProgrRicorso() {
		return mChiaveProgrRicorso;
	}

	public String getStatoValidazione() {
		return mStatoValidazione;
	}

	public String getDescTipoRicorso() {
		return mDescTipoRicorso;
	}

	public Date getDataArrivoCancelleriaIniziale() {
		return mDataArrivoCancelleriaIniziale;
	}

	public Date getDataArrivoCancelleriaFinale() {
		return mDataArrivoCancelleriaFinale;
	}
	// FINE MEV_65

	//
	// METODI SET()
	//
	public void setChiaveUfficio(String aValore) {
		mChiaveUfficio = aValore;
	}

	public void setChiaveUfficioInserimento(String aValore) {
		mChiaveUfficioInserimento = aValore;
	}

	public void setChiaveAnnoIniziale(BigDecimal aValore) {
		mChiaveAnnoIniziale = aValore;
	}

	public void setChiaveAnnoFinale(BigDecimal aValore) {
		mChiaveAnnoFinale = aValore;
	}

	public void setChiaveProgrIniziale(BigDecimal aValore) {
		mChiaveProgrIniziale = aValore;
	}

	public void setChiaveProgrFinale(BigDecimal aValore) {
		mChiaveProgrFinale = aValore;
	}

	public void setDataIscrizioneIniziale(Date aValore) {
		mDataIscrizioneIniziale = aValore;
	}

	public void setDataIscrizioneFinale(Date aValore) {
		mDataIscrizioneFinale = aValore;
	}

	public void setCodTipoAtto(String aValore) {
		mCodTipoAtto = aValore;
	}

	public void setCodOggettoSige(String aValore) {
		mCodOggettoSige = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setIdSezione(BigDecimal aValore) {
		mIdSezione = aValore;
	}

	public void setDataFinePendenza(Date aValore) {
		mDataFinePendenza = aValore;
	}

	public void setDataDefinizioneIniziale(Date aValore) {
		mDataDefinizioneIniziale = aValore;
	}

	public void setDataDefinizioneFinale(Date aValore) {
		mDataDefinizioneFinale = aValore;
	}

	public void setCodTipoRito(String aValore) {
		mCodTipoRito = aValore;
	}

	// MEV_65
	public void setCodPosizioneGiuridica(String aValore) {
		mCodPosizioneGiuridica = aValore;
	}

	public void setCodNazione(String aValore) {
		mCodNazione = aValore;
	}

	public void setDescPosizioneGiuridica(String aValore) {
		mDescPosizioneGiuridica = aValore;
	}

	public void setDescMagistrato(String aValore) {
		mDescMagistrato = aValore;
	}

	public void setDescSezione(String aValore) {
		mDescSezione = aValore;
	}

	public void setDescNazione(String aValore) {
		mDescNazione = aValore;
	}

	public void setTipoRicorso(String aValore) {
		mTipoRicorso = aValore;
	}

	public void setChiaveAnnoRicorso(BigDecimal aValore) {
		mChiaveAnnoRicorso = aValore;
	}

	public void setChiaveProgrRicorso(BigDecimal aValore) {
		mChiaveProgrRicorso = aValore;
	}

	public void setStatoValidazione(String aValore) {
		mStatoValidazione = aValore;
	}

	public void setDescTipoRicorso(String aValore) {
		mDescTipoRicorso = aValore;
	}

	public void setDataArrivoCancelleriaIniziale(Date aValore) {
		mDataArrivoCancelleriaIniziale = aValore;
	}

	public void setDataArrivoCancelleriaFinale(Date aValore) {
		mDataArrivoCancelleriaFinale = aValore;
	}
	// FINE MEV_65

}