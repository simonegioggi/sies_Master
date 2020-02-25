package siap.sius.decretounificazione.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioDecretoUnificazione
 * </p>
 * <p>
 * Description: Classe Action per la load di Dettaglio Decreto Unificazione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioDecretoUnificazione extends ActionSiap implements ICostantiDecretoUnificazione {

	public String processRequest() throws Exception {

		this.setLinkRitorno(); // STUB
		// UtenteModel lUtenteMod = new
		// UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		EventoModel lEveMod = new EventoModel();
		FascicoloGPModel lFasUnificante = new FascicoloGPModel();
		FascicoloGPModel lFasUnificato = new FascicoloGPModel();

		// Lettura dell'Evento di Unificazione.
		String lId = getRequestStringParameter(CAMPO_ID_EVENTO_UNIFICAZIONE);
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lEveCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

		// Lettura dei fascicoli SIUS Unificante e Unificato.
		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lFasUnificato = lCtrl.ExRicercaFascicoloByKey(lEveMod.getFasSiuIdFascicoloSius());
		lFasUnificante = lCtrl
				.ExRicercaFascicoloByKey(lFasUnificato.getFascicoloSiusModel().getFasSiuIdFascicoloSius());

		// Si Passa nella request l'Evento di Unificazione.
		setRequestAttribute("eventoUnificazione", lEveMod);

		// Si Passa nella request il Procedimento unificante.
		setRequestAttribute("fascicoloUnificante", lFasUnificante);

		// Si Passa nella request anno/numero del fascicolo SIUS Unificato e dataUnificazione.
		// STUB 03/05/2004 setRequestAttribute("annoNumeroUnificato",
		// lFasUnificato.getFascicoloSiusModel().getChiaveAnno().toString()+"/"+lFasUnificato.getFascicoloSiusModel().getChiaveProgr().toString()
		// );
		setRequestAttribute("annoFascicoloUnificato",
				lFasUnificato.getFascicoloSiusModel().getChiaveAnno().toString());
		setRequestAttribute("progrFascicoloUnificato",
				lFasUnificato.getFascicoloSiusModel().getChiaveProgr().toString());
		setRequestAttribute("dataUnificazione", DateUtils
				.getDateToString(lFasUnificato.getFascicoloSiusModel().getDataDefinizione(), "dd/MM/yyyy"));

		return PG_DETTAGLIODECRETOUNIFICAZIONE;
	}

}