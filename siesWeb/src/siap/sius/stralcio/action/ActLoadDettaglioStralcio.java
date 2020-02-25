package siap.sius.stralcio.action;

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
 * Title: ActLoadDettaglioStralcio
 * </p>
 * <p>
 * Description: Classe Action per la load di Dettaglio Stralcio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioStralcio extends ActionSiap implements ICostantiStralcio {

	public String processRequest() throws Exception {

		// this.setLinkRitorno();
		// Recupero l'utente dalla sessione.
		// UtenteModel lUtenteMod = new
		// UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		EventoModel lEveMod = new EventoModel();

		// Lettura dell'Evento di Stralcio.
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lEveCtrl.ExRicercaEventoByKey(this.getRequestBigDecimalParameter(CAMPO_ID_EVENTO_STRALCIO));

		// Lettura dei fascicoli SIUS Stralciato e Destinazione Stralcio.
		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		FascicoloGPModel lFasDestStralcio = lCtrl
				.ExRicercaFascicoloByKey(lEveMod.getFasSiuIdFascicoloSiusDest());

		// Si Passa nella request l'Evento di Stralcio.
		setRequestAttribute("eventoStralcio", lEveMod);

		// Si Passa nella session il Procedimento Stralciato.
		FascicoloGPModel lFasStralcio = lCtrl.ExRicercaFascicoloByKey(lEveMod.getFasSiuIdFascicoloSius());
		setRequestAttribute("fascicoloStralcio", lFasStralcio);
		setSessionAttribute("fascicoloSiusGP", lFasStralcio);

		// Si Passa nella request il Procedimento Destinazione Stralcio.
		setRequestAttribute("fascicoloDestStralcio", lFasDestStralcio);

		// Si Passa nella request anno/numero del fascicolo SIUS DestStralcio e dataStralcio.
		// STUB 03/05/2004 setRequestAttribute("annoNumeroDestStralcio",
		// lFasDestStralcio.getFascicoloSiusModel().getChiaveAnno().toString()+"/"+lFasDestStralcio.getFascicoloSiusModel().getChiaveProgr().toString()
		// );
		setRequestAttribute("annoFascicoloDestStralcio",
				lFasDestStralcio.getFascicoloSiusModel().getChiaveAnno().toString());
		setRequestAttribute("progrFascicoloDestStralcio",
				lFasDestStralcio.getFascicoloSiusModel().getChiaveProgr().toString());
		setRequestAttribute("dataStralcio", DateUtils.getDateToString(
				lFasDestStralcio.getFascicoloSiusModel().getDataDefinizione(), "dd/MM/yyyy"));
		// STUB 03/05/2005 Aggiunto il parametro di flagIns.
		if (isRequestParameterNullObj("FlagIns"))
			setRequestAttribute("flagInsert", " ");
		else
			setRequestAttribute("flagInsert", getRequestStringParameter("FlagIns"));

		return PG_DETTAGLIOSTRALCIO;
	}

}