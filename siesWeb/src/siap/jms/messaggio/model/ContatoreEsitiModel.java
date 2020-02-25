package siap.jms.messaggio.model;

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.config.JMSProperties;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ContatoreEsitiModel
 * </p>
 * <p>
 * Description: Classe di Helper per le ricerche Multiple
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ContatoreEsitiModel extends GenericModel implements ICostantiJMS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4069150853270458918L;

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);
		
	private int mTrovati; // BDi che hanno effettuto e risposto alla ricerca con trovato
	private int mNonTrovati; // BDi che hanno effettuto e risposto alla ricerca con non trovato
	private int mNonSpediti; // BDi non raggiungibili
	private int mDestinazioni; // BDi totali coinvolte per la ricera
	private int mInAttesa; // BDi che non hanno risposto
	private int mNonCoinvolte; // BDi non coinvolte per la ricerca

	public ContatoreEsitiModel() {
		mTrovati = 0;
		mNonTrovati = 0;
		mNonSpediti = 0;
		mInAttesa = 0;

		try { // Destinazioni coinvolte per la ricerca
			Vector allBDI = new Vector();
			allBDI = (Vector) JMSProperties.getInstance().getAllBDI().clone();
			// Le BDI trovate nel database meno la BDI mittente
			mDestinazioni = allBDI.size() - 1;
			mNonCoinvolte = 28 - mDestinazioni;
		} catch (Exception ex) { // Se per qualche motivo non riesco a recuperare le informazioni dal DB
									// settodi default i parametri a 28 BDI non coinvolte 0.
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Errore nel costruttore di ContatoreEsitiModel.");
			mDestinazioni = 28;
			mNonCoinvolte = 0;
		}

	}

	public int getTrovati() {
		return mTrovati;
	}

	public int getNonTrovati() {
		return mNonTrovati;
	}

	public int getNonSpediti() {
		return mNonSpediti;
	}

	public int getDestinazioni() {
		return mDestinazioni;
	}

	public int getInAttesa() {
		return mInAttesa;
	}

	public int getNonCoinvolte() {
		return mNonCoinvolte;
	}

	public void setTrovati(int aValore) {
		mTrovati = aValore;
	}

	public void setNonTrovati(int aValore) {
		mNonTrovati = aValore;
	}

	public void setNonSpediti(int aValore) {
		mNonSpediti = aValore;
	}

	public void setDestinazioni(int aValore) {
		mDestinazioni = aValore;
	}

	public void setInAttesa(int aValore) {
		mInAttesa = aValore;
	}

	public void setNonCoinvolte(int aValore) {
		mNonCoinvolte = aValore;
	}

	/**
	 * Calcolo BDI che non hanno risposto alla richiesta di ricerca
	 */
	public void calcolaBDIInAttesa() {
		mInAttesa = mDestinazioni - (mTrovati + mNonTrovati + mNonSpediti);
	}

}