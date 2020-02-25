package siap.regesies.regesentenza.model;

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
	private static final long serialVersionUID = -6084319984921550840L;

	private Vector mResidenze;
	private SentenzaModel mSentenza;
	private Vector mReati;
	private Vector mCircostanze;
	private Vector mNotizieDiReato;
	// private Vector mDifensori;
	private SoggettoModel mSoggetto;
	private UtenteModel mUtente; // Utente che effettua l'importazione dei dati da Rege
	private FascicoloSiepModel mFascicolo;
	private NoteFascicoloModel mNoteFascicolo;

	public ProvvedimentoSiepModel() {
		init();
	}

	private void init() {
		mResidenze = null;
		mSentenza = null;
		mReati = null;
		mCircostanze = null;
		mNotizieDiReato = null;
		// mDifensori = null;
		mSoggetto = null;
		mUtente = null;
		mFascicolo = null;
		mNoteFascicolo = null;
	}

	/**
	 * Costruttore di copia
	 * 
	 * public ProvvedimentoSiepModel(ProvvedimentoModel aProvv) { init();
	 * 
	 * if (aProvv.getResidenze() != null) mResidenze = new Vector(aProvv.getResidenze()); if
	 * (aProvv.getResidenze() != null) mResidenze = new Vector(aProvv.getResidenze()); if
	 * (aProvv.getSentenza() != null) mSentenza = new SentenzaModel(aProvv.getSentenza()); if
	 * (aProvv.getReati() != null) mReati = new Vector(aProvv.getReati()); if (aProvv.getCircostanze() !=
	 * null) mCircostanze = new Vector(aProvv.getCircostanze()); if (aProvv.getNotizieDiReato() != null)
	 * mNotizieDiReato = new Vector(aProvv.getNotizieDiReato()); if (aProvv.getDifensori() != null) mDifensori
	 * = new Vector(aProvv.getDifensori());
	 * 
	 * mIsResidenzeCheck = aProvv.isResidenzeCheck(); mIsReatoCheck = aProvv.isReatoCheck();
	 * mIsNotiziaReatoCheck = aProvv.isNotiziaReatoCheck(); mIsCircostanzaCheck = aProvv.isCircostanzaCheck();
	 * mEstendiFascicoloSiep = aProvv.isEstendiFascicoloSiep(); mIdSoggettoOmonimo =
	 * aProvv.getIdSoggettoOmonimo();
	 * 
	 * if (aProvv.getSoggetto() != null) mSoggetto = new SoggettoModel(aProvv.getSoggetto()); if
	 * (aProvv.getUtente() != null) mUtente = new UtenteModel(aProvv.getUtente()); if
	 * (aProvv.getFascicoloSiep() != null) mFascicolo = new FascicoloSiepModel(aProvv.getFascicoloSiep()); }
	 */

	// Metodi GET
	public Vector getResidenze() {
		return mResidenze;
	}

	public SentenzaModel getSentenza() {
		return mSentenza;
	}

	public Vector getReati() {
		return mReati;
	}

	public Vector getCircostanze() {
		return mCircostanze;
	}

	public Vector getNotizieDiReato() {
		return mNotizieDiReato;
	}

	// public Vector getDifensori(){return mDifensori;}
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
	public void setResidenze(Vector aValore) {
		mResidenze = aValore;
	}

	public void setSentenza(SentenzaModel aValore) {
		mSentenza = aValore;
	}

	public void setReati(Vector aValore) {
		mReati = aValore;
	}

	public void setCircostanze(Vector aValore) {
		mCircostanze = aValore;
	}

	public void setNotizieDiReato(Vector aValore) {
		mNotizieDiReato = aValore;
	}

	// public void setDifensori(Vector aValore){ mDifensori = aValore;}
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