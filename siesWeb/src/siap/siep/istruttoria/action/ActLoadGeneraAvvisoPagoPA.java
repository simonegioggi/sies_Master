package siap.siep.istruttoria.action;

import f3b.web.IWebConstants;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;

/**
 * MEV_2023-13: aggiunta classe di caricamento avviso
 * Title: ActLoadGeneraAvvisoPagoPA 
 * Description: Classe che permette di generare un avviso di pagamento per PagoPA
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadGeneraAvvisoPagoPA extends ActionSiap implements ICostantiIstruttoria {

	public String processRequest() throws Exception {

		if (isSessionAttributeNullObj("fascicolo")) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
					+ "siap.siep.istruttoria.action.ActLoadGeneraAvvisoPagoPA";
			return lPage;
		}

		isFascicoloSiepDiCompetenza();
		isEventoNonValidato();

		UtenteModel lUtenteModel = getUtenteConnesso();

		setRequestAttribute("utente", lUtenteModel);

		return PG_LOAD_GENERA_AVVISO_PAGOPA;
	}

}