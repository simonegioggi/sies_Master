package siap.siep.beneficio.action;

import java.util.Vector;

import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActListaProcAssociatiIndu
 * </p>
 * <p>
 * Description: Classe Action elenco dei procedimenti associati al soggetto
 * </p>
 * <p>
 * (Gestione Revoca Beneficio - Revoca Indulto in altro provvedimento)
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

public class ActListaProcAssociatiIndu extends ActionSiap implements ICostantiBeneficio {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// definisce il nodel di tipo sogetto che poi usa nella ricerca come parametro di input
		SoggettoModel lSogMod = (SoggettoModel) getSessionAttribute("soggetto");
		// FascicoloSiepModel lFasc = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// chiama il contreller
		// ricercafascicolo x soggetto e Tipo Beneficio-
		// cerca tutte le sentenze e i relativi numeri di fascicolo correlati al soggetto in questione

		String TipoBen = "= '03'";
		ISentenza lCtrlSenFas = SIEPLookupRemote.getSentenzaRemote();
		Vector lSentenzaFascicolo = lCtrlSenFas.ExRicercaSentenzaFascicolo(lSogMod, TipoBen);
		// SentenzaFascicoloModel lSenFasMod = new SentenzaFascicoloModel();
		// Iterator itx = lSentenzaFascicolo.iterator();
		// while (itx.hasNext()) {
		// lSenFasMod = (SentenzaFascicoloModel) itx.next();
		// }
		setRequestAttribute("lSentenzaFascicolo", lSentenzaFascicolo);

		return PG_LISTA_PROCEDIMENTI_ASSOCIATI_INDU;
	}

}