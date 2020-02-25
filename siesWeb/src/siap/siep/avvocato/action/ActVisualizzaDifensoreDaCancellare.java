package siap.siep.avvocato.action;

/**
* <p>Title: ActVisualizzaDifensoreDaCancellare</p>
* <p>Description: Classe Action per l'inserimento di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

@SuppressWarnings("rawtypes")
public class ActVisualizzaDifensoreDaCancellare extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *         <p>
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector avvocati = new Vector();

		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
			AvvocatoModel lmModelAppo = new AvvocatoModel();
			lmModelAppo
					.setIdAvvocato(this.getRequestBigDecimalParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO));
			avvocati = lCtrl.ExRicercaAvvocatoPerInserimento(lmModelAppo);
			setRequestAttribute("avvocati", avvocati);

			String nome = null;
			String cognome = null;

			String foro = null;
			if (!this.isRequestParameterNullObj("foro")) {
				foro = this.getRequestStringParameter("foro");

				setRequestAttribute("foro", foro);
			}

			if (!this.isRequestParameterNullObj("nome")) {
				nome = this.getRequestStringParameter("nome");
				setRequestAttribute("nome", nome);
			}
			if (!this.isRequestParameterNullObj("cognome")) {
				cognome = this.getRequestStringParameter("cognome");
				setRequestAttribute("cognome", cognome);
			}
		} else {
			AvvocatoModel lAvvModRic = new AvvocatoModel();
			if (!this.isRequestParameterNullObj(CAMPO_COGNOME))
				lAvvModRic.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
			if (!this.isRequestParameterNullObj(CAMPO_NOME))
				lAvvModRic.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
			if (!this.isRequestParameterNullObj(CAMPO_FORO))
				lAvvModRic.setForo(getRequestStringParameter(ICostantiAvvocato.CAMPO_FORO).toUpperCase());
			lAvvModRic.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());
			if (!this.isRequestParameterNullObj(CAMPO_ANNO_DATA_NASCITA)
					&& (!this.isRequestParameterNullObj(CAMPO_MESE_DATA_NASCITA)
							&& (!this.isRequestParameterNullObj(CAMPO_GIORNO_DATA_NASCITA))))
				lAvvModRic.setDataNascita(getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA,
						ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA,
						ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA));

			avvocati = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvModRic);
			if (avvocati.size() > 1) {
				if ((this.getRequestStringParameter(CAMPO_ANNO_DATA_NASCITA).equals("")
						&& this.getRequestStringParameter(CAMPO_MESE_DATA_NASCITA).equals("")
						&& this.getRequestStringParameter(CAMPO_GIORNO_DATA_NASCITA).equals(""))
						&& (this.getRequestStringParameter(CAMPO_FORO).equals(""))) {
					throw new F3BException(F3BException.USER_MESSAGE,
							"Dettagliare meglio con data di nascita o Foro la ricerca");

				} else {

					throw new F3BException(F3BException.USER_MESSAGE,
							"Attenzione: esistono più avvocati con gli stessi dati. Richiamare la funzione di ricerca per cancellare");
				}
			}

			this.setRequestAttribute("avvocati", avvocati);

		}
		AvvocatoModel lAvv = new AvvocatoModel();
		lAvv = (AvvocatoModel) avvocati.get(0);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" apparten = " + lAvv.getCodUffAppartenenza());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" utente   = " + this.getCodUfficioUtenteConnesso());
		if (!lAvv.getCodUffAppartenenza().equals(this.getCodUfficioUtenteConnesso())) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		}
		Option lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(),
				lAvv.getCodNonAttivita());
		setRequestAttribute("autoritaAvvocato", "" + lOption);
		this.setRequestAttribute("modalita", "M");

		return PG_DIFENSORE_DA_CANCELLARE;
	}

}