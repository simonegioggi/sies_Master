package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.Vector;

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

	public String processRequest() throws Exception {

		// Gestione pulsante di ritorno
		// setLinkRitorno();

		// String idCivilmenteObbligato = getRequestStringParameter(CAMPO_ID_CIVILMENTE_OBBLIGATO);
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		// CivilmenteObbligatoModel com = ico
		// .ExRicercaCivilmenteObbligatoByKey(new BigDecimal(idCivilmenteObbligato));
		// setRequestAttribute("civilmenteObbligato", com);
		String idFascicoloSiep = getRequestStringParameter(CAMPO_ID_FASCICOLO_SIEP);
		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(new BigDecimal(idFascicoloSiep));
		setRequestAttribute("civilmenteObbligati", coms);
		setRequestAttribute("idFascicoloSiep", idFascicoloSiep);
		setRequestAttribute("Modificabile", "SI");
		setRequestAttribute("Cancellabile", "SI");

		return PG_DETTAGLIO_CIVILMENTE_OBBLIGATO;
	}

}