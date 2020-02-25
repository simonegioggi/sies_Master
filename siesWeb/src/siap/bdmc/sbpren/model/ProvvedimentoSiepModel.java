package siap.bdmc.sbpren.model;

import java.util.Vector;

import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notefascicolo.model.NoteFascicoloModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ProvvedimentoSiepModel
 * </p>
 * <p>
 * Description: Aggregato Model del Provvedimento appena creato ed inserito da dati Rege
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings("rawtypes")
public class ProvvedimentoSiepModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4774600167413058837L;

	private Vector mSoggettiOmonimi;
	private Vector mReati;
	private Vector mCircostanze;
	private Vector mMisCautBdmc;

	// private Vector mDifensori;
	private SoggettoModel mSoggetto;
	private SentenzaModel mSentenza;
	private UtenteModel mUtente; // Utente che effettua l'importazione dei dati da Rege
	private FascicoloSiepModel mFascicolo;
	private NoteFascicoloModel mNoteFascicolo;

	public ProvvedimentoSiepModel() {
		init();
	}

	private void init() {
		mMisCautBdmc = null;
		mSoggettiOmonimi = null;
		mSentenza = null;
		mReati = null;
		mCircostanze = null;
		// mDifensori = null;
		mSoggetto = null;
		mUtente = null;
		mFascicolo = null;
		mNoteFascicolo = null;
	}

	// Metodi GET
	public Vector getReati() {
		return mReati;
	}

	public Vector getCircostanze() {
		return mCircostanze;
	}

	public Vector getSoggettiOmonimi() {
		return mSoggettiOmonimi;
	}

	public SentenzaModel getSentenza() {
		return mSentenza;
	}

	public Vector getMisCautBdmc() {
		return mMisCautBdmc;
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public UtenteModel getUtente() {
		return mUtente;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicolo;
	}

	public NoteFascicoloModel getNoteFascicoloModel() {
		return mNoteFascicolo;
	}

	// Metodi SET

	public void setReati(Vector aValore) {
		mReati = aValore;
	}

	public void setCircostanze(Vector aValore) {
		mCircostanze = aValore;
	}

	public void setSoggettiOmonimi(Vector aValore) {
		mSoggettiOmonimi = aValore;
	}

	public void setSentenza(SentenzaModel aValore) {
		mSentenza = aValore;
	}

	public void setMisCautBdmc(Vector aValore) {
		mMisCautBdmc = aValore;
	}

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setUtente(UtenteModel aValore) {
		mUtente = aValore;
	}

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicolo = aValore;
	}

	public void setNoteFascicolo(NoteFascicoloModel aValore) {
		mNoteFascicolo = aValore;
	}

}