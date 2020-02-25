package siap.siep.misuraalternativa.action;

import java.util.Collection;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;

public class ActLoadInserisciMACess51bisDetDomTerm extends ActCessazione51bis {

	/**
	 * n.b. L'inserimento viene effettuato in due fasi a causa della presenza del calcolo della pena
	 * (eventuale) Fase 1: si proviene dalla griglia della funzioni, si carica la form di registrazione
	 * dell'ordinanza di cessazione e eventuale calcolo della pena Fase 2: si proviene dalla Action di
	 * inserimento dell'ordinanza e calcolo pena e si carica la form per l'inserimento dei dati el
	 * provvedimento di esecuzione
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		String lEsito = null;
		if (isRequestParameterNullObj("isInsProvvSorv")) {
			lEsito = super.loadInserisciProvvedimetoSorveglianza();
		} else {
			lEsito = super.loadInserisciProvvedimetoEsecuzione();
		}

		if (lEsito != null)
			return lEsito;

		// ==========================================================================
		//
		// ==========================================================================
		// Collection <DecodificheModel> lCollOggettoMDS51bis = new
		// Vector(DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMAEspPressoDomMDS51bis());
		Collection<DecodificheModel> lCollOggettoMDS51bis = new Vector(
				DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMADetDomTermMDS51bis());
		Option lOggettoMDS51bis = new Option(lCollOggettoMDS51bis);
		setRequestAttribute("comboOggettiMDS", lOggettoMDS51bis.toString());

		setRequestAttribute("tipoCessazione", "DETENZIONE_DOMICILIARE_TERMINE");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_CESSAZIONE_51BIS;
	}

}