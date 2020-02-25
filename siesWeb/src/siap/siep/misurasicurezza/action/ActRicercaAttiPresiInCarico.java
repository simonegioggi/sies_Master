package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import siap.jms.ICostantiJMS;
import siap.jms.jmscode.controller.JmsCodeController;
import siap.jms.jmscode.model.JmsCodeModel;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.presaincarico.action.ICostantiPresaincarico;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Action che effettua la ricerca dei messaggio ricevuti per competenza relativi alle misure di sicurezza ed
 * eaborati (FLAG_VISTO = 'S')
 * 
 * @author d.fiorletta
 *
 */
@SuppressWarnings("rawtypes")
public class ActRicercaAttiPresiInCarico extends ActionSiap implements ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {

		this.setLinkRitorno();
		setRequestAttribute(IWebConstants.LINK_RITORNO, "10");
		String lRitorno = getQueryRequestUrl();
		setRequestAttribute("linkRitorno", lRitorno);

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// ==========================================================================
		// Recupero i Criteri di ricerca impostati in maschera
		// ==========================================================================
		// Ufficio Mittente
		String lTipoUffMitt = null;
		if (!isRequestParameterNullObj(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO)
				&& !"-".equals(getRequestStringParameter(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO))) {
			lTipoUffMitt = getRequestStringParameter(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO);
		}

		String lDescComuneUffMitt = null;
		if (!isRequestParameterNullObj(ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO))
			lDescComuneUffMitt = getRequestStringParameter(ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO);

		String lCodiceUffMitt = null;
		if (lTipoUffMitt != null && lDescComuneUffMitt != null)
			lCodiceUffMitt = getCodUfficioByCodTipoUfficioDescrComune(lTipoUffMitt, lDescComuneUffMitt);

		// Anno e Numero
		BigDecimal lChiaveAnno = null;
		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO)
				&& getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO).length() > 0) {
			lChiaveAnno = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO);
		}

		BigDecimal lChiaveProgr = null;
		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR)
				&& getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR).length() > 0) {
			lChiaveProgr = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR);
		}

		// Esito
		String lCodEsito = null;
		if (!isRequestParameterNullObj(ICostantiSicoJMS.CAMPO_TIPO_ESITO))
			lCodEsito = getRequestStringParameter(ICostantiSicoJMS.CAMPO_TIPO_ESITO);

		// N.B. Il filtro viene messo su MESSAGGIO.DATA_INVIO del messaggio di
		// RICHIESTA. Tale data viene in realtà valorizzata con la data
		// di sistema al momento dello scodamento del messaggio per cui è la
		// data di ricezione e non la data di trasmissione. Le due date tuttavia
		// in genere coincidono a meno di errori in fase di trasmissione.
		// Nel caso di stessa BDI in inoltre il messagio di RICHIESTA non viene
		// mai salvato in fase di scodamento in quanto già presente a sistema.
		// Solo in questo caso DATA_INVIO è proprio la data di trasmissione.
		Date lDataTrasmissioneDal = null;
		lDataTrasmissioneDal = (getRequestDateParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO,
				ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO,
				ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO));
		Date lDataTrasmissioneAl = null;
		lDataTrasmissioneAl = (getRequestDateParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE,
				ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE,
				ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE));

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("lDataTrasmissioneDal = "+lDataTrasmissioneDal);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("lDataTrasmissioneAl  = "+lDataTrasmissioneAl);

		// Se la Data inizio non viene valorizzata si imposta al mese precedente.
		// tolto: la ricerca è paginata per cui non ci sono problemi ad effettuare
		// una ricerca senza filtri
		// if (lDataTrasmissioneDal == null)
		// lDataTrasmissioneDal = DateUtils.moveDateTo(DateUtils.getSysDate(),Calendar.MONTH, -2);
		// // Se la Data fine non viene valorizzata si imposta con quella odierna.
		// if (lDataTrasmissioneAl == null)
		// lDataTrasmissioneAl = DateUtils.getSysDate();

		// ==========================================================================
		// Effettuo la Ricerca
		// ==========================================================================
		if ("TUTTI".equals(lCodEsito))
			lCodEsito = null;

		Vector<String> lListaEsiti = null;
		if (lCodEsito != null) {
			lListaEsiti = new Vector<String>();
			lListaEsiti.add(lCodEsito);

			// anche i messaggi in stato 'Trasmesso x competenza' vanno considerati
			// 'in attesa di risposta'
			if ("-".equals(lCodEsito))
				lListaEsiti.add(ICostantiJMS.TRASFERITO);
		}

		Vector<String> lListaTipoOperazione = new Vector<String>();
		lListaTipoOperazione.add(ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS);
		lListaTipoOperazione.add(ICostantiJMS.TRASFERIMENTO_ESECUZIONE_MS);

		// Ricerca Messaggi
		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		Vector<MessaggioModel> lVect = lCtrl.ExRicercaMessaggi(ICostantiJMS.DELIVERY_MODE_RICEVUTO,
				ICostantiJMS.RICHIESTA, lListaTipoOperazione
				// , lCodEsito
				, lListaEsiti, "S" // Flag_visto. Non recupero quelli ancora da elaborare
				, lChiaveAnno, lChiaveProgr, lCodiceUffMitt // aChiaveUfficioSiep: la ricerca viene fatta per
															// Ufficio titolare degli atti trasmessi
				, null // aCodUfficioMitt
				, getCodUfficioUtenteConnesso() // ufficio dest
				, lDataTrasmissioneDal, lDataTrasmissioneAl, Integer.parseInt(lPagina));

		setRequestAttribute("Messaggi", lVect);

		// Recupero il numero totale di record
		BigDecimal lCountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			lCountRisultati = lCtrl.ExCountRicercaMessaggi(ICostantiJMS.DELIVERY_MODE_RICEVUTO,
					ICostantiJMS.RICHIESTA, lListaTipoOperazione
					// , lCodEsito
					, lListaEsiti, "S" // Flag_visto. Non recupero quelli ancora da elaborare
					, lChiaveAnno, lChiaveProgr, lCodiceUffMitt // aChiaveUfficioSiep: la ricerca viene fatta
																// per Ufficio titolare degli atti trasmessi
					, null // aCodUfficioMitt
					, getCodUfficioUtenteConnesso() // ufficio dest
					, lDataTrasmissioneDal, lDataTrasmissioneAl);
		} else {
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		// =====================================================
		// Ripasso alla maschera i criteri di ricerca
		if (lCodiceUffMitt != null) {
			UfficioModel lUfficioMittente = getUfficioByCodUfficio(lCodiceUffMitt);
			setRequestAttribute("aUfficioMittente", lUfficioMittente);
		}

		String lDescEsito = null;
		if (lCodEsito != null) {
			JmsCodeController lCtrlJms = new JmsCodeController();
			JmsCodeModel lJmsModel = lCtrlJms.ExRicercaJmsCodeByKey("CODICE_ESITO", lCodEsito);
			lDescEsito = lJmsModel.getDescrizione();
		}

		setRequestAttribute("aEsito", lDescEsito);

		if (lDataTrasmissioneDal != null)
			setRequestAttribute("aDataTrasmissioneDal",
					DateUtils.getDateToString(lDataTrasmissioneDal, "dd/MM/yyyy"));
		if (lDataTrasmissioneAl != null)
			setRequestAttribute("aDataTrasmissioneAl",
					DateUtils.getDateToString(lDataTrasmissioneAl, "dd/MM/yyyy"));
		//
		// ==========================================

		// Passo i dati per gestire la paginazione
		setRequestAttribute("CountRisultati", lCountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		return PG_LISTA_ATTI_PRESI_IN_CARICO;
	}

	/**
	 * Recupera solo i parametri di ricerca da passare alla JSP per la costruzione del link "torna indietro"
	 * 
	 * @return
	 * @throws F3BException
	 */
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

		return lRequest.substring(0, lRequest.length() - 1);

	}

}