package siap.sige.fascicolo.action;

import java.util.ArrayList;
import java.util.Collection;

import siap.sico.decodifiche.controller.DecodificheManager;

/**
 * <p>
 * Title: ActLoadInserisciFascicoloPerSoggettoIgnoto
 * </p>
 * <p>
 * Description: Classe Action per la load Inserisci FascicoloSige anche non conoscendo i dati del Soggetto
 * (Ignoto)
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciFascicoloPerSoggettoIgnoto extends ActLoadInserisciFascicolo {

	public String processRequest() throws Exception {

		// Se c'è un Fascicolo SIEP sessione, va dismesso;
		if (!isSessionAttributeNullObj("fascicolo")) {
			setSessionAttribute("fascicolo", null);
		}

		// Si rimuovono eventuali dati preesistenti
		rimuoviFascicoloSigeEstesoDallaSessione();

		// Si rimuovono eventuali Soggetti presenti in sessione
		if (!isSessionAttributeNullObj("soggetto")) {
			setSessionAttribute("soggetto", null);
		}

		// Viene chiamata questa funzione con null perchè non c'è il Fascicolo SIEP
		preparaRequest(null);

		// Modalità Inserimento per Soggetto Ignoto
		setRequestAttribute("modalita", "ISI");

		return PG_LOAD_INSERISCIFASCICOLOSIGE;
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