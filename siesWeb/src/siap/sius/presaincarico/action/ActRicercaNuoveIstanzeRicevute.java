package siap.sius.presaincarico.action;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRicercaNuoveIstanzeRicevute extends ActionSiap implements ICostantiPresaincarico {

	public String processRequest() throws Exception {

		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null && JMSProperties
				.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		this.setLinkRitorno();

		MessaggioModel lMessaggio = new MessaggioModel();
		lMessaggio.setCodUfficioDestinatario(getCodUfficioUtenteConnesso());
		lMessaggio.setCodTipoMessaggio("01");
		lMessaggio.setCodTipoOperazione(getRequestStringParameter(ICostantiMessaggio.CAMPO_TIPO_OPERAZIONE));
		if (!isRequestChecked(CAMPO_INCLUDE_INCARICO))
			lMessaggio.setFlagVisto("N");
		else
			setRequestAttribute("flagIncludeInCarico", "S");

		// Filtro sull'ufficio emittente
		if (getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO).length() > 0
				&& getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).length() > 0) {
			String lCodUffEmittente = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO),
					getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO));
			lMessaggio.setCodUfficioMittente(lCodUffEmittente);
			setRequestAttribute("codUfficio", lCodUffEmittente);
			setRequestAttribute("descrUfficio", getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO) + " "
					+ getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)); // STUB 15/03/2005
		}

		// Filtro sul Fascicolo SIEP
		if (getRequestStringParameter(CAMPO_ANNO_FASCICOLO_SIEP).trim().length() > 0
				&& getRequestStringParameter(CAMPO_PROGR_FASCICOLO_SIEP).trim().length() > 0) {
			lMessaggio.setChiaveAnnoSiep(getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIEP));
			lMessaggio.setChiaveProgrSiep(getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIEP));
			setRequestAttribute("annoSiep", getRequestStringParameter(CAMPO_ANNO_FASCICOLO_SIEP));
			setRequestAttribute("progrSiep", getRequestStringParameter(CAMPO_PROGR_FASCICOLO_SIEP));
		}

		// Filtro sul Fascicolo SIUS
		if (getRequestStringParameter(CAMPO_ANNO_FASCICOLO_SIUS).trim().length() > 0
				&& getRequestStringParameter(CAMPO_PROGR_FASCICOLO_SIUS).trim().length() > 0) {
			lMessaggio.setChiaveAnnoSius(getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIUS));
			lMessaggio.setChiaveProgrSius(getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIUS));
			setRequestAttribute("annoSius", getRequestStringParameter(CAMPO_ANNO_FASCICOLO_SIUS));
			setRequestAttribute("progrSius", getRequestStringParameter(CAMPO_PROGR_FASCICOLO_SIUS));
		}

		// Ricerca Messaggi
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		Vector lVect = lCrtl.ExRicercaMessaggio(lMessaggio);

		// Filtro sulla data di trasmissione
		Date lDataInizio = getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE_ATTI,
				CAMPO_MESE_DATA_RICEZIONE_ATTI, CAMPO_GIORNO_DATA_RICEZIONE_ATTI);
		Date lDataFine = getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				CAMPO_MESE_DATA_TRASMISSIONE_ATTI, CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);

		// 05/03/2008 Se la Data inizio non viene valorizzata si imposta al mese precedente.
		if (lDataInizio == null)
			lDataInizio = DateUtils.getMonthBefore(DateUtils.getSysDate());
		// 05/03/2008 Se la data fine non viene valorizzata si imposta con quella odierna.
		if (lDataFine == null)
			lDataFine = DateUtils.getSysDate();

		if (lDataInizio != null && lDataFine != null) {
			lVect = filtroPerData(lVect, lDataInizio, lDataFine);
			setRequestAttribute("data1", DateUtils.getDateToString(lDataInizio, "dd-MM-yyyy"));
			setRequestAttribute("data2", DateUtils.getDateToString(lDataFine, "dd-MM-yyyy"));
		}

		setRequestAttribute("Messaggi", lVect);

		// return ICostantiMessaggio.PG_LISTA_MESSAGGI_RICEVUTI_SIUS;
		return IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiNuoveIstanzeRicevuti.jsp";
	}

	private Vector filtroPerData(Vector aVect, Date aDataIniziale, Date aDataFinale) {
		Vector lRectVect = new Vector();
		MessaggioModel lMessCorrente;
		Date lDataCorr = null;

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			lMessCorrente = (MessaggioModel) itx.next();
			lDataCorr = lMessCorrente.getDataInvio();
			if (!aDataIniziale.after(lDataCorr) && !aDataFinale.before(lDataCorr))
				lRectVect.add(lMessCorrente);
		}
		return lRectVect;
	}

}