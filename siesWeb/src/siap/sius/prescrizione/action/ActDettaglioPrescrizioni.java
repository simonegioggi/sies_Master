package siap.sius.prescrizione.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.prescrizione.controller.IPrescrizione;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title:ActDettaglioPrescrizioni
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioPrescrizioni extends ActionSiap implements ICostantiPrescrizione {

	public String processRequest() throws Exception {

		// FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		// lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		IPrescrizione lCtrl = SIUSLookupRemote.getPrescrizioneRemote();
		// String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		BigDecimal LIdEve = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		Vector lPrescrizioni = new Vector();
		try {
			/*
			 * Le prescrizioni sono collegate all'evento e non più al deposito ordinanza. Luigi 12-12-2003
			 */
			lPrescrizioni = lCtrl.ExRicercaPrescrizioneByEvento(LIdEve);
			// lPrescrizioni =
			// lCtrl.ExRicercaPrescrizioneByOrdinanza(getRequestBigDecimalParameter(CAMPO_DEP_OPID_DEPOSITO_ORDINANZA_PC));
		} catch (F3BException ex) {
		}

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrlEve.ExRicercaEventoNotificaByKey(LIdEve);

		setRequestAttribute("eventonotifica", lEveMod);

		// setRequestAttribute("modifica",getRequestStringParameter("modifica"));

		setRequestAttribute("prescrizioni", lPrescrizioni);

		return PG_LOAD_DETTAGLIOPRESCRIZIONE;
	}

}