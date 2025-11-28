package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.ufficio.action.ICostantiUfficio;
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
	    //===============================================================
	    // MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni
	    // Si aggiungono i criteri di ricerca. 
	    Date lDataTrasmissioneDal = null;
	    Date lDataTrasmissioneAl = null;
	    
        if (!isRequestParameterNullObj(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO)) {
            lDataTrasmissioneDal = getRequestDateParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO,
                    ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO,
                    ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO);
            lDataTrasmissioneAl = getRequestDateParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE,
                    ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE,
                    ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE);          
        }
        else {
            if (lDataTrasmissioneDal == null)
                lDataTrasmissioneDal = DateUtils.getEnneMonthBefore(DateUtils.getSysDate(), 2);

            // Se la data fine non viene valorizzata si imposta con quella odierna.
            if (lDataTrasmissioneAl == null)
                lDataTrasmissioneAl = DateUtils.getSysDate();
            
        }
        
        setRequestAttribute(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO, DateUtils.getDateToString(lDataTrasmissioneDal, "dd"));
        setRequestAttribute(ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO, DateUtils.getDateToString(lDataTrasmissioneDal, "MM"));
        setRequestAttribute(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO, DateUtils.getDateToString(lDataTrasmissioneDal, "yyyy"));
        
        setRequestAttribute(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE, DateUtils.getDateToString(lDataTrasmissioneAl, "dd"));
        setRequestAttribute(ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE, DateUtils.getDateToString(lDataTrasmissioneAl, "MM"));
        setRequestAttribute(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE, DateUtils.getDateToString(lDataTrasmissioneAl, "yyyy"));
        
        // Filtro per - Ufficio Mittente - Ufficio che ha inviato l'esito
        String lCodTipoUfficio = null;
        String lDescrComuneUfficio = null;
        String lCodUfficioMitt = null;
        if (!isRequestParameterNullObj(ICostantiUfficio.CAMPO_TIPO_UFFICIO)
                && !getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO).equals("")
                && !getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO).equals("-")) {
            lCodTipoUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO);
            lDescrComuneUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO);

            lCodUfficioMitt = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescrComuneUfficio);
            
        }
        
        // Estremi procedimento Trasmesso
        BigDecimal lChiaveAnnoSiep = null;
        BigDecimal lChiaveProgrSiep = null;
        String     lChiaveUfficioSiep = getCodUfficioUtenteConnesso();
        if (!isRequestParameterNullObj(ICostantiJMS.CHIAVE_ANNO_SIEP)
                && !getRequestStringParameter(ICostantiJMS.CHIAVE_ANNO_SIEP).equals("")) {
            lChiaveAnnoSiep = getRequestBigDecimalParameter(ICostantiJMS.CHIAVE_ANNO_SIEP);
        }

        if (!isRequestParameterNullObj(ICostantiJMS.CHIAVE_PROGR_SIEP)
                && !getRequestStringParameter(ICostantiJMS.CHIAVE_PROGR_SIEP).equals("")) {
            lChiaveProgrSiep = getRequestBigDecimalParameter(ICostantiJMS.CHIAVE_PROGR_SIEP);
        }
        /*
        // Estremi procedimento cumulante
        if (!isRequestParameterNullObj(ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE)
                && !getRequestStringParameter(ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE).equals("")) {
            lMessaggio.setChiaveAnnoFasCumulante(getRequestBigDecimalParameter(ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE));
        }

        if (!isRequestParameterNullObj(ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE)
                && !getRequestStringParameter(ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE).equals("")) {
            lMessaggio.setChiaveProgrFasCumulante(getRequestBigDecimalParameter(ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE));
        }       
        */
        
        /*
        // Estremi del soggetto
        if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COGNOME)
                && !getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).equals("")) {
            lMessaggio.setCognomeSoggetto (getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME));
        }

        if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_NOME)
                && !getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME).equals("")) {
            lMessaggio.setNomeSoggetto(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME));
        }	    
	    */
	    
        // MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni - FINE
	    
	    
	    
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
		// MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni
		// Si aggiunge filtro sul tipo trasmissione
		if (isRequestParameterNullObj(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO)) {
		    // Arrivo dalla griglia: di default scelgo tutti
		    lListaTipoOperazione.add(ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA);
	        lListaTipoOperazione.add(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI);
	        lListaTipoOperazione.add(ICostantiJMS.ESITO_SEGUITO_ATTI);
		}
		else {
		    // Arrivo dalla form di ricerca: uso quelli impostati
	        if (isRequestChecked(ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA))
	            lListaTipoOperazione.add(ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA);
	        if (isRequestChecked(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI))
	            lListaTipoOperazione.add(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI);
	        if (isRequestChecked(ICostantiJMS.ESITO_SEGUITO_ATTI))
	            lListaTipoOperazione.add(ICostantiJMS.ESITO_SEGUITO_ATTI);
	        
	        // n.b. almeno un valore deve essere impostato altrimenti 
	        //      potrebbero uscire messaggio NON pertinenti
	        //      Se non è stato selezionato nulla li metto tutti di default
	        if (lListaTipoOperazione.size()==0) {
	            lListaTipoOperazione.add(ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA);
	            lListaTipoOperazione.add(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI);
	            lListaTipoOperazione.add(ICostantiJMS.ESITO_SEGUITO_ATTI);	            
	        }   
		}
		
		for (String lTipoOp: lListaTipoOperazione) {
	          if (lTipoOp.equals(ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA))
	              setRequestAttribute("tipoEsitoTrasfCompCk", "checked");
	          else if (lTipoOp.equals(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI))
                  setRequestAttribute("tipoEsitoComuProcureCk", "checked");
	          else if (lTipoOp.equals(ICostantiJMS.ESITO_SEGUITO_ATTI))
                  setRequestAttribute("tipoEsitoSeguitoAttiCk", "checked");
		}

		IModuloCumulo lCtrl = SIEPLookupRemote.getModuloCumuloRemote();
		Vector<MessaggioModel> lVect = lCtrl.ExRicercaMessaggi(null // ICostantiJMS.DELIVERY_MODE_INVIATO
				, lListaTipoMessaggio, lListaTipoOperazione, null // lCodEsito
				, "N" // Flag_visto.
				, lChiaveAnnoSiep // MEV_2025-48 - 2.15, null // aChiaveAnnoSiep
				, lChiaveProgrSiep //MEV_2025-48 - 2.15, null // aChiaveProgrSiep
				, lChiaveUfficioSiep //MEV_2025-48 - 2.15, null // aChiaveUfficioSiep
				, lCodUfficioMitt //MEV_2025-48 - 2.15, null // aCodUfficioMitt
				, getCodUfficioUtenteConnesso() // ufficio dest
				, lDataTrasmissioneDal //MEV_2025-48 - 2.15, null // lDataTrasmissioneDal
				, lDataTrasmissioneAl //MEV_2025-48 - 2.15, null // lDataTrasmissioneAl
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
	                , lChiaveAnnoSiep //MEV_2025-48 - 2.15, null // aChiaveAnnoSiep
	                , lChiaveProgrSiep //MEV_2025-48 - 2.15, null // aChiaveProgrSiep
	                , lChiaveUfficioSiep //MEV_2025-48 - 2.15, null // aChiaveUfficioSiep
					, lCodUfficioMitt //MEV_2025-48 - 2.15 , null // aCodUfficioMitt
					, getCodUfficioUtenteConnesso() // ufficio dest
					, lDataTrasmissioneDal //MEV_2025-48 - 2.15 , null // lDataTrasmissioneDal
					, lDataTrasmissioneAl //MEV_2025-48 - 2.15, null // lDataTrasmissioneAl
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
				if ("00067".equals(lMess.getCodTipoOperazione())
				    // MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni
				    // Test su presenza solleciti prima di ricercarli
				    && lMess.getContaSolleciti()!=null 
				    && lMess.getContaSolleciti().intValue()>0
				    // MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni - FINE
				   ) 
				{
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

	    /* MEV_2025-48 – Atti pervenuti per competenza al cumulo */
        Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
        lOption.setFilter(new String[] { "-", "PM", "PGCAP", "PMM" });
        if (lCodTipoUfficio != null)
            lOption.setSelected(lCodTipoUfficio);
        else
            lOption.setSelected("-");
        setRequestAttribute("tipoUfficioRichiedente", "" + lOption);
        /* MEV_2025-48 – FINE */
        
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