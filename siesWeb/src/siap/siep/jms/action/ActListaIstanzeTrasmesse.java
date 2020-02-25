package siap.siep.jms.action;

import java.util.Date;
import java.util.Vector;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;

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
@SuppressWarnings("rawtypes")
public class ActListaIstanzeTrasmesse extends ActionSiap implements ICostantiMessaggio, ICostantiSiepJMS,
		ICostantiSicoJMS {

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

		// Si Verifica se l'ufficio destinatario sia stato correttamente impostato.
		String lCodUffDest = "-";
		String lDescUffDest = "";
		if (!(getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO).equalsIgnoreCase("-"))
				&& !(getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO).equalsIgnoreCase(""))) {
			lCodUffDest = this.getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO),
					getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO));
			lDescUffDest = getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO) + " di "
					+ getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO);
		}

		String lAnnoSiep = (getRequestStringParameter(CHIAVE_ANNO_SIEP));
		String lProgrSiep = (getRequestStringParameter(CHIAVE_PROGR_SIEP));

//		JmsCodeController lCrtl = new JmsCodeController();

		String tipoEsito = (getRequestStringParameter(CAMPO_TIPO_ESITO));
		String descTipoEsito = "";
		String tipoUtente = (getRequestStringParameter(CAMPO_TIPO_UTENTE));
		String codUtente = null;
		String descTipoUtente = "Tutti";

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

		try {
			IMessaggio lCrtlMessaggio = JMSLookupRemote.getMessaggioRemote();
			lVect = lCrtlMessaggio.ExRicercaMessaggioEsitoConFiltri(ICostantiJMS.TRASFERIMENTO_ISTANZA,
					codUfficio, codUtente, tipoEsito, lCodUffDest, lAnnoSiep, lProgrSiep, dataRicercaInizio,
					dataRicercaFine);
		} catch (F3BException ex) {
		}

		this.setRequestAttribute("Messaggi", lVect);

		setRequestAttribute("dataRicercaInizio", DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy"));
		setRequestAttribute("dataRicercaFine", DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy"));
		setRequestAttribute("codUfficioDestinatario", lCodUffDest);
		setRequestAttribute("descUfficioDestinatario", lDescUffDest.toUpperCase());
		setRequestAttribute("annoSiep", lAnnoSiep);
		setRequestAttribute("progrSiep", lProgrSiep);
		setRequestAttribute("tipoEsito", tipoEsito);
		setRequestAttribute("descTipoEsito", descTipoEsito);
		setRequestAttribute("descTipoUtente", descTipoUtente);
		setRequestAttribute("codUtente", codUtente);

		return PG_LISTAISTANZETRASMESSE;
	}

}