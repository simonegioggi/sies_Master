package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;

public class ActListaProvvedimentiGiudiceCassazione extends ActionSiap implements ICostantiMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSiep = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

		String[] lCodici = { "MS01", "MS02", "MS03", "MS04" };
		Vector lListaProvv = null;
		IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();

		lListaProvv = lCtrl.ExRicercaDecretoOrdinanzaSiepGiudiceCassazione(lIdFascicoloSiep, lCodici);

		setRequestAttribute("ListaProvv", lListaProvv);

		if (getRequestStringParameter("formname").compareTo("ArchGiuCassa") == 0)
			return PG_LISTA_INSERT_PROVV_GIUDICE_CASSAZIONE; // Inserimento Archiviazione
		else
			return PG_LISTA_PROVV_GIUDICE_CASSAZIONE; // Modifica Archiviazione
	}

}