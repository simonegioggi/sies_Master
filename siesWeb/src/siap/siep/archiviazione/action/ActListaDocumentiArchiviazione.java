package siap.siep.archiviazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;

@SuppressWarnings("rawtypes")
public class ActListaDocumentiArchiviazione extends ActionSiap implements ICostantiArchiviazione {

	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSiep = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

		String[] aMotivo = { "0825", "0824", "0800", "0801", "0807", "0802", "0803", "0804", "0805", "0806",
				"0816", "0817", "0809", "2140", "0264" };

		String[] aTipo = { "02", "03" };
		EventoModel lEveMod = new EventoModel();
		lEveMod.setFlagDocumentoRegistrato("S");
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicoloSiep);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		Vector lListaEventiArch = lCtrl.ExRicercaEventiPerMotivoTipoProvv(aMotivo, aTipo, lEveMod);

		setRequestAttribute("documentiArch", lListaEventiArch);

		return PG_LOAD_LISTA;
	}

}