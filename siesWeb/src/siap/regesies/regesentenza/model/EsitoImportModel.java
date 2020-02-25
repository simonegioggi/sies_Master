package siap.regesies.regesentenza.model;

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
	private static final long serialVersionUID = 6086831468492258527L;

	private ProvvedimentoModel mProvvedimento;
	private ProvvedimentoSiepModel mProvvedimentoSiep;

	private String mEsitoSoggetto;
	private String mEsitoCancellazioneRege;
	private Vector mEsitoResidenze;
	private String mEsitoSoggettiOmonimi;
	private String mEsitoSentenza;
	private Vector mEsitoReati;
	private Vector mEsitoCircostanze;
	private Vector mEsitoNotizieDiReato;
	private String mEsitoDifensori;
	private String mEsitodSoggettoOmonimo;
	private String mEsitoUtente; // Utente che effettua l'importazione dei dati da Rege
	private String mEsitoFascicolo;
	private String mEsitoDispositivo;

	/**
	 * Costruttore di default
	 */
	public EsitoImportModel() {
		mProvvedimento = null;

		mEsitoSoggetto = ICostantiRegeSentenza.ESITO_POSITIVO;
		mEsitoCancellazioneRege = ICostantiRegeSentenza.ESITO_POSITIVO;
		mEsitoResidenze = null;
		mEsitoSoggettiOmonimi = ICostantiRegeSentenza.ESITO_POSITIVO;
		mEsitoSentenza = ICostantiRegeSentenza.ESITO_POSITIVO;
		mEsitoReati = null;
		mEsitoCircostanze = null;
		mEsitoNotizieDiReato = null;
		mEsitoDifensori = ICostantiRegeSentenza.ESITO_POSITIVO;
		mEsitodSoggettoOmonimo = ICostantiRegeSentenza.ESITO_POSITIVO;
		mEsitoUtente = ""; // Utente che effettua l'importazione dei dati da Rege
		mEsitoFascicolo = ICostantiRegeSentenza.ESITO_POSITIVO;
		mProvvedimentoSiep = null;
		mEsitoDispositivo = ICostantiRegeSentenza.ESITO_POSITIVO;
	}

	/**
	 * Costruttore di default
	 */
	public EsitoImportModel(EsitoImportModel aEsito) {
		mProvvedimento = new ProvvedimentoModel(aEsito.getProvvedimento());

		mEsitoSoggetto = aEsito.getEsitoSoggetto();
		mEsitoCancellazioneRege = aEsito.getEsitoCancellazioneRege();
		mEsitoResidenze = aEsito.getEsitoResidenze();
		mEsitoSoggettiOmonimi = aEsito.getEsitoSoggettiOmonimi();
		mEsitoSentenza = aEsito.getEsitoSentenza();
		mEsitoReati = aEsito.getEsitoReati();
		mEsitoCircostanze = aEsito.getEsitoCircostanze();
		mEsitoNotizieDiReato = aEsito.getEsitoNotizieDiReato();
		mEsitoDifensori = aEsito.getEsitoDifensori();
		mEsitodSoggettoOmonimo = aEsito.getEsitodSoggettoOmonimo();
		mEsitoUtente = aEsito.getEsitoUtente(); // Utente che effettua l'importazione dei dati da Rege
		mEsitoFascicolo = aEsito.getEsitoFascicolo();
		mProvvedimentoSiep = aEsito.getProvvedimentoSiep();
		mEsitoDispositivo = aEsito.getEsitoDispositivo();
	}

	/*
	 * Metodi GET
	 */

	public ProvvedimentoModel getProvvedimento() {
		return mProvvedimento;
	}

	public ProvvedimentoSiepModel getProvvedimentoSiep() {
		return mProvvedimentoSiep;
	}

	public String getEsitoCancellazioneRege() {
		return mEsitoCancellazioneRege;
	}

	public String getEsitoSoggetto() {
		return mEsitoSoggetto;
	}

	public Vector getEsitoResidenze() {
		return mEsitoResidenze;
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

	public Vector getEsitoNotizieDiReato() {
		return mEsitoNotizieDiReato;
	}

	public String getEsitoDifensori() {
		return mEsitoDifensori;
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

	public String getEsitoDispositivo() {
		return mEsitoDispositivo;
	}

	/**
	 * Metodi SET
	 * 
	 * @param aValore
	 * @return
	 */
	public void setProvvedimento(ProvvedimentoModel aValore) {
		mProvvedimento = aValore;
	}

	public void setProvvedimentoSiep(ProvvedimentoSiepModel aValore) {
		mProvvedimentoSiep = aValore;
	}

	public void setEsitoCancellazioneRege(String aValore) {
		mEsitoCancellazioneRege = aValore;
	}

	public void setEsitoSoggetto(String aValore) {
		mEsitoSoggetto = aValore;
	}

	public void setEsitoResidenze(Vector aValore) {
		mEsitoResidenze = aValore;
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

	public void setEsitoNotizieDiReato(Vector aValore) {
		mEsitoNotizieDiReato = aValore;
	}

	public void setEsitoDifensori(String aValore) {
		mEsitoDifensori = aValore;
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

	public void setEsitoDispositivo(String aValore) {
		mEsitoDispositivo = aValore;
	}

}