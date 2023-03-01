package siap.siep.pagoPA.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13 
 * Title: ActDettaglioCivilmenteObbligato 
 * Description: Classe Action per il dettaglio del Civilmente Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActDettaglioCivilmenteObbligato extends ActionSiap implements ICostantiPagoPA {

	public String processRequest() throws F3BException {

		String idCivilmenteObbligato = getRequestStringParameter(CAMPO_ID_CIVILMENTE_OBBLIGATO);
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		CivilmenteObbligatoModel com = ico
				.ExRicercaCivilmenteObbligatoByKey(new BigDecimal(idCivilmenteObbligato));
		setRequestAttribute("civilmenteObbligato", com);

		return PG_DETTAGLIO_CIVILMENTE_OBBLIGATO;
	}

}