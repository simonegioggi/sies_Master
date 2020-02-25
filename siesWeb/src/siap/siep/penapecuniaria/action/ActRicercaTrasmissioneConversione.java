package siap.siep.penapecuniaria.action;

import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.web.ActionSiap;

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
public class ActRicercaTrasmissioneConversione extends ActionSiap
		implements ICostantiMessaggio, ICostantiSicoJMS, ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("0002 ActRicercaTrasmConv - Ento ");

		this.setLinkRitorno();
		Vector lVect = null;

		// Recupero informazioni per i filtri di ricerca.
		Date dataRicercaInizio = (getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO,
				CAMPO_MESE_DATA_TRASMISSIONE_INIZIO, CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO));
		Date dataRicercaFine = (getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_FINE,
				CAMPO_MESE_DATA_TRASMISSIONE_FINE, CAMPO_GIORNO_DATA_TRASMISSIONE_FINE));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("0002 ActRicercaTrasmConv - Ricerca Data Inizio =>" + dataRicercaInizio + "<");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("0002 ActRicercaTrasmConv - Ricerca Data Fine =>" + dataRicercaFine + "<");

		// 05/03/2008 Se la Data inizio non viene valorizzata si imposta al mese precedente.
		if (dataRicercaInizio == null)
			dataRicercaInizio = DateUtils.getMonthBefore(DateUtils.getSysDate());
		// 05/03/2008 Se la data fine non viene valorizzata si imposta con quella odierna.
		if (dataRicercaFine == null)
			dataRicercaFine = DateUtils.getSysDate();
		/*
		 * String codTipoOperazione = ( getRequestStringParameter(CAMPO_COD_TIPO_OPERAZIONE) );
		 * 
		 * JmsCodeController lCrtl = new JmsCodeController(); Collection collTipoOperazione = (Collection)
		 * lCrtl.ExRicercaPerDominioEDescrizione("TIPO_OPERAZIONE", "TRASFERIMENTO" ); String
		 * descTipoOperazione = DecodificheUtils.getDescbyCode(collTipoOperazione, codTipoOperazione) ;
		 */
		// TRASMISSIONE ATTI PER CONVERSIONE - CodTipoOperazione = ?
		String codTipoOperazione = "";

		String AnnSiep = (getRequestStringParameter(CHIAVE_ANNO_SIEP));
		String NumSiep = (getRequestStringParameter(CHIAVE_PROGR_SIEP));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("0002 ActRicercaTrasmConv - Anno =>" + AnnSiep + "<");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("0002 ActRicercaTrasmConv - Numero =>" + NumSiep + "<");

		// -- UFFICIO SORVEGLIANZA.

		// String tipoUfficio = ( getRequestStringParameter(CAMPO_TIPO_UFFICIO) );
		// String codUfficio = null;
		// if(tipoUfficio.equals("1"))

		String codUfficio = this.getCodUfficioUtenteConnesso();
		// String codUfficioDest = ( getRequestStringParameter(CAMPO_COD_UFFICIO_DESTINATARIO) );
		String codUfficioDest = (getRequestStringParameter(ICostantiPenaPecuniaria.CAMPO_COD_SEDE_UDS));

		// -- UFFICIO RECUPERO CREDITI

		// String codUffRC = (
		// getRequestStringParameter(ICostantiPenaPecuniaria.CAMPO_COD_TIPO_AUTORITA_EMITTENTE) );
		// String codLuoRC = ( getRequestStringParameter(ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE) );

		// -- UTENTE --

		String tipoUtente = (getRequestStringParameter(CAMPO_TIPO_UTENTE));
		String codUtente = null;
		String descTipoUtente = "";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("0002 ActRicercaTrasmConv - Ute =>" + tipoUtente + "<");

		if (tipoUtente.equals("1")) {
			codUtente = this.getCodUtenteConnesso();
			descTipoUtente = "Utente Collegato";
		} else if (tipoUtente.equals("2")) {
			codUtente = (getRequestStringParameter(CAMPO_COD_UTENTE));
			descTipoUtente = "Utente con Codice";
		} else if (tipoUtente.equals("0")) {
			codUtente = null;
			descTipoUtente = "Tutti";
		}

		// -- ESITO --

		String tipoEsito = (getRequestStringParameter(CAMPO_TIPO_ESITO));
		String descTipoEsito = "";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("0002 ActRicercaTrasmConv - Esito =>" + tipoEsito + "<");

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

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("0002 ActRicercaTrasmConv - Sone dentro TRY");

			IMessaggio lCrtlMessaggio = JMSLookupRemote.getMessaggioRemote();
			lVect = lCrtlMessaggio.ExRicercaMessaggioEsitoConFiltri(codTipoOperazione, codUfficio, codUtente,
					tipoEsito, codUfficioDest, AnnSiep, NumSiep, dataRicercaInizio, dataRicercaFine);

			// ExRicercaMessaggioEsitoConFiltri(String aCodTipoOper, String aUfficio, String aUtente, String
			// aEsito, String aCodUffDest, String aAnnoSiep, String aProgrSiep, Date dataRicercaInizio, Date
			// dataRicercaFine)
			// ExRicercaMessaggioEsitoRicercaFascConFiltri(String aAnnoSiep, String aProgrSiep, String
			// aUfficio, String aUfficioDestinatario, String aUtente, String aEsito, String aTipoOperazione,
			// Date dataRicercaInizio, Date dataRicercaFine, int aPage)

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("0002 ActRicercaTrasmConv - Dopo Ricerca MessEsiFiltr =>" + lVect.size() + "<");

		} catch (F3BException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ActListaMessaggiTrasmessi F3BException");
		}

		this.setRequestAttribute("Messaggi", lVect);

		this.setRequestAttribute("Messaggi", lVect);

		setRequestAttribute("dataRicercaInizio", DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy"));
		setRequestAttribute("dataRicercaFine", DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy"));
		// setRequestAttribute("codTipoOperazione", codTipoOperazione );
		// setRequestAttribute("descTipoOperazione", descTipoOperazione );
		setRequestAttribute("tipoEsito", tipoEsito);
		setRequestAttribute("descTipoEsito", descTipoEsito);
		setRequestAttribute("descTipoUtente", descTipoUtente);
		setRequestAttribute("codUtente", codUtente);
		//
		setRequestAttribute("UfficioDest", codUfficioDest);
		setRequestAttribute("AnnoProcSiep", AnnSiep);
		setRequestAttribute("NumProcSiep", NumSiep);

		return PG_LISTA_MESSAGGI_TRASMESSI;
	}
}