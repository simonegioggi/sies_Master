package siap.siep.istruttoria.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notiziareato.controller.NotiziaReatoController;
import siap.siep.notiziareato.model.NotiziaReatoModel;

/**
 * <p>
 * Title: ActDettaglioRichiestaCodiceCui
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio della Richiesta Codice CUI
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
public class ActDettaglioRichiestaCodiceCui extends ActionSiap implements ICostantiIstruttoria {

	public String processRequest() throws Exception {

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(new BigDecimal(lId));

		NotiziaReatoModel lNotMod = new NotiziaReatoModel();
		// chiama il controller della Notizia di Reato
		NotiziaReatoController lNRCtrl = new NotiziaReatoController();
		// prende dalla Session l'ID del procedimento
		lNotMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		// se non ci sono notizie di reato accedo comunque alla pagina
		Vector lVect;
		try {
			lVect = lNRCtrl.ExRicercaNotiziaReato(lNotMod);
		} catch (Exception ex) {
			lVect = null;
		}

		// se vengo dall'elenco istruttoria risetto il parametro per il template
		if (isRequestParameterNullObj("ListaTemplate")) {
			if (lEveMod.getEvento().getCodMotivo().equals("0557")) {
				setRequestAttribute("ListaTemplate", "0");
			} else if (lEveMod.getEvento().getCodMotivo().equals("0578")) {
				setRequestAttribute("ListaTemplate", "1");
			}
		}

		// setta la risposta nella request
		setRequestAttribute("elenconotiziareato", lVect);
		setRequestAttribute("eventonotifica", lEveMod);

		return ICostantiIstruttoria.PG_DETTAGLIO_RICHIESTA_CODICE_CUI;
	}

}