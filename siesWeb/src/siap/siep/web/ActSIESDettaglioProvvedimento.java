package siap.siep.web;

import java.math.BigDecimal;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActSIESDettaglioProvvedimento
 * </p>
 * <p>
 * Description: Clase padre di tutti i dettagli dei provvedimenti
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings("rawtypes")
public class ActSIESDettaglioProvvedimento extends ActionSiap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private boolean mIsFirstPena = false;

	public ActSIESDettaglioProvvedimento() {
	}

	public void caricaFascicoloInSessione(HttpServletRequest aRequest) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Costruttore di  ActSIESDettaglioProvvedimento");

		// Caricare in sessione il fascicolo legato all'evento
		try {
			// Controllo che risolve i problemi di sessione
			// nel caso un dettaglio di un evento venga richiamato da
			// una delle funzioni di ricerca

			String lStringIdEvento = aRequest.getParameter(ICostantiEvento.CAMPO_ID_EVENTO);
			BigDecimal lIdEvento = null;

			if (lStringIdEvento != null) {
				if (!lStringIdEvento.trim().equals(""))
					lIdEvento = new BigDecimal(lStringIdEvento.trim());
			}

			if (lIdEvento != null) {
				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoModel lEveMod = lCtrlEve.ExRicercaEventoByKey(lIdEvento);

				if (lEveMod != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lEveMod " + lEveMod);

					BigDecimal lIdFascicolo = lEveMod.getFasSieIdFascicoloSiep();

					IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
					FascicoloSiepModel lFasRet = lCtrlFasc.ExRicercaFascicoloByKeyNoError(lIdFascicolo);

					if (lFasRet != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("lFasRet " + lFasRet);

						setRequestAttribute("fascicolo", lFasRet);
						setSessionAttribute("fascicolo", lFasRet);

						setSessionAttribute("soggetto", lFasRet.getSoggetto());
						setSessionAttribute("sentenza", lFasRet.getSentenza());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore nel costruttore di ActSIESDettaglioProvvedimento " + ex);
		}
	}

	/**
	 * getPosizioneGiuridicaLuogoDetenzioneAltraCausa - ricerca la posisizone giuridica associata all'evento o
	 * al fascicolo siep, seleziona anche l'altra causa e l'istituto di detenzione
	 * 
	 * @param aIdEvento
	 *            - id dell'evento corrente
	 * @return PosizioneGiuridicaLuogoDetenzioneAltraCausaModel
	 * @throws F3BException
	 */
	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel getPosizioneGiuridicaLuogoDetenzioneAltraCausa(
			BigDecimal aIdEvento, BigDecimal aIdFascicolo) throws F3BException {
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByIdEvento(aIdEvento);

		if (lPos == null || (lPos != null && lPos.getPosizioneGiuridica() == null)) {
			Logger.getRootLogger().info("Sono in pos==null");
			lPos = lPosCtrl
					.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(aIdFascicolo);
		}
		return lPos;
	}

	/**
	 * getPosizioneGiuridica - restituisce la sola posizione giuridica legata all'evento o al fascicolo
	 * corrente
	 * 
	 * @param aIdEvento
	 * @param aIdFascicolo
	 * @return PosizioneGiuridicaModel
	 * @throws F3BException
	 */
	protected PosizioneGiuridicaModel getPosizioneGiuridica(BigDecimal aIdEvento, BigDecimal aIdFascicolo)
			throws F3BException {
		PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaByIdEvento(aIdEvento);

		if (lPos == null) {
			Logger.getRootLogger().info("Sono in pos giuridica = null");
			lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(aIdFascicolo);
		}
		return lPos;
	}

	/**
	 * getPosizioneGiuridicaPrecedente - restituisce la sola posizione giuridica precedente legata all'evento
	 * o al fascicolo corrente
	 * 
	 * @param aIdEvento
	 * @param aIdFascicolo
	 * @return PosizioneGiuridicaModel
	 * @throws F3BException
	 */
	protected PosizioneGiuridicaModel getPosizioneGiuridicaPrecedente(BigDecimal aIdEvento,
			BigDecimal aIdFascicolo) throws F3BException {
		PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaByIdEvento(aIdEvento);

		if (lPos == null) {
			Logger.getRootLogger().info("Sono in pos giuridica = null");
			lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(aIdFascicolo);
		}
		return lPos;
	}

	/**
	 * getPenaResidua - restituisce la Pena Residua associata all'evento corrente nel caso in cui è stato
	 * appena inserito e la pena non è ancora associata all'Evento viene ricercata per id fascicolo.
	 * 
	 * @param aIdEvento
	 * @return PenaResiduaModel
	 * @throws F3BException
	 */
	public PenaResiduaModel getPenaResidua(BigDecimal aIdEvento, BigDecimal aIdFascicolo)
			throws F3BException {
		IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenMod = lCtrlp.ExRicercaPenaResiduaByIdEvento(aIdEvento);
		if (lPenMod == null) {
			mIsFirstPena = true;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Sono nel caso della prima validazione!");
			lPenMod = lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(aIdFascicolo);
		}

		return lPenMod;
	}

	/**
	 * get Pena Residua Precedente Validata
	 * 
	 * @param aIdEvento
	 *            - Id dell'Evento
	 * @return PenaResiduaModel - Id del fascicolo Siep
	 * @throws F3BException
	 */
	protected PenaResiduaModel getPenaResiduaPrecedenteValidata(BigDecimal aIdEvento, BigDecimal aIdFascicolo)
			throws F3BException {
		IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenModPrecedente = null;

		if (mIsFirstPena)
			lPenModPrecedente = lCtrlp.ExRicercaPenaResiduaUltimaValidata(aIdFascicolo);
		else
			lPenModPrecedente = lCtrlp.ExRicercaPenaPrecedenteByKeyEvento(aIdFascicolo, aIdEvento);

		return lPenModPrecedente;
	}

	/**
	 * Restituisci l'evento istanza
	 * 
	 * @param aIdEvento
	 * @param aIdFascicolo
	 * @return EventoModel istanza
	 * @throws F3BException
	 */
	protected EventoModel getEventoIstanza(BigDecimal aIdEvento, BigDecimal aIdFascicolo)
			throws F3BException {

		EventoModel lEveModel = new EventoModel();
		EventoModel lReturn = null;
		IEvento lEvCtrl = SICOLookupRemote.getEventoRemote();

		if (aIdEvento != null) { // L'evento è legato all'istanza
			lReturn = lEvCtrl.ExRicercaEventoIstanzaByKey(aIdEvento);
		} else { // Non ho legato l'istanza all'evento
			lEveModel.setFasSieIdFascicoloSiep(aIdFascicolo);
			lEveModel.setCodTipoEvento("03");
			Vector lVectEvento = null;
			try {
				lVectEvento = lEvCtrl.ExRicercaEventoIstanza(aIdFascicolo);
			} catch (F3BException e) {
				if (e.getErrorCode() != F3BException.USER_MESSAGE) {
					throw e;
				}
			}

			if (lVectEvento != null) {
				if (lVectEvento.size() > 0) {
					lReturn = (EventoModel) lVectEvento.firstElement();
				}
			}
		}
		return lReturn;
	}

	/**
	 * Restituisci l'evento istanza rigettata
	 * 
	 * @param aIdEvento
	 * @param aIdFascicolo
	 * @return EventoModel istanza
	 * @throws F3BException
	 */
	protected EventoModel getEventoIstanzaRigettata(BigDecimal aIdEvento, BigDecimal aIdFascicolo)
			throws F3BException {

		// EventoModel lEveModel = new EventoModel();
		EventoModel lReturn = null;
		IEvento lEvCtrl = SICOLookupRemote.getEventoRemote();

		if (aIdEvento != null) { // L'evento è legato all'istanza
			lReturn = lEvCtrl.ExRicercaEventoIstanzaByKey(aIdEvento);
		} else { // Non ho legato l'istanza all'evento

			// Istanza
			EventoModel lEveIstanzaMod = new EventoModel();
			lEveIstanzaMod.setFasSieIdFascicoloSiep(aIdFascicolo);
			Vector lVectEvento = null;
			try {
				lVectEvento = lEvCtrl.ExRicercaEventoIstanzaRigettata(lEveIstanzaMod);
			} catch (F3BException e) {
				if (e.getErrorCode() != F3BException.USER_MESSAGE)
					throw e;
			}

			if (lVectEvento != null) {
				if (lVectEvento.size() > 0)
					lReturn = (EventoModel) lVectEvento.firstElement();
			}

		}
		return lReturn;
	}

}