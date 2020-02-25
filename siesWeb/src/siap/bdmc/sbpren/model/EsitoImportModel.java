package siap.bdmc.sbpren.model;

import java.util.Vector;

import siap.regesies.regesentenza.action.ICostantiRegeSentenza;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: EsitoImportModel
 * </p>
 * <p>
 * Description: Aggregato Model che incapsula il risultato dell'importr da REGE a SIEP
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings("rawtypes")
public class EsitoImportModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8941972568548239826L;

	private ProvvedimentoModelBDMC mProvvedimento;
	private ProvvedimentoSiepModel mProvvedimentoSiep;

	private String mEsitoSoggetto;
	private String mEsitoCancellazioneBDMC;
	private Vector mEsitoPeriPren;
	private String mEsitoSoggettiOmonimi;
	private String mEsitoSentenza;
	private Vector mEsitoReati;
	private Vector mEsitoCircostanze;
	private String mEsitodSoggettoOmonimo;
	private String mEsitoUtente; // Utente che effettua l'importazione dei dati da Rege
	private String mEsitoFascicolo;

	/**
	 * Costruttore di default
	 */
	public EsitoImportModel() {
		mProvvedimento = null;

		mEsitoSoggetto = ICostantiRegeSentenza.ESITO_POSITIVO;
		mEsitoCancellazioneBDMC = ICostantiRegeSentenza.ESITO_POSITIVO;
		mEsitoPeriPren = null;
		mEsitoSoggettiOmonimi = ICostantiRegeSentenza.ESITO_POSITIVO;
		mEsitoSentenza = ICostantiRegeSentenza.ESITO_POSITIVO;
		mEsitoReati = null;
		mEsitoCircostanze = null;
		mEsitodSoggettoOmonimo = ICostantiRegeSentenza.ESITO_POSITIVO;
		mEsitoUtente = ""; // Utente che effettua l'importazione dei dati da Rege
		mEsitoFascicolo = ICostantiRegeSentenza.ESITO_POSITIVO;
		mProvvedimentoSiep = null;
	}

	/**
	 * Costruttore di default
	 */
	public EsitoImportModel(EsitoImportModel aEsito) {
		mProvvedimento = new ProvvedimentoModelBDMC(aEsito.getProvvedimento());

		mEsitoSoggetto = aEsito.getEsitoSoggetto();
		mEsitoCancellazioneBDMC = aEsito.getEsitoCancellazioneRege();
		mEsitoPeriPren = aEsito.getEsitoPeriPren();
		mEsitoSoggettiOmonimi = aEsito.getEsitoSoggettiOmonimi();
		mEsitoSentenza = aEsito.getEsitoSentenza();
		mEsitoReati = aEsito.getEsitoReati();
		mEsitoCircostanze = aEsito.getEsitoCircostanze();
		mEsitodSoggettoOmonimo = aEsito.getEsitodSoggettoOmonimo();
		mEsitoUtente = aEsito.getEsitoUtente(); // Utente che effettua l'importazione dei dati da Rege
		mEsitoFascicolo = aEsito.getEsitoFascicolo();
		mProvvedimentoSiep = aEsito.getProvvedimentoSiep();
	}

	/*
	 * Metodi GET
	 */

	public ProvvedimentoModelBDMC getProvvedimento() {
		return mProvvedimento;
	}

	public ProvvedimentoSiepModel getProvvedimentoSiep() {
		return mProvvedimentoSiep;
	}

	public String getEsitoCancellazioneRege() {
		return mEsitoCancellazioneBDMC;
	}

	public String getEsitoSoggetto() {
		return mEsitoSoggetto;
	}

	public Vector getEsitoPeriPren() {
		return mEsitoPeriPren;
	}

	public String getEsitoSoggettiOmonimi() {
		return mEsitoSoggettiOmonimi;
	}

	public String getEsitoSentenza() {
		return mEsitoSentenza;
	}

	public Vector getEsitoReati() {
		return mEsitoReati;
	}

	public Vector getEsitoCircostanze() {
		return mEsitoCircostanze;
	}

	public String getEsitodSoggettoOmonimo() {
		return mEsitodSoggettoOmonimo;
	}

	public String getEsitoUtente() {
		return mEsitoUtente;
	}

	public String getEsitoFascicolo() {
		return mEsitoFascicolo;
	}

	/**
	 * Metodi SET
	 * 
	 * @param aValore
	 * @return
	 */
	public void setProvvedimento(ProvvedimentoModelBDMC aValore) {
		mProvvedimento = aValore;
	}

	public void setProvvedimentoSiep(ProvvedimentoSiepModel aValore) {
		mProvvedimentoSiep = aValore;
	}

	public void setEsitoCancellazioneBDMC(String aValore) {
		mEsitoCancellazioneBDMC = aValore;
	}

	public void setEsitoSoggetto(String aValore) {
		mEsitoSoggetto = aValore;
	}

	public void setEsitoPeriPren(Vector aValore) {
		mEsitoPeriPren = aValore;
	}

	public void setEsitoSoggettiOmonimi(String aValore) {
		mEsitoSoggettiOmonimi = aValore;
	}

	public void setEsitoSentenza(String aValore) {
		mEsitoSentenza = aValore;
	}

	public void setEsitoReati(Vector aValore) {
		mEsitoReati = aValore;
	}

	public void setEsitoCircostanze(Vector aValore) {
		mEsitoCircostanze = aValore;
	}

	public void setEsitodSoggettoOmonimo(String aValore) {
		mEsitodSoggettoOmonimo = aValore;
	}

	public void setEsitoUtente(String aValore) {
		mEsitoUtente = aValore;
	}

	public void setEsitoFascicolo(String aValore) {
		mEsitoFascicolo = aValore;
	}

}