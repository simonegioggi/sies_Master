package siap.sico.evento.action;

/**
* <p>Title: ActLoadDettaglioEvento</p>
* <p>Description: Classe Action per la load dettaglio di Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;

@SuppressWarnings("rawtypes")
public class ActLoadDettaglioOmesseNotifiche extends ActionSiap implements ICostantiEvento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(CAMPO_ID_EVENTO);

		INotifica lCtrl = SIEPLookupRemote.getNotificaRemote();
		Vector lNot = lCtrl.ExRicercaEstesaNotificaDataAvvNotificaNullByKeyEvento(lIdEvento);

		setRequestAttribute("notifiche", lNot);

		// Ricerca il Fascicolo Siep per metterlo in sessione
		// nel caso il Dettaglio venga richiamato dall'Elenco OE Omesse Notifiche
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);

		if (lEveMod != null && lEveMod.getFasSieIdFascicoloSiep() != null) {
			IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFasRet = lCtrlFasc
					.ExRicercaFascicoloByKey(lEveMod.getFasSieIdFascicoloSiep());

			if (lFasRet != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lFasRet " + lFasRet);

				setRequestAttribute("fascicolo", lFasRet);
				setSessionAttribute("fascicolo", lFasRet);
			}
		}

		return ICostantiEvento.PG_RICERCA_NOTIFICHE_LS;
	}

}