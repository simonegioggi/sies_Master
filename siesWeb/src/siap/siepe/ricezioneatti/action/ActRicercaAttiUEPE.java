package siap.siepe.ricezioneatti.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siepe.ricezioneatti.model.RicercaMessaggioModel;

/**
 * <p>
 * Title: ActRicercaAttiPerUEPE
 * </p>
 * <p>
 * Description: Azione che si occupa di eseguire l'interrogazione dell'elenco degli atti in ricezione e
 * presetarne il risultato utilizzando l'opportuna jsp.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull Italia Gruppo Eunics SpA
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActRicercaAttiUEPE extends ActionSiap
		implements ICostantiRicezioneAtti, ICostantiMessaggio, ICostantiSoggetto {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// Controlla i messsaggi in ricezione
		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null && JMSProperties
				.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		// Model per visualizzazione criteri di ricerca.
		RicercaMessaggioModel lRicModel = new RicercaMessaggioModel();

		// Imposta il codice dell'ufficio destinatario, il quale corrisponde a quello dell'utente connesso.
		lRicModel.setCodUfficioDestinatario(getCodUfficioUtenteConnesso());

		// Questa classe controlla i MESSAGGI ricevuti in base ai parametri impostati (per Numero SIEPE / Tipo
		// Atto).
		// Si Valorizza <> "-" se l'ufficio emittente è stato correttamente impostato.
		String lCodUffMittente = "-", lTipoOperazione = "-";

		BigDecimal lAnno = null, lProgr = null;

		String lCodTipoAtto = getRequestStringParameter(CAMPO_COD_TIPO_ATTO);

		// Verifica valorizzazione dei campi, e ricerca codice ufficio per la decrizione
		// del comune. I valori, vengono contestualmente impostati nel model RicercaMessaggioModel.
		if (!(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO).equalsIgnoreCase("-"))
				&& !(getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equalsIgnoreCase(""))) {
			lCodUffMittente = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO),
					getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO));
			lRicModel.setCodUfficioMittente(lCodUffMittente);
			lRicModel.setDescrUfficioMittente(getUfficioByCodUfficio(lCodUffMittente).getDescrTipoUfficio()
					+ " - " + getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).toUpperCase());
		}

		// Valorizza i criteri di ricerca per anno e prog siepe
		if (getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_SIEPE) != null) {
			lTipoOperazione = "SIEPE";
			lAnno = getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_SIEPE);
			lProgr = getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_SIEPE);
			lRicModel.setChiaveAnnoSiepe(lAnno);
			lRicModel.setChiaveProgrSiepe(lProgr);
			lRicModel.setCodTipoOperazione(lTipoOperazione);
		} else if (lCodTipoAtto.compareTo("-") != 0) {
			// Recupero HIGH_VALUE per associazione TIPO_ATTO a TIPO_OPERAZIONE
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			lModel.setContesto("TIPO_ATTO");
			Collection lColTipoAtto = lDecodifiche.ExRicercaDecodifiche(lModel);

			if (DecodificheUtils.getCodAltebyCode(lColTipoAtto, lCodTipoAtto) != null) {
				lTipoOperazione = DecodificheUtils.getCodAltebyCode(lColTipoAtto, lCodTipoAtto);
				lRicModel.setCodTipoOperazione(lTipoOperazione);
				lRicModel.setDescrTipoOperazione(DecodificheUtils.getDescbyCode(lColTipoAtto, lCodTipoAtto));
			} else {
				lTipoOperazione = "99999";
				lRicModel.setCodTipoOperazione(lTipoOperazione);// Valore max impostabile.
			}

		} // end if

		// Recupera dalla request il falg per lo stato di ricezione, lo stesso viene
		// opportunamente impostato nel model RicercaMessaggioModel.
		String lFlagVisto = getRequestStringParameter(CAMPO_STATO_RICEZIONE);
		lRicModel.setFlagVisto(lFlagVisto);

		// Esegue Lookup del Contoller responsabile dell'interrogazione dei messaggi.
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();

		/*
		 * Vector lVect = lCrtl.ExRicercaMessaggioRichiestaPerTipoOperazioneFascicolo(
		 * getCodUfficioUtenteConnesso(), lCodUffMittente, lTipoOperazione, lAnno, lProgr,null,lFlagVisto);
		 */

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("#### Contenuto MessaggioModel : " + lRicModel);

		Vector lVect = lCrtl.ExRicercaMessaggioRichiestaPerTipoOperazioneFascicolo(lRicModel, null);

		// Imposta il link di ritono
		this.setLinkRitorno();

		// Imposta il model RicercaMessaggioModel, per riportare i criteri
		// di ricerca precedentemente impostati e per i quali si è eseguita
		// la ricerca richiesta.
		lRicModel.setCodUfficioMittente(lCodUffMittente);
		lRicModel.setCodTipoOperazione(lTipoOperazione);

		// Imposta in Request gli opportuni dati.
		setRequestAttribute("Messaggi", lVect);
		setRequestAttribute("FiltroRicerca", lRicModel);

		return PG_LISTA_ATTI_RICEVUTI; // Ritorna il path della JSP .
	}
}