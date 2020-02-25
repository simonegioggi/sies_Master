/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.action;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import it.eng.giustizia.avvocatura.controller.IAvvisiSius;
import it.eng.giustizia.avvocatura.util.ElencoAvvisiMapper;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi.AVVISO;
import it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi.DATIAVVISOINPUT;
import it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi.DATIAVVISOOUTPUT;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.avvocatura.model.AvvisiElencoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * @author caporizzo
 */
public class RicercaAvvisiSiusAction extends ActionSius {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public DATIAVVISOOUTPUT ricercaAvvisi(DATIAVVISOINPUT input) throws Exception {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: RicercaAvvisiSiusAction, metodo: ricercaAvvisi");

		// Chiamata al controller
		IAvvisiSius ias = SIUSLookupRemote.getAvvisiSiusRemote();

		// Data inizioRicerca
		Date dataInizioRicerca = null;
		Date dataFineRicerca = null;
		if (PropertyUtil.isPresent(input.getDataEmissioneInizio())) {
			if (PropertyUtil.isPresent(input.getDataEmissioneInizio().getAnno())
					&& PropertyUtil.isPresent(input.getDataEmissioneInizio().getMese())
					&& PropertyUtil.isPresent(input.getDataEmissioneInizio().getGiorno()))
				dataInizioRicerca = DateUtils.getDate(input.getDataEmissioneInizio().getAnno(), input
						.getDataEmissioneInizio().getMese(), input.getDataEmissioneInizio().getGiorno());
		}

		if (PropertyUtil.isPresent(input.getDataEmissioneFine())) {
			if (PropertyUtil.isPresent(input.getDataEmissioneFine().getAnno())
					&& PropertyUtil.isPresent(input.getDataEmissioneFine().getMese())
					&& PropertyUtil.isPresent(input.getDataEmissioneFine().getGiorno()))
				dataFineRicerca = DateUtils.getDate(input.getDataEmissioneFine().getAnno(), input
						.getDataEmissioneFine().getMese(), input.getDataEmissioneFine().getGiorno());
		}

		// ricerca degli avvisi
		Vector<AvvisiElencoModel> elencoAvvisi = ias.ricercaAvvisiSius(input.getCodiceFiscaleAvvocato(),
				input.getCodTipoUfficio(), input.getCodDistretto(), input.getCodStatoAvviso(),
				dataInizioRicerca, dataFineRicerca);

		// info per il log
		avvocaturaLogger.debug("Valorizzo l'oggetto di output: DATIAVVISOOUTPUT");
		// copia dei dati dal model al type
		DATIAVVISOOUTPUT output = copyModelToType(elencoAvvisi);

		// valore di ritorno
		return output;
	}

	/**
	 * @param elencoAvvisi
	 * @return
	 * @throws F3BException
	 */
	private DATIAVVISOOUTPUT copyModelToType(Vector<AvvisiElencoModel> elencoAvvisi) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: RicercaAvvisiSiusAction, metodo: copyModelToType");

		// instanzio ed inizializzo un oggetto di tipo "DATIAVVISOOUTPUT"
		DATIAVVISOOUTPUT dso = new DATIAVVISOOUTPUT();

		try {
			// controllo preventivo
			List<AVVISO> lst = ElencoAvvisiMapper.mapElencoAvvisi(elencoAvvisi);
			if (lst != null && lst.size() > 0) {
				// SOLO I PRIMI 50 elementi
				if (lst.size() <= 50)
					dso.getAVVISO().addAll(lst);
				else {
					for (int i = 0; i < 50; i++)
						dso.getAVVISO().add(lst.get(i));
				}
				// imposto l'ERRORE
				dso.setERRORE(Mapper.mapErroreAvviso("000", ""));
			} else
				// imposto l'ERRORE
				dso.setERRORE(Mapper.mapErroreAvviso("040", ""));
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error("Errore nella mappatura dei dati della ricerca elenco avvisi: " + e.getMessage(), e);
			// imposto l'ERRORE
			dso.setERRORE(Mapper.mapErroreAvviso("041", e.getMessage()));
		}

		// valore di ritorno
		return dso;
	}

}