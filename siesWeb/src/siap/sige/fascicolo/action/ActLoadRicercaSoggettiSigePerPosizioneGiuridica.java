package siap.sige.fascicolo.action;

import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistrato.util.MagistratoUtils;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.web.ActionSige;

/**
 * MEV_65: aggiunta classe Action per la visualizzazione della maschera di Ricerca Soggetti SIGE per posizione
 * giuridica.
 *
 * @author Gioggi
 * @version 1.0
 */
public class ActLoadRicercaSoggettiSigePerPosizioneGiuridica extends ActionSige
		implements ICostantiFascicoloSige {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Elenco magistrati
		Option magistrati = new Option(
				MagistratoUtils.getElencoMagistratiPerRicercaSige(getCodUfficioUtenteConnesso()), "-");
		setRequestAttribute("elencoMagistrati", "" + magistrati);

		// Elenco delle Sezioni previste per l'Ufficio
		Option sezioni = new Option(SezioneUtils.getElencoSezioniPerRicerca(getCodUfficioUtenteConnesso()));
		sezioni.setAddBlankItem(true);
		sezioni.setValueBlankItem("-");
		setRequestAttribute("elencoSezioni", "" + sezioni);

		// Elenco delle nazioni
		// Option nazioni = new Option(DecodificheManager.getInstance().getNazioni());
		Vector v = new Vector(DecodificheManager.getInstance().getNazioni());
		// tolgo ITALIA poichè trattasi di stranieri
		v.remove(new DecodificheModel("039", "-", "NAZIONE", "-", "-", "-", "-", "-", "-"));
		Option nazioni = new Option(v);
		setRequestAttribute("elencoNazioni", "" + nazioni);

		// recupero oggetti in sessione
		if (!isSessionAttributeNullObj("model"))
			removeSessionAttribute("model");

		// restituisce la jsp di VIEW
		return PG_LOAD_RICERCASOGGETTISIGE_PERPOSIZIONEGIURIDICA;
	}

}