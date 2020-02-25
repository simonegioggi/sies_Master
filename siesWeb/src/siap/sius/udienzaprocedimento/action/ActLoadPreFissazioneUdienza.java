package siap.sius.udienzaprocedimento.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciFissazioneUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della form di inserimento Fissazione Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadPreFissazioneUdienza extends ActRicercaFSPuntuale
		implements ICostantiUdienzaProcedimento, ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	String mRectPage = PG_LOAD_INSERISCIPREFISSAZIONEUDIENZA;
	IUdienzaProcedimento mUdiProcModel = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		BigDecimal lIdFasSius = null;
		FascicoloGPModel lFasGPMod = null;

		// Gestione bottone di ritorno
		if (isRequestParameterNullObj("ritorno"))
			setLinkRitorno();
		else
			gestioneRitorno();

		if (isRequestParameterNullObj("ritorno")
				&& isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)) {
			// Invoca la process Request della superclasse se provengo dal menu'.
			super.processRequest();
		}

		// Fascicolo Sius
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo SIUS non presente in sessione!");

		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		if (lFasGPMod == null || lFasGPMod.getFascicoloSiusModel() == null
				|| lFasGPMod.getGeneraleProcedimentoModel() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dati Fascicolo non presenti in sessione!");

		// Viene effettuato il controllo sulla preesistenza di un Provvedimento definitorio
		// già emesso per il Fascicolo SIUS.
		// Se esiste almeno un provvedimento di questo tipo non può esserne emesso una prefissazione udienza.
		RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(
				lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();
		if (lEsistenzaDoc)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Per il procedimento indicato è già stato emesso un provvedimento. "
							+ "Non è consentito emettere un nuovo provvedimento");

		lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		// LookUp del Controller
		mUdiProcModel = SIUSLookupRemote.getUdienzaProcedimentoRemote();

		// Se non esiste una Udienza già fissata o prefissata per il Fascicolo
		if (analisiUdienzeProcedimento(
				lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento())) {
			if (this.IsFascicoloSiusModificabile() == false)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						ICostantiFascicoloSius.MSG_NON_MODIFICABILE);

			// Lock per evitare la fissazione contemporanea di 2 Udienze per lo stesso fascicolo
			LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIUS",
					lIdFasSius.toString(), getCodUtenteConnesso(), getSession().getId());
			if (lck != null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il  " + lck.getEntity()
						+ " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
				return IWebConstants.PG_MESSAGE;
			}
		} // endif analisiUdienzeProcedimento
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return mRectPage;
	}

	/**
	 * Viene analizzata la lista delle udienze già assegnate al Procedimento. Se esiste una udienza già
	 * Fissata non si può inserire una Pre Fissazione. Se esiste una PRE_FISSAZIONE Udienza non se ne può
	 * inserire una nuova.
	 * 
	 * @param aUdiPro
	 * @return true se può essere effettuata la Fissazione.
	 */
	private boolean analisiUdienzeProcedimento(BigDecimal aIdGenProc) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".analisiUdienzeProcedimento: inizio");
		boolean retValue = true;
		// Se nella request c'è l'ID di UDIENZA_PROCEDIMENTO siamo nel caso di una Rifissazione
		if (!isRequestParameterNullObj(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO)
				&& isRequestParameterNullObj("dettaglio")) {
			setRequestAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO,
					getRequestStringParameter(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Rifissazione Udienza"
					+ getRequestStringParameter(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO));
		} else {

			Vector lUdiProVect = null;
			// Preventivamente si controlla l'esistenza di una Udienza per il fascicolo
			lUdiProVect = mUdiProcModel.ExRicercaUdienzaProcedimentoByGeneraleProcedimento(aIdGenProc);

			if (lUdiProVect != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Numero di record trovati ->" + lUdiProVect.size());

				if (ricercaFissazione(lUdiProVect))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Esiste già una Udienza Fissata per il fascicolo!");

				// Ora Ricerca eventuale PRE Fissazione
				Iterator itx = lUdiProVect.iterator();
				while (itx.hasNext()) {
					UdienzaProcedimentoModel lUdiPro = (UdienzaProcedimentoModel) itx.next();
					if (lUdiPro != null && lUdiPro.getFlagRinviata() != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Flag ->" + lUdiPro.getFlagRinviata());

						if (lUdiPro.getFlagRinviata()
								.compareTo(ICostantiUdienzaProcedimento.UDIENZA_PREFISSATA) == 0) {
							if (isUdienzaProcedimentoOK(lUdiPro, 0)) {
								// Gestione Udienza già PRE-FISSATA
								gestionePreFissazione(lUdiPro);
								retValue = false;
								break;
							}
						}
					}
				}
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Elenco Udienze preesistenti vuoto");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".analisiUdienzeProcedimento: fine");
		return retValue;
	}

	/*
	 * La funzione ricerca nell'elenco delle Udienza_procedimento una Fissazione Udienza. Ritorna true se la
	 * trova, false nell'altro caso.
	 */
	private boolean ricercaFissazione(Vector aUdiProVect) {

		boolean lRet = false;

		Iterator itx = aUdiProVect.iterator();
		while (itx.hasNext()) {
			UdienzaProcedimentoModel lUdiPro = (UdienzaProcedimentoModel) itx.next();
			if (lUdiPro != null && lUdiPro.getFlagRinviata() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Flag ->" + lUdiPro.getFlagRinviata());

				if (lUdiPro.getFlagRinviata().compareTo(ICostantiUdienzaProcedimento.UDIENZA_FISSATA) == 0
						|| lUdiPro.getFlagRinviata()
								.compareTo(ICostantiUdienzaProcedimento.UDIENZA_SEGUITO_RINVIO) == 0) {
					if (isUdienzaProcedimentoOK(lUdiPro, 0)) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Udienza già fissata!! ->" + lUdiPro.getFlagRinviata());
						lRet = true;
						break;
					}
				}
			}
		}
		return lRet;
	}

	private void gestionePreFissazione(UdienzaProcedimentoModel aUdiPro) throws Exception {

		// Si prepara la pagina di dettaglio
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".gestionePreFissazione: inizio");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerca UDIENZA ");
		UdienzaModel lUdienza = null;
		IUdienza lUdi = SIUSLookupRemote.getUdienzaRemote();
		lUdienza = lUdi.ExRicercaUdienzaByKey(aUdiPro.getUdiIdUdienza());
		if (lUdienza == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Udienza non trovata !");
		// L'Udienza viene passata nella request
		setRequestAttribute("udienza", lUdienza);
		// l'ID UDIENZA_PROCEDIMENTO viene passato nella request.
		setRequestAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO,
				aUdiPro.getIdUdienzaProcedimento().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".gestionePreFissazione: fine");
	}

	// Controlla la coerenza dei dati nel record di UDIENZA_PROCEDIMENTO.
	// I controlli sono:
	// La valorizzazione del campo UDI_ID_UDIENZA viene sempre controllata.
	// La valorizzazione del campo EVE_ID_EVENTO viene controllata solo se il parametro aTipoControlli > 0.
	private boolean isUdienzaProcedimentoOK(UdienzaProcedimentoModel aUdiPro, int aTipoControlli) {

		boolean retValue = true;
		if (aUdiPro.getUdiIdUdienza() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn(" manca UDI_ID_UDIENZA nel record in UDIENZA_PROCEDIMENTO! ID ->"
					+ aUdiPro.getIdUdienzaProcedimento());
			retValue = false;
		}

		if (aTipoControlli > 0 && aUdiPro.getEveIdEvento() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn(" manca EVE_ID_UDIENZA nel record in UDIENZA_PROCEDIMENTO! ID ->"
					+ aUdiPro.getIdUdienzaProcedimento());
			retValue = false;
		}

		return retValue;
	}

}