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

public class ActLoadRichiestaRelazioneCSSA extends ActionSiap implements ICostantiRichiestaAtti {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel()
				.getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// LISTA CSSA
		Collection lCol = DecodificheManager.getInstance().getTipoAutorita();
		// MEV10-s3: aggiunto controllo su tipologia di ufficio connesso
		String codTipoUfficio = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel().
				getCodTipoUfficio();
		String[] lStringFilter;
		if ("UDSM".equals(codTipoUfficio) || "TDSM".equals(codTipoUfficio))
			lStringFilter = new String[]{ "-", "40", "B5" };
		else
			lStringFilter = new String[]{ "40" };
		Option lOption2 = new Option(lCol);
		lOption2.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti", "" + lOption2);
		
		// INIZIO: MEV_2019-09 (D.lgs. 123/2018)
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lUltimoEventoRichAtti = lEveCtrl.ricercaUltimoEventoRichiestaAttiIsruttoriByIdFasc (((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("ultimoEventoRichAtti", lUltimoEventoRichAtti);
		// FINE: MEV_2019-09 (D.lgs. 123/2018)		
		
		return PG_LOAD_RICHIESTARELAZIONECSSA; // restituisce la jsp di VIEW
	}
}
