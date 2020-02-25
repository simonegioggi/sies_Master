package siap.sige.fascicolo.action;

import java.util.ArrayList;
import java.util.Collection;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.model.SoggettoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadInserisciFascicoloDaSoggetto
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di FascicoloSige da dettaglio Soggetto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciFascicoloDaSoggettoManuale extends ActLoadInserisciFascicolo {

	public String processRequest() throws Exception {

		// Se c'è un Fascicolo SIEP sessione, va dismesso; mentre il soggetto deve esserci !!!
		if (!isSessionAttributeNullObj("fascicolo"))
			setSessionAttribute("fascicolo", null);

		SoggettoModel soggetto = null;
		// Deve esserci il soggetto in sessione
		if (!isSessionAttributeNullObj("soggetto")) {
			// ID Soggetto.
			soggetto = (SoggettoModel) getSessionAttribute("soggetto");
			setRequestAttribute("IDSoggetto", soggetto.getIdSoggetto().toString());
		} else
			throw new F3BException(F3BException.USER_MESSAGE, "Soggetto non presente in sessione ");

		// Si rimuovono eventuali dati preesistenti
		rimuoviFascicoloSigeEstesoDallaSessione();

		// Viene chiamata questa funzione con null perchè non c'è il Fascicolo SIEP
		preparaRequest(null);

		// Modalità Inserimento da Soggetto
		setRequestAttribute("modalita", "IS");

		return PG_LOAD_INSERISCIFASCICOLOSIGEMANUALE;
	}

	/**
	 * Funzione per la preparazione dell'elenco delle Posizioni Giuridiche che popolerà la ComboList. Questa
	 * funzione presente nella classe padre, viene riscritta perchè nell'Iscrizione da Soggetto si fornisce la
	 * lista di posizioni giuridiche relative all'Esecuzione.
	 */
	protected ArrayList getListaPosGiuridiche() {
		// Preparazione della Lista di tipi di Posizione Giuridica
		Collection lCollPosGiuEsecuzione = DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione();

		ArrayList lPosizioneGiuridica = new ArrayList(lCollPosGiuEsecuzione);

		return lPosizioneGiuridica;
	}

}