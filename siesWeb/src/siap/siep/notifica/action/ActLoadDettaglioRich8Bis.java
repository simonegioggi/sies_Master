package siap.siep.notifica.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioRich8Bis
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Scadenzario
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
public class ActLoadDettaglioRich8Bis extends ActionSiap implements ICostantiNotifica {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		Vector idRinnovi = new Vector();
		RinnovoModel lRinModUno = new RinnovoModel();
		RinnovoModel lRinModDue = new RinnovoModel();
		// controllo se provengo dall'upload per passare id rinnovi modificati
		if (!this.isRequestParameterNullObj("idRinnnovoUno")) {
			lRinModUno.setIdRinnovo(this.getRequestBigDecimalParameter("idRinnnovoUno"));
			idRinnovi.add(lRinModUno);
			if (!this.isRequestParameterNullObj("idRinnnovoDue")
					&& !this.getRequestStringParameter("idRinnnovoDue").equals("null")) {
				lRinModDue.setIdRinnovo(this.getRequestBigDecimalParameter("idRinnnovoDue"));

				idRinnovi.add(lRinModDue);
			}
		} else {
			idRinnovi = (Vector) this.getRequestAttribute("idRinnovi");

		}
		Vector rinnovo = new Vector();
		RinnovoModel lRinMod = new RinnovoModel();
		RinnovoModel lRinModRic = new RinnovoModel();

		IRinnovo lCtlRin = SIEPLookupRemote.getRinnovoRemote();

		for (int i = 0; i < idRinnovi.size(); i++) {
			lRinMod = (RinnovoModel) idRinnovi.get(i);
			lRinModRic = lCtlRin.ExRicercaRinnovoByKey(lRinMod.getIdRinnovo());
			rinnovo.add(lRinModRic);

		}

		EventoNotificaModel lEveNot = new EventoNotificaModel();
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(), "LS");

		setRequestAttribute("rinnovi", rinnovo);

		this.setRequestAttribute("eventonotifica", lEveNot);

		return PG_LOAD_DETTAGLIO_RICH_INFO_8_BIS; // restituisce la jsp di VIEW
	}

}