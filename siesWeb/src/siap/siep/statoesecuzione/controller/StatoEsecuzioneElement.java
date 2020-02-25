package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

import f3b.controller.GenericController;
import siap.siep.statoesecuzione.config.StampaProperties;
import siap.siep.statoesecuzione.model.EventoModel;

/**
 * StatoEsecuzioneElement Classe che generalizza il concetto di Elemento che compare all'intero dello stato di
 * Esecuzione
 * 
 * @author Giselda De Vita
 */
@SuppressWarnings("rawtypes")
public class StatoEsecuzioneElement extends GenericController implements AStatoEsecuzioneElement {

	protected Hashtable mHashPenaResidua;
	protected Hashtable mHashEventiRiferimento;
	protected Hashtable mHashAnnotazioni;
	protected Hashtable mHashMisure;
	protected EventoModel mEventoStatoEsecuzione;

	/* Stringa che indica LA detratta e da detrarre */
	protected String mStringLiberazioneAnticipata = null;
	// 20/05/2014 Nuova L.A. - DL 146/2014
	protected String mStringNewLiberazioneAnticipata = null;

	// Vector di Eve id Eventi per la memorizzazione degli eventi legati
	// agli eventi correnti (tipo Ordinanza/Decreti etc.)
	protected Vector mEveIdEvento;

	protected static StampaProperties mCostanti = StampaProperties.getInstance();

	/**
	 * Costruttore che referenzia e pulisce le hash table
	 */
	public StatoEsecuzioneElement() {
		mHashPenaResidua = new Hashtable();
		mHashEventiRiferimento = new Hashtable();
		mHashAnnotazioni = new Hashtable();
		mHashMisure = new Hashtable();
		mEveIdEvento = new Vector();
		mHashPenaResidua.clear();
		mHashEventiRiferimento.clear();
		mHashAnnotazioni.clear();
		mHashMisure.clear();
	}

	/**
	 * Costruttore che referenzia e pulisce le hash table
	 */
	public StatoEsecuzioneElement(StatoEsecuzioneElement aCopy) {
		this.mHashPenaResidua = aCopy.getHashPenaResidua();
		this.mHashEventiRiferimento = aCopy.getHashEventiRiferimento();
		this.mHashAnnotazioni = aCopy.getHashAnnotazioni();
		this.mHashMisure = aCopy.getHashMisure();
		this.mEveIdEvento = aCopy.getEveIdEvento();
	}

	/*
	 * public static StatoEsecuzioneElement getInstance() { if( mStatoEsecuzioneElement == null) {
	 * mStatoEsecuzioneElement = new StatoEsecuzioneElement(); }
	 * 
	 * return mStatoEsecuzioneElement; }
	 */

	/**
	 * Metodi SET
	 * 
	 * @param aValore
	 */
	public void setHashPenaResidua(Hashtable aValore) {
		mHashPenaResidua = aValore;
	}

	public void setHashEventiRiferimento(Hashtable aValore) {
		mHashEventiRiferimento = aValore;
	}

	public void setHashAnnotazioni(Hashtable aValore) {
		mHashAnnotazioni = aValore;
	}

	public void setHashMisure(Hashtable aValore) {
		mHashMisure = aValore;
	}

	/**
	 * Metodi GET degli attributi
	 * 
	 * @return
	 */
	public Hashtable getHashPenaResidua() {
		return mHashPenaResidua;
	}

	public Hashtable getHashEventiRiferimento() {
		return mHashEventiRiferimento;
	}

	public Hashtable getHashAnnotazioni() {
		return mHashAnnotazioni;
	}

	public Hashtable getHashMisure() {
		return mHashMisure;
	}

	public void elabora(siap.sico.evento.model.EventoModel aEvento) {
	}

	public EventoModel getEventoModel() {
		return mEventoStatoEsecuzione;
	}

	public Vector getEveIdEvento() {
		return mEveIdEvento;
	}

	public void setEveIdEvento(Vector eveIdEvento) {
		mEveIdEvento = eveIdEvento;
	}

	public boolean searchEveIdEvento(BigDecimal lEveIdEvento) {
		boolean lResult = false;

		if (mEveIdEvento != null && mEveIdEvento.size() > 0)
			lResult = this.mEveIdEvento.contains(lEveIdEvento);

		return lResult;
	}

	public String getStringLiberazioneAnticipata() {
		return mStringLiberazioneAnticipata;
	}

	public void setStringLiberazioneAnticipata(String stringLiberazioneAnticipata) {
		mStringLiberazioneAnticipata = stringLiberazioneAnticipata;
	}

	// 20/05/2014 Nuova L.A. - DL 146/2014
	public String getStringNewLiberazioneAnticipata() {
		return mStringNewLiberazioneAnticipata;
	}

	public void setStringNewLiberazioneAnticipata(String stringNewLiberazioneAnticipata) {
		mStringNewLiberazioneAnticipata = stringNewLiberazioneAnticipata;
	}

}