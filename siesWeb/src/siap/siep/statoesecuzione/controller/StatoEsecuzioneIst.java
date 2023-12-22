package siap.siep.statoesecuzione.controller;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.Utils;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.statoesecuzione.model.EventoModel;

/**
 * Stato Esecuzione Istruttoria Estende l'elemento Stato Esecuzione Element in generale
 *
 * @author Giselda De Vita
 */
public class StatoEsecuzioneIst extends StatoEsecuzioneElement {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneIst(StatoEsecuzioneElement aStat) {
		super(aStat);
	}

	/**
	 * IMplementazione del metodo elabora dell'elemento
	 *
	 * Regola: Istanza pervenuta il 22-10-2006 (oggetto) trasmessa in data (data). trasmissione) a ufficio
	 * (Tds/Uds/GE) di (luogo)
	 *
	 * Esempio:
	 *
	 * Istanza pervenuta il 22-10-2006 (oggetto)
	 *
	 * Istanza pervenuta il 22-10-2006 (oggetto) trasmessa in data 12/12/2006 a ufficio Tribunale di
	 * Sorveglianza di Napoli
	 */
	public void elabora(siap.sico.evento.model.EventoModel aEvento) {

		try {

			EventoModel lEvento = new EventoModel(aEvento);
			lEvento.setFamiglia("IST");

			// descrizione_data
			lEvento.setDescrizioneData(mCostanti.getProperty("ISTANZA_PERVENUTA_IN_DATA"));

			// data
			lEvento.setDataEmissione(aEvento.getDataEmissione());

			/*
			 * if esiste la data trasmissione e l'ufficio a cui si trasmette.
			 * lEvento.setDescrizioneData(mCostanti.getProperty("ISTANZA_TRASMESSA_IN_DATA"));
			 */

			// [SG] Ticket#20200818015 - errori JBWEB000236 su server.log
			UfficioModel lUfficio = new UfficioModel();
			if (!Utils.isNullObj(aEvento) && Utils.isPresent(aEvento.getCodUfficioDestinatario())) {
				// Chiamata al controller.
				IUfficio lUff = SICOLookupRemote.getUfficioRemote();
				lUfficio = lUff.getUfficioByKey(aEvento.getCodUfficioDestinatario());
			}
			lEvento.setDescrLuogoEmittente(lUfficio.getDescrComune());
			lEvento.setDescrUfficioEmittente(lUfficio.getDescrTipoUfficio());

			mEventoStatoEsecuzione = lEvento;
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in StatoEsecuzioneIst", ex);
			ex.printStackTrace();
		}
	}

}