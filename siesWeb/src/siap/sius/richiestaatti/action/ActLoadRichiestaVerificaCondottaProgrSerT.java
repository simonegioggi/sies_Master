package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaVerificaCondottaProgrSerT extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Decodifica DESTINATARIO
		Collection lCol = (DecodificheManager.getInstance()).getTipoAutorita();
		setRequestAttribute("codTipoUfficioS", "29");
		setRequestAttribute("descTipoUfficioS", DecodificheUtils.getDescbyCode(lCol, "29"));

		// INIZIO: MEV_9 (D.lgs. 123/2018)
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lUltimoEventoRichAtti = lEveCtrl.ricercaUltimoEventoRichiestaAttiIsruttoriByIdFasc (((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("ultimoEventoRichAtti", lUltimoEventoRichAtti);
		// FINE: MEV_9 (D.lgs. 123/2018)
		
		return PG_LOAD_RICHIESTAVERIFICACONDOTTAPROGRSERT; // restituisce la jsp di VIEW
	}

}