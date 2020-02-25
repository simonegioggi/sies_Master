package siap.siep.alias.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.web.ActionSiap;
import siap.siep.alias.controller.AliasController;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaAliasSoggetto
 * </p>
 * <p>
 * Description:Classe Action per la ricerca di Alias dall'elenco della ricerca soggetto
 * </p>
 */
@SuppressWarnings("rawtypes")
public class ActRicercaAliasSoggetto extends ActionSiap implements ICostantiAlias {

	public String processRequest() throws F3BException {

		// prende dalla request l'id del soggetto
		BigDecimal IdSoggetto = getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO);

		// chiama il controller
		AliasController lCtrlAl = new AliasController();
		// Instanzia il vettore ed esegue la ricerca sugli alias tramite l'id del soggetto
		Vector lVect = lCtrlAl.ExRicercaAliasByIdSoggetto(IdSoggetto);
		// setta la risposta nella request
		setRequestAttribute("aliasvect", lVect);

		// Apre la pagina con l'elenco degli Alias relativi ad soggetto
		return PG_RICERCAALIASSOGGETTO;
	}

}