package siap.sico.jms.action;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import siap.jms.JMSLookupRemote;
import siap.jms.jmscode.controller.JmsCodeController;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;

/**
 * ActListaMessaggiTrasmessi - Classe che carica la lista dei messaggi trasmessi
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActListaMessaggiTrasmessi extends ActionSiap implements ICostantiMessaggio, ICostantiSicoJMS {

	public String processRequest() throws Exception {

		this.setLinkRitorno();
		Vector lVect = null;

		// Recupero informazioni per i filtri di ricerca.
		Date dataRicercaInizio = (getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO,
				CAMPO_MESE_DATA_TRASMISSIONE_INIZIO, CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO));
		Date dataRicercaFine = (getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_FINE,
				CAMPO_MESE_DATA_TRASMISSIONE_FINE, CAMPO_GIORNO_DATA_TRASMISSIONE_FINE));

		// 05/03/2008 Se la Data inizio non viene valorizzata si imposta al mese precedente.
		if (dataRicercaInizio == null)
			dataRicercaInizio = DateUtils.getMonthBefore(DateUtils.getSysDate());
		// 05/03/2008 Se la data fine non viene valorizzata si imposta con quella odierna.
		if (dataRicercaFine == null)
			dataRicercaFine = DateUtils.getSysDate();

		String codTipoOperazione = (getRequestStringParameter(CAMPO_COD_TIPO_OPERAZIONE));

		JmsCodeController lCrtl = new JmsCodeController();
		Collection collTipoOperazione = lCrtl.ExRicercaPerDominioEDescrizione("TIPO_OPERAZIONE",
				"TRASFERIMENTO");
		String descTipoOperazione = DecodificheUtils.getDescbyCode(collTipoOperazione, codTipoOperazione);

		String tipoEsito = (getRequestStringParameter(CAMPO_TIPO_ESITO));
		String descTipoEsito = "";
		String tipoUtente = (getRequestStringParameter(CAMPO_TIPO_UTENTE));
		String codUtente = null;
		String descTipoUtente = "Tutti";
		// String tipoUfficio = ( getRequestStringParameter(CAMPO_TIPO_UFFICIO) );
		// String codUfficio = null;
		// if(tipoUfficio.equals("1"))
		String codUfficio = this.getCodUfficioUtenteConnesso();
		if (tipoUtente.equals("1")) {
			codUtente = this.getCodUtenteConnesso();
			descTipoUtente = "Utente Collegato";
		} else if (tipoUtente.equals("2")) {
			codUtente = (getRequestStringParameter(CAMPO_COD_UTENTE));
			descTipoUtente = "Utente con Codice";
		}
		if (tipoEsito.equals("0")) {
			tipoEsito = null;
			descTipoEsito = "Tutti";
		} else if (tipoEsito.equals("1")) {
			tipoEsito = "10000";
			descTipoEsito = "In attesa di risposta";
		} else if (tipoEsito.equals("2")) {
			tipoEsito = "00000";
			descTipoEsito = "Esito Positivo";
		}

		IMessaggio lCrtlMessaggio = JMSLookupRemote.getMessaggioRemote();
		lVect = lCrtlMessaggio.ExRicercaMessaggioEsitoConFiltri(codUfficio, codUtente, tipoEsito,
				codTipoOperazione, dataRicercaInizio, dataRicercaFine);

		// TODO: Ticket#202501220131: modificato il caricamento della combo degli uffici
		// allora anche la pagina di dettaglio dovrà avere la stessa dicitura? decommentare nel caso
		// Iterator i = lVect.iterator();
		// while (i.hasNext()) {
		// MessaggioModel mm = (MessaggioModel) i.next();
		// IUfficio iu = SICOLookupRemote.getUfficioRemote();
		// UfficioModel um = iu.ExRicercaUfficioByCod(mm.getCodUfficioDestinatario());
		// if ("UDSM".equalsIgnoreCase(um.getCodTipoUfficio())) {
		// mm.setDescrUfficioDestinatario("Magistrato di Sorveglianza per i minorenni");
		// break;
		// }
		// }

		this.setRequestAttribute("Messaggi", lVect);

		setRequestAttribute("dataRicercaInizio", DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy"));
		setRequestAttribute("dataRicercaFine", DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy"));
		setRequestAttribute("codTipoOperazione", codTipoOperazione);
		setRequestAttribute("descTipoOperazione", descTipoOperazione);
		setRequestAttribute("tipoEsito", tipoEsito);
		setRequestAttribute("descTipoEsito", descTipoEsito);
		setRequestAttribute("descTipoUtente", descTipoUtente);
		setRequestAttribute("codUtente", codUtente);

		return PG_LISTA_MESSAGGI_TRASMESSI;
	}

}