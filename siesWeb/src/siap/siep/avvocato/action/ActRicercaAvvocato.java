package siap.siep.avvocato.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
//import siap.siep.avvocato.controller.AvvocatoController;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings("rawtypes")
public class ActRicercaAvvocato extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		AvvocatoModel lAvvMod = new AvvocatoModel();
		Vector lVect = null;
		// AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
		lAvvMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		// MEV_21: aggiungo controllo se dalla pagina la ricerca è su TUTTI I FORI!!!
		if (!isRequestChecked(CAMPO_FLAG_TUTTI_FORI))
			lAvvMod.setForo(getRequestStringParameter(CAMPO_FORO));

		lAvvMod.setCodUffAppartenenza(getCodUfficioUtenteConnesso());

		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();

		try {
			lVect = lCtrl.ExRicercaAvvocatoPerUffApparteneza(lAvvMod);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nessun Elemento trovato");
		}

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("avvocato", lVect);

		String lPage = PG_RICERCAAVVOCATO;

		if (getRequestStringParameter("modalita").compareTo("BREVE") == 0)
			lPage = PG_RICERCAAVVOCATOBREVE;

		return lPage;
	}

}