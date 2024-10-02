package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.DateUtils;
import f3b.web.html.Option;

public class ActLoadRichiestaOrdinDecrAltroTdSUdS extends ActionSiap implements ICostantiRichiestaAtti {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel()
				.getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// LISTA UFFICI
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoUfficio();
		// MEV10-s3: aggiunte tipologia di ufficio
		String[] lStringFilter = { "TDS", "UDS", "TDSM", "UDSM" };

		Option lOption = new Option(lTipoIstituto);
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);

		// INIZIO: MEV_2019-09 (D.lgs. 123/2018)
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lUltimoEventoRichAtti = lEveCtrl.ricercaUltimoEventoRichiestaAttiIsruttoriByIdFasc (((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("ultimoEventoRichAtti", lUltimoEventoRichAtti);
		// FINE: MEV_2019-09 (D.lgs. 123/2018)	
		
		return PG_LOAD_RICHIESTAORDINDECRALTROTDSUDS; // restituisce la jsp di VIEW
	}
}