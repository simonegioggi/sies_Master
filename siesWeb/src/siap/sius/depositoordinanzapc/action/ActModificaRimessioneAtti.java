package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Action che realizza la modifica del Provvedimento SIUS: Ordinanza di Rimessione Atti. I dati coinvolti
 * nell'operazione di modifica sono: la data di emissione le motivazioni la corte a cui si rimettono gli atti
 * i destinatari di notifiche e comunicazioni
 * 
 * @author Mmartiello
 *
 */
@SuppressWarnings("rawtypes")
public class ActModificaRimessioneAtti extends ActInserisciRimessioneAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// Inizializzazione data odierna
	private Date mOggi = DateUtils.getSysDate();

	// Data emissione
	private Date mDataEmissione;

	public String processRequest() throws Exception {

		String lRetPage = IWebConstants.PG_MESSAGE;
		BigDecimal lIdOrdinanza = null;
		BigDecimal lIdDecreto = null;

		// Lettura data di emissione
		mDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		// Lettura ID Ordinanza o Decreto
		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC)
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC)
						.trim().length() > 0)
			lIdOrdinanza = getRequestBigDecimalParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC);
		else
			throw new F3BException(F3BException.USER_MESSAGE, "ID Ordinanza non valorizzato nella form !");

		// Nessun aggiornamento e Tenori ed Esiti.
		// Nessun aggiornamento Evento
		// Lettura ID Evento
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoModel lEvento = null;
		TenoreModel[] lTenori = null;
		DepositoOrdinanzaPcModel DepOrdPcMod = null;
		DepositoOrdinanzaPcModel DepOrdPcModNew = null;

		lEvento = generaEvento();
		lTenori = generaTenori();

		// Aggiornamento dei dati
		IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepOrdPcMod = lCtrl.ExRicercaOrdinanzaRimessioneAttiPcByKeyPerUpdate(lIdOrdinanza);

		if (DepOrdPcMod == null)
			throw new F3BException(F3BException.USER_MESSAGE, "ID Ordinanza non valido !");

		DepOrdPcModNew = generaOrdinanzaAggiornamento(DepOrdPcMod);

		lCtrl.ExAggiornaTenoriEvento(lTenori, lEvento, lIdOrdinanza, lIdDecreto);
		lCtrl.ExModificaDepositoOrdinanzaPc(DepOrdPcModNew);

		// Cancellazione delle vecchie notifiche
		Vector listaVecchieNotifiche = null;
		ArrayList listaNuoveNotifiche = null;

		// Chiamata al Controller NOTIFICA
		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		try {
			listaVecchieNotifiche = lCtrlNot.ExRicercaNotificaByKeyEvento(lIdEvento);
		} catch (F3BException f3bex) {
			if (f3bex.getMessage().contains("Nessun Elemento trovato")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ActModificaRimessioneAtti: Trappato Errore di assenza Notifiche ");
			} else
				throw f3bex;
		}

		if (listaVecchieNotifiche != null) {
			Iterator itxNot = listaVecchieNotifiche.iterator();
			while (itxNot.hasNext())
				lCtrlNot.ExCancellaNotifica((NotificaModel) itxNot.next());
		}
		// Generazione delle nuove notifiche

		listaNuoveNotifiche = generaNotifiche(mDataEmissione, lIdEvento);

		// Inserimento delle nuove notifiche

		listaNuoveNotifiche = lCtrlNot.ExInserisciNotifiche((ArrayList) listaNuoveNotifiche);

		// Redirezione alla pagina di dettaglio Ordinanza
		if (lIdOrdinanza != null)
			lRetPage = dettaglioOrdinanza(lIdEvento);
		else
			throw new F3BException(F3BException.USER_MESSAGE, "ID Ordinanza non valorizzato nella form !");

		return lRetPage;
	}

	/**
	 * Prepara il Model per l'evento da aggiornare.
	 * <p>
	 * 
	 * @param aDataEmissione
	 *            Date data di emissione.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return EventoModel ritorna l'evento model.
	 */
	private EventoModel generaEvento() throws F3BException {

		// Prepara Model Evento.
		EventoModel lEvento = new EventoModel();
		lEvento.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		lEvento.setDataEmissione(mDataEmissione);
		lEvento.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lEvento.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lEvento.setDataAggiornamento(mOggi);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Evento = " + lEvento);
		return lEvento;
	}

	/**
	 * Aggiorna ognuno dei tenori collegati al provvedimento con la nuova data di emissione
	 * 
	 * @return
	 * @throws F3BException
	 */
	private TenoreModel[] generaTenori() throws F3BException {

		TenoreModel[] lTenori = null;

		// Lettura ID ed Esito per i Tenori.
		String[] lIdTenori = getRequestStringParameters(ICostantiTenore.CAMPO_ID_TENORE);

		int lSizeArray = lIdTenori.length;
		if (lSizeArray > 0) {
			lTenori = new TenoreModel[lSizeArray];
			for (int i = 0; i < lSizeArray; i++) {
				TenoreModel lTenModel = new TenoreModel();

				lTenModel.setIdTenore(new BigDecimal(lIdTenori[i]));
				lTenModel.setCodEsitoTenore("0605");
				lTenModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																						// dell'operatore che
																						// aggiorna
				lTenModel.setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice dell'operatore che
																				// aggiorna
				lTenModel.setDataAggiornamento(mOggi);
				lTenModel.setData(mDataEmissione);

				// Inserimenti i-esimo Tenore
				lTenori[i] = lTenModel;

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Tenore n." + i + " = " + lTenori[i]);

			}
		}
		return lTenori;
	}

	// Funzione di lettura dei dati dell'Ordinanza
	private DepositoOrdinanzaPcModel generaOrdinanzaAggiornamento(DepositoOrdinanzaPcModel aModel)
			throws F3BException {

		///////////////////////////////////////////////////
		// Aggiornamento dei dati in DepositoOrdinanza. //
		/////////////////////////////////////////////////
		String filtroRimessione = "";
		String filtroRimessioneDescr = "";

		aModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		aModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		aModel.setDataAggiornamento(mOggi);

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_NATURA_PROVVEDIMENTO))
			aModel.setCodNaturaProvvedimento(
					getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_NATURA_PROVVEDIMENTO));

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.FILTRO_RIMESSIONE)) {
			filtroRimessione = getRequestStringParameter(ICostantiDepositoOrdinanzaPc.FILTRO_RIMESSIONE);
			if (filtroRimessione.compareToIgnoreCase("cortecostituzionale") == 0)
				filtroRimessioneDescr = "alla Corte Costituzionale per giudizio di legittimità costituzionale";
			if (filtroRimessione.compareToIgnoreCase("corteeuropea") == 0)
				filtroRimessioneDescr = "alla Corte Giustizia Europea per giudizio di legittimità in materia di interpretazione trattati internazionali";

			if ((filtroRimessione.compareToIgnoreCase("altro") == 0)
					&& !isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ALTRO_RIMESSIONI)) {
				filtroRimessioneDescr = "a "
						+ getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ALTRO_RIMESSIONI);
			}
		}

		aModel.setOggettoProcedimento(filtroRimessioneDescr);

		return aModel;
	}

	/**
	 * Redirige l'uscita al Dettaglio dell'Ordinanza.
	 * 
	 * @param aIdEvento
	 * @return
	 */
	private String dettaglioOrdinanza(BigDecimal aIdEvento) {

		String lRetPage = null;
		// dettaglio dell'ordinanza
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, aIdEvento.toString());
		lRetPage = lRedirectTo.toString();

		return lRetPage;
	}

}