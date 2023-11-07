package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13 
 * Title: ActCancellaCivilmenteObbligato 
 * Description: Classe Action per la cancellazione del Civilmente Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActCancellaCivilmenteObbligato extends ActionSiap implements ICostantiPagoPA {

	public String processRequest() throws Exception {

		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		String idFascicoloSiep = getRequestStringParameter(CAMPO_ID_FASCICOLO_SIEP);
		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(new BigDecimal(idFascicoloSiep));

		Iterator<CivilmenteObbligatoModel> itx = coms.iterator();
		while (itx.hasNext()) {
			CivilmenteObbligatoModel com = (CivilmenteObbligatoModel) itx.next();
			ico.ExCancellaCivilmenteObbligato(com.getIdCivilmenteObbligato());
		}

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + idFascicoloSiep;
	}

}