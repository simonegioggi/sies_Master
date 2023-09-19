package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadInserisciRateizzazione extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// se già presenti dati per il fascicolo allora carico la form del dettaglio
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Recupero la pena Complessiva da visualizzare (multa e ammenda)
		IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
		DettaglioPenaComplessivaModel lDettMod = lCtrl
				.ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(fsm.getIdFascicoloSiep());
		setRequestAttribute("dettaglioPenaComplessiva", lDettMod);

		// 2023.09.19 - Vado in inserimento SOLO se non ci sono rate Libere
		//
		if (!isRequestParameterNullEmptyObj("inserimento") && getRequestStringParameter("inserimento").equals("true")) {
		  // Provengo dal dettaglio tasto aggiungi
      setRequestAttribute("modalita", "I");
      return PG_LOAD_INSERISCI_RATEIZZAZIONE_PP;
		}
		else {
		  // Provengo dal Menu. In presenza di rate vado sempre sul dettaglio 
		  IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		  
		  //Vector<RateizzazionePPModel> rateizzazioniLibere = irpp.exRicercaRateizzazioniLibereByIdFasc(fsm.getIdFascicoloSiep());
		  Vector<RateizzazionePPModel> rateizzazioni = irpp.exRicercaRateizzazioniByIdFasc(fsm.getIdFascicoloSiep());
		  if (!rateizzazioni.isEmpty()) {
	      String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
	          + "=siap.siep.rateizzazionepp.action.ActLoadDettagloRateizzazione";
	      return lPage;    
		  }
		  else {
        setRequestAttribute("modalita", "I");
        return PG_LOAD_INSERISCI_RATEIZZAZIONE_PP;
		  }		  
		}
		
		
/*	
		// MEV_2023-33: aggiunto controllo per storicizzazione evento OIP
		// Ricerca i pagamenti per id Facicolo
		Vector<RateizzazionePPModel> rateizzazioni = new Vector<>();
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		rateizzazioni = irpp.exRicercaRateizzazioniByIdFasc(fsm.getIdFascicoloSiep());
    
		boolean isEventoRateizzazioneAnnullato = false;
		if (!rateizzazioni.isEmpty()) {
			Iterator<RateizzazionePPModel> iterRPPM = rateizzazioni.iterator();
			while (iterRPPM.hasNext()) {
				RateizzazionePPModel rppm = iterRPPM.next();
				BigDecimal idEvento = rppm.getEveIdEvento();
				if (!Utils.isNullObj(idEvento)) {
					IEvento ie = SICOLookupRemote.getEventoRemote();
					EventoModel em = ie.ExRicercaEventoByKey(idEvento);
					isEventoRateizzazioneAnnullato = "A".equals(em.getFlagDocumentoRegistrato());
				} else {
					isEventoRateizzazioneAnnullato = false;
					break;
				}
			}
		}

		if (rateizzazioni.size() > 0 && !isEventoRateizzazioneAnnullato) {
			siesLogger.debug("Presenti già rate per il fascicolo. Carico il dettaglio");
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.rateizzazionepp.action.ActLoadDettagloRateizzazione";
			return lPage;
		} else {
			// Se fascicolo validato non posso consentire l'inserimento
			if ("S".equals(fsm.getFlagValidato())) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Modalita' di Pagamento non presente. "
						+ "Impossibile procedere all'inserimento in quanto il procedimento risulta gia' "
						+ "validato.<BR>Per inserire i dati annullare la validazione del procedimento dal "
						+ "menu' 'Funzioni Amministrative'.");
				lRedirigi.setAction("siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&"
						+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + fsm.getIdFascicoloSiep());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			} else {
				setRequestAttribute("modalita", "I");
				return PG_LOAD_INSERISCI_RATEIZZAZIONE_PP;
			}
		}
		
		*/
	}

}