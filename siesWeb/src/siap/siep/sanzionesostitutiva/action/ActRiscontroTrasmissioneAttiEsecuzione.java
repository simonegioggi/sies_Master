package siap.siep.sanzionesostitutiva.action;

import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.web.ActionSiap;
import siap.siep.jms.action.ICostantiSiepJMS;

/**
 * Classe action per il riscontro della trasmissione atti per esecuzione pena sostitutiva
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActRiscontroTrasmissioneAttiEsecuzione extends ActionSiap
		implements ICostantiSicoJMS, ICostantiSanzioneSostitutiva {

	// info per il log
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		setLinkRitorno();
		Vector v = null;

		// Recupero informazioni per i filtri di ricerca.
		Date dataRicercaInizio = (getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO,
				CAMPO_MESE_DATA_TRASMISSIONE_INIZIO, CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO));
		Date dataRicercaFine = (getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_FINE,
				CAMPO_MESE_DATA_TRASMISSIONE_FINE, CAMPO_GIORNO_DATA_TRASMISSIONE_FINE));

		// Se la Data inizio non viene valorizzata si imposta al mese precedente
		if (dataRicercaInizio == null)
			dataRicercaInizio = DateUtils.getMonthBefore(DateUtils.getSysDate());
		// Se la data fine non viene valorizzata si imposta con quella odierna
		if (dataRicercaFine == null)
			dataRicercaFine = DateUtils.getSysDate();

		// Si Verifica se l'ufficio destinatario sia stato correttamente impostato.
		String codUffDest = "-";
		String descUffDest = "";
		if (!(getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO).equalsIgnoreCase("-"))
				&& !(getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO).equalsIgnoreCase(""))) {
			codUffDest = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO),
					getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO));
			descUffDest = getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO) + " di "
					+ getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO);
		}

		String annoSiep = (getRequestStringParameter(CHIAVE_ANNO_SIEP));
		String progrSiep = (getRequestStringParameter(CHIAVE_PROGR_SIEP));

		String tipoEsito = (getRequestStringParameter(CAMPO_TIPO_ESITO));
		String descTipoEsito = "";
		String tipoUtente = (getRequestStringParameter(CAMPO_TIPO_UTENTE));
		String codUtente = null;
		String descTipoUtente = "Tutti";

		String codUfficio = getCodUfficioUtenteConnesso();
		if (tipoUtente.equals("1")) {
			codUtente = getCodUtenteConnesso();
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

		try {
			IMessaggio im = JMSLookupRemote.getMessaggioRemote();
			v = im.ExRicercaMessaggioEsitoConFiltri(TRASFERIMENTO_PENA_SOSTITUTIVA, codUfficio,
					codUtente, tipoEsito, codUffDest, annoSiep, progrSiep, dataRicercaInizio,
					dataRicercaFine);
		} catch (F3BException ex) {
			// info per il log
			siesLogger.error(ex.getMessage());
			ex.printStackTrace();
		}

		setRequestAttribute("Messaggi", v);

		setRequestAttribute("dataRicercaInizio", DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy"));
		setRequestAttribute("dataRicercaFine", DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy"));
		setRequestAttribute("codUfficioDestinatario", codUffDest);
		setRequestAttribute("descUfficioDestinatario", descUffDest.toUpperCase());
		setRequestAttribute("annoSiep", annoSiep);
		setRequestAttribute("progrSiep", progrSiep);
		setRequestAttribute("tipoEsito", tipoEsito);
		setRequestAttribute("descTipoEsito", descTipoEsito);
		setRequestAttribute("descTipoUtente", descTipoUtente);
		setRequestAttribute("codUtente", codUtente);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_RICERCA_TRASMISSIONE_ATTI_ESECUZIONE_PS;
	}

}