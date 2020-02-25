/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.action;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.ws.type.richiestaStampa.DATISTAMPAOUTPUT;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * @author Gioggi
 */
public class RichiestaStampaSiusAction extends ActionSius {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Metodo per l'elaborazione della richiesta della stampa
	 * 
	 * @param idFascicoloSius
	 * @param codDistretto
	 * @param codiceFiscaleAvvocato
	 * @param codTipoUfficio
	 * @return DATISTAMPAOUTPUT
	 * @throws Exception
	 */
	public DATISTAMPAOUTPUT richiestaStampa(BigDecimal idFascicoloSius, String codDistretto,
			String codiceFiscaleAvvocato, String codTipoUfficio) throws Exception {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: RichiestaStampaSiusAction, metodo: richiestaStampa");

		// richiesta per codice ufficio -- ExStampaProcedimento
		IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();

		// ricerco codice ufficio appartenenza
		String codUfficio = ifs.ricercaCodUfficioAppartenenza(idFascicoloSius, codiceFiscaleAvvocato, codDistretto, codTipoUfficio);

		// info per il log
		avvocaturaLogger.debug("Eseguo la richiesta di stampa per: ID_FASCICOLO_SIUS = " + idFascicoloSius);
		avvocaturaLogger.debug("Eseguo la richiesta di stampa per: COD_DISTRETTO = " + codDistretto);
		avvocaturaLogger.debug("Eseguo la richiesta di stampa per: COD_FISCALE_AVVOCATO = " + codiceFiscaleAvvocato);
		avvocaturaLogger.debug("Eseguo la richiesta di stampa per: COD_TIPO_UFFICIO = " + codTipoUfficio);
		avvocaturaLogger.debug("Eseguo la richiesta di stampa per: COD_UFFICIO = " + codUfficio);
		// eseguo la richiesta di stampa
		ByteArrayOutputStream report = ifs.richiestaStampa(idFascicoloSius, codDistretto,
				codiceFiscaleAvvocato, codTipoUfficio, codUfficio);

		// info per il log
		avvocaturaLogger.debug("Valorizzo l'oggetto di output: DATISTAMPAOUTPUT");
		// copia dei dati dal model al type
		DATISTAMPAOUTPUT dso = copyModelToType(report, idFascicoloSius);

		// valore di ritorno
		return dso;
	}

	/**
	 * Metodo per la valorizzazione del type di output
	 * 
	 * @param report
	 * @param idFascicoloSius
	 * @return DATISTAMPAOUTPUT
	 * @throws Exception
	 */
	private DATISTAMPAOUTPUT copyModelToType(ByteArrayOutputStream report, BigDecimal idFascicoloSius)
			throws Exception {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: RichiestaStampaSiusAction, metodo: copyModelToType");

		// instanzio ed inizializzo un oggetto di tipo "DATISTAMPAOUTPUT"
		DATISTAMPAOUTPUT dso = new DATISTAMPAOUTPUT();

		try {
			// recupero dati richiesta stampa
			dso.setALLEGATORTF(report.toByteArray());
			dso.setIdFascicoloSius(idFascicoloSius.toBigInteger());

			// imposto l'ERRORE
			dso.setERRORE(Mapper.mapErroreRichiestaStampa("000", ""));
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error("Errore nella mappatura dei dati della richiesta stampa: " + e.getMessage(), e);
			// imposto l'ERRORE
			dso.setERRORE(Mapper.mapErroreRichiestaStampa("017", e.getMessage()));
		}

		// valore di ritorno
		return dso;
	}

}