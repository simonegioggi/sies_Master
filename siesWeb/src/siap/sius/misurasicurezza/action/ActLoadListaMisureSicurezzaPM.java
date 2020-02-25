package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.List;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadListaMisureSicurezzaPM extends ActMisuraSicurezza implements ICostantiSiusMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		List lListaMisureSius = null;
		IPeriodoAltraMisura lCtrl = SIUSLookupRemote.getPeriodoAltraMisuraRemote();

		// 14/05/2008 Se provengo dal DettaglioProcedimentoSIEP, ho il parametro CAMPO_ID_FASCICOLO_SIEP nella
		// request.
		String lIdFasSiep = null;
		if (!this.isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			lIdFasSiep = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
			setRequestAttribute("IdFascicoloSiep ", lIdFasSiep);
		}

		if (lIdFasSiep != null && lIdFasSiep.length() > 1)
			lListaMisureSius = lCtrl.ExRicercaMisuraSicurezzaByIdSiep(new BigDecimal(lIdFasSiep));

		// 16/05/2008 Preleva l'evento collegato all'ultimo elemento di lListaSanzioniSius
		// per controllare se si riferisce a un provvedimento validato.
		if (lListaMisureSius.size() > 0) {
			PeriodoAltraMisuraModel ultimoPAS = (PeriodoAltraMisuraModel) (lListaMisureSius
					.get(lListaMisureSius.size() - 1));
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoModel lEveMod = lCtrlEve.ExRicercaEventoByKey(ultimoPAS.getEveIdEvento());
			setRequestAttribute("eventoUltimoPAS", lEveMod);
		}
		setRequestAttribute("listaMisureSius", lListaMisureSius);
		return PG_LOAD_LISTAMISURESICUREZZA;

	}
}
