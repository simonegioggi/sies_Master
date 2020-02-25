/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.action;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.ProcedimentiDelSoggettoMapper;
import it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti.DATIPROCEDIMENTOTYPE;
import it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti.ELENCOPROCEDIMENTIOUTPUT;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * @author Gioggi
 */
@SuppressWarnings("rawtypes")
public class ElencoProcedimentiDelSoggettoSiusAction extends ActionSius {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Metodo per la valorizzazione del type di output
	 * 
	 * @param procedimentiDelSoggetto
	 * @return DATIPROCEDIMENTOOUTPUT
	 * @throws Exception
	 */
	private ELENCOPROCEDIMENTIOUTPUT copyModelToType(Vector procedimentiDelSoggetto) throws Exception {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: ElencoProcedimentiDelSoggettoSiusAction, metodo: copyModelToType");

		// instanzio ed inizializzo un oggetto di tipo "DATISOGGETTOOUTPUT"
		ELENCOPROCEDIMENTIOUTPUT dpo = new ELENCOPROCEDIMENTIOUTPUT();

		try {
			// controllo preventivo
			List<DATIPROCEDIMENTOTYPE> lfs = ProcedimentiDelSoggettoMapper
					.mapProcedimentiDelSoggetto(procedimentiDelSoggetto);
			if (lfs != null) {
				// SOLO I PRIMI 50 elementi
				if (lfs.size() <= 50)
					dpo.getElencoProcedimenti().addAll(lfs);
				else {
					for (int i = 0; i < 50; i++)
						dpo.getElencoProcedimenti().add(lfs.get(i));
				}
			}

			// imposto l'ERRORE
			dpo.setERRORE(Mapper.mapErroreElencoProcedimenti("000", ""));
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error(
					"Errore nella mappatura dei dati della ricerca dei procedimenti del soggetto: "
							+ e.getMessage(), e);
			// imposto l'ERRORE
			dpo.setERRORE(Mapper.mapErroreElencoProcedimenti("037", e.getMessage()));
		}

		// valore di ritorno
		return dpo;
	}

	/**
	 * Metodo per la ricerca dei procedimenti del soggetto
	 * 
	 * @param codDistretto
	 * @param codFiscaleAvvocato
	 * @param codTipoUfficio
	 * @param idSoggetto
	 * @return ELENCOPROCEDIMENTIOUTPUT
	 * @throws Exception
	 */
	public ELENCOPROCEDIMENTIOUTPUT elencoProcedimentiDelSoggetto(String codDistretto,
			String codFiscaleAvvocato, String codTipoUfficio, BigInteger idSoggetto) throws Exception {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: ElencoProcedimentiDelSoggettoSiusAction, metodo: elencoProcedimentiDelSoggetto");

		// chiamo il FascicoloSiusController.
		IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
		Vector procedimentiDelSoggetto = ifs.elencoProcedimentiDelSoggetto(new BigDecimal(idSoggetto), codDistretto,
				codFiscaleAvvocato, codTipoUfficio);

		// info per il log
		avvocaturaLogger.debug("Valorizzo l'oggetto di output: DATIPROCEDIMENTOOUTPUT");
		// copia dei dati dal model al type
		ELENCOPROCEDIMENTIOUTPUT epo = copyModelToType(procedimentiDelSoggetto);

		// valore di ritorno
		return epo;
	}

}