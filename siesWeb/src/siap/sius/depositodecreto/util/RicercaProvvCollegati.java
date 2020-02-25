package siap.sius.depositodecreto.util;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.util.SICOLookupRemote;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoFascicoloLicenzeModel;
import siap.sius.depositodecreto.model.DepositoDecretoFascicoloModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RicercaProvvedimentiCollegati.
 * </p>
 * <p>
 * Description: La classe raggruppa funzioni di utilità usati per la ricerca di Provvedimenti tra loro
 * collegati.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Luigi
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RicercaProvvCollegati {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// Fascicolo SIUS di riferimento
//	private BigDecimal mIdFascicolo = null;
	// Codice Ufficio di riferimento
	private String mCodUff = "";
	// Controller necessari per le ricerche
//	private IUfficio mUffCtrl = null;
	private IDepositoDecreto mDecCtrl = null;
//	private IEvento mEveCtrl = null;
	private ILicenzaPeriodiLibAnticipata mLicCtrl = null;

	// Costruttore semplice
	public RicercaProvvCollegati() {

//		mIdFascicolo = null;
//		mUffCtrl = null;
		mDecCtrl = null;
//		mEveCtrl = null;
		mLicCtrl = null;
	}

	// Costruttore con inizializzazione Ufficio
	public RicercaProvvCollegati(String aCodUfficio) throws Exception {

		mCodUff = aCodUfficio;
	}

	/**
	 * <p>
	 * Title:Ricerca del decreto dal Soggetto.
	 * </p>
	 * <p>
	 * Description: La funzione effettua la ricerca di un decreto di tipo Permesso definito dal parametro
	 * aTipoDecreto e legato ad una Licenza del tipo stabilito da aTipoLicenza. Nel caso la ricerca
	 * restituisca più decreti, la funzione restituisce solo il primo (il più recente). La funzione
	 * restituisce oltre al Decreto anche dei dati del Fascicolo SIUS, raggruppati nel model
	 * DepositoDecretoFascicoloModel.
	 * 
	 * @param aIdSoggetto
	 * @param aTipoDecreto
	 * @param aTipoLicenza
	 * @return DepositoDecretoFascicoloModel
	 * @throws Exception
	 */
	public DepositoDecretoFascicoloModel ricercaDecretoPermessoDaSog(BigDecimal aIdSoggetto,
			String aTipoDecreto, String aTipoLicenza) throws Exception {

		DepositoDecretoFascicoloModel lDepDecMod = null;
		Vector lDecreti = null;
		lDecreti = ricercaDecretiPermessoDaSog(aIdSoggetto, aTipoDecreto, aTipoLicenza);
		if (lDecreti != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreti di permesso riferimenti trovati: " + lDecreti.size());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nessun decreto permesso di riferimento trovato");

		if (lDecreti != null && lDecreti.size() > 0)
			lDepDecMod = (DepositoDecretoFascicoloModel) lDecreti.get(0);
		return lDepDecMod;
	}

	// Ricerca del decreto e fascicolo di riferimento dall'Id Evento
	/*
	 * INUTILE ! public DepositoDecretoFascicoloModel ricercaDecretoPermessoDaIdEvento(BigDecimal aIdEvento)
	 * throws Exception { DepositoDecretoFascicoloModel lDepDecMod = null;
	 * 
	 * // Ricerca Decreti if (mDecCtrl == null) mDecCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
	 * lDepDecMod = mDecCtrl.ExRicercaDecretoFascicoloByIdEvento(aIdEvento); return lDepDecMod; }
	 */

	public Vector ricercaPermessi(BigDecimal aIdEvento) throws Exception {

		Vector lPermessi = null;
		try {
			// Ricerca Permessi
			if (mLicCtrl == null)
				mLicCtrl = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
			lPermessi = mLicCtrl.ExRicercaLicenzeByEve(aIdEvento);
		} catch (F3BException fex) {
			// Si filtra l'eccezione per elementi non trovati
			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ricercaPermessi -> " + fex);
			} else
				throw fex;
		} catch (Exception ex) {
			throw ex;
		}
		return lPermessi;
	}

	private Vector ricercaDecretiPermessoDaSog(BigDecimal aIdSoggetto, String aTipoDecreto,
			String aTipoLicenza) throws Exception {

		Vector lDecreti = null;
		try {
			// Ricerca Decreti
			if (mDecCtrl == null)
				mDecCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
			lDecreti = mDecCtrl.ExRicercaDecretoFascicoloByIdSoggetto(aIdSoggetto, aTipoDecreto,
					aTipoLicenza, mCodUff);
		}
		/*
		 * catch(F3BException fex) { // Si filtra l'eccezione per elementi non trovati if
		 * (fex.getErrorCode()== fex.USER_MESSAGE) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
		 * istanza siesLogger al posto di LogF3B.getLogger() siesLogger.info("ricercaDecretiPermessoDaSog -> "
		 * + fex ); } else throw fex; }
		 */
		catch (Exception ex) {
			throw ex;
		}
		return lDecreti;
	}

	/**
	 * Ricerca Decreti Permessi e Licenze ad essi collegati. Restituisce un Vector di modelli di tipo
	 * DepositoDecretoFascicoloLicenzeModel.
	 * 
	 * @param aIdSoggetto
	 * @param aTipoDecreto
	 * @param aTipoLicenza
	 * @return Vector
	 * @throws Exception
	 */
	public Vector ricercaDecretiLicenzeDaSog(BigDecimal aIdSoggetto, String aTipoDecreto, String aTipoLicenza)
			throws Exception {

		Vector lDecreti = null;
		Vector lDecretiLicenze = new Vector();
		try {
			// Ricerca Decreti
			if (mDecCtrl == null)
				mDecCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
			lDecreti = mDecCtrl.ExRicercaDecretoFascicoloByIdSoggetto(aIdSoggetto, aTipoDecreto,
					aTipoLicenza, mCodUff);

			if (lDecreti != null && lDecreti.size() > 0) {
				Iterator itx = lDecreti.iterator();
				Vector lLicenze = null;
				while (itx.hasNext()) {
					// Ricerca licenze collegate al decreto
					DepositoDecretoFascicoloModel lDecretoFas = (DepositoDecretoFascicoloModel) itx.next();

					if (lDecretoFas.getDepositoDecreto() != null
							&& lDecretoFas.getDepositoDecreto().getIdEventoGenerato() != null) {
						lLicenze = ricercaPermessi(lDecretoFas.getDepositoDecreto().getIdEventoGenerato());
					}
					lDecretiLicenze.add(new DepositoDecretoFascicoloLicenzeModel(lDecretoFas
							.getDepositoDecreto(), lDecretoFas.getFascicolo(), lLicenze));
				}
			}
		} catch (F3BException fex) {
			// Si filtra l'eccezione per elementi non trovati
			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ricercaDecretiPermessoDaSog -> " + fex);
			} else
				throw fex;
		} catch (Exception ex) {
			throw ex;
		}
		return lDecretiLicenze;
	}

}