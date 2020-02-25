package siap.sius.unificazione.action;

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
 * Title: ActLoadDettaglioUnificazione
 * </p>
 * <p>
 * Description: Classe Action per la load di Dettaglio Unificazione
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
public class ActLoadDettaglioUnificazione extends ActionSiap implements ICostantiUnificazione {

	public String processRequest() throws Exception {

		// this.setLinkRitorno(); // STUB 03/04/2004
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
		// STUB 03/05/2005 Aggiunto il parametro di flagIns.
		if (isRequestParameterNullObj("FlagIns"))
			setRequestAttribute("flagInsert", " ");
		else
			setRequestAttribute("flagInsert", getRequestStringParameter("FlagIns"));

		return PG_DETTAGLIOUNIFICAZIONE;
	}

}