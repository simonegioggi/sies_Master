package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadRicercaTrasmissioniSolleciti
 * </p>
 * <p>
 * Description: Classe Action utilizzata UNICAMENTE per invocare la ricerca direttamente dalla griglia delle
 * Istruttorie/Richieste
 * </p>
 */
public class ActLoadRicercaTrasmissioniSolleciti extends ActionSiap implements ICostantiRichiesta {

	/**
	 * Effettua la ricerca di tutti i Messaggi di risposta di presa in carico.
	 */
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// I listener non servono in questa fase
		/*
		 * if ( JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE")!=null &&
		 * JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")
		 * ) { SIAPReceiver.getInstance().testInArrivo(); SIAPReceiver.getInstance().testInPartenza();
		 * SIAPReceiver.getInstance().testStampa(); } else{ SIAPReceiver.getInstance(); }
		 */

		// ==========================================================================
		// NEW Ricerca Paginata

		this.setLinkRitorno();
		setRequestAttribute(IWebConstants.LINK_RITORNO, "10");
		String lRitorno = getQueryRequestUrl();
		setRequestAttribute("linkRitorno", lRitorno);

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		Vector<String> lListaTipoMessaggio = new Vector<>();
		lListaTipoMessaggio.add(ICostantiJMS.ESITO);
		lListaTipoMessaggio.add(ICostantiJMS.RICHIESTA);

		Vector<String> lListaTipoOperazione = new Vector<>();
		lListaTipoOperazione.add(ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA);
		lListaTipoOperazione.add(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI);
		lListaTipoOperazione.add(ICostantiJMS.ESITO_SEGUITO_ATTI);

		IModuloCumulo lCtrl = SIEPLookupRemote.getModuloCumuloRemote();
		Vector<MessaggioModel> lVect = lCtrl.ExRicercaMessaggi(null // ICostantiJMS.DELIVERY_MODE_INVIATO
				, lListaTipoMessaggio, lListaTipoOperazione, null // lCodEsito
				, "N" // Flag_visto.
				, null // aChiaveAnnoSiep
				, null // aChiaveProgrSiep
				, null // aChiaveUfficioSiep
				, null // aCodUfficioMitt
				, getCodUfficioUtenteConnesso() // ufficio dest
				, null // lDataTrasmissioneDal
				, null // lDataTrasmissioneAl
				, null // lCognome
				, null // lNome
				, null // aChiaveAnnoFasCumulante
				, null // aChiaveProgrFasCumulante
				, null // aChiaveUfficioFasCumulante
				, Integer.parseInt(lPagina));

		// Recupero il numero totale di record
		BigDecimal lCountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			siesLogger.info(">>>>>>>>   Conto i Risultati = ");
			lCountRisultati = lCtrl.ExCountRicercaMessaggi(null, lListaTipoMessaggio, lListaTipoOperazione,
					null, "N" // Flag_visto. Non recupero quelli ancora da elaborare
					, null // aChiaveAnnoSiep
					, null // aChiaveProgrSiep
					, null // aChiaveUfficioSiep
					, null // aCodUfficioMitt
					, getCodUfficioUtenteConnesso() // ufficio dest
					, null // lDataTrasmissioneDal
					, null // lDataTrasmissioneAl
					, null // lCognome
					, null // lNome
					, null // aChiaveAnnoFasCumulante
					, null // aChiaveProgrFasCumulante
					, null // aChiaveUfficioFasCumulante
			);
		} else {
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		// Passo i dati per gestire la paginazione
		setRequestAttribute("CountRisultati", lCountRisultati);
		siesLogger.info(">>>>>>>>   lCountRisultati = " + lCountRisultati);

		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// END ricerca paginata
		// ==========================================================================

		/*
		 * OLD
		 *
		 * this.setLinkRitorno();
		 *
		 * MessaggioModel lMessaggio = new MessaggioModel(); lMessaggio.setCodTipoMessaggio
		 * (ICostantiJMS.ESITO); lMessaggio.setCodTipoOperazione
		 * (ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA); lMessaggio.setCodUfficioDestinatario
		 * (getCodUfficioUtenteConnesso());
		 *
		 * // Ricerca Messaggi IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote(); Vector lVect =
		 * lCrtl.ExRicercaMessaggio(lMessaggio);
		 */

		// ============================================================
		// Cerco Solleciti
		MessaggioModel lMess = null;
		Vector<MessaggioModel> lVecSoll = null;
		MessaggioModel lMessSoll = null;

		for (int k = 0; k < lVect.size(); k++) {
			lMess = lVect.get(k);
			if (lMess != null && lMess.getIdMessaggio() != null) {
				if ("00067".equals(lMess.getCodTipoOperazione())) {
					IMessaggio CtrlMess = JMSLookupRemote.getMessaggioRemote();
					lVecSoll = new Vector<>(CtrlMess.ExRicercaMessaggioByIdMessaggioSollecitato("00068",
							"" + lMess.getIdMessaggio()));

					// La Ricerca Solleciti è Ordinata in modo decrescente; Il Primo elemento è Il Sollecito
					// con MAX Data_Invio
					if (lVecSoll != null && lVecSoll.size() > 0) {
						lMessSoll = lVecSoll.get(0);
						lMess.setDataUltimoSollecito(lMessSoll.getDataInvio());
					}
				}
			}
		}

		setRequestAttribute("Messaggi", lVect);
		return ICostantiRichiesta.PG_LOAD_RISCONTRO_SOLLECITI;

	}

	/**
	 *
	 * @param aVect
	 * @param aDataIniziale
	 * @param aDataFinale
	 * @return
	 */
	// private Vector filtroPerData(Vector aVect, Date aDataIniziale, Date aDataFinale) {
	// Vector lRectVect = new Vector();
	// MessaggioModel lMessCorrente;
	// Date lDataCorr = null;
	//
	// Iterator itx = aVect.iterator();
	// while (itx.hasNext()) {
	// lMessCorrente = (MessaggioModel) itx.next();
	// lDataCorr = lMessCorrente.getDataInvio();
	// if (!aDataIniziale.after(lDataCorr) && !aDataFinale.before(lDataCorr))
	// lRectVect.add(lMessCorrente);
	// }
	// return lRectVect;
	// }

	/**
	 * Recupera solo i parametri di ricerca da passare alla JSP per la costruzione del link "torna indietro"
	 *
	 * @return
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	private String getQueryRequestUrl() throws F3BException {

		String lRequest = ""; // this.getRequest().getRequestURL() + "?";
		Set lKeys = getRequest().getParameterMap().keySet();

		Iterator itx = lKeys.iterator();
		while (itx.hasNext()) {
			String key = (String) itx.next();
			// Copia di tutti gli attributi tranne LINK_RITORNO e FLAG_RITORNO
			if (!key.equals(IWebConstants.LINK_RITORNO) && !key.equals(IWebConstants.FLAG_RITORNO)
					&& !key.equals(IWebConstants.ACTION_FIELD)) {
				lRequest += key + "=" + getRequestStringParameter(key) + "&";
			}
		}

		if (lRequest.length() > 0) {
			return lRequest.substring(0, lRequest.length() - 1);
		} else {
			return lRequest;
		}
	}

}