package siap.siep.penapecuniaria.action;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import siap.jms.JMSLookupRemote;
import siap.jms.jmscode.controller.JmsCodeController;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActListaTrasmissioneConversione extends ActionSiap implements ICostantiMessaggio,
		ICostantiSicoJMS {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setLinkRitorno();
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

		String codTipoOperazione = "00069";

		JmsCodeController lCrtl = new JmsCodeController();
		Collection collTipoOperazione = (Collection) lCrtl.ExRicercaPerDominioEDescrizione("TIPO_OPERAZIONE",
				"CONVERSIONE");
		String descTipoOperazione = DecodificheUtils.getDescbyCode(collTipoOperazione, codTipoOperazione);

		String tipoEsito = (getRequestStringParameter(CAMPO_TIPO_ESITO));
		String descTipoEsito = "";
		String tipoUtente = (getRequestStringParameter(CAMPO_TIPO_UTENTE));
		String codUtente = null;
		String descTipoUtente = "Tutti";
		// String tipoUfficio = ( getRequestStringParameter(CAMPO_TIPO_UFFICIO) );
		// String codUfficio = null;
		// if(tipoUfficio.equals("1"))
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
			IMessaggio lCrtlMessaggio = JMSLookupRemote.getMessaggioRemote();
			lVect = lCrtlMessaggio.ExRicercaMessaggioEsitoConFiltri(codUfficio, codUtente, tipoEsito,
					codTipoOperazione, dataRicercaInizio, dataRicercaFine);
		} catch (F3BException ex) {
		}

		setRequestAttribute("Messaggi", lVect);

		setRequestAttribute("Messaggi", lVect);

		setRequestAttribute("dataRicercaInizio", DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy"));
		setRequestAttribute("dataRicercaFine", DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy"));
		setRequestAttribute("codTipoOperazione", codTipoOperazione);
		setRequestAttribute("descTipoOperazione", descTipoOperazione);
		setRequestAttribute("tipoEsito", tipoEsito);
		setRequestAttribute("descTipoEsito", descTipoEsito);
		setRequestAttribute("descTipoUtente", descTipoUtente);
		setRequestAttribute("codUtente", codUtente);

		String PG_LISTA_TRASMISSIONE_COV = IWebConstants.ROOT_DIR
				+ "files/siap/siep/penapecuniaria/ListaTrasmissioneConversione.jsp";
		return PG_LISTA_TRASMISSIONE_COV;
	}

}