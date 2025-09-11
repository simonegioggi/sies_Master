package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaEstrattoSentenza extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// LISTA UFFICI
		// 04/06/2007 Aggiunte altre tipologie di ufficio
		// Collection lTipoIstituto = DecodificheManager.getInstance().getTipoUfficioS();
		// String[] lStringFilter = {"DIB","CAP","GP","DIBM"};
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoUfficio();
		String[] lStringFilter = { "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIPMI", "GIP", "GIPM",
				"GP", "GUPMI", "GUP", "GUPM", "PT", "PM", "PMM", "PMPT", "PGCAP", "PGMI", "PGMID", "PMI",
				"TRIBSD", "CAPSM", "TMI", "DIB", "DIBM" };

		Option lOption = new Option(lTipoIstituto);
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);
		
		// INIZIO: MEV_9 (D.lgs. 123/2018)
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lUltimoEventoRichAtti = lEveCtrl.ricercaUltimoEventoRichiestaAttiIsruttoriByIdFasc (((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("ultimoEventoRichAtti", lUltimoEventoRichAtti);
		// FINE: MEV_9 (D.lgs. 123/2018)
		
		return PG_LOAD_RICHIESTAESTRATTOSENTENZA; // restituisce la jsp di VIEW
	}

}