package siap.siep.scadenzario.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_39
 * <p>
 * Title: ActLoadAggiornaScadenzaMisuraSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di Scadenzario per
 * </p>
 * <p>
 * aggiornamento Misura di Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 *
 * @author SGIOGGI
 * @version 1.0
 */
public class ActLoadAggiornaScadenzaMisuraSicurezza extends ActionSiap implements ICostantiScadenzario {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		ParametroModel pm = new ParametroModel();
		// Ricerca se esiste Periodo per quell'Ufficio
		pm.setCodUfficioValidita(getCodUfficioUtenteConnesso());
		pm.setNomeParametro("INIZIO MISURA");
		IParametro ip = SIEPLookupRemote.getParametroRemote();
		Vector parametri = ip.ExRicercaParametroUfficioConnesso(pm);

		// casistica in cui per l'ufficio sia già stato inserito un valore per questo PARAMETRO
		if (parametri != null && parametri.size() != 0) {
			ParametroModel par = (ParametroModel) parametri.get(0);
			setRequestAttribute("anni_scadenza", par.getAnni() != null ? par.getAnni().toString() : "0");
			setRequestAttribute("mesi_scadenza", par.getMesi() != null ? par.getMesi().toString() : "0");
			setRequestAttribute("giorni_scadenza",
					par.getGiorni() != null ? par.getGiorni().toString() : "0");
		} else {
			// casistica in cui per l'ufficio NON sia già stato inserito un valore per questo PARAMETRO
			// IN QUESTO CASO IL VALORE DI DEFAULT DEVE ESSERE DI 6 MESI
			setRequestAttribute("anni_scadenza", "0");
			setRequestAttribute("mesi_scadenza", "6");
			setRequestAttribute("giorni_scadenza", "0");
		}

		// pagina di ritorno
		return PG_LOAD_AGGIORNASCADENZA_MS;
	}

}