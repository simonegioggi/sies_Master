package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.presaincarico.action.ICostantiPresaincarico;

/**
 * <p>
 * Title: ActLoadRicercaAttiCompetenzaRicevuti
 * </p>
 * <p>
 * Description: Classe Action utilizzata UNICAMENTE per invocare la ricerca diretatmente da menù orizzontale.
 * Si ricerca SOLO gli atti ricevuti per competenza per assorbimento in cumulo 00066 non ancora presi in
 * carico
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadRicercaAttiCompetenzaRicevuti extends ActionSiap implements ICostantiRichiesta {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null && JMSProperties
				.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		boolean chiamanteMenu = false;
		if (!isRequestParameterNullObj("chiamante")) {
			String chiamante = getRequestStringParameter("chiamante");
			siesLogger.debug("chiamante = " + chiamante);
			chiamanteMenu = true;
		} else {
			siesLogger.debug("chiamante = Assente");
		}

		this.setLinkRitorno();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		MessaggioModel lMessaggio = new MessaggioModel();
		lMessaggio.setCodUfficioDestinatario(getCodUfficioUtenteConnesso());
		lMessaggio.setCodTipoMessaggio(ICostantiJMS.RICHIESTA);
		// lMessaggio.setCodTipoOperazione(ICostantiJMS.TRASFERIMENTO_COMPETENZA);
		lMessaggio.setFlagVisto("N"); // non ancora presi in carico
		lMessaggio.setCodUfficioMittente("");

		Vector<String> lListaTipoOperazione = new Vector<>();
		lListaTipoOperazione.add(ICostantiJMS.TRASFERIMENTO_COMPETENZA);
		lListaTipoOperazione.add(ICostantiJMS.SEGUITO_ATTI_TRASFERIMENTO_COMPETENZA);
		lListaTipoOperazione.add(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI);

		// MEV26 - CUMULO. Se provengo dall'istruttoria filtro direttamente i
		// fascicolo trasmessi destinati al fascicolo Cumulante		
		// if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)) {
		if (!isRequestParameterNullEmptyObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)) {
			BigDecimal lIdIstruttoria = getRequestBigDecimalParameter(
					ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

			if (lIdIstruttoria == null) {
				RedirectTo lRedirigi = new RedirectTo();

				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessuna istruttoria selezionata");
				lRedirigi.setAction("siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				throw new F3BException(F3BException.USER_MESSAGE, "Nessuna istruttoria selezionata");
			}

			IIstruttoriaCumulo lIstrCumCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			IstruttoriaCumuloModel lIstruttoriaCumuloModel = lIstrCumCtrl
					.ExRicercaIstruttoriaCumuloById(lIdIstruttoria);

			setRequestAttribute("IstruttoriaCumulo", lIstruttoriaCumuloModel);

			IFascicoloSiep lFascicoloCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFascModel = lFascicoloCtrl
					.ExRicercaFascicoloByKey(lIstruttoriaCumuloModel.getFasSieIdFascicoloSiep());

			lMessaggio.setChiaveAnnoFasCumulante(lFascModel.getChiaveAnno());
			lMessaggio.setChiaveProgrFasCumulante(lFascModel.getChiaveProgr());
			
			setRequestAttribute("ChiaveAnnoCumulante", lFascModel.getChiaveAnno().toString());
			setRequestAttribute("ChiaveProgrCumulante", lFascModel.getChiaveProgr().toString());

		}

		// Filtro sulla data di trasmissione
		Date lDataInizio = null;
		Date lDataFine = null;
		/* MEV_2025-48 – Atti pervenuti per competenza al cumulo */
		/*
		if (!isRequestParameterNullObj(ICostantiPresaincarico.CAMPO_ANNO_DATA_RICEZIONE_ATTI)) {
			lDataInizio = getRequestDateParameter(ICostantiPresaincarico.CAMPO_ANNO_DATA_RICEZIONE_ATTI,
					ICostantiPresaincarico.CAMPO_MESE_DATA_RICEZIONE_ATTI,
					ICostantiPresaincarico.CAMPO_GIORNO_DATA_RICEZIONE_ATTI);
			lDataFine = getRequestDateParameter(ICostantiPresaincarico.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					ICostantiPresaincarico.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
					ICostantiPresaincarico.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);
		}*/
		
		if (!isRequestParameterNullObj(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO)) {
			lDataInizio = getRequestDateParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO,
					ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO,
					ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO);
			lDataFine = getRequestDateParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE,
					ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE,
					ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE);			
		}
		else {
			if (lDataInizio == null)
				lDataInizio = DateUtils.getEnneMonthBefore(DateUtils.getSysDate(), 2);

			// Se la data fine non viene valorizzata si imposta con quella odierna.
			if (lDataFine == null)
				lDataFine = DateUtils.getSysDate();
			
		}
		
		setRequestAttribute(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO, DateUtils.getDateToString(lDataInizio, "dd"));
		setRequestAttribute(ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO, DateUtils.getDateToString(lDataInizio, "MM"));
		setRequestAttribute(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO, DateUtils.getDateToString(lDataInizio, "yyyy"));
		
		setRequestAttribute(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE, DateUtils.getDateToString(lDataFine, "dd"));
		setRequestAttribute(ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE, DateUtils.getDateToString(lDataFine, "MM"));
		setRequestAttribute(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE, DateUtils.getDateToString(lDataFine, "yyyy"));
		
		// Filtro per - Ufficio Mittente
		String lCodTipoUfficio = null;
		String lDescrComuneUfficio = null;
		String lCodUfficioMitt = null;
		if (!isRequestParameterNullObj(ICostantiUfficio.CAMPO_TIPO_UFFICIO)
				&& !getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO).equals("")
				&& !getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO).equals("-")) {
			lCodTipoUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO);
			lDescrComuneUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO);

			lCodUfficioMitt = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescrComuneUfficio);
			lMessaggio.setCodUfficioMittente(lCodUfficioMitt);
		}
		
		
		// Estremi procedimento Trasmesso
		if (!isRequestParameterNullObj(ICostantiJMS.CHIAVE_ANNO_SIEP)
				&& !getRequestStringParameter(ICostantiJMS.CHIAVE_ANNO_SIEP).equals("")) {
			lMessaggio.setChiaveAnnoSiep (getRequestBigDecimalParameter(ICostantiJMS.CHIAVE_ANNO_SIEP));
		}

		if (!isRequestParameterNullObj(ICostantiJMS.CHIAVE_PROGR_SIEP)
				&& !getRequestStringParameter(ICostantiJMS.CHIAVE_PROGR_SIEP).equals("")) {
			lMessaggio.setChiaveProgrSiep(getRequestBigDecimalParameter(ICostantiJMS.CHIAVE_PROGR_SIEP));
		}
		
		// Estremi procedimento cumulante
		if (!isRequestParameterNullObj(ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE)
				&& !getRequestStringParameter(ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE).equals("")) {
			lMessaggio.setChiaveAnnoFasCumulante(getRequestBigDecimalParameter(ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE));
		}

		if (!isRequestParameterNullObj(ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE)
				&& !getRequestStringParameter(ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE).equals("")) {
			lMessaggio.setChiaveProgrFasCumulante(getRequestBigDecimalParameter(ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE));
		}		

		// Estremi del soggetto
		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COGNOME)
				&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).equals("")) {
			lMessaggio.setCognomeSoggetto (getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME));
		}

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_NOME)
				&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME).equals("")) {
			lMessaggio.setNomeSoggetto(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME));
		}		
		
		siesLogger.debug("lMessaggio = "+lMessaggio.toString());
		/* MEV_2025-48 – FINE */

		siesLogger.debug("Data Inizio = "+lDataInizio);
		siesLogger.debug("Data Fine   = "+lDataFine);
/*		
		// Se la Data inizio non viene valorizzata si imposta a 2 mesi mesi precedenti.
		if (lDataInizio == null)
			lDataInizio = DateUtils.getEnneMonthBefore(DateUtils.getSysDate(), 2);

		// Se la data fine non viene valorizzata si imposta con quella odierna.
		if (lDataFine == null)
			lDataFine = DateUtils.getSysDate();
*/
		// Ricerca Messaggi
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();

		BigDecimal lCountRisultati = null;
		if (isRequestParameterNullObj("CountRisultati")) {
			lCountRisultati = lCrtl.ExCountMessaggiRicevutiPaged(lMessaggio, lListaTipoOperazione,
					lDataInizio, lDataFine, 0);
		} else {
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		// Vector lVect = lCrtl.ExRicercaMessaggio(lMessaggio);
		Vector lVect = lCrtl.ExRicercaMessaggiRicevutiPaged(lMessaggio, lListaTipoOperazione, lDataInizio,
				lDataFine, Integer.parseInt(lPagina));

		if (lDataInizio != null && lDataFine != null) {
			// lVect = filtroPerData(lVect, lDataInizio, lDataFine);
			setRequestAttribute("data1", DateUtils.getDateToString(lDataInizio, "dd-MM-yyyy"));
			setRequestAttribute("data2", DateUtils.getDateToString(lDataFine, "dd-MM-yyyy"));
		}

		setRequestAttribute("Messaggi", lVect);

		setRequestAttribute("CountRisultati", lCountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		/* MEV_2025-48 – Atti pervenuti per competenza al cumulo */
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		lOption.setFilter(new String[] { "-", "PM", "PGCAP", "PMM" });
		if (lCodTipoUfficio != null)
			lOption.setSelected(lCodTipoUfficio);
		else
			lOption.setSelected("-");
		setRequestAttribute("tipoUfficioRichiedente", "" + lOption);
		/* MEV_2025-48 – FINE */
		
		if (chiamanteMenu) {
			if (lVect != null)
				setRequestAttribute("contaAttiRicevuti", lVect.size() + "");
			else
				setRequestAttribute("contaAttiRicevuti", "0");

			return ICostantiMessaggio.PG_CONTA_MESSAGGI_RICEVUTI_TRASMISSIONE_COMPETENZA;
		}

		return ICostantiMessaggio.PG_LISTA_MESSAGGI_RICEVUTI_TRASMISSIONE_COMPETENZA;
	}

	/**
	 * Esclude i messaggi che hanno datatInvio non ricadente nell'intervallo di date indicato
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
	// Iterator itx = aVect.iterator();
	// while (itx.hasNext()) {
	// lMessCorrente = (MessaggioModel) itx.next();
	// lDataCorr = lMessCorrente.getDataInvio();
	// if (!aDataIniziale.after(lDataCorr) && !aDataFinale.before(lDataCorr))
	// lRectVect.add(lMessCorrente);
	// }
	// return lRectVect;
	// }

}