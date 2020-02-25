package siap.sico.evento.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;

/**
 * Classe Action per recuperare la lista degli ordini di esecuzione emessi
 * 
 * @author
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActListaOE extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSiep = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

		List lListaOrdiniEsecuzione = null;
		// lListaOrdiniEsecuzione = lCtrlEvento.ExRicercaEventoByFascicoloSiep( lIdFascicoloSiep);
		lListaOrdiniEsecuzione = lCtrlEvento.ExRicercaOEPerRestituzioneByFascicolo(lIdFascicoloSiep);

		Vector lOrdiniEsecuzioneNotifiche = new Vector();

		if (lListaOrdiniEsecuzione != null) {
			for (Iterator itx = lListaOrdiniEsecuzione.iterator(); itx.hasNext();) {
				EventoModel lOrdineEsecuzione = (EventoModel) itx.next();
				EventoNotificaModel lEveNotMod = lCtrlEvento
						.ExRicercaEventoNotificaByKey(lOrdineEsecuzione.getIdEvento());
				lOrdiniEsecuzioneNotifiche.add(lEveNotMod);
			}
		}

		setRequestAttribute("OrdiniEsecuzione", lOrdiniEsecuzioneNotifiche);

		return PG_LISTA_ORDINI_ESECUZIONE;
		// return IWebConstants.ROOT_DIR + "files/siap/sico/evento/ListaOrdiniEsecuzione.jsp";
	}

}