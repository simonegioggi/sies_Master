/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.action;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.util.SoggettiConProcedimentiMapper;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.DATISOGGETTOOUTPUT;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.SOGGETTOTYPE;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * @author Gioggi
 */
@SuppressWarnings("rawtypes")
public class RicercaSoggettiConProcedimentiSiusAction extends ActionSius {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Metodo per la ricerca dei soggetti con procedimenti
	 * 
	 * @param codDistretto
	 * @param codFiscaleAvvocato
	 * @param codTipoUfficio
	 * @param soggetto
	 * @return DATISOGGETTOOUTPUT
	 * @throws Exception
	 */
	public DATISOGGETTOOUTPUT ricercaSoggettiConProcedimenti(String codDistretto, String codFiscaleAvvocato,
			String codTipoUfficio, SOGGETTOTYPE soggetto) throws Exception {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: RicercaSoggettiConProcedimentiSiusAction, metodo: ricercaSoggettiConProcedimenti");

		// Instanzio ed inizializzo un oggetto di tipo "SoggettoModel"
		SoggettoModel sm = new SoggettoModel();
		// Si Riempie il model del Soggetto.
		sm.setCognome(soggetto.getCognome());
		if (PropertyUtil.isPresent(soggetto.getNome()))
			sm.setNome(soggetto.getNome());
		// Comune di nascita
		if (PropertyUtil.isPresent(soggetto.getCodComuneNascita()))
			sm.setCodComuneNascita(soggetto.getCodComuneNascita());
		if (PropertyUtil.isPresent(soggetto.getDescrComuneNascita()))
			sm.setDescrComuneNascita(soggetto.getDescrComuneNascita());
		// Data di nascita
		if (PropertyUtil.isPresent(soggetto.getDataNascita())) {
			if (PropertyUtil.isPresent(soggetto.getDataNascita().getAnno())
					&& PropertyUtil.isPresent(soggetto.getDataNascita().getMese())
					&& PropertyUtil.isPresent(soggetto.getDataNascita().getGiorno()))
				sm.setDataNascita(DateUtils.getDate(soggetto.getDataNascita().getAnno(), soggetto
						.getDataNascita().getMese(), soggetto.getDataNascita().getGiorno()));
		}
		// Stato di nascita
		if (PropertyUtil.isPresent(soggetto.getCodStatoNascita()))
			sm.setCodStatoNascita(soggetto.getCodStatoNascita());
		if (PropertyUtil.isPresent(soggetto.getDescrStatoNascita()))
			sm.setDescrStatoNascita(soggetto.getDescrStatoNascita());
		if (PropertyUtil.isPresent(soggetto.getPaternita()))
			sm.setPaternita(soggetto.getPaternita());
		if (PropertyUtil.isPresent(soggetto.getCodCs()))
			sm.setCodCs(soggetto.getCodCs());

		// Chiamata al controller
		IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
		Vector soggettiConProcedimenti = ifs.ricercaSoggettiConProcedimenti(sm, codDistretto,
				codFiscaleAvvocato, codTipoUfficio);

		// info per il log
		avvocaturaLogger.debug("Valorizzo l'oggetto di output: DATISOGGETTOOUTPUT");
		// copia dei dati dal model al type
		DATISOGGETTOOUTPUT dso = copyModelToType(soggettiConProcedimenti);

		// valore di ritorno
		return dso;
	}

	/**
	 * Metodo per la valorizzazione del type di output
	 * 
	 * @param soggettiConProcedimenti
	 * @return DATISOGGETTOOUTPUT
	 * @throws Exception
	 */
	private DATISOGGETTOOUTPUT copyModelToType(Vector soggettiConProcedimenti) throws Exception {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: RicercaSoggettiConProcedimentiSiusAction, metodo: copyModelToType");

		// instanzio ed inizializzo un oggetto di tipo "DATISOGGETTOOUTPUT"
		DATISOGGETTOOUTPUT dso = new DATISOGGETTOOUTPUT();

		try {
			// controllo preventivo
			List<SOGGETTOTYPE> lst = SoggettiConProcedimentiMapper
					.mapSoggettiConProcedimenti(soggettiConProcedimenti);
			if (lst != null && lst.size() > 0) {
				// SOLO I PRIMI 50 elementi
				if (lst.size() <= 50)
					dso.getElencoSoggetti().addAll(lst);
				else {
					for (int i = 0; i < 50; i++)
						dso.getElencoSoggetti().add(lst.get(i));
				}
				// imposto l'ERRORE
				dso.setERRORE(Mapper.mapErroreProcedimento("000", ""));
			} else
				// imposto l'ERRORE
				dso.setERRORE(Mapper.mapErroreProcedimento("038", ""));
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error(
					"Errore nella mappatura dei dati della ricerca del soggetto con procedimenti: "
							+ e.getMessage(), e);
			// imposto l'ERRORE
			dso.setERRORE(Mapper.mapErroreProcedimento("036", e.getMessage()));
		}

		// valore di ritorno
		return dso;
	}

}